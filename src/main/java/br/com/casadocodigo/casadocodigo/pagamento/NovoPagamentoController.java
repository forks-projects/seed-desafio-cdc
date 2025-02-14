package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.share.ErroDTO;
import br.com.casadocodigo.casadocodigo.share.ResponseErroDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class NovoPagamentoController {
    private final PagamentoService pagamentoService;

    public NovoPagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/v1/pagamentos")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoPagamentoRequest novoPagamentoRequest, UriComponentsBuilder uriBuilder) throws MethodArgumentNotValidException {
        Optional<Pagamento> possivelPagamento = pagamentoService.salvar(novoPagamentoRequest);
        if (possivelPagamento.isEmpty()) {
            ErroDTO erroDTO = new ErroDTO("total", "diferente do valor total calculado");
            ArrayList<ErroDTO> listaErros = new ArrayList<>();
            listaErros.add(erroDTO);
            ResponseErroDTO erroDto = new ResponseErroDTO(400, "Informação inválida", listaErros);
            return ResponseEntity.badRequest().body(erroDto);
        }
        URI uri = uriBuilder.path("/v1/pagamentos/{id}").buildAndExpand(possivelPagamento.get().getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/v1/pagamentos/{pagamentoId}")
    public ResponseEntity<?> detalhes(@PathVariable("pagamentoId") Long pagamentoId) {
        Pagamento pagamento = pagamentoService.buscarDetalhePagamento(pagamentoId);
        return ResponseEntity.ok(new PagamentoDetalheResponse(pagamento));
    }

}
