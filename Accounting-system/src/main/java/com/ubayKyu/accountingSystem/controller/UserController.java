package com.ubayKyu.accountingSystem.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubayKyu.accountingSystem.dto.UserInfoInterface;
import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.LoginService;
import com.ubayKyu.accountingSystem.service.UserInfoService;
import com.ubayKyu.accountingSystem.service.WriteTextService;

@Controller
public class UserController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserInfoService UserInfoService;
	
	@Autowired
	WriteTextService WriteTextService;
	
	/*-----------------------------UserList.html-----------------------------*/
	
	// UserList.html Controller Get
	@GetMapping("/UserList")
	public String userListPage(Model model,
				RedirectAttributes redirectAttrs) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		Integer userLevel = user.getUserLevel();
		if(userLevel > 0) {
			redirectAttrs.addFlashAttribute("message", "您的權限不足，無法訪問該頁面");
			return "redirect:/UserProfile";
		}
		
		//於html使用th:each將UserInfo的List加入table中印出會員列表
		List<UserInfoInterface> userInfoList = UserInfoService.getUserInfoInterface();
		model.addAttribute("userInfoListTable", userInfoList);
		
		return "UserList";	
	}
	
	// UserList.html Controller Post >> Delete
	@PostMapping("/UserList")
	public String userListDel(Model model,
				@RequestParam(value ="ckbDelete", required = false) String[] userIDsForDel,
				RedirectAttributes redirectAttrs) {

		if (!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String currentUserID = user.getUserID();
		String currentName = user.getName();
		
		Integer userInfoCount = 0;
		String selfIsDelete = "";
		
		if(userIDsForDel != null) {
			for(String eachUserID : userIDsForDel) {
				
				Optional<UserInfo> userInfoToDel = UserInfoService.findByUserID(eachUserID);
				String name = userInfoToDel.get().getName();
				
				//寫入 Log.log 中 >> Accounting-system\Log.log
				try {
					WriteTextService.writeToText("管理者 " + currentName + " 於 " + LocalDate.now() + " 刪除使用者 " + name);
				} catch (IOException e) {
					e.printStackTrace();
				}
				//顯示於 Console 中
				System.out.println("管理者 " + currentName + " 於 " + LocalDate.now() + " 刪除使用者 " + name);
				
				if(currentUserID.equals(eachUserID)) //若有刪除自己的狀況則先儲存UserID，迴圈結束後登出
					selfIsDelete = eachUserID;
				
				//進資料庫刪除
				userInfoCount =  UserInfoService.deleteUserInfoAccountingNoteAndCategoryByUserID(eachUserID);
			}
			
			if(currentUserID.equals(selfIsDelete)) { //將自己登出
				redirectAttrs.addFlashAttribute("message","已將此會員(選取之會員)及其流水帳、分類刪除，回到預設頁");
				LoginService.RemoveLoginSession(session);
				return "redirect:/Default";
			}
			redirectAttrs.addFlashAttribute("message","已將選取之會員及其流水帳、分類刪除，剩餘 " + userInfoCount + " 位會員");
		}else
			redirectAttrs.addFlashAttribute("message","未選取任何項目");
		
		return "redirect:/UserList";
	}
	
	/*----------------------------UserDetail.html----------------------------*/
	
	// UserDetail.html Controller Get
	@GetMapping("/UserDetail")
	public String userDetailPage(Model model,
				@RequestParam(value = "userID", required = false) String userID,
				RedirectAttributes redirectAttrs) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		Integer userLevel = user.getUserLevel();
		if(userLevel > 0) {
			boolean IsAdminToUser = false;
			IsAdminToUser = (boolean)session.getAttribute("AdminToUser");
			redirectAttrs.addFlashAttribute("message", "您的權限不足，無法訪問該頁面\r\n");
			if(IsAdminToUser) {
				redirectAttrs.addFlashAttribute("message", "已降級為一般會員\r\n");
				session.setAttribute("AdminToUser", false);
			}
			return "redirect:/UserProfile";
		}
		
		//編輯模式 >> 帶出個人資訊內容
		if( userID != null) {
			Optional<UserInfoInterface> userInfoForEdit = UserInfoService.getUserInfoInterfaceByUserID(userID);
			model.addAttribute("account", userInfoForEdit.get().getaccount());
			model.addAttribute("name", userInfoForEdit.get().getname());
			model.addAttribute("email", userInfoForEdit.get().getemail());
			model.addAttribute("userLevel", userInfoForEdit.get().getuser_level());
			model.addAttribute("createTime", userInfoForEdit.get().getcreate_date());
			model.addAttribute("editTime", userInfoForEdit.get().getedit_date());
		}
		
		return "UserDetail";	
	}
	
	// UserDetail.html Controller Post >> CreateOrUpdate
	@PostMapping("/UserDetail")
	public String userDetailCreateOrUpdate(Model model,
				@RequestParam(value = "userID", required = false) String userID,
				@RequestParam(value = "txtAccount", required = false) String txtAccount,
				@RequestParam(value = "txtName", required = false) String txtName,
				@RequestParam(value = "txtEmail", required = false) String txtEmail,
				@RequestParam(value = "ddlUserLevel", required = false) Integer ddlUserLevel,
				@RequestParam(value = "hiddenCreateDate", required = false) String hiddenCreateDate,
				RedirectAttributes redirectAttrs) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//前、後台同時進行輸入檢查
		String message = "";
		if(txtAccount.isEmpty() || txtAccount == null)
			message += "帳號不可為空\r\n";
		
		if(txtName.isEmpty() || txtName == null)
			message += "姓名不可為空\r\n";
		
		if(txtEmail.isEmpty() || txtEmail == null)
			message += "Email不可為空\r\n";
		
		//新增模式時
		if(userID == null && UserInfoService.IsAccountExist(txtAccount)) //檢查是否重複
			message += "此帳號已被使用\r\n";
		
		//編輯模式時
		if(userID != null && ddlUserLevel == 1 && UserInfoService.AdminUserLevelCheck(userID)) //權限降級時檢查管理員人數
			message += "管理員人數不能低於一人\r\n";
		
		//自己降級
		UserInfo userFromSession = (UserInfo)session.getAttribute("UserLoginInfo");
		String userIDFromSession = userFromSession.getUserID();
		if(userID != null && userID.equals(userIDFromSession) && ddlUserLevel == 1)
			session.setAttribute("AdminToUser", true);
		
		if(!message.isEmpty()) {
			redirectAttrs.addFlashAttribute("message", message);
			if(userID == null)
				return "redirect:/UserDetail";
			else
				return "redirect:/UserDetail?userID=" + userID;
		}
		
		//新增或編輯使用者
		UserInfo UserInfo = new UserInfo();
		if(userID == null) {
			userID = UUID.randomUUID().toString();
			UserInfo.setCreateDate(LocalDateTime.now());
			message = "新增成功";
		}
		else {
			//需先將從前端hidden抓到的CreateDate格式轉成yyyy/MM/dd HH:mm:ss
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			UserInfo.setCreateDate(LocalDateTime.parse(hiddenCreateDate, formatter));
			UserInfo.setEditDate(LocalDateTime.now());
			message = "編輯成功";
		}
		UserInfoService.SaveUserInfo(UserInfo, userID, txtAccount, txtName, txtEmail, ddlUserLevel);
		
		//取得登入者資訊
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		if(user.getUserID().equals(userID)) //編輯模式下更新Session
		{
			UserInfo NewUserInfoToSession = UserInfoService.findByUserID(userID).get();
			session.setAttribute("UserLoginInfo", NewUserInfoToSession);
		}
		
		redirectAttrs.addFlashAttribute("message", message);
		return "redirect:/UserDetail?userID=" + userID;
		
	}
}
