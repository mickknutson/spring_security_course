package io.baselogic.springsecurity.crypto.bcrypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

/**
 * A utility class that can be used to convert passwords to use BCrypt.
 * Implementation of PasswordEncoder that uses the BCrypt strong hashing function.
 *
 * @author Mick Knutson
 * @see BCryptPasswordEncoder
 */
@Slf4j
public final class BCryptPasswordEncoderMain {

    public static String encode(final String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.encode(password);
    }

    public static Boolean matches(final String rawPassword,
                                  final String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Encode a single password if supplied as args[0]
     * Otherwise, encode the standard passwords:
     * <pre>"user1", "admin1", "user2", "admin", "test"</pre>
     *
     * @param args single password to encode
     */
    public static void main(final String[] args) {
        String[] passwords = {"user1", "admin1", "user2", "admin", "test"};

        if (args != null && args.length == 1) {
            log.info(encode(args[0]));
        }
        else {
            log.info("\n\nEncoding passwords: {}", Arrays.toString(passwords));
            for(String raw: passwords){
                String encoded = encode(raw);
                log.info("\n[{}] matches [{}]: {}", raw, encode(raw), matches(raw, encoded));
                log.info("\n{}: [{bcrypt}{}]", raw, encoded);
            }
        }
    }

} // The End...
