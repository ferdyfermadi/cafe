package com.ferdyfermadi.cafe.model.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCustomerRequest {
    private String name;
    private String tableNumber;
    private Integer page;
    private Integer size;
}
