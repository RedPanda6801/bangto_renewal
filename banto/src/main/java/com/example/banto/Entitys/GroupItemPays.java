package com.example.banto.Entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupItemPays {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="ITEM_TITLE")
    private String itemTitle;

    @Column(name="OPTION_INFO")
    private String optionInfo;

    @Column(name="AMOUNT", nullable=false)
    private Integer amount;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="PAY", nullable=false)
    private Integer pay;
    
    @Column(name="DELIVER_INFO", nullable=false)
    private DeliverType deliverInfo;

    @Column(name="GROUP_ITEM_PK")
    private Integer groupItemPk;

    @Column(name="SOLD_DATE", nullable=false)
    private LocalDateTime soldDate;

    @ManyToOne
    @JoinColumn(name="ITEM_PK")
    private Items item;

    @ManyToOne
    @JoinColumn(name="BUYER_PK")
    private Users user;

}
