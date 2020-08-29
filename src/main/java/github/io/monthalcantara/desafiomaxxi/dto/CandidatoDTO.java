package github.io.monthalcantara.desafiomaxxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatoDTO {

    @NotBlank(message = "Campo nome é obrigatório")
    private String nome;
    @NotBlank(message = "Campo partido é obrigatório")
    private String partido;
    @NotNull(message = "Campo numero é obrigatório")
    private int numero;
}
