package br.com.casadocodigo.casadocodigo.livro;

public class LivroEspecificoResponse {
    private Long id;
    private String titulo;

    public LivroEspecificoResponse(Livro livro) {
        this.id = livro.getId();
        this.titulo = livro.getTitulo();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}
