package com.github.gun2.springsecurityforpersistentsession.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AppController {


    @GetMapping("/")
    @ResponseBody
    public String home(){
        return """
                <html>
                <body>
                    <h1>
                        WELCOME!
                        <br/>
                        <a href="/logout">logout</a>
                    </h1>
                </body>
                </html>
                """;
    }
}
