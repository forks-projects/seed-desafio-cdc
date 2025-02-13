package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    @ManyToOne
    private Pais pais;

    @ManyToOne
    @Nullable
    private Estado estado;

    @NotNull @Min(value = 1)
    private BigDecimal total;

    @NotNull
    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Item> itens;

    @ManyToOne
    @JoinColumn(name = "cupons_desconto_codigo")
    private CupomDesconto cupomDesconto;

    private LocalDate dataCompra = LocalDate.now();

    @Deprecated
    public Pagamento() {
    }

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
                     List<Item> itens,
                     CupomDesconto cupomDesconto) {
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
        this.cupomDesconto = cupomDesconto;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCep() {
        return cep;
    }

    public Pais getPais() {
        return pais;
    }

    public Estado getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public CupomDesconto getCupomDesconto() {
        return cupomDesconto;
    }

    public List<Item> getItens() {
        return itens;
    }

    public boolean isTotalValido() {
        BigDecimal totalCalculado = this.calcularTotalDosItens();
        return total.compareTo(totalCalculado) == 0;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    private BigDecimal calcularTotalDosItens() {
        return this.getItens().stream()
                .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
