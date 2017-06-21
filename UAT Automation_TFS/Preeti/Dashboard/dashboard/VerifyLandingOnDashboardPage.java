package com.uat.suite.dashboard;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyLandingOnDashboardPage extends TestSuiteBase
{
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	String stakeholderDashboardTabHighlighted;
	
	
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
	}
	
	
	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyLandingOnDashboardPage(String Role) throws Exception
	{
		count++;
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
				
		APP_LOGS.debug("Opening Browser...for user "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{	
				Thread.sleep(1000);
			    try
			    {
				
					String dashboardTabHighlighted = getElement("UAT_dashboard_Id").getAttribute("class");
					Thread.sleep(2000);
					
					if(Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager"))
					{
							if(assertTrue(dashboardTabHighlighted.equals(OR.getProperty("DashboardTab_Class")) && getElement("UAT_stakeholderDashboard_Id").isDisplayed()
								&& getElement("Dashboard_pageHeaderText_Id").getText().equals(resourceFileConversion.getProperty("Dashboard_pageHeadingText"))))
							{
								
								APP_LOGS.debug("Dashboard tab is Highlighted and stakeholder Dashboard tab is visible and Page title is 'Dashboard' for user '"+Role+"'.");
								comments+="PASS- Dashboard tab is Highlighted and stakeholder Dashboard tab is visible and Page title is 'Dashboard' for user '"+Role+"'. ";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Dashboard tab is not Highlighted and stakeholder Dashboard tab is not visible and Page title is not 'Dashboard' for user '"+Role+"'.");
								comments+="FAIL- Dashboard tab is not Highlighted and stakeholder Dashboard tab is not visible and Page title is not 'Dashboard' for user '"+Role+"'. ";												
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DashboardTabNotHighlightedForUser"+Role);
								
							}
					}
					
					
					else if(Role.equalsIgnoreCase("Stakeholder"))
					{
							stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
							
							if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class")) && (!getElement("UAT_dashboard_Id").isDisplayed())
									&& getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class").contains("selected")))
							{
								APP_LOGS.debug("Stakeholder Dashboard tab is Highlighted and Consolidated View tab is selected and Dashboard tab is not visible for user '"+Role+"'.");
								comments+="PASS- Stakeholder Dashboard tab is Highlighted and Consolidated View tab is selected and Dashboard tab is not visible for user '"+Role+"'. ";
								
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Stakeholder Dashboard tab is not Highlighted and Consolidated View tab is not selected and Dashboard tab is visible for user '"+Role+"'. ");
								comments+="FAIL- Stakeholder Dashboard tab is not Highlighted and Consolidated View tab is not selected and Dashboard tab is visible for user '"+Role+"'. ";							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotHighlightedForUser"+Role);
								
							}
					}
					
					
					else if((Role.equalsIgnoreCase("Stakeholder") && Role.equalsIgnoreCase("Version Lead") && Role.equalsIgnoreCase("Test Manager")) 
							||(Role.equalsIgnoreCase("Stakeholder") && Role.equalsIgnoreCase("Version Lead")) || 
							(Role.equalsIgnoreCase("Stakeholder") && Role.equalsIgnoreCase("Test Manager")))
					{
							stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
							
							if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class")) && getElement("UAT_dashboard_Id").isDisplayed()
									&& getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class").contains("selected")))
							{
								APP_LOGS.debug("Stakeholder Dashboard tab is Highlighted and Consolidated View tab is selected and Dashboard tab is also visible for user '"+Role+"'.");
								comments+="PASS- Stakeholder Dashboard tab is Highlighted and Consolidated View tab is selected and Dashboard tab is also visible for user '"+Role+"'. ";
								
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Stakeholder Dashboard tab is not Highlighted and Consolidated View tab is not selected and Dashboard tab is not visible for user '"+Role+"'. ");
								comments+="FAIL- Stakeholder Dashboard tab is not Highlighted and Consolidated View tab is not selected and Dashboard tab is not visible for user '"+Role+"'. ";							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotHighlightedForUser"+Role);
								
							}
					}
					
					
					else if(Role.equalsIgnoreCase("Tester"))
					{
							if(assertTrue(dashboardTabHighlighted.equals(OR.getProperty("DashboardTab_Class")) && getElement("UAT_stakeholderDashboard_Id").getAttribute("disabled").contains("true")
								&& getElement("Dashboard_pageHeaderText_Id").getText().equals(resourceFileConversion.getProperty("Dashboard_pageHeadingText"))))
							{
								APP_LOGS.debug("Dashboard tab is Highlighted and Page heading is 'Dashboard' and Stakeholder Dashboard tab is disabled for user '"+Role+"'.");
								comments+="PASS- Dashboard tab is Highlighted and Page heading is 'Dashboard' and Stakeholder Dashboard tab is disabled for user '"+Role+"'. ";
								
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Dashboard tab is not Highlighted and Page heading is not 'Dashboard' and Stakeholder Dashboard tab is enabled for user '"+Role+"'. ");
								comments+="FAIL- Dashboard tab is not Highlighted and Page heading is not 'Dashboard' and Stakeholder Dashboard tab is enabled for user '"+Role+"'. ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DashboardTabNotHighlightedForUser"+Role);
								
							}
					}
			    }
			    catch(Throwable t)
			    {
			    	t.printStackTrace();
			    	fail=true;
			    	APP_LOGS.debug("Exception occured");
					comments+="Exception occured";
					assertTrue(false);
			    }
				closeBrowser();
		}
		
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
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
		else if(!isLoginSuccess){
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
