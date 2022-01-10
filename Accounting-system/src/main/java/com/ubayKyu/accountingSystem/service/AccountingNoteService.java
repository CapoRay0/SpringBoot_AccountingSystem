package com.ubayKyu.accountingSystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.dto.AccountingNoteInterface;
import com.ubayKyu.accountingSystem.entity.AccountingNote;
import com.ubayKyu.accountingSystem.repository.AccountingNoteRepository;

@Service
public class AccountingNoteService {

	@Autowired
	private AccountingNoteRepository repository;
	
	/*-----------------------------Default.html------------------------------*/
	
	//找出全部AccountingNoteRepository
	public List<AccountingNote> getAccountingNotes() {
		return repository.findAll();
	}
	//取得初次記帳
	public LocalDateTime getFirstDate() {
		return repository.GetFirstDate();
	}
	//取得最後記帳
	public LocalDateTime getLastDate() {
		return repository.GetLastDate();
	}
	//取得記帳數量
	public Integer getAccountCount() {
		return repository.GetAccountCount();
	}
	
	/*--------------------------AccountingList.html--------------------------*/
	
	//將查出的List於html使用th:each印出流水帳列表
	public List<AccountingNoteInterface> getAccountingNoteInterfaceListByUserID(String userid) {
		return repository.GetAccountingNoteInterfaceListByUserID(userid);
	}
	//內建刪除語法
	public void deleteById(Integer accid) {
		repository.deleteById(accid);
	}
	//分別取得收入及支出的加總(全部)
	public int getAccountingNoteAmountSum(String userid, Integer actType) {
		Integer sum = repository.GetAccountingNoteAmountSum(userid, actType);
		if(sum == null)
			sum = 0;
		return sum;
	}
	//分別取得收入及支出的加總(本月)
	public int getAccountingNoteAmountSumThisMonth(String userid, Integer actType) {
		Integer sum = repository.GetAccountingNoteAmountSumThisMonth(userid, actType);
		if(sum == null)
			sum = 0;
		return sum;
	}
	
	/*-------------------------AccountingDetail.html-------------------------*/
	
	//內建查詢語法
	public Optional<AccountingNote> findById(Integer accID) {
		return repository.findById(accID);
	}
	//新增、編輯流水帳，並回傳AccID
	public Integer saveAccountingNote(AccountingNote acc) {
		repository.save(acc);
		if(acc.getAccID() == null) { //新增模式
			List<AccountingNote> accList = repository.GetAccountingNoteByUserID(acc.getUserID());
			return accList.get(accList.size()).getAccID(); //回傳當前登入者的最後一筆AccID >> 剛新增出來的
		}
		return acc.getAccID(); //回傳自己的AccID
	}
}
