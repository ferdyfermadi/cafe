package com.ferdyfermadi.cafe.service;

import com.ferdyfermadi.cafe.model.dto.request.CreateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.SearchMenuRequest;
import com.ferdyfermadi.cafe.model.dto.request.UpdateMenuRequest;
import com.ferdyfermadi.cafe.model.dto.response.CommonResponse;
import com.ferdyfermadi.cafe.model.dto.response.MenuResponse;

import java.util.List;

public interface MenuService {
    MenuResponse create(CreateMenuRequest request);
    CommonResponse<List<MenuResponse>> getAll(int page, int size);
    MenuResponse getById(String id);
    void delete(String id);
    CommonResponse<List<MenuResponse>> search(SearchMenuRequest request);
    MenuResponse update(String id, UpdateMenuRequest request);
}