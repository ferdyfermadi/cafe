package com.ferdyfermadi.cafe.service;

import com.ferdyfermadi.cafe.model.dto.request.CreateOrderRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(CreateOrderRequest request);
    OrderResponse getById(String id);
    CommonResponse<List<OrderResponse>> getAll(Integer page, Integer size);
    List<OrderResponse> getByCustomerName(String name);
    List<OrderResponse> getOrderByCustomer(String name);
    List<OrderResponse> getByStatus(String status);
    OrderResponse updateStatusToPaid(String id);
    byte[] generateInvoice(String orderId);
}
