package io.baselogic.springsecurity.crypto.password;

import io.baselogic.springsecurity.authentication.encoding.Sha256PasswordEncoderMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.Arrays;

/**
 * A utility class that can be used to convert passwords to use Sha256 with salt using Spring Security's crypto module.
 *
 * @author mickknutson
 * @see Sha256PasswordEncoderMain
 *
 * @since chapter04.01
 */
@Slf4j
public class CryptoSha256PasswordEncoderMain {

    public static String encode(String password) {
        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }

    /**
     * Encode a single password if supplied as args[0]
     * Otherwise, encode the standard passwords:
     * <pre>"user1", "admin1", "user2"</pre>
     *
     * @param args single password to encode
     */
    public static void main(String[] args) {
        String[] passwords = {"user1", "admin1", "user2"};

        if (args.length == 1) {
            log.info(encode(args[0]));
        }
        else {
            log.info("Encoding passwords: {}", Arrays.toString(passwords));
            for(String psswd: passwords){
                log.info("[{}]", encode(psswd));
            }
        }
    }

} // The End...
