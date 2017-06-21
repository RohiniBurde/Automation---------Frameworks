package com.uat.suite.dashboard;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.org.apache.xerces.internal.impl.dv.xs.MonthDayDV;
import com.uat.base.Credentials;
import com.uat.util.ErrorUtil;
import com.uat.util.TestUtil;
@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyDashboardPageContent extends TestSuiteBase{
//Ekta Unnecessary variables defined
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	static boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	int portfolioFlag=0;
	int totalPages;
	int projectTestPassSummaryRows;
	String projectCell,versionCell,testPassCell,roleCell,actionCell;
	int daysLeft,notCompletedCount,passCount,failCount;
	String actual;
	String groupTestPassSummary;
		
	@BeforeTest
	public void checkTestSkip(){
		APP_LOGS.debug("Begining the Verification of the Dashboard Page Content");
		System.out.println("Begining the Verification of the Dashboard Page Content");
		
		if(!TestUtil.isTestCaseRunnable(dashboardSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(dashboardSuiteXls, this.getClass().getSimpleName());
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();				
	}
	
	@Test(dataProvider="getTestData")
	public void VerifyDashboardPageContents1(String Role, String GroupName, String Portfolio, String Project, String Version,
			String VersionLead,String EndMonth,String EndYear,String EndDate,String TestPassNameI,String TestPassNameII, 
			String TestManager,String TP_Tester,String TesterRoleCreation,String TesterRoleSelection, String TestCaseI, 
			String TestCaseII, String TestStepI, String TestStepII,String TestStepExpRes) throws Exception
	{

		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		comments="";
		
		int tester_count = Integer.parseInt(TP_Tester);
		tester = getUsers("Tester", tester_count);
		
		int testManager_count = Integer.parseInt(TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		APP_LOGS.debug(" Executing Test Case -> Verify Dashboard Page Contents...");
		System.out.println(" Executing Test Case -> Verify Dashboard Page Contents...");				
		
		openBrowser();
		APP_LOGS.debug("Opening Browser... ");
		System.out.println("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		
		if(isLoginSuccess)
		{
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug("Clicking On Test Management Tab (PASS).");
			System.out.println("Clicking On Test Management Tab (PASS).");
			
		
			//Step 2:  clicking on Project Tab
			getElement("TM_projectsTab_Id").click();
			APP_LOGS.debug("Clicking On Project Tab  (PASS).");
			System.out.println("Clicking On Project Tab  (PASS).");
			Thread.sleep(1000);
			
			//Project creation
			if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Project creation is not done successfully (FAIL).");
				comments=comments+"Project creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project not created successfully");//reports
			}
			
			APP_LOGS.debug("Project successfully created (PASS).");
			
			// Test Pass creation 1st
			if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassNameI, EndMonth, EndYear, EndDate, testManager.get(0).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation is not done successfully (FAIL).");
				comments=comments+"Test Pass Creation is not done successfully Fails (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
			
			APP_LOGS.debug("First Test Pass successfully created (PASS).");
				
			// Test Pass creation 2nd
			if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassNameII, EndMonth, EndYear, EndDate, testManager.get(1).username))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation is not done successfully (FAIL).");
				comments=comments+"Test Pass Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
			
			APP_LOGS.debug("Second Test Pass successfully created (PASS).");
				
			// Tester creation for 1st Test Pass
			if(!createTester(GroupName, Portfolio, Project, Version, TestPassNameI, tester.get(0).username, TesterRoleCreation, TesterRoleSelection))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester Creation is not done successfully (FAIL).");
				comments=comments+"Tester Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
			}
			
			APP_LOGS.debug("Tester created successfully for TP I (PASS).");
				
			// Tester creation for 2nd Test Pass
			if(!createTester(GroupName, Portfolio, Project, Version, TestPassNameII, tester.get(0).username, TesterRoleCreation, TesterRoleSelection))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester Creation is not done successfully (FAIL).");
				comments=comments+"Tester Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
			}
			
			APP_LOGS.debug("Tester created successfully for TP II (PASS).");
				
			// Test Case creation for Ist Test Pass
			if(!createTestCase(GroupName, Portfolio, Project, Version, TestPassNameI, TestCaseI))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Case Creation is not done successfully for TP I (FAIL).");
				comments=comments+"Test Case Creation is not done successfully for TP I (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case TP I is not created successfully");//reports
			}
			
			APP_LOGS.debug("Test Case created successfully for TP I (PASS).");
				
				
			// Test Case creation for IInd Test Pass
			if(!createTestCase(GroupName, Portfolio, Project, Version, TestPassNameII, TestCaseII))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Case Creation is not done successfully for TP II (FAIL).");
				comments=comments+"Test Case Creation is not done successfully for TP II (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case TP II is not created successfully");//reports
			}

			APP_LOGS.debug("Test Case created successfully for TP II (PASS).");
				
			// Test Step 1 creation for Ist Test Pass
			if(!createTestStep(GroupName, Portfolio, Project, Version, TestPassNameI,TestStepI, TestStepExpRes, TestCaseI, TesterRoleSelection))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Step Creation is not done successfully for TP I (FAIL).");
				comments=comments+"Test Step Creation is not done successfully for TP I (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step TP I is not created successfully");//reports
			}

			APP_LOGS.debug("Test Step created successfully for TP I (PASS).");
				
			// Test Step creation for IInd Test Pass
			if(!createTestStep(GroupName, Portfolio, Project, Version, TestPassNameII, TestStepII, TestStepExpRes, TestCaseII, TesterRoleSelection))
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Step Creation is not done successfully for TP II (FAIL).");
				comments=comments+"Test Step Creation is not done successfully for TP II (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step TP II is not created successfully");//reports
			}

			APP_LOGS.debug("Test Step created successfully for TP II (PASS).");
					
			APP_LOGS.debug("Entire Project created successfully (PASS).");
			comments=comments+"Entire Project created successfully (PASS).";
			
			closeBrowser();
			
			// Version Lead**************************************************************
			openBrowser();
			APP_LOGS.debug("*****Opening Browser for other than tester*****");
			System.out.println("*****Opening Browser for versionLead*****");
			
			//The main purpose of test case is not served. There is no validation to check if the right person sees the right thing.
			
			
			if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
			{
				Thread.sleep(1000);
				//Ekta comments have lot of grammatical mistakes
				//Ekta there should have been a try catch block
				if(!compareStrings(resourceFileConversion.getProperty("Dashboard_pageHeadingText"),getObject("Dashboard_pageHeaderText").getText()))
				{
					fail=true;
					APP_LOGS.debug("User is landing on other than Dashboard page (FAIL).");
					comments=comments+"User is landing on other than Dashboard page (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardPageLoginFailed");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is landing on other than dashboard page");//reports
				}
				
				APP_LOGS.debug("User is landing on Dashboard page successfully (PASS).");
				
				//*****************************when project and Test pass status div is visible***********************************
				if(isElementExistsByXpath("Dashboard_projectStatusAndTestPassDiv"))
				{
					//Project Status div
					Thread.sleep(1000);
					if(getObject("Dashboard_projectStatusDiv").isDisplayed())
					{
						//Is project status text is visible???
						if(!compareStrings(resourceFileConversion.getProperty("Dashboard_projectStatusText"),getObject("Dashboard_projectStatusHeading").getText().trim()))
						{
							fail=true;
							APP_LOGS.debug("Project Status section text is not visible (FAIL).");
							comments=comments+"Project Status section text is not visible (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardProjectStatusTextNotVisible");
						}
						else
						{
							APP_LOGS.debug("Project Status section text is visible (PASS).");
							comments=comments+"Project Status section text is visible (PASS).";
						}
						
						//Is portfolio drop down visible???
						if(getElement("DashboardProjectStatus_portfolio_Id").isDisplayed())
						{
							APP_LOGS.debug("Project Status - Portfolio drop down is visible (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusPortfolioNotVisible");
							APP_LOGS.debug("Project Status - Portfolio drop down is not visible (FAIL).");
							comments=comments+"Project Status - Portfolio drop down is not visible (FAIL).";
						}
						
						//Is project drop down visible???
						if(getObject("DashboardProjectStatus_project").isDisplayed())
						{
							APP_LOGS.debug("Project Status - Project drop down is visible (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusProjectNotVisible");
							APP_LOGS.debug("Project Status - Project drop down is not visible (FAIL).");
							comments=comments+"Project Status - Project drop down is not visible (FAIL).";
						}
						
						//Is version drop down visible???
						if(getElement("DashboardProjectStatus_version_Id").isDisplayed())
						{
							APP_LOGS.debug("Project Status - Version drop down is visible (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusVersionNotVisible");
							APP_LOGS.debug("Project Status - Version drop down is not visible (FAIL).");
							comments=comments+"Project Status - Version drop down is not visible (FAIL).";
						}
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Project Status section is not visible (FAIL).");
						comments=comments+"Project Status section is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardProjectStatusSectionNotVisible");
						closeBrowser();
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see project status section");
					}
					
					//Test Pass div
					if(getObject("Dashboard_testPassStatusDiv").isDisplayed())
					{
						//Is test pass status text visible???
						if(!compareStrings(resourceFileConversion.getProperty("Dashboard_testPassStatusText"), getObject("Dashboard_testPassStatusHeading").getText().trim()))
						{
							fail=true;
							APP_LOGS.debug("Test Pass Status section text is not visible (FAIL).");
							comments=comments+"Test Pass Status section text is not visible (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassStatusTextNotVisible");
						}
						else
						{
							APP_LOGS.debug("Test Pass Status section text is visible (PASS).");
						}
						
						//Is test pass drop down is visible???
						if(getObject("Dashboard_testPassStatus_testPassDropDown").isDisplayed())
						{
							APP_LOGS.debug("Test Pass Status - Test Pass drop down is visible (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassStatusDropDownNotVisible");
							APP_LOGS.debug("Test Pass Status - Test Pass drop down is not visible (FAIL).");
							comments=comments+"Test Pass Status - Test Pass drop down is not visible (FAIL).";
						}
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Pass Status section is not visible (FAIL).");
						comments=comments+"Test Pass Status section is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassStatusSectionNotVisible");
						closeBrowser();
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see test pass status section");
					}
					
					//Project Status - portfolio drop down selection
					List<WebElement> portfolioDD = getElement("Dashboard_portfolioDD_ID").findElements(By.tagName("option"));
					for(int i=0; i<portfolioDD.size(); i++)
					{
						if(portfolioDD.get(i).getText().equals(Portfolio))
						{
							Thread.sleep(1000);
							portfolioDD.get(i).click();
							APP_LOGS.debug("Portfolio is found (PASS).");
							comments=comments+"Portfolio is found (PASS).";
							portfolioFlag++;
							break;							
						}
					}
					
					if(portfolioFlag==1)
					{
						//Project Status - project, version and test pass drop down selection.Buttons verification
						
						List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
						for(int i=0; i<projectDD.size(); i++)
						{
							if(projectDD.get(i).getText().equals(Project))
							{
								Thread.sleep(1000);
								projectDD.get(i).click();
								APP_LOGS.debug("Project is found (PASS).");
								comments=comments+"Project is found (PASS).";
								break;							
							}
							APP_LOGS.debug("Project is not found (FAIL).");
							comments=comments+"Project is not found (FAIL).";
						}
						
						List<WebElement> versionDD = getElement("Dashboard_versionDD_ID").findElements(By.tagName("option"));
						for(int i=0; i<versionDD.size(); i++)
						{
							if(versionDD.get(i).getText().equals(Version))
							{
								Thread.sleep(1000);
								versionDD.get(i).click();
								APP_LOGS.debug("Version is found (PASS).");
								comments=comments+"Version is found (PASS).";
								break;							
							}
							APP_LOGS.debug("Version is not found (FAIL).");
							comments=comments+"Version is not found (FAIL).";
						}
						
						List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
						if(compareStrings(TestPassNameI, testPassDD.get(0).getAttribute("title")))
						{
							Thread.sleep(1000);
							testPassDD.get(0).click();
							APP_LOGS.debug("Test Pass I is found (PASS).");
							comments=comments+"Test Pass I is found (PASS).";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Test Pass I is not found (FAIL).");
							comments=comments+"Test Pass I is not found (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassINotFound");
						}
						if(compareStrings(TestPassNameII, testPassDD.get(1).getAttribute("title")))
						{
							Thread.sleep(1000);
							testPassDD.get(1).click();
							APP_LOGS.debug("Test Pass II is found (PASS).");
							comments=comments+"Test Pass II is found (PASS).";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Test Pass II is not found (FAIL).");
							comments=comments+"Test Pass II is not found (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassIINotFound");
						}
							
													
						if(!isElementExistsByXpath("Dashboard_detailedAnalysis"))
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Detailed Analysis button is not found (FAIL).");
							comments=comments+"Detailed Analysis button is not found (FAIL).";
						}
						else
						{
							APP_LOGS.debug("Detailed Analysis button is found (PASS).");
							comments=comments+"Detailed Analysis button is found (PASS).";
						}
						
						if(!isElementExistsById("Dashboard_nuberOfTesterParticipatedButton_ID"))
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Number of Tester button is not found (FAIL).");
							comments=comments+"Number of Tester button is not found (FAIL).";
						}
						else
						{
							APP_LOGS.debug("Number of Tester button is found (PASS).");
							comments=comments+"Number of Tester button is found (PASS).";
						}
						
						if(isElementExistsByXpath("Dashboard_triageButton"))
						{
							APP_LOGS.debug("Triage button is not found (PASS).");
							comments=comments+"Triage button is not found (PASS).";
						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Triage button is found (FAIL).");
							comments=comments+"Triage button is found (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageButtonIsVisible");
						}
						
						
						APP_LOGS.debug("Project Status and Test Pass status is found and verified (PASS).");
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Portfolio is not found (FAIL).");
						comments=comments+"Portfolio is not found (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PortfolioNotFound");
					}
				}
				
				//*****************************when My Activity and Testing status div is visible***********************************
				else
				{
					//when my activity and testing status is visible
					if(getObject("Dashboard_myActivityAndTesingStatusDiv").isDisplayed())
					{
						//my activity text and find project in my activity
						if(!myActivityDataVerify(Project, Version, TestPassNameI, TesterRoleSelection))
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("User fails to see the project in My Activity Area (FAIL).");
							comments+="User fails to see the project in My Activity Area (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotContainsProject");
						}
						else
						{
							APP_LOGS.debug("User is able to see the My Activity Area and also the Project is visible(PASS).");
							comments+="User is able to see the My Activity Area and also the Project is visible(PASS).";
						}
						
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("User fails to see the My Activity Area (FAIL).");
						comments+="User fails to see the My Activity Area (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotVisible");
						closeBrowser();
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see my activity");
					}
						
					
					if(getObject("Dashboard_tesingStatusDiv").isDisplayed())
					{
						//testing status section div
						if(!compareStrings(resourceFileConversion.getProperty("Dashboard_testingStatusHead"),getObject("DashboardTestingStatus_testingStatusHeading").getText()))
						{
							fail=true;
							APP_LOGS.debug("Testing Status Is not showing (FAIL).");
							comments=comments+"Testing Status Is not showing (FAIL).";
						}
						else
						{
							APP_LOGS.debug("Testing Status Is showing (PASS).");
							comments=comments+"Testing Status Is showing (PASS).";
						}
						
						//Testing Status - project drop down
						if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",1).isDisplayed())
						{
							APP_LOGS.debug("Testing Status - Project drop down is showing (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusProjectNotVisible");
							APP_LOGS.debug("Testing Status - Project drop down is not visible (FAIL).");
							comments=comments+"Testing Status - Project drop down is not visible (FAIL).";
						}

						//Testing Status - version drop down
						if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",2).isDisplayed())
						{
							APP_LOGS.debug("Testing Status - Version drop down is showing (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusVersionNotVisible");
							APP_LOGS.debug("Testing Status - Version drop down is not visible (FAIL).");
							comments=comments+"Testing Status - Version drop down is not visible (FAIL).";
						}
						
						//Testing Status - test pass drop down
						if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",3).isDisplayed())
						{
							APP_LOGS.debug("Testing Status - Test Pass drop down is showing (PASS).");
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusTestPassNotVisible");
							APP_LOGS.debug("Testing Status - Test Pass drop is not visible (FAIL).");
							comments=comments+"Testing Status - Test Pass drop is not visible (FAIL).";
						}
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Testing Status Section is not showing (FAIL).");
						comments=comments+"Testing Status Section is not showing (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusSectionNotVisible");
						closeBrowser();
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see testing status section");
					}
						
					List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
					for(int i=0; i<projectDD.size(); i++)
					{
						if(projectDD.get(i).getAttribute("title").equals(Project))
						{
							Thread.sleep(1000);
							projectDD.get(i).click();
							APP_LOGS.debug("Project is found (PASS).");
							comments=comments+"Project is found (PASS).";
							break;							
						}
					}
					
					List<WebElement> versionDD = getElement("DashboardTestingStatus_versionDD").findElements(By.tagName("option"));
					for(int i=0; i<versionDD.size(); i++)
					{
						if(versionDD.get(i).getText().equals(Version))
						{
							Thread.sleep(1000);
							versionDD.get(i).click();
							APP_LOGS.debug("Version is found (PASS).");
							comments=comments+"Version is found (PASS).";
							break;							
						}
					}
					
					List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
					if(compareStrings(TestPassNameI, testPassDD.get(0).getAttribute("title")))
					{
						Thread.sleep(1000);
						testPassDD.get(0).click();
						APP_LOGS.debug("Test Pass I is found (PASS).");
						comments=comments+"Test Pass I is found (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Test Pass I is not found (FAIL).");
						comments=comments+"Test Pass I is not found (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassINotFound");
					}
					if(compareStrings(TestPassNameII, testPassDD.get(1).getAttribute("title")))
					{
						Thread.sleep(1000);
						testPassDD.get(1).click();
						APP_LOGS.debug("Test Pass II is found (PASS).");
						comments=comments+"Test Pass II is found (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Test Pass II is not found (FAIL).");
						comments=comments+"Test Pass II is not found (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassIINotFound");
					}
				}
				
				
				if(getObject("Dashboard_projectAndTestPassSummaryDiv").isDisplayed())
				{
					if(!compareStrings(resourceFileConversion.getProperty("Dashboard_expProjectAndTestPassSummaryText"), getObject("Dashboard_projectAndTestPassSummaryText").getText()))
					{
						fail=true;
						APP_LOGS.debug("Project and Test Pass Summary text is not matched (FAIL).");
						comments=comments+"Project and Test Pass Summary text is not matched (FAIL).";
					}
					else
					{
						APP_LOGS.debug("Project and Test Pass Summary text is matched (PASS).");
					}
					
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					Date date = new Date();
					System.out.println(dateFormat.format(date));
					String dateStart=dateFormat.format(date);
					
					if(!projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameI, dateStart, TestPassNameII, EndDate, EndYear, EndMonth))
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectInProjectAndTestPassSummaryNotFound");
						APP_LOGS.debug("Project in Project and Test Pass Summary is not found (FAIL).");
						comments=comments+"Project in Project and Test Pass Summary is not found (FAIL).";
					}
					else
					{
						APP_LOGS.debug("Project in Project and Test Pass Summary is found and verified (PASS).");
					}
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Project and Test Pass Summary is not found (FAIL).");
					comments=comments+"Project and Test Pass Summary is not found (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectAndTestPassSummaryNotFound");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see project and test pass summary");
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Login for Version Lead is Unsuccessfull (FAIL).");
				comments=comments+"Login for Version Lead is Unsuccessfull (FAIL).";
			}
			APP_LOGS.debug("*****Closing browser for version Lead*****");
			
			//Ekta session id is null exception would be throws if test manager login fails because login function itself closes the browser
			closeBrowser();
			
			// Tester**************************************************************
			openBrowser();
			APP_LOGS.debug("*****Opening Browser for tester*****");
			System.out.println("*****Opening Browser for Tester*****");
			
			if(login(tester.get(0).username,tester.get(0).password,"Tester"))
			{
				// My Activity Area and Testing Status area
				if(getObject("Dashboard_myActivityAndTesingStatusDiv").isDisplayed())
				{
					//my activity text and find project in my activity
					if(!myActivityDataVerify(Project, Version, TestPassNameI, TesterRoleSelection))
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("User fails to see the project in My Activity Area (FAIL).");
						comments+="User fails to see the project in My Activity Area (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotContainsProject");
					}
					else
					{
						APP_LOGS.debug("User is able to see the My Activity Area and also the Project is visible(PASS).");
						comments+="User is able to see the My Activity Area and also the Project is visible(PASS).";
					}
					
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("User fails to see the My Activity Area (FAIL).");
					comments+="User fails to see the My Activity Area (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotVisible");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see My Activity area");
				}
					
				
				if(getObject("Dashboard_tesingStatusDiv").isDisplayed())
				{
					//testing status section div
					if(!compareStrings(resourceFileConversion.getProperty("Dashboard_testingStatusHead"),getObject("DashboardTestingStatus_testingStatusHeading").getText()))
					{
						fail=true;
						APP_LOGS.debug("Testing Status section Is not showing (FAIL).");
						comments=comments+"Testing Status section Is not showing (FAIL).";
					}
					else
					{
						APP_LOGS.debug("Testing Status section is showing (PASS).");
						comments=comments+"Testing Status section is showing (PASS).";
					}
					
					//Testing Status Section  - project drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",1).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Project drop down is showing (PASS).");
					}
					else
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusProjectNotVisible");
						APP_LOGS.debug("Testing Status - Project drop down is not visible (FAIL).");
						comments=comments+"Testing Status - Project drop down is not visible (FAIL).";
					}

					//Testing Status Section - version drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",2).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Version drop down is showing (PASS).");
					}
					else
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusVersionNotVisible");
						APP_LOGS.debug("Testing Status - Version drop down is not visible (FAIL).");
						comments=comments+"Testing Status - Version drop down is not visible (FAIL).";
					}
					
					//Testing Status Section - test pass drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",3).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Test Pass drop down is showing (PASS).");
					}
					else
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusTestPassNotVisible");
						APP_LOGS.debug("Testing Status - Test Pass drop is not visible (FAIL).");
						comments=comments+"Testing Status - Test Pass drop is not visible (FAIL).";
					}
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Testing Status Section is not showing (FAIL).");
					comments=comments+"Testing Status Section is not showing (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusSectionNotVisible");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see testing status section");
				}
					
				List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
				for(int i=0; i<projectDD.size(); i++)
				{
					if(projectDD.get(i).getAttribute("title").equals(Project))
					{
						Thread.sleep(1000);
						projectDD.get(i).click();
						APP_LOGS.debug("Project is found (PASS).");
						comments=comments+"Project is found (PASS).";
						break;							
					}
				}
				
				List<WebElement> versionDD = getElement("DashboardTestingStatus_versionDD").findElements(By.tagName("option"));
				for(int i=0; i<versionDD.size(); i++)
				{
					if(versionDD.get(i).getText().equals(Version))
					{
						Thread.sleep(1000);
						versionDD.get(i).click();
						APP_LOGS.debug("Version is found (PASS).");
						comments=comments+"Version is found (PASS).";
						break;							
					}
				}
				
				List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
				if(compareStrings(TestPassNameI, testPassDD.get(0).getAttribute("title")))
				{
					Thread.sleep(1000);
					testPassDD.get(1).click();
					APP_LOGS.debug("Test Pass I is found (PASS).");
					comments=comments+"Test Pass I is found (PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Test Pass I is not found (FAIL).");
					comments=comments+"Test Pass I is not found (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassINotFound");
				}
				
				if(compareStrings(TestPassNameII, testPassDD.get(1).getAttribute("title")))
				{
					Thread.sleep(1000);
					testPassDD.get(0).click();
					APP_LOGS.debug("Test Pass II is found (PASS).");
					comments=comments+"Test Pass II is found (PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Test Pass II is not found (FAIL).");
					comments=comments+"Test Pass II is not found (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassIINotFound");
				}
				
				if(!isElementExistsByXpath("Dashboard_detailedAnalysis"))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Detailed Analysis button is not found (FAIL).");
					comments=comments+"Detailed Analysis button is not found (FAIL).";
				}
				else
				{
					APP_LOGS.debug("Detailed Analysis button is found (PASS).");
					comments=comments+"Detailed Analysis button is found (PASS).";
				}
				
				// Project and Test Pass summary
				if(getObject("Dashboard_projectAndTestPassSummaryDiv").isDisplayed())
				{
					
					if(!compareStrings(resourceFileConversion.getProperty("Dashboard_expProjectAndTestPassSummaryText"), getObject("Dashboard_projectAndTestPassSummaryText").getText()))
					{
						fail=true;
						APP_LOGS.debug("Project and Test Pass Summary text is not matched (FAIL).");
						comments=comments+"Project and Test Pass Summary text is not matched (FAIL).";
					}
					else
					{
						APP_LOGS.debug("Project and Test Pass Summary text is matched (PASS).");
					}
					
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					Date date = new Date();
					System.out.println(dateFormat.format(date));
					String dateStart=dateFormat.format(date);
					
					if(!projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameI, dateStart, TestPassNameII, EndDate, EndYear, EndMonth))
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectInProjectAndTestPassSummaryNotFound");
						APP_LOGS.debug("Project in Project and Test Pass Summary is not found (FAIL).");
						comments=comments+"Project in Project and Test Pass Summary is not found (FAIL).";
					}
					else
					{
						APP_LOGS.debug("Project in Project and Test Pass Summary is found and verified (PASS).");
						comments=comments+"Project in Project and Test Pass Summary is found and verified (PASS).";
					}
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Project and Test Pass Summary section is not found (FAIL).");
					comments=comments+"Project and Test Pass Summary section is not found (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectAndTestPassSummaryNotFound");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as user is not able to see project and test pass Summary");//reports
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Login for Tester is Unsuccessfull (FAIL).");
				comments=comments+"Login for Tester is Unsuccessfull (FAIL).";
			}
			APP_LOGS.debug("*****Closing browser for Tester*****");
			closeBrowser();
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Login Unsuccessfull (FAIL).");
			comments=comments+"Login Unsuccessfull (FAIL).";
		}
	}
	
	
	
	
	public boolean isElementExistsByXpath(String key)
    {
		try
		{
			eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
			return true;
		}
		catch(Throwable t)
		{
			return false;
		}
    }
    
	

	
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
		skip=false;
		fail=false;
	}

	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	public boolean isElementExistsById(String key)
    {
		 try
		 {
			  eventfiringdriver.findElement(By.id(OR.getProperty(key))).isDisplayed();
			  return true;
		 }
	     catch(Throwable t)
	     {
	    	 return false;
	     }
    }
	
	
	private boolean myActivityDataVerify(String Project,String Version,String TestPassName,String TesterRole)
	{
		//Ekta The loop here will keep running even if the project is found and will keep clicking next
		
		//Ekta Even if the project is not found, false will not be returned
		try
		{
			
			if(compareStrings(resourceFileConversion.getProperty("Dashboard_myActivityHead"), getObject("DashboardMyActivity_myActivityHeading").getText()))
			{
				APP_LOGS.debug("My Activity text is matched (PASS).");
			}
			else
			{
				fail=true;
				APP_LOGS.debug("My Activity text is not matched (FAIL).");
				comments=comments+"My Activity text is not matched (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivityTextNotMatch");
			}
			
		   //function call MyActPagination
		   if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
		   {
			    APP_LOGS.debug("Only 1 page available on View All page.");
			    totalPages=1;
		   }
		   else 
		   {
			    APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
			    totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
		   }
		   for (int i = 0; i < totalPages; i++) 
		   {
			   int myActivityTableRows = getElement("DashboardMyActivity_table_Id").findElements(By.tagName("tr")).size();
			   for (int j = 1; j <= myActivityTableRows; j++) 
			   {
					projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
				    versionCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
				    testPassCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
				    roleCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");
				    daysLeft = Integer.parseInt(getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_daysLeftColumn2", j).getText());
				    notCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());
				    passCount = Integer.parseInt(getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_passCountColumn2", j).getText());
				    failCount = Integer.parseInt(getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_failCountColumn2", j).getText());
				    actionCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_actionColumn2", j).getText();
			       
			     if (projectCell.equals(Project) && versionCell.equals(Version) && testPassCell.equals(TestPassName) && roleCell.equals(TesterRole)) 
			     {
			    	APP_LOGS.debug("In My Activity Section project found (PASS).");
					comments=comments+"In My Activity Section proper found (PASS).";
			     }
			  }
			   
			   if (totalPages>1&&!(i==totalPages-1)) 
				{
				   getObject("DashboardProjectTestPassSummary_nextLink").click();
				   Thread.sleep(500);
				}
		   }
		   APP_LOGS.debug("My Activity block is executed (PASS).");
		}
		catch (Throwable e) {
			e.printStackTrace();
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("My Activity Is not showing proper Data (FAIL).");
			comments=comments+"My Activity Is not showing proper Data (FAIL).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivityNotContainProject");
			return false;
		}
		return true;
	}
	
	
	public boolean projectTestPassSummary(String GroupName,String Portfolio,String Project,String Version,
			String TestPassNameI,String dateStart,String TestPassNameII,String EndDate,String EndYear,String EndMonth)
	{
		
		//Ekta does not check if the project is not found. This would return true even if project si not found
		try 
		{
			Thread.sleep(200);
			if (getElement("Dashboard_projectTestPassSummary_ID").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				totalPages=1;
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
				totalPages = getElement("Dashboard_projectTestPassSummary_ID").findElements(By.xpath("div/a")).size();
			}
			
			for (int i = 0; i < totalPages; i++) 
			{
				//entering into project and Test Pass summary
				projectTestPassSummaryRows = getObject("Dashboard_projectTestPassSummaryTable").findElements(By.tagName("tr")).size();
				for (int j = 1; j <= projectTestPassSummaryRows; j++) 
				{
					List<String> projectDetails=new ArrayList<String>();
					groupTestPassSummary=getElement("Dashboard_projectTestPassSummary_group_Id").findElements(By.tagName("tr")).get(j-1).getAttribute("group");
					
					if(groupTestPassSummary.equals(GroupName))
					{
						int countFlagForProject=0;
						int groupTdSize=getObject("Dashboard_projectTestPassSummaryTable").findElements(By.xpath("tr["+j+"]/td")).size();
						if(getObject("Dashboard_projectTestPassSummaryTable").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(0).getAttribute("title").equals(Portfolio)
								|| getObject("Dashboard_projectTestPassSummaryTable").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(1).getAttribute("title").equals(Portfolio))
						{
							for(int td=0;td<groupTdSize;td++)
							{
								Thread.sleep(200);
								if(td>=groupTdSize-3)
								actual = getObject("Dashboard_projectTestPassSummaryTable").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getText();
								else
								actual = getObject("Dashboard_projectTestPassSummaryTable").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getAttribute("title");
								projectDetails.add(actual);
								countFlagForProject++;
							}
						}
						
						if(countFlagForProject>0)
						{
							if(projectDetails.size()==9)
							{
								String testManagerReplace = (testManager.get(0).username).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(GroupName) 
									&& projectDetails.get(1).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(2).equalsIgnoreCase(Project)  
									&& projectDetails.get(3).equalsIgnoreCase(Version) 
									&& projectDetails.get(4).equalsIgnoreCase(TestPassNameI) 
									&& projectDetails.get(5).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(6).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(7).equalsIgnoreCase(dateStart)
									&& projectDetails.get(8).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
									{
										APP_LOGS.debug("Project in Project and Test Pass Summary is found (PASS).");
									}	
							}
							else
							{
								String testManagerReplace = (testManager.get(1).username).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(1).equalsIgnoreCase(Project)  
									&& projectDetails.get(2).equalsIgnoreCase(Version) 
									&& projectDetails.get(3).equalsIgnoreCase(TestPassNameII) 
									&& projectDetails.get(4).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(5).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(6).equalsIgnoreCase(dateStart)
									&& projectDetails.get(7).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
									{
										APP_LOGS.debug("Project in Project and Test Pass Summary is found (PASS).");
									}	
							}
						}
					}
				}          	
					
				if (totalPages>1&&!(i==totalPages-1)) 
				{
					getObject("DashboardProjectTestPassSummary_nextLink").click();
					Thread.sleep(500);
				}
			}
			
		} 
		catch (Throwable e) 
		{
			fail=true;
			APP_LOGS.debug("Project in Project and Test Pass Summary is not found (FAIL).");
			comments=comments+"Project in Project and Test Pass Summary is not found (FAIL).";
			return false;
		}
		return true;
	}
	
	private String getMonthNumber(String month)
	{
		switch (month) {
		case "Jan":
			return "01";
		case "Feb":
			return "02";
		case "Mar":
			return "03";
		case "Apr":
			return "04";
		case "May":
			return "05";
		case "Jun":
			return "06";
		case "Jul":
			return "07";
		case "Aug":
			return "08";
		case "Sep":
			return "09";
		case "Oct":
			return "10";
		case "Nov":
			return "11";
		case "Dec":
			return "12";

		default:
			return null;
		}
	}
	
}
