package com.example.banto.Payments;

import com.example.banto.Enums.DeliverType;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;
import com.example.banto.Utils.PurchaseHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldItems {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ITEM_NAME", nullable=false)
    private String itemName;

    @Column(name="AMOUNT", nullable=false)
    private Integer amount;

    @Column(name="SOLD_PRICE", nullable=false)
    private Integer soldPrice;

    @Column(name="DELIVER_INFO", nullable=false)
    @Enumerated(EnumType.STRING)  // Enum 값을 문자열로 저장
    private DeliverType deliverInfo;

    @Column(name="BUYER_NAME")
    private String userName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="OPTION_PK")
    private Options option;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="PAYMENT_PK")
    private Payments payment;

    public static SoldItems toEntity(Users user, Options option, Integer amount){
        return SoldItems.builder()
            .userName(user.getName())
            .deliverInfo(DeliverType.Preparing)
            .soldPrice(PurchaseHandler.priceCulc(option, amount))
            .itemName(option.getItem().getTitle())
            .amount(amount)
            .build();
    }
}