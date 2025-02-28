package br.com.casadocodigo.casadocodigo.unit.categoria;

import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.NovaCategoriaRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NovaCategoriaRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        String categoriaEsperada = "Tecnologia";
        NovaCategoriaRequest request = new NovaCategoriaRequest(categoriaEsperada);

        Categoria categoria = request.toModel();

        assertNotNull(categoria);
        assertEquals(categoria.getNome(), request.getNome());
    }
}