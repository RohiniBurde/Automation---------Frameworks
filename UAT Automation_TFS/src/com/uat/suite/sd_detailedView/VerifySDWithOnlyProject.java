package com.uat.suite.sd_detailedView;

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

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifySDWithOnlyProject extends TestSuiteBase  
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;	
	
	Select selectDateDD;
	Select selectGroup ;
	Select selectPortfolio;
	
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
	public void testVerifyDVTestersSection(String Role) throws Exception
	{
		
		count++;		
	
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		
		APP_LOGS.debug("Opening Browser... for role "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try
			{
				int projectsPerPage;	    
			   
			    String groupName = null;
				String portfolioName = null;
				String projectName = null;
				String versionName = null;
				String startDate;
				String endDate;
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				int breakCount = 0;
				do
				{
					
					projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
					
					for (int j = 1; j <= projectsPerPage; j++) 
					{						
						
						getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", j).click();
						
						//go to test pass page
					      getElement("TM_testPassesTab_Id").click(); 							      
					     
					      Thread.sleep(1500);
						
						 if (getElement("TestPassViewAll_NoTestPassAvailable_Id").getText().contains("No")) 
					     {
							 
							getElement("TM_projectsTab_Id").click();
							
							Thread.sleep(2500);	
							
							getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
							
							groupName = getElement("ProjectCreateNew_groupDropDown_Id").getAttribute("value");
							
							portfolioName = getElement("ProjectCreateNew_PortfolioDropDown_Id").getAttribute("value");
							
							projectName = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
							
							versionName =getObject("ProjectCreateNew_versionTextField").getAttribute("value");
							
							startDate = getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
							
							endDate = getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");	
							
							breakCount = 1;
							
							break;
						}
						 
						getElement("TM_projectsTab_Id").click();
							
						Thread.sleep(1500);	
						
					}
					
					if (breakCount == 1) 
					{
						break;
					}
					
				} while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
				
				
			    
			    getElement("UAT_stakeholderDashboard_Id").click();
				
				Thread.sleep(3000);
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				
				boolean projectFound = false;
				
				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				if (defaultGroupSelected.equalsIgnoreCase("No Group") )
				{
					APP_LOGS.debug("No project is displayed as 'No Group' is displayed in Group Dropdown.");
					
					comments += "\n-No project is displayed as 'No Group' is displayed in Group Dropdown. So, skipping the test. ";
				}
				else 
				{
					breakCount = 0;
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						
						if (selectGroup.getOptions().get(i).getText().equals(groupName)) 
						{
							
							selectGroup.selectByVisibleText(groupName);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								String portfolioNameInDD = selectPortfolio.getOptions().get(j).getAttribute("title");
								
								if (portfolioNameInDD.equals(portfolioName)) 
								{
									selectPortfolio.selectByVisibleText(portfolioNameInDD);
									
									do 
									{
										int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
										
										for (int k = 1; k <= numOfProjectPresentInGrid; k++) 
										{
																		
											String projectNameInGrid = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k).getAttribute("title");
											
											String versionFromGrid = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", k).getAttribute("title");
											
											if (  projectNameInGrid.equals(projectName) && 
												  versionFromGrid.equals(versionName) &&
												  ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
											{
											
												projectFound = true;
											
											}
											
										}
										
								} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
								
								breakCount = 1;
								
								break;
								
								}
								
							}
							
							if (breakCount == 1) 
							{
								break;
							}
							
						}
						
					}
						
					if ( projectFound ) 
					{

						fail=true;
						
						assertTrue(false);
						
						APP_LOGS.debug("Details of Project without Test Pass has FOUND in Test Pass Section of Detailed View. (Fail)");
						
						comments += "\n- Details of Project without Test Pass has FOUND in Test Pass Section of Detailed View. (Fail)";
					
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Details of Project without Test Pass has FOUND in Test Pass Section");
						
					}
					else 
					{
						
						APP_LOGS.debug("Details of Project without Test Pass has NOT found in Test Pass Section of Detailed View.");
						
						comments += "\n- Details of Project without Test Pass has NOT found in Test Pass Section of Detailed View.";
						
					}
						
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
	
	// FUNCTIONS
	
	
	
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
