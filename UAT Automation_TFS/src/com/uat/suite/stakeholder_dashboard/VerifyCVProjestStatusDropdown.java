/**
Author Name: Preeti Walde
Created: 24th Dec 2014
Last Modified: 23rd Jan 2014
Description: Code to test 'All, Active and Completed' options in Project Status dropdown in Consolidated View tab.
*/

package com.uat.suite.stakeholder_dashboard;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;
//import com.uat.test.projectExtraction;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyCVProjestStatusDropdown extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	int rows;
	int cols;
	String[ ][ ] projectVersionArr;
	String[ ][ ] projectDetails;
	String[] displayProjectArray;
	String quarter = null;	
	DateFormat dateFormat;
	String systemDate;
	int fiscalYear;
	int month;
	int numOfProjectPresentInGrid;
	
	// Runmode of test case in a suite	
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(stakeholderDashboardSuiteXls, this.getClass().getSimpleName());
	}
	
	
	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyCVProjestStatusDropdown(String Role) throws Exception
	{
		int noTestStepAvailableTagSize;
		
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
				try
				{		
						
						if(Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager")
		        	 			|| Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Admin+Stakeholder"))
		        	 	{
		        	 		//click on Stakeholder dashboard page
							getElement("UAT_stakeholderDashboard_Id").click();
							Thread.sleep(1000);
		        	 	}
						
						APP_LOGS.debug("Clicking On Test Management Tab ");					    
					    getElement("UAT_testManagement_Id").click();
					    Thread.sleep(3000);
					    
					    String totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
					    
					    int projectsPerPage;
					   
					    int totalNumOfProjectInt = Integer.parseInt(totalNumOfProject);
					    
					    rows = totalNumOfProjectInt;// total records
					    cols = 8; // hardcoaded 
					    
					    projectDetails = new String[rows][cols];
					    
					    ArrayList<String> projectArrayList = new ArrayList<String>();
					    
					    int projectDetailsRowIndex = 0; 
					    
					    do
					    {					     
						     projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
						     
						     for (int j = 1; j <= projectsPerPage; j++) 
						     {      
						      
							      getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
							      
							      String groupName = new Select(getElement("ProjectCreateNew_groupDropDown_Id")).getFirstSelectedOption().getText();					      
							      String portfolioName = new Select(getElement("ProjectCreateNew_PortfolioDropDown_Id")).getFirstSelectedOption().getText();					      
							      String projectName = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");					      
							      String versionName =getObject("ProjectCreateNew_versionTextField").getAttribute("value");					      
							      String versionLead = getObject("ProjectCreateNew_versionLeadDisplayField").getText();					      
							      String endDate = getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");      
							      String startDate = getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
							      
							      projectDetails[projectDetailsRowIndex][0]= groupName;//Group names 					      
							      projectDetails[projectDetailsRowIndex][1]= portfolioName;//Portfolio names 					      
							      projectDetails[projectDetailsRowIndex][2]= projectName;//project names ;					      
							      projectDetails[projectDetailsRowIndex][3]= versionName;//Version ;					      
							      projectDetails[projectDetailsRowIndex][4]= versionLead;//Version Lead names ;					      
							      projectDetails[projectDetailsRowIndex][5]= endDate;//End Date names ;     
							      projectDetails[projectDetailsRowIndex][6]= startDate;//End Date names ;    
							      
							      Thread.sleep(500);					      
							      
							      String projectNameToBeInArray = null; 					      
							       
							      getElement("TM_testStepsTab_Id").click(); 
							      
							      Thread.sleep(1500);
							      
							      if (!getObject("TestSteps_testPassTitleSelected").getAttribute("title").equals("No Test Pass")) 
							      {
								       int numberOfTestPassInTestPassDropdown = eventfiringdriver.findElements(By.xpath("//ul[@id = 'tpul']/li")).size();
								
								       for (int k = 1; k <= numberOfTestPassInTestPassDropdown; k++) 
								       {
							    	   		Thread.sleep(500);
							       
							    	   		getElement("Attachments_testPassDropDown_Id").click();
							            
							    	   		Thread.sleep(500);
							        
							    	   		getObject("Attachment_testpassNameDDXpath1", "Attachments_xPath2_end", k).click();
							        
									        if (!(getElement("TestSteps_noTestStepAvailable_Text_Id").getText().contains(" No"))) 
									        {											        		
										         if (getElement("TestStepsViewAll_table_Id").isDisplayed()) 
										         {									          
										        	 	projectNameToBeInArray = getObject("TestSteps_projectTitleSelected").getAttribute("title");										         
										        	 	projectArrayList.add(projectNameToBeInArray);	
										        	 	
										        	 	String storeArraylist = projectArrayList.get(0);
										        	 	
													    projectDetails[projectDetailsRowIndex][7]= storeArraylist;	
													    projectArrayList.clear();
													    
										        	 	break;					          
										         }								         
									        }								        
								       }								       
							      }
					      									     
							      getElement("TM_projectsTab_Id").click();									      
							      Thread.sleep(2500);      
							      
							      projectDetailsRowIndex++;  					 
						      
						     }   
					     
					    } while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
					
					    					    
					    //click on Stakeholder dashboard page
						getElement("UAT_stakeholderDashboard_Id").click();
						Thread.sleep(1000);
					
						//select All option from project status dropdown
						Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
						projectStatusDD.selectByValue("All");	
						
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
																		
						if(numOfProjectPresentInGrid==0)
						{			
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
							
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'All' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'All' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'All' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'All' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								if(compareIntegers(projectDetails.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+projectDetails.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+projectDetails.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+projectDetails.length+". Test case failed");
								}
						}						
						else
						{
							//verify projects for All option
							verifyProjectSection(projectDetails);
						}
												
						//verify select filter, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
												
						//verify projects for completed option of project status dropdown
						projectStatusDD.selectByValue("Completed");
						
						String[][] completedProjectArray = getCompletedProjects(projectDetails);
						 					    
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Completed' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Completed' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Completed' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Completed' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								if(compareIntegers(completedProjectArray.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as completedProjectArray count "+completedProjectArray.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as completedProjectArray count "+completedProjectArray.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as completedProjectArray count "+completedProjectArray.length+". Test case failed");
								}
						}						
						else
						{
								//verify projects for Completed option
								verifyProjectSection(completedProjectArray);
						}	
						
						//verify select filter, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						//verify projects for active option of project status dropdown
						projectStatusDD.selectByValue("Active");
						
						//this array will collect all the projects which falls under current quarter and fiscal year
					    String[][] activeProjectArray = getActiveProjects(projectDetails);
					    
					    numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Active' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Active' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Active' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Active' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								
								if(compareIntegers(activeProjectArray.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as activeProjectArray count "+activeProjectArray.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as activeProjectArray count "+activeProjectArray.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as activeProjectArray count "+activeProjectArray.length+". Test case failed");
								}
						}						
						else
						{
								//verify projects for Active option
								verifyProjectSection(activeProjectArray);
						}
						
						//verify select filter, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						projectArrayList.clear();
						projectDetails = null;
						projectVersionArr = null;
						displayProjectArray=null;
						
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					APP_LOGS.debug("Exception occured");
				}
				
				closeBrowser();
		}
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
		}	
	}

	
	
	//function to get the number of project for completed criteria of quarter and fiscal year 
	public String[][] getCompletedProjects(String[][] projectArrayToSearchIn) throws ParseException
	{
		Date projectEndDate;		
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForCompletedStatus = null;	
		
		int projectsCountForCompletedStatus;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			//get project end date
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			//compare project end date with system date and add its index to array list if project end date is less than system date
			if ((projectEndDate.compareTo(sysdateInDateFormat)< 0 ) &&(projectArrayToSearchIn[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}			
		}
		
		//get the size of array list
		projectsCountForCompletedStatus = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForCompletedStatus = new String[projectsCountForCompletedStatus][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForCompletedStatus.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForCompletedStatus[j][0] = projectArrayToSearchIn[projectRowToGetDetailsFrom][0];
			projectDetailsForCompletedStatus[j][1] = projectArrayToSearchIn[projectRowToGetDetailsFrom][1];
			projectDetailsForCompletedStatus[j][2] = projectArrayToSearchIn[projectRowToGetDetailsFrom][2];
			projectDetailsForCompletedStatus[j][3] = projectArrayToSearchIn[projectRowToGetDetailsFrom][3];
			projectDetailsForCompletedStatus[j][4] = projectArrayToSearchIn[projectRowToGetDetailsFrom][4];
			projectDetailsForCompletedStatus[j][5] = projectArrayToSearchIn[projectRowToGetDetailsFrom][5];
			projectDetailsForCompletedStatus[j][6] = projectArrayToSearchIn[projectRowToGetDetailsFrom][6];
			projectDetailsForCompletedStatus[j][7] = projectArrayToSearchIn[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForCompletedStatus;
		
	}
	
	//function to get the number of project falls in defined criteria of quarter and fiscal year
	private String[][] getActiveProjects(String[][] projectArray) throws ParseException
	{
		Date projectStartDate;
		Date projectEndDate;
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForReqFYAndQuarter = null;		
				
		int projectsCountForReqFYandQuarter;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArray.length; i++) 
		{	
			projectStartDate = dateFormat.parse(projectArray[i][6]);
			projectEndDate = dateFormat.parse(projectArray[i][5]);

			if ((projectEndDate.compareTo(sysdateInDateFormat) >= 0 && projectStartDate.compareTo(sysdateInDateFormat) <= 0)
					&& (projectArray[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}			
		}
			
		projectsCountForReqFYandQuarter = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForReqFYAndQuarter = new String[projectsCountForReqFYandQuarter][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqFYAndQuarter.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqFYAndQuarter[j][0] = projectArray[projectRowToGetDetailsFrom][0];
			projectDetailsForReqFYAndQuarter[j][1] = projectArray[projectRowToGetDetailsFrom][1];
			projectDetailsForReqFYAndQuarter[j][2] = projectArray[projectRowToGetDetailsFrom][2];
			projectDetailsForReqFYAndQuarter[j][3] = projectArray[projectRowToGetDetailsFrom][3];
			projectDetailsForReqFYAndQuarter[j][4] = projectArray[projectRowToGetDetailsFrom][4];
			projectDetailsForReqFYAndQuarter[j][5] = projectArray[projectRowToGetDetailsFrom][5];
			projectDetailsForReqFYAndQuarter[j][6] = projectArray[projectRowToGetDetailsFrom][6];
			projectDetailsForReqFYAndQuarter[j][7] = projectArray[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForReqFYAndQuarter;
		
	}
	
	
	//verify project grid		
	private void verifyProjectSection(String[][] filterArray) throws IOException
	{
		displayProjectArray = new String[6];
		int projectVerifiedCount=0;
		
		try
		{							
				do
				{	
					int numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
					
					for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
					{   						
							int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
							int i=1;
																	
							if(grpTDSize==6)
							{
								displayProjectArray[0]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
							}
							displayProjectArray[1]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
							displayProjectArray[2]= getObject("StakeholderDashboardConsolidatedView_projectNameWithLinkXpath1", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath2", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath3", x, i++).getAttribute("title");
							displayProjectArray[3]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
							displayProjectArray[4]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
							displayProjectArray[5]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
							
							//verify projects displayed in CV tab
							if(compareProjects(filterArray, displayProjectArray))	
							{
								APP_LOGS.debug("Displayed Project "+displayProjectArray[2]+" entry is correct for selected filter criteria.(Pass)");
								projectVerifiedCount++;
							}
							else
							{
								fail=true;
								comments += "\n Displayed Project "+displayProjectArray[2]+" entry is incorrect for selected criteria.(Fail) ";						
								APP_LOGS.debug("Displayed Project "+displayProjectArray[2]+" entry is incorrect for selected criteria. Test case failed");
								assertTrue(false);
							}
					}
					
				} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
				
				
				//verify verified project count
				if(compareIntegers(filterArray.length, projectVerifiedCount))
				{
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is same as the count "+filterArray.length+".");
				}
				else
				{
					fail=true;
					comments += "\n Displayed project count "+projectVerifiedCount+" is same as the count "+filterArray.length+".(Fail) ";						
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is same as the count "+filterArray.length+". Test case failed");
				}
			
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;
		}
	}
	
	
	//comparing projects 
	private boolean compareProjects(String[][] projectDetails, String[] displayProject)
	{		
		for (int i = 0; i < projectDetails.length; i++) 
		{
			if (displayProject[0].equals(projectDetails[i][0])
				&& displayProject[1].equals(projectDetails[i][1])
				&& displayProject[2].equals(projectDetails[i][2])
				&& displayProject[3].equals(projectDetails[i][3])
				&& displayProject[4].equals(projectDetails[i][4])
				&& displayProject[5].replace('-', '/').equals(projectDetails[i][5])) 
			{
				return true;
			} 
			
		}
		
		return false;
		
	}
		
		
		
	//verify all dropdowns, quarter checkboxes and active tester fields
	private void verifyDropdownsAndActiveTesterFields()
	{
		try
		{
			//verify default selection in Select Date dropdown and its available options
			Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
			
			if (assertTrue(getElement("StakeholderDashboard_selectDateDropDown_Id").isDisplayed())) 
			{
					String dateFilterOption = selectDateDD.getFirstSelectedOption().getText();
					
					if (compareStrings("Select Filter", dateFilterOption)) 
					 {
						 APP_LOGS.debug("Date Filter dropdown value set to 'Select Filter' option.");							 
						 comments += "\n Date Filter dropdown value set to 'Select Filter' option.(Pass)";							 
					 }
					 else 
					 {
						fail=true;								
						comments += "\n Date Filter dropdown value is not set to 'Select Filter' option.(Fail) ";							
						APP_LOGS.debug("Date Filter dropdown value is not set to 'Select Filter' option. Test case failed");							
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Select Date dropdown not set to 'Select Filter' option");
					 }
			}
			else 
			{
					fail=true;					
					comments += "\n 'Select Date' dropdown is not displayed on Consolidated View tab.(Fail) ";					
					APP_LOGS.debug("'Select Date' dropdown is not displayed on Consolidated View tab. Test case failed");					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Select Date' dropdown not visible");
			}
			
			
			//verify default selection in Fiscal Year dropdown and its available options like FY14, FY15
			Select selectFiscalYearDD = new Select(getElement("StakeholderDashboard_fiscalYearDropDown_Id"));
			
			if (assertTrue(getElement("StakeholderDashboard_fiscalYearDropDown_Id").isDisplayed())) 
			{
					String defaultSelectedFiscalYear = selectFiscalYearDD.getFirstSelectedOption().getText();
										
					dateFormat = new SimpleDateFormat("MM/dd/yyyy");					
					Date date = new Date();					
					systemDate = dateFormat.format(date);
					
					month = Integer.parseInt(systemDate.substring(0, 2));
				
					if(month >= 7)		
					{				
						fiscalYear = Integer.parseInt(systemDate.substring(8))+1;
						
						if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
						{
							APP_LOGS.debug("Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default. ");								
							comments += "\n Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
						}
						else 
						{
							fail=true;								
							comments += "\n Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
							APP_LOGS.debug("Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year "+fiscalYear+" is not displayed selected in 'Fiscal Year' dropdown");
						}
					}
					else
					{
						fiscalYear = Integer.parseInt(systemDate.substring(8));
						
						if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
						{
							APP_LOGS.debug("Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default. ");								
							comments += "\n Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
						}
						else 
						{
							fail=true;								
							comments += "\n Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
							APP_LOGS.debug("Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year "+fiscalYear+" is not displayed selected in 'Fiscal Year' dropdown");
						}
					}
			}
			else 
			{
					fail=true;						
					comments += "\n 'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page.(Fail) ";						
					APP_LOGS.debug("'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page. Test case failed");						
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Fiscal Year' dropdown not displayed");
			}
			
			
			//verify quarter checkboxes visible or not and current quarter is selected by default or not
			if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isDisplayed() 
					&& getElement("StakeholderDashboard_Q2Checkbox_Id").isDisplayed() 
					&& getElement("StakeholderDashboard_Q3Checkbox_Id").isDisplayed() 
					&& getElement("StakeholderDashboard_Q4Checkbox_Id").isDisplayed())) 
			{
				
					if(assertTrue((!getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected())
							&& (!getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected())
							&& (!getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected())
							&& (!getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected())))
					{
						comments += "\n Any/All Quarter checkboxes/checkbox are/is unselected on Stakeholder Dashboard Page.(Pass) ";						
						APP_LOGS.debug("Any/All Quarter checkboxes/checkbox are/is unselected on Stakeholder Dashboard Page.");							
					}
					else
					{							
							fail=true;						
							comments += "\n Any/All Quarter checkboxes/checkbox is/are selected.(Fail) ";						
							APP_LOGS.debug("Any/All Quarter checkboxes/checkbox is/are selected. Test case failed");						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkboxes selected");
					}
			}
			else 
			{
					fail=true;						
					comments += "\n Any/All Quarter checkboxes/checkbox are/is not displayed on Stakeholder Dashboard Page.(Fail) ";						
					APP_LOGS.debug("Any/All Quarter checkboxes/checkbox are/is not displayed on Stakeholder Dashboard Page. Test case failed");						
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkboxes not available");
			}
			
			
			//verify if active tester count is visible or not
			if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").isDisplayed()))
			{
					comments += "\n Active Tester Count NOT Visible.(Pass) ";						
					APP_LOGS.debug("Active Tester Count NOT Visible.");
			}
			else
			{	
					fail=true;						
					comments += "\n Active Tester Count visible.(Fail) ";						
					APP_LOGS.debug("Active Tester Count visible. Test case failed");						
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveTesterCountVisible");
			}
			
			//verify execution bar graph displays or not
			if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_executionBarGraph_Id").isDisplayed()))
			{
					comments += "\n Execution bar graph NOT visible.(Pass) ";						
					APP_LOGS.debug("Execution bar graph NOT visible.(Pass) ");
			}
			else
			{
					fail=true;
					comments += "\n Execution bar graph visible.(Fail) ";						
					APP_LOGS.debug("Execution bar graph visible.");
			}
			
			//verify executed count link visible or not
			if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_executedStatusCountLabel_Id").isDisplayed()))
			{
					comments += "\n Executed count NOT visible.(Pass) ";						
					APP_LOGS.debug("Executed count NOT visible.");
			}
			else
			{
					fail=true;
					comments += "\n Executed count visible.(Fail) ";						
					APP_LOGS.debug("Executed count visible.");
			}		
			
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;
		}
	}
	
	
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		
	}
	
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, "Test Cases", TestUtil.getRowNum(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, "Test Cases", TestUtil.getRowNum(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(stakeholderDashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
	
	
}