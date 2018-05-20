package com.betalpha.fosun.controller;

import com.betalpha.fosun.api.ErrorMessageApi;
import com.betalpha.fosun.api.LoginInfoApi;
import com.betalpha.fosun.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/api/session", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserLoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity getOrganizationStructure(@RequestBody LoginInfoApi loginInfoApi,
                                                   ServletRequest servletRequest,
                                                   ServletResponse servletResponse) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userName = loginInfoApi.getName();
        Map<String, String> userMap = userService.getUserMap();//name-id
        String userId = userMap.get(userName);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(loginInfoApi.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageApi("用户名或密码错误"));
        }
        Cookie[] cookies = request.getCookies();
        String cookieUserId = "";
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if ("cookie_user".equals(cookie.getName())) {
                    cookieUserId = cookie.getValue();
                }
            }
        }
        if ("".equals(cookieUserId)) {
            log.info("save cookie");
            request.getSession().setAttribute("userId", userId);
            Cookie cookieUser = new Cookie("cookie_user", userId);

            cookieUser.setMaxAge(24 * 60 * 60);
            cookieUser.setPath("/");
            response.addCookie(cookieUser);
        }
        HttpHeaders headers=new HttpHeaders();
        headers.add("cookie_user",userId);
        return ResponseEntity.ok().headers(headers).build();
    }
}
