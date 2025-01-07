package br.com.casadocodigo.casadocodigo.autor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovoAutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    @DisplayName("Deve cadastrar um autor com sucesso")
    void deveCadastrarAutorComSucesso() throws Exception {
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "João Silva",
                "joao.silva@example.com",
                "Autor de ficção científica."
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 409 ao tentar cadastrar um autor com e-mail já existente")
    void deveRetornarErroSeEmailJaExistir() throws Exception {
        Autor autorExistente = new Autor("João Silva", "joao.silva@example.com", "Descrição existente.", LocalDateTime.now());
        autorRepository.save(autorExistente);

        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "João Oliveira Silva",
                "joao.silva@example.com",
                "Outra descrição."
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar cadastrar um autor com nome vazio")
    void deveRetornarErroSeNomeForVazio() throws Exception {
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "",
                "maria.oliveira@example.com",
                "Descrição válida."
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar cadastrar um autor com e-mail inválido")
    void deveRetornarErroSeEmailForInvalido() throws Exception {
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "Maria Oliveira",
                "email-invalido",
                "Descrição válida."
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar cadastrar um autor com e-mail em branco")
    void deveRetornarErroSeEmailEmBranco() throws Exception {
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "Maria Oliveira",
                "",
                "Descrição válida."
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar cadastrar um autor com descrição maior que 400 caracteres")
    void deveRetornarErroSeDescricaoForMuitoLonga() throws Exception {
        String descricaoMuitoLonga = "a".repeat(401);
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "Maria Oliveira",
                "maria.oliveira@example.com",
                descricaoMuitoLonga
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao tentar cadastrar um autor com descrição em branco")
    void deveRetornarErroSeDescricaoEmBranco() throws Exception {
        String descricaoEmBranco = "";
        NovoAutorRequest novoAutorRequest = new NovoAutorRequest(
                "Maria Oliveira",
                "maria.oliveira@example.com",
                descricaoEmBranco
        );

        mockMvc.perform(post("/v1/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoAutorRequest)))
                .andExpect(status().isBadRequest());
    }
}
