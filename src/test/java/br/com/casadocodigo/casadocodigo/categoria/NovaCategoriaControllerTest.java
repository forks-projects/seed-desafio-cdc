package br.com.casadocodigo.casadocodigo.categoria;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        NovaCategoriaRequest novaCategoriaRequest = new NovaCategoriaRequest("Ficção");

        mockMvc.perform(post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 409 quando a categoria já existir")
    void deveRetornarErroSeCategoriaJaExistir() throws Exception {
        Categoria categoriaExistente = new Categoria("Drama");
        categoriaRepository.save(categoriaExistente);

        NovaCategoriaRequest novaCategoriaRequest = new NovaCategoriaRequest("Drama");

        mockMvc.perform(post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando o nome da categoria for inválido")
    void deveRetornarErroSeNomeForInvalido() throws Exception {
        NovaCategoriaRequest novaCategoriaRequest = new NovaCategoriaRequest(""); 

        mockMvc.perform(post("/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaCategoriaRequest)))
                .andExpect(status().isBadRequest());
    }
}
