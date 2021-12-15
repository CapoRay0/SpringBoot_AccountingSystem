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
	public String categoryListPage(Model model, 
								   @RequestParam(value="id") String userID) {
		
		//於html使用th:each將Category的List加入table中印出分類列表
		List<CategoryInterface> categoryList = CategoryService.getCategoryInterfaceListByUserID(userID);
		model.addAttribute("categoryListTable",categoryList);
		
        if(LoginService.CheckLoginSession(session))
        	return "CategoryList";
        else
        	return "redirect:/Login";
	}
	
	//CategoryList.html Controller Post >> Delete
	@PostMapping("/CategoryList")
    public String CategoryListDel(Model model, 
    							  @RequestParam(value="id") String userID,
    							  @RequestParam(value ="ckbDelete", required = false) String[] categoryIDsForDel,
    							  RedirectAttributes redirectAttrs) {
		
		if(categoryIDsForDel != null) { //如果CheckBox有被勾選
			for(String eachCategory : categoryIDsForDel) { //Java的foreach寫法，跑categoryDel陣列中的每個eachCategory
				int count = CategoryService.getCountByCategoryIDForDel(eachCategory);
				if(count == 0) { //該分類中無流水帳才可刪除
					CategoryService.deleteById(eachCategory); //內建刪除方法
					redirectAttrs.addFlashAttribute("message","刪除成功");
				}
			}
		}else
			redirectAttrs.addFlashAttribute("message","未選取任何項目");
		
		if(LoginService.CheckLoginSession(session))
        	return "redirect:/CategoryList?id=" + userID;
        else
        	return "redirect:/Login";
    }
	
	/*--------------------------CategoryDetail.html--------------------------*/
	
	//CategoryDetail.html Controller Get
	@GetMapping("/CategoryDetail")
	public String categoryDetailPage(Model model, 
									 @RequestParam(value="id") String userID, 
									 @RequestParam(value="categoryID", required = false) String categoryID) {
		//帶出標題與備註內容
		if(categoryID != null) {
			Optional<Category> categoryForEdit = CategoryService.findById(categoryID);
			model.addAttribute("caption", categoryForEdit.get().getCaption());
			if(categoryForEdit.get().getBody() != null) 
				model.addAttribute("body", categoryForEdit.get().getBody());
		}
		
	    if(LoginService.CheckLoginSession(session))
	        return "CategoryDetail";
	    else
	        return "redirect:/Login";
	}

	//CategoryDetail.html Controller Post >> CreateOrUpdate
	@PostMapping("/CategoryDetail")
	public String categoryDetailPage(Model model, 
//									 HttpServletRequest request, 
									 @RequestParam(value="id") String userID, 
									 @RequestParam(value="categoryID", required = false) String categoryID, 
									 @RequestParam(value="txtCaption", required = false) String txtCaption, 
									 @RequestParam(value="txtBody", required = false) String txtBody,
									 RedirectAttributes redirectAttrs) {
		//直接從網址上抓下來的方法
//		String categoryID = request.getParameter("categoryID");
//		String userID = request.getParameter("id");
		
//		System.out.println(categoryID);
//		System.out.println(userID);
//		System.out.println(txtCaption);
//		System.out.println(txtBody);
		
		// 檢查是否登入
        if(!LoginService.CheckLoginSession(session))
        	return "redirect:/Login";
        
        // 檢查標題有無重複
        if(CategoryService.IsCategoryCaptionExist(userID, txtCaption, categoryID))
        {
             redirectAttrs.addFlashAttribute("message", "此分類標題已經存在，請更換標題內容");
             if(categoryID == null)
            	 return "redirect:/CategoryDetail?id=" + userID; // 新增
             else
            	 return "redirect:/CategoryDetail?id=" + userID +"&categoryID=" + categoryID;  // 編輯
        }
        
        // 若無重複才進行新增或編輯
        if (categoryID == null) // 新增模式
		{
			String newCategoryID = CategoryService.AddCategory(userID, txtCaption, txtBody);
			redirectAttrs.addFlashAttribute("message", "新增成功");
			return "redirect:/CategoryDetail?id=" + userID + "&categoryID=" + newCategoryID;
		}
		else // 編輯模式
		{
			CategoryService.UpdateCategory(categoryID, txtCaption, txtBody);
			redirectAttrs.addFlashAttribute("message", "編輯成功");
			return "redirect:/CategoryDetail?id=" + userID + "&categoryID=" + categoryID;
		}
        
	}
}
