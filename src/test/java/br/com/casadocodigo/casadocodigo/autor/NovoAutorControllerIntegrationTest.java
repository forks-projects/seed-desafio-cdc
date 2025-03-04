package br.com.casadocodigo.casadocodigo.autor;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovoAutorControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    private static Set<String> emailsGerado = new HashSet<>();

    @Property(tries = 10)
    @Label("Cadastrar livro")
    void cadastrarLivro(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome,
            @ForAll @AlphaChars  @StringLength(min = 1, max = 50) String email,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String descricao
    ) throws Exception {
        Assumptions.assumeTrue(emailsGerado.add(email));
        Map<String, String> payloadMap = Map.of(
                "nome", nome,
                "email", email.concat("@dominio.com"),
                "descricao", descricao
        );
        String payload = new ObjectMapper().writeValueAsString(payloadMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Property(tries = 10)
    @Label("Cadastrar livro")
    void cadastrarLivroValidadoSpring(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome,
            @ForAll @AlphaChars  @StringLength(min = 1, max = 50) String email,
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String descricao
    ) throws Exception {
        Assumptions.assumeTrue(emailsGerado.add(email));
        Map<String, String> payloadMap = Map.of(
                "nome", nome,
                "email", email.concat("@dominio.com"),
                "descricao", descricao
        );
        String payload = new ObjectMapper().writeValueAsString(payloadMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/validadores-customizado-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}