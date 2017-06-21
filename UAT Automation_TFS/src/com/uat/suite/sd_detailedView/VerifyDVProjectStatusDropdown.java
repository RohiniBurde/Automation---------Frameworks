package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
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

public class VerifyDVProjectStatusDropdown extends TestSuiteBase  
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;	
	
	Select projectStatusDD;
	Select selectGroup ;
	Select selectPortfolio;
	
	int rows;
	int cols;
	static Object[ ][ ] projectDetails;
	
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
	public void testVerifyDVProjectStatusDropdown(String Role ) throws Exception
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
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				String totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
				
				int projectsPerPage;
			
				int totalNumOfProjectInt = Integer.parseInt(totalNumOfProject);
				
				rows = totalNumOfProjectInt;// total records
				cols = 7; // hardcoaded	
				
				projectDetails = new Object[rows][cols];
				
				ArrayList<String> testPassArrayList = new ArrayList<String>();
				
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
						
					
						String testPassNameToBeInArray = null; 
						
							
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
										
										testPassNameToBeInArray = getObject("TestSteps_testPassTitleSelected").getAttribute("title");
									
										testPassArrayList.add(testPassNameToBeInArray);
										
									}
									
								}
								
							}
							
						}
						
						
						String storeArraylist = "";

						for (int i = 0; i < testPassArrayList.size(); i++) 
						{
							String tempVariableForArraylist = testPassArrayList.get(i);
							
							storeArraylist += tempVariableForArraylist  + ", ";
							
						}

						projectDetails[projectDetailsRowIndex][6]= storeArraylist;
						
						
						/*System.out.println("Group "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][0]);
						System.out.println("Portfolio "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][1]);
						System.out.println("Project "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][2]);
						System.out.println("Version "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][3]);
						System.out.println("StartDate "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][4]);
						System.out.println("EndDate "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][5]);
						System.out.println("Test pass "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][6]);*/
							
						/*String[] testPassInAProject = (projectDetails[projectDetailsRowIndex][6].toString()).split(",");
						
						for (int k = 0; k < testPassInAProject.length; k++) 
						{
							System.out.println("testPassInAProject "+testPassInAProject[k]);
						}*/
						
												
						/*String[] eachElementInArraylist = storeArraylist.split(", ");
						
						for (int i = 0; i < eachElementInArraylist.length; i++) 
						{
							System.out.println("eachElementInArraylist "+i+"-"+eachElementInArraylist[i]);
						}*/
						
						
						testPassArrayList.clear();
						
						Thread.sleep(1000);
						
						getElement("TM_projectsTab_Id").click();
						
						Thread.sleep(2500);						
						
						projectDetailsRowIndex++;		
						
						
					}			
					
				} while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
				
				//System.out.println("Test pass "+0 +"- "+ projectDetails[0][6]);
				
				/*String[] testPassInAProject = (projectDetails[0][6].toString()).split(", ");
				
				for (int k = 0; k < testPassInAProject.length; k++) 
				{
					System.out.println("testPassInAProject "+testPassInAProject[k]);
				}*/
				
				
				
				getElement("UAT_stakeholderDashboard_Id").click();
				
				Thread.sleep(3000);
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));

				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				
				projectStatusDD.selectByIndex(0); // Select ALL from dropdown
				
				Thread.sleep(1000);
				
				if (compareStrings("All", projectStatusDD.getFirstSelectedOption().getText())) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						//Verifying Group Drop down elements
						
						int groupCount = 0;
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectDetails);
	
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
						
						if (compareIntegers(uniqueGroupArray.length, groupCount)) 
						{
							
							APP_LOGS.debug("All the Groups are displayed in Group Drp down.");
							
							comments += "\n- All the Groups are displayed in Group Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Groups are NOT displayed in Group Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Groups are NOT displayed in Group Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Groups are displayed in Group Drp down.");
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						
						int portfolioCount = 0;
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectDetails);
						
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
							
						
						if (compareIntegers(1, portfolioCount)) 
						{
							
							APP_LOGS.debug("All the Portfolio are displayed in Portfolio Drp down.");
							
							comments += "\n- All the Portfolio are displayed in Portfolio Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Portfolio are NOT displayed in Portfolio Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Portfolio are NOT displayed in Portfolio Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Portfolio are displayed in Portfolio Drp down.");
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
						
						
						if (compareIntegers(totalNumOfProjectInt, projectCount)) 
						{
							
							APP_LOGS.debug("All the Projects are displayed in Group Drp down.");
							
							comments += "\n- All the Projects are displayed in Group Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Projects are NOT displayed in Group Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Projects are NOT displayed in Group Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Projects are displayed in Group Drp down");
						}
						
						
						//Verifying Test Passes
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectDetails);
						
						
						int testPassCount = 0;
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								Thread.sleep(500);
								
								if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).getText();
											
											for (int l = 0; l < testPassToBeDisplayedInDetailedView.length; l++) 
											{
												
												if (testPassNameInGrid.equals(testPassToBeDisplayedInDetailedView[l][0])) 
												{
													testPassCount++;
												}
											}	
											
										}
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
								
							}
							
						}
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, testPassCount)) 
						{
							
							APP_LOGS.debug("All the Test passes are displayed in Grid.");
							
							comments += "\n- All the Test passes are displayed in Grid.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Test passes are NOT displayed in Grid.(Fail) ";
							
							APP_LOGS.debug("All the Test passes are NOT displayed in Grid. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Test passes are NOT displayed in Grid");
						}
						
				}
				else 
				{
					skip = true;
					
					APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'All' is Selected in Project Status Dropdown.");
					
					comments += "\n- 'No Groups' is displayed in Group Dropdown when 'All' is Selected in Project Status Dropdown. So, verification cannot be done and skipping the test.";
				}
					
					
					/*
					
					APP_LOGS.debug("Total count of project present on detailed view after selecting 'All' are:"+ projectCount);
					
					System.out.println("projectsDisplayedOnDetailedView "+projectCount);
				
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						int flagGroupPresent = 0;
						
						int flagPortfolioPresent = 0;
						
						//int flagProjectPresent = 0;
						
						int flagTestPassPresent = 0;
						
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
							// Check if the group fetched above is present in group array or not
							
							for (int j = 0; j < projectDetails.length; j++) 
							{
								
								if (groupNamePresentInGroupDD.equals(projectDetails[j][0])) 
								{
									flagGroupPresent = 1;	
									
									selectGroup.selectByVisibleText(groupNamePresentInGroupDD);
									
									for (int j2 = 1; j2 < selectPortfolio.getOptions().size(); j2++) 
									{
		
										String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(j2).getText();
										
										for (int k = 0; k < projectDetails.length; k++) 
										{
											
											if (portfolioNamePresentInPortfolioDD.equals(projectDetails[k][1])) 
											{
												
												flagPortfolioPresent = 1;
												
												selectPortfolio.selectByVisibleText(portfolioNamePresentInPortfolioDD);
													
												do
												{
		 											int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
												
													for (int l = 1; l < numOfProjectPresentInGrid; l++) 
													{
														
														String projectNameinDetailedView = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).getAttribute("title");
														
														boolean isProjectSelected = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).isSelected();
														
														for (int m = 0; m < projectDetails.length; m++) 
														{
															
															if (projectNameinDetailedView.equals(projectDetails[m][2]) && isProjectSelected) 
															{
															
																//flagProjectPresent = flagProjectPresent+1;
																
																
																if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
																{
																	do
																	{
																		int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
																		
																		for (int n = 1; n <= numberOfTestPassDisplayed; n++) 
																		{
																			
																			String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", n).getText();
																			
																			for (int o = 0; o < projectDetails.length; o++) 
																			{
																				
																				String[] testPassNamesPresentOn1IndexOfProjectDetailsArray = (projectDetails[o][6].toString()).split(",");
																				
																				
																				for (int p = 0; p < testPassNamesPresentOn1IndexOfProjectDetailsArray.length; p++) 
																				{
																					
																					if (testPassNamesPresentOn1IndexOfProjectDetailsArray[p].contains(testPassNameInGrid)) 
																					{
																						
																						flagTestPassPresent = 1;
																						
																						break;
																					}
																					
																				}
																				
																			}	
																			
																		}
																		
																	} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
																	
																}
																
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
							
							APP_LOGS.debug("All the Groups are displayed in Group Drp down.");
							
							comments += "\n- All the Groups are displayed in Group Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Groups are NOT displayed in Group Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Groups are NOT displayed in Group Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Groups are displayed in Group Drp down.");
						}
						
						if (compareIntegers(1, flagPortfolioPresent)) 
						{
							
							APP_LOGS.debug("All the Portfolio are displayed in Group Drp down.");
							
							comments += "\n- All the Portfolio are displayed in Group Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Portfolio are NOT displayed in Group Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Portfolio are NOT displayed in Group Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Portfolio are displayed in Group Drp down.");
						}
						
						
						if (compareIntegers(totalNumOfProjectInt, projectCount)) 
						{
							
							APP_LOGS.debug("All the Projects are displayed in Group Drp down.");
							
							comments += "\n- All the Projects are displayed in Group Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Projects are NOT displayed in Group Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Projects are NOT displayed in Group Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Projects are displayed in Group Drp down");
						}
						
						if (compareIntegers(1, flagTestPassPresent)) 
						{
							
							APP_LOGS.debug("All the Test passes are displayed in Grid.");
							
							comments += "\n- All the Test passes are displayed in Grid.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Test passes are NOT displayed in Grid.(Fail) ";
							
							APP_LOGS.debug("All the Test passes are NOT displayed in Grid. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Test passes are NOT displayed in Grid");
						}
						
						
					}*/
				}
				else 
				{
					fail=true;
					
					comments += "\n- All is not display selected in Project Status Dropdown after Selecting All.(Fail) ";
					
					APP_LOGS.debug("All is not display selected in Project Status Dropdown after Selecting All. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All is not display selected in Project Status Dropdown");	
				}
				
				
				Thread.sleep(1000);
				
				projectStatusDD.selectByIndex(1); // Select Active from dropdown
				
				Thread.sleep(1000);
				
				if (compareStrings("Active", projectStatusDD.getFirstSelectedOption().getText())) 
				{
					
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						String[][] projectStatusDetailsDisplayedWithActive = projectStatusInActive(projectDetails);
						
						//Verifying Group Drop down elements
						
						int groupCount = 0;
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectStatusDetailsDisplayedWithActive);
	
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
						
						
						if (compareIntegers(uniqueGroupArray.length, groupCount)) 
						{
							
							APP_LOGS.debug("All Active Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
						}
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						
						int portfolioCount = 0;
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectStatusDetailsDisplayedWithActive);
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectStatusDetailsDisplayedWithActive.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectStatusDetailsDisplayedWithActive[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						
						if (compareIntegers(1, portfolioCount)) 
						{
							
							APP_LOGS.debug("All Active Portfolio are displayed in Portfolio Drp down.");
							
							comments += "\n- All Active Portfolio are displayed in Portfolio Drp down.";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All the Active Portfolio are NOT displayed in Portfolio Drp down.(Fail) ";
							
							APP_LOGS.debug("All the Active Portfolio are NOT displayed in Portfolio Drp down. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Portfolio are displayed in Portfolio Drp down.");
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
						
						
						if (compareIntegers(projectStatusDetailsDisplayedWithActive.length , projectCount)) 
						{
							
							APP_LOGS.debug("All Active Projects are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Projects are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Projects are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Projects are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Active Projects are not displayed in grid");
						}
						
						
						
						//Verifying Test Passes
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectStatusDetailsDisplayedWithActive);
						
						int testPassCount = 0;
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								Thread.sleep(500);
								
								if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).getText();
											
											for (int l = 0; l < testPassToBeDisplayedInDetailedView.length; l++) 
											{
												
												if (testPassNameInGrid.equals(testPassToBeDisplayedInDetailedView[l][0])) 
												{
													
													testPassCount++;
													
												}
												
											}	
											
										}
										
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
								
							}
							
						}
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, testPassCount)) 
						{
							
							APP_LOGS.debug("All Active Test passes are displayed in Grid whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Test passes are displayed in Grid whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Test passes are NOT displayed in Grid whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Test passes are NOT displayed in Grid whose end date is equal to or greater than today's date . Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Active Test passes are NOT displayed in Grid");
						}
						
						
						
						
					/*	
					
					
					
					//Get the total number of projects present in the detailed view
					int projectsDisplayedOnDetailedView = 0;
					
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
									projectsDisplayedOnDetailedView++;
								}
							} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
							
						}
						
					}
					
					APP_LOGS.debug("Total count of project present on detailed view after selecting 'Active' are:"+ projectsDisplayedOnDetailedView);
					
					System.out.println("projectsDisplayedOnDetailedView "+projectsDisplayedOnDetailedView);
				
				
					
					for (int i = 0; i < projectStatusDetailsDisplayedWithActive.length; i++) 
					{
						System.out.println("Active Group :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][0]);
						System.out.println("Active portfolio :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][1]);
						System.out.println("Active Project :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][2]);
						System.out.println("Active Version :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][3]);
						System.out.println("Active Start Date :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][4]);
						System.out.println("Active End Date :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][5]);
						System.out.println("Active Test Pass :"+i+"- "+projectStatusDetailsDisplayedWithActive[i][6]);
					}
					

					
						int flagGroupPresent = 0;
						int flagPortfolioPresent = 0;
						//int flagProjectPresent = 0;
						int flagTestPassPresent = 0;
					
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
							// Check if the group fetched above is present in group array or not
							
							for (int j = 0; j < projectStatusDetailsDisplayedWithActive.length; j++) 
							{
								
								if (groupNamePresentInGroupDD.equals(projectStatusDetailsDisplayedWithActive[j][0])) 
								{
									flagGroupPresent = 1;	
									
									selectGroup.selectByVisibleText(groupNamePresentInGroupDD);
									
									for (int j1 = 1; j1 < selectPortfolio.getOptions().size(); j1++) 
									{
		
										String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(j1).getText();
										
										for (int k = 0; k < projectStatusDetailsDisplayedWithActive.length; k++) 
										{
											
											if (portfolioNamePresentInPortfolioDD.equals(projectStatusDetailsDisplayedWithActive[k][1])) 
											{
												
												flagPortfolioPresent = 1;
												
												selectPortfolio.selectByVisibleText(portfolioNamePresentInPortfolioDD);
													
												do
												{
		 											int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
												
													for (int l = 1; l < numOfProjectPresentInGrid; l++) 
													{
														
														String projectNameinDetailedView = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).getAttribute("title");
														
														boolean isProjectSelected = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).isSelected();
														
														for (int m = 0; m < projectStatusDetailsDisplayedWithActive.length; m++) 
														{
															
															if (projectNameinDetailedView.equals(projectStatusDetailsDisplayedWithActive[m][2]) && isProjectSelected) 
															{
															
															//	flagProjectPresent = flagProjectPresent+1;
																
																
																if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
																{
																	do
																	{
																		int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
																		
																		for (int n = 1; n <= numberOfTestPassDisplayed; n++) 
																		{
																			
																			String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", n).getText();
																			
																			for (int o = 0; o < projectStatusDetailsDisplayedWithActive.length; o++) 
																			{
																				
																				String[] testPassNamesPresentOn1IndexOfProjectDetailsArray = (projectStatusDetailsDisplayedWithActive[o][6].toString()).split(",");
																				
																				
																				for (int p = 0; p < testPassNamesPresentOn1IndexOfProjectDetailsArray.length; p++) 
																				{
																					
																					if (testPassNamesPresentOn1IndexOfProjectDetailsArray[p].contains(testPassNameInGrid)) 
																					{
																						
																						flagTestPassPresent = 1;
																						
																						break;
																					}
																					
																				}
																				
																			}	
																			
																		}
																		
																	} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
																	
																}
																
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
							
							APP_LOGS.debug("All Active Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
						}
						
						if (compareIntegers(1, flagPortfolioPresent)) 
						{
							
							APP_LOGS.debug("All Active Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
						}
						
						System.out.println("projectsDisplayedOnDetailedView ACTIVE: "+projectsDisplayedOnDetailedView);
						
						System.out.println("projectDetailsDisplayedInDetailedView.length  : "+projectStatusDetailsDisplayedWithActive.length );
						
						if (compareIntegers(projectStatusDetailsDisplayedWithActive.length , projectsDisplayedOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Active Projects are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Projects are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Projects are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Projects are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Projects are not displayed in grid");
						}
						
						if (compareIntegers(1, flagTestPassPresent)) 
						{
							
							APP_LOGS.debug("All Active Test passes are displayed in Grid whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Active Test passes are displayed in Grid whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Active Test passes are NOT displayed in Grid whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Active Test passes are NOT displayed in Grid whose end date is equal to or greater than today's date . Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Test passes are NOT displayed in Grid");
						}*/
						
					}
					else 
					{
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'Active' is Selected in Project Status Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'Active' is Selected in Project Status Dropdown. So, verification cannot be done.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- Active is not display selected in Project Status Dropdown after Selecting Active.(Fail) ";
					
					APP_LOGS.debug("Active is not display selected in Project Status Dropdown after Selecting Active. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Active is not display selected in Project Status Dropdown");
				}
				
				
				
				Thread.sleep(1000);
				
				projectStatusDD.selectByIndex(2); // Select Completed from dropdown
				
				Thread.sleep(1000);
				
				if (compareStrings("Completed", projectStatusDD.getFirstSelectedOption().getText())) 
				{
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )	 
					{
						
						String[][] projectStatusDetailsDisplayedWithCompleted = projectStatusInCompleted(projectDetails);
						
						//Verifying Group Drop down elements
						
						int groupCount = 0;
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectStatusDetailsDisplayedWithCompleted);
	
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
						
						
						if (compareIntegers(uniqueGroupArray.length, groupCount)) 
						{
							APP_LOGS.debug("All Completed Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Completed Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Completed Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Completed Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
						}
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						
						int portfolioCount = 0;
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectStatusDetailsDisplayedWithCompleted);
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectStatusDetailsDisplayedWithCompleted.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectStatusDetailsDisplayedWithCompleted[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						
						if (compareIntegers(1, portfolioCount)) 
						{
							
							APP_LOGS.debug("All Completed Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .");
							
							comments += "\n- All Completed Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Completed Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
							
							APP_LOGS.debug("All Completed Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
							
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
						
						
						if (compareIntegers(projectStatusDetailsDisplayedWithCompleted.length , projectCount)) 
						{
							
							APP_LOGS.debug("All Completed Projects are displayed in Group Drp down whose end date is one less than today's date");
							
							comments += "\n- All Completed Projects are displayed in Group Drp down whose end date is one less than today's date";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Completed Projects are NOT displayed in Group Drp down whose end date is one less than today's date(Fail) ";
							
							APP_LOGS.debug("All Completed Projects are NOT displayed in Group Drp down whose end date is one less than today's date Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Completed Projects are not displayed in grid");
						}
						
						
						
						//Verifying Test Passes
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectStatusDetailsDisplayedWithCompleted);
						
						int testPassCount = 0;
						
						for (int i = 0; i < selectGroup.getOptions().size(); i++) 
						{
							selectGroup.selectByIndex(i);
							
							Thread.sleep(500);
							
							for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
							{
								selectPortfolio.selectByIndex(j);
								
								Thread.sleep(500);
								
								if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
								{
									do
									{
										int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
										
										for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
										{
											
											String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).getText();
											
											for (int l = 0; l < testPassToBeDisplayedInDetailedView.length; l++) 
											{
												
												if (testPassNameInGrid.equals(testPassToBeDisplayedInDetailedView[l][0])) 
												{
													
													testPassCount++;
													
												}
												
											}	
											
										}
										
										
									} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
									
								}
								
							}
							
						}
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, testPassCount)) 
						{
							APP_LOGS.debug("All Completed Test passes are displayed in Grid whose end date is one less than today's date");
							
							comments += "\n- All Completed Test passes are displayed in Grid whose end date is one less than today's date";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Completed Test passes are NOT displayed in Grid whose end date is one less than today's date(Fail) ";
							
							APP_LOGS.debug("All Completed Test passes are NOT displayed in Grid whose end date is one less than today's date Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Test passes are NOT displayed in Grid");
						}
						
						
						
						
						
						/*
						
						
						
						
						
						
						Thread.sleep(1000);
						
						//Get the total number of projects present in the detailed view
						int projectsDisplayedOnDetailedView = 0;
						
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
										projectsDisplayedOnDetailedView++;
									}
								} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
								
							}
							
						}
						
						APP_LOGS.debug("Total count of project present on detailed view after selecting 'Completed' are:"+ projectsDisplayedOnDetailedView);
						
						System.out.println("projectsDisplayedOnDetailedView Completed"+projectsDisplayedOnDetailedView);
					
						
						
						for (int i = 0; i < projectStatusDetailsDisplayedWithCompleted.length; i++) 
						{
							System.out.println("Completed Group :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][0]);
							System.out.println("Completed portfolio :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][1]);
							System.out.println("Completed Project :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][2]);
							System.out.println("Completed Version :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][3]);
							System.out.println("Completed Start Date :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][4]);
							System.out.println("Completed End Date :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][5]);
							System.out.println("Completed Test Pass :"+i+"- "+projectStatusDetailsDisplayedWithCompleted[i][6]);
						}
						
						
						String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
						
						
						
							if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
						{
							int flagGroupPresent = 0;
							int flagPortfolioPresent = 0;
							//int flagProjectPresent = 0;
							int flagTestPassPresent = 0;
						
							for (int i = 0; i < selectGroup.getOptions().size(); i++) 
							{
								String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
								// Check if the group fetched above is present in group array or not
								
								for (int j = 0; j < projectStatusDetailsDisplayedWithCompleted.length; j++) 
								{
									
									if (groupNamePresentInGroupDD.equals(projectStatusDetailsDisplayedWithCompleted[j][0])) 
									{
										flagGroupPresent = 1;	
										
										selectGroup.selectByVisibleText(groupNamePresentInGroupDD);
										
										for (int j1 = 1; j1 < selectPortfolio.getOptions().size(); j1++) 
										{
			
											String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(j1).getText();
											
											for (int k = 0; k < projectStatusDetailsDisplayedWithCompleted.length; k++) 
											{
												
												if (portfolioNamePresentInPortfolioDD.equals(projectStatusDetailsDisplayedWithCompleted[k][1])) 
												{
													
													flagPortfolioPresent = 1;
													
													selectPortfolio.selectByVisibleText(portfolioNamePresentInPortfolioDD);
														
													do
													{
			 											int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
													
														for (int l = 1; l < numOfProjectPresentInGrid; l++) 
														{
															
															String projectNameinDetailedView = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).getAttribute("title");
															
															boolean isProjectSelected = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", l).isSelected();
															
															for (int m = 0; m < projectStatusDetailsDisplayedWithCompleted.length; m++) 
															{
																
																if (projectNameinDetailedView.equals(projectStatusDetailsDisplayedWithCompleted[m][2]) && isProjectSelected) 
																{
																
																	//flagProjectPresent = flagProjectPresent+1;
																	
																	
																	if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
																	{
																		do
																		{
																			int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
																			
																			for (int n = 1; n <= numberOfTestPassDisplayed; n++) 
																			{
																				
																				String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", n).getText();
																				
																				for (int o = 0; o < projectStatusDetailsDisplayedWithCompleted.length; o++) 
																				{
																					
																					String[] testPassNamesPresentOn1IndexOfProjectDetailsArray = (projectStatusDetailsDisplayedWithCompleted[o][6].toString()).split(",");
																					
																					
																					for (int p = 0; p < testPassNamesPresentOn1IndexOfProjectDetailsArray.length; p++) 
																					{
																						
																						if (testPassNamesPresentOn1IndexOfProjectDetailsArray[p].contains(testPassNameInGrid)) 
																						{
																							
																							flagTestPassPresent = 1;
																							
																							break;
																						}
																						
																					}
																					
																				}	
																				
																			}
																			
																		} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
																		
																	}
																	
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
								
								APP_LOGS.debug("All Completed Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .");
								
								comments += "\n- All Completed Groups are displayed in Group Drp down whose end date is equal to or greater than today's date .";
							}
							else 
							{
								fail=true;
								
								comments += "\n- All Completed Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
								
								APP_LOGS.debug("All Completed Groups are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
								
							}
							
							if (compareIntegers(1, flagPortfolioPresent)) 
							{
								
								APP_LOGS.debug("All Completed Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .");
								
								comments += "\n- All Completed Portfolio are displayed in Group Drp down whose end date is equal to or greater than today's date .";
							}
							else 
							{
								fail=true;
								
								comments += "\n- All Completed Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date .(Fail) ";
								
								APP_LOGS.debug("All Completed Portfolio are NOT displayed in Group Drp down whose end date is equal to or greater than today's date . Test case failed");
								
							}
							
	
							System.out.println("projectsDisplayedOnDetailedView completed : "+projectsDisplayedOnDetailedView);
							
							System.out.println("projectDetailsDisplayedInDetailedView.length "+projectStatusDetailsDisplayedWithCompleted.length);
							
							if (compareIntegers(projectStatusDetailsDisplayedWithCompleted.length , projectsDisplayedOnDetailedView)) 
							{
								
								APP_LOGS.debug("All Completed Projects are displayed in Group Drp down whose end date is one less than today's date");
								
								comments += "\n- All Completed Projects are displayed in Group Drp down whose end date is one less than today's date";
							}
							else 
							{
								fail=true;
								
								comments += "\n- All Completed Projects are NOT displayed in Group Drp down whose end date is one less than today's date(Fail) ";
								
								APP_LOGS.debug("All Completed Projects are NOT displayed in Group Drp down whose end date is one less than today's date Test case failed");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Projects are not displayed in grid");
							}
							
							if (compareIntegers(1, flagTestPassPresent)) 
							{
								
								APP_LOGS.debug("All Completed Test passes are displayed in Grid whose end date is one less than today's date");
								
								comments += "\n- All Completed Test passes are displayed in Grid whose end date is one less than today's date";
							}
							else 
							{
								fail=true;
								
								comments += "\n- All Completed Test passes are NOT displayed in Grid whose end date is one less than today's date(Fail) ";
								
								APP_LOGS.debug("All Completed Test passes are NOT displayed in Grid whose end date is one less than today's date Test case failed");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the Test passes are NOT displayed in Grid");
							}*/
					}
					else 
					{
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'Completed' is Selected in Project Status Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'Completed' is Selected in Project Status Dropdown. So, verification cannot be done.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- Completed is not display selected in Project Status Dropdown after Selecting Completed.(Fail) ";
					
					APP_LOGS.debug("Completed is not display selected in Project Status Dropdown after Selecting Completed. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Completed is not display selected in Project Status Dropdown");
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
	
	private String[][] projectStatusInActive(Object[][] projectArrayToSearchIn) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		String systemDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			projectEndDate = dateFormat.parse((String) projectArrayToSearchIn[i][5]);
			
			if (projectEndDate.compareTo(sysdateInDateFormat) >= 0) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
		
	}
	
	private String[][] projectStatusInCompleted(Object[][] projectArrayToSearchIn) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		String systemDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		/*Date startDateInDateFormat = dateFormat.parse(startDate);
		*/
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			projectEndDate = dateFormat.parse((String) projectArrayToSearchIn[i][5]);
			
			if (projectEndDate.compareTo(sysdateInDateFormat) < 0) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] = (String) projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
		
	}
	
	
	private String[][] getTestPassToBeDisplayed(Object[][] projectArrayToSearchIn) throws IOException
	{
		ArrayList<String> arrayListToStoreEachElementsWithoutBlank = new ArrayList<String>();
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			
			String testPassInArray = (String)projectArrayToSearchIn[i][6];
			
			
			if (!(testPassInArray.equals("No Test Pass")||testPassInArray.equals(""))) 
			{ 
				
				String[] eachElementInArraylist = (String[]) ((String) projectArrayToSearchIn[i][6]).split(", ");
				
				for (int j = 0; j < eachElementInArraylist.length; j++) 
				{
					arrayListToStoreEachElementsWithoutBlank.add(eachElementInArraylist[j]);
				}
				
			}
		}
	
		int requiredTestPassCount = arrayListToStoreEachElementsWithoutBlank.size();
		
		String[][] requiredTestPasses = new String[requiredTestPassCount][1];
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < requiredTestPasses.length; j++) 
		{
			
			requiredTestPasses[j][0] = arrayListToStoreEachElementsWithoutBlank.get(j);
			
		}
		
		
		return requiredTestPasses;
		
		
	}
	
	private String[] getUniqueGroupsFromArray(Object[][] arrayOfTheDetailsToBeGetFrom)
	{
		
		Set<String> setArray = new HashSet<>();
		
		String[] returnStringArray ;
		
		for (int i = 0; i < arrayOfTheDetailsToBeGetFrom.length; i++) 
		{
			setArray.add((String) arrayOfTheDetailsToBeGetFrom[i][0]);	
		}
		
		returnStringArray = new String[setArray.size()];
		
		int i = 0;
		
		Iterator<String> it = setArray.iterator();
		
		while (it.hasNext()) 
		{
			returnStringArray[i] = (String) it.next();
			
			i++;
		}
		
		return returnStringArray;
		
	}
	
	private String[] getUniquePortfolioFromArray(Object[][] arrayOfTheDetailsToBeGetFrom)
	{
		
		Set<String> setArray = new HashSet<>();
		
		String[] returnStringArray ;
		
		for (int i = 0; i < arrayOfTheDetailsToBeGetFrom.length; i++) 
		{
			setArray.add((String) arrayOfTheDetailsToBeGetFrom[i][1]);	
		}
		
		returnStringArray = new String[setArray.size()];
		
		int i = 0;
		
		Iterator<String> it = setArray.iterator();
		
		while (it.hasNext()) 
		{
			returnStringArray[i] = (String) it.next();
			
			i++;
		}
		
		return returnStringArray;
		
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
