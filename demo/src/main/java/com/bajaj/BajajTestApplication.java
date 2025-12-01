package com.bajaj;

import com.bajaj.service.BajajHiringService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BajajTestApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BajajTestApplication.class, args);

        // Execute test immediately on startup
        BajajHiringService service = context.getBean(BajajHiringService.class);

        // Your registration details
        service.executeHiringTest(
                "Umang Patwari",
                "22BLC1278",
                "umangpatwari2082003@gmail.com"
        );
    }
}
