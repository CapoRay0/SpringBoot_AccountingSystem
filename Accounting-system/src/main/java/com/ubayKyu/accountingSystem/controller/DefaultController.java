package com.ubayKyu.accountingSystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.Const.UrlPath;
import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.FormatService;
import com.ubayKyu.accountingSystem.service.LoginService;
import com.ubayKyu.accountingSystem.service.UserInfoService;
import com.ubayKyu.accountingSystem.service.AccountingNoteService;

@Controller
public class DefaultController {
	
	@Autowired
	HttpSession session; //Session依賴注入
	
	@Autowired
	private UserInfoService UserInfoService; //查找UserInfo資料庫的方法集
	
	@Autowired
	private AccountingNoteService AccountingNoteService; //查找AccountingNote資料庫的方法集
	
	// Default.html Controller Get
	@GetMapping(value = {"/","/Default"})
	public String defaultPage(Model model) {
		
		//List<AccountingNote> accForDate = AccountingNoteRepository.GetAccOrderbyCreateDate();
		//String firstDate = accForDate.get(0).getCreateDate().toString();
		//String lastDate = accForDate.get(accForDate.size()-1).getCreateDate().toString();
		
		String firstDate = "尚無紀錄"; //初次記帳
		String lastDate = "尚無紀錄"; //最後記帳
		
		if(AccountingNoteService.getFirstDate() != null) //如果create_date有資料
			firstDate = FormatService.FormatDateTime(AccountingNoteService.getFirstDate()); //丟 LocalDateTime 進去，回傳 Format 後的 String
		
		if(AccountingNoteService.getLastDate() != null) //如果create_date有資料
			lastDate = FormatService.FormatDateTime(AccountingNoteService.getLastDate()); //丟 LocalDateTime 進去，回傳 Format 後的 String
		
		Integer accountCount = AccountingNoteService.getAccountCount(); //記帳數量
		List<UserInfo> UserInfo = UserInfoService.getUserInfos(); //會員數
		
		model.addAttribute("firstAccRecordTime", firstDate);
		model.addAttribute("lastAccRecordTime", lastDate);
		model.addAttribute("recordCount", accountCount);
		model.addAttribute("memberCount", UserInfo.size());
		return "Default";
	}
	
	// (Layout.html) 登出 >> 清除 Session 並導頁回 Default
	@RequestMapping("/btnLogout")
	public String btnLogout(Model model, RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("message", "已成功登出，將您導回預設頁");
		LoginService.RemoveLoginSession(session);
		return "redirect:" + UrlPath.URL_DEFAULT;
	}
	
}
