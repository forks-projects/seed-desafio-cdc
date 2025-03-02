package br.com.casadocodigo.casadocodigo.unit.share;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.integration.pagamento.NovoPagamentoRequestBuilder;
import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import br.com.casadocodigo.casadocodigo.share.CupomDescontoValidator;
import br.com.casadocodigo.casadocodigo.share.CupomDescontoValido;
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
import java.util.Collections;
import java.util.List;

import static jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CupomDescontoValidatorUnitTest {
    @InjectMocks
    private CupomDescontoValidator cupomDescontoValidator;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private Query query;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Test
    @DisplayName("Deve ser válido quando cupom existe")
    void deveSerValidoQuandoCupomExiste() {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comCupomDesconto("CUPOMJAVA")
                .build();
        CupomDesconto cupomDesconto = new CupomDesconto("CUPOMJAVA", BigDecimal.TEN, LocalDate.now().plusDays(1L));
        when(entityManager.createQuery(contains("select cupom from CupomDesconto cupom"))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(cupomDesconto));
        CupomDescontoValidoLocal cupomDescontoValidoLocal = new CupomDescontoValidoLocal();
        cupomDescontoValidator.initialize(cupomDescontoValidoLocal);

        boolean resultado = cupomDescontoValidator.isValid(novoPagamentoRequest, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser válido quando está sem cupom")
    void deveSerValidoQuandoEstaSemCupom() {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comCupomDesconto(null)
                .build();
        CupomDescontoValidoLocal cupomDescontoValidoLocal = new CupomDescontoValidoLocal();
        cupomDescontoValidator.initialize(cupomDescontoValidoLocal);

        boolean resultado = cupomDescontoValidator.isValid(novoPagamentoRequest, context);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando cupom não existe")
    void deveSerInvalidoQuandoCupomNaoExiste() {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comCupomDesconto("CUPOMJAVA")
                .build();
        when(entityManager.createQuery(contains("select cupom from CupomDesconto cupom"))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        CupomDescontoValidoLocal cupomDescontoValidoLocal = new CupomDescontoValidoLocal();
        cupomDescontoValidator.initialize(cupomDescontoValidoLocal);

        boolean resultado = cupomDescontoValidator.isValid(novoPagamentoRequest, context);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve ser inválido quando cupom está vencido")
    void deveSerInvalidoQuandoCupomEstaVencido() {
        NovoPagamentoRequest novoPagamentoRequest = NovoPagamentoRequestBuilder.umPagamento()
                .comCupomDesconto("CUPOMJAVA")
                .build();
        CupomDesconto cumpoVencido = new CupomDesconto("CUPOMJAVA",
                BigDecimal.TEN,
                LocalDate.now().minusDays(1L));
        when(entityManager.createQuery(contains("select cupom from CupomDesconto cupom"))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(cumpoVencido));
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
        NodeBuilderCustomizableContext builderCustomizableContext = mock(NodeBuilderCustomizableContext.class);
        when(violationBuilder.addPropertyNode(anyString())).thenReturn(builderCustomizableContext);
        when(builderCustomizableContext.addConstraintViolation()).thenReturn(context);

        CupomDescontoValidoLocal cupomDescontoValidoLocal = new CupomDescontoValidoLocal();
        cupomDescontoValidator.initialize(cupomDescontoValidoLocal);

        boolean resultado = cupomDescontoValidator.isValid(novoPagamentoRequest, context);

        assertFalse(resultado);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("está vencido");
    }

    private class CupomDescontoValidoLocal implements CupomDescontoValido {
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
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}