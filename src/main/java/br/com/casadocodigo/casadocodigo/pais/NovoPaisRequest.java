package br.com.casadocodigo.casadocodigo.pais;

import br.com.casadocodigo.casadocodigo.share.ValorUnico;
import jakarta.validation.constraints.NotBlank;

public class NovoPaisRequest {
    @NotBlank
    @ValorUnico(classeDaEntidade = Pais.class, nomeDoCampo = "nome", message = "Pais já está cadastrado")
    private String nome;

    public NovoPaisRequest(@NotBlank String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public Pais toModel() {
        return new Pais(this.nome);
    }
}
