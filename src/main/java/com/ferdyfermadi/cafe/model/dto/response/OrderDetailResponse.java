package com.ferdyfermadi.cafe.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private String id;
    private String menuName;
    private Long menuPrice;
    private Integer qty;
    private Long totalPrice;
    private String note;
}
