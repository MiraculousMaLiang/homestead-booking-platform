package com.homestead.booking.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homestead.booking.common.Result;
import com.homestead.booking.common.ResultCode;
import com.homestead.booking.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 *
 * @author homestead
 * @since 2025-11-18
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${homestead.jwt.header}")
    private String header;

    @Value("${homestead.jwt.prefix}")
    private String prefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 获取token
        String token = request.getHeader(header);
        if (token == null || token.isEmpty()) {
            writeErrorResponse(response, ResultCode.USER_NOT_LOGIN);
            return false;
        }

        // 去除前缀
        if (token.startsWith(prefix)) {
            token = token.substring(prefix.length()).trim();
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            writeErrorResponse(response, ResultCode.USER_LOGIN_EXPIRED);
            return false;
        }

        // 将用户ID存入request
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }

    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response, ResultCode resultCode) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Result<?> result = Result.error(resultCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);

        response.getWriter().write(json);
    }

}
