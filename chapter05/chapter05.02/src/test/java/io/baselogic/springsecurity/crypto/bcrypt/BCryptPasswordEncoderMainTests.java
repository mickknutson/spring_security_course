package io.baselogic.springsecurity.crypto.bcrypt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
@Slf4j
public class BCryptPasswordEncoderMainTests {

    private static final String PASSWORD = "user1";
    private static final String PASSWORD_ENCODED = "$2a$04$gBdMIzQ5P2Ffb4L/epcKSOiYRlwPcUKx1jlfENvOUMpSAm4PsRdK2";


    @BeforeEach
    public void beforeEachTest() {
    }


    @Test
    @DisplayName("BCryptPasswordEncoderMain - encode")
    public void encode() {
        String result = BCryptPasswordEncoderMain.encode(PASSWORD);
        log.info("Encoded password: [{}]", result);
//        assertThat(result).isEqualTo(PASSWORD_ENCODED);
    }

    @Test
    @DisplayName("BCryptPasswordEncoderMain - matches")
    public void matches() {
        String result = BCryptPasswordEncoderMain.encode(PASSWORD);
        log.info("Encoded password: [{}]", result);
//        assertThat(result).isEqualTo(PASSWORD_ENCODED);
    }


    @Test
    @DisplayName("BCryptPasswordEncoderMain - main method")
    public void main() {
        String[] args = {"PASSWORD"};
        BCryptPasswordEncoderMain.main(args);
//        assertThat(result).isEqualTo(PASSWORD_ENCODED);
    }

    @Test
    @DisplayName("BCryptPasswordEncoderMain - main method - null input")
    public void main_null_args() {
        String[] args = null;
        BCryptPasswordEncoderMain.main(args);
//        assertThat(result).isEqualTo(PASSWORD_ENCODED);
    }

} // The End...
