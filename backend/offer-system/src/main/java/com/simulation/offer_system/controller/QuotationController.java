package com.simulation.offer_system.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simulation.offer_system.dto.QuotationDTO;
import com.simulation.offer_system.dto.QuotationItemDTO;
import com.simulation.offer_system.entity.Product;
import com.simulation.offer_system.entity.Quotation;
import com.simulation.offer_system.entity.QuotationItem;
import com.simulation.offer_system.repository.QuotationItemRepository;
import com.simulation.offer_system.service.ExcelService;
import com.simulation.offer_system.service.ProductService;
import com.simulation.offer_system.service.QuotationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuotationController {
	
	private final QuotationItemRepository quotationItemRepository;
	private final QuotationService quotationService;
    private final ProductService productService;
    private final ExcelService excelService;

    
    @GetMapping("/export-products")
    public ResponseEntity<InputStreamResource> exportProducts(
            @RequestParam String email, 
            @RequestParam List<Long> productIds) {

        
        Quotation quotation = quotationService.createNewQuotationRequest(email);

       
        List<Product> selectedProducts = productService.getProductsByIds(productIds);

        // 3. Excel'i SADECE BU SEÇİLEN ürünlerle oluşturuyoruz
        InputStreamResource file = new InputStreamResource(
            excelService.exportProductsToExcel(selectedProducts)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quotation_" + quotation.getId() + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    
    @PostMapping("/import")
    public ResponseEntity<String> importQuotation(
            @RequestParam("file") MultipartFile file,
            @RequestParam("quotationId") Long quotationId) { 
        
        String result = quotationService.createQuotationFromExcel(file, quotationId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveQuotation(@PathVariable Long id) {
        String result = quotationService.approveQuotation(id);
        return ResponseEntity.ok(result);
    }
   
    @GetMapping("/products/{productId}/history")
    public ResponseEntity<List<QuotationItemDTO>> getProductPriceHistory(@PathVariable Long productId) {
        
        return ResponseEntity.ok(quotationService.getProductPriceHistory(productId));
    }
    
    @GetMapping
    public ResponseEntity<List<QuotationDTO>> getAllQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    // Detay sayfası için tekil çekim
    @GetMapping("/{id}")
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotationById(id));
    }
    
}
