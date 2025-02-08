package br.com.casadocodigo.casadocodigo.cupom_desconto;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupomDescontoRepository extends CrudRepository<CupomDesconto, Long> {
}
