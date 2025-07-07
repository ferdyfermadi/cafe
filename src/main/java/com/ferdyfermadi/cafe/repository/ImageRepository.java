package com.ferdyfermadi.cafe.repository;

import com.ferdyfermadi.cafe.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
