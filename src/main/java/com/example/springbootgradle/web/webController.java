package com.example.springbootgradle.web;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.IOException;


@RestController
public class webController {

    imageController imageProcessorController = new imageController();

    @RequestMapping(method = RequestMethod.POST, value = "/image")
    public String addNewImage(HttpServletRequest requestEntity) throws IOException {
        Integer code = this.imageProcessorController.addNewImage(requestEntity.getInputStream());
        return "Successfully added image. Image ID: " + code.toString();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/image/{id}")
    public String removeImage(@PathVariable Integer id) throws IOException {
            this.imageProcessorController.removeImage(id);
            return "Successfully removed image.";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/image/{id}/size")
    public String sizeOfImage(@PathVariable Integer id) throws dataNotFoundException {
        JSONObject json = this.imageProcessorController.sizeOfImage(id);
        return "JSON size file: " + json;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/image/{id}/histogram")
    public String histogramImage(@PathVariable Integer id) throws IOException {
        try {
            JSONObject json = this.imageProcessorController.histogramImage(id);
            return "JSON histogram file: " + json;
        } catch (IOException e) {
            return "IOException thrown.";
        }
    }
    @RequestMapping(value = "/image/{id}/crop/{x}/{y}/{w}/{h}", method = RequestMethod.GET, produces =
            MediaType.IMAGE_PNG_VALUE)
    public byte[] getGrayImage(@PathVariable Integer id, @PathVariable Integer x,@PathVariable Integer y,
                               @PathVariable Integer w, @PathVariable Integer h) throws Exception {
        return imageProcessorController.getSubimage(id,x,y,w,h);
    }
}
