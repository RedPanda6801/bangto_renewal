package com.example.banto.Boards.Comments;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Options.Options;
import jakarta.validation.constraints.NotNull;
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
	@NotNull
	private String content;
	@NotNull
	private Integer star;
	@NotNull
	private Long optionPk;
	private LocalDateTime writeDate;
	private Long soldItemPk;
	private Long itemPk;
	private Long userPk;
	private String optionInfo;
	private String writer;
	private List<String> commentImages;
	
	public static CommentDTO toDTO(Comments entity) {
        return CommentDTO.builder()
			.id(entity.getId())
			.content(entity.getContent())
			.star(entity.getStar())
			.writeDate(entity.getWriteDate())
			.commentImages(!entity.getCommentImages().isEmpty() ?
				entity.getCommentImages().stream().map(image ->{
					try{
						return image.getImgUrl();
					}catch (Exception e){
						return null;
					}
				}).toList() : null)
			.userPk(entity.getUser().getId())
			.writer(entity.getUser().getName())
			.optionInfo(entity.getOption().getOptionInfo())
			.build();
    }
}
