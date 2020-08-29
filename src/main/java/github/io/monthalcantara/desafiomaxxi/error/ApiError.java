package github.io.monthalcantara.desafiomaxxi.error;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiError {
    @Getter
    List<String> erros;

    public ApiError(String erro) {
        this.erros = Arrays.asList(erro);
    }
}
