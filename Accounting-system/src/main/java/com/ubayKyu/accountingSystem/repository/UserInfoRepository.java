package com.ubayKyu.accountingSystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String>{
	
	List<UserInfo> findAll(); //Default.html的會員數將以 UserInfo.size(); 來查詢
	
	/*---------------------------Login.html-----------------------------*/
	// 進行登入驗證，同時查詢登入者的個人資訊
	@Query(value = "SELECT [userid]"
				+ "		,[account]"
				+ "    	,[create_date]"
				+ "    	,[email]"
				+ "    	,[name]"
				+ "    	,[pwd]"
				+ "    	,[user_level]"
				+ "	FROM [user_info]"
				+ " WHERE [account]=:account AND [pwd]=:pwd"
				, nativeQuery = true)
	UserInfo GetUserInfoForLogin(@Param("account") String account, @Param("pwd") String pwd);
	
	/*------------------------------------------------------------------*/
}
