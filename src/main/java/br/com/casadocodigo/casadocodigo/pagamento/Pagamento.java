package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobreNome;

    @NotBlank
    private String cpfCnpj;

    @NotBlank
    private String endereco;

    @NotBlank
    private String complemento;

    @NotBlank
    private String cidade;


    @NotBlank
    private String telefone;

    @NotBlank
    private String cep;

    @NotNull
    @OneToOne
    private Pais pais;

    @OneToOne
    @Nullable
    private Estado estado;

    public Pagamento(@NotBlank @Email String email,
                     @NotBlank String nome,
                     @NotBlank String sobreNome,
                     @NotBlank String cpfCnpj,
                     @NotBlank String endereco,
                     @NotBlank String complemento,
                     @NotBlank String telefone,
                     @NotBlank String cep,
                     @NotBlank String cidade,
                     @NotNull Pais pais,
                     Estado estado) {

    }

    public Long getId() {
        return id;
    }
}
