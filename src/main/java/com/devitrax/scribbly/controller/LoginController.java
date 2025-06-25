package com.devitrax.scribbly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling login page requests.
 */
@Controller
public class LoginController {

    /**
     * Handles GET requests to "/login" and returns the login view.
     *
     * @return the name of the login template
     */
    @GetMapping("/login")
    public String index() {
        return "authentication/login";
    }
}
