package br.gov.mma.biometric;

import br.gov.mma.biometric.model.BiometricType;
import br.gov.mma.biometric.model.User;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação em memória do banco de dados de usuários.
 * Utiliza estruturas de dados concorrentes para garantir thread safety.
 * 
 * Valida: Requisitos 4.3, 4.4, 10.1, 10.2, 10.3
 */
public class InMemoryUserDatabase implements UserDatabase {
    
    // Mapa de userId para User - garante unicidade de userId
    private final ConcurrentHashMap<String, User> usersByUserId;
    
    // Mapa de hash de template biométrico para User - garante unicidade de template
    private final ConcurrentHashMap<Integer, User> usersByBiometricHash;
    
    // Factory para obter matchers apropriados
    private final BiometricMatcherFactory matcherFactory;
    
    // CryptoService para descriptografar templates ao comparar
    private final CryptoService cryptoService;
    
    /**
     * Construtor que inicializa as estruturas de dados concorrentes.
     * 
     * @param cryptoService serviço de criptografia para descriptografar templates
     */
    public InMemoryUserDatabase(CryptoService cryptoService) {
        this.usersByUserId = new ConcurrentHashMap<>();
        this.usersByBiometricHash = new ConcurrentHashMap<>();
        this.matcherFactory = new BiometricMatcherFactory();
        this.cryptoService = cryptoService;
    }
    
    /**
     * Busca um usuário por seu template biométrico.
     * Compara o template fornecido com todos os templates armazenados usando o matcher apropriado.
     * 
     * @param template template biométrico a ser buscado
     * @param type tipo de biometria do template
     * @return Optional contendo o usuário se encontrado, vazio caso contrário
     */
    @Override
    public Optional<User> findUserByBiometric(byte[] template, BiometricType type) {
        if (template == null || template.length == 0 || type == null) {
            return Optional.empty();
        }
        
        // Obter o matcher apropriado para o tipo biométrico
        BiometricMatcher matcher = matcherFactory.getMatcher(type);
        
        // Obter threshold baseado no tipo biométrico
        double threshold = getThresholdForType(type);
        
        // Iterar sobre todos os usuários e comparar templates
        for (User user : usersByUserId.values()) {
            // Verificar se o tipo biométrico corresponde
            if (user.getBiometricType() != type) {
                continue;
            }
            
            try {
                // Descriptografar o template armazenado para comparação
                byte[] storedTemplate = cryptoService.decrypt(user.getStoredBiometricTemplate());
                
                // Verificar se os templates têm o mesmo tamanho
                if (template.length != storedTemplate.length) {
                    continue;
                }
                
                // Calcular similaridade
                double similarity = matcher.calculateSimilarity(template, storedTemplate);
                
                // Se a similaridade é maior ou igual ao threshold, encontramos o usuário
                if (similarity >= threshold) {
                    return Optional.of(user);
                }
            } catch (CryptoException e) {
                // Se falhar ao descriptografar, pular este usuário
                continue;
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Busca um usuário por seu identificador único.
     * 
     * @param userId identificador do usuário
     * @return Optional contendo o usuário se encontrado, vazio caso contrário
     */
    @Override
    public Optional<User> findUserById(String userId) {
        if (userId == null || userId.isEmpty()) {
            return Optional.empty();
        }
        
        User user = usersByUserId.get(userId);
        return Optional.ofNullable(user);
    }
    
    /**
     * Salva um novo usuário no banco de dados.
     * Valida unicidade de userId e template biométrico.
     * 
     * @param user usuário a ser salvo
     * @return true se o usuário foi salvo com sucesso, false caso contrário
     */
    @Override
    public boolean saveUser(User user) {
        if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
            return false;
        }
        
        // Verificar se userId já existe (Requisito 4.3, 10.1)
        if (usersByUserId.containsKey(user.getUserId())) {
            return false;
        }
        
        // Verificar se template biométrico já existe (Requisito 4.4, 10.2)
        int templateHash = Arrays.hashCode(user.getStoredBiometricTemplate());
        if (usersByBiometricHash.containsKey(templateHash)) {
            // Verificar se é realmente o mesmo template (não apenas colisão de hash)
            User existingUser = usersByBiometricHash.get(templateHash);
            if (Arrays.equals(existingUser.getStoredBiometricTemplate(), 
                             user.getStoredBiometricTemplate())) {
                return false;
            }
        }
        
        // Usar putIfAbsent para garantir atomicidade (Requisito 10.3)
        User previousUser = usersByUserId.putIfAbsent(user.getUserId(), user);
        if (previousUser != null) {
            // Outro thread inseriu o usuário entre nossa verificação e inserção
            return false;
        }
        
        // Adicionar ao mapa de templates biométricos
        usersByBiometricHash.put(templateHash, user);
        
        return true;
    }
    
    /**
     * Atualiza os dados de um usuário existente.
     * 
     * @param user usuário com dados atualizados
     * @return true se o usuário foi atualizado com sucesso, false caso contrário
     */
    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
            return false;
        }
        
        // Verificar se o usuário existe
        if (!usersByUserId.containsKey(user.getUserId())) {
            return false;
        }
        
        // Obter o usuário antigo para remover do mapa de templates
        User oldUser = usersByUserId.get(user.getUserId());
        
        // Remover o hash do template antigo
        int oldTemplateHash = Arrays.hashCode(oldUser.getStoredBiometricTemplate());
        usersByBiometricHash.remove(oldTemplateHash);
        
        // Atualizar o usuário
        usersByUserId.put(user.getUserId(), user);
        
        // Adicionar o novo hash do template
        int newTemplateHash = Arrays.hashCode(user.getStoredBiometricTemplate());
        usersByBiometricHash.put(newTemplateHash, user);
        
        return true;
    }
    
    /**
     * Retorna o threshold de similaridade baseado no tipo biométrico.
     * 
     * @param type tipo biométrico
     * @return threshold de similaridade (0.85 para FINGERPRINT, 0.88 para FACIAL_RECOGNITION, 0.92 para IRIS_SCAN)
     */
    private double getThresholdForType(BiometricType type) {
        switch (type) {
            case FINGERPRINT:
                return 0.85;
            case FACIAL_RECOGNITION:
                return 0.88;
            case IRIS_SCAN:
                return 0.92;
            default:
                throw new IllegalArgumentException("Tipo biométrico desconhecido: " + type);
        }
    }
}
