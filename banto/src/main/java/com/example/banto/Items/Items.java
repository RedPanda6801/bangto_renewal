package com.example.banto.Items;
import java.util.List;

import com.example.banto.Carts.Carts;
import com.example.banto.Entitys.*;
import com.example.banto.Enums.CategoryType;
import com.example.banto.Options.Options;
import com.example.banto.Stores.Stores;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    
    @Column(name="IMG")
    private String img;

    @ManyToOne
    @JoinColumn(name="STORE_PK")
    private Stores store;

    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Options> options;

    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Favorites> favorites;
    
    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Carts> carts;
    
    public static Items toEntity(ItemDTO dto) {
    	return Items.builder()
    			.title(dto.getTitle())
    			.price(dto.getPrice())
    			.content(dto.getContent())
    			.category(CategoryType.valueOf(dto.getCategory()))
    			.build();
    }
}
