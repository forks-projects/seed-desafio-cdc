package br.com.casadocodigo.casadocodigo.autor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class NovoAutorRequest {
    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(max = 400)
    private String descricao;

    private LocalDateTime dataHoraCriacao;

    public NovoAutorRequest(String nome, String email, String descricao) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
        this.dataHoraCriacao = LocalDateTime.now();
    }

    public Autor toModel() {
        return new Autor(this.nome, this.email, this.descricao, this.dataHoraCriacao);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDescricao() {
        return descricao;
    }
}
