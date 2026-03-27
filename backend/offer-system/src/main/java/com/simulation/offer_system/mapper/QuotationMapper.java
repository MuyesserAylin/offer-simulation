package com.simulation.offer_system.mapper;

import java.util.stream.Collectors;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.stereotype.Component;

import com.simulation.offer_system.dto.QuotationDTO;
import com.simulation.offer_system.dto.QuotationItemDTO;
import com.simulation.offer_system.entity.Quotation;

@Component
public class QuotationMapper {
	
	public QuotationDTO toDto(Quotation quotation) {
        if (quotation == null) return null;

        QuotationDTO dto = new QuotationDTO();
        dto.setId(quotation.getId());
        dto.setCustomerEmail(quotation.getCustomerEmail());
        dto.setStatus(quotation.getStatus());
        
        
        if (quotation.getItems() != null) {
            dto.setItems(quotation.getItems().stream().map(item -> {
                QuotationItemDTO itemDto = new QuotationItemDTO();
                itemDto.setProductId(item.getProduct().getId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setOfferedPrice(item.getOfferedPrice());
                return itemDto;
            }).collect(Collectors.toList()));
        }
        
        return dto;
    }

}
