package com.example.banto.Items;
import java.util.List;

import com.example.banto.Enums.CategoryType;
import com.example.banto.Options.Options;
import com.example.banto.Stores.Stores;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Items {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="TITLE", nullable=false)
    private String title;
    
	@Column(name="CATEGORY",  nullable=false)
	private CategoryType category;
    
    @Column(name="PRICE", nullable=false)
    private Integer price;
    
    @Column(name="CONTENT", nullable=false)
    private String content;

    @OneToMany(mappedBy="item", cascade = CascadeType.REMOVE, orphanRemoval = true)  // 추가: cascade 설정
    private List<ItemImages> itemImages;

    @ManyToOne
    @JoinColumn(name="STORE_PK")
    private Stores store;

    @OneToMany(mappedBy="item", cascade = CascadeType.REMOVE, orphanRemoval = true)  // 추가: cascade 설정
    private List<Options> options;

/*    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Favorites> favorites;*/

    public static Items toEntity(ItemDTO dto) {
    	return Items.builder()
    			.title(dto.getTitle())
    			.price(dto.getPrice())
    			.content(dto.getContent())
    			.category(dto.getCategory())
    			.build();
    }
}
