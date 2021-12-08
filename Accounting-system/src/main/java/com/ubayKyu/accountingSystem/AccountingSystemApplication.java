package com.ubayKyu.accountingSystem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ubayKyu.accountingSystem.entity.UserInfo;

@SpringBootApplication
//public class AccountingSystemApplication implements CommandLineRunner {
	public class AccountingSystemApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(AccountingSystemApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		String sql = "SELECT * FROM UserInfo";
//		List<UserInfo> userinfos = jdbcTemplate.query(sql,
//				BeanPropertyRowMapper.newInstance(UserInfo.class));
//		userinfos.forEach(System.out :: println);
//	}

}
