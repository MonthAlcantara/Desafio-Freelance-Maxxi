package github.io.monthalcantara.desafiomaxxi.repository;

import github.io.monthalcantara.desafiomaxxi.model.Candidato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Page<Candidato> findAll(Pageable pageable);

    void deleteById(Long id);

    Optional<Candidato> findById(Long id);
}
