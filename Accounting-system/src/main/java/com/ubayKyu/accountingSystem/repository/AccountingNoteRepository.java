package com.ubayKyu.accountingSystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.dto.AccountingNoteInterface;
import com.ubayKyu.accountingSystem.entity.AccountingNote;

@Repository
public interface AccountingNoteRepository extends JpaRepository<AccountingNote, Integer>{
	
	/*------------------------------Default.html-----------------------------*/
	
	//初次記帳
	@Query(value = "SELECT Min([create_date])"
				+ " FROM [accounting_note]"
			, nativeQuery = true)
	LocalDateTime GetFirstDate();
	
	//最後記帳
	@Query(value = "SELECT Max([create_date])"
				+ " FROM [accounting_note]"
				, nativeQuery = true)
	LocalDateTime GetLastDate();
	
	//記帳數量
	@Query(value = "SELECT Count(*)"
				+ " FROM [accounting_note]"
			, nativeQuery = true)
	Integer GetAccountCount();
	
//	//(效率不佳)依時間排列查詢
//	@Query(value = "SELECT [accid]"
//				+ "		,[act_type]"
//				+ "     ,[amount]"
//				+ "     ,[body]"
//				+ "     ,[caption]"
//				+ "     ,[categoryid]"
//				+ "     ,[create_date]"
//				+ "     ,[userid]"
//				+ " FROM [accounting_note]"
//				+ " ORDER BY [create_date] ASC"
//				, nativeQuery = true)
//	List<AccountingNote> GetAccOrderbyCreateDate();
	
	/*--------------------------AccountingList.html--------------------------*/
	
	//取得登入者的流水帳資訊(Interface)
	@Query(value = "SELECT A.[accid]"
				+ "		,A.[act_type]"
				+ "    	,A.[amount]"
				+ "    	,A.[body]"
				+ "    	,A.[caption]"
				+ "    	,A.[categoryid]"
				+ "    	,FORMAT(A.[create_date], 'yyyy/MM/dd') AS [create_date]"
				+ "    	,A.[userid]"
				+ "    	,C.[caption] AS [category_caption]"
				+ "	FROM [accounting_note] AS A"
				+ "	LEFT JOIN [category] AS C ON C.[categoryid] = A.[categoryid]"
				+ " WHERE A.[userid] =:userid"
				+ " GROUP BY A.[accid], A.[act_type], A.[amount], A.[body], A.[caption], A.[categoryid], A.[create_date], A.[userid], C.[caption]"
				+ " ORDER BY [create_date] DESC, [amount] DESC"
				, nativeQuery = true)
	List<AccountingNoteInterface> GetAccountingNoteInterfaceListByUserID(@Param("userid") String userid);
	
	//分別取得收入及支出的加總(全部)
	@Query(value = "SELECT SUM(amount) AS [amount]"
				+ " FROM [accounting_note]"
				+ " WHERE [userid]=:userid AND [act_type]=:actType"
				, nativeQuery = true)
	Integer GetAccountingNoteAmountSum(@Param("userid") String userid, @Param("actType") Integer actType);
	
	//分別取得收入及支出的加總(本月)
	@Query(value = "DECLARE @firstdate DATETIME"
				+ " SET @firstdate = dateadd(m, datediff(m, 0, getdate()), 0)"
				+ " DECLARE @lastdate DATETIME"
				+ " SET @lastdate = dateadd(day, -1, dateadd(m, datediff(m, 0, getdate()) + 1, 0))"
				//取得本月的第一天及最後一天，再將條件設定為兩者之間
				+ " SELECT SUM(amount) AS [amount]"
				+ " FROM [accounting_note]"
				+ " WHERE [userid]=:userid AND [act_type]=:actType AND [create_date] BETWEEN @firstdate AND @lastdate"
				,nativeQuery = true)
	Integer GetAccountingNoteAmountSumThisMonth(@Param("userid") String userid, @Param("actType") Integer actType);
	
	/*------------------------AccountingDetail.html--------------------------*/
	
	//取得剛新增的accID，回傳後顯示回Url
	@Query(value = "SELECT *"
				+ " FROM [accounting_note]"
				+ " WHERE [userid]=:userid"
				, nativeQuery = true)
	List<AccountingNote> GetAccountingNoteByUserID(@Param("userid") String userid);
}
