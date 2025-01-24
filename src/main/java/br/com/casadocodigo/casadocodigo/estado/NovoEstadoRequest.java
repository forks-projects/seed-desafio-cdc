package br.com.casadocodigo.casadocodigo.estado;

import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import br.com.casadocodigo.casadocodigo.share.ExisteId;
import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NovoEstadoRequest {
    @NotBlank
    @ValorUnico(classeDaEntidade = Estado.class, nomeDoCampo = "nome", message = "Estado já está cadastrado")
    private String nome;

    @NotNull
    @ExisteId(classeDaEntidade = Pais.class, nomeDoCampo = "id", message = "País não encontrado")
    private Long idPais;

    public NovoEstadoRequest(@NotBlank String nome, @NotNull Long idPais) {
        this.nome = nome;
        this.idPais = idPais;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdPais() {
        return idPais;
    }

    public Estado toModel(PaisRepository paisRepository) {
        Pais pais = paisRepository.findById(this.idPais).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pais não encontrado")
        );
        return new Estado(this.nome, pais);
    }
}
