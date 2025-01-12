package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovoLivroRequest {
    @NotBlank
    @ValorUnico(classeDaEntidade = Livro.class, nomeDoCampo = "titulo", message = "já está cadastrado")
    private String titulo;

    @NotBlank
    @Length(max = 500)
    @Lob
    private String resumo;

    @NotBlank
    private String sumario;

    @Min(20)
    @NotNull
    private BigDecimal preco;

    @Min(100)
    private int numeroPaginas;

    @NotBlank
    @ValorUnico(classeDaEntidade = Livro.class, nomeDoCampo = "isbn", message = "já está cadastrado")
    private String  isbn;

    @Future
    @NotNull
    private LocalDate dataPublicacao;

    @NotNull
    private Long idCategoria;

    @NotNull
    private Long idAutor;

    public NovoLivroRequest(@NotBlank String titulo,
                            @Length(max = 500) String resumo,
                            @NotBlank String sumario,
                            @NotNull @Min(20) BigDecimal preco,
                            @Min(100) int numeroPaginas,
                            @NotBlank String isbn,
                            @NotNull @Future LocalDate dataPublicacao,
                            @NotNull Long idCategoria,
                            @NotNull Long idAutor) {
        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.idCategoria = idCategoria;
        this.idAutor = idAutor;
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

    public Long getIdCategoria() {
        return idCategoria;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public Livro toModel(@NotNull CategoriaRepository categoriaRepository, @NotNull AutorRepository autorRepository) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada")
        );
        Autor autor = autorRepository.findById(idAutor).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado")
        );
        return new Livro(
                titulo,
                resumo,
                sumario,
                preco,
                numeroPaginas,
                isbn,
                dataPublicacao,
                categoria,
                autor
        );
    }
}
