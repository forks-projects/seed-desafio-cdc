package br.com.casadocodigo.casadocodigo.share;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.Assert;

import java.util.List;

public class ValorUnicoValidator implements ConstraintValidator<ValorUnico, Object> {
    private String nomeDoCampo;
    private Class<?> classeDaEntidade;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(ValorUnico constraintAnnotation) {
        nomeDoCampo = constraintAnnotation.nomeDoCampo();
        classeDaEntidade = constraintAnnotation.classeDaEntidade();
    }

    @Override
    public boolean isValid(Object valorDoNomeDoCampo, ConstraintValidatorContext constraintValidatorContext) {
        String jpql = "SELECT e FROM " + classeDaEntidade.getName() + " e WHERE " + nomeDoCampo + " = :pValorDoNomeDoCampo";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("pValorDoNomeDoCampo", valorDoNomeDoCampo);
        List resultList = query.getResultList();
        Assert.state(resultList.size() <=1, "Foi encontrado mais de um "+ classeDaEntidade +" com o atributo "+ nomeDoCampo +" = "+ valorDoNomeDoCampo);
        return resultList.isEmpty();
    }
}
