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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        return passwordEncoder.encode(password);
    }

    public static Boolean matches(final String rawPassword,
                                  final String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Encode a single password if supplied as args[0]
     * Otherwise, encode the standard passwords:
     * <pre>"user1", "admin1", "user2", "admin", "test"</pre>
     *
     * @param args single password to encode
     */
    public static void main(final String[] args) {
        StringBuilder sb = new StringBuilder(1_000);
        sb.append("\n------------------------------------------------");
        sb.append("\nLets encrypt our standard passwords with our PasswordEncoder:");
        sb.append("\n------------------------------------------------");

        String[] passwords = {"user1", "admin1", "user2"};
        sb.append("\n\nEncoding passwords: ").append(Arrays.toString(passwords));
        sb.append("\n------------------------------------------------\n");

        for(String raw: passwords){
            String encoded = encode(raw);

            sb.append("\n[").append(raw).append("] ");
            sb.append("Matches [").append(encoded).append("] ");
            sb.append(": ").append(matches(raw, encoded));

            sb.append("\nValue for database: \n");
            sb.append("[").append(encoded).append("]");
            sb.append("\n------------------------------------------------\n");
        }

        sb.append("\n\n------------------------------------------------\n\n");

        log.info(sb.toString());
    }

} // The End...
