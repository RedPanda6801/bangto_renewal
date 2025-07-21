package com.example.banto.Items;
import java.util.List;

import com.example.banto.Carts.Carts;
import com.example.banto.Comments.Comments;
import com.example.banto.Entitys.Favorites;
import com.example.banto.Options.Options;
import com.example.banto.Qnas.QNAs;
import com.example.banto.Stores.Stores;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    private Long id;

    private String title;

	private String category;

    private Integer price;
    
    private String content;

    private String img;
    
    private Integer amount;
    
    private Long storePk;
    
    private Integer star;
    
    //@JsonIgnore
    private Stores store;
    
    private List<QNAs> qnas;

    private List<Comments> comments;

    private List<Options> options;
  
    private List<Favorites> favorites;

    private List<Carts> carts;
    
    @JsonIgnore
    private String storeName;
    
    @JsonIgnore
    private String priceSort;
    
    @JsonIgnore
    private Integer page;
    
    @JsonIgnore
    private Integer size;
    
    public ItemDTO(Long id, String title, String category, Integer price,
        String content, String img, Integer amount, Integer star) {
		 this.id = id;
		 this.title = title;
		 this.category = category;
		 this.price = price;
		 this.content = content;
		 this.img = img;
		 this.amount = amount;
		 this.star = star;
	}
    
    public static ItemDTO toDTO(Items entity) {
    	Integer amount = 0;
    	for(Options option : entity.getOptions()) {
    		amount += option.getAmount();
    	}
        return ItemDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .category(entity.getCategory().toString())
                .price(entity.getPrice())
                .content(entity.getContent())
                .img(entity.getImg())
                .amount(amount)
                .options(entity.getOptions())
                .star(entity.getFavorites().size())
                .qnas(entity.getQnas())
                .comments(entity.getComments())
                .options(entity.getOptions())
                .store(entity.getStore())
                .build();
    }


}
