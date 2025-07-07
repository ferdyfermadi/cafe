package com.ferdyfermadi.cafe.repository;

import com.ferdyfermadi.cafe.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, String> {
}
