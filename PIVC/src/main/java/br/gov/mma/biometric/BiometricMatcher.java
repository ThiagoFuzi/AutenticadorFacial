package br.gov.mma.biometric;

/**
 * Interface para comparação de templates biométricos.
 * Responsável por calcular similaridade entre templates e determinar correspondência.
 * 
 * Valida: Requisitos 1.1, 5.1
 */
public interface BiometricMatcher {
    
    /**
     * Calcula o score de similaridade entre dois templates biométricos.
     * 
     * @param capturedTemplate template capturado durante autenticação
     * @param storedTemplate template armazenado no banco de dados
     * @return score de similaridade entre 0.0 (totalmente diferente) e 1.0 (idêntico)
     */
    double calculateSimilarity(byte[] capturedTemplate, byte[] storedTemplate);
}
