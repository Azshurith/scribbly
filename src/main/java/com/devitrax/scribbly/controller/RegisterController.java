package com.devitrax.scribbly.controller;

import com.devitrax.scribbly.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

/**
 * Controller responsible for handling user registration.
 */
@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration page. If the user is already authenticated, redirects to the post list.
     *
     * @param principal the currently authenticated user
     * @return the registration view or a redirect
     */
    @GetMapping("/register")
    public String index(Principal principal) {
        if (principal != null) {
            return "redirect:/post/list";
        }
        return "authentication/register";
    }

    /**
     * Handles user registration form submission.
     *
     * @param username the desired username
     * @param password the desired password
     * @return redirect to the login page after successful registration
     */
    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password
    ) {
        userService.register(username, password);
        return "redirect:/login";
    }
}