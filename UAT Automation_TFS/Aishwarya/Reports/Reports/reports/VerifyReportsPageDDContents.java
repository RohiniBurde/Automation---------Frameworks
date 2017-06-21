/* Author Name:-Aishwarya Deshpande
 * Created Date:-3rd Dec 2014
 * Last Modified on Date:-19th Dec 2014
 * Module Description:- Verification of Drop down contents on Reports Page with the positive data  
 */
 
package com.uat.suite.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class VerifyReportsPageDDContents extends TestSuiteBase{
	
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
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug(" Executing VerifyReportsPageDDContents Test Case");

		if(!TestUtil.isTestCaseRunnable(ReportsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ReportsSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyReportsPageDDContents(String Role, String GroupName, String Portfolio, String	ProjectName,
			String Version, String endMonth, String	endYear, String	endDate, String VersionLead, String	testPassName1,
			String testManager, String testPassName2, String testerName, String Role1, String Role2, String testCaseName,
			String testStepName, String assignedRole, String expectedResult) throws Exception
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
				APP_LOGS.debug(ProjectName+" Project not Created Successfully with version:"+Version+".");
				comments=ProjectName+" Project not Created Successfully with version:"+Version+" (Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName+" Project Created Successfully with version:"+Version+".");
			
			 // creating test pass,test case,testers and test step
			 if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully.");
				comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass1Creation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName1+" Test Pass Created Successfully.");
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully.");
				comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass2Creation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");
			
			 if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName1, tester.get(0).username, Role1, Role1))
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

			 if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName1, tester.get(1).username, Role2, Role2))
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

			 if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName1, testCaseName))
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

			 if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName1, testStepName, expectedResult, testCaseName, assignedRole))
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
			
			 APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
			 comments+="Data has been made Successfully from Test Management tab... ";

			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
			
 	         APP_LOGS.debug("Opening Browser... ");
			 openBrowser();

			 try
			 {
				 if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
				 {
				    APP_LOGS.debug("Logged in browser with Test Manager");
	 	            
	 	            // clicking on Triage Tab
					getElement("UAT_reports_Id").click();
					APP_LOGS.debug("Reports tab clicked ");
					Thread.sleep(2000);
					
					//Verify the drop downs,button and default values of DD when logged in as Test Manager
					APP_LOGS.debug("Verifying the drop downs,button and default values of DD when logged in as Test Manager");
					verifyDropDown();
					
					//Verification of Drop Down Contents
					
					//Project
					List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName, ProjectName, "Project");
	
					//Version
					List<WebElement> listOfVersions=getElement("Triage_versionDropDown_Id").findElements(By.tagName("option"));
					String actualVersionArray[]= new String[listOfVersions.size()];
					for(int i=0;i<listOfVersions.size();i++)
					{	
						actualVersionArray[i]=listOfVersions.get(i).getText();
						if(listOfVersions.get(i).getText().equals(Version))
						{
							listOfVersions.get(i).click();
							APP_LOGS.debug("Version is selected");
							comments+="Version is selected";
							break;
						}
					}
					String expectedVersionArray[] = {Version};
					DDContentVerifiaction(expectedVersionArray, actualVersionArray, "Version");
	
					//Test Pass
					List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					String firstTestPassOption=listOfTestPass.get(0).getText();
					
					String actualtestPassArray[]=selectDDContent(listOfTestPass, testPassName1, "Test Pass");
					
					//Verification of first Test Pass option
					verifyFirstDDOption("Reports_firstTPOption", firstTestPassOption);
					String expectedtestPassArray[] = {testPassName1, testPassName2};
					DDContentVerifiaction(expectedtestPassArray, actualtestPassArray, "Test Pass");
	
					//Tester
					List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					String firstTesterOption=listOfTester.get(0).getText();
					
					String tester1Name=tester.get(0).username.replace(".", " ");
					String tester2Name=tester.get(1).username.replace(".", " ");
					String actualtesterArray[]=selectDDContentByText(listOfTester, tester1Name, "Tester");
					
					//Verification of first Tester option
					verifyFirstDDOption("Reports_firsdTesterOption", firstTesterOption);
					
					String expectedtesterArray[] = {tester1Name,tester2Name };
	
					DDContentVerifiaction(expectedtesterArray, actualtesterArray, "Tester");
					
					//Tester Role
					List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
					String firstTesterRoleOption=listOfTesterRole.get(0).getText();
					
					String testerRoleArray[]=selectDDContent(listOfTesterRole, Role1,"Tester Role");
	
					//Verification of first Tester Role option
					verifyFirstDDOption("Reports_firsdTesterRoleOption", firstTesterRoleOption);
					
					String expectedTesterRoleArray[] = {Role1};
					DDContentVerifiaction(expectedTesterRoleArray, testerRoleArray, "Tester Role");
				
					//Status
					List<WebElement> listOfStatus=getElement("Reports_StatusDropDown_Id").findElements(By.tagName("option"));
					String firstStatusOption=listOfStatus.get(0).getText();
					String statusArray[]=selectDDContentByText(listOfStatus, "Pass", "Status");				
					
					//Verification of first Tester Role option
					verifyFirstDDOption("Reports_firsdStatusOption", firstStatusOption);
					
					String expectedStatusArray[] = {"Pass", "Fail", "Not Completed" };
					DDContentVerifiaction(expectedStatusArray, statusArray, "Status");
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Failed Login for Role: Test Manager");
		           	comments="Failed Login for Role: Test Manager(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TMLoginFailed");
				}
				APP_LOGS.debug("Closing browser");
		 	    closeBrowser(); 
				 
	 	        APP_LOGS.debug("Opening Browser... ");
				openBrowser();
				
				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
				{
	 	            APP_LOGS.debug("Logged in browser with Tester");
	 	            
	 	            // clicking on Triage Tab
					getElement("UAT_reports_Id").click();
					APP_LOGS.debug("Reports tab clicked ");
					Thread.sleep(2000);
					
					//Verify the drop downs,button and default values of DD when logged in as Tester
					APP_LOGS.debug("Verifying the drop downs,button and default values of DD when logged in as Tester");
					verifyDropDown();
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Failed Login for Role: Tester");
		           	comments="Failed Login for Role: Tester(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
				}
			}
			catch(Throwable t)
	        {
				fail=true;
				assertTrue(false);
	           	t.printStackTrace();
	           	comments="Exception occured while contents verification(Fail).";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
	        }
			
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();	
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");	
		}	
	}
	
	private void DDContentVerifiaction(String expectedContentArray[], String actualContentArray[], String DDName)
	{
		if(compareIntegers(expectedContentArray.length,actualContentArray.length))
		{
			for(int i=0;i<expectedContentArray.length;i++)
			{
				if(assertTrue(expectedContentArray[i].equalsIgnoreCase(actualContentArray[i])))
				{
					comments+=expectedContentArray[i]+" "+DDName+" present in the dropdown(Pass).";
					APP_LOGS.debug(expectedContentArray[i]+" "+DDName+" present in the dropdown ");
				}
				else
				{
					fail=true;
					comments+=expectedContentArray[i]+" "+DDName+" not present in the dropdown(Fail)";
					APP_LOGS.debug(expectedContentArray[i]+" "+DDName+" not present in the dropdown ");
				}
			}
		}
		else
		{
			fail=true;
			comments+="Total Number of elements in "+DDName+" do not match";
			APP_LOGS.debug("Total Number of elements in "+DDName+" do not match");
		}
	}
	
	private String[] selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		String actualArray[] = new String[listOfElements.size()-1];
		for(int i=1;i<listOfElements.size();i++){
			actualArray[i-1]=listOfElements.get(i).getAttribute("title");
			if(actualArray[i-1].equalsIgnoreCase(selectOption)){
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}	
		return actualArray;
	}
	
	private String[] selectDDContentByText(List<WebElement> listOfElements, String selectOption, String DDName){
		String actualArray[] = new String[listOfElements.size()-1];
		for(int i=1;i<listOfElements.size();i++)
		{	
			actualArray[i-1]=listOfElements.get(i).getText();
			if(actualArray[i-1].equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}
		return actualArray;
	}
	
	private void verifyFirstDDOption(String expectedFirstOption, String actualFirstOption)
	{
		if(compareStrings(resourceFileConversion.getProperty(expectedFirstOption),actualFirstOption))
		{
			comments+=actualFirstOption +" option is present in the dropdown(Pass).";
			APP_LOGS.debug(actualFirstOption+" option is present in the dropdown ");
		}
		else
		{
			fail=true;
			comments+=actualFirstOption+" option is not present in the dropdown(Fail)";
			APP_LOGS.debug(actualFirstOption+" option is not present in the dropdown ");
		}
	}
	
	private void verifyDefaultDDText(String expectedDDValue, String actualDDValue, String DDElement)
	{
		if(compareStrings(resourceFileConversion.getProperty(expectedDDValue), actualDDValue))
		{
			APP_LOGS.debug(DDElement+" Dropdown verified and has default value: "+resourceFileConversion.getProperty(expectedDDValue));
			comments+=DDElement+" Dropdown verified and has default value: "+resourceFileConversion.getProperty(expectedDDValue)+" (Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(DDElement+" Dropdown does not have default value: "+resourceFileConversion.getProperty(expectedDDValue));
			comments+=DDElement+" Dropdown does not have default value: "+resourceFileConversion.getProperty(expectedDDValue)+" (Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), DDElement+"DefaultTextFailed");
		}
	}
	
	private void verifyDropDown() throws IOException
	{	
		//Selecting Project and Verifying default value for Projects Dropdown
		Select projectGroup = new Select(getElement("Triage_projectDropDown_Id"));
		String defaultProjectDDValue= projectGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultProjectDDText", defaultProjectDDValue, "Project");
					
		//Selecting Version and Verifying default value for Projects Dropdown
		Select versionGroup = new Select(getElement("Triage_versionDropDown_Id"));
		String defaultVersionDDValue= versionGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultVersionDDText", defaultVersionDDValue, "Version");
					
		//Selecting Test Pass and Verifying default value for Test Pass Dropdown
		Select testPassGroup = new Select(getElement("Triage_testPassDropDown_Id"));
		String defaultTestPassDDValue= testPassGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultTestPassDDText", defaultTestPassDDValue, "Test Pass");
					
		//Selecting Tester and Verifying default value for Tester Dropdown
		Select TesterGroup = new Select(getElement("Triage_testerDropDown_Id"));
		String defaultTesterDDValue= TesterGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultTesterDDText", defaultTesterDDValue, "Tester");
					
		//Selecting Tester Role and Verifying default value for Tester Role Dropdown
		Select testerRoleGroup = new Select(getElement("Triage_testerRoleDropDown_Id"));
		String defaultTesterRoleDDValue= testerRoleGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultTesterRoleDDText", defaultTesterRoleDDValue, "Tester Role");
		
		//Selecting Tester Role and Verifying default value for Tester Role Dropdown
		Select statusGroup = new Select(getElement("Reports_StatusDropDown_Id"));
		String defaultStatusDDValue= statusGroup.getFirstSelectedOption().getText();
		verifyDefaultDDText("Reports_defaultStatusDDText", defaultStatusDDValue, "Tester Role");
		
		if(getObject("Report_GoButton").isDisplayed())
		{
			APP_LOGS.debug("Go button is visible on Reports Page");
			comments+="Go button is visible on Reports Page(Pass)";
		}
		else
		{
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("Go button is not visible on Reports Page");
			comments+="Go button is not visible on Reports Page(Fail)";
		}
		
		if(getObject("Report_ExportButton").isDisplayed())
		{
			APP_LOGS.debug("Export button is visible on Reports Page");
			comments+="Export button is visible on Reports Page(Pass)";
		}
		else
		{
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("Export button is not visible on Reports Page");
			comments+="Export button is not visible on Reports Page(Fail)";
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
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ReportsSuiteXls, this.getClass().getSimpleName()) ;
	}
}
