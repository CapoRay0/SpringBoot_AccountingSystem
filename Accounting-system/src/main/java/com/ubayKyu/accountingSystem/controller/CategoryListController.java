package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class CategoryListController {
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/CategoryList")
	public String categoryListPage(Model model) {
		
        if(LoginService.CheckLoginSession(session))
        	return "CategoryList";
        else
        	return "redirect:/Login";
	}
	
}
