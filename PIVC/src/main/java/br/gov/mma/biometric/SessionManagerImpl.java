package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.User;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação do gerenciador de sessões para tokens de autenticação.
 * Gera tokens únicos e armazena contexto de sessão para usuários autenticados.
 * 
 * Valida: Requisitos 7.1, 7.2, 7.4
 */
public class SessionManagerImpl implements SessionManager {
    
    private static final int TOKEN_BYTE_SIZE = 32; // 256 bits
    private final SecureRandom secureRandom;
    private final Map<String, SessionContext> sessions;
    
    /**
     * Contexto de sessão armazenado para cada token.
     */
    private static class SessionContext {
        private final User user;
        private final AccessLevel grantedLevel;
        private final LocalDateTime timestamp;
        
        public SessionContext(User user, AccessLevel grantedLevel) {
            this.user = user;
            this.grantedLevel = grantedLevel;
            this.timestamp = LocalDateTime.now();
        }
        
        public User getUser() {
            return user;
        }
        
        public AccessLevel getGrantedLevel() {
            return grantedLevel;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
    
    /**
     * Construtor padrão.
     * Inicializa o gerador de números aleatórios seguros e o armazenamento de sessões.
     */
    public SessionManagerImpl() {
        this.secureRandom = new SecureRandom();
        this.sessions = new ConcurrentHashMap<>();
    }
    
    /**
     * Cria uma nova sessão para um usuário autenticado.
     * Gera um token único usando SecureRandom e Base64.
     * 
     * @param user usuário autenticado (não nulo)
     * @param grantedLevel nível de acesso concedido (não nulo)
     * @return token de sessão único e não vazio
     * @throws IllegalArgumentException se user ou grantedLevel forem nulos
     */
    @Override
    public String createSession(User user, AccessLevel grantedLevel) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (grantedLevel == null) {
            throw new IllegalArgumentException("GrantedLevel cannot be null");
        }
        
        // Gerar token único
        String token = generateUniqueToken();
        
        // Armazenar contexto da sessão
        SessionContext context = new SessionContext(user, grantedLevel);
        sessions.put(token, context);
        
        return token;
    }
    
    /**
     * Gera um token único usando SecureRandom.
     * Garante que o token não existe no mapa de sessões.
     * 
     * @return token único codificado em Base64
     */
    private String generateUniqueToken() {
        String token;
        do {
            byte[] randomBytes = new byte[TOKEN_BYTE_SIZE];
            secureRandom.nextBytes(randomBytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        } while (sessions.containsKey(token)); // Garantir unicidade
        
        return token;
    }
    
    /**
     * Valida se um token existe e é válido.
     * 
     * @param token token de sessão a validar
     * @return true se o token existe, false caso contrário
     */
    public boolean isValidToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return sessions.containsKey(token);
    }
    
    /**
     * Recupera o usuário associado a um token de sessão.
     * 
     * @param token token de sessão
     * @return usuário associado ao token, ou null se token inválido
     */
    public User getUserByToken(String token) {
        SessionContext context = sessions.get(token);
        return context != null ? context.getUser() : null;
    }
    
    /**
     * Recupera o nível de acesso concedido associado a um token de sessão.
     * 
     * @param token token de sessão
     * @return nível de acesso concedido, ou null se token inválido
     */
    public AccessLevel getGrantedLevelByToken(String token) {
        SessionContext context = sessions.get(token);
        return context != null ? context.getGrantedLevel() : null;
    }
    
    /**
     * Recupera o timestamp de criação da sessão.
     * 
     * @param token token de sessão
     * @return timestamp de criação, ou null se token inválido
     */
    public LocalDateTime getSessionTimestamp(String token) {
        SessionContext context = sessions.get(token);
        return context != null ? context.getTimestamp() : null;
    }
    
    /**
     * Invalida uma sessão removendo o token.
     * 
     * @param token token de sessão a invalidar
     * @return true se a sessão foi removida, false se não existia
     */
    public boolean invalidateSession(String token) {
        return sessions.remove(token) != null;
    }
    
    /**
     * Retorna o número de sessões ativas.
     * 
     * @return número de sessões ativas
     */
    public int getActiveSessionCount() {
        return sessions.size();
    }
}
