package com.farmstock.repository;
import com.farmstock.model.Crop;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface CropRepository extends JpaRepository<Crop, Long> {
    boolean existsByNameIgnoreCase(String name);
    // row lock so two concurrent sales can't both pass the stock check
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Crop c where c.id = :id")
    Optional<Crop> findByIdForUpdate(@Param("id") Long id);
}
