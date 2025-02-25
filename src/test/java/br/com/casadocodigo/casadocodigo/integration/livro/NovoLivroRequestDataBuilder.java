package br.com.casadocodigo.casadocodigo.integration.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.livro.NovoLivroRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovoLivroRequestDataBuilder {

    private String titulo = "Título Padrão";
    private String resumo = "Resumo Padrão";
    private String sumario = "Sumário Padrão";
    private BigDecimal preco = BigDecimal.valueOf(30.00);
    private int numeroPaginas = 150;
    private String isbn = "123-456-789";
    private LocalDate dataPublicacao = LocalDate.now().plusMonths(1);
    private Long idCategoria = 1L;
    private Long idAutor = 1L;

    public NovoLivroRequestDataBuilder() {
    }

    public static NovoLivroRequestDataBuilder umLivro() {
        return new NovoLivroRequestDataBuilder();
    }

    public static NovoLivroRequestDataBuilder umLivroSemTitulo(Categoria categoria, Autor autor) {
        return new NovoLivroRequestDataBuilder().comTitulo("")
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId());
    }

    public static NovoLivroRequestDataBuilder umLivroSemTituloCategoriaAutor() {
        return new NovoLivroRequestDataBuilder().comTitulo("");
    }

    public static NovoLivroRequestDataBuilder umLivroCustomizado(Categoria categoria, Autor autor) {
        return new NovoLivroRequestDataBuilder()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId());
    }

    public static NovoLivroRequestDataBuilder umLivroComResumoComMaisDe500Caracteres(Categoria categoria, Autor autor) {
        return new NovoLivroRequestDataBuilder()
                .comResumo("a".repeat(501))
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId());
    }

    public NovoLivroRequestDataBuilder comTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public NovoLivroRequestDataBuilder comResumo(String resumo) {
        this.resumo = resumo;
        return this;
    }

    public NovoLivroRequestDataBuilder comSumario(String sumario) {
        this.sumario = sumario;
        return this;
    }

    public NovoLivroRequestDataBuilder comPreco(BigDecimal preco) {
        this.preco = preco;
        return this;
    }

    public NovoLivroRequestDataBuilder comNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
        return this;
    }

    public NovoLivroRequestDataBuilder comIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public NovoLivroRequestDataBuilder comDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
        return this;
    }

    public NovoLivroRequestDataBuilder comIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
        return this;
    }

    public NovoLivroRequestDataBuilder comIdAutor(Long idAutor) {
        this.idAutor = idAutor;
        return this;
    }

    public NovoLivroRequest build() {
        return new NovoLivroRequest(
                titulo,
                resumo,
                sumario,
                preco,
                numeroPaginas,
                isbn,
                dataPublicacao,
                idCategoria,
                idAutor
        );
    }
}
