package vn.dungmount.jobsite.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.service.UserService;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public User createUser(@RequestBody User user){

        return this.userService.createUser(user);
        
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id){
         this.userService.deleteUser(id);
        return "Delete successfully";
    }

    
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Long id){
       return this.userService.getUserById(id); 
    }
    @GetMapping("/users")
    public List<User> getAllUser(){
         List<User> users=this.userService.getAllUsers();
        return users;
    }
    
    @PutMapping("/user")
    public User updateUser(@RequestBody User user){
        User update=this.userService.updateUser(user);
        return update;
        
    }
}
