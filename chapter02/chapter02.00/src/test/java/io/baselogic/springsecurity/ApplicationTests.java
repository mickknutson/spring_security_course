package io.baselogic.springsecurity;

// Junit 5: -----------------------------------------

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// Assert-J
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest//(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@Test
	public void contextLoads() {

        Application.main(new String[0]);
	}

} // The End...
