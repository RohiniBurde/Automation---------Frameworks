/**
 * @author preeti.walde
 * Created: 2nd Feb 2015
 * Last Modified: 16th Feb 2015
 * Description: Code to verify project section in Consolidated View tab.
 */

package com.uat.suite.stakeholder_dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
public class VerifyCVProjectSection extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	String[] displayProjectArray;
	String[][] detailsArrayForVerification;
	int rows;
	int cols;
	String[ ][ ] projectDetails;
	Select projectStatusDD;
	
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
	public void verifyCVProjectSection(String Role) throws Exception
	{
		int numOfProjectPresentInGrid;
		int projectUnselectedCounter=0;
		String parentWindowHandle;		
		int noTestStepAvailableTagSize=0;
		String numberOfTextOnPieChart = null;
		
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
			        	 			|| Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Admin+Stakeholder") || Role.equalsIgnoreCase("Stakeholder+Version Lead"))
			        	 	{
			        	 		//click on Stakeholder dashboard page
								getElement("UAT_stakeholderDashboard_Id").click();
								Thread.sleep(1000);
			        	 	}
															
							//select All option from project status dropdown
							projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
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
												getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
												projectUnselectedCounter++;
											}
										}
										else
										{
											if(getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
											{
												getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
												projectUnselectedCounter++;
											}
										}
									}
									
								} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
							}
							
							if(projectUnselectedCounter>0)
							{
									//verify pie chart if no project selected
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
									
									//int pieChartCount = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.tagName("svg")).size();
									long pieChart = (long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg').size();");
									int pieChartCount = (int)pieChart;
									
									if(pieChartCount==0)
									{
										APP_LOGS.debug("Pie Chart not exist as no project is selected.");
										comments+="Pie Chart not exist as no project is selected.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Pie Chart exist though no project is selected");
										comments+="Pie Chart exist though no project is selected.(Fail) ";
										assertTrue(false);
									}
							}
							
							//go to first page
							goToFirstPageOfProject();
							
							ArrayList<String> projectsToBeVerified = new ArrayList<String>();							
							ArrayList<String> versionToBeVerified = new ArrayList<String>();
							
							//verify the pie chart for only one project						
							do
							{	
								numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
								
								for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
								{   									
									int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																		
									if(grpTDSize==6)
									{
										//if project name is selected unselect the project
										if(!getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).isSelected())
										{
											getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
											
											noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
											
											if(noTestStepAvailableTagSize!=1)
											{
												numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text').length"));
												
												if ( numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3") ) 
												{
														String projectNameFromGrid = getObject("StakeholderDashboardConsolidatedView_projectNameTD3Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD3Xpath2", x).getAttribute("title");														
														String versionFromGrid = getObject("StakeholderDashboardConsolidatedView_versionTD4Xpath1", "StakeholderDashboardConsolidatedView_versionTD4Xpath2", x).getText();													
														
														projectsToBeVerified.add(projectNameFromGrid);														
														versionToBeVerified.add(versionFromGrid);
														
														break;
												}
												else
												{
													getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
												}
												
											}
											else
											{
												getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
											}
										}
									}
									else
									{
										if(!getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
										{
											getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
											
											noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
											
											if(noTestStepAvailableTagSize!=1)
												{
													numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text').length"));
													
													if (numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3")) 
													{
															String projectNameFromGrid = getObject("StakeholderDashboardConsolidatedView_projectNameTD2Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD2Xpath2", x).getAttribute("title");														
															String versionFromGrid = getObject("StakeholderDashboardConsolidatedView_versionTD3Xpath1", "StakeholderDashboardConsolidatedView_versionTD3Xpath2", x).getText();													
															
															projectsToBeVerified.add(projectNameFromGrid);														
															versionToBeVerified.add(versionFromGrid);
															
															break;
													}
													else
													{
														getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
													}
											}
											else
											{
												getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
											}
										}									
									}
								}
								
								
							} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
							
							
							
							//verify if pie chart displays for selected project, if displays then verify values
							if((noTestStepAvailableTagSize!=1) && (!numberOfTextOnPieChart.equals("1")))
							{
									//create array to store selected project and version
									detailsArrayForVerification = new String[projectsToBeVerified.size()][6];
									
									for (int i = 0; i < detailsArrayForVerification.length; i++) 
									{
										detailsArrayForVerification[i][0] = projectsToBeVerified.get(i);								
										detailsArrayForVerification[i][1] = versionToBeVerified.get(i);								
									}
									
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
																}														
															}
															
															int passPercentage = Math.round( ( passCount / ( passCount + failCount + ncCount ) ) * 100 );													
															int failPercentage = Math.round( ( failCount / ( passCount + failCount + ncCount ) ) * 100 );													
															int ncPercentage = Math.round( ( ncCount / ( passCount + failCount + ncCount ) ) * 100 );
															
															//put those test pass pass/fail/nc test step count into array which contains test step>0
															detailsArrayForVerification[i][3] = String.valueOf(passPercentage);													
															detailsArrayForVerification[i][4] = String.valueOf(failPercentage);													
															detailsArrayForVerification[i][5] = String.valueOf(ncPercentage);													
															
															testPassCountToBeDisplayedOnDetailedView = 0;
															passCount = 0;
															failCount = 0;
															ncCount = 0;
														}
														else 
														{
															fail=true;													
															APP_LOGS.debug("'No Test Pass Available' text is displayed while pie chart is already displayed on stakeholder dashboard Detailed view page. Test case failed.");													
															comments += "\n- 'No Test Pass Available' text is displayed while pie chart is already displayed on stakeholder dashboard Detailed view page. (Fail)";													
															TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Pass Available' text is displayed while pie chart is already displayed on SD page.");
															
															throw new SkipException("'No Test Pass Available' text is displayed while pie chart is already displayed on stakeholder dashboard Detailed view page.... So Skipping all tests");
														}
														break;
													}											
												}										
												break;							
											}									
										}								
									}
									
									int allPassAddition = 0;
									int allFailAddition = 0;
									int allNCAddition = 0;
	
									for (int j = 0; j < detailsArrayForVerification.length; j++) 
									{
										allPassAddition += Integer.parseInt( detailsArrayForVerification[j][3] );								
										allFailAddition += Integer.parseInt( detailsArrayForVerification[j][4] );								
										allNCAddition += Integer.parseInt( detailsArrayForVerification[j][5] );										
									}
									
									//convert into percentage
									int collaborativePassPercentageInt = Math.round(  allPassAddition / detailsArrayForVerification.length);							
									int collaborativeFailPercentageInt = Math.round( allFailAddition / detailsArrayForVerification.length);							
									int collaborativeNCPercentageInt = Math.round(  allNCAddition / detailsArrayForVerification.length);	
									
									// If pass, Fail, NC percentage are same then PassPercenatge will be "PassPercenatge + 1" (Preference will be given to Pass) 
									if((collaborativePassPercentageInt == collaborativeFailPercentageInt ) && ( collaborativeFailPercentageInt == collaborativeNCPercentageInt)) 
									{								
										collaborativePassPercentageInt = collaborativePassPercentageInt + 1; 								
									}
															
									String collaborativePassPercentage = Integer.valueOf(collaborativePassPercentageInt).toString();							
									String collaborativeFailPercentage = Integer.valueOf(collaborativeFailPercentageInt).toString();							
									String collaborativeNCPercentage = Integer.valueOf(collaborativeNCPercentageInt).toString();
								
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
									
									//click on Stakeholder dashboard page
									getElement("UAT_stakeholderDashboard_Id").click();
									Thread.sleep(1000);
																							
									//select All option from project status dropdown	
									projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
									projectStatusDD.selectByValue("All");							
									Thread.sleep(500);
									
									//unselect all project								
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
													getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
													projectUnselectedCounter++;
												}
											}
											else
											{
												if(getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
												{
													getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
													projectUnselectedCounter++;
												}
											}
										}
										
									} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
									
									if(projectUnselectedCounter>0)
									{
										APP_LOGS.debug("All projects are unselected");													
									}
									
									//go to first page
									goToFirstPageOfProject();
									
									//verify the project name for which pie chart value need to be verified						
									do
									{	
										numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
										
										for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
										{   		
											int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
																				
											if(grpTDSize==6)
											{
												//verify project
												for (int m = 0; m < detailsArrayForVerification.length; m++) 
												{
													if(getObject("StakeholderDashboardConsolidatedView_projectNameTD3Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD3Xpath2", x).getAttribute("title").equals(detailsArrayForVerification[m][0])
															&& getObject("StakeholderDashboardConsolidatedView_versionTD4Xpath1", "StakeholderDashboardConsolidatedView_versionTD4Xpath2", x).getText().equals(detailsArrayForVerification[m][1]))
													{
														if(!getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).isSelected())
														{
															getObject("StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD3ChkBoxXpath2", x).click();
														}
														
														noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
														
														if(noTestStepAvailableTagSize!=1)
														{
															numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text').length"));
															
															if ( numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3") ) 
															{
																	break;
															}
															
														}
													}
												}
											}
											else
											{
												for (int m = 0; m < detailsArrayForVerification.length; m++) 
												{
													if(getObject("StakeholderDashboardConsolidatedView_projectNameTD2Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD2Xpath2", x).getAttribute("title").equals(detailsArrayForVerification[m][0])
															&& getObject("StakeholderDashboardConsolidatedView_versionTD3Xpath1", "StakeholderDashboardConsolidatedView_versionTD3Xpath2", x).getText().equals(detailsArrayForVerification[m][1]))
													{
														if(!getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).isSelected())
														{
															getObject("StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath1", "StakeholderDashboardConsolidatedView_projectTD2ChkBoxXpath2", x).click();
														}
														
														noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
																										
														if(noTestStepAvailableTagSize!=1)
														{
															numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChartDet').find('svg text').length"));
															
															if (numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3")) 
															{
																	break;
															}
															
														}
													}
												}
											}
										}
										
										
									} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
									
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
							else
							{
									APP_LOGS.debug("No Test Step Available or Pie chart contains only one value. Hence cannot verify pie chart values for selected project.");
									comments+="No Test Step Available or Pie chart contains only one value. Hence cannot verify pie chart values for selected project.(Pass) ";
							}
							
							
							//go to first page
							goToFirstPageOfProject();
							
							//code for clicking on project name to open in edit mode
							do
							{	
								displayProjectArray = new String[6];
								
								numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
								parentWindowHandle = eventfiringdriver.getWindowHandle();
								
								for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
								{   
									int i=1;
									int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
									
									if(grpTDSize==6)
									{												
			                			displayProjectArray[0]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
									}
									displayProjectArray[1]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
									displayProjectArray[2]= getObject("StakeholderDashboardConsolidatedView_projectNameWithLinkXpath1", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath2", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath3", x, i++).getAttribute("title");
									displayProjectArray[3]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
									displayProjectArray[4]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
									displayProjectArray[5]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
									
									//click on project										
									if(grpTDSize==6)
									{
										getObject("StakeholderDashboardConsolidatedView_projectNameTD3Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD3Xpath2", x).click();
										Thread.sleep(1000);
									}
									else
									{
										getObject("StakeholderDashboardConsolidatedView_projectNameTD2Xpath1", "StakeholderDashboardConsolidatedView_projectNameTD2Xpath2", x).click();
										Thread.sleep(1000);
									}
									
									//verify project entry in edit mode
									if(verifyProjectInEditMode(parentWindowHandle, displayProjectArray))
									{
										APP_LOGS.debug("Project '"+displayProjectArray[2]+"' is verified in edit mode.");
									}
									else
									{
										fail=true;
										APP_LOGS.debug("Project '"+displayProjectArray[2]+"' is not verified in edit mode.");
										comments+="Project '"+displayProjectArray[2]+"' is not verified in edit mode.(Fail) ";
										assertTrue(false);
									}
								}
								
							} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
							
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
	
		
	
	//common code opening project in new browser window
	 private boolean verifyProjectInEditMode(String  parentHandleName, String[] recievedArray)
	 {
		 try
		 {	
		     // call the method numberOfWindowsToBe as follows
		     //WebDriverWait wait = new WebDriverWait(eventfiringdriver, 15, 100);
		     wait.until(numberOfWindowsToBe(2));
		     Set<String> availableWindows = eventfiringdriver.getWindowHandles();//this set size is
		     
		     // returned as 1 and not 2
		      String newWindow = null;
		      for (String window : availableWindows) {
		           	  if (!parentHandleName.equals(window)) {
		              newWindow = window;
		          }
		      }

		      // switch to new window
		      eventfiringdriver.switchTo().window(newWindow);
		      eventfiringdriver.manage().window().maximize();
		      Thread.sleep(1000);
		      
		      //verify edit project heading visible, if yer verify project name
		      if(getObject("Projects_editProjectLink").isDisplayed())
		      {
			    	Select groupDropDown;
			  		String displayedGroup;
			  		Select portfolioDropDown;
			  		String displayedPortfolio;
			  		String displayedProject;
			  		String displayedVersion;			  		
			  		String selectedEndDate;
			  		String displayedVersionLead;
			  		
		    	  	// get the fields name  	  
		    	  	groupDropDown = new Select(getElement("ProjectCreateNew_groupDropDown_Id"));
					displayedGroup= groupDropDown.getFirstSelectedOption().getText();
					
					portfolioDropDown= new Select(getElement("ProjectCreateNew_PortfolioDropDown_Id"));
					displayedPortfolio= portfolioDropDown.getFirstSelectedOption().getText();
					
					//projectDropDown= new Select(getElement("ProjectCreateNew_projectDropDown_Id"));
					displayedProject= getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
					
					displayedVersion=getObject("ProjectCreateNew_versionTextField").getAttribute("value");
					
					selectedEndDate=getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");					
					
					displayedVersionLead=getObject("ProjectCreateNew_versionLeadDisplayField").getText();
					
					if(assertTrue(displayedGroup.equals(recievedArray[0])
							&& displayedPortfolio.equals(recievedArray[1])
							&& displayedProject.equals(recievedArray[2])
							&& displayedVersion.equals(recievedArray[3])
							&& selectedEndDate.replace('/', '-').equals(recievedArray[4])
							&& displayedVersionLead.equals(recievedArray[5])))
					{
						
							setImplicitWait(1);	
							
							//close the new window
							eventfiringdriver.close();	
							
							//switch to parent
							eventfiringdriver.switchTo().window(parentHandleName);
							
							return true;
					}
					else
					{		
							setImplicitWait(1);		
							
				      		//close the new window
				      		eventfiringdriver.close();	
				      		
				      		//switch to parent
				      		eventfiringdriver.switchTo().window(parentHandleName);
				      		
				      		return false;
					}
		      }
		      else
		      {
		    	  fail = true;
		    	  comments+="Edit Project link not available. Hence cannot verify project '"+recievedArray[2]+"' details in edit mode.(Fail). ";
		    	  APP_LOGS.debug("Edit Project link not available. Hence cannot verify project '"+recievedArray[2]+"' details in edit mode.");
		    	  assertTrue(false);
		    	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Project link not visible for '"+recievedArray[2]+"' project");
		      }
		      
		      setImplicitWait(1);
		      
		      //close the new window
		      eventfiringdriver.close();
		      
		      //switch to parent
		      eventfiringdriver.switchTo().window(parentHandleName);
		     
		 }
		 catch(Throwable t)
		 {
			  t.printStackTrace();
			  fail=true;
			  return false;
		 }
		 finally
		 {
			 resetImplicitWait();			 
		 }
		 
		 return false;
	 }
		 
		 
	 //code for no of windows 
	 private ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
	     return new ExpectedCondition<Boolean>() {
	       @Override
	       public Boolean apply(WebDriver driver) {
	        eventfiringdriver.getWindowHandles();
	         return eventfiringdriver.getWindowHandles().size() == numberOfWindows;
	       }
	     };
	  
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