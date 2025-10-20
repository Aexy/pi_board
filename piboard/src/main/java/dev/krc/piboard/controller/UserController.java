package dev.krc.piboard.controller;

import dev.krc.piboard.dto.UserLoginDto;
import dev.krc.piboard.dto.UserRegistrationDto;
import dev.krc.piboard.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.Duration;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Display registration get
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new UserRegistrationDto());
        return "register";
    }

    //Handles registration submission
    @PostMapping("/register")
    public String submitRegister(@ModelAttribute("user") UserRegistrationDto registerDto, Model model) {
        HttpStatusCode statusCode = userService.register(registerDto).getStatusCode();
        switch (statusCode) {
            case HttpStatus.CONFLICT -> {
                model.addAttribute("error", "User already exists");
                return "register";
            }
            case HttpStatus.BAD_REQUEST -> {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }
            case HttpStatus.CREATED -> {
                model.addAttribute("success", "success");
                return "redirect:/reg-success";
            }
            default -> {
                model.addAttribute("error",statusCode.toString());
                return "error";
            }
        }
    }

    //Display regSuccess get
    @GetMapping("/reg-success")
    public String regSuccess() {
        return "reg-success";
    }


    //Display Login Get
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDto", new UserLoginDto());
        return "login";
    }

    //Handles login submission
    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginDto loginDto, Model model, HttpServletResponse httpServletResponse, HttpSession httpSession) {
        HttpStatusCode statusCode = userService.login(loginDto).getStatusCode();
        switch(statusCode){
            case HttpStatus.OK -> {
                httpSession.setAttribute("jwt",userService.generateJwtToken(loginDto.getEmail()));
                return "redirect:/dashboard";
            }
            case HttpStatus.UNAUTHORIZED -> {
                model.addAttribute("error", "Invalid email or password");
                return "redirect:/login";
            }
            default -> {
                model.addAttribute("loginDto", new UserLoginDto());
                return "login";
            }
        }
    }

    //Display success get
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse, HttpSession httpSession) {
        httpSession.invalidate();
        return "login";
    }

    //**************Auxiliary Functions**************

    //**************Direct Endpoint Provider Methods**************

    @GetMapping("/pi-register")
    public ResponseEntity<String> register() {
        return new ResponseEntity<>("Post only", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PostMapping("/pi-register")
    public ResponseEntity<String> register(UserRegistrationDto registerDto){
        return userService.register(registerDto);
    }

    @GetMapping("/pi-login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("Post only", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PostMapping("/pi-login")
    public ResponseEntity<String> register(UserLoginDto loginDto){
        return userService.login(loginDto);
    }
}
