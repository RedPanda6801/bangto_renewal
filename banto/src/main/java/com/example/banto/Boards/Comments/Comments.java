package com.example.banto.Boards.Comments;

import java.time.LocalDateTime;
import java.util.List;

import com.example.banto.Items.Items;
import com.example.banto.Options.Options;
import com.example.banto.Users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comments {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="CONTENT", nullable=false)
	private String content;
	
	@Column(name="STAR",  nullable=false)
	private Integer star;
	
	@Column(name = "WRITE_DATE", nullable=false)
	private LocalDateTime writeDate;
	
	@OneToMany(mappedBy="comment")
	private List<CommentImages> commentImages;

	@ManyToOne
	@JoinColumn(name="BUYER_PK")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="OPTION_PK")
	private Options option;

	public static Comments toEntity(CommentDTO dto, Options option, Users user){
		return Comments.builder()
			.content(dto.getContent())
			.star(dto.getStar())
			.writeDate(LocalDateTime.now())
			.user(user)
			.option(option)
			.build();
	}
}
