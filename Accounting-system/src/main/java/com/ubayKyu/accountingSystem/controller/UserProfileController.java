package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class UserProfileController {

	@Autowired
	HttpSession session;
	
	@GetMapping("/UserProfile")
	public String UserProfilePage(Model model) {
        model.addAttribute("account", "Ray Wu");
        model.addAttribute("caption", "名字");
        model.addAttribute("email", "caporay123@gmail.com");
        
        if(LoginService.CheckLoginSession(session))
        	return "UserProfile";
        else
        	return "redirect:/Login";
	}
}
