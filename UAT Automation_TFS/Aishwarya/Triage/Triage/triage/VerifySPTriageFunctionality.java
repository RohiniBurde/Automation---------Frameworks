/* Author Name:-Aishwarya Deshpande
 * Created Date:-28th Nov 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of SharePoint functionality on Triage Page with the positive data  
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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifySPTriageFunctionality extends TestSuiteBase{
	
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
	public void checkTestSkip(){
		APP_LOGS.debug("Executing VerifySPTriageFunctionality Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
	}

	@Test(dataProvider="getTestData")
	public void Test_VerifySPTriageFunctionality(String Role, String GroupName, String Portfolio, String ProjectName, 
				String Version, String endMonth, String endYear,String endDate,String VersionLead, String testPassName, 
				String testManager, String testerName, String testerRole, String testCaseName, String testStepName,
				String assignedRole, String expectedResult, String triageDetails, String Owner, String Status, String Priority, 
				String Severity, String ClosedDate, String ClosedMonth, String ClosedYear, String bugTitle, String BugURL, 
				String expectedTriageSavedSuccessfullyMessage, String expectedNoTestStepMessage)throws Exception
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
						
				// creating test pass,test case,testers and test step
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
					APP_LOGS.debug("Tester not Created Successfully.");
					comments+="Tester not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTesterCreation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester Created Successfully.");

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
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStep1Creation");
					closeBrowser();

					throw new SkipException("Test Step not created successfully");
				}
				APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
				
				comments="Data Created successfully in Test Management tab.";
				APP_LOGS.debug("Data Created successfully in Test Management tab.");
				
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
						
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
			 	APP_LOGS.debug("Closing Browser... ");
			 	closeBrowser();
					
			    APP_LOGS.debug("Opening Browser... ");
			 	openBrowser();
			 	  	
			 	if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			 	{
			 		try
			 		{
					 	    APP_LOGS.debug("Logged in browser with Test Manager");
					 	            
					 	    // clicking on Triage Tab
							getElement("UAT_triage_Id").click();
							APP_LOGS.debug("Triage tab clicked ");
							Thread.sleep(2000);
							
							//Selecting Project
							List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
							selectDDContent(listOfProjectsName,ProjectName , "Project");
							
							//Selecting Version
							List<WebElement> listOfVersions=getElement("Triage_versionDropDown_Id").findElements(By.tagName("option"));
							selectVersion(listOfVersions, Version);
							
							//Selecting Tets Pass 
							List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
							selectDDContent(listOfTestPass, testPassName, "Test Pass");
							
							//Selecting Tester
							List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
							selectTester(listOfTester, tester.get(0).username.replace(".", " "));
							
							//Selecting Tester Role
							List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
							selectDDContent(listOfTesterRole, testerRole, "Tester Role");
				 				
							getElement("Triage_goButton_img").click();
							Thread.sleep(3000);
									
							List<WebElement> listOfTestSteps =getObject("Traige_testStepGrid").findElements(By.tagName("tr"));
							for(int i=1;i<=listOfTestSteps.size();i++)
							{
								String actualtestStepName=	getObject("Triage_failedTestStepNameXpath1", "Triage_failedTestStepNameXpath2", i).getAttribute("title");
								String traigeLinkAvailable=	getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).getText();
										
								if(assertTrue(testStepName.equals(actualtestStepName) && resourceFileConversion.getProperty("Triage_TSGridLinkName").equals(traigeLinkAvailable)))
								{
									APP_LOGS.debug("Failed Test Step Present and clicking on triage link");
									comments+="Failed Test Step Present and clicking on triage link(Pass).";
									getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).click();
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Either Failed Test Step is not Present or triage link not available ");
									comments+="Either Failed Test Step is not Present or triage link not available(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedTSNotMatching");
								}
							}
									
							//Clicking on yes radio button
							getObject("TriagePopupWindow_BugYes").click();
									
							//Entering Triage details
							getElement("TriagePopupWindow_TriageDetails_Id").sendKeys(triageDetails);
									
							//Selecting owner
							getObject("TriagePopupWindow_Owner_PeoplePickerImg").click();
							driver.switchTo().frame(3);
							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
							getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(test_Manager.get(0).username); 
							getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
							getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
							getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
							driver.switchTo().defaultContent();

							//Entering Testing Date
							getElement("TriagePopupWindow_TestingDate_Id").sendKeys("12//10//2014");
									 									
							//Selecting status
							Select statusList =  new Select(getElement("TriagePopupWindow_Status_Id"));
							statusList.selectByVisibleText(Status);
									
							//Selecting Priority
							Select priorityList =  new Select(getElement("TriagePopupWindow_Priority_Id"));
							priorityList.selectByVisibleText(Priority);
									
							//Selecting Severity
							Select severityList =  new Select(getElement("TriagePopupWindow_Severity_Id"));
							severityList.selectByVisibleText(Severity);
									
							//Entering Closed Date
							selectStartOrEndDate(getObject("TriagePopupWindow_CloseDate_img"), ClosedMonth, ClosedYear, ClosedDate);
									
							//Entering Bug Title
							getElement("TriagePopupWindow_BugTitle_Id").sendKeys(bugTitle);
									
							//Entering Bug URL
							getElement("TriagePopupWindow_BugUrl_Id").sendKeys(BugURL);
									
							//clicking on save button
							getObject("Testing_ratingSaveButton").click();
							
							//comparing Triage saved successfully message
							String actualTriageSavedSuccessfullyMessage=getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
							if(compareStrings(expectedTriageSavedSuccessfullyMessage,actualTriageSavedSuccessfullyMessage))
							{
								APP_LOGS.debug("Triage is saved successfully for a failed Test Step with message: " +actualTriageSavedSuccessfullyMessage);
								comments+="Triage is saved successfully for a failed Test Step with message: " +actualTriageSavedSuccessfullyMessage+"(Pass).";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Triage is not saved successfully with message: " +actualTriageSavedSuccessfullyMessage);
								comments+="Triage is not saved successfully with message: " +actualTriageSavedSuccessfullyMessage+"(Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageNotSavedSuccessfully");
							}
									
							getObject("Tester_testeraddedsuccessfullyOkButton").click();    //Click on Save button
									
							//To Verify if Triage link is Changed to Triaged or not
							for(int i=1;i<=listOfTestSteps.size();i++)
							{
								String actualTriagedLink= getObject("Triage_failedTestStepNameXpath1", "Triage_TSGrid_link", i).getText();
								if(compareStrings(resourceFileConversion.getProperty("Triage_TriagedLinkName"), actualTriagedLink))
								{
									APP_LOGS.debug("Triage link changed to Triaged ");
									comments+="Triage link changed to Triaged(Pass). ";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Triage link not changed to Triaged ");
									comments+="Triage link not changed to Triaged(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoTriagedLink");
								}
								String actualTSName= getObject("Triage_failedTestStepNameXpath1","Triage_failedTestStepNameXpath2",i).getText();

								//Selecting Test step To view and verify the details of already logged bug
								if(actualTSName.equalsIgnoreCase(testStepName))
								{
									APP_LOGS.debug("Test step found and clicking on triaged link to verify the entered details");
									comments+="Test step found and clicking on triaged link to verify the entered details.";
									getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).click();
									break;
								}
							}
								
							//Verification of  the details of already logged bug
							//verification for Yes option selected or not
							if(getObject("TriagePopupWindow_BugYes").isSelected())
							{
								APP_LOGS.debug("Yes option is selected");
								comments+="Yes option is selected(Pass).";
							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Yes option is selected for Bug");
								comments+="Yes option is selected for Bug(Pass).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BugConditionNotMatching");
							}
											
							//Verifying Triage details
							String actualTriagedetails=getElement("TriagePopupWindow_TriageDetails_Id").getText();
							verifyBugDetails(triageDetails, actualTriagedetails, "Triage details");
									
							//Verification for owner
							String actualOwner=getElement("TriagePopupWindow_Owner_Id").getText();
							if(assertTrue(actualOwner.equalsIgnoreCase(test_Manager.get(0).username.replace(".", " "))))
							{
								APP_LOGS.debug("Owner Name is matched ");
								comments+="Owner Name is matched(Pass).";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Owner Name is not matching ");
								comments+="Owner Name is not matching(Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "OwnerNotMatching");
							}
									
							//Verifying Testing Date
							String actualTestingDate=getElement("TriagePopupWindow_TestingDate_Id").getAttribute("value");
							String expectedTestingDate="12/10/2014";
							verifyBugDetails(expectedTestingDate, actualTestingDate, "Testing Date");

							//Verifiying Status
							String selectedStatus =statusList.getFirstSelectedOption().getAttribute("value");
							verifyBugDetails(Status, selectedStatus, "Status");
									
							//Verifying Priority
							String selectedPriority=priorityList.getFirstSelectedOption().getAttribute("value");
							verifyBugDetails(Priority, selectedPriority, "Priority");
									
							//Verifying Severity
							String selectedSeverity=severityList.getFirstSelectedOption().getAttribute("value");
							verifyBugDetails(Severity, selectedSeverity, "Severity");
									
							//Verifying Closed Date
							String actualClosedDate=getElement("TriagePopupWindow_CloseDate").getAttribute("value");
							String month=getMonthNumber(ClosedMonth);
							String expectedClosedDate=month+"/"+ClosedDate+"/"+ClosedYear;
							verifyBugDetails(expectedClosedDate, actualClosedDate, "Closed Date");
							
							//Verifying Bug Title
							String actualBugTitle=getElement("TriagePopupWindow_BugTitle_Id").getAttribute("value");
							verifyBugDetails(bugTitle, actualBugTitle, "Bug Title");
							
							//Verifying Bug URL
							String actualBugUrl=getElement("TriagePopupWindow_BugUrl_Id").getAttribute("value");
							verifyBugDetails(BugURL, actualBugUrl, "Bug URL");
									
							getObject("TestPasses_cancleButton").click();   //Clicking on Cancel button
			 		}
			 		catch(Throwable t)
					{
						 fail=true;
						 assertTrue(false);
						 t.printStackTrace();
						 comments="Failed Login for Role" +Role;
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
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
							APP_LOGS.debug("Testing Complete Link has not been clicked using provided details. Test case has been failed.");
							comments+="Fail Occurred:- Testing Complete has not been clicked... ";
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
			        TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
				}
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser... ");
				openBrowser();
	
				if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
				{
			        APP_LOGS.debug("Logged in browser with Test Manager");
								 	            
		            //Clicking on Triage Tab
			        getElement("UAT_triage_Id").click();
					APP_LOGS.debug("Triage tab clicked ");
					Thread.sleep(2000);
												
					List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName, ProjectName, "Project");
								
					List<WebElement> listOfVersions1=getElement("Triage_versionDropDown_Id").findElements(By.tagName("option"));
					selectVersion(listOfVersions1,Version);
												
					List<WebElement> listOfTestPass1=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfTestPass1, testPassName, "Test Pass");
					
					List<WebElement> listOfTester1=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					selectTester(listOfTester1, tester.get(0).username.replace(".", " "));
						
					List<WebElement> listOfTesterRole1=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfTesterRole1, testerRole, "Tester Role");
							 				
					getElement("Triage_goButton_img").click();
	
					//To verify is the test step after making it pass is visible on triage page or not
										
					String actualNoTestStepMessage=getObject("Triage_testStepDetailSection").getText();
					if(compareStrings(expectedNoTestStepMessage, actualNoTestStepMessage))
					{
						APP_LOGS.debug("Test Step is not visible after making it Pass with message: "+actualNoTestStepMessage);
						comments+="Test Step is not visible after making it Pass with message: "+actualNoTestStepMessage+" (Pass).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Test Step is visible after making it Pass with message: "+actualNoTestStepMessage);
						comments+="Test Step is not visible after making it Pass with message: "+actualNoTestStepMessage+" (Fail).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TSVisible");
					}
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Failed Login for Role: Test Manager");
				    comments="Failed Login for Role: Test Manager(Fail)";
				    TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TMLoginFailed");
				}
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
			}
			else 
			{
				APP_LOGS.debug("Login Not Successful for Role: "+Role);	
			}	
				
	}
	
	private void verifyBugDetails(String expectedDetail, String actualDetail, String element)
	{
		if(compareStrings(expectedDetail, actualDetail))
		{
			APP_LOGS.debug(element+" matched ");
			comments+=element+" matched(Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" not matching ");
			comments+=element+" not matching(Fail).";
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
		
	private void selectVersion(List<WebElement> listOfVersions, String selectVersion)
	{
		for(int i=0;i<listOfVersions.size();i++)
		{
			if(listOfVersions.get(i).getText().equals(selectVersion))
			{
				listOfVersions.get(i).click();
				APP_LOGS.debug("Version is selected");
				break;
			}
		}
	}
		
	private void selectTester(List<WebElement> listOfTester, String selectTester)
	{
		for(int i=1;i<listOfTester.size();i++)
		{	
			String testerList=listOfTester.get(i).getText();
			if(testerList.equalsIgnoreCase(selectTester))
			{
				listOfTester.get(i).click();
				APP_LOGS.debug("Tester is selected");
				break;
			}
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
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TriageSuiteXls, "Test Cases", TestUtil.getRowNum(TriageSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}

	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TriageSuiteXls, this.getClass().getSimpleName()) ;
	}

}
