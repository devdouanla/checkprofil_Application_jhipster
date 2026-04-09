package com.devdouanla.repository;

import com.devdouanla.domain.QuestionAsk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuestionAsk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionAskRepository extends JpaRepository<QuestionAsk, Long> {
    
}
