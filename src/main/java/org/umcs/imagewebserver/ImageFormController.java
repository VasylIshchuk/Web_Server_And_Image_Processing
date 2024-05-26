package org.umcs.imagewebserver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("imageform")
public class ImageFormController {
    @GetMapping("image")
    public String getIndex(){
        return "index";
    }
    @PostMapping("upload")
    public String upload(@RequestParam("image")  MultipartFile file,
                         @RequestParam("value") int value, Model model){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            image = increaseBrightnessWithPoolOfThreads(image,value);
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            ImageIO.write(image,"jpg",imageBytes);
            model.addAttribute("image", Base64.getEncoder().encodeToString(imageBytes.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "image";
    }
//    Spring MVC is used to link HTTP request parameters with the Model.

//    @RequestParam("image"): This annotation parameter tells Spring to find a parameter named "image"
//    in the request and pass it to the controller method. In this case, "image" is the name
//    of the field (<input type="file" name="image") that corresponds to the selected file.

//    MultipartFile is a type that displays the file passed in the request.

//    You can use @ModelAttribute to automatically populate an object of type 'Rectangle'
//    with this data, if .html have th:field="*{name}" for all fields.

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
                int alpha = color.getAlpha();
                blue = increaseColor(blue,value);
                green = increaseColor(green,value);
                red = increaseColor(red,value);
                alpha = increaseColor(alpha,value);
                int rgb = blue | (green << 8) | (red << 16) | (alpha << 24);
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
