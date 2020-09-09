package github.io.monthalcantara.desafiomaxxi.cache;

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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableCaching
public class CacheTest {
    @MockBean
    private CandidatoRepository candidatoRepository;

    static String CANDIDATO_API = "/v1/candidatos";

    @Autowired
    MockMvc mvc;

    private Candidato candidato;

    @Test
    @DisplayName("Deve buscar a primeira vez no banco e na segunda usar o cache")
    public void testCacheFindById() throws Exception {
        candidato = geradorDeCandidato();
        BDDMockito
                .given(candidatoRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(candidato));

        for (int i = 0; i < 10; i++) {

            MockHttpServletRequestBuilder requestSemCacheTest = MockMvcRequestBuilders
                    .get(CANDIDATO_API.concat("/1"));

            mvc.perform(requestSemCacheTest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(1L))
                    .andExpect(jsonPath("numero").value(13))
                    .andExpect(jsonPath("partido").value("PT"))
                    .andExpect(jsonPath("nome").value("LULA"));
        }

        Mockito.verify(candidatoRepository,
                Mockito.times(1))
                .findById(1L);

    }

    private Candidato geradorDeCandidato() {
        return Candidato.builder()
                .numero(13)
                .partido("PT")
                .nome("LULA")
                .id(1L)
                .build();
    }
}
