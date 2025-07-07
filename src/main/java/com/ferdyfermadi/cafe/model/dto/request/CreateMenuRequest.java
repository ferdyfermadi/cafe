package com.ferdyfermadi.cafe.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMenuRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long price;

    private MultipartFile image;

}
