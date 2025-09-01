package com.example.banto;

import com.example.banto.Enums.CategoryType;
import com.example.banto.Items.ItemBulkRepository;
import com.example.banto.Items.ItemDTO;
import com.example.banto.Items.ItemRepository;
import com.example.banto.Items.Items;
import com.example.banto.Stores.StoreRepository;
import com.example.banto.Stores.Stores;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DummyDataTest {

    @Autowired
    private ItemBulkRepository itemBulkRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @Transactional
    public void setDummyItemDataWithoutBulk(){
        // 카테고리 배열 선언
        CategoryType[] categories = CategoryType.values();
        List<Items> itemList = getItems(categories);
        long startTime = System.currentTimeMillis();
        itemRepository.saveAll(itemList);
        long endTime = System.currentTimeMillis();
        System.out.println("---------------------------------");
        System.out.printf("수행시간: %d\n", endTime - startTime);
        System.out.println("---------------------------------");
    }

    @Test
    @Transactional
    public void setDummyItemData(){
        // 카테고리 배열 선언
        CategoryType[] categories = CategoryType.values();
        List<ItemDTO> itemList = getItemDTOS(categories);
        long startTime = System.currentTimeMillis();
        itemBulkRepository.saveAll(itemList);
        long endTime = System.currentTimeMillis();
        System.out.println("---------------------------------");
        System.out.printf("수행시간: %d\n", endTime - startTime);
        System.out.println("---------------------------------");
    }

    private List<ItemDTO> getItemDTOS(CategoryType[] categories) {
        List<ItemDTO> itemList = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            CategoryType category = categories[i % categories.length];
            String title = String.format("%s_%d", category.name(), i);
            ItemDTO dto = new ItemDTO();
            dto.setCategory(category);
            dto.setContent("내용" + i);
            dto.setPrice((i * 1000) % Integer.MAX_VALUE);
            dto.setTitle(title);
            dto.setStorePk((long)1);
            itemList.add(dto);
        }
        return itemList;
    }

    private List<Items> getItems(CategoryType[] categories) {
        List<Items> itemList = new ArrayList<>();
        Stores store = storeRepository.findById((long) 1).get();
        for (int i = 1; i < 10000; i++) {
            CategoryType category = categories[i % categories.length];
            String title = String.format("%s_%d", category.name(), i);
            Items entity = new Items();
            entity.setCategory(category);
            entity.setContent("내용" + i);
            entity.setPrice((i * 1000) % Integer.MAX_VALUE);
            entity.setTitle(title);
            entity.setStore(store);
            itemList.add(entity);
        }
        return itemList;
    }
}
