package com.npht.springtodo.controller.client;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import com.npht.springtodo.dto.UserLogin;
import com.npht.springtodo.repository.ProjectRepository;
import com.npht.springtodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "")
public class HomeController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectRepository projectRepo;

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
        // model.addAttribute("loggedInUser",
        // userRepo.findByEmail(principal.getName()));
        return "view/client/profile";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLogin(Model model, Principal principal) {
        // model.addAttribute("users", userRepo.findAll());
        if (principal != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new UserLogin());
        return "view/client/login";
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "dashboard")
    public String getDashboard(Model model, Principal principal) {
        model.addAttribute("projects", projectRepo.findByUser_Email(principal.getName()));
        model.addAttribute("cate", "dashboard");
        return "view/client/dashboard";
    }

    // TODO: add remember token, encrypt password
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