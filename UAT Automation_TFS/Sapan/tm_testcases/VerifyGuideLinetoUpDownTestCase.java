package com.uat.suite.tm_testcases;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyGuideLinetoUpDownTestCase extends TestSuiteBase {
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	String comments;

	
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
		
			
		}
		
	
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void Test_VerifyGuideLinetoUpDownTestCase (String Role,String ExpectedDownloadedtemplatetoimportText) throws Exception
		{
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			
			
			APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
			
			
			APP_LOGS.debug("Opening Browser... ");
			
			
			openBrowser();
			
			
			isLoginSuccess = login(Role);
			

			if(isLoginSuccess)
			{
							
				try
				{
					//click on testManagement tab
					APP_LOGS.debug("Clicking On Test Management Tab ");
					
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(1000);
					
					
					APP_LOGS.debug("Clicking On Test Cases Tab ");
					
					
					getElement("TestCaseNavigation_Id").click();
					
					Thread.sleep(3000);					
					
					
					getElement("TestCasesDownload_UploadTemplateTestCasesLink_Id").click();
					
					
					
					String downloadedtemplatetoimportText=getObject("TestCaseDownloadTemplateText").getText();
					
					
					
					
					if(compareStrings(downloadedtemplatetoimportText, ExpectedDownloadedtemplatetoimportText))
					{
						APP_LOGS.debug("Dowmload Template Import Text is Showing Properly");
					}
					else
					{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DownloadTemplateTextIssue");
						
					}
					
					String HowToImportStepText =getElement("TestCaseDownloadUploadTemplate_ImportStepMessage_Id").getText();
					
					/*if (compareStrings(ExpectedHowToImportStepText,HowToImportStepText))
					{
						APP_LOGS.debug("Dowmload Template Import Steps Messgae is Showing Properly");
					}
					else
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"VerifyGuideLinetoUpDownTestCasesError");
					}*/
					
					
					
					
				/*	//myAlert.accept();
					Robot r = new Robot();
					

					r.keyPress(KeyEvent.VK_ENTER);*/
					
					getElement("TestCasesDownloadUploadTestCases_TestCasesGuideLineLink_Id").click();
					
					//Alert myAlert=eventfiringdriver.switchTo().alert();
					//myAlert.accept();
					Robot r = new Robot();
					r.keyPress(KeyEvent.VK_ENTER);
					
					
					APP_LOGS.debug("Guidelines to Upload and download Steps Message for  Test Cases Showing properly ");
					comments+="Guidelines to Upload and Download Steps for Test Cases showing Properly (Pass)  .";
					
					
					closeBrowser();
					
			
				}
				
				catch(Throwable t)
				{
					t.printStackTrace();
					fail = true;
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
					comments+="Skip Exception or other exception occured" ;
				}
			
			}	
		
			else 
			{
				isLoginSuccess=false;
				APP_LOGS.debug("Login Not Successful");
		
			}	


				

		}				
	





		@AfterMethod
		public void reportDataSetResult()
		{
			if(skip)
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else
			{
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			
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