package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class UserController {
	
	@Autowired
	HttpSession session;
	
	// UserList.html Controller
	@GetMapping("/UserList")
	public String userListPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		
		
		return "UserList";	
	}
	
	// UserDetail.html Controller
	@GetMapping("/UserDetail")
	public String userDetailPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		model.addAttribute("account", "CapoRay");
		model.addAttribute("name", "吳叡");
		model.addAttribute("email", "caporay132@gmail.com");
		model.addAttribute("createTime", "2021/8/2 下午 09:24:16");
		model.addAttribute("updateTime", "2021/8/5 下午 11:52:15");
		
		return "UserDetail";	
	}
}
