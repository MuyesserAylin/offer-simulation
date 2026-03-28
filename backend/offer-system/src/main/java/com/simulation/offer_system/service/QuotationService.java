package com.simulation.offer_system.service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

import com.simulation.offer_system.dto.QuotationDTO;
import com.simulation.offer_system.dto.QuotationItemDTO;
import com.simulation.offer_system.entity.Product;
import com.simulation.offer_system.entity.Quotation;
import com.simulation.offer_system.entity.QuotationItem;
import com.simulation.offer_system.mapper.QuotationMapper;
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
    private final EmailService emailService;
    private final QuotationMapper quotationMapper;
    
    public Quotation createNewQuotationRequest(String email) {
        Quotation quotation = new Quotation();
        quotation.setCustomerEmail(email);
        quotation.setStatus("PENDING"); 
        
        return quotationRepository.save(quotation);
    }

    @Transactional 
    public String createQuotationFromExcel(MultipartFile file, Long quotationId) {
        
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Teklif bulunamadı! ID: " + quotationId));

        List<QuotationItemDTO> itemsFromExcel = excelService.readExcel(file);

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

        quotation.setStatus("PRICED"); // Fiyatlar Excel'den yüklendi
        quotationRepository.save(quotation);

        return "Teklif (ID: " + quotationId + ") başarıyla fiyatlandırıldı. Müşteri: " + quotation.getCustomerEmail();
    }
    
    public String approveQuotation(Long quotationId) {
    	Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Teklif bulunamadı!"));

        quotation.setStatus("APPROVED");
        
    
        StringBuilder mailIcerigi = new StringBuilder();
        mailIcerigi.append("Sayın Müşterimiz,\n\nTeklif detaylarınız aşağıdadır:\n");
        mailIcerigi.append("------------------------------------------\n");

        for (QuotationItem item : quotation.getItems()) {
            Product product = item.getProduct();
            
            product.setLastQuotedPrice(item.getOfferedPrice());
            product.setLastQuotationDate(LocalDateTime.now());
            productRepository.save(product); 

            // 2. MAİL METNİNE EKLE
            mailIcerigi.append("Ürün: ").append(product.getName())
                       .append(" | Fiyat: ").append(item.getOfferedPrice()).append(" TL\n");
        }

        mailIcerigi.append("------------------------------------------\n");
        mailIcerigi.append("Toplam Teklif ID: ").append(quotationId);
        
        quotationRepository.save(quotation);

        
        emailService.sendEmail(
            quotation.getCustomerEmail(),
            "Teklif Detayları ve Onay Bilgisi",
            mailIcerigi.toString()
        );

        return "Teklif onaylandı, ürün fiyatları güncellendi ve müşteriye detaylı mail gönderildi.";
    }
    
    public List<QuotationItemDTO> getProductPriceHistory(Long productId) {
  
        return quotationItemRepository.findByProductId(productId)
                .stream()
                .map(item -> {
                    
                    QuotationItemDTO dto = new QuotationItemDTO();
                    dto.setProductId(item.getProduct().getId());
                    dto.setProductName(item.getProduct().getName());
                    dto.setOfferedPrice(item.getOfferedPrice());
                    return dto;
                })
                .collect(Collectors.toList());
    }
   

    // Tüm teklifleri DTO olarak döner
    public List<QuotationDTO> getAllQuotations() {
        return quotationRepository.findAll()
                .stream()
                .map(quotationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Tek bir teklifi DTO olarak döner
    public QuotationDTO getQuotationById(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teklif bulunamadı: " + id));
        return quotationMapper.toDto(quotation);
    }
}
