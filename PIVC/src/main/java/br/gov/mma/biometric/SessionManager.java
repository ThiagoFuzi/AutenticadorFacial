package br.gov.mma.biometric;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.User;

/**
 * Interface para gerenciamento de sessões e tokens.
 * Responsável por criar e gerenciar tokens de sessão para usuários autenticados.
 * 
 * Valida: Requisitos 7.1, 7.2
 */
public interface SessionManager {
    
    /**
     * Cria uma nova sessão para um usuário autenticado.
     * 
     * @param user usuário autenticado
     * @param grantedLevel nível de acesso concedido
     * @return token de sessão único e não vazio
     */
    String createSession(User user, AccessLevel grantedLevel);
}
