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

public class VerifyDVTestPassSection extends TestSuiteBase  
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
	public void testVerifyDVTestPassSection(String Role ) throws Exception
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
					boolean isTestPassStatusDisplayed = false;
					
					detailsArrayForVerification = new String[1][7];
					
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
					
						getElement("UAT_dashboard_Id").click();
						
						getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
						
						Select selectTester = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
						
						List<WebElement> projectDD = getElement("Dashboard_projectDD_ID").findElements(By.tagName("option"));
						
						int totalTester = 0;
						String allTesterName = "";
						String allPassCountOnGauge = "";
						String allFailCountOnGauge = "";
						String allNCCountOnGauge = "";
						
						for (int i = 0; i < detailsArrayForVerification.length; i++) 
						{
							for(int j=0; j<projectDD.size(); j++)
							{
								if(projectDD.get(j).getAttribute("title").equals(detailsArrayForVerification[0][0]))
								{
									Thread.sleep(500);
									
									projectDD.get(j).click();
									
									APP_LOGS.debug("Project is selected.");
									
		
									List<WebElement> versionDD = getElement("DetailedAnalysis_versionDropDown_Id").findElements(By.tagName("option"));
									
									for(int k=0; k < versionDD.size(); k++)
									{
										
										if(versionDD.get(k).getAttribute("title").equals(detailsArrayForVerification[0][1]))
										{
											versionDD.get(k).click();
											
											
												List<WebElement> numberOfTestPass = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
												
												for (int l = 0; l < numberOfTestPass.size(); l++) 
												{
													
													if (numberOfTestPass.get(l).getAttribute("title").equals(detailsArrayForVerification[0][2] ) ) 
													{
														numberOfTestPass.get(l).click();
														
														List<WebElement> numberOfTester = getElement("DetailedAnalysis_testerDropDown_Id").findElements(By.tagName("option"));
														
														totalTester = numberOfTester.size();
														
														for (int m = 0; m < numberOfTester.size(); m++) 
														{
															String testerName = selectTester.getOptions().get(m).getText();
															
															//testersToBeVerified.add(testerName); // All Tester Added in Arraylist
															
															allTesterName += testerName + ", ";
		
															selectTester.selectByIndex(m);
															
															
															String passCountOnGauge = (String) eventfiringdriver.executeScript("return $('#visualizationPass').find('table tbody tr td div div svg g g').text()");
															
															//passPercentageToBeVerified.add(passCountOnGauge);
															
															allPassCountOnGauge += passCountOnGauge + ", ";
															
															String failCountOnGauge = (String) eventfiringdriver.executeScript("return $('#visualizationFail').find('table tbody tr td div div svg g g').text()");
															
															//failPercentageToBeVerified.add(failCountOnGauge);
		
															allFailCountOnGauge += failCountOnGauge + ", ";
															
															String NCCountOnGauge = (String) eventfiringdriver.executeScript("return $('#visualizationNC').find('table tbody tr td div div svg g g').text()");
														
															//ncPercentageToBeVerified.add(NCCountOnGauge);
														
															allNCCountOnGauge += NCCountOnGauge + ", ";
														}
														
													}
													
													
													
												}
												
											break;
										}
										
									}
									
									break;							
								}
								
							}
							
						}
						
						detailsArrayForVerification[0][3] = allTesterName;
						detailsArrayForVerification[0][4] = allPassCountOnGauge;
						detailsArrayForVerification[0][5] = allFailCountOnGauge;
						detailsArrayForVerification[0][6] = allNCCountOnGauge;
						
						System.out.println("detailsArrayForVerification[0][3] "+ detailsArrayForVerification[0][3]);
						System.out.println("detailsArrayForVerification[0][4] "+ detailsArrayForVerification[0][4]);
						System.out.println("detailsArrayForVerification[0][5] "+ detailsArrayForVerification[0][5]);
						System.out.println("detailsArrayForVerification[0][6] "+ detailsArrayForVerification[0][6]);
						
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
														
														breakCount = 1;
														
														break;
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
							
						} // Project has been selected
						
						
						String[] testerNames = detailsArrayForVerification[0][3].split(", ");
						
						String[] totalPassByEveryTester = detailsArrayForVerification[0][4].split(", ");
						
						String[] totalFailByEveryTester = detailsArrayForVerification[0][5].split(", ");
						
						String[] totalNCByEveryTester = detailsArrayForVerification[0][6].split(", ");
						
						
						for (int i = 0; i < totalNCByEveryTester.length; i++) 
						{
							System.out.println("testerNames["+i+"] " + testerNames[i]);
							System.out.println("totalPassByEveryTester["+i+"] " + totalPassByEveryTester[i]);
							System.out.println("totalFailByEveryTester["+i+"] " + totalFailByEveryTester[i]);
							System.out.println("totalNCByEveryTester["+i+"] " + totalNCByEveryTester[i]);
						}
						
						
						String passPercentageDisplayedInGreenStatusGrid = "";
						String failPercentageDisplayedInRedStatusGrid = "";
						String ncPercentageDisplayedInYellowStatusGrid = "";
					
						ArrayList<String> testersUnderBarGraph = new ArrayList<String>();
						
						do 
						{
							int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
							
							for (int i = 1; i <= numberOfTestPassDisplayed; i++) 
							{
								String testPassNameFromGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", i).getText();
								
								if (testPassNameFromGrid.equals(detailsArrayForVerification[0][2])) 
								{
									
									passPercentageDisplayedInGreenStatusGrid = getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassGreenStatus2", i).getText();
									
									failPercentageDisplayedInRedStatusGrid = getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassRedStatus2", i).getText();
									
									ncPercentageDisplayedInYellowStatusGrid = getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassYellowStatus2", i).getText();
								
									
									//get the position of Arrow
									
									if (  assertTrue( getElement("SDDetailedView_showTesterDetailsArrow_Id").isDisplayed() ) && 
										  compareStrings( "Show Tester Details", getElement("SDDetailedView_showTesterDetailsArrow_Id").getText() ) )
									{
										//Click on Test pass Link and verify all testers are displayed in Tester list bar graphs
										
										getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", i).click();
										
										Thread.sleep(500);
										
										do 
										{
											String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
											
											int totalBarGraphsDisplayed = Integer.parseInt( totalBarGraphsDisplayedInString );
											
											for (int j = 0; j < totalBarGraphsDisplayed; j++) 
											{
												String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
												
												testersUnderBarGraph.add(testerNameDisplayedUnderBarGraphs);
											}
											
											
										} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
										
									}
									else 
									{
										fail=true;
										
										APP_LOGS.debug("'Show Tester Details' arrow is not displayed OR arrow doesn's contains Text as 'Show Tester Details'");
										
										comments += "\n- 'Show Tester Details' arrow is not displayed OR arrow doesn's contains Text as 'Show Tester Details'";
									
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Show Tester Details' arrow is not displayed OR arrow doesn's contains Text as 'Show Tester Details'");
										
									}
									
									break;
								} 
			
							}
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
						
						
						
						if ( compareStrings(totalPassByEveryTester[0], passPercentageDisplayedInGreenStatusGrid) && 
							 compareStrings(totalFailByEveryTester[0], failPercentageDisplayedInRedStatusGrid) && 
						     compareStrings(totalNCByEveryTester[0], ncPercentageDisplayedInYellowStatusGrid) ) 
						{
							APP_LOGS.debug("'Pass, Fail, Not Completed' percentage displayed on Status block in Test Pass Grid is correct.");
							
							comments += "\n- 'Pass, Fail, Not Completed' percentage displayed on Status block in Test Pass Grid is correct.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("'Pass, Fail, Not Completed' percentage displayed on Status block in Test Pass Grid is NOT correct. (Fail)");
							
							comments += "\n- 'Pass, Fail, Not Completed' percentage displayed on Status block in Test Pass Grid is NOT correct. (Fail)";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass/Fail/Not Completed status percentage is NOT correct. (Fail)");
							
							throw new SkipException("'Pass, Fail, Not Completed' percentage displayed on Status block in Test Pass Grid is NOT correct. (Fail)... So Skipping all tests");
							
						}
						
						
						for (int i = 0; i < testersUnderBarGraph.size(); i++) 
						{
			
							System.out.println("testersUnderBarGraph "+i+" "+testersUnderBarGraph.get(i));
						
						}
						
						
						
						int successCount = 0;
						
						String allLinkColor = getObject("SDDetailedView_allLinkInTesterList").getAttribute("class");
						
			
						if ( compareIntegers( ( testerNames.length -1 ), testersUnderBarGraph.size() ) &&
							 assertTrue( getElement("SDDetailedView_hideTesterDetailsArrow_Id").isDisplayed() ) && 
							 compareStrings("txtColor2", allLinkColor ) &&
							 compareStrings("Hide Tester Details", getElement("SDDetailedView_hideTesterDetailsArrow_Id").getText() ) ) 
						{
							for (int i = 0; i < testersUnderBarGraph.size(); i++) 
							{
								if (testersUnderBarGraph.get(i).equals(testerNames[i + 1])) // As First Tester name is 'All Testers' we increase index count of array by 1
								{
									successCount++;
								}
							}
							
						}
						else 
						{
							fail=true;
							
							APP_LOGS.debug("Total number of testers displayed for Test Pass Is NOT correct OR 'Hide Tester Details' arrow is not displayed OR arrow doesn's contains Text as 'Hide Tester Details'");
							
							comments += "\n- Total number of testers displayed for Test Pass Is NOT correct OR 'Hide Tester Details' arrow is not displayed OR arrow doesn's contains Text as 'Hide Tester Details'";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total number of testers are NOT correct OR 'Hide Tester Details' arrow is not working properly");
							
						}
						
						
						if (compareIntegers( ( testerNames.length - 1 ), successCount)) 
						{
							APP_LOGS.debug("All Testers displayed in Tester list grid are correct.");
							
							comments += "\n- All Testers displayed in Tester list grid are correct.";
							
							comments += "\n- 'Show/Hide Tester Details' arrows are displayed properly and arrow contains Text as 'Show/Hide Tester Details' accordingly";
							
							comments += "\n- 'All' link is highlighted in Tester List Grid after Click on Test pass Name";
							
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("All Testers displayed in Tester list grid are NOT correct. (Fail)");
							
							comments += "\n- All Testers displayed in Tester list grid are NOT correct. (Fail)";
						
							comments += "\n- 'Show/Hide Tester Details' arrows are NOT displayed properly and arrow doesn't contain Text as 'Show/Hide Tester Details' accordingly";
							
							comments += "\n- 'All' link is NOT highlighted in Tester List Grid after Click on Test pass Name";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All Testers displayed in Tester list grid are NOT correct. (Fail)");
							
							throw new SkipException("All Testers displayed in Tester list grid are NOT correct. (Fail)... So Skipping all tests");
							
						}
						
			
			
						//Click individually on green, red, Yellow colors of status bar
						
						ArrayList<String> expectedNumberOfTesterForPass = new ArrayList<String>();
			
						ArrayList<String> expectedTesterNameForPass = new ArrayList<String>();
						
						for (int i = 0; i < totalPassByEveryTester.length; i++) 
						{
							if (! totalPassByEveryTester[i].equals("0")) 
							{
								expectedNumberOfTesterForPass.add(totalPassByEveryTester[i]);
								
								expectedTesterNameForPass.add(testerNames[i]);
							}
						}
						
						System.out.println("expectedNumberOfTesterForPass.size() "+expectedNumberOfTesterForPass.size());
						
						for (int i = 0; i < expectedTesterNameForPass.size(); i++) 
						{
							System.out.println("expectedTesterNameForPass.get("+i+") "+expectedTesterNameForPass.get(i));
						}
						
						ArrayList<String> testerNameUnderBarGraphForPass = new ArrayList<String>();
						ArrayList<String> testerNameUnderBarGraphForFail = new ArrayList<String>();
						ArrayList<String> testerNameUnderBarGraphForNC = new ArrayList<String>();
						
						int totalBarGraphsDisplayedForPass = 0;
						int totalBarGraphsDisplayedForFail = 0;
						int totalBarGraphsDisplayedForNC = 0;
						
						do 
						{
							int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
							
							for (int i = 1; i <= numberOfTestPassDisplayed; i++) 
							{
								String testPassNameFromGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", i).getText();
								
								if (testPassNameFromGrid.equals(detailsArrayForVerification[0][2])) 
								{
									
									if ( ! ( getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassGreenStatus2", i).getText().equals("0") ) ) 
									{
										
										getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassGreenStatus2", i).click();
										
										//Click on Test pass Link and verify all testers are displayed in Tester list bar graphs
										
										do 
										{
											
											String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
											
											totalBarGraphsDisplayedForPass += Integer.parseInt( totalBarGraphsDisplayedInString );
											
											int totalBarGraphsDisplayed = Integer.parseInt( totalBarGraphsDisplayedInString );
											
											for (int j = 0; j < totalBarGraphsDisplayed; j++) 
											{
												String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
												
												testerNameUnderBarGraphForPass.add(testerNameDisplayedUnderBarGraphs);
												
											}
											
											
										} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
										
									}
									/*else 
									{
										testersUnderBarGraphForPass.add("No 'Pass' is displayed for selected project");
									}*/
									break;
								} 
			
							}
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
						
						
						for (int i = 0; i < testerNameUnderBarGraphForPass.size(); i++) 
						{
							System.out.println("testerNameUnderBarGraphForPass.get("+i+") "+ testerNameUnderBarGraphForPass.get(i));
						}
						
						String passedLinkColor = getObject("SDDetailedView_passedLinkInTesterList").getAttribute("class");
						
						successCount = 0;
						
						if ( compareIntegers(expectedNumberOfTesterForPass.size(), ( totalBarGraphsDisplayedForPass + 1 ) ) &&
							 compareStrings("txtColor2", passedLinkColor ) ) // +1 is done because of 'All Tester' Entry in Arraylist
						{
							for (int i = 0; i < testerNameUnderBarGraphForPass.size(); i++) 
							{
								if ( testerNameUnderBarGraphForPass.get(i).equals( expectedTesterNameForPass.get(i + 1) ) ) 
								{
									successCount++;
								}
							}
						}
						else 
						{
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list are NOT correct. (Fail)";
						
						}
						
						
						if (compareIntegers(expectedNumberOfTesterForPass.size(), ( successCount + 1) ) ) 
						{
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Pass' are correct.");
							
							comments += "\n- Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Pass' are correct.";
						
							comments += "\n- 'Passed' link is highlighted in Tester List Grid after Click on Green Status";
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Pass' are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Pass' are NOT correct. (Fail)";
						
						}
						
						
						
						ArrayList<String> expectedNumberOfTesterForFail = new ArrayList<String>();
			
						ArrayList<String> expectedTesterNameForFail = new ArrayList<String>();
						
						for (int i = 0; i < totalFailByEveryTester.length; i++) 
						{
							if (! totalFailByEveryTester[i].equals("0")) 
							{
								expectedNumberOfTesterForFail.add(totalFailByEveryTester[i]);
								
								expectedTesterNameForFail.add(testerNames[i]);
							}
						}
						
						System.out.println("expectedNumberOfTesterForFail.size() "+expectedNumberOfTesterForFail.size());
						
						for (int i = 0; i < expectedTesterNameForFail.size(); i++) 
						{
							System.out.println("expectedTesterNameForFail.get("+i+") "+expectedTesterNameForFail.get(i));
						}
						
						do 
						{
							int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
							
							for (int i = 1; i <= numberOfTestPassDisplayed; i++) 
							{
								String testPassNameFromGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", i).getText();
								
								if (testPassNameFromGrid.equals(detailsArrayForVerification[0][2])) 
								{
									if ( ! ( getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassRedStatus2", i).getText().equals("0") ) ) 
									{
									
										getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassRedStatus2", i).click();
										
										//Click on Test pass Link and verify all testers are displayed in Tester list bar graphs
										
										do 
										{
											
											String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
											
											totalBarGraphsDisplayedForFail += Integer.parseInt( totalBarGraphsDisplayedInString );
											
											int totalBarGraphsDisplayed = Integer.parseInt( totalBarGraphsDisplayedInString );
											
											for (int j = 0; j < totalBarGraphsDisplayed; j++) 
											{
												String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
												
												
												testerNameUnderBarGraphForFail.add(testerNameDisplayedUnderBarGraphs);
											}
											
											
										} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
										
									}
									/*else 
									{
										testersUnderBarGraphForFail.add("No 'Fail' is displayed for selected project");
									}*/
									break;
								} 
			
							}
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
						
						
						String failedLinkColor = getObject("SDDetailedView_failedLinkInTesterList").getAttribute("class");
						
						successCount = 0;
						
						if ( compareIntegers(expectedNumberOfTesterForFail.size(), ( totalBarGraphsDisplayedForFail + 1 ) ) &&
							 compareStrings("txtColor2", failedLinkColor ) ) 
						{
							for (int i = 0; i < testerNameUnderBarGraphForFail.size(); i++) 
							{
								if ( testerNameUnderBarGraphForFail.get(i).equals( expectedTesterNameForFail.get(i+1) ) ) 
								{
									successCount++;
								}
							}
						}
						else 
						{
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list are NOT correct. (Fail)";
						
						}
						
						if ( compareIntegers( expectedNumberOfTesterForFail.size(), ( successCount + 1 ) ) ) 
						{
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Fail' are correct.");
							
							comments += "\n- Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Fail' are correct.";
						
							comments += "\n- 'Failed' link is highlighted in Tester List Grid after Click on Red Status";
							
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Fail' are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Fail' are NOT correct. (Fail)";
						
						}
						
						
						
						ArrayList<String> expectedNumberOfTesterForNC = new ArrayList<String>();
			
						ArrayList<String> expectedTesterNameForNC = new ArrayList<String>();
						
						for (int i = 0; i < totalNCByEveryTester.length; i++) 
						{
							if (! totalNCByEveryTester[i].equals("0")) 
							{
								expectedNumberOfTesterForNC.add(totalNCByEveryTester[i]);
								
								expectedTesterNameForNC.add(testerNames[i]);
							}
						}
						
						System.out.println("expectedNumberOfTesterForNC.size() "+expectedNumberOfTesterForNC.size());
						
						
						do 
						{
							int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
							
							for (int i = 1; i <= numberOfTestPassDisplayed; i++) 
							{
								String testPassNameFromGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", i).getText();
								
								if (testPassNameFromGrid.equals(detailsArrayForVerification[0][2])) 
								{
									
									if ( ! ( getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassYellowStatus2", i).getText().equals("0") ) ) 
									{
										
										getObject("SDDetailedView_testPassStatus1", "SDDetailedView_testPassYellowStatus2", i).click();
									   
										
										//Click on Test pass Link and verify all testers are displayed in Tester list bar graphs
										
										do 
										{
											
											String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
											
											totalBarGraphsDisplayedForNC += Integer.parseInt( totalBarGraphsDisplayedInString );
											
											int totalBarGraphsDisplayed = Integer.parseInt( totalBarGraphsDisplayedInString );
											
											for (int j = 0; j < totalBarGraphsDisplayed; j++) 
											{
												String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
												
												
												testerNameUnderBarGraphForNC.add(testerNameDisplayedUnderBarGraphs);
											}
											
											
										} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
										
									}
									/*else 
									{
										testersUnderBarGraphForNC.add("No 'Not Completed' is displayed for selected project");
									}*/
									break;
								} 
			
							}
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
						
						
						String notCompletedLinkColor = getObject("SDDetailedView_notCompletedLinkInTesterList").getAttribute("class");
						
						successCount = 0;
						
						if ( compareIntegers(expectedNumberOfTesterForNC.size(), ( totalBarGraphsDisplayedForNC + 1) ) &&
							 compareStrings("txtColor2", notCompletedLinkColor ) ) 
						{
							for (int i = 0; i < testerNameUnderBarGraphForNC.size(); i++) 
							{
								if ( testerNameUnderBarGraphForNC.get(i).equals( expectedTesterNameForNC.get(i+1) ) ) 
								{
									successCount++;
								}
							}
						}
						else 
						{
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list (While NC) are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list (While NC) are NOT correct. (Fail)";
						
						}
						
						
						if ( compareIntegers(expectedNumberOfTesterForNC.size(), ( successCount + 1 ) ) ) 
						{
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Not Completed' are correct.");
							
							comments += "\n- Total number of Tester displayed in Tester list are Correct and Tester Names displayed for 'Not Completed' are correct.";
						
							comments += "\n- 'Not Completed' link is highlighted in Tester List Grid after Click on Yellow Status";
							
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Not Completed' are NOT correct. (Fail)");
							
							comments += "\n- Total number of Tester displayed in Tester list are NOT correct OR Tester Names displayed for 'Not Completed' are NOT correct. (Fail)";
						
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
	
	//Functions
	

	
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
