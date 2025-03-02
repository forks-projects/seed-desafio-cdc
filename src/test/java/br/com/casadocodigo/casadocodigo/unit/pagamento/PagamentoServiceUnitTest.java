package br.com.casadocodigo.casadocodigo.unit.pagamento;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDescontoRepository;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.integration.pagamento.NovoPagamentoRequestBuilder;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pagamento.Item;
import br.com.casadocodigo.casadocodigo.pagamento.NovoItemRequest;
import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import br.com.casadocodigo.casadocodigo.pagamento.Pagamento;
import br.com.casadocodigo.casadocodigo.pagamento.PagamentoRepository;
import br.com.casadocodigo.casadocodigo.pagamento.PagamentoService;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceUnitTest {
    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private CupomDescontoRepository cupomDescontoRepository;

    private NovoPagamentoRequest novoPagamentoRequest;

    private Pagamento pagamentoValido;

    @Test
    @DisplayName("Deve salvar pagamento válido")
    void deveSalvarPagamentoValido() throws MethodArgumentNotValidException {
        BigDecimal totalPagamento = new BigDecimal("200.00");
        pagamentoValido = criarPagamento();
        long idPais = 1L;
        long livroId = 2L;
        NovoItemRequest novoItemRequest = new NovoItemRequest(livroId, 2);
        novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(idPais)
                .comItens(List.of(novoItemRequest))
                .comTotal(totalPagamento)
                .build();

        when(paisRepository.findById(novoPagamentoRequest.getIdPais())).thenReturn(Optional.of(pagamentoValido.getPais()));
        Livro livro = pagamentoValido.getItens().get(0).getLivro();
        when(livroRepository.findById(novoItemRequest.getLivroId())).thenReturn(Optional.of(livro));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoValido);

        Optional<Pagamento> pagamentoSalvo = pagamentoService.salvar(novoPagamentoRequest);

        assertTrue(pagamentoSalvo.isPresent());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Não deve salvar pagamento quando total pagamento diferente do total dos itens")
    void naoDeveSalvarPagamentoQuandoTotalPagamentoDiferenteTotalDosItens() throws MethodArgumentNotValidException {
        Livro livro = criarLivro();
        Pais pais = new Pais("Brasil");
        Long livroId = 1L;
        NovoItemRequest item = new NovoItemRequest(livroId, 1);
        BigDecimal totalPagamentoDiferenteTotalItens = BigDecimal.ZERO;
        novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comItens(List.of(item))
                .comTotal(totalPagamentoDiferenteTotalItens)
                .build();
        when(paisRepository.findById(novoPagamentoRequest.getIdPais())).thenReturn(Optional.of(pais));
        when(livroRepository.findById(livroId)).thenReturn(Optional.of(livro));

        Optional<Pagamento> pagamentoSalvo = pagamentoService.salvar(novoPagamentoRequest);

        assertTrue(pagamentoSalvo.isEmpty());
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve buscar pagamento por ID quando existir")
    void deveBuscarPagamentoPorIdQuandoExistir() {
        Long pagamentoId = 1L;
        pagamentoValido = criarPagamento();

        when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.of(pagamentoValido));

        Pagamento pagamentoEncontrado = pagamentoService.buscarDetalhePagamento(pagamentoId);

        assertNotNull(pagamentoEncontrado);
        assertEquals(pagamentoValido, pagamentoEncontrado);
        verify(pagamentoRepository).findById(pagamentoId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagamento não for encontrado")
    void deveLancarExcecaoQuandoPagamentoNaoForEncontrado() {
        Long pagamentoId = 1L;
        when(pagamentoRepository.findById(pagamentoId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> pagamentoService.buscarDetalhePagamento(pagamentoId)
        );

        assertEquals("404 NOT_FOUND \"Pagamento não encontrado\"", exception.getMessage());
        verify(pagamentoRepository).findById(pagamentoId);
    }

    private Livro criarLivro() {
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "escritor", LocalDateTime.now());
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

    private Pagamento criarPagamento() {
        Livro livro = criarLivro();
        Item item = new Item(livro, 1);
        BigDecimal totalPagamento = new BigDecimal("200.00");
        Pais pais = new Pais("Brasil");
        return new Pagamento(
                "email@email.com", "João", "Silva", "12345678900",
                "Rua Exemplo", "Apto 101", "999999999", "12345-678", "Cidade",
                pais, null, totalPagamento, List.of(item), null
        );
    }

}