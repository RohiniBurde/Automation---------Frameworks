/**
 * @author preeti.walde
 * Created: 23rd Jan 2015
 * Last Modified: 28th Jan 2015
 * Description: Code to test 'Select Filter, Date From-To, Today, Today-1, Today-7' options in Select Date dropdown in Consolidated View tab.
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

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyCVSelectDateDropdown extends TestSuiteBase
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
	DateFormat dateFormat;
	String systemDate;
	int fiscalYear;
	int month;
	String[] displayProjectArray;
	int numOfProjectPresentInGrid;
	int noTestStepAvailableTagSize;
	
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
	public void verifyCVSelectDateDropdown(String Role, String StartDateMonth, String StartDateYear, String StartDate,
			String EndDateMonth, String EndDateYear, String EndDate) throws Exception
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
						
						Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
												
						//select Today-1 option from select date dropdown
						selectDateDD.selectByVisibleText("Today - 1");
						
						//this array will collect all the projects which falls under today-1 date criteria		
						String[][] dayMinus1OptionArray = getProjectsForDayMinus1Option(projectDetails);
						 
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Today-1' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Today-1' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Today-1' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Today-1' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								if(compareIntegers(dayMinus1OptionArray.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+dayMinus1OptionArray.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+dayMinus1OptionArray.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as projectDetails count "+dayMinus1OptionArray.length+". Test case failed");
								}
						}
						else
						{
								//verify projects for Today-1 option
								verifyProjectSection(dayMinus1OptionArray);
						}
						
						//verify project status, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						//verify projects for Last 7 Days option of select date dropdown
						selectDateDD.selectByVisibleText("Last 7 Days");
						
						//this array will collect all the projects which falls under last 7 days date criteria		
						String[][] dayMinus7OptionArray = getProjectsForDayMinus7Option(projectDetails);
						
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Last 7 Days' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Last 7 Days' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Last 7 Days' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Last 7 Days' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								if(compareIntegers(dayMinus7OptionArray.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as dayMinus7OptionArray count "+dayMinus7OptionArray.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as dayMinus7OptionArray count "+dayMinus7OptionArray.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as dayMinus7OptionArray count "+dayMinus7OptionArray.length+". Test case failed");
								}
						}
						else
						{
								//verify projects for Last 7 Days option
								verifyProjectSection(dayMinus7OptionArray);
						}
						
						//verify project status, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						selectDateDD.selectByVisibleText("Today");	
						
						//this array will collect all the projects which falls under today date criteria		
						String[][] todayDateOptionArray = getProjectsForTodayDateOption(projectDetails);
						 
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Today' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Today' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Today' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Today' filter criteria.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								
								if(compareIntegers(todayDateOptionArray.length, numOfProjectPresentInGrid))
								{
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as todayDateOptionArray count "+todayDateOptionArray.length+".");
								}
								else
								{
									fail=true;
									comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as todayDateOptionArray count "+todayDateOptionArray.length+".(Fail) ";						
									APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as todayDateOptionArray count "+todayDateOptionArray.length+". Test case failed");
								}
						}
						else
						{
								//verify projects for Today option
								verifyProjectSection(todayDateOptionArray);
						}
						
						//verify project status, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						//verify projects for Dates - From/To option of select date dropdown
						selectDateDD.selectByVisibleText("Dates - From/To");
						
						if(getElement("StakeholderDashboardConsolidatedView_fromDateTextBox_Id").isDisplayed()
								&& getElement("StakeholderDashboardConsolidatedView_toDateTextBox_Id").isDisplayed())
						{
							//select from date
							selectStartOrEndDate(getObject("StakeholderDashboardConsolidatedView_startDateImage"),StartDateMonth,StartDateYear,StartDate);
							
							//select to date
							selectStartOrEndDate(getObject("StakeholderDashboardConsolidatedView_endDateImage"),EndDateMonth, EndDateYear, EndDate);
							
							String fromDate = getElement("StakeholderDashboardConsolidatedView_fromDateTextBox_Id").getAttribute("value");
							String toDate = getElement("StakeholderDashboardConsolidatedView_toDateTextBox_Id").getAttribute("value");
							
							//this array will collect all the projects which falls from/to date criteria		
							String[][] datesFromToOptionArray = getProjectsForDatesFromToOption(projectDetails,fromDate,toDate);
							
							numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
							
							if(numOfProjectPresentInGrid==0)
							{
									noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
									
									if(noTestStepAvailableTagSize==1)
									{
										if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
										{
											APP_LOGS.debug("No Projects Available for 'Dates - From/To' filter criteria..verified 'No Test Steps Available!' message");
											comments+="No Projects Available for 'Dates - From/To' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
										}
										else
										{
											fail=true;
											APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Dates - From/To' filter criteria");
											comments+="'No Test Step Available' message not shown though no projects available for selected 'Dates - From/To' filter criteria.(Fail) ";
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
										}
									}
									
									if(compareIntegers(datesFromToOptionArray.length, numOfProjectPresentInGrid))
									{
										APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as datesFromToOptionArray count "+datesFromToOptionArray.length+".");
									}
									else
									{
										fail=true;
										comments += "\n Displayed project count "+numOfProjectPresentInGrid+" is same as datesFromToOptionArray count "+datesFromToOptionArray.length+".(Fail) ";						
										APP_LOGS.debug("Displayed project count "+numOfProjectPresentInGrid+" is same as datesFromToOptionArray count "+datesFromToOptionArray.length+". Test case failed");
									}
							}
							else
							{
									//verify projects for From/TO Date option
									verifyProjectSection(datesFromToOptionArray);
							}
						    
						}
						else
						{
							fail=true;
							APP_LOGS.debug("'From date and To date' textboxes not visible");
							comments+="'From date and To date' textboxes not visible.(Fail) ";
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'From date and To date' textboxes not visible");						
						}
						
						//verify project status, fiscal year, quarter checkboxes, active tester, execution bar graph, executed status count options
						verifyDropdownsAndActiveTesterFields();
						
						
						//verify projects for Select Filter option of select date dropdown
						selectDateDD.selectByVisibleText("Select Filter");
						
						numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();					
						
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("No Projects Available for 'Select Filter' filter criteria..verified 'No Test Steps Available!' message");
										comments+="No Projects Available for 'Select Filter' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Select Filter' filter criteria");
										comments+="'No Test Step Available' message not shown though no projects available for selected 'Select Filter' filter criteria.(Fail) ";
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
								//verify projects for Select Filter option
								verifyProjectSection(projectDetails);
						}
						
						
						//verify default selection in project status dropdown and other options available like All, Active, Completed
						Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
						
						if (assertTrue(getElement("StakeholderDashboard_projectStatusDropDown_Id").isDisplayed())) 
						{
							 String selectedProjectStatus = projectStatusDD.getFirstSelectedOption().getText();
							 
							 if (compareStrings("All", selectedProjectStatus)) 
							 {
								 APP_LOGS.debug("'All' is selected as default in 'Project Status' dropdown");						 
								 comments += "\n 'All' is selected as default in 'Project Status' dropdown(Pass).";
								 
							 }
							 else 
							 {
								fail=true;							
								comments += "\n 'All' is NOT selected as default in 'Project Status' dropdown.(Fail) ";						
								APP_LOGS.debug("'All' is NOT selected as default in 'Project Status' dropdown. Test case failed");						
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Active' option is not displayed selected by default in Project Status dropdown");
							 }
						}
						else 
						{
								fail=true;					
								comments += "\n 'Project Status' dropdown is not displayed on Consolidated View tab.(Fail) ";					
								APP_LOGS.debug("'Project Status' dropdown is not displayed on Consolidated View tab. Test case failed");					
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Project Status' dropdown not visible");
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
						
						//verify if active tester count, execution bar graph, executed status count is visible or not			
						if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_activeTesterLabel_Id").isDisplayed()))
						{
								comments += "\n 'Active Tester' count label not visible.(Pass) ";					
								APP_LOGS.debug("Active Tester count label not visible");
						}
						else
						{
							fail=true;
							comments += "\n Active Tester count label visible.(Fail) ";						
							APP_LOGS.debug("Active Tester count label visible.");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveTesterLabelVisible");
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
						
						
						//release memory of all array
						projectDetails=null;
						projectArrayList.clear();
						projectVersionArr=null;
						displayProjectArray=null;
						
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					APP_LOGS.debug("Exception occured");
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

	
	
	//function to get the number of project for completed criteria of quarter and fiscal year 
	public String[][] getProjectsForTodayDateOption(String[][] projectArrayToSearchIn) throws ParseException
	{
		Date projectEndDate;	
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForTodayDate = null;	
		
		int projectsCountForTodayDate;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			//get project end date
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			//compare project end date with system date and add its index to array list if project end date is less than system date (Today option)
			if ((projectEndDate.compareTo(sysdateInDateFormat) >= 0) &&(projectArrayToSearchIn[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}	
		}
		
		//get the size of array list
		projectsCountForTodayDate = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForTodayDate = new String[projectsCountForTodayDate][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForTodayDate.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForTodayDate[j][0] = projectArrayToSearchIn[projectRowToGetDetailsFrom][0];
			projectDetailsForTodayDate[j][1] = projectArrayToSearchIn[projectRowToGetDetailsFrom][1];
			projectDetailsForTodayDate[j][2] = projectArrayToSearchIn[projectRowToGetDetailsFrom][2];
			projectDetailsForTodayDate[j][3] = projectArrayToSearchIn[projectRowToGetDetailsFrom][3];
			projectDetailsForTodayDate[j][4] = projectArrayToSearchIn[projectRowToGetDetailsFrom][4];
			projectDetailsForTodayDate[j][5] = projectArrayToSearchIn[projectRowToGetDetailsFrom][5];
			projectDetailsForTodayDate[j][6] = projectArrayToSearchIn[projectRowToGetDetailsFrom][6];
			projectDetailsForTodayDate[j][7] = projectArrayToSearchIn[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForTodayDate;
		
	}
	
	
	//function which returns the projects according to the group
	private String[][] getProjectsForDayMinus1Option(String[][] projectArray) throws ParseException
	{
		Date projectEndDate;		
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForDayMinus1Date = null;	
		
		int projectsCountForDayMinus1Date;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		int n = 1;
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArray.length; i++) 
		{	
			//get project end date
			projectEndDate = dateFormat.parse(projectArray[i][5]);
			
			Date dayMinus1Date = new Date(sysdateInDateFormat.getTime() - n * 24 * 3600 * 1000) ; //Subtract n days
			
			//compare project end date with system date and add its index to array list if project end date is one less than system date (Today-1 option)
			if ((projectEndDate.compareTo(dayMinus1Date) >= 0) &&(projectArray[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}	
		}
		
		//get the size of array list
		projectsCountForDayMinus1Date = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForDayMinus1Date = new String[projectsCountForDayMinus1Date][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForDayMinus1Date.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForDayMinus1Date[j][0] = projectArray[projectRowToGetDetailsFrom][0];
			projectDetailsForDayMinus1Date[j][1] = projectArray[projectRowToGetDetailsFrom][1];
			projectDetailsForDayMinus1Date[j][2] = projectArray[projectRowToGetDetailsFrom][2];
			projectDetailsForDayMinus1Date[j][3] = projectArray[projectRowToGetDetailsFrom][3];
			projectDetailsForDayMinus1Date[j][4] = projectArray[projectRowToGetDetailsFrom][4];
			projectDetailsForDayMinus1Date[j][5] = projectArray[projectRowToGetDetailsFrom][5];
			projectDetailsForDayMinus1Date[j][6] = projectArray[projectRowToGetDetailsFrom][6];
			projectDetailsForDayMinus1Date[j][7] = projectArray[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForDayMinus1Date;
		
		
	}
	
	
	//function which returns the projects according to the group
	private String[][] getProjectsForDayMinus7Option(String[][] projectArray) throws ParseException
	{
		Date projectEndDate;		
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForDayMinus7Date = null;	
		
		int projectsCountForDayMinus7Date;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		int n = 7;
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArray.length; i++) 
		{	
			//get project end date
			projectEndDate = dateFormat.parse(projectArray[i][5]);
			
			Date dayMinus7Date = new Date(sysdateInDateFormat.getTime() - n * 24 * 3600 * 1000) ; //Subtract n days
			
			//compare project end date with system date and add its index to array list if project end date is one less than system date (Today-1 option)
			if ((projectEndDate.compareTo(dayMinus7Date) >= 0) &&(projectArray[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}	
		}
		
		//get the size of array list
		projectsCountForDayMinus7Date = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForDayMinus7Date = new String[projectsCountForDayMinus7Date][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForDayMinus7Date.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForDayMinus7Date[j][0] = projectArray[projectRowToGetDetailsFrom][0];
			projectDetailsForDayMinus7Date[j][1] = projectArray[projectRowToGetDetailsFrom][1];
			projectDetailsForDayMinus7Date[j][2] = projectArray[projectRowToGetDetailsFrom][2];
			projectDetailsForDayMinus7Date[j][3] = projectArray[projectRowToGetDetailsFrom][3];
			projectDetailsForDayMinus7Date[j][4] = projectArray[projectRowToGetDetailsFrom][4];
			projectDetailsForDayMinus7Date[j][5] = projectArray[projectRowToGetDetailsFrom][5];
			projectDetailsForDayMinus7Date[j][6] = projectArray[projectRowToGetDetailsFrom][6];
			projectDetailsForDayMinus7Date[j][7] = projectArray[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForDayMinus7Date;
		
		
	}
	
	//function which returns the projects according to the group
	private String[][] getProjectsForDatesFromToOption(String[][] projectArray, String fromdate, String todate) throws ParseException
	{
		Date projectEndDate;		
		Date projectStartDate;
		Date fromDate;
		Date toDate;
		
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForFromToDate = null;	
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		int projectsCountForFromToDate;		
			
		//loop to get the index for matching value
		for (int i = 0; i < projectArray.length; i++) 
		{	
			//get project end date and start date			
			projectEndDate = dateFormat.parse(projectArray[i][5]);
			projectStartDate = dateFormat.parse(projectArray[i][6]);
			fromDate = dateFormat.parse(fromdate);
			toDate = dateFormat.parse(todate);		
			
			//compare project end date with system date and add its index to array list if project end date is one less than system date (Today-1 option)
			if (((projectEndDate.after(fromDate) && projectEndDate.before(toDate)) ||
				      (projectStartDate.after(fromDate) && projectStartDate.before(toDate)) ||
				      ((projectEndDate.after(fromDate) && projectEndDate.before(toDate) ) && (projectStartDate.after(fromDate) && projectStartDate.before(toDate))) ||
				      (projectStartDate.before(fromDate) && projectEndDate.after(toDate))) && (projectArray[i][7]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}	
		}
		
		//get the size of array list
		projectsCountForFromToDate = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForFromToDate = new String[projectsCountForFromToDate][8];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForFromToDate.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForFromToDate[j][0] = projectArray[projectRowToGetDetailsFrom][0];
			projectDetailsForFromToDate[j][1] = projectArray[projectRowToGetDetailsFrom][1];
			projectDetailsForFromToDate[j][2] = projectArray[projectRowToGetDetailsFrom][2];
			projectDetailsForFromToDate[j][3] = projectArray[projectRowToGetDetailsFrom][3];
			projectDetailsForFromToDate[j][4] = projectArray[projectRowToGetDetailsFrom][4];
			projectDetailsForFromToDate[j][5] = projectArray[projectRowToGetDetailsFrom][5];
			projectDetailsForFromToDate[j][6] = projectArray[projectRowToGetDetailsFrom][6];
			projectDetailsForFromToDate[j][7] = projectArray[projectRowToGetDetailsFrom][7];
		}
				
		return projectDetailsForFromToDate;
		
		
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
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is same as count "+filterArray.length+".");
				}
				else
				{
					fail=true;
					comments += "\n Displayed project count "+projectVerifiedCount+" is not same as count "+filterArray.length+".(Fail) ";						
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is not same as count "+filterArray.length+". Test case failed");
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
		String activeTesterCountLable;
		int activeTester;
		
		try
		{
			//verify default selection in project status dropdown and other options available like All, Active, Completed
			Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
			
			if (assertTrue(getElement("StakeholderDashboard_projectStatusDropDown_Id").isDisplayed())) 
			{
				 String selectedProjectStatus = projectStatusDD.getFirstSelectedOption().getText();
				 
				 if (compareStrings("All", selectedProjectStatus)) 
				 {
					 APP_LOGS.debug("'All' is selected as default in 'Project Status' dropdown");						 
					 comments += "\n 'All' is selected as default in 'Project Status' dropdown(Pass).";
					 
				 }
				 else 
				 {
					fail=true;							
					comments += "\n 'All' is NOT selected as default in 'Project Status' dropdown.(Fail) ";						
					APP_LOGS.debug("'All' is NOT selected as default in 'Project Status' dropdown. Test case failed");						
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Active' option is not displayed selected by default in Project Status dropdown");
				 }
			}
			else 
			{
					fail=true;					
					comments += "\n 'Project Status' dropdown is not displayed on Consolidated View tab.(Fail) ";					
					APP_LOGS.debug("'Project Status' dropdown is not displayed on Consolidated View tab. Test case failed");					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Project Status' dropdown not visible");
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
			
			
			//if pie chart not available then only verify active tester section
			noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
			
			if(noTestStepAvailableTagSize==1)
			{
					//verify active tester count label displays or not 
					if(assertTrue(getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").isDisplayed()))
					{											
							activeTesterCountLable = getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").getText();
							activeTester = Integer.parseInt(activeTesterCountLable);
							
							if(activeTester==0)
							{																					
									comments += "\n 'Active Tester' count is 0 as no project available in project section grid.(Pass) ";						
									APP_LOGS.debug("'Active Tester' count is 0 as no project available in project section grid.");	
							}
							else
							{
									fail=true;
									comments += "\n 'Active Tester' count is greater than 0 though no project available in project section grid.(Fail) ";						
									APP_LOGS.debug("'Active Tester' count is greater than 0 though no project available in project section grid.");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveTesterCountIsNotZero");
									assertTrue(false);
							}
					}
					else
					{
							fail=true;
							comments += "\n 'Active Tester' count label not displays.(Fail) ";						
							APP_LOGS.debug("'Active Tester' count label not displays.");						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Active Tester' count label not visible");
					}
				
			}
			else
			{
					//verify if active tester count, execution bar graph, executed status count is visible or not			
					if(assertTrue(getElement("StakeholderDashboardConsolidatedView_activeTesterLabel_Id").isDisplayed()))
					{
							activeTesterCountLable = getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").getText();
							activeTester = Integer.parseInt(activeTesterCountLable);
							
							if(activeTester==0)
							{
									//verify no testing done message displays or not
									if(assertTrue(getElement("StakeholderDashboardConsolidatedView_noTestingDoneLabel_Id").isDisplayed()))
									{
										comments += "\n 'No Testing done in selected date range.' message visible.(Pass) ";						
										APP_LOGS.debug("'No Testing done in selected date range.' message visible.");
									}
									else
									{
										fail=true;
										comments += "\n 'No Testing done in selected date range.' message NOT visible.(Fail) ";						
										APP_LOGS.debug("'No Testing done in selected date range.' message NOT visible.");
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
							else
							{
									//verify no testing done message displays or not
									if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_noTestingDoneLabel_Id").isDisplayed()))
									{
										comments += "\n 'No Testing done in selected date range.' message NOT visible.(Pass) ";						
										APP_LOGS.debug("'No Testing done in selected date range.' message NOT visible.");
									}
									else
									{
										fail=true;
										comments += "\n 'No Testing done in selected date range.' message visible.(Fail) ";						
										APP_LOGS.debug("'No Testing done in selected date range.' message visible.");
									}		
								
									//verify execution bar graph displays or not
									if(assertTrue(getElement("StakeholderDashboardConsolidatedView_executionBarGraph_Id").isDisplayed()))
									{
										comments += "\n Execution bar graph visible.(Pass) ";						
										APP_LOGS.debug("Execution bar graph visible.(Pass) ");
									}
									else
									{
										fail=true;
										comments += "\n Execution bar graph NOT visible.(Fail) ";						
										APP_LOGS.debug("Execution bar graph NOT visible.");
									}
									
									//verify executed count link visible or not
									if(assertTrue(getElement("StakeholderDashboardConsolidatedView_executedStatusCountLabel_Id").isDisplayed()))
									{
										String executedCount = getElement("StakeholderDashboardConsolidatedView_executedStatusCountLabel_Id").getText();
										comments += "\n Executed count is "+executedCount;						
										APP_LOGS.debug("Executed count is "+executedCount);
									}
									else
									{
										fail=true;
										comments += "\n Executed count not visible.(Fail) ";						
										APP_LOGS.debug("Executed count not visible.");
									}																					
							}
					}
					else
					{
						fail=true;
						comments += "\n Active Tester count label not visible.(Fail) ";						
						APP_LOGS.debug("Active Tester count label not visible.");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveTesterLabelNotVisible");
					}
			}	
		}
		catch(Throwable t)
		{
			t.printStackTrace();
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



