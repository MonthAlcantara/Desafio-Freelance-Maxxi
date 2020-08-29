package github.io.monthalcantara.desafiomaxxi.controller;

import github.io.monthalcantara.desafiomaxxi.dto.CandidatoDTO;
import github.io.monthalcantara.desafiomaxxi.exceptions.RecursoNaoEncontrado;
import github.io.monthalcantara.desafiomaxxi.model.Candidato;
import github.io.monthalcantara.desafiomaxxi.repository.CandidatoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ResponseStatus(HttpStatus.OK)
    public Page getAll(@PageableDefault(size = 5) Pageable pageable) {
      log.info("Buscando todos os candidatos registrados em banco");
        Page candidatos = candidatoRepository.findAll(pageable);
        return candidatos;
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        log.info("Buscando o candidato registrado em banco com o id: {}", id);
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);
        return new ResponseEntity(candidatoOptional.orElseThrow(() -> new RecursoNaoEncontrado("Recurso não encontrado")), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CandidatoDTO save(@RequestBody @Valid CandidatoDTO novoCandidato) {
        log.info("Adicionando um novo candidato no banco de dados");
        Candidato candidato = converteDtoParaCandidato(novoCandidato);
        candidato = candidatoRepository.save(candidato);
        return converteCandidatoParaDTO(candidato);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid CandidatoDTO candidatoDTO){
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        log.info("Buscando o candidato registrado em banco com o id: {}", id);
        candidatoRepository.deleteById(id);
    }

    private Candidato converteDtoParaCandidato(CandidatoDTO candidatoDTO) {
        return new Candidato().builder()
                .nome(candidatoDTO.getNome())
                .partido(candidatoDTO.getPartido())
                .numero(candidatoDTO.getNumero())
                .build();
    }

    private CandidatoDTO converteCandidatoParaDTO(Candidato candidato){
        return new CandidatoDTO().builder()
                .nome(candidato.getNome())
                .numero(candidato.getNumero())
                .partido(candidato.getPartido())
                .build();
    }

}

