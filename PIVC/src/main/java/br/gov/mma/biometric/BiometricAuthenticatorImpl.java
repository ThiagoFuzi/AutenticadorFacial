package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.AuthenticationResult;
import br.gov.mma.biometric.model.BiometricData;
import br.gov.mma.biometric.model.BiometricType;
import br.gov.mma.biometric.model.User;

import java.util.Optional;

/**
 * Implementação do autenticador biométrico.
 * Responsável por autenticar usuários, cadastrar novos usuários e revogar acessos.
 * 
 * Valida: Requisitos 1.1, 1.2, 1.3, 1.4, 1.5, 2.1, 3.1, 3.2, 3.3, 3.4, 4.1-4.7, 
 *         5.1-5.7, 6.1-6.5, 7.1-7.4, 8.1-8.5, 10.1-10.5
 */
public class BiometricAuthenticatorImpl implements BiometricAuthenticator {
    
    // Threshold de qualidade mínima para autenticação (Requisito 1.2)
    private static final double MINIMUM_QUALITY_THRESHOLD = 0.7;
    
    // Threshold de qualidade mínima para enrollment (Requisito 4.1)
    private static final double ENROLLMENT_QUALITY_THRESHOLD = 0.8;
    
    // Dependências injetadas
    private final UserDatabase userDatabase;
    private final SessionManager sessionManager;
    private final AuditLog auditLog;
    private final BiometricMatcherFactory matcherFactory;
    private final CryptoService cryptoService;
    
    /**
     * Construtor que inicializa o autenticador com suas dependências.
     * 
     * @param userDatabase banco de dados de usuários
     * @param sessionManager gerenciador de sessões
     * @param auditLog log de auditoria
     * @param cryptoService serviço de criptografia
     * @throws IllegalArgumentException se alguma dependência for nula
     */
    public BiometricAuthenticatorImpl(UserDatabase userDatabase, 
                                     SessionManager sessionManager,
                                     AuditLog auditLog,
                                     CryptoService cryptoService) {
        if (userDatabase == null) {
            throw new IllegalArgumentException("UserDatabase não pode ser nulo");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("SessionManager não pode ser nulo");
        }
        if (auditLog == null) {
            throw new IllegalArgumentException("AuditLog não pode ser nulo");
        }
        if (cryptoService == null) {
            throw new IllegalArgumentException("CryptoService não pode ser nulo");
        }
        
        this.userDatabase = userDatabase;
        this.sessionManager = sessionManager;
        this.auditLog = auditLog;
        this.cryptoService = cryptoService;
        this.matcherFactory = new BiometricMatcherFactory();
    }
    
