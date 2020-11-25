package com.terrasi.terrasiapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "/application-dev.properties")
class TerrasiapiApplicationTests {

    @Test
    void contextLoads() {
    }

}
