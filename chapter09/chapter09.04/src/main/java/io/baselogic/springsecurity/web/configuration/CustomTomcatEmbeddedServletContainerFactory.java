package io.baselogic.springsecurity.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This configuration class will force all HTTP traffic to redirect to HTTPS.
 *
 * @author mickknutson
 *
 * @since chapter07.01 Class created.
 */
@Configuration
@Profile("tls")
//@ConditionalOnProperty(name = "tls.enabled", matchIfMissing = false)
@Slf4j
public class CustomTomcatEmbeddedServletContainerFactory {

    @Value("${defaultPort: 8080}")
    private int defaultPort;

    @Value("${redirectPort: 8443}")
    private int redirectPort;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        log.info("*** Setting Custom Tomcat specific configurations ***");

        return factory -> {
            factory.addAdditionalTomcatConnectors(createSslRedirectingConnector());
            factory.addContextCustomizers(context -> {
                context.addConstraint(createSecurityConstraints());
            });
        };
    }

    /**
     * Customize {@link TomcatServletWebServerFactory} by creating a {@link TomcatContextCustomizer}
     * @return Connector Configured to redirect for TLS connections
     */
    private SecurityConstraint createSecurityConstraints() {

        SecurityConstraint securityConstraint = new SecurityConstraint();
        securityConstraint.setUserConstraint("CONFIDENTIAL");

        SecurityCollection collection = new SecurityCollection();
        collection.addPattern("/*");
        securityConstraint.addCollection(collection);

        return securityConstraint;
    }

    /**
     * Create an HTTP redirect connector
     * NOTE: This only works with HTTP/1.1
     * @return Connector Configured to redirect for TLS connections
     */
    private Connector createSslRedirectingConnector() {
        log.info("***** Redirecting http traffic from port {}, to port {} *****", defaultPort, redirectPort);
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(defaultPort);
        connector.setRedirectPort(redirectPort);

        /**
         Not sure, but I think this flag is for cookies:
         <cookie-config>
         <http-only>true</http-only>
         <secure>true</secure>
         </cookie-config>
         */
        connector.setSecure(false);

        return connector;
    }

} // The End...
