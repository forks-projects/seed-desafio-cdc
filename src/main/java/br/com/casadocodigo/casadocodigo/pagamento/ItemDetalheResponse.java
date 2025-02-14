package br.com.casadocodigo.casadocodigo.pagamento;

import java.math.BigDecimal;

public class ItemDetalheResponse {
    private Long livroId;
    private String nomeLivro;
    private Integer quantidade;
    private BigDecimal preco;

    public ItemDetalheResponse(Item item) {
        this.livroId = item.getLivro().getId();
        this.nomeLivro = item.getLivro().getTitulo();
        this.quantidade = item.getQuantidade();
        this.preco = item.getPreco();
    }

    public Long getLivroId() {
        return livroId;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
