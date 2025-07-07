package com.ferdyfermadi.cafe.controller;

import com.ferdyfermadi.cafe.model.constants.APIUrl;
import com.ferdyfermadi.cafe.model.constants.ResponseMessage;
import com.ferdyfermadi.cafe.model.dto.request.CreateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.UpdateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.MenuResponse;
import com.ferdyfermadi.cafe.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APIUrl.MENU_API)
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<CommonResponse<MenuResponse>> createMenu(@ModelAttribute @Valid CreateMenuRequest request) {
        MenuResponse response = menuService.create(request);
        return ResponseEntity.ok(CommonResponse.<MenuResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(menuService.getAll(page, size));
    }

    @GetMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<MenuResponse>> getById(@PathVariable String id) {
        MenuResponse response = menuService.getById(id);
        return ResponseEntity.ok(CommonResponse.<MenuResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build());
    }

    @DeleteMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        menuService.delete(id);
        return ResponseEntity.ok(CommonResponse.<String>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .data("Menu deleted successfully")
                .build());
    }

    @PostMapping(APIUrl.PATH_VAR_SEARCH)
    public ResponseEntity<CommonResponse<List<MenuResponse>>> search(@RequestBody SearchMenuRequest request) {
        return ResponseEntity.ok(menuService.search(request));
    }

    @PutMapping(APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<MenuResponse>> updateMenu(
            @PathVariable String id,
            @ModelAttribute @Valid UpdateMenuRequest request
    ) {
        MenuResponse response = menuService.update(id, request);
        return ResponseEntity.ok(CommonResponse.<MenuResponse>builder()
                .statusCode(200)
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(response)
                .build());
    }
}
