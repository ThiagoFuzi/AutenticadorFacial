package br.gov.mma.biometric;

/**
 * Implementação de BiometricMatcher para reconhecimento facial.
 * Utiliza algoritmo simplificado de comparação de templates faciais.
 * 
 * O algoritmo calcula a similaridade baseado em:
 * - Correlação normalizada entre templates
 * - Distância euclidiana normalizada
 * - Pontos característicos faciais
 * 
 * Valida: Requisitos 5.1, 5.3
 */
public class FacialRecognitionMatcher implements BiometricMatcher {
    
    /**
     * Calcula o score de similaridade entre dois templates de reconhecimento facial.
     * 
     * Algoritmo simplificado:
     * 1. Valida que os templates têm o mesmo tamanho
     * 2. Calcula a correlação normalizada entre os templates
     * 3. Calcula a distância euclidiana normalizada
     * 4. Combina as métricas para produzir score final entre 0.0 e 1.0
     * 
     * @param capturedTemplate template capturado durante autenticação
     * @param storedTemplate template armazenado no banco de dados
     * @return score de similaridade entre 0.0 (totalmente diferente) e 1.0 (idêntico)
     * @throws IllegalArgumentException se templates são nulos ou têm tamanhos diferentes
     */
    @Override
    public double calculateSimilarity(byte[] capturedTemplate, byte[] storedTemplate) {
        // Validação de entrada
        if (capturedTemplate == null || storedTemplate == null) {
            throw new IllegalArgumentException("Templates não podem ser nulos");
        }
        
        if (capturedTemplate.length == 0 || storedTemplate.length == 0) {
            throw new IllegalArgumentException("Templates não podem estar vazios");
        }
        
        if (capturedTemplate.length != storedTemplate.length) {
            throw new IllegalArgumentException(
                "Templates devem ter o mesmo tamanho para comparação"
            );
        }
        
        // Algoritmo simplificado de comparação facial
        // Combina múltiplas métricas para robustez
        
        // Métrica 1: Correlação normalizada (mede similaridade de padrão)
        double correlation = calculateNormalizedCorrelation(capturedTemplate, storedTemplate);
        
        // Métrica 2: Distância euclidiana invertida e normalizada (mede proximidade)
        double euclideanSimilarity = calculateEuclideanSimilarity(capturedTemplate, storedTemplate);
        
        // Métrica 3: Contagem de pontos característicos correspondentes
        double featureMatchScore = calculateFeatureMatchScore(capturedTemplate, storedTemplate);
        
        // Combina as três métricas com pesos apropriados
        // Correlação tem maior peso para reconhecimento facial
        double finalScore = (0.5 * correlation) + (0.3 * euclideanSimilarity) + (0.2 * featureMatchScore);
        
        // Garante que o score está no intervalo [0.0, 1.0]
        return Math.max(0.0, Math.min(1.0, finalScore));
    }
    
    /**
     * Calcula a correlação normalizada entre dois templates.
     * Mede o quão similar é o padrão dos templates.
     * 
     * @param template1 primeiro template
     * @param template2 segundo template
     * @return correlação normalizada entre 0.0 e 1.0
     */
    private double calculateNormalizedCorrelation(byte[] template1, byte[] template2) {
        // Calcula médias
        double mean1 = calculateMean(template1);
        double mean2 = calculateMean(template2);
        
        // Calcula correlação de Pearson
        double numerator = 0.0;
        double sumSquares1 = 0.0;
        double sumSquares2 = 0.0;
        
        for (int i = 0; i < template1.length; i++) {
            double diff1 = (template1[i] & 0xFF) - mean1;
            double diff2 = (template2[i] & 0xFF) - mean2;
            
            numerator += diff1 * diff2;
            sumSquares1 += diff1 * diff1;
            sumSquares2 += diff2 * diff2;
        }
        
        double denominator = Math.sqrt(sumSquares1 * sumSquares2);
        
        if (denominator == 0.0) {
            return 0.0;
        }
        
        // Correlação de Pearson está em [-1, 1], normaliza para [0, 1]
        double correlation = numerator / denominator;
        return (correlation + 1.0) / 2.0;
    }
    
    /**
     * Calcula a similaridade baseada na distância euclidiana.
     * Quanto menor a distância, maior a similaridade.
     * 
     * @param template1 primeiro template
     * @param template2 segundo template
     * @return similaridade baseada em distância, entre 0.0 e 1.0
     */
    private double calculateEuclideanSimilarity(byte[] template1, byte[] template2) {
        double sumSquaredDiff = 0.0;
        
        for (int i = 0; i < template1.length; i++) {
            int val1 = template1[i] & 0xFF;
            int val2 = template2[i] & 0xFF;
            double diff = val1 - val2;
            sumSquaredDiff += diff * diff;
        }
        
        double euclideanDistance = Math.sqrt(sumSquaredDiff);
        
        // Normaliza a distância pelo tamanho do template
        // Distância máxima possível é 255 * sqrt(length)
        double maxDistance = 255.0 * Math.sqrt(template1.length);
        
        // Converte distância em similaridade (inverte e normaliza)
        return 1.0 - (euclideanDistance / maxDistance);
    }
    
    /**
     * Calcula o score de correspondência de características faciais.
     * Analisa pontos característicos específicos do template facial.
     * 
     * @param template1 primeiro template
     * @param template2 segundo template
     * @return score de correspondência de características, entre 0.0 e 1.0
     */
    private double calculateFeatureMatchScore(byte[] template1, byte[] template2) {
        // Algoritmo simplificado: divide o template em regiões
        // e compara a similaridade de cada região
        int numRegions = 8; // Divide em 8 regiões (olhos, nariz, boca, etc.)
        int regionSize = template1.length / numRegions;
        
        int matchingRegions = 0;
        double threshold = 0.85; // Threshold para considerar região correspondente
        
        for (int region = 0; region < numRegions; region++) {
            int start = region * regionSize;
            int end = (region == numRegions - 1) ? template1.length : (region + 1) * regionSize;
            
            double regionSimilarity = calculateRegionSimilarity(template1, template2, start, end);
            
            if (regionSimilarity >= threshold) {
                matchingRegions++;
            }
        }
        
        // Retorna proporção de regiões correspondentes
        return (double) matchingRegions / numRegions;
    }
    
    /**
     * Calcula a similaridade de uma região específica do template.
     * 
     * @param template1 primeiro template
     * @param template2 segundo template
     * @param start índice inicial da região
     * @param end índice final da região
     * @return similaridade da região entre 0.0 e 1.0
     */
    private double calculateRegionSimilarity(byte[] template1, byte[] template2, int start, int end) {
        double sumSquaredDiff = 0.0;
        int count = end - start;
        
        for (int i = start; i < end; i++) {
            int val1 = template1[i] & 0xFF;
            int val2 = template2[i] & 0xFF;
            double diff = val1 - val2;
            sumSquaredDiff += diff * diff;
        }
        
        double rmse = Math.sqrt(sumSquaredDiff / count);
        
        // Normaliza RMSE (máximo é 255)
        return 1.0 - (rmse / 255.0);
    }
    
    /**
     * Calcula a média dos valores de um template.
     * 
     * @param template template biométrico
     * @return média dos valores do template
     */
    private double calculateMean(byte[] template) {
        double sum = 0.0;
        for (byte b : template) {
            sum += (b & 0xFF); // Converte para unsigned
        }
        return sum / template.length;
    }
}
