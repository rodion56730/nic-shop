package org.nicetu.nicshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories// явное указание пакета
public class NicShopApplication {


    public static void main(String[] args) {
        SpringApplication.run(NicShopApplication.class, args);

    }

}
