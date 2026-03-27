package com.simulation.offer_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simulation.offer_system.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

}
