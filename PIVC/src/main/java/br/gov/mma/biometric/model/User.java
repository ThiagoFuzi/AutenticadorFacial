package br.gov.mma.biometric.model;

/**
 * Usuário do sistema de autenticação biométrica.
 * Contém informações de identificação, credenciais biométricas e nível de acesso.
 * 
 * Requisitos: 9.4
 */
public class User {
    private final String userId;
    private final String name;
    private final String role;
    private final AccessLevel maxAccessLevel;
    private final byte[] storedBiometricTemplate;
    private final BiometricType biometricType;
    private final boolean isActive;
    
    public User(String userId, String name, String role, AccessLevel maxAccessLevel,
                byte[] storedBiometricTemplate, BiometricType biometricType, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.maxAccessLevel = maxAccessLevel;
        this.storedBiometricTemplate = storedBiometricTemplate;
        this.biometricType = biometricType;
        this.isActive = isActive;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getRole() {
        return role;
    }
    
    public AccessLevel getMaxAccessLevel() {
        return maxAccessLevel;
    }
    
    public byte[] getStoredBiometricTemplate() {
        return storedBiometricTemplate;
    }
    
    public BiometricType getBiometricType() {
        return biometricType;
    }
    
    public boolean isActive() {
        return isActive;
    }
}
