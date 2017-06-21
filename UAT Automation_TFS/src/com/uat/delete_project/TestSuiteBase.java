package com.uat.delete_project;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase{

	// check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of Delete Projects Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Delete Projects Suite")){
			APP_LOGS.debug("Skipped Delete Projects Suite  as the runmode was set to NO");
			throw new SkipException("RUnmode of Delete Projects Suite set to NO ... So Skipping all tests in Delete Projects Suite");
		}
		
	}		
		

}

