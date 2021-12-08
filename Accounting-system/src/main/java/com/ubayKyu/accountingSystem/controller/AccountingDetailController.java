package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class AccountingDetailController {

	@Autowired
	HttpSession session;
	
	@GetMapping("/AccountingDetail")
	public String accountingDetailPage(Model model) {
		model.addAttribute("money", "1486");
		model.addAttribute("caption", "哈囉");
		model.addAttribute("note", "我是Ray");
		
		if(LoginService.CheckLoginSession(session))
			return "AccountingDetail";
        else
        	return "redirect:/Login";
	}
}
