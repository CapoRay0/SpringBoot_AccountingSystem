package com.ubayKyu.accountingSystem.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatService
{
	//帶 LocalDateTime 進來，Format 後回傳 String
    public static String FormatDateTime (LocalDateTime dateTime)
    {
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	    String dtfString = dtf.format(dateTime);
	    return dtfString;
    }
}