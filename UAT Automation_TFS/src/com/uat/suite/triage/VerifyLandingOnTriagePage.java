/* Author Name:-Aishwarya Deshpande
 * Created Date:-26th Nov 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Landing on Triage Page with the positive data  
 */

package com.uat.suite.triage;

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

public class VerifyLandingOnTriagePage extends TestSuiteBase{
	
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
		APP_LOGS.debug("Executing VerifyLandingOnTriagePage Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
			
	@Test(dataProvider="getTestData")
	public void Test_VerifyLandingOnTriagePage(String Role) throws Exception
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
			if(getElement("UAT_triage_Id").isDisplayed())
			{	
				APP_LOGS.debug("Triage tab is displayed for user: "+Role+". ");
				comments+=" Triage tab is displayed for user: "+Role+". ";

				// clicking on Triage Tab
				getElement("UAT_triage_Id").click();
				APP_LOGS.debug("Triage tab clicked ");
				Thread.sleep(2000);
						
				String triage_tab_highlighted=getElement("UAT_triage_Id").getAttribute("class");
				Thread.sleep(2000);
						
				if(compareStrings(triage_tab_highlighted,OR.getProperty("UAT_testManagementTab_Class"))){
					APP_LOGS.debug("Triage tab is Highlighted for " +Role);
					comments+="Traige tab is Highlighted for User "+Role+"(Pass).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Triage tab is not Highlighted for " +Role);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageTabHighlightFailure");
					comments+="Traige tab is not Highlighted for User "+Role+"(Fail).";
				}
						
				String actualtraigePageHeadingText = getObject("Traige_pageHeading").getText();
						
				if(compareStrings(actualtraigePageHeadingText,resourceFileConversion.getProperty("TraigePageHeadingText")))
				{
					APP_LOGS.debug("User " + Role+ " is landing on 'Triage' Page successfully ");
					comments+=" User " + Role+ " is landing on 'Triage' Page successfully(Pass).";
				} 
				else
				{
					fail=true;
					APP_LOGS.debug("User " + Role+ " is not landing on 'Triage' Page");
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageTabLandingFailure");
					comments+=" User " + Role+ " is not landing on 'Triage' Page(Fail).";					
				}				
			}
			else
			{
				if(Role.equals("Tester"))
				{
					APP_LOGS.debug("Triage tab not dispalyed for user:  " +Role);
					comments="Triage page tab dispalyed for user:  " +Role+" (Pass).";
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Triage tab not dispalyed for user:  " +Role);
					comments="Triage page tab dispalyed for user:  " +Role+"(Fail).";
				}
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
	public void reportDataSetResult(){
		if(skip)
		{
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else{
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
			
	@AfterTest
	public void reportTestResult() throws Exception{
		if(isTestPass)
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
			
	@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TriageSuiteXls, this.getClass().getSimpleName()) ;
	}
			
}
