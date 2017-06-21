package com.uat.suite.tm_tester;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class VerifyLandingOnTesterPage extends TestSuiteBase  {
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String comments;
	
	
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		}

		@Test(dataProvider="getTestData")
		public void testVerifyLandingOnTesterPage(String Role) throws Exception
		{
			comments = "";
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			APP_LOGS.debug(" Executing Test Case -> VerifyTesterPageContents...");
			
			System.out.println(" Executing Test Case -> VerifyTesterPageContents...");				
			
			openBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			
			System.out.println("Opening Browser... ");
			
			isLoginSuccess = login(Role);
			
			if(isLoginSuccess)
			{
			
				// clicking on Test Management Tab
				getElement("UAT_testManagement_Id").click();
		
				
				APP_LOGS.debug("Clicking On Test Management Tab ");
			
				System.out.println("Clicking On Test Management Tab ");
				//Thread.sleep(3000);
			
				// finding class Attribute Of PROJECTS Tab To Compare with Class Attribute Value which we are providing in AsserEquals

				getObject("Testers_testerTab").click();
				
				
				Thread.sleep(1000);
				
				//String SubHead_TESTERS_Highlighted  =getObject("Testers_testerTab").getAttribute("class");
				
				String SubHead_TESTERS_Highlighted  =getElement("TesterNavigation_Id").getAttribute("class");
				
				
				compareStrings(SubHead_TESTERS_Highlighted,OR.getProperty("Testers_testerTab_Class"));
				
				APP_LOGS.debug("TESTRES TAB is Highlighted ");
				
				System.out.println("TESTERS TAB is Highlighted ");
				
				String actualTestersPageHeadingText = getObject("Testers_testersDetailsHeading").getText();
				
				try {
					compareStrings(actualTestersPageHeadingText,"Tester Details");
					
					APP_LOGS.debug("User " + Role+ " is landing on 'Testers' Page successfully ");
					
					comments="User " + Role+ " is landing on 'Testers' Page successfully (PASS)";
				
				} catch (Exception e) {
					
					fail=true;
					
					APP_LOGS.debug("User " + Role+ " is not landing on 'Testers' Page (FAIL)");
					
					comments="User " + Role+ " is not landing on 'Testers' Page (FAIL)";
					
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
				closeBrowser();
			}		
		}
		
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else
			{
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;
		}
	
	
		@AfterTest
		public void reportTestResult(){
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
		
		}
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
		}
}
