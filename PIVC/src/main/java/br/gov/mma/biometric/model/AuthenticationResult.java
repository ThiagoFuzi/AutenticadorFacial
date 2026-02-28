package br.gov.mma.biometric.model;

import java.time.LocalDateTime;

/**
 * Resultado de uma tentativa de autenticação biométrica.
 * Contém informações sobre sucesso/falha, usuário autenticado, nível concedido e token de sessão.
 * 
 * Requisitos: 9.4
 */
public class AuthenticationResult {
    private final boolean success;
    private final User user;
    private final AccessLevel grantedLevel;
    private final String message;
    private final LocalDateTime timestamp;
    private final String sessionToken;
    
    private AuthenticationResult(boolean success, User user, AccessLevel grantedLevel,
                                 String message, String sessionToken) {
        this.success = success;
        this.user = user;
        this.grantedLevel = grantedLevel;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.sessionToken = sessionToken;
    }
    
    public static AuthenticationResult success(User user, AccessLevel grantedLevel, String token) {
        return new AuthenticationResult(true, user, grantedLevel,
            "Autenticação bem-sucedida", token);
    }
    
    public static AuthenticationResult failure(String message) {
        return new AuthenticationResult(false, null, null, message, null);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public User getUser() {
        return user;
    }
    
    public AccessLevel getGrantedLevel() {
        return grantedLevel;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSessionToken() {
        return sessionToken;
    }
}
