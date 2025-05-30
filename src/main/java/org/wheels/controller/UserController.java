package org.wheels.controller;

import org.springframework.web.bind.annotation.*;
import org.wheels.model.User;
import org.wheels.model.Plan;
import org.wheels.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public List<User> getAll() { return userService.getAllUsers(); }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    public void create(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PutMapping("/{email}")
    public void update(@PathVariable String email, @RequestBody User user) {
        user.setEmail(email);
        userService.saveUser(user);
    }

    @DeleteMapping("/{email}")
    public void delete(@PathVariable String email) {
        userService.deleteUserByEmail(email);
    }

    @GetMapping("/{email}/plan")
    public Plan getPlan(@PathVariable String email) {
        return userService.getUserByEmail(email).getPlano();
    }

    @PutMapping("/{email}/plan")
    public void changePlan(@PathVariable String email, @RequestBody Plan newPlan) {
        userService.changePlan(email, newPlan);
    }

    @PutMapping("/{email}/plan/cancel")
    public void cancelPlan(@PathVariable String email) {
        userService.cancelPlan(email);
    }

    @GetMapping("/{email}/multa")
    public double getMulta(@PathVariable String email) {
        return userService.getUserByEmail(email).getMultaAtual();
    }
}