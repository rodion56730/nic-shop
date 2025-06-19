package org.nicetu.nicshop;

import lombok.RequiredArgsConstructor;
import org.nicetu.nicshop.domain.Role;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class NicShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(NicShopApplication.class, args);

    }

}
