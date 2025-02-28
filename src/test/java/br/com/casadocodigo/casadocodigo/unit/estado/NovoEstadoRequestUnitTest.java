package br.com.casadocodigo.casadocodigo.unit.estado;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.NovoEstadoRequest;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NovoEstadoRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        String estadoEsperado = "São Paulo";
        long idPais = 1L;
        NovoEstadoRequest request = new NovoEstadoRequest(estadoEsperado, idPais);
        Pais pais = new Pais("Brasil");
        PaisRepository paisRepository = mock(PaisRepository.class);
        when(paisRepository.findById(idPais)).thenReturn(Optional.of(pais));

        Estado estado = request.toModel(paisRepository);

        assertNotNull(estado);
        assertEquals(estado.getNome(), estadoEsperado);
        assertEquals(estado.getPais().getNome(), pais.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o país não for encontrado")
    void deveLancarExcecaoQuandoPaisNaoForEncontrado() {
        PaisRepository paisRepository = mock(PaisRepository.class);
        long idPais = 99L;
        when(paisRepository.findById(idPais)).thenReturn(Optional.empty());
        NovoEstadoRequest request = new NovoEstadoRequest("São Paulo", idPais);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(paisRepository)
        );
        assertEquals("404 NOT_FOUND \"Pais não encontrado\"", exception.getMessage());
    }

}