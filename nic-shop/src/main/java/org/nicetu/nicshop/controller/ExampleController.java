package org.nicetu.nicshop.controller;


import lombok.RequiredArgsConstructor;
import org.nicetu.nicshop.domain.Role;
import org.nicetu.nicshop.service.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
public class ExampleController {
    private final UserServiceImpl userService;
    //@GetMapping("/hello")
//    public String sayHello() {
//        var user = User.builder()
//                .username("request.getUsername()")
//                .email("request.getEmail()")
//                .password("passwordEncoder.encode(request.getPassword())")
//                .roles(Role.CLIENT)
//                .address("")
//                .build();
//
//        System.out.println( userService.create(user));
//        return "Hello, Spring!";
//    }
}
