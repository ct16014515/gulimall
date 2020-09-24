package com.iflytek.gulimall.autherserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GulimallAurherServerMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /**
         *   @GetMapping("/login.html")
         *     public String login(Model model) {
         *         return "login";
         *     }
         *
         *     @GetMapping("/register.html")
         *     public String register(Model model) {
         *         return "register";
         *     }
         */
        registry.addViewController("register.html").setViewName("register");


    }

}
