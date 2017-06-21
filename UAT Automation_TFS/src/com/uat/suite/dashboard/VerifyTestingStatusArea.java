package com.uat.suite.dashboard;

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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;
@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyTestingStatusArea extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	boolean totalCountVisibility=false;
	int totalPages;
	int projectTestPassSummaryRows;
	boolean portfolioFlag=false;
	String projectCell,versionCell,testPassCell,roleCell,actionCell;
	int daysLeft,notCompletedCount,passCount,failCount;
	int projectNoTestStep=1;
	int testPassNoTestStep=1;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(dashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(dashboardSuiteXls, this.getClass().getSimpleName());
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();	
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
			
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void verifyTestingStatusArea(String Role, String GroupName, String Portfolio, String Project, String Version,
			String VersionLead,String EndMonth,String EndYear,String EndDate,String testPass, String TestManager,
			String TP_Tester,String TesterRole, String testCase, String testStep, String TestStepExpectedResult)throws Exception
	{
		count++;
		comments="";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int tester_count = Integer.parseInt(TP_Tester);
		tester = getUsers("Tester", tester_count);
		
		int testManager_count = Integer.parseInt(TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		openBrowser();
		APP_LOGS.debug("Opening Browser for Non Tester... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug("Clicking On Test Management Tab.");

			//Project creation
			if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Project Creation fails(FAIL).");
				comments+="Project Creation Fails(FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");
				closeBrowser();
				
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project not created successfully");//reports
			}
			APP_LOGS.debug("Project successfully created(PASS).");
			
			//1st Test Pass creation 
			if(!createTestPass(GroupName, Portfolio, Project, Version, testPass, EndMonth, EndYear, EndDate, testManager.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation fails(FAIL).");
				comments+="Test Pass Creation Fails(FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
			APP_LOGS.debug("Test Pass successfully created(PASS).");
 
			APP_LOGS.debug("Closing Browser..");
			closeBrowser();
			
			APP_LOGS.debug("Opening browser..");
			openBrowser();
			Thread.sleep(1000);
			
			if(login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead"))
			{
				try
				{
					//Verify project status is visible or not
					if(compareStrings(resourceFileConversion.getProperty("Dashboard_projectStatusText"),getObject("Dashboard_projectStatusHeading").getText().trim()))
					{
						APP_LOGS.debug("Project Status text is verified(PASS).");
						comments+="Project Status text is verified(PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project Status text does not matched(FAIL).");
						comments+="Project Status text does not matched(FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectStatusTextNotMatching");
					}
					
					//Verifying Portfolio and Selecting Portfolio, Project, Version, TestPassNameI from Dropdown
					selectPortfolio(Portfolio, Project, Version, testPass);
					
					//Verifying Pie chart and Message Display
					verifyProjectAndTPStatusContent();
					
					getElement("UAT_testManagement_Id").click();
					APP_LOGS.debug("Clicking On Test Management Tab.");
					
					// Tester creation for Test Pass
					if(!createTester(GroupName, Portfolio, Project, Version, testPass, tester.get(0).username, TesterRole, TesterRole))
					{
						fail=true;
						APP_LOGS.debug("Tester Creation fails(FAIL).");
						comments+="Tester Creation Fails(FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
					}
					APP_LOGS.debug("Tester created successfully(PASS).");

					// Test Case creation for Test Pass
					if(!createTestCase(GroupName, Portfolio, Project, Version, testPass, testCase))
					{
						fail=true;
						APP_LOGS.debug("Test Case Creation fails  for TP I (FAIL).");
						comments+="Test Case Creation Fails for TP I (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase");
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case is not created successfully");//reports
					}
					APP_LOGS.debug("Test Case created successfully(PASS).");
					
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(1000);
							
					//Verifying Portfolio and Selecting Portfolio, Project, Version, TestPassNameI from Dropdown
					selectPortfolio(Portfolio, Project, Version, testPass);
					
					//Verifying Pie chart and Message Display
					verifyProjectAndTPStatusContent();
					
					//To verify 'Number of testers participated' button display
					verifyNoOfTester();
					 
					//To verify 'Detailed analysis' button display
					verifyDetailedAnalysis();
					 
					getElement("UAT_testManagement_Id").click();
					APP_LOGS.debug("Clicking On Test Management Tab.");
					Thread.sleep(500);
					 
					// Test Step creation for Test Pass
					if(!createTestStep(GroupName, Portfolio, Project, Version, testPass, testStep, TestStepExpectedResult, testCase, TesterRole))
					{
						fail=true;
						APP_LOGS.debug("Test Step Creation fails for TP I (FAIL).");
						comments+="Test Step Creation Fails for TP I (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						closeBrowser();
							
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step is not created successfully");//reports
					}
					APP_LOGS.debug("Test Step created successfully(PASS).");
					 
					Thread.sleep(3000);
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(2000);
					 
					//Verifying Portfolio and Selecting Portfolio, Project, Version, TestPassNameI from Dropdown
					selectPortfolio(Portfolio, Project, Version, testPass);
					
					//Verifying Project Pie Chart display
					if(!verifyProjectPieChart())
					{
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"PortfolioIsNotFound");
						closeBrowser();
						throw new SkipException("Project Pie chart is not available(FAIL).");
					}
					 
					//Verifying Test Pass Pie Chart display
					if(!verifyTestPassPieChart())
					{
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"PortfolioIsNotFound");
						closeBrowser();
						throw new SkipException("Test Pass Pie chart is not available(FAIL).");
					}
					 
					//To verify if 'Detailed analysis' buttons is displayed or not and its functioning
					if(verifyDetailedAnalysis())
					{
						getObject("Dashboard_detailedAnalysis").click();
						//verify
						Thread.sleep(500);
						Select selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
						Select selectVersion = new Select(getElement("DetailedAnalysis_versionDropDown_Id"));
						Select selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
						Select selectTester = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
						Select selectRole = new Select(getElement("DetailedAnalysis_roleDropDown_Id"));
							
						if(assertTrue(selectProject.getFirstSelectedOption().getAttribute("title").equals(Project)
								&& selectVersion.getFirstSelectedOption().getAttribute("title").equals(Version)
								&& selectTestPass.getFirstSelectedOption().getAttribute("title").equals(testPass)
								&& selectTester.getFirstSelectedOption().getText().equals("All Testers")
								&& selectRole.getFirstSelectedOption().getText().equals("All Role")))
						{
							APP_LOGS.debug("Detailed Analysis button is functioning properly(PASS).");
							comments+="Detailed Analysis button is functioning properly(PASS).";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Detailed Analysis button is not functioning properly(FAIL).");
							comments+="Detailed Analysis button is not functioning properly(FAIL).";
						}
					}
				
					//back to dashboard page
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(1000);
					
					//Selecting Portfolio, Project, Version, TestPassNameI from Dropdown
					selectPortfolio(Portfolio, Project, Version, testPass);
					
					//To verify if 'Number of testers participated' button is displayed or not and its functioning
					verifyNoOfTester();
			
				//Export functionality is not working properly so the below code is commented
					/*		if(verifyNoOfTester())
					{
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
					            
					    getElement("DashboardMyActivity_numberOfTesterParticipatedBtn_Id").click();
					    if(result.equals(false))
					    {
					      //Active x code
					      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
					      if(assertTrue(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed()))
					      {
					        getObject("TestStepCreateNew_TestStepActiveX_Close").click();
					        APP_LOGS.debug("ActiveX is disabled and hence cannot export file(PASS)");
					        comments+="ActiveX is disabled and hence cannot export file(PASS).";
					        throw new SkipException("ActiveX is disabled and hence cannot export details");
					      }
					      else
					      {	
					    	  fail=true;
					          APP_LOGS.debug("An alert informing the user of disabled activex should be present(FAIL).");
					          comments+="An alert informing the user of disabled activex should be present(FAIL).";
					          TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabled");
					          comments+="Fail- Activex is disabled..but the popup not displayed";
					      }
					    }
					    else
					    {
					    	APP_LOGS.debug("File exported successfully(PASS)");
					    	comments+="File exported successfully(PASS).";
					    }
					}*/
				}
				catch(Throwable t)
				{
					fail=true;
		 			assertTrue(false);
		 			t.printStackTrace();
		 			comments+="Exception in Testing Status Area for Version Lead.";
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Tester Login Unsuccessfull(FAIL).");
				comments+="Tester Login Unsuccessfull(FAIL).";
			}
			APP_LOGS.debug("Closing Browser.");
			closeBrowser();
			
			openBrowser();
			APP_LOGS.debug("Opening Browser for Tester... ");
				
			if(login(tester.get(0).username, tester.get(0).password, "Tester"))
			{
				try
				{
					//Project Dropdown Verification
					if(assertTrue(isElementExistsById("DetailedAnalysis_projectDropDown_Id")))
					{	
						APP_LOGS.debug("Project dropdown is visible(PASS).");
						comments+="Project dropdown is visible(PASS).";

						List<WebElement> projectTSDD = getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));
						for(int i=0; i<projectTSDD.size(); i++)
						{
							if(projectTSDD.get(i).getAttribute("title").equals(Project))
							{
								Thread.sleep(1000);
								projectTSDD.get(i).click();
								break;							
							}
						}
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project dropdown not visible(FAIL).");
						comments+="Project dropdown not visible(FAIL).";
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project dropdown not visible");//reports
					}
					
					//Version Dropdown Verification
					if(assertTrue(isElementExistsById("DetailedAnalysis_versionDropDown_Id")))
					{	
						APP_LOGS.debug("Version dropdown is visible(PASS).");
						comments+="Version dropdown is visible(PASS).";

						List<WebElement> versionTSDD = getElement("DetailedAnalysis_versionDropDown_Id").findElements(By.tagName("option"));
						for(int i=0; i<versionTSDD.size(); i++)
						{
							if(versionTSDD.get(i).getText().equals(Version))
							{
								Thread.sleep(1000);
								versionTSDD.get(i).click();
								break;							
							}
						}
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Version dropdown not visible(FAIL).");
						comments+="Version dropdown not visible(FAIL).";
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Version dropdown not visible");//reports
					}
					
					//Test Pass Dropdown Verification
					if(assertTrue(isElementExistsById("DetailedAnalysis_testPassDropDown_Id")))
					{	
						APP_LOGS.debug("Test Pass dropdown is visible(PASS).");
						comments+="Test Pass dropdown is visible(PASS).";

						List<WebElement> testPassTSDD = getElement("DetailedAnalysis_testPassDropDown_Id").findElements(By.tagName("option"));
						for(int i=0; i<testPassTSDD.size(); i++)
						{
							if(testPassTSDD.get(i).getAttribute("title").equals(testPass))
							{
								Thread.sleep(1000);
								testPassTSDD.get(i).click();
								break;							
							}
						}
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Test Pass dropdown not visible(FAIL).");
						comments+="Test Pass dropdown not visible(FAIL).";
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass dropdown not visible");//reports
					}
					
					//Verifcation of Test Pass Pie Chart display
					if(assertTrue(isElementExistsByXpath("DashboardTestPass_pieChart")))
				    {
						APP_LOGS.debug("Test Pass pie chart is visible(PASS).");
						comments+="Test Pass pie chart is visible(PASS).";
				    }
					else
					{
				    	fail=true;
				    	APP_LOGS.debug("Test Pass pie chart is not visible(FAIL).");
						comments+="Test Pass pie chart is not visible(FAIL).";
				    }
					
					//To verify if 'Detailed analysis' buttons is displayed or not and its functioning
					if(verifyDetailedAnalysis())
					{
						getObject("Dashboard_detailedAnalysis").click();
						//verify
						Thread.sleep(500);
						Select selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
						Select selectVersion = new Select(getElement("DetailedAnalysis_versionDropDown_Id"));
						Select selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
						Select selectTester = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
						Select selectRole = new Select(getElement("DetailedAnalysis_roleDropDown_Id"));
							
						if(assertTrue(selectProject.getFirstSelectedOption().getAttribute("title").equals(Project)
								&& selectVersion.getFirstSelectedOption().getAttribute("title").equals(Version)
								&& selectTestPass.getFirstSelectedOption().getAttribute("title").equals(testPass)
								&& selectTester.getFirstSelectedOption().getText().equals("All Testers")
								&& selectRole.getFirstSelectedOption().getText().equals("All Role")))
						{
							APP_LOGS.debug("Detailed Analysis button is functioning properly(PASS).");
							comments+="Detailed Analysis button is functioning properly(PASS).";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Detailed Analysis button is not functioning properly(FAIL).");
							comments+="Detailed Analysis button is not functioning properly(FAIL).";
						}
					}
				}
				catch(Throwable t)
				{
					fail=true;
		 			assertTrue(false);
		 			t.printStackTrace();
		 			comments+="Exception in Testing Status Area for Tester.";
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Tester Login Unsuccessfull(FAIL).");
				comments+="Tester Login Unsuccessfull(FAIL).";
			}
			APP_LOGS.debug("Closing Browser.");
			closeBrowser();
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Login Unsuccessfull(FAIL).");
			comments+="Login Unsuccessfull(FAIL).";
		}
	}
	
	private void selectPortfolio(String Portfolio,String Project, String Version,String TestPassNameI ) throws IOException, InterruptedException
	{
		//verify portfolio is present in the drop down
		List<WebElement> portfolioDD = getElement("Dashboard_portfolioDD_ID").findElements(By.tagName("option"));
		for(int i=0; i<portfolioDD.size(); i++)
		{	
			String portfolioName=portfolioDD.get(i).getAttribute("title");
			if(portfolioName.equals(Portfolio))
			{
				Thread.sleep(1000);
				portfolioDD.get(i).click();
				APP_LOGS.debug("Portfolio is found (PASS).");
				portfolioFlag=true;
				break;							
			}
		}
		
		//verify whether project is present and select it
		if(portfolioFlag==true)
		{
			APP_LOGS.debug("Portfolio is found (PASS).");
			comments+="Portfolio is found (PASS).";
			projectStatus(Project, Version, TestPassNameI);
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Portfolio is not found (FAIL).");
			comments+="Portfolio is not found (FAIL).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"PortfolioIsNotFound");
			closeBrowser();
			
			throw new SkipException("Portfolio is not available (FAIL).");
		}
	}
	
	private void projectStatus(String Project,String Version,String TestPassName) throws IOException, InterruptedException
	{
		List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
		for(int i=0; i<projectDD.size(); i++)
		{
			if(projectDD.get(i).getText().equals(Project))
			{
				Thread.sleep(1000);
				projectDD.get(i).click();
				APP_LOGS.debug("Project is found (PASS).");
				break;							
			}
		}
				
		List<WebElement> versionDD = getElement("Dashboard_versionDD_ID").findElements(By.tagName("option"));
		for(int i=0; i<versionDD.size(); i++)
		{
			if(versionDD.get(i).getText().equals(Version))
			{
				Thread.sleep(1000);
				versionDD.get(i).click();
				APP_LOGS.debug("Version is found (PASS).");
				break;							
			}
		}
				
		List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
		for(int i=0; i<testPassDD.size(); i++)
		{	
			String testPassName=testPassDD.get(i).getAttribute("title");
			if(testPassName.equals(TestPassName))
			{
				Thread.sleep(1000);
				testPassDD.get(i).click();
				APP_LOGS.debug("Test Pass is found (PASS).");
				break;							
			}
		}
	}
		
	private boolean verifyDetailedAnalysis()
	{
		if(assertTrue(isElementExistsByXpath("Dashboard_detailedAnalysis")))
		{
			APP_LOGS.debug("Detailed Analysis Button is displayed(PASS).");
			comments+="Detailed Analysis Button is displayed(PASS).";
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Detailed Analysis Button not displayed(FAIL).");
			comments+="Detailed Analysis Button not displayed(FAIL).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisButtonNotDisplayed");
			return false;
		}
	}
		
	private boolean verifyNoOfTester() throws IOException
	{
		if(assertTrue(isElementExistsById("DashboardMyActivity_numberOfTesterParticipatedBtn_Id")))
		{
			APP_LOGS.debug("Number of Testers Particiapted Button is displayed(PASS).");
			comments+="Number of Testers Particiapted Button is displayed(PASS).";
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Number of Testers Particiapted Button not displayed(FAIL).");
			comments+="Number of Testers Particiapted Button not displayed(FAIL).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoOfTesterButtonNotDisplayed");
			return false;
		}
	}
		
	private boolean verifyTestPassPieChart() throws IOException
	{
		if(assertTrue(isElementExistsByXpath("DashboardTestPass_pieChart")))
		{
			APP_LOGS.debug("Test Pass Pie chart is visible(PASS).");
			comments+="Test Pass Pie chart is visible(PASS).";
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Test Pass Pie chart is visible (FAIL).");
			comments+="Test Pass Pie chart is not visible (FAIL).";
			return false;
		}
	}
		
	private boolean verifyProjectPieChart() throws IOException
	{
		if(assertTrue(isElementExistsByXpath("DashboardProject_pieChart")))
		{
			APP_LOGS.debug("Project Pie chart is visible(PASS).");
			comments+="Project Pie chart is visible(PASS).";
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Project Pie chart is not visible(FAIL).");
			comments+="Project Pie chart is not visible(FAIL).";
			return false;
		}
	}
		
	private boolean verifyNoTestStepAvailMsgProject() throws IOException
	{
		//for Test Pass
		if(compareStrings(resourceFileConversion.getProperty("Dashboard_noTestStepAvailable"),getElement("DashboardTestingStatus_noTestPassAvailable_Id").getText()))
		{
			APP_LOGS.debug("No Test Steps available message is displayed (PASS).");
			comments+="No Test Steps available message is displayed (PASS).";
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("No Test Steps available message is not displayed(FAIL).");
			comments+="No Test Steps available message is not displayed(FAIL).";
			return false;
		}
	}
		
	private boolean verifyNoTestStepAvailMsgTestPass() throws IOException
	{
		//for Test Cases
		if(compareStrings(resourceFileConversion.getProperty("Dashboard_noTestStepAvailable"),getElement("DashboardTestingStatus_noTestCaseAvailable_Id").getText()))
		{
			APP_LOGS.debug("No Test Steps available message is displayed(PASS).");
			comments+="No Test Steps available message is displayed(PASS).";
			testPassNoTestStep++;
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug("No Test Steps available message is not displayed(FAIL).");
			comments+="No Test Steps available message is not displayed(FAIL).";
			return false;
		}
	}
		
	private void verifyProjectAndTPStatusContent() throws IOException
	{
		//To verify if Pie chart in Project Status is displayed or not
		if(assertTrue(!isElementExistsByXpath("DashboardProject_pieChart")))
		{	
			APP_LOGS.debug("Project Status Test Steps Pie Chart is not displayed(PASS).");
			comments+="Project Status Test Steps Pie Chart is not displayed(PASS). ";
		}
		else
		{	
			fail=true;
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectPieChartDispalyed");
			APP_LOGS.debug("Project Status Test Steps Pie Chart is displayed(FAIL).");
			comments+="Project Status Test Steps Pie Chart is displayed(FAIL).";
		}
			
		//Message verification in Project Status Section 
		if(!verifyNoTestStepAvailMsgProject())
		{
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepAvailableMsgNotFound");
			closeBrowser();
			throw new SkipException("Test Steps should not available (FAIL).");
		}
			
		//To verify if Pie chart in Test Pass Status is displayed or not
		if(assertTrue(!isElementExistsByXpath("DashboardTestPass_pieChart")))
		{	
			APP_LOGS.debug("TestPass Status Test Steps Pie Chart is not displayed(PASS).");
			comments+="TestPass Status Test Steps Pie Chart is not displayed(PASS). ";
		}
		else
		{
			fail=true;
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TPPieChartDispalyed");
			APP_LOGS.debug("TestPass Status Test Steps Pie Chart is displayed(FAIL).");
			comments+="TestPass Status Test Steps Pie Chart is displayed(FAIL).";
		}
			
		//Message verification in Test Pass Status Section
		if(!verifyNoTestStepAvailMsgTestPass())
		{
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepAvailableMsgNotFound");
			closeBrowser();
			throw new SkipException("No Test Steps available Message not found(FAIL).");
		}
	}
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}		
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
		
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
}
			
					