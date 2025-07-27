package com.subscriptionapi.billing_system.dto;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;

}
