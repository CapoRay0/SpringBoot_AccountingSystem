package com.ubayKyu.accountingSystem.controller;

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

import com.ubayKyu.accountingSystem.dto.CategoryInterface;
import com.ubayKyu.accountingSystem.entity.Category;
import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.service.LoginService;
import com.ubayKyu.accountingSystem.service.CategoryService;

@Controller
public class CategoryController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	private CategoryService CategoryService;
	
	/*---------------------------CategoryList.html---------------------------*/
	
	//CategoryList.html Controller Get
	@GetMapping("/CategoryList")
	public String categoryListPage(Model model) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		//取得登入者的UserID
		UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
		//於html使用th:each將Category的List加入table中印出分類列表
		List<CategoryInterface> categoryList = CategoryService.getCategoryInterfaceListByUserID(userID);
		model.addAttribute("categoryListTable", categoryList);
		
		return "CategoryList";
	}
	
	//CategoryList.html Controller Post >> Delete
	@PostMapping("/CategoryList")
    public String CategoryListDel(Model model, 
    							  @RequestParam(value ="ckbDelete", required = false) String[] categoryIDsForDel,
    							  RedirectAttributes redirectAttrs) {
		
		if(!LoginService.CheckLoginSession(session))
			return "redirect:/Login";
		
		if(categoryIDsForDel != null) { //如果CheckBox有被勾選
			String isDelete = "";
			for(String eachCategory : categoryIDsForDel) { //Java的foreach寫法，跑categoryDel陣列中的每個eachCategory
				int count = CategoryService.getCountByCategoryIDForDel(eachCategory);
				if(count == 0) { //該分類中無流水帳才可刪除
					CategoryService.deleteById(eachCategory); //內建刪除方法
					isDelete = "Delete";
				}
			}
			if(isDelete.isEmpty())
				redirectAttrs.addFlashAttribute("message","選取項目尚有流水帳，無法刪除");
			else
				redirectAttrs.addFlashAttribute("message","刪除成功");
		}else
			redirectAttrs.addFlashAttribute("message","未選取任何項目");
		
		return "redirect:/CategoryList";
    }
	
	/*--------------------------CategoryDetail.html--------------------------*/
	
	//CategoryDetail.html Controller Get
	@GetMapping("/CategoryDetail")
	public String categoryDetailPage(Model model, 
									 @RequestParam(value="categoryID", required = false) String categoryID) {
		
		if(!LoginService.CheckLoginSession(session))
	    	return "redirect:/Login";
		
		//編輯模式 >> 帶出標題與備註內容
		if(categoryID != null) {
			Optional<Category> categoryForEdit = CategoryService.findByCategoryID(categoryID);
			model.addAttribute("caption", categoryForEdit.get().getCaption());
			if(categoryForEdit.get().getBody() != null) 
				model.addAttribute("body", categoryForEdit.get().getBody());
		}
		
	    return "CategoryDetail";
	}

	//CategoryDetail.html Controller Post >> CreateOrUpdate
	@PostMapping("/CategoryDetail")
	public String categoryDetailCreateOrUpdate(Model model, 
								   //HttpServletRequest request, 
									 @RequestParam(value="categoryID", required = false) String categoryID, 
									 @RequestParam(value="txtCaption", required = false) String txtCaption, 
									 @RequestParam(value="txtBody", required = false) String txtBody,
									 RedirectAttributes redirectAttrs) {
		
        if(!LoginService.CheckLoginSession(session))
        	return "redirect:/Login";
        
//		直接從網址上抓下來的方法 >> HttpServletRequest
//		String categoryID = request.getParameter("categoryID");
		
        //取得登入者的UserID
        UserInfo user = (UserInfo)session.getAttribute("UserLoginInfo");
		String userID = user.getUserID();
		
        //前、後台同時進行輸入檢查
        String message = "";
        if(txtCaption.isEmpty() || txtCaption == null)
        	message += "標題不可為空\r\n";
        
        if(CategoryService.IsCategoryCaptionExist(userID, txtCaption, categoryID)) //檢查標題有無重複
        	message += "此分類標題已經存在，請更換標題內容\r\n";
        
        if(!message.isEmpty()) {
        	redirectAttrs.addFlashAttribute("message", message);
        	if(categoryID == null)
        		return "redirect:/CategoryDetail"; //新增
            else
              	return "redirect:/CategoryDetail?categoryID=" + categoryID;  //編輯
        }
        
        //若無重複才進行新增或編輯
        if (categoryID == null) //新增模式
		{
			categoryID = CategoryService.AddCategory(userID, txtCaption, txtBody);
			message = "新增成功";
		}
		else //編輯模式
		{
			CategoryService.UpdateCategory(categoryID, txtCaption, txtBody);
			message = "編輯成功";
		}
        
        redirectAttrs.addFlashAttribute("message", message);
        return "redirect:/CategoryDetail?categoryID=" + categoryID;
	}
}
