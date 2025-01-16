package br.com.casadocodigo.casadocodigo.livro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivroEspecificoController {
    private final LivroRepository livroRepository;

    public LivroEspecificoController(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @GetMapping("/v1/livros")
    public ResponseEntity<Page<LivroEspecificoResponse>> buscarLivros(Pageable pageable) {
        Page<Livro> livrosPage = livroRepository.findAll(pageable);
        Page<LivroEspecificoResponse> responsePage = livrosPage.map(LivroEspecificoResponse::new);
        return ResponseEntity.ok(responsePage);
    }
}
