package com.ferdyfermadi.cafe.service;

import com.ferdyfermadi.cafe.model.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageResponse upload(MultipartFile file);
    List<ImageResponse> getAll();
    ImageResponse getById(String id);
}
