package com.ferdyfermadi.cafe.service;

import com.ferdyfermadi.cafe.model.dto.request.CreateCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CommonResponse<List<CustomerResponse>> getAllCustomers(int page, int size);
    CustomerResponse getCustomerById(String id);
    void deleteCustomer(String id);
    CommonResponse<List<CustomerResponse>> searchCustomers(SearchCustomerRequest request);
    List<CustomerResponse> searchByName(String name);
}
