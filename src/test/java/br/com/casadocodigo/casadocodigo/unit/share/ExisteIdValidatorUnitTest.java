package br.com.casadocodigo.casadocodigo.unit.share;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.pagamento.Item;
import br.com.casadocodigo.casadocodigo.share.ExisteId;
import br.com.casadocodigo.casadocodigo.share.ExisteIdValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExisteIdValidatorUnitTest {

    @InjectMocks
    private ExisteIdValidator existeIdValidator;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private Query query;

    @Test
    @DisplayName("Deve ser válido quando id não existe")
    void deveSerValidoQuandoIdNaoExiste() {
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        ExistedIdLocal existedIdLocal = new ExistedIdLocal();
        existeIdValidator.initialize(existedIdLocal);

        boolean resultado = existeIdValidator.isValid("livroId", context);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando id existe")
    void deveSerInvalidoQuandoIdExiste() {
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        Livro livro = new Livro("Clean Code",
                "Mesmo um código ruim pode funcionar",
                "Sumario Livro",
                new BigDecimal("100.00"),
                100,
                "abc-123-cba",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
        Item item1 = new Item(livro, 2);

        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(item1));
        ExistedIdLocal existedIdLocal = new ExistedIdLocal();
        existeIdValidator.initialize(existedIdLocal);

        boolean resultado = existeIdValidator.isValid("livroId", context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando existir mais de um id")
    void deveSerInvalidoQuandoExistirMaisDeUmId() {
        Categoria categoria = new Categoria("Tecnologia");
        Autor autor = new Autor(" Robert C. Martin", "email@email.com", "esacritor", LocalDateTime.now());
        Livro livro = new Livro("Clean Code",
                "Mesmo um código ruim pode funcionar",
                "Sumario Livro",
                new BigDecimal("100.00"),
                100,
                "abc-123-cba",
                LocalDate.now().plusMonths(1L),
                categoria,
                autor);
        Item item1 = new Item(livro, 2);
        Item item2 = new Item(livro, 2);
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(item1, item2));
        ExistedIdLocal existedIdLocal = new ExistedIdLocal();
        existeIdValidator.initialize(existedIdLocal);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            existeIdValidator.isValid("livroId", context);
        });

        assertTrue(exception.getMessage().contains("aconteceu algo estranho"));
    }


}

class ExistedIdLocal implements ExisteId {

    @Override
    public String message() {
        return "";
    }

    @Override
    public Class<?>[] groups() {
        return new Class[0];
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }

    @Override
    public String nomeDoCampo() {
        return "livroId";
    }

    @Override
    public Class<?> classeDaEntidade() {
        return Item.class;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}