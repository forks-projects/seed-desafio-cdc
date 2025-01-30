package br.com.casadocodigo.casadocodigo.pagamento;

public class NovoPagamentoRequestBuilder {
    private String email;
    private String nome;
    private String sobreNome;
    private String cpfCnpj;
    private String endereco;
    private String complemento;
    private String telefone;
    private String cidade;
    private String cep;
    private Long idPais;
    private Long idEstado;

    public NovoPagamentoRequestBuilder() {
        this.email = "cliente@teste.com";
        this.nome = "Nome Teste";
        this.sobreNome = "Sobrenome Teste";
        this.cpfCnpj = "704.724.660-62";
//        exemplo de cnpj
//        this.cpfCnpj = "12.970.137/0001-58";
        this.endereco = "Rua Teste, 123";
        this.complemento = "Apto 101";
        this.telefone = "(11) 99999-9999";
        this.cidade = "São Paulo";
        this.cep = "12345-678";
        this.idPais = 1L;
        this.idEstado = null;
    }

    public NovoPagamentoRequestBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public NovoPagamentoRequestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public NovoPagamentoRequestBuilder comSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
        return this;
    }

    public NovoPagamentoRequestBuilder comCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
        return this;
    }

    public NovoPagamentoRequestBuilder comEndereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public NovoPagamentoRequestBuilder comComplemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public NovoPagamentoRequestBuilder comTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public NovoPagamentoRequestBuilder comCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public NovoPagamentoRequestBuilder comCep(String cep) {
        this.cep = cep;
        return this;
    }

    public NovoPagamentoRequestBuilder comIdPais(Long idPais) {
        this.idPais = idPais;
        return this;
    }

    public NovoPagamentoRequestBuilder comIdEstado(Long idEstado) {
        this.idEstado = idEstado;
        return this;
    }

    public static NovoPagamentoRequestBuilder umPagamento() {
        return new NovoPagamentoRequestBuilder();
    }

    public NovoPagamentoRequest build() {
        return new NovoPagamentoRequest(
                email, nome, sobreNome, cpfCnpj, endereco, complemento, telefone, cep, cidade, idPais, idEstado
        );
    }
}
