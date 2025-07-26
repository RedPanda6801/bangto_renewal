package com.example.banto.Items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemImagesRepository extends JpaRepository<ItemImages, Long> {
    @Query("SELECT img.imgUrl FROM ItemImages img WHERE img.items.id = :itemId")
    List<String> findAllUrlByItemId(Long itemId);

    List<ItemImages> findAllByItemId(Long itemId);
}