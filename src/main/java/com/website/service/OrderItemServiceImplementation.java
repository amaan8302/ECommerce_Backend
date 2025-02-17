package com.website.service;
import org.springframework.stereotype.Service;

import com.website.model.OrderItem;
import com.website.repository.OrderItemRepository;

@Service
public class OrderItemServiceImplementation implements OrderItemService {
	private OrderItemRepository orderItemRepository;
	public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
		this.orderItemRepository=orderItemRepository;
	}
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		return orderItemRepository.save(orderItem);
	}

}
