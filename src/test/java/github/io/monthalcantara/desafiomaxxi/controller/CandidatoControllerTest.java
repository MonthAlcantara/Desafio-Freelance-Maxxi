package github.io.monthalcantara.desafiomaxxi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.io.monthalcantara.desafiomaxxi.model.Candidato;
import github.io.monthalcantara.desafiomaxxi.repository.CandidatoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CandidatoControllerTest {

    @MockBean
    private CandidatoRepository candidatoRepository;

    static String CANDIDATO_API = "/v1/candidatos";

    @Autowired
    MockMvc mvc;

    private Candidato candidato;


    @Test
    @DisplayName("Deve salvar um novo candidato e devolver status 201")
    public void deveSalvarCandidato() throws Exception {
        candidato = geradorDeCandidato();
        String json = new ObjectMapper().writeValueAsString(candidato);
        BDDMockito.given(candidatoRepository.save(Mockito.any(Candidato.class))).willReturn(candidato);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CANDIDATO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request).andExpect(status().isCreated())
                .andExpect(jsonPath("numero").value(13))
                .andExpect(jsonPath("partido").value("PT"))
                .andExpect(jsonPath("nome").value("LULA"));
    }

    @Test
    @DisplayName("Deve buscar um candidato pelo id e devolver status 200")
    public void deveBuscarCandidatoPeloId() throws Exception {
        candidato = geradorDeCandidato();
        String json = new ObjectMapper().writeValueAsString(candidato);
        BDDMockito.given(candidatoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(candidato));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(CANDIDATO_API.concat("/1"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(json);

    mvc
            .perform(request).andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1L))
            .andExpect(jsonPath("numero").value(13))
            .andExpect(jsonPath("partido").value("PT"))
            .andExpect(jsonPath("nome").value("LULA"));
    }

    @Test
    @DisplayName("Deve atualizar um candidato salvo e devolver status 200")
    public void deveAtualizarCandidato() throws Exception {
        candidato = geradorDeCandidato();
        String json = new ObjectMapper().writeValueAsString(candidato);
        BDDMockito.given(candidatoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(candidato));
        BDDMockito.given(candidatoRepository.save(Mockito.any(Candidato.class))).willReturn(candidato);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CANDIDATO_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("numero").value(13))
                .andExpect(jsonPath("partido").value("PT"))
                .andExpect(jsonPath("nome").value("LULA"));

    }

    @Test
    @DisplayName("Deve deletar um candidato e devolver status 204")
    public void deveDeletarCandidato() throws Exception {
        candidato = geradorDeCandidato();
        BDDMockito.given(candidatoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(candidato));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CANDIDATO_API.concat("/1"));

        mvc
                .perform(request).andExpect(status().isNoContent());
    }

    private Candidato geradorDeCandidato(){
        return Candidato.builder()
                .numero(13)
                .partido("PT")
                .nome("LULA")
                .id(1L)
                .build();
    }


}
