/**
 * @author preeti.walde
 * Created: 4th Feb 2015
 * Last Modified: 5th Feb 2015
 * Description: Code to verify various filter on test step grid in Consolidated View tab.
 */

package com.uat.suite.stakeholder_dashboard;

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
public class VerifyCVTestStepFilter extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	int initialTestStepTRCount=0;
	int testStepTRCountWhen1ChkBoxChecked;
	int testStepTRCountAfterUncheckingCheckBox;
	int testStepTRCountAfterCheckingAllCheckBox;
	String filterAppliedAttr;
	int testStepGrid;
	
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
	public void verifyCVTestStepFilter(String Role) throws Exception
	{
		int numOfProjectPresentInGrid;
		int noTestStepMsgSize;		
		int passCount;		
		String testStepPassedText;
		String selectedProject = null;
		String selectedTP = null;
		String selectedTC = null;
		String selectedSecondTester = null;
		int noTestStepAvailableTagSize;
		int projectSelectedCounter=0;
		
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
						
						//select All option from project status dropdown
						Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
						projectStatusDD.selectByValue("All");	
						Thread.sleep(500);
						
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
							
							throw new SkipException("No Projects Available in Project Section..hence cannot verify Test Step Filter operation.");										
							
						}					
						else
						{
								//code for verifying pie chart is displayed if no project is selected								
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
						 }
						
						//verify if pie chart visible or not for selected projects 
						if(projectSelectedCounter > 0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
										skip=true;
										APP_LOGS.debug("'No Test Steps Available!' for selected projects..hence cannot verify Test Step Filter operation.");
										comments+="'No Test Steps Available!' for selected projects..hence cannot verify Test Step Filter operation.(Skip) ";
								
										throw new SkipException("No Test Step Available for selected projects..hence skipping test step filter operation.");										
								}	
								else
								{
										APP_LOGS.debug("Pie chart available for selected projects..proceeding with test step filter operation.");
									
										//click on Passed link to verify paas test steps							
										getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").click();
										Thread.sleep(500);
										
										testStepPassedText = getElement("StakeholderDashboardConsolidatedView_testStepPassCount_Id").getText();
										
										noTestStepMsgSize = getElement("StakeholderDashboardConsolidatedView_noTestStepLabelDiv_Id").findElements(By.tagName("a")).size();
										
										if(noTestStepMsgSize==1)
										{
											if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepLabel"), getObject("StakeholderDashboardConsolidatedView_noTestStepLabel").getText()))
											{
												APP_LOGS.debug("No Test Steps available! for '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link.");
												comments+="No Test Steps available! for '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link.(Pass) ";
											}
											else
											{
												fail=true;
												APP_LOGS.debug("'No Test Steps available!' message not shown for '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link.");
												comments+="'No Test Steps available!' message not shown for '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link.(Fail) ";
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link");
											}
											
											throw new SkipException("No Test Step Available for selected '"+getElement("StakeholderDashboardConsolidatedView_statusLinkPassed_Id").getText()+"' link..hence cannot verify Test Step Filter operation.");
										}
										else
										{
												passCount=0;
												
												do
												{		
														testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
												
														for (int j = 1; j <= testStepGrid; j++) 
														{
																if(getObject("StakeholderDashboardConsolidatedView_testStepStatusColXpath1", "StakeholderDashboardConsolidatedView_testStepStatusColXpath2", j).getText().equals("Pass"))
																{
																	passCount++;
																}
														}
														
														initialTestStepTRCount+= testStepGrid;
													
												}while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink"));	
												
												//compare pass count and verify
												if(compareIntegers(Integer.parseInt(testStepPassedText), passCount))
												{
													comments += "\n Count for 'Passed' link test steps "+passCount+" is correct and matched with "+Integer.parseInt(testStepPassedText)+".(Pass) ";						
													APP_LOGS.debug("Count for 'Passed' link test steps "+passCount+" is correct and matched with "+Integer.parseInt(testStepPassedText)+".");
												}
												else
												{
													fail=true;
													comments += "\n Count for 'Passed' link test steps "+passCount+" is incorrect and doesnot matched with "+Integer.parseInt(testStepPassedText)+".(Fail) ";						
													APP_LOGS.debug("Count for 'Passed' link test steps "+passCount+" is incorrect and doesnot matched with "+Integer.parseInt(testStepPassedText)+".");
												}
										 }
										
										 //verify project filter
										 if(verifyFilterForRespectiveColumn("StakeholderDashboardConsolidatedView_projectFilter","projectItemsInProjectFilter",
												 "noneLinkOfProjectFilter_Id","OkButtonOfProjectFilter","projectNameOfProjectFilter", "firstProjectChkBox", 
												 "StakeholderDashboardConsolidatedView_testStepProjectColXpath1", 
												 "StakeholderDashboardConsolidatedView_testStepProjectColXpath2", "allLinkOfProjectFilter","clearFilterLinkOfProject_Id",
												 "cancelButtonOfProjectFilter"))
										 {
											 	comments += "\n Project Filter functionality is successfully verified.(Pass) ";						
												APP_LOGS.debug("Project Filter functionality is successfully verified");
										 }
										 else
										 {
											 	fail=true;
											 	comments += "\n Project Filter functionality is unsuccessful.(Fail) ";						
												APP_LOGS.debug("Project Filter functionality is unsuccessful");
												assertTrue(false);
										 }
			
										 //verify test pass filter
										 if(verifyFilterForRespectiveColumn("StakeholderDashboardConsolidatedView_testPassFilter","testPassItemsInTPFilter",
												 "noneLinkOfTPFilter_Id","OkButtonOfTPFilter","testPassNameOfTPFilter",
												 "firstTPChkBox", "StakeholderDashboardConsolidatedView_testStepTestPassColXpath1",
												 "StakeholderDashboardConsolidatedView_testStepTestPassColXpath2", "allLinkOfTPFilter","clearFilterLinkOfTP_Id",
												 "cancelButtonOfTPFilter"))
										 {
											 	comments += "\n Test Pass Filter functionality is successfully verified.(Pass) ";						
												APP_LOGS.debug("Test Pass Filter functionality is successfully verified");
										 }
										 else
										 {
											 	fail=true;
											 	comments += "\n Test Pass Filter functionality is unsuccessful.(Fail) ";						
												APP_LOGS.debug("Test Pass Filter functionality is unsuccessful");
												assertTrue(false);
										 }
										 
										 //verify test case filter
										 if(verifyFilterForRespectiveColumn("StakeholderDashboardConsolidatedView_testCaseFilter","testCaseItemsInTCFilter",
												 "noneLinkOfTCFilter_Id","OkButtonOfTCFilter","testCaseNameOfTCFilter",
												 "firstTCChkBox", "StakeholderDashboardConsolidatedView_testStepTestCaseColXpath1",
												 "StakeholderDashboardConsolidatedView_testStepTestCaseColXpath2", "allLinkOfTCFilter","clearFilterLinkOfTC_Id",
												 "cancelButtonOfTCFilter"))
										 {
											 	comments += "\n Test Case Filter functionality is successfully verified.(Pass) ";						
												APP_LOGS.debug("Test Case Filter functionality is successfully verified");
										 }
										 else
										 {
											 	fail=true;
											 	comments += "\n Test Case Filter functionality is unsuccessful.(Fail) ";						
												APP_LOGS.debug("Test Case Filter functionality is unsuccessful");
												assertTrue(false);
										 }
										 
										 //verify tester filter
										 if(verifyFilterForRespectiveColumn("StakeholderDashboardConsolidatedView_testerFilter","testerItemsInTesterFilter",
												 "noneLinkOfTesterFilter_Id","OkButtonOfTesterFilter","testerNameOfTesterFilter",
												 "firstTesterChkBox", "StakeholderDashboardConsolidatedView_testStepTesterColXpath1",
												 "StakeholderDashboardConsolidatedView_testStepTesterColXpath2", "allLinkOfTesterFilter","clearFilterLinkOfTester_Id",
												 "cancelButtonOfTesterFilter"))
										 {
											 	comments += "\n Tester Filter functionality is successfully verified.(Pass) ";						
												APP_LOGS.debug("Tester Filter functionality is successfully verified");
										 }
										 else
										 {
											 	fail=true;
											 	comments += "\n Tester Filter functionality is unsuccessful.(Fail) ";						
												APP_LOGS.debug("Tester Filter functionality is unsuccessful");
												assertTrue(false);
										 }
										 
										 //verify role filter
										 if(verifyFilterForRespectiveColumn("StakeholderDashboardConsolidatedView_roleFilter","roleItemsInRoleFilter",
												 "noneLinkOfRoleFilter_Id","OkButtonOfRoleFilter","roleNameOfRoleFilter",
												 "firstRoleChkBox", "StakeholderDashboardConsolidatedView_testStepRoleColXpath1",
												 "StakeholderDashboardConsolidatedView_testStepRoleColXpath2", "allLinkOfRoleFilter","clearFilterLinkofRole_Id",
												 "cancelButtonOfRoleFilter"))
										 {
											 	comments += "\n Role Filter functionality is successfully verified.(Pass) ";						
												APP_LOGS.debug("Role Filter functionality is successfully verified");
										 }
										 else
										 {
											 	fail=true;
											 	comments += "\n Role Filter functionality is unsuccessful.(Fail) ";						
												APP_LOGS.debug("Role Filter functionality is unsuccessful");
												assertTrue(false);
										 }
										 
										Thread.sleep(500);
										
										 //verify multiple filter  functionality
										 getObject("StakeholderDashboardConsolidatedView_projectFilter").click();
										 Thread.sleep(500);
										 
										 //no of checkboxes
									     int numberOfPrjCheckBoxPresent = getObject("projectItemsInProjectFilter").findElements(By.tagName("li")).size();
									    						     
									     if (numberOfPrjCheckBoxPresent > 1) 
									     {			
									    	 	 //click on none option
									    	 	 getElement("noneLinkOfProjectFilter_Id").click();
									    	 	 
										    	 //get first project name
										    	 selectedProject = getObject("projectNameOfProjectFilter").getAttribute("title");
										    	 
										    	 //select first project checkbox
											     getObject("firstProjectChkBox").click();
											     
											     getObject("OkButtonOfProjectFilter").click();
											     
											     Thread.sleep(500);								     
											     
											     //verify data for one item is checked
											     testStepTRCountWhen1ChkBoxChecked=0;
											     
											     do 
											     {
											    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
														
														for (int j = 1; j <= testStepGrid; j++) 
														{
																if(getObject("StakeholderDashboardConsolidatedView_testStepProjectColXpath1", "StakeholderDashboardConsolidatedView_testStepProjectColXpath2", j).getText().equals(selectedProject))
																{
																	APP_LOGS.debug("Filtered project "+selectedProject+" is showing in test step grid");
																}
														}
														testStepTRCountWhen1ChkBoxChecked+= testStepGrid;
											      
											     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink")); 
										     
											     Thread.sleep(500);
									     }
									     else 
									     {
									    	 //click on cancel button as only one entry is available in filter box
									    	 getObject("cancelButtonOfProjectFilter").click();
									     }
									     
									     //click on test pass filter icon
										 getObject("StakeholderDashboardConsolidatedView_testPassFilter").click();
										 Thread.sleep(500);
										 
										//no of checkboxes
									     int numberOfTPCheckBoxPresent = getObject("testPassItemsInTPFilter").findElements(By.tagName("li")).size();
									    						     
									     if (numberOfTPCheckBoxPresent > 1) 
									     {			
									    	 	 //click on none link
										    	 getElement("noneLinkOfTPFilter_Id").click();
										    	 
										    	 //select first test pass checkbox
											     getObject("firstTPChkBox").click();
											     
											     //get first test pass name
										    	 selectedTP = getObject("testPassNameOfTPFilter").getText();
										    	 
											     getObject("OkButtonOfTPFilter").click();
											     
											     Thread.sleep(500);								     
											     
											     //verify data for one item is checked
											     testStepTRCountWhen1ChkBoxChecked=0;
											     
											     do 
											     {
											    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
														
														for (int j = 1; j <= testStepGrid; j++) 
														{
																if(getObject("StakeholderDashboardConsolidatedView_testStepProjectColXpath1", "StakeholderDashboardConsolidatedView_testStepProjectColXpath2", j).getText().equals(selectedProject)
																		&& getObject("StakeholderDashboardConsolidatedView_testStepTestPassColXpath1", "StakeholderDashboardConsolidatedView_testStepTestPassColXpath2", j).getText().equals(selectedTP))
																{
																	APP_LOGS.debug("Filtered project "+selectedProject+" and filtered test pass "+selectedTP+" is showing in test step grid");
																}
														}
														testStepTRCountWhen1ChkBoxChecked+= testStepGrid;
											      
											     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink")); 
										     
											     Thread.sleep(500);
										     }
										     else 
										     {
										    	 //click on cancel button as only one entry is available in filter box
										    	 getObject("cancelButtonOfTPFilter").click();
										     }
											     
										     //click on test case filter icon
											 getObject("StakeholderDashboardConsolidatedView_testCaseFilter").click();
											 Thread.sleep(500);
											 
											 //no of checkboxes
										     int numberOfTCCheckBoxPresent = getObject("testCaseItemsInTCFilter").findElements(By.tagName("li")).size();
										    						     
										     if (numberOfTCCheckBoxPresent > 1) 
										     {			
										    	 	 //click on none link
											    	 getElement("noneLinkOfTCFilter_Id").click();
											    	 
											    	 //select first test case checkbox
												     getObject("firstTCChkBox").click();
												     
												     //get first test case name
											    	 selectedTC = getObject("testCaseNameOfTCFilter").getText();
											    	 
												     getObject("OkButtonOfTCFilter").click();
												     
												     Thread.sleep(500);								     
												     
												     //verify data for one item is checked
												     testStepTRCountWhen1ChkBoxChecked=0;
												     
												     do 
												     {
												    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
															
															for (int j = 1; j <= testStepGrid; j++) 
															{
																	if(getObject("StakeholderDashboardConsolidatedView_testStepProjectColXpath1", "StakeholderDashboardConsolidatedView_testStepProjectColXpath2", j).getText().equals(selectedProject)
																			&& getObject("StakeholderDashboardConsolidatedView_testStepTestPassColXpath1", "StakeholderDashboardConsolidatedView_testStepTestPassColXpath2", j).getText().equals(selectedTP)
																			&& getObject("StakeholderDashboardConsolidatedView_testStepTestCaseColXpath1", "StakeholderDashboardConsolidatedView_testStepTestCaseColXpath2", j).getText().equals(selectedTC))
																	{
																		APP_LOGS.debug("Filtered project "+selectedProject+", filtered test pass "+selectedTP+" and filtered test case "+selectedTC+" is showing in test step grid");
																	}
															}
															testStepTRCountWhen1ChkBoxChecked+= testStepGrid;
												      
												     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink"));
												     
												     Thread.sleep(500);
										     }
										     else 
										     {
										    	 //click on cancel button as only one entry is available in filter box
										    	 getObject("cancelButtonOfTCFilter").click();
										     }	
											     
										     //click on tester filter icon
											 getObject("StakeholderDashboardConsolidatedView_testerFilter").click();
											 Thread.sleep(500);
											 
											//no of checkboxes
										     int numberOfTesterCheckBoxPresent = getObject("testerItemsInTesterFilter").findElements(By.tagName("li")).size();
										    						     
										     if (numberOfTesterCheckBoxPresent > 1) 
										     {	
										    	 	//if first check box is selected, uncheck it
											    	 if(getObject("firstTesterChkBox").isSelected())
											    	 {
											    		 getObject("firstTesterChkBox").click();
											    	 }
											    	 
											    	 //select second checkbox
											    	 getObject("secondTesterChkBox").click();
											    	 
											    	 //get second tester name
											    	 selectedSecondTester = getObject("tester2OfTesterFilter").getAttribute("title");
											    	 
											    	 //click ok button
											    	 getObject("OkButtonOfTesterFilter").click();
											    	 
											    	 if(getElement("itemNotSelectedAlertBox_Id").isDisplayed())
											    	 {
											    		 	if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepMsgForSelectedFilter"), getElement("itemNotSelectedAlertBox_Id").getText()))
											    		 	{
											    		 		comments += "\n 'No Test Step available with selected filter!' message verified.(Pass) ";						
																APP_LOGS.debug("'No Test Step available with selected filter!' message verified");
																
																//click on ok button
																getObject("itemNotSelectedAlertBoxOkButton").click();
																Thread.sleep(500);
											    		 	}
											    		 	else
											    		 	{
											    		 		fail=true;
																comments += "\n 'No Test Step available with selected filter!' message is incorrect.(Fail) ";						
																APP_LOGS.debug("'No Test Step available with selected filter!' message is incorrect");
											    		 	}
											    	 }
											    	 else
											    	 {
											    		//verify data for one item is checked
													     testStepTRCountWhen1ChkBoxChecked=0;
													     
													     do 
													     {
													    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
																
																for (int j = 1; j <= testStepGrid; j++) 
																{
																		if(getObject("StakeholderDashboardConsolidatedView_testStepProjectColXpath1", "StakeholderDashboardConsolidatedView_testStepProjectColXpath2", j).getText().equals(selectedProject)
																				&& getObject("StakeholderDashboardConsolidatedView_testStepTestPassColXpath1", "StakeholderDashboardConsolidatedView_testStepTestPassColXpath2", j).getText().equals(selectedTP)
																				&& getObject("StakeholderDashboardConsolidatedView_testStepTestCaseColXpath1", "StakeholderDashboardConsolidatedView_testStepTestCaseColXpath2", j).getText().equals(selectedTC)
																				&& getObject("StakeholderDashboardConsolidatedView_testStepTesterColXpath1", "StakeholderDashboardConsolidatedView_testStepTesterColXpath2", j).getText().equals(selectedSecondTester))
																		{
																			APP_LOGS.debug("Filtered project "+selectedProject+", filtered test pass "+selectedTP+", filtered test case "+selectedTC+" and filtered tester "+selectedSecondTester+" is showing in test step grid");
																		}
																}
																testStepTRCountWhen1ChkBoxChecked+= testStepGrid;
													      
													     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink"));
											    	 }
										     }
										     else 
										     {
										    	 //click on cancel button as only one entry is available in filter box
										    	 getObject("cancelButtonOfTesterFilter").click();
										     }
										     
										     //clear all filters one by one
									    	 String testerWithFilter = getObject("StakeholderDashboardConsolidatedView_testerFilter").getAttribute("src");
									    	 String testCaseWithFilter = getObject("StakeholderDashboardConsolidatedView_testCaseFilter").getAttribute("src");
									    	 String testPassWithFilter = getObject("StakeholderDashboardConsolidatedView_testPassFilter").getAttribute("src");
									    	 String projectWithFilter = getObject("StakeholderDashboardConsolidatedView_projectFilter").getAttribute("src");
									    	 
									    	 if(testerWithFilter.contains("with-filter.png"))
									    	 {
									    		  getObject("StakeholderDashboardConsolidatedView_testerFilter").click();
												  Thread.sleep(500);
												 
									    		  //Click on Clear Filter	
											      getElement("clearFilterLinkOfTester_Id").click();
											      APP_LOGS.debug("Filtered result for tester is cleared.");
									    	 }
									    	 
									    	 if(testCaseWithFilter.contains("with-filter.png"))
									    	 {
									    		  getObject("StakeholderDashboardConsolidatedView_testCaseFilter").click();
												  Thread.sleep(500);
												 
									    		  //Click on Clear Filter	
											      getElement("clearFilterLinkOfTC_Id").click();
											      APP_LOGS.debug("Filtered result for test case is cleared.");
									    	 }
									    	 
									    	 if(testPassWithFilter.contains("with-filter.png"))
									    	 {
									    		  getObject("StakeholderDashboardConsolidatedView_testPassFilter").click();
												  Thread.sleep(500);
												  
									    		  //Click on Clear Filter	
											      getElement("clearFilterLinkOfTP_Id").click();
											      APP_LOGS.debug("Filtered result for test pass is cleared.");
									    	 }
									    	 
									    	 if(projectWithFilter.contains("with-filter.png"))
									    	 {
									    		  getObject("StakeholderDashboardConsolidatedView_projectFilter").click();
												  Thread.sleep(500);
												  
									    		  //Click on Clear Filter	
											      getElement("clearFilterLinkOfProject_Id").click();
											      APP_LOGS.debug("Filtered result for project is cleared.");
									    	 }
									    	 
									    	 APP_LOGS.debug("Applying multiple filter functionality is successfully verified.");
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
				catch(Throwable t)
				{
					t.printStackTrace();
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
		

	
	private boolean verifyFilterForRespectiveColumn(String filterImage, String listItemsInFilterBox, String noneLink,
			String okButton, String firstListItem, String firstListItemCheckBox, String filteredItemXpath1, 
			String filteredItemXpath2, String allLink, String clearFilterLink, String cancelButton)
	{
		String selectedFilterItem;
		
		try
		{
		     setImplicitWait(1);
		    
		     //click on project filter icon
		     getObject(filterImage).click();
			 Thread.sleep(2000);
			 
			 //no of checkboxes
		     int numberOfCheckBoxPresent = getObject(listItemsInFilterBox).findElements(By.tagName("li")).size();
		    						     
		     if (numberOfCheckBoxPresent > 1) 
		     {							    	 	
			    	 //click on none option
	    	 		 getElement(noneLink).click();
	    	 		 
	    	 		 getObject(okButton).click();
			    	 
			    	 //verify alert
			    	 if(getElement("itemNotSelectedAlertBox_Id").isDisplayed())
			    	 {
			    		 	if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noFilterItemSelectedMsg"), getElement("itemNotSelectedAlertBox_Id").getText()))
			    		 	{
			    		 		comments += "\n 'Atleast one filter item should be selected!' message verified.(Pass) ";						
								APP_LOGS.debug("'Atleast one filter item should be selected!' message verified");
								
								//click on ok button
								getObject("itemNotSelectedAlertBoxOkButton").click();
								Thread.sleep(500);
			    		 	}
			    		 	else
			    		 	{
			    		 		fail=true;
								comments += "\n 'Atleast one filter item should be selected!' message is incorrect.(Fail) ";						
								APP_LOGS.debug("'Atleast one filter item should be selected!' message is incorrect");
			    		 	}
			    	 }
			    	 else
			    	 {
			    		 	fail=true;
							comments += "\n Filter item not selected alert box not visible.(Fail) ";						
							APP_LOGS.debug("Filter item not selected alert box not visible");
			    	 }
		    	 
			    	 //verify if li is having title attribute or not
			    	 boolean isLIHavingTitleAttr = getObject(firstListItem).getAttribute("title").isEmpty();
			    	 
			    	 //get first project/test pass/test case/tester/role name
			    	 if(isLIHavingTitleAttr==true)
			    	 {			    		 
			    		 selectedFilterItem = (String) eventfiringdriver.executeScript("return $('#ulItemsdvTP').find('div li:eq(0) span').text()");
			    	 }
			    	 else
			    	 {
			    		 selectedFilterItem = getObject(firstListItem).getAttribute("title");
			    	 }
			    	 
			    	 //select first project checkbox
			    	 getObject(firstListItemCheckBox).click();
				     
			    	 getObject(okButton).click();
				     
			    	 Thread.sleep(1000);
				     
				     //verify filter is applied
				     filterAppliedAttr = getObject(filterImage).getAttribute("src");
				     if(assertTrue(filterAppliedAttr.contains("with-filter.png")))
				     {
				    	 	APP_LOGS.debug("Filter image is changed to filter applied image.");
				    	 	comments += "\n- Filter image is changed to filter applied image.(Pass) ";
				     }
				     else
				     {
				    	 	fail=true;
				    	 	APP_LOGS.debug("Filter image is not changed to filter applied image.");
				    	 	comments += "\n- Filter image is not changed to filter applied image.(Fail) ";
				    	 	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Filter image is not changed to filter applied image.");
				     }
				     
				     //verify data for one item is checked
				     testStepTRCountWhen1ChkBoxChecked=0;
				     
				     do 
				     {
				    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
							
							for (int j = 1; j <= testStepGrid; j++) 
							{
									if(getObject(filteredItemXpath1, filteredItemXpath2, j).getText().equals(selectedFilterItem))
									{
										APP_LOGS.debug(selectedFilterItem+" is showing in test step grid");
									}
							}
							testStepTRCountWhen1ChkBoxChecked+= testStepGrid;
				      
				     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink")); 
			     
				     Thread.sleep(1000);
				     
				     //click on filter image				   
				     getObject(filterImage).click();
				     
				     Thread.sleep(2000);
				     
				     //Click on ALL, so that all checkBoxes Get checked 						     
				     getObject(allLink).click();
				     
				     getObject(okButton).click();
				     								    
				     //verify grid for all option	
				     testStepTRCountAfterCheckingAllCheckBox=0;
				     do 
				     {
				    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
														
							testStepTRCountAfterCheckingAllCheckBox+= testStepGrid;
				      
				     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink")); 
				     
				     //verify tr counts when first option is unchecked and remaining option is checked
				     if(compareIntegers(initialTestStepTRCount, testStepTRCountAfterCheckingAllCheckBox))
				     {
				    	 	APP_LOGS.debug("Total Entries displayed in table after checking all checkbox is correct.");
				    	 	comments += "\n- Total Entries displayed in table after checking all checkbox is correct.(Pass) ";
				     }
				     else
				     {
				    	 	fail=true;
				    	 	APP_LOGS.debug("Total Entries displayed in table after checking all checkbox is incorrect.");
				    	 	comments += "\n- Total Entries displayed in table after checking all checkbox is incorrect.(Fail) ";
				    	 	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total Entries displayed in table after checking all checkbox is NOT correct.");
				     }
				     
				     Thread.sleep(1000);
				     
				     //click on filter image
				     getObject(filterImage).click();	
				     
				     Thread.sleep(2000);
				     
				     //unselect first option
				     getObject(firstListItemCheckBox).click();
				     
				     getObject(okButton).click();
				     
				     testStepTRCountAfterUncheckingCheckBox=0;
			    
				     //verify data for filtered item when first project project is unchecked and rest of projects checked	
				     do 
				     {
				    	 	testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
														
							testStepTRCountAfterUncheckingCheckBox+= testStepGrid;
				      
				     }while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink")); 
				     
				     //verify tr counts when first option is unchecked and remaining option is checked
				     if(compareIntegers((initialTestStepTRCount-testStepTRCountWhen1ChkBoxChecked), testStepTRCountAfterUncheckingCheckBox))
				     {
				    	 	APP_LOGS.debug("Total Entries displayed in table after Unchecking any checkbox "+selectedFilterItem+" is correct.");
				    	 	comments += "\n- Total Entries displayed in table after Unchecking any checkbox "+selectedFilterItem+" is correct.(Pass) ";
				     }
				     else
				     {
				    	 	fail=true;
				    	 	APP_LOGS.debug("Total Entries displayed in table after Unchecking any checkbox "+selectedFilterItem+" is incorrect.");
				    	 	comments += "\n- Total Entries displayed in table after Unchecking any checkbox "+selectedFilterItem+" is incorrect.(Fail) ";
				    	 	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total Entries displayed in table after Unchecking any checkbox is NOT correct.");
				     }
				     
				     Thread.sleep(1000);
				     
				     //Click on Filter logo							     
				     getObject(filterImage).click();
				     	
				     Thread.sleep(2000);
				     
				     //Verify whether "Clear Filter" link is present and Click on it								    
				     if (getElement(clearFilterLink).isDisplayed())
				     {
					      APP_LOGS.debug("'Clear Filter' link is displayed after filter is applied.");									      
					      comments += "\n- 'Clear Filter' link is displayed after filter is applied.(Pass) ";
					      
					      //Click on Clear Filter	
					      getElement(clearFilterLink).click();
				     }
				     else 
				     {
					      fail=true;								      
					      APP_LOGS.debug("'Clear Filter' link is NOT displayed after filter is applied.");								      
					      comments += "\n- 'Clear Filter' link is NOT displayed after filter is applied. (Fail)";								      
					      TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Clear Filter' link is NOT displayed after filter is applied.");
				     }
				     
				     Thread.sleep(1000);
				     
				     //verify filter is cleared
				     filterAppliedAttr = getObject(filterImage).getAttribute("src");
				     if(assertTrue(filterAppliedAttr.contains("no-filter.png")))
				     {
				    	 	APP_LOGS.debug("Filter image is changed to clear filter image.");
				    	 	comments += "\n- Filter image is changed to clear filter image.(Pass) ";
				     }
				     else
				     {
				    	 	fail=true;
				    	 	APP_LOGS.debug("Filter image is not changed to clear filter image.");
				    	 	comments += "\n- Filter image is not changed to clear filter image.(Fail) ";
				    	 	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Filter image is not changed to clear filter image.");
				     }
				     return true;
		     }
		     else 
		     {
		    	 //click on cancel button as only one entry is available in filter box
		    	 getObject(cancelButton).click();
		    	 return true;
		     }
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;			
		}
		finally
	    {
			resetImplicitWait();
	    }
		return false;
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