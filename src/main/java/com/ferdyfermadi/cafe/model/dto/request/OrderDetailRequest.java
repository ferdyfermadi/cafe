package com.ferdyfermadi.cafe.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailRequest {

    @NotBlank(message = "Menu name is required")
    private String menuName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer qty;

    private String note;
}
