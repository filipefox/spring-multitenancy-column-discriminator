package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PatchMapping("/{id}")
    public User updateById(@PathVariable("id") int id, @RequestBody User user) {
        return userService.updateById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        userService.deleteById(id);
    }
}