package github.io.monthalcantara.desafiomaxxi.error;

import github.io.monthalcantara.desafiomaxxi.exceptions.RecursoNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroController {

    @ExceptionHandler(RecursoNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError capturaErro(RecursoNaoEncontrado e){
        return new ApiError(e.getMessage());
    }

}
