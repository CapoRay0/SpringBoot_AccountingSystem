package com.ubayKyu.accountingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,String>{
	
	//UserInfo findByAccountAndPwd(String account, String pwd);
	
	List<UserInfo> findAll();
	
	@Query(value = "SELECT [userid]"
			+ "      ,[account]"
			+ "      ,[create_date]"
			+ "      ,[email]"
			+ "      ,[name]"
			+ "      ,[pwd]"
			+ "      ,[user_level]"
			+ "  FROM [AccountingNote].[dbo].[user_info]"
			+"   WHERE [account]=:account AND [pwd]=:pwd"
			, nativeQuery = true)
	UserInfo GetUserForLogin(@Param("account") String account, @Param("pwd") String pwd);
	
	
//	@Query(value = "SELECT [userid],"
//			+ "[name]"
//			+ " FROM [user_info] WHERE [name]=:name", nativeQuery = true)
//	UserInfo zxc(@Param("name") String name);
	
}
