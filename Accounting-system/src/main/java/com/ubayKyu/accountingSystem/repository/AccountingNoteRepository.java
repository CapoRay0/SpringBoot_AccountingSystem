package com.ubayKyu.accountingSystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.entity.AccountingNote;

@Repository
public interface AccountingNoteRepository extends JpaRepository<AccountingNote, Integer>{
	
	List<AccountingNote> findAll();
	
	/*---------------------------Default.html---------------------------*/
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
    
//    //(效率不佳)依時間排列查詢
//    @Query(value = "SELECT [accid]"
//    			+ "		,[act_type]"
//    			+ "     ,[amount]"
//    			+ "     ,[body]"
//            	+ "     ,[caption]"
//            	+ "     ,[categoryid]"
//            	+ "     ,[create_date]"
//            	+ "     ,[userid]"
//            	+ " FROM [accounting_note]"
//            	+ " ORDER BY [create_date] ASC"
//            	, nativeQuery = true)
//    List<AccountingNote> GetAccOrderbyCreateDate();
    /*------------------------------------------------------------------*/
    
}







