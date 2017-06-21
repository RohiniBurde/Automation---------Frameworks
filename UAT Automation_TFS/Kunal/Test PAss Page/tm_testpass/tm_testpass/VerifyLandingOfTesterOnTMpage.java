package com.uat.suite.tm_testpass;

import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;
public class VerifyLandingOfTesterOnTMpage extends TestSuiteBase
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
	public void checkTestSkip ()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}

	@Test(dataProvider = "getData")
	public void verifyUserLandingOfTester(String role)throws Exception
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
		
			try
			{
				 APP_LOGS.debug(this.getClass().getSimpleName()+ " checking the Heading of Page");
			     
				 WebElement TM_Tab_Disabled = getElement("UAT_testManagement_Id");
			  
			     String isDisabled = TM_Tab_Disabled.getAttribute("disabled");
			  
			    if (isDisabled==null || !isDisabled.equals("true"))
			    { 
			    	
			      fail=true;
			   
			      APP_LOGS.debug("Test Management Tab link: Enabled");
			   
			      comments = "Fail occurred: Test Management Tab link: Enabled... ";
			      
			      TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Management Tab link: Enabled");
			    }
			    else
			    { 
			      APP_LOGS.debug("Test Management Tab link: Disabled");
			      
			      APP_LOGS.debug("For User " + role + ": Test Management Tab is showing Disabled Successfully ");
			      
			      comments = "Test Management Tab is showing Disabled Successfully... ";
			    }
			  
			}
			catch(Throwable t)
			{ 
				fail=true;
				
				comments = comments+"Exception occurred: While verifying landing of tester on Test Management page... ";
			}
		
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");
			isLoginSuccess  = true;
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
