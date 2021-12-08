package com.ubayKyu.accountingSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.repository.UserInfoRepository;

@Service
public class UserInfoService {
	@Autowired
	private UserInfoRepository repository;
	
	public List<UserInfo> getUserInfos(){
		return repository.findAll();
	}
	
	//test
	public UserInfo saveUserInfo(UserInfo UserInfo) {
		return repository.save(UserInfo);
	}
	
	public List<UserInfo> getUserInfoById(List<String> ids){
		return repository.findAllById(ids);
	}
	
	public String deleteUserInfo(String id) {
		repository.deleteById(id);
		return "Deleted!";
	}
}
