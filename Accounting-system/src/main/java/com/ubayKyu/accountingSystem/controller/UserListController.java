package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class UserListController {
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/UserList")
	public String userListPage(Model model) {
		
		if(LoginService.CheckLoginSession(session))
			return "UserList";
        else
        	return "redirect:/Login";
	}
	
}
