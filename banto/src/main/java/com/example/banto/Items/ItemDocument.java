package com.example.banto.Items;

import com.example.banto.Enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "items")
@Setting(replicas = 0)
public class ItemDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Long, index = false)
    private Long itemId;

    @Field(type = FieldType.Long, index = false)
    private Long storePk;

    @Field(type = FieldType.Text)
    private String storeName;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Keyword)
    private CategoryType category;

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Text, index = false)
    private String mainImage;

    public static ItemDocument toDoc(Items entity, String mainImage){
        return ItemDocument.builder()
            .itemId(entity.getId())
            .storePk(entity.getStore().getId())
            .storeName(entity.getStore().getStoreName())
            .title(entity.getTitle())
            .category(entity.getCategory())
            .price(entity.getPrice())
            .mainImage(mainImage)
            .build();
    }
}
