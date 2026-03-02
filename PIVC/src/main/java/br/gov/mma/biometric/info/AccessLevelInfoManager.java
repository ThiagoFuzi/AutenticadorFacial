package br.gov.mma.biometric.info;

import br.gov.mma.biometric.model.AccessLevel;
import br.gov.mma.biometric.model.InformaçõesPorNível;
import br.gov.mma.biometric.model.Seção;
import java.util.List;

/**
 * Interface para gerenciar informações por nível de acesso.
 */
public interface AccessLevelInfoManager {
    
    /**
     * Obter todas as informações para um nível de acesso.
     * 
     * @param nivelAcesso o nível de acesso
     * @return InformaçõesPorNível ou null se não encontrado
     */
    InformaçõesPorNível obterInformacoes(AccessLevel nivelAcesso);
    
    /**
     * Obter seções específicas de um nível.
     * 
     * @param nivelAcesso o nível de acesso
     * @return lista de seções (nunca null)
     */
    List<Seção> obterSecoes(AccessLevel nivelAcesso);
    
    /**
     * Obter conteúdo de uma seção específica.
     * 
     * @param nivelAcesso o nível de acesso
     * @param nomeSecao o nome da seção
     * @return conteúdo da seção ou null se não encontrado
     */
    String obterConteudo(AccessLevel nivelAcesso, String nomeSecao);
    
    /**
     * Atualizar informações de um nível.
     * 
     * @param nivelAcesso o nível de acesso
     * @param informacoes as novas informações
     */
    void atualizarInformacoes(AccessLevel nivelAcesso, InformaçõesPorNível informacoes);
    
    /**
     * Inicializar informações padrão para todos os níveis.
     */
    void inicializarInformacoesPadrão();
}
