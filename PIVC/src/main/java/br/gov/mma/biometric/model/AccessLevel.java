package br.gov.mma.biometric.model;

/**
 * Níveis de acesso hierárquicos do sistema de autenticação biométrica.
 * Implementa três níveis: Público (1), Restrito (2) e Confidencial (3).
 * 
 * Requisitos: 2.5
 */
public enum AccessLevel {
    PUBLIC(1, "Acesso Público"),
    RESTRICTED(2, "Acesso Restrito - Diretores"),
    CONFIDENTIAL(3, "Acesso Confidencial - Ministro");
    
    private final int level;
    private final String description;
    
    AccessLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDescription() {
        return description;
    }
}
