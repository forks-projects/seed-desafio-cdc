package br.com.casadocodigo.casadocodigo.integration.estado;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.estado.NovoEstadoRequest;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Tag("integrationTest")
class NovoEstadoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    private Pais pais;

    @BeforeEach
    public void setup() {
        pais = new Pais("Brasil");
        paisRepository.save(pais);
    }

    @Test
    @DisplayName("Deve cadastrar estado com sucesso")
    void deveCadastrarEstadoComSucesso() throws Exception {
        NovoEstadoRequest novoEstadoRequest = new NovoEstadoRequest("São Paulo", pais.getId());

        mockMvc.perform(post("/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("fornecerDadosParaErrosHttpStatus")
    @DisplayName("Deve retornar erro 400 quando cadastrar livro com dados invalidos")
    void deveRetornarErro400QuandoCadastrarEstadoComDadosInvalidos(NovoEstadoRequestBuilder novoEstadoRequestBuilder, int statusEsperado, String descricaoErro) throws Exception {
        NovoEstadoRequest novoEstadoRequest = novoEstadoRequestBuilder.build();

        mockMvc.perform(post("/v1/estados")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(status().is(statusEsperado));
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("fornecerDadosParaMensagensErro")
    @DisplayName("Deve mostrar mensagem de erro quando estado com dados invalidos")
    void deveMostrarMensagemDeErroQuandoEstadoComDadosInvalidos(NovoEstadoRequestBuilder novoEstadoRequestBuilder, String nomeCampo, String mensagemErro, String descricaoErro) throws Exception {
        NovoEstadoRequest novoEstadoRequest = (Objects.equals(nomeCampo, "idPais"))?
                novoEstadoRequestBuilder.build():
                novoEstadoRequestBuilder.comIdPais(pais.getId()).build();

        mockMvc.perform(post("/v1/estados")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value(nomeCampo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando país não encontrado")
    void deveRetornarErro400QuandoPaisNaoEncontrado() throws Exception {
        NovoEstadoRequest novoEstadoRequest = new NovoEstadoRequest("Rio de Janeiro", 999L);

        mockMvc.perform(post("/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país não encontrado")
    void deveMostrarMensagemErro400QuandoPaisNaoEncontrado() throws Exception {
        NovoEstadoRequest novoEstadoRequest = new NovoEstadoRequest("Rio de Janeiro", 999L);

        mockMvc.perform(post("/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idPais"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("País não encontrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando nome do Estado já está cadastrado")
    void deveRetornarErro400QuandoNomeEstadoJaEstaCadastrado() throws Exception {
        NovoEstadoRequest novoEstadoRequest = new NovoEstadoRequest("Rio de Janeiro", pais.getId());
        Estado estado = novoEstadoRequest.toModel(paisRepository);
        estadoRepository.save(estado);

        mockMvc.perform(post("/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando nome do estado já está cadastrado")
    void deveMostrarMensagemErroQuandoNomeDoEstadoJaEstaCadastrado() throws Exception {
        NovoEstadoRequest novoEstadoRequest = new NovoEstadoRequest("Rio de Janeiro", pais.getId());
        Estado estado = novoEstadoRequest.toModel(paisRepository);
        estadoRepository.save(estado);

        mockMvc.perform(post("/v1/estados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEstadoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("nome"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Estado já está cadastrado"));
    }

    static Stream<Arguments> fornecerDadosParaErrosHttpStatus() {
            return Stream.of(
                    Arguments.of(
                            NovoEstadoRequestBuilder.umEstado().comNome(""),
                            400,
                            "Título em branco"
                    ),
                    Arguments.of(
                            NovoEstadoRequestBuilder.umEstado().comIdPais(null),
                            400,
                            "Id do Pais nulo"
                    )
            );
    }

    static Stream<Arguments> fornecerDadosParaMensagensErro() {
            return Stream.of(
                    Arguments.of(
                            NovoEstadoRequestBuilder.umEstado().comNome(""),
                            "nome",
                            "não deve estar em branco",
                            "Nome em branco"
                    ),
                    Arguments.of(
                            NovoEstadoRequestBuilder.umEstado().comIdPais(null),
                            "idPais",
                            "não deve ser nulo",
                            "Id do Pais nulo"
                    )
            );
    }

}