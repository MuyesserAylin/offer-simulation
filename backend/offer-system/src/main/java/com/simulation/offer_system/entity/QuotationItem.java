package com.simulation.offer_system.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quotation_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class QuotationItem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    
    @ManyToOne
    @JoinColumn(name = "quotation_id")
    @JsonIgnore
    private Quotation quotation;

    @Column(nullable = false)
    private Double offeredPrice;

}
