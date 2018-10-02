package com.rimson.c.dailyarticle.uitl;

import java.io.File;

public class FileIsExists {

    public static boolean fileIsExists(String strFile){
        try {
            File file=new File(strFile);
            if (!file.exists()){
                return false;
            }
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
