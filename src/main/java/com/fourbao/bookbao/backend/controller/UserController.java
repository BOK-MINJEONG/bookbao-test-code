package com.fourbao.bookbao.backend.controller;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponse;
import com.fourbao.bookbao.backend.dto.request.UserEmailUpdateRequest;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//User 마이페이지 기능을 제공하는 Controller

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/mypage")
    public BaseResponse<UserMyPageResponse> getMyPage(HttpServletRequest request) {
        try {
            UserMyPageResponse userMyPageResponse = userServiceImpl.getMyPage(request);
            return new BaseResponse<>(userMyPageResponse);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/update-email")
    public BaseResponse<String> updateEmail(HttpServletRequest request, @RequestBody UserEmailUpdateRequest emailUpdateRequest) {
        try {
            userServiceImpl.updateEmail(request, emailUpdateRequest);
            return new BaseResponse<>("이메일 수정을 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
