package br.gov.mma.biometric;

/**
 * Exceção lançada quando ocorre erro durante a captura de dados biométricos.
 */
public class BiometricCaptureException extends Exception {
    
    public BiometricCaptureException(String message) {
        super(message);
    }
    
    public BiometricCaptureException(String message, Throwable cause) {
        super(message, cause);
    }
}
