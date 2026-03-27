package com.simulation.offer_system.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDTO {
	private Long id;
    private String customerEmail;
    private String status; 
    private List<QuotationItemDTO> items;

}
