package vn.dungmount.jobsite.domain.response;

import java.time.Instant;


import lombok.Getter;
import lombok.Setter;
import vn.dungmount.jobsite.util.constant.GenderEnum;
@Getter
@Setter
public class ResCreateUserDTO {
    private Long id;
    private String name;
    private String email;
    int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
    private CompanyUser company;
    
        @Getter
        @Setter
        public static class CompanyUser{
            private Long id;
            private String name;
        }
}
