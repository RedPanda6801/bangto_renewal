package com.example.banto.Comments;

import java.time.LocalDateTime;

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
	
	@Column(name="IMG", nullable=true)
    private String img;

	@ManyToOne
	@JoinColumn(name="BUYER_PK")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="OPTION_PK")
	private Options option;
}
