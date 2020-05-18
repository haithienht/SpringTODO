package com.npht.springtodo.controller.client;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.npht.springtodo.dto.UserLogin;
import com.npht.springtodo.model.User;
import com.npht.springtodo.repository.UserRepository;
import com.npht.springtodo.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "")
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private HttpSession session;

    @Autowired
    AuthenticationManager authManager;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getHome(Model model) {
        // model.addAttribute("users", userRepo.findAll());
        return "view/client/index";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String getProfile(Model model, Principal principal) {
    
        model.addAttribute("loggedInUser", userRepo.findByEmail(principal.getName()));
        return "view/client/profile";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLogin(Model model) {
        // model.addAttribute("users", userRepo.findAll());
        model.addAttribute("user", new UserLogin());
        return "view/client/login";
    }

    // TODO: add remember token
    // authentication setting

    // @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    // public @ResponseBody Object postLogin(@Valid @ModelAttribute("user")
    // UserLogin user, Model model,
    // BindingResult errors, HttpServletResponse response) {
    // model.addAttribute("users", userRepo.findAll());
    // BindingResult newErrors = new BeanPropertyBindingResult(user, "user");
    // newErrors.addError(errors.getFieldError("email"));
    // newErrors.addError(errors.getFieldError("password"));
    // if (newErrors.hasErrors()) {
    // return newErrors.getAllErrors();
    // }
    // return errors.getAllErrors();

    // return "post login";
    // response.setStatus(HttpServletResponse.SC_OK);
    // if (!errors.hasErrors()) {
    // User u = userRepo.findByEmailAndPassword(user.getEmail(),
    // user.getPassword());
    // if (u == null) {
    // errors.reject("common", "Invalid login information. Please check again your
    // email and password.");
    // } else {
    // u.setPassword("");
    // UsernamePasswordAuthenticationToken authReq = new
    // UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    // Authentication auth = authManager.authenticate(authReq);
    // SecurityContext sc = SecurityContextHolder.getContext();
    // sc.setAuthentication(auth);
    // session.setAttribute("loggedInUser", u);
    // }
    // }
    // return errors.getAllErrors();
    // }

}