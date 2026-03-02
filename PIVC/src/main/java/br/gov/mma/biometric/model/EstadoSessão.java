package br.gov.mma.biometric.model;

import java.time.LocalDateTime;

/**
 * Representa o estado da sessão do usuário autenticado.
 */
public class EstadoSessão {
    private final User usuarioAutenticado;
    private final AccessLevel nivelConcedido;
    private final String tokenSessao;
    private final LocalDateTime horaAutenticacao;
    private boolean ativo;
    
    public EstadoSessão(User usuarioAutenticado, AccessLevel nivelConcedido, String tokenSessao) {
        this.usuarioAutenticado = usuarioAutenticado;
        this.nivelConcedido = nivelConcedido;
        this.tokenSessao = tokenSessao;
        this.horaAutenticacao = LocalDateTime.now();
        this.ativo = true;
    }
    
    public User getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    public AccessLevel getNivelConcedido() {
        return nivelConcedido;
    }
    
    public String getTokenSessao() {
        return tokenSessao;
    }
    
    public LocalDateTime getHoraAutenticacao() {
        return horaAutenticacao;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void desativar() {
        this.ativo = false;
    }
}
