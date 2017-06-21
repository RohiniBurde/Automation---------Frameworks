package com.uat.suite.sd_detailedView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
public class VerifyDVTestStepFilters extends TestSuiteBase  
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
	public void testVerifyDVTestStepFilters(String Role ) throws Exception
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
						
						Select selectRole = new Select(getElement("Reports_selectRole_Id"));
						
						
						ArrayList<String> roleArraylist  = new ArrayList<String>();
						
						//Array for the records for each tester with column TesterName, 
						
						String[][] testerArray = new String[ ( listOfTester.size() - 1 ) ][5];			
						
						for (int i = 0; i < testerArray.length ; i++) 
						{
							testerArray[i][0] = listOfTester.get(i+1).getText();
							
							selectTester.selectByIndex(i+1);
							
							//Adding Role for the tester
							for (int j = 1; j < selectRole.getOptions().size(); j++) 
							{
								roleArraylist.add( selectRole.getOptions().get(j).getText() );
							}
							
							String storeArraylist = "";
							
							for (int j = 0; j < roleArraylist.size(); j++) 
							{
								String tempVariableForArraylist = roleArraylist.get(j);
								
								storeArraylist += tempVariableForArraylist  + ", ";
								
							}
		
							testerArray[i][1] = storeArraylist;//Role added
							roleArraylist.clear();
							
							//Adding Pass/Fail/NC unique test case names for each tester
							
							for (int j = 1; j < selectStatus.getOptions().size(); j++) 
							{	
								
								testerArray[i][j+1] = "";
								
								selectStatus.selectByIndex(j);
								
								getObject("Report_GoButton").click();
								
								int tablePresence = eventfiringdriver.findElements(By.xpath("//div[@id='testStepDetails']/table")).size();
								
								if ( tablePresence == 1) 
								{
									Set<String> testCaseSet = new HashSet<>();
									
									do 
									{
										List<WebElement> totalTestCasesDisplayed = eventfiringdriver.findElements(By.xpath("//div[@id = 'testStepDetails']/table/tbody/tr"));
										
										for (int k = 1; k <= totalTestCasesDisplayed.size(); k++) 
										{
											
											testCaseSet.add( getObject("Reports_testCaseName1", "Reports_testCaseName2", k).getAttribute("title") );	
											
										}	
										
									} while (ifElementIsClickableThenClick("Reports_testStepGridNextLink"));
									
									Iterator<String> it = testCaseSet.iterator();
									
									while (it.hasNext()) 
									{
										testerArray[i][j+1] += (String) it.next() + ", ";							
										
									}
									
								}
								
							}
							
						}
						
						for (int j = 0; j < testerArray.length; j++) 
						{
						
							System.out.println("testerArray testerName "+testerArray[j][0]);
							System.out.println("testerArray Role "+testerArray[j][1]);
							System.out.println("testerArray TCpass "+testerArray[j][2]);
							System.out.println("testerArray TCfail "+testerArray[j][3]);
							System.out.println("testerArray TCnc "+testerArray[j][4]);
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
						
						
						
						
						
						
						
						
						
						
						
						//Loop for Total Bar graphs displayed 
						
						/*ArrayList<String> testerNameUnderBarGraphForFail = new ArrayList<String>();
						
						do 
						{
							
							String totalBarGraphsDisplayedInString = Long.toString( (Long) eventfiringdriver.executeScript("return $('#testerChart').find('svg text title').length ") );
							
							int totalBarGraphsDisplayedForFail = Integer.parseInt( totalBarGraphsDisplayedInString );
							
							int totalBarGraphsDisplayed = Integer.parseInt( totalBarGraphsDisplayedInString );
							
							for (int j = 0; j < totalBarGraphsDisplayed; j++) 
							{
								String testerNameDisplayedUnderBarGraphs = (String) eventfiringdriver.executeScript("return $('#testerChart').find('svg text:eq("+j+") tspan').text() ");
								
								
								testerNameUnderBarGraphForFail.add(testerNameDisplayedUnderBarGraphs);
							}
							
							
						} while (ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));*/
						
						
						/*selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
						selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
						selectGroup.selectByIndex(5);
						selectPortfolio.selectByIndex(1);
						getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", 4).click();*/
						
							for (int i = 1; i <= 3; i++) 
						{
							String barGraphFillColor = (String)eventfiringdriver.executeScript("return document.getElementById('testerChart').children[1].getElementsByTagName('path')["+i+"].getAttribute('fill')");
							
							System.out.println(barGraphFillColor);
							
							WebElement barGraphFillColorClick;
							
							if(barGraphFillColor.equals("#008000"))
							{
								//Get the color of the Bar Graph
									barGraphFillColorClick = (WebElement)eventfiringdriver.executeScript("return document.getElementById('testerChart').children[1].getElementsByTagName('path')[1]");
								//Click on the color
									barGraphFillColorClick.click();
							}
							
							if(barGraphFillColor.equals("#ff0000"))
							{
								barGraphFillColorClick = (WebElement)eventfiringdriver.executeScript("return document.getElementById('testerChart').children[1].getElementsByTagName('path')[2]");
								
								barGraphFillColorClick.click();
							}
							
							if (barGraphFillColor.equals("#ffa500")) 
							{
								//Get the color of the Bar Graph
									barGraphFillColorClick = (WebElement)eventfiringdriver.executeScript("return document.getElementById('testerChart').children[1].getElementsByTagName('path')[3]");
								//Click on the color
									barGraphFillColorClick.click();
							}
							
						}
						
						int numberOfTrBeforeUncheck = 0;
						
						int numberOfTrInTestStepTable;
						
						do 
						{
							numberOfTrInTestStepTable = eventfiringdriver.findElements(By.xpath("//div[@id='tblData']/table/tbody/tr")).size();
							
							numberOfTrBeforeUncheck += numberOfTrInTestStepTable;
								
							System.out.println("numberOfTrBeforeUncheck "+numberOfTrBeforeUncheck);
								
						}  while (ifElementIsNotDisableThenClick("SDDetailedView_testStepGridNextButton_Id"));	
						 
						
						 getObject("filterLogoXpathTC").click();
							
						 Thread.sleep(1000);
						
						 getObject("noneLinkXpathTC").click();
							
						 Thread.sleep(1000);
							
						 getObject("OkButtonofFilterTC").click();
							
						 Thread.sleep(1000);
						
						 String popupText = getElement("popUpTextXpathTC_Id").getText();
							
						 System.out.println("Popup Text: "+popupText);
							
						 if (compareStrings("Atleast one filter item should be selected!", popupText)) 
						 {
							APP_LOGS.debug("'Atleast one filter item should be selected!' is displayed when no checkbox is selected.");
								
							comments += "\n- 'Atleast one filter item should be selected!' is displayed when no checkbox is selected.";
							
								
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("'Atleast one filter item should be selected!' is NOT displayed when no checkbox is selected. (Fail)");
							
							comments += "\n- 'Atleast one filter item should be selected!' is displayed when no checkbox is selected. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Atleast one filter item should be selected!' popup Text is NOT displayed");
							
						
						}
							
						 getObject("popupOkButton").click();
						
						
						 int numberOfCheckBoxPresent = eventfiringdriver.findElements(By.xpath("//ul[@id = 'ulItemsdvTestCasesID']/div/li")).size();
						
						 
						 if (numberOfCheckBoxPresent > 1) 
						 {
							
							getObject("firstChkBoxOnFilterXpath").click();
							
							getObject("OkButtonofFilter").click();//see this MAy create prob ...delete
							
							Thread.sleep(1000);
							
							int numberOfTrAfterUncheck = 0;
							
							do 
							{
								numberOfTrInTestStepTable = eventfiringdriver.findElements(By.xpath("//div[@id='tblData']/table/tbody/tr")).size();
								
								numberOfTrAfterUncheck += numberOfTrInTestStepTable;
								
							}  while (ifElementIsNotDisableThenClick("SDDetailedView_testStepGridNextButton_Id"));	
							
							int numberOfTrWhenOneCheckBoxIsChecked = numberOfTrAfterUncheck;
							
							//Click on ALL, Verify the number of checkboxes present.. if one then dont Click .. if more than 1 then Click first and Click ok .. 
							//and check whether number of rows before != to newer one 
							//Click on Filter logo
							
							getObject("filterLogoXpath").click();
							
							Thread.sleep(1000);
							
							//Click on ALL, so that all checkBoxes Get checked 
							getObject("allLinkOnFilter").click();
							
							//Click on first Test Case to uncheck the textBox 
							getObject("firstChkBoxOnFilterXpath").click();
							//Click on OK Button	
							
							getObject("OkButtonofFilter").click();//see this MAy create prob ...delete
							
							Thread.sleep(2000);
							
							//Get the Number of TR present after uncheck 
							
							do 
							{
								numberOfTrInTestStepTable = eventfiringdriver.findElements(By.xpath("//div[@id='tblData']/table/tbody/tr")).size();
								
								numberOfTrAfterUncheck = numberOfTrInTestStepTable;
								
							} while (ifElementIsNotDisableThenClick("SDDetailedView_testStepGridNextButton_Id"));	
							
							//number Of Tr Before Uncheck should not be same as after
							System.out.println("numberOfTrAfterUncheck "+numberOfTrAfterUncheck);
							
							System.out.println("numberOfTrBeforeUncheck "+ numberOfTrBeforeUncheck);
							
							if (compareIntegers((numberOfTrBeforeUncheck - numberOfTrWhenOneCheckBoxIsChecked), numberOfTrAfterUncheck)) 
							{
								
								APP_LOGS.debug("Total Entries displayed in table after Unchecking any checkbox is correct.");
								
								comments += "\n- Total Entries displayed in table after Unchecking any checkbox is correct.";
								
									
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("Total Entries displayed in table after Unchecking any checkbox is NOT correct. (Fail)");
								
								comments += "\n- Total Entries displayed in table after Unchecking any checkbox is NOT correct. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total Entries displayed in table after Unchecking any checkbox is NOT correct.");
								
							
							}
							
							//Click on Filter logo
							
							getObject("filterLogoXpath").click();
							
							Thread.sleep(1000);
							//Verify whether "Clear Filter" link is present and Click on it
						
							String clearFilterText = getObject("clearFilterLinkXpath").getText();
							
							if (compareStrings( "Clear Filter", clearFilterText))
							{
								APP_LOGS.debug("'Clear Filter' link is displayed after filter is applied.");
								
								comments += "\n- 'Clear Filter' link is displayed after filter is applied.";
								
									
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("'Clear Filter' link is NOT displayed after filter is applied. (Fail)");
								
								comments += "\n- 'Clear Filter' link is NOT displayed after filter is applied. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Clear Filter' link is NOT displayed after filter is applied.");
								
							
							}
							//Click on Clear Filter
							
							Thread.sleep(2000);
		
							getObject("clearFilterLinkXpath").click();
							
							int numberOfTrAfterClearFilterClick = 0 ;  
							do 
							{
								numberOfTrInTestStepTable = eventfiringdriver.findElements(By.xpath("//div[@id='tblData']/table/tbody/tr")).size();
								
								numberOfTrAfterClearFilterClick += numberOfTrInTestStepTable;
								
							} while (ifElementIsNotDisableThenClick("SDDetailedView_testStepGridNextButton_Id"));	
						
						
							if (compareIntegers(numberOfTrBeforeUncheck , numberOfTrAfterClearFilterClick)) 
							{
								APP_LOGS.debug("Total entries displayed after click on 'Clear Filter' is correct.");
								
								comments += "\n- Total entries displayed after click on 'Clear Filter' is correct.";
								
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("Total entries displayed after click on 'Clear Filter' is NOT correct. (Fail)");
								
								comments += "\n- Total entries displayed after click on 'Clear Filter' is NOT correct. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total entries displayed after click on 'Clear Filter' is NOT correct.");
								
							
							}
							
						 }
						 else 
						 {
							getObject("cancelButtonofFilterTC").click();
						 }
						
						 //Add Role Filter Testing After Confirmation
						
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
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
