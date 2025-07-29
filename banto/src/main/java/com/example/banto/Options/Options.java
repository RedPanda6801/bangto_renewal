package com.example.banto.Options;

import java.util.List;

import com.example.banto.Boards.Comments.Comments;
import com.example.banto.SoldItems.SoldItems;
import com.example.banto.Boards.Qnas.QNAs;
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
    @ManyToOne
    @JoinColumn(name="ITEM_PK")
    private Items item;

    @JsonIgnore
    @OneToMany(mappedBy="option", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OptionImages> optionImages;

    // option과 item이 사라져도 남도록 함
    @JsonIgnore
    @OneToMany(mappedBy="option")
    private List<QNAs> qnas;

    @JsonIgnore
    @OneToMany(mappedBy="option")
    private List<Comments> comments;

    @JsonIgnore
    @OneToMany(mappedBy="option")
    private List<SoldItems> soldItems;

    public static Options toEntity(OptionDTO dto, Items item){
        return Options.builder()
            .item(item)
            .addPrice(dto.getAddPrice())
            .optionInfo(dto.getOptionInfo())
            .amount(dto.getAmount())
            .build();
    }
}
