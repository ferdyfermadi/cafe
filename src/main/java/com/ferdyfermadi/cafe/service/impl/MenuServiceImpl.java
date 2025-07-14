package com.ferdyfermadi.cafe.service.impl;

import com.ferdyfermadi.cafe.model.constants.MenuCategory;
import com.ferdyfermadi.cafe.model.dto.request.CreateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.UpdateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchMenuRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.MenuResponse;
import com.ferdyfermadi.cafe.model.dto.response.PagingResponse;
import com.ferdyfermadi.cafe.model.entity.Image;
import com.ferdyfermadi.cafe.model.entity.Menu;
import com.ferdyfermadi.cafe.repository.ImageRepository;
import com.ferdyfermadi.cafe.repository.MenuRepository;
import com.ferdyfermadi.cafe.service.MenuService;
import com.ferdyfermadi.cafe.specification.MenuSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ImageRepository imageRepository;

    @Override
    public MenuResponse create(CreateMenuRequest request) {
        validateRequest(request.getName());

        Image image = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            MultipartFile file = request.getImage();
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = "/uploads";

            try {
                Path path = Paths.get(uploadDir);
                if (!Files.exists(path)) Files.createDirectories(path);

                Path filePath = path.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                image = imageRepository.save(Image.builder()
                        .name(fileName)
                        .path(filePath.toString())
                        .size(file.getSize())
                        .contentType(file.getContentType())
                        .fileId(UUID.randomUUID().toString())
                        .build());

            } catch (IOException e) {
                throw new RuntimeException("Failed to store image file", e);
            }
        }

        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .image(image)
                .category(request.getCategory())
                .build();

        menuRepository.save(menu);
        return mapToResponse(menu);
    }


    @Override
    public MenuResponse update(String id, UpdateMenuRequest request) {
        Menu existing = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        if (menuRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new RuntimeException("Menu name already used");
        }

        Image image = null;
        if (request.getImageId() != null && !request.getImageId().isBlank()) {
            image = imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new RuntimeException("Image not found"));
        }

        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setCategory(request.getCategory());
        existing.setImage(image);

        return mapToResponse(menuRepository.save(existing));
    }

    @Override
    public CommonResponse<List<MenuResponse>> getAll(int page, int size) {
        Page<Menu> result = menuRepository.findAll(PageRequest.of(page, size));
        return CommonResponse.<List<MenuResponse>>builder()
                .statusCode(200)
                .message("Success")
                .data(result.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .paging(toPaging(result, page, size))
                .build();
    }

    @Override
    public MenuResponse getById(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        return mapToResponse(menu);
    }

    @Override
    public void delete(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        menuRepository.delete(menu);
    }

    @Override
    public CommonResponse<List<MenuResponse>> search(SearchMenuRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Page<Menu> result = menuRepository.findAll(
                MenuSpecification.getSpecification(request),
                PageRequest.of(page, size)
        );

        return CommonResponse.<List<MenuResponse>>builder()
                .statusCode(200)
                .message("Success")
                .data(result.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .paging(toPaging(result, page, size))
                .build();
    }

    private MenuResponse mapToResponse(Menu m) {
        return MenuResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .price(m.getPrice())
                .category(m.getCategory())
                .imageUrl(m.getImage() != null ? m.getImage().getPath() : null)
                .build();
    }

    private PagingResponse toPaging(Page<?> page, int pageNum, int size) {
        return PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .page(pageNum)
                .size(size)
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    private void validateRequest(String name) {
        if(menuRepository.existsByNameIgnoreCase(name)){
            throw new IllegalArgumentException("Menu name already exist");
        }
    }
}
