package com.kmacode.identityservice.configuration;


import com.kmacode.identityservice.entity.User;
import com.kmacode.identityservice.enums.Role;
import com.kmacode.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationInitConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    PasswordEncoder passwordEncoder;


    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
               if(userRepository.findByUsername("admin").isEmpty()) {
                   var roles = new HashSet<String>();
                   roles.add(Role.ADMIN.name());
                   User user = User.builder()
                           .username("admin")
                           .password(passwordEncoder.encode("admin"))
                           .roles(roles)

                           .build();
                   userRepository.save(user);
                   log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
