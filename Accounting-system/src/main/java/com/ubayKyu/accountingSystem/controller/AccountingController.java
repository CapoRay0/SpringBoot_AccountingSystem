package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.AccountingNoteService;
import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class AccountingController {

	@Autowired
	HttpSession session;
	
	@Autowired
	private AccountingNoteService AccountingNoteService;
	
	/*--------------------------AccountingList.html--------------------------*/
	
	// AccountingList.html Controller Get
	@GetMapping("/AccountingList")
	public String accountingListPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		model.addAttribute("count", "486");
		
		return "AccountingList";
	}
	
	// AccountingList.html Controller Post >> Delete
	@PostMapping("/AccountingList")
	public String accountingListDel(Model model) {
		
		if (!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//DELETE
		
		return "redirect:/AccountingList";
	}
	
	/*-------------------------AccountingDetail.html-------------------------*/
	
	// AccountingDetail.html Controller
	@GetMapping("/AccountingDetail")
	public String accountingDetailPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		model.addAttribute("money", "1486");
		model.addAttribute("caption", "哈囉");
		model.addAttribute("note", "我是Ray");
		
		return "AccountingDetail";
	}
}
