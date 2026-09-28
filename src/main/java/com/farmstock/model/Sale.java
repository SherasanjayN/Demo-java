package com.farmstock.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
public class Sale {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "crop_id") private Crop crop;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal quantity;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal pricePerUnit;
    @Column(nullable = false) private LocalDate saleDate;
    public Sale() {}
    public Sale(Crop c, BigDecimal q, BigDecimal p, LocalDate d) { crop = c; quantity = q; pricePerUnit = p; saleDate = d; }
    public Long getId() { return id; } public Crop getCrop() { return crop; }
    public BigDecimal getQuantity() { return quantity; } public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public LocalDate getSaleDate() { return saleDate; }
}
