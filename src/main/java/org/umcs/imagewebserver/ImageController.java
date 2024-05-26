package org.umcs.imagewebserver;

import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("image")
public class ImageController {
//    Base64 is a method of encoding binary data in text format.
    @GetMapping("getBase64")
    public String increaseBrightnessOfBase64(@RequestParam(value = "value") int value, @RequestBody String base64){
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));

            image = increaseBrightnessWithPoolOfThreads(image,value);

            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
//           Used to write bytes to an array. This object allows you to dynamically create an array of bytes.
            ImageIO.write(image, "jpg", imageBytes);
            return Base64.getEncoder().encodeToString(imageBytes.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    ??????????????????????????????????????????????????????????????????????????????????????
    @GetMapping("getImage")
    public byte[] increaseBrightnessImage(@RequestParam(value = "value") int value, @RequestBody String base64){
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));

            image = increaseBrightnessWithPoolOfThreads(image,value);

            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", imageBytes);
            return  imageBytes.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private BufferedImage increaseBrightnessWithPoolOfThreads(BufferedImage image, int value ){
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < image.getHeight(); ++i) {
            final int y = i;
            executorService.execute(() -> {
                for (int x = 0; x < image.getWidth(); ++x) {
                    Color color = new Color(image.getRGB(x,y));
                    int blue = color.getBlue();
                    int green = color.getGreen();
                    int red = color.getRed();
                    blue = increaseColor(blue,value);
                    green = increaseColor(green,value);
                    red = increaseColor(red,value);
                    int rgb = blue | (green << 8) | (red << 16);
                    image.setRGB(x, y, rgb);
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  image;
    }
    private int increaseColor(int color,int constant){
        color +=constant;
        if (color>255) return 255;
        else if (color<0) return 0;
        return  color;
    }
}
