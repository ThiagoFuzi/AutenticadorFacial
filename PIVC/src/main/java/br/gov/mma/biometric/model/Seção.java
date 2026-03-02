package br.gov.mma.biometric.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma seção dentro de informações de um nível de acesso.
 */
public class Seção {
    private final String nome;
    private final String descricao;
    private final List<String> dados;
    
    public Seção(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dados = new ArrayList<>();
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public List<String> getDados() {
        return new ArrayList<>(dados);
    }
    
    public void adicionarDado(String dado) {
        if (dado != null && !dado.trim().isEmpty()) {
            dados.add(dado);
        }
    }
    
    public void adicionarDados(List<String> novosDados) {
        if (novosDados != null) {
            novosDados.forEach(this::adicionarDado);
        }
    }
}
