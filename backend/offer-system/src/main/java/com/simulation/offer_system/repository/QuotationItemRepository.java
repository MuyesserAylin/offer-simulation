package com.simulation.offer_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulation.offer_system.entity.QuotationItem;
@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem,Long>{
	
	List<QuotationItem> findByProductId(Long productId);

}
