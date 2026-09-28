package com.farmstock.controller;
import com.farmstock.dto.Dtos.*;
import com.farmstock.model.*;
import com.farmstock.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@RestController @RequestMapping("/api")
public class FarmController {
    private final FarmService svc;
    public FarmController(FarmService s) { svc = s; }

    @PostMapping("/crops") @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> addCrop(@Valid @RequestBody CropReq r) {
        Crop c = svc.createCrop(r); return Map.of("id", c.getId(), "name", c.getName());
    }
    @GetMapping("/crops")
    public List<Map<String, Object>> crops() {
        return svc.listCrops().stream().map(c -> Map.<String, Object>of("id", c.getId(), "name", c.getName())).toList();
    }

    @PutMapping("/crops/{id}")
    public Map<String, Object> updateCrop(@PathVariable Long id, @Valid @RequestBody CropReq r) {
        Crop c = svc.updateCrop(id, r); return Map.of("id", c.getId(), "name", c.getName());
    }

    @DeleteMapping("/crops/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCrop(@PathVariable Long id) {
        svc.deleteCrop(id);
    }
    // Feature 1
    @PostMapping("/harvests") @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> harvest(@Valid @RequestBody HarvestReq r) {
        HarvestBatch h = svc.logHarvest(r); return Map.of("id", h.getId(), "crop", h.getCrop().getName(), "quantity", h.getQuantity());
    }
    // Features 2 + 4
    @PostMapping("/sales") @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> sale(@Valid @RequestBody SaleReq r) {
        Sale s = svc.recordSale(r); return Map.of("id", s.getId(), "crop", s.getCrop().getName(), "quantity", s.getQuantity());
    }
    // Feature 3
    @GetMapping("/stock") public List<StockRes> stock() { return svc.allStock(); }
    @GetMapping("/stock/{cropId}") public StockRes stock(@PathVariable Long cropId) { return svc.stockFor(cropId); }
    // Feature 5
    @GetMapping("/revenue")
    public List<RevenueRes> revenue(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return svc.revenue(from, to);
    }
}
