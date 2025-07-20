package com.example.banto.Entitys;
import java.util.List;

import com.example.banto.DTOs.ItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"store", "qnas" ,"options", "events", "comments", "favorites", "carts", "pays"})
public class Items {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
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

    // 1:N Relation (cascade = remove)
    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<QNAs> qnas;
    
    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Options> options;

    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<GroupBuyItems> events;

    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Comments> comments;

    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Favorites> favorites;
    
    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Carts> carts;

    // 1 : N Relation (cascade = null)
    @JsonIgnore
    @OneToMany(mappedBy="item", cascade = CascadeType.PERSIST)  // 추가: cascade 설정
    private List<GroupItemPays> pays;
    
    public static Items toEntity(ItemDTO dto) {
    	return Items.builder()
    			.title(dto.getTitle())
    			.price(dto.getPrice())
    			.content(dto.getContent())
    			.category(CategoryType.valueOf(dto.getCategory()))
    			.build();
    }
}
