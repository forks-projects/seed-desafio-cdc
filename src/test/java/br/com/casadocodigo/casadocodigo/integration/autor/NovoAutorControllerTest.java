package br.com.casadocodigo.casadocodigo.integration.autor;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("integrationTest")
class NovoAutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("fornecerDadosParaCadastroComSucesso")
    @DisplayName("Deve cadastrar um autor com sucesso")
    void deveCadastrarAutorComSucesso(NovoAutorRequest novoAutorRequest, String descricaoTeste) throws Exception {
        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @MethodSource("fornecerDadosParaErros")
    @DisplayName("Deve retornar erro ao cadastrar autor com dados inválidos")
    void deveRetornarErroAoCadastrarAutorComDadosInvalidos(
            NovoAutorRequest novoAutorRequest, int statusEsperado, String nomeCampo, String descricaoErro, String descricaoTeste) throws Exception {
        mockMvc.perform(post("/v1/autores")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().is(statusEsperado))
                .andExpect(jsonPath("$.listaErros[0].campo").value(nomeCampo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(descricaoErro));
    }

    @Test
    @DisplayName("Deve retornar erro 400 com mensagem de erro ao tentar cadastrar um autor com e-mail já existente")
    void deveRetornarErro400ComMensagemDeErroAoTentarCadastrarUmAutorComEmailExistente() throws Exception {
        Autor autorExistente = new Autor("João Silva", "joao.silva@email.com", "Descrição existente.", LocalDateTime.now());
        autorRepository.save(autorExistente);

        NovoAutorRequest novoAutorRequest = NovoAutorRequestDataBuilder.umAutor()
                .comEmail("joao.silva@email.com")
                .build();

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("email"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("E-mail já está cadastrado"));
    }

    static Stream<Arguments> fornecerDadosParaCadastroComSucesso() {
        return Stream.of(
                Arguments.of(NovoAutorRequestDataBuilder.umAutor().build(), "Descrição padrão"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutor().comDescricao("a".repeat(399)).build(), "Descrição com 399 caracteres"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutor().comDescricao("a".repeat(400)).build(), "Descrição com 400 caracteres")
        );
    }

    static Stream<Arguments> fornecerDadosParaErros() {
        return Stream.of(
                Arguments.of(NovoAutorRequestDataBuilder.umAutorComNomeVazio().build(),
                        400,
                        "nome",
                        "não deve estar em branco",
                        "Nome vazio"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutorComEmailInvalido().build(),
                        400,
                        "email",
                        "deve ser um endereço de e-mail bem formado",
                        "E-mail inválido"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutorComEmailEmBranco().build(),
                        400,
                        "email",
                        "não deve estar em branco",
                        "E-mail em branco"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutor()
                                .comDescricao("a".repeat(401))
                                .build(),
                        400,
                        "descricao",
                        "o comprimento deve ser entre 0 e 400",
                        "Descrição maior que 400 caracteres"),
                Arguments.of(NovoAutorRequestDataBuilder.umAutorComDescricaoEmBranco().build(),
                        400,
                        "descricao",
                        "não deve estar em branco",
                        "Descrição em branco")
        );
    }
}
