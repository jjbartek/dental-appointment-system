package com.das.repositories;

import com.das.entities.Role;
import com.das.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @Test
    void userByEmailCanBeFound() {
        //given
        String email = "ted.baker@gmail.com";
        User user = new User("Ted Baker", email, "password", List.of(Role.RECEPTIONIST));
        underTest.save(user);

        //when
        Boolean userExists = underTest.existsByEmail(email);

        //should
        assertThat(userExists).isTrue();
    }

    @Test
    void userByEmailCanNotBeFound() {
        //given
        String email = "ted.baker@gmail.com";

        //when
        Boolean userExists = underTest.existsByEmail(email);

        //should
        assertThat(userExists).isFalse();
    }

    @Test
    void userByEmailShouldBeReturned() {
        //given
        String email = "ted.baker@gmail.com";
        User user = new User("Ted Baker", email, "password", List.of(Role.RECEPTIONIST));
        user = underTest.save(user);

        //when
        Optional<User> userFound = underTest.findByEmail(email);

        //should
        assertThat(userFound.isPresent()).isTrue();
        assertThat(userFound.get()).isEqualTo(user);
    }

    @Test
    void userByEmailShouldNotBeReturned() {
        //given
        String email = "ted.baker@gmail.com";

        //when
        Optional<User> userFound = underTest.findByEmail(email);

        //should
        assertThat(userFound.isPresent()).isFalse();
    }

    @Test
    void usersByEmailOrNameShouldBeReturned() {
        //given
        User userTed = new User("Ted Baker", "ted.baker@gmail.com", "password", List.of(Role.RECEPTIONIST));
        User userRobert = new User("Robert Baker", "robert.baker@gmail.com", "password", List.of(Role.RECEPTIONIST));
        userTed = underTest.save(userTed);
        userRobert = underTest.save(userRobert);

        //when
        Page<User> pageOfUsersByName = underTest.findByEmailOrName("baKeR", null);
        Page<User> pageOfUsersByEmail = underTest.findByEmailOrName("tEd", null);

        //should
        assertThat(pageOfUsersByName.getTotalElements()).isEqualTo(2);
        assertThat(pageOfUsersByEmail.getTotalElements()).isEqualTo(1);
        assertThat(pageOfUsersByName.toList()).isEqualTo(List.of(userTed, userRobert));
        assertThat(pageOfUsersByEmail.toList()).isEqualTo(List.of(userTed));
    }
}