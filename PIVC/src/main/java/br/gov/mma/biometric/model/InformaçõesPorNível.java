package br.gov.mma.biometric.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa informações específicas para um nível de acesso.
 */
public class InformaçõesPorNível {
    private final AccessLevel nivelAcesso;
    private final String titulo;
    private final String conteudo;
    private final List<Seção> secoes;
    private final LocalDateTime dataAtualizacao;
    
    public InformaçõesPorNível(AccessLevel nivelAcesso, String titulo, String conteudo) {
        this.nivelAcesso = nivelAcesso;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.secoes = new ArrayList<>();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public AccessLevel getNivelAcesso() {
        return nivelAcesso;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getConteudo() {
        return conteudo;
    }
    
    public List<Seção> getSecoes() {
        return new ArrayList<>(secoes);
    }
    
    public void adicionarSecao(Seção secao) {
        if (secao != null) {
            secoes.add(secao);
        }
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}
