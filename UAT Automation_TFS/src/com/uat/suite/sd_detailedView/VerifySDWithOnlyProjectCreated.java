package com.uat.suite.sd_detailedView;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;
import com.uat.base.Credentials;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifySDWithOnlyProjectCreated extends TestSuiteBase  
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;	
	
	Select selectGroup ;
	Select selectPortfolio;
	ArrayList<Credentials> stakeholder_versionLead;
	String[][] detailsArrayForVerification;
	
	
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

	
	@Test(dataProvider="getTestData")
	public void testVerifySDWithOnlyProjectCreated(String Role, String GroupName, String PortfolioName, String ProjectName, String Version,
									   String EndMonth, String EndYear, String EndDate, String stakeholder_VersionLead ) throws Exception
	{
		
		count++;		
	
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		
		int stakeholder_VersionLeadCount = Integer.parseInt(stakeholder_VersionLead);
		stakeholder_versionLead = getUsers("Stakeholder+Version Lead", stakeholder_VersionLeadCount);
		

		APP_LOGS.debug("Opening Browser... for role "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try
			{
				APP_LOGS.debug("Clicking On Test Management Tab ");
			
				getElement("UAT_testManagement_Id").click();
			
				Thread.sleep(3000);
			
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, stakeholder_versionLead.get(0).username, stakeholder_versionLead.get(0).username))
	 			{
	 				fail=true;
	 				
	 				APP_LOGS.debug(ProjectName+" not created successfully. ");
	 				
	 				comments= comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
	 				
	 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
	 			}
				else 
				{
					APP_LOGS.debug(ProjectName+" created successfully. ");
				}
				
				
				APP_LOGS.debug("Closing the browser after creation of Test Passes.");
				
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser for role "+stakeholder_versionLead.get(0).username);
				
				openBrowser();
				
				if(login(stakeholder_versionLead.get(0).username,stakeholder_versionLead.get(0).password, "Stakeholder+Version Lead"))
				{
				
				
					getElement("UAT_stakeholderDashboard_Id").click();
					
					Thread.sleep(3000);
					
					getObject("StakeholderDashboard_detailedViewPageTab").click();
					
					Thread.sleep(1500);
					
					
					selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						String grpName = selectGroup.getOptions().get(i).getText();
						
						if (grpName.equals(GroupName)) 
						{
							selectGroup.selectByIndex(i);
						}
					}
					
					selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
					
					Thread.sleep(2000);
					
					selectGroup.selectByValue(GroupName);
					
					selectPortfolio.selectByVisibleText(PortfolioName);
					
					int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
					
					String noTestStepsAvailable = getObject("SDDetailedView_pieChartPresent").getText();
					
					
					
				//	getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", 1).getText();
					
					int successCount = 0;
					do
					{
						int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
					
						for (int i = 1; i <= numOfProjectPresentInGrid; i++) 
						{
							String projectNameFromGrid = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", i).getAttribute("title");
						
							String versionFromGrid = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", i).getAttribute("title");
						
							if (projectNameFromGrid.equals(ProjectName) && versionFromGrid.equals(Version)) 
							{
								
								successCount++;
								
							}
							
						}
						
					} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
					
					
					
					if (getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() && 
							compareIntegers(1, noTestStepAvailableTagSize)) 
					{
						String noTestPassText = getElement("SDDetailedView_noTestPassAvailableText_Id").getText();
						
						if ( compareStrings( "No Test Pass(es) Available." , noTestPassText ) &&
							 compareIntegers(1, successCount))  
						{
							
							APP_LOGS.debug("Created project is displayed in Project grid.");
							
							APP_LOGS.debug("'No Test Pass(es) Available.' message is displayed");
							
							comments += "\n- Created project is displayed in Project grid.";
							
							comments += "\n- 'No Test Pass(es) Available.' message is displayed";
						
						}
						else 
						{
							
							fail=true;
							
							comments += "\n- Created project is NOT displayed in Project grid OR 'No Test Pass(es) Available.' message is NOT displayed. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Created project is NOT displayed OR 'No Test Pass(es) Available.' message is NOT displayed");
							
						}
						
						if (  compareStrings( "No Test Steps Available!" , noTestStepsAvailable )  )  
						{
							APP_LOGS.debug("'No Test Steps Available!' message is displayed");
							
							comments += "\n- 'No Test Steps Available!' message is displayed";
						
						}
						else 
						{
							
							fail=true;
							
							comments += "\n- 'No Test Steps Available!' message NOT displayed.(Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available!' message NOT displayed");
							
						}
						
					}
					else 
					{
						fail=true;
						
						assertTrue(false);
						
						comments += "\n- 'No Test Steps Available!' message is NOT displayed in pie chart Section. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available!' message is NOT displayed");
						
					}
				}
				else 
				{
					fail=true;
					APP_LOGS.debug("Login Unsuccessful for Test Manager "+stakeholder_versionLead.get(0).username);				
					comments += "Login Unsuccessful for Test Manager "+stakeholder_versionLead.get(0).username;
					assertTrue(false);	
				}
			}
			catch(SkipException t)
			 {
				skip = true; 
			   
			   	t.printStackTrace();
				
				comments += "\n- Skip exception has Occurred.";
				
			 }
			catch(Throwable t)
			{
				t.printStackTrace();
				
				fail=true;
				
				assertTrue(false);	
				
				comments += "\n- Fail :-Any other exception has Occurred.";
				
				APP_LOGS.debug("Any other exception has Occurred. Test Case has been Failed");
			}
			
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);
			
			comments+="Login Unsuccessful for Tester "+Role;
		}
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(SD_detailedViewXls, this.getClass().getSimpleName()) ;
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
}
