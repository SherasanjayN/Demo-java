package com.farmstock.repository;
import com.farmstock.model.Sale;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("select coalesce(sum(s.quantity),0) from Sale s where s.crop.id = :cropId")
    BigDecimal totalSold(@Param("cropId") Long cropId);

    @Query("select s.crop.id, s.crop.name, sum(s.quantity * s.pricePerUnit) from Sale s " +
           "where s.saleDate between :from and :to group by s.crop.id, s.crop.name")
    List<Object[]> revenueBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
