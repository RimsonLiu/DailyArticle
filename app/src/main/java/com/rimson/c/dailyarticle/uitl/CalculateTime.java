package com.rimson.c.dailyarticle.uitl;

public class CalculateTime {
    public static String calculateTime(int time){
        int minute;
        int second;

        if (time>=60){
            minute=time/60;
            second=time%60;

           if (second<10){
               return minute+":"+"0"+second;
           }else {
               return minute+":"+second;
           }

        }else {
            second=time;
            if (second<10){
                return "0"+":"+"0"+second;
            }else {
                return "0"+":"+second;
            }
        }
    }
}
