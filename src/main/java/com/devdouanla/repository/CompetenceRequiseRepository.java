package com.devdouanla.repository;

import com.devdouanla.domain.CompetenceRequise;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompetenceRequise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompetenceRequiseRepository extends JpaRepository<CompetenceRequise, Long> {


      List<CompetenceRequise> findByPosteId(Long posteId);
}
