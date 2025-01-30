package br.com.casadocodigo.casadocodigo.pagamento;

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
public class NovoPagamentoController {
    private final PaisRepository paisRepository;
    private final EstadoRepository estadoRepository;

    public NovoPagamentoController(PaisRepository paisRepository, EstadoRepository estadoRepository) {
        this.paisRepository = paisRepository;
        this.estadoRepository = estadoRepository;
    }

    @PostMapping("/v1/pagamentos")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoPagamentoRequest novoPagamentoRequest) throws MethodArgumentNotValidException, NoSuchMethodException {
        Pagamento pagamento = novoPagamentoRequest.toModel(paisRepository, estadoRepository);
        return ResponseEntity.ok().build();
    }
}
