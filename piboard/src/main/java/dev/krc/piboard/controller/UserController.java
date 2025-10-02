package dev.krc.piboard.controller;

import dev.krc.piboard.dto.LoginDto;
import dev.krc.piboard.dto.UserRegistrationDto;
import dev.krc.piboard.model.Users;
import dev.krc.piboard.repository.UsersRepository;
import dev.krc.piboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Objects;

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
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    //Handles login submission
    @PostMapping("/login")
    public String login(@ModelAttribute("user")LoginDto loginDto, Model model, HttpSession session) {
        HttpStatusCode statusCode = userService.login(loginDto).getStatusCode();
        switch(statusCode){
            case HttpStatus.OK -> {
                session.setAttribute("userMail", loginDto.getEmail());
                return "redirect:/dashboard";
            }
            case HttpStatus.UNAUTHORIZED -> {
                model.addAttribute("error", "Invalid email or password");
                return "redirect:/login";
            }
            default -> {
                model.addAttribute("loginDto", new LoginDto());
                return "redirect:/login";
            }
        }
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String email = Objects.toString(session.getAttribute("userMail"));
        if(email == null) {
            return "redirect:/login";
        }
        model.addAttribute("email", email);
        return "dashboard";
    }

}
