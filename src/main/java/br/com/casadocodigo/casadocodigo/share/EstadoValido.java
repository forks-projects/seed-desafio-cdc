package br.com.casadocodigo.casadocodigo.share;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EstadoValidoValidator.class})
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EstadoValido {
    String message() default "deve ser um Estado válido";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}