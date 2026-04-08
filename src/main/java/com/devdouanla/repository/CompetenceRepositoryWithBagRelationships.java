package com.devdouanla.repository;

import com.devdouanla.domain.Competence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CompetenceRepositoryWithBagRelationships {
    Optional<Competence> fetchBagRelationships(Optional<Competence> competence);

    List<Competence> fetchBagRelationships(List<Competence> competences);

    Page<Competence> fetchBagRelationships(Page<Competence> competences);
}
