package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.dto.Meta;
import vn.dungmount.jobsite.domain.dto.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User createUser(User user){
        return this.userRepository.save(user);
    }
    public void deleteUser(Long id){
        this.userRepository.deleteById(id);
    }
    public User getUserById(Long id){
    Optional<User> optional=this.userRepository.findById(id);
    if(optional.isPresent()){
        return optional.get();
    }
    else return null;
    }
    public ResultPaginationDTO getAllUsers(Pageable page){
        Page<User>pageUser=this.userRepository.findAll(page);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        Meta mt=new Meta();
        mt.setPage(pageUser.getNumber());
        mt.setPageSize(pageUser.getSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());
        return rs;
    }
    public User updateUser(User user){
        User currentUser=this.getUserById(user.getId());
        if(currentUser!=null){
            currentUser.setEmail(user.getEmail());
            currentUser.setName(user.getName());
            currentUser.setPassword(user.getPassword());
            this.userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User getUserByEmail(String email){
       return this.userRepository.findByEmail(email);
    }
}
