/* Author Name:-Aishwarya Deshpande
 * Created Date:-8th Dec 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Tooltip Messages on Reports Page with the positive data  
 */

package com.uat.suite.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class VerifyTooltipMessage extends TestSuiteBase{
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
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
		APP_LOGS.debug(" Executing VerifyTooltipMessage Test Case");

		if(!TestUtil.isTestCaseRunnable(ReportsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ReportsSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyTooltipMessage(String Role, String GroupName, String	Portfolio, String ProjectName, String Version, 
			String projectDescription, String endMonth, String endYear, String endDate, String VersionLead, String testPassName, 
			String testManager, String testerName, String testerRole, String testCaseName, String testStepName, String assignedRole, 
			String expectedResult,String actualResult, String ExpectedReportsTabMessage, String ExpectedGoButtonMessage, String expectedExportButtonMessage) 
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
					
			 if(!createProject(GroupName, Portfolio, ProjectName, Version, projectDescription, endMonth, endYear, endDate, versionLead.get(0).username))
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
			 comments+="Data has been made Successfully from Test Management tab... ";
			
			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
			
 	         APP_LOGS.debug("Opening Browser... ");
  			 openBrowser();
				
			 if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			 {
	            APP_LOGS.debug("Logged in browser with Test Manager");
	            try
				{	   
	            	//Verifying Tooltip Message on Hovering on Reports Tab
	         
	       /*  	    builder = new Actions((driver));
					WebElement ReportTab =getElement("UAT_reports_Id");
					builder.moveToElement(ReportTab).build().perform();
					//Thread.sleep(2000);
					String actualReportsTabMessage = (String)eventfiringdriver.executeScript("return $('.tipMid').text()");
				//	String actualReportsTabMessage = (String) (eventfiringdriver.executeScript("return $('."+OR.getProperty("DetailedAnalysis_ExtractTooltipMessage_Class")+"').text()"));
			    //  String actualReportsTabMessage=getElementByClassAttr("DetailedAnalysis_ExtractTooltipMessage_Class").getText();
			    //  String actualReportsTabMessage=getObject("DetailedAnalysis_ExtractTooltipMessage").getText();
					if(compareStrings(ExpectedReportsTabMessage, actualReportsTabMessage))
					{
					 	APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is verified");						 
					 	comments+="Export Test Case Statistics Button Tooltip Message is verified(Pass).";
					}
					else{
						fail = true;
						APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is not matching");
						comments+="Export Test Case Statistics Button Tooltip Message not matching(Fail).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportButtonMessageNotMatching");
				    }
	            */
	            	
	                //Clicking on Reports Tab
	                getElement("UAT_reports_Id").click();
				    APP_LOGS.debug("Reports tab clicked ");
				    Thread.sleep(2000);
				
				    //Verifying Tooltip Message on Hovering on Go button
				    String actualGoButtonMessage=HoverElement("Report_GoButton");
				    verifyHoveredText(ExpectedGoButtonMessage, actualGoButtonMessage, "Go Button");
					
					//Verifying Tooltip Message on Hovering on Export button
				    String actualExportButtonMessage=HoverElement("Report_ExportButton");
				    verifyHoveredText(expectedExportButtonMessage, actualExportButtonMessage, "Export Button");
					
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
					for(int i=0;i<=listOfTestPass.size();i++)
					{	
						String actualOption=listOfTestPass.get(i).getAttribute("title");
						if(actualOption.equalsIgnoreCase(testPassName))
						{
							listOfTestPass.get(i).click();
							APP_LOGS.debug("Test Pass is selected");
							break;
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
					for(int i=0;i<listOfTesterRole.size();i++){
						String testerRoleResult=listOfTesterRole.get(i).getAttribute("title");
						if(testerRoleResult.equalsIgnoreCase(testerRole)){
							listOfTesterRole.get(i).click();
							APP_LOGS.debug("Tester role is selected");
							break;
						}
					}
					
					getObject("Report_GoButton").click();
					
					//Verification of Project Description Tooltip Message
					builder = new Actions((driver));
					WebElement ProjectDesc =getElement("Triage_ExtractProjectDescription");
					String actualPDMessage = ProjectDesc.getAttribute("title");
					builder.moveToElement(ProjectDesc).build().perform();
					
					verifyHoveredText(projectDescription, actualPDMessage, "Project Description");
							
					//Verification of Test Step Details on Hovering
					List<WebElement> listOfTestSteps=getObject("Traige_testStepGrid").findElements(By.tagName("tr"));
					for(int i=1;i<=listOfTestSteps.size();i++)
					{
						String actualTCName=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPName2", i);
						String actualTSName=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPDescription2", i);
						String actualRole=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TestManger2", i);
						String actualExpectedResult=HoverElement("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPEndDate2", i);
						String actualResultDisplayed=HoverElement("Triage_failedTestStepNameXpath1", "Reports_testPassGrid_PassedTS", i);
						String actualTester=HoverElement("Triage_failedTestStepNameXpath1", "Reports_testPassGrid_FailedTS", i);
						
						verifyHoveredText(testCaseName, actualTCName, "TestCase");
						verifyHoveredText(testStepName, actualTSName, "TestStep");
						verifyHoveredText(testerRole, actualRole, "Tester Role");
						verifyHoveredText(expectedResult, actualExpectedResult, "Expected Result");
						verifyHoveredText(actualResult, actualResultDisplayed, "Actual Result");
						
						String expectedTester=tester.get(0).username.replace(".", " ");
						if(assertTrue(expectedTester.equalsIgnoreCase(actualTester))){
							APP_LOGS.debug("Tester Tooltip Message is verified");						 
							comments+="Tester Tooltip Message is verified(Pass).";
						}
						else{
							fail = true;
							APP_LOGS.debug("Tester Tooltip Message is not matching");
							comments+="Tester Tooltip Message not matching(Fail).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterMessageNotMatching");
						}
					}
				}
				catch(Throwable t)
	            {
					fail=true;
					assertTrue(false);
					t.printStackTrace();
	           	 	comments+="Exception in Exporting to Excel on Reports page.";
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
	
	private String HoverElement(String hoveredElement)
	{
		builder = new Actions((driver));
		WebElement ElementName =getObject(hoveredElement);
		builder.moveToElement(ElementName).build().perform();
		
		String actualMessage = ElementName.getAttribute("title");
		
		return actualMessage;
	}
	
	private String HoverElement(String hoveredElement1, String hoveredElement2, int i)
	{
		builder = new Actions((driver));
		WebElement ElementName =getObject(hoveredElement1,hoveredElement2, i);
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
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ReportsSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	private boolean createProject(String group, String portfolio, String project, String version, String description,String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");

		getObject("Projects_createNewProjectLink").click();
		try 
		{
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
				dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(version);
				
				getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(description);
				
				selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
				
				getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
				
				Thread.sleep(500);driver.switchTo().frame(1);
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
}
