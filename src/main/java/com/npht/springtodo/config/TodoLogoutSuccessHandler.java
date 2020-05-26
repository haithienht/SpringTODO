package com.npht.springtodo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

// @Component("todoLogoutSuccessHandler")
public class TodoLogoutSuccessHandler implements LogoutSuccessHandler {
    private RedirectStrategy redirect = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.removeAttribute("loggedInUser");
        }
        redirect.sendRedirect(request, response, "/");
    }

}