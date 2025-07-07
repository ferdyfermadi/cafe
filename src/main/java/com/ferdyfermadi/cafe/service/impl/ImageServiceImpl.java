package com.ferdyfermadi.cafe.service.impl;

import com.ferdyfermadi.cafe.model.dto.response.ImageResponse;
import com.ferdyfermadi.cafe.model.entity.Image;
import com.ferdyfermadi.cafe.repository.ImageRepository;
import com.ferdyfermadi.cafe.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private static final String BASE_PATH = "uploads/images/"; // kamu bisa sesuaikan ini untuk file system
    private static final String BASE_URL = "http://localhost:8080/" + BASE_PATH;

    @Override
    public ImageResponse upload(MultipartFile file) {
        try {
            String id = UUID.randomUUID().toString();
            String filename = id + "_" + file.getOriginalFilename();
            String path = BASE_PATH + filename;

            File dest = new File(path);
            dest.getParentFile().mkdirs(); // buat folder jika belum ada
            file.transferTo(dest);

            Image image = Image.builder()
                    .id(id)
                    .name(file.getOriginalFilename())
                    .path(BASE_URL + filename)
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .fileId(filename)
                    .build();

            return mapToResponse(imageRepository.save(image));
        } catch (IOException e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<ImageResponse> getAll() {
        return imageRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ImageResponse getById(String id) {
        return imageRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    private ImageResponse mapToResponse(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .name(image.getName())
                .url(image.getPath())
                .size(image.getSize())
                .contentType(image.getContentType())
                .build();
    }
}
