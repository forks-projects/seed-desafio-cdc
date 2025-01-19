package br.com.casadocodigo.casadocodigo.autor;

public class AutorDetalheResponse {
    private String nome;
    private String descricao;

    public AutorDetalheResponse(Autor autor) {
        this.nome = autor.getNome();
        this.descricao = autor.getDescricao();
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
