package br.com.casadocodigo.casadocodigo.unit.cupom_desconto;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.cupom_desconto.NovoCupomDescontoRequest;
import br.com.casadocodigo.casadocodigo.integration.cupom_desconto.NovoCupomDescontoRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NovoCupomDescontoRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        NovoCupomDescontoRequest request = NovoCupomDescontoRequestBuilder.umCupom().build();

        CupomDesconto cupomDesconto = request.toModel();

        assertNotNull(cupomDesconto);
        assertEquals(cupomDesconto.getCodigo(), request.getCodigo());
        assertEquals(cupomDesconto.getPercentualDesconto(), request.getPercentualDesconto());
        assertEquals(cupomDesconto.getValidade(), request.getValidade());
    }
}