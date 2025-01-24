package br.com.casadocodigo.casadocodigo.estado;

import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class NovoEstadoController {

    private final EstadoRepository estadoRepository;
    private final PaisRepository paisRepository;

    public NovoEstadoController(EstadoRepository estadoRepository, PaisRepository paisRepository) {
        this.estadoRepository = estadoRepository;
        this.paisRepository = paisRepository;
    }

    @PostMapping("/v1/estados")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoEstadoRequest novoEstadoRequest, UriComponentsBuilder uriBuilder) {
        Estado estado = novoEstadoRequest.toModel(paisRepository);
        estadoRepository.save(estado);
        URI uri = uriBuilder.path("/v1/estados/{id}").buildAndExpand(estado.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
