package br.com.casadocodigo.casadocodigo.cupom_desconto;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class NovoCupomDescontoController {
    private final CupomDescontoRepository cupomDescontoRepository;

    public NovoCupomDescontoController(CupomDescontoRepository cupomDescontoRepository) {
        this.cupomDescontoRepository = cupomDescontoRepository;
    }

    @PostMapping("/v1/cupons-desconto")
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody NovoCupomDescontoRequest novoCupomDescontoRequest, UriComponentsBuilder uriBuilder) {
        CupomDesconto cupomDesconto = novoCupomDescontoRequest.toModel();
        cupomDescontoRepository.save(cupomDesconto);
        URI uri = uriBuilder.path("/v1/cupomDesconto/{codigo}").buildAndExpand(cupomDesconto.getCodigo()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
