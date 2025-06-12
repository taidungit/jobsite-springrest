package vn.dungmount.jobsite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
public class HelloController {


    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        if(true)
        throw new IdInvalidException("check hii");
        return "Hello World ";
    }
}
