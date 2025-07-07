package com.ferdyfermadi.cafe.controller;

import com.ferdyfermadi.cafe.model.constants.APIUrl;
import com.ferdyfermadi.cafe.model.constants.ResponseMessage;
import com.ferdyfermadi.cafe.model.dto.request.CreateOrderRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.OrderResponse;
import com.ferdyfermadi.cafe.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APIUrl.ORDER_API)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> create(@RequestBody @Valid CreateOrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.ok(CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(response)
                .build());
    }

    @GetMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<OrderResponse>> getById(@PathVariable String id) {
        OrderResponse response = orderService.getById(id);
        return ResponseEntity.ok(CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(orderService.getAll(page, size));
    }

    @GetMapping(APIUrl.PATH_VAR_CUSTOMER)
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getByCustomerName(
            @RequestParam String name
    ) {
        List<OrderResponse> response = orderService.getByCustomerName(name);
        return ResponseEntity.ok(CommonResponse.<List<OrderResponse>>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @GetMapping(APIUrl.PATH_VAR_CUSTOMER+"/{customerId}")
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrdersByCustomer(
            @PathVariable String customerId
    ) {
        List<OrderResponse> response = orderService.getOrderByCustomer(customerId);
        return ResponseEntity.ok(CommonResponse.<List<OrderResponse>>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @GetMapping(params = "status")
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getByStatus(@RequestParam String status) {
        List<OrderResponse> response = orderService.getByStatus(status);
        return ResponseEntity.ok(CommonResponse.<List<OrderResponse>>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @PutMapping(APIUrl.PATH_VAR_STATUS)
    public ResponseEntity<CommonResponse<OrderResponse>> updateStatusToPaid(@PathVariable String id) {
        OrderResponse response = orderService.updateStatusToPaid(id);
        return ResponseEntity.ok(CommonResponse.<OrderResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(response)
                .build());
    }

    @GetMapping(APIUrl.PATH_VAR_INVOICE)
    public ResponseEntity<byte[]> generateInvoice(@PathVariable String id) {
        byte[] pdf = orderService.generateInvoice(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_" + id + ".pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

}
