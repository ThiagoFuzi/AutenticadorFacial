package br.gov.mma.biometric;

/**
 * Interface para geração determinística de templates biométricos.
 * 
 * Garante que o mesmo usuário sempre gera o mesmo template biométrico,
 * enquanto usuários diferentes geram templates completamente diferentes.
 * 
 * Esta interface é essencial para corrigir o bug de autenticação permissiva,
 * onde templates aleatórios causavam falsos positivos.
 * 
 * Valida: Requisitos 2.1, 2.2, 2.3, 2.4, 2.5
 */
public interface DeterministicTemplateGenerator {
    
    /**
     * Gera um template biométrico determinístico baseado no ID do usuário.
     * 
     * Propriedades garantidas:
     * - Determinismo: O mesmo userId sempre gera o mesmo template
     * - Tamanho: O template sempre tem exatamente 512 bytes
     * - Divergência: Usuários diferentes geram templates com similaridade < 0.88
     * - Variação: O template contém bytes variados (não é preenchido com zeros)
     * 
     * Algoritmo:
     * 1. Calcula hash SHA-256 do userId
     * 2. Converte o hash para um seed long
     * 3. Usa Random com o seed para gerar 512 bytes
     * 4. Aplica transformação XOR com o hash para aumentar divergência
     * 
     * @param userId identificador único do usuário (não pode ser nulo ou vazio)
     * @return array de 512 bytes contendo o template biométrico determinístico
     * @throws IllegalArgumentException se userId for nulo ou vazio
     * 
     * Exemplo de uso:
     * <pre>
     * DeterministicTemplateGenerator generator = new DeterministicTemplateGeneratorImpl();
     * byte[] template1 = generator.generateDeterministicTemplate("USER-001");
     * byte[] template2 = generator.generateDeterministicTemplate("USER-001");
     * // template1 e template2 são idênticos
     * 
     * byte[] template3 = generator.generateDeterministicTemplate("USER-002");
     * // template3 é completamente diferente de template1
     * </pre>
     */
    byte[] generateDeterministicTemplate(String userId);
}
