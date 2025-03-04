package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import br.com.casadocodigo.casadocodigo.compartilhado.CustomMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.spring.JqwikSpringSupport;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovoLivroControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomMockMvc customMockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AutorRepository autorRepository;
    private Categoria categoria = new Categoria("categoria");;
    private Autor autor = new Autor("Robert C. Martin",
            "email@email.com",
            "escritor",
            LocalDateTime.now()
    );
    private static Set<String> isbnCadastrado = new HashSet<>();
    private static Set<String> tituloCadastrado = new HashSet<>();
    private String idCategoria;
    private String idAutor;

    @Property(tries = 10)
    @Label("Cadastrar livro")
    void cadastrarLivro(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String titulo,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String resumo,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String sumario,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String isbn,
            @ForAll @BigRange(min = "20", max = "100") BigDecimal preco,
            @ForAll @IntRange(min = 100) @StringLength(min = 1, max = 255) int numeroPaginas
    ) throws Exception {
        Assumptions.assumeTrue(isbnCadastrado.add(isbn));
        Assumptions.assumeTrue(tituloCadastrado.add(titulo));

        Map<String, Object> payloadCategoria = Map.of("nome", categoria.getNome());
        Map<String, Object> payloadAutor = Map.of(
                "nome", autor.getNome(),
                "email", "martin@email.com.br",
                "descricao", autor.getDescricao());

        customMockMvc.post("/v1/categorias", payloadCategoria);
        customMockMvc.post("/v1/autores", payloadAutor);
        categoria = categoriaRepository.findByNome(categoria.getNome()).orElse(categoria);
        idCategoria = categoria.getId().toString();
        autor = autorRepository.findByEmail(payloadAutor.get("email").toString()).orElse(autor);
        idAutor = autor.getId().toString();

        Map<String, String> payloadMap = Map.of(
                "titulo", titulo,
                "resumo", resumo,
                "sumario", sumario,
                "isbn", isbn,
                "preco", preco.toString(),
                "numeroPaginas", String.valueOf(numeroPaginas),
                "dataPublicacao", LocalDate.now().plusDays(1L).toString(),
                "idCategoria", idCategoria,
                "idAutor", idAutor
        );

        String payload = new ObjectMapper().writeValueAsString(payloadMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
