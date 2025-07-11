package vn.dungmount.jobsite.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.dungmount.jobsite.util.constant.EnumGender;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private Long id;
    private String name;
    private String email;
    int age;
    private EnumGender gender;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
}
