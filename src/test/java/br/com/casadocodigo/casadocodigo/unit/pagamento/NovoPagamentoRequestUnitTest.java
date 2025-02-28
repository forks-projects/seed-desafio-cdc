package br.com.casadocodigo.casadocodigo.unit.pagamento;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDescontoRepository;
import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.integration.pagamento.NovoPagamentoRequestBuilder;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pagamento.NovoItemRequest;
import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import br.com.casadocodigo.casadocodigo.pagamento.Pagamento;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NovoPagamentoRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() throws MethodArgumentNotValidException {
        Pais pais = new Pais("Brasil");
        Estado estado = new Estado("São Paulo", pais);
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
        CupomDesconto cupomDesconto = new CupomDesconto("CUPOM10", BigDecimal.TEN, LocalDate.now().plusMonths(1l));
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(1L)
                .comIdEstado(1L)
                .comItens(List.of(new NovoItemRequest(1L, 1)))
                .comTotal(new BigDecimal("100.00"))
                .comCupomDesconto(cupomDesconto.getCodigo())
                .build();
        PaisRepository paisRepository = mock(PaisRepository.class);
        EstadoRepository estadoRepository = mock(EstadoRepository.class);
        LivroRepository livroRepository = mock(LivroRepository.class);
        CupomDescontoRepository cupomDescontoRepository = mock(CupomDescontoRepository.class);

        when(paisRepository.findById(request.getIdPais())).thenReturn(Optional.of(pais));
        when(estadoRepository.findById(request.getIdEstado())).thenReturn(Optional.of(estado));
        when(livroRepository.findById(request.getIdEstado())).thenReturn(Optional.of(livro));
        when(cupomDescontoRepository.findById(request.getCupomDesconto())).thenReturn(Optional.of(cupomDesconto));

        Pagamento pagamento = request.toModel(paisRepository, estadoRepository, livroRepository, cupomDescontoRepository);

        assertNotNull(pagamento);
        assertEquals(pagamento.getEmail(), request.getEmail());
        assertEquals(pagamento.getNome(), request.getNome());
        assertEquals(pagamento.getSobreNome(), request.getSobreNome());
        assertEquals(pagamento.getCpfCnpj(), request.getCpfCnpj());
        assertEquals(pagamento.getEndereco(), request.getEndereco());
        assertEquals(pagamento.getComplemento(), request.getComplemento());
        assertEquals(pagamento.getTelefone(), request.getTelefone());
        assertEquals(pagamento.getCidade(), request.getCidade());
        assertEquals(pagamento.getCep(), request.getCep());
        assertEquals(pagamento.getPais().getNome(), pais.getNome());
        assertEquals(pagamento.getEstado().getNome(), estado.getNome());
        assertEquals(pagamento.getTotal(), request.getTotal());
        assertEquals(pagamento.getItens().size(), request.getItensRequest().size());
        assertEquals(pagamento.getCupomDesconto().getCodigo(), request.getCupomDesconto());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o país não for encontrado")
    void deveLancarExcecaoQuandoPaisNaoForEncontrado() {
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder
                .umPagamento()
                .comIdPais(1L)
                .build();
        PaisRepository paisRepository = mock(PaisRepository.class);
        EstadoRepository estadoRepository = mock(EstadoRepository.class);
        LivroRepository livroRepository = mock(LivroRepository.class);
        CupomDescontoRepository cupomDescontoRepository = mock(CupomDescontoRepository.class);
        when(paisRepository.findById(request.getIdPais())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(paisRepository, estadoRepository, livroRepository, cupomDescontoRepository)
        );
        assertEquals("400 BAD_REQUEST \"País não encontrado\"", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando estado não for encontrado")
    void deveLancarExcecaoQuandoEstadoNaoForEncontrado() {
        Pais pais = new Pais("Brasil");
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder
                .umPagamento()
                .comIdPais(1L)
                .comIdEstado(1L)
                .build();
        PaisRepository paisRepository = mock(PaisRepository.class);
        EstadoRepository estadoRepository = mock(EstadoRepository.class);
        LivroRepository livroRepository = mock(LivroRepository.class);
        CupomDescontoRepository cupomDescontoRepository = mock(CupomDescontoRepository.class);
        when(paisRepository.findById(request.getIdPais())).thenReturn(Optional.of(pais));
        when(estadoRepository.findById(request.getIdEstado())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(paisRepository, estadoRepository, livroRepository, cupomDescontoRepository)
        );
        assertEquals("400 BAD_REQUEST \"Estado não encontrado\"", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cupom não for encontrado")
    void deveLancarExcecaoQuandoCupomNaoForEncontrado() {
        Pais pais = new Pais("Brasil");
        Estado estado = new Estado("São Paulo", pais);
        String cupomInvalido = "CUPOM_INVALIDO";
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder
                .umPagamento()
                .comIdPais(1L)
                .comIdEstado(1L)
                .comCupomDesconto(cupomInvalido)
                .build();
        PaisRepository paisRepository = mock(PaisRepository.class);
        EstadoRepository estadoRepository = mock(EstadoRepository.class);
        LivroRepository livroRepository = mock(LivroRepository.class);
        CupomDescontoRepository cupomDescontoRepository = mock(CupomDescontoRepository.class);
        when(paisRepository.findById(request.getIdPais())).thenReturn(Optional.of(pais));
        when(estadoRepository.findById(request.getIdEstado())).thenReturn(Optional.of(estado));
        when(cupomDescontoRepository.findById(cupomInvalido)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(paisRepository, estadoRepository, livroRepository, cupomDescontoRepository)
        );
        assertEquals("400 BAD_REQUEST \"Cupom de descontro não encontrado\"", exception.getMessage());
    }
}