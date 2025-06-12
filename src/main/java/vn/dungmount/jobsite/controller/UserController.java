package vn.dungmount.jobsite.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.service.UserService;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
 
    public UserController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        String hashPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User taidung= this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(taidung);
        
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException{
        if(id>=1500){
            throw new IdInvalidException("Id khong duoc lon qua 1500");
        }
         this.userService.deleteUser(id);
        // return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.ok("delete successfully");
    }

    
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
       User taidung= this.userService.getUserById(id);
       return ResponseEntity.ok(taidung); 
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
         List<User> users=this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User update=this.userService.updateUser(user);
        return ResponseEntity.ok(update);
        
    }
}
