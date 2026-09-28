package com.farmstock.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public final class Dtos {
    private Dtos() {}
    public record CropReq(@NotBlank String name, String unit) {}
    public record HarvestReq(@NotNull Long cropId, @NotNull @Positive BigDecimal quantity,
                             @NotNull @PastOrPresent LocalDate harvestDate) {}
    public record SaleReq(@NotNull Long cropId, @NotNull @Positive BigDecimal quantity,
                          @NotNull @Positive BigDecimal pricePerUnit, @PastOrPresent LocalDate saleDate) {}
    public record StockRes(Long cropId, String cropName, BigDecimal harvested, BigDecimal sold, BigDecimal stock) {}
    public record RevenueRes(Long cropId, String cropName, BigDecimal revenue) {}
}
