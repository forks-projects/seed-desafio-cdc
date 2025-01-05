package br.com.casadocodigo.casadocodigo.categoria;

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
public class NovaCategoriaController {

    private final CategoriaRepository categoriaRepository;

    public NovaCategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping("/v1/categorias")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovaCategoriaRequest novaCategoriaRequest, UriComponentsBuilder uriBuilder) {
        categoriaRepository.findByNome(novaCategoriaRequest.getNome()).ifPresent(categoria -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Categoria já está cadastrada");
        });
        Categoria categoria = novaCategoriaRequest.toModel();
        categoriaRepository.save(categoria);
        URI uri = uriBuilder.path("/v1/categorias/{id}").buildAndExpand(categoria.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
