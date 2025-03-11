package com.kmacode.identityservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class UserUpdateRequest {
      String password;
      String firstName;
      String lastName;
      LocalDate dob;
}
