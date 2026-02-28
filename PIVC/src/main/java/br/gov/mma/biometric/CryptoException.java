package br.gov.mma.biometric;

/**
 * Exception thrown when cryptographic operations fail.
 */
public class CryptoException extends Exception {
    
    public CryptoException(String message) {
        super(message);
    }
    
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
