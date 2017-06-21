package com.uat.suite.dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyDashboardPageContent extends TestSuiteBase
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
	ArrayList<Credentials> testManager_Tester;
	boolean portfolioFlag;
	int totalPages;
	int projectTestPassSummaryRows;
	String projectCell,versionCell,testPassCell,roleCell,actionCell;
	int daysLeft,notCompletedCount,passCount,failCount;
	String actual;
	String groupTestPassSummary;
	Utility utilRecorder = new Utility();
	
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
		testManager_Tester=new ArrayList<Credentials>();
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void VerifyDashboardPageContents(String Role, String GroupName, String Portfolio, String Project, String Version,
			String VersionLead,String EndMonth,String EndYear,String EndDate,String TestPassNameI,String TestPassNameII, 
			String TestManager,String TP_Tester,String TesterRole1,String TesterRole2, String TM_Tester, String TestCaseI, 
			String TestCaseII, String TestStepI, String TestStepII, String TestStepExpResult) throws Exception
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
		
		int TMTester_count = Integer.parseInt(TM_Tester);
		testManager_Tester= getUsers("Test Manager+Tester", TMTester_count);
		
		openBrowser();
		APP_LOGS.debug("Opening Browser...for user "+Role);
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			APP_LOGS.debug("Clicking On Test Management Tab.");
			getElement("UAT_testManagement_Id").click();
			Thread.sleep(3000);
			
			//Project creation
			if(createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
			{
				APP_LOGS.debug("Project successfully created.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Project creation is not done successfully (FAIL).");
				comments+="Project creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project not created successfully");//reports
			}
			
			// Test Pass creation 1st
			if(createTestPass(GroupName, Portfolio, Project, Version, TestPassNameI, EndMonth, EndYear, EndDate, testManager.get(0).username))
			{
				APP_LOGS.debug("First Test Pass successfully created.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation is not done successfully (FAIL).");
				comments+="Test Pass Creation is not done successfully Fails (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
				
			// Test Pass creation 2nd
			if(createTestPass(GroupName, Portfolio, Project, Version, TestPassNameII, EndMonth, EndYear, EndDate, testManager_Tester.get(0).username))
			{
				APP_LOGS.debug("Second Test Pass successfully created (PASS).");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation is not done successfully (FAIL).");
				comments+="Test Pass Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
				
			// Tester creation for 1st Test Pass
			if(createTester(GroupName, Portfolio, Project, Version, TestPassNameI, tester.get(0).username, TesterRole1, TesterRole1))
			{
				APP_LOGS.debug("Tester created successfully for TP I.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester Creation is not done successfully (FAIL).");
				comments+="Tester Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
			}
				
			// Tester creation for 2nd Test Pass
			if(createTester(GroupName, Portfolio, Project, Version, TestPassNameII, testManager_Tester.get(0).username, TesterRole2, TesterRole2))
			{
				APP_LOGS.debug("Tester created successfully for TP II.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester Creation is not done successfully (FAIL).");
				comments+="Tester Creation is not done successfully (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created successfully");//reports
			}
			
			// Test Case creation for Ist Test Pass
			if(createTestCase(GroupName, Portfolio, Project, Version, TestPassNameI, TestCaseI))
			{
				APP_LOGS.debug("Test Case created successfully for TP I.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Case Creation is not done successfully for TP I (FAIL).");
				comments+="Test Case Creation is not done successfully for TP I (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case TP I is not created successfully");//reports
			}
			
			// Test Case creation for IInd Test Pass
			if(createTestCase(GroupName, Portfolio, Project, Version, TestPassNameII, TestCaseII))
			{
				APP_LOGS.debug("Test Case created successfully for TP II .");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Case Creation is not done successfully for TP II (FAIL).");
				comments+="Test Case Creation is not done successfully for TP II (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestCase");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Case TP II is not created successfully");//reports
			}
			
			// Test Step 1 creation for Ist Test Pass
			if(createTestStep(GroupName, Portfolio, Project, Version, TestPassNameI,TestStepI, TestStepExpResult, TestCaseI, TesterRole1))
			{
				APP_LOGS.debug("Test Step created successfully for TP I.");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Step Creation is not done successfully for TP I (FAIL).");
				comments+="Test Step Creation is not done successfully for TP I (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step TP I is not created successfully");//reports
			}
				
			// Test Step creation for IInd Test Pass
			if(createTestStep(GroupName, Portfolio, Project, Version, TestPassNameII, TestStepII, TestStepExpResult, TestCaseII, TesterRole2))
			{
				APP_LOGS.debug("Test Step created successfully for TP II (PASS).");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Step Creation is not done successfully for TP II (FAIL).");
				comments+="Test Step Creation is not done successfully for TP II (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Step TP II is not created successfully");//reports
			}

			//back to Dashboard page
			getElement("UAT_dashboard_Id").click();
			Thread.sleep(2000);
			
			APP_LOGS.debug("verify project test pass status contents");
			projectTestPassStatus(Portfolio, Project, Version, TestPassNameI, TestPassNameII);
			
			//verify number of testers participated button
			if(assertTrue(isElementExistsById("Dashboard_nuberOfTesterParticipatedButton_ID")))
			{
				APP_LOGS.debug("Number of Tester button is visible (PASS).");
				comments+="Number of Tester button is visible (PASS).";
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Number of Tester button is not visible (FAIL).");
				comments+="Number of Tester button is not visible (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NumberOfTesterButtonNotVisible");
			}
			
			//verify triage button
			if(assertTrue(isElementExistsByXpath("Dashboard_triageButton")))
			{
				APP_LOGS.debug("Triage button is not visible (PASS).");
				comments+="Triage button is not visible (PASS).";
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Triage button is visible (FAIL).");
				comments+="Triage button is visible (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageButtonIsVisible");
			}
			
			APP_LOGS.debug("verify project and test pass summary for Test Pass 1");
			projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameI, EndDate, EndYear, EndMonth, testManager.get(0).username);

			APP_LOGS.debug("verify project and test pass summary for Test Pass 2");
			projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameII, EndDate, EndYear, EndMonth, testManager_Tester.get(0).username);
	
			closeBrowser();
			
			//Login with Role: Version Lead
			openBrowser();
			APP_LOGS.debug("login with Version Lead");
			
			if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
			{
				APP_LOGS.debug("verify project test pass status contents");
				projectTestPassStatus(Portfolio, Project, Version, TestPassNameI, TestPassNameII);
				
				//verify number of testers participated button
				if(assertTrue(isElementExistsById("Dashboard_nuberOfTesterParticipatedButton_ID")))
				{
					APP_LOGS.debug("Number of Tester button is visible (PASS).");
					comments+="Number of Tester button is visible (PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Number of Tester button is not visible (FAIL).");
					comments+="Number of Tester button is not visible (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NumberOfTesterButtonNotVisible");
				}
				
				//verify triage button
				if(assertTrue(isElementExistsByXpath("Dashboard_triageButton")))
				{
					APP_LOGS.debug("Triage button is not visible (PASS).");
					comments+="Triage button is not visible (PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Triage button is visible (FAIL).");
					comments+="Triage button is visible (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TriageButtonIsVisible");
				}
				
				APP_LOGS.debug("verify project and test pass summary for Test Pass1");
				projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameI, EndDate, EndYear, EndMonth, testManager.get(0).username);
				
				APP_LOGS.debug("verify project and test pass summary for Test Pass2");
				projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameII, EndDate, EndYear, EndMonth, testManager_Tester.get(0).username);
			}
			else
			{
				APP_LOGS.debug("Login Unsuccessfull for the user with role 'Version Lead'");
				comments+="Login Unsuccessfull for the user with role 'Version Lead'";
			}  
			
			closeBrowser();
				
			//Login with Role: Tester
			openBrowser();
			APP_LOGS.debug("login with tester");
			if(login(tester.get(0).username,tester.get(0).password,"Tester"))
			{
			    APP_LOGS.debug("verify project test pass status contents");
				myActivityTestingStatus(Project, Version, TestPassNameI, TesterRole1);
					
				APP_LOGS.debug("verify project and test pass summary for Test Pass 1");
				projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameI, EndDate, EndYear, EndMonth, testManager.get(0).username);
	
			}
			else 
			{
				APP_LOGS.debug("Login Unsuccessfull for the user with role 'Tester'.");
				comments+="Login Unsuccessfull for the user with role 'Tester'.";
			}
			
			closeBrowser();
			
			//Login with Role: Test Manager+Tester
			openBrowser();
			APP_LOGS.debug("login with Test Manager+Tester");
			if(login(testManager_Tester.get(0).username,testManager_Tester.get(0).password,"Test Manager+Tester"))
			{
			    APP_LOGS.debug("verify project test pass status contents");
				myActivityTestingStatus(Project, Version, TestPassNameII, TesterRole2);
					
				//verify number of testers participated button
				if(assertTrue(isElementExistsById("Dashboard_nuberOfTesterParticipatedButton_ID")))
				{
					APP_LOGS.debug("Number of Tester button is visible (PASS).");
					comments+="Number of Tester button is visible (PASS).";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Number of Tester button is not visible (FAIL).");
					comments+="Number of Tester button is not visible (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NumberOfTesterButtonNotVisible");
				}
				
				APP_LOGS.debug("verify project and test pass summary for Test Pass 2");
				projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassNameII, EndDate, EndYear, EndMonth, testManager_Tester.get(0).username);
				
				closeBrowser();
			}
			else 
			{
				APP_LOGS.debug("Login Unsuccessfull for the user with role 'Test Manager+Tester'");
				comments+="Login Unsuccessfull for the user with role 'Test Manager+Tester'";
			}
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
			comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
		}
		utilRecorder.stopRecording();
	}
	
	private boolean verifyProjectInMyActivity(String Project,String Version,String TestPassName,String TesterRole)
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean projectFound=false;
		
		try
		{
		   //function call MyActPagination
		   if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
		   {
			    APP_LOGS.debug("Only 1 page available on My Activity area.");
			    totalPages=1;
			    paginationCount=1;
		   }
		   else 
		   {
			    APP_LOGS.debug("More than 1 page avaialble on My Activity area. Calculating total pages.");
			    totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
		   }
		   
		    nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
			   int myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
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
				    	projectFound=true;
				    	return true;
				    }
		   	   }
			   
			   if (totalPages>1 && projectFound==false) 
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
		catch (Throwable e) 
		{
			e.printStackTrace();
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("Exception in verifyMyActivityData function");
			comments+="Exception in verifyMyActivityData function. ";
			return false;
		}
	}
	
	public boolean verifyProjectInProjectTestPassSummary(String GroupName,String Portfolio,String Project,String Version,
			String TestPassName,String dateStart,String EndDate,String EndYear,String EndMonth, String testManager)
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean testPassNameFound = false;
		
		try 
		{
			Thread.sleep(200);
			
			do
			{
				//entering into project and Test Pass summary
				projectTestPassSummaryRows = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).size();
				for (int j = 1; j <= projectTestPassSummaryRows; j++) 
				{
					List<String> projectDetails=new ArrayList<String>();
					groupTestPassSummary=getElement("Dashboard_projectTestPassSummary_group_Id").findElements(By.tagName("tr")).get(j-1).getAttribute("group");
					
					if(groupTestPassSummary.equals(GroupName))
					{
						int countFlagForProject=0;
						int groupTdSize=getObject("DashboardProjectTestPassSummary_table").findElements(By.xpath("tr["+j+"]/td")).size();
						if(getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(0).getAttribute("title").equals(Portfolio)
								|| getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(1).getAttribute("title").equals(Portfolio))
						{
							for(int td=0;td<groupTdSize;td++)
							{
								Thread.sleep(200);
								if(td>=groupTdSize-3)
									actual = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getText();
								else
									actual = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getAttribute("title");
									projectDetails.add(actual);
									countFlagForProject++;
							}
						}
						
						if(countFlagForProject>0)
						{
							if(projectDetails.size()==9)
							{
								String testManagerReplace = (testManager).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(GroupName) 
									&& projectDetails.get(1).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(2).equalsIgnoreCase(Project)  
									&& projectDetails.get(3).equalsIgnoreCase(Version) 
									&& projectDetails.get(4).equalsIgnoreCase(TestPassName) 
									&& projectDetails.get(5).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(6).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(7).equalsIgnoreCase(dateStart)
									&& projectDetails.get(8).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
								{
										APP_LOGS.debug("Project and "+TestPassName+" is found in Project and Test Pass Summary(PASS).");
										comments+="Project and "+TestPassName+" is found in Project and Test Pass Summary(PASS).";
//										testPassNameFound=true;
										return true;
								}
							}
							else
							{
								String testManagerReplace = (testManager).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(1).equalsIgnoreCase(Project)  
									&& projectDetails.get(2).equalsIgnoreCase(Version) 
									&& projectDetails.get(3).equalsIgnoreCase(TestPassName) 
									&& projectDetails.get(4).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(5).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(6).equalsIgnoreCase(dateStart)
									&& projectDetails.get(7).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
								{
										APP_LOGS.debug("Project and "+TestPassName+" is found in Project and Test Pass Summary(PASS).");
										comments+="Project and "+TestPassName+" is found in Project and Test Pass Summary(PASS).";
//										testPassNameFound=true;
										return true;
								}	
							}
						}
					}
				} 	
			}while(ifElementIsClickableThenClick("DashboardProjectTestPassSummary_nextLink"));
		} 
		catch (Throwable e) 
		{
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("Exception in verifyProjectInProjectTestPassSummary function.");
			comments+="Exception in verifyProjectInProjectTestPassSummary function. ";
			return false;
		}
		return false;
	}
	
	private void projectTestPassStatus(String Portfolio, String Project, String Version, String TestPassNameI, String TestPassNameII)
	{
		try
		{
			//compare page heading
			if(compareStrings(resourceFileConversion.getProperty("Dashboard_pageHeadingText"),getObject("Dashboard_pageHeaderText").getText()))
			{
				APP_LOGS.debug("Dashboard Page Landing Sucessful(PASS). ");
				comments+="Dashboard Page Landing Sucessful(PASS). ";
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Dashboard Page Landing Unsucessful(FAIL).");
				comments+="Dashboard Page Landing Unsucessful(FAIL). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardPageLoginFailed");
				closeBrowser();
				
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Landing on Dashboard Page is unsuccessful");//reports
			}
			
			//when project and Test pass status div is visible
			if(assertTrue(isElementExistsByXpath("Dashboard_projectStatusAndTestPassDiv")))
			{
				if(assertTrue(isElementExistsByXpath("Dashboard_projectStatusDiv")))
				{
					//verify if project status text is matched or not
					if(compareStrings(resourceFileConversion.getProperty("Dashboard_projectStatusText"),getObject("Dashboard_projectStatusHeading").getText().trim()))
					{
						APP_LOGS.debug("Project Status section text is matched (PASS).");
						comments+="Project Status section text is matched (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project Status text is not matched (FAIL).");
						comments+="Project Status section text is not matched (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusTextNotMatched");
					}
					
					//Is portfolio drop down visible
					if(assertTrue(isElementExistsById("DashboardProjectStatus_portfolio_Id")))
					{
						APP_LOGS.debug("Project Status - Portfolio drop down is visible (PASS).");
						comments+="Project Status - Portfolio drop down is visible (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project Status - Portfolio drop down is not visible (FAIL).");
						comments+="Project Status - Portfolio drop down is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusPortfolioDropdownNotVisible");
					}
					
					//Is project drop down visible
					if(assertTrue(isElementExistsByXpath("DashboardProjectStatus_project")))
					{
						APP_LOGS.debug("Project Status - Project drop down is visible (PASS).");
						comments+="Project Status - Project drop down is visible (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project Status - Project drop down is not visible (FAIL).");
						comments+="Project Status - Project drop down is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusProjectDropdownNotVisible");
					}
					
					//Is version drop down visible
					if(assertTrue(isElementExistsById(("DashboardProjectStatus_version_Id"))))
					{
						APP_LOGS.debug("Project Status - Version drop down is visible (PASS).");
						comments+="Project Status - Version drop down is visible (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project Status - Version drop down is not visible (FAIL).");
						comments+="Project Status - Version drop down is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusVersionDropdownNotVisible");
					}
					
					//Project Status - portfolio selection
					List<WebElement> portfolioDD = getElement("Dashboard_portfolioDD_ID").findElements(By.tagName("option"));
					for(int i=0; i<portfolioDD.size(); i++)
					{
						if(portfolioDD.get(i).getText().equals(Portfolio))
						{
							Thread.sleep(1000);
							portfolioDD.get(i).click();
							APP_LOGS.debug("Portfolio is found (PASS).");
							comments+="Portfolio is found (PASS).";
							portfolioFlag=true;;
							break;	
						}
					}
					if(portfolioFlag==true)
					{
						//project selection
						List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
						for(int i=0; i<projectDD.size(); i++)
						{
							if(projectDD.get(i).getAttribute("title").equals(Project))
							{
								Thread.sleep(1000);
								projectDD.get(i).click();
								APP_LOGS.debug("Project is selected.");
								break;							
							}
						}
						
						//version selection
						List<WebElement> versionDD = getElement("Dashboard_versionDD_ID").findElements(By.tagName("option"));
						for(int i=0; i<versionDD.size(); i++)
						{
							if(versionDD.get(i).getText().equals(Version))
							{
								Thread.sleep(1000);
								versionDD.get(i).click();
								APP_LOGS.debug("Version is selected.");
								break;							
							}
						}
					}
					
					//verify project pie chart for selected project
					if(assertTrue(isElementExistsByXpath("DashboardProject_pieChart")))
					{	
						APP_LOGS.debug("Project Status Test Steps Pie Chart is displayed(PASS).");
						comments+="Project Status Test Steps Pie Chart is displayed(PASS). ";
					}
					else
					{	
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectPieChartNotDispalyed");
						APP_LOGS.debug("Project Status Test Steps Pie Chart is not displayed(FAIL).");
						comments+="Project Status Test Steps Pie Chart is not displayed(FAIL).";
					}
				}
				else
				{
						fail=true;
						APP_LOGS.debug("Project Status section is not visible (FAIL).");
						comments+="Project Status section is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusSectionNotVisible");
				}
				
				//verify test pass status div
				if(assertTrue(isElementExistsByXpath("Dashboard_testPassStatusDiv")))
				{
					//test pass selection
					Select selectTestPass = new Select(getElement("Dashboard_testPassDD_ID"));
					
					List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
					String OptionInTestPassDD = null;
					for(int i=0; i<testPassDD.size(); i++)
					{
						OptionInTestPassDD = selectTestPass.getOptions().get(i).getAttribute("title");
						if (i==0) 
						{
							if(OptionInTestPassDD.equals(TestPassNameI))
							{
								APP_LOGS.debug("Test Pass I is found (PASS).");
								comments+="Test Pass I is found (PASS).";
								testPassDD.get(i).click();
								Thread.sleep(1000);
								
								//verify test pass pie chart for selected test pass
								if(assertTrue(isElementExistsByXpath("DashboardTestPass_pieChart")))
								{	
									APP_LOGS.debug("TestPass Status Test Steps Pie Chart is displayed(PASS).");
									comments+="TestPass Status Test Steps Pie Chart is displayed(PASS). ";
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TPPieChartDispalyed");
									APP_LOGS.debug("TestPass Status Test Steps Pie Chart is not displayed(FAIL).");
									comments+="TestPass Status Test Steps Pie Chart is not displayed(FAIL).";
								}
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Test Pass I is not found (FAIL).");
								comments+="Test Pass I is not found (FAIL).";
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassINotFound");
							}
						}
						if (i==1) 
						{
							if(OptionInTestPassDD.equals(TestPassNameII))
							{
								APP_LOGS.debug("Test Pass II is found (PASS).");
								comments+="Test Pass II is found (PASS).";
								testPassDD.get(i).click();
								Thread.sleep(1000);
								
								//verify test pass pie chart for selected test pass
								if(assertTrue(isElementExistsByXpath("DashboardTestPass_pieChart")))
								{	
									APP_LOGS.debug("TestPass Status Test Steps Pie Chart is displayed(PASS).");
									comments+="TestPass Status Test Steps Pie Chart is displayed(PASS). ";
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TPPieChartDispalyed");
									APP_LOGS.debug("TestPass Status Test Steps Pie Chart is not displayed(FAIL).");
									comments+="TestPass Status Test Steps Pie Chart is not displayed(FAIL).";
								}
							}
							else
							{
									fail=true;
									APP_LOGS.debug("Test Pass II is not found (FAIL).");
									comments+="Test Pass II is not found (FAIL).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassIINotFound");
									assertTrue(false);
							}
						}
					}
					
					//verify if detailed analysis button available or not
					if(assertTrue(isElementExistsByXpath("Dashboard_detailedAnalysis")))
					{
						APP_LOGS.debug("Detailed Analysis button is visible (PASS).");
						comments+="Detailed Analysis button is visible (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Detailed Analysis button is not visible (FAIL).");
						comments+="Detailed Analysis button is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisButtonNotVisible");
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Test Pass Status section is not visible (FAIL).");
					comments+="Test Pass Status section is not visible (FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardTestPassStatusSectionNotVisible");
				}
			}
			else
			{
					fail=true;
					APP_LOGS.debug("Project Status and Test Pass Status area is blank(FAIL).");
					comments+="Project Status and Test Pass Status area is blank(FAIL). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectStatusTestPassStatusNotVisible");
			}
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
			assertTrue(false);
			APP_LOGS.debug("Exception in projectTestPassStatus function.");
			comments+="Exception in projectTestPassStatus function. ";
		}
	}
	
	private void projectTestPassSummary(String GroupName, String Portfolio, String Project, String Version, String TestPassName,
			String EndDate, String EndYear, String EndMonth, String testManager)
	{
		try
		{
			if(assertTrue(isElementExistsByXpath("DashboardProjectTestPassSummaryDiv")))
			{
				//verify project test pass summary text
				if(compareStrings(resourceFileConversion.getProperty("Dashboard_expProjectAndTestPassSummaryText"), getObject("DashboardProjectTestPassSummary_Label").getText().trim()))
				{
					APP_LOGS.debug("Project and Test Pass Summary text matched(PASS).");
					comments+="Project and Test Pass Summary text matched(PASS). ";
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Project and Test Pass Summary text not matched(FAIL).");
					comments+="Project and Test Pass Summary text not matched((FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectTestPassSummaryHeadingNotMatched");
				}
				
				//verify grid in project test pass summary
				if(assertTrue(isElementExistsById("DashboardProjectTestPassSummary_Table_Id")))
				{
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					Date date = new Date();
					
					String dateStart=dateFormat.format(date);
					
					if(verifyProjectInProjectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassName, dateStart, EndDate, EndYear, EndMonth, testManager))
					{
						APP_LOGS.debug("Project in Project and Test Pass Summary is found and verified (PASS).");
						comments+="Project in Project and Test Pass Summary is found and verified (PASS).";
					}
					else
					{
						APP_LOGS.debug("Project in Project and Test Pass Summary is not found (FAIL).");
						comments+="Project in Project and Test Pass Summary is not found (FAIL).";
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectInProjectAndTestPassSummaryNotFound");
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Project and Test Pass Summary table not available (FAIL).");
					comments+="Project and Test Pass Summary table not available (FAIL).";
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Project and Test Pass Summary area not visible(FAIL).");
				comments+="Project and Test Pass Summary area not visible(FAIL). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectTestPassSummaryAreaNotVisible");
			}
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
			assertTrue(false);	
			APP_LOGS.debug("Exception in projectTestPassSummary function.");
			comments+="Exception in projectTestPassSummary function. ";
		}
	}
	
	private void myActivityTestingStatus(String Project, String Version, String TestPassName, String TesterRoleSelection)
	{
		try
		{
			//compare page heading
			if(compareStrings(resourceFileConversion.getProperty("Dashboard_pageHeadingText"),getObject("Dashboard_pageHeaderText").getText()))
			{
				APP_LOGS.debug("Dashboard Page Landing Sucessful(PASS). ");
				comments+="Dashboard Page Landing Sucessful(PASS). ";
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Dashboard Page Landing Unsucessful(FAIL).");
				comments+="Dashboard Page Landing Unsucessful(FAIL). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DashboardPageLoginFailed");
				closeBrowser();
				
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Landing on Dashboard Page is unsuccessful");//reports
			}
		
			//verify my activity and testing status div visible or not
			if(assertTrue(isElementExistsById("DashboardMyActivityAndTestingStatus_Div_Id")))
			{	
				//verify my activity area
				if(assertTrue(isElementExistsByXpath("DashboardMyActivityDiv")))
				{
						//compare my activity heading
						if(compareStrings(resourceFileConversion.getProperty("Dashboard_myActivityHead"),getObject("DashboardMyActivity_Label").getText()))
						{
							APP_LOGS.debug("My activity heading is verified(PASS). ");
							comments+="My activity heading is verified(PASS). ";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("My activity heading not matched(FAIL).");
							comments+="My activity heading not matched(FAIL). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivityHeadingNotMatched");
						}
						
						
						//my activity text and find project in my activity
						if(verifyProjectInMyActivity(Project, Version, TestPassName, TesterRoleSelection))
						{
							APP_LOGS.debug("Project, version, test pass and role is matched(PASS).");
							comments+="Project, version, test pass and role is matched(PASS). ";
						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Required project, version, test pass, role not found in My Activity grid (FAIL).");
							comments+="Required project, version, test pass, role not found in My Activity grid (FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotContainProject");
						}
				}
				else
				{
						fail=true;
						APP_LOGS.debug("My Activity Area is blank(FAIL).");
						comments+="My Activity Area is blank(FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivitySectionNotVisible");
				}
				
				//verify testing status area
				if(assertTrue(isElementExistsByXpath("DashboardTesingStatusDiv")))
				{
					//compare testing status heading
					if(compareStrings(resourceFileConversion.getProperty("Dashboard_testingStatusHead"),getObject("DashboardTestingStatus_Label").getText()))
					{
						APP_LOGS.debug("Testing Status heading is verified(PASS). ");
						comments+="Testing Status heading is verified(PASS). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Testing Status heading not matched(FAIL).");
						comments+="Testing Status heading not matched(FAIL). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusHeadingNotMatched");
					}
					
					
					//Testing Status - project drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",1).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Project drop down is visible (PASS).");
						comments+="Testing Status - Project drop down is visible (PASS). ";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Testing Status - Project drop down is not visible (FAIL).");
						comments+="Testing Status - Project drop down is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusProjectDropdownNotVisible");
					}

					//Testing Status - version drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",2).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Version drop down is visible (PASS).");
						comments+="Testing Status - Version drop down is visible (PASS). ";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Testing Status - Version drop down is not visible (FAIL).");
						comments+="Testing Status - Version drop down is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusVersionDropdownNotVisible");
					}
					
					//Testing Status - test pass drop down
					if(getObject("Dashboard_tesingStatusDropDown1","Dashboard_tesingStatusDropDown2",3).isDisplayed())
					{
						APP_LOGS.debug("Testing Status - Test Pass drop down is visible (PASS).");
						comments+="Testing Status - Test Pass drop down is visible (PASS). ";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Testing Status - Test Pass drop is not visible (FAIL).");
						comments+="Testing Status - Test Pass drop is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusTestPassNotVisible");
					}
					
					//project selection
					List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
					for(int i=0; i<projectDD.size(); i++)
					{
						if(projectDD.get(i).getAttribute("title").equals(Project))
						{
							Thread.sleep(1000);
							projectDD.get(i).click();
							APP_LOGS.debug("Project is selected.");
							break;							
						}
					}
					
					//version selection
					List<WebElement> versionDD = getElement("DashboardTestingStatus_versionDD").findElements(By.tagName("option"));
					for(int i=0; i<versionDD.size(); i++)
					{
						if(versionDD.get(i).getText().equals(Version))
						{
							Thread.sleep(1000);
							versionDD.get(i).click();
							APP_LOGS.debug("Version is selected.");
							break;							
						}
					}
					
					//test pass selection
					List<WebElement> testPassDD = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
					String OptionInTestPassDD = null;
					int testPassFound=0;
					for(int i=0; i<testPassDD.size(); i++)
					{
						OptionInTestPassDD = testPassDD.get(i).getAttribute("title");
						
						if(OptionInTestPassDD.equals(TestPassName))
						{
							testPassDD.get(i).click();
							Thread.sleep(1000);
							
							//verify test pass pie chart for selected test pass
							if(assertTrue(isElementExistsByXpath("DashboardTestPass_pieChart")))
							{	
								APP_LOGS.debug("TestPass Status Test Steps Pie Chart is displayed(PASS).");
								comments+="TestPass Status Test Steps Pie Chart is displayed(PASS). ";
							}
							else
							{
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TPPieChartDispalyed");
								APP_LOGS.debug("TestPass Status Test Steps Pie Chart is not displayed(FAIL).");
								comments+="TestPass Status Test Steps Pie Chart is not displayed(FAIL).";
							}
							
							testPassFound++;
							APP_LOGS.debug("Test Pass is found (PASS).");
							comments+="Test Pass is found (PASS).";
							break;
						}
					}
					if(testPassFound==0)
					{
						fail=true;
						APP_LOGS.debug("Test Pass not found in TestPass dropdown(FAIL).");
						comments+="Test Pass not found in TestPass dropdown(FAIL).";
						assertTrue(false);
					}
					
					//verify if detailed analysis button available or not
					if(assertTrue(isElementExistsByXpath("Dashboard_detailedAnalysis")))
					{
						APP_LOGS.debug("Detailed Analysis button is visible (PASS).");
						comments+="Detailed Analysis button is visible (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Detailed Analysis button is not visible (FAIL).");
						comments+="Detailed Analysis button is not visible (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisButtonNotVisible");
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Testing Status Area is blank(FAIL).");
					comments+="Testing Status Area is blank(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingStatusSectionNotVisible");
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("My Activity and Testing Status area is blank(FAIL).");
				comments+="My Activity and Testing Status area is blank(FAIL). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MyActivityAndTestingStatusNotVisible");
			}
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
			assertTrue(false);
		}
	}
	
	private String getMonthNumber(String month)
	{
		switch (month) 
		{
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
