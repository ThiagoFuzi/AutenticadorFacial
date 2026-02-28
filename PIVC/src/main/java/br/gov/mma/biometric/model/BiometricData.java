package br.gov.mma.biometric.model;

import java.time.LocalDateTime;

/**
 * Dados biométricos capturados do usuário.
 * Contém o template biométrico, tipo, timestamp de captura e qualidade.
 * 
 * Requisitos: 9.4
 */
public class BiometricData {
    private final byte[] template;
    private final BiometricType type;
    private final LocalDateTime captureTime;
    private final double quality;
    
    public BiometricData(byte[] template, BiometricType type, double quality) {
        this.template = template;
        this.type = type;
        this.captureTime = LocalDateTime.now();
        this.quality = quality;
    }
    
    public byte[] getTemplate() {
        return template;
    }
    
    public BiometricType getType() {
        return type;
    }
    
    public LocalDateTime getCaptureTime() {
        return captureTime;
    }
    
    public double getQuality() {
        return quality;
    }
}
