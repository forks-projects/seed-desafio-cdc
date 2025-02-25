package br.com.casadocodigo.casadocodigo.integration.categoria;

import br.com.casadocodigo.casadocodigo.categoria.NovaCategoriaRequest;

public class NovaCategoriaRequestDataBuilder {
    private String nome = "Categoria Padrão";
    private NovaCategoriaRequest novaCategoriaRequest;

    public NovaCategoriaRequestDataBuilder() {
        this.novaCategoriaRequest = new NovaCategoriaRequest(nome);
    }
    public NovaCategoriaRequestDataBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }
    public NovaCategoriaRequest build() {
        return new NovaCategoriaRequest(nome);
    }

    static public NovaCategoriaRequestDataBuilder umaCategoria() {
        return new NovaCategoriaRequestDataBuilder();
    }

    static public NovaCategoriaRequestDataBuilder umaCategoriaComNomeEmBranco() {
      return new NovaCategoriaRequestDataBuilder().comNome("");
    }

    static public NovaCategoriaRequestDataBuilder umaCategoriaComNomeNulo() {
      return new NovaCategoriaRequestDataBuilder().comNome(null);
    }
}
