package com.example.banto.Payments;

import com.example.banto.SoldItems.SoldItems;
import com.example.banto.Users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payments {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="TOTAL_PRICE", nullable = false)
    private Integer totalPrice;

    @Column(name="PAY_DATE")
    private LocalDateTime payDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="BUYER_PK")
    private Users user;

    //@ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy="payment")
    private List<SoldItems> soldItems;
}
