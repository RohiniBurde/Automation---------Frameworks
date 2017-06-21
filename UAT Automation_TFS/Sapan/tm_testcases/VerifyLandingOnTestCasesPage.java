package com.uat.suite.tm_testcases;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyLandingOnTestCasesPage extends TestSuiteBase{
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip()
		{
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
		}
	
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void Test_VerifyLandingOnTestCasesPage(String Role) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		//APP_LOGS.debug(" Executing Test Case -> VerifyLandingOnTestCasesPage...");
			
		APP_LOGS.debug("Opening Browser... ");
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{	
			try
			{
		
				// clicking on Test Management Tab
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				Thread.sleep(2000);
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(2000);
				APP_LOGS.debug("Clicking On TEST CASES Tab ...");
				getElement("TM_testCasesTab_Id").click();
				
				Thread.sleep(800);
				String pageHeaderText  = getElementByClassAttr("TestCases_pageHeaderText_Class").getText();
				//eventfiringdriver.findElement(By.id("navTestCases")).click();
				Thread.sleep(800);
				
				if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_TestCaseDetailsText"),pageHeaderText))
				{
			
					APP_LOGS.debug("Test Cases page is open...");
						
					APP_LOGS.debug("User " + Role+ " is landing on  'Test Cases' Page successfully ");
						
				}
				else
				{
					fail=true;	
					//assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "LandingOnTestCasePageIssue");
					APP_LOGS.debug("Landing  (Fail)");
					
					
							
				}
				
		
				
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
			}
			catch (Throwable e) 
			{
					e.printStackTrace();
					fail = true;
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "exception");
					APP_LOGS.debug("exception Occured");
					
			}
			
		}
			
}

	
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
		
		}
		else
		
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=false;
		

	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
}
		