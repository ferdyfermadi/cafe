package com.ferdyfermadi.cafe.repository;

import com.ferdyfermadi.cafe.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    List<Customer> findByNameContainingIgnoreCase(String name);
}
