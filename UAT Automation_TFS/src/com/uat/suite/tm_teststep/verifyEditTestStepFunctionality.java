package com.uat.suite.tm_teststep;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class verifyEditTestStepFunctionality extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	String testCaseCreatedSuccessMessage;
	String comments;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());			

		if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
		
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		testers=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void Test_TCs_CreateNewTestcase (String Role,String GroupName,String PortfolioName,String ProjectName, 
			String Version,String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,
			String TestManager,String Tester,String AddRole,String TestCaseName, String TestCaseName2, String TestStepName,String EditedTestStepName,String TestStepExpectedResult, String testStepExpectedResult2,
			String invalidRoleUpdatePopupMessage, String testStepSuccessUpdate) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		comments="";
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);

		int testManager_count = Integer.parseInt(TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		int testers_count = Integer.parseInt(Tester);
		testers = getUsers("Tester", testers_count);
		
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
				APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
				
				if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
				{
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
					comments="Project Creation Unsuccessful(Fail) by "+Role+"  . ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+" . ");
					throw new SkipException("Project Creation Unsuccessful");
				}
				
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
				
				openBrowser();
				
				if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
				{
					//click on testManagement tab
					APP_LOGS.debug("Clicking On Test Management Tab ");
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(2000);
					
					if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{	
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
						comments="Test Pass Creation Unsuccessful(Fail)   ." ;
						APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
						throw new SkipException("Test Pass Creation Unsuccessful");
					}
					
					APP_LOGS.debug("Closing Browser... ");
					closeBrowser();
					
					APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
					
					openBrowser();
					
					if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
					{
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
								
						if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
							comments="Tester Creation Unsuccessful(Fail)   ." ;
							APP_LOGS.debug("Tester Creation Unsuccessful");
							throw new SkipException("Tester Creation Unsuccessful");
						}
						
						APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
						
						if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName,TestCaseName))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
							APP_LOGS.debug("Test Case Creation Unsuccessfull");
							comments="Test Case Creation Unsuccessful(Fail)   ." ;
							throw new SkipException("Test Case Creation Unsuccessful");
						}
						
						//Creating second test case 
						Thread.sleep(1500);
						
						getObject("TestCase_createNewProjectLink").click();
						
						Thread.sleep(1000);
						
						getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(TestCaseName2); 
						
						getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
						
						Thread.sleep(2000);
						
			//		 	getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
						
						APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Step ");
						
						if (!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestStepName, TestStepExpectedResult, TestCaseName, AddRole))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
							APP_LOGS.debug("Test Step Creation Unsuccessfull");
							comments="Test Step Creation Unsuccessful(Fail)   ." ;
							throw new SkipException("Test Step Creation Unsuccessful");
						}
						
						APP_LOGS.debug("Clicking on View All Link after Test Step Creation ");
						
						
				//		getElement("TestStepNavigation_Id").click();
						
						getObject("TestStep_viewAll").click();
						
						//Verification of created test step test step in View all
						
						APP_LOGS.debug(" Created Test Step is in view All present or not ? ");	
						
						Thread.sleep(2000);
						List<WebElement> listofTestStep =getElement("TestStepViewAll_Table_Id").findElements(By.tagName("tr"));
						
						for(int testStepCount=1;testStepCount<=listofTestStep.size();testStepCount++)
						{
							//Verification of copied test name with the actual test step name in other test pass
							APP_LOGS.debug("Verfication of test step name is matched with the actual test step name");		
							String actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
							
							//Verification of copied test case name  with the actual test case name in other test pass
							String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
							
							String actualExpectedResult = getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_ExpectedResultXpath2", testStepCount).getText();
									
							APP_LOGS.debug("Comparing the name of test step with the actual test step, test case name with the actual test case name and expected result");
							if(compareStrings(TestStepName, actualTestStepName)&&compareStrings(TestCaseName, actualTestCaseName)&&compareStrings(TestStepExpectedResult, actualExpectedResult))
							{
								APP_LOGS.debug("Name of test step , test case and exp result is matched");									
								comments=comments+"Name of Test Step and other details matched with saved ones in grid (PASS)   ";
								APP_LOGS.debug("The name of test steps are matched , hence clicking on Edit icon");									
								getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepEditIconXpath2", testStepCount).click();
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Unable to Click On Edit Icon as Name of Test Step Is not Matched");
								comments=comments+"Unable to Click On Edit Icon as Name of Test Step Is not Matched (fail)   ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Saved test step details not matched with grid details");
							}
						 }	
						
						 APP_LOGS.debug("Updating the Created Test Step");
						 eventfiringdriver.executeScript("$(document).contents().find('#rte1').contents().find('body').text('')");
						 String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+EditedTestStepName+"')";
						 eventfiringdriver.executeScript(testStepDetails);
						 
						 List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
						 int numOfTestCases = TestCaseSelectionNames.size();
						 for(int i = 0; i<numOfTestCases;i++)
						 {
							getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
						 }
						 
						 List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
						 int numOfRoles = roleSelectionNames.size();
						 for(int i = 0; i<numOfRoles;i++)
						 {
							getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
						 }
						 
						 getElement("TestStepCreateNew_testStepExpectedResults_ID").clear(); 
							
						 getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResult2); 
							
						 getElement("TestStepCreateNew_testStepUpdateBtn_Id").click();
						 
						 if(compareStrings(invalidRoleUpdatePopupMessage, getElement("TestStepCreateNew_successMessagePopupText_Id").getText()))
						 {
							 comments+="Test Step could not be updated as there was no tester assigned with Standard Role (PASS) ";
						 }
						 else
						 {
							 fail=true;
							 comments=comments+"Test step was updated with Standard Role even when there was no testerassigned with it (fail) .";
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test step updated with Standard Role although there was no tester assigned with it");
						 }
							 
						 getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
						 
						 if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(1).username,"Standard", "Standard")) 
						 {	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
							comments="Tester Creation Unsuccessful(Fail)   ." ;
							APP_LOGS.debug("Tester Creation Unsuccessful");
							throw new SkipException("Tester Creation Unsuccessful");
						 } 
						 
						 getElement("TM_testStepsTab_Id").click();
						 
						 if(searchForTheTestStep(TestStepName))
						 {
							 eventfiringdriver.executeScript("$(document).contents().find('#rte1').contents().find('body').text('')");
							 testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+EditedTestStepName+"')";
							 eventfiringdriver.executeScript(testStepDetails);
							 
							 TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
							 numOfTestCases = TestCaseSelectionNames.size();
							 for(int i = 0; i<numOfTestCases;i++)
							 {
								getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
							 }
							 
							 roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
							 numOfRoles = roleSelectionNames.size();
							 for(int i = 0; i<numOfRoles;i++)
							 {
								getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							 }
							 
							 getElement("TestStepCreateNew_testStepExpectedResults_ID").clear(); 
								
							 getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResult2); 
								
							 getElement("TestStepCreateNew_testStepUpdateBtn_Id").click();
							 
							 String actualTestStepUpdatedMessage=getTextFromAutoHidePopUp();
						//	 getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText();
							 if(compareStrings(testStepSuccessUpdate, actualTestStepUpdatedMessage))
							 {
								 APP_LOGS.debug("Test Step Updated Successfully");
								 comments+="Test Step Updated Successfully (PASS) ";
							 }
							 else
							 {
								 fail=true;
								 comments=comments+"Test step Updated Success Message is not shown (fail)   .";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test step Updated Success Message is not shown");
							 }
					//		 getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
							 
						 }
						 else
						 {
							 fail=true;
							 assertTrue(false);
							 comments+="Test Step was not visible in grid after page refresh (FAIL)";
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step was not visible in grid after page refresh");
						 }
						
						 getObject("TestStep_viewAll").click();
						 
						 //Verification Of Updated test step 
						 APP_LOGS.debug(" Updated Test Step is in view All present or not ? ");	
						 listofTestStep =getElement("TestStepViewAll_Table_Id").findElements(By.tagName("tr"));
						 for(int testStepCount=1;testStepCount<=listofTestStep.size();testStepCount++)
						 {
							//Verification of copied test name with the actual test step name in other test pass
							APP_LOGS.debug("Verfication of test step name is matched with the actual test step name");		
							String actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
							
							//Verification of copied test case name  with the actual test case name in other test pass
							String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
						
							String actualExpectedResult = getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_ExpectedResultXpath2", testStepCount).getText();
							
							APP_LOGS.debug("Comparing the name of test step with the actual test step and  test case name with the actual test case name");
							if(compareStrings(EditedTestStepName, actualTestStepName)&&compareStrings(TestCaseName2, actualTestCaseName)&&compareStrings(testStepExpectedResult2, actualExpectedResult))
							{
								APP_LOGS.debug("Name of Edited Test Step, Test Case and expected result Updated Successfully");
								comments=comments+"Name of Edited Test Step, Test Case and expected result Updated Successfully. (PASS) ";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Unable to Click On Edit Icon as Name of Test Step Is not Matched");
								comments=comments+"Unable to find updated Test Step  (fail)   ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unable to find updated Test Step");
							}
							
							getObject("TestStepViewAll_editProjectIcon1", "TestStepViewAll_TestStepEditIconXpath2", testStepCount).click();
							
							if(verifyTestStepFields(EditedTestStepName, testStepExpectedResult2, TestCaseName2, "Standard"))
							{
								APP_LOGS.debug("All details of edited test step matched");
								comments+="All details of edited test step matched (PASS) ";
							}else
							{
								fail=true;
								APP_LOGS.debug("All details of edited test step did not match");
								comments=comments+"All details of edited test step did not match (fail)   ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All details of edited test step did not match");
							}
						 }
					}
					else 
					{
						fail= true;
						APP_LOGS.debug("Test Manager Login Not Successful");					
					}
				}
				else
				{
					fail= true;
					APP_LOGS.debug("Version Lead Login Not Successful");
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				assertTrue(false);
				fail = true;
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception while editing test step");
				comments+="Skip Exception or other exception occured while editing test step (FAIL) " ;
			}
			
			closeBrowser();
			
		}
		else
		{
			APP_LOGS.debug(Role +" could not login successfully");
		}
	}
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		

	}
	@AfterTest
	public void reportTestResult() throws Exception
	{
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
								
					

