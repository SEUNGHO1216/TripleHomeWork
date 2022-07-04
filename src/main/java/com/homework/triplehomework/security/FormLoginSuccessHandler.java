package com.homework.triplehomework.security;

import com.homework.triplehomework.security.jwt.JwtTokenUtils;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        try{
            response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out=response.getWriter();/*.write("로그인이 완료되었습니다.");*/ /*현재 한글 메시지 깨짐 오류*/

            JSONObject json = new JSONObject();
            json.put("msg","로그인이 완료되었습니다");

            out.print(json);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
