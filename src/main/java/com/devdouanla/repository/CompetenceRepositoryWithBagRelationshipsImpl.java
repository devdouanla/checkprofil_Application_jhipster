package com.devdouanla.repository;

import com.devdouanla.domain.Competence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CompetenceRepositoryWithBagRelationshipsImpl implements CompetenceRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String COMPETENCES_PARAMETER = "competences";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Competence> fetchBagRelationships(Optional<Competence> competence) {
        return competence.map(this::fetchExpertses);
    }

    @Override
    public Page<Competence> fetchBagRelationships(Page<Competence> competences) {
        return new PageImpl<>(fetchBagRelationships(competences.getContent()), competences.getPageable(), competences.getTotalElements());
    }

    @Override
    public List<Competence> fetchBagRelationships(List<Competence> competences) {
        return Optional.of(competences).map(this::fetchExpertses).orElse(Collections.emptyList());
    }

    Competence fetchExpertses(Competence result) {
        return entityManager
            .createQuery(
                "select competence from Competence competence left join fetch competence.expertses where competence.id = :id",
                Competence.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Competence> fetchExpertses(List<Competence> competences) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, competences.size()).forEach(index -> order.put(competences.get(index).getId(), index));
        List<Competence> result = entityManager
            .createQuery(
                "select competence from Competence competence left join fetch competence.expertses where competence in :competences",
                Competence.class
            )
            .setParameter(COMPETENCES_PARAMETER, competences)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
