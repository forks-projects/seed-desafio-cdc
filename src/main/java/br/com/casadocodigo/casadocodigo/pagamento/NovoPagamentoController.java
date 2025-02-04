package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
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
    private final LivroRepository livroRepository;
    private final PagamentoRepository pagamentoRepository;

    public NovoPagamentoController(PaisRepository paisRepository, EstadoRepository estadoRepository, LivroRepository livroRepository, PagamentoRepository pagamentoRepository) {
        this.paisRepository = paisRepository;
        this.estadoRepository = estadoRepository;
        this.livroRepository = livroRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @PostMapping("/v1/pagamentos")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoPagamentoRequest novoPagamentoRequest) throws MethodArgumentNotValidException, NoSuchMethodException {
        Pagamento pagamento = novoPagamentoRequest.toModel(paisRepository, estadoRepository, livroRepository);

        pagamento.getItens().forEach(item -> {
            item.setPagamento(pagamento);
        });

        pagamentoRepository.save(pagamento);

        return ResponseEntity.ok().build();
    }
}
