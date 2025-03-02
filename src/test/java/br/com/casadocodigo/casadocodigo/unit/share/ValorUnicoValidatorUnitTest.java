package br.com.casadocodigo.casadocodigo.unit.share;

import br.com.casadocodigo.casadocodigo.categoria.Categoria;
import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import br.com.casadocodigo.casadocodigo.share.ValorUnicoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jdk.jfr.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValorUnicoValidatorUnitTest {
    private ValorUnicoValidator valorUnicoValidator;
    private EntityManager entityManager;
    private ConstraintValidatorContext context;
    private Query query;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        valorUnicoValidator = new ValorUnicoValidator();
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
        context = mock(ConstraintValidatorContext.class);
        ReflectionTestUtils.setField(valorUnicoValidator, "entityManager", entityManager);
    }

    @Test
    @DisplayName("Deve ser válido quando valor campo unico")
    void deveSerValidoQuandoValorCampoUnico() {
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        ValorUnicoLocal valorUnicoLocal = new ValorUnicoLocal();
        valorUnicoValidator.initialize(valorUnicoLocal);

        boolean resultado = valorUnicoValidator.isValid("nome", context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando valor campo já esta cadastrado")
    void deveSerInValidoQuandoValorCampoEstaCadastrado() {
        Categoria categoria = new Categoria("tecnologia");
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(categoria));
        ValorUnicoLocal valorUnicoLocal = new ValorUnicoLocal();
        valorUnicoValidator.initialize(valorUnicoLocal);

        boolean resultado = valorUnicoValidator.isValid("nome", context);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando encontrar mais de um valor campo registrado")
    void deveSerInValidoQuandoEncontrarMaisDeUmValorCampoRegistro() {
        Categoria categoria1 = new Categoria("tecnologia");
        Categoria categoria2 = new Categoria("hardware");
        when(entityManager.createQuery(contains("SELECT e FROM "))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(categoria1, categoria2));
        ValorUnicoLocal valorUnicoLocal = new ValorUnicoLocal();
        valorUnicoValidator.initialize(valorUnicoLocal);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> valorUnicoValidator.isValid("nome", context)
        );
        assertTrue(exception.getMessage().contains("Foi encontrado mais de um"));
    }
}

class ValorUnicoLocal implements ValorUnico {
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
        return "nome";
    }

    @Override
    public Class<?> classeDaEntidade() {
        return Category.class;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
