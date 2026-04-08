package com.devdouanla.repository;

import com.devdouanla.domain.Expert;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Expert entity.
 */
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    default Optional<Expert> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Expert> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Expert> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select expert from Expert expert left join fetch expert.user", countQuery = "select count(expert) from Expert expert")
    Page<Expert> findAllWithToOneRelationships(Pageable pageable);

    @Query("select expert from Expert expert left join fetch expert.user")
    List<Expert> findAllWithToOneRelationships();

    @Query("select expert from Expert expert left join fetch expert.user where expert.id =:id")
    Optional<Expert> findOneWithToOneRelationships(@Param("id") Long id);
}
