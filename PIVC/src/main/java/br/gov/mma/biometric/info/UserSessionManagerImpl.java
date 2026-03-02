package br.gov.mma.biometric.info;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.EstadoSessão;
import br.gov.mma.biometric.model.User;

/**
 * Implementação do gerenciador de sessão do usuário.
 */
public class UserSessionManagerImpl implements UserSessionManager {
    
    private EstadoSessão sessaoAtual;
    
    @Override
    public void iniciarSessao(User usuario, AccessLevel nivelConcedido, String token) {
        if (usuario != null && nivelConcedido != null && token != null) {
            this.sessaoAtual = new EstadoSessão(usuario, nivelConcedido, token);
        }
    }
    
    @Override
    public User obterUsuarioAtual() {
        if (temSessaoAtiva()) {
            return sessaoAtual.getUsuarioAutenticado();
        }
        return null;
    }
    
    @Override
    public AccessLevel obterNivelAtual() {
        if (temSessaoAtiva()) {
            return sessaoAtual.getNivelConcedido();
        }
        return null;
    }
    
    @Override
    public String obterTokenAtual() {
        if (temSessaoAtiva()) {
            return sessaoAtual.getTokenSessao();
        }
        return null;
    }
    
    @Override
    public boolean temSessaoAtiva() {
        return sessaoAtual != null && sessaoAtual.isAtivo();
    }
    
    @Override
    public void encerrarSessao() {
        if (sessaoAtual != null) {
            sessaoAtual.desativar();
            sessaoAtual = null;
        }
    }
}
