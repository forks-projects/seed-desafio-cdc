package br.com.casadocodigo.casadocodigo.pais;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class NovoPaisController {

    private final PaisRepository paisRepository;

    public NovoPaisController(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @PostMapping("/v1/paises")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoPaisRequest novoPaisRequest, UriComponentsBuilder uriBuilder) {
        Pais pais = novoPaisRequest.toModel();
        paisRepository.save(pais);
        URI uri = uriBuilder.path("/v1/paises/{id}").buildAndExpand(pais.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
