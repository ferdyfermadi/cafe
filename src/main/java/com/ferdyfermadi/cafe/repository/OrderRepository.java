package com.ferdyfermadi.cafe.repository;

import com.ferdyfermadi.cafe.model.constants.PaymentStatus;
import com.ferdyfermadi.cafe.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByCustomer_NameIgnoreCase(String name);
    List<Order> findAllByStatus(PaymentStatus status);
    List<Order> findByCustomerId(String customerId);
}
