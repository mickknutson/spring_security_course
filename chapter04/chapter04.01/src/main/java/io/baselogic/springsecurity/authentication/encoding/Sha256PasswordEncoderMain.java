package io.baselogic.springsecurity.authentication.encoding;

import io.baselogic.springsecurity.crypto.password.CryptoSha256PasswordEncoderMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * A utility class that can be used to convert passwords to use Sha256 without any salt. This uses Spring Security's old
 * {@link PasswordEncoder} interface. Typically applications should prefer the new crypto module's PasswordEncoder as
 * demonstrated in {@link CryptoSha256PasswordEncoderMain}.
 *
 * @author mickknutson
 * @see CryptoSha256PasswordEncoderMain
 *
 * @since chapter04.01
 */
@Slf4j
public class Sha256PasswordEncoderMain {

    public static String encode(String password) {
//        ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
//        String encodedPassword = encoder.encodePassword(password, null);
//        return encodedPassword;
        return password;
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
