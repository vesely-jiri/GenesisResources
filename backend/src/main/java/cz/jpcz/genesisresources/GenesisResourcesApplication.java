package cz.jpcz.genesisresources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class GenesisResourcesApplication {

    public static void main(String[] args) {
        log.info("Application starting");

        SpringApplication.run(GenesisResourcesApplication.class, args);
    }
}