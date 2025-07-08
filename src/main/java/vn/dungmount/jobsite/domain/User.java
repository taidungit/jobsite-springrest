package vn.dungmount.jobsite.domain;

import java.sql.Date;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.dungmount.jobsite.util.SecurityUtil;
import vn.dungmount.jobsite.util.constant.EnumGender;

@Table(name="users")
@Entity
@Getter
@Setter
public class User {
      @Id 
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotBlank(message = "Không được để trống email")
    private String email;
    @NotBlank(message = "Không được để trông mật khẩu")
    private String password;
    int age;
    @Enumerated(EnumType.STRING)
    private EnumGender gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate(){
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()== true ?SecurityUtil.getCurrentUserLogin().get():"";
        this.createdAt=Instant.now();
    }
@PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy= SecurityUtil.getCurrentUserLogin().isPresent()== true ?SecurityUtil.getCurrentUserLogin().get():"";
        this.updatedAt=Instant.now();
    }
  
    

}
