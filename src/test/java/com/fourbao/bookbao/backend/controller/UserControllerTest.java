package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.dto.response.UserMyPageHistoriesResponse;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.UserRepository;
import com.fourbao.bookbao.backend.service.UserServiceImpl;
import com.fourbao.bookbao.backend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * @InjectMocks -> @Autowired로 생성자 주입
     */
    @Autowired
    private UserController userController;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("마이페이지 로직 확인 - 성공")
    public void getMyPage_Success() throws Exception {
        List<UserMyPageHistoriesResponse> testList = new ArrayList();
        /**
         * response body (성공)
         * {
         *     "isSuccess": true,
         *     "code": 200,
         *     "message": "요청에 성공하였습니다.",
         *     "result": {
         *         "name": "복민정",
         *         "id": "22011819",
         *         "histories": [
         *             {
         *                 "id": 1,
         *                 "name": "name1",
         *                 "author": "author1",
         *                 "publisher": "publisher1",
         *                 "price": 1000,
         *                 "thumbnail": "thumbnail1"
         *             }
         *         ]
         *     }
         * }
         */

        // given
        UserMyPageHistoriesResponse historyResponse = UserMyPageHistoriesResponse.builder()
                .id(1)
                .name("name1")
                .author("author1")
                .publisher("publisher1")
                .price(1000)
                .thumbnail("thumbnail1")
                .build();

        List<UserMyPageHistoriesResponse> histories = Collections.singletonList(historyResponse);

        UserMyPageResponse userMyPageResponse = UserMyPageResponse.builder()
                .name("복민정")
                .id("22011819")
                .histories(histories)
                .build();
        when(userServiceImpl.getMyPage(any(HttpServletRequest.class))).thenReturn(userMyPageResponse);

        // when & then
        mockMvc.perform(get("/api/v1/user/mypage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.name").value("복민정"))
                .andExpect(jsonPath("$.result.id").value("22011819"))
                .andExpect(jsonPath("$.result.histories").isArray())
                .andExpect(jsonPath("$.result.histories[0].id").value(1))
                .andExpect(jsonPath("$.result.histories[0].name").value("name1"))
                .andExpect(jsonPath("$.result.histories[0].author").value("author1"))
                .andExpect(jsonPath("$.result.histories[0].publisher").value("publisher1"))
                .andExpect(jsonPath("$.result.histories[0].price").value(1000))
                .andExpect(jsonPath("$.result.histories[0].thumbnail").value("thumbnail1"));
        verify(userServiceImpl, times(1)).getMyPage(any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("마이페이지 로직 확인 - 실패")
    public void getMyPage_Fail() throws Exception {

    }
}