package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.estado.NovoEstadoRequestBuilder;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NovoPagamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    @DisplayName("Deve ter pagamento com dados validos")
    void deveTerPagamentoComDadosValidos() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("fornecerDadosParaErrosHttpStatus")
    @DisplayName("Deve retornar erro 400 quando cadastrar pagamento com dados invalidos")
    void deveRetornarErro400QuandoCadastrarPagamentoComDadosInvalidos(NovoPagamentoRequestBuilder novoPagamentoRequestBuilder, int statusEsperado, String descricaoErro) throws Exception {
        NovoPagamentoRequest novoPagamentoRequest = novoPagamentoRequestBuilder.build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().is(statusEsperado));
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("fornecerDadosParaMensagensErro")
    @DisplayName("Deve mostrar mensagem de erro quando cadastrar pagamento com dados invalidos")
    void deveMostrarMensagemErroQuandoCadastrarPagamentoComDadosInvalidos(NovoPagamentoRequestBuilder novoPagamentoRequestBuilder, String nomeCampo, String mensagemErro, String descricaoErro) throws Exception {
        Pais pais = paisRepository.save(new Pais("Brasil"));
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoPagamentoRequest novoPagamentoRequest = novoPagamentoRequestBuilder.comIdPais(pais.getId()).comIdEstado(estado.getId()).build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value(nomeCampo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país não encontrado")
    void deveRetornarErro400QuandoPaisNaoEncontrado() throws Exception {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento().comIdPais(999L).build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país não encontrado")
    void deveMostrarMensagemErroQuandoPaisNaoEncontrado() throws Exception {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento().comIdPais(999L).comIdEstado(null).build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idPais"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("País não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país não encontrado")
    void deveRetornarErro400QuandoEstadoNaoEncontrado() throws Exception {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento().comIdEstado(999L).build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando estado não encontrado")
    void deveMostrarMensagemErroQuandoEstadoNaoEncontrado() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(999L).build();


        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país possui estado e id estado está nulo")
    void deveRetornarErro400QuandoPaisPossuiEstadoEIdEstadoEstaNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(null)
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país possui estado e id estado está nulo")
    void deveMostrarMensagemErroQuandoPaisPossuiEstadoEIdEstadoEstaNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(null)
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país possui estado e id estado pertence a outro país")
    void deveRetornarErro400QuandoPaisPossuiEstadoEIdEstadoPertenceAOutroPais() throws Exception {
        Pais pais1 = new Pais("Brasil");
        paisRepository.save(pais1);
        Pais pais2 = new Pais("Argentina");
        paisRepository.save(pais2);
        Estado estado1 = NovoEstadoRequestBuilder.umEstado().comIdPais(pais1.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado1);
        Estado estado2 = NovoEstadoRequestBuilder.umEstado().comNome("Catamarca").comIdPais(pais2.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado2);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais1.getId())
                .comIdEstado(estado2.getId())
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país possui estado e id estado pertence a outro país")
    void deveMostrarMensagemDeErroQuandoPaisPossuiEstadoEIdEstadoPertenceAOutroPais() throws Exception {
        Pais pais1 = new Pais("Brasil");
        paisRepository.save(pais1);
        Pais pais2 = new Pais("Argentina");
        paisRepository.save(pais2);
        Estado estado1 = NovoEstadoRequestBuilder.umEstado().comIdPais(pais1.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado1);
        Estado estado2 = NovoEstadoRequestBuilder.umEstado().comNome("Catamarca").comIdPais(pais2.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado2);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais2.getId())
                .comIdEstado(estado1.getId())
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não pertence a este País"));
    }

    static Stream<Arguments> fornecerDadosParaErrosHttpStatus() {
        return Stream.of(
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail(""),
                        400,
                        "Email em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail("email_email"),
                        400,
                        "Email mal formado"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comNome(""),
                        400,
                        "Nome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comSobreNome(""),
                        400,
                        "Sobrenome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCpfCnpj("123456"),
                        400,
                        "CpfCnpj inválido"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEndereco(""),
                        400,
                        "Endereço não pode estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comComplemento(""),
                        400,
                        "Complemento não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCidade(""),
                        400,
                        "Cidade não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comIdPais(null),
                        400,
                        "IdPais não deve ser nulo"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comTelefone(""),
                        400,
                        "Telefone não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCep(""),
                        400,
                        "CEP não deve estar em branco"
                )
        );
    }

    static Stream<Arguments> fornecerDadosParaMensagensErro() {
        return Stream.of(
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail(""),
                        "email",
                        "não deve estar em branco",
                        "Email em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail("email_email"),
                        "email",
                        "deve ser um endereço de e-mail bem formado",
                        "Email mal formado"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comNome(""),
                        "nome",
                        "não deve estar em branco",
                        "Nome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comSobreNome(""),
                        "sobreNome",
                        "não deve estar em branco",
                        "Sobrenome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCpfCnpj("123"),
                        "cpfCnpj",
                        "inválido. Utilize o formato 999.999.999-99 para CPF ou 99.999.999/9999-99 para CNPJ",
                        "CPF ou CNPJ não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEndereco(""),
                        "endereco",
                        "não deve estar em branco",
                        "Endereço não pode estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comComplemento(""),
                        "complemento",
                        "não deve estar em branco",
                        "Complemento não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCidade(""),
                        "cidade",
                        "não deve estar em branco",
                        "Cidade não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comTelefone(""),
                        "telefone",
                        "não deve estar em branco",
                        "Telefone não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCep(""),
                        "cep",
                        "não deve estar em branco",
                        "CEP não deve estar em branco"
                )
        );
    }
}