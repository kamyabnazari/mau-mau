package htw.berlin.de.game_manager;


import htw.berlin.de.game_manager.controller.ControllerExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "htw.berlin.de")
@EnableJpaRepositories("htw.berlin.de")
@ComponentScan(basePackages = { "htw.berlin.de" })
@EntityScan("htw.berlin.de")
public class SpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}