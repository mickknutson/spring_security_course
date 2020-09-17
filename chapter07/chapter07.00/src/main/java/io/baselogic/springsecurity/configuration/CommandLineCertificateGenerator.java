package io.baselogic.springsecurity.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Generate Client side certificates
 */
@Configuration
@Slf4j
public class CommandLineCertificateGenerator {

    @Bean
    public CommandLineRunner generateCertificate(ApplicationContext ctx) {
        return args -> {
            StringBuilder sb = new StringBuilder(1_000);

            sb.append("\n------------------------------------------------");
            sb.append("\nLet's generate a certificate:");
            sb.append("\n------------------------------------------------");
            // Command:
            /*
            keytool -genkeypair -alias event_manager_client -keyalg RSA -validity 365 -keystore event_manager_clientauth.p12 -storetype PKCS12
             */

            // dialog:
            /*
            What is your first and last name?
            [Unknown]: admin1@baselogic.com
            ... etc

            Is CN=admin1@baselogic.com, OU=Event Manager, O=EM, L=Park City, ST=UT,
            C=US correct?
            [no]: yes
             */

            sb.append("\n------------------------------------------------\n");



            sb.append("\n\n------------------------------------------------\n\n");

            log.info(sb.toString());
        };
    }

} // The End...
