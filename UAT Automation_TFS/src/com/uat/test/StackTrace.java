package com.uat.test;

import org.testng.annotations.Test;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class StackTrace extends TestBase{
	
	
	
	@Test
	public void getStuck() throws Exception
	{
		initialize();
		openBrowser();
		login("Admin");
		getObject("pagal hai kya");
		closeBrowser();
		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Your desired File Name");
		
	}

}
