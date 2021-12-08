package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class AccountingListController {

	@Autowired
	HttpSession session;
	
	@GetMapping("/AccountingList")
	public String accountingListPage(Model model) {
		model.addAttribute("count", "486");
		
		if(LoginService.CheckLoginSession(session))
			return "AccountingList";
        else
        	return "redirect:/Login";
	}
}
