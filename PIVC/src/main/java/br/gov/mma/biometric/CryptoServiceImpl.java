package br.gov.mma.biometric;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Implementation of CryptoService using AES-256-GCM encryption.
 * 
 * AES-256-GCM provides:
 * - Strong encryption (256-bit key)
 * - Authentication (GCM mode)
 * - Protection against tampering
 * 
 * Requirements: 4.5, 10.4
 */
public class CryptoServiceImpl implements CryptoService {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256; // AES-256
    private static final int GCM_IV_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 128; // 128 bits
    
    private final SecretKey secretKey;
    private final SecureRandom secureRandom;
    
    /**
     * Creates a new CryptoService with a generated AES-256 key.
     * 
     * @throws CryptoException if key generation fails
     */
    public CryptoServiceImpl() throws CryptoException {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_SIZE);
            this.secretKey = keyGenerator.generateKey();
            this.secureRandom = new SecureRandom();
        } catch (Exception e) {
            throw new CryptoException("Failed to initialize CryptoService", e);
        }
    }
    
    /**
     * Creates a new CryptoService with a provided key.
     * 
     * @param keyBytes the 256-bit (32 bytes) AES key
     * @throws CryptoException if key is invalid
     */
    public CryptoServiceImpl(byte[] keyBytes) throws CryptoException {
        if (keyBytes == null || keyBytes.length != 32) {
            throw new CryptoException("Key must be 32 bytes (256 bits) for AES-256");
        }
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        this.secureRandom = new SecureRandom();
    }
    
    @Override
    public byte[] encrypt(byte[] template) throws CryptoException {
        if (template == null || template.length == 0) {
            throw new CryptoException("Template cannot be null or empty");
        }
        
        try {
            // Generate random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            // Initialize cipher
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            // Encrypt
            byte[] ciphertext = cipher.doFinal(template);
            
            // Combine IV and ciphertext: [IV][ciphertext]
            byte[] encrypted = new byte[GCM_IV_LENGTH + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, GCM_IV_LENGTH);
            System.arraycopy(ciphertext, 0, encrypted, GCM_IV_LENGTH, ciphertext.length);
            
            return encrypted;
            
        } catch (Exception e) {
            throw new CryptoException("Encryption failed", e);
        }
    }
    
    @Override
    public byte[] decrypt(byte[] encryptedTemplate) throws CryptoException {
        if (encryptedTemplate == null || encryptedTemplate.length <= GCM_IV_LENGTH) {
            throw new CryptoException("Encrypted template is invalid");
        }
        
        try {
            // Extract IV and ciphertext
            byte[] iv = Arrays.copyOfRange(encryptedTemplate, 0, GCM_IV_LENGTH);
            byte[] ciphertext = Arrays.copyOfRange(encryptedTemplate, GCM_IV_LENGTH, encryptedTemplate.length);
            
            // Initialize cipher
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            // Decrypt
            return cipher.doFinal(ciphertext);
            
        } catch (Exception e) {
            throw new CryptoException("Decryption failed", e);
        }
    }
    
    /**
     * Gets the encryption key bytes (for persistence/configuration).
     * WARNING: Handle with extreme care - this is sensitive cryptographic material.
     * 
     * @return the 256-bit encryption key
     */
    public byte[] getKeyBytes() {
        return secretKey.getEncoded();
    }
}
