package br.com.casadocodigo.casadocodigo.categoria;

import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import jakarta.validation.constraints.NotBlank;

public class NovaCategoriaRequest {
    @NotBlank
    @ValorUnico(classeDaEntidade = Categoria.class, nomeDoCampo = "nome", message = "Categoria já está cadastrada")
    private String nome;

    public NovaCategoriaRequest(String nome) {
        this.nome = nome;
    }

    public @NotBlank String getNome() {
        return nome;
    }

    public Categoria toModel() {
        return new Categoria(this.nome);
    }
}
