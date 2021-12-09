package com.ubayKyu.accountingSystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
	
	List<Category> findAll();
	
	/*---------------------------CategoryList.html---------------------------*/
	// 找出登入者的分類資訊
	@Query(value = "SELECT C.[categoryid]"
				+ "		,C.[body]"
				+ "    	,C.[caption]"
				+ "    	,C.[create_date]"
				+ "    	,C.[userid]"
				+ "    	,COUNT(A.[categoryid]) [Count]"
				+ "	FROM [category] AS C"
				+ "	LEFT JOIN [accounting_note] AS A ON A.[categoryid] = C.[categoryid]"
				+ " WHERE C.[userid]=:userid"
				+ " GROUP BY C.[categoryid], C.[body], C.[caption], C.[create_date], C.[userid]"
				, nativeQuery = true)
	List<Category> GetCategoryByUserID(@Param("userid") String userid);
	/*------------------------------------------------------------------*/
}
