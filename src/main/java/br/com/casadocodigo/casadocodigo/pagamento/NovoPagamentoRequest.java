package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDesconto;
import br.com.casadocodigo.casadocodigo.cupom_desconto.CupomDescontoRepository;
import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.livro.LivroRepository;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import br.com.casadocodigo.casadocodigo.share.CumpomDescontoValido;
import br.com.casadocodigo.casadocodigo.share.DocumentoValido;
import br.com.casadocodigo.casadocodigo.share.EstadoValido;
import br.com.casadocodigo.casadocodigo.share.ExisteId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EstadoValido
@CumpomDescontoValido
public class NovoPagamentoRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String sobreNome;

    @NotBlank
    @DocumentoValido
    private String cpfCnpj;

    @NotBlank
    private String endereco;

    @NotBlank
    private String complemento;

    @NotBlank
    private String telefone;

    @NotBlank
    private String cidade;

    @NotBlank
    private String cep;

    @NotNull
    @ExisteId(classeDaEntidade = Pais.class, nomeDoCampo = "id", message = "País não encontrado")
    private Long idPais;

    private Long idEstado;

    @NotNull
    @Min(value = 1)
    private BigDecimal total;

    @Size(min = 1, message = "deve ter pelo menos 1 item")
    @NotNull
    @Valid
    private List<NovoItemRequest> itensRequest = new ArrayList<>();

    @ExisteId(classeDaEntidade = CupomDesconto.class, nomeDoCampo = "codigo", message = "Cupom de desconto não encontrado")
    private String cupomDesconto;

    private LocalDate dataPagamento;

    public NovoPagamentoRequest(@NotBlank @Email String email,
                                @NotBlank String nome,
                                @NotBlank String sobreNome,
                                @NotBlank String cpfCnpj,
                                @NotBlank String endereco,
                                @NotBlank String complemento,
                                @NotBlank String telefone,
                                @NotBlank String cep,
                                @NotBlank String cidade,
                                @NotNull Long idPais,
                                Long idEstado,
                                @NotNull @Min(value = 0) BigDecimal total,
                                List<NovoItemRequest> itensRequest,
                                String cupomDesconto
                                ) {
        this.email = email;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.cpfCnpj = cpfCnpj;
        this.endereco = endereco;
        this.complemento = complemento;
        this.cep = cep;
        this.telefone = telefone;
        this.cidade = cidade;
        this.idPais = idPais;
        this.idEstado = idEstado;
        this.total = total;
        this.itensRequest = itensRequest;
        this.cupomDesconto = cupomDesconto;
        this.dataPagamento = LocalDate.now();
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

    public Long getIdPais() {
        return idPais;
    }

    public Long getIdEstado() {
        return idEstado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<NovoItemRequest> getItensRequest() {
        return itensRequest;
    }

    public String getCupomDesconto() {
        return cupomDesconto;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public Pagamento toModel(PaisRepository paisRepository,
                             EstadoRepository estadoRepository,
                             LivroRepository livroRepository,
                             CupomDescontoRepository cupomDescontoRepository) throws MethodArgumentNotValidException {
        Pais pais = paisRepository.findById(idPais).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "País não encontrado")
        );

        Estado estado = null;
        CupomDesconto cupomDesconto1 = null;

        if(!Objects.isNull(idEstado)) {
            estado = estadoRepository.findById(idEstado).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado não encontrado")
            );
        }

        if (!Objects.isNull(this.cupomDesconto)) {
            cupomDesconto1 = cupomDescontoRepository.findById(cupomDesconto).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cupom de descontro não encontrado")
            );
        }

        List<Item> itens = itensRequest.stream().map(itemRequest -> itemRequest.toModel(livroRepository)).toList();

        return new Pagamento(
                email,
                nome,
                sobreNome,
                cpfCnpj,
                endereco,
                complemento,
                telefone,
                cep,
                cidade,
                pais,
                estado,
                total,
                itens,
                cupomDesconto1
        );
    }
}
