package org.umcs.imagewebserver;

import org.springframework.web.bind.annotation.*;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
//This annotation indicates that the class is a RESTful web service CONTROLLER and contains a method to
//handle an HTTP-request. As a result of the method, this controller is automatically transformed into
//JSON or XML and sent in response to HTTP.
@RequestMapping("rectangle")
// Define the base URL by which the controller will handle requests
public class RectangleController {

    private final Rectangle  rectangleDefault = new Rectangle(40,20,70,30,"purple");
    private List<Rectangle> rectangles = new ArrayList<>();
    
    @GetMapping("get")
//Marks a method as HTTP GET request handle.This method will be called for GET requests by URL ("/rectangle/get")
    public Rectangle getRectangle(){
        return rectangleDefault;
    }
    @PostMapping("add")
//    This annotation indicates, that method will be called only for HTTP POST-requests
//    on pointed URL-way, which in this case ("/rectangle/add").
    public String addRectangle(){
        rectangles.add(rectangleDefault);
        return "Successfully added";
    }
    @GetMapping("getList")
    public List<Rectangle> getRectangleList(){
        return rectangles;
    }
    @GetMapping("getSVG")
    public StringBuffer getSvgRectangles(){
        StringBuffer svgRrectangles = new StringBuffer();
        svgRrectangles.append("<svg width=\"300\" height=\"130\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        for(Rectangle rectangle : rectangles){
            svgRrectangles.append(String.format(Locale.ENGLISH,
                    "\t<rect width=\"%d\" height=\"%d\" x=\"%d\" y=\"%d\" fill=\"%s\" />\n"
                    ,rectangle.getWidth(),rectangle.getHeight()
                    ,rectangle.getX(),rectangle.getY()
                    ,rectangle.getColor()));
        }
        svgRrectangles.append("</svg>\n\n");
        return svgRrectangles;
    }
}
