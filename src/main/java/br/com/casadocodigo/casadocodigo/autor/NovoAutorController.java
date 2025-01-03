package br.com.casadocodigo.casadocodigo.autor;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class NovoAutorController {

    private final AutorRepository autorRepository;

    public NovoAutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @PostMapping("/v1/autores")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoAutorRequest novoAutorRequest, UriComponentsBuilder uriBuilder) {
        autorRepository.findByEmail(novoAutorRequest.getEmail()).ifPresent(autor -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O e-mail já está em uso.");
        });
        Autor autor = novoAutorRequest.toModel();
        autorRepository.save(autor);
        URI uri = uriBuilder.path("/v1/autores/{id}").buildAndExpand(autor.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
