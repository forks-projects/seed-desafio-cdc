package br.com.casadocodigo.casadocodigo.autor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Entity
@Table(name = "autores", uniqueConstraints = {
        @UniqueConstraint(name = "uc_email", columnNames = "email")
})
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(max = 400)
    @Column(length = 400)
    private String descricao;

    @NotNull
    private LocalDateTime dataHoraCriacao;

    public Autor() {
    }

    public Autor(@NotBlank @NotBlank String nome,
                 @NotBlank @Email @NotBlank @Email String email,
                 @NotBlank @Length(max = 400) String descricao,
                 @NotNull LocalDateTime dataHoraCriacao) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public Long getId() {
        return this.id;
    }
}
