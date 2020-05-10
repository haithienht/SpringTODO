package com.npht.springtodo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.npht.springtodo.repository.UserRepository;

@Controller
@RequestMapping(value = "admin/user")
public class UserAdminController {
	@Autowired
	private UserRepository userRepo;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "view/admin/user";
	}
}
