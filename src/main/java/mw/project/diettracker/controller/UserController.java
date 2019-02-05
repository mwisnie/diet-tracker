package mw.project.diettracker.controller;

import mw.project.diettracker.entity.User;
import mw.project.diettracker.entity.dto.UserDTO;
import mw.project.diettracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return service.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody UserDTO data) {
        return service.createUser(data);
    }

    @PutMapping
    public User updateUser(@RequestBody UserDTO data) {
        return service.updateUser(data);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        service.deleteUserById(id);
    }

}
