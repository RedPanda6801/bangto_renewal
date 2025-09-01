package com.example.banto.Stores;

import java.util.List;

import com.example.banto.Items.Items;
import com.example.banto.SellerAuths.SellerAuthDTO;
import com.example.banto.SellerAuths.SellerAuths;
import com.example.banto.Sellers.Sellers;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stores {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="STORE_NAME", nullable=false)
    private String storeName;

    @Column(name="BUSI_NUM", nullable=false, unique = true)
    private String busiNum;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name="SELLER_PK")
    private Sellers seller;

    @OneToMany(mappedBy="store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Items> items;

    // 인증서 승인 시 첫 매장 추가
    public static Stores toInitEntity(SellerAuths apply, Sellers seller) {
        return Stores.builder()
            .seller(seller)
            .busiNum(apply.getBusiNum())
            .storeName(apply.getStoreName())
            .build();
    }

    public static Stores toEntity(StoreDTO dto, Sellers seller) {
    return Stores.builder()
        .seller(seller)
        .busiNum(dto.getBusiNum())
        .storeName(dto.getStoreName())
        .build();
    }
}
