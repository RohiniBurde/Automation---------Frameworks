package com.uat.suite.tm_testpass;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifySendEmailButtonFunctions extends TestSuiteBase  
{
	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=false;
	boolean alertTextForMoreThan55Charector = false;
	String runmodes[]=null;
	String comments;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;

	boolean sendEmailToBeginTestingButtonIsDisplayed;
	boolean sendTestResultsEmailButtonIsDisplayed;
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
		testManager=new ArrayList<Credentials>();
		//testManager=getUsers("Test Manager", 1)
		tester=new ArrayList<Credentials>();
		//testManager=getUsers("Test Manager", 1)
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyToAcceptCombiOfAlphabets(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
			String TestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate,String Assert_createTesterFirst,String Assert_createTestCaseNTestStepFirst, String TP_Tester_TesterName, String TP_TestCase_TestCaseName,
			String TP_TestStep_TestStepDetails) throws Exception
		{
		// test the runmode of current dataset
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				
				throw new SkipException("Runmode for test set data set to no "+count);
			}
			
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			int tester_count = Integer.parseInt(TP_Tester_TesterName);
			tester = getUsers("Tester", tester_count);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
			
			openBrowser();
			
			isLoginSuccess = login(role);
			
			if (isLoginSuccess) 
			{
				//click on testManagement tab
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				
				Thread.sleep(2000);
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(2000);
				
				//Click on Test Pass
				gotoTestPassPage();
				
				Thread.sleep(2000);
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
				
				selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
						"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
				
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
				
				selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
						"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
			
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
				
				selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
						"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
				
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
				
				selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
						"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);
				
				
				
				APP_LOGS.debug("Checking Whether 'Send Email To Begin Testing' and 'send Test Results Email' Buttons aren't Displayed when No Test Pass are Available ");
				sendEmailToBeginTestingButtonIsDisplayed = getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").isDisplayed();
				sendTestResultsEmailButtonIsDisplayed = getElement("TestPassViewAll_sendTestResultsEmailButton_Id").isDisplayed();
				
				if (!(sendEmailToBeginTestingButtonIsDisplayed&&sendTestResultsEmailButtonIsDisplayed)) 
				{
					APP_LOGS.debug("'Send Email To Begin Testing' and 'send Test Results Email' Buttons are not Displayed. : Test Case Has been passed" );
				}
				
				if(enterValidDataInMandatoryFieldsOfTestPass(TestPassName, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
				{
					comments = "Mandatory data in test pass has provided... ";
				}
				else
				{
					fail = true;
					
					comments = "Fail Occurred: while providing mandatory data in test pass... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect data in mandatory fields");
				}
				
				getObject("TestPasses_viewAllProjectLink").click();

				gotoTestPassPage();
				
				sendEmailToBeginTestingButtonIsDisplayed = getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").isDisplayed();
				sendTestResultsEmailButtonIsDisplayed = getElement("TestPassViewAll_sendTestResultsEmailButton_Id").isDisplayed();
				
				if (sendEmailToBeginTestingButtonIsDisplayed&&sendTestResultsEmailButtonIsDisplayed) 
				{
					APP_LOGS.debug("'Send Email To Begin Testing' and 'send Test Results Email' Buttons are Displayed. : Test Case Has been passed" );
				}
				
				APP_LOGS.debug("Click on 'Send Email to Begin Testing' Button Without creating tester");
				clickOnSendEmailToBeginTestingButton();
				Thread.sleep(1000);
				clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(Assert_createTesterFirst);
				
				APP_LOGS.debug("Click on Send Email Button Without creating tester and after that Click On Ok Button");
				clickOnSendEmailToBeginTestingButton();
				
				clickOnOkButtonAndVerifyTesterPageIsDisplayed();
				
				gotoTestPassPage();
				
				Thread.sleep(1000);
				
				APP_LOGS.debug("Click on 'Send Test Result Email' button Without creating tester");
				clickOnSendTestResultsEmailButton();
				Thread.sleep(1000);
				clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(Assert_createTesterFirst);
				
				APP_LOGS.debug("Click on Send Email Button Without creating tester and after that Click On Ok Button");
				clickOnSendTestResultsEmailButton();
				
				clickOnOkButtonAndVerifyTesterPageIsDisplayed();
				
				
				APP_LOGS.debug("Adding a Tester From Tester Details Page");
				
				//gotoTestPassPage();
				Thread.sleep(1500);
				
				getElement("TM_testersTab_Id").click();
				Thread.sleep(1500);
				
				getObject("TesterDetails_addTesterTab").click();
				
				APP_LOGS.debug("Handeling Active-x Popup.");
				
				activeXHandling();
				
				APP_LOGS.debug("Adding Mandatory Fields in ADD Tester Page");
				
				
							
				APP_LOGS.debug("Clicking on Tester Image Icon...");
				getObject("TesterDetailsAddTester_testerPeoplePickerImg").click();
				  
				driver.switchTo().frame(1);
				APP_LOGS.debug("Switched to the Version Lead frame...");
			  
				wait = new WebDriverWait(driver,200);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			  
				APP_LOGS.debug(tester.get(0).username + " : Input text in Tester text field...");
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(tester.get(0).username); 
			   
				APP_LOGS.debug("Click on Search button from frame ...");
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			   
				APP_LOGS.debug("Tester is selected from search result...");
				getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			  
				String displayNamefromPioplePicker = getObject("TesterDetailsAddTester_testerDisplayNameFromPeoplePicker").getText();
				System.out.println("display name from people picker: "+displayNamefromPioplePicker);
				
				APP_LOGS.debug("Click on OK button from frame ...");
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			  
				driver.switchTo().defaultContent();
				
				Thread.sleep(1000);
				
				APP_LOGS.debug("Clicking the check box for Role");
				getElement("TesterDetailsAddTester_standardRoleCheckBox_Id").click();
				
				getObject("TesterDetailsAddTester_SaveBtn").click();
				
				String textFromThePopupAfterSaveButton = getObject("TesterDetailsAddTester_testerPopupTextAfterClickOnSave").getText();
				System.out.println("textFromThePopupAfterSaveButton : "+textFromThePopupAfterSaveButton);
				
				System.out.println("textFromThePopupAfterSaveButton : "+displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) Standard.\nPlease click \"VIEW ALL\" to see the details.");
				
				if (textFromThePopupAfterSaveButton.equals(displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) Standard.\nPlease click \"VIEW ALL\" to see the details.")) 
				{
					System.out.println("*******************");
					APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
					
					APP_LOGS.debug("Click on OK Button");
				//	System.out.println(getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").getText());
					Thread.sleep(1000);
				//	eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
					getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
					APP_LOGS.debug("Clicked on OK Button");
					
					
					
					
					//Ok button is not visible with xpahgetObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
					//div[@id='divAlert']/following-sibling::div[9]/div[1]/button[1]/span
					
					
					comments = comments+ "Tester saved successfully ";
				}
				else {
					fail = true;
					
					APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
					
					comments = comments+ "Fail Occurred: Popup text is Not as expected while saving a Tester. ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
				}
				Thread.sleep(2000);
				
				gotoTestPassPage();
				
				clickOnSendEmailToBeginTestingButton();
				Thread.sleep(1500);
				
				clickOnOkButtonAndVerifyTestCasePageIsDisplayed(Assert_createTestCaseNTestStepFirst);
				
				gotoTestPassPage();
				
				Thread.sleep(500);
				clickOnSendTestResultsEmailButton();
				
				Thread.sleep(1500);
				clickOnOkButtonAndVerifyTestCasePageIsDisplayed(Assert_createTestCaseNTestStepFirst);
				
				//gotoTestPassPage();
				
				gotoTestCasePage();
				
				APP_LOGS.debug("Creating new Test case.");
				
				getObject("TestPasses_createNewTestCaseNTestStepLink").click();
				
				getElement("TestPassCreateTestCase_testCaseNameField_Id").sendKeys(TP_TestCase_TestCaseName);
					
				getObject("TestPassCreateTestCase_saveTestCaseButton").click();
				
				String textFromThePopupAfterSaveButtonOfTCDetails = getObject("TestPassCreateTestCase_testCasePopupTextAfterClickOnSave").getText();
				
				System.out.println("textFromThePopupAfterSaveButton : "+textFromThePopupAfterSaveButtonOfTCDetails);
				
				if (textFromThePopupAfterSaveButtonOfTCDetails.equals("Test Case added successfully")) 
				{
					APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButtonOfTCDetails);
					
					APP_LOGS.debug("Click on OK Button");

					Thread.sleep(1000);

					getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
					APP_LOGS.debug("Clicked on OK Button");
					APP_LOGS.debug("Test Case has been added");
					
					comments = comments+ "A test case has saved... ";
				}
				else {
					fail = true;
				
					APP_LOGS.debug("Popup text is Not as expected while saving a Test case. Test Case has been failed");
				
					comments = comments+ "Fail Occurred: while saving a test case... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "unexpected popup text while saving test case");
				}
				
				APP_LOGS.debug("create a test step for the Test Case made.");
				
				gotoTestStepPage();
				
				getObject("TestPasses_createNewTestCaseNTestStepLink").click();
				
				getObject("TestPassCreateTestStep_firstChkBoxInSelectTestCaseField").click();
				try 
				{
					APP_LOGS.debug("Entering valid data in test Step Details IFrame.");
					
					String abc = "$(document).contents().find('#rte1').contents().find('body').text('"+TP_TestStep_TestStepDetails+"')";
					
					eventfiringdriver.executeScript(abc);
					
				} 
				catch (Throwable t) 
				{
					APP_LOGS.debug("Some error while Adding data in test Step details. Test Case has been Failed");
					
					fail= true;
					
					comments = comments+ "Fail Occurred: while adding test step details... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "adding mandatory data in test step");
				}
		
				getObject("TestPassCreateTestStep_firstChkBoxInAssignRoleField").click();
				
				getObject("TestPassCreateTestStep_saveTestStepButton").click();
				
				String textFromThePopupAfterSaveButtonOfTestStepDetails = getObject("TestPassCreateTestStep_testStepPopupTextAfterClickOnSave").getText();
				
				if (textFromThePopupAfterSaveButtonOfTestStepDetails.equals("Test Step added successfully!")) 
				{
					APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButtonOfTestStepDetails);
					
					APP_LOGS.debug("Click on OK Button");

					Thread.sleep(1000);

					getObject("TestPassCreateTestStep_testStepAddedsuccessfullyOkButton").click();
					APP_LOGS.debug("Clicked on OK Button");
					APP_LOGS.debug("Test Case has been added");
				}
				else {
					fail = true;
					
					APP_LOGS.debug("Popup text is Not as expected while saving a test step. Test Case has been failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "unexpected popup text while saving test step");
				}
				
				gotoTestPassPage();
				
				clickOnSendEmailToBeginTestingButton();

				try 
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
				
				gotoTestCasePage();
				Thread.sleep(1000);
				
				deleteTestCaseFromFirstRowIfProvidedTestPassNamePresent(TP_TestCase_TestCaseName);
				
				gotoTestPassPage();
				Thread.sleep(1000);
				
				deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName);
				
				//closeBrowser();	
			}
			else 
			{
				APP_LOGS.debug("Login NOT SUCCESSFUL");
				
				isLoginSuccess=false;
			}	

		}
	
	//private Functions
	
	private void gotoTestPassPage() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TM_testPassesTab_Id").click();
		Thread.sleep(500);
	}
	private void gotoTestCasePage() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TM_testCasesTab_Id").click();
		Thread.sleep(500);
	}
	private void gotoTestStepPage() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TM_testStepsTab_Id").click();
		Thread.sleep(500);
	}
	
	private void clickOnSendTestResultsEmailButton() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TestPassViewAll_sendTestResultsEmailButton_Id").click();
	}
	private void clickOnSendEmailToBeginTestingButton() throws IOException, InterruptedException
	{
		Thread.sleep(500);
		getElement("TestPassViewAll_sendEmailToBeginTestingButton_Id").click();
	}
	
	private void clickOnCancelButtonAndVerifySamePageIsDisplayedAndAlsoAssertPopupText(String Assert_createTesterFirst)
	{
		try 
		{
			String popupTextAsTesterNotAvailable = getObject("TestPasses_popupText").getText();
			
			compareStrings(popupTextAsTesterNotAvailable, Assert_createTesterFirst);
			
			//Assert.assertEquals(popupTextAsTesterNotAvailable, "Sorry! No Tester is available for selected Test Pass ! Please create the Tester First.");	
			
			APP_LOGS.debug(popupTextAsTesterNotAvailable + " : Expected Text is Displayed. Test Case Has been passed" );
		} 
		catch (Throwable t) 
		{
			fail = true;
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" Assertion has been failed.");
		}
		
		try 
		{
			getObject("TestPasses_cancleButton").click();
			
			getObject("TestPasses_testPassHeading").isDisplayed();
			
			APP_LOGS.debug("Click On Cancel button. Test Pass Detail Is Displayed. Test Case has been Passed");
		} 
		catch (Throwable t) 
		{
			fail = true;
			APP_LOGS.debug("Click On Cancel button. Test Pass Detail Is Not Displayed. Test Case has been Failed");
		}	
		
	}
	
	private void clickOnOkButtonAndVerifyTesterPageIsDisplayed()
	{
		try 
		{
			getObject("TestPasses_okButtonForPopup").click();
			
			getObject("TestPasses_testerDetailsPageHeading").isDisplayed();
			
			APP_LOGS.debug("Click On Ok button. Tester Details Page Is Displayed. Test Case has been Passed");
		} 
		catch (Throwable t) 
		{
			fail = true;
			APP_LOGS.debug("Click On Ok button. Tester Details Page Is Not Displayed. Test Case has been Failed");
		}
	}
	
	private void clickOnOkButtonAndVerifyTestCasePageIsDisplayed(String Assert_createTestCaseNTestStepFirst)
	{
		try 
		{
			String popupTextAsTestCaseNotAvailable = getObject("TestPasses_popupText").getText();
			compareStrings(popupTextAsTestCaseNotAvailable, Assert_createTestCaseNTestStepFirst);
		
			//	Assert.assertEquals(popupTextAsTestCaseNotAvailable, "Sorry! No Test Case or Test Step is available for selected Test Pass ! Please create the Test Case or Test Step First.");	
			
			APP_LOGS.debug(popupTextAsTestCaseNotAvailable + " : Expected Text is Displayed. Test Case Has been passed" );
		} 
		catch (Throwable t) 
		{
			fail = true;
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" Assertion has been failed.");
		}
		
		try 
		{
			getObject("TestPasses_okButtonForPopup").click();
			
			getObject("TestPasses_testCaseDetailsPageHeading").isDisplayed();
			
			APP_LOGS.debug("Click On Ok button. Tester Details Page Is Displayed. Test Case has been Passed");
		} 
		catch (Throwable t) 
		{
			fail = true;
			APP_LOGS.debug("Click On Ok button. Tester Details Page Is Not Displayed. Test Case has been Failed");
		}
	}
	
	private void deleteTestCaseFromFirstRowIfProvidedTestPassNamePresent(String TestPassName) throws InterruptedException, IOException
	{
	
		APP_LOGS.debug("Deleting Created Test Case");
		
	//	getObject("TestPasses_viewAllProjectLink").click();
		
		if(!getElement("TestPassViewAll_NoTestCaseAvailable_Id").isDisplayed())
		{
			
			String testPassNameFromViewAllPage = getObject("TestPassViewAll_testCaseNameFromViewAllPage").getText();
			
			if (testPassNameFromViewAllPage.equals(TestPassName)) 
			{
				APP_LOGS.debug("Deleting Test Pass");
				
				//getObject("TestPassViewAll_deleteTestPassNameFromViewAllPage1", "TestPassViewAll_deleteTestPassNameFromViewAllPage2", i).click();
				getObject("TestPassViewAll_deleteTestCaseNameImageFromViewAllPage").click();
				getObject("TestPassViewAll_popUpDeleteButton").click();
				Thread.sleep(1500);
				getObject("TestPasses_okButtonForPopup").click();
			}
			else 
			{
				APP_LOGS.debug("Test Case name Not Found.");
			}
		}
		else {
			APP_LOGS.debug("No Test Case Are present on Test Case Details Page");
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
	public void reportTestResult() throws InterruptedException
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
	Thread.sleep(2000);
	closeBrowser();
	}
	
	
}
