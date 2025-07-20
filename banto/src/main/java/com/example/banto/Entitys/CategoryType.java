package com.example.banto.Entitys;

public enum CategoryType {
	Clothing("의류"),
	Cosmetics("화장품"),
	Electronics("전자기기"),
	Furnitures("가구"),
	Foods("식품"),
	Sports("스포츠용품"),
	Kids("유아용품"),
	Cars("차량용품"),
	Pets("반려동물 용품"),
	Books("도서");
	
	private final String value;
	
	CategoryType(String value){
		this.value = value;
	}
	
	public String getValue(){
        return value;
    }
}
