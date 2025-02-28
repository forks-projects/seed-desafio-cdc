package br.com.casadocodigo.casadocodigo.unit.autor;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequest;
import br.com.casadocodigo.casadocodigo.integration.autor.NovoAutorRequestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NovoAutorRequestTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        NovoAutorRequest request = NovoAutorRequestDataBuilder.umAutor().build();

        Autor autor = request.toModel();

        assertNotNull(autor);
        assertEquals(autor.getNome(), request.getNome());
        assertEquals(autor.getDescricao(), request.getDescricao());
    }

}