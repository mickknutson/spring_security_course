package io.baselogic.springsecurity.crypto.bcrypt;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BCryptPasswordEncoderMain {

    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.encode(password);
    }

    /**
     * Encode a single password if supplied as args[0]
     * Otherwise, encode the standard passwords:
     * <pre>"user1", "admin1", "user2", "admin", "test"</pre>
     *
     * @param args single password to encode
     */
    public static void main(String[] args) {
        String[] passwords = {"user1", "admin1", "user2", "admin", "test"};

        if (args.length == 1) {
            log.info(encode(args[0]));
        }
        else {
            log.info("\n\nEncoding passwords: {}", Arrays.toString(passwords));
            for(String psswd: passwords){
                log.info("\n{}: [{bcrypt}{}]", psswd, encode(psswd));
            }
        }
    }

} // The End...
