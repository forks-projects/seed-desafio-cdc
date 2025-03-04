package br.com.casadocodigo.casadocodigo.categoria;

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
class NovaCategoriaControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;
    private static Set<String> categoriaCadastrada = new HashSet<>();

    @Property(tries = 10)
    @Label("Cadastrar categoria")
    void cadastrarCategoria(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome
    ) throws Exception {
        Assumptions.assumeTrue(categoriaCadastrada.add(nome));
        Map<String, String> payloadMap = Map.of("nome", nome);
        String payload = new ObjectMapper().writeValueAsString(payloadMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}