package com.uat.suite.dashboard;

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
public class VerifyTriageLink_ExceptTester extends TestSuiteBase
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
	int totalPages;
	boolean portfolioFlag=true;
	boolean triageButtonFound=false;
	String projectCell,versionCell,testPassCell,roleCell,actionCell;

	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
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
	}
			
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void VerifyTriageLink_ExceptTester1(String Role, String GroupName, String Portfolio, String Project, String Version,
			String VersionLead,String EndMonth,String EndYear,String EndDate,String testPassName1,String testPassName2, 
			String TestManager,String TP_Tester,String TesterRole, String testCase1, String testCase2, String testStep1, 
			String testStep2,String TestStepExpRes,String TestStepResult) throws Exception
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
		APP_LOGS.debug("Opening Browser... ");
				
		//Step 1 Login as a Tester
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
			
				// Test Pass creation 1st
				if(!createTestPass(GroupName, Portfolio, Project, Version, testPassName1, EndMonth, EndYear, EndDate, testManager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName1+" Creation fails(FAIL).");
					comments+=testPassName1+" Creation Fails(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass1");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass "+testPassName1+"is not created successfully");//reports
				}
				APP_LOGS.debug("First Test Pass successfully created(PASS).");
			
				// Test Pass creation 2nd
				if(!createTestPass(GroupName, Portfolio, Project, Version, testPassName2, EndMonth, EndYear, EndDate, testManager.get(0).username))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testPassName2+" Creation fails(FAIL).");
					comments+=testPassName2+" Creation fails(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass2");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass"+testPassName2+" is not created successfully");//reports
				}
				APP_LOGS.debug("Second Test Pass successfully created(PASS).");
			
				// Tester creation for 1st Test Pass
				if(!createTester(GroupName, Portfolio, Project, Version, testPassName1, tester.get(0).username, TesterRole, TesterRole))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester Creation fails for Test Pass1(FAIL).");
					comments+="Tester Creation fails for Test Pass1(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
				}
				APP_LOGS.debug("Tester created successfully for Test Pass1(PASS).");
			
				// Tester creation for 2nd Test Pass
				if(!createTester(GroupName, Portfolio, Project, Version, testPassName2, tester.get(0).username, TesterRole, TesterRole))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Tester Creation fails for Test Pass2(FAIL).");
					comments+="Tester Creation fails for Test Pass2(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
				}
				APP_LOGS.debug("Tester created successfully for Test Pass2(PASS).");
			
				// Test Case creation for 1st Test Pass
				if(!createTestCase(GroupName, Portfolio, Project, Version, testPassName1, testCase1))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testCase1+" Creation fails for Test Pass1(FAIL).");
					comments+=testCase1+" Creation Fails for Test Pass1(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase1");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case"+testCase1+" is not created successfully");//reports
				}
				APP_LOGS.debug(testCase1+" created successfully for Test Pass1(PASS).");
			
				// Test Case creation for 2nd Test Pass
				if(!createTestCase(GroupName, Portfolio, Project, Version, testPassName2, testCase2))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testCase2+" Creation fails for Test Pass2(FAIL).");
					comments+=testCase2+" Creation Fails for Test Pass2(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase2");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case"+testCase2+" is not created successfully");//reports
				}
				APP_LOGS.debug(testCase2+" created successfully for Test Pass2(PASS).");
			
				// Test Step 1 creation for 1st Test Pass
				if(!createTestStep(GroupName, Portfolio, Project, Version, testPassName1, testStep1, TestStepExpRes, testCase1, TesterRole))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testStep1+" Creation fails for Test Pass1(FAIL).");
					comments+=testStep1+" Creation Fails for Test Pass1(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep1");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step"+testStep1+" of Test Pass1 is not created successfully");//reports
				}
				APP_LOGS.debug("Test Step created successfully for TP I (PASS).");
				
				// Test Step 2 creation for 2nd Test Pass
				if(!createTestStep(GroupName, Portfolio, Project, Version, testPassName2, testStep2, TestStepExpRes, testCase2, TesterRole))
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug(testStep2+" Creation fails for Test Pass2(FAIL).");
					comments+=testStep2+" Creation Fails for Test Pass2(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep2");
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step "+testStep2+" of Test Pass2 is not created successfully");//reports
				}
				APP_LOGS.debug(testStep2+" created successfully for Test Pass2(PASS).");
				
			
			closeBrowser();
				
			APP_LOGS.debug("Opening Browser and Login as Tester to perform testing");
			openBrowser();
				
			if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			{
				//Verifying Project is Availability to the Tester and perform testing
				if(myActivityDataVerify(Project, Version, testPassName1, TesterRole, "Fail", testStep1))
				{
					APP_LOGS.debug("Testing done successfully for test pass "+testPassName1+" (PASS)");
					comments+="Testing done successfully for test pass "+testPassName1+" (PASS). ";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Testing not done successfully for test pass "+testPassName1+" (FAIL)");
					comments+="Testing not done successfully for test pass "+testPassName1+" (FAIL)";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPass1TestingUnsuccessful");
					throw new SkipException("Testing unsuccessful for test pass "+testPassName1+" so skipping the test case.");
				}
				
				if(myActivityDataVerify(Project, Version, testPassName2, TesterRole, "Pass", testStep2))
				{
					APP_LOGS.debug("Testing done successfully for test pass "+testPassName2+" (PASS)");
					comments+="Testing done successfully for test pass "+testPassName2+" (PASS). ";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Testing not done successfully for test pass "+testPassName2+" (FAIL)");
					comments+="Testing not done successfully for test pass "+testPassName2+" (FAIL)";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPass2TestingUnsuccessful");
					throw new SkipException("Testing unsuccessful for test pass "+testPassName2+" so skipping the test case.");
				}
				
				
				//Selecting Project, Version and Test Pass from dropdown
				dropdownSelection("Dashboard_projectDD_ID","DetailedAnalysis_versionDropDown_Id","Dashboard_testPassDD_ID", Project, VersionLead, testPassName1);
				
				
				//Verify Detailed Analysis button
				APP_LOGS.debug("Verifying Detailed Analysis button for Tester");
				if(assertTrue(isElementExistsByXpath("Dashboard_detailedAnalysis")))
				{
					APP_LOGS.debug("Detailed Analysis button is visible(PASS).");
					comments+="Detailed Analysis button is visible(PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Detailed Analysis button is not visible(FAIL).");
					comments+="Detailed Analysis button is not visible(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DetailedAnalysisButtonNotVisible");
				}

				//Verify Triage button
				APP_LOGS.debug("Verifying Triage button for Tester");
				if(assertTrue(!isElementExistsByXpath("Dashboard_triageButton")))
				{
					APP_LOGS.debug("Triage button is not visible for Tester(PASS).");
					comments+="Triage button is not visible for Tester(PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Triage button is visible for Tester(FAIL).");
					comments+="Triage is visible for Tester(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TriageButtonVisibleForTester");
				}
				closeBrowser();
				
			}
			else
			{
				APP_LOGS.debug("Login unsuccessful for tester "+tester.get(0).username);
				comments+="Login unsuccessful for tester "+tester.get(0).username;
			}
		
			
			
			openBrowser();
			APP_LOGS.debug("Opening Browser... for other than Tester");
			
			if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
			{	
				//Verifying and Selecting Portfolio
				selectPortfolio("Dashboard_projectDD_ID","Dashboard_versionDD_ID","Dashboard_testPassDD_ID",Portfolio, Project, VersionLead, testPassName1);
				
				//Verify Detailed Analysis button
				APP_LOGS.debug("Verifying Detailed Analysis button for Non Tester");
				if(assertTrue(isElementExistsByXpath("Dashboard_detailedAnalysis")))
				{
					APP_LOGS.debug("Detailed Analysis button is visible(PASS).");
					comments+="Detailed Analysis button is visible(PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Detailed Analysis button is not visible(FAIL).");
					comments+="Detailed Analysis button is not visible(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DetailedAnalysisButtonNotVisible");
				}
				
				
				//Verify Triage button
				APP_LOGS.debug("Verifying Triage button for Non Tester");
				if(assertTrue(isElementExistsByXpath("Dashboard_triageButton")))
				{
					APP_LOGS.debug("Triage button is visible for test pass "+testPassName1+" as it is having failed steps(PASS).");
					comments+="Triage button is visible for test pass "+testPassName1+" as it is having failed steps(PASS). ";
					triageButtonFound=true;
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Triage button is not visible for test pass "+testPassName1+" though it is having failed steps(FAIL).");
					comments+="Triage button is not visible for test pass "+testPassName1+" though it is having failed steps(FAIL). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TriageButtonNotVisibleForFailedTestPass");
				}
				
				//Clicking on the Triage button
				if(triageButtonFound==true)
				{
					getObject("Dashboard_triageButton").click();
					Thread.sleep(3000);
					
					//Project 
                    Select projectDefaultText = new Select(getElement("Triage_projectDropDown_Id"));
                    
                    //Version
                    Select versionDefaultText = new Select(getElement("Triage_versionDropDown_Id"));
                    
                    //Test Pass  
                    Select testPassDefaultText = new Select(getElement("Triage_testPassDropDown_Id"));
                    
                    //Tester
                    Select testerDefaultText = new Select(getElement("Triage_testerDropDown_Id"));
                    
                    //Role
                    Select roleDefaultText = new Select(getElement("Triage_testerRoleDropDown_Id"));
                    
                    //Verification of if default selection option displaying in drop down is matching with the sheet data
                    if((assertTrue(projectDefaultText.getFirstSelectedOption().getAttribute("title").equals(Project)))
                         && (assertTrue(versionDefaultText.getFirstSelectedOption().getText().equals(Version)))
                         && (assertTrue(testPassDefaultText.getFirstSelectedOption().getAttribute("title").equals(testPassName1)))
                         && (assertTrue(testerDefaultText.getFirstSelectedOption().getText().equals("All Tester")))
                         && (assertTrue(roleDefaultText.getFirstSelectedOption().getText().equals("All Role"))))
                    {
                    	APP_LOGS.debug("Triage page dropdowns selection is matched with dashboard page content selection(PASS).");
						comments+="Triage page dropdowns selection is matched with dashboard page content selection(PASS). ";
                    }
                    else 
                    {
						fail=true;
						APP_LOGS.debug("Triage page dropdowns selection not matched with dashboard page content selection(FAIL).");
						comments+="Triage page dropdowns selection not matched with dashboard page content selection(FAIL). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TriagePageDetailsNotMatched");
					}
				}  
				
				//Redirecting to dashboard
				getElement("UAT_dashboard_Id").click();
				
				
				//Verifying and Selecting Portfolio
				selectPortfolio("Dashboard_projectDD_ID","Dashboard_versionDD_ID","Dashboard_testPassDD_ID",Portfolio, Project, VersionLead, testPassName2);
				Thread.sleep(1000);
				
				//Verify Triage button
				APP_LOGS.debug("Verify Triage button for Test Pass with Pass Test Step");
				if(isElementExistsByXpath("Dashboard_triageButton"))
				{	
					String displayedTriageLinkText=getObject("Dashboard_triageButton").getText();
					if(assertTrue(!(resourceFileConversion.getProperty("Dashboard_TriageLinkText").equals(displayedTriageLinkText))))
					{
						APP_LOGS.debug("Triage button is not visible for Test Pass "+testPassName2+" (PASS).");
						comments+="Triage button is not visible for Test Pass "+testPassName2+" (PASS). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Triage button is visible for Test Pass "+testPassName2+" though no failed step available(FAIL).");
						comments+="Triage button is visible for Test Pass "+testPassName2+" though no failed step available(FAIL). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TriageButtonVisibleForPassedTestPass");
					}
				}
				 closeBrowser();
 			}
			else
			{
				APP_LOGS.debug("Login Unsuccessfull for Version Lead "+versionLead.get(0).username);
				comments+="Login Unsuccessfull for Version Lead "+versionLead.get(0).username;
			}
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessfull for user "+Role);
			comments+="Login Unsuccessfull for user "+Role;
		}
	}
	
	//My Activity View for Tester
	private boolean myActivityDataVerify(String Project,String Version,String TestPassName,String TesterRole,String testStepResult,String testStep)
	{	
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean testingPerformed = false;
		
		try
		{
			if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				totalPages=1;
				paginationCount=1;
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
			    totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}
			
			nextLinkEnabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
			   int myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
			   for (int j = 1; j <= myActivityTableRows; j++) 
			   {
				 projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
			     versionCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
			     testPassCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
			     roleCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getText();
			     actionCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_actionColumn2", j).getText();
			       
			     if (projectCell.equals(Project) && versionCell.equals(Version) && testPassCell.equals(TestPassName) && roleCell.equals(TesterRole)) 
			     {
			    	//if project exist then do testing through tester and fail some test Step
			    	if(compareStrings("Begin Testing", actionCell))
					{
                    	APP_LOGS.debug("Action is Begin Testing.");
                    		
                    	getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_actionColumn2", j).click();
                    	Thread.sleep(3000);
                    	
                    	if(testStepResult.equalsIgnoreCase("Pass"))
                    	{
                    		getElement("TestingPage_passRadioButton_Id").click();
                    		APP_LOGS.debug("Status of Test Step: "+testStep+" is changed to Pass.");
                    	}
                    	else if(testStepResult.equalsIgnoreCase("Fail"))
                    	{
                    		Thread.sleep(500);
                    		getElement("TestingPage_failRadioButton_Id").click();
                    		APP_LOGS.debug("Status of Test Step: "+testStep+" is changed to Fail.");
                    	}
                    	else
                    	{
                    		getElement("TestingPage_pendingRadioButton_Id").click();
                    		APP_LOGS.debug("Status of Test Step: "+testStep+" is changed to Pending.");
                    	}
                    	getElement("TestingPage_saveButton_Id").click(); //Save button
                    	Thread.sleep(500);
                    		
                    	APP_LOGS.debug("Clicking on very Satisfied Radio Button");  //Feedback popup
        				getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
        				Thread.sleep(500);
        					
       					APP_LOGS.debug("Clicking on Save button of Rating popup");
       					getObject("TestingPageRatingPopup_saveButton").click();    //save button of feedback popup
       					Thread.sleep(500);
        					
       					APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
       					getObject("TestingPage_returnToHomeButton").click();   //Return to home button
                    	Thread.sleep(1000);
                    
                    	testingPerformed=true;
                    	return true;
					}
			    	else
			    	{
		    		 	fail=true;
		    		 	APP_LOGS.debug("Testing is not done as action cell is not 'Begin Testing'(FAIL).");
		    		 	comments+="Testing is not done as action cell is not 'Begin Testing'(FAIL). ";
		    		 	return false;
		    		 }
			      }
			   }
			   //if total pages greater than 1 click next link
			   if (totalPages>1 && testingPerformed==false) 
			   {
				    	if(isElementExistsByXpath("DashboardMyActivity_NextLink"))
				    	{
				    		nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
				    		getObject("DashboardMyActivity_NextLink").click();
							Thread.sleep(500);
				    	}
				    	else
				    	{
				    		APP_LOGS.debug("Next link disabled.");
				    	}
				}
			    else
			    {
			    	break;
			    }
			   
			 }
			return false;
		}
		catch(Throwable t)
		{
			fail=true;
	 		APP_LOGS.debug("Exception in Testing at the dashboard page(FAIL).");
	 		comments+="Exception in Testing at the dashboard page (FAIL).";
	 		return false;
		}
		
	}
	
	private void selectPortfolio(String ProjectDD,String versionDD,String testPassDD,String Portfolio, String Project,String Version,String TestPassName) throws IOException, InterruptedException
	{
		//Verification of portfolio presence 
		List<WebElement> portfolioDD = getElement("Dashboard_portfolioDD_ID").findElements(By.tagName("option"));
		for(int i=0; i<portfolioDD.size(); i++)
		{
			if(portfolioDD.get(i).getAttribute("title").equals(Portfolio))
			{
				Thread.sleep(1000);
				portfolioDD.get(i).click();
				APP_LOGS.debug("Portfolio is selected.");
				portfolioFlag=true;
				break;							
			}
		}
	
		//verify whether project is present and select it
		if(portfolioFlag==true)
		{
			APP_LOGS.debug("Portfolio is found(PASS).");
			dropdownSelection(ProjectDD,versionDD,testPassDD,Project, Version, TestPassName);
		}
		else
		{
			fail=true;
			APP_LOGS.debug("Portfolio is not found (FAIL).");
			comments+="Portfolio is not found (FAIL).";
			assertTrue(false);
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"PortfolioIsNotFound");
			closeBrowser();
			
			throw new SkipException("Portfolio is not available so skipping project, version, test pass selection.");
		}
	}
	
	//Project Status View for non Tester
	private void dropdownSelection(String ProjectDD,String versionDD,String testPassDD,String ProjectName,String Version,String TestPassName) throws IOException, InterruptedException
	{	
		//Project selection
		List<WebElement> projectList = getElement(ProjectDD).findElements(By.tagName("option"));
		for(int i=0; i<projectList.size(); i++)
		{
			if(projectList.get(i).getAttribute("title").equals(ProjectName))
			{
				Thread.sleep(1000);
				projectList.get(i).click();
				APP_LOGS.debug("Project is selected.");
				break;							
			}
		}
		
		//Version selection
		List<WebElement> versionList = getElement(versionDD).findElements(By.tagName("option"));
		for(int i=0; i<versionList.size(); i++)
		{
			if(versionList.get(i).getText().equals(Version))
			{
				Thread.sleep(1000);
				versionList.get(i).click();
				APP_LOGS.debug("Version is selected.");
				break;							
			}
		}
		
		//Test Pass Selection
		List<WebElement> testPassList = getElement(testPassDD).findElements(By.tagName("option"));
		for(int i=0; i<testPassList.size(); i++) 
		{
			if(testPassList.get(i).getAttribute("title").equals(TestPassName))
			{
				Thread.sleep(1000);
				testPassList.get(i).click();
				APP_LOGS.debug(TestPassName+" is selected.");
				break;							
			}
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
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
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
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
			
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
}
