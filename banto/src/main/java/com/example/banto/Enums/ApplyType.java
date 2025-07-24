package com.example.banto.Enums;

import lombok.Getter;

@Getter
public enum ApplyType {
	Processing("처리중"),
	Accepted("승인됨"),
	Duplicated("반려됨");
	
	private final String value;
	
	ApplyType(String value){
		this.value = value;
	}
}
