package br.com.casadocodigo.casadocodigo.share;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {CumpomDescontoValidator.class})
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CumpomDescontoValido {
    String message() default "está vencido";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}