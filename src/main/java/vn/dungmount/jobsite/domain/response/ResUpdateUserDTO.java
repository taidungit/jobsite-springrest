package vn.dungmount.jobsite.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dungmount.jobsite.util.constant.GenderEnum;
@Getter
@Setter
public class ResUpdateUserDTO {
     private Long id;
    private String name;
    private String email;
    int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private CompanyUser company;
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompanyUser {
            private Long id;
            private String name;
        }
}
