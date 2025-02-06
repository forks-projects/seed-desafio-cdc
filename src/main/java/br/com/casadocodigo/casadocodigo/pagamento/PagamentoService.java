package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Service
public class PagamentoService {
    private final PaisRepository paisRepository;
    private final EstadoRepository estadoRepository;
    private final LivroRepository livroRepository;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PaisRepository paisRepository, EstadoRepository estadoRepository, LivroRepository livroRepository, PagamentoRepository pagamentoRepository) {
        this.paisRepository = paisRepository;
        this.estadoRepository = estadoRepository;
        this.livroRepository = livroRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Optional<Pagamento> salvar(NovoPagamentoRequest novoPagamentoRequest) throws MethodArgumentNotValidException {
        Pagamento pagamento = novoPagamentoRequest.toModel(paisRepository, estadoRepository, livroRepository);

        if (pagamento.isTotalValido()) {
            pagamento.getItens().forEach(item -> item.associarPagamento(pagamento));
            pagamentoRepository.save(pagamento);
            return Optional.of(pagamento);
        }
        return Optional.empty();
    }
}

