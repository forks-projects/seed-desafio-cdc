package br.com.casadocodigo.casadocodigo.unit.livro;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.categoria.CategoriaRepository;
import br.com.casadocodigo.casadocodigo.integration.livro.NovoLivroRequestDataBuilder;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.NovoLivroRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NovoLivroRequestUnitTest {
    @Test
    @DisplayName("Deve mapear request para model")
    void deveMapearRequestParaModel() {
        NovoLivroRequest request = NovoLivroRequestDataBuilder.umLivro().build();
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        AutorRepository autorRepository = mock(AutorRepository.class);
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        when(categoriaRepository.findById(request.getIdCategoria())).thenReturn(Optional.of(categoria));
        when(autorRepository.findById(request.getIdAutor())).thenReturn(Optional.of(autor));

        Livro livro = request.toModel(categoriaRepository, autorRepository);

        assertNotNull(livro);
        assertEquals(livro.getTitulo(), request.getTitulo());
        assertEquals(livro.getResumo(), request.getResumo());
        assertEquals(livro.getSumario(), request.getSumario());
        assertEquals(livro.getPreco(), request.getPreco());
        assertEquals(livro.getNumeroPaginas(), request.getNumeroPaginas());
        assertEquals(livro.getIsbn(), request.getIsbn());
        assertEquals(livro.getDataPublicacao(), request.getDataPublicacao());
        assertNotNull(livro.getCategoria());
        assertNotNull(livro.getAutor());
    }

    @Test
    @DisplayName("Deve lançar exceção quando categoria não for encontrada")
    void deveLancarExcecaoQuandoCategoriaNaoForEncontrado() {
        NovoLivroRequest request = NovoLivroRequestDataBuilder.umLivro().build();
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        AutorRepository autorRepository = mock(AutorRepository.class);
        when(categoriaRepository.findById(request.getIdCategoria())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(categoriaRepository, autorRepository)
        );
        assertEquals("400 BAD_REQUEST \"Categoria não encontrada\"", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não for encontrado")
    void deveLancarExcecaoQuandoAutorNaoForEncontrado() {
        NovoLivroRequest request = NovoLivroRequestDataBuilder.umLivro().build();
        CategoriaRepository categoriaRepository = mock(CategoriaRepository.class);
        AutorRepository autorRepository = mock(AutorRepository.class);
        Categoria categoria = new Categoria("Tecnologia");
        when(categoriaRepository.findById(request.getIdCategoria())).thenReturn(Optional.of(categoria));
        when(autorRepository.findById(request.getIdAutor())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> request.toModel(categoriaRepository, autorRepository)
        );
        assertEquals("400 BAD_REQUEST \"Autor não encontrado\"", exception.getMessage());
    }
}
