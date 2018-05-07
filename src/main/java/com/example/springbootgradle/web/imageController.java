package com.example.springbootgradle.web;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.validation.constraints.Null;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
class dataNotFoundException extends RuntimeException {
    public dataNotFoundException(String exception) {
    super(exception);
}}

public class imageController {
    private Map<Integer,BufferedImage> map = new HashMap<>();
    private Integer counter = 0;

    Integer addNewImage(ServletInputStream inputStream)throws IOException{
        InputStream imageStream = new BufferedInputStream(inputStream);
        BufferedImage image= ImageIO.read(imageStream);
        BufferedImage bwImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        bwImage.createGraphics().drawImage(image, 0, 0, null);
        map.put(counter,bwImage);
        counter+=1;
        return counter - 1;
    }
    void removeImage(Integer id)throws dataNotFoundException{
        if(!map.containsKey(id)||map.get(id)==null) throw new dataNotFoundException("No image with given id");
        map.put(id,null);
    }

    JSONObject sizeOfImage(Integer id)throws dataNotFoundException{
        if(!map.containsKey(id)||map.get(id)==null) throw new dataNotFoundException("No image with given id");
        JSONObject retVal = new JSONObject();
        try{
            retVal.put("Height",map.get(id).getHeight());
            retVal.put("Width",map.get(id).getWidth());
        }catch(JSONException e){}
        return retVal;
    }

    JSONObject histogramImage(Integer id)throws IOException{
        if(!map.containsKey(id)||map.get(id)==null) throw new dataNotFoundException("No image with given id");
        JSONObject retVal = new JSONObject();
        int black = 0, white = 0;
        try{
            for(int i = 0; i<map.get(id).getWidth();i++){
                for(int j = 0; j<map.get(id).getHeight();j++){
                    System.out.println(map.get(id).getRGB(i,j));
                    if(map.get(id).getRGB(i,j)==-1){
                        white++;
                    } else black++;
                }
            }
            retVal.put("Black",black);
            retVal.put("White",white);
        }catch(JSONException e){ }
        return retVal;
    }

    byte[] getSubimage(Integer id, Integer x, Integer y, Integer w, Integer h)throws IOException{
        if(!map.containsKey(id)||map.get(id)==null) throw new dataNotFoundException("No image with given id");
        if(x+w>map.get(id).getWidth()||y+h>map.get(id).getHeight()) throw new IOException("Declared area is not part of a image.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( map.get(id).getSubimage(x,y,w,h), "jpg", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }
}
