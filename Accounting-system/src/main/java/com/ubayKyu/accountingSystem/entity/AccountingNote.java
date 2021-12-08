package com.ubayKyu.accountingSystem.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AccountingNote")
public class AccountingNote {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="AccID", nullable=false, columnDefinition="int")
    private Integer AccID;
    
    @Column(name="UserID", nullable=false, columnDefinition="uniqueidentifier")
    private String UserID;
    
    @Column(name="Caption", nullable=true, columnDefinition="nvarchar(100)")
    private String Caption;
    
    @Column(name="Amount", nullable=false, columnDefinition="int")
    private Integer Amount;
    
    @Column(name="ActType", nullable=false, columnDefinition="int")
    private Integer ActType;
    
    @Column(name="CreateDate", nullable=false, columnDefinition="datetime default getdate()")
    private LocalDateTime CreateDate;
    
    @Column(name="Body", nullable=true, columnDefinition="nvarchar(500)")
    private String Body;
    
    @Column(name="CategoryID", nullable=true, columnDefinition="uniqueidentifier")
    private String CategoryID;

    
	public Integer getAccID() {
		return AccID;
	}

	public void setAccID(Integer accID) {
		AccID = accID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getCaption() {
		return Caption;
	}

	public void setCaption(String caption) {
		Caption = caption;
	}

	public Integer getAmount() {
		return Amount;
	}

	public void setAmount(Integer amount) {
		Amount = amount;
	}

	public Integer getActType() {
		return ActType;
	}

	public void setActType(Integer actType) {
		ActType = actType;
	}

	public LocalDateTime getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		CreateDate = createDate;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public String getCategoryID() {
		return CategoryID;
	}

	public void setCategoryID(String categoryID) {
		CategoryID = categoryID;
	}

	@Override
	public String toString() {
		return "AccountingNote [AccID=" + AccID + ", UserID=" + UserID + ", Caption=" + Caption + ", Amount=" + Amount
				+ ", ActType=" + ActType + ", CreateDate=" + CreateDate + ", Body=" + Body + ", CategoryID="
				+ CategoryID + "]";
	}
    
}