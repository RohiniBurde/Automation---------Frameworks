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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyContentsOfTestStepsPage extends TestSuiteBase{
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	String comments;
	static boolean isLoginSuccess=true;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
		}
	
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void VerifyTestStepsPageContents(String role,String group,String portfolio,String project, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String testPassName,
			String testPassEndMonth, String testPassEndYear,String testPassEndDate, String testerRole,
			String testCaseName,String testCaseDescription, String testStepName, String testStepExpectedResult) throws Exception
	{
		count++;
		
		ArrayList<Credentials> versionLead = getUsers("Version Lead", 1);
		ArrayList<Credentials> testManager = getUsers("Test Manager", 1);
		ArrayList<Credentials> tester = getUsers("Tester", 1);
		
		String otherProject = project+"1";
		String testStepPageHeaderText;
		String selectedGroup;
		String selectedPortfolio;
		String selectedProject;
		String selectedVersion;
		String selectedTestPass;
		
		boolean viewAllIsDisplayed;
		boolean createNewIsDisplayed;
		boolean importExportTestStepsIsDisplayed;
		
		boolean noTestStepIsDisplayed;
		boolean totalRecordsIsDisplayed;
		
		int totalRecordsCount;
		int totalTestSteps;
		
		
		if(!(runmodes[count].equalsIgnoreCase("Y") && versionLead!=null && testManager!=null && tester!=null))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		
		
		openBrowser();
		
		if (login(role)) 
		{
			
			
			getElement("UAT_testManagement_Id").click();
			
			Thread.sleep(1000);
			
			if(!createProject(group, portfolio, otherProject, version, projectEndMonth, projectEndYear, projectEndDate, 
					versionLead.get(0).username))
			{
				fail=true;
				APP_LOGS.debug(otherProject+" Project not Created Successfully.");
				comments=otherProject+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Project Successfully not created");
				
			}
			
			APP_LOGS.debug(otherProject+" Project Created Successfully.");
			comments= otherProject+" Project Created Successfully(Pass). ";
			
			closeBrowser();
			
			openBrowser();
			
			if (login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead")) 
			{
				
				
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(1000);
				
				if(!createProject(group, portfolio, project, version, projectEndMonth, projectEndYear, projectEndDate, 
						versionLead.get(0).username))
				{
					fail=true;
					APP_LOGS.debug(project+" Project not Created Successfully.");
					comments+=project+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
					closeBrowser();

					throw new SkipException("Project Successfully not created");
				}
				
				APP_LOGS.debug(project+" Project Created Successfully.");
				comments+=otherProject+" Project Created Successfully(Pass). ";
				
				if(!createTestPass(group, portfolio, project, version, testPassName, testPassEndMonth, testPassEndYear, 
						testPassEndDate, testManager.get(0).username))
				{
					
					fail=true;
					APP_LOGS.debug("Test Pass not Created Successfully.");
					comments+="Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
					closeBrowser();

					throw new SkipException("Test Pass Successfully not created");
					
				}
				
				APP_LOGS.debug("Test Pass Created Successfully.");
				comments+="Test Pass Created Successfully(Pass). ";
				
				
				if (!createTester(group, portfolio, project, version, testPassName, tester.get(0).username, testerRole, testerRole)) 
				{
					
					fail=true;
					APP_LOGS.debug("Tester not Created Successfully.");
					comments+="Tester not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
					closeBrowser();

					throw new SkipException("Tester Successfully not created");
					
				}
				
				APP_LOGS.debug("Tester Created Successfully.");
				comments+="Tester Created Successfully(Pass). ";
				
				
				if (!createTestCase(group, portfolio, project, version, testPassName, testCaseName)) 
				{
					
					fail=true;
					APP_LOGS.debug("Test Case not Created Successfully.");
					comments+="Test Case not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
					closeBrowser();

					throw new SkipException("Test Case Successfully not created");
					
				}
				
				APP_LOGS.debug("Test Case Created Successfully.");
				comments+="Test Case Created Successfully(Pass). ";
				
								
				try
				{
					
					getElement("TestStepNavigation_Id").click();
					
					Thread.sleep(1000);
					
					testStepPageHeaderText = getElementByClassAttr("TestSteps_pageHeaderText_Class").getText();
					
					if (!testStepPageHeaderText.contains("Test Step")) 
					{
						fail=true;
						APP_LOGS.debug("Test Step page landing unsuccessfull.");
						comments+="Test Step page landing unsuccessfull(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test StepPageLandingUnsuccessfull");
						//closeBrowser();

						throw new SkipException("Test Step page landing unsuccessfull");
						
					}
					
					APP_LOGS.debug("Test Step page landing successfull.");
					comments+="Test Step page landing successfull(Pass). ";
					
					selectedGroup= getObject("TestSteps_groupTitleSelected").getAttribute("title");
					selectedPortfolio= getObject("TestSteps_portfolioTitle").getAttribute("title");
					selectedProject= getObject("TestSteps_projectTitleSelected").getAttribute("title");
					selectedVersion = getObject("TestSteps_versionTitleSelected").getAttribute("title");
					selectedTestPass= getObject("TestSteps_testPassTitleSelected").getAttribute("title");
					
					if (assertTrue(selectedGroup.equals(group) && selectedPortfolio.equals(portfolio) && 
							selectedProject.equals(project) && selectedVersion.equals(version)&& selectedTestPass.equals(testPassName))) 
					{
						APP_LOGS.debug("Recent created group/project displayed in navigation pane.");
						comments+="Recent created group/project displayed in navigation pane(Pass). ";
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("Recent created group/project not displayed in navigation pane.");
						comments+="Recent created group/project not displayed in navigation pane(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "RecentProjectDisplay");
						
					}
					
					
					viewAllIsDisplayed = getObject("TestSteps_viewAllLink").isDisplayed();
					createNewIsDisplayed = getObject("TestSteps_createNewLink").isDisplayed();
					importExportTestStepsIsDisplayed = getObject("TestSteps_ImportExportTestStepsLink").isDisplayed();
					totalRecordsIsDisplayed= getElement("TestSteps_totalRecordsDiv_Id").isDisplayed();
					noTestStepIsDisplayed = getElement("TestSteps_noTestStepAvailable_Text_Id").isDisplayed();
					if (assertTrue(viewAllIsDisplayed && createNewIsDisplayed)) 
					{
						APP_LOGS.debug("View All and Create New link displayed.");
						comments+="View All and Create New link displayed(Pass). ";
						
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("View All and Create New link not displayed.");
						comments+="View All and Create New link not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ViewAllCreateNewDisplay");
						
					}
										
					if (assertTrue(noTestStepIsDisplayed)) 
					{
						APP_LOGS.debug("'No Test Step Available' displayed.");
						comments+="'No Test Step Available' displayed(Pass). ";
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("'Test Step Available' not displayed.");
						comments+="'Test Step Available' not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test StepDisplay");
						
					}
					
					if (!assertTrue(totalRecordsIsDisplayed)) 
					{
						APP_LOGS.debug("'Total Records Count' not displayed.");
						comments+="'Total Records Count' not displayed(Pass). ";
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("'Total Records Count' displayed.");
						comments+="'Total Records Count' displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsDisplay");
						
					}
					
					if (assertTrue(importExportTestStepsIsDisplayed)) 
					{
						APP_LOGS.debug("'importExportTestStepsIsDisplayed' link displayed.");
						comments+="'importExportTestStepsIsDisplayed' link displayed(Pass). ";
					}
					else 
					{
						fail = true;
						
						APP_LOGS.debug("'importExportTestStepsIsDisplayed' link not displayed.");
						comments+="'importExportTestStepsIsDisplayed' link not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "importExportTestStepsLinkDisplay");
						
					}
					
					Thread.sleep(1000);
					
                    getObject("TestStep_createNewProjectLink").click();
					
					Thread.sleep(1000);
					
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				    
				    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResult); 
					
					List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
					int numOfTestCases = TestCaseSelectionNames.size();
					for(int i = 0; i<numOfTestCases;i++)
					{
							if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCaseName))
							{
								getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
								break;
							}
					}
					
					String[] testerRoleSelectionArray = testerRole.split(",");
					List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
							{
								getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
						}
					}
					
					getElement("TestStepCreateNew_testStepSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					
					getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();
					Thread.sleep(500);
					
					//click on View All link
					getObject("TestSteps_viewAllLink").click();
					
					totalTestSteps = getElement("TestStepsViewAll_table_Id").findElements(By.xpath("table/tbody/tr")).size();
					
					if (totalTestSteps!=0) 
					{
						APP_LOGS.debug("Test Step Table displayed.");
						comments+="Test Step Table displayed(Pass). ";
						
						totalRecordsIsDisplayed= getElement("TestSteps_totalRecordsDiv_Id").isDisplayed();
						
						
						if (assertTrue(totalRecordsIsDisplayed)) 
						{
							
							APP_LOGS.debug("'Total Records Count' displayed after test step created .");
							comments+="'Total Records Count' displayed after test step created(Pass). ";
							
							totalRecordsCount=Integer.parseInt(getElement("TestSteps_totalRecordsCountText_Id").getText()); 
							
							if (totalTestSteps==totalRecordsCount) 
							{
								APP_LOGS.debug("'Total Records Count' is correct .");
								comments+="'Total Records Count' is correct(Pass). ";
								
							}
							else
							{
								fail = true;
								APP_LOGS.debug("'Total Records Count' is not correct .");
								comments+="'Total Records Count' is not correct(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsCount");
								assertTrue(false);
							
							}
						}
						else 
						{
							fail = true;
							APP_LOGS.debug("'Total Records Count' not displayed after test step created .");
							comments+="'Total Records Count' not displayed after test step created(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsNotDisplayed");
							
							
						}
					}
					else
					{
						fail = true;
						APP_LOGS.debug("Test Step Table not displayed.");
						comments+="Test Step Table not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepTableDisplay");
						assertTrue(false);
					}
					
					
					
					closeBrowser();
					
				
					
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail = true;
					APP_LOGS.debug("Exception in Test Step Tab");
					comments+="Exception in Test Step Tab. ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepTabException");
					assertTrue(false);
					closeBrowser();
					
				}								
				
				
				
			}
			else
			{
				isLoginSuccess = false;
				APP_LOGS.debug("Login Unsuccessfull");
				comments+="Login Unsuccessfull";
				
			}
			
			
			
			
		}
		else
		{
			isLoginSuccess = false;
			APP_LOGS.debug("Login Unsuccessfull");
			comments+="Login Unsuccessfull";
		}
		
		
			
			
		
		
		
	}
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
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
	public void reportTestResult(){
		if(isTestPass)
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			TestUtil.printComments(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
			TestUtil.printComments(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), comments);
		}
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
}
