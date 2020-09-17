package io.baselogic.springsecurity.configuration;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;

/**
 * Concrete implementation of {@link SocialConfigurer} with convenient default implementations of methods.
 * Specifically connecting our dataSource with the provider connection in order to save OAuth2 attribute
 * data into our local database.
 *
 * @author mickknutson
 * @since chapter09.01
 */
public class DatabaseSocialConfigurer extends SocialConfigurerAdapter {

    private final DataSource dataSource;

    public DatabaseSocialConfigurer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        TextEncryptor textEncryptor = Encryptors.noOpText();
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, textEncryptor);
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer config, Environment env) {
        super.addConnectionFactories(config, env);
    }

} // The End...
