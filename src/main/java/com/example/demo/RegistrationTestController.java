package com.example.demo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegistrationTestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerUser(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping("/register")
    public String validateUsers(@Valid User user, BindingResult bindingResult, Model model){
        boolean validation = true;
        if(bindingResult.hasErrors()){
            return "register";
        }
        if(userRepository.existsByEmail(user.getEmail())){
            model.addAttribute("emailError", "E-mail zajęty! Podaj inny!");
            validation = false;
        }
        if(userRepository.existsByUsername(user.getUsername())){
            model.addAttribute("usernameError", "Nazwa użytkownika zajęta! Podaj inną!");
            validation = false;
        }
        if(validation) {
            user.setId(null);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/login";
        } else{
            return "register";
        }
    }
}
