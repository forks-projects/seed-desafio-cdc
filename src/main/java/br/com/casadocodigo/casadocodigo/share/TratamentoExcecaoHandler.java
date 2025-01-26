package br.com.casadocodigo.casadocodigo.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TratamentoExcecaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseErroDTO tratarErrosValidacaoCampos(MethodArgumentNotValidException exception) {
        List<FieldError> listaErros = exception.getBindingResult().getFieldErrors();
        ArrayList<ErroDTO> camposComErro = new ArrayList<>();
        listaErros.forEach(error -> {
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            ErroDTO erro = new ErroDTO(error.getField(), mensagem);
            camposComErro.add(erro);
        });

        return new ResponseErroDTO(
                exception.getStatusCode().value(),
                "Informação inválida",
                camposComErro
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseErroDTO> tratarCustomizado(ResponseStatusException exception) {
        ResponseErroDTO responseErroDTO = new ResponseErroDTO(
                exception.getStatusCode().value(),
                exception.getReason(),
                new ArrayList<>()
        );
        return ResponseEntity
                    .status(exception.getStatusCode())
                    .body(responseErroDTO);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErroDTO handleGenericException(Exception exception) {
        return new ResponseErroDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro inesperado: " + exception.getMessage(),
                new ArrayList<>()
        );
    }
}