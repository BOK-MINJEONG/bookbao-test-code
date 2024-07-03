package com.fourbao.bookbao.backend.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserTest {
    @Test
    @DisplayName("user 생성 테스트")
    public void createUserTest() {
        /* given */
        User user = User.builder()
                .name("testName")
                .schoolId("testNum")
                .email("testEmail")
                .build();

        /* when, then */
        Assertions.assertThat(user.getName()).isEqualTo("testName");
        Assertions.assertThat(user.getSchoolId()).isEqualTo("testNum");
        Assertions.assertThat(user.getEmail()).isEqualTo("testEmail");
    }

    // todo
    @Test
    @DisplayName("email update test")
    public void updateEmail() {
        /* given */
        User user = User.builder()
                .name("bok")
                .schoolId("1234")
                .email("test@gmail.com")
                .build();

        /* when */
        user.setEmail("update@gmail.com");

        /* then */
        Assertions.assertThat(user.getEmail()).isEqualTo("update@gmail.com");
    }
}