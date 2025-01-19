package br.com.casadocodigo.casadocodigo.livro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BuscaLivrosController {
    private final LivroRepository livroRepository;

    public BuscaLivrosController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @GetMapping("/v1/livros")
    public ResponseEntity<Page<LivroEspecificoResponse>> buscarLivros(Pageable pageable) {
        Page<Livro> livrosPage = livroRepository.findAll(pageable);
        Page<LivroEspecificoResponse> responsePage = livrosPage.map(LivroEspecificoResponse::new);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/v1/livros/{idLivro}")
    public ResponseEntity<LivroDetalheResponse> buscarLivroPorId(@PathVariable("idLivro") Long idLivro) {
        Livro livro = livroRepository.findById(idLivro)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        return ResponseEntity.ok(new LivroDetalheResponse(livro));
    }
}
