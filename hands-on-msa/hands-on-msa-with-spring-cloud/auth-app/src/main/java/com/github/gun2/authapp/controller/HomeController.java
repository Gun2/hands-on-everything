package com.github.gun2.authapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    ResponseEntity<String> home(){
        return ResponseEntity.ok("Hello");
    }
}
