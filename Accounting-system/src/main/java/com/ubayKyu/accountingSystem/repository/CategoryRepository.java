package com.ubayKyu.accountingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.dto.CategoryInterface;
import com.ubayKyu.accountingSystem.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
	
	/*---------------------------CategoryList.html---------------------------*/
	
	// 取得登入者的分類資訊(Interface)
	@Query(value = "SELECT C.[categoryid]"
				+ " 	,C.[body]"
				+ "    	,C.[caption]"
				+ "    	,FORMAT(C.[create_date], 'yyyy/MM/dd') AS [create_date]"
				+ "    	,C.[userid]"
				+ "    	,COUNT(A.[categoryid]) [count]"
				+ "	FROM [category] AS C"
				+ "	LEFT JOIN [accounting_note] AS A ON A.[categoryid] = C.[categoryid]"
				+ " WHERE C.[userid] =:userid"
				+ " GROUP BY C.[categoryid], C.[body], C.[caption], C.[create_date], C.[userid]"
				+ " ORDER BY [count] DESC, [create_date] DESC"
				, nativeQuery = true)
	 List<CategoryInterface> GetCategoryInterfaceListByUserID(@Param("userid") String userid);
	
	// 取得該分類資訊旗下的流水帳數量(count) >> 若為0才可刪除分類
	@Query(value = "SELECT COUNT(A.categoryid) [count]"
				+ " FROM category AS C"
				+ " LEFT JOIN [accounting_note] AS A ON A.[categoryid] = C.[categoryid]"
				+ " WHERE C.[categoryid] =:categoryid"
				+ " GROUP BY C.[categoryid]"
				, nativeQuery = true)
	Integer GetCountByCategoryIDForDel(@Param("categoryid") String categoryid);
	
	// 檢查是否有重複的標題
	@Query(value = "SELECT COUNT(*) [count]"
				+ " FROM [category]"
				+ " WHERE [userid] =:userid AND [caption] =:caption"
				, nativeQuery = true)
	int IsCategoryCaptionExistFindByCaptionAndUserID(@Param("userid") String userid, @Param("caption") String caption);
	
	/*------------------------AccountingDetail.html--------------------------*/
	
	// 顯示出AccountingDetail中的分類下拉選單
	@Query(value = "SELECT *"
				+ " FROM [category]"
				+ " WHERE [userid] =:userid"
				, nativeQuery = true)
	List<Category> GetCategoryByUserID(@Param("userid") String userid);
}
