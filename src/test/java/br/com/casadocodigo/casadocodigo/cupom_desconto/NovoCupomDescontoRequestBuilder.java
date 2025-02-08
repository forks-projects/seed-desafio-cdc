package br.com.casadocodigo.casadocodigo.cupom_desconto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovoCupomDescontoRequestBuilder {

    private String codigo;
    private BigDecimal percentualDesconto;
    private LocalDate validade;

    public NovoCupomDescontoRequestBuilder() {
        this.codigo = "DESCONTO10";
        this.percentualDesconto = BigDecimal.TEN;
        this.validade = LocalDate.now().plusDays(10);
    }

    public static NovoCupomDescontoRequestBuilder umCupom() {
        return new NovoCupomDescontoRequestBuilder();
    }

    public NovoCupomDescontoRequestBuilder comCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public NovoCupomDescontoRequestBuilder comPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
        return this;
    }

    public NovoCupomDescontoRequestBuilder comValidade(LocalDate validade) {
        this.validade = validade;
        return this;
    }

    public NovoCupomDescontoRequest build() {
        return new NovoCupomDescontoRequest(codigo, percentualDesconto, validade);
    }
}
