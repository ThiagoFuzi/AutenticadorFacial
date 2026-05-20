package br.gov.mma.biometric;

/**
 * Implementação de BiometricMatcher para reconhecimento facial baseado em LBP.
 *
 * Compara templates (concatenação de histogramas LBP por célula) usando
 * distância chi-quadrado, que é a métrica canônica pra histogramas LBP e
 * supera comparações euclidianas/Pearson por amplificar diferenças
 * proporcionais em bins de pouca massa — onde a identidade facial fica.
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
    /**
     * Escala de calibração da exponencial de chi-quadrado.
     *
     * Como o template agora é concatenação de histogramas LBP normalizados (0-255),
     * a métrica canônica é a distância chi-quadrado, que penaliza fortemente
     * diferenças entre bins com pouca massa — o oposto da euclidiana, que dilui
     * tudo numa média global. Convertemos a distância em similaridade via
     * exp(-chi² / SCALE) para mapear em [0,1].
     *
     * Calibragem com dados reais (capturas via webcam VGA, LBP com box blur):
     *   - mesma pessoa: chi² ≈ 440-520 → similaridade 0.77-0.80
     *   - pessoa diferente: chi² ≈ 1700-2300 → similaridade 0.33-0.42
     * Com scale=2000 e threshold=0.55, sobra ~0.22 de margem dos dois lados.
     */
    private static final double CHI_SQUARED_SCALE = 2000.0;

    @Override
    public double calculateSimilarity(byte[] capturedTemplate, byte[] storedTemplate) {
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

        // Distância chi-quadrado entre histogramas: Σ (a-b)² / (a+b)
        // É a métrica padrão pra comparar histogramas LBP — sensível a diferenças
        // proporcionais em bins de pouca massa, que é onde a identidade facial reside.
        double chiSquared = 0.0;
        for (int i = 0; i < capturedTemplate.length; i++) {
            int v1 = capturedTemplate[i] & 0xFF;
            int v2 = storedTemplate[i] & 0xFF;
            int sum = v1 + v2;
            if (sum > 0) {
                int diff = v1 - v2;
                chiSquared += (double) (diff * diff) / sum;
            }
        }

        double similarity = Math.exp(-chiSquared / CHI_SQUARED_SCALE);

        // Log de diagnóstico — útil enquanto a calibragem ainda está sendo afinada
        System.out.println(String.format(
            "[Matcher] chi² = %.1f | similarity = %.4f", chiSquared, similarity
        ));

        return Math.max(0.0, Math.min(1.0, similarity));
    }
}
