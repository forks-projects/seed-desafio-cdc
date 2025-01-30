package br.com.casadocodigo.casadocodigo.pagamento;

import br.com.casadocodigo.casadocodigo.estado.Estado;
import br.com.casadocodigo.casadocodigo.estado.EstadoRepository;
import br.com.casadocodigo.casadocodigo.pais.Pais;
import br.com.casadocodigo.casadocodigo.pais.PaisRepository;
import br.com.casadocodigo.casadocodigo.share.DocumentoValido;
import br.com.casadocodigo.casadocodigo.share.EstadoValido;
import br.com.casadocodigo.casadocodigo.share.ExisteId;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@EstadoValido
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
                                Long idEstado) {
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

    public Pagamento toModel(PaisRepository paisRepository, EstadoRepository estadoRepository) throws MethodArgumentNotValidException {
        Pais pais = paisRepository.findById(idPais).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "País não encontrado")
        );
        Estado estado = estadoRepository.findById(idEstado).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado não encontrado")
        );

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
                estado
        );
    }
}
