package com.ferdyfermadi.cafe.controller;

import com.ferdyfermadi.cafe.model.constants.APIUrl;
import com.ferdyfermadi.cafe.model.constants.ResponseMessage;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.ImageResponse;
import com.ferdyfermadi.cafe.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(APIUrl.IMAGE_API)
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<CommonResponse<ImageResponse>> upload(@RequestParam("file") MultipartFile file) {
        ImageResponse response = imageService.upload(file);
        return ResponseEntity.ok(CommonResponse.<ImageResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ImageResponse>>> getAll() {
        return ResponseEntity.ok(CommonResponse.<List<ImageResponse>>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(imageService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ImageResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(CommonResponse.<ImageResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(imageService.getById(id))
                .build());
    }
}
