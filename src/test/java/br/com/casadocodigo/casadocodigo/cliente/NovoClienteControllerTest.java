package br.com.casadocodigo.casadocodigo.cliente;

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
class NovoClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    @DisplayName("Deve ter cliente com dados validos")
    void deveTerClienteComDadosValidos() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("fornecerDadosParaErrosHttpStatus")
    @DisplayName("Deve retornar erro 400 quando cadastrar cliente com dados invalidos")
    void deveRetornarErro400QuandoCadastrarClienteComDadosInvalidos(NovoClienteRequestBuilder novoClienteRequestBuilder, int statusEsperado, String descricaoErro) throws Exception {
        NovoClienteRequest novoClienteRequest = novoClienteRequestBuilder.build();

        mockMvc.perform(post("/v1/clientes")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(status().is(statusEsperado));
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("fornecerDadosParaMensagensErro")
    @DisplayName("Deve mostrar mensagem de erro quando cadastrar cliente com dados invalidos")
    void deveMostrarMensagemErroQuandoCadastrarClienteComDadosInvalidos(NovoClienteRequestBuilder novoClienteRequestBuilder, String nomeCampo, String mensagemErro, String descricaoErro) throws Exception {
        NovoClienteRequest novoClienteRequest = novoClienteRequestBuilder.comIdPais(null).build();
        if (!nomeCampo.equals("idPais")) {
            Pais pais = paisRepository.save(new Pais("Brasil"));
            novoClienteRequest = novoClienteRequestBuilder.comIdPais(pais.getId()).build();
        }

        mockMvc.perform(post("/v1/clientes")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value(nomeCampo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país não encontrado")
    void deveRetornarErro400QuandoPaisNaoEncontrado() throws Exception {
        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente().comIdPais(999L).build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país não encontrado")
    void deveMostrarMensagemErro400QuandoPaisNaoEncontrado() throws Exception {
        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente().comIdPais(999L).build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idPais"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("País não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país não encontrado")
    void deveRetornarErro400QuandoEstadoNaoEncontrado() throws Exception {
        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente().comIdEstado(999L).build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando estado não encontrado")
    void deveMostrarMensagemErro400QuandoEstadoNaoEncontrado() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente()
                .comIdPais(pais.getId())
                .comIdEstado(999L).build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Estado não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país possui estado e id estado está nulo")
    void deveRetornarErro400QuandoPaisPossuiEstadoEIdEstadoEstaNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente()
                .comIdPais(pais.getId())
                .comIdEstado(null)
                .build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro 400 quando país possui estado e id estado está nulo")
    void deveMostrarMensagemErroQuandoPaisPossuiEstadoEIdEstadoEstaNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoClienteRequest novoClienteRequest = NovoClienteRequestBuilder.umCliente()
                .comIdPais(pais.getId())
                .comIdEstado(null)
                .build();

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoClienteRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    static Stream<Arguments> fornecerDadosParaErrosHttpStatus() {
        return Stream.of(
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEmail(""),
                        400,
                        "Email em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEmail("email_email"),
                        400,
                        "Email mal formado"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comNome(""),
                        400,
                        "Nome não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comSobreNome(""),
                        400,
                        "Sobrenome não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCpfCnpj("123456"),
                        400,
                        "CpfCnpj inválido"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEndereco(""),
                        400,
                        "Endereço não pode estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comComplemento(""),
                        400,
                        "Complemento não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCidade(""),
                        400,
                        "Cidade não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comIdPais(null),
                        400,
                        "IdPais não deve ser nulo"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comTelefone(""),
                        400,
                        "Telefone não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCep(""),
                        400,
                        "CEP não deve estar em branco"
                )
        );
    }

    static Stream<Arguments> fornecerDadosParaMensagensErro() {
        return Stream.of(
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEmail(""),
                        "email",
                        "não deve estar em branco",
                        "Email em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEmail("email_email"),
                        "email",
                        "deve ser um endereço de e-mail bem formado",
                        "Email mal formado"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comNome(""),
                        "nome",
                        "não deve estar em branco",
                        "Nome não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comSobreNome(""),
                        "sobreNome",
                        "não deve estar em branco",
                        "Sobrenome não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCpfCnpj("123"),
                        "cpfCnpj",
                        "inválido. Utilize o formato 999.999.999-99 para CPF ou 99.999.999/9999-99 para CNPJ",
                        "CPF ou CNPJ não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comEndereco(""),
                        "endereco",
                        "não deve estar em branco",
                        "Endereço não pode estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comComplemento(""),
                        "complemento",
                        "não deve estar em branco",
                        "Complemento não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCidade(""),
                        "cidade",
                        "não deve estar em branco",
                        "Cidade não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comIdPais(null),
                        "idPais",
                        "não deve ser nulo",
                        "IdPais não deve ser nulo"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comTelefone(""),
                        "telefone",
                        "não deve estar em branco",
                        "Telefone não deve estar em branco"
                ),
                Arguments.of(
                        NovoClienteRequestBuilder.umCliente().comCep(""),
                        "cep",
                        "não deve estar em branco",
                        "CEP não deve estar em branco"
                )
        );
    }
}