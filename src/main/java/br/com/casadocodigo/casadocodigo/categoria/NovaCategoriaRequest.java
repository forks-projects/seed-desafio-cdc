package br.com.casadocodigo.casadocodigo.categoria;

import jakarta.validation.constraints.NotBlank;

public class NovaCategoriaRequest {
    @NotBlank
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
