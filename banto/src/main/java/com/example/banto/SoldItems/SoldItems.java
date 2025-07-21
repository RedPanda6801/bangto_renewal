package com.example.banto.SoldItems;

import java.time.LocalDateTime;

import com.example.banto.Enums.DeliverType;
import com.example.banto.Options.Options;
import com.example.banto.Payments.Payments;
import com.example.banto.Users.Users;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="OPTION_PK")
    private Options option;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="BUYER_PK")
    private Users user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="PAYMENT_PK")
    private Payments payment;
}