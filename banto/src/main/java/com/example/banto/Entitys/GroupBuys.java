package com.example.banto.Entitys;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.banto.DTOs.GroupBuyDTO;
import com.example.banto.DTOs.StoreDTO;

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
public class GroupBuys {
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "START_DATE", nullable=false)
	private LocalDateTime startDate;
	
	@Column(name="END_DATE", nullable=false)
	private LocalDateTime endDate;
	
	@OneToMany(mappedBy="event", cascade = CascadeType.PERSIST)
	private List<GroupBuyItems> items;
	
	public static GroupBuys toEntity(GroupBuyDTO dto) {
        return GroupBuys.builder()
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
