package org.umcs.imagewebserver;

import image.ImageProcessor;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;


@RestController
@RequestMapping("image")
public class ImageController {
    //    Base64 is a method of encoding binary data in text format.
    @GetMapping("getBase64")
    public String increaseBrightnessOfBase64(@RequestParam(value = "value") int value, @RequestBody String base64) {
        String[] dividedBase64 = base64.split(",");
        String formatName = getFormatName(dividedBase64[0]);
        BufferedImage image = increaseBrightness(value, dividedBase64[1]);
        return dividedBase64[0] + ',' + encoder(image, formatName);
    }
    //  curl -X GET "localhost:8080/image/getBase64?value=50" -H 'Content-Type: application/json' -d  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC'

    @GetMapping("getImage")
    public byte[] increaseBrightnessImage(@RequestParam(value = "value") int value, @RequestBody String base64) {
        try {
            String[] dividedBase64 = base64.split(",");
            String formatName = getFormatName(dividedBase64[0]);

            BufferedImage image = increaseBrightness(value, dividedBase64[1]);

            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            ImageIO.write(image, formatName, imageBytes);
            return imageBytes.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //  curl -o image.png -X GET  "localhost:8080/image/getImage?value=50" -H 'Content-Type: application/json' -d  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC'

    private String getFormatName(String base64MediaType) {
        return base64MediaType.substring(base64MediaType.indexOf('/') + 1, base64MediaType.indexOf(';'));
    }

    private BufferedImage increaseBrightness(int value, String base64Data) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(decoder(base64Data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageProcessor ip = new ImageProcessor(image);
        ip.increaseBrightnessWithPoolOfThreads(value);
        return image;
    }

    private ByteArrayInputStream decoder(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new ByteArrayInputStream(decodedBytes);
    }

    private String encoder(BufferedImage image, String formatName) {
        try {
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            //  Used to write bytes to an array. This object allows you to dynamically create an array of bytes.
            ImageIO.write(image, formatName, imageBytes);
            return Base64.getEncoder().encodeToString(imageBytes.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
