package com.example.banto.Payments;

import com.example.banto.Enums.DeliverType;
import com.example.banto.Users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    private Integer totalPrice;

    private LocalDateTime payDate;

    private DeliverType deliverInfo;

    private Long userId;
}
