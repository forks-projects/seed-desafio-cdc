package br.com.casadocodigo.casadocodigo.cliente;

public class NovoClienteRequestBuilder {
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

    public NovoClienteRequestBuilder() {
        this.email = "cliente@teste.com";
        this.nome = "Nome Teste";
        this.sobreNome = "Sobrenome Teste";
        this.cpfCnpj = "12345678900";
        this.endereco = "Rua Teste, 123";
        this.complemento = "Apto 101";
        this.telefone = "(11) 99999-9999";
        this.cidade = "São Paulo";
        this.cep = "12345-678";
        this.idPais = 1L;
        this.idEstado = null;
    }

    public NovoClienteRequestBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public NovoClienteRequestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public NovoClienteRequestBuilder comSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
        return this;
    }

    public NovoClienteRequestBuilder comCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
        return this;
    }

    public NovoClienteRequestBuilder comEndereco(String endereco) {
        this.endereco = endereco;
        return this;
    }

    public NovoClienteRequestBuilder comComplemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public NovoClienteRequestBuilder comTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public NovoClienteRequestBuilder comCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public NovoClienteRequestBuilder comCep(String cep) {
        this.cep = cep;
        return this;
    }

    public NovoClienteRequestBuilder comIdPais(Long idPais) {
        this.idPais = idPais;
        return this;
    }

    public NovoClienteRequestBuilder comIdEstado(Long idEstado) {
        this.idEstado = idEstado;
        return this;
    }

    public static NovoClienteRequestBuilder umCliente() {
        return new NovoClienteRequestBuilder();
    }

    public NovoClienteRequest build() {
        return new NovoClienteRequest(
                email, nome, sobreNome, cpfCnpj, endereco, complemento, telefone, cep, cidade, idPais, idEstado
        );
    }
}
