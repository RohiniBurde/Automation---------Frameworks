package com.uat.suite.tm_testpass;

import java.io.IOException;
import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifySendEmailButtonFunctions extends TestSuiteBase  
{
	static int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPass=true;
	boolean isLoginSuccess=false;
	boolean alertTextForMoreThan55Charector = false;
	String runmodes[]=null;
	String comments="";
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> stakeHolder;
	Utility utilRecorder = new Utility();
	boolean sendEmailToBeginTestingButtonIsDisplayed;
	boolean sendTestResultsEmailButtonIsDisplayed;
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider = "getTestData")
	public void VerifySendEmailFunctionality(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL,
			String TestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate,String Assert_createTesterFirst,String Assert_createTestCaseNTestStepFirst, String TP_Tester_TesterName, String TP_TesterRole, String TP_TestCase_TestCaseName,
			String TP_TestStep_TestStepDetails) throws Exception
		{
		// test the runmode of current dataset
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				
				throw new SkipException("Runmode for test set data set to no "+count);
			}
			comments="";
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			int tester_count = Integer.parseInt(TP_Tester_TesterName);
			tester = getUsers("Tester", tester_count);
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);
			
			int stakeHolder_count = Integer.parseInt(Stakeholder);
			stakeHolder = getUsers("Stakeholder", stakeHolder_count);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
			
			openBrowser();
			
			isLoginSuccess = true;//login(role);
			
			if (isLoginSuccess) 
			{
				
				try
				{
					//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					
					//Thread.sleep(2000);
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(500);
					
					if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, 
							versionLead.get(0).username, stakeHolder.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(ProjectName +" Project not Created Successfully.");
						comments=ProjectName +" Project not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessful");
						closeBrowser();
						assertTrue(false);
						throw new SkipException("Project Successfully not created");	
					}
					
					APP_LOGS.debug(ProjectName+" Project Created Successfully.");
					comments= ProjectName+" Project Created Successfully(Pass). ";
					
					closeBrowser();
					
					openBrowser();
							
					if (login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead")) 
					{
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						
						//Click on Test Pass
						gotoTestPassPage();
						
						//Thread.sleep(2000);
	
						selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, VersionLead);
						
						APP_LOGS.debug("Checking Whether 'Send Email To Begin Testing' and 'send Test Results Email' Buttons aren't Displayed when No Test Pass are Available ");
						
						sendEmailToBeginTestingButtonIsDisplayed = getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").isDisplayed();
						
						sendTestResultsEmailButtonIsDisplayed = getElement("TestPassViewAll_sendTestResultsEmailButton_Id").isDisplayed();
						
						if (assertTrue(!(sendEmailToBeginTestingButtonIsDisplayed&&sendTestResultsEmailButtonIsDisplayed))) 
						{
							APP_LOGS.debug("'Send Email To Begin Testing' and 'send Test Results Email' Buttons are not Displayed in absence of Test Pass. : (PASS)" );
							comments+="'Send Email To Begin Testing' and 'send Test Results Email' Buttons are not Displayed in absence of Test Pass. : (PASS)";
						}
						else
						{
							assertTrue(false);
	
							fail = true;
							
							comments += "'Send Email To Begin Testing' and 'send Test Results Email' Buttons are Displayed even when no testpasses are present. : (FAIL)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The email buttons should not have been there in absence of test passes");
						}
						
						if(!enterValidDataInMandatoryFieldsOfTestPass(TestPassName, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
						{
							assertTrue(false);
	
							fail = true;
							
							comments += "Fail Occurred: while providing mandatory data in test pass... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect data in mandatory fields");
						}
						
						Thread.sleep(500);
						//getObject("TestPasses_viewAllProjectLink").click();
						
						sendEmailToBeginTestingButtonIsDisplayed = getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").isDisplayed();
						
						sendTestResultsEmailButtonIsDisplayed = getElement("TestPassViewAll_sendTestResultsEmailButton_Id").isDisplayed();
						
						if (assertTrue(sendEmailToBeginTestingButtonIsDisplayed&&sendTestResultsEmailButtonIsDisplayed)) 
						{
							APP_LOGS.debug("'Send Email To Begin Testing' and 'send Test Results Email' Buttons are Displayed in presence of test passes. : Test Case Has been passed" );
							comments+="The email buttons are present in presence of test passes(PASS)";
						}
						else
						{
							fail = true;
							
							comments += "The email buttons should have been present in presence of test passes but they were not(FAIL)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The email buttons should have been presnet in presence of test passes but they were not");
							
							closeBrowser();
							
							throw new SkipException("The email buttons should have been presnet in presence of test passes but they were not");
						
						}
						
						APP_LOGS.debug("Click on 'Send Email to Begin Testing' Button Without creating tester");
						
						//Thread.sleep(500);		
						//getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").click();		
						
						clickOnSendEmailToBeginTestingButton();
					
						//Thread.sleep(1000);
						
						if(!clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(Assert_createTesterFirst))
						{
							assertTrue(false);
	
							fail = true;
							comments+="On clicking Cancel, user should have remained in same page but the page changed (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Cancel, user should have remained in same page but the page changed");
						}
						
						APP_LOGS.debug("Click on Send Email Button Without creating tester and after that Click On Ok Button");
						
						clickOnSendEmailToBeginTestingButton();
						
						//Thread.sleep(1000);
						
						if(!clickOnOkButtonAndVerifyTesterPageIsDisplayed())
						{
							assertTrue(false);
	
							fail = true;
							comments+="On clicking Ok, user should have been redirected to tester page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to tester page but was not");
						}
						
						gotoTestPassPage();
						
						//Thread.sleep(1000);
						
						APP_LOGS.debug("Click on 'Send Test Result Email' button Without creating tester");
						
						clickOnSendTestResultsEmailButton();
						
						//Thread.sleep(1000);
						
						if(!clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(Assert_createTesterFirst))
						{
							assertTrue(false);
	
							fail = true;
							comments+="On clicking Cancel, user should have remained in same page but the page changed (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Cancel, user should have remained in same page but the page changed");
						}
						
						APP_LOGS.debug("Click on Send Tests Result Button Without creating tester and after that Click On Ok Button");
						
						clickOnSendTestResultsEmailButton();
						
						if(!clickOnOkButtonAndVerifyTesterPageIsDisplayed())
						{
							assertTrue(false);
	
							fail = true;
							comments+="On clicking Ok, user should have been redirected to tester page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to tester page but was not");
						}
						
						APP_LOGS.debug("Adding a Tester From Tester Details Page");
						
						createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, TP_TesterRole, TP_TesterRole);
						
						gotoTestPassPage();
						
						//Thread.sleep(2000);
						
						clickOnSendEmailToBeginTestingButton();
						
						//Thread.sleep(1500);
						
						if(!clickOnOkButtonAndVerifyTestCasePageIsDisplayed(Assert_createTestCaseNTestStepFirst))
						{
							assertTrue(false);
	
							fail = true;
							comments+="On clicking Ok, user should have been redirected to test case page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to test case page but was not");
						}
						
						gotoTestPassPage();
						
						//Thread.sleep(500);
						clickOnSendTestResultsEmailButton();
						
						//Thread.sleep(1500);
						
						if(!clickOnOkButtonAndVerifyTestCasePageIsDisplayed(Assert_createTestCaseNTestStepFirst))
						{
							fail = true;
							comments+="On clicking Ok, user should have been redirected to test case page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to test case page but was not");
							assertTrue(false);
	
						}
						
						createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_TestCase_TestCaseName);
						
						gotoTestPassPage();
						
						//Thread.sleep(2000);
						
						clickOnSendEmailToBeginTestingButton();
						
						//Thread.sleep(1500);
						
						if(!clickOnOkButtonAndVerifyTestStepPageIsDisplayed(Assert_createTestCaseNTestStepFirst))
						{
							fail = true;
							comments+="On clicking Ok, user should have been redirected to test case page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to test case page but was not");
							assertTrue(false);
							getElement("TM_testStepsTab_Id").click();
						}
						
						gotoTestPassPage();
						
						//Thread.sleep(500);
						clickOnSendTestResultsEmailButton();
						
						//Thread.sleep(1500);
						
						if(!clickOnOkButtonAndVerifyTestStepPageIsDisplayed(Assert_createTestCaseNTestStepFirst))
						{
							fail = true;
							comments+="On clicking Ok, user should have been redirected to test case page but was not (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "On clicking Ok, user should have been redirected to test case page but was not");
							assertTrue(false);
							getElement("TM_testStepsTab_Id").click();
						}
						
						APP_LOGS.debug("create a test step for the Test Case made.");
						
						createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_TestStep_TestStepDetails, "", TP_TestCase_TestCaseName, TP_TesterRole);
						
						gotoTestPassPage();
						
						clickOnSendEmailToBeginTestingButton();
						
						String textFromAlert = waitForElementVisibility("TestPasses_popupText_Id",10).getText();
						
						if (textFromAlert!=null) {
							
							fail = true;
							comments+="After creating tester, test case and test step, user still received the alert (FAIL)";
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "After creating tester test case and test step user still received the alert");
							
						}
		
						/*try 
						{
							APP_LOGS.debug("IE alert popup handling. Alert came because Outlook is open");
							eventfiringdriver.switchTo().alert().accept();
						} 
						catch (Exception e) 
						{
							APP_LOGS.debug("No alert has come as outlook is closed");
							clickOnSendEmailToBeginTestingButton();
							
							if (getObject("TestPassViewAll_prerequisitesForOutlookAlert").isDisplayed()) 
							{
								APP_LOGS.debug("Prerequisites For Outlook Alert is displayed");
								getObject("TestPassViewAll_crossCloseButtonForOutlookMailPopup").click();
							}
						}
						*/
					
						
					}
					else
					{
						APP_LOGS.debug("Login not successful for version lead");
						
						fail=true;
					}
					
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					comments+="Exception Occured";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception Occured");
					
				}
				
				closeBrowser();	
				
			}
			else 
			{
				APP_LOGS.debug("Login NOT SUCCESSFUL");
				
				//fail=true;
			}	

		}
	
	//private Functions
	
	private void gotoTestPassPage() throws IOException, InterruptedException
	{
		//Thread.sleep(500);
		getElement("TM_testPassesTab_Id").click();
		Thread.sleep(500);
	}
	private void clickOnSendTestResultsEmailButton() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TestPassViewAll_sendTestResultsEmailButton_Id").click();
		//Thread.sleep(500);
	}
	private void clickOnSendEmailToBeginTestingButton() throws IOException, InterruptedException
	{
		Thread.sleep(500);		
		getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").click();		
		//Thread.sleep(500);
		
	}
	
	private boolean clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(String Assert_createTesterFirst) throws IOException, InterruptedException
	{
		String popupTextAsTesterNotAvailable = waitForElementVisibility("TestPasses_popupText_Id",10).getText();
		
		if(compareStrings(popupTextAsTesterNotAvailable, Assert_createTesterFirst))
		{
			APP_LOGS.debug(popupTextAsTesterNotAvailable + " : Expected Text is Displayed in absence of tester. (PASS)" );	
		}
		else
		{
			fail=true;
			assertTrue(false);
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected pop up text was not displayed in absence of tester. (FAIL)");
			comments+="Expected pop up text was not displayed in absence of tester. (FAIL)";
		}
		getObject("TestPasses_cancleButton").click();
		
		if(compareStrings(getElementByClassAttr("PageHeaderText_Class").getText(), resourceFileConversion.getProperty("TestPass_Heading")))
		return true;
		else
		return false;
	}
	
	private boolean clickOnOkButtonAndVerifyTesterPageIsDisplayed() throws IOException, InterruptedException
	{
		waitForElementVisibility("TestPasses_popupText_Id",10);
		getObject("TestPasses_okButtonForPopup_Email").click();
		Thread.sleep(500);

		if(compareStrings(getElementByClassAttr("PageHeaderText_Class").getText(), resourceFileConversion.getProperty("Tester_Heading")))
		return true;
		else
		{
			getElement("TM_testersTab_Id").click();
			comments+="Did not redirect to tester page (Fail)";
			return false;
		}
	}
	
	private boolean clickOnOkButtonAndVerifyTestCasePageIsDisplayed(String Assert_createTestCaseNTestStepFirst) throws IOException, InterruptedException
	{
		String popupTextAsTestCaseNotAvailable = waitForElementVisibility("TestPasses_popupText_Id",10).getText();
		
		if(compareStrings(popupTextAsTestCaseNotAvailable, Assert_createTestCaseNTestStepFirst))
		APP_LOGS.debug(popupTextAsTestCaseNotAvailable + " : Expected Text is Displayed. Test Case Has been passed" );	
		else
		{
			fail=true;
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected pop up text was not displayed");
			comments+="Expected pop up text was not displayed";
			assertTrue(false);
		}
		
		getObject("TestPasses_okButtonForPopup").click();
		Thread.sleep(1000);
		if(compareStrings(getElementByClassAttr("PageHeaderText_Class").getText(), resourceFileConversion.getProperty("TestCase_Heading")))
		return true;
		else
		{
			comments+="Did not redirect to test case page (Fail)";
			getElement("TM_testCasesTab_Id").click();
			return false;
		}
		
	}
	
	private boolean clickOnOkButtonAndVerifyTestStepPageIsDisplayed(String Assert_createTestCaseNTestStepFirst) throws IOException, InterruptedException
	{
		String popupTextAsTestStepNotAvailable = waitForElementVisibility("TestPasses_popupText_Id",10).getText();
		
		if(compareStrings(popupTextAsTestStepNotAvailable, Assert_createTestCaseNTestStepFirst))
		APP_LOGS.debug(popupTextAsTestStepNotAvailable + " : Expected Text is Displayed. Test Case Has been passed" );	
		else
		{
			fail=true;
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected pop up text was not displayed");
			comments+="Expected pop up text was not displayed";
			assertTrue(false);
		}
		
		getObject("TestPasses_okButtonForPopup").click();
		Thread.sleep(1000);
		if(compareStrings(getElementByClassAttr("PageHeaderText_Class").getText(), resourceFileConversion.getProperty("TestStep_Heading")))
		return true;
		else
		{
			comments+="Did not redirect to test step page (Fail)";
			
			return false;	
		}
	}
	

	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
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
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	
}
