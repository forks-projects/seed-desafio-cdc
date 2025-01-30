package br.com.casadocodigo.casadocodigo.share;

import br.com.casadocodigo.casadocodigo.pagamento.NovoPagamentoRequest;
import br.com.casadocodigo.casadocodigo.estado.Estado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;


public class EstadoValidoValidator implements ConstraintValidator<EstadoValido, NovoPagamentoRequest> {
    private String message;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(EstadoValido constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(NovoPagamentoRequest novoPagamentoRequest, ConstraintValidatorContext constraintValidatorContext) {
        String jpql = "SELECT estado FROM Estado estado WHERE estado.pais.id = :pIdPais";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("pIdPais", novoPagamentoRequest.getIdPais());
        int quantidadeDeEstadosPorPais = query.getResultList().size();

        if (quantidadeDeEstadosPorPais > 0) {
            if (Objects.isNull(novoPagamentoRequest.getIdEstado())) {
                message = "não deve ser nulo";
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode("idEstado").addConstraintViolation();
                return false;
            }

            String jpqlEstadoPertenceAoPais = "SELECT estado FROM Estado estado WHERE estado.id = :pIdEstado";
            Query queryEstadoPertenceAoPais = entityManager.createQuery(jpqlEstadoPertenceAoPais);
            queryEstadoPertenceAoPais.setParameter("pIdEstado", novoPagamentoRequest.getIdEstado());

            if (queryEstadoPertenceAoPais.getResultList().isEmpty()) {
                message = "não encontrado";
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode("idEstado").addConstraintViolation();
                return false;
            }

            Estado estado = (Estado) queryEstadoPertenceAoPais.getResultList().get(0);

            if (!novoPagamentoRequest.getIdPais().equals(estado.getPais().getId())) {
                message = "não pertence a este País";
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode("idEstado").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}