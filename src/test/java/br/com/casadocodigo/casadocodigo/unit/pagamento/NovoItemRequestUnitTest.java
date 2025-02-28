package br.com.casadocodigo.casadocodigo.unit.pagamento;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pagamento.Item;
import br.com.casadocodigo.casadocodigo.pagamento.NovoItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NovoItemRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        Long livroId = 1L;
        Integer quantidade = 2;
        NovoItemRequest request = new NovoItemRequest(livroId, quantidade);
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        Livro livro = new Livro("Clean Code",
                "Mesmo um código ruim pode funcionar",
                "Sumario Livro",
                new BigDecimal("100.00"),
                100,
                "abc-123-cba",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
        LivroRepository livroRepository = mock(LivroRepository.class);
        when(livroRepository.findById(livroId)).thenReturn(Optional.of(livro));

        Item item = request.toModel(livroRepository);

        assertNotNull(item);
        assertNotNull(item.getLivro());
        assertEquals(item.getQuantidade(), request.getQuantidade());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o livro não for encontrado")
    void deveLancarExcecaoQuandoLivroNaoForEncontrado() {
        Long livroId = 99L;
        LivroRepository livroRepository = mock(LivroRepository.class);
        when(livroRepository.findById(livroId)).thenReturn(Optional.empty());
        NovoItemRequest request = new NovoItemRequest(livroId, 2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(livroRepository)
        );
        assertEquals("400 BAD_REQUEST \"Livro não encontrado\"", exception.getMessage());
    }
}
