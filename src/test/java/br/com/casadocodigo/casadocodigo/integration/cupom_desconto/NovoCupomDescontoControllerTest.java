package br.com.casadocodigo.casadocodigo.integration.cupom_desconto;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDescontoRepository;
import br.com.casadocodigo.casadocodigo.cupom_desconto.NovoCupomDescontoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("integrationTest")
class NovoCupomDescontoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CupomDescontoRepository cupomDescontoRepository;

    @Test
    @DisplayName("Deve cadastrar um cupom de desconto com sucesso")
    void deveCadastrarCupomComSucesso() throws Exception {
        NovoCupomDescontoRequest request = NovoCupomDescontoRequestBuilder.umCupom().build();

        mockMvc.perform(post("/v1/cupons-desconto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando codigo cupom já cadastrado")
    void deveRetornarErroQuandoCodigoCupomJaCadastrado() throws Exception {
        CupomDesconto cupomExistente = NovoCupomDescontoRequestBuilder.umCupom().build().toModel();
        cupomDescontoRepository.save(cupomExistente);
        NovoCupomDescontoRequest request = NovoCupomDescontoRequestBuilder.umCupom().build();

        mockMvc.perform(post("/v1/cupons-desconto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("codigo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Código do Cupom de desconto já está cadastrado"));
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @MethodSource("provideInvalidosCuponsRequest")
    @DisplayName("Deve mostrar mensagem de erro quando cupom com dados inválidos")
    void deveRetornarErroParaRequisicoesInvalidas(
            NovoCupomDescontoRequest request,
            int expectedStatus,
            String campo,
            String mensagemErro,
            String descricaoErro) throws Exception {

        mockMvc.perform(post("/v1/cupons-desconto")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.listaErros[0].campo").value(campo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    private static Stream<Arguments> provideInvalidosCuponsRequest() {
        return Stream.of(
                Arguments.of(
             NovoCupomDescontoRequestBuilder.umCupom().comCodigo("").build(),
                        400,
                        "codigo",
                        "não deve estar em branco",
                        "Código em branco"
                ),
                Arguments.of(
                        NovoCupomDescontoRequestBuilder.umCupom().comCodigo(null).build(),
                        400,
                        "codigo",
                        "não deve estar em branco",
                        "Código nulo"
                ),
                Arguments.of(
                        NovoCupomDescontoRequestBuilder.umCupom().comPercentualDesconto(null).build(),
                        400,
                        "percentualDesconto",
                        "não deve ser nulo",
                        "Percentual de desconto nulo"
                ),
                Arguments.of(
                        NovoCupomDescontoRequestBuilder.umCupom().comPercentualDesconto(new BigDecimal("-5.0")).build(),
                        400,
                        "percentualDesconto",
                        "deve ser maior que 0",
                        "Percentual de desconto menor que 1"
                ),
                Arguments.of(
                        NovoCupomDescontoRequestBuilder.umCupom().comValidade(null).build(),
                        400,
                        "validade",
                        "não deve ser nulo",
                        "data de Validade nula"
                ),
                Arguments.of(
                        NovoCupomDescontoRequestBuilder.umCupom().comValidade(LocalDate.now().minusDays(1)).build(),
                        400,
                        "validade",
                        "deve ser uma data futura",
                        "Data de Validade em data passada"
                )
        );
    }
}
