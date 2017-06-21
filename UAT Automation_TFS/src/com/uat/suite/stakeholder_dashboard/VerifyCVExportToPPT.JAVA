/**
 * @author preeti.walde
 * Created: 28th Jan 2015
 * Last Modified: 10th Feb 2015
 * Description: Code to test 'Export To PPT' functionality for Select Date dropdown in Consolidated View tab.
 */

package com.uat.suite.stakeholder_dashboard;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class VerifyCVExportToPPT extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
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
	public void verifyCVExportToPPT(String Role, String StartDateMonth, String StartDateYear, String StartDate,
			String EndDateMonth, String EndDateYear, String EndDate) throws Exception
	{		
		String selectedDateFilterOptn;
		
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
				
				//verify if export to ppt button visible or not
				if(assertTrue(!getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").isDisplayed()))
				{
						fail=true;						
						comments += "\n Export To PPT button is not displayed..hence cannot perform Export TO PPT operation.(Fail) ";						
						APP_LOGS.debug("Export To PPT button is not displayed..hence cannot perform Export TO PPT operation. Test case failed");						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTButtonNotVisible");
						
						throw new SkipException("Export TO PPT button not available..hence skipping Export TO PPT operation.");
				}
				else
				{
						Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
						
						//select Today option from select date dropdown
						selectDateDD.selectByVisibleText("Today");	
						
						selectedDateFilterOptn = getElement("StakeholderDashboard_selectDateDropDown_Id").getAttribute("value");
						
						//perform export to ppt operation
						verifyExportToPPTForTodayAndToday_1(selectedDateFilterOptn);
										
												
						//select Today-1 option from select date dropdown				
						selectDateDD.selectByVisibleText("Today - 1");
						
						selectedDateFilterOptn = getElement("StakeholderDashboard_selectDateDropDown_Id").getAttribute("value");
						
						//perform export to ppt operation
						verifyExportToPPTForTodayAndToday_1(selectedDateFilterOptn);
									
					
						//verify projects for Last 7 Days option of select date dropdown
						selectDateDD.selectByVisibleText("Last 7 Days");
						
						selectedDateFilterOptn = getElement("StakeholderDashboard_selectDateDropDown_Id").getAttribute("value");							
						
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
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for 'Last 7 Days' filter");
										}
								}								
								
								//export data to PPT for Last 7 Days option
								getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
								Thread.sleep(500);
								
								if(assertTrue(waitForElementVisibility("itemNotSelectedAlertBox_Id", 2)!= null))
								{
										if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noProjectMsgForSelectedFilter"), getElement("itemNotSelectedAlertBox_Id").getText()))
										{
											comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected 'Last 7 Days' option.(Pass) ";						
											APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected 'Last 7 Days' option.");
											
											//click on Ok button
											getObject("itemNotSelectedAlertBoxOkButton").click();
											Thread.sleep(500);
										}
										else
										{
											fail=true;
											comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for 'Last 7 Days' option.(Fail) ";						
											APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for 'Last 7 Days' option.");
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Message for no project available is incorrect for 'Last 7 Days' filter");									
										}
								}
								else
								{
									fail=true;						
									comments += "\n No Project Available alert dialog box is not displayed for 'Last 7 Days' filter.(Fail) ";						
									APP_LOGS.debug("No Project Available alert dialog box is not displayed for 'Last 7 Days' filter. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoProjectAvailableAlertNotVisibleFor'Last 7 Days'Filter");
								}
						}
						else
						{				
								//for daily status
								vefifyExportToPPTForLast7Days(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForLast7DaysOption_Id",
										"StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForLast7Days", "StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForLast7Days", 
										"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForLast7Days","StakeholderDashboardCV_dailyStatusText");
								
								//for consolidated status
								vefifyExportToPPTForLast7Days(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForLast7DaysOption_Id",
										"StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForLast7Days", "StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForLast7Days", 
										"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForLast7Days", "StakeholderDashboardCV_consolidateStatusText");
						}
						
						
						//verify projects for Dates - From/To option of select date dropdown
						selectDateDD.selectByVisibleText("Dates - From/To");
						
						selectedDateFilterOptn = getElement("StakeholderDashboard_selectDateDropDown_Id").getAttribute("value");
						
						if(getElement("StakeholderDashboardConsolidatedView_fromDateTextBox_Id").isDisplayed()
								&& getElement("StakeholderDashboardConsolidatedView_toDateTextBox_Id").isDisplayed())
						{
									//select from date
									selectStartOrEndDate(getObject("StakeholderDashboardConsolidatedView_startDateImage"),StartDateMonth,StartDateYear,StartDate);
									
									//select to date
									selectStartOrEndDate(getObject("StakeholderDashboardConsolidatedView_endDateImage"),EndDateMonth, EndDateYear, EndDate);
									
									numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
									
									if(numOfProjectPresentInGrid==0)
									{
										noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
										
										if(noTestStepAvailableTagSize==1)
										{						
												if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
												{
													APP_LOGS.debug("No Projects Available for 'Dates-From/To' filter criteria..verified 'No Test Steps Available!' message");
													comments+="No Projects Available for 'Dates-From/To' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
												}
												else
												{
													fail=true;
													APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected 'Dates-From/To' filter criteria");
													comments+="'No Test Step Available' message not shown though no projects available for selected 'Dates-From/To' filter criteria.(Fail) ";
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for 'Dates-From/To' filter");
												}
										}
																				
										//export data to PPT for Today option
										getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
										Thread.sleep(500);
										
										if(assertTrue(waitForElementVisibility("itemNotSelectedAlertBox_Id", 2)!= null))
										{
												if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noProjectMsgForSelectedFilter"), getElement("itemNotSelectedAlertBox_Id").getText()))
												{
													comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected 'Dates-From/To' filter.(Pass) ";						
													APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected 'Dates-From/To' filter.");
													
													//click on Ok button
													getObject("itemNotSelectedAlertBoxOkButton").click();
													Thread.sleep(500);
												}
												else
												{
													fail=true;
													comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for 'Dates - From/To' filter.(Fail) ";						
													APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for 'Dates - From/To' filter.");
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Message for no project available is incorrect for 'Dates-From/To' filter");									
												}
										}
										else
										{
											fail=true;						
											comments += "\n No Project Available alert dialog box is not displayed for 'Dates - From/To' filter.(Fail) ";						
											APP_LOGS.debug("No Project Available alert dialog box is not displayed for 'Dates - From/To' filter. Test case failed");						
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoProjectAvailableAlertNotVisibleFor'Dates-From/To'Filter");
										}
									}
									else
									{						
											//for monthly status
											vefifyExportToPPTForFromTODates(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForFromToDateOption_Id",
													"StakeholderDashboardConsolidatedView_monthlyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_weeklyStatusCheckBoxForFromToDates", 
													"StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForFromToDates",
													"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForFromToDates", "StakeholderDashboardCV_monthlyStatusText");
										
											//for weekly status
											vefifyExportToPPTForFromTODates(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForFromToDateOption_Id",
												"StakeholderDashboardConsolidatedView_weeklyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_monthlyStatusCheckBoxForFromToDates", 
												"StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForFromToDates",
												"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForFromToDates", "StakeholderDashboardCV_weeklyStatusText");			
																		
											//for daily status
											vefifyExportToPPTForFromTODates(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForFromToDateOption_Id",
													"StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_weeklyStatusCheckBoxForFromToDates", 
													"StakeholderDashboardConsolidatedView_monthlyStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForFromToDates",
													"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForFromToDates", "StakeholderDashboardCV_dailyStatusText");
											
											//for consolidated status
											vefifyExportToPPTForFromTODates(selectedDateFilterOptn, "StakeholderDashboardConsolidatedView_statusPopupForFromToDateOption_Id",
													"StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForFromToDates", "StakeholderDashboardConsolidatedView_weeklyStatusCheckBoxForFromToDates", 
													"StakeholderDashboardConsolidatedView_monthlyStatusCheckBoxForFromToDates",	"StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForFromToDates", 
													"StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForFromToDates", "StakeholderDashboardCV_consolidateStatusText");
													
											
											//export data to PPT for all options option
											getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
											Thread.sleep(1000);
											
											//verify select pattern box is visible for all 4 status
											if(assertTrue(waitForElementVisibility("StakeholderDashboardConsolidatedView_statusPopupForFromToDateOption_Id", 300)!= null))
											{
														if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_monthlyStatusText"), getObject("StakeholderDashboardConsolidatedView_monthlyStatusCheckBoxForFromToDates").getAttribute("value"))
																&& compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_weeklyStatusText"), getObject("StakeholderDashboardConsolidatedView_weeklyStatusCheckBoxForFromToDates").getAttribute("value"))
																&& compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_dailyStatusText"), getObject("StakeholderDashboardConsolidatedView_dailyStatusCheckBoxForFromToDates").getAttribute("value"))
																&& compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_consolidateStatusText"), getObject("StakeholderDashboardConsolidatedView_consolidatedStatusCheckBoxForFromToDates").getAttribute("value")))
														{	
																	//select all check box
																	//getObject("StakeholderDashboardConsolidatedView_allCheckBoxesForFromToDates").click();
																	eventfiringdriver.executeScript("$('#statusPopUpForDates').find('input').click();");
																	Thread.sleep(100);
																	
																	//click on Ok button
																	getObject("StakeholderDashboardConsolidatedView_selectPatternPopupOkButtonForFromToDates").click();
																	Thread.sleep(1000);
																	
																	//verify alert
																	if(assertTrue(waitForElementVisibility("StakeholderDashboardConsolidatedView_exportToPPTAlertBox", 500)!= null))
																	{
																			if(getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBox").getText().contains("Requested PPT file is exported successfully"))
																			{
																				comments += "\n Export To PPT operation successful for 'All' checkboxes in 'Dates-From/To' filter.(Pass) ";						
																				APP_LOGS.debug("Export To PPT operation successful for 'All' checkboxes in 'Dates-From/To' filter.");
																				
																				//click on Ok button
																				getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBoxOkButton").click();
																				Thread.sleep(1000);
																			}
																			else
																			{
																				fail=true;
																				comments += "\n Export To PPT operation unsuccessful for 'All' checkboxes in 'Dates-From/To' filter.(Fail) ";						
																				APP_LOGS.debug("Export To PPT operation unsuccessful for 'All' checkboxes in 'Dates-From/To' filter.");
																				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Export to PPT text is not matched 'All' checkboxes in 'Dates-From/To' filter");
																				assertTrue(false);
																			}
																	}
																	else
																	{
																		fail=true;						
																		comments += "\n Export To PPT alert dialog box is not displayed for 'All' checkboxes in 'Dates-From/To' filter.(Fail) ";						
																		APP_LOGS.debug("Export To PPT alert dialog box is not displayed for 'All' checkboxes in 'Dates-From/To' filter. Test case failed");						
																		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTAlertNotVisibleFor'All'CheckboxesIn'Dates-From/To'Filter");
																	}
														}
														else
														{
															fail=true;						
															comments += "\n 'All/Any' checkboxes in 'Dates-From/To' filter not available.(Fail) ";						
															APP_LOGS.debug("'All/Any' checkboxes in 'Dates-From/To' filter not available. Test case failed");						
															TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'All/Any' checkboxes in 'Dates-From/To' filter not available");
														}							
											}
											else
											{
													fail=true;						
													comments += "\n Select Pattern alert dialog box is not displayed for 'All' checkboxes in 'Dates-From/To' filter.(Fail) ";						
													APP_LOGS.debug("Select Pattern alert dialog box is not displayed for 'All' checkboxes in 'Dates-From/To' filter. Test case failed");						
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SelectPatternAlertNotVisibleFor'All'CheckboxesIn'Dates-From/To'Filter");
											}
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
				}
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
		
	
	
	private void verifyExportToPPTForTodayAndToday_1(String selectedFilterOption) throws InterruptedException, IOException, AWTException
	{
		try
		{
			int numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
			
			if(numOfProjectPresentInGrid==0)
			{
				noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
				
				if(noTestStepAvailableTagSize==1)
				{						
					if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
					{
						APP_LOGS.debug("No Projects Available for '"+selectedFilterOption+"' filter criteria..verified 'No Test Steps Available!' message");
						comments+="No Projects Available for '"+selectedFilterOption+"' filter criteria..verified 'No Test Steps Available!' message.(Pass) ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("'No Test Step Available' message not shown though no projects available for selected '"+selectedFilterOption+"' filter criteria");
						comments+="'No Test Step Available' message not shown though no projects available for selected '"+selectedFilterOption+"' filter criteria.(Fail) ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for '"+selectedFilterOption+"' filter");
					}
				}
				
				//export data to PPT for selected option
				getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
				Thread.sleep(500);
				
				if(assertTrue(waitForElementVisibility("itemNotSelectedAlertBox_Id", 2)!= null))
				{
						if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noProjectMsgForSelectedFilter"), getElement("itemNotSelectedAlertBox_Id").getText()))
						{
							comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected '"+selectedFilterOption+"' option.(Pass) ";						
							APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message is verified for selected '"+selectedFilterOption+"' option.");
							
							//click on Ok button
							getObject("itemNotSelectedAlertBoxOkButton").click();
							Thread.sleep(500);
						}
						else
						{
							fail=true;
							comments += "\n 'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for '"+selectedFilterOption+"' option.(Fail) ";						
							APP_LOGS.debug("'No 'Project' is available in selected 'Date Filter' criteria.' message not visible for '"+selectedFilterOption+"' option.");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Message 'No project available' is incorrect for "+selectedFilterOption+" Filter");									
						}
				}
				else
				{
					fail=true;						
					comments += "\n No Project Available alert dialog box is not displayed for '"+selectedFilterOption+"' filter.(Fail) ";						
					APP_LOGS.debug("No Project Available alert dialog box is not displayed for '"+selectedFilterOption+"' filter. Test case failed");						
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoProjectAvailableAlertNotVisibleFor"+selectedFilterOption+"Filter");
				}
				
			}
			else
			{
					//export data to PPT for Today option
					getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
					Thread.sleep(1000);
					
					//verify auto alert 
					handleAlert();
					
					//verify PPT exported popup
					if(assertTrue(waitForElementVisibility("StakeholderDashboardConsolidatedView_exportToPPTAlertBox", 500)!= null))
					{
							if(getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBox").getText().contains("Requested PPT file is exported successfully"))
							{
								comments += "\n Export To PPT operation successful for selected '"+selectedFilterOption+"' option.(Pass) ";						
								APP_LOGS.debug("Export To PPT operation successful for selected option '"+selectedFilterOption+"'.");
								
								//click on Ok button
								getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBoxOkButton").click();
								Thread.sleep(1000);
							}
							else
							{
								fail=true;
								comments += "\n Export To PPT operation unsuccessful for selected '"+selectedFilterOption+"'.(Fail) ";						
								APP_LOGS.debug("Export To PPT operation unsuccessful for selected option '"+selectedFilterOption+"'.");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Export to PPT text is not matched for '"+selectedFilterOption+"' filter");
								assertTrue(false);
							}
					}
					else
					{
						fail=true;						
						comments += "\n Export To PPT alert dialog box is not displayed for '"+selectedFilterOption+"'.(Fail) ";						
						APP_LOGS.debug("Export To PPT alert dialog box is not displayed for '"+selectedFilterOption+"'. Test case failed");						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTAlertNotVisibleFor"+selectedFilterOption+".");
					}
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			fail=true;
			APP_LOGS.debug("Exception occured in 'verifyExportToPPTForTodayAndToday_1'");
			assertTrue(false);
		}
	}
	
	
	private void vefifyExportToPPTForLast7Days(String selectedFilterOption, String patternPopup, String checkBoxToBeSelect,
			String checkBoxToBeUnSelect, String patternPopupOkBtn, String checkBoxNameToBeSelect)
	{
		try
		{		
				//export data to PPT for Last 7 Days option
				getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
				Thread.sleep(1000);
				
				//verify select pattern box is visible for daily status
				if(assertTrue(waitForElementVisibility(patternPopup, 300)!= null))
				{
						//if checkbox is checked, uncheck it
						if(getObject(checkBoxToBeUnSelect).isSelected())
						{
							getObject(checkBoxToBeUnSelect).click();
						}
						
						if(compareStrings(resourceFileConversion.getProperty(checkBoxNameToBeSelect), getObject(checkBoxToBeSelect).getAttribute("value")))
						{	
								//select check box daily status
								getObject(checkBoxToBeSelect).click();
								
								//click on Ok button
								getObject(patternPopupOkBtn).click();
								Thread.sleep(1000);
								
								//verify auto popup for access is visible or not
								handleAlert();
								
								//verify ppt export successfully popup
								if(assertTrue(waitForElementVisibility("StakeholderDashboardConsolidatedView_exportToPPTAlertBox", 500)!= null))
								{
										if(getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBox").getText().contains("Requested PPT file is exported successfully"))
										{
											comments += "\n Export To PPT operation successful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.(Pass) ";						
											APP_LOGS.debug("Export To PPT operation successful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.");
											
											//click on Ok button
											getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBoxOkButton").click();
											Thread.sleep(500);
										}
										else
										{
											fail=true;
											comments += "\n Export To PPT operation unsuccessful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.(Fail) ";						
											APP_LOGS.debug("Export To PPT operation unsuccessful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.");
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Export to PPT text is not matched for '"+getObject(checkBoxToBeSelect).getAttribute("value")+" Status' in '"+selectedFilterOption+"' filter");
											assertTrue(false);
										}
								}
								else
								{
									fail=true;						
									comments += "\n Export To PPT alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.(Fail) ";						
									APP_LOGS.debug("Export To PPT alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTAlertNotVisibleFor'"+getObject(checkBoxToBeSelect).getAttribute("value")+"'OptionIn'"+selectedFilterOption+"'Filter");
								}
						}
						else
						{
							fail=true;						
							comments += "\n '"+getObject(checkBoxToBeSelect).getAttribute("value")+" Status' option not available for '"+selectedFilterOption+"' Filter.(Fail) ";						
							APP_LOGS.debug("'"+getObject(checkBoxToBeSelect).getAttribute("value")+" Status' option not available for '"+selectedFilterOption+"' Filter. Test case failed");						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), ""+getObject(checkBoxToBeSelect).getAttribute("value")+"StatusOptionNotVisibleFor'"+selectedFilterOption+"'Filter");
						}							
				}
				else
				{
						fail=true;						
						comments += "\n Select Pattern alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter.(Fail) ";						
						APP_LOGS.debug("Select Pattern alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' Filter. Test case failed");						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SelectPatternAlertNotVisibleFor'"+getObject(checkBoxToBeSelect).getAttribute("value")+"'OptionIn'"+selectedFilterOption+"'Filter");
				}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;
			APP_LOGS.debug("Exception occured in 'vefifyExportToPPTForLast7DaysAndFromToDate'");
			assertTrue(false);
		}
		
		
	}
	
	
	private void vefifyExportToPPTForFromTODates(String selectedFilterOption, String patternPopup, String checkBoxToBeSelect,
			String checkBoxToBeUnSelect1, String checkBoxToBeUnSelect2, String checkBoxToBeUnSelect3, String patternPopupOkBtn, 
			String checkBoxNameToBeSelect)
	{
		try
		{
				//export data to PPT for monthly staus in From/To date filter
				getObject("StakeholderDashboardConsolidatedView_exportToPPTBtn").click();
				Thread.sleep(1000);
				
				//verify select pattern box is visible for selected status
				if(assertTrue(waitForElementVisibility(patternPopup, 300)!= null))
				{		
						//if checkbox is checked, uncheck it
						if(getObject(checkBoxToBeUnSelect1).isSelected())
						{
							getObject(checkBoxToBeUnSelect1).click();
						}
						
						if(getObject(checkBoxToBeUnSelect2).isSelected())
						{
							getObject(checkBoxToBeUnSelect2).click();
						}
						
						if(getObject(checkBoxToBeUnSelect3).isSelected())
						{
							getObject(checkBoxToBeUnSelect3).click();
						}
					
						if(compareStrings(resourceFileConversion.getProperty(checkBoxNameToBeSelect), getObject(checkBoxToBeSelect).getAttribute("value")))
						{	
								//select check box
								getObject(checkBoxToBeSelect).click();
								
								//click on Ok button
								getObject(patternPopupOkBtn).click();
								Thread.sleep(1500);
								
								//verify auto popup for access is visible or not
								handleAlert();
																					
								//verify alert
								if(assertTrue(waitForElementVisibility("StakeholderDashboardConsolidatedView_exportToPPTAlertBox", 500)!= null))													
								{															
										if(getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBox").getText().contains("Requested PPT file is exported successfully"))
										{
											comments += "\n Export To PPT operation successful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.(Pass) ";						
											APP_LOGS.debug("Export To PPT operation successful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.");
											
											//click on Ok button
											getObject("StakeholderDashboardConsolidatedView_exportToPPTAlertBoxOkButton").click();
											Thread.sleep(500);
										}
										else
										{
											fail=true;
											comments += "\n Export To PPT operation unsuccessful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.(Fail) ";						
											APP_LOGS.debug("Export To PPT operation unsuccessful for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.");
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Export to PPT text is not matched for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter");
											assertTrue(false);
										}
								}
								else
								{
									fail=true;						
									comments += "\n Export To PPT alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.(Fail) ";						
									APP_LOGS.debug("Export To PPT alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter. Test case failed");						
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportToPPTAlertNotVisibleFor'"+getObject(checkBoxToBeSelect).getAttribute("value")+"'OptionIn'"+selectedFilterOption+"'Filter");
								}
						}
						else
						{
							fail=true;						
							comments += "\n '"+getObject(checkBoxToBeSelect).getAttribute("value")+" Status' option not available for '"+selectedFilterOption+"' filter.(Fail) ";						
							APP_LOGS.debug("'"+getObject(checkBoxToBeSelect).getAttribute("value")+" Status' option not available for '"+selectedFilterOption+"' filter. Test case failed");						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), ""+getObject(checkBoxToBeSelect).getAttribute("value")+"StatusOptionNotVisibleFor'"+selectedFilterOption+"'Filter");
						}							
				}
				else
				{
						fail=true;						
						comments += "\n Select Pattern alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter.(Fail) ";						
						APP_LOGS.debug("Select Pattern alert dialog box is not displayed for '"+getObject(checkBoxToBeSelect).getAttribute("value")+"' option in '"+selectedFilterOption+"' filter. Test case failed");						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SelectPatternAlertNotVisibleFor'"+getObject(checkBoxToBeSelect).getAttribute("value")+"'OptionIn'"+selectedFilterOption+"'Filter");
				}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;
			APP_LOGS.debug("Exception occured in 'vefifyExportToPPTForFromTODates'");
			assertTrue(false);
		}
	}
	
	
	//function to handle auto alert
	private void handleAlert() throws AWTException, InterruptedException
	{
			try
			{
				WebDriverWait w = new WebDriverWait(eventfiringdriver, 10);	
				
				w.until(ExpectedConditions.alertIsPresent());				
				eventfiringdriver.switchTo().alert();
				
		        Robot r = new Robot();
		        
				r.keyPress(KeyEvent.VK_TAB);
				r.keyPress(KeyEvent.VK_ENTER);	
		    }
			
		    catch(Throwable t)
		    {    	
		    	
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