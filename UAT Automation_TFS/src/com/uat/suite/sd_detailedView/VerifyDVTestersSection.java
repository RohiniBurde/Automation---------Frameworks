package com.uat.suite.sd_detailedView;

import java.util.ArrayList;
import java.util.List;

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

public class VerifyDVTestersSection extends TestSuiteBase  
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
	String[][] testerArray;
	
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
				
				if( Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager")
	        	 	    || Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Stakeholder+Admin")
	        	 		||Role.equalsIgnoreCase("Stakeholder+Version Lead")|| Role.equalsIgnoreCase("Admin+Stakeholder"))
	        	 	{
						getElement("UAT_stakeholderDashboard_Id").click();
					
						Thread.sleep(1000);
	        	 	}
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				
				selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));

				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				if ( ! defaultGroupSelected.equalsIgnoreCase("No Group") )
				{
						
					detailsArrayForVerification = new String[1][7];
					
					boolean isTestPassStatusDisplayed = false;
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						int breakCount = 0;
						
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
						{
							selectPortfolio.selectByIndex(j);
							
							
							if ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) 
							{
								do
								{
									int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
									
									for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
									{
										
										String passStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forPass", k).getText();
										
										String failStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forFail", k).getText();
										
										String ncStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forNC", k).getText();
										
										//This Condition can be changed to only TWO conditions
										
										if ( !( passStatus.equals("0") || failStatus.equals("0") || ncStatus.equals("0") ) ) 
										{
											
											isTestPassStatusDisplayed = true;
											
											do
											{
												int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
											
												
												int exicuteOnlyOnceCount = 0;
												
												if (exicuteOnlyOnceCount == 0) 
												{
													do {
														
														for (int l1 = 1; l1 <= numOfProjectPresentInGrid; l1++) 
														{
															getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", l1).click();
														}
														
													} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
													
													exicuteOnlyOnceCount = 1;
												}
												
												
												if ( ! ( getElement("SDDetailedView_projectPagination_Id").findElements(By.xpath("div/span")).size()==3 ) ) 
												{
													getObject("SDDetailedView_firstPageOfPagination").click();
													
												}
												
												for (int l = 1; l <= numOfProjectPresentInGrid; l++) 
												{
													
													//un-check the project check boxes
													getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", l).click();
												
													if ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
													{
														do
														{
															int numberOfTestPassDisplayed2 = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
															
															for (int m = 1; m <= numberOfTestPassDisplayed2; m++) 
															{
																
																String passStatus2 = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forPass", m).getText();
																
																String failStatus2 = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forFail", m).getText();
																
																String ncStatus2 = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forNC", m).getText();
																
																//This Condition can be changed to only TWO conditions
																
																if ( ! ( passStatus2.equals("0") || failStatus2.equals("0") || ncStatus2.equals("0") ) ) 
																{
																	String projectNameFromGrid = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).getAttribute("title");
																	
																	String versionFromGrid = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", l).getAttribute("title");
																
																	String testPassName = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", m).getText();
																	
																	detailsArrayForVerification[0][0] = projectNameFromGrid;
																	
																	detailsArrayForVerification[0][1] = versionFromGrid;
																	
																	detailsArrayForVerification[0][2] = testPassName;
	 																		
																	breakCount = 1;
																	
																	break;
																}
														
															}
															
														} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
													}
	
													
													if (breakCount == 1) 
													{
														break;
													}
													
												}
												
											} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
											
										}
											
										if (breakCount == 1) 
										{
											break;
										}
										
									}
									
								} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
								
							}
								
							if (breakCount == 1) 
							{
								break;
							}
							
						}
								
						if (breakCount == 1) 
						{
							break;
						}
							
					}
					
					if ( ! isTestPassStatusDisplayed ) 
					{
						APP_LOGS.debug("Group is present but No Proper Test Pass Status(Pass,Fail, Not Completed) is displayed to be verified (for default selection). So Skipping all the tests.");
						
						comments += "\n- Group is present but No Proper Test Pass Status(Pass,Fail, Not Completed) is displayed to be verified (for default selection). So Skipping all the tests.";
						
						throw new SkipException("Group is present but No Proper Test Pass Status(Pass,Fail, Not Completed) is displayed to be verified (for default selection). So Skipping all the tests.");
					}
					
					System.out.println("Project : "+ detailsArrayForVerification[0][0]);
					
					System.out.println("Version : "+ detailsArrayForVerification[0][1]);
					
					System.out.println("Test Pass : "+ detailsArrayForVerification[0][2]);
					
					
					closeBrowser();
					
					openBrowser();
					
					isLoginSuccess = login("Stakeholder+Admin");
					
					if(isLoginSuccess)
					{
					
						getElement("UAT_reports_Id").click();
						
						APP_LOGS.debug("Reports tab clicked ");
						
						Thread.sleep(2000);
						
						List<WebElement> listOfProjectsName = getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
						
						selectDDContent(listOfProjectsName, detailsArrayForVerification[0][0]);
						
						
						Select listOfVersions =  new Select(getElement("Triage_versionDropDown_Id"));
						
						listOfVersions.selectByVisibleText(detailsArrayForVerification[0][1]);
						
						
						List<WebElement> listOfTestPass = getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
						
						selectDDContentByText( listOfTestPass, detailsArrayForVerification[0][2]);
						
						
						List<WebElement> listOfTester = getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
						
						Select selectTester = new Select(getElement("Reports_selectTesterDD_Id"));
						
						Select selectStatus = new Select(getElement("Reports_StatusDropDown_Id"));
						
					/*	Select selectRole = new Select(getElement("Reports_selectRole_Id"));
						
						
						ArrayList<String> roleArraylist  = new ArrayList<String>();
					*/	
						//Array for the records for each tester with column TesterName, 
						
						testerArray = new String[ ( listOfTester.size() - 1 ) ][4];			
						
						for (int i = 0; i < testerArray.length ; i++) 
						{
							testerArray[i][0] = listOfTester.get(i+1).getText();
							
							selectTester.selectByIndex(i+1);
							
							
							selectStatus.selectByVisibleText("Pass");
							
							getObject("Report_GoButton").click();
							
							int tablePresence = eventfiringdriver.findElements(By.xpath("//div[@id='testStepDetails']/table")).size();
							
							testerArray[i][1] = "";
							
							if ( tablePresence == 1) 
							{
								testerArray[i][1] = "Display In Pass";
							}
							
							selectStatus.selectByVisibleText("Fail");
							
							getObject("Report_GoButton").click();
							
							tablePresence = eventfiringdriver.findElements(By.xpath("//div[@id='testStepDetails']/table")).size();
							
							testerArray[i][2] = "";
							
							if ( tablePresence == 1) 
							{
								testerArray[i][2] = "Display In Fail";
							}
							
							
							selectStatus.selectByVisibleText("Not Completed");
							
							getObject("Report_GoButton").click();
							
							tablePresence = eventfiringdriver.findElements(By.xpath("//div[@id='testStepDetails']/table")).size();
							
							testerArray[i][3] = "";
							
							if ( tablePresence == 1) 
							{
								testerArray[i][3] = "Display In Not Completed";
							}
							
						}
						
						for (int j = 0; j < testerArray.length; j++) 
						{
						
							System.out.println("testerArray testerName "+testerArray[j][0]);
							System.out.println("testerArray Pass "+testerArray[j][1]);
							System.out.println("testerArray Fail "+testerArray[j][2]);
							System.out.println("testerArray NC "+testerArray[j][3]);
						
						}
					}
					else 
					{
						APP_LOGS.debug("Login Unsuccessful for user "+"Admin");
						
						comments+="Login Unsuccessful for user Admin";
					
						throw new SkipException("Login Unsuccessful for user Admin.... So Skipping all tests");
					}
					
					closeBrowser();
					
					openBrowser();
					
					isLoginSuccess = login(Role);
					
					if(isLoginSuccess)
					{
					
						if( Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager")
			        	 	    || Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Stakeholder+Admin")
			        	 		||Role.equalsIgnoreCase("Stakeholder+Version Lead")|| Role.equalsIgnoreCase("Admin+Stakeholder"))
			        	 	{
								getElement("UAT_stakeholderDashboard_Id").click();
							
								Thread.sleep(1000);
			        	 	}
						
						getObject("StakeholderDashboard_detailedViewPageTab").click();
						
						Thread.sleep(1500);
						
						
						//Code to Select the Project Which has been stored in Array earlier
						
						selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
						
						selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							int breakCount = 0;
							
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								do 
								{	
									int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
									
									for (int k = 1; k <= numOfProjectPresentInGrid; k++) 
									{
																	
										String projectName = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k).getAttribute("title");
										
										String version = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", k).getAttribute("title");
										
										if ( projectName.equals(detailsArrayForVerification[0][0]) && version.equals(detailsArrayForVerification[0][1])) 
										{
											int exicuteOnlyOnceCount = 0;
											
											if (exicuteOnlyOnceCount == 0) 
											{
												
												do 
												{
													int numOfProjectPresentInGrid2 = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
													
													for (int k1 = 1; k1 <= numOfProjectPresentInGrid2; k1++) 
													{
														
														getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", k1).click();
													
													}
													
												} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
												
												
												exicuteOnlyOnceCount = 1;
												
											}
											
											
											if ( ! ( getElement("SDDetailedView_projectPagination_Id").findElements(By.xpath("div/span")).size() == 3 ) ) 
											{
												
												getObject("SDDetailedView_firstPageOfPagination").click();
												
											}
											
											do 
											{
												int numOfProjectPresentInGrid2 = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
												
												for (int k1 = 1; k1 <= numOfProjectPresentInGrid2; k1++) 
												{
													String projectName2 = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k1).getAttribute("title");
													
													String version2 = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", k1).getAttribute("title");
													
													
													if ( projectName2.equals(detailsArrayForVerification[0][0]) && version2.equals(detailsArrayForVerification[0][1])) 
													{
														
														getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", k1).click();
														
														//click on Test pass name 
														do
														{
															int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
															
															for (int m = 1; m <= numberOfTestPassDisplayed; m++) 
															{
																
																String passStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forPass", m).getText();
																
																String failStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forFail", m).getText();
																
																String ncStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forNC", m).getText();
																
																//This Condition can be changed to only TWO conditions
																
																if ( ! ( passStatus.equals("0") || failStatus.equals("0") || ncStatus.equals("0") ) ) 
																{
																
																	getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", m).click();
																	
																	breakCount = 1;
																	
																	break;
																}
														
															}
															
														} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
														
														if (breakCount ==1) 
														{
															break;
														}
													}
													
												}
												
											} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
											
											
											if (breakCount ==1) 
											{
												break;
											}
											
										}
										
									}
									
								} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
								
								if (breakCount ==1) 
								{
									break;
								}
								
							}
							if (breakCount ==1) 
							{
								break;
							}
							
						} // Project and Test Pass has been selected
						
						int totalBarGraphsDisplayed = 0;
						
						int successCount = 0;
						
						do 
						{
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							totalBarGraphsDisplayed += Integer.parseInt( totalBarGraphsDisplayedInString );
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
						
						
						if ( compareIntegers(testerArray.length, totalBarGraphsDisplayed) ) 
						{
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								if ( testerNameDisplayedUnderBarGraphs.equals(testerArray[j][0]) ) 
								{
									successCount++;
								}
								
							}
							
						}
						
						String allLinkColor = getObject("SDDetailedView_allLinkInTesterList").getAttribute("class");
						
						
						if ( compareIntegers( testerArray.length, successCount ) &&
							 compareStrings( "txtColor2", allLinkColor ) ) 
						{
		
							APP_LOGS.debug("All tester bar graphs are displayed in Tester List when 'All' is selected");
							
							comments += "\n- All tester bar graphs are displayed in Tester List when 'All' is selected";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("All tester bar graphs are displayed in Tester List when 'All' is selected (Fail)");
							
							comments += "\n- All tester bar graphs are displayed in Tester List when 'All' is selected (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass/Fail/Not Completed status percentage is NOT correct. (Fail)");
							
						}
						
						totalBarGraphsDisplayed = 0;
						
						ArrayList<String> testersForPass = new ArrayList<String>();
						ArrayList<String> testersForFail = new ArrayList<String>();
						ArrayList<String> testersForNC = new ArrayList<String>();
						
						successCount = 0;
						
						getElement("SDDetailedView_passedLink_Id").click();
						
						testersForPass = testersForPass(testerArray);
						
						for (int i = 0; i < testersForPass.size(); i++) 
						{
							System.out.println("testersForPass.get(i) "+testersForPass.get(i));
						}
						
						do 
						{
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							totalBarGraphsDisplayed += Integer.parseInt( totalBarGraphsDisplayedInString );
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
						
						
						if ( compareIntegers(testersForPass.size(), totalBarGraphsDisplayed) ) 
						{
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								if ( testerNameDisplayedUnderBarGraphs.equals(testersForPass.get(j)) ) 
								{
									successCount++;
								}
								
							}
							
						}
						
						String passedLinkColor = getObject("SDDetailedView_passedLinkInTesterList").getAttribute("class");
						
						
						if ( compareIntegers( testersForPass.size(), successCount ) &&
							 compareStrings( "txtColor2", passedLinkColor ) ) 
						{
		
							APP_LOGS.debug("On clicking 'Passed' link, all testers entry of the selected test pass where test steps are 'passed' are displayed.");
							
							comments += "\n- On clicking 'Passed' link, all testers entry of the selected test pass where test steps are 'passed' are displayed.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On clicking 'Passed' link, all testers entry of the selected test pass where test steps are 'passed' are NOT displayed. (Fail)");
							
							comments += "\n- On clicking 'Passed' link, all testers entry of the selected test pass where test steps are 'passed' are NOT displayed. (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Entries for pass are not correct (Fail)");
							
						}
						
						
						totalBarGraphsDisplayed = 0;
						successCount = 0;
						
						getElement("SDDetailedView_notCompletedLink_Id").click();
						
						testersForNC = testersForNC(testerArray);
						
						
						do 
						{
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							totalBarGraphsDisplayed += Integer.parseInt( totalBarGraphsDisplayedInString );
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
						
						
						if ( compareIntegers(testersForNC.size(), totalBarGraphsDisplayed) ) 
						{
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								if ( testerNameDisplayedUnderBarGraphs.equals(testersForNC.get(j)) ) 
								{
									successCount++;
								}
								
							}
							
						}
						
						String notCompletedLinkColor = getObject("SDDetailedView_notCompletedLinkInTesterList").getAttribute("class");
						
						
						if ( compareIntegers( testersForNC.size(), successCount ) &&
							 compareStrings( "txtColor2", notCompletedLinkColor ) ) 
						{
		
							APP_LOGS.debug("On clicking 'Not Completed' link, all testers entry of the selected test pass where test steps are 'Not Completed' are displayed.");
							
							comments += "\n- On clicking 'Not Completed' link, all testers entry of the selected test pass where test steps are 'Not Completed' are displayed.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On clicking 'Not Completed' link, all testers entry of the selected test pass where test steps are 'Not Completed' are NOT displayed. (Fail)");
							
							comments += "\n- On clicking 'Not Completed' link, all testers entry of the selected test pass where test steps are 'Not Completed' are NOT displayed. (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Entries for Not Completed are not correct (Fail)");
							
						}
						
						totalBarGraphsDisplayed = 0;
						successCount = 0;
						
						getElement("SDDetailedView_failedLink_Id").click();
						
						testersForFail = testersForFail(testerArray);
						
						
						do 
						{
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							totalBarGraphsDisplayed += Integer.parseInt( totalBarGraphsDisplayedInString );
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
						
						
						if ( compareIntegers(testersForFail.size(), totalBarGraphsDisplayed) ) 
						{
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								if ( testerNameDisplayedUnderBarGraphs.equals(testersForFail.get(j)) ) 
								{
									successCount++;
								}
								
							}
							
						}
						
						String failedLinkColor = getObject("SDDetailedView_failedLinkInTesterList").getAttribute("class");
						
						
						if ( compareIntegers( testersForFail.size(), successCount ) &&
							 compareStrings( "txtColor2", failedLinkColor ) ) 
						{
		
							APP_LOGS.debug("On clicking 'Failed' link, all testers entry of the selected test pass where test steps are 'Failed' are displayed.");
							
							comments += "\n- On clicking 'Failed' link, all testers entry of the selected test pass where test steps are 'Failed' are displayed.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On clicking 'Failed' link, all testers entry of the selected test pass where test steps are 'Failed' are NOT displayed. (Fail)");
							
							comments += "\n- On clicking 'Failed' link, all testers entry of the selected test pass where test steps are 'Failed' are NOT displayed. (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Entries for Failed are not correct (Fail)");
							
						}
						
						totalBarGraphsDisplayed = 0;
						successCount = 0;
						
						getElement("SDDetailedView_allLink_Id").click();
						
						
						do 
						{
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							totalBarGraphsDisplayed += Integer.parseInt( totalBarGraphsDisplayedInString );
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
						
						
						if ( compareIntegers(testerArray.length, totalBarGraphsDisplayed) ) 
						{
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								if ( testerNameDisplayedUnderBarGraphs.equals(testerArray[j][0]) ) 
								{
									successCount++;
								}
								
							}
							
						}
						
						allLinkColor = getObject("SDDetailedView_allLinkInTesterList").getAttribute("class");
						
						
						if ( compareIntegers( testerArray.length, successCount ) &&
							 compareStrings( "txtColor2", allLinkColor ) ) 
						{
		
							APP_LOGS.debug("On clicking 'All' link, all testers entry of the selected test pass where test steps are 'All' are displayed.");
							
							comments += "\n- On clicking 'All' link, all testers entry of the selected test pass where test steps are 'All' are displayed.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On clicking 'All' link, all testers entry of the selected test pass where test steps are 'All' are NOT displayed. (Fail)");
							
							comments += "\n- On clicking 'All' link, all testers entry of the selected test pass where test steps are 'All' are NOT displayed. (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Entries for All are not correct (Fail)");
							
						}
						
						
						
						
						boolean foundStatusWithoutPass = false;
						boolean foundStatusWithoutFail = false;
						boolean foundStatusWithoutNC = false;
						
									
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							int breakCount = 0;
							
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								
								if ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String passStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forPass", k).getText();
											
											if (  Integer.parseInt(passStatus) == 0) 
											{
												
												getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).click();
												
												breakCount = 1;
												
												foundStatusWithoutPass = true;
												
												break;
											}
												
										}
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
									
								if (breakCount == 1) 
								{
									break;
								}
								
							}
									
							if (breakCount == 1) 
							{
								break;
							}
								
						}
						
						if (foundStatusWithoutPass) 
						{
							getElement("SDDetailedView_passedLink_Id").click();
							
							String passTestStepNotAvailbaleText = getElement("SDDetailedView_testStepNotAvailable_Id").getText();
							
							if (passTestStepNotAvailbaleText.contains("Passed Test Steps are not available.")) 
							{
								APP_LOGS.debug("'Passed Test Steps are not available.' message is displayed when No Passed steps are present.");
								
								comments += "\n- 'Passed Test Steps are not available.' message is displayed when 'Passed' Test Steps are not present.";
							
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("'Passed Test Steps are not available.' message is NOT displayed when No Passed steps are present. (Fail)");
								
								comments += "\n- 'Passed Test Steps are not available.' message is NOT displayed when 'Passed' Test Steps are not present. (Fail)";
							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Passed Test Steps are not available.' message is NOT displayed");
								
							}
						}
						
						
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							int breakCount = 0;
							
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								
								if ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String failStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forFail", k).getText();
											
											if (  Integer.parseInt(failStatus) == 0) 
											{
												
												getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).click();
												
												breakCount = 1;
												
												foundStatusWithoutFail = true; 
												
												break;
											}
												
										}
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
									
								if (breakCount == 1) 
								{
									break;
								}
								
							}
									
							if (breakCount == 1) 
							{
								break;
							}
								
						}
						
						if (foundStatusWithoutFail) 
						{
							
							getElement("SDDetailedView_failedLink_Id").click();
							
							String failedTestStepNotAvailbaleText = getElement("SDDetailedView_testStepNotAvailable_Id").getText();
							
							if (failedTestStepNotAvailbaleText.contains("Failed Test Steps are not available.")) 
							{
								APP_LOGS.debug("'Failed Test Steps are not available.' message is displayed when No failed steps are present.");
								
								comments += "\n- 'Failed Test Steps are not available.' message is displayed when 'failed' Test Steps are not present.";
							
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("'Failed Test Steps are not available.' message is NOT displayed when No failed steps are present. (Fail)");
								
								comments += "\n- 'Failed Test Steps are not available.' message is NOT displayed when 'failed' Test Steps are not present. (Fail)";
							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Failed Test Steps are not available.' message is NOT displayed");
								
							}
							
						}
						
						
		
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							int breakCount = 0;
							
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								
								if ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String ncStatus = getObject("SDDetailedView_statusBarInGrid1", "SDDetailedView_statusBarInGrid2forNC", k).getText();
											
											if (  Integer.parseInt(ncStatus) == 0) 
											{
												
												getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).click();
												
												breakCount = 1;
												
												foundStatusWithoutNC = true; 
												
												break;
											}
												
										}
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
									
								if (breakCount == 1) 
								{
									break;
								}
								
							}
									
							if (breakCount == 1) 
							{
								break;
							}
								
						}
						
						if (foundStatusWithoutNC) 
						{
							
							getElement("SDDetailedView_notCompletedLink_Id").click();
							
							String ncTestStepNotAvailbaleText = getElement("SDDetailedView_testStepNotAvailable_Id").getText();
							
							if (ncTestStepNotAvailbaleText.contains("Not Completed Test Steps are not available.")) 
							{
								APP_LOGS.debug("'Not Completed Test Steps are not available.' message is displayed when No not completed steps are present.");
								
								comments += "\n- 'Not Completed Test Steps are not available.' message is displayed when 'not completed' Test Steps are not present.";
							
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("'Not Completed Test Steps are not available.' message is NOT displayed when No not completed steps are present. (Fail)");
								
								comments += "\n- 'Not Completed Test Steps are not available.' message is NOT displayed when 'not completed' Test Steps are not present. (Fail)";
							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Not Completed Test Steps are not available.' message is NOT displayed");
								
							}
							
						}
					}
					else 
					{
						APP_LOGS.debug("Login while verification is Unsuccessful for user "+Role);
						
						comments+="Login while verification is Unsuccessful for user "+Role;
					
						throw new SkipException("Login while verification is Unsuccessful for user "+Role+".... So Skipping all tests");
					}
				}
				else 
				{
					skip = true;
					
					APP_LOGS.debug("No Groups are present for the logged-in user (for the current quarter).");
					
					comments += "\n- No Groups are present for the logged-in user (for the current quarter). So, verification cannot be done and skipping the test.";
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
	
	private ArrayList<String> testersForPass (String[][] testerArray)
	{
		ArrayList<String> returnArrayList = new ArrayList<String>();
		
		for (int i = 0; i < testerArray.length; i++) 
		{
			if (testerArray[i][1].equals("Display In Pass")) 
			{
				returnArrayList.add(testerArray[i][0]);
			}
		}
		return returnArrayList;
	}
	
	private ArrayList<String> testersForFail (String[][] testerArray)
	{
		ArrayList<String> returnArrayList = new ArrayList<String>();
		
		for (int i = 0; i < testerArray.length; i++) 
		{
			if (testerArray[i][2].equals("Display In Fail")) 
			{
				returnArrayList.add(testerArray[i][0]);
			}
		}
		return returnArrayList;
	}
	
	private ArrayList<String> testersForNC (String[][] testerArray)
	{
		ArrayList<String> returnArrayList = new ArrayList<String>();
		
		for (int i = 0; i < testerArray.length; i++) 
		{
			if (testerArray[i][3].equals("Display In Not Completed")) 
			{
				returnArrayList.add(testerArray[i][0]);
			}
		}
		return returnArrayList;
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