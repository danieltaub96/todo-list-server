package com.david.daniel.todolist.controllers.ping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author danieltaub on 01/02/2022.
 */
@RestController
@RequestMapping(value = "/ping")
public class PingController {
    @GetMapping
    public String pingGet() {
        return "ping working";
    }
}
