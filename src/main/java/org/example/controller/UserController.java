package org.example.controller;

import org.example.entity.NewUser;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.request.LoginRequest;
import org.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    // По умолчанию создаётся роль студента, т.к обладает наименьшими правами
    @PostMapping("/registration")
    public ResponseEntity register(@RequestBody NewUser newUser){
        String token = userService.createUser(newUser.login, newUser.password, List.of(Role.USER));
        User user = userService.getByLogin(newUser.login);
        return ResponseEntity.ok().body(new LoginRequest(token, user.getRoles()));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody NewUser newUser){
        String token = null;
        try {
            token = userService.login(newUser.login, newUser.password);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Wrong login/password");
        }
        User user = userService.getByLogin(newUser.login);
        return ResponseEntity.ok().body(new LoginRequest(token, user.getRoles()));
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestParam("password") String password){
        try {
            userService.changePassword(password);
            return ResponseEntity.ok().body("Password changed");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error changing password");
        }
    }

    @GetMapping("/roles")
    public ResponseEntity roles(@RequestHeader("Authorization") String token){
        HashMap<String, Object> response = new HashMap<>();
        String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        response.put("id", userService.getByLogin(login).getId());
        response.put("roles",userService.getRoles());
        return ResponseEntity.ok().body(response);
    }
}
