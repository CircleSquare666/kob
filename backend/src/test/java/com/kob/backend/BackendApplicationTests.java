package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("pyz"));
        System.out.println(passwordEncoder.encode("pvc"));
        System.out.println(passwordEncoder.encode("pmn"));
        System.out.println(passwordEncoder.encode("pvc"));
        System.out.println(passwordEncoder.encode("pma"));
        System.out.println(passwordEncoder.encode("pxc"));

    }

}
