package com.increff.pos.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {
    public static void savePdf(String base64,String name){
        String path = new File("./src/main/resources/com/increff/pos/pdf").getAbsolutePath();
        File file = new File(path+"/"+name+".pdf");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] decoder = Base64.getDecoder().decode(base64);
            fos.write(decoder);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
