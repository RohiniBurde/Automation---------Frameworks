/* Author Name:-Aishwarya Deshpande
 * Created Date:-3rd Dec 2014
 * Last Modified on Date:-9th Feb 2015
 * Module Description:- Verification of Landing on Reports Page with the positive data and verifying if Reports tab is highlighted.  
 */

package com.uat.suite.reports;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyLandingOnReportsPage extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	Utility utilRecorder = new Utility();

	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing VerifyLandingOnReportsPage Test Case");

		if(!TestUtil.isTestCaseRunnable(ReportsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ReportsSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyLandingOnReportsPage(String Role) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		comments="";			

		APP_LOGS.debug("Opening Browser... ");
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			if(getElement("UAT_reports_Id").isDisplayed())
			{	
				APP_LOGS.debug("Reports tab is displayed for user: "+Role);
				comments="Reports tab is displayed for user: "+Role+"(Pass). ";

				// clicking on Reports Tab
				getElement("UAT_reports_Id").click();
				APP_LOGS.debug("Reports tab clicked ");
				Thread.sleep(2000);
				
				String reportsTab_Highlighted=getElement("UAT_reports_Id").getAttribute("class");
				Thread.sleep(2000);
				
				if(compareStrings(OR.getProperty("UAT_testManagementTab_Class"),reportsTab_Highlighted))
				{
					APP_LOGS.debug("Reports tab is Highlighted for " +Role+"(Pass).");
					comments+="Reports tab is Highlighted for User "+Role+"(Pass).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Reports tab is not Highlighted for " +Role+"(Fail).");
					comments+="Reports tab is not Highlighted for User "+Role+"(Fail).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ReportsTabNotHighlightedFor"+Role);
				}
				
				String actualReportsPageHeadingText = getElement("Reports_pageHeading").getText();
				
				if(compareStrings(resourceFileConversion.getProperty("ReportsPageHeadingText"),actualReportsPageHeadingText))
				{
					APP_LOGS.debug("User " + Role+ " is landing on 'Reports' Page successfully(Pass).");
					comments+=" User " + Role+ " is landing on 'Reports' Page successfully(Pass).";
				} 
				else
				{
					fail=true;
					APP_LOGS.debug("User " + Role+ " is not landing on 'Reports' Page(Fail).");
					comments+=" User " + Role+ " is not landing on 'Reports' Page(Fail).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ReportsPageHeadingNotMatchingFor"+Role);
				}				
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Reports page not dispalyed for user:  " +Role);
				comments="Reports page not dispalyed for user:  " +Role+"(Fail).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ReportsPageNotDisplayedFor"+Role);
			}
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");
		}	
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ReportsSuiteXls, this.getClass().getSimpleName()) ;
	}
}
