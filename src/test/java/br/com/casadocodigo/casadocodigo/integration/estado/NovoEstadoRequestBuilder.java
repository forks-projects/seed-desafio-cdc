package br.com.casadocodigo.casadocodigo.integration.estado;

import br.com.casadocodigo.casadocodigo.estado.NovoEstadoRequest;

public class NovoEstadoRequestBuilder {

    private String nome;
    private Long paisId;

    public NovoEstadoRequestBuilder() {
        this.nome = "São Paulo";
        this.paisId = 1L;
    }

    public static NovoEstadoRequestBuilder umEstado() {
        return new NovoEstadoRequestBuilder();
    }

    public NovoEstadoRequestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public NovoEstadoRequestBuilder comIdPais(Long paisId) {
        this.paisId = paisId;
        return this;
    }

    public NovoEstadoRequest build() {
        return new NovoEstadoRequest(nome, paisId);
    }
}

