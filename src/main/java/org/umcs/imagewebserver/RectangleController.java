package org.umcs.imagewebserver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    private List<Rectangle> rectangles = new ArrayList<>();

// Marks a method as HTTP GET(It is used to get information from the server) request handle.This method will be
// called for GET requests by URL ("/rectangle/get").(curl -X GET localhost:8080/rectangle/getDefault)
    @GetMapping("test")
    public Rectangle getRectangle(){
        return new Rectangle(40,20,70,30,"purple");
    }

//  This annotation indicates, that method will be called only for HTTP POST-requests(
//  Used to send data to the server, in purpose of create or update resource)
//  on pointed URL-way, which in this case ("/rectangle/add").(curl -X POST localhost:8080/rectangle/add -H
//  'Content-Type: application/json' -d '{"x":40,"y":20,"width":70,"height":30,"color":"purple"}')
    @PostMapping("add")
    public String addRectangle(@RequestBody Rectangle rectangle){
        rectangles.add(rectangle);
        return "The object was successfully added :)";
    }
//    The "@RequestBody" annotation takes the object from the request body. Spring expects this object
//    to be in JSON or XML format, and it will be automatically converted to the corresponding Java object.
//
//    Now, for this process to work correctly, the object class you're passing in,
//    must have methods that allow you to access its fields - getters and setters.
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
        svgRrectangles.append("</svg>\n");
        return svgRrectangles;
    }
    @GetMapping("get")
    public Rectangle getRectangleByIndex(@RequestParam(value="index",defaultValue = "0") int index){
        if(index < 0 || index > rectangles.size()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return rectangles.get(index);
    }
//    @RequestParam to bind a request parameter value to a method argument.This method will be called
//    for GET requests by URL ("/rectangle/get?index=my_index").(url -X GET "localhost:8080/rectangle/get")

    @PutMapping("modify")
    public String modifyRectangle(
            @RequestParam(value="index",defaultValue = "0") int index, @RequestBody Rectangle rectangle){
        if(index < 0 || index > rectangles.size()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        rectangles.set(index,rectangle);
        return  "The object was successfully modified :)";
    }
//    url -X PUT "localhost:8080/rectangle/modify?index=my_index" -H 'Content-Type: application/json' -d '{"x":40,"y":20,"width":70,"height":30,"color":"purple"}'

    @DeleteMapping("delete/{index}")
    public String deleteRectangleFromList(@PathVariable int index){
        rectangles.remove(index);
        return "The object was successfully deleted :)";
    }
//    Used to bind path variables in the URL , with method arguments  in the controller.
//    It allows pull out variable from the URL and pass it as an argument to the method.
//    curl -X DELETE localhost:8080/rectangle/delete/my_index
}
