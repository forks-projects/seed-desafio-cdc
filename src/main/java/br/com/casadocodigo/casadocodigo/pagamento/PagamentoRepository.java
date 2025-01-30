package br.com.casadocodigo.casadocodigo.pagamento;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {
}
