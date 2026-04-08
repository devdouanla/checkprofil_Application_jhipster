package com.devdouanla.repository;

import com.devdouanla.domain.Epreuve;
import com.devdouanla.domain.Question;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

      List<Question> findByEpreuveId(Long epreuveId);

}
