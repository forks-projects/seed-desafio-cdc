package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

    private Categoria categoria;

    private Autor autor;

    @BeforeEach
    public void criarDadosIniciais() {
        this.categoria = criarCategoria();
        this.autor = criarAutor();
    }

    @Test
    @DisplayName("Deve cadastrar livro com sucesso")
    void deveCadastrarLivroComSucesso() throws Exception {

        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando titulo em branco")
    void deveRetornarErro400QuandoTituloEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroSemTitulo(categoria, autor)
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando titulo em branco")
    void deveMostrarErroQuandoTituloEmBranco() throws Exception {

        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroSemTitulo(categoria, autor)
                .build();

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
        Livro livro = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor)
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor).build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro titulo cadastrado quando título já está cadastrado")
    void deveMostrarErroTituloCadastradoQuandoTituloJaEstaCadastrado() throws Exception {
        Livro livro = criarLivro(categoria, autor);
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor)
                .comTitulo(livro.getTitulo())
                .comIsbn("123")
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("titulo"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("já está cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando resumo em branco")
    void deveRetornarErro400QuandoResumoEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor)
                .comResumo("")
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando resumo em branco")
    void deveMostrarErroQuandoResumoEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor)
                .comResumo("")
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroComResumoComMaisDe500Caracteres(categoria, autor)
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando resumo com mais de 500 caracteres")
    void deveMostrarErroQuandoResumoComMais500Caracteres() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroComResumoComMaisDe500Caracteres(categoria, autor)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comPreco(null)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando preço é nulo")
    void deveMostrarErroQuandoPrecoNulo() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comPreco(null)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comPreco(BigDecimal.TEN)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando preço menor que 20")
    void deveMostrarErroQuandoPrecoMenor20() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comPreco(BigDecimal.TEN)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("preco"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ser maior que ou igual à 20"));
    }

    @Test
    @DisplayName("Deve retornar erro 400 quando número pagina maior ou igual que 100")
    void deveRetornarErro400QuandoNumeroPaginaMenorQue100() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comNumeroPaginas(99)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando número página maior ou igual a que 100")
    void deveMostrarErroQuandoNumeroPaginaMenorQue100() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comNumeroPaginas(99)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIsbn("")
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando isbn em branco")
    void deveMostrarErroQuandoIsbnEmBranco() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIsbn("")
                .build();

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
        Livro livro = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .build().toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comTitulo("Título 2")
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando isbn já está cadastrado")
    void deveMostrarErroQuandoIsbnJaEstaCadastrado() throws Exception {
        Livro livro = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .build().toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comTitulo("Título 2")
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comDataPublicacao(null)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando dataPublicacao está nula")
    void deveMostrarErroQuandoDataPublicacaoNula() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comDataPublicacao(null)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comDataPublicacao(LocalDate.now().minusMonths(1))
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando dataPublicacao está no passado")
    void deveMostrarErroQuandoDataPublicacaoNoPassado() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comDataPublicacao(LocalDate.now().minusDays(30))
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdCategoria(null)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando categoria está nula")
    void deveMostrarErroQuandoCategoriaNula() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdCategoria(null)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdCategoria(9999L)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando categoria não encontrada")
    void deveMostrarErroQuandoCategoriaNaoEncontrada() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdCategoria(9999L)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdAutor(null)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando autor nulo")
    void deveMostrarErroQuandoAutorNUlo() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdAutor(null)
                .build();

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
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdAutor(9999L)
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve mostrar erro quando autor não encontrado")
    void deveMostrarErroQuandoAutorNaoEncontrado() throws Exception {
        NovoLivroRequest novoLivroRequest = NovoLivroRequestDataBuilder
                .umLivroCustomizado(categoria, autor)
                .comIdAutor(9999L)
                .build();

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
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        return autorRepository.save(autor);
    }

    private Livro criarLivro(Categoria categoria, Autor autor) {
        Livro livro = NovoLivroRequestDataBuilder.umLivroCustomizado(categoria, autor).build().toModel(categoriaRepository, autorRepository);
        return livroRepository.save(livro);
    }
}
