package com.example.banto.Sellers;

import com.example.banto.Stores.Stores;
import com.example.banto.Users.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sellers {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="IS_BANNED", nullable = false)
	private Boolean isBanned;

	//@ToString.Exclude  // 필드에 적용
	@OneToMany(mappedBy="seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Stores> stores;

	@JsonIgnore
	//@EqualsAndHashCode.Exclude
	//@ToString.Exclude  // 필드에 적용
	@OneToOne
	@JoinColumn(name = "USER_PK")
	private Users user;

	public static Sellers toEntity(Users user) {
		return Sellers.builder()
			.user(user)
			.isBanned(false)
			.build();
	}
}
