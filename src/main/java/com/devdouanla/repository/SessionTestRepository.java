package com.devdouanla.repository;

import com.devdouanla.domain.SessionTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SessionTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionTestRepository extends JpaRepository<SessionTest, Long>, JpaSpecificationExecutor<SessionTest> {}
