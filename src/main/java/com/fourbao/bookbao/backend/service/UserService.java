package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.dto.request.UserEmailUpdateRequest;
import com.fourbao.bookbao.backend.dto.response.UserMyPageResponse;
import com.fourbao.bookbao.backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    User getUser(HttpServletRequest request);
    UserMyPageResponse getMyPage(HttpServletRequest request);
    void updateEmail(HttpServletRequest request, UserEmailUpdateRequest emailUpdateRequest);
}
