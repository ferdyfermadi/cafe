package com.ferdyfermadi.cafe.model.dto.request;

import com.ferdyfermadi.cafe.model.constants.MenuCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private MenuCategory category;
}
