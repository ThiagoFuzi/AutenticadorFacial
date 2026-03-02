package br.gov.mma.biometric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Implementação de gerador determinístico de templates biométricos.
 * 
 * Usa SHA-256 para gerar um seed determinístico a partir do userId,
 * garantindo que o mesmo usuário sempre gera o mesmo template.
 * 
 * Valida: Requisitos 2.1, 2.2, 2.3, 2.4, 2.5
 */
public class DeterministicTemplateGeneratorImpl implements DeterministicTemplateGenerator {
    
    private static final int TEMPLATE_SIZE = 512;
    private static final String HASH_ALGORITHM = "SHA-256";
    
    /**
     * Gera um template biométrico determinístico baseado no ID do usuário.
     * 
     * @param userId identificador único do usuário
     * @return array de 512 bytes contendo o template biométrico determinístico
     * @throws IllegalArgumentException se userId for nulo ou vazio
     */
    @Override
    public byte[] generateDeterministicTemplate(String userId) {
        // Validação de entrada
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId não pode ser nulo ou vazio");
        }
        
        try {
            // Step 1: Calcular hash SHA-256 do userId
            byte[] userIdHash = hashUserId(userId);
            
            // Step 2: Converter hash para seed long
            long seed = bytesToLong(userIdHash);
            
            // Step 3: Gerar template usando Random com seed determinístico
            Random random = new Random(seed);
            byte[] template = new byte[TEMPLATE_SIZE];
            random.nextBytes(template);
            
            // Step 4: Aplicar transformação XOR com hash para aumentar divergência
            for (int i = 0; i < template.length; i++) {
                template[i] ^= userIdHash[i % userIdHash.length];
            }
            
            return template;
            
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 sempre deve estar disponível em Java
            throw new RuntimeException("SHA-256 não disponível", e);
        }
    }
    
    /**
     * Calcula o hash SHA-256 do userId.
     * 
     * @param userId identificador do usuário
     * @return array de bytes contendo o hash SHA-256
     * @throws NoSuchAlgorithmException se SHA-256 não estiver disponível
     */
    private byte[] hashUserId(String userId) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return digest.digest(userId.getBytes());
    }
    
    /**
     * Converte os primeiros 8 bytes de um array em um long.
     * 
     * @param bytes array de bytes
     * @return valor long derivado dos primeiros 8 bytes
     */
    private long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < Math.min(8, bytes.length); i++) {
            result = (result << 8) | (bytes[i] & 0xFF);
        }
        return result;
    }
}
