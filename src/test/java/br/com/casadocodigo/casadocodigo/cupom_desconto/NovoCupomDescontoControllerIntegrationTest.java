package br.com.casadocodigo.casadocodigo.cupom_desconto;

import br.com.casadocodigo.casadocodigo.compartilhado.CustomMockMvc;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.spring.JqwikSpringSupport;
import net.jqwik.time.api.Dates;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@JqwikSpringSupport
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class NovoCupomDescontoControllerIntegrationTest {
    @Autowired
    private CustomMockMvc customMockMvc;
    private Set<String> codigoCadastrado = new HashSet<>();
    private static String ENDPOINT_CUPOM = "/v1/cupons-desconto";

    @Property(tries = 10)
    @Label("Cadastrar cupom")
    void cadastrarCupom(
            @ForAll @AlphaChars @StringLength(min = 1, max = 255) String codigo,
            @ForAll @Positive @BigRange(min = "0", max = "100") BigDecimal percentualDesconto,
            @ForAll("datasFuturas") LocalDate dataPublicacao
    ) throws Exception {
        Assumptions.assumeTrue(codigoCadastrado.add(codigo));
        Map<String, Object> requestCupom = Map.of(
                "codigo", codigo,
                "percentualDesconto", percentualDesconto,
                "validade", dataPublicacao
        );

        customMockMvc.post(ENDPOINT_CUPOM, requestCupom)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        customMockMvc.post(ENDPOINT_CUPOM, requestCupom)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Provide
    Arbitrary<LocalDate> datasFuturas() {
        return Dates.dates().atTheEarliest(LocalDate.now().plusDays(1));
    }
}