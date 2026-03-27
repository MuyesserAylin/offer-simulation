package com.simulation.offer_system.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; 
    
    private Double lastQuotedPrice=0.0; 

    private LocalDateTime lastQuotationDate; 

    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<QuotationItem> quotationItems;
}