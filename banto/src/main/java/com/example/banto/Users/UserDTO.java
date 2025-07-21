package com.example.banto.Users;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    @Email
    private String email;
    @NotNull
    private String pw;

    private String name;

    private String addr;

    private String phone;

    private LocalDateTime regDate;

    private Boolean snsAuth;

    private Integer cash;

    public static UserDTO toDTO(Users entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .pw(null)
                .name(entity.getName())
                .addr(entity.getAddr())
                .phone(entity.getPhone())
                .regDate(entity.getRegDate())
                .snsAuth(entity.getSnsAuth())
                .cash(entity.getCash())
                .build();
    }
}