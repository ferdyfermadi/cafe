package com.ferdyfermadi.cafe.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMenuRequest {
    @NotBlank
    private String name;

    @Min(0)
    private Long price;

    private String imageId;

    @NotBlank
    private String mainCourse;
}
