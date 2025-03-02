package br.com.casadocodigo.casadocodigo.unit.share;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.integration.pagamento.NovoPagamentoRequestBuilder;
import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.share.EstadoValidoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstadoValidoValidatorUnitTest {
    private EntityManager entityManager;
    private ConstraintValidatorContext context;
    private ConstraintViolationBuilder violationBuilder;
    private EstadoValidoValidator estadoValidoValidator;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        entityManager = mock(EntityManager.class);
        context = mock(ConstraintValidatorContext.class);
        violationBuilder = mock(ConstraintViolationBuilder.class);
        estadoValidoValidator = new EstadoValidoValidator();
        Field entityManagerField = EstadoValidoValidator.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(estadoValidoValidator, entityManager);
    }

    @Test
    @DisplayName("Deve ser inválido quando país tem estados, mas idEstado é nulo")
    void deveSerInvalidoQuandoPaisTemEstadosMasIdEstadoNulo() {
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento()
                .comIdEstado(null)
                .build();

        Pais pais = new Pais("Brasil");
        Estado estadoCadastrado = new Estado("São Paulo", pais);
        Query query = mock(Query.class);
        NodeBuilderCustomizableContext builderCustomizableContext = mock(NodeBuilderCustomizableContext.class);

        when(entityManager.createQuery(contains("WHERE estado.pais.id ="))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(estadoCadastrado));
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(builderCustomizableContext);
        when(builderCustomizableContext.addConstraintViolation()).thenReturn(context);

        boolean resultado = estadoValidoValidator.isValid(request, context);

        assertFalse(resultado);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("não deve ser nulo");
    }

    @Test
    @DisplayName("Deve ser inválido quando estado não encontrado")
    void deveSerInvalidoQuandoEstadoNaoEncontrado() {
        Long idPais = 1L;
        Long idEstadoNaoEncontrado = 2L;
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(idPais)
                .comIdEstado(idEstadoNaoEncontrado)
                .build();
        Pais outroPais = new Pais("Venezuela");
        Estado estado = new Estado("Miranda", outroPais);

        Query queryEstadoPorPais = mock(Query.class);
        Query queryEstado = mock(Query.class);
        NodeBuilderCustomizableContext builderCustomizableContext = mock(NodeBuilderCustomizableContext.class);

        when(entityManager.createQuery(contains("WHERE estado.pais.id ="))).thenReturn(queryEstadoPorPais);
        when(queryEstadoPorPais.getResultList()).thenReturn(List.of(estado));

        when(entityManager.createQuery(contains("WHERE estado.id ="))).thenReturn(queryEstado);
        when(queryEstado.getResultList()).thenReturn(Collections.emptyList());
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(builderCustomizableContext);
        when(builderCustomizableContext.addConstraintViolation()).thenReturn(context);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        boolean resultado = estadoValidoValidator.isValid(request, context);

        assertFalse(resultado);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("não encontrado");
    }

    @Test
    @DisplayName("Deve ser inválido quando estado não pertence ao país")
    void deveSerInvalidoQuandoEstadoNaoPertenceAoPais() throws NoSuchFieldException, IllegalAccessException {
        Long idPais = 1L;
        Long idEstadoOutroPais = 2L;
        Long idPaisEstadoOutroPais = 99L;
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(idPais)
                .comIdEstado(idEstadoOutroPais)
                .build();
        Pais outroPais = new Pais("Venezuela");
        Estado estado = new Estado("Miranda", outroPais);
        Field entityManagerField = Pais.class.getDeclaredField("id");
        entityManagerField.setAccessible(true);
        entityManagerField.set(outroPais, idPaisEstadoOutroPais);

        Query queryEstadoPorPais = mock(Query.class);
        Query queryEstado = mock(Query.class);
        NodeBuilderCustomizableContext builderCustomizableContext = mock(NodeBuilderCustomizableContext.class);

        when(entityManager.createQuery(contains("WHERE estado.pais.id ="))).thenReturn(queryEstadoPorPais);
        when(queryEstadoPorPais.getResultList()).thenReturn(List.of(estado));

        when(entityManager.createQuery(contains("WHERE estado.id ="))).thenReturn(queryEstado);
        when(queryEstado.getResultList()).thenReturn(List.of(estado));
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(builderCustomizableContext);
        when(builderCustomizableContext.addConstraintViolation()).thenReturn(context);
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);

        boolean resultado = estadoValidoValidator.isValid(request, context);

        assertFalse(resultado);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("não pertence a este País");
    }

    @Test
    @DisplayName("Deve ser válido quando estado pertence ao país")
    void deveSerValidoQuandoEstadoPertenceAoPais() throws NoSuchFieldException, IllegalAccessException {
        Long idPais = 1L;
        Long idEstado = 2L;
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento()
                .comIdPais(idPais)
                .comIdEstado(idEstado)
                .build();
        Pais pais = new Pais("Venezuela");
        Estado estado = new Estado("Miranda", pais);
        Field entityManagerField = Pais.class.getDeclaredField("id");
        entityManagerField.setAccessible(true);
        entityManagerField.set(pais, idPais);
        Query queryEstadoPorPais = mock(Query.class);
        Query queryEstado = mock(Query.class);
        when(entityManager.createQuery(contains("WHERE estado.pais.id ="))).thenReturn(queryEstadoPorPais);
        when(queryEstadoPorPais.getResultList()).thenReturn(List.of(estado));
        when(entityManager.createQuery(contains("WHERE estado.id ="))).thenReturn(queryEstado);
        when(queryEstado.getResultList()).thenReturn(List.of(estado));

        boolean resultado = estadoValidoValidator.isValid(request, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando país não tem estados cadastrados")
    void deveSerValidoQuandoPaisNaoTemEstados() {
        NovoPagamentoRequest request = NovoPagamentoRequestBuilder.umPagamento().build();
        Query query = mock(Query.class);
        when(entityManager.createQuery(contains("WHERE estado.pais.id = :pIdPais"))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        boolean resultado = estadoValidoValidator.isValid(request, context);

        assertTrue(resultado);
    }
}