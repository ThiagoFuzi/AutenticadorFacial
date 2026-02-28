package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.BiometricData;
import br.gov.mma.biometric.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementação do sistema de auditoria.
 * Registra todas as operações do sistema de forma permanente e imutável.
 * 
 * Valida: Requisitos 6.1, 6.2, 6.3, 6.4, 6.5, 6.6
 */
public class AuditLogImpl implements AuditLog {
    
    private static final String LOG_FILE = "audit.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    /**
     * Registra uma tentativa de autenticação falhada.
     * 
     * @param reason motivo da falha
     * @param biometricData dados biométricos utilizados (sem informações sensíveis)
     */
    @Override
    public void logFailedAttempt(String reason, BiometricData biometricData) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String biometricType = biometricData != null ? biometricData.getType().toString() : "UNKNOWN";
        double quality = biometricData != null ? biometricData.getQuality() : 0.0;
        
        String logEntry = String.format(
            "[%s] FAILED_AUTHENTICATION | Reason: %s | BiometricType: %s | Quality: %.2f",
            timestamp, reason, biometricType, quality
        );
        
        writeLog(logEntry);
    }
    
    /**
     * Registra uma autenticação bem-sucedida.
     * 
     * @param user usuário autenticado
     * @param grantedLevel nível de acesso concedido
     */
    @Override
    public void logSuccessfulAuthentication(User user, AccessLevel grantedLevel) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        String logEntry = String.format(
            "[%s] SUCCESSFUL_AUTHENTICATION | UserId: %s | UserName: %s | GrantedLevel: %s (%d)",
            timestamp, user.getUserId(), user.getName(), 
            grantedLevel.name(), grantedLevel.getLevel()
        );
        
        writeLog(logEntry);
    }

    /**
     * Registra uma falha no processo de enrollment.
     * 
     * @param reason motivo da falha
     */
    @Override
    public void logEnrollmentFailure(String reason) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        String logEntry = String.format(
            "[%s] ENROLLMENT_FAILURE | Reason: %s",
            timestamp, reason
        );
        
        writeLog(logEntry);
    }
    
    /**
     * Registra um enrollment bem-sucedido.
     * 
     * @param userId identificador do usuário cadastrado
     * @param accessLevel nível de acesso do usuário
     */
    @Override
    public void logSuccessfulEnrollment(String userId, AccessLevel accessLevel) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        String logEntry = String.format(
            "[%s] SUCCESSFUL_ENROLLMENT | UserId: %s | AccessLevel: %s (%d)",
            timestamp, userId, accessLevel.name(), accessLevel.getLevel()
        );
        
        writeLog(logEntry);
    }
    
    /**
     * Registra uma verificação de nível de acesso.
     * 
     * @param userId identificador do usuário
     * @param requestedLevel nível de acesso solicitado
     * @param granted se o acesso foi concedido ou negado
     */
    @Override
    public void logAccessCheck(String userId, AccessLevel requestedLevel, boolean granted) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String decision = granted ? "GRANTED" : "DENIED";
        
        String logEntry = String.format(
            "[%s] ACCESS_CHECK | UserId: %s | RequestedLevel: %s (%d) | Decision: %s",
            timestamp, userId, requestedLevel.name(), requestedLevel.getLevel(), decision
        );
        
        writeLog(logEntry);
    }
    
    /**
     * Registra uma exceção ocorrida no sistema.
     * 
     * @param message mensagem descritiva do erro
     * @param exception exceção capturada
     */
    @Override
    public void logException(String message, Exception exception) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        String logEntry = String.format(
            "[%s] EXCEPTION | Message: %s | Exception: %s | ExceptionMessage: %s",
            timestamp, message, exception.getClass().getName(), exception.getMessage()
        );
        
        writeLog(logEntry);
    }
    
    /**
     * Escreve uma entrada no arquivo de log de forma permanente e imutável.
     * Usa modo append para garantir que logs anteriores não sejam sobrescritos.
     * 
     * @param logEntry entrada a ser registrada
     */
    private void writeLog(String logEntry) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(logEntry);
            out.flush();
            
        } catch (IOException e) {
            // Em caso de falha ao escrever no log, imprime no console
            // para não perder a informação de auditoria
            System.err.println("ERRO AO ESCREVER LOG DE AUDITORIA: " + e.getMessage());
            System.err.println("Log entry: " + logEntry);
        }
    }
}
