package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pagamentos")
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

    @NotNull @Min(value = 1)
    private BigDecimal total;

    @NotNull
    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Item> itens;

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
                     Estado estado,
                     @NotNull @Min(value = 1) BigDecimal total,
                     List<Item> itens) {
        this.email = email;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.cpfCnpj = cpfCnpj;
        this.endereco = endereco;
        this.complemento = complemento;
        this.telefone = telefone;
        this.cep = cep;
        this.cidade = cidade;
        this.pais = pais;
        this.estado = estado;
        this.total = total;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItens() {
        return itens;
    }

    public boolean isTotalValido() {
        BigDecimal totalCalculado = this.calcularTotalDosItens();
        return Objects.equals(total, totalCalculado);
    }

    private BigDecimal calcularTotalDosItens() {
        return this.getItens().stream()
                .map(item -> item.getLivro().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
