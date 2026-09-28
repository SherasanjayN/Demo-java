package com.farmstock.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
public class HarvestBatch {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "crop_id") private Crop crop;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal quantity;
    @Column(nullable = false) private LocalDate harvestDate;
    public HarvestBatch() {}
    public HarvestBatch(Crop c, BigDecimal q, LocalDate d) { crop = c; quantity = q; harvestDate = d; }
    public Long getId() { return id; } public Crop getCrop() { return crop; }
    public BigDecimal getQuantity() { return quantity; } public LocalDate getHarvestDate() { return harvestDate; }
}
