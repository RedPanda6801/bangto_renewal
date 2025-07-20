package com.example.banto.Entitys;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.DTOs.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="EMAIL", nullable=false, unique=true)
    private String email;

    @JsonIgnore
    @Column(name="PW")
    private String pw;

    @Column(name="NAME", nullable=false)
    private String name;

    @Column(name="ADDR")
    private String addr;

    @Column(name="PHONE")
    private String phone;

    @Column(name = "REG_DATE", nullable=false)
    private LocalDateTime regDate;

    @Column(name="SNS_AUTH", nullable=false)
    private Boolean snsAuth;

    // 1 : 1 Relation
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallets wallets;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Sellers sellers;

    // 1 : N Relation (Cascade = REMOVE)
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SellerAuths> sellerAuths;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorites> favorites;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Carts> carts;

    // 1 : N Relation (Cascade = null)
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<SoldItems> soldItems;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<GroupItemPays> groupItemPays;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Comments> comments;

    @JsonIgnore
    @JsonManagedReference  // 이쪽만 JSON 변환 대상이 됨
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<QNAs> qnas;

    public static Users toEntity(UserDTO dto) {
        return Users.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .pw(dto.getPw())
                .name(dto.getName())
                .addr(dto.getAddr())
                .phone(dto.getPhone())
                .regDate(dto.getRegDate())
                .snsAuth(dto.getSnsAuth())
                .build();
    }
}
