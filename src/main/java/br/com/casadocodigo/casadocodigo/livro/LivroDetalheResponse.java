package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.AutorDetalheResponse;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaDetalheResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LivroDetalheResponse {
    private String titulo;
    private String resumo;
    private String sumario;
    private BigDecimal preco;
    private int numeroPaginas;
    private String  isbn;
    private LocalDate dataPublicacao;
    private AutorDetalheResponse autor;
    private CategoriaDetalheResponse categoria;

    public LivroDetalheResponse(Livro livro) {
        this.titulo = livro.getTitulo();
        this.resumo = livro.getResumo();
        this.sumario = livro.getSumario();
        this.preco = livro.getPreco();
        this.numeroPaginas = livro.getNumeroPaginas();
        this.isbn = livro.getIsbn();
        this.dataPublicacao = livro.getDataPublicacao();
        this.autor = new AutorDetalheResponse(livro.getAutor());
        this.categoria = new CategoriaDetalheResponse(livro.getCategoria());
    }

    public String getTitulo() {
        return titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public String getSumario() {
        return sumario;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public AutorDetalheResponse getAutor() {
        return autor;
    }

    public CategoriaDetalheResponse getCategoria() {
        return categoria;
    }
}
