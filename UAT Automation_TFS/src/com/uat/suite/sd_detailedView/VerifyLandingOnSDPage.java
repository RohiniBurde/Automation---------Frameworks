/**
Author Name: Preeti Walde
Created: 24th Dec 2014
Last Modified: 19th Jan 2015
Description: Code to test landing on Stakeholder Dashboard page by various users.

*/

package com.uat.suite.sd_detailedView;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyLandingOnSDPage extends TestSuiteBase
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
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());		
		
		if(!TestUtil.isTestCaseRunnable(SD_detailedViewXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(SD_detailedViewXls, this.getClass().getSimpleName());
	}
	
	
	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyLandingOnSDPage(String Role) throws Exception
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
							if(Role.equalsIgnoreCase("Stakeholder"))
							{
									stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
									
									if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class")) && getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class").contains("selected")
										&& getObject("StakeholderDashboard_detailedViewTab").isDisplayed() && (!getElement("UAT_dashboard_Id").isDisplayed())))
									{
										APP_LOGS.debug("Stakeholder Dashboard tab is Highlighted..Consolidated View tab is selected by default.. Detailed View tab is visible..and Dashboard page is not visible for user '"+Role+"'.");
										comments+="Stakeholder Dashboard tab is Highlighted..Consolidated View tab is selected by default.. Detailed View tab is visible..Dashboard page is not visible for user '"+Role+"'.(Pass) ";										
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Stakeholder Dashboard tab is not Highlighted..or Consolidated View tab is not selected by default.. or Detailed View tab is not visible..or Dashboard tab is visible for user '"+Role+"'.");
										comments+="Stakeholder Dashboard tab is not Highlighted..or Consolidated View tab is not selected by default..or Detailed View tab is not visible..or Dashboard tab is visible for user '"+Role+"'.(Fail) ";							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotHighlightedForUser"+Role);
										
									}
							}
						
						
							//stakeholder+admin	or stakeholder+version lead or stakeholder+test manager		
							else if(Role.equalsIgnoreCase("Stakeholder+Admin") || Role.equalsIgnoreCase("Stakeholder+Version Lead") ||
									Role.equalsIgnoreCase("Stakeholder+Test Manager"))
							{
									stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
									
									if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class")) && getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class").contains("selected")
											&& getObject("StakeholderDashboard_detailedViewTab").isDisplayed() && getElement("UAT_dashboard_Id").isDisplayed()))
									{
										APP_LOGS.debug("Stakeholder Dashboard tab is Highlighted..Consolidated View tab is selected..Dashboard tab is also visible for user '"+Role+"'.");
										comments+="Stakeholder Dashboard tab is Highlighted and Consolidated View tab is selected and Dashboard tab is also visible for user '"+Role+"'.(Pass) ";
										
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Stakeholder Dashboard tab is not Highlighted.. or Consolidated View tab is not selected..or Dashboard tab is not visible for user '"+Role+"'. ");
										comments+="Stakeholder Dashboard tab is not Highlighted..or Consolidated View tab is not selected..or Dashboard tab is not visible for user '"+Role+"'.(Fail) ";							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotHighlightedForUser"+Role);
									}
							}
						
											
							//stakeholder+tester				
							else if(Role.equalsIgnoreCase("Stakeholder+Tester"))
							{																	
									String dashboardTabHighlighted = getElement("UAT_dashboard_Id").getAttribute("class");
									
									if(assertTrue(dashboardTabHighlighted.equals(OR.getProperty("DashboardTab_Class")) && getElement("UAT_stakeholderDashboard_Id").isDisplayed()))
									{
											APP_LOGS.debug(Role+" lands on Dashboard Page successfully..Stakeholder Dshboard tab is visible. ");
											comments+=Role+" lands on Dashboard Page successfully..Stakeholder Dshboard tab is visible.(Pass) ";
											
											//click on stakeholder dashboard tab
											getElement("UAT_stakeholderDashboard_Id").click();
											Thread.sleep(1000);
											
											stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
											
											if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class")) && getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class").contains("selected")
													&& getObject("StakeholderDashboard_detailedViewTab").isDisplayed()))
											{
												APP_LOGS.debug("Stakeholder Dashboard tab is Highlighted..Consolidated View tab is selected by default for user '"+Role+"'.");
												comments+="Stakeholder Dashboard tab is Highlighted..Consolidated View tab is selected by default for user '"+Role+"'.(Pass) ";
											}
											else
											{
												fail=true;
												APP_LOGS.debug("Stakeholder Dashboard tab is not Highlighted..or Consolidated View tab is not selected by default for user '"+Role+"'. ");
												comments+="Stakeholder Dashboard tab is not Highlighted..or Consolidated View tab is not selected by default for user '"+Role+"'.(Pass) ";							
												TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotHighlightedForUser"+Role);
											}
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Stakeholder Dashboard tab is not visible for user '"+Role+"'. ");
										comments+="Stakeholder Dashboard tab is not visible for user '"+Role+"'.(Fail) ";							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"StakeholderDashboardTabNotVisibleForUser"+Role);
									}
							}
							else
							{
								fail=true;
								APP_LOGS.debug("User role is something different than defined role");
								comments+="User role is something different than defined role";	
								assertTrue(false);
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
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);	
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		//skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "SKIP");
		else if(isTestPass)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "FAIL");
		skip=false;
	}
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(SD_detailedViewXls, this.getClass().getSimpleName()) ;
	}
	
}
