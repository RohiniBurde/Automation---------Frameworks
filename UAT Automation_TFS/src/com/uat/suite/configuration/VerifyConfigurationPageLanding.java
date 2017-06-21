/* Author Name:-Aishwarya Deshpande
 * Created Date:- 26th Dec 2014
 * Last Modified on Date:- 9th Jan 2015
 * Module Description:- Verification of Landing on Configuration Page
 */

package com.uat.suite.configuration;

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

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyConfigurationPageLanding extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	
	//Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing VerifyConfigurationPageLanding Test Case");

		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyConfigurationPageLanding(String Role) throws Exception
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
			if(isElementExistsById("UAT_configuration_Id"))
			{
				APP_LOGS.debug("Configuration tab is displayed for user: "+Role+"(Pass).");
				comments+="Configuration tab is displayed for user: "+Role+"(Pass).";
				try
				{
					//Clicking on Configuration Tab
					getElement("UAT_configuration_Id").click();
					APP_LOGS.debug("Configuration tab clicked ");
					Thread.sleep(2000);
					
					String configurationTabHighlighted=getElement("UAT_configuration_Id").getAttribute("class");
					Thread.sleep(2000);
				
					if(compareStrings(OR.getProperty("UAT_configurationTab_Class"),configurationTabHighlighted))
					{
						APP_LOGS.debug("Configuration tab is Highlighted for '"+Role+"'(Pass).");
						comments+="Configuration tab is Highlighted for User '"+Role+"'(Pass).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Configuration tab is not Highlighted for '" +Role+"'(Fail).");
						comments+="Configuration tab is not Highlighted for User '"+Role+"'(Fail).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "QueryTabNotHighlightedfor"+Role);
					}
					
					List<WebElement> configurationTabsList=getObject("Configuration_configurationTabsList_Xpath").findElements(By.tagName("li"));
					for(int i=1;i<=configurationTabsList.size();i++)
					{
						String configurationList=getObject("Configuration_tabHighlighted_Xpath1", "Configuration_tabHighlighted_Xpath2", i).getAttribute("class");
						if(configurationList.equalsIgnoreCase(resourceFileConversion.getProperty("Configuration_highlightedTabClassAttr")))	
						{
							if(Role.equalsIgnoreCase("Version Lead")|| Role.equalsIgnoreCase("Test Manager")|| Role.equalsIgnoreCase("Stakeholder")|| Role.equalsIgnoreCase("Admin"))
							{
								String configurationTabName=getObject("Configuration_tabHighlighted_Xpath1", "Configuration_configurationTabName_Xpath2", i).getText();
								if(compareStrings(resourceFileConversion.getProperty("Configuration_NonTesterDefaultTabText"), configurationTabName))
								{
									APP_LOGS.debug("General Settings tab is Highlighted for "+Role+"(Pass).");
									comments+="General Settings tab is Highlighted for "+Role+"(Pass).";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("General Settings tab is not Highlighted for "+Role+"(Fail).");
									comments+="General Settings tab is not Highlighted for "+Role+"(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "GSTabNotHighlightedfor"+Role);
								}
								break;
							}
							else if(Role.equalsIgnoreCase("Tester"))        //Role=Tester
							{
								String configurationTabName=getObject("Configuration_tabHighlighted_Xpath1", "Configuration_configurationTabName_Xpath2", i).getText();
								if(compareStrings(resourceFileConversion.getProperty("Configuration_TesterDefaultTabText"), configurationTabName))
								{
									APP_LOGS.debug("'Configuration' tab is Highlighted for "+Role+"(Pass).");
									comments+="'Configuration' tab is Highlighted for "+Role+"(Pass).";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("'Configuration' tab is not Highlighted for "+Role+"(Fail).");
									comments+="'Configuration' is not Highlighted for "+Role+"(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ConfigurationTabNotHighlightedfor"+Role);
								}
								break;
							}
							else if(Role.equalsIgnoreCase("Test Manager+Tester"))      //Role=Tester+Test Manager
							{
								String configurationTabName=getObject("Configuration_tabHighlighted_Xpath1", "Configuration_configurationTabName_Xpath2", i).getText();
								if(compareStrings(resourceFileConversion.getProperty("Configuration_TesterDefaultTabText"), configurationTabName))
								{
									APP_LOGS.debug("'Configuration' tab is Highlighted for "+Role+"(Pass).");
									comments+="'Configuration' tab is Highlighted for "+Role+"(Pass).";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("'Configuration' tab is not Highlighted for "+Role+"(Fail).");
									comments+="'Configuration' tab is not Highlighted for "+Role+"(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ConfigurationTabNotHighlightedfor"+Role);
								}
								break;
							}
						}
					}
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					assertTrue(false);
					comments+="EXCEPTION in Landing on Configuration Page for the Role: "+Role+"(Fail).";
					APP_LOGS.debug("EXCEPTION in Landing on Configuration Page for the Role: "+Role+"(Fail).");
				}
					
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Configuration tab is not dispalyed for user:  "+Role+"(Fail).");
				comments="Configuration tab is not dispalyed for user:  "+Role+"(Fail).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ConfigurationTabNotDisplayedFor"+Role);
			}
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");
			comments+="Login Not Successful";
		}	
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ConfigurationSuiteXls, this.getClass().getSimpleName()) ;
	}
}
