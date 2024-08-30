package org.umcs.imagewebserver;

import image.ImageProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


@Controller
@RequestMapping("imageform")
public class ImageFormController {
    @GetMapping("image")
    public String getIndex(){
        return "form";
    }
    @PostMapping("upload")
    public String upload(@RequestParam("image")  MultipartFile file,
                         @RequestParam("value") int value, Model model){
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageProcessor ip = new ImageProcessor(image);
            ip.increaseBrightnessWithPoolOfThreads(value);

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
}
