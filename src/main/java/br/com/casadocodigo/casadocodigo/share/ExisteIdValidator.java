package br.com.casadocodigo.casadocodigo.share;

import org.springframework.util.Assert;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class ExisteIdValidator implements ConstraintValidator<ExisteId, Object> {
    private String nomeDoCampo;
    private Class<?> classeDaEntidade;

    private final EntityManager entityManager;

    public ExisteIdValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(ExisteId constraintAnnotation) {
        nomeDoCampo = constraintAnnotation.nomeDoCampo();
        classeDaEntidade = constraintAnnotation.classeDaEntidade();
    }

    @Override
    public boolean isValid(Object valorDoNomeDoCampo, ConstraintValidatorContext constraintValidatorContext) {
        if (valorDoNomeDoCampo == null) return true;
        String jpql = "SELECT e FROM " + classeDaEntidade.getName() + " e WHERE " + nomeDoCampo + " = :pValorDoNomeDoCampo";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("pValorDoNomeDoCampo", valorDoNomeDoCampo);

        List resultList = query.getResultList();
        Assert.isTrue(resultList.size() <=1, "aconteceu algo estranho e você tem mais de um " + classeDaEntidade + " com o atributo " + nomeDoCampo + " com o valor = " + valorDoNomeDoCampo);
        return !resultList.isEmpty();
    }
}