package br.gov.mma.biometric.ui;

import br.gov.mma.biometric.info.UserSessionManager;
import br.gov.mma.biometric.model.InformaçõesPorNível;
import br.gov.mma.biometric.model.Seção;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Implementação do painel dinâmico de informações.
 */
public class DynamicInfoPanelImpl extends JPanel implements DynamicInfoPanel {
    
    private final UserSessionManager gerenciadorSessao;
    private JTextArea areaConteudo;
    private InformaçõesPorNível informacoesAtual;
    
    public DynamicInfoPanelImpl(UserSessionManager gerenciadorSessao) {
        this.gerenciadorSessao = gerenciadorSessao;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        areaConteudo = new JTextArea();
        areaConteudo.setEditable(false);
        areaConteudo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        areaConteudo.setLineWrap(true);
        areaConteudo.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(areaConteudo);
        add(scrollPane, BorderLayout.CENTER);
        
        exibirMensagemSemAutenticacao();
    }
    
    @Override
    public void atualizarComInformacoes(InformaçõesPorNível informacoes) {
        if (informacoes == null) {
            exibirMensagemSemAutenticacao();
            return;
        }
        
        this.informacoesAtual = informacoes;
        
        StringBuilder sb = new StringBuilder();
        
        // Título
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(informacoes.getTitulo()).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        // Conteúdo principal
        sb.append(informacoes.getConteudo()).append("\n\n");
        
        // Seções
        List<Seção> secoes = informacoes.getSecoes();
        for (Seção secao : secoes) {
            sb.append("───────────────────────────────────────────────────────────\n");
            sb.append("▸ ").append(secao.getNome()).append("\n");
            sb.append("───────────────────────────────────────────────────────────\n");
            sb.append(secao.getDescricao()).append("\n\n");
            
            List<String> dados = secao.getDados();
            for (String dado : dados) {
                sb.append("  • ").append(dado).append("\n");
            }
            sb.append("\n");
        }
        
        areaConteudo.setText(sb.toString());
        areaConteudo.setCaretPosition(0);
    }
    
    @Override
    public void limpar() {
        areaConteudo.setText("");
        informacoesAtual = null;
    }
    
    @Override
    public void exibirMensagemSemAutenticacao() {
        String mensagem = "═══════════════════════════════════════════════════════════\n" +
                         "INFORMAÇÕES DO SISTEMA\n" +
                         "═══════════════════════════════════════════════════════════\n\n" +
                         "Aguardando autenticação...\n\n" +
                         "Para visualizar informações específicas, você precisa:\n\n" +
                         "1. Ir para a aba 'Autenticação'\n" +
                         "2. Selecionar o nível de acesso desejado\n" +
                         "3. Clicar em 'Capturar e Autenticar'\n" +
                         "4. Posicionar seu rosto na frente da webcam\n\n" +
                         "Após autenticação bem-sucedida, as informações específicas\n" +
                         "para seu nível de acesso serão exibidas aqui.\n\n" +
                         "NÍVEIS DE ACESSO:\n\n" +
                         "• PÚBLICO (Nível 1)\n" +
                         "  Informações gerais acessíveis a todos os funcionários\n\n" +
                         "• RESTRITO (Nível 2)\n" +
                         "  Informações restritas aos diretores de divisões\n\n" +
                         "• CONFIDENCIAL (Nível 3)\n" +
                         "  Informações confidenciais acessíveis apenas ao Ministro";
        
        areaConteudo.setText(mensagem);
        areaConteudo.setCaretPosition(0);
    }
    
    @Override
    public JPanel obterComponenteSwing() {
        return this;
    }
}
