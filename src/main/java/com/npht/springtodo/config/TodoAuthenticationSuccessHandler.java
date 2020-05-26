package com.npht.springtodo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.npht.springtodo.model.User;
import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// @Component("todoAuthenticationSuccessHandler")
public class TodoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserRepository userRepo;

    private RedirectStrategy redirect = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        User user = userRepo.findByEmail(auth.getName());
        if (session != null) {
            session.setAttribute("loggedInUser", user);
        }

        if (!response.isCommitted()) {
            String redirectUrl = "";
            if (user.getIsAdmin()) {
                redirectUrl = "/admin";
            } else {
                redirectUrl = "/dashboard";
            }
            redirect.sendRedirect(request, response, redirectUrl);
        }
    }

}