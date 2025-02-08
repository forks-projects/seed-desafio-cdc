package br.com.casadocodigo.casadocodigo.cupom_desconto;

import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NovoCupomDescontoRequest {
    @NotBlank
    @ValorUnico(classeDaEntidade = CupomDesconto.class, nomeDoCampo = "codigo", message = "Código do Cupom de desconto já está cadastrado")
    private String codigo;

    @NotNull
    @Positive
    private BigDecimal percentualDesconto;

    @Future
    @NotNull
    private LocalDate validade;

    public NovoCupomDescontoRequest(String codigo, BigDecimal percentualDesconto, LocalDate validade) {
        this.codigo = codigo;
        this.percentualDesconto = percentualDesconto;
        this.validade = validade;
    }

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public LocalDate getValidade() {
        return validade;
    }
    
    public CupomDesconto toModel() {
      return new CupomDesconto(codigo, percentualDesconto, validade);
    }
}
