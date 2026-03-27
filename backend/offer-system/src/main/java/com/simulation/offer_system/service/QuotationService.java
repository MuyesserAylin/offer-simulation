package com.simulation.offer_system.service;

import java.time.LocalDateTime;

import java.util.List;

import com.simulation.offer_system.dto.QuotationItemDTO;
import com.simulation.offer_system.entity.Product;
import com.simulation.offer_system.entity.Quotation;
import com.simulation.offer_system.entity.QuotationItem;
import com.simulation.offer_system.repository.ProductRepository;
import com.simulation.offer_system.repository.QuotationItemRepository;
import com.simulation.offer_system.repository.QuotationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
@RequiredArgsConstructor

public class QuotationService {

	private final ExcelService excelService;
    private final ProductRepository productRepository;
    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;

    @Transactional 
    public String createQuotationFromExcel(MultipartFile file, String customerEmail) {
        
        
        List<QuotationItemDTO> itemsFromExcel = excelService.readExcel(file);

        
        Quotation quotation = new Quotation();
        quotation.setCustomerEmail(customerEmail);
        quotation.setStatus("PENDING"); 
        quotation.setCreatedAt(LocalDateTime.now());
        quotation = quotationRepository.save(quotation);

        
        for (QuotationItemDTO itemDto : itemsFromExcel) {
        
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Ürün bulunamadı ID: " + itemDto.getProductId()));

            
            product.setLastQuotationDate(LocalDateTime.now());
            product.setLastQuotedPrice(itemDto.getOfferedPrice());
            productRepository.save(product);


            QuotationItem quotationItem = new QuotationItem();
            quotationItem.setQuotation(quotation);
            quotationItem.setProduct(product);
            quotationItem.setOfferedPrice(itemDto.getOfferedPrice());
            quotationItemRepository.save(quotationItem);
        }

        return "Teklif başarıyla oluşturuldu. ID: " + quotation.getId();
    }
    
    public String approveQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Teklif bulunamadı!"));

        
        quotation.setStatus("APPROVED");
        quotationRepository.save(quotation);

        // BURADA: İleride "Mail Gönder" kodunu çağıracağız.
        
        return "Teklif başarıyla onaylandı ve durumu APPROVED yapıldı.";
    }
    
    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }
}
