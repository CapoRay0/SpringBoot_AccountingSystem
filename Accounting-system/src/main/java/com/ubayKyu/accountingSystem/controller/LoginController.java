package com.ubayKyu.accountingSystem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class LoginController
{
	@Autowired
	HttpSession session;
	
	@Autowired  //與service層進行互動
	private LoginService loginService;
	
	// Login.html Controller
	@GetMapping("/Login")
	public String loginPage(Model model)
	{
		if(LoginService.CheckLoginSession(session))
	        return "redirect:/UserProfile";
		else
			return "Login";
	}
	
	//將輸入值帶入，呼叫service層的方法與資料庫比對
	@RequestMapping("userLogin")
	public String getLogin(@RequestParam("account") String account, @RequestParam("pwd") String pwd, Model model, RedirectAttributes redirectAttrs)
	{
		 boolean boolLogin=loginService.TryLogin(account, pwd);
		 
		 if(boolLogin == true)
		 {
			 return "redirect:/UserProfile"; //重新導向到指定的網址
		 }
		 else
		 {
			 redirectAttrs.addFlashAttribute("message","登入失敗，請檢查帳號及密碼是否正確");
			 return "redirect:/Login"; //重新導向到指定的網址
		 }
	 }
}
