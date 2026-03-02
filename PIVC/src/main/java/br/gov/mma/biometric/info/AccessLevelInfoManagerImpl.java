package br.gov.mma.biometric.info;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.InformaçõesPorNível;
import br.gov.mma.biometric.model.Seção;
import java.util.*;

/**
 * Implementação do gerenciador de informações por nível de acesso.
 */
public class AccessLevelInfoManagerImpl implements AccessLevelInfoManager {
    
    private final Map<AccessLevel, InformaçõesPorNível> informacoesPorNivel;
    
    public AccessLevelInfoManagerImpl() {
        this.informacoesPorNivel = new HashMap<>();
        inicializarInformacoesPadrão();
    }
    
    @Override
    public InformaçõesPorNível obterInformacoes(AccessLevel nivelAcesso) {
        if (nivelAcesso == null) {
            return null;
        }
        return informacoesPorNivel.get(nivelAcesso);
    }
    
    @Override
    public List<Seção> obterSecoes(AccessLevel nivelAcesso) {
        InformaçõesPorNível info = obterInformacoes(nivelAcesso);
        if (info == null) {
            return new ArrayList<>();
        }
        return info.getSecoes();
    }
    
    @Override
    public String obterConteudo(AccessLevel nivelAcesso, String nomeSecao) {
        if (nivelAcesso == null || nomeSecao == null) {
            return null;
        }
        
        List<Seção> secoes = obterSecoes(nivelAcesso);
        return secoes.stream()
            .filter(s -> s.getNome().equals(nomeSecao))
            .map(Seção::getDescricao)
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public void atualizarInformacoes(AccessLevel nivelAcesso, InformaçõesPorNível informacoes) {
        if (nivelAcesso != null && informacoes != null) {
            informacoesPorNivel.put(nivelAcesso, informacoes);
        }
    }
    
    @Override
    public void inicializarInformacoesPadrão() {
        // Informações PUBLIC
        InformaçõesPorNível infoPublic = new InformaçõesPorNível(
            AccessLevel.PUBLIC,
            "Informações Públicas do Sistema",
            "Informações gerais acessíveis a todos os funcionários"
        );
        
        Seção secao1 = new Seção("Sobre o Sistema", "Informações gerais sobre o PIVC");
        secao1.adicionarDados(Arrays.asList(
            "Sistema de Autenticação Biométrica PIVC",
            "Ministério do Meio Ambiente",
            "Versão 1.0",
            "Desenvolvido em Java Swing"
        ));
        infoPublic.adicionarSecao(secao1);
        
        Seção secao2 = new Seção("Estatísticas Gerais", "Dados públicos do sistema");
        secao2.adicionarDados(Arrays.asList(
            "Total de Usuários: 150",
            "Autenticações Hoje: 45",
            "Taxa de Sucesso: 98.5%"
        ));
        infoPublic.adicionarSecao(secao2);
        
        informacoesPorNivel.put(AccessLevel.PUBLIC, infoPublic);
        
        // Informações RESTRICTED
        InformaçõesPorNível infoRestricted = new InformaçõesPorNível(
            AccessLevel.RESTRICTED,
            "Informações Restritas - Diretores",
            "Informações sobre divisões e estatísticas detalhadas"
        );
        
        Seção secao3 = new Seção("Divisões do Ministério", "Estrutura organizacional");
        secao3.adicionarDados(Arrays.asList(
            "Divisão de Fiscalização: 25 funcionários",
            "Divisão de Análise: 18 funcionários",
            "Divisão de Compliance: 12 funcionários"
        ));
        infoRestricted.adicionarSecao(secao3);
        
        Seção secao4 = new Seção("Relatórios de Divisão", "Desempenho por divisão");
        secao4.adicionarDados(Arrays.asList(
            "Fiscalização: 234 inspeções realizadas",
            "Análise: 156 amostras processadas",
            "Compliance: 89 auditorias completadas"
        ));
        infoRestricted.adicionarSecao(secao4);
        
        informacoesPorNivel.put(AccessLevel.RESTRICTED, infoRestricted);
        
        // Informações CONFIDENTIAL
        InformaçõesPorNível infoConfidential = new InformaçõesPorNível(
            AccessLevel.CONFIDENTIAL,
            "Informações Confidenciais - Ministro",
            "Informações estratégicas e relatórios confidenciais"
        );
        
        Seção secao5 = new Seção("Relatórios Estratégicos", "Análise estratégica do ministério");
        secao5.adicionarDados(Arrays.asList(
            "Propriedades com Agrotóxicos Proibidos: 1.234",
            "Multas Aplicadas: R$ 45.678.900",
            "Processos em Andamento: 567"
        ));
        infoConfidential.adicionarSecao(secao5);
        
        Seção secao6 = new Seção("Decisões Estratégicas", "Planos e decisões de alto nível");
        secao6.adicionarDados(Arrays.asList(
            "Plano de Ação 2024: Aumentar fiscalização em 40%",
            "Orçamento Aprovado: R$ 120 milhões",
            "Metas: Reduzir uso de agrotóxicos em 25%"
        ));
        infoConfidential.adicionarSecao(secao6);
        
        informacoesPorNivel.put(AccessLevel.CONFIDENTIAL, infoConfidential);
    }
}
