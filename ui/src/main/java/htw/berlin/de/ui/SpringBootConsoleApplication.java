package htw.berlin.de.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Card game MauMau main App class
 */
@SpringBootApplication(scanBasePackages = "htw.berlin.de")
@EnableJpaRepositories("htw.berlin.de")
@ComponentScan(basePackages = { "htw.berlin.de" })
@EntityScan("htw.berlin.de")
public class SpringBootConsoleApplication {

    public static void main(String[] args) {
        try{
            System.out.println("its in here");
            SpringApplication.run(SpringBootConsoleApplication.class, args);
            System.out.println("not here");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
