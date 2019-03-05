package mw.project.diettracker.controller;

import mw.project.diettracker.entity.dto.UserDTO;
import mw.project.diettracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable("id") Long id) {
        return service.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO data) {
        return service.createUser(data);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO data) {
        return service.updateUser(data);
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable Long id) {
        service.deleteUserById(id);
    }

}
