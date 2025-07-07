package com.ferdyfermadi.cafe.service.impl;

import com.ferdyfermadi.cafe.model.constants.PaymentStatus;
import com.ferdyfermadi.cafe.model.constants.ResponseMessage;
import com.ferdyfermadi.cafe.model.dto.request.CreateOrderRequest;
import com.ferdyfermadi.cafe.model.dto.request.OrderDetailRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.OrderDetailResponse;
import com.ferdyfermadi.cafe.model.dto.response.OrderResponse;
import com.ferdyfermadi.cafe.model.entity.*;
import com.ferdyfermadi.cafe.repository.*;
import com.ferdyfermadi.cafe.service.OrderService;
import com.ferdyfermadi.cafe.specification.MenuSpecification;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final MenuRepository menuRepository;

    @Override
    public OrderResponse create(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = Order.builder()
                .customer(customer)
                .transactionDate(new Date())
                .status(PaymentStatus.UNPAID)
                .build();
        order = orderRepository.save(order);

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (OrderDetailRequest item : request.getOrderDetails()) {
            Menu menu = menuRepository.findOne(MenuSpecification.nameEquals(item.getMenuName()))
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found: " + item.getMenuName()));

            Long totalPrice = menu.getPrice() * item.getQty();

            OrderDetails detail = OrderDetails.builder()
                    .order(order)
                    .menu(menu)
                    .menuPrice(menu.getPrice())
                    .qty(item.getQty())
                    .totalPrice(totalPrice)
                    .note(item.getNote())
                    .build();

            orderDetailsList.add(detail);
        }

        orderDetailRepository.saveAll(orderDetailsList);
        order.setOrderDetails(orderDetailsList);

        return toOrderResponse(order);
    }

    @Override
    public OrderResponse getById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toOrderResponse(order);
    }

    @Override
    public CommonResponse<List<OrderResponse>> getAll(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var result = orderRepository.findAll(pageable);

        List<OrderResponse> responses = result.getContent().stream()
                .map(this::toOrderResponse)
                .toList();

        return CommonResponse.<List<OrderResponse>>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(responses)
                .paging(
                        com.ferdyfermadi.cafe.model.dto.response.PagingResponse.builder()
                                .page(page)
                                .size(size)
                                .totalElements(result.getTotalElements())
                                .totalPages(result.getTotalPages())
                                .hasNext(result.hasNext())
                                .hasPrevious(result.hasPrevious())
                                .build()
                )
                .build();
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderDetailResponse> detailResponses = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponse.builder()
                        .id(detail.getId())
                        .menuName(detail.getMenu().getName())
                        .menuPrice(detail.getMenuPrice())
                        .qty(detail.getQty())
                        .totalPrice(detail.getTotalPrice())
                        .note(detail.getNote())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomer().getName())
                .tableNumber(order.getCustomer().getTableNumber())
                .transactionDate(order.getTransactionDate())
                .status(order.getStatus())
                .orderDetails(detailResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getByCustomerName(String name) {
        List<Order> orders = orderRepository.findAllByCustomer_NameIgnoreCase(name);

        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getOrderByCustomer(String customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getByStatus(String status) {
        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        List<Order> orders = orderRepository.findAllByStatus(paymentStatus);

        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse updateStatusToPaid(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        return toOrderResponse(order);
    }

    @Override
    public byte[] generateInvoice(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Invoice", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Customer: " + order.getCustomer().getName(), bodyFont));
            document.add(new Paragraph("Table Number: " + order.getCustomer().getTableNumber(), bodyFont));
            document.add(new Paragraph("Transaction Date: " + order.getTransactionDate(), bodyFont));
            document.add(new Paragraph("Status: " + order.getStatus().name(), bodyFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Menu");
            table.addCell("Price");
            table.addCell("Qty");
            table.addCell("Total");

            long grandTotal = 0L;
            for (OrderDetails detail : order.getOrderDetails()) {
                table.addCell(detail.getMenu().getName());
                table.addCell(String.valueOf(detail.getMenuPrice()));
                table.addCell(String.valueOf(detail.getQty()));
                table.addCell(String.valueOf(detail.getTotalPrice()));
                grandTotal += detail.getTotalPrice();
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Grand Total: " + grandTotal, bodyFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for your order!", bodyFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice", e);
        }
    }
}
