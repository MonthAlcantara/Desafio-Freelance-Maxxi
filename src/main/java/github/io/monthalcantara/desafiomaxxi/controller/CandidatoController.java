package github.io.monthalcantara.desafiomaxxi.controller;

import github.io.monthalcantara.desafiomaxxi.dto.CandidatoDTO;
import github.io.monthalcantara.desafiomaxxi.exceptions.RecursoNaoEncontrado;
import github.io.monthalcantara.desafiomaxxi.model.Candidato;
import github.io.monthalcantara.desafiomaxxi.repository.CandidatoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/candidatos")
@Slf4j
public class CandidatoController {
    @Autowired
    CandidatoRepository candidatoRepository;

    @GetMapping
    @Cacheable("candidatos")
    @ApiOperation("Busca todos os Candidatos")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Candidatos não localizados"),
            @ApiResponse(code = 200, message = "Candidatos localizados")})
    public Page getAll(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        log.info("Buscando todos os candidatos registrados em banco");
        Page candidatos = candidatoRepository.findAll(pageable);
        return candidatos;
    }

    @GetMapping("/{id}")
    @Cacheable("candidatos")
    @ApiOperation("Busca um Candidato pelo id")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 200, message = "Candidato localizado")})
    public Candidato findById(@PathVariable Long id) {
        log.info("Buscando o candidato registrado em banco com o id: {}", id);
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);
        return candidatoOptional.orElseThrow(() -> new RecursoNaoEncontrado("Recurso não encontrado"));
    }

    @PostMapping
    @Cacheable("candidatos")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cadastra um novo Candidato")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Candidato cadastrado"),
            @ApiResponse(code = 201, message = "Candidato cadastrado com sucesso")})
    public CandidatoDTO save(@RequestBody @Valid CandidatoDTO novoCandidato) {
        log.info("Adicionando um novo candidato no banco de dados");
        Candidato candidato = converteDtoParaCandidato(novoCandidato);
        candidato = candidatoRepository.save(candidato);
        return converteCandidatoParaDTO(candidato);
    }

    @PutMapping("/{id}")
    @Cacheable("candidatos")
    @ApiOperation("Atualiza um Candidato")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 200, message = "Candidato localizado"),
            @ApiResponse(code = 201, message = "Candidato atualizado com sucesso")})
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid CandidatoDTO candidatoDTO) {
        log.info("Atualizando o candidatos registrado em banco com o id: {} ", id);
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);
        return new ResponseEntity(candidatoOptional.map(c -> {
            c.setNome(candidatoDTO.getNome());
            c.setPartido(candidatoDTO.getPartido());
            c.setNumero(candidatoDTO.getNumero());
            return converteCandidatoParaDTO(candidatoRepository.save(c));
        }).orElseThrow(() -> new RecursoNaoEncontrado("Recurso não encontrado")), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Cacheable("candidatos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Exclui um Candidato")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Recurso não encontrado"),
            @ApiResponse(code = 200, message = "Candidato localizado"),
            @ApiResponse(code = 201, message = "Candidato excluído com sucesso")})
    public void delete(@PathVariable Long id) {
        log.info("Buscando o candidato registrado em banco com o id: {}", id);
        Optional<Candidato> candidato = candidatoRepository.findById(id);
        if (candidato.isPresent()) {
            candidatoRepository.deleteById(id);
        } else {
            throw new RecursoNaoEncontrado("Recurso não encontrado");
        }
    }

    @GetMapping("/cancel")
    @CacheEvict("candidatos")
    public void cancel() {
        System.out.println("Limpando cache");
    }

    private Candidato converteDtoParaCandidato(CandidatoDTO candidatoDTO) {
        return new Candidato().builder()
                .nome(candidatoDTO.getNome())
                .partido(candidatoDTO.getPartido())
                .numero(candidatoDTO.getNumero())
                .build();
    }

    private CandidatoDTO converteCandidatoParaDTO(Candidato candidato) {
        return new CandidatoDTO().builder()
                .nome(candidato.getNome())
                .numero(candidato.getNumero())
                .partido(candidato.getPartido())
                .build();
    }

}

