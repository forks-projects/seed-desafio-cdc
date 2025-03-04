package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.compartilhado.CustomMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class NovoPagamentoControllerIntegrationTest {
    @Autowired
    private CustomMockMvc customMockMvc;
    private static String ENDPOINT_CATEGORIAS = "/v1/categorias";
    private static String ENDPOINT_AUTORES = "/v1/autores";
    private static String ENDPOINT_LIVROS = "/v1/livros";
    private static String ENDPOINT_PAISES = "/v1/paises";
    private static String ENDPOINT_ESTADOS = "/v1/estados";
    private static String ENDPOINT_PAGAMENTOS = "/v1/pagamentos";
    private static String ENDPOINT_CUPOMDESCONTO = "/v1/cupons-desconto";

    @Property(tries = 10)
    @Label("Cadastrar pagamento")
    void cadastrarPagamento(
            @ForAll @AlphaChars @StringLength(min = 1, max = 50) String email,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String sobreNome,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String endereco,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String complemento,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String cidade,
            @ForAll @NumericChars @StringLength(min = 1, max = 20) String telefone,
            @ForAll @NumericChars @StringLength(min = 1, max = 8) String cep,
            @ForAll @IntRange(min = 1,max = 50) int quantidade
    ) throws Exception {
        Map<String, Object> requestCategoria = criarMapCategoria();
        Map<String, Object> requestAutores = criarMapAutores();
        Map<String, Object> requestLivros = criarMapLivros();
        Map<String, Object> requestPais = criarMapPais();
        Map<String, Object> requestEstado = criarMapEstado();
        Map<String, Object> requestItens = criarMapItens(quantidade);
        HashMap<String, Object> requestPagamento = criarMapPagamento(email,
                nome,
                sobreNome,
                endereco,
                complemento,
                cidade,
                telefone,
                cep,
                quantidade,
                requestItens
        );
        customMockMvc.post(ENDPOINT_CATEGORIAS, requestCategoria);
        customMockMvc.post(ENDPOINT_AUTORES, requestAutores);
        customMockMvc.post(ENDPOINT_LIVROS, requestLivros);
        customMockMvc.post(ENDPOINT_PAISES, requestPais);
        customMockMvc.post(ENDPOINT_ESTADOS, requestEstado);

        customMockMvc.post(ENDPOINT_PAGAMENTOS, requestPagamento)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        requestPagamento.put("total", new BigDecimal("999.00"));
        customMockMvc.post(ENDPOINT_PAGAMENTOS, requestPagamento)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @DisplayName("Detalhar pagamento SEM desconto")
    void detalharPagamento() throws Exception {
        Map<String, Object> requestCategoria = criarMapCategoria();
        Map<String, Object> requestAutores = criarMapAutores();
        Map<String, Object> requestLivros = criarMapLivros();
        Map<String, Object> requestPais = criarMapPais();
        Map<String, Object> requestEstado = criarMapEstado();
        Map<String, Object> requestItens = criarMapItens(2);
        HashMap<String, Object> requestPagamento = criarMapPagamento(requestItens);
        HashMap<String, Object> detalhePagamento = criarMapDetalhePagamentoSemDesconto(requestPagamento);
        customMockMvc.post(ENDPOINT_CATEGORIAS, requestCategoria);
        customMockMvc.post(ENDPOINT_AUTORES, requestAutores);
        customMockMvc.post(ENDPOINT_LIVROS, requestLivros);
        customMockMvc.post(ENDPOINT_PAISES, requestPais);
        customMockMvc.post(ENDPOINT_ESTADOS, requestEstado);
        customMockMvc.post(ENDPOINT_PAGAMENTOS, requestPagamento);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String pagamentoJson = objectMapper.writeValueAsString(detalhePagamento);

        ResultActions result = customMockMvc.get(ENDPOINT_PAGAMENTOS.concat("/1"));
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(pagamentoJson));
    }

    @Test
    @DisplayName("Detalhar pagamento COM cupom desconto e Pais sem Estado")
    void detalharPagamentoComCupomDesconto() throws Exception {
        Map<String, Object> requestCategoria = criarMapCategoria();
        Map<String, Object> requestAutores = criarMapAutores();
        Map<String, Object> requestLivros = criarMapLivros();
        Map<String, Object> requestPais = criarMapPais();
        Map<String, Object> requestItens = criarMapItens(2);
        Map<String, Object> requestCupom = criarMapCupom();
        Map<String, Object> requestPagamento = criarMapPagamentoComDescontoSemEstado(requestItens, requestCupom);
        HashMap<String, Object> detalhePagamento = criarMapDetalhePagamentoComDescontoSemEstado(requestPagamento, requestCupom);

        customMockMvc.post(ENDPOINT_CATEGORIAS, requestCategoria);
        customMockMvc.post(ENDPOINT_AUTORES, requestAutores);
        customMockMvc.post(ENDPOINT_LIVROS, requestLivros);
        customMockMvc.post(ENDPOINT_PAISES, requestPais);
        customMockMvc.post(ENDPOINT_CUPOMDESCONTO, requestCupom);
        customMockMvc.post(ENDPOINT_PAGAMENTOS, requestPagamento);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String pagamentoJson = objectMapper.writeValueAsString(detalhePagamento);

        ResultActions result = customMockMvc.get(ENDPOINT_PAGAMENTOS.concat("/1"));
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(pagamentoJson));
    }

    private static Map<String, Object> criarMapEstado() {
        return Map.of("nome", "São Paulo", "idPais", "1");
    }

    private static Map<String, Object> criarMapLivros() {
        return Map.of(
                "titulo", "Clean Code",
                "resumo", "Código ruim",
                "sumario", "um sumário bem grande",
                "isbn", "abc-123-abc-123",
                "preco", 20.00,
                "numeroPaginas", 100,
                "dataPublicacao", LocalDate.now().plusDays(1L).toString(),
                "idCategoria", 1,
                "idAutor", 1
        );
    }

    private static Map<String, Object> criarMapAutores() {
        return Map.of(
                "nome", "Martin",
                "email", "martin@email.com",
                "descricao", "Escritor renomado"
        );
    }

    private Map<String, Object> criarMapCategoria() {
        return Map.of("nome", "tecnologia");
    }

    private static Map<String, Object> criarMapItens(int quantidade) {
        return Map.of(
                "livroId", 1,
                "quantidade", quantidade
        );
    }

    private static Map<String, Object> criarMapPais() {
        return Map.of("nome", "Brasil");
    }

    private static Map<String, Object> criarMapCupom() {
        return Map.of(
                "codigo", "CUPOM10",
                "percentualDesconto", BigDecimal.TEN.toString(),
                "validade", LocalDate.now().plusDays(1L).toString()
        );
    }

    private static HashMap<String, Object> criarMapPagamento(
            String email,
            String nome,
            String sobreNome,
            String endereco,
            String complemento,
            String cidade,
            String telefone,
            String cep,
            int quantidade,
            Map<String, Object> requestItens
    ) {
        HashMap<String, Object> requestPagamento = new HashMap<>();
        requestPagamento.put("email", email + "@gmail.com");
        requestPagamento.put("nome", nome);
        requestPagamento.put("sobreNome", sobreNome);
        requestPagamento.put("cpfCnpj", "704.724.660-62");
        requestPagamento.put("endereco", endereco);
        requestPagamento.put("complemento", complemento);
        requestPagamento.put("telefone", telefone);
        requestPagamento.put("cidade", cidade);
        requestPagamento.put("cep", cep);
        requestPagamento.put("idPais", "1");
        requestPagamento.put("idEstado", "1");
        BigDecimal total = new BigDecimal("20.00").multiply(new BigDecimal(quantidade));
        requestPagamento.put("total", total);
        List<Map<String, Object>> itens = List.of(requestItens);
        requestPagamento.put("itensRequest", itens);
        return requestPagamento;
    }

    private static HashMap<String, Object> criarMapPagamento(Map<String, Object> requestItens) {
        HashMap<String, Object> requestPagamento = new HashMap<>();
        requestPagamento.put("email", "email@gmail.com");
        requestPagamento.put("nome", "Carlos");
        requestPagamento.put("sobreNome", "Silva");
        requestPagamento.put("cpfCnpj", "704.724.660-62");
        requestPagamento.put("endereco", "Rua 1");
        requestPagamento.put("complemento", "fundos");
        requestPagamento.put("telefone", "(13) 99999-9999)");
        requestPagamento.put("cidade", "Cajati");
        requestPagamento.put("cep", "11910-999");
        requestPagamento.put("idPais", "1");
        requestPagamento.put("idEstado", "1");
        BigDecimal total = new BigDecimal("20.00").multiply(new BigDecimal(2));
        requestPagamento.put("total", total);
        List<Map<String, Object>> itens = List.of(requestItens);
        requestPagamento.put("itensRequest", itens);
        return requestPagamento;
    }

    private static HashMap<String, Object> criarMapPagamentoComDescontoSemEstado(Map<String, Object> requestItens, Map<String, Object> requestCupom) {
        HashMap<String, Object> requestPagamento = criarMapPagamento(requestItens);
        requestPagamento.put("cupomDesconto", requestCupom.get("codigo"));
        requestPagamento.put("idEstado", null);
        return requestPagamento;
    }

    private static HashMap<String, Object> criarMapDetalhePagamentoSemDesconto(Map<String, Object> requestPagamento) {
        HashMap<String, Object> detalhePagamento = new HashMap<>();
        detalhePagamento.put("email", requestPagamento.get("email"));
        detalhePagamento.put("nome", requestPagamento.get("nome"));
        detalhePagamento.put("sobreNome", requestPagamento.get("sobreNome"));
        detalhePagamento.put("cpfCnpj", requestPagamento.get("cpfCnpj"));
        detalhePagamento.put("endereco", requestPagamento.get("endereco"));
        detalhePagamento.put("complemento", requestPagamento.get("complemento"));
        detalhePagamento.put("telefone", requestPagamento.get("telefone"));
        detalhePagamento.put("cidade", requestPagamento.get("cidade"));
        detalhePagamento.put("cep", requestPagamento.get("cep"));
        detalhePagamento.put("nomePais", "Brasil");
        detalhePagamento.put("nomeEstado", "São Paulo");
        detalhePagamento.put("total", requestPagamento.get("total"));
        detalhePagamento.put("temDesconto", false);
        detalhePagamento.put("dataCompra", LocalDate.now().toString());
        detalhePagamento.put("totalComDesconto", null);
        detalhePagamento.put("valorDoDesconto", 0);
        return detalhePagamento;
    }

    private static HashMap<String, Object> criarMapDetalhePagamentoComDescontoSemEstado(Map<String, Object> requestPagamento, Map<String, Object> requestCupom) {
        HashMap<String, Object> detalhePagamento = criarMapDetalhePagamentoSemDesconto(requestPagamento);
        detalhePagamento.put("nomeEstado", null);
        detalhePagamento.put("temDesconto", true);
        BigDecimal total = (BigDecimal) requestPagamento.get("total");
        BigDecimal percentualDesconto = new BigDecimal(requestCupom.get("percentualDesconto").toString());
        BigDecimal desconto = total.multiply(percentualDesconto)
                .divide(new BigDecimal("100.00"), RoundingMode.HALF_UP);
        detalhePagamento.put("valorDoDesconto", desconto);
        detalhePagamento.put("totalComDesconto", total.subtract(desconto));
        return detalhePagamento;
    }
}
