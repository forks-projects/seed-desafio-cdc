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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

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

    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("fornecerDadosParaErrosHttpStatus")
    @DisplayName("Deve retornar erro 400 quando cadastrar livro com dados invalidos")
    void deveRetornarErro400QuandoTituloEmBranco(NovoLivroRequestDataBuilder novoLivroRequestDataBuilder, int statusEsperado, String descricaoErro) throws Exception {
        NovoLivroRequest novoLivroRequest = novoLivroRequestDataBuilder
                .comIdAutor(categoria.getId())
                .comIdAutor(autor.getId())
                .build();

        mockMvc.perform(post("/v1/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest(name = "[{index}] {3}")
    @MethodSource("fornecerDadosParaMensagensErro")
    @DisplayName("Deve mostrar mensagem com a descrição do erro")
    void deveMostrarMensagemComDescricaoDoErro(NovoLivroRequestDataBuilder novoLivroRequestDataBuilder, String campo, String mensagemErro, String descricaoErro) throws Exception {
        NovoLivroRequest novoLivroRequest = novoLivroRequestDataBuilder
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build();

        mockMvc.perform(post("/v1/livros")
                .header("Accept-Language", "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoLivroRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value(campo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
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

    static Stream<Arguments> fornecerDadosParaErrosHttpStatus() {
        return Stream.of(
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivroSemTituloCategoriaAutor(),
                        400,
                        "Título em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comResumo(""),
                        400,
                        "Resumo em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comResumo("a".repeat(501)),
                        400,
                        "Resumo com mais 500 caracteres"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comPreco(null),
                        400,
                        "Preço nulo"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comPreco(new BigDecimal("19")),
                        400,
                        "Preço menor que 20"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comNumeroPaginas(99),
                        400,
                        "Número página menor que 100"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comIsbn(""),
                        400,
                        "Isbn em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comDataPublicacao(null),
                        400,
                        "Data publicação nula"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comDataPublicacao(LocalDate.now().minusMonths(1)),
                        400,
                        "Data publicação no passado"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comIdCategoria(null),
                        400,
                        "Categoria nula"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comIdAutor(null),
                        400,
                        "Autor nulo"
                )
        );
    }

    static Stream<Arguments> fornecerDadosParaMensagensErro() {
        return Stream.of(
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivroSemTituloCategoriaAutor(),
                        "titulo",
                        "não deve estar em branco",
                        "Título em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comResumo(""),
                        "resumo",
                        "não deve estar em branco",
                        "Resumo em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comResumo("a".repeat(501)),
                        "resumo",
                        "o comprimento deve ser entre 0 e 500",
                        "Resumo com mais 500 caracteres"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comPreco(null),
                        "preco",
                        "não deve ser nulo",
                        "Preço nulo"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comPreco(new BigDecimal("19")),
                        "preco",
                        "deve ser maior que ou igual à 20",
                        "Preço menor que 20"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comNumeroPaginas(99),
                        "numeroPaginas",
                        "deve ser maior que ou igual à 100",
                        "Número página menor que 100"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comIsbn(""),
                        "isbn",
                        "não deve estar em branco",
                        "Isbn em branco"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comDataPublicacao(null),
                        "dataPublicacao",
                        "não deve ser nulo",
                        "Data publicação nula"
                ),
                Arguments.of(
                        NovoLivroRequestDataBuilder.umLivro().comDataPublicacao(LocalDate.now().minusMonths(1)),
                        "dataPublicacao",
                        "deve ser uma data futura",
                        "Data publicação no passado"
                )
        );
    }
}
