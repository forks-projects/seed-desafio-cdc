package br.com.casadocodigo.casadocodigo.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import br.com.casadocodigo.casadocodigo.categoria.NovaCategoriaRequestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Tag("integrationTest")
class BuscaLivrosControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Livro livro;

    @Test
    @DisplayName("Deve retornar os detalhes de um livro existente")
    void deveRetornarDetalhesDeLivroExistente() throws Exception {
        criarLivro();

        mockMvc.perform(get("/v1/livros/"+livro.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is(livro.getTitulo())))
                .andExpect(jsonPath("$.resumo", is(livro.getResumo())))
                .andExpect(jsonPath("$.sumario", is(livro.getSumario())))
                .andExpect(jsonPath("$.preco", is(livro.getPreco().doubleValue())))
                .andExpect(jsonPath("$.numeroPaginas", is(livro.getNumeroPaginas())))
                .andExpect(jsonPath("$.isbn", is(livro.getIsbn())))
                .andExpect(jsonPath("$.autor.nome", is(livro.getAutor().getNome())))
                .andExpect(jsonPath("$.autor.descricao", is(livro.getAutor().getDescricao())))
                .andExpect(jsonPath("$.categoria.nome", is(livro.getCategoria().getNome())));
    }

    @Test
    @DisplayName("Deve retornar 404 quando o livro não for encontrado")
    void deveRetornar404SeLivroNaoForEncontrado() throws Exception {
        mockMvc.perform(get("/v1/livros/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.erro", is("Livro não encontrado")));
    }

    @Test
    @DisplayName("Deve retornar uma página de livros com sucesso")
    void deveRetornarPaginaDeLivrosComSucesso() throws Exception {
        criarLivros();

        mockMvc.perform(get("/v1/livros")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "titulo,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].titulo", is("Título do Livro 1")))
                .andExpect(jsonPath("$.content[1].id", notNullValue()))
                .andExpect(jsonPath("$.content[1].titulo", is("Título do Livro 2")))
                .andExpect(jsonPath("$.page.size", is(2)))
                .andExpect(jsonPath("$.page.number", is(0)))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andExpect(jsonPath("$.page.totalPages", is(1)));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando não há livros no banco de dados")
    void deveRetornarPaginaVaziaQuandoNaoHaLivrosNoBanco() throws Exception {
        mockMvc.perform(get("/v1/livros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", Matchers.is(hasSize(0))))
                .andExpect(jsonPath("$.page.totalElements").value(0))
                .andExpect(jsonPath("$.page.totalPages").value(0));
    }

    public void criarLivro() {
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdAutor(autor.getId())
                .comIdCategoria(categoria.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        this.livro = livroRepository.save(livro);
    }

    public void criarLivros() {
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Livro livro1 = NovoLivroRequestDataBuilder.umLivro()
                .comTitulo("Título do Livro 1")
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro1);

        Livro livro2 = NovoLivroRequestDataBuilder.umLivro()
                .comTitulo("Título do Livro 2")
                .comIsbn("012-345-678")
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro2);
    }

}