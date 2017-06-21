/* Author Name:-Aishwarya Deshpande
 * Created Date:-12th Dec 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Tooltip Messages on Triage Page with the positive data  
 */

package com.uat.suite.triage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class VerifyTriageTooltipText extends TestSuiteBase{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	Actions builder;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Executing VerifyTriageTooltipText Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyTriageTooltipText(String Role, String GroupName, String Portfolio, String ProjectName,
			String Version, String endMonth, String	endYear, String endDate, String VersionLead, String testPassName,
			String testManager, String	testerName, String testerRole, String testCaseName, String testStepName, 
			String assignedRole, String expectedResult, String actualResult, String expectedTriageTabMessage,
			String expectedGoButtonMessage, String expectedExportButtonMessage, String bugTitle) 
			throws Exception
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
			
			APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
			comments+="Data has been made Successfully from Test Management tab.";
			
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			openBrowser();
			
			if(login(tester.get(0).username, tester.get(0).password, "Tester"))
			{
	            APP_LOGS.debug("Logged in browser with Tester ");
	            
	            try
	            {	
	 	        	if(!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, testPassName)) 
					{
						fail = true;
						APP_LOGS.debug("Begin Testing has not been clicked using provided details. Test case has been failed.");
						comments+="Fail Occurred:- Begin Testing has not been clicked... ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Begin Testing has not been clicked");
						
						throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
					}
	 	        	
	 	        	//Entering actual Result
	            	getElement("Testing_actualResult_Id").sendKeys(actualResult);
	            	
	 	        	APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");
					clickOnFAILRadioButtonAndSave();
					
					APP_LOGS.debug("Clicking on very Satisfied Radio Button");
					getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on Save button of Rating popup");
					getObject("TestingPageRatingPopup_saveButton").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
					getObject("TestingPage_returnToHomeButton").click();
							
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
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			openBrowser();
			
			if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			{
	            APP_LOGS.debug("Logged in browser with Test Manager");
	            try
				{
	            	/*//Verifying Tooltip Message on Hovering on Triage Tab
	            	builder = new Actions((driver));
	     			WebElement TriageTab =getElement("UAT_triage_Id");
	     			builder.moveToElement(TriageTab).build().perform();
	     			//Thread.sleep(2000);
	     			String actualTriageTabMessage=getObject("DetailedAnalysis_ExtractTooltipMessage").getText();
					if(compareStrings(expectedTriageTabMessage, actualTriageTabMessage))
					{
					 	APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is verified");						 
					 	comments+="Export Test Case Statistics Button Tooltip Message is verified(Pass).";
					}
					else{
						fail = true;
						APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is not matching");
						comments+="Export Test Case Statistics Button Tooltip Message not matching(Fail).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportButtonMessageNotMatching");
				    }*/
					
					// Clicking on Triage Tab
	                getElement("UAT_triage_Id").click();
				    APP_LOGS.debug("Triage tab clicked ");
				    Thread.sleep(2000);
				
				    //Verifying Tooltip Message on Hovering on Go button
				    String actualGoButtonMessage=HoverElement("Triage_goButton_img");
				    verifyHoveredText(expectedGoButtonMessage, actualGoButtonMessage, "Go Button");
					
					//Verifying Tooltip Message on Hovering on Export button
				    String actualExportButtonMessage=HoverElement("Triage_exportButton_img");
				    verifyHoveredText(expectedExportButtonMessage, actualExportButtonMessage, "Export Button");
					
				    //Selecting Project
				    List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName, ProjectName, "Project");
				
					//Selecting Version
					List<WebElement> listOfVersions=getElement("Triage_versionDropDown_Id").findElements(By.tagName("option"));
					String actualVersionArray[]= new String[listOfVersions.size()];
					for(int i=0;i<listOfVersions.size();i++)
					{	
						actualVersionArray[i]=listOfVersions.get(i).getText();
						if(listOfVersions.get(i).getText().equals(Version))
						{
							listOfVersions.get(i).click();
							APP_LOGS.debug("Version is selected");
							break;
						}
					}
					
					//Selecting Test Pass
					List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfTestPass, testPassName, "Test Pass");
	     			
					//Selecting Tester
					List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					for(int i=1;i<listOfTester.size();i++)
					{	
						String actualTester=listOfTester.get(i).getText();
						if(actualTester.equalsIgnoreCase(tester.get(0).username.replace(".", " ")))
						{
							listOfTester.get(i).click();
							APP_LOGS.debug("Tester is selected");
						}
					}
					
					//Selecting Tester Role
					List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfTesterRole,testerRole,"Tester Role");
					
					//Clicking on Go button
					getElement("Triage_goButton_img").click();
					
					//Verification of Project Name Tooltip Message
					builder = new Actions((driver));
					WebElement Project =getElement("Triage_ExtractProjectName");
					builder.moveToElement(Project).build().perform();
					
					String expectedProjectName="Project -"+ProjectName;
					int indexOfID = getElement("Triage_ExtractProjectName").getText().indexOf("(#ID");
					String displayedProjectName = Project.getText().substring(0, indexOfID);

					verifyHoveredText(expectedProjectName, displayedProjectName, "Project Name");
					
					//Verification of Test Step Details on Hovering
					List<WebElement> listOfTestSteps=getObject("Traige_testStepGrid").findElements(By.tagName("tr"));
					for(int i=1;i<=listOfTestSteps.size();i++)
					{	
						String actualtestStepName=	getObject("Triage_failedTestStepNameXpath1", "Triage_failedTestStepNameXpath2", i).getAttribute("title");
						String traigeLinkAvailable=	getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).getText();
								
						if(assertTrue(testStepName.equals(actualtestStepName) && resourceFileConversion.getProperty("Triage_TSGridLinkName").equals(traigeLinkAvailable)))
						{
							APP_LOGS.debug("Failed Test Step Present and clicking on triage link");
							comments+="Failed Test Step Present and clicking on triage link";
							getObject("Triage_failedTestStepNameXpath1","Triage_TSGrid_link",i).click();
							
							//Clicking on yes radio button
							getObject("TriagePopupWindow_BugYes").click();
							
							//Entering Bug Title
							getElement("TriagePopupWindow_BugTitle_Id").sendKeys(bugTitle);
							
							//clicking on save button
							getObject("Testing_ratingSaveButton").click();
							
							getObject("Tester_testeraddedsuccessfullyOkButton").click();    //Click on Save button
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Either Failed Test Step is not Present or triage link not available ");
							comments+="Either Failed Test Step is not Present or triage link not available(Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedTSNotMatching");
						}
					}
					
					//Verifying Tootltip Text on Hover on Test Step Grid rows 
					for(int i=1;i<=listOfTestSteps.size();i++)
					{
						String displayedTSName=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPDescription2", i);
						String displayedRole=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TestManger2", i);
						String displayedExpectedResult=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPEndDate2", i);
						String displayedActualResult=HoverElement("Triage_failedTestStepNameXpath1", "Reports_testPassGrid_PassedTS", i);
						String displayedBugTitle=HoverElement("Triage_failedTestStepNameXpath1", "Triage_ExtractBugTitle2", i);
						
						verifyHoveredText(testStepName, displayedTSName, "TestStep");
						verifyHoveredText(testerRole, displayedRole, "Tester Role");
						verifyHoveredText(expectedResult, displayedExpectedResult, "Expected Result");
						verifyHoveredText(actualResult, displayedActualResult, "Actual Result");
						verifyHoveredText(bugTitle, displayedBugTitle, "Bug Title");
					}
				}
	            catch(Throwable t)
	            {
	 			 fail=true;
	 			 assertTrue(false);
	 			 t.printStackTrace();
	           	 comments+="Exception in Verifying Tooltip Text on Triage Page.";
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
	
	private String HoverElement(String hoveredElement) throws IOException{
		builder = new Actions((driver));
		WebElement ElementName =getElement(hoveredElement);
		builder.moveToElement(ElementName).build().perform();
		
		String actualMessage = ElementName.getAttribute("title");
		
		return actualMessage;
	}
	
	private void verifyHoveredText(String expectedMessage,String actualMessage, String element)
	{    
		if(compareStrings(expectedMessage,actualMessage))
		{
			APP_LOGS.debug(element+" Tooltip Message is verified");						 
			comments+=element+" Tooltip Message is verified(Pass).";
		}
		else{
			fail = true;
			APP_LOGS.debug(element+" Tooltip Message is not matching");
			comments+=element+" Tooltip Message not matching(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), element+"MessageNotMatching");
		}
	}
	
	private void selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		String actualArray[] = new String[listOfElements.size()-1];
		for(int i=1;i<listOfElements.size();i++)
		{
			actualArray[i-1]=listOfElements.get(i).getAttribute("title");
			if(actualArray[i-1].equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}	
	}
	
	private String HoverElement(String hoveredElement1, String hoveredElement2, int i){
		builder = new Actions((driver));
		WebElement ElementName =getObject(hoveredElement1,hoveredElement2, i);
		builder.moveToElement(ElementName).build().perform();
		
		String actualMessage = ElementName.getAttribute("title");
		
		return actualMessage;
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
