package br.gov.mma.biometric;

import br.gov.mma.biometric.model.BiometricData;

/**
 * Interface do scanner biométrico.
 * Responsável por capturar dados biométricos e calcular qualidade da captura.
 * 
 * Valida: Requisitos 1.1, 4.1
 */
public interface BiometricScanner {
    
    /**
     * Captura dados biométricos do usuário.
     * 
     * @return dados biométricos capturados incluindo template, tipo e qualidade
     * @throws BiometricCaptureException se ocorrer erro durante a captura
     */
    BiometricData capture() throws BiometricCaptureException;
    
    /**
     * Calcula a qualidade de um template biométrico.
     * 
     * @param template template biométrico a ser avaliado
     * @return score de qualidade entre 0.0 e 1.0
     */
    double calculateQuality(byte[] template);
}
