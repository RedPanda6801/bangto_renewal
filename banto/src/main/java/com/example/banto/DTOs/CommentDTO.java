package com.example.banto.DTOs;

import java.time.LocalDateTime;

import com.example.banto.Entitys.Comments;
import com.example.banto.Entitys.SellerAuths;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
	private Integer id;
	private String content;
	private Integer star;
	private LocalDateTime writeDate;
	private Integer soldItemPk;
	private Integer itemPk;
	private Integer userPk;
	private String writer;
	private String img;
	
	public static CommentDTO toDTO(Comments entity) {
        return CommentDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .star(entity.getStar())
                .writeDate(entity.getWriteDate())
                .img(entity.getImg())
                .itemPk(entity.getItem().getId())
                .userPk(entity.getUser().getId())
                .writer(entity.getUser().getName())
                .build();
    }
}
