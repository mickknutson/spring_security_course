package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.dao.TestUtils;
import io.baselogic.springsecurity.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * DefaultEventServiceTests
 *
 * @since chapter03.03
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserDetailsServiceTests {

    // Mockito:
    @MockBean
    private AppUserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl service;



    @BeforeEach
    public void beforeEachTest() {
    }

    //-----------------------------------------------------------------------//

    @Test
    @DisplayName("loadUserByUsername - null User")
    public void loadUserByUsername_null_user() {

        // Expectation
        given(userRepository.findByEmail(any(String.class)))
                .willReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(TestUtils.admin1.getEmail());
        });
    }


} // The End...