    /**
     * Autentica um usuário usando dados biométricos e verifica permissão para o nível solicitado.
     * 
     * Algoritmo:
     * 1. Valida qualidade da captura biométrica (>= 0.7)
     * 2. Busca usuário por template biométrico
     * 3. Verifica se usuário está ativo
     * 4. Realiza verificação biométrica com template armazenado
     * 5. Verifica autorização de nível de acesso
     * 6. Gera token de sessão em caso de sucesso
     * 7. Registra tentativa no log de auditoria
     * 
     * @param biometricData dados biométricos capturados do usuário
     * @param requestedLevel nível de acesso solicitado
     * @return resultado da autenticação contendo sucesso, usuário, nível concedido e token de sessão
     * 
     * Valida: Requisitos 1.1, 1.2, 1.3, 1.4, 1.5, 2.1, 3.1, 3.2, 6.1, 6.2, 6.3, 7.1, 7.2, 7.3, 
     *         8.1, 8.2, 8.3, 8.4, 8.5
     */
    @Override
    public AuthenticationResult authenticate(BiometricData biometricData, AccessLevel requestedLevel) {
        // Validação de entrada
        if (biometricData == null) {
            auditLog.logException("BiometricData é nulo", new IllegalArgumentException("BiometricData null"));
            return AuthenticationResult.failure("Erro interno no sistema de autenticação");
        }
        
        if (requestedLevel == null) {
            auditLog.logException("AccessLevel é nulo", new IllegalArgumentException("AccessLevel null"));
            return AuthenticationResult.failure("Erro interno no sistema de autenticação");
        }
        
        try {
            // Step 1: Validar qualidade da captura biométrica (Requisito 1.2)
            if (biometricData.getQuality() < MINIMUM_QUALITY_THRESHOLD) {
                auditLog.logFailedAttempt("Qualidade biométrica insuficiente", biometricData);
                return AuthenticationResult.failure("Qualidade da captura biométrica insuficiente");
            }
            
            // Step 2: Buscar usuário por template biométrico
            Optional<User> userOpt = userDatabase.findUserByBiometric(
                biometricData.getTemplate(),
                biometricData.getType()
            );
            
            if (!userOpt.isPresent()) {
                auditLog.logFailedAttempt("Usuário não encontrado", biometricData);
                return AuthenticationResult.failure("Biometria não reconhecida");
            }
            
            User user = userOpt.get();
            
            // Step 3: Verificar se usuário está ativo (Requisito 3.1)
            if (!user.isActive()) {
                auditLog.logFailedAttempt("Usuário inativo: " + user.getUserId(), biometricData);
                return AuthenticationResult.failure("Acesso negado: usuário inativo");
            }
            
            // Step 4: Verificar correspondência biométrica com alta precisão (Requisito 1.3)
            boolean biometricMatch = verifyBiometric(
                biometricData.getTemplate(),
                user.getStoredBiometricTemplate(),
                biometricData.getType()
            );
            
            if (!biometricMatch) {
                auditLog.logFailedAttempt("Falha na verificação biométrica", biometricData);
                return AuthenticationResult.failure("Falha na verificação biométrica");
            }
            
            // Step 5: Verificar nível de acesso (Requisito 2.1)
            boolean hasAccess = checkAccessLevel(user, requestedLevel);
            
            if (!hasAccess) {
                auditLog.logFailedAttempt(
                    "Nível de acesso insuficiente: " + user.getUserId() + 
                    " tentou acessar " + requestedLevel,
                    biometricData
                );
                return AuthenticationResult.failure(
                    "Acesso negado: nível de permissão insuficiente"
                );
            }
            
            // Step 6: Gerar token de sessão (Requisito 1.4, 7.1)
            String sessionToken = sessionManager.createSession(user, requestedLevel);
            
            // Step 7: Registrar sucesso (Requisito 6.2)
            auditLog.logSuccessfulAuthentication(user, requestedLevel);
            
            // Retornar resultado de sucesso (Requisito 1.4)
            return AuthenticationResult.success(user, requestedLevel, sessionToken);
            
        } catch (Exception e) {
            // Registrar exceção e retornar erro genérico (Requisito 8.6)
            auditLog.logException("Erro durante autenticação", e);
            return AuthenticationResult.failure("Erro interno no sistema de autenticação");
        }
    }
    
    /**
     * Cadastra um novo usuário no sistema com seus dados biométricos.
     * 
     * Algoritmo:
     * 1. Valida qualidade biométrica (>= 0.8)
     * 2. Verifica se userId já existe
     * 3. Verifica se template biométrico já existe
     * 4. Criptografa template biométrico
     * 5. Cria usuário com template criptografado
     * 6. Salva usuário no banco de dados atomicamente
     * 7. Registra enrollment no log de auditoria
     * 
     * @param user dados do usuário a ser cadastrado
     * @param biometricData dados biométricos do usuário
     * @return true se o cadastro foi bem-sucedido, false caso contrário
     * 
     * Valida: Requisitos 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 10.1, 10.2, 10.3, 10.4
     */
    @Override
    public boolean enrollUser(User user, BiometricData biometricData) {
        // Validação de entrada
        if (user == null || biometricData == null) {
            auditLog.logEnrollmentFailure("Dados de entrada inválidos");
            return false;
        }
        
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            auditLog.logEnrollmentFailure("UserId inválido");
            return false;
        }
        
