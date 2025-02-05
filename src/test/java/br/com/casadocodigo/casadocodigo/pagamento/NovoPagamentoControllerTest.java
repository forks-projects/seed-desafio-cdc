package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import br.com.casadocodigo.casadocodigo.categoria.NovaCategoriaRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.estado.NovoEstadoRequestBuilder;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.livro.NovoLivroRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NovoPagamentoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    @DisplayName("Deve ter pagamento com dados validos")
    void deveTerPagamentoComDadosValidos() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .comPreco(new BigDecimal("30"))
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comTotal(new BigDecimal("90"))
                .comItens(List.of(new NovoItemRequest(livro.getId(), 3)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest(name = "[{index}] {4}")
    @MethodSource("fornecerDadosParaMensagensErro")
    @DisplayName("Deve mostrar mensagem de erro quando cadastrar pagamento com dados invalidos")
    void deveMostrarMensagemErroQuandoCadastrarPagamentoComDadosInvalidos(NovoPagamentoRequestBuilder novoPagamentoRequestBuilder, int statusEsperado, String nomeCampo, String mensagemErro, String descricaoErro) throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = novoPagamentoRequestBuilder
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of(new NovoItemRequest(livro.getId(), 2)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().is(statusEsperado))
                .andExpect(jsonPath("$.listaErros[0].campo").value(nomeCampo))
                .andExpect(jsonPath("$.listaErros[0].erro").value(mensagemErro));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando idPais nulo")
    void deveMostrarMensagemDeErroQuandoIdPaisNUlo() throws Exception {
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(null)
                .comItens(List.of(new NovoItemRequest(livro.getId(), 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("idPais"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país não encontrado")
    void deveMostrarMensagemErroQuandoPaisNaoEncontrado() throws Exception {
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(999L)
                .comItens(List.of(new NovoItemRequest(livro.getId(), 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("idPais"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("País não encontrado"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando estado não encontrado")
    void deveMostrarMensagemErroQuandoEstadoNaoEncontrado() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(999L)
                .comItens(List.of(new NovoItemRequest(livro.getId(), 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não encontrado"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país possui estado e id estado está nulo")
    void deveMostrarMensagemErroQuandoPaisPossuiEstadoEIdEstadoEstaNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(null)
                .comItens(List.of(new NovoItemRequest(livro.getId(), 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando país possui estado e id estado pertence a outro país")
    void deveMostrarMensagemDeErroQuandoPaisPossuiEstadoEIdEstadoPertenceAOutroPais() throws Exception {
        Pais pais1 = new Pais("Brasil");
        paisRepository.save(pais1);
        Pais pais2 = new Pais("Argentina");
        paisRepository.save(pais2);
        Estado estado1 = NovoEstadoRequestBuilder.umEstado().comIdPais(pais1.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado1);
        Estado estado2 = NovoEstadoRequestBuilder.umEstado().comNome("Catamarca").comIdPais(pais2.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado2);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais2.getId())
                .comIdEstado(estado1.getId())
                .comItens(List.of(new NovoItemRequest(livro.getId(), 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("idEstado"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não pertence a este País"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com item nulo")
    void deveMostrarMensagemDeErroQuandoPagamentoComItemNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(null)
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com menos de 1 item")
    void deveMostrarMensagemDeErroQuandoPagamentoComMenosDeUmItem() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of())
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ter pelo menos 1 item"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com item com id livro nulo")
    void deveMostrarMensagemDeErroQuandoPagamentoComItemComIdLivroNulo() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of(new NovoItemRequest(null, 2)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest[0].livroId"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com item com id livro não encontrado")
    void deveMostrarMensagemDeErroQuandoPagamentoComItemComIdLivroNaoEncontrado() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of(new NovoItemRequest(999L, 1)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest[0].livroId"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("Livro não encontrado"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com quantidade nula")
    void deveMostrarMensagemDeErroQuandoPagamentoComItemComQuantidadeNula() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of(new NovoItemRequest(livro.getId(), null)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest[0].quantidade"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("não deve ser nulo"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com quantidade maior que zero")
    void deveMostrarMensagemDeErroQuandoPagamentoComItemComQuantidadeMaiorQueZero() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comItens(List.of(new NovoItemRequest(livro.getId(), 0)))
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("itensRequest[0].quantidade"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("deve ser maior que ou igual à 1"));
    }

    @Test
    @DisplayName("Deve mostrar mensagem de erro quando pagamento com total calculado diferente do total enviado")
    void deveMostrarMensagemDeErroQuandoPagamentoComTotalCalculadoServidorDiferenteTotalEnviado() throws Exception {
        Pais pais = new Pais("Brasil");
        paisRepository.save(pais);
        Estado estado = NovoEstadoRequestBuilder.umEstado().comIdPais(pais.getId()).build().toModel(paisRepository);
        estadoRepository.save(estado);
        Categoria categoria = NovaCategoriaRequestDataBuilder.umaCategoria().build().toModel();
        categoriaRepository.save(categoria);
        Autor autor = NovoAutorRequestDataBuilder.umAutor().build().toModel();
        autorRepository.save(autor);
        Livro livro1 = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .comPreco(new BigDecimal("20"))
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro1);
        Livro livro2 = NovoLivroRequestDataBuilder.umLivro()
                .comIdCategoria(categoria.getId())
                .comIdAutor(autor.getId())
                .comTitulo("Livro 2")
                .comIsbn("123-123-123")
                .comPreco(new BigDecimal("30"))
                .build()
                .toModel(categoriaRepository, autorRepository);
        livroRepository.save(livro2);

        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(pais.getId())
                .comIdEstado(estado.getId())
                .comTotal(new BigDecimal("10"))
                .comItens(List.of(
                        new NovoItemRequest(livro1.getId(), 1),
                        new NovoItemRequest(livro2.getId(), 2))
                )
                .build();

        mockMvc.perform(post("/v1/pagamentos")
                        .header("Accept-Language", "pt-BR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoPagamentoRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listaErros[0].campo").value("total"))
                .andExpect(jsonPath("$.listaErros[0].erro").value("diferente do valor total calculado"));
    }

    static Stream<Arguments> fornecerDadosParaMensagensErro() {
        return Stream.of(
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail(""),
                        400,
                        "email",
                        "não deve estar em branco",
                        "Email em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEmail("email_email"),
                        400,
                        "email",
                        "deve ser um endereço de e-mail bem formado",
                        "Email mal formado"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comNome(""),
                        400,
                        "nome",
                        "não deve estar em branco",
                        "Nome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comSobreNome(""),
                        400,
                        "sobreNome",
                        "não deve estar em branco",
                        "Sobrenome não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCpfCnpj("123"),
                        400,
                        "cpfCnpj",
                        "inválido. Utilize o formato 999.999.999-99 para CPF ou 99.999.999/9999-99 para CNPJ",
                        "CPF ou CNPJ não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comEndereco(""),
                        400,
                        "endereco",
                        "não deve estar em branco",
                        "Endereço não pode estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comComplemento(""),
                        400,
                        "complemento",
                        "não deve estar em branco",
                        "Complemento não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCidade(""),
                        400,
                        "cidade",
                        "não deve estar em branco",
                        "Cidade não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comTelefone(""),
                        400,
                        "telefone",
                        "não deve estar em branco",
                        "Telefone não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comCep(""),
                        400,
                        "cep",
                        "não deve estar em branco",
                        "CEP não deve estar em branco"
                ),
                Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comTotal(null),
                        400,
                        "total",
                        "não deve ser nulo",
                        "Total não deve ser nulo"
                ),Arguments.of(
                        NovoPagamentoRequestBuilder.umPagamento().comTotal(BigDecimal.ZERO),
                        400,
                        "total",
                        "deve ser maior que ou igual à 1",
                        "Total não deve ser menor que 1"
                )
        );
    }
}
