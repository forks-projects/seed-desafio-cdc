package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import br.com.casadocodigo.casadocodigo.share.ErroDTO;
import br.com.casadocodigo.casadocodigo.share.ResponseErroDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;

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
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoPagamentoRequest novoPagamentoRequest, UriComponentsBuilder uriBuilder) throws MethodArgumentNotValidException, NoSuchMethodException {
        Pagamento pagamento = novoPagamentoRequest.toModel(paisRepository, estadoRepository, livroRepository);

        pagamento.getItens().forEach(item -> {
            item.setPagamento(pagamento);
        });

        BigDecimal totalCalculado = pagamento.getItens().stream()
                .map(item -> item.getLivro().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pagamento.isTotalIgualServidor(totalCalculado)) {
            pagamentoRepository.save(pagamento);
            URI uri = uriBuilder.path("/v1/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }

        ErroDTO erroDTO = new ErroDTO("total", "diferente do valor total calculado");
        ArrayList<ErroDTO> listaErros = new ArrayList<>();
        listaErros.add(erroDTO);
        ResponseErroDTO erroDto = new ResponseErroDTO(400, "Informação inválida", listaErros);
        return ResponseEntity.badRequest().body(erroDto);
    }
}
