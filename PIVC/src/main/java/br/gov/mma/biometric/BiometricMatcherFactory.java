package br.gov.mma.biometric;

import br.gov.mma.biometric.model.BiometricType;

/**
 * Factory para criar instâncias apropriadas de BiometricMatcher baseado no tipo biométrico.
 * Implementa o padrão Factory para encapsular a lógica de criação de matchers.
 * 
 * Valida: Requisitos 9.5
 */
public class BiometricMatcherFactory {
    
    /**
     * Retorna uma instância de BiometricMatcher apropriada para o tipo biométrico especificado.
     * 
     * @param type tipo biométrico para o qual criar o matcher
     * @return instância de BiometricMatcher apropriada para o tipo
     * @throws IllegalArgumentException se o tipo biométrico não é suportado
     */
    public BiometricMatcher getMatcher(BiometricType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tipo biométrico não pode ser nulo");
        }
        
        switch (type) {
            case FACIAL_RECOGNITION:
                return new FacialRecognitionMatcher();
            
            case FINGERPRINT:
            case IRIS_SCAN:
                throw new UnsupportedOperationException(
                    "Tipo biométrico " + type + " ainda não implementado"
                );
            
            default:
                throw new IllegalArgumentException("Tipo biométrico desconhecido: " + type);
        }
    }
}
