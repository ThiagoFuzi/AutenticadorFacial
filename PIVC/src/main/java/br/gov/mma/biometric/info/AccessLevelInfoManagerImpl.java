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
        inicializarNivelPublico();
        inicializarNivelRestrito();
        inicializarNivelConfidencial();
    }

    /**
     * NÍVEL 1 — PÚBLICO
     * Informações educativas e legais acessíveis a qualquer funcionário do MMA.
     * Não contém dados nominais de propriedades, pessoas ou operações em curso.
     */
    private void inicializarNivelPublico() {
        InformaçõesPorNível info = new InformaçõesPorNível(
            AccessLevel.PUBLIC,
            "Nível 1 — Informações Públicas",
            "Conteúdo educativo, legislação e canais de denúncia disponíveis a todos."
        );

        Seção legislacao = new Seção(
            "Legislação Aplicável",
            "Marco normativo brasileiro sobre agrotóxicos e proteção de recursos hídricos."
        );
        legislacao.adicionarDados(Arrays.asList(
            "Lei nº 7.802/1989 — Lei dos Agrotóxicos (registro, produção, uso e descarte)",
            "Decreto nº 4.074/2002 — Regulamenta a Lei nº 7.802/1989",
            "Lei nº 9.605/1998 — Crimes Ambientais (uso e armazenamento irregular)",
            "Lei nº 9.433/1997 — Política Nacional de Recursos Hídricos",
            "Resoluções CONAMA 357/2005 e 396/2008 — Qualidade de águas superficiais e subterrâneas"
        ));
        info.adicionarSecao(legislacao);

        Seção impactos = new Seção(
            "Impactos Ambientais Conhecidos",
            "Por que certos agrotóxicos são proibidos no Brasil."
        );
        impactos.adicionarDados(Arrays.asList(
            "Bioacumulação em peixes e cadeia alimentar (rios e estuários)",
            "Contaminação de lençóis freáticos por percolação no solo",
            "Mortandade de polinizadores (abelhas) e perda de biodiversidade",
            "Carcinogenicidade e toxicidade neurológica documentadas para humanos",
            "Persistência ambiental superior a 10 anos para organoclorados"
        ));
        info.adicionarSecao(impactos);

        Seção canais = new Seção(
            "Como Denunciar",
            "Canais oficiais para reporte de uso irregular de agrotóxicos."
        );
        canais.adicionarDados(Arrays.asList(
            "Linha Verde IBAMA: 0800 61 8080 (24h, sigilo garantido)",
            "Disque Denúncia Ambiental: 181",
            "Ouvidoria do MMA: ouvidoria@mma.gov.br",
            "Portal Ibama Cidadão: www.gov.br/ibama (denúncia online)"
        ));
        info.adicionarSecao(canais);

        Seção estatisticasGerais = new Seção(
            "Estatísticas Públicas — Exercício Vigente",
            "Indicadores agregados divulgados em relatórios anuais."
        );
        estatisticasGerais.adicionarDados(Arrays.asList(
            "Inspeções concluídas e publicadas: 2.847",
            "Autuações com trânsito em julgado: 1.203",
            "Toneladas de produtos irregulares apreendidas: 187,4",
            "Municípios com algum auto de infração lavrado: 412"
        ));
        info.adicionarSecao(estatisticasGerais);

        informacoesPorNivel.put(AccessLevel.PUBLIC, info);
    }

    /**
     * NÍVEL 2 — RESTRITO (Diretores de Divisão)
     * Dados operacionais agregados por região e substância, campanhas de
     * fiscalização em curso e relatórios técnicos das divisões. Não inclui
     * identificação nominal de propriedades nem operações sigilosas.
     */
    private void inicializarNivelRestrito() {
        InformaçõesPorNível info = new InformaçõesPorNível(
            AccessLevel.RESTRICTED,
            "Nível 2 — Informações Restritas (Diretores de Divisão)",
            "Dados operacionais regionais, campanhas ativas e laudos técnicos."
        );

        Seção substancias = new Seção(
            "Substâncias Proibidas Sob Monitoramento",
            "Compostos com maior incidência de detecção em amostras de campo (2024-2025)."
        );
        substancias.adicionarDados(Arrays.asList(
            "Paraquate — banido desde 2020 (RDC 177/2017 da ANVISA)",
            "Endossulfam — banido desde 2013, ainda detectado em apreensões",
            "Carbofurano — banido desde 2017, contrabando da fronteira sul",
            "Lactofeno e Tricloroetileno — em processo de reavaliação toxicológica",
            "Glifosato — uso permitido mas sob monitoramento ampliado por suspeita"
        ));
        info.adicionarSecao(substancias);

        Seção contaminacao = new Seção(
            "Contaminação Detectada por Bacia Hidrográfica",
            "Pontos de coleta acima do limite CONAMA 357/2005 — últimos 12 meses."
        );
        contaminacao.adicionarDados(Arrays.asList(
            "Bacia do Paraná: 47 pontos críticos (concentração média 2,3x o limite)",
            "Bacia do São Francisco: 31 pontos críticos (Carbofurano em afluentes)",
            "Bacia Amazônica: 12 pontos (Glifosato e Atrazina em igarapés do MT/PA)",
            "Bacia do Paraíba do Sul: 18 pontos críticos (foz com poluição costeira)",
            "Aquífero Guarani: 6 poços com detecção de organoclorados (RS e PR)"
        ));
        info.adicionarSecao(contaminacao);

        Seção campanhas = new Seção(
            "Campanhas de Fiscalização em Curso",
            "Operações conjuntas com IBAMA, PF e Polícias Ambientais Estaduais."
        );
        campanhas.adicionarDados(Arrays.asList(
            "Operação Guardiões do Bioma — Cerrado (GO, TO, MA, PI, BA) — 124 propriedades alvo",
            "Operação Maré Limpa — Costa NE (BA, PE, CE) — foco em escoamento agrícola",
            "Operação Aquífero — RS/SC — análise de poços artesianos em região vinícola",
            "Operação Fronteira Sul — RS/PR — contrabando do Paraguai e Argentina"
        ));
        info.adicionarSecao(campanhas);

        Seção divisoes = new Seção(
            "Desempenho por Divisão (Trimestre Atual)",
            "Indicadores internos compartilhados entre diretores."
        );
        divisoes.adicionarDados(Arrays.asList(
            "Divisão de Fiscalização: 234 inspeções, 167 autuações, R$ 12,4 mi em multas",
            "Divisão de Análise Laboratorial: 1.058 amostras processadas, 412 positivas",
            "Divisão de Inteligência Ambiental: 23 investigações abertas, 9 concluídas",
            "Divisão Jurídica: 89 processos administrativos, 14 transitados em julgado"
        ));
        info.adicionarSecao(divisoes);

        informacoesPorNivel.put(AccessLevel.RESTRICTED, info);
    }

    /**
     * NÍVEL 3 — CONFIDENCIAL (Ministro)
     * Dossiês nominais de propriedades sob investigação, operações sigilosas
     * em andamento, ramificações políticas e empresariais, articulação
     * internacional. Sigilo absoluto: vazamento configura crime (Lei 12.527/2011).
     */
    private void inicializarNivelConfidencial() {
        InformaçõesPorNível info = new InformaçõesPorNível(
            AccessLevel.CONFIDENTIAL,
            "Nível 3 — Confidencial (Acesso Exclusivo do Ministro)",
            "Dossiês nominais, operações sigilosas e articulação política. "
            + "ATENÇÃO: vazamento sujeito à Lei nº 12.527/2011."
        );

        Seção dossies = new Seção(
            "Dossiês de Propriedades Sob Investigação Sigilosa",
            "Identificação nominal, geolocalização e substâncias detectadas. "
            + "Estes dados ainda não foram judicializados."
        );
        dossies.adicionarDados(Arrays.asList(
            "[CASO 2025-014] Fazenda Boa Sorte — Sorriso/MT — Paraquate em poços (5,8x limite)",
            "[CASO 2025-017] Agropecuária Vale Verde — Luís Eduardo Magalhães/BA — Carbofurano",
            "[CASO 2025-021] Grupo Aliança Agro — 4 propriedades (GO/MT/MS) — investigação federal",
            "[CASO 2025-024] Fazenda São Marcos — Ponta Porã/MS — rota de contrabando do Paraguai",
            "[CASO 2025-029] Cooperativa Frutos do Cerrado — Barreiras/BA — 12 sócios investigados"
        ));
        info.adicionarSecao(dossies);

        Seção operacoes = new Seção(
            "Operações Sigilosas em Curso",
            "Coordenação com PF, Receita Federal e órgãos internacionais. "
            + "Datas de deflagração estritamente reservadas."
        );
        operacoes.adicionarDados(Arrays.asList(
            "Operação RAÍZES PROFUNDAS — desarticulação de rede de revenda clandestina (8 estados)",
            "Operação ÁGUA TURVA — investigação de contaminação proposital de mananciais por concorrência agrícola",
            "Operação CORREDOR — interceptação de contrabando via fronteira BR-PY (em coordenação com SENAD)",
            "Operação FAROL — auditoria sigilosa em laboratórios privados suspeitos de adulterar laudos"
        ));
        info.adicionarSecao(operacoes);

        Seção articulacao = new Seção(
            "Articulação Política e Internacional",
            "Pendências sensíveis com Casa Civil, Congresso e parceiros externos."
        );
        articulacao.adicionarDados(Arrays.asList(
            "Pressão parlamentar para flexibilizar reavaliação do Glifosato (FPA — 287 votos)",
            "Acordo bilateral BR-UY em negociação para rastreio de embarques fluviais (Bacia do Prata)",
            "Pendência com EPA (EUA) sobre exportação de soja com resíduos acima do MRL europeu",
            "Posicionamento Casa Civil: priorizar Cerrado no próximo PPA por pressão do agronegócio"
        ));
        info.adicionarSecao(articulacao);

        Seção contaminacaoCritica = new Seção(
            "Contaminação Crítica Não Divulgada",
            "Pontos onde a divulgação imediata causaria pânico ou prejuízo econômico "
            + "regional. Comunicação sob coordenação da assessoria."
        );
        contaminacaoCritica.adicionarDados(Arrays.asList(
            "Rio Piracicaba (SP) — concentração de Atrazina 9,4x o limite, captação de 4 municípios",
            "Lagoa do Peri (SC) — eutrofização severa por escoamento agrícola, abastecimento ameaçado",
            "Reservatório de Furnas (MG) — detecção pontual de Endossulfam (origem em investigação)",
            "Estuário do Rio Capibaribe (PE) — bioacumulação em mariscos, risco à pesca artesanal"
        ));
        info.adicionarSecao(contaminacaoCritica);

        Seção decisoes = new Seção(
            "Decisões Estratégicas Pendentes",
            "Itens que aguardam manifestação ministerial direta."
        );
        decisoes.adicionarDados(Arrays.asList(
            "Aprovação do PNCA 2026-2030 (Plano Nacional de Controle de Agrotóxicos)",
            "Realocação de R$ 34 milhões para ampliação da rede laboratorial Norte/Nordeste",
            "Decisão sobre divulgação pública dos casos 2025-014 e 2025-021 (impacto eleitoral)",
            "Nomeação de novo diretor para a Divisão de Inteligência Ambiental"
        ));
        info.adicionarSecao(decisoes);

        informacoesPorNivel.put(AccessLevel.CONFIDENTIAL, info);
    }
}
