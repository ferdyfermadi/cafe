package com.ferdyfermadi.cafe.model.dto.response;

import com.ferdyfermadi.cafe.model.constants.PaymentStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String id;
    private String customerName;
    private String tableNumber;
    private Date transactionDate;
    private PaymentStatus status;
    private List<OrderDetailResponse> orderDetails;
}
