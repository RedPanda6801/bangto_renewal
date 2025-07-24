package com.example.banto.Options;

import java.util.List;

import com.example.banto.Carts.Carts;
import com.example.banto.Comments.Comments;
import com.example.banto.SoldItems.SoldItems;
import com.example.banto.Qnas.QNAs;
import com.example.banto.Items.Items;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Options {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="ADD_PRICE", nullable=false)
    private Integer addPrice;
    
    @Column(name="OPTION_INFO", nullable=false)
    private String optionInfo;
    
    @Column(name="AMOUNT", nullable=false)
    private Integer amount;

    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionImages> optionImages;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="ITEM_PK")
    private Items item;
 
    // 'option' 필드가 Carts 엔티티에 존재해야 함
    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Carts> carts;

    // 1:N Relation (cascade = remove)
    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<QNAs> qnas;

    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<Comments> comments;

    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.ALL)  // 추가: cascade 설정
    private List<SoldItems> soldItems;
}
