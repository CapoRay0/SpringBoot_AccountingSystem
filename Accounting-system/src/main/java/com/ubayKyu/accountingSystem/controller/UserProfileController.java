package com.ubayKyu.accountingSystem.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.LoginService;
import com.ubayKyu.accountingSystem.service.UserInfoService;

@Controller
public class UserProfileController {

	@Autowired
	HttpSession session;
	
	@Autowired
	UserInfoService UserInfoService;
	
	/*---------------------------UserProfile.html----------------------------*/
	
	// UserProfile.html Controller Get
	@GetMapping("/UserProfile")
	public String UserProfilePage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
		Optional<UserInfo> userInfoForEdit = UserInfoService.findByUserID(userID);
		model.addAttribute("account", userInfoForEdit.get().getAccount());
		model.addAttribute("name", userInfoForEdit.get().getName());
		model.addAttribute("email", userInfoForEdit.get().getEmail());
		
		return "UserProfile";
	}
	
	// UserProfile.html Controller Post
	@PostMapping("/UserProfile")
	public String UserProfilePageUpdate(Model model,
				@RequestParam(value="txtName", required = false) String txtName, 
				@RequestParam(value="txtEmail", required = false) String txtEmail, 
				RedirectAttributes redirectAttrs) {
			
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//前、後台同時進行輸入檢查
		String message = "";
		if(txtName.isEmpty() || txtName == null)
			message += "姓名不可為空\r\n";
		
		if(txtEmail.isEmpty() || txtEmail == null)
			message += "Email不可為空\r\n";
		
		if(!message.isEmpty())
		{
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/UserProfile";
		}
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();

		UserInfoService.UpdateUserProfile(userID, txtName, txtEmail);
		redirectAttrs.addFlashAttribute("message", "個人資訊修改成功");
		
		return "redirect:/UserProfile";
	}
}
