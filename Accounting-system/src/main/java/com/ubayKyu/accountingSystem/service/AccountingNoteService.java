package com.ubayKyu.accountingSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.entity.AccountingNote;
import com.ubayKyu.accountingSystem.repository.AccountingNoteRepository;

@Service
public class AccountingNoteService {

	@Autowired
	private AccountingNoteRepository repository;
	
	//找出全部AccountingNoteRepository
	public List<AccountingNote> getAccountingNotes(){
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
}
