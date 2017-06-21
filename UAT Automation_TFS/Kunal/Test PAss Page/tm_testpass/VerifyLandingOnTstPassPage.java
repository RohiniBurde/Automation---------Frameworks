package com.uat.suite.tm_testpass;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class VerifyLandingOnTstPassPage extends TestSuiteBase
{
	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=false;
	String comments;
	
	String runmodes[]=null;

	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider = "getData")
	public void verifyUserLanding(String role)throws Exception
	{
		// test the runmode of current dataset
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			throw new SkipException("Runmode for test set data set to no "+count);
		}
		APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
		openBrowser();
		
		isLoginSuccess = login(role);
		
		if (isLoginSuccess) 
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
		
			getElement("UAT_testManagement_Id").click();
			
			Thread.sleep(3000);
			
			getElement("TM_testPassesTab_Id").click();

			String SubHead_testPass_Highlighted  = getObject("TestPasses_testPassTab").getAttribute("class");

		
			//Assert.assertEquals(SubHead_testPass_Highlighted,OR.getProperty("TestPasses_testPassTab_Class"));
			if(!compareStrings(SubHead_testPass_Highlighted, OR.getProperty("TestPasses_testPassTab_Class")))
			{
				APP_LOGS.debug("Test Pass tab is not highlighted/displayed");
				
				fail=true;
				
				comments = "Exception occurred: Test Pass tab is not highlighted/displayed... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass tab is not highlighted");
				closeBrowser();
				
				throw new SkipException("Test Pass tabwas expected to be highlighted/displayed but was not");
			
			}
			
			APP_LOGS.debug("Test Pass TAB is Highlighted ");
		
			APP_LOGS.debug("User " + role+ " is landing on' Test Pass' Page successfully ");
			
			comments = "Test Pass tab is highlighted/displayed... ";

			APP_LOGS.debug("Closing Browser... ");
			
			closeBrowser();

		}else {
			APP_LOGS.debug("Login Not Successful");
		}
}
	
	@DataProvider
	public Object[][] getData()
	{
		return TestUtil.getData(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		skip=false;
		fail=false;
		
	}
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
}
