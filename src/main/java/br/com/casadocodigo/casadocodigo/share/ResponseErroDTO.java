package br.com.casadocodigo.casadocodigo.share;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseErroDTO {
    private int status;
    private String erro;
    private List<ErroDTO> listaErros;

    public ResponseErroDTO(int status, String erro, ArrayList<ErroDTO> camposComErro) {
        this.status = status;
        this.erro = erro;
        this.listaErros = camposComErro;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public List<ErroDTO> getListaErros() {
        return listaErros;
    }
}
