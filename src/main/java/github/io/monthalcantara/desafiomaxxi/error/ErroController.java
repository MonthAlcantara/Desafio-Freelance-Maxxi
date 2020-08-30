package github.io.monthalcantara.desafiomaxxi.error;

import github.io.monthalcantara.desafiomaxxi.exceptions.RecursoNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErroController {

    @ExceptionHandler(RecursoNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError capturaErroNotFound(RecursoNaoEncontrado e){
        return new ApiError(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError capturaErroValidationError(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        List<String> messages = bindingResult.getAllErrors()
                .stream().map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiError(messages);

    }

}
