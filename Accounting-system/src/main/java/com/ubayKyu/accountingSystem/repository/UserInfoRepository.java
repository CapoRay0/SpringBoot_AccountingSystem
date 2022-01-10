package com.ubayKyu.accountingSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.dto.UserInfoInterface;
import com.ubayKyu.accountingSystem.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String>{
	
	/*------------------------------Login.html-------------------------------*/
	
	// 進行登入驗證，同時查詢登入者的個人資訊
	@Query(value = "SELECT [userid]"
				+ "		,[account]"
				+ "    	,[create_date]"
				+ "    	,[email]"
				+ "    	,[name]"
				+ "    	,[pwd]"
				+ "    	,[user_level]"
				+ "    	,[edit_date]"
				+ "	FROM [user_info]"
				+ " WHERE [account]=:account AND [pwd]=:pwd"
				, nativeQuery = true)
	 UserInfo GetUserInfoForLogin(@Param("account") String account, @Param("pwd") String pwd);
	
	/*----------------------------UserList.html------------------------------*/
	
	//取得會員資訊(Interface)
	@Query(value = "SELECT [userid]"
				+ " 	,[account]"
				+ "    	,FORMAT([create_date], 'yyyy/MM/dd hh:mm') AS [create_date]"
				+ "    	,[email]"
				+ "    	,[name]"
				+ "    	,[user_level]"
				+ "	FROM [user_info]"
				+ "	ORDER BY [create_date] DESC"
				, nativeQuery = true)
	List<UserInfoInterface> GetUserInfoInterface();
	
	//若SQL不回傳則須加上 @Modifying
	//刪除會員、流水帳及分類資訊
	@Query(value = "DELETE FROM [user_info]"
				+ "	WHERE [user_info].[userid] =:userid"
				+ " DELETE FROM [accounting_note]"
				+ "	WHERE [accounting_note].[userid] =:userid"
				+ "	DELETE FROM [category]"
				+ "	WHERE [category].[userid] =:userid"
				+ "	SELECT COUNT(*) FROM [user_info]"
				, nativeQuery = true)
	Integer DeleteUserInfoAccountingNoteAndCategoryByUserID(@Param("userid") String userid);
	
	/*---------------------------UserDetail.html-----------------------------*/
	
	//取得個人資訊以進行編輯
	@Query(value = "SELECT [userid]"
				+ " 	,[account]"
				+ " 	,FORMAT([create_date], 'yyyy/MM/dd hh:mm:ss') AS [create_date]"
				+ " 	,[email]"
				+ " 	,[name]"
				+ " 	,[user_level]"
				+ " 	,FORMAT([edit_date], 'yyyy/MM/dd hh:mm:ss') AS [edit_date]"
				+ " FROM [user_info]"
				+ " WHERE [userid] =:userid"
				, nativeQuery = true)
	Optional<UserInfoInterface> GetUserInfoInterfaceByUserID(@Param("userid") String userid);
	
	//檢查帳號是否重複
	@Query(value = "SELECT COUNT(*)"
				+ " FROM [user_info]"
				+ " WHERE [account] =:account"
				, nativeQuery = true)
	int GetUserAccountByAccount(@Param("account") String account);
	
	//取得當前管理員人數
	@Query(value = "SELECT COUNT(*)"
				+ " FROM [user_info]"
				+ " WHERE [user_level] = 0"
				, nativeQuery = true)
	int GetAdminUserCount();
}
