package com.ferdyfermadi.cafe.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchMenuRequest {
    private String name;
    private Long minPrice;
    private Long maxPrice;
    private Integer page;
    private Integer size;
}
