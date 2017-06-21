/* Author Name:-Aishwarya Deshpande
 * Created Date:-28th Nov 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Go button functionality on Triage Page with the positive data  
 */

package com.uat.suite.triage;

import java.io.IOException;
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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyGoButtonFunctionality extends TestSuiteBase{
	
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
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing VerifyGoButtonFunctionality Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
			runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
	}

	@Test(dataProvider="getTestData")
	public void Test_VerifyGoButtonFunctionality(String Role, String GroupName, String Portfolio, 
			String ProjectName1, String projectDescription, String ProjectName2,String ProjectName3, String Version, 
			String endMonth,String endYear, String endDate, String VersionLead, String testPassName1, 
			String TP_description, String testManager, String testPassName2, String testPassName3, String testPassName4, 
			String testPassName5, String testerName, String Role1, String Role2, String Role3, String testCaseName1, 
			String testStepName1,String assignedRole1, String expectedResult, String testCaseName2, String testStepName2,
			String assignedRole2, String expectedNoProjectMessage, String expectedNoTPAvailableMessage, 
			String expectedNoTesterAvailableMessage, String expectedNoTestCaseMessage, String expectedNoTestStepMessage)throws Exception
	{
			count++;
				
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			comments+="";			

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
					 
				if(!createProject(GroupName, Portfolio, ProjectName1, Version, projectDescription,endMonth, endYear, endDate, versionLead.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(ProjectName1+" Project not Created Successfully.");
					comments=ProjectName1+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject1Creation");
					closeBrowser();

					throw new SkipException("Project: "+ProjectName1+" not created successfully");
				}
				APP_LOGS.debug(ProjectName1+" Project Created Successfully.");
						
				if(!createProject(GroupName, Portfolio, ProjectName2, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(ProjectName2+" Project not Created Successfully.");
					comments=ProjectName2+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject2Creation");
					closeBrowser();

					throw new SkipException("Project: "+ProjectName2+" not created successfully");
				}
				APP_LOGS.debug(ProjectName2+" Project Created Successfully.");

				if(!createProject(GroupName, Portfolio, ProjectName3, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(ProjectName3+" Project not Created Successfully.");
					comments=ProjectName3+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject3Creation");
					closeBrowser();

					throw new SkipException("Project: "+ProjectName3+" not created successfully");
				}
				APP_LOGS.debug(ProjectName3+" Project Created Successfully.");
						
				//Extracting the Project ID with a Test Pass with a failed test step
				String actualProjectID=searchProjectAndExtractID(ProjectName1, Version);
				String expectedProjectID="(#ID: "+actualProjectID+")";
						
				if(!createTestPass(GroupName, Portfolio, ProjectName1, Version, testPassName1,TP_description, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully.");
					comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass1Creation");
					closeBrowser();

					throw new SkipException("Test Pass: "+testPassName1+" not created successfully");
				}
				APP_LOGS.debug(testPassName1+" Test Pass Created Successfully.");
						
				if(!createTestPass(GroupName, Portfolio, ProjectName1, Version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully.");
					comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass2Creation");
					closeBrowser();

					throw new SkipException("Test Pass: "+testPassName2+" not created successfully");
				}
				APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");

				if(!createTestPass(GroupName, Portfolio, ProjectName3, Version, testPassName3, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName3+" Test Pass not Created Successfully.");
					comments+=testPassName3+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass3Creation");
					closeBrowser();

					throw new SkipException("Test Pass: "+testPassName3+" not created successfully");
				}
				APP_LOGS.debug(testPassName3+" Test Pass Created Successfully.");
						
				if(!createTestPass(GroupName, Portfolio, ProjectName3, Version, testPassName4, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName4+" Test Pass not Created Successfully.");
					comments+=testPassName4+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass4Creation");
					closeBrowser();

					throw new SkipException("Test Pass: "+testPassName4+" not created successfully");
				}
				APP_LOGS.debug(testPassName4+" Test Pass Created Successfully.");
				
				if(!createTestPass(GroupName, Portfolio, ProjectName3, Version, testPassName5, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName5+" Test Pass not Created Successfully.");
					comments+=testPassName5+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass5Creation");
					closeBrowser();

					throw new SkipException("Test Pass: "+testPassName5+" not created successfully");
				}
				APP_LOGS.debug(testPassName5+" Test Pass Created Successfully.");
						
				//Extracting the Test Pass Id which has a failed test step
				String expectedTestPassID=searchTPAndExtractID(GroupName, Portfolio, ProjectName1, Version, testPassName1);  
						
				if(!createTester(GroupName, Portfolio, ProjectName1, Version, testPassName1, tester.get(0).username, Role1, Role1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester with role "+Role1+"not Created Successfully.");
					comments+="Tester with role "+Role1+" not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester with role "+Role1+" Created Successfully.");

				if(!createTester(GroupName, Portfolio, ProjectName1, Version, testPassName2, tester.get(1).username, Role2, Role2))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester with role "+Role2+"not Created Successfully.");
					comments+="Tester with role "+Role2+" not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester2Creation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester with role "+Role2+" Created Successfully.");
						
				if(!createTester(GroupName, Portfolio, ProjectName3, Version, testPassName4, tester.get(0).username, Role1, Role1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester with role "+Role1+"not Created Successfully.");
					comments+="Tester with role "+Role1+" not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester with role "+Role1+" Created Successfully.");
				
				if(!createTester(GroupName, Portfolio, ProjectName3, Version, testPassName5, tester.get(2).username, Role3, Role3))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester with role "+Role1+"not Created Successfully.");
					comments+="Tester with role "+Role1+" not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester with role "+Role1+" Created Successfully.");
						
				if(!createTestCase(GroupName, Portfolio, ProjectName1, Version, testPassName1, testCaseName1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testCaseName1+" Test Case not Created Successfully.");
					comments+=testCaseName1+" Test Case not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCase1Creation");
					closeBrowser();

					throw new SkipException("Test Case:"+testCaseName1+" of Test Pass:"+testPassName1+" not created successfully");
				}
				APP_LOGS.debug(testCaseName1+" Test Case Created Successfully.");

				if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStepName1, expectedResult, testCaseName1, assignedRole1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testStepName1+" Test Step not Created Successfully.");
					comments+=testStepName1+" Test Step not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStep1Creation");
					closeBrowser();

					throw new SkipException("Test Step:"+testStepName1+" not created successfully");
				}
				APP_LOGS.debug(testStepName1+" Test Step Created Successfully.");
						
				if(!createTestCase(GroupName, Portfolio, ProjectName3, Version, testPassName4, testCaseName1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testCaseName1+" Test Case not Created Successfully.");
					comments+=testCaseName1+" Test Case not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCase1Creation");
					closeBrowser();

					throw new SkipException("Test Case:"+testCaseName1+" of Test Pass:"+testPassName4+" not created successfully");
				}
				APP_LOGS.debug(testCaseName1+" Test Case Created Successfully.");
				
				if(!createTestCase(GroupName, Portfolio, ProjectName3, Version, testPassName5, testCaseName2))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testCaseName2+" Test Case not Created Successfully.");
					comments+=testCaseName2+" Test Case not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCase2Creation");
					closeBrowser();

					throw new SkipException("Test Case:"+testCaseName2+" of Test Pass:"+testPassName5+" not created successfully");
				}
				APP_LOGS.debug(testCaseName2+" Test Case Created Successfully.");
				
				if(!createTestStep(GroupName, Portfolio, ProjectName3, Version, testPassName5, testStepName2, expectedResult, testCaseName2, assignedRole2))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testStepName2+" Test Step not Created Successfully.");
					comments+=testStepName2+" Test Step not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStep2Creation");
					closeBrowser();

					throw new SkipException("Test Step:"+testStepName2+" not created successfully");
				}
				APP_LOGS.debug(testStepName2+" Test Step Created Successfully.");
				
				APP_LOGS.debug("Data created successfully in Test Management tab..");
				comments="Data created successfully in Test Management tab..";

				APP_LOGS.debug("Closing Browser");
				closeBrowser();
		 			
		        APP_LOGS.debug("Opening Browser... ");
		 		openBrowser();
		 				
		 		if(login(tester.get(0).username,tester.get(0).password, "Tester"))
				{
		            APP_LOGS.debug("Logged in browser with Tester ");
		            
		            try
		            {
		 	        	if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName1, Version, testPassName1)) 
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
						fail = true;
						APP_LOGS.debug("Exception occurred: Testing the Test Management data from dashboard page. Test Case has been failed.");
						comments += "Exception occurred: While Testing the Test Management data from dashboard page.";
						closeBrowser();
						
						throw new SkipException("Exception occurred: While Testing the Test Management data from dashboard page.... So Skipping all tests");
					}
				}
			 	else
			 	{
					fail=true;
					APP_LOGS.debug("Failed Login for Role: Tester");
		           	comments="Failed Login for Role: Tester(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
				}
			    APP_LOGS.debug("Closing Browser");
		 		closeBrowser();
		 		
				APP_LOGS.debug("Opening Browser... ");
		 		openBrowser();
		 				
		 		if(login(tester.get(2).username,tester.get(2).password, "Tester"))
				{
		            try
		            {
		            	if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName3, Version, testPassName5)) 
						{
							fail = true;
							APP_LOGS.debug("Begin Testing has not been clicked using provided details. Test case has been failed.");
							comments+="Fail Occurred:- Begin Testing has not been clicked... ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Begin Testing has not been clicked");
								
							throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
						}
			 	        APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");
						clickOnPASSRadioButtonAndSave();
						Thread.sleep(1000);
							
						APP_LOGS.debug("Clicking on Some what Satisfied Radio Button");
						getObject("Testing_ratingSomewhatsatisfied").click();
						Thread.sleep(500);
							
						APP_LOGS.debug("Clicking on Save button of Rating popup");
						getObject("Testing_ratingSaveButton").click();
						Thread.sleep(500);
		 	       
		            }
		            catch (Throwable t) 
					{
						fail = true;
						APP_LOGS.debug("Exception occurred: Testing the Test Management data from dashboard page. Test Case has been failed.");
						comments += "Exception occurred: While Testing the Test Management data from dashboard page.";
						closeBrowser();
						
						throw new SkipException("Exception occurred: While Testing the Test Management data from dashboard page.... So Skipping all tests");
					}
				}
			 	else
			 	{
					fail=true;
					APP_LOGS.debug("Failed Login for Role: Tester");
		           	comments="Failed Login for Role: Tester(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"Tester2LoginFailed");
				}
			    APP_LOGS.debug("Closing Browser");
		 		closeBrowser();
			    
				APP_LOGS.debug("Opening Browser... ");
		 		openBrowser();
		 				
		 		if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
		 		{
			 	    APP_LOGS.debug("Logged in browser with Version Lead");
			 	            
			 	    //Clicking on Triage Tab
					getElement("UAT_triage_Id").click();
					APP_LOGS.debug("Triage tab clicked ");
					Thread.sleep(2000);
			 				
		 			try
		 			{
		 				//Verifying No Project Selected Message
		 				getElement("Triage_goButton_img").click();
						String actualNoProjectMessage=getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
						verifyPopUpMessage(expectedNoProjectMessage, actualNoProjectMessage,"No Project selected");
						
						//Verifying for no Test Pass available
						List<WebElement> listOfProject=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfProject, ProjectName2, "Project");
						APP_LOGS.debug("Selected project"+ProjectName2+" as it has no Test Pass");
					
						Select listOfVersions =  new Select(getElement("Triage_versionDropDown_Id"));
						listOfVersions.selectByVisibleText(Version);
							
						getElement("Triage_goButton_img").click();
						
						String actualNoTPAvailableMessage=getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
						verifyPopUpMessage(expectedNoTPAvailableMessage, actualNoTPAvailableMessage, "No Test Pass available");
						
						//Verifying No Tester available popup message
						selectDDContent(listOfProject, ProjectName3, "Project");
						APP_LOGS.debug("Selected project"+ProjectName3+" as it has no Tester");
							
						listOfVersions.selectByVisibleText(Version);    //Select version
						
						List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTestPass, testPassName3, "Test Pass");
						getElement("Triage_goButton_img").click();
							
						String actualNoTesterAvailableMessage=getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
						verifyPopUpMessage(expectedNoTesterAvailableMessage, actualNoTesterAvailableMessage,"No Tester available");
							
						//Verifying No Test Case available message
						selectDDContent(listOfProject, ProjectName1, "Project");
						APP_LOGS.debug("Selected project"+ProjectName1+" as it has no Test Case");
							
						listOfVersions.selectByVisibleText(Version);    //Select version
							
						List<WebElement> listOfTestPass1=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTestPass1, testPassName2, "Test Pass");
							
						List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
						selectTester(listOfTester,tester.get(1).username.replace(".", " ") );
							
						List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTesterRole, Role2, "Tester Role");
						
						getElement("Triage_goButton_img").click();
							
						String actualNoTestCaseMessage=getObject("Triage_testStepDetailSection").getText();
						verifyText(expectedNoTestCaseMessage,actualNoTestCaseMessage , "No Test case available");
						
							
						//Verifying No Test Step available message
						selectDDContent(listOfProject, ProjectName3, "Project");
						APP_LOGS.debug("Selected project"+ProjectName3+" as it has no Test Step");
							
						listOfVersions.selectByVisibleText(Version);    //Select version
						
						List<WebElement> listOfTestPass2=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTestPass2, testPassName4, "Test Pass");
							
						List<WebElement> listOfTester1=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
						selectTester(listOfTester1, tester.get(0).username.replace(".", " "));
							
						List<WebElement> listOfTesterRole1=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTesterRole1, Role1, "Tester Role");
							
						getElement("Triage_goButton_img").click();
						String actualNoTestStepMessage=getObject("Triage_testStepDetailSection").getText();
						verifyText(expectedNoTestStepMessage,actualNoTestStepMessage , "No Test Step available");
						
						//Verifying No Failed Test Step available message
						selectDDContent(listOfProject, ProjectName3, "Project");
						APP_LOGS.debug("Selected project"+ProjectName3+" as it has no Test Step");
							
						listOfVersions.selectByVisibleText(Version);    //Select version
						
						List<WebElement> listOfTestPass3=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTestPass3, testPassName5, "Test Pass");
							
						List<WebElement> listOfTester2=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
						selectTester(listOfTester2, tester.get(2).username.replace(".", " "));
							
						List<WebElement> listOfTesterRole2=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTesterRole2, Role3, "Tester Role");
							
						getElement("Triage_goButton_img").click();
						String actualNoFailedTestStepMessage=getObject("Triage_testStepDetailSection").getText();
						verifyText(expectedNoTestStepMessage,actualNoFailedTestStepMessage , "No Failed Test Step available");	
							
						//Verifying Failed Test Step available message
						selectDDContent(listOfProject, ProjectName1, "Project");
						APP_LOGS.debug("Selected project"+ProjectName1+" as it has a failed Test Step");
							
						listOfVersions.selectByVisibleText(Version);    //Select version
						
						List<WebElement> listOfTestPass4=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTestPass4, testPassName1, "Test Pass");
							
						List<WebElement> listOfTester4=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
						selectTester(listOfTester4, tester.get(0).username.replace(".", " "));
					
						List<WebElement> listOfTesterRole3=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
						selectDDContent(listOfTesterRole3, Role1, "Tester Role");
							
						getElement("Triage_goButton_img").click();
							
						//Verifying Project name Id and Description
						String displayedProjectName=getElement("Triage_ExtractProjectName").getAttribute("title");
						String displayedProjectID=getObject("Triage_ExtractProjectID").getText();
						
						//Verification of Project Id
						verifyText(ProjectName1, displayedProjectName, "Project Name");

						//Verifcation of Project Name
						verifyText(expectedProjectID, displayedProjectID, "Project Id");

						//Verification of Project Description
						String displayedProjectDescription=getElement("Triage_ExtractProjectDescription").getText();
						verifyText(projectDescription, displayedProjectDescription, "Project Description");
						
							
						List<WebElement> listOfTestPasses=getObject("Triage_testPassGrid").findElements(By.tagName("tr"));
						for(int i=1;i<=listOfTestPasses.size();i++)
						{
							String displayedTPID=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPID2", i).getText();
							String displayedTPName=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPName2", i).getText();
							String displayedTPDescription=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPDescription2", i).getText();
							String displayedTestManager=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TestManger2", i).getText();
							String displayedTPEndDate=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPEndDate2", i).getText();
								
							String expectedManager=test_Manager.get(0).username.replace(".", " ");
							String month=getMonthNumber(endMonth);
							String expectedEndDate=month+"-"+endDate+"-"+endYear;
								
							verifyText(expectedTestPassID, displayedTPID, "Test Pass ID");
							
							verifyText(testPassName1, displayedTPName, "Test Pass Name");
							
							verifyText(TP_description, displayedTPDescription, "Test Pass description");
								
							//Verification of Test Manager
							if(assertTrue(expectedManager.equalsIgnoreCase(displayedTestManager)))
							{
								APP_LOGS.debug("Correct Test Manager is displayed");
								comments+="Correct Test Manager is displayed(Pass).";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Correct Test Manager is not displayed ");
								comments+="Correct Test Manager is not displayed (Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DisplayedManagerNotMatching");
							}
								
							//Verification of Test Pass End Date
							verifyText(expectedEndDate, displayedTPEndDate, "Test Pass End Date");
						}

						List<WebElement> listOfTestSteps =getObject("Traige_testStepGrid").findElements(By.tagName("tr"));
						for(int i=1;i<=listOfTestSteps.size();i++)
						{
							String actualtestStepName=	getObject("Triage_failedTestStepNameXpath1", "Triage_failedTestStepNameXpath2", i).getAttribute("title");
							String traigeLinkAvailable=	getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).getText();
								
							if(assertTrue(testStepName1.equals(actualtestStepName) && resourceFileConversion.getProperty("Triage_TSGridLinkName").equals(traigeLinkAvailable)))
							{
								APP_LOGS.debug("Failed Test Step Present and triage link available verified");
								comments+="Failed Test Step Present and triage link available verified(Pass).";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Failed Test Step not Present and triage link not available ");
								comments+="Failed Test Step not Present and triage link not available (Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedTSNotMatching");
							}
						}
				 	}
				 	catch(Throwable t)
			        {
				       fail=true;
				       assertTrue(false);
				       t.printStackTrace();
				       comments="Exception in Go Button Functionality on Triage Page";
				       TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
			        }
		 		}
		 		else
		 		{
	 				fail=true;
					APP_LOGS.debug("Failed Login for Role: Version Lead");
			        comments="Failed Login for Role: Version Lead(Fail).";
			        TestUtil.takeScreenShot(this.getClass().getSimpleName(),"VLLoginFailed");
			        closeBrowser();
		 		}
		 		APP_LOGS.debug("Closing Browser... ");
				closeBrowser();	
			}
			else 
			{
				APP_LOGS.debug("Login Not Successful");
			}	
	}
			
	//common function for creating project
	private boolean createProject(String group, String portfolio, String project, String version, String description,String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");
		//wait.until(ExpectedConditions.presenceOfElementLocated())

		getObject("Projects_createNewProjectLink").click();
		try 
		{
			dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
			dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
			dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );

			getObject("ProjectCreateNew_versionTextField").sendKeys(version);
			//getElement("Version").sendKeys(version);
			getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(description);
						
			selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
			
			getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
			driver.switchTo().frame(1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
			getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			driver.switchTo().defaultContent();
						
			getObject("ProjectCreateNew_projectSaveBtn").click();
			Thread.sleep(2000);
						
			if (getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText().contains("successfully")) 
			{
				getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
				
	}
			
	private boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
			String description, String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2000);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(2000);
		getObject("TestPasses_createNewProjectLink").click();
				
		try 
		{
			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			//getElement("Version").sendKeys(version);
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
			getElement("TestPassCreateNew_descriptionTextField_ID").sendKeys(description);
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
						   
			driver.switchTo().frame(1);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
			getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
			getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
			getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
			driver.switchTo().defaultContent();
						
			selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
						
			getObject("TestPassCreateNew_testPassSaveBtn").click();
			Thread.sleep(2000);							
						
			if (getElement("TestPassCreateNew_testPassSuccessMessageText_Id").getText().contains("successfully")) 
			{
				getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
	}
	
	private void verifyPopUpMessage(String expectedMessage, String actualMessage, String content)
	{
		if(compareStrings(expectedMessage, actualMessage))
		{
			APP_LOGS.debug(content+" message verified");
			comments+=content+" message verified(Pass).";
			getObject("Tester_testeraddedsuccessfullyOkButton").click();
		}
		else
		{
			fail=true;
			APP_LOGS.debug(content+" message doesnot match");
			comments+=content+" message doesnot match(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), content+"MessageNotMatching");
		}
	}
	
	private void verifyText(String expectedText, String actualText, String element)
	{
		if(compareStrings(expectedText, actualText))
		{
			APP_LOGS.debug(element+" verified");
			comments+=element+" verified(Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" doesnot match");
			comments+=element+" doesnot match(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), element+"NotMatching");
		}
	}
	
	private void selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		for(int i=1;i<listOfElements.size();i++)
		{
			String ProjectNames=listOfElements.get(i).getAttribute("title");
			if(ProjectNames.equals(selectOption))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
				break;
			}
		}
	}
		
		
	private void selectTester(List<WebElement> listOfTester, String selectTester)
	{
		for(int i=1;i<=listOfTester.size();i++)
		{
			if(listOfTester.get(i).getText().equalsIgnoreCase(selectTester))
			{
				listOfTester.get(i).click();
				APP_LOGS.debug("Tester is selected");
				break;
			}
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
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TriageSuiteXls, this.getClass().getSimpleName()) ;
	}

}
