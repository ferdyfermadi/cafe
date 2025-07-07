package com.ferdyfermadi.cafe.service.impl;

import com.ferdyfermadi.cafe.model.dto.request.CreateCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.CustomerResponse;
import com.ferdyfermadi.cafe.model.dto.response.PagingResponse;
import com.ferdyfermadi.cafe.model.entity.Customer;
import com.ferdyfermadi.cafe.repository.CustomerRepository;
import com.ferdyfermadi.cafe.service.CustomerService;
import com.ferdyfermadi.cafe.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .tableNumber(request.getTableNumber())
                .build();

        Customer saved = customerRepository.save(customer);

        return CustomerResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .tableNumber(saved.getTableNumber())
                .build();
    }

    @Override
    public CommonResponse<List<CustomerResponse>> getAllCustomers(int page, int size) {
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));

        List<CustomerResponse> responses = customerPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(200)
                .message("Successfully fetch data")
                .data(responses)
                .paging(toPaging(customerPage, page, size))
                .build();
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToResponse(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public CommonResponse<List<CustomerResponse>> searchCustomers(SearchCustomerRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Page<Customer> customerPage = customerRepository.findAll(
                CustomerSpecification.getSpecification(request),
                PageRequest.of(page, size)
        );

        List<CustomerResponse> responses = customerPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(200)
                .message("Successfully search customers")
                .data(responses)
                .paging(toPaging(customerPage, page, size))
                .build();
    }

    private CustomerResponse mapToResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .tableNumber(c.getTableNumber())
                .build();
    }

    private PagingResponse toPaging(Page<?> page, int pageNumber, int size) {
        return PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .page(pageNumber)
                .size(size)
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    @Override
    public List<CustomerResponse> searchByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream()
                .map(this::mapToResponse)
                .toList();
    }
}
