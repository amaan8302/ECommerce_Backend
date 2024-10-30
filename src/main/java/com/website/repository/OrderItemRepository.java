package com.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
