package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.livro.Livro;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "itens")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    @NotNull
    @Min(value = 1)
    private Integer quantidade;

    @NotNull
    @Min(value = 1)
    private BigDecimal preco;

    public Item() {
    }

    public Item(Livro livro, @NotNull @Min(value = 1) Integer quantidade) {
        this.livro = livro;
        this.quantidade = quantidade;
        this.preco = livro.getPreco();
    }


    public Livro getLivro() {
        return livro;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void associarPagamento(@NotNull Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
