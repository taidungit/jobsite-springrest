package vn.dungmount.jobsite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class ResLoginDTO {
    private String accessToken;
    private String refreshToken;
    private UserLogin user;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static public class UserLogin {
        private Long id;
        private String email;
        private String name;
    }

}

