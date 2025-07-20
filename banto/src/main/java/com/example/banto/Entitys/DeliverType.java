package com.example.banto.Entitys;

public enum DeliverType {
	Preparing("배송전"),
	Delivering("배송중"),
	Delivered("배송완료");
	
	private final String value;
	
	DeliverType(String value){
		this.value = value;
	}
	
	public String getValue(){
        return value;
    }
}
