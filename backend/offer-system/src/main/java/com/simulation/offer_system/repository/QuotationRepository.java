package com.simulation.offer_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulation.offer_system.entity.Quotation;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

}
