/* Author Name:-Aishwarya Deshpande
 * Created Date:-27th Nov 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Contents of Triage Page with the positive data  
 */

package com.uat.suite.triage;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

public class VerifyExportButtonFunctionality extends TestSuiteBase{
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	boolean flag=false;
	int totalPages;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Executing VerifyExportButtonFunctionality Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
			
	@Test(dataProvider="getTestData")
	public void Test_VerifyExportButtonFunctionality(String Role, String GroupName, String Portfolio, 
			String ProjectName, String Version, String endMonth,String endYear, String endDate, String	VersionLead, 
			String testPassName, String testManager, String testerName, String	testerRole, String testCaseName, 
			String testStepName,String assignedRole, String expectedResult)throws Exception
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
			//version lead
			int versionlead_count = Integer.parseInt(VersionLead);
			versionLead=new ArrayList<Credentials>();
			versionLead = getUsers("Version Lead", versionlead_count);
					 
			//TestManager 
			int testManager_count = Integer.parseInt(testManager);
			test_Manager=new ArrayList<Credentials>();
			test_Manager = getUsers("Test Manager", testManager_count);
					 
			//Tester 
			int tester_count = Integer.parseInt(testerName);
			tester=new ArrayList<Credentials>();
			tester = getUsers("Tester", tester_count);
					 
			//click on testManagement tab
			APP_LOGS.debug(" Clicking on Test Management Tab ");
			getElement("UAT_testManagement_Id").click();
			Thread.sleep(1000);
					
			if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
				comments=ProjectName+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			}
			APP_LOGS.debug(ProjectName+" Project Created Successfully.");
					
			if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName+" Test Pass not Created Successfully.");
				comments+=testPassName+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPassCreation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			}
			APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
				
			if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+testerRole+"not Created Successfully.");
				comments+="Tester with role "+testerRole+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
				closeBrowser();

				throw new SkipException("Tester not created successfully");
			}
			APP_LOGS.debug(" Tester with role "+testerRole+" Created Successfully.");
					
			if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
				comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCaseCreation");
				closeBrowser();

				throw new SkipException("Test Case not created successfully");
			}
			APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
				
			if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, expectedResult, testCaseName, assignedRole))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStepName+" Test Step not Created Successfully.");
				comments+=testStepName+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStepCreation");
				closeBrowser();

				throw new SkipException("Test Step not created successfully");
			}
			APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
					
			closeBrowser();
		 	APP_LOGS.debug(" Closed Browser after creating Project,Test Pass,Tester,Test Case,Test Step ");
	 			
	        APP_LOGS.debug("Opening Browser... ");
	 		openBrowser();
	 				
	 		if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			{
	            APP_LOGS.debug("Logged in browser with Tester ");
	            
	            try
	            {
	 	        	if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, testPassName)) 
					{
						fail = true;
						APP_LOGS.debug("Begin Testing has not been clicked using provided details. Test case has been failed.");
						comments+="Fail Occurred:- Begin Testing has not been clicked... ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Begin Testing has not been clicked");
						
						throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
					}
	 	        	APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");
					clickOnFAILRadioButtonAndSave();
					Thread.sleep(1000);
					
					APP_LOGS.debug("Clicking on Some what Satisfied Radio Button");
					getObject("Testing_ratingSomewhatsatisfied").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on Save button of Rating popup");
					getObject("Testing_ratingSaveButton").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
					getObject("Testing_testStepFeedback").click();
							
					Thread.sleep(1000);
	 	       
	            }
	            catch (Throwable t) 
				{
	            	t.printStackTrace();
					fail = true;
					assertTrue(false);
					APP_LOGS.debug("Exception occurred: Testing the Test Management data from dashboard page. Test Case has been failed.");
					comments += "Exception occurred: While Testing the Test Management data from dashboard page.";
				}
			}
		 	else
		 	{
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Tester");
	           	comments="Failed Login for Role: Tester(Fail)";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
			}
			closeBrowser();
		    APP_LOGS.debug("Closed Browser after making the test step fail ");
					
		    APP_LOGS.debug("Opening Browser... ");
	 		openBrowser();
	 				
	 		if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
	 		{
		 	    APP_LOGS.debug("Logged in browser with Test Manager");
		 	        
		 	    // Clicking on Triage Tab
				getElement("UAT_triage_Id").click();
				APP_LOGS.debug("Triage tab clicked ");
				Thread.sleep(2000);
		 	            
		 		try
		 		{
		 			//Selecting Project
		 			List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					for(int i=1;i<=listOfProjectsName.size();i++)
					{
						String listProjects=listOfProjectsName.get(i).getAttribute("title");
						if(listProjects.equalsIgnoreCase(ProjectName))
						{
							listOfProjectsName.get(i).click();
							APP_LOGS.debug("Project is selected");
							break;
						}
					}
						
					//Selecting Version
					Select listOfVersions =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions.selectByVisibleText(Version);    //Select version
						
					//Selecting Test Pass
					List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					for(int i=1;i<listOfTestPass.size();i++)
					{	
						String actualOption=listOfTestPass.get(i).getAttribute("title");
						if(actualOption.equalsIgnoreCase(testPassName))
						{
							listOfTestPass.get(i).click();
							APP_LOGS.debug("Test Pass is selected");
						}
					}
						
					//Selecting Tester
					List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					for(int i=1;i<=listOfTester.size();i++)
					{
						if(listOfTester.get(i).getText().equalsIgnoreCase(tester.get(0).username.replace(".", " ")))
						{
							listOfTester.get(i).click();
							APP_LOGS.debug("Tester is selected");
							break;
						}
					}
						
					//Selecting Tester Role
					List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
					for(int i=0;i<listOfTesterRole.size();i++)
					{
						String testerRoleResult=listOfTesterRole.get(i).getAttribute("title");
						if(testerRoleResult.equalsIgnoreCase(testerRole))
						{
							listOfTesterRole.get(i).click();
							APP_LOGS.debug("Tester Role is selected");
							break;
						}
					}
						
					//Verifying activex control
			        Boolean result = true;
			        try
			        {
			            eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
			            Thread.sleep(2000);
			        }
			        catch(Throwable t)
			        {
			        	result = false;
			        }
			            
			        getElement("Triage_exportButton_img").click();
			        if(result.equals(false))
			        {
			            //Active x code
			            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
			            if(assertTrue(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed()))
			            {
			               getObject("TestStepCreateNew_TestStepActiveX_Close").click();
			               APP_LOGS.debug("ActiveX is disabled and hence cannot export file");
			               comments+="ActiveX is disabled and hence cannot export file";
			               throw new SkipException("ActiveX is disabled and hence cannot export details");
			            }
			            else
			            {	
			               fail=true;
			               APP_LOGS.debug("An alert informing the user of disabled activex should be present.");
			               comments+="An alert informing the user of disabled activex should be present.";
			               TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabled");
			               comments+="Fail- Activex is disabled..but the popup not displayed";
			            }
			        }
			        else
			        {
			           APP_LOGS.debug("Details exported successfully");
			           comments+="Details exported successfully";
			        }
		 		}
		 		catch(Throwable t)
		        {
		           	t.printStackTrace();
		        }
	 		}
	 		else
			{
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Test Manager");
			    comments="Failed Login for Role: Test Manager(Fail)";
			    TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TMLoginFailed");
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
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TriageSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TriageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}

	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
			
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TriageSuiteXls, this.getClass().getSimpleName()) ;
	}
}
