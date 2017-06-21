package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class VerifyDVTabContent extends TestSuiteBase 
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
	Select selectGroup ;
	Select selectPortfolio;
	
	
	DateFormat dateFormat;
	String systemDate;
	
	
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
	public void testVerifyDVTabContent(String Role ) throws Exception
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
				
				if( Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager")
	        	 	    || Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Stakeholder+Admin")
	        	 		||Role.equalsIgnoreCase("Stakeholder+Version Lead")|| Role.equalsIgnoreCase("Admin+Stakeholder"))
	        	 	{
						getElement("UAT_stakeholderDashboard_Id").click();
					
						Thread.sleep(1000);
	        	 	}
				
				
				if (getObject("StakeholderDashboard_consolidatedViewPageTab").isDisplayed() && 
						getObject("StakeholderDashboard_detailedViewPageTab").isDisplayed()) 
				{
					String consoldatedViewClass = getObject("StakeholderDashboard_consolidatedViewPageTab").getAttribute("class");
					
					if (assertTrue(consoldatedViewClass.contains("ui-tabs-selected"))) 
					{
						APP_LOGS.debug("'Consolidated View' is selected by default along with 'Detailed View' tab");
						 
						comments += "\n- 'Consolidated View' is selected by default along with 'Detailed View' tab";
						 
					}
					else 
					{
						fail=true;
							
						comments += "\n- 'Consolidated View' is NOT selected by default along with 'Detailed View' tab.(Fail) ";
						
						APP_LOGS.debug("'Consolidated View' is NOT selected by default along with 'Detailed View' tab. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Consolidated View' is NOT selected by default");
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- Consolidated view and Detailed View tabs are not displayed on Stakeholder Dashboard Page.(Fail) ";
					
					APP_LOGS.debug("Consolidated view and Detailed View tabs are not displayed on Stakeholder Dashboard Page. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Consolidated and Detailed View tabs not displayed");
					
					throw new SkipException("Consolidated view and Detailed View tabs are not displayed on Stakeholder Dashboard Page.... So Skipping all tests");
				}
				

				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				
				
				Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
				
				if (assertTrue(getElement("StakeholderDashboard_projectStatusDropDown_Id").isDisplayed())) 
				{
					 String selectedProjectStatus = projectStatusDD.getFirstSelectedOption().getText();
					 
					 
					 for (int i = 0; i < projectStatusDD.getOptions().size(); i++) 
					 {
						 if (i==0) 
						 {
							 if (compareStrings("All", projectStatusDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'All' is displayed in Project Status Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'All' is not displayed in Project Status Dropdown(Fail) ";
								
								APP_LOGS.debug("'All' is not displayed in Project Status Dropdown. Test case failed");
									
							 }
						 }
						 
						 if (i==1) 
						 {
							 if (compareStrings("Active", projectStatusDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Active' is displayed in Project Status Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Active' is not displayed in Project Status Dropdown(Fail) ";
								
								APP_LOGS.debug("'Active' is not displayed in Project Status Dropdown. Test case failed");
									
							 }
						 }
						
						 if (i==2) 
						 {
							 if (compareStrings("Completed", projectStatusDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Completed' is displayed in Project Status Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Completed' is not displayed in Project Status Dropdown(Fail) ";
								
								APP_LOGS.debug("'Completed' is not displayed in Project Status Dropdown. Test case failed");
									
							 }
							 
						 }
						 
						  
					 }
					 
					 
					 
					 if (compareStrings("Active", selectedProjectStatus)) 
					 {
						 APP_LOGS.debug("In 'Project Status' dropdown 'Active' is selected as default.");
						 
						 comments += "\n- In 'Project Status' dropdown 'Active' is selected as default.";
						 
					 }
					 else 
					 {
						fail=true;
							
						comments += "\n- In 'Project Status' dropdown 'Active' is NOT selected as default.(Fail) ";
						
						APP_LOGS.debug("In 'Project Status' dropdown 'Active' is NOT selected as default. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Status dropdown displaying other option than 'Active'");
					 }
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Project Status' dropdown is not displayed on Stakeholder Dashboard Page.(Fail) ";
					
					APP_LOGS.debug("'Project Status' dropdown is not displayed on Stakeholder Dashboard Page. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Project Status' dropdown not displayed");
				}
				
				
				
				
				
				Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));
				
				if (assertTrue(getElement("StakeholderDashboard_selectDateDropDown_Id").isDisplayed())) 
				{
					 String defaultSelectedDate = selectDateDD.getFirstSelectedOption().getText();
					 
					 
					 for (int i = 0; i < selectDateDD.getOptions().size(); i++) 
					 {
						 if (i==0) 
						 {
							 if (compareStrings("Select Filter", selectDateDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Select Filter' is displayed in Select Date Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Select Filter' is not displayed in Select Date Dropdown(Fail) ";
								
								APP_LOGS.debug("'Select Filter' is not displayed in Select Date Dropdown. Test case failed");
									
							 }
						 }
						 
						 if (i==1) 
						 {
							 if (compareStrings("Dates - From/To", selectDateDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Dates - From/To' is displayed in Select Date Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Dates - From/To' is not displayed in Select Date Dropdown(Fail) ";
								
								APP_LOGS.debug("'Dates - From/To' is not displayed in Select Date Dropdown. Test case failed");
									
							 }
						 }
						
						 if (i==2) 
						 {
							 if (compareStrings("Today", selectDateDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Today' is displayed in Select Date Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Today' is not displayed in Select Date Dropdown(Fail) ";
								
								APP_LOGS.debug("'Today' is not displayed in Select Date Dropdown. Test case failed");
									
							 }
							 
						 }
						 
						 if (i==3) 
						 {
							 if (compareStrings("Today - 1", selectDateDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Today - 1' is displayed in Select Date Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Today - 1' is not displayed in Select Date Dropdown(Fail) ";
								
								APP_LOGS.debug("'Today - 1' is not displayed in Select Date Dropdown. Test case failed");
									
							 }
							 
						 }
						 
						 if (i==4) 
						 {
							 if (compareStrings("Last 7 Days", selectDateDD.getOptions().get(i).getText())) 
							 {
								 APP_LOGS.debug("'Last 7 Days' is displayed in Select Date Dropdown.");
							 }
							 else 
							 {
								fail=true;
									
								comments += "\n- 'Last 7 Days' is not displayed in Select Date Dropdown(Fail) ";
								
								APP_LOGS.debug("'Last 7 Days' is not displayed in Select Date Dropdown. Test case failed");
									
							 }
							 
						 }
						 
						  
					 }
					 
					 
					 
					 if (compareStrings("Today", defaultSelectedDate)) 
					 {
						 APP_LOGS.debug("In 'Select Date' dropdown 'Today' is selected as default.");
						 
						 comments += "\n- In 'Select Date' dropdown 'Today' is selected as default.";
						 
					 }
					 else 
					 {
						fail=true;
							
						comments += "\n- In 'Select Date' dropdown 'Today' is NOT selected as default.(Fail) ";
						
						APP_LOGS.debug("In 'Select Date' dropdown 'Today' is NOT selected as default. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Select Date dropdown displaying other option than 'Today'");
					 }
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Select Date' dropdown is not displayed on Stakeholder Dashboard Page.(Fail) ";
					
					APP_LOGS.debug("'Select Date' dropdown is not displayed on Stakeholder Dashboard Page. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Select Date' dropdown not displayed");
				}
				
				
				
				
				Select fiscalYearDD = new Select(getElement("StakeholderDashboard_fiscalYearDropDown_Id"));
				
				String defaultSelectedFiscalYear = fiscalYearDD.getFirstSelectedOption().getText();
				
				int fiscalYear;
				
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				
				Date date = new Date();
				
				systemDate = dateFormat.format(date);
				
				int month = Integer.parseInt(systemDate.substring(0, 2));
				
				
				if (assertTrue(getElement("StakeholderDashboard_fiscalYearDropDown_Id").isDisplayed())) 
				{
					if(month >= 7)		
					{				
						fiscalYear = Integer.parseInt(systemDate.substring(8))+1;	
						
						if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
						{
							APP_LOGS.debug("Current fiscal year is displayed in 'Fiscal Year' dropdown as default. ");
							
							comments += "\n- Current fiscal year is displayed in 'Fiscal Year' dropdown as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current fiscal year is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";
							
							APP_LOGS.debug("Current fiscal year is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year is not displayed in 'Fiscal Year' dropdown");
						}
					}
					else
					{
						fiscalYear = Integer.parseInt(systemDate.substring(8));
						
						if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
						{
							APP_LOGS.debug("Current fiscal year is displayed in 'Fiscal Year' dropdown as default. ");
							
							comments += "\n- Current fiscal year is displayed in 'Fiscal Year' dropdown as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current fiscal year is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";
							
							APP_LOGS.debug("Current fiscal year is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year is not displayed in 'Fiscal Year' dropdown");
						}
					}
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page.(Fail) ";
					
					APP_LOGS.debug("'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Fiscal Year' dropdown not displayed");
				}
				
				
				if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isDisplayed() && 
						getElement("StakeholderDashboard_Q2Checkbox_Id").isDisplayed() &&
						getElement("StakeholderDashboard_Q3Checkbox_Id").isDisplayed() &&
						getElement("StakeholderDashboard_Q4Checkbox_Id").isDisplayed())) 
				{
					
					if(month>=7 && month<= 9)
					{
						if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected())) 
						{
							APP_LOGS.debug("Current Quarter is selected as default. ");
							
							comments += "\n- Current Quarter is selected as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current Quarter is Not selected as default.(Fail) ";
							
							APP_LOGS.debug("Current Quarter is Not selected as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
						}
					}
					else if(month>=10 && month<= 12)
					{
						if (assertTrue(getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected())) 
						{
							APP_LOGS.debug("Current Quarter is selected as default. ");
							
							comments += "\n- Current Quarter is selected as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current Quarter is Not selected as default.(Fail) ";
							
							APP_LOGS.debug("Current Quarter is Not selected as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
						}
					}
					else if(month>=1 && month<= 3)
					{
						if (assertTrue(getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected())) 
						{
							APP_LOGS.debug("Current Quarter is selected as default. ");
							
							comments += "\n- Current Quarter is selected as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current Quarter is Not selected as default.(Fail) ";
							
							APP_LOGS.debug("Current Quarter is Not selected as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
						}
					}
					else
					{
						if (assertTrue(getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected())) 
						{
							APP_LOGS.debug("Current Quarter is selected as default. ");
							
							comments += "\n- Current Quarter is selected as default.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- Current Quarter is Not selected as default.(Fail) ";
							
							APP_LOGS.debug("Current Quarter is Not selected as default. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
						}
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page.(Fail) ";
					
					APP_LOGS.debug("Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkboxes not displayed");
				}
				
				
				// A Project grid containing following columns: Project Name, Version, Version Lead, End Date.
				
				if (getObject("SDDetailedView_projectGridTable").isDisplayed()) 
				{
					String projectTopHeading = getObject("SDDetailedView_projectGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 1).getText();
					
					if (assertTrue(projectTopHeading.contains("Project"))) 
					{
						APP_LOGS.debug("Project Grid Contains the 'Project' Column");
						
						comments += "\n- Project Grid Contains the 'Project' Column.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid doesnot Contain the 'Project' Column.(Fail) ";
						
						APP_LOGS.debug("Project Grid doesnot Contain the 'Project' Column. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Project' Column");
					}
					
					String versionTopHeading = getObject("SDDetailedView_projectGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 2).getText();
					
					if (assertTrue(versionTopHeading.contains("Version"))) 
					{
						APP_LOGS.debug("Project Grid Contains the 'Version' Column");
						
						comments += "\n- Project Grid Contains the 'Version' Column.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid doesnot Contain the 'Version' Column.(Fail) ";
						
						APP_LOGS.debug("Project Grid doesnot Contain the 'Version' Column. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Version' Column");
					}
					
					String versionLeadTopHeading = getObject("SDDetailedView_projectGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 3).getText();
					
					if (assertTrue(versionLeadTopHeading.contains("Version Lead"))) 
					{
						APP_LOGS.debug("Project Grid Contains the 'Version Lead' Column");
						
						comments += "\n- Project Grid Contains the 'Version Lead' Column.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid doesnot Contain the 'Version Lead' Column.(Fail) ";
						
						APP_LOGS.debug("Project Grid doesnot Contain the 'Version Lead' Column. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'Version Lead' Column");
					}
					
					String endDateTopHeading = getObject("SDDetailedView_projectGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 4).getText();
					
					if (assertTrue(endDateTopHeading.contains("End Date"))) 
					{
						APP_LOGS.debug("Project Grid Contains the 'End Date' Column");
						
						comments += "\n- Project Grid Contains the 'End Date' Column.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid doesnot Contain the 'End Date' Column.(Fail) ";
						
						APP_LOGS.debug("Project Grid doesnot Contain the 'End Date' Column. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid doesnot Contain the 'End Date' Column");
					}
					
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- Project grid Table is not displayed.(Fail) ";
					
					APP_LOGS.debug("Project grid Table is not displayed. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "A Project grid Table is not displayed.");
					
					throw new SkipException("Project grid Table is not displayed.... So Skipping all tests");
				}
				
				
				
				//Test pass(es)Status grid contains
				
				if (compareStrings("rgt reptTestPassList", getObject("SDDetailedView_testPassStatusRightDiv").getAttribute("class"))) 
				{
					String testPassStatusText = getElement("SDDetailedView_testPassStatusText_Id").getText();
					
					if (compareStrings("Test Pass(es) Status", testPassStatusText)) 
					{
						
						String noTestPassAvailableText = getElement("SDDetailedView_noTestPassAvailableText_Id").getText();
						
						if (noTestPassAvailableText.equals("No Test Pass(es) Available.")) 
						{
							
							APP_LOGS.debug("No Test Pass(es) Available. - text is displayed in Test Pass(es) Status grid.");
							
							comments += "\n- No Test Pass(es) Available. - text is displayed in Test Pass(es) Status grid..";
							
						}
						else 
						{
							if (assertTrue(getElement("SDDetailedView_testPassTable_Id").isDisplayed())) 
							{
								
								String projectTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 1).getText();
								
								if (assertTrue(projectTopHeadingInTestPassStatus.contains("Project"))) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'Project' Column");
									
									comments += "\n- Test Pass Grid Contains the 'Project' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'Project' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'Project' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'Project' Column");
								}
								
								String versionTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 2).getText();
								
								if (assertTrue(versionTopHeadingInTestPassStatus.contains("Version"))) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'Version' Column");
									
									comments += "\n- Test Pass Grid Contains the 'Version' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'Version' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'Version' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'Version' Column");
								}
								
								String testPassTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 3).getText();
																
								String clickonLinkForMoreDetailsText =getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableSpanHeading2", 3).getText();
																
								if (assertTrue(testPassTopHeadingInTestPassStatus.contains("Test Pass")) && 
										compareStrings("Click on link for more details", clickonLinkForMoreDetailsText)) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'Test Pass' Column");
									
									comments += "\n- Test Pass Grid Contains the 'Test Pass' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'Test Pass' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'Test Pass' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'Test Pass' Column");
								}
								
								String testManagerTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 4).getText();
								
								if (assertTrue(testManagerTopHeadingInTestPassStatus.contains("Test Manager"))) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'Test Manager' Column");
									
									comments += "\n- Test Pass Grid Contains the 'Test Manager' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'Test Manager' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'Test Manager' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'Test Manager' Column");
								}
								
								String endDateTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 5).getText();
								
								if (assertTrue(endDateTopHeadingInTestPassStatus.contains("End Date"))) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'End Date' Column");
									
									comments += "\n- Test Pass Grid Contains the 'End Date' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'End Date' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'End Date' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'End Date' Column");
								}
								
								String statusTopHeadingInTestPassStatus = getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableHeadings2", 6).getText();
																
								String clickStatusBlockForMoreDetailsText =getObject("SDDetailedView_testPassGridTableHeadings1", "SDDetailedView_gridTableSpanHeading2", 6).getText();

								if (assertTrue(statusTopHeadingInTestPassStatus.contains("Status (%)")) &&
										compareStrings("Click Status block for more details", clickStatusBlockForMoreDetailsText)) 
								{
									APP_LOGS.debug("Test Pass Grid Contains the 'Status' Column");
									
									comments += "\n- Test Pass Grid Contains the 'Status' Column.";
								}
								else 
								{
									fail=true;
									
									comments += "\n- Test Pass Grid doesnot Contain the 'Status' Column.(Fail) ";
									
									APP_LOGS.debug("Test Pass Grid doesnot Contain the 'Status' Column. Test case failed");
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Grid doesnot Contain the 'Status' Column");
								}
								
							}
							else 
							{
								fail=true;
								
								comments += "\n- Test pass Status grid is not displayed (No Test Pass(es) Available - also not displayed).(Fail) ";
								
								APP_LOGS.debug("Test pass Status grid is not displayed (No Test Pass(es) Available - also not displayed). Test case failed");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test pass Status grid is not displayed");
							}
							
						}
						
					}
					else 
					{
						fail=true;
						
						comments += "\n- Test Pass(es) Status Text is not displayed at right.(Fail) ";
						
						APP_LOGS.debug("Test Pass(es) Status Text is not displayed at right. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass(es) Status Text is not displayed at right");
					}
				}
				else 
				{
					fail=true;
					
					comments += "\n- Test Pass(es) Status Grid is not displayed at right.(Fail) ";
					
					APP_LOGS.debug("Test Pass(es) Status Grid is not displayed at right. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass(es) Status Grid is not displayed at right");
				}
				
				
				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				
				int noTestingDoneflag = 0;
				
				int barGraphFlag = 0;
				
				for (int i = 1; i < selectDateDD.getOptions().size(); i++) 
				{
				
					selectDateDD.selectByIndex(i);
				
					Thread.sleep(2500);
					
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") || noTestStepAvailableTagSize == 1 ) )	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						if (getElement("SDDetailedView_noTestingDoneInSelectedRangeText_Id").isDisplayed()) 
						{
							String noTestingDoneText = getElement("SDDetailedView_noTestingDoneInSelectedRangeText_Id").getText();
							
							if (assertTrue(noTestingDoneText.contains("No Testing done in selected date range."))) 
							{
								noTestingDoneflag = 1;
							}
							else 
							{
								noTestingDoneflag = 2;
								
								break;
							}
						}
						else 
						{
							if (getElement("SDDetailedView_exicutedBarGraph_Id").isDisplayed() && getObject("SDDetailedView_exicutedText").isDisplayed()) 
							{
								barGraphFlag = 1;
							}
							else 
							{
								barGraphFlag = 2;
								
								break;
							}
						}
					}
						
				}

				if (noTestingDoneflag == 1) 
				{
					APP_LOGS.debug("'No Testing done in selected date range.' is displayed when No Testing is done in selected Date range");
					
					comments += "\n- 'No Testing done in selected date range.' is displayed when No Testing is done in selected Date range ";
				}
				else if (noTestingDoneflag == 2)  
				{
					fail=true;
					
					assertTrue(false);
					
					comments += "\n- 'No Testing done in selected date range.' is displayed when No Testing is done in selected Date range.(Fail) ";
					
					APP_LOGS.debug("'No Testing done in selected date range.' is displayed when No Testing is done in selected Date range");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Testing done in selected date range' is not Displayed");
				}
				
				
				
				if (barGraphFlag ==1) 
				{
					APP_LOGS.debug("Execution bar graph is displayed in the Status Section after selection of Select Date Filter. ");
					
					comments += "\n- Execution bar graph is displayed in the Status Section after selection of Select Date Filter..";
				}
				else if (barGraphFlag ==2) 
				{
					fail=true;
					
					assertTrue(false);
					
					comments += "\n- Execution bar graph is NOT displayed in the Status Section after selection of Select Date Filter.(Fail) ";
					
					APP_LOGS.debug("Execution bar graph is NOT displayed in the Status Section after selection of Select Date Filter.");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Execution bar graph is NOT displayed");
				}
				
				
				
				
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				String totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
				
				int projectsPerPage;
			
				int totalNumOfProjectInt = Integer.parseInt(totalNumOfProject);
				
				rows = totalNumOfProjectInt;// total records
				cols = 6; // hardcoaded	
				
				projectDetails = new String[rows][cols];
				
				
				int projectDetailsRowIndex = 0;	
				
				do 
				{
					
					projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
					
					for (int j = 1; j <= projectsPerPage; j++) 
					{						
						
						getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
						
						String groupName = getElement("ProjectCreateNew_groupDropDown_Id").getAttribute("value");
						
						String portfolioName = getElement("ProjectCreateNew_PortfolioDropDown_Id").getAttribute("value");
						
						String projectName = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
						
						String versionName =getObject("ProjectCreateNew_versionTextField").getAttribute("value");
						
						String startDate = getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
						
						String endDate = getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");						
						
						projectDetails[projectDetailsRowIndex][0]= groupName;//End Date names 
						
						projectDetails[projectDetailsRowIndex][1]= portfolioName;//End Date names 
						
						projectDetails[projectDetailsRowIndex][2]= projectName;//project names ;
						
						projectDetails[projectDetailsRowIndex][3]= versionName;//Version ;
						
						projectDetails[projectDetailsRowIndex][4]= startDate;//Start Date names ;
						
						projectDetails[projectDetailsRowIndex][5]= endDate;//End Date names ;					
						
						Thread.sleep(500);
						
						getObject("Projects_viewAllProjectLink").click();
						
						Thread.sleep(500);						
						
						projectDetailsRowIndex++;						
						
					}			
					
				} while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
				
				
				getElement("UAT_stakeholderDashboard_Id").click();
				
				Thread.sleep(3000);
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				

				String[][] projectDetailsDisplayedInDetailedView = getProjectDisplayInDetailedView(projectDetails);
				
				/*for (int i = 0; i < projectDetailsDisplayedInDetailedView.length; i++) 
				{
					System.out.println("Group "+i +"- "+ projectDetailsDisplayedInDetailedView[i][0]);
					System.out.println("Portfolio "+i +"- "+ projectDetailsDisplayedInDetailedView[i][1]);
					System.out.println("Project "+i +"- "+ projectDetailsDisplayedInDetailedView[i][2]);
					System.out.println("Version "+i +"- "+ projectDetailsDisplayedInDetailedView[i][3]);
					System.out.println("StartDate "+i +"- "+ projectDetailsDisplayedInDetailedView[i][4]);
					System.out.println("EndDate "+i +"- "+ projectDetailsDisplayedInDetailedView[i][5]);
					
					
					
				}*/
				
				
				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
				
				if (!( defaultGroupSelected.equalsIgnoreCase("No Group") || noTestStepAvailableTagSize == 1 ) ) //noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
				{
					
					//Verifying Group Drop down elements
					
					int groupCount = 0;
					
					String[] uniqueGroupArray = getUniqueGroupsFromArray(projectDetailsDisplayedInDetailedView);

					if (compareIntegers(uniqueGroupArray.length, selectGroup.getOptions().size())) 
					{
					
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
							
							for (int j = 0; j < uniqueGroupArray.length; j++) 
							{
								if (groupNamePresentInGroupDD.equals(uniqueGroupArray[j])) 
								{
									groupCount++;
									
									break;
								}
							}
						}
						
					}
					
					System.out.println("groupCount : "+groupCount);
					
					if (compareIntegers(uniqueGroupArray.length, groupCount)) 
					{
						
						APP_LOGS.debug("Group drop down contains those Groups of project whose end date is equal to or greater than today's date and present in the current quarter.");
						
						comments += "\n- Group drop down contains those Groups of project whose end date is equal to or greater than today's date and present in the current quarter.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Group Drop Down contains unexpected Group Name.(Fail) ";
						
						APP_LOGS.debug("Group Drop Down contains unexpected Group Name. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Group Drop Down contains unexpected Group Name");
					}
					
					
					
					//Verifying Portfolio Drop down elements
					
					//For verifying the portfolio .. need to apply proper verification logic 
					//Following logic is not enough for the verification
					
					
					int portfolioCount = 0;
					
					String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectDetailsDisplayedInDetailedView);
					
					for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
					{
						String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
						
						for (int j = 0; j < projectDetails.length; j++) 
						{
							if (portfolioNamePresentInPortfolioDD.equals(projectDetails[j][1])) //portfolioNamePresentInPortfolioDD.equals(projectDetails[k][1])
							{
								portfolioCount = 1;
							}
						}
					}
						
					System.out.println("portfolioCount : "+portfolioCount);
					
					if (compareIntegers(1, portfolioCount)) 
					{
						
						APP_LOGS.debug("Portfolio drop down contains those Portfolio of project whose end date is equal to or greater than today's date and present in the current quarter.");
						
						comments += "\n- Portfolio drop down contains those Portfolio of project whose end date is equal to or greater than today's date and present in the current quarter.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Portfolio Drop Down contains unexpected Portfolio Name.(Fail) ";
						
						APP_LOGS.debug("Portfolio Drop Down contains unexpected Portfolio Name. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Portfolio Drop Down contains unexpected Portfolio Name");
					}
					
					
					//Verifying Projects 
					
					//Get the total number of projects present in the detailed view
					int projectCount = 0;
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
						{
							selectPortfolio.selectByIndex(j);
							
							Thread.sleep(500);
							
							do
							{
								int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
							
								for (int k = 0; k < numOfProjectPresentInGrid; k++) 
								{
									projectCount++;
								}
							} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
							
						}
						
					}
					
					
					if (compareIntegers(projectDetailsDisplayedInDetailedView.length, projectCount)) 
					{
						
						APP_LOGS.debug("Projects whose end date is equal to or greater than today's date and present in the current quarter are displayed in Grid.");
						
						comments += "\n- Projects whose end date is equal to or greater than today's date and present in the current quarter are displayed in Grid.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid contains unexpected Project(s).(Fail) ";
						
						APP_LOGS.debug("Project Grid contains unexpected Project(s). Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid contains unexpected Project(s).");
					}
					
				}
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					/*int flagGroupPresent = 0;
					int flagPortfolioPresent= 0;
					int flagProjectPresent= 0;
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
						// Check if the group fetched above is present in group array or not
						
						for (int j = 0; j < projectDetailsDisplayedInDetailedView.length; j++) 
						{
							
							if (groupNamePresentInGroupDD.equals(projectDetailsDisplayedInDetailedView[j][0])) 
							{
								flagGroupPresent = 1;	
								
								selectGroup.selectByVisibleText(groupNamePresentInGroupDD);
								
								for (int j2 = 1; j2 < selectPortfolio.getOptions().size(); j2++) 
								{
	
									String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(j2).getText();
									
									for (int k = 0; k < projectDetailsDisplayedInDetailedView.length; k++) 
									{
										
										if (portfolioNamePresentInPortfolioDD.equals(projectDetailsDisplayedInDetailedView[k][1])) 
										{
											
											flagPortfolioPresent = 1;
											
											selectPortfolio.selectByVisibleText(portfolioNamePresentInPortfolioDD);
												
											do
											{
	 											int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
											
												for (int l = 1; l < numOfProjectPresentInGrid; l++) 
												{
													String projectName = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).getAttribute("title");
													
													for (int m = 0; m < projectDetailsDisplayedInDetailedView.length; m++) 
													{
														
														if (projectName.equals(projectDetailsDisplayedInDetailedView[m][2])) 
														{
														
															flagProjectPresent = flagProjectPresent+1;
															
															break;
														}
														
													}
													
												}
												
												
											} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
											
										}
										
									}
									
								}
								
							}
							
						}
						
					}
					
					
					
					if (compareIntegers(1, flagGroupPresent)) 
					{
						
						APP_LOGS.debug("Group drop down contains those Groups of project whose end date is equal to or greater than today's date and present in the current quarter.");
						
						comments += "\n- Group drop down contains those Groups of project whose end date is equal to or greater than today's date and present in the current quarter.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Group Drop Down contains unexpected Group Name.(Fail) ";
						
						APP_LOGS.debug("Group Drop Down contains unexpected Group Name. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Group Drop Down contains unexpected Group Name");
					}
					
					if (compareIntegers(1, flagPortfolioPresent)) 
					{
						
						APP_LOGS.debug("Portfolio drop down contains those Portfolio of project whose end date is equal to or greater than today's date and present in the current quarter.");
						
						comments += "\n- Portfolio drop down contains those Portfolio of project whose end date is equal to or greater than today's date and present in the current quarter.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Portfolio Drop Down contains unexpected Portfolio Name.(Fail) ";
						
						APP_LOGS.debug("Portfolio Drop Down contains unexpected Portfolio Name. Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Portfolio Drop Down contains unexpected Portfolio Name");
					}
					
					System.out.println("flagProjectPresent : "+flagProjectPresent);
					
					if (compareIntegers(projectDetailsDisplayedInDetailedView.length , flagProjectPresent)) 
					{
						
						APP_LOGS.debug("Projects whose end date is equal to or greater than today's date and present in the current quarter are displayed in Grid.");
						
						comments += "\n- Projects whose end date is equal to or greater than today's date and present in the current quarter are displayed in Grid.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- Project Grid contains unexpected Project(s).(Fail) ";
						
						APP_LOGS.debug("Project Grid contains unexpected Project(s). Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Grid contains unexpected Project(s).");
					}
				}*/
				
				
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
	
	//Functions
	
	private String[][] getProjectsForFYAndQuarter(int fiscalYear ,int quarter, String[][] projectArrayToSearchIn)
	{
		//variable to find out the indexes based on comparison
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		//variable to store rows for desired indexes of the actual array
		String[][] projectDetailsForReqFYAndQuarter = null;
		
		String projectStartDate;
		String projectEndDate;
		int projectsCountForReqFYandQuarter;
		
		//loop to get the index for matching value
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			projectStartDate = projectArrayToSearchIn[i][4];
			projectEndDate = projectArrayToSearchIn[i][5];

			if ((getFiscalYear(projectStartDate)==fiscalYear && getQuarter(projectStartDate) == quarter) ||
					(getFiscalYear(projectEndDate)==fiscalYear && getQuarter(projectEndDate) == quarter)) 
			{
				arrayToStoreIndexOfReqProjects.add(i);
			}
			
		}
			
		projectsCountForReqFYandQuarter = arrayToStoreIndexOfReqProjects.size();
		
		projectDetailsForReqFYAndQuarter = new String[projectsCountForReqFYandQuarter][6];
		
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
		}
		
		
		return projectDetailsForReqFYAndQuarter;
		
	}	
	
	
	private String[][] getProjectDisplayInDetailedView( String[][] projectArrayToSearchIn ) throws ParseException, IOException
	{
		
		Date projectStartDate;
		Date projectEndDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		/*Date startDateInDateFormat = dateFormat.parse(startDate);
		*/
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			projectStartDate = dateFormat.parse(projectArrayToSearchIn[i][4]);
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
		
		
			if (projectEndDate.compareTo(sysdateInDateFormat) >= 0  &&  !(projectStartDate.compareTo(sysdateInDateFormat) > 0)) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][6];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] = projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
		}
		
		
		return projectDetailsForReqDateRange;
		
		
	}
	
	private int currentlySelectedQuarter() throws IOException
	{
		if (getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected()) 
		{
			return 1;
		}
		else if (getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected()) 
		{
			return 2;
		}
		else if (getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected()) 
		{
			return 3;
		}
		else  

			return 4;
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
