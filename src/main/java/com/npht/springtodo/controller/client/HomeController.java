package com.npht.springtodo.controller.client;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.npht.springtodo.dto.UserLogin;
import com.npht.springtodo.model.User;
import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
// import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "")
public class HomeController {

    @Autowired
    private UserRepository userRepo;

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
        return errors.getAllErrors();
    }

}