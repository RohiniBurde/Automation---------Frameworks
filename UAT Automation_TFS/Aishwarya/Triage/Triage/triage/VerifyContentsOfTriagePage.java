/* Author Name:-Aishwarya Deshpande
 * Created Date:-26th Nov 2014
 * Last Modified on Date:- 19th Dec 2014
 * Module Description:- Verification of Contents of Triage Page with the positive data  
 */

package com.uat.suite.triage;

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

public class VerifyContentsOfTriagePage extends TestSuiteBase {
	

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
		APP_LOGS.debug("Executing VerifyContentsOfTriagePage Test Case");

		if(!TestUtil.isTestCaseRunnable(TriageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TriageSuiteXls, this.getClass().getSimpleName());
	}
			
	@Test(dataProvider="getTestData")
	public void Test_VerifyContentsOfTriagePage(String Role, String GroupName, String Portfolio, String ProjectName, 
			String Version1, String Version2, String endMonth, String endYear, String endDate, String VersionLead, 
			String testPassName1, String testManager,String testPassName2, String testerName, String Role1, String Role2, 
			String	testCaseName, String testStepName,String assignedRole, String expectedResult)throws Exception
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
						
			if(!createProject(GroupName, Portfolio, ProjectName, Version1, endMonth, endYear, endDate, versionLead.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName+" Project not Created Successfully with version:"+Version1+".");
				comments=ProjectName+" Project not Created Successfully with version:"+Version1+" (Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectVersion1Creation");
				closeBrowser();
	
				throw new SkipException("Project with "+Version1+" not created successfully");
			}
			APP_LOGS.debug(ProjectName+" Project Created Successfully with version:"+Version1+".");
						
			if(!createProject(GroupName, Portfolio, ProjectName, Version2, endMonth, endYear, endDate, versionLead.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName+" Project not Created Successfully with version:"+Version2+".");
				comments=ProjectName+" Project not Created Successfully with version:"+Version2+" (Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectVersion2Creation");
				closeBrowser();
	
				throw new SkipException("Project with "+Version2+" not created successfully");
			}
			APP_LOGS.debug(ProjectName+" Project Created Successfully with version:"+Version2+".");
					
			// creating test pass,test case,testers and test step
			if(!createTestPass(GroupName, Portfolio, ProjectName, Version1, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully.");
				comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass1Creation");
				closeBrowser();
	
				throw new SkipException("Test Pass "+testPassName1+" not created successfully");
			}
			APP_LOGS.debug(testPassName1+" Test Pass Created Successfully.");
						
			if(!createTestPass(GroupName, Portfolio, ProjectName, Version1, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully.");
				comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass2Creation");
				closeBrowser();
	
				throw new SkipException("Test Pass "+testPassName2+" not created successfully");
			}
			APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");
					
			if(!createTester(GroupName, Portfolio, ProjectName, Version1, testPassName1, tester.get(0).username, Role1, Role1))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+Role1+"not Created Successfully.");
				comments+="Tester with role "+Role1+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
				closeBrowser();
	
				throw new SkipException("Tester with Role: "+Role1+" not created successfully");
			}
			APP_LOGS.debug(" Tester with role "+Role1+" Created Successfully.");
	
			if(!createTester(GroupName, Portfolio, ProjectName, Version1, testPassName1, tester.get(1).username, Role2, Role2))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+Role2+"not Created Successfully.");
				comments+="Tester with role "+Role2+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester2Creation");
				closeBrowser();
	
				throw new SkipException("Tester with Role: "+Role2+"  not created successfully");
			}
			APP_LOGS.debug(" Tester with role "+Role2+" Created Successfully.");
	
			if(!createTestCase(GroupName, Portfolio, ProjectName, Version1, testPassName1, testCaseName))
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
	
			if(!createTestStep(GroupName, Portfolio, ProjectName, Version1, testPassName1, testStepName, expectedResult, testCaseName, assignedRole))
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
						
			comments="Data Created successfully.";
			APP_LOGS.debug("Data Created successfully.");
						
			closeBrowser();
 		    APP_LOGS.debug(" Closed Browser after creating data on Test Management tab.");
		 				
			APP_LOGS.debug("Opening Browser... ");
			openBrowser();
		 				
			if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			{
				try
				{
					APP_LOGS.debug("Logged in browser with Test Manager");
				 	            
					// Clicking on Triage Tab
					getElement("UAT_triage_Id").click();
					APP_LOGS.debug("Triage tab clicked ");
					Thread.sleep(2000);
								
					//Selecting Project and Verifying default value for Projects Dropdown
					Select projectGroup = new Select(getElement("Triage_projectDropDown_Id"));
					String defaultProjectDDValue= projectGroup.getFirstSelectedOption().getText();
					verifyDefaultDDText("Triage_defaultProjectDDText", defaultProjectDDValue, "Project");
								
					//Selecting Version and Verifying default value for Projects Dropdown
					Select versionGroup = new Select(getElement("Triage_versionDropDown_Id"));
					String defaultVersionDDValue= versionGroup.getFirstSelectedOption().getText();
					verifyDefaultDDText("Triage_defaultVersionDDText", defaultVersionDDValue, "Version");
								
					//Selecting Test Pass and Verifying default value for Test Pass Dropdown
					Select testPassGroup = new Select(getElement("Triage_testPassDropDown_Id"));
					String defaultTestPassDDValue= testPassGroup.getFirstSelectedOption().getText();
					verifyDefaultDDText("Triage_defaultTestPassDDText", defaultTestPassDDValue, "Test Pass");
								
					//Selecting Tester and Verifying default value for Tester Dropdown
					Select TesterGroup = new Select(getElement("Triage_testerDropDown_Id"));
					String defaultTesterDDValue= TesterGroup.getFirstSelectedOption().getText();
					verifyDefaultDDText("Triage_defaultTesterDDText", defaultTesterDDValue, "Tester");
								
					//Selecting Tester Role and Verifying default value for Tester Role Dropdown
					Select testerRoleGroup = new Select(getElement("Triage_testerRoleDropDown_Id"));
					String defaultTesterRoleDDValue= testerRoleGroup.getFirstSelectedOption().getText();
					verifyDefaultDDText("Triage_defaultTesterRoleDDText", defaultTesterRoleDDValue, "Tester Role");
								
					//Verifying if Go button is displayed or not on Triage Page
					if(getElement("Triage_goButton_img").isDisplayed())
					{
						APP_LOGS.debug("Go button is visible on Triage Page");
						comments+="Go button is visible on Triage Page(Pass)";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Go button is not visible on Triage Page");
						comments+="Go button is not visible on Triage Page(Fail)";
					}
								
					//Verifying if Export button is displayed or not on Triage Page
					if(getElement("Triage_exportButton_img").isDisplayed())
					{
						APP_LOGS.debug("Export button is visible on Triage Page");
						comments+="Export button is visible on Triage Page(Pass)";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Export button is not visible on Triage Page");
						comments+="Export button is not visible on Triage Page(Fail)";
					}
								
					APP_LOGS.debug("Verifying Triage Page Dropdown Contents");
					
					//Selecting Project
					List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName, ProjectName, "Project");
				
					//Selecting Version
					List<WebElement> listOfVersions=getElement("Triage_versionDropDown_Id").findElements(By.tagName("option"));
					String actualVersionArray[]= new String[listOfVersions.size()];
					for(int i=0;i<listOfVersions.size();i++)
					{	
						actualVersionArray[i]=listOfVersions.get(i).getText();
						if(listOfVersions.get(i).getText().equals(Version1))
						{
							listOfVersions.get(i).click();
							APP_LOGS.debug("Version is selected");
							break;
						}
					}
					String expectedVersionArray[] = {Version1};
					DDContentVerifiaction(expectedVersionArray, actualVersionArray, "Version");
				
					//Selecting Test Pass
					List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					String firstTestPassOption=listOfTestPass.get(0).getText();
					String actualtestPassArray[]=selectDDContent(listOfTestPass, testPassName1, "Test Pass");
								
					//Verification of first Test Pass option
					verifyFirstDDOption("Triage_firstTestPassOption", firstTestPassOption);
								
					//Verifcation of Test Pass Dropdown contents
					String expectedtestPassArray[] = {testPassName2, testPassName1};
					DDContentVerifiaction(expectedtestPassArray, actualtestPassArray, "Test Pass");
				
					//Selecting Tester
					List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					String firstTesterOption=listOfTester.get(0).getText();
					String tester1Name=tester.get(0).username.replace(".", " ");
					String tester2Name=tester.get(1).username.replace(".", " ");
					String actualtesterArray[]=selectDDContentByText(listOfTester, tester1Name, "Tester");
								
					//Verification of first Tester option
					verifyFirstDDOption("Triage_firstTesterOption", firstTesterOption);
								
					//Verification of Tester Dropdown Content
					String expectedtesterArray[] = {tester1Name, tester2Name};
					DDContentVerifiaction(expectedtesterArray, actualtesterArray, "Tester");
								
					//Selecting Tester Role
					List<WebElement> listOfTesterRole=getElement("Triage_testerRoleDropDown_Id").findElements(By.tagName("option"));
					String firstTesterRoleOption=listOfTesterRole.get(0).getText();
					String testerRoleArray[]=selectDDContent(listOfTesterRole, Role1,"Tester Role");
				
					//Verification of first Tester Role option
					verifyFirstDDOption("Triage_firstTesterRoleOption", firstTesterRoleOption);
								
					//Verifcation of Tester Dropdown Contents
					String expectedTesterRoleArray[] = {Role1};
					DDContentVerifiaction(expectedTesterRoleArray, testerRoleArray, "Tester Role");
				}
		 		catch(Throwable t)
		 		{
		 			fail=true;
		 			assertTrue(false);
		 			t.printStackTrace();
		 			comments+="Exception in Triage Page Content.";
		 			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TriagePageContentFailure");
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
			
	private String[] selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
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
		return actualArray;
	}
			
	private String[] selectDDContentByText(List<WebElement> listOfElements, String selectOption, String DDName)
	{
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
			comments+="Total Number of elements in"+DDName+" do not match";
			APP_LOGS.debug("Total Number of elements in"+DDName+" do not match");
		}
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
