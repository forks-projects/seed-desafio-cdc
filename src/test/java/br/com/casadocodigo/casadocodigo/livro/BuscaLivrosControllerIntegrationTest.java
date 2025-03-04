package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@Transactional
class BuscaLivrosControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    private Categoria categoria;

    private Autor autor;

    private Livro livro;

    private String idLIvro;

    private static String ENDPOINT_LIVRO = "/v1/livros/";

    @BeforeEach
    void setup() {
        categoria = new Categoria("Tecnologia");
        autor = new Autor(" Robert C. Martin", "email@email.com", "escritor", LocalDateTime.now());
        livro = new Livro("Clean Code",
                "Mesmo um código ruim pode funcionar",
                "Sumario Livro",
                new BigDecimal("100.00"),
                100,
                "abc-123-cba",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
        categoriaRepository.save(categoria);
        autorRepository.save(autor);
        livroRepository.save(livro);
    }

    @Test
    @DisplayName("Buscar Livro")
    void buscarLivro() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions result = mvc.perform(requestBuilder);
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Buscar livro por id")
    void buscarLivroPorId() throws Exception {
        idLIvro = livro.getId().toString();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ENDPOINT_LIVRO.concat(idLIvro))
                .contentType(MediaType.APPLICATION_JSON);

        Map<String, String> detalheAutor = Map.of("nome", autor.getNome(), "descricao", autor.getDescricao());
        Map<String, String> detalheCategoria = Map.of("nome", categoria.getNome());
        Map<String, Object> detalheLivro = Map.of(
                "titulo", livro.getTitulo(),
                "resumo", livro.getResumo(),
                "sumario", livro.getSumario(),
                "preco", livro.getPreco().setScale(2, RoundingMode.HALF_UP),
                "numeroPaginas", livro.getNumeroPaginas(),
                "isbn", livro.getIsbn(),
                "dataPublicacao", livro.getDataPublicacao().toString(),
                "categoria", detalheCategoria,
                "autor", detalheAutor
        );
        String livroJson = new ObjectMapper().writeValueAsString(detalheLivro);

        ResultActions result = mvc.perform(requestBuilder);
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
              .andExpect(MockMvcResultMatchers.content().json(livroJson));
    }

    @Test
    @DisplayName("Buscar livro por livro não cadastrado")
    void buscarPorLivroNaoCadastrado() throws Exception {
        idLIvro = "9999999";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(ENDPOINT_LIVRO.concat(idLIvro))
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions result = mvc.perform(requestBuilder);
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}