package com.uat.suite.tm_project;

import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase{

	// check if the suite ex has to be skiped
		@BeforeSuite
		public void checkSuiteSkip() throws Exception{
			initialize();
			APP_LOGS.debug("Checking Runmode of TM_Project Suite");
			if(!TestUtil.isSuiteRunnable(suiteXls, "TM_Project Suite")){
				APP_LOGS.debug("Skipped Test Management Suite  as the runmode was set to NO");
				throw new SkipException("RUnmode of Test Management Suite set to NO ... So Skipping all tests in Test Management Suite");
			}
			
		}
}
