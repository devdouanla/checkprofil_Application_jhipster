package com.devdouanla.repository;

import com.devdouanla.domain.RH;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RH entity.
 */
@Repository
public interface RHRepository extends JpaRepository<RH, Long> {
    default Optional<RH> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RH> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RH> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select rH from RH rH left join fetch rH.user", countQuery = "select count(rH) from RH rH")
    Page<RH> findAllWithToOneRelationships(Pageable pageable);

    @Query("select rH from RH rH left join fetch rH.user")
    List<RH> findAllWithToOneRelationships();

    @Query("select rH from RH rH left join fetch rH.user where rH.id =:id")
    Optional<RH> findOneWithToOneRelationships(@Param("id") Long id);
}
