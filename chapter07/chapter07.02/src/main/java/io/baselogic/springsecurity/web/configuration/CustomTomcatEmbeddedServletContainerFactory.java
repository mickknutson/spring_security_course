package io.baselogic.springsecurity.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
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

    @Value("${tlsPort: 8443}")
    private int tlsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        log.info("***** Starting custom Tomcat Server *****");

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    /**
     * Create an HTTP redirect connector
     * NOTE: This only works with HTTP/1.1
     * @return
     */
    private Connector httpConnector() {
        log.info("***** Redirecting http traffic from port {}, to port {} *****", defaultPort, tlsPort);
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(defaultPort);
        connector.setSecure(false);
        connector.setRedirectPort(tlsPort);
        return connector;
    }

} // The End...
