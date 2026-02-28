package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.BiometricData;
import br.gov.mma.biometric.model.User;

/**
 * Interface para registro de auditoria.
 * Responsável por registrar todas as tentativas de autenticação e operações do sistema.
 * 
 * Valida: Requisitos 6.1, 6.2, 6.3, 6.4, 6.5, 6.6
 */
public interface AuditLog {
    
    /**
     * Registra uma tentativa de autenticação falhada.
     * 
     * @param reason motivo da falha
     * @param biometricData dados biométricos utilizados (sem informações sensíveis)
     */
    void logFailedAttempt(String reason, BiometricData biometricData);
    
    /**
     * Registra uma autenticação bem-sucedida.
     * 
     * @param user usuário autenticado
     * @param grantedLevel nível de acesso concedido
     */
    void logSuccessfulAuthentication(User user, AccessLevel grantedLevel);
    
    /**
     * Registra uma falha no processo de enrollment.
     * 
     * @param reason motivo da falha
     */
    void logEnrollmentFailure(String reason);
    
    /**
     * Registra um enrollment bem-sucedido.
     * 
     * @param userId identificador do usuário cadastrado
     * @param accessLevel nível de acesso do usuário
     */
    void logSuccessfulEnrollment(String userId, AccessLevel accessLevel);
    
    /**
     * Registra uma verificação de nível de acesso.
     * 
     * @param userId identificador do usuário
     * @param requestedLevel nível de acesso solicitado
     * @param granted se o acesso foi concedido ou negado
     */
    void logAccessCheck(String userId, AccessLevel requestedLevel, boolean granted);
    
    /**
     * Registra uma exceção ocorrida no sistema.
     * 
     * @param message mensagem descritiva do erro
     * @param exception exceção capturada
     */
    void logException(String message, Exception exception);
}
