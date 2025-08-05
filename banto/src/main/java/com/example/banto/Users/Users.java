package com.example.banto.Users;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Boards.Comments.Comments;
import com.example.banto.Payments.Payments;
import com.example.banto.Boards.Qnas.QNAs;
import com.example.banto.SellerAuths.SellerAuths;
import com.example.banto.Sellers.Sellers;
import com.example.banto.Payments.SoldItems;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

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

    // 가입일 -> 데이터 저장 시간에 자동 기입
    @Column(name = "REG_DATE", nullable=false)
    private LocalDateTime regDate;
    // 소셜 로그인 여부
    @Column(name="SNS_AUTH", nullable=false)
    private Boolean snsAuth;
    // 지갑
    @Column(name="CASH", nullable = false)
    private Integer cash;

    @JsonIgnore
    //@EqualsAndHashCode.Exclude
    //@ToString.Exclude
    @OneToOne(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Sellers sellers;

    // 1 : N Relation (Cascade = REMOVE)
    @JsonIgnore
    //@ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SellerAuths> sellerAuths;

/*    @JsonIgnore
    //@ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorites> favorites;*/

    // 1 : N Relation (Cascade = null)
    @JsonIgnore
    //@ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Payments> payments;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy="user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Comments> comments;

    @JsonIgnore
    //@JsonManagedReference  // 이쪽만 JSON 변환 대상이 됨
    //@ToString.Exclude
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
            .regDate(
                dto.getRegDate() != null ?
                    dto.getRegDate() : LocalDateTime.now()
            )
            .snsAuth(dto.getSnsAuth())
            .cash(dto.getCash() != null ? dto.getCash() : 0)
            .build();
    }
}
