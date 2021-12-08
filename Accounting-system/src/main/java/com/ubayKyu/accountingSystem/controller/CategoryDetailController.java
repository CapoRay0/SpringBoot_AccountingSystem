package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class CategoryDetailController {
	
	@Autowired
	HttpSession session;
	
	@GetMapping("/CategoryDetail")
	public String categoryDetailPage(Model model) {
		model.addAttribute("caption", "你好啊");
		model.addAttribute("note", "這裡是備註");
		
        if(LoginService.CheckLoginSession(session))
        	return "CategoryDetail";
        else
        	return "redirect:/Login";
	}
}
