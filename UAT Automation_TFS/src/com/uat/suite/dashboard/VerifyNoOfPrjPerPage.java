package com.uat.suite.dashboard;


import java.util.ArrayList;

import org.openqa.selenium.By;
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
public class VerifyNoOfPrjPerPage extends TestSuiteBase
{

	int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPass=true;
	boolean isLoginSuccess=false;
	String comments;
	String runmodes[]=null;
	int projectLimitPerPage=5;
	int projectsPerPage;
	int totalPages;
	int availableProjects;
	int requiredProjects=6;
	int anchorSize;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	Credentials user;
	boolean previousLinkDisabled;
	boolean nextLinkDisabled;
	String[] projectArray;
	String[] versionArray;
	String[] testPassArray;
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
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider = "getTestData")
	public void verifyNoOfPrjPerPage(String role, String groupName, String portfolioName, String projectName, String versionLeadName, 
			String version,	String projectEndMonth, String projectEndYear, String projectEndDate, String testPassName, String testManagerName, 
			String testPassEndMonth, String testPassEndYear,String testPassEndDate) throws Exception
    {
		
		count++;
		comments= "";	 
		
		 // test the runmode of current dataset
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));
			throw new SkipException("Runmode for test set data set to no "+count);
		}
		
		//version lead
		int versionlead_count = Integer.parseInt(versionLeadName);
		versionLead= new ArrayList<Credentials>();
		versionLead = getUsers("Version Lead", versionlead_count);
		
		//TestManager 
		int testManager_count = Integer.parseInt(testManagerName);
		testManager=new ArrayList<Credentials>();
		testManager = getUsers("Test Manager", testManager_count);

		APP_LOGS.debug("Opening Browser...for user "+role);
		 
		openBrowser();
		isLoginSuccess = login(role);
		 
		if(isLoginSuccess)
		{
		    try
		    {
		    	getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				//verify if grid is available on project page
				if(getElement("ProjectViewAll_projectTable_Id").isDisplayed())
				{
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(3000);
					
					if(getElement("DashboardProjectTestPassSummary_Table_Id").isDisplayed())
					{
						APP_LOGS.debug("Project and Test Pass Summary grid is showing(Pass). ");
				    	comments+="Project and Test Pass Summary grid is showing(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("'No Project Available' message is showing though projects are present in 'View All' section of Project page(Fail). ");
				    	comments+="'No Project Available' message is showing though projects are present in 'View All' section of Project page(Fail).  ";
				    	assertTrue(false);
					}
				}
				else
				{
					//verify if message is available on project page
					String noProjectAvailableMsgOnProjectPage = getElement("ProjectViewAll_NoProjectsLabel_Id").getText();
					
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(3000);
					
					String noProjectAvailableMsgOnProjectTestPassSummary = getObject("DashboardProjectTestPassSummary_NoProjectsLabel").getText();
					
					if(noProjectAvailableMsgOnProjectPage.equals(resourceFileConversion.getProperty("ProjectViewAll_noProjectsAvailable"))
					  && noProjectAvailableMsgOnProjectTestPassSummary.equals(resourceFileConversion.getProperty("DashboardProjectTestPassSummary_noProjectAvailable")))
					{
						APP_LOGS.debug("'No Project Available' message is correct on 'Project and Test Pass Summary'(Pass). ");
				    	comments+="'No Project Available' message is correct on 'Project and Test Pass Summary'(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project grid is showing on 'Project and Test Pass Summary' though no project available in 'View All' section of Project page(Fail). ");
				    	comments+="Project grid is showing on 'Project and Test Pass Summary' though no project available in 'View All' section of Project page(Fail). ";
				    	assertTrue(false);
					}
				}
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				//create project
				projectArray = projectName.split(",");
				versionArray = version.split(",");
				createProject(groupName, portfolioName, projectArray[0], versionArray[0], projectEndMonth, projectEndYear, projectEndDate, versionLead.get(0).username);
				
				//go to test pass page
				getElement("TestPassNavigation_Id").click();
				Thread.sleep(2000);
				
				//verify if test pass grid displayed or not
				if(getElement("TestPassViewAll_testPassTable_Id").isDisplayed())
				{
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(3000);
					
					if(getElement("DashboardProjectTestPassSummary_Table_Id").isDisplayed())
					{
						APP_LOGS.debug("Project and Test Pass Summary grid is showing(Pass). ");
				    	comments+="Project and Test Pass Summary grid is showing(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("'No Test Pass Available' message is showing though test passes are present in 'View All' section of Test Pass page(Fail). ");
				    	comments+="'No Test Pass Available' message is showing though test passes are present in 'View All' section of Test Pass page(Fail).";
				    	assertTrue(false);
					}
				}
				else
				{
					//verify if message is available on project page
					String noTestPassAvailableMsgOnTestPassPage = getElement("TestPassViewAll_NoTestPassAvailable_Id").getText();
					
					getElement("UAT_dashboard_Id").click();
					Thread.sleep(3000);
					
					if(isElementExistsByXpath("DashboardProjectTestPassSummary_NoTestPassLabel"))
					{
						String noTestPassAvailableMsgOnProjectTestPassSummary = getObject("DashboardProjectTestPassSummary_NoTestPassLabel").getText();
						
						if(noTestPassAvailableMsgOnTestPassPage.equals(resourceFileConversion.getProperty("TestPassViewAll_noTestPassAvailable"))
						  && noTestPassAvailableMsgOnProjectTestPassSummary.equals(resourceFileConversion.getProperty("DashboardProjectTestPassSummary_noTestPassAvailable")))
						{
							APP_LOGS.debug("'No Test Pass Available' message is correct on 'Project and Test Pass Summary'(Pass). ");
					    	comments+="'No Test Pass Available' message is correct on 'Project and Test Pass Summary'(Pass). ";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Project grid is showing on 'Project and Test Pass Summary' (Fail). ");
					    	comments+="Project grid is showing on 'Project and Test Pass Summary' though (Fail). ";
					    	assertTrue(false);
						}
					}
					else if (getElement("DashboardProjectTestPassSummary_Table_Id").isDisplayed()) 
					{
						APP_LOGS.debug("'Project and Test Pass Summary' Grid is displayed(Pass). ");
				    	comments+="'Project and Test Pass Summary' Grid is displayed(Pass). ";
					}
					else
					{
						assertTrue(false);
						fail=true;
						APP_LOGS.debug("'No Test Pass Available' label and 'Project and Test Pass Summary' Grid are not found(Fail). ");
				    	comments+="'No Test Pass Available' label and 'Project and Test Pass Summary' Grid are not found(Fail). ";
					}
				}
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				getElement("TestPassNavigation_Id").click();
				Thread.sleep(2000);
				
				//create test pass
				testPassArray = testPassName.split(",");
				createTestPass(groupName, portfolioName, projectArray[0], versionArray[0], testPassArray[0], testPassEndMonth, testPassEndYear, testPassEndDate, testManager.get(0).username);
				
				getElement("UAT_dashboard_Id").click();
				Thread.sleep(3000);
		    }
		    catch(Throwable t)
		    {
		    	fail=true;
		    	t.printStackTrace();
		    	assertTrue(false);
		    }
			closeBrowser();
			
			APP_LOGS.debug("Opening Browser...for user "+versionLead.get(0).username);						
			openBrowser();
			
			if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
			{
				try
				{
				   	if (getElement("DashboardProjectTestPassSummary_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
				   	{
						APP_LOGS.debug("Only 1 page available on Project and Test Pass Summary area.");
						totalPages=1;
						
						previousLinkDisabled = getObject("DashboardProjectTestPassSummary_previousLinkDisabled").isDisplayed();
						nextLinkDisabled = getObject("DashboardProjectTestPassSummary_nextLinkDisabled").isDisplayed();
						anchorSize = getElement("DashboardProjectTestPassSummary_Pagination_Id").findElements(By.xpath("div/a")).size();
						
						if(assertTrue(anchorSize==0 && previousLinkDisabled && nextLinkDisabled))
						{
							APP_LOGS.debug("Next and Previous links are disabled as only one page is available.");
							comments+="Next and Previous links are disabled(Pass). ";
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Next and Previous links are enabled though only 1 page is available.");
							comments+="Next and Previous links are enabled though only 1 page is available(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NextPreviousLinksEnabled");
						}
							
					}
					else 
					{
						APP_LOGS.debug("More than 1 page avaialble on Project and Test Pass Summary area. Calculating total pages.");
						totalPages = getElement("DashboardProjectTestPassSummary_Pagination_Id").findElements(By.xpath("div/a")).size();
					}
						
					if (totalPages>1)
					{
						for (int i = 0; i < totalPages; i++) 
						{
							APP_LOGS.debug("Calculating total projects available");
							projectsPerPage = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).size();
							
							nextLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLinkDisabled");
							previousLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_previousLinkDisabled");
									
							if (projectLimitPerPage == projectsPerPage) 
							{
								if((assertTrue(previousLinkDisabled && !nextLinkDisabled)))
								{
									APP_LOGS.debug("Previous link is disabled on first page and Next link is enabled.");
									comments+="Previous link is disabled on first page and Next link is enabled(Pass). ";
										
									while(nextLinkDisabled==false)
									{
										//click on next link
										getObject("DashboardProjectTestPassSummary_nextLink").click();
										Thread.sleep(500);
										nextLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLinkDisabled");
									}
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Next link is disabled. ");
									comments+="Next link is disabled(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NextLinkDisabled");
								}
							}
							else if(projectLimitPerPage != projectsPerPage) 
							{
								if(assertTrue(!previousLinkDisabled && nextLinkDisabled))
								{
									APP_LOGS.debug("Previuos link is enabled.");
									comments+="Previuos link is enabled on last page and Next link is disabled(Pass). ";
										
									while(previousLinkDisabled==false)
									{
										//click on previous link
										getObject("DashboardProjectTestPassSummary_previousLink").click();
										Thread.sleep(500);
										previousLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_previousLinkDisabled");
									}
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Previous link is disabled.");
									comments+="Previous link is disabled(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PreviousLinkDisabled");
								}
							}
						}	
					}
					else
					{
						availableProjects = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).size();
						projectArray = projectName.split(",");
						versionArray = version.split(",");
						requiredProjects = requiredProjects - availableProjects;
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(3000);
						
						for (int i = 1; i <=requiredProjects; i++) 
						{
							if(!createProject(groupName, portfolioName, projectArray[i], versionArray[i], projectEndMonth, projectEndYear, projectEndDate, versionLead.get(0).username ))
							{
								fail=true;
								APP_LOGS.debug("Project not created successfully.");
								comments+="Project not created successfully(Fail). ";
								assertTrue(false);
								closeBrowser();
								
								throw new SkipException("Project is not created successfully ... So Skipping all tests in Dashboard Suite");
							}
							else
							{
								APP_LOGS.debug("Project created successfully.");
							}
						}
						
						testPassArray = testPassName.split(",");
						
						for (int i = 1; i <=requiredProjects; i++) 
						{
							if(!createTestPass(groupName, portfolioName, projectArray[i], versionArray[i], testPassArray[i], 
									projectEndMonth, projectEndYear, projectEndDate, testManager.get(0).username))
							{
								fail=true;
								APP_LOGS.debug("Test Pass not created successfully");
								comments+="Test Pass not created successfully(Fail). ";
								assertTrue(false);
								closeBrowser();
								
								throw new SkipException("Test Pass not created successfully ... So Skipping all tests in Dashboard Suite");
							}
							else
							{
								APP_LOGS.debug("Test Pass created successfully.");
							}
						}
						
						getElement("UAT_dashboard_Id").click();
						
						totalPages = getElement("DashboardProjectTestPassSummary_Pagination_Id").findElements(By.xpath("div/a")).size();
						
						for (int i = 0; i < totalPages; i++) 
						{
							APP_LOGS.debug("Calculating total projects available on page 1");
							projectsPerPage = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).size();
							
							nextLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLinkDisabled");
							previousLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_previousLinkDisabled");
									
							if (projectLimitPerPage == projectsPerPage) 
							{
								if(assertTrue(previousLinkDisabled && !nextLinkDisabled))
								{
									APP_LOGS.debug("Next link is enabled.");
									comments+="Next link is enabled(Pass). ";
											
									while(nextLinkDisabled==false)
									{
										//click on next link
										getObject("DashboardProjectTestPassSummary_nextLink").click();
										Thread.sleep(500);
										nextLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLinkDisabled");
									}
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Next link is disabled. ");
									comments+="Next link is disabled(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NextLinkDisabled");
								}
							}
							else if(projectLimitPerPage != projectsPerPage)
							{
								if(assertTrue(!previousLinkDisabled && nextLinkDisabled))
								{
									APP_LOGS.debug("Previuos link is enabled.");
									comments+="Previuos link is enabled(Pass). ";
											
									while(previousLinkDisabled==false)
									{
										//click on previous link
										getObject("DashboardProjectTestPassSummary_previousLink").click();
										Thread.sleep(500);
										previousLinkDisabled = isElementExistsByXpath("DashboardProjectTestPassSummary_previousLinkDisabled");
									}
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Previous link is disabled.");
									comments+="Previous link is disabled(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PreviousLinkDisabled");
								}
							}
						}	
					}
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					assertTrue(false);
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
			APP_LOGS.debug("Login Unsuccessfull for user "+role);
			comments+="Login Unsuccessfull for user "+role;
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
