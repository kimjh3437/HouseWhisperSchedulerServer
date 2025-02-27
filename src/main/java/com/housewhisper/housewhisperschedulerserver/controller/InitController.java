package com.housewhisper.housewhisperschedulerserver.controller;

import com.housewhisper.housewhisperschedulerserver.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private InitService initService;

    /**
     * Endpoint to initialize the system.
     */
    @PostMapping("/init")
    public void initialize() {
        initService.init();
    }

    /**
     * Endpoint to generate a random description.
     * @return String containing the generated description.
     */
    @GetMapping("/generateRandomDescription")
    public String generateRandomDescription() {
        return initService.generateRandomDescription();
    }
}