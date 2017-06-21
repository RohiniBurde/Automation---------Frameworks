/**
 * @author preeti.walde
 * Created: 7th Jan 2015
   Last Modified: 22nd Jan 2015
   Description: Code to verify contents on Consolidated View tab.
 */
package com.uat.suite.stakeholder_dashboard;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyCVTabContent extends TestSuiteBase
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
	String[ ][ ] projectDetails;
	String quarter = null;
	int fiscalYear;
	DateFormat dateFormat;
	String systemDate;
	String[] displayProjectArray;
	Select projectStatusDD;
	String[][] detailsArrayForVerification;
	String[][] detailsArrayForPieChartVerification;
	
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
	public void verifyCVTabContent(String Role) throws Exception
	{		
		String activeTesterCountLable;
		int month=0;
		int noTestStepAvailableTagSize;
		long pieChart;
		int pieChartCount;
		int projectSelectedCounter=0;
		String collaborativePassPercentage = null;
		String collaborativeFailPercentage = null;
		String collaborativeNCPercentage = null;
		
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
							
							String stakeholderDashboardTabHighlighted = getElementByClassAttr("StakeholderDashboardTab_Class").getAttribute("class");
							
							if(assertTrue(stakeholderDashboardTabHighlighted.equals(OR.getProperty("StakeholderDashboardTab_Class"))))
							{
								comments+= "\n Landing on Stakeholder Dashboard Page is successfull.(Pass) ";					
								APP_LOGS.debug("Landing on Stakeholder Dashboard Page is successfull.");		
							}
							else
							{
								fail=true;					
								comments+= "\n Landing on Stakeholder Dashboard Page is unsuccessfull.(Fail) ";					
								APP_LOGS.debug("Landing on Stakeholder Dashboard Page is unsuccessfull. Test case failed");					
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "LandingUnsuccessfulOnStakeholderDashboardPage");
								closeBrowser();
								
								throw new SkipException("Landing on Stakeholder Dashboard Page is unsuccessfull.... So Skipping all tests.");									
							}
							
							
							//verify if consolidated and detailed view tabs are visible or not
							if(assertTrue(getObject("StakeholderDashboard_consolidatedViewTab").isDisplayed() && getObject("StakeholderDashboard_detailedViewTab").isDisplayed()))
							{
									String consoldatedViewTabClass = getObject("StakeholderDashboard_consolidatedViewTab").getAttribute("class");
									
									if (assertTrue(consoldatedViewTabClass.contains("ui-tabs-selected"))) 
									{
										APP_LOGS.debug("'Consolidated View' is selected by default along with 'Detailed View' tab");						 
										comments += "\n 'Consolidated View' is selected by default along with 'Detailed View' tab.(Pass) ";										 
									}
									else 
									{
										fail=true;							
										comments += "\n 'Consolidated View' is NOT selected by default along with 'Detailed View' tab.(Fail) ";						
										APP_LOGS.debug("'Consolidated View' is NOT selected by default along with 'Detailed View' tab. Test case failed");						
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Consolidated View' is NOT selected by default");
									}
							}
							else
							{
									fail=true;					
									comments+= "\n Consolidated View and Detailed Viea tab not available on Stakeholder Dashboard Page.(Fail) ";					
									APP_LOGS.debug("Consolidated View and Detailed Viea tab not available on Stakeholder Dashboard Page. Test case failed");					
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CVAndDVTabNotAvailable");									
							}
							
							
							//verify default selection in project status dropdown and other options available like All, Active, Completed
							projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
							
							if (assertTrue(getElement("StakeholderDashboard_projectStatusDropDown_Id").isDisplayed())) 
							{
								 String selectedProjectStatus = projectStatusDD.getFirstSelectedOption().getText();
								 
								 if (compareStrings("Active", selectedProjectStatus)) 
								 {
									 APP_LOGS.debug("'Active' is selected as default in 'Project Status' dropdown");						 
									 comments += "\n 'Active' is selected as default in 'Project Status' dropdown(Pass).";
									 
								 }
								 else 
								 {
									fail=true;							
									comments += "\n 'Active' is NOT selected as default in 'Project Status' dropdown.(Fail) ";						
									APP_LOGS.debug("'Active' is NOT selected as default in 'Project Status' dropdown. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Active' option is not displayed selected by default in Project Status dropdown");
								 }
								 
								 for (int i = 0; i < projectStatusDD.getOptions().size(); i++) 
								 {
									 if (i==0) 
									 {
											 if (compareStrings("All", projectStatusDD.getOptions().get(i).getText())) 
											 {
												 APP_LOGS.debug("'All' is available in Project Status Dropdown.");
											 }
											 else 
											 {
												fail=true;									
												comments += "\n 'All' is not available in Project Status Dropdown(Fail)";								
												APP_LOGS.debug("'All' is not available in Project Status Dropdown. Test case failed");									
											 }
									 }
									 
									 if (i==1) 
									 {
											 if (compareStrings("Active", projectStatusDD.getOptions().get(i).getText())) 
											 {
												 APP_LOGS.debug("'Active' is available in Project Status Dropdown.");
											 }
											 else 
											 {
												fail=true;									
												comments += "\n 'Active' is not available in Project Status Dropdown(Fail) ";								
												APP_LOGS.debug("'Active' is not available in Project Status Dropdown. Test case failed");									
											 }
									 }
									
									 if (i==2) 
									 {
											 if (compareStrings("Completed", projectStatusDD.getOptions().get(i).getText())) 
											 {
												 APP_LOGS.debug("'Completed' is available in Project Status Dropdown.");
											 }
											 else 
											 {
												fail=true;									
												comments += "\n 'Completed' is not available in Project Status Dropdown(Fail) ";								
												APP_LOGS.debug("'Completed' is not available in Project Status Dropdown. Test case failed");									
											 }							 
									 }					 
							   }
							}
							else 
							{
									fail=true;					
									comments += "\n 'Project Status' dropdown is not displayed on Consolidated View tab.(Fail) ";					
									APP_LOGS.debug("'Project Status' dropdown is not displayed on Consolidated View tab. Test case failed");					
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Project Status' dropdown not visible");
							}
							
							
							//verify default selection in Select Date dropdown and its available options
							Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
							
							if (assertTrue(getElement("StakeholderDashboard_selectDateDropDown_Id").isDisplayed())) 
							{
								String defaultSelectedDate = selectDateDD.getFirstSelectedOption().getText();
								
								if (compareStrings("Today", defaultSelectedDate)) 
								 {
									 APP_LOGS.debug("'Today' is selected as default in 'Select Date' dropdown.");							 
									 comments += "\n 'Today' is selected as default in 'Select Date' dropdown.(Pass)";							 
								 }
								 else 
								 {
									fail=true;								
									comments += "\n 'Today' is NOT selected as default in 'Select Date' dropdown.(Fail) ";							
									APP_LOGS.debug("'Today' is NOT selected as default in 'Select Date' dropdown. Test case failed");							
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Today' option is not displayed selected by default in Select Date dropdown");
								 }
								
								for (int i = 0; i < selectDateDD.getOptions().size(); i++) 
								{
									 if (i==0) 
									 {
										 if (compareStrings("Select Filter", selectDateDD.getOptions().get(i).getText())) 
										 {
											 APP_LOGS.debug("'Select Filter' is available in Select Date Dropdown.");
										 }
										 else 
										 {
											fail=true;										
											comments += "\n 'Select Filter' is not available in Select Date Dropdown(Fail) ";									
											APP_LOGS.debug("'Select Filter' is not available in Select Date Dropdown. Test case failed");										
										 }
									 }
									 
									 if (i==1) 
									 {
										 if (compareStrings("Dates - From/To", selectDateDD.getOptions().get(i).getText())) 
										 {
											 APP_LOGS.debug("'Dates - From/To' is available in Select Date Dropdown.");
										 }
										 else 
										 {
											fail=true;										
											comments += "\n 'Dates - From/To' is not available in Select Date Dropdown(Fail) ";									
											APP_LOGS.debug("'Dates - From/To' is not available in Select Date Dropdown. Test case failed");										
										 }
									 }
									
									 if (i==2) 
									 {
										 if (compareStrings("Today", selectDateDD.getOptions().get(i).getText())) 
										 {
											 APP_LOGS.debug("'Today' is available in Select Date Dropdown.");
										 }
										 else 
										 {
											fail=true;										
											comments += "\n 'Today' is not available in Select Date Dropdown(Fail) ";									
											APP_LOGS.debug("'Today' is not available in Select Date Dropdown. Test case failed");										
										 }								 
									 }
									 
									 if (i==3) 
									 {
										 if (compareStrings("Today - 1", selectDateDD.getOptions().get(i).getText())) 
										 {
											 APP_LOGS.debug("'Today - 1' is available in Select Date Dropdown.");
										 }
										 else 
										 {
											fail=true;										
											comments += "\n 'Today - 1' is not available in Select Date Dropdown(Fail) ";									
											APP_LOGS.debug("'Today - 1' is not available in Select Date Dropdown. Test case failed");										
										 }								 
									 }
									 
									 if (i==4) 
									 {
										 if (compareStrings("Last 7 Days", selectDateDD.getOptions().get(i).getText())) 
										 {
											 APP_LOGS.debug("'Last 7 Days' is available in Select Date Dropdown.");
										 }
										 else 
										 {
											fail=true;										
											comments += "\n 'Last 7 Days' is not available in Select Date Dropdown(Fail) ";									
											APP_LOGS.debug("'Last 7 Days' is not available in Select Date Dropdown. Test case failed");										
										 }								 
									 }							 						  
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
											APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default. ");								
											comments += "\n Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
										}
										else 
										{
											fail=true;								
											comments += "\n Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
											APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year '"+fiscalYear+"' is not displayed selected in 'Fiscal Year' dropdown");
										}
									}
									else
									{
										fiscalYear = Integer.parseInt(systemDate.substring(8));
										
										if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
										{
											APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default. ");								
											comments += "\n Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
										}
										else 
										{
											fail=true;								
											comments += "\n Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
											APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year '"+fiscalYear+"' is not displayed selected in 'Fiscal Year' dropdown");
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
								
										if(month>=7 && month<= 9)
										{
												if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected())) 
												{
													APP_LOGS.debug("Current Quarter 'Q1' is selected as default. ");								
													comments += "\n Current Quarter 'Q1' is selected as default.(Pass)";
												}
												else 
												{
													fail=true;								
													comments += "\n Current Quarter 'Q1' is Not selected as default.(Fail) ";								
													APP_LOGS.debug("Current Quarter 'Q1' is Not selected as default. Test case failed");								
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q1' is Not selected as default.");
												}
										}
										else if(month>=10 && month<= 12)
										{
												if (assertTrue(getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected())) 
												{
													APP_LOGS.debug("Current Quarter 'Q2' is selected as default. ");								
													comments += "\n Current Quarter 'Q2' is selected as default.(Pass)";
												}
												else 
												{
													fail=true;								
													comments += "\n Current Quarter 'Q2' is Not selected as default.(Fail) ";								
													APP_LOGS.debug("Current Quarter 'Q2' is Not selected as default. Test case failed");								
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q2' is Not selected as default.");
												}
										}
										else if(month>=1 && month<= 3)
										{
												if (assertTrue(getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected())) 
												{
													APP_LOGS.debug("Current Quarter 'Q3' is selected as default. ");								
													comments += "\n Current Quarter 'Q3' is selected as default.";
												}
												else 
												{
													fail=true;								
													comments += "\n Current Quarter 'Q3' is Not selected as default.(Fail) ";								
													APP_LOGS.debug("Current Quarter 'Q3' is Not selected as default. Test case failed");								
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q3' is Not selected as default.");
												}
										}
										else
										{
												if (assertTrue(getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected())) 
												{
													APP_LOGS.debug("Current Quarter 'Q4' is selected as default. ");								
													comments += "\n Current Quarter 'Q4' is selected as default.(Pass) ";
												}
												else 
												{
													fail=true;								
													comments += "\n Current Quarter 'Q4' is Not selected as default.(Fail) ";								
													APP_LOGS.debug("Current Quarter 'Q4' is Not selected as default. Test case failed");								
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
												}
										}						
							}
							else 
							{
									fail=true;						
									comments += "\n Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page.(Fail) ";						
									APP_LOGS.debug("Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkboxes not available");
							}
							
							
							//verify project section grid and the columns Group, Portfolio, Project, Version, Version Lead, End Date
							if (getElement("StakeholderDashboardConsolidatedView_projectGridTopHeading_Id").isDisplayed()) 
							{
									String groupHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 1).getText();
									
									if (assertTrue(groupHeading.contains("Group"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'Group' Column");							
										comments += "\n Project Grid Contains the 'Group' Column.(Pass) ";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'Group' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'Group' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Group' Column");
									}
									
									String portfolioHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 2).getText();
									
									if (assertTrue(portfolioHeading.contains("Portfolio"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'Portfolio' Column");							
										comments += "\n Project Grid Contains the 'Portfolio' Column.(Pass) ";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'Portfolio' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'Portfolio' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Portfolio' Column");
									}
									
									String projectHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 3).getText();
									
									if (assertTrue(projectHeading.contains("Project"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'Project' Column");							
										comments += "\n Project Grid Contains the 'Project' Column.(Pass)";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'Project' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'Project' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Project' Column");
									}
									
									String versionHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 4).getText();
									
									if (assertTrue(versionHeading.contains("Version"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'Version' Column");							
										comments += "\n Project Grid Contains the 'Version' Column.(Pass)";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'Version' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'Version' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Version' Column");
									}
									
									String versionLeadHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 5).getText();
									
									if (assertTrue(versionLeadHeading.contains("Version Lead"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'Version Lead' Column");							
										comments += "\n Project Grid Contains the 'Version Lead' Column.(Pass) ";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'Version Lead' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'Version Lead' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Version Lead' Column");
									}
									
									String endDateHeading = getObject("StakeholderDashboardConsolidatedView_projectGridTableHeading1", "StakeholderDashboardConsolidatedView_projectGridTableHeading2", 6).getText();
									
									if (assertTrue(endDateHeading.contains("End Date"))) 
									{
										APP_LOGS.debug("Project Grid Contains the 'End Date' Column");							
										comments += "\n Project Grid Contains the 'End Date' Column.(Pass) ";
									}
									else 
									{
										fail=true;							
										comments += "\n Project Grid doesnot Contain the 'End Date' Column.(Fail) ";							
										APP_LOGS.debug("Project Grid doesnot Contain the 'End Date' Column. Test case failed");							
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'End Date' Column");
									}						
							}
							else 
							{
									fail=true;						
									comments += "\n Project grid Heading Columns not displayed.(Fail) ";						
									APP_LOGS.debug("Project grid Heading Columns not displayed. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project grid Heading Columns not displayed.");
							}
			         							

							//verify status links
							if(assertTrue(getElement("StakeholderDashboardConsolidatedView_statusLinkAll_Id").isDisplayed()
									&& getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").isDisplayed()
									&& getElement("StakeholderDashboardConsolidatedView_statusLinkNotCompleted_Id").isDisplayed()
									&& getElement("StakeholderDashboardConsolidatedView_statusLinkFailed_Id").isDisplayed()
									&& getElement("StakeholderDashboardConsolidatedView_statusLinkExecuted_Id").isDisplayed()))
							{
										comments += "\n All status links 'All, Passed, Not Completed, Failed and Executed' displayed on Stakeholder Dashboard Page.(Pass) ";						
										APP_LOGS.debug("All status links 'All, Passed, Not Completed, Failed and Executed' displayed on Stakeholder Dashboard Page.");
							}
							else
							{
									fail=true;
									comments += "\n Any/All status links are not displayed on Stakeholder Dashboard Page.(Fail) ";						
									APP_LOGS.debug("Any/All status links are not displayed on Stakeholder Dashboard Page. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Status Links not displayed");
							}
							
							
							//verify 'Executed link' is selected by default
							String selectedStatusLink = getElement("StakeholderDashboardConsolidatedView_statusLinkExecuted_Id").getCssValue("color");
							
							if(selectedStatusLink.contains("rgba(7, 105, 240, 1)"))
							{
								comments += "\n By default 'Executed' link displayed selected.(Pass) ";						
								APP_LOGS.debug("By default 'Executed' link displayed selected.");
							}
							else
							{
								fail=true;
								assertTrue(false);
								comments += "\n By default 'Executed' link is NOT displayed selected.(Fail) ";						
								APP_LOGS.debug("By default 'Executed' link is NOT displayed selected. Test case failed");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExecutedStatusLinkNotShownDefaultSelected");
							}
							
							
							//verify if export to ppt button visible or not
							if(assertTrue(getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").isDisplayed()))
							{
								comments += "\n Export To PPT button is displayed.(Pass) ";						
								APP_LOGS.debug("Export To PPT button is displayed.");
							}
							else
							{
								fail=true;						
								comments += "\n Export To PPT button is not displayed.(Fail) ";						
								APP_LOGS.debug("Export To PPT button is not displayed. Test case failed");						
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTButtonNotVisible");
							}
							
							
							int numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
							
							//if no project available for selected default criteria
							if(numOfProjectPresentInGrid==0)
							{	
									noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
									
									if(noTestStepAvailableTagSize==1)
									{
										if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
										{
											APP_LOGS.debug("Verified 'No Test Steps Available!' message");
											comments+="Verified 'No Test Steps Available!' message.(Pass) ";
										}
										else
										{
											fail=true;
											APP_LOGS.debug("'No Test Step Available' message not shown");
											comments+="'No Test Step Available' message not shown.(Fail) ";
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
										}
									}
									
									//verify pie chart display or not
									pieChart = (long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg').size();");
									pieChartCount = (int)pieChart;
									
									if(pieChartCount!=1)
									{
										APP_LOGS.debug("Pie Chart not exist as no project available.");
										comments+="Pie Chart not exist as no project available.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Pie Chart exist though no project is available");
										comments+="Pie Chart exist though no project is availables.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Status Pie Chart displayed though no project available");
										assertTrue(false);
									}
									
									//verify active tester count label displays or not 
									if(assertTrue(getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").isDisplayed()))
									{											
											activeTesterCountLable = getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").getText();
											int activeTester = Integer.parseInt(activeTesterCountLable);
											
											if(activeTester==0)
											{																					
													comments += "\n 'Active Tester' count label is visible and count is 0 as no project available in project section grid.(Pass) ";						
													APP_LOGS.debug("'Active Tester' count label is visible and count is 0 as no project available in project section grid.");	
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
									
									
									//verify execution bar graph displays or not
									if(assertTrue(!getElement("StakeholderDashboardConsolidatedView_executionBarGraph_Id").isDisplayed()))
									{
										comments += "\n Execution bar graph NOT visible.(Pass) ";						
										APP_LOGS.debug("Execution bar graph NOT visible.");
									}
									else
									{
										fail=true;
										comments += "\n Execution bar graph visible.(Fail) ";						
										APP_LOGS.debug("Execution bar graph visible.");
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Execution bar graph visible");
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
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Executed count visible");
									}
							}
							else
							{
									//verify projects displayed in selected project status, selected date and quarter criteria
									APP_LOGS.debug("Clicking On Test Management Tab ");								    
								    getElement("UAT_testManagement_Id").click();
								    Thread.sleep(3000);
								    
								    String totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
								    
								    int projectsPerPage;
								   
								    int totalNumOfProjectInt = Integer.parseInt(totalNumOfProject);
								    
								    rows = totalNumOfProjectInt;// total records
								    cols = 7; // hardcoaded 
								    
								    projectDetails = new String[rows][cols];
								    
								    ArrayList<String> projectArrayList = new ArrayList<String>();
								    
								    int projectDetailsRowIndex = 0; 
								    
								    do
								    {					     
									     projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
									     
									     for (int j = 1; j <= projectsPerPage; j++) 
									     {      
									      
										      getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
										      
										      String groupName_PrjPage = new Select(getElement("ProjectCreateNew_groupDropDown_Id")).getFirstSelectedOption().getText();					      
										      String portfolioName_PrjPage = new Select(getElement("ProjectCreateNew_PortfolioDropDown_Id")).getFirstSelectedOption().getText();					      
										      String projectName_PrjPage = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");					      
										      String versionName_PrjPage =getObject("ProjectCreateNew_versionTextField").getAttribute("value");					      
										      String versionLead_PrjPage = getObject("ProjectCreateNew_versionLeadDisplayField").getText();						      
										      String endDate_PrjPage = getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");      
										      
										      projectDetails[projectDetailsRowIndex][0]= groupName_PrjPage;//Group names 					      
										      projectDetails[projectDetailsRowIndex][1]= portfolioName_PrjPage;//Portfolio names 					      
										      projectDetails[projectDetailsRowIndex][2]= projectName_PrjPage;//project names ;					      
										      projectDetails[projectDetailsRowIndex][3]= versionName_PrjPage;//Version ;					      
										      projectDetails[projectDetailsRowIndex][4]= versionLead_PrjPage;//Version Lead names ;					      
										      projectDetails[projectDetailsRowIndex][5]= endDate_PrjPage;//End Date names ;     
										      
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
													        	 	
																    projectDetails[projectDetailsRowIndex][6]= storeArraylist;	
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
									
									APP_LOGS.debug("Clicking On Stakeholder Dashboard Tab");	
									getElement("UAT_stakeholderDashboard_Id").click();					
									Thread.sleep(3000);
																			
									String[][] projectDisplayedInConsolidatedView = getProjectsForFYAndQuarter(15,3,projectDetails);
																		
									//verify the projects
									verifyProjectSection(projectDisplayedInConsolidatedView);									
									
									goToFirstPageOfProject();
									
									ArrayList<String> projectsToBeVerified = new ArrayList<String>();							
									ArrayList<String> versionToBeVerified = new ArrayList<String>();
									
									//code for verifying pie chart is displayed if project is selected								
									do
									{	
										numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
										
										for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
										{   	
											int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																				
											if(grpTDSize==6)
											{
												//if project name is selected unselect the project
												if(getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).isSelected())
												{
														String projectNameFromGrid = getObject("StakeholderDashboardConsolidatedView_projectNameTD3Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD3Xpath2", x).getAttribute("title");														
														String versionFromGrid = getObject("StakeholderDashboardConsolidatedView_versionTD4Xpath1", "StakeholderDashboardConsolidatedView_versionTD4Xpath2", x).getText();													
														
														projectsToBeVerified.add(projectNameFromGrid);														
														versionToBeVerified.add(versionFromGrid);
													
														projectSelectedCounter++;
												}
											}
											else
											{
												if(getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
												{
														String projectNameFromGrid = getObject("StakeholderDashboardConsolidatedView_projectNameTD2Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD2Xpath2", x).getAttribute("title");														
														String versionFromGrid = getObject("StakeholderDashboardConsolidatedView_versionTD3Xpath1", "StakeholderDashboardConsolidatedView_versionTD3Xpath2", x).getText();													
														
														projectsToBeVerified.add(projectNameFromGrid);														
														versionToBeVerified.add(versionFromGrid);
														
														projectSelectedCounter++;
												}
											}
										}
										
									} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
									
													
									//if all project names are selected then verify pie chart
									if(projectSelectedCounter > 0)
									{
											noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
											
											if(noTestStepAvailableTagSize==1)
											{
													skip=true;
													APP_LOGS.debug("'No Test Steps Available!' for selected projects..hence cannot verify pie chart values.");
													comments+="'No Test Steps Available!' for selected projects..hence cannot verify pie chart values.(Skip) ";
											
													throw new SkipException("No Test Step Available for selected projects..hence skipping pie chart value verification.");										
											}	
											else
											{
													APP_LOGS.debug("Status Pie Chart is displayed.");
													
													String numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text').length"));
													
													if (numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3")) 
													{
														//create array to store selected project and version
														detailsArrayForVerification = new String[projectsToBeVerified.size()][6];														
														
														for (int i = 0; i < detailsArrayForVerification.length; i++) 
														{
															detailsArrayForVerification[i][0] = projectsToBeVerified.get(i);								
															detailsArrayForVerification[i][1] = versionToBeVerified.get(i);								
														}
														
														if(Role.equalsIgnoreCase("Stakeholder"))
														{
															closeBrowser();
															
															openBrowser();
															
															if(login("Admin"))
															{
																	//code for veriying pie chart on few project selection
																	getElement("UAT_dashboard_Id").click();						
																	
																	getElement("DashboardMyActivity_detailedAnalysisBtn_Id").click();
																	
																	Select selectTestPass = new Select(getElement("Dashboard_testPassDD_ID"));
																	
																	List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
																	
																	int testPassCountToBeDisplayedOnDetailedView = 0;
																	
																	float passCount = 0;
																	float failCount = 0;
																	float ncCount = 0;
																	
																	for (int i = 0; i < projectsToBeVerified.size(); i++) 
																	{
																		for(int j=0; j<projectDD.size(); j++)
																		{
																			if(projectDD.get(j).getAttribute("title").equals(projectsToBeVerified.get(i)))
																			{
																				Thread.sleep(1000);
																				
																				projectDD.get(j).click();
																				
																				APP_LOGS.debug("Project is selected.");										
									
																				List<WebElement> versionDD = getElement("DetailedAnalysis_versionDropDown_Id").findElements(By.tagName("option"));
																				
																				for(int k=0; k<versionDD.size(); k++)
																				{											
																					if(versionDD.get(k).getAttribute("title").equals(versionToBeVerified.get(i)))
																					{
																						versionDD.get(k).click();												
																						
																						if (!(selectTestPass.getFirstSelectedOption().getText().equals("No Test Pass Available"))) 
																						{
																							List<WebElement> numberOfTestPass = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));													
																							testPassCountToBeDisplayedOnDetailedView = numberOfTestPass.size();
																							
																							detailsArrayForVerification[i][2] = String.valueOf(testPassCountToBeDisplayedOnDetailedView);
																							
																							for (int l = 0; l < numberOfTestPass.size(); l++) 
																							{														
																								numberOfTestPass.get(l).click();
																								
																								String totalTestCases = getElement("DetailedAnalysis_DescriptionTableTotalTestCase_Id").getText();														
																								String totalTestSteps = getElement("DetailedAnalysis_DescriptionTableTotalTS_Id").getText();
																								
																								if (Integer.parseInt(totalTestCases) > 0 && Integer.parseInt(totalTestSteps) > 0)  
																								{
																									String testStepPassedText = getElement("DetailedAnalysis_testStepPassedText_Id").getText().substring(18);															
																									passCount = passCount + Integer.parseInt(testStepPassedText);															
																									
																									String testStepFailedText = getElement("DetailedAnalysis_testStepFailedText_Id").getText().substring(18);															
																									failCount = failCount + Integer.parseInt(testStepFailedText);															
																									
																									String testStepNCText = getElement("DetailedAnalysis_testStepNCText_Id").getText().substring(14);															
																									ncCount = ncCount + Integer.parseInt(testStepNCText);	
																									
																									int passPercentage = Math.round( ( passCount / ( passCount + failCount + ncCount ) ) * 100 );													
																									int failPercentage = Math.round( ( failCount / ( passCount + failCount + ncCount ) ) * 100 );													
																									int ncPercentage = Math.round( ( ncCount / ( passCount + failCount + ncCount ) ) * 100 );
																									
																									//put those test pass pass/fail/nc test step count into array which contains test step>0
																									detailsArrayForVerification[i][3] = String.valueOf(passPercentage);													
																									detailsArrayForVerification[i][4] = String.valueOf(failPercentage);													
																									detailsArrayForVerification[i][5] = String.valueOf(ncPercentage);		
																								}														
																							}
																							
																							testPassCountToBeDisplayedOnDetailedView = 0;
																							passCount = 0;
																							failCount = 0;
																							ncCount = 0;
																						}
																					}											
																				}							
																			}									
																		}								
																	}
																	
																	int allPassAddition = 0;
																	int allFailAddition = 0;
																	int allNCAddition = 0;
																	
																	ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
																	
																	for (int j = 0; j < detailsArrayForVerification.length; j++) 
																	{
																		//add only those projects into array who has test steps
																			if((detailsArrayForVerification[j][3]!=null) && (detailsArrayForVerification[j][4]!=null)
																					&& (detailsArrayForVerification[j][5]!=null))
																			{
																				arrayToStoreIndexOfReqProjects.add(j);
																			}								
																	}
																	
																	int projectsCountForPieChartVerification = arrayToStoreIndexOfReqProjects.size();
																	
																	detailsArrayForPieChartVerification = new String[projectsCountForPieChartVerification][6];
																	
																	int projectRowToGetDetailsFrom;
																	
																	//loop to insert values based on the indexes got from the above loop
																	for (int j = 0; j < detailsArrayForPieChartVerification.length; j++) 
																	{
																		projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
																		
																		detailsArrayForPieChartVerification[j][0] = detailsArrayForVerification[projectRowToGetDetailsFrom][0];
																		detailsArrayForPieChartVerification[j][1] = detailsArrayForVerification[projectRowToGetDetailsFrom][1];
																		detailsArrayForPieChartVerification[j][2] = detailsArrayForVerification[projectRowToGetDetailsFrom][2];
																		detailsArrayForPieChartVerification[j][3] = detailsArrayForVerification[projectRowToGetDetailsFrom][3];
																		detailsArrayForPieChartVerification[j][4] = detailsArrayForVerification[projectRowToGetDetailsFrom][4];
																		detailsArrayForPieChartVerification[j][5] = detailsArrayForVerification[projectRowToGetDetailsFrom][5];
																		
																		allPassAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][3]);								
																		allFailAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][4]);								
																		allNCAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][5]);
																	}
																	
																	//convert into percentage
																	int collaborativePassPercentageInt = Math.round(  allPassAddition / detailsArrayForPieChartVerification.length);							
																	int collaborativeFailPercentageInt = Math.round( allFailAddition / detailsArrayForPieChartVerification.length);							
																	int collaborativeNCPercentageInt = Math.round(  allNCAddition / detailsArrayForPieChartVerification.length);	
																	
																	
																	// If pass, Fail, NC percentage are same then PassPercenatge will be "PassPercenatge + 1" (Preference will be given to Pass) 
																	if(((collaborativePassPercentageInt == collaborativeFailPercentageInt) && (collaborativeFailPercentageInt == collaborativeNCPercentageInt))
																			|| (collaborativePassPercentageInt+collaborativeFailPercentageInt+collaborativeNCPercentageInt == 99)
																			|| (collaborativePassPercentageInt+collaborativeFailPercentageInt == 99)
																			|| (collaborativePassPercentageInt+collaborativeNCPercentageInt == 99))
																	{								
																		collaborativePassPercentageInt = collaborativePassPercentageInt + 1; 								
																	}
																	else if(collaborativeFailPercentageInt+collaborativeNCPercentageInt == 99)
																	{
																		collaborativeFailPercentageInt = collaborativeFailPercentageInt + 1;
																	}
																							
																	collaborativePassPercentage = Integer.valueOf(collaborativePassPercentageInt).toString();							
																	collaborativeFailPercentage = Integer.valueOf(collaborativeFailPercentageInt).toString();							
																	collaborativeNCPercentage = Integer.valueOf(collaborativeNCPercentageInt).toString();
																
																	
																	//if percentage is 0 then keep it blank
																	if (collaborativePassPercentage.equals("0")) 
																	{
																		collaborativePassPercentage = "";
																	}
																	
																	if (collaborativeFailPercentage.equals("0")) 
																	{
																		collaborativeFailPercentage = "";
																	}
																	
																	if (collaborativeNCPercentage.equals("0")) 
																	{
																		collaborativeNCPercentage = "";
																	}													
																	
																	closeBrowser();
															}
															else
															{
																APP_LOGS.debug("Login Unsuccessfull for the user with role Admin");
																comments+="Login Unsuccessfull for the user with role Admin";
															}
															
															//again login with stakeholder
															openBrowser();

															
															if(login(Role))
															{
																APP_LOGS.debug("User is logged in with role "+Role);
															}
															else
															{
																APP_LOGS.debug("Login Unsuccessfull for the user with role "+Role);
																comments+="Login Unsuccessfull for the user with role "+Role;
															}
														}
														else
														{
																//code for veriying pie chart on few project selection
																getElement("UAT_dashboard_Id").click();						
																
																getElement("DashboardMyActivity_detailedAnalysisBtn_Id").click();
																
																Select selectTestPass = new Select(getElement("Dashboard_testPassDD_ID"));
																
																List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
																
																int testPassCountToBeDisplayedOnDetailedView = 0;
																
																float passCount = 0;
																float failCount = 0;
																float ncCount = 0;
																
																for (int i = 0; i < projectsToBeVerified.size(); i++) 
																{
																	for(int j=0; j<projectDD.size(); j++)
																	{
																		if(projectDD.get(j).getAttribute("title").equals(projectsToBeVerified.get(i)))
																		{
																			Thread.sleep(1000);
																			
																			projectDD.get(j).click();
																			
																			APP_LOGS.debug("Project is selected.");										
								
																			List<WebElement> versionDD = getElement("DetailedAnalysis_versionDropDown_Id").findElements(By.tagName("option"));
																			
																			for(int k=0; k<versionDD.size(); k++)
																			{											
																				if(versionDD.get(k).getAttribute("title").equals(versionToBeVerified.get(i)))
																				{
																					versionDD.get(k).click();												
																					
																					if (!(selectTestPass.getFirstSelectedOption().getText().equals("No Test Pass Available"))) 
																					{
																						List<WebElement> numberOfTestPass = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));													
																						testPassCountToBeDisplayedOnDetailedView = numberOfTestPass.size();
																						
																						detailsArrayForVerification[i][2] = String.valueOf(testPassCountToBeDisplayedOnDetailedView);
																						
																						for (int l = 0; l < numberOfTestPass.size(); l++) 
																						{														
																							numberOfTestPass.get(l).click();
																							
																							String totalTestCases = getElement("DetailedAnalysis_DescriptionTableTotalTestCase_Id").getText();														
																							String totalTestSteps = getElement("DetailedAnalysis_DescriptionTableTotalTS_Id").getText();
																							
																							if (Integer.parseInt(totalTestCases) > 0 && Integer.parseInt(totalTestSteps) > 0)  
																							{
																								String testStepPassedText = getElement("DetailedAnalysis_testStepPassedText_Id").getText().substring(18);															
																								passCount = passCount + Integer.parseInt(testStepPassedText);															
																								
																								String testStepFailedText = getElement("DetailedAnalysis_testStepFailedText_Id").getText().substring(18);															
																								failCount = failCount + Integer.parseInt(testStepFailedText);															
																								
																								String testStepNCText = getElement("DetailedAnalysis_testStepNCText_Id").getText().substring(14);															
																								ncCount = ncCount + Integer.parseInt(testStepNCText);	
																								
																								int passPercentage = Math.round( ( passCount / ( passCount + failCount + ncCount ) ) * 100 );													
																								int failPercentage = Math.round( ( failCount / ( passCount + failCount + ncCount ) ) * 100 );													
																								int ncPercentage = Math.round( ( ncCount / ( passCount + failCount + ncCount ) ) * 100 );
																								
																								//put those test pass pass/fail/nc test step count into array which contains test step>0
																								detailsArrayForVerification[i][3] = String.valueOf(passPercentage);													
																								detailsArrayForVerification[i][4] = String.valueOf(failPercentage);													
																								detailsArrayForVerification[i][5] = String.valueOf(ncPercentage);		
																							}														
																						}
																						
																						testPassCountToBeDisplayedOnDetailedView = 0;
																						passCount = 0;
																						failCount = 0;
																						ncCount = 0;
																					}
																				}											
																			}							
																		}									
																	}								
																}
																
																int allPassAddition = 0;
																int allFailAddition = 0;
																int allNCAddition = 0;
																
																ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
																
																for (int j = 0; j < detailsArrayForVerification.length; j++) 
																{
																	//add only those projects into array who has test steps
																		if((detailsArrayForVerification[j][3]!=null) && (detailsArrayForVerification[j][4]!=null)
																				&& (detailsArrayForVerification[j][5]!=null))
																		{
																			arrayToStoreIndexOfReqProjects.add(j);
																		}								
																}
																
																int projectsCountForPieChartVerification = arrayToStoreIndexOfReqProjects.size();
																
																detailsArrayForPieChartVerification = new String[projectsCountForPieChartVerification][6];
																
																int projectRowToGetDetailsFrom;
																
																//loop to insert values based on the indexes got from the above loop
																for (int j = 0; j < detailsArrayForPieChartVerification.length; j++) 
																{
																	projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
																	
																	detailsArrayForPieChartVerification[j][0] = detailsArrayForVerification[projectRowToGetDetailsFrom][0];
																	detailsArrayForPieChartVerification[j][1] = detailsArrayForVerification[projectRowToGetDetailsFrom][1];
																	detailsArrayForPieChartVerification[j][2] = detailsArrayForVerification[projectRowToGetDetailsFrom][2];
																	detailsArrayForPieChartVerification[j][3] = detailsArrayForVerification[projectRowToGetDetailsFrom][3];
																	detailsArrayForPieChartVerification[j][4] = detailsArrayForVerification[projectRowToGetDetailsFrom][4];
																	detailsArrayForPieChartVerification[j][5] = detailsArrayForVerification[projectRowToGetDetailsFrom][5];
																	
																	allPassAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][3]);								
																	allFailAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][4]);								
																	allNCAddition += Integer.parseInt(detailsArrayForPieChartVerification[j][5]);
																}
																
																//convert into percentage
																int collaborativePassPercentageInt = Math.round(  allPassAddition / detailsArrayForPieChartVerification.length);							
																int collaborativeFailPercentageInt = Math.round( allFailAddition / detailsArrayForPieChartVerification.length);							
																int collaborativeNCPercentageInt = Math.round(  allNCAddition / detailsArrayForPieChartVerification.length);	
																
																
																// If pass, Fail, NC percentage are same then PassPercenatge will be "PassPercenatge + 1" (Preference will be given to Pass) 
																if(((collaborativePassPercentageInt == collaborativeFailPercentageInt) && (collaborativeFailPercentageInt == collaborativeNCPercentageInt))
																		|| (collaborativePassPercentageInt+collaborativeFailPercentageInt+collaborativeNCPercentageInt == 99)
																		|| (collaborativePassPercentageInt+collaborativeFailPercentageInt == 99)
																		|| (collaborativePassPercentageInt+collaborativeNCPercentageInt == 99))
																{								
																	collaborativePassPercentageInt = collaborativePassPercentageInt + 1; 								
																}
																else if(collaborativeFailPercentageInt+collaborativeNCPercentageInt == 99)
																{
																	collaborativeFailPercentageInt = collaborativeFailPercentageInt + 1;
																}
																						
																collaborativePassPercentage = Integer.valueOf(collaborativePassPercentageInt).toString();							
																collaborativeFailPercentage = Integer.valueOf(collaborativeFailPercentageInt).toString();							
																collaborativeNCPercentage = Integer.valueOf(collaborativeNCPercentageInt).toString();
															
																
																//if percentage is 0 then keep it blank
																if (collaborativePassPercentage.equals("0")) 
																{
																	collaborativePassPercentage = "";
																}
																
																if (collaborativeFailPercentage.equals("0")) 
																{
																	collaborativeFailPercentage = "";
																}
																
																if (collaborativeNCPercentage.equals("0")) 
																{
																	collaborativeNCPercentage = "";
																}													
														}
														
														
														//click on Stakeholder dashboard page
														getElement("UAT_stakeholderDashboard_Id").click();
														Thread.sleep(1000);
																												
														//verify if all projects are selected to check values on pie chart								
														do
														{	
															numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
															
															for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
															{   	
																int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																									
																if(grpTDSize==6)
																{
																	//if project name is selected unselect the project
																	if(getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).isSelected())
																	{
																		projectSelectedCounter++;
																	}
																}
																else
																{
																	if(getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
																	{
																		projectSelectedCounter++;
																	}
																}
															}
															
														} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
														
														if(projectSelectedCounter>0)
														{
																String PassPercentageOnPieChart = (String) eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text:eq(0) tspan').text()");	
																String FailPercentageOnPieChart = (String) eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text:eq(2) tspan').text()");							
																String NCPercentageOnPieChart = (String) eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text:eq(1) tspan').text()");
																
																if (compareStrings(collaborativePassPercentage,PassPercentageOnPieChart))
																{								
																	APP_LOGS.debug("'Pass' percentage displayed on Pie Chart is Correct for selected project.");								
																	comments += "\n- 'Pass' percentage displayed on Pie Chart is Correct for selected project.(Pass) ";							
																}
																else 
																{
																	fail=true;								
																	APP_LOGS.debug("'Pass' percentage displayed on Pie Chart is NOT Correct for selected project.");								
																	comments += "\n- 'Pass' percentage displayed on Pie Chart is NOT Correct for selected project.(Fail) ";								
																	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Pass' percentage on Pie Chart is NOT Correct for selected project");
																}							
																
																if (compareStrings(collaborativeFailPercentage,FailPercentageOnPieChart))
																{
																	APP_LOGS.debug("'Fail' percentage displayed on Pie Chart is Correct for selected project.");								
																	comments += "\n- 'Fail' percentage displayed on Pie Chart is Correct for selected project.(Pass) ";							
																}
																else 
																{
																	
																	fail=true;								
																	APP_LOGS.debug("'Fail' percentage displayed on Pie Chart is NOT Correct for selected project.");								
																	comments += "\n- 'Fail' percentage displayed on Pie Chart is NOT Correct for selected project.(Fail) ";								
																	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Fail' percentage on Pie Chart is NOT Correct for selected project");								
																}
																							
																if (compareStrings(collaborativeNCPercentage,NCPercentageOnPieChart))
																{
																	APP_LOGS.debug("'Not Completed' percentage displayed on Pie Chart is Correct for selected project.");								
																	comments += "\n- 'Not Completed' percentage displayed on Pie Chart is Correct for selected project.(Pass) ";
																
																}
																else 
																{								
																	fail=true;								
																	APP_LOGS.debug("'Not Completed' percentage displayed on Pie Chart is NOT Correct for selected project.");								
																	comments += "\n- 'Not Completed' percentage displayed on Pie Chart is NOT Correct for selected project.(Fail)";								
																	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'NC' percentage on Pie Chart is NOT Correct for selected project");
																}													
														}
													}	
													else
													{
														APP_LOGS.debug("Pie chart contains only one value. Hence cannot verify pie chart values for selected project.");
														comments+="Pie chart contains only one value. Hence cannot verify pie chart values for selected project.(Pass) ";
													}
													
													//verify active tester count display or not
													if(assertTrue(getElement("StakeholderDashboardConsolidatedView_activeTesterLabel_Id").isDisplayed()))
													{
															comments += "\n Active Tester count visible.(Pass) ";						
															APP_LOGS.debug("Active Tester count visible ");
															
															activeTesterCountLable = getElement("StakeholderDashboardConsolidatedView_activeTesterCount_Id").getText();
															int activeTester = Integer.parseInt(activeTesterCountLable);
															
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
										else
										{
												noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
												
												if(noTestStepAvailableTagSize==1)
												{
														APP_LOGS.debug("'No Test Steps Available!' visible as no project is selected.");
														comments+="'No Test Steps Available!' visible as no project is selected.(Pass) ";
												}	
										}
							}
		         }
		         catch(Throwable t)
		         {
		        	 t.printStackTrace();
		        	 fail=true;
		        	 assertTrue(false);
		        	 APP_LOGS.debug("Exception occured in Consolidated View tab of Stakeholder Dashboard Page");				
		         }			
		         closeBrowser();
		         
		}
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
		}	
		
	}
	
	
	//function to get the number of project falls in defined criteria of quarter and fiscal year
	private String[][] getProjectsForFYAndQuarter(int fiscalYear ,int quarter, String[][] projectArrayToSearchIn) throws ParseException
	{
		Date projectEndDateInDateFormat;
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForReqFYAndQuarter = null;		
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");		
		Date date = new Date();		
		systemDate = dateFormat.format(date);		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		String projectEndDate;
		int projectsCountForReqFYandQuarter;		
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			projectEndDate = projectArrayToSearchIn[i][5];
			projectEndDateInDateFormat = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			if (((getFiscalYear(projectEndDate)==fiscalYear || getQuarter(projectEndDate) == quarter) 
					&& (projectEndDateInDateFormat.compareTo(sysdateInDateFormat)>=0)) && (projectArrayToSearchIn[i][6]!=null)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}			
		}
			
		projectsCountForReqFYandQuarter = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForReqFYAndQuarter = new String[projectsCountForReqFYandQuarter][7];
		
		int projectRowToGetDetailsFrom;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqFYAndQuarter.length; j++) 
		{
			projectRowToGetDetailsFrom = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqFYAndQuarter[j][0] = projectArrayToSearchIn[projectRowToGetDetailsFrom][0];
			projectDetailsForReqFYAndQuarter[j][1] = projectArrayToSearchIn[projectRowToGetDetailsFrom][1];
			projectDetailsForReqFYAndQuarter[j][2] = projectArrayToSearchIn[projectRowToGetDetailsFrom][2];
			projectDetailsForReqFYAndQuarter[j][3] = projectArrayToSearchIn[projectRowToGetDetailsFrom][3];
			projectDetailsForReqFYAndQuarter[j][4] = projectArrayToSearchIn[projectRowToGetDetailsFrom][4];
			projectDetailsForReqFYAndQuarter[j][5] = projectArrayToSearchIn[projectRowToGetDetailsFrom][5];
			projectDetailsForReqFYAndQuarter[j][6] = projectArrayToSearchIn[projectRowToGetDetailsFrom][6];
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
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is same as count "+filterArray.length+".");
				}
				else
				{
					fail=true;
					comments += "\n Displayed project count "+projectVerifiedCount+" is same as count "+filterArray.length+".(Fail) ";						
					APP_LOGS.debug("Displayed project count "+projectVerifiedCount+" is same as count "+filterArray.length+". Test case failed");
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
	
	//click previous till first page of project section	
	private void goToFirstPageOfProject() throws IOException
	{
		do
		{
			 APP_LOGS.debug("Go to First Page.");
			
		}while(ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_PreviousLink"));
		
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
