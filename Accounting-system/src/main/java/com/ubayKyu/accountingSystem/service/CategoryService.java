package com.ubayKyu.accountingSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.dto.CategoryInterface;
import com.ubayKyu.accountingSystem.entity.Category;
import com.ubayKyu.accountingSystem.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired 
	private CategoryRepository repository;
	
	/*---------------------------CategoryList.html---------------------------*/
	
	//找出全部CategoryRepository
	public List<Category> getCategorys() {
		return repository.findAll();
	}
	//將查出的List於html使用th:each印出分類列表
	public List<CategoryInterface> getCategoryInterfaceListByUserID(String userID) {
		return repository.GetCategoryInterfaceListByUserID(userID);
	}
	//分類資訊旗下流水帳數若為0才可刪除分類
	public Integer getCountByCategoryIDForDel(String categoryID) {
		return repository.GetCountByCategoryIDForDel(categoryID);
	}
	//內建刪除語法
	public void deleteById(String categoryID) {
		repository.deleteById(categoryID);
	}
	
	/*--------------------------CategoryDetail.html--------------------------*/
	
	//內建查詢語法
	public Optional<Category> findByCategoryID(String categoryID) {
		return repository.findById(categoryID);
	}
	//檢查標題是否存在，編輯模式下可以維持不變，但禁止與其他Caption相同
	public boolean IsCategoryCaptionExist(String userid, String txtCaption, String categoryID) {
		Integer captionFormDB = repository.IsCategoryCaptionExistFindByCaptionAndUserID(userid, txtCaption);
		if(captionFormDB > 0 && categoryID != null) { // 編輯模式時
			Optional<Category> categoryForEdit = repository.findById(categoryID);
			if (categoryForEdit.get().getCaption().equals(txtCaption)) 
				return false; // 標題沒變，且沒有與其他標題重複
			else
				return true; // 重複了，跳警告
		}
		else if(captionFormDB == 0) // 新增模式時
			return false; // 沒有重複
		else
			return true; // 重複了，跳警告
	}
	//新增分類
	public String AddCategory(String userID, String txtCaption, String txtBody) {
		Category newCategory = new Category();
		String guid = UUID.randomUUID().toString();
		newCategory.setCategoryID(guid); // categoryid
		newCategory.setCaption(txtCaption); // caption
		newCategory.setBody(txtBody); // body
		newCategory.setCreateDate(LocalDateTime.now()); // createdate
		newCategory.setUserID(userID); // userid
		repository.save(newCategory); // 內建儲存語法(新增、更新都適用)
		
		return newCategory.getCategoryID(); // 將創建的Guid回傳
	}
	//編輯分類
	public void UpdateCategory(String categoryID, String txtCaption, String txtBody) {
		Optional<Category> categoryForEdit = repository.findById(categoryID);
		categoryForEdit.get().setCaption(txtCaption); // 更新Caption
		categoryForEdit.get().setBody(txtBody); // 更新Body
		repository.save(categoryForEdit.get()); // 內建儲存語法(新增、更新都適用)
	}
	
	/*------------------------AccountingDetail.html--------------------------*/
	
	//顯示出AccountingDetail中的分類下拉選單
	public List<Category> getCategoryByUserID(String userID) {
		return repository.GetCategoryByUserID(userID);
	}
}
