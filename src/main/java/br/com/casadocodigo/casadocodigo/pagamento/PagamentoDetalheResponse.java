package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PagamentoDetalheResponse {
    private String email;

    private String nome;

    private String sobreNome;

    private String cpfCnpj;

    private String endereco;

    private String complemento;

    private String telefone;

    private String cidade;

    private String cep;

    private String  nomePais;

    private String nomeEstado = null;

    private BigDecimal total;

    private List<ItemDetalheResponse> itens = new ArrayList<>();

    private Boolean temDesconto = false;

    private LocalDate dataCompra;

    private BigDecimal totalComDesconto;

    private BigDecimal valorDoDesconto = BigDecimal.ZERO;

    public PagamentoDetalheResponse(Pagamento pagamento) {
        this.email = pagamento.getEmail();
        this.nome = pagamento.getNome();
        this.sobreNome = pagamento.getSobreNome();
        this.cpfCnpj = pagamento.getCpfCnpj();
        this.endereco = pagamento.getEndereco();
        this.complemento = pagamento.getComplemento();
        this.cep = pagamento.getCep();
        this.telefone = pagamento.getTelefone();
        this.cidade = pagamento.getCidade();
        this.nomePais = pagamento.getPais().getNome();
        if (!Objects.isNull(pagamento.getEstado())) {
            this.nomeEstado = pagamento.getEstado().getNome();
        }
        this.total = pagamento.getTotal();
        this.itens = pagamento.getItens().stream().map(ItemDetalheResponse::new).toList();

        if (!Objects.isNull(pagamento.getCupomDesconto())) {
            this.temDesconto = true;
            CupomDesconto cupomDesconto = pagamento.getCupomDesconto();
            BigDecimal desconto = this.total.multiply(cupomDesconto.getPercentualDesconto())
                    .divide(new BigDecimal("100.00"), RoundingMode.HALF_UP);
            this.totalComDesconto = this.total.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
            this.valorDoDesconto = desconto.setScale(2, RoundingMode.HALF_UP);
        }
        this.dataCompra = pagamento.getDataCompra();
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

    public String getTelefone() {
        return telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCep() {
        return cep;
    }

    public String getNomePais() {
        return nomePais;
    }

    public String getNomeEstado() {
        return nomeEstado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<ItemDetalheResponse> getItens() {
        return itens;
    }

    public Boolean getTemDesconto() {
        return temDesconto;
    }

    public BigDecimal getTotalComDesconto() {
        return totalComDesconto;
    }

    public BigDecimal getValorDoDesconto() {
        return valorDoDesconto;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }
}
