package com.example.banto.Utils;

import com.example.banto.Options.Options;

public class PurchaseHandler {
    public static int priceCulc(Options option, int amount){
        return (option.getAddPrice() + option.getItem().getPrice()) * amount;
    }
}
