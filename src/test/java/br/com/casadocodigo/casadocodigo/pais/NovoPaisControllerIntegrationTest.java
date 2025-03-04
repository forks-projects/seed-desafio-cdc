package br.com.casadocodigo.casadocodigo.pais;

import br.com.casadocodigo.casadocodigo.compartilhado.CustomMockMvc;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovoPaisControllerIntegrationTest {
    @Autowired
    private CustomMockMvc customMockMvc;
    private static String ENDPOINT_PAISES = "/v1/paises";
    private Set<String> nomeCadastrado  = new HashSet<>();

    @Property(tries = 10)
    @Label("Cadastrar pais")
    void cadastrarPais(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome) throws Exception {
        Assumptions.assumeTrue(nomeCadastrado.add(nome));
        Map<String, Object> request = Map.of("nome", nome);
        customMockMvc.post(ENDPOINT_PAISES, request)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        customMockMvc.post(ENDPOINT_PAISES, request)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}