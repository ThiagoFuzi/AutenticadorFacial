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
                         "SISTEMA DE CONTROLE DE AGROTÓXICOS PROIBIDOS — MMA\n" +
                         "═══════════════════════════════════════════════════════════\n\n" +
                         "Base de dados sobre propriedades rurais com uso de agrotóxicos\n" +
                         "proibidos e seus impactos em lençóis freáticos, rios e mares.\n\n" +
                         "Aguardando autenticação biométrica...\n\n" +
                         "Para acessar:\n" +
                         "  1. Aba 'Autenticação'\n" +
                         "  2. Selecione o nível solicitado\n" +
                         "  3. Clique em 'Capturar e Autenticar'\n" +
                         "  4. Posicione seu rosto na webcam\n\n" +
                         "NÍVEIS DE ACESSO:\n\n" +
                         "• NÍVEL 1 — PÚBLICO\n" +
                         "  Legislação, impactos ambientais, canais de denúncia e\n" +
                         "  estatísticas agregadas. Aberto a todos os funcionários.\n\n" +
                         "• NÍVEL 2 — RESTRITO (Diretores de Divisão)\n" +
                         "  Substâncias monitoradas, contaminação por bacia hidrográfica,\n" +
                         "  campanhas de fiscalização em curso e desempenho por divisão.\n\n" +
                         "• NÍVEL 3 — CONFIDENCIAL (Ministro)\n" +
                         "  Dossiês nominais de propriedades sob investigação, operações\n" +
                         "  sigilosas, articulação política e decisões estratégicas.";

        areaConteudo.setText(mensagem);
        areaConteudo.setCaretPosition(0);
    }
    
    @Override
    public JPanel obterComponenteSwing() {
        return this;
    }
}
