package br.gov.mma.biometric;

import br.gov.mma.biometric.model.BiometricData;
import br.gov.mma.biometric.model.BiometricType;
import java.util.Random;

/**
 * Implementação de scanner de reconhecimento facial.
 * Esta é uma implementação simulada para fins de demonstração que gera templates faciais aleatórios.
 * 
 * Em um sistema de produção, esta classe se integraria com hardware de câmera e bibliotecas
 * de reconhecimento facial para capturar e processar imagens faciais reais.
 * 
 * Valida: Requisitos 1.1, 9.2
 */
public class FacialRecognitionScanner implements BiometricScanner {
    
    private static final int FACIAL_TEMPLATE_SIZE = 512; // Tamanho típico de template facial em bytes
    private static final double MIN_QUALITY = 0.5;
    private static final double MAX_QUALITY = 1.0;
    private final Random random;
    
    /**
     * Construtor padrão que inicializa o scanner com gerador aleatório.
     */
    public FacialRecognitionScanner() {
        this.random = new Random();
    }
    
    /**
     * Construtor para testes que permite injetar uma seed para resultados reproduzíveis.
     * 
     * @param seed seed para o gerador de números aleatórios
     */
    public FacialRecognitionScanner(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Captura dados biométricos faciais simulados.
     * 
     * Gera um template facial aleatório de 512 bytes e calcula sua qualidade.
     * Em um sistema real, isso capturaria uma imagem facial através de uma câmera,
     * extrairia características faciais (pontos de referência, distâncias entre características,
     * texturas, etc.) e as codificaria em um template.
     * 
     * @return dados biométricos contendo template facial, tipo e qualidade
     * @throws BiometricCaptureException se ocorrer erro durante a captura simulada
     */
    @Override
    public BiometricData capture() throws BiometricCaptureException {
        try {
            // Simular tempo de captura e processamento
            Thread.sleep(100 + random.nextInt(200)); // 100-300ms
            
            // Gerar template facial aleatório
            byte[] template = new byte[FACIAL_TEMPLATE_SIZE];
            random.nextBytes(template);
            
            // Calcular qualidade do template
            double quality = calculateQuality(template);
            
            // Criar e retornar dados biométricos
            return new BiometricData(template, BiometricType.FACIAL_RECOGNITION, quality);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BiometricCaptureException("Captura facial interrompida", e);
        } catch (Exception e) {
            throw new BiometricCaptureException("Erro ao capturar dados faciais: " + e.getMessage(), e);
        }
    }
    
    /**
     * Calcula a qualidade de um template facial.
     * 
     * A qualidade é baseada em vários fatores simulados:
     * - Variância dos bytes (maior variância = melhor qualidade)
     * - Distribuição de valores (distribuição uniforme = melhor qualidade)
     * - Presença de padrões (menos padrões repetitivos = melhor qualidade)
     * 
     * Em um sistema real, a qualidade seria baseada em:
     * - Iluminação adequada da face
     * - Posição e ângulo da face
     * - Nitidez da imagem
     * - Detecção clara de características faciais
     * - Ausência de oclusões (óculos escuros, máscaras, etc.)
     * 
     * @param template template facial a ser avaliado
     * @return score de qualidade entre 0.5 e 1.0
     */
    @Override
    public double calculateQuality(byte[] template) {
        if (template == null || template.length == 0) {
            return 0.0;
        }
        
        // Fator 1: Calcular variância dos bytes (normalizado)
        double mean = 0.0;
        for (byte b : template) {
            mean += (b & 0xFF);
        }
        mean /= template.length;
        
        double variance = 0.0;
        for (byte b : template) {
            double diff = (b & 0xFF) - mean;
            variance += diff * diff;
        }
        variance /= template.length;
        
        // Normalizar variância (máximo teórico é ~5461 para bytes)
        double varianceFactor = Math.min(variance / 5461.0, 1.0);
        
        // Fator 2: Avaliar distribuição de valores (histograma simplificado)
        int[] histogram = new int[256];
        for (byte b : template) {
            histogram[b & 0xFF]++;
        }
        
        // Calcular entropia simplificada
        double entropy = 0.0;
        for (int count : histogram) {
            if (count > 0) {
                double probability = (double) count / template.length;
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }
        }
        
        // Normalizar entropia (máximo é 8 bits para 256 valores)
        double entropyFactor = Math.min(entropy / 8.0, 1.0);
        
        // Fator 3: Detectar padrões repetitivos (penalizar sequências idênticas)
        int repetitions = 0;
        for (int i = 1; i < template.length; i++) {
            if (template[i] == template[i - 1]) {
                repetitions++;
            }
        }
        double repetitionFactor = 1.0 - ((double) repetitions / template.length);
        
        // Combinar fatores com pesos
        double rawQuality = (varianceFactor * 0.3) + (entropyFactor * 0.5) + (repetitionFactor * 0.2);
        
        // Escalar para o intervalo [MIN_QUALITY, MAX_QUALITY]
        double quality = MIN_QUALITY + (rawQuality * (MAX_QUALITY - MIN_QUALITY));
        
        // Garantir que está no intervalo válido
        return Math.max(MIN_QUALITY, Math.min(MAX_QUALITY, quality));
    }
}
