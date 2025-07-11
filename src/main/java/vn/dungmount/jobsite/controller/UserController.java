package vn.dungmount.jobsite.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.response.ResCreateUserDTO;
import vn.dungmount.jobsite.domain.response.ResUpdateUserDTO;
import vn.dungmount.jobsite.domain.response.ResUserDTO;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.service.UserService;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
 
    public UserController(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user) throws IdInvalidException{
        boolean isEmailExist=this.userService.isEmailExist(user.getEmail());
        if(isEmailExist){ throw new IdInvalidException(
            "Email "+user.getEmail()+" đã tồn tại. Vui lòng nhập email khác!"
        );
    }
        String hashPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User taidung= this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertResCreateUserDTO(taidung));
        
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException{
       User user= this.userService.getUserById(id);
        if(user==null){
            throw new IdInvalidException("User không tồn tại!");
        }
         this.userService.deleteUser(id);
        // return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.ok(null);
    }

    
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user ")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException{
         User user= this.userService.getUserById(id);
        if(user==null){
            throw new IdInvalidException("User không tồn tại!");
        }
       return ResponseEntity.ok(this.userService.convResUserDTO(user)); 
    }
    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(  @Filter Specification<User> spec,Pageable pageable){
           
        ResultPaginationDTO users=this.userService.getAllUsers(spec,pageable);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user)throws IdInvalidException{
        User user1= this.userService.updateUser(user);
      
          if(user1==null){
            throw new IdInvalidException("User không tồn tại!");
        }
        return ResponseEntity.ok(this.userService.convertResUpdateUserDTO(user1));
        
    }
}
