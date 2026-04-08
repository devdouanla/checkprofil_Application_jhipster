package com.devdouanla.repository;

import com.devdouanla.domain.CompetenceRequise;
import com.devdouanla.domain.Epreuve;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Epreuve entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpreuveRepository extends JpaRepository<Epreuve, Long>, JpaSpecificationExecutor<Epreuve> {

  List<Epreuve> findByCompetenceId(Long competenceId);
  

}
