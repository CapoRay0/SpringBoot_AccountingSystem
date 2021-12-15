//package com.ubayKyu.accountingSystem.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//
//import com.ubayKyu.accountingSystem.dto.User;
//import com.ubayKyu.accountingSystem.entity.UserInfo;
//import com.ubayKyu.accountingSystem.service.UserInfoService;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//@Controller
//public class UserInfoController {
//
//	@Autowired
//	private UserInfoService service;
//
//	@PostMapping("/addUserInfo")
//	public UserInfo addClient(@RequestBody UserInfo userInfo) {
//		return service.saveUserInfo(userInfo);
//	}
//
//	@GetMapping("/userInfos")
//	public List<UserInfo> findAllClient() {
//		return service.getUserInfos();
//	}
//
//	@GetMapping("/hello")
//	public String hello(Model model) {
//		model.addAttribute("message", "Hello Thymeleaf!!");
//		return "hello";
//	}
//	
//	@PostMapping("/login")
//	public void login(@ModelAttribute User user) {
//		System.out.println(user.name);
//	}
//}