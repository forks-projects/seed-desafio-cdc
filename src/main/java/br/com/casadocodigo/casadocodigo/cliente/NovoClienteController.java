package br.com.casadocodigo.casadocodigo.cliente;

import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NovoClienteController {
    private final PaisRepository paisRepository;
    private final EstadoRepository estadoRepository;

    public NovoClienteController(PaisRepository paisRepository, EstadoRepository estadoRepository) {
        this.paisRepository = paisRepository;
        this.estadoRepository = estadoRepository;
    }

    @PostMapping("/v1/clientes")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoClienteRequest novoClienteRequest) throws MethodArgumentNotValidException, NoSuchMethodException {
        Cliente cliente = novoClienteRequest.toModel(paisRepository, estadoRepository);
        return ResponseEntity.ok().build();
    }
}
