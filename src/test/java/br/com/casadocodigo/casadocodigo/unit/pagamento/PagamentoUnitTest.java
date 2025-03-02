package br.com.casadocodigo.casadocodigo.unit.pagamento;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.pagamento.Item;
import br.com.casadocodigo.casadocodigo.pagamento.Pagamento;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PagamentoUnitTest {
    @Test
    @DisplayName("Deve retornar true quando o total informado for válido")
    void deveRetornarTrueQuandoTotalForValido() {
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        Livro livro1 = criarLivro1(categoria, autor);
        Livro livro2 = criarLivro2(categoria, autor);
        List<Item> itens = List.of(new Item(livro1, 2), new Item(livro2, 1));
        BigDecimal totalPagamento = new BigDecimal("350.00");
        Pagamento pagamento = criarPagamento(totalPagamento, itens);

        assertTrue(pagamento.isTotalValido());
    }

    @Test
    @DisplayName("Deve retornar false quando o total informado for inválido")
    void deveRetornarFalseQuandoTotalForInvalido() {
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        Livro livro1 = criarLivro1(categoria, autor);
        Livro livro2 = criarLivro2(categoria, autor);
        List<Item> itens = List.of(new Item(livro1, 1), new Item(livro2, 1));
        BigDecimal totalPagamanetoErrado = new BigDecimal("100.00");
        Pagamento pagamento = criarPagamento(totalPagamanetoErrado, itens);

        assertFalse(pagamento.isTotalValido());
    }

    private Livro criarLivro1(Categoria categoria, Autor autor) {
        return new Livro("Clean Code",
                "Mesmo um código ruim pode funcionar",
                "Sumario Livro",
                new BigDecimal("100.00"),
                100,
                "abc-123-cba",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
    }

    private Livro criarLivro2(Categoria categoria, Autor autor) {
        return new Livro("Clean Architecture",
                "As regras universais de arquitetura de software aumentam dramaticamente",
                "Sumario Livro",
                new BigDecimal("150.00"),
                100,
                "abc-123-def",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
    }

    private Pagamento criarPagamento(BigDecimal totalPagamento, List<Item> itens) {
        return new Pagamento(
                "email@email.com", "João", "Silva", "12345678900",
                "Rua Exemplo", "Apto 101", "999999999", "12345-678", "Cidade",
                new Pais("Brasil"), null, totalPagamento, itens, null
        );
    }
}