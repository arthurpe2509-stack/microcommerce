package com.microcomerce.order_service.repository;

import com.microcomerce.order_service.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
