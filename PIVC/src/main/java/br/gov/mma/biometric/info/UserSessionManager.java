package br.gov.mma.biometric.info;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.User;

/**
 * Interface para gerenciar a sessão do usuário autenticado.
 */
public interface UserSessionManager {
    
    /**
     * Iniciar nova sessão com usuário autenticado.
     * 
     * @param usuario o usuário autenticado
     * @param nivelConcedido o nível de acesso concedido
     * @param token o token de sessão
     */
    void iniciarSessao(User usuario, AccessLevel nivelConcedido, String token);
    
    /**
     * Obter usuário da sessão atual.
     * 
     * @return User ou null se sem sessão ativa
     */
    User obterUsuarioAtual();
    
    /**
     * Obter nível de acesso da sessão atual.
     * 
     * @return AccessLevel ou null se sem sessão ativa
     */
    AccessLevel obterNivelAtual();
    
    /**
     * Obter token da sessão atual.
     * 
     * @return token ou null se sem sessão ativa
     */
    String obterTokenAtual();
    
    /**
     * Verificar se há sessão ativa.
     * 
     * @return true se sessão ativa, false caso contrário
     */
    boolean temSessaoAtiva();
    
    /**
     * Encerrar sessão atual.
     */
    void encerrarSessao();
}
