package com.ferdyfermadi.cafe.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponse {
    private String id;
    private String name;
    private Long price;
    private String imageUrl;

}

