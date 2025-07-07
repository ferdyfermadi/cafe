package com.ferdyfermadi.cafe.controller;

import com.ferdyfermadi.cafe.model.constants.APIUrl;
import com.ferdyfermadi.cafe.model.constants.ResponseMessage;
import com.ferdyfermadi.cafe.model.dto.request.CreateCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchCustomerRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.CustomerResponse;
import com.ferdyfermadi.cafe.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APIUrl.CUSTOMER_API)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> createCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.ok(CommonResponse.<CustomerResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }

    @GetMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(CommonResponse.<CustomerResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @DeleteMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<String>> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(CommonResponse.<String>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data("Customer deleted successfully")
                .build());
    }

    @GetMapping(APIUrl.PATH_VAR_SEARCH)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> searchCustomer(@RequestParam String name) {
        List<CustomerResponse> result = customerService.searchByName(name);
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(200)
                .message("successfully fetch data")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
