package com.ubayKyu.accountingSystem.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.dto.AccountingNoteInterface;
import com.ubayKyu.accountingSystem.entity.AccountingNote;
import com.ubayKyu.accountingSystem.entity.Category;
import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.AccountingNoteService;
import com.ubayKyu.accountingSystem.service.CategoryService;
import com.ubayKyu.accountingSystem.service.LoginService;

@Controller
public class AccountingController {

	@Autowired
	HttpSession session;
	
	@Autowired
	private AccountingNoteService AccountingNoteService;
	
	@Autowired
	private CategoryService CategoryService;
	
	/*--------------------------AccountingList.html--------------------------*/
	
	// AccountingList.html Controller Get
	@GetMapping("/AccountingList")
	public String accountingListPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者的UserID
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
		//於html使用th:each將AccountingNote的List加入table中印出流水帳列表
		List<AccountingNoteInterface> accountingNoteList = AccountingNoteService.getAccountingNoteInterfaceListByUserID(userID);
		model.addAttribute("accountingNoteListTable", accountingNoteList);
		
		//分別取得收入及支出的加總後進行相減取得總金額
		Integer amountSum = (AccountingNoteService.getAccountingNoteAmountSum(userID, 1)) - (AccountingNoteService.getAccountingNoteAmountSum(userID, 0));
		model.addAttribute("amountSum", amountSum); //總金額小計
		Integer amountSumThisMonth = (AccountingNoteService.getAccountingNoteAmountSumThisMonth(userID, 1)) -(AccountingNoteService.getAccountingNoteAmountSumThisMonth(userID, 0));
		model.addAttribute("amountSumThisMonth", amountSumThisMonth); //本月小計
		
		return "AccountingList";
	}
	
	// AccountingList.html Controller Post >> Delete
	@PostMapping("/AccountingList")
	public String accountingListDel(Model model,
				@RequestParam(value ="ckbDelete", required = false) Integer[] accIDsForDel,
				RedirectAttributes redirectAttrs) {
		
		if (!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		if(accIDsForDel != null) {
			for(Integer eachAccountingNote : accIDsForDel) {
				AccountingNoteService.deleteById(eachAccountingNote); //內建刪除方法
			}
			redirectAttrs.addFlashAttribute("message","刪除成功");
		}else
			redirectAttrs.addFlashAttribute("message","未選取任何項目");
		
		return "redirect:/AccountingList";
	}
	
	/*-------------------------AccountingDetail.html-------------------------*/
	
	// AccountingDetail.html Controller Get
	@GetMapping("/AccountingDetail")
	public String accountingDetailPage(Model model,
				@RequestParam(value="accID", required = false) Integer accID) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者的UserID
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
		//於html使用th:each將Category的名稱加入下拉選單中印出
		List<Category> categoryNameList = CategoryService.getCategoryByUserID(userID);
		model.addAttribute("CategoryNameList", categoryNameList); //分類名稱下拉選單
		
		//編輯模式 >> 帶出收支、金額、標題與備註內容
		if(accID != null) {
			Optional<AccountingNote> accountingNoteForEdit = AccountingNoteService.findById(accID);
			model.addAttribute("ddlActType", accountingNoteForEdit.get().getActType()); //透過jQuery傳至html
			model.addAttribute("ddlCategoryType", accountingNoteForEdit.get().getCategoryID()); //透過jQuery傳至html
			model.addAttribute("amount", accountingNoteForEdit.get().getAmount());
			model.addAttribute("caption", accountingNoteForEdit.get().getCaption());
			if(accountingNoteForEdit.get().getBody() != null)
				model.addAttribute("body", accountingNoteForEdit.get().getBody());
			model.addAttribute("hiddenDateTime", accountingNoteForEdit.get().getCreateDate()); //資料庫抓出日期放入前台hidden，讓Post時可以抓到
		}
		
		return "AccountingDetail";
	}
	
	//AaccountingDetail.html Controller Post >> CreateOrUpdate
	@PostMapping("/AccountingDetail")
	public String accountingDetailCreateOrUpdate(Model model,
				@RequestParam(value="accID", required = false) Integer accID, // Url
				@RequestParam(value="ddlActType", required = false) Integer ddltype,
				@RequestParam(value="ddlCategoryType", required = false) String ddlCategory,
				@RequestParam(value="txtAmount", required = false) String txtAmount,
				@RequestParam(value="txtCaption", required = false) String txtCaption,
				@RequestParam(value="txtBody", required = false) String txtBody,
				@RequestParam(value="hiddenDate", required = false) String hiddenDateTime,
				RedirectAttributes redirectAttrs
				) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//前、後台同時進行輸入檢查
		String message = "";
		if(txtAmount.isEmpty() || txtAmount == null) {
			message = "金額不可為空\r\n";
			txtAmount = "0";
		}
		
		Integer amount = Integer.parseInt(txtAmount);
		if(amount > 10000000 || amount < 0)
			message = "輸入金額不可超過一千萬\r\n";
		
		if(txtCaption.isEmpty() || txtCaption == null)
			message += "標題不可為空\r\n";
		
		if(!message.isEmpty()) {
			redirectAttrs.addFlashAttribute("message", message);
			if(accID == null)
				return "redirect:/AccountingDetail";
			else
				return "redirect:/AccountingDetail?accID=" + accID;
		}
		
		//取得登入者的UserID
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
		AccountingNote newOrUpdateAcc = new AccountingNote();
		if(!ddlCategory.isEmpty())
			newOrUpdateAcc.setCategoryID(ddlCategory);
		newOrUpdateAcc.setActType(ddltype);
		newOrUpdateAcc.setAmount(amount);
		newOrUpdateAcc.setCaption(txtCaption);
		newOrUpdateAcc.setBody(txtBody);
		newOrUpdateAcc.setUserID(userID);
		
		if(accID == null) {
			newOrUpdateAcc.setCreateDate(LocalDateTime.now());
			message = "新增成功";
		}else {
			newOrUpdateAcc.setAccID(accID); // 防止資料庫自動新增(Identity)
			newOrUpdateAcc.setCreateDate(LocalDateTime.parse(hiddenDateTime));
			message = "編輯成功";
		}
		
		String newAccID = AccountingNoteService.saveAccountingNote(newOrUpdateAcc).toString();
		redirectAttrs.addFlashAttribute("message", message);
		
		return "redirect:/AccountingDetail?accID=" + newAccID;
	}
}
