package br.gov.mma.biometric;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CryptoService implementation.
 * Tests encryption and decryption of biometric templates using AES-256.
 * 
 * Requirements: 4.5, 10.4
 */
class CryptoServiceTest {
    
    private CryptoService cryptoService;
    
    @BeforeEach
    void setUp() throws CryptoException {
        cryptoService = new CryptoServiceImpl();
    }
    
    @Test
    @DisplayName("Should encrypt and decrypt template successfully")
    void testEncryptDecrypt() throws CryptoException {
        // Arrange
        byte[] originalTemplate = "biometric_template_data_12345".getBytes();
        
        // Act
        byte[] encrypted = cryptoService.encrypt(originalTemplate);
        byte[] decrypted = cryptoService.decrypt(encrypted);
        
        // Assert
        assertNotNull(encrypted, "Encrypted template should not be null");
        assertNotNull(decrypted, "Decrypted template should not be null");
        assertArrayEquals(originalTemplate, decrypted, "Decrypted template should match original");
    }
    
    @Test
    @DisplayName("Should produce different ciphertext for same plaintext (due to random IV)")
    void testEncryptionProducesDifferentCiphertext() throws CryptoException {
        // Arrange
        byte[] template = "biometric_template_data".getBytes();
        
        // Act
        byte[] encrypted1 = cryptoService.encrypt(template);
        byte[] encrypted2 = cryptoService.encrypt(template);
        
        // Assert
        assertFalse(Arrays.equals(encrypted1, encrypted2), 
            "Two encryptions of same data should produce different ciphertext (random IV)");
    }
    
    @Test
    @DisplayName("Should produce ciphertext different from plaintext")
    void testEncryptedDifferentFromOriginal() throws CryptoException {
        // Arrange
        byte[] template = "biometric_template_data".getBytes();
        
        // Act
        byte[] encrypted = cryptoService.encrypt(template);
        
        // Assert
        assertFalse(Arrays.equals(template, encrypted), 
            "Encrypted template should be different from original");
    }
    
    @Test
    @DisplayName("Should throw exception when encrypting null template")
    void testEncryptNullTemplate() {
        // Act & Assert
        assertThrows(CryptoException.class, () -> cryptoService.encrypt(null),
            "Should throw CryptoException for null template");
    }
    
    @Test
    @DisplayName("Should throw exception when encrypting empty template")
    void testEncryptEmptyTemplate() {
        // Arrange
        byte[] emptyTemplate = new byte[0];
        
        // Act & Assert
        assertThrows(CryptoException.class, () -> cryptoService.encrypt(emptyTemplate),
            "Should throw CryptoException for empty template");
    }
    
    @Test
    @DisplayName("Should throw exception when decrypting null template")
    void testDecryptNullTemplate() {
        // Act & Assert
        assertThrows(CryptoException.class, () -> cryptoService.decrypt(null),
            "Should throw CryptoException for null encrypted template");
    }
    
    @Test
    @DisplayName("Should throw exception when decrypting invalid template")
    void testDecryptInvalidTemplate() {
        // Arrange - template too short (less than IV length)
        byte[] invalidTemplate = new byte[5];
        
        // Act & Assert
        assertThrows(CryptoException.class, () -> cryptoService.decrypt(invalidTemplate),
            "Should throw CryptoException for invalid encrypted template");
    }
    
    @Test
    @DisplayName("Should throw exception when decrypting corrupted template")
    void testDecryptCorruptedTemplate() throws CryptoException {
        // Arrange
        byte[] template = "biometric_template_data".getBytes();
        byte[] encrypted = cryptoService.encrypt(template);
        
        // Corrupt the encrypted data
        encrypted[encrypted.length - 1] ^= 0xFF;
        
        // Act & Assert
        byte[] finalEncrypted = encrypted;
        assertThrows(CryptoException.class, () -> cryptoService.decrypt(finalEncrypted),
            "Should throw CryptoException for corrupted encrypted template");
    }
    
    @Test
    @DisplayName("Should handle large biometric templates")
    void testEncryptDecryptLargeTemplate() throws CryptoException {
        // Arrange - simulate a large biometric template (e.g., high-res fingerprint)
        byte[] largeTemplate = new byte[10000];
        Arrays.fill(largeTemplate, (byte) 0x42);
        
        // Act
        byte[] encrypted = cryptoService.encrypt(largeTemplate);
        byte[] decrypted = cryptoService.decrypt(encrypted);
        
        // Assert
        assertArrayEquals(largeTemplate, decrypted, 
            "Should correctly encrypt and decrypt large templates");
    }
    
    @Test
    @DisplayName("Should work with different CryptoService instances using same key")
    void testDifferentInstancesSameKey() throws CryptoException {
        // Arrange
        CryptoServiceImpl service1 = new CryptoServiceImpl();
        byte[] key = service1.getKeyBytes();
        CryptoServiceImpl service2 = new CryptoServiceImpl(key);
        
        byte[] template = "biometric_template_data".getBytes();
        
        // Act
        byte[] encrypted = service1.encrypt(template);
        byte[] decrypted = service2.decrypt(encrypted);
        
        // Assert
        assertArrayEquals(template, decrypted,
            "Different instances with same key should be able to decrypt each other's data");
    }
    
    @Test
    @DisplayName("Should throw exception when creating service with invalid key size")
    void testInvalidKeySize() {
        // Arrange - key with wrong size (not 32 bytes)
        byte[] invalidKey = new byte[16]; // 128 bits instead of 256
        
        // Act & Assert
        assertThrows(CryptoException.class, () -> new CryptoServiceImpl(invalidKey),
            "Should throw CryptoException for invalid key size");
    }
    
    @Test
    @DisplayName("Should throw exception when creating service with null key")
    void testNullKey() {
        // Act & Assert
        assertThrows(CryptoException.class, () -> new CryptoServiceImpl(null),
            "Should throw CryptoException for null key");
    }
}
