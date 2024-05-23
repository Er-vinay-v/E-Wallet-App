package org.UserService.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.UserService.model.User;
import org.UserService.model.enums.UserIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String name;
    private String email;
    private String phoneNo;
    private String password;
    private String userIdentifier;
    private String userIdentifierValue;
    private String dob;
    private String address;

    public User toUser(){
        User user = User.builder()
                .name(this.name)
                .email(this.email)
                .phone(this.phoneNo)
                .address(this.address)
                .userIdentifier(UserIdentifier.valueOf(this.userIdentifier))
                .userIdentifierValue(this.userIdentifierValue)
                .dob(this.dob).build();
        return user;

    }
}
