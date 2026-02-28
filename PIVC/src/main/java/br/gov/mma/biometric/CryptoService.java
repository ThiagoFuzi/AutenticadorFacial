package br.gov.mma.biometric;

/**
 * Service for encrypting and decrypting biometric templates.
 * Uses AES-256 encryption to protect sensitive biometric data.
 * 
 * Requirements: 4.5, 10.4
 */
public interface CryptoService {
    
    /**
     * Encrypts a biometric template using AES-256 encryption.
     * 
     * @param template the raw biometric template to encrypt
     * @return the encrypted template
     * @throws CryptoException if encryption fails
     */
    byte[] encrypt(byte[] template) throws CryptoException;
    
    /**
     * Decrypts an encrypted biometric template.
     * 
     * @param encryptedTemplate the encrypted template to decrypt
     * @return the decrypted biometric template
     * @throws CryptoException if decryption fails
     */
    byte[] decrypt(byte[] encryptedTemplate) throws CryptoException;
}
