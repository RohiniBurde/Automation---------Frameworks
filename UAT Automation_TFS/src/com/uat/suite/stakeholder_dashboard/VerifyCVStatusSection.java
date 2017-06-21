/**
 * @author preeti.walde
 * Created: 3rd Feb 2015
   Last Modified: 3rd Feb 2015
   Description: Code to verify status links 'All, Passed, Not Completed, Failed, Executed' in Consolidated View tab.
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
public class VerifyCVStatusSection extends TestSuiteBase
{	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	int passCount=0;
	int failCount=0;
	int NCCount=0;	
	
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
	public void verifyCVStatusSection(String Role) throws Exception
	{			
		String totalTestStepText;
		String testStepPassedText;
		String testStepFailedText;
		String testStepNCText;
		int numOfProjectPresentInGrid;
		int projectSelectedCounter;
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
						}
						else
						{
							projectSelectedCounter=0;
							
							do
							{	
								numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
								
								for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
								{   	
									int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																		
									if(grpTDSize==6)
									{
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
			        	 
							
							//if all project names are selected then verify pie chart
							if(projectSelectedCounter!=0)
							{
									long pieChart = (long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg').size();");
									int pieChartCount = (int)pieChart;
									
									if(pieChartCount==1)
									{
											if(assertTrue(getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").isDisplayed()))
											{
													comments += "\n Status Pie Chart is displayed.(Pass) ";						
													APP_LOGS.debug("Status Pie Chart is displayed.");
											}
											else
											{
												fail=true;
												comments += "\n Status Pie Chart NOT displayed.(Fail) ";						
												APP_LOGS.debug("Status Pie Chart NOT displayed.");
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StatusPieChartNotVisible");
											}
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
									
									totalTestStepText = getElement("StakeholderDashboardConsolidatedView_testStepTotalTestStepCount_Id").getText();
									testStepPassedText = getElement("StakeholderDashboardConsolidatedView_testStepPassCount_Id").getText();
									testStepFailedText = getElement("StakeholderDashboardConsolidatedView_testStepFailCount_Id").getText();
									testStepNCText = getElement("StakeholderDashboardConsolidatedView_testStepNotCompletedCount_Id").getText();
																		
									
									//click on All link
									int totalTSForAll = verifyStatusLink("StakeholderDashboardConsolidatedView_statusLinkAll_Id", passCount,
											failCount, NCCount);
									
									
									//compare total test steps count and verify
									if(compareIntegers(Integer.parseInt(totalTestStepText), totalTSForAll))
									{
										comments += "\n Count for 'All' link test steps count "+totalTSForAll+" is correct and matched with "+Integer.parseInt(totalTestStepText)+".(Pass) ";						
										APP_LOGS.debug("Count for 'All' link test steps count "+totalTSForAll+" is correct and matched with "+Integer.parseInt(totalTestStepText)+".");
									}
									else
									{
										fail=true;
										comments += "\n Count for 'All' link test steps count "+totalTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(totalTestStepText)+".(Fail) ";						
										APP_LOGS.debug("Count for 'All' link test steps count "+totalTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(totalTestStepText)+".");
									}
									
									
									//click on Passed link
									int passedTSForAll = verifyStatusLink("StakeholderDashboardConsolidatedView_statusLinkPassed_Id", passCount,
											failCount, NCCount);
										
									
									//compare pass count and verify
									if(compareIntegers(Integer.parseInt(testStepPassedText), passedTSForAll))
									{
										comments += "\n Count for 'Passed' link test steps count "+passedTSForAll+" is correct and matched with "+Integer.parseInt(testStepPassedText)+".(Pass) ";						
										APP_LOGS.debug("Count for 'Passed' link test steps count "+passedTSForAll+" is correct and matched with "+Integer.parseInt(testStepPassedText)+".");
									}
									else
									{
										fail=true;
										comments += "\n Count for 'Passed' link test steps count "+passedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepPassedText)+".(Fail) ";						
										APP_LOGS.debug("Count for 'Passed' link test steps count "+passedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepPassedText)+".");
									}
									
									
									//click on Not Completed link
									int notCompletedTSForAll = verifyStatusLink("StakeholderDashboardConsolidatedView_statusLinkNotCompleted_Id", passCount,
											failCount, NCCount);
												
									
									//compare not completed count and verify
									if(compareIntegers(Integer.parseInt(testStepNCText), notCompletedTSForAll))
									{
										comments += "\n Count for 'Not Comlpeted' link test steps count "+notCompletedTSForAll+" is correct and matched with "+Integer.parseInt(testStepNCText)+".(Pass) ";						
										APP_LOGS.debug("Count for 'Not Comlpeted' link test steps count "+notCompletedTSForAll+" is correct and matched with "+Integer.parseInt(testStepNCText)+".");
									}
									else
									{
										fail=true;
										comments += "\n Count for 'Not Comlpeted' link test steps count "+notCompletedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepNCText)+".(Fail) ";						
										APP_LOGS.debug("Count for 'Not Comlpeted' link test steps count "+notCompletedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepNCText)+".");
									}
									
									
									//click on Failed link
									int failedTSForAll = verifyStatusLink("StakeholderDashboardConsolidatedView_statusLinkFailed_Id", passCount,
											failCount, NCCount);
											
									
									//compare failed count and verify
									if(compareIntegers(Integer.parseInt(testStepFailedText), failedTSForAll))
									{
										comments += "\n Count for 'Failed' link test steps count "+failedTSForAll+" is correct and matched with "+Integer.parseInt(testStepFailedText)+".(Pass) ";						
										APP_LOGS.debug("Count for 'Failed' link test steps count "+failedTSForAll+" is correct and matched with "+Integer.parseInt(testStepFailedText)+".");
									}
									else
									{
										fail=true;
										comments += "\n Count for 'Failed' link test steps count "+failedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepFailedText)+".(Fail) ";						
										APP_LOGS.debug("Count for 'Failed' link test steps count "+failedTSForAll+" is incorrect and doesnot matched with "+Integer.parseInt(testStepFailedText)+".");
									}
							}
							else
							{
								APP_LOGS.debug("No Test Step Available.");
							}
						}
						
						
						//select date
						Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
						selectDateDD.selectByVisibleText("Today");	
						Thread.sleep(500);
						
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
						}
						else
						{
							projectSelectedCounter=0;
							
							do
							{	
								numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
								
								for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
								{   	
									int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																		
									if(grpTDSize==6)
									{										
										if(getObject("StakeholderDashboardConsolidatedView_projectNameChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectNameChkBoxXpath2", x).isSelected())
										{
											projectSelectedCounter++;
										}
									}
									else
									{
										if(getObject("StakeholderDashboardConsolidatedView_projectChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectChkBoxXpath2", x).isSelected())
										{
											projectSelectedCounter++;
										}
									}
								}
								
							} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
			        	 
							
							//if all project names are selected then verify pie chart
							if(projectSelectedCounter!=0)
							{
									long pieChart = (long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg').size();");
									int pieChartCount = (int)pieChart;
									
									if(pieChartCount==1)
									{
											if(assertTrue(getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").isDisplayed()))
											{
													comments += "\n Status Pie Chart is displayed for Today filter.(Pass) ";						
													APP_LOGS.debug("Status Pie Chart is displayed for Today filter.");
											}
											else
											{
												fail=true;
												comments += "\n Status Pie Chart NOT displayed for Today filter.(Fail) ";						
												APP_LOGS.debug("Status Pie Chart NOT displayed for Today filter.");
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StatusPieChartNotVisibleForTodayFilter");
											}
									}
									
									testStepPassedText = getElement("StakeholderDashboardConsolidatedView_testStepPassCount_Id").getText();
									testStepFailedText = getElement("StakeholderDashboardConsolidatedView_testStepFailCount_Id").getText();
									totalTestStepText = getElement("StakeholderDashboardConsolidatedView_testStepTotalTestStepCount_Id").getText();
									
									//click on Executed link
									int executedTSForToday = verifyStatusLink("StakeholderDashboardConsolidatedView_statusLinkExecuted_Id", passCount,
											failCount, NCCount);
										
									
									//compare total test steps count and verify
									if(compareIntegers(Integer.parseInt(totalTestStepText), executedTSForToday))
									{
										comments += "\n Count for 'Executed' link test steps count "+executedTSForToday+" is correct and matched with "+Integer.parseInt(totalTestStepText)+".(Pass) ";						
										APP_LOGS.debug("Count for 'Executed' link test steps count "+executedTSForToday+" is correct and matched with "+Integer.parseInt(totalTestStepText)+".");
									}
									else
									{
										fail=true;
										comments += "\n Count for 'Executed' link test steps count "+executedTSForToday+" is incorrect and doesnot matched with "+Integer.parseInt(totalTestStepText)+".(Fail) ";						
										APP_LOGS.debug("Count for 'Executed' link test steps count "+executedTSForToday+" is incorrect and doesnot matched with "+Integer.parseInt(totalTestStepText)+".");
									}
							}
							else
							{
								APP_LOGS.debug("No Test Step Available.");
							}
						}
		         }
		         
		         catch(Throwable t)
		         {
		        	 t.printStackTrace();
		        	 fail=true;
		        	 assertTrue(false);
		        	 APP_LOGS.debug("Exception occured in Consolidated View tab of Stakeholder Dashboard Page");						 
					 comments += "\n Exception occured in Consolidated View tab of Stakeholder Dashboard Page.(Fail)";						
		         }	
		         
		         closeBrowser();
			         
		}
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
		}	
			
	}


	private int verifyStatusLink(String statusLink, int pass, int Fail, int notCompleted)
	{	
		int total = 0;
		
		try
		{
				getElement(statusLink).click();
				Thread.sleep(500);
				
				int noTestStepMsgSize = getElement("StakeholderDashboardConsolidatedView_noTestStepLabelDiv_Id").findElements(By.tagName("a")).size();
				
				if(noTestStepMsgSize==1)
				{
					if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepLabel"), getObject("StakeholderDashboardConsolidatedView_noTestStepLabel").getText()))
					{
						APP_LOGS.debug("No Test Steps available! message is verified for '"+getElement(statusLink).getText()+"' link.");
						comments+="No Test Steps available! message is verified for '"+getElement(statusLink).getText()+"' link.(Pass) ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("'No Test Steps available!' message not shown for '"+getElement(statusLink).getText()+"' link.");
						comments+="'No Test Steps available!' message not shown for '"+getElement(statusLink).getText()+"' link.(Fail) ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for "+getElement(statusLink).getText()+" link");
					}
				}
				else
				{
					do
					{		
							int testStepGrid = getElement("StakeholderDashboardConsolidatedView_testStepGrid_Id").findElements(By.xpath("tbody/tr")).size();
					
							for (int j = 1; j <= testStepGrid; j++) 
							{
									if(getObject("StakeholderDashboardConsolidatedView_testStepStatusColXpath1", "StakeholderDashboardConsolidatedView_testStepStatusColXpath2", j).getText().equals("Pass"))
									{
										pass++;
									}
									
									else if(getObject("StakeholderDashboardConsolidatedView_testStepStatusColXpath1", "StakeholderDashboardConsolidatedView_testStepStatusColXpath2", j).getText().equals("Fail"))
									{
										Fail++;
									}
									
									else
									{
										notCompleted++;
									}
							}
						
					}while(ifElementClickable("StakeholderDashboardConsolidatedView_testStepNextLink"));	
				}
				
				total = pass+Fail+notCompleted;
				
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
		}
		
		return total;
			
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