package com.uat.suite.tm_testStep;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class VerifyLandingOnTestStepsPage extends TestSuiteBase {
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			APP_LOGS.debug(" Executing VerifyLandingOnTestStepsPage Test Case");
			System.out.println(" Executing VerifyLandingOnTestStepsPage Test Case");				

			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
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
						
			APP_LOGS.debug("Opening Browser... ");
			System.out.println("Opening Browser... ");
			openBrowser();

			isLoginSuccess = login(Role);
			
			if(isLoginSuccess)
			{	
				// clicking on Test Management Tab
				getElement("UAT_testManagement_Id").click();
				APP_LOGS.debug(" Test Management tab clicked ");
				System.out.println(" Test Management tab clicked ");
				Thread.sleep(2000);
				
				//clicking on TestStep tab
				getElement("TM_testStepsTab_Id").click();
				APP_LOGS.debug(" TestStep tab clicked ");
				System.out.println(" TestStep tab clicked ");
				Thread.sleep(2000);
				
				String testStep_tab_highlighted=getElement("TM_testStepsTab_Id").getAttribute("class");
				Thread.sleep(2000);
				
				compareStrings(testStep_tab_highlighted,OR.getProperty("TM_testStepTab_class"));
				
				APP_LOGS.debug("Test Step tab is Highlighted for " +Role);
				System.out.println("Test Step tab is Highlighted for " +Role);
				
				comments="TestStep tab is Highlighted for User "+Role+".";
				
				String actualtestStepPageHeadingText = getObject("TM_testStepTabHeadingDetails").getText();
				
				boolean headingSuccessResult=compareStrings(actualtestStepPageHeadingText,resourceFileConversion.getProperty("TM_TestStepTab_TestStepDetailsText"));
				if(headingSuccessResult==true)
				{
					APP_LOGS.debug("User " + Role+ " is landing on 'Test Step' Page successfully ");
					comments+=" User " + Role+ " is landing on 'Test Step' Page successfully (PASS)";
				} 
				else{
					fail=true;
					APP_LOGS.debug("User " + Role+ " is not landing on 'Test Step' Page (FAIL)");
					comments+=" User " + Role+ " is not landing on 'Test Step' Page (FAIL)";					
				}				

				APP_LOGS.debug("Closing Browser... ");
				System.out.println("Closing Browser... ");
				closeBrowser();
			}
			
			else 
			{
				isLoginSuccess=false;
				APP_LOGS.debug("Login Not Successful");
				System.out.println("Login Not Successful");
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
		public void reportTestResult(){
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
		
		}
		
		
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
		}
		
		
		
}
