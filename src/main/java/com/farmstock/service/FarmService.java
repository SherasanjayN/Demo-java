package com.farmstock.service;
import com.farmstock.dto.Dtos.*;
import com.farmstock.exception.*;
import com.farmstock.model.*;
import com.farmstock.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Service
public class FarmService {
    private final CropRepository crops; private final HarvestBatchRepository harvests; private final SaleRepository sales;
    public FarmService(CropRepository c, HarvestBatchRepository h, SaleRepository s) { crops = c; harvests = h; sales = s; }

    @Transactional
    public Crop createCrop(CropReq r) {
        if (crops.existsByNameIgnoreCase(r.name().trim()))
            throw new IllegalArgumentException("Crop already exists: " + r.name());
        return crops.save(new Crop(r.name().trim(), r.unit()));
    }
    public List<Crop> listCrops() { return crops.findAll(); }

    @Transactional
    public Crop updateCrop(Long id, CropReq r) {
        Crop c = crops.findById(id).orElseThrow(() -> new NotFoundException("Crop not found: " + id));
        c.setName(r.name().trim());
        c.setUnit(r.unit());
        return crops.save(c);
    }

    @Transactional
    public void deleteCrop(Long id) {
        if (!crops.existsById(id)) throw new NotFoundException("Crop not found: " + id);
        crops.deleteById(id);
    }

    @Transactional
    public HarvestBatch logHarvest(HarvestReq r) {
        Crop c = crops.findById(r.cropId()).orElseThrow(() -> new NotFoundException("Crop not found: " + r.cropId()));
        return harvests.save(new HarvestBatch(c, r.quantity(), r.harvestDate()));
    }

    /** Rule: stock = harvested - sold. Checked BEFORE saving, under a row lock on the crop. */
    @Transactional
    public Sale recordSale(SaleReq r) {
        Crop c = crops.findByIdForUpdate(r.cropId()).orElseThrow(() -> new NotFoundException("Crop not found: " + r.cropId()));
        BigDecimal available = harvests.totalHarvested(c.getId()).subtract(sales.totalSold(c.getId()));
        if (r.quantity().compareTo(available) > 0)
            throw new InsufficientStockException("Insufficient stock for " + c.getName() + ": requested "
                    + r.quantity() + ", available " + available);
        return sales.save(new Sale(c, r.quantity(), r.pricePerUnit(), r.saleDate() != null ? r.saleDate() : LocalDate.now()));
    }

    public StockRes stockFor(Long cropId) {
        Crop c = crops.findById(cropId).orElseThrow(() -> new NotFoundException("Crop not found: " + cropId));
        BigDecimal h = harvests.totalHarvested(cropId), s = sales.totalSold(cropId);
        return new StockRes(c.getId(), c.getName(), h, s, h.subtract(s));
    }
    public List<StockRes> allStock() { return crops.findAll().stream().map(c -> stockFor(c.getId())).toList(); }

    public List<RevenueRes> revenue(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new IllegalArgumentException("'from' must not be after 'to'");
        return sales.revenueBetween(from, to).stream()
                .map(o -> new RevenueRes((Long) o[0], (String) o[1], (BigDecimal) o[2])).toList();
    }
}
