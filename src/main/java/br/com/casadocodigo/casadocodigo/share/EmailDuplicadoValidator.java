package br.com.casadocodigo.casadocodigo.share;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class EmailDuplicadoValidator implements Validator {

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NovoAutorRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        NovoAutorRequest novoAutorRequest = (NovoAutorRequest) target;
        Optional<Autor> autorEncontrado = autorRepository.findByEmail(novoAutorRequest.getEmail());

        if (autorEncontrado.isPresent()) {
            errors.rejectValue("email", null,
                    "O email " + novoAutorRequest.getEmail() + " já está cadastrado. Adicione um outro email");
        }
    }
}