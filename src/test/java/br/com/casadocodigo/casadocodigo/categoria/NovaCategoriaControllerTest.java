package br.com.casadocodigo.casadocodigo.categoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovaCategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    @DisplayName("Deve cadastrar uma categoria com sucesso")
    void deveCadastrarCategoriaComSucesso() throws Exception {
        NovaCategoriaRequest novaCategoriaRequest = NovaCategoriaRequestDataBuilder.umaCategoria().build();

        mockMvc.perform(post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando a categoria já existir")
    void deveRetornarErroSeCategoriaJaExistir() throws Exception {
        NovaCategoriaRequest novaCategoriaRequest = NovaCategoriaRequestDataBuilder
                .umaCategoria().comNome("Drama").build();

        Categoria categoriaExistente = new Categoria(novaCategoriaRequest.getNome());
        categoriaRepository.save(categoriaExistente);

        mockMvc.perform(post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar mensagem de erro quando categoria já existir")
    void deveRetornarMensagemDeErroQuandoCategoriaJaExistir() throws Exception {
        NovaCategoriaRequest novaCategoriaRequest = NovaCategoriaRequestDataBuilder
                .umaCategoria().comNome("Drama").build();

        Categoria categoriaExistente = new Categoria(novaCategoriaRequest.getNome());
        categoriaRepository.save(categoriaExistente);

        mockMvc.perform(post("/v1/categorias")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("nome"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Categoria já está cadastrada"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidasCategoriasRequest")
    @DisplayName("Deve retornar erro e mensagem adequada para requisições inválidas")
    void deveRetornarErroParaRequisicoesInvalidas(
            NovaCategoriaRequest novaCategoriaRequest,
            int expectedStatus,
            String campo,
            String mensagemErro) throws Exception {

        mockMvc.perform(post("/v1/categorias")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.listaErros[0].campo").value(campo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    private static Stream<Object[]> provideInvalidasCategoriasRequest() {
        return Stream.of(
                new Object[]{
                        NovaCategoriaRequestDataBuilder.umaCategoriaComNomeEmBranco().build(),
                        400,
                        "nome",
                        "não deve estar em branco"
                },
                new Object[]{
                        NovaCategoriaRequestDataBuilder.umaCategoriaComNomeNulo().build(),
                        400,
                        "nome",
                        "não deve estar em branco"
                }
        );
    }
}
