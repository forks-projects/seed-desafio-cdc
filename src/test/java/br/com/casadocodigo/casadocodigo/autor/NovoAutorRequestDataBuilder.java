package br.com.casadocodigo.casadocodigo.autor;

public class NovoAutorRequestDataBuilder {
    private String nome = "Autor Padrão";
    private String email = "autor@exemplo.com";
    private String descricao = "Descrição padrão do autor";

    public NovoAutorRequestDataBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public NovoAutorRequestDataBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public NovoAutorRequestDataBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public NovoAutorRequest build() {
        return new NovoAutorRequest(nome, email, descricao);
    }

    public static NovoAutorRequestDataBuilder umAutor() {
        return new NovoAutorRequestDataBuilder();
    }

    public static NovoAutorRequestDataBuilder umAutorComNomeVazio() {
        return new NovoAutorRequestDataBuilder().comNome("");
    }

    public static NovoAutorRequestDataBuilder umAutorComEmailInvalido() {
        return new NovoAutorRequestDataBuilder().comEmail("email-invalido");
    }

    public static NovoAutorRequestDataBuilder umAutorComEmailEmBranco() {
        return new NovoAutorRequestDataBuilder().comEmail("");
    }

    public static NovoAutorRequestDataBuilder umAutorComDescricaoEmBranco() {
        return new NovoAutorRequestDataBuilder().comDescricao("");
    }
}
