package br.com.casadocodigo.casadocodigo.categoria;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categorias", uniqueConstraints = {
        @UniqueConstraint(name = "uc_nome", columnNames = "nome")
})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Deprecated
    public Categoria() {
    }

    public Categoria(@NotBlank String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return this.id;
    }
}
