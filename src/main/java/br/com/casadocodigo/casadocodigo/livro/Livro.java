package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "livros", uniqueConstraints = {
        @UniqueConstraint(name = "uc_titulo", columnNames = "titulo"),
        @UniqueConstraint(name = "uc_isbn", columnNames = "isbn")
})
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    @Length(max = 500)
    private String resumo;

    @NotBlank
    private String sumario;

    @Min(20)
    @NotNull
    private BigDecimal preco;

    @Min(100)
    private int numeroPaginas;

    @NotBlank
    private String  isbn;

    @Future
    @NotNull
    private LocalDate dataPublicacao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Autor autor;

    @Deprecated
    public Livro() {}

    public Livro(String titulo,
                 String resumo,
                 String sumario,
                 BigDecimal preco,
                 int numeroPaginas,
                 String isbn,
                 LocalDate dataPublicacao,
                 Categoria categoria,
                 Autor autor) {
        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.numeroPaginas = numeroPaginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.categoria = categoria;
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }
}
