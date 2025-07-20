package com.example.banto.Utils;

public class PayCulculator {

    public static int cashBackCulc(int max, int cur, int pay){
        // 달성율
        double achieveRate = (double) (max-cur) / max * 100;
        if(achieveRate == 100.0){
            return (int)Math.round(pay * 0.2);
        }else if(achieveRate >= 80.0){
            return (int)Math.round(pay * 0.15);
        }else if(achieveRate >= 50.0){
            System.out.println(pay * 0.1);
            return (int)(pay * 0.1);
        }else{
            return 0;
        }
    }
}
