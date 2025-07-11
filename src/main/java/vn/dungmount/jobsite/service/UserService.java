package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.dto.Meta;
import vn.dungmount.jobsite.domain.dto.ResCreateUserDTO;
import vn.dungmount.jobsite.domain.dto.ResUpdateUserDTO;
import vn.dungmount.jobsite.domain.dto.ResUserDTO;
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
    public ResultPaginationDTO getAllUsers(Specification<User> spec,Pageable page){
        Page<User>pageUser=this.userRepository.findAll(spec,page);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        Meta mt=new Meta();
        mt.setPage(page.getPageNumber()+1);
        mt.setPageSize(page.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        
            List<ResUserDTO> listUser = pageUser.getContent()
            .stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getName(),
                item.getEmail(),
                item.getAge(),
                item.getGender(),
                item.getAddress(),
                item.getUpdatedAt(),
                item.getCreatedAt()))
            .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;

    }
    public User updateUser(User user){
        User currentUser=this.getUserById(user.getId());
        if(currentUser!=null){
            currentUser.setName(user.getName());
            currentUser.setAddress(user.getAddress());
            currentUser.setAge(user.getAge());
            currentUser.setGender(user.getGender());
            currentUser=this.userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User getUserByEmail(String email){
       return this.userRepository.findByEmail(email);
    }
    public boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
    public ResCreateUserDTO convertResCreateUserDTO(User user){
        ResCreateUserDTO userDTO=new ResCreateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }
    public ResUpdateUserDTO convertResUpdateUserDTO(User user){
        ResUpdateUserDTO userDTO=new ResUpdateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    public ResUserDTO convResUserDTO(User user){
        ResUserDTO userDTO=new ResUserDTO();
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
    public void updateUserToken(String token,String email){
        User currentUser=this.getUserByEmail(email);
        if(currentUser!=null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token,String email){
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
