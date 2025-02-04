package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.livro.Livro;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.share.ExisteId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NovoItemRequest {
    @NotNull
    @ExisteId(classeDaEntidade = Livro.class, nomeDoCampo = "id", message = "Livro não encontrado")
    private Long livroId;

    @NotNull
    @Min(value = 1)
    private Integer quantidade;

    public NovoItemRequest(Long livroId, Integer quantidade) {
        this.livroId = livroId;
        this.quantidade = quantidade;
    }

    public Long getLivroId() {
        return livroId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Item toModel(LivroRepository livroRepository) {
        Livro livro = livroRepository.findById(this.livroId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não encontrado")
        );
        return new Item(livro, this.quantidade);
    }
}
