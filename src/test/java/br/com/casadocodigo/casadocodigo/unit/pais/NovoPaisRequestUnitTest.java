package br.com.casadocodigo.casadocodigo.unit.pais;

import br.com.casadocodigo.casadocodigo.pais.NovoPaisRequest;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NovoPaisRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        String paisEsperado = "Brasil";
        NovoPaisRequest request = new NovoPaisRequest(paisEsperado);

        Pais pais = request.toModel();

        assertNotNull(pais);
        assertEquals(pais.getNome(), request.getNome());
    }
}
