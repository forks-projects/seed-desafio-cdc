package br.com.casadocodigo.casadocodigo.share;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;


public class CupomDescontoValidator implements ConstraintValidator<CupomDescontoValido, Object> {
    private String message;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(CupomDescontoValido constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object novoPagamentoRequest, ConstraintValidatorContext constraintValidatorContext) {
        NovoPagamentoRequest request = (NovoPagamentoRequest) novoPagamentoRequest;
        if (Objects.isNull(request.getCupomDesconto())) return true;
        String jpql = "select cupom from CupomDesconto cupom where codigo = :pCodigo";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("pCodigo", request.getCupomDesconto());
        if (query.getResultList().isEmpty()) {
            return false;
        }
        CupomDesconto cupomDesconto = (CupomDesconto) query.getResultList().get(0);
        if (cupomDesconto.getValidade().isBefore(request.getDataPagamento())) {
            message = "está vencido";
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode("cupomDesconto").addConstraintViolation();
            return false;
        }
        return true;
    }
}