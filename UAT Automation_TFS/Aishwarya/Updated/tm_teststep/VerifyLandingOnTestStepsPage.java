package com.uat.suite.tm_teststep;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.util.TestUtil;

public class VerifyLandingOnTestStepsPage extends TestSuiteBase {
	
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
		public void checkTestSkip() throws Exception{

			APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());			

			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		
		@Test(dataProvider="getTestData")
		public void Test_VerifyLandingOnTestStepsPage(String Role) throws Exception
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
				if(!Role.equals("Tester"))
				{
					// clicking on Test Management Tab
					getElement("UAT_testManagement_Id").click();
					APP_LOGS.debug(" Test Management tab clicked ");
					Thread.sleep(2000);
					
					//clicking on TestStep tab
					getElement("TM_testStepsTab_Id").click();
					APP_LOGS.debug(" TestStep tab clicked ");
					Thread.sleep(2000);
					
					String testStep_tab_highlighted=getElement("TM_testStepsTab_Id").getAttribute("class");
					Thread.sleep(2000);
					
					if(compareStrings(OR.getProperty("TM_testStepTab_class"),testStep_tab_highlighted))
					{
						APP_LOGS.debug("Test Step tab is Highlighted for " +Role);
						comments+="Test Step tab is Highlighted for " +Role +"(PASS) ";
					}
					else
					{
						fail=true;
						comments+="The Test Step tab is not highlighted for this user (FAIL) ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The Test Step tab is not highlighted for Role "+Role);
					}
					String actualtestStepPageHeadingText = getObject("TM_testStepTabHeadingDetails").getText();
					
					boolean headingSuccessResult=compareStrings(actualtestStepPageHeadingText,resourceFileConversion.getProperty("TM_TestStepTab_TestStepDetailsText"));
					if(headingSuccessResult==true)
					{
						APP_LOGS.debug("User " + Role+ " is landing on 'Test Step' Page successfully ");
						comments+=" User " + Role+ " is landing on 'Test Step' Page successfully (PASS)";
					} 
					else
					{
						fail=true;
						APP_LOGS.debug("User " + Role+ " is not landing on 'Test Step' Page (FAIL)");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User " + Role+ " is not landing on 'Test Step' Page");
						comments+="User " + Role+ " is not landing on 'Test Step' Page (FAIL)";					
					}
				}
				else 
				{
					String dashboardTabHighlighted = getElement("UAT_dashboard_Id").getAttribute("class");
					if(!(compareStrings(OR.getProperty("DashboardTab_Class"),dashboardTabHighlighted)) && (assertTrue(getElement("UAT_stakeholderDashboard_Id").getAttribute("disabled").contains("true"))))
					{
						fail=true;
						comments+="The test management was clickable for tester (FAIL) ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The test management was clickable for tester");
					} 
				}
				
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
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;
			

		}
		
		
		@AfterTest
		public void reportTestResult() throws Exception{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}
		
		
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
		}
}
