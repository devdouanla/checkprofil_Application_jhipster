package com.devdouanla.repository;

import com.devdouanla.domain.ReponseCandidat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReponseCandidat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReponseCandidatRepository extends JpaRepository<ReponseCandidat, Long> {}
