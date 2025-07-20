package com.example.banto.Entitys;

public enum ApplyType {
	Processing("처리중"),
	Accepted("승인됨"),
	Duplicated("반려됨"),
	Banned("추방됨");
	
	private final String value;
	
	ApplyType(String value){
		this.value = value;
	}
	
	public String getValue(){
        return value;
    }
}
