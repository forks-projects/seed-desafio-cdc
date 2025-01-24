package br.com.casadocodigo.casadocodigo.pais;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PaisTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaisRepository paisRepository;

    @Test
    @DisplayName("Deve cadastrar Pais com sucesso")
    void deveCadastrarPaisComSucesso() throws Exception {
        NovoPaisRequest novoPaisRequest = new NovoPaisRequest("Brazil");

        mockMvc.perform(post("/v1/paises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPaisRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando cadastrar Pais com nome em branco")
    void deveRetornarErro400QuandoCadastrarPaisComNomeEmBranco() throws Exception {
        NovoPaisRequest novoPaisRequest = new NovoPaisRequest("");

        mockMvc.perform(post("/v1/paises")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPaisRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando cadastrar ais com Nome em branco")
    void deveMostrarMensagemDeErroQuandoCadastrarPaisComNomeEmBranco() throws Exception {
        NovoPaisRequest novoPaisRequest = new NovoPaisRequest("");

        mockMvc.perform(post("/v1/paises")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPaisRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("nome"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve estar em branco"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando nome Pais já está cadastrado")
    void deveRetornarErro400QuandoNomePaisJaEstaCadastrado() throws Exception {
        NovoPaisRequest novoPaisRequest = new NovoPaisRequest("Brasil");
        paisRepository.save(novoPaisRequest.toModel());

        mockMvc.perform(post("/v1/paises")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPaisRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando nome Pais já está cadastrado")
    void deveMostrarMensagemDeErroQuandoPaisJaEstaCadastrado() throws Exception {
        NovoPaisRequest novoPaisRequest = new NovoPaisRequest("Brasil");
        paisRepository.save(novoPaisRequest.toModel());

        mockMvc.perform(post("/v1/paises")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPaisRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("nome"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Pais já está cadastrado"));
    }

}