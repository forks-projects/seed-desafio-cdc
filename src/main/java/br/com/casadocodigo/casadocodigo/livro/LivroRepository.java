package br.com.casadocodigo.casadocodigo.livro;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends PagingAndSortingRepository<Livro, Long> {
    Livro save(Livro livro);

    Optional<Livro> findById(Long idLivro);
}
