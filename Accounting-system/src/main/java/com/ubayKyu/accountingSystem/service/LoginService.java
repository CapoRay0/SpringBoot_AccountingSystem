package com.ubayKyu.accountingSystem.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.entity.UserInfo;

@Service
public class LoginService {
	@Autowired
	HttpSession session;
	
	@Autowired
	private UserInfoService UserInfoService; //透過Service查找UserInfo資料庫的方法集
	
	//登入驗證
	public boolean TryLogin(String inpAccount, String inpPWD) {
		boolean boolLogin = false;
		
		UserInfo user = UserInfoService.getUserInfo(inpAccount, inpPWD);//進資料庫比對帳號密碼是否正確 (存在)
		if (user != null) { //如果帳號與密碼資料庫都有查到
			boolLogin = true; //登入成功
			session.setAttribute("UserLoginInfo", user); //將 UserID 放進登入Session
			session.setAttribute("AdminToUser", false);
		}
		
		return boolLogin;
	}
	//清除登入Session
	public static void RemoveLoginSession(HttpSession session) {
		session.removeAttribute("UserLoginInfo");
		session.removeAttribute("AdminToUser");
	}
	//檢查登入Session是否為空值
	public static boolean CheckLoginSession(HttpSession session) {
		Object isExist = session.getAttribute("UserLoginInfo");
		if(isExist != null)
			return true;
		else
			return false;
	}
}