package br.com.casadocodigo.casadocodigo.cupom_desconto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons_desconto", uniqueConstraints = {
        @UniqueConstraint(name = "uc_codigo_cupom_desconto", columnNames = "codigo")
})
public class CupomDesconto {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @NotBlank
    private String codigo;

    @NotNull
    @Positive
    private BigDecimal percentualDesconto;

    @Future
    @NotNull
    private LocalDate validade;

    @Deprecated
    public CupomDesconto() {
    }

    public CupomDesconto(@NotBlank String codigo,
                         @NotNull @Positive BigDecimal percentualDesconto,
                         @Future @NotNull LocalDate validade) {
        this.codigo = codigo;
        this.percentualDesconto = percentualDesconto;
        this.validade = validade;
    }

    public String getCodigo() {
        return codigo;
    }
}
