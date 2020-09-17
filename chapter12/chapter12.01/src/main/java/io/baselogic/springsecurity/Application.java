package io.baselogic.springsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Profile("debug")
    @Bean
    public CommandLineRunner passwordEncoding(ApplicationContext ctx) {
        return args -> {
            StringBuilder sb = new StringBuilder(1_000);

            sb.append("\n------------------------------------------------");
            sb.append("\nLets encrypt our standard passwords with our PasswordEncoder:");
            sb.append("\n------------------------------------------------");

            String[] passwords = {"user1", "admin1", "user2"};
            sb.append("\n\nEncoding passwords: ").append(Arrays.toString(passwords));
            sb.append("\n------------------------------------------------\n");

            for(String raw: passwords){
                String encoded = passwordEncoder.encode(raw);

                sb.append("\n[").append(raw).append("] ");
                sb.append("Matches [").append(encoded).append("] ");
                sb.append(": ").append(passwordEncoder.matches(raw, encoded));

                sb.append("\nValue for database: \n");
                sb.append("[").append(encoded).append("]");
                sb.append("\n------------------------------------------------\n");
            }

            sb.append("\n\n------------------------------------------------\n\n");

            log.info(sb.toString());
        };
    }

    @Profile("trace")
    @Bean
    public CommandLineRunner viewBeansInContext(ApplicationContext ctx) {
        return args -> {

            StringBuilder sb = new StringBuilder(1_000);
            sb.append("\n------------------------------------------------");
            sb.append("Let's inspect the beans provided by Spring Boot:");
            sb.append("\n------------------------------------------------");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                sb.append(beanName);
            }

            sb.append("\n------------------------------------------------\n");

            log.debug(sb.toString());
        };
    }
} // The End...
