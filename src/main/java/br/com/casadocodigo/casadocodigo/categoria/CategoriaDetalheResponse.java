package br.com.casadocodigo.casadocodigo.categoria;

public class CategoriaDetalheResponse {
    private String nome;

    public CategoriaDetalheResponse(Categoria categoria) {
        this.nome = categoria.getNome();
    }

    public String getNome() {
        return nome;
    }
}
