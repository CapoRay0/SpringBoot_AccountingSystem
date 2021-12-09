package com.ubayKyu.accountingSystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.repository.UserInfoRepository;
import com.ubayKyu.accountingSystem.service.FormatService;
import com.ubayKyu.accountingSystem.service.LoginService;
import com.ubayKyu.accountingSystem.repository.AccountingNoteRepository;

@Controller
public class DefaultController {
	
	@Autowired
	HttpSession session; //Session依賴注入
	
	@Autowired
	private UserInfoRepository UserInfoRepository; //查找UserInfo資料庫的方法集
	
	@Autowired
	private AccountingNoteRepository AccountingNoteRepository; //查找AccountingNote資料庫的方法集
	
	// Default.html Controller
	@GetMapping(value = {"/","/Default"})
	public String defaultPage(Model model) {
		
		//List<AccountingNote> accForDate = AccountingNoteRepository.GetAccOrderbyCreateDate();
		//String firstDate = accForDate.get(0).getCreateDate().toString();
		//String lastDate = accForDate.get(accForDate.size()-1).getCreateDate().toString();
		
		String firstDate = "尚無紀錄"; //初次記帳
		String lastDate = "尚無紀錄"; //最後記帳
		
		if(AccountingNoteRepository.GetFirstDate() != null) //如果create_date有資料
			firstDate = FormatService.FormatDateTime(AccountingNoteRepository.GetFirstDate()); //丟 LocalDateTime 進去，回傳 Format 後的 String
		
		if(AccountingNoteRepository.GetLastDate() != null) //如果create_date有資料
			lastDate = FormatService.FormatDateTime(AccountingNoteRepository.GetLastDate()); //丟 LocalDateTime 進去，回傳 Format 後的 String
		
        Integer accountCount = AccountingNoteRepository.GetAccountCount(); //記帳數量
		List<UserInfo> UserInfo = UserInfoRepository.findAll(); //會員數
		
		//UserInfo za = UserInfoRepository.zxc("叡ray");
		//String asd = za.getName();
		
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
		return "redirect:/Default";
	}
	
}
