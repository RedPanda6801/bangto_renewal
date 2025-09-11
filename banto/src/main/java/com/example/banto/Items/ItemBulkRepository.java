package com.example.banto.Items;

import com.example.banto.Options.OptionDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemBulkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ItemRepository itemRepository;

    @Transactional
    public void saveAll(List<ItemDTO> itemList) {
        int batchSize = 500;
        String sql = "INSERT INTO items (title, category, price, content, store_pk)" +
            "VALUES (?, ?, ?, ?, ?)";

        for (int i = 0; i < itemList.size(); i += batchSize) {
            // 끝자리 i+500이 list 크기를 넘어가는 것 방지
            int end = Math.min(i + batchSize, itemList.size());
            List<ItemDTO> batchItemList = itemList.subList(i, end);
            jdbcTemplate.batchUpdate(sql,
                batchItemList,
                batchSize,
                (PreparedStatement ps, ItemDTO dto) -> {
                    ps.setString(1, dto.getTitle());
                    ps.setInt(2, dto.getCategory().ordinal());
                    ps.setLong(3, dto.getPrice());
                    ps.setString(4, dto.getContent());
                    ps.setLong(5, dto.getStorePk());
                });
        }
    }

    @Transactional
    public void saveOptionAll(List<OptionDTO> optionList) {
        int batchSize = 500;
        String sql = "INSERT INTO options (option_info, amount, add_price, item_pk)" +
            "VALUES (?, ?, ?, ?)";

        for (int i = 0; i < optionList.size(); i += batchSize) {
            // 끝자리 i+500이 list 크기를 넘어가는 것 방지
            int end = Math.min(i + batchSize, optionList.size());
            List<OptionDTO> optionItemList = optionList.subList(i, end);
            jdbcTemplate.batchUpdate(sql,
                optionItemList,
                batchSize,
                (PreparedStatement ps, OptionDTO dto) -> {
                    ps.setString(1, dto.getOptionInfo());
                    ps.setInt(2, dto.getAmount());
                    ps.setInt(3, dto.getAddPrice());
                    ps.setLong(4, dto.getItemPk());
                });
        }
    }


}
