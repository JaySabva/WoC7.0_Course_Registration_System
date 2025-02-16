package org.jaysabva.woc_crs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WoCCrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoCCrsApplication.class, args);
    }

}
