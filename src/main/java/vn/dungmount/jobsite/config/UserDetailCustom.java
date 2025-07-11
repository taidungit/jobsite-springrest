package vn.dungmount.jobsite.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.dungmount.jobsite.service.UserService;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {
private final UserService userService;

    public UserDetailCustom(UserService userService) {
    this.userService = userService;
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      vn.dungmount.jobsite.domain.User user=this.userService.getUserByEmail(username);//email đây chính là username
       if (user == null) {
            throw new UsernameNotFoundException("Username/password không hợp lệ");
        }
       return new User(
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
       );
    }
    
}
