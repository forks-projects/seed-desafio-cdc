package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NovoLivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    @DisplayName("Deve cadastrar livro com sucesso")
    void deveCadastrarLivroComSucesso() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando titulo em branco")
    void deveRetornarErro400QuandoTituloEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando titulo em branco")
    void deveMostrarErroQuandoTituloEmBranco() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("titulo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve estar em branco"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando título já cadastrado")
    void deveRetornarErro400QuandoTituloJaCadastrado() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();
        Livro livro = new Livro("Título do Livro", "resumo", "sumário do livro", new BigDecimal("20"), 100, "123-456-789", LocalDate.now().plusDays(1), categoria, autor);
        livroRepository.save(livro);

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro titulo cadastrado quando título já está cadastrado")
    void deveMostrarErroTituloCadastradoQuandoTituloJaEstaCadastrado() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();
        criarLivro(categoria, autor);

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "012-345-678",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("titulo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("já está cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando resumo em branco")
    void deveRetornarErro400QuandoResumoEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando resumo em branco")
    void deveMostrarErroQuandoResumoEmBranco() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "",
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("resumo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve estar em branco"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando resumo com mais de 500 caracteres")
    void deveRetornarErro400QuandoResumoComMaisDe500Caracteres() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "a".repeat(501),
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando resumo com mais de 500 caracteres")
    void deveMostrarErroQuandoResumoComMais500Caracteres() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "a".repeat(501),
                "Sumário do livro",
                new BigDecimal("25.50"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("resumo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("o comprimento deve ser entre 0 e 500"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando preço nulo")
    void deveRetornarErro400QuandoPrecoNulo() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                null,
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando preço é nulo")
    void deveMostrarErroQuandoPrecoNulo() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                null,
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("preco"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando preço menor que 20")
    void deveRetornarErro400QuandoPrecoMenor20() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("19"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando preço menor que 20")
    void deveMostrarErroQuandoPrecoMenor20() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("19"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("preco"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ser maior que ou igual à 20"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando número pagina maior ou igual que 100")
    void deveRetornarErro400QuandoNumeroPaginaMaiorOuIgualA100() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                99,
                "123-456-789",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando número página maior ou igual a que 100")
    void deveMostrarErroQuandoNumeroPaginaMaiorOuIgualA100() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                99,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("numeroPaginas"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ser maior que ou igual à 100"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando isbn em branco")
    void deveRetornarErro400QuandoIsbnEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                100,
                "",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando nisbn em branco")
    void deveMostrarErroQuandoIsbnEmBranco() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                100,
                "",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("isbn"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve estar em branco"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando isbn já está cadastrado")
    void deveRetornarErro400QuandoIsbnDeveSerUnico() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();
        Livro livro = criarLivro(categoria, autor);

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro novo",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    private Livro criarLivro(Categoria categoria, Autor autor) {
        Livro livro = new Livro("Título do Livro", "resumo", "sumário do livro", new BigDecimal("20"), 100, "123-456-789", LocalDate.now().plusDays(1), categoria, autor);
        return livroRepository.save(livro);
    }

    @Test
    @DisplayName("Deve mostrar erro quando isbn já está cadastrado")
    void deveMostrarErroQuandoIsbnJaEstaCadastrado() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();
        Livro livro = new Livro("Título do Livro", "resumo", "sumário do livro", new BigDecimal("20"), 100, "123-456-789", LocalDate.now().plusDays(1), categoria, autor);
        livroRepository.save(livro);

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Título do Livro Novo",
                "Resumo do livro com menos de 500 caracteres.",
                "Sumário do livro",
                new BigDecimal("20"),
                100,
                "123-456-789",
                LocalDate.now().plusDays(1),
                autor.getId(),
                categoria.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("isbn"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("já está cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando dataPublicacao está nula")
    void deveRetornarErro400QuandoDataPublicacaoNula() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                null,
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando dataPublicacao está nula")
    void deveMostrarErroQuandoDataPublicacaoNula() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                null,
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("dataPublicacao"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando dataPublicacao está no passado")
    void deveRetornarErro400QuandoDataPublicacaoNoPassado() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().minusDays(1),
                1L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando dataPublicacao está no passado")
    void deveMostrarErroQuandoDataPublicacaoNoPassado() throws Exception {
        Categoria categoria = criarCategoria();
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().minusDays(1),
                categoria.getId(),
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("dataPublicacao"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ser uma data futura"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando categoria está nula")
    void deveRetornarErro400QuandoCategoriaNula() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                null,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando categoria está nula")
    void deveMostrarErroQuandoCategoriaNula() throws Exception {
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                null,
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idCategoria"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando categoria não encontrada")
    void deveRetornarErro400QuandoCategoriaNaoEncontrada() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                99L,
                1L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando categoria não encontrada")
    void deveMostrarErroQuandoCategoriaNaoEncontrada() throws Exception {
        Autor autor = criarAutor();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                99L,
                autor.getId()
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idCategoria"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Categoria não encontrada"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando autor nulo")
    void deveRetornarErro400QuandoAutorNulo() throws Exception {
        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                1L,
                null
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando autor nulo")
    void deveMostrarErroQuandoAutorNUlo() throws Exception {
        Categoria categoria = criarCategoria();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                categoria.getId(),
                null
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idAutor"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando autor não encontrado")
    void deveRetornarErro400QuandoAutorNaoEncontrado() throws Exception {
        Categoria categoria = criarCategoria();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                categoria.getId(),
                99L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando autor não encontrado")
    void deveMostrarErroQuandoAutorNaoEncontrado() throws Exception {
        Categoria categoria = criarCategoria();

        NovoLivroRequest novoLivroRequest = new NovoLivroRequest(
                "Livro Teste",
                "Resumo Teste",
                "Sumário Teste",
                new BigDecimal("30.00"),
                150,
                "123-123-123",
                LocalDate.now().plusDays(1L),
                categoria.getId(),
                99L
        );

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idAutor"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Autor não encontrado"));
    }

    private Categoria criarCategoria() {
        return categoriaRepository.save(new Categoria("categoria 1"));
    }

    private Autor criarAutor() {
        Autor autor = new Autor("João Silva",
                "joao.silva@example.com",
                "Descrição existente.",
                LocalDateTime.now());
        return autorRepository.save(autor);
    }
}
