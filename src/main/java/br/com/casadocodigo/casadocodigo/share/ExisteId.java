package br.com.casadocodigo.casadocodigo.share;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.annotation.*;
/**
 * Verifica se um identificador, não nulo, existe no banco de dados
 */
@Documented
@Constraint(validatedBy = {ExisteIdValidator.class})
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExisteId {
    String message() default "não encontrado";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String nomeDoCampo();

    Class<?> classeDaEntidade();
}