package com.ubayKyu.accountingSystem.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class WriteTextService {
	
//	@Value("${username}")
//    String username;

	public void writeToText(String messageString) throws IOException {
		
		//File path = new File(ResourceUtils.getURL("classpath:").getPath());
		//File file = new File(path.getAbsolutePath(),"static/Log.log"); //C:\Users\Admin\git\SpringBoot_AccountingSystem\Accounting-system\target\classes\static\Log.log
		
		String path = System.getProperty("user.dir");//當前專案路徑
        path += "/Log.log";
		
//		String path = "C:\\Logs\\Log.log";
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();

		// write
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(messageString + "\r\n");
		bw.flush();
		bw.close();
		fw.close();

//	    // read
//	    FileReader fr = new FileReader(file);
//	    BufferedReader br = new BufferedReader(fr);
//	    String str = br.readLine();

	}
}
