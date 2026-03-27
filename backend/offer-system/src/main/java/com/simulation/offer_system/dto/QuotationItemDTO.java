package com.simulation.offer_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuotationItemDTO {
	
	private Long productId;     
    private String productName; 
    private Double offeredPrice;

}
