package com.simulation.offer_system.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quotations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String status="PENDING"; 

    private LocalDateTime createdAt = LocalDateTime.now();

    
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationItem> items;
}