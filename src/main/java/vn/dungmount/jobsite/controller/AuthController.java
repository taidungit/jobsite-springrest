package vn.dungmount.jobsite.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.dto.LoginDTO;
import vn.dungmount.jobsite.domain.dto.ResLoginDTO;
import vn.dungmount.jobsite.service.UserService;
import vn.dungmount.jobsite.util.SecurityUtil;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${dungmount.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService=userService;
    }

  

    @PostMapping("auth/login")
    public ResponseEntity<ResLoginDTO>login(@Valid @RequestBody LoginDTO loginDTO){
        //Nạp input gồm username/password vào Security


 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //create token
    ResLoginDTO res=new ResLoginDTO();
    User currentUserDB=this.userService.getUserByEmail(loginDTO.getUsername());
    if(currentUserDB!=null){
    ResLoginDTO.UserLogin u = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(),currentUserDB.getName());
    res.setUser(u);

    }
    String accessToken=this.securityUtil.createAcessToken(authentication.getName(),res.getUser());

    res.setAccessToken(accessToken);
    //create refresh token
    String refreshToken=this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);
    // update user
    this.userService.updateUserToken(refreshToken,loginDTO.getUsername());

    //set cookies
    ResponseCookie responseCookie=
    ResponseCookie.from("refresh_token",refreshToken)
    .httpOnly(true)
    .secure(true)
    .path("/")
    .maxAge(refreshTokenExpiration)
    .build();
        return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount(){
        String email=SecurityUtil.getCurrentUserLogin().isPresent()?SecurityUtil.getCurrentUserLogin().get():"";
        User currentUserDB=this.userService.getUserByEmail(email);
        ResLoginDTO.UserLogin res=new ResLoginDTO.UserLogin();
        if(currentUserDB!=null){
            res.setEmail(currentUserDB.getEmail());
            res.setId(currentUserDB.getId());
            res.setName(currentUserDB.getName());
        }

        return ResponseEntity.ok().body(res);
    }
    @GetMapping("/auth/refresh")
    @ApiMessage("Get refresh token")
    public ResponseEntity<ResLoginDTO>getRefreshToken(
        @CookieValue(name="refresh_token",defaultValue = "abc")String refresh_token
    ) throws IdInvalidException{
        if(refresh_token.equals("abc")){
            throw new IdInvalidException("Bạn không có refresh token ở cookies");
        }
        //check valid
        Jwt decodedToken=this.securityUtil.checkValidRefreshToken(refresh_token);
        String email=decodedToken.getSubject();
        //check user by token + email
        User currentUser=this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if(currentUser==null){
            throw new IdInvalidException("Refresh token không hợp lệ");
        }
        //issue new token,set refresh token as cookies
        //create token
    ResLoginDTO res=new ResLoginDTO();
    User currentUserDB=this.userService.getUserByEmail(email);
    if(currentUserDB!=null){
    ResLoginDTO.UserLogin u = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(),currentUserDB.getName());
    res.setUser(u);

    }
    String accessToken=this.securityUtil.createAcessToken(email,res.getUser());

    res.setAccessToken(accessToken);
    //create refresh token
    String refreshToken=this.securityUtil.createRefreshToken(email, res);
    // update user
    this.userService.updateUserToken(refreshToken,email);

    //set cookies
    ResponseCookie responseCookie=
    ResponseCookie.from("refresh_token",refreshToken)
    .httpOnly(true)
    .secure(true)
    .path("/")
    .maxAge(refreshTokenExpiration)
    .build();
        return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
        .body(res);
    }
}


