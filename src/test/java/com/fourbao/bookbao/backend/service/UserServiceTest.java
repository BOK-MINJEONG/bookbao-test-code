package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)      // test에서 가짜 객체를 사용하므로
public class UserServiceTest {
    // Test 주체
    UserService userService;
    
    // Test 협력자
    // @MockBean으로 가짜 객체를 만듦
    @MockBean UserRepository userRepository;
    @MockBean BookRepository bookRepository;
    @MockBean JwtUtils jwtUtils;

    /**
     * Test를 실행하기 전마다 UserService에 가짜 객체를 주입
     * 테스트 메서드에 필요한 repository만 주입, 나머지는 null
     * ex) userService = new UserServiceImpl(userRepository, null, null);
     */
    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository, bookRepository, jwtUtils);
    }

//    @Test
//    @DisplayName("mypage test")
//    public void getMypageTest() {
//        /* given */
//        User user1 = User.builder()
//                .name("name1")
//                .schoolId("num1")
//                .email("email1")
//                .build();
//
//        ReflectionTestUtils.setField(user1, "id", 1);
//
//        Mockito.when(userRepository.save(user1)).thenReturn(user1);
//
//        /* when */
//
//        /* then */
//        Assertions.assertThat(user1.)
//    }
}