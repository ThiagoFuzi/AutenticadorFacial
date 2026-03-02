package br.gov.mma.biometric.ui;

import br.gov.mma.biometric.model.InformaçõesPorNível;
import javax.swing.JPanel;

/**
 * Interface para painel dinâmico de informações.
 */
public interface DynamicInfoPanel {
    
    /**
     * Atualizar painel com informações.
     * 
     * @param informacoes as informações a exibir
     */
    void atualizarComInformacoes(InformaçõesPorNível informacoes);
    
    /**
     * Limpar painel.
     */
    void limpar();
    
    /**
     * Exibir mensagem de sem autenticação.
     */
    void exibirMensagemSemAutenticacao();
    
    /**
     * Obter componente Swing para adicionar à interface.
     * 
     * @return JPanel
     */
    JPanel obterComponenteSwing();
}
