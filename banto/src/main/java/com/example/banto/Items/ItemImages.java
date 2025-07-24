package com.example.banto.Items;

import com.example.banto.Stores.Stores;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImages {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="IMG")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="ITEM_PK")
    private Items item;

    public static ItemImages toEntity(Items item, String imgUrl){
        return ItemImages.builder()
            .item(item)
            .imgUrl(imgUrl)
            .build();
    }
}
