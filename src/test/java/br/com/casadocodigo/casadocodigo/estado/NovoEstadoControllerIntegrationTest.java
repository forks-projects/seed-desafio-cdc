package br.com.casadocodigo.casadocodigo.estado;

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
class NovoEstadoControllerIntegrationTest {
    @Autowired
    private CustomMockMvc customMockMvc;
    private Set<String> nomeCadastrado  = new HashSet<>();
    private static String ENDPOINT_ESTADOS = "/v1/estados";
    private static String ENDPOINT_PAISES = "/v1/paises";

    @Property(tries = 10)
    @Label("Cadastrar estado")
    void cadastrarEstado(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String nome) throws Exception {
        Assumptions.assumeTrue(nomeCadastrado.add(nome));
        Map<String, Object> requestPais = Map.of("nome", nome);
        Map<String, Object> requestEstado = Map.of("nome", nome, "idPais", "1");
        customMockMvc.post(ENDPOINT_PAISES, requestPais);

        customMockMvc.post(ENDPOINT_ESTADOS, requestEstado)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        customMockMvc.post(ENDPOINT_ESTADOS, requestEstado)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}