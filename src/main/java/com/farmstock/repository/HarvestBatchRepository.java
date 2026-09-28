package com.farmstock.repository;
import com.farmstock.model.HarvestBatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
public interface HarvestBatchRepository extends JpaRepository<HarvestBatch, Long> {
    @Query("select coalesce(sum(h.quantity),0) from HarvestBatch h where h.crop.id = :cropId")
    BigDecimal totalHarvested(@Param("cropId") Long cropId);
}
