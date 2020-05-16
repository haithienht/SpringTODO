package com.npht.springtodo.controller.client;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.npht.springtodo.dto.UserLogin;
import com.npht.springtodo.model.User;
import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getHome(Model model) {
        // model.addAttribute("users", userRepo.findAll());
        return "view/client/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(Model model) {
        // model.addAttribute("users", userRepo.findAll());
        model.addAttribute("user", new UserLogin());
        return "view/client/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Object postLogin(@Valid @ModelAttribute("user") UserLogin user, Model model,
            BindingResult errors, HttpServletResponse response) {
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
        //TODO: Add authentication with Spring Security, JDBC "spring boot database authentication session"
        if (!errors.hasErrors()) {
            User u = userRepo.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (u == null) {
                errors.reject("common", "Invalid login information. Please check again your email and password.");
            } else {
                u.setPassword("");
                session.setAttribute("loggedInUser", u);
            }
        }
        return errors.getAllErrors();
    }

}