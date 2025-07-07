package com.ferdyfermadi.cafe.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String id;
    private String name;
    private String url;
    private Long size;
    private String contentType;
}
