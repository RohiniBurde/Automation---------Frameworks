package com.uat.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;


public class Concatenate extends TestBase
{
	
	@Test
	public void test() throws Exception
	{
		/*Calendar calendar;
		
		SimpleDateFormat Time;
		calendar = Calendar.getInstance();
		Time = new SimpleDateFormat("hh.mm.ss");
		
		System.out.println("Initialize begin at "+Time.format(calendar.getTime()));

		initialize();
		 printTime("Initialize end at");

		openBrowser();
		 printTime("Login begin at");

		login("Admin");
		printTime("Login function execution completed at");
		
		closeBrowser();*/
		
		//initialize();
		//openBrowser();
		System.out.println(OR.getProperty("UAT_testManagement_Id"));
		//TestUtil.takeScreenShot("test", "test");
		closeBrowser();
		
	}
	
}