        try {
            // Step 1: Verificar qualidade biométrica mínima para enrollment (Requisito 4.1, 4.2)
            if (biometricData.getQuality() < ENROLLMENT_QUALITY_THRESHOLD) {
                auditLog.logEnrollmentFailure(
                    "Qualidade biométrica insuficiente para enrollment: " + 
                    biometricData.getQuality()
                );
                return false;
            }
            
            // Step 2: Verificar se usuário já existe (Requisito 4.3, 10.1)
            Optional<User> existingUser = userDatabase.findUserById(user.getUserId());
            if (existingUser.isPresent()) {
                auditLog.logEnrollmentFailure("Usuário já existe: " + user.getUserId());
                return false;
            }
            
            // Step 3: Verificar se biometria já está cadastrada (Requisito 4.4, 10.2)
            Optional<User> existingBiometric = userDatabase.findUserByBiometric(
                biometricData.getTemplate(),
                biometricData.getType()
            );
            if (existingBiometric.isPresent()) {
                auditLog.logEnrollmentFailure("Biometria já cadastrada");
                return false;
            }
            
            // Step 4: Criptografar template biométrico (Requisito 4.5, 10.4)
            byte[] encryptedTemplate = cryptoService.encrypt(biometricData.getTemplate());
            
            // Step 5: Criar usuário com template criptografado
            User userWithBiometric = new User(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getMaxAccessLevel(),
                encryptedTemplate,
                biometricData.getType(),
                true
            );
            
            // Step 6: Salvar no banco de dados (operação atômica - Requisito 4.7, 10.3)
            boolean saved = userDatabase.saveUser(userWithBiometric);
            
            if (saved) {
                // Step 7: Registrar enrollment bem-sucedido (Requisito 4.6, 6.4)
                auditLog.logSuccessfulEnrollment(user.getUserId(), user.getMaxAccessLevel());
                return true;
            } else {
                auditLog.logEnrollmentFailure("Falha ao salvar no banco de dados");
                return false;
            }
            
        } catch (CryptoException e) {
            auditLog.logException("Erro ao criptografar template biométrico", e);
            return false;
        } catch (Exception e) {
            auditLog.logException("Erro durante enrollment", e);
            return false;
        }
    }
    
    /**
     * Revoga o acesso de um usuário, marcando-o como inativo.
     * 
     * @param userId identificador do usuário
     * @return true se a revogação foi bem-sucedida, false caso contrário
     * 
     * Valida: Requisitos 3.3, 3.4
     */
    @Override
    public boolean revokeAccess(String userId) {
        // Validação de entrada
        if (userId == null || userId.isEmpty()) {
            return false;
        }
        
        try {
            // Step 1: Buscar usuário
            Optional<User> userOpt = userDatabase.findUserById(userId);
            
            if (!userOpt.isPresent()) {
                return false;
            }
            
            User user = userOpt.get();
            
            // Step 2: Criar novo usuário marcado como inativo (Requisito 3.3)
            User inactiveUser = new User(
                user.getUserId(),
                user.getName(),
                user.getRole(),
                user.getMaxAccessLevel(),
                user.getStoredBiometricTemplate(),
                user.getBiometricType(),
                false // Marcar como inativo
            );
            
            // Step 3: Atualizar usuário no banco de dados
            boolean updated = userDatabase.updateUser(inactiveUser);
            
            if (updated) {
                // Step 4: Registrar revogação no log de auditoria
                auditLog.logAccessCheck(userId, null, false);
            }
            
            return updated;
            
        } catch (Exception e) {
            auditLog.logException("Erro ao revogar acesso", e);
            return false;
        }
    }
    
    /**
     * Verifica correspondência biométrica entre template capturado e armazenado.
     * 
     * Algoritmo:
     * 1. Descriptografa template armazenado
     * 2. Valida tamanho dos templates
     * 3. Seleciona matcher apropriado para o tipo biométrico
     * 4. Calcula score de similaridade
     * 5. Aplica threshold baseado no tipo biométrico
     * 6. Registra métrica de verificação
     * 
     * @param capturedTemplate template capturado durante autenticação
     * @param storedTemplate template armazenado (criptografado)
     * @param type tipo biométrico
     * @return true se os templates correspondem, false caso contrário
     * 
     * Valida: Requisitos 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 10.5
     */
    private boolean verifyBiometric(byte[] capturedTemplate, byte[] storedTemplate, BiometricType type) {
        try {
            // Step 1: Descriptografar template armazenado
            byte[] decryptedTemplate = cryptoService.decrypt(storedTemplate);
            
            // Step 2: Validar tamanho dos templates (Requisito 10.5)
            if (capturedTemplate.length != decryptedTemplate.length) {
                return false;
            }
            
            // Step 3: Obter matcher apropriado para o tipo biométrico
            BiometricMatcher matcher = matcherFactory.getMatcher(type);
            
            // Step 4: Calcular score de similaridade (Requisito 5.1)
            double similarityScore = matcher.calculateSimilarity(capturedTemplate, decryptedTemplate);
            
            // Step 5: Aplicar threshold baseado no tipo biométrico (Requisitos 5.2, 5.3, 5.4)
            double threshold = getThresholdForType(type);
            
            // Step 6: Decisão de verificação (Requisitos 5.5, 5.6)
            boolean isMatch = similarityScore >= threshold;
            
            // Nota: Registro de métricas seria feito aqui (Requisito 5.7)
            // mas não temos interface MetricsCollector implementada ainda
            
            return isMatch;
            
        } catch (CryptoException e) {
            auditLog.logException("Erro ao descriptografar template", e);
            return false;
        } catch (Exception e) {
            auditLog.logException("Erro durante verificação biométrica", e);
            return false;
        }
    }
    
    /**
     * Verifica se o usuário tem permissão para acessar o nível solicitado.
     * 
     * Algoritmo:
     * 1. Obtém nível máximo do usuário
     * 2. Compara hierarquicamente com nível solicitado
     * 3. Registra verificação de acesso
     * 
     * @param user usuário a verificar
     * @param requestedLevel nível de acesso solicitado
     * @return true se o usuário tem permissão, false caso contrário
     * 
     * Valida: Requisitos 2.1, 2.2, 2.3, 2.4, 6.5
     */
    private boolean checkAccessLevel(User user, AccessLevel requestedLevel) {
        // Step 1: Obter nível máximo do usuário
        AccessLevel userMaxLevel = user.getMaxAccessLevel();
        
        // Step 2: Comparar níveis hierarquicamente (Requisito 2.1)
        // Nível maior ou igual permite acesso
        boolean hasAccess = userMaxLevel.getLevel() >= requestedLevel.getLevel();
        
        // Step 3: Registrar verificação de acesso (Requisito 6.5)
        auditLog.logAccessCheck(user.getUserId(), requestedLevel, hasAccess);
        
        return hasAccess;
    }
    
    /**
     * Retorna o threshold de similaridade baseado no tipo biométrico.
     * 
     * @param type tipo biométrico
     * @return threshold de similaridade
     * 
     * Valida: Requisitos 5.2, 5.3, 5.4
     */
    private double getThresholdForType(BiometricType type) {
        switch (type) {
            case FINGERPRINT:
                return 0.85; // Requisito 5.2
            case FACIAL_RECOGNITION:
                return 0.88; // Requisito 5.3
            case IRIS_SCAN:
                return 0.92; // Requisito 5.4
            default:
                throw new IllegalArgumentException("Tipo biométrico desconhecido: " + type);
        }
    }
}
