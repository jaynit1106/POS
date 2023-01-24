package com.increff.pos.util;

import com.increff.pos.model.OrderItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class Base64Util {

    @Autowired
    private static final RestTemplate restTemplate = new RestTemplate();
    public static String getBase64(List<OrderItemData> data){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String apiUrl = "http://localhost:9090/pos/api/pdf";
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(apiUrl,data,String.class);
        return apiResponse.getBody();
    }

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
