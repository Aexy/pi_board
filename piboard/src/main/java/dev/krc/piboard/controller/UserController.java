package dev.krc.piboard.controller;

import dev.krc.piboard.dto.UserLoginDto;
import dev.krc.piboard.dto.UserRegistrationDto;
import dev.krc.piboard.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

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
        return "users/register";
    }

    //Handles registration submission
    @PostMapping("/register")
    public String submitRegister(@ModelAttribute("user") UserRegistrationDto registerDto, Model model) {
        HttpStatusCode statusCode = userService.register(registerDto).getStatusCode();
        switch (statusCode) {
            case HttpStatus.CONFLICT -> {
                model.addAttribute("error", "User already exists");
                return "users/register";
            }
            case HttpStatus.BAD_REQUEST -> {
                model.addAttribute("error", "Passwords do not match");
                return "users/register";
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
        return "users/reg-success";
    }


    //Display Login Get
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,Model model) {
        if(error != null) {
            model.addAttribute("error", Map.of("message", "Invalid Credentials"));
        }
        model.addAttribute("loginDto", new UserLoginDto());
        return "users/login";
    }

    //Handles login submission
    @PostMapping("/login")
    public String login(@ModelAttribute("user") UserLoginDto loginDto, Model model, HttpSession httpSession) {
        ResponseEntity<String> response = userService.login(loginDto);
        if(response.getStatusCode().is2xxSuccessful()) {
            httpSession.setAttribute("jwt",response.getBody());
            return "redirect:/dashboard";
        }
        model.addAttribute("loginDto", new UserLoginDto());
        return "users/login";
    }

    //Display success get
    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Model model, HttpSession session) {
        return "users/dashboard";
    }

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public String settings(Model model, HttpSession session) {
        return "users/settings";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse, HttpSession httpSession) {
        httpSession.invalidate();
        return "users/login";
    }

    //**************Auxiliary Functions**************

    //**************Direct Endpoint Provider Methods**************

    @GetMapping("/pi-register")
    public ResponseEntity<String> register() {
        return new ResponseEntity<>("Post only", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @GetMapping("/pi-login")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("Post only", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PostMapping("/pi-register")
    public ResponseEntity<String> register(UserRegistrationDto registerDto){
        return userService.register(registerDto);
    }

    @PostMapping("/pi-login")
    public ResponseEntity<Map<String,String>> register(UserLoginDto loginDto){
        ResponseEntity<String> response = userService.login(loginDto);
        assert response.getBody() != null;
        return new ResponseEntity<>(Map.of("token",response.getBody()), response.getStatusCode());
    }
}
