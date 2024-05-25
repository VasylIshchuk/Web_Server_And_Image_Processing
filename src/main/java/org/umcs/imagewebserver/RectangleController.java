package org.umcs.imagewebserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//This annotation indicates that the class is a RESTful web service CONTROLLER and contains a method to
//handle an HTTP-request. As a result of the method, this controller is automatically transformed into
//JSON or XML and sent in response to HTTP.
@RequestMapping("rectangle")
// Define the base URL by which the controller will handle requests
public class RectangleController {

    private final Rectangle  rectangle = new Rectangle(40,20,70,30,"purple");

    @GetMapping("get")
//Marks a method as HTTP GET request handle.This method will be called for GET requests by URL ("/rectangle/get")
    public Rectangle getRectangle(){
        return rectangle;
    }

}
