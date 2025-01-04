package br.com.casadocodigo.casadocodigo.autor;

import br.com.casadocodigo.casadocodigo.share.EmailDuplicadoValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class NovoAutorComValidadorEmailDuplicadoCustomizadoController {

    private final AutorRepository autorRepository;
    private final EmailDuplicadoValidator validadorEmail;

    public NovoAutorComValidadorEmailDuplicadoCustomizadoController(AutorRepository autorRepository, EmailDuplicadoValidator validadorEmail) {
        this.autorRepository = autorRepository;
        this.validadorEmail = validadorEmail;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validadorEmail);
    }

    @PostMapping("/v1/validadores-customizado-email")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoAutorRequest novoAutorRequest, UriComponentsBuilder uriBuilder) {
        Autor autor = novoAutorRequest.toModel();
        autorRepository.save(autor);
        URI uri = uriBuilder.path("/v1/autores/{id}").buildAndExpand(autor.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
