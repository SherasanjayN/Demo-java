package com.farmstock.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Crop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
    private String unit; // kg, quintal...
    @OneToMany(mappedBy = "crop") private List<HarvestBatch> batches = new ArrayList<>();
    @OneToMany(mappedBy = "crop") private List<Sale> sales = new ArrayList<>();
    public Crop() {}
    public Crop(String name, String unit) { this.name = name; this.unit = unit; }
    public Long getId() { return id; } public String getName() { return name; } public String getUnit() { return unit; }
    public void setName(String n) { name = n; } public void setUnit(String u) { unit = u; }
}
