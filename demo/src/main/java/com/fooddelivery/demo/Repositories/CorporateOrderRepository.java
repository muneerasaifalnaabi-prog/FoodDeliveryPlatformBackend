package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.CorporateOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateOrderRepository extends JpaRepository<CorporateOrder,Integer> {
}
