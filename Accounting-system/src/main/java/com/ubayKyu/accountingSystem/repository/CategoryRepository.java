package com.ubayKyu.accountingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubayKyu.accountingSystem.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	
}
