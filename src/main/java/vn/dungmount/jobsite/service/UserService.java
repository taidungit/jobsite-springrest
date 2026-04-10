package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.Role;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.response.ResCreateUserDTO;
import vn.dungmount.jobsite.domain.response.ResUpdateUserDTO;
import vn.dungmount.jobsite.domain.response.ResUserDTO;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.CompanyRepository;
import vn.dungmount.jobsite.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository,CompanyRepository companyRepository, RoleService roleService ) {
        this.userRepository = userRepository;
        this.companyRepository=companyRepository;
        this.roleService=roleService;
    }
    public User createUser(User user){
        //check company
        if(user.getCompany()!=null){
            Optional<Company> optional=this.companyRepository.findById(user.getCompany().getId());
            user.setCompany(optional.isPresent()?optional.get():null);
        }
        if(user.getRole()!=null){
            Role role = this.roleService.findById(user.getRole().getId());
            user.setRole(role);
        }
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
        public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable page) {
        Page<User> pageUser = this.userRepository.findAll(spec, page);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(page.getPageNumber() + 1);
        mt.setPageSize(page.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream()
                .map(item -> this.convertToResUserDTO(item))
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
            if(user.getCompany()!=null){
            Optional<Company>optional=this.companyRepository.findById(user.getCompany().getId());
            currentUser.setCompany(optional.isPresent()?optional.get():null);
            }
            if(user.getRole()!=null){
            Role role = this.roleService.findById(user.getRole().getId());
            currentUser.setRole(role);
            }
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
        ResCreateUserDTO.CompanyUser com =new ResCreateUserDTO.CompanyUser();
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setCreatedAt(user.getCreatedAt());
        if(user.getCompany()!=null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            userDTO.setCompany(com);
        }
        return userDTO;
    }
    public ResUpdateUserDTO convertResUpdateUserDTO(User user){
        ResUpdateUserDTO userDTO=new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
        if(user.getCompany()!=null){
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            userDTO.setCompany(com);
        }
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setUpdatedAt(user.getUpdatedAt());

        return userDTO;
    }

    public ResUserDTO convertToResUserDTO(User item) {
    ResUserDTO res = new ResUserDTO();
    res.setId(item.getId());
    res.setName(item.getName());
    res.setEmail(item.getEmail());
    res.setAge(item.getAge());
    res.setGender(item.getGender());
    res.setAddress(item.getAddress());
    res.setCreatedAt(item.getCreatedAt());
    res.setUpdatedAt(item.getUpdatedAt());
    if (item.getCompany() != null) {
        ResUserDTO.CompanyUser com = new ResUserDTO.CompanyUser();
        com.setId(item.getCompany().getId());
        com.setName(item.getCompany().getName());
        res.setCompany(com);
    }
    if (item.getRole() != null) {
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
        roleUser.setId(item.getRole().getId());
        roleUser.setName(item.getRole().getName());
        res.setRole(roleUser);
    }

    return res;
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
