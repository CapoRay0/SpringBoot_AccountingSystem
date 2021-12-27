package com.ubayKyu.accountingSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubayKyu.accountingSystem.dto.UserInfoInterface;
import com.ubayKyu.accountingSystem.entity.UserInfo;
import com.ubayKyu.accountingSystem.repository.UserInfoRepository;

@Service
public class UserInfoService {
	
	@Autowired
	private UserInfoRepository repository;
	
	/*------------------------------Default.html-----------------------------*/
	
	//找出全部UserInfoRepository >> Default.html的會員數將以 UserInfo.size(); 來查詢
	public List<UserInfo> getUserInfos() {
		return repository.findAll();
	}
	
	/*------------------------------Login.html-------------------------------*/
	
	//從Repository取得登入驗證
	public UserInfo getUserInfo(String acc, String pwd) {
		return repository.GetUserInfoForLogin(acc, pwd);
	}
	
	/*---------------------------UserProfile.html----------------------------*/
	
	//帶出個人資訊 >> 也用於UserDetail來更新Session
	public Optional<UserInfo> findByUserID(String userID) {
		return repository.findById(userID);
	}
	//編輯個人資訊
	public void UpdateUserProfile(String userID, String txtName, String txtEmail) {
		Optional<UserInfo> userInfoForEdit = repository.findById(userID);
		userInfoForEdit.get().setName(txtName); // 更新Name
		userInfoForEdit.get().setEmail(txtEmail); // 更新Email
		repository.save(userInfoForEdit.get()); // 內建儲存語法
	}
	
	/*----------------------------UserList.html------------------------------*/
	
	//將查出的List於html使用th:each印出會員列表
	public List<UserInfoInterface> getUserInfoInterface() {
		return repository.GetUserInfoInterface();
	}
	//若SQL不回傳則須加上 @Transactional
	//刪除會員、流水帳及分類資訊
	public Integer deleteUserInfoAccountingNoteAndCategoryByUserID(String userID) {
		return repository.DeleteUserInfoAccountingNoteAndCategoryByUserID(userID);
	}
	
	/*---------------------------UserDetail.html-----------------------------*/
	
	//取得個人資訊以進行編輯
	public Optional<UserInfoInterface> getUserInfoInterfaceByUserID(String userID) {
        return repository.GetUserInfoInterfaceByUserID(userID);
    }
	//檢查帳號有無重複
	public boolean IsAccountExist(String account) {
        if (repository.GetUserAccountByAccount(account) == 0)
            return false;
        else
            return true;
    }
	//新增、編輯使用者
	public void SaveUserInfo(UserInfo user, String userID, String txtAccount, String txtName, String txtEmail, Integer ddlUserLevel) {
        user.setUserID(userID);
        user.setAccount(txtAccount);
        user.setEmail(txtEmail);
        user.setName(txtName);
        user.setPWD("12345678");
        user.setUserLevel(ddlUserLevel);
        repository.save(user);
    }
}
