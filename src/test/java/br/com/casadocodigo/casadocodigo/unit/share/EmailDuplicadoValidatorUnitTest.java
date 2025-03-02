package br.com.casadocodigo.casadocodigo.unit.share;

import br.com.casadocodigo.casadocodigo.autor.Autor;
import br.com.casadocodigo.casadocodigo.autor.AutorRepository;
import br.com.casadocodigo.casadocodigo.autor.NovoAutorRequest;
import br.com.casadocodigo.casadocodigo.share.EmailDuplicadoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailDuplicadoValidatorUnitTest {
    @InjectMocks
    private EmailDuplicadoValidator emailDuplicadoValidator;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private Errors errors;

    private NovoAutorRequest novoAutorRequest;

    @BeforeEach
    void setUp() {
        novoAutorRequest = new NovoAutorRequest("João", "joao@email.com", "Autor de livros técnicos");
    }

    @Test
    @DisplayName("Deve aceitar email quando não estiver cadastrado")
    void deveAceitarEmailQuandoNaoEstiverCadastrado() {
        when(autorRepository.findByEmail(novoAutorRequest.getEmail())).thenReturn(Optional.empty());

        emailDuplicadoValidator.validate(novoAutorRequest, errors);

        verify(errors, never()).rejectValue(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Deve rejeitar email quando já estiver cadastrado")
    void deveRejeitarEmailQuandoJaEstiverCadastrado() {
        Autor autorExistente = new Autor("João", "joao@email.com", "Autor de livros técnicos", LocalDateTime.now());
        when(autorRepository.findByEmail(novoAutorRequest.getEmail())).thenReturn(Optional.of(autorExistente));

        emailDuplicadoValidator.validate(novoAutorRequest, errors);

        verify(errors).rejectValue("email", null, "O email joao@email.com já está cadastrado. Adicione um outro email");
    }

    @Test
    @DisplayName("Não deve validar caso já existam outros erros no formulário")
    void naoDeveValidarCasoJaExistamOutrosErros() {
        when(errors.hasErrors()).thenReturn(true);

        emailDuplicadoValidator.validate(novoAutorRequest, errors);

        verify(errors, never()).rejectValue(anyString(), any(), anyString());
        verify(autorRepository, never()).findByEmail(anyString());
    }
}