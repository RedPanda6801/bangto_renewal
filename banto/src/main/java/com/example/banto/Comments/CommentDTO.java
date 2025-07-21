package com.example.banto.Comments;

import java.time.LocalDateTime;

import com.example.banto.Options.Options;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
	private Long id;
	private String content;
	private Integer star;
	private LocalDateTime writeDate;
	private Long soldItemPk;
	private Long itemPk;
	private Long userPk;
	private Options option;
	private String writer;
	private String img;
	
	public static CommentDTO toDTO(Comments entity) {
        return CommentDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .star(entity.getStar())
                .writeDate(entity.getWriteDate())
                .img(entity.getImg())
                .userPk(entity.getUser().getId())
                .writer(entity.getUser().getName())
				.option(entity.getOption())
                .build();
    }
}
