package com.uat.util;

import org.testng.annotations.Test;

import com.uat.base.TestBase;

public class test extends TestBase{

	@Test
	public void test1() throws Exception
	{
		initialize();
		openBrowser();
		login("Admin");
		getObject("testingSplit");
		closeBrowser();
	}
}
