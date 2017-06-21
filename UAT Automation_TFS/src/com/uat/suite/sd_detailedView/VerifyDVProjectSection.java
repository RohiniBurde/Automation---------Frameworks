package com.uat.suite.sd_detailedView;

import java.util.ArrayList;
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

public class VerifyDVProjectSection extends TestSuiteBase  
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
	
	String collaborativePassPercentage;
	String collaborativeFailPercentage;
	String collaborativeNCPercentage;
	
	String[][] detailsArrayForVerification;
	
	int rows;
	int cols;
	static String[ ][ ] projectDetails;
	
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
				
				//click on Stakeholder dashboard page
			
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				
				selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));

				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )
				{
				
					boolean isTestStepAvailableDisplayed = true;
					
					ArrayList<String> projectsToBeVerified = new ArrayList<String>();
					
					ArrayList<String> versionToBeVerified = new ArrayList<String>();
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						int breakCount = 0;
						
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
						{
							selectPortfolio.selectByIndex(j);
							
							Thread.sleep(1000);
							
							int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
							
							defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
							
							
							if (!(defaultGroupSelected.equalsIgnoreCase("No Group") || noTestStepAvailableTagSize == 1 ) )
							{
								isTestStepAvailableDisplayed = false;
	
								String numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChart').find('svg text').length"));
								
								System.out.println("numberOfTextOnPieChart "+numberOfTextOnPieChart);
								
								//Get the number of Texts on Pie chart and if it is more than 1 then get All the projects in an array
								if ( numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3") ) 
								{
									
									do
									{
										int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
									
										for (int k = 1; k <= numOfProjectPresentInGrid; k++) 
										{
											String projectNameFromGrid = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k).getAttribute("title");
										
											String versionFromGrid = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", k).getAttribute("title");
										
											projectsToBeVerified.add(projectNameFromGrid);
											
											versionToBeVerified.add(versionFromGrid);
											
											break;
										}
										
									} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
									
									breakCount = 1;
									
									break;
								}
								
							}
							
						}
						if (breakCount == 1) 
						{
							break;
						}
						
					}
					
					
					if (isTestStepAvailableDisplayed) 
					{
						APP_LOGS.debug("Group is present but No Proper Data is displayed to be verified (for default selection). So Skipping all the tests.");
						
						comments += "\n- Group is present but No Proper Data is displayed to be verified (for default selection). So Skipping all the tests.";
						
						throw new SkipException("Group is present but No Proper Data is displayed to be verified (for default selection). So Skipping all the tests.");
					}
					
					
					for (int j = 0; j < projectsToBeVerified.size(); j++) 
					{
						System.out.println(projectsToBeVerified.get(j));	
						
						System.out.println(versionToBeVerified.get(j));	
						
						
					}
					
					detailsArrayForVerification = new String[projectsToBeVerified.size()][6];
					
					for (int i = 0; i < detailsArrayForVerification.length; i++) 
					{
						detailsArrayForVerification[i][0] = projectsToBeVerified.get(i);
						
						detailsArrayForVerification[i][1] = versionToBeVerified.get(i);
						
					}
					
					for (int i = 0; i < detailsArrayForVerification.length; i++) 
					{
						System.out.println("detailsArrayForVerification[i][0] "+ detailsArrayForVerification[i][0]);
						
						System.out.println("detailsArrayForVerification[i][1] "+ detailsArrayForVerification[i][1]);
					}
					
					
					
					closeBrowser();
					
					openBrowser();
					
					isLoginSuccess = login("Stakeholder+Admin");
					
					if(isLoginSuccess)
					{
					
					
						getElement("UAT_dashboard_Id").click();
						
						getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
						
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
											
											
											if ( ! ( selectTestPass.getFirstSelectedOption().getText().equals("No Test Pass Available") ) ) 
											{
												List<WebElement> numberOfTestPass = getElement("Dashboard_testPassDD_ID").findElements(By.tagName("option"));
												
											//	detailsArrayForVerification[i][2] = String.valueOf(testPassCountToBeDisplayedOnDetailedView);
												
												
												for (int l = 0; l < numberOfTestPass.size(); l++) 
												{
													
													numberOfTestPass.get(l).click();
													
													String totalTestCases = getElement("DetailedAnalysis_DescriptionTableTotalTestCase_Id").getText();
													
													String totalTestSteps = getElement("DetailedAnalysis_DescriptionTableTotalTS_Id").getText();
													
													if ( Integer.parseInt(totalTestCases) > 0 && Integer.parseInt(totalTestSteps) > 0)  
													{
														
														
														String testStepPassedText = getElement("DetailedAnalysis_testStepPassedText_Id").getText().substring(18);
														
														passCount = passCount + Integer.parseInt(testStepPassedText);
														
														
														String testStepFailedText = getElement("DetailedAnalysis_testStepFailedText_Id").getText().substring(18);
														
														failCount = failCount + Integer.parseInt(testStepFailedText);
														
														
														String testStepNCText = getElement("DetailedAnalysis_testStepNCText_Id").getText().substring(14);
														
														ncCount = ncCount + Integer.parseInt(testStepNCText);
														
														testPassCountToBeDisplayedOnDetailedView += 1;
														
													}
													
												}
												
												
												
												
												detailsArrayForVerification[i][2] = String.valueOf(testPassCountToBeDisplayedOnDetailedView);
												
												int passPercentage = Math.round( ( passCount / ( passCount + failCount + ncCount ) ) * 100 );
												
												int failPercentage = Math.round( ( failCount / ( passCount + failCount + ncCount ) ) * 100 );
												
												int ncPercentage = Math.round( ( ncCount / ( passCount + failCount + ncCount ) ) * 100 );
												
												detailsArrayForVerification[i][3] = String.valueOf(passPercentage);
												
												detailsArrayForVerification[i][4] = String.valueOf(failPercentage);
												
												detailsArrayForVerification[i][5] = String.valueOf(ncPercentage);
												
												
												testPassCountToBeDisplayedOnDetailedView = 0;
												passCount = 0;
												failCount = 0;
												ncCount = 0;
												
												System.out.println("testPassCountToBeDisplayedOnDetailedView "+ testPassCountToBeDisplayedOnDetailedView);
												
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
						
						
						for (int k = 0; k < detailsArrayForVerification.length; k++) 
						{
							System.out.println("detailsArrayForVerification["+k+"][0] Project "+ detailsArrayForVerification[k][0]);	
							System.out.println("detailsArrayForVerification["+k+"][1] version "+ detailsArrayForVerification[k][1]);	
							System.out.println("detailsArrayForVerification["+k+"][2] testPassCount "+ detailsArrayForVerification[k][2]);	
							System.out.println("detailsArrayForVerification["+k+"][3] pass% "+ detailsArrayForVerification[k][3]);
							System.out.println("detailsArrayForVerification["+k+"][4] fail% "+ detailsArrayForVerification[k][4]);
							System.out.println("detailsArrayForVerification["+k+"][5] nc% "+ detailsArrayForVerification[k][5]);
						
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
						
						int collaborativePassPercentageInt = Math.round(  allPassAddition / detailsArrayForVerification.length  );
						
						int collaborativeFailPercentageInt = Math.round( allFailAddition / detailsArrayForVerification.length  );
						
						int collaborativeNCPercentageInt = Math.round(  allNCAddition / detailsArrayForVerification.length  );
					
						
						// If pass, Fail, NC percentage are same then PassPercenatge will be "PassPercenatge + 1" (Preference will be given to Pass) 
						/*if ( ( collaborativePassPercentageInt == collaborativeFailPercentageInt ) &&
								( collaborativeFailPercentageInt == collaborativeNCPercentageInt ) ) 
						{
							
							collaborativePassPercentageInt = collaborativePassPercentageInt + 1; 
							
						}*/
						
						// If pass, Fail, NC percentage are same then PassPercenatge will be "PassPercenatge + 1" (Preference will be given to Pass) 
			              if( ( (collaborativePassPercentageInt == collaborativeFailPercentageInt) && (collaborativeFailPercentageInt == collaborativeNCPercentageInt) ) ||
			                    (collaborativePassPercentageInt + collaborativeFailPercentageInt + collaborativeNCPercentageInt == 99) ||
			                    (collaborativePassPercentageInt + collaborativeFailPercentageInt == 99) ||
			                    (collaborativePassPercentageInt + collaborativeNCPercentageInt == 99)) 
			              {   
			            	  
			            	  collaborativePassPercentageInt = collaborativePassPercentageInt + 1;         
			              
			              }
			              else if( collaborativeFailPercentageInt + collaborativeNCPercentageInt == 99 )
			              {
			            	  
			            	  collaborativeFailPercentageInt = collaborativeFailPercentageInt + 1;
			              
			              }
			              
					
						collaborativePassPercentage = Integer.valueOf(collaborativePassPercentageInt).toString();
						
						collaborativeFailPercentage = Integer.valueOf(collaborativeFailPercentageInt).toString();
						
						collaborativeNCPercentage = Integer.valueOf(collaborativeNCPercentageInt).toString();
					
						
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
								
								Thread.sleep(1000);
								
								int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
								
								defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
								
								
								if (!(defaultGroupSelected.equalsIgnoreCase("No Group") || noTestStepAvailableTagSize == 1 ) )
								{
		
									String numberOfTextOnPieChart = Long.toString((Long)eventfiringdriver.executeScript("return $('#pieChart').find('svg text').length"));
									
									if ( numberOfTextOnPieChart.equals("2") || numberOfTextOnPieChart.equals("3") ) 
									{
										
										breakCount = 1;
										
										break;
									}
									
								}
								
							}
		
							if (breakCount == 1) 
							{
								break;
							}
						
						}
						String collaborativePassPercentageOnPieChart = (String) eventfiringdriver.executeScript( "return $('#pieChart').find('svg text:eq(0) tspan').text()" ) ;
						
						//String collaborativePassPercentageOnPieChart = Long.toString( (Long) eventfiringdriver.executeScript( "return $('#pieChart').find('svg text:eq(0) tspan').text()" ) );
						
						String collaborativeFailPercentageOnPieChart = (String) eventfiringdriver.executeScript( "return $('#pieChart').find('svg text:eq(2) tspan').text()" );
						
						String collaborativeNCPercentageOnPieChart = (String) eventfiringdriver.executeScript( "return $('#pieChart').find('svg text:eq(1) tspan').text()" ) ;
						
						
						if ( compareStrings( collaborativePassPercentage , collaborativePassPercentageOnPieChart ) )
						{
							
							APP_LOGS.debug("On Selection of all Projects, 'Pass' percentage displayed on Pie Chart is Correct.");
							
							comments += "\n- On Selection of all Projects, 'Pass' percentage displayed on Pie Chart is Correct.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On Selection of all Projects, 'Pass' percentage displayed on Pie Chart is NOT Correct.");
							
							comments += "\n- On Selection of all Projects, 'Pass' percentage displayed on Pie Chart is NOT Correct. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Collaborative 'Pass' percentage on Pie Chart is NOT Correct");
							
							throw new SkipException("Collaborative 'Pass' percentage on Pie Chart is NOT Correct.... So Skipping all tests");
							
						}
						
						
						if ( compareStrings( collaborativeFailPercentage , collaborativeFailPercentageOnPieChart ) )
						{
							APP_LOGS.debug("On Selection of all Projects, 'Fail' percentage displayed on Pie Chart is Correct.");
							
							comments += "\n- On Selection of all Projects, 'Fail' percentage displayed on Pie Chart is Correct.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On Selection of all Projects, 'Fail' percentage displayed on Pie Chart is NOT Correct.");
							
							comments += "\n- On Selection of all Projects, 'Fail' percentage displayed on Pie Chart is NOT Correct. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Collaborative 'Fail' percentage on Pie Chart is NOT Correct");
							
							throw new SkipException("Collaborative 'Fail' percentage on Pie Chart is NOT Correct.... So Skipping all tests");
							
						}
						
						
						if ( compareStrings( collaborativeNCPercentage , collaborativeNCPercentageOnPieChart ) )
						{
							APP_LOGS.debug("On Selection of all Projects, 'Not Completed' percentage displayed on Pie Chart is Correct.");
							
							comments += "\n- On Selection of all Projects, 'Not Completed' percentage displayed on Pie Chart is Correct.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On Selection of all Projects, 'Not Completed' percentage displayed on Pie Chart is NOT Correct.");
							
							comments += "\n- On Selection of all Projects, 'Not Completed' percentage displayed on Pie Chart is NOT Correct. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Collaborative 'NC' percentage on Pie Chart is NOT Correct");
						
							throw new SkipException("Collaborative 'Not Completed' percentage on Pie Chart is NOT Correct.... So Skipping all tests");
						}
						
						
						// uncheck all project and then check one project to verify pie chart values
						
						do
						{
							int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
						
							for (int i = 1; i <= numOfProjectPresentInGrid; i++) 
							{
								getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", i).click();
							}
							
						} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));
						
						
						
						
						/*try 
						{
							
							
							if(getObject("AttachmentsViewAll_PreviousLinkEnabled").isEnabled())
							{
								
								getObject("SDDetailedView_firstPageOfPagination").click();
							
							}
						
						}
						catch (Exception e) 
						{
							APP_LOGS.debug("Project Section Pagination is not enabled.");
						}*/
						
						if ( ! ( getElement("SDDetailedView_projectPagination_Id").findElements(By.xpath("div/span")).size() == 3 ) ) 
						{
							getObject("SDDetailedView_firstPageOfPagination").click();
							
						}
						
						
						int successCount = 0;
						
						do 
						{
							
							int totalTestPassDisplayed = 0;
									
							for (int i = 0; i < detailsArrayForVerification.length; i++) 
							{
								int checkBoxIndex = i+1;
								
								getObject("SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", checkBoxIndex ).click();
								
								if (detailsArrayForVerification[i][3].equals("0")) 
								{
									detailsArrayForVerification[i][3] = "";
								}
								
								if (detailsArrayForVerification[i][4].equals("0")) 
								{
									detailsArrayForVerification[i][4] = "";
								}
								
								if (detailsArrayForVerification[i][5].equals("0")) 
								{
									detailsArrayForVerification[i][5] = "";
								}
								
								
								
								int noTestStepAvailableTagSize = eventfiringdriver.findElements(By.xpath("//div[@id = 'pieChart']/div/b")).size();
								
								if ( ! ( noTestStepAvailableTagSize == 1 ) )
								{
									int svgCounter =0;
									String individualPassPercentageOnPieChart;
									String individualNCPercentageOnPieChart;
									String individualFailPercentageOnPieChart;
									
									if (detailsArrayForVerification[i][3].equals("")) 
									{
										individualPassPercentageOnPieChart = "";							
									}
									else
									{
										String temporaryVariable = "return $('#pieChart').find('svg text:eq("+(svgCounter++)+") tspan').text()";
										
										individualPassPercentageOnPieChart = (String) eventfiringdriver.executeScript( temporaryVariable ) ;
										
									}	
									
									
									if (detailsArrayForVerification[i][5].equals("")) 
									{
										individualNCPercentageOnPieChart= "";
									}
									else
									{
										String temporaryVariable = "return $('#pieChart').find('svg text:eq("+(svgCounter++)+") tspan').text()";
										
										individualNCPercentageOnPieChart = (String) eventfiringdriver.executeScript( temporaryVariable ) ;
										
									}
									
									
									if (detailsArrayForVerification[i][4].equals("")) 
									{
										individualFailPercentageOnPieChart= "";
									}
									else
									{
										String temporaryVariable = "return $('#pieChart').find('svg text:eq("+(svgCounter++)+") tspan').text()";
										
										individualFailPercentageOnPieChart = (String) eventfiringdriver.executeScript( temporaryVariable ) ;
										
									}
									
									//getting the test Pass Count
									if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
									{
										
										do 
										{
											int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
											
											totalTestPassDisplayed += numberOfTestPassDisplayed;	
										
											Thread.sleep(1000);
											
										} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
										
										
									}
									
									System.out.println("totalTestPassDisplayed "  + totalTestPassDisplayed);
									
									if (  compareStrings(detailsArrayForVerification[i][3], individualPassPercentageOnPieChart)  && 
										  compareStrings(detailsArrayForVerification[i][4], individualFailPercentageOnPieChart) &&  
										  compareStrings(detailsArrayForVerification[i][5], individualNCPercentageOnPieChart) &&
										  compareStrings(detailsArrayForVerification[i][2], Integer.valueOf( totalTestPassDisplayed ).toString()) ) 
									{
										
										successCount++ ;
										
										totalTestPassDisplayed = 0;
									}
									else 
									{
										successCount = 0;
										
										break;
									}
									
								}
								
								getObject( "SDDetailedView_projectCheckBoxInGrid1", "SDDetailedView_projectCheckBoxInGrid2", checkBoxIndex ).click(); //unchecking the checked Check box
								
							}
							
							
						} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));
						
						
						
						
						if (compareIntegers(detailsArrayForVerification.length, successCount)) 
						{
							APP_LOGS.debug("On selection of individual project, status pie chart has changed accordingly. Pass/Fail/Not Completed Percentage displayed is Correct. The test pass section is filled with all the test passes of selected project.");
							
							comments += "\n- On selection of individual project, status pie chart has changed accordingly. Pass/Fail/Not Completed Percentage displayed is Correct. The test pass section is filled with all the test passes of selected project.";
						
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("On selection of individual project Pass/Fail/Not Completed Percentage displayed is NOT Correct OR The test pass section is NOT filled with all the test passes of selected project.");
							
							comments += "\n- On selection of individual project Pass/Fail/Not Completed Percentage displayed is NOT Correct OR The test pass section is NOT filled with all the test passes of selected project. (Fail)";
							
						}
						
						
						
						
						detailsArrayForVerification = null;
						
						
						selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
						
						selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
						
						int breakCount = 0;
						
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
								
									for (int k = 1; k <= numOfProjectPresentInGrid; k++) 
									{
										int projectLinkSize = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr["+k+"]/td[1]/a")).size();
										
										System.out.println("projectLinkSize "+ projectLinkSize);
										
										if (projectLinkSize == 1) 
										{
											
											selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
											
											selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
											
											String selectedGroup = selectGroup.getFirstSelectedOption().getText();
											
											String selectedPortfolio = selectPortfolio.getFirstSelectedOption().getText();
											
											String projectNameFromGrid = getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k).getAttribute("title");
											
											String versionFromGrid = getObject("SDDetailedView_versionInGrid1", "SDDetailedView_versionInGrid2", k).getAttribute("title");
											
											getObject("SDDetailedView_projectNameInGrid1", "SDDetailedView_projectNameInGrid2", k).click();
											
											//Thread.sleep(2000);
											
											Set<String> windowId = driver.getWindowHandles();    // get  window id of current window
											
											
											Iterator<String> itererator = windowId.iterator();   
		
									        String mainWinID = itererator.next();
									        System.out.println("mainWinID "+mainWinID);
									       
									        String  newAdwinID = itererator.next();
									        System.out.println("newAdwinID "+newAdwinID);
									        
									        eventfiringdriver.switchTo().window(newAdwinID);
		
									        eventfiringdriver.manage().window().maximize();
											
											Thread.sleep(1000);
											
											String projectDetailsPageHead = getObject("ProjectEdit_pageHeading").getText();
											
											String editProjectText = getObject("ProjectsEditProjects_editProjectLink").getText();
											
											String groupNameOnEditPage = getElement("ProjectCreateNew_groupDropDown_Id").getAttribute("value");
											
											String portfolioNameOnEditPage = getElement("ProjectCreateNew_PortfolioDropDown_Id").getAttribute("value");
													
											String projectNameOnEditPage = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
											
											String	versionOnEditPage = getObject("ProjectCreateNew_versionTextField").getAttribute("value");
											
											if (  compareStrings( "Project Details", projectDetailsPageHead ) &&
												  assertTrue( getObject( "ProjectsEditProjects_editProjectLink").isDisplayed() ) &&
												  compareStrings( "EDIT PROJECT", editProjectText ) &&
												  compareStrings( selectedGroup, groupNameOnEditPage ) && 
												  compareStrings( selectedPortfolio, portfolioNameOnEditPage ) &&
												  compareStrings( projectNameFromGrid, projectNameOnEditPage ) &&
												  compareStrings( versionFromGrid, versionOnEditPage ) ) 
											{
												APP_LOGS.debug("User has redirected to respective project's details page when Clicked on the project.");
												
												comments += "\n- User has redirected to respective project's details page when Clicked on the project.";
											
											}
											else 
											{
												
												fail=true;
												
												APP_LOGS.debug("User has NOT redirected to respective project's details page OR project Name/Version/Group/Portfolio does NOT matched, when Clicked on the project on stakeholder Dashboard Detailed view Page.");
												
												comments += "\n- User has NOT redirected to respective project's details page OR project Name/Version/Group/Portfolio does NOT matched, when Clicked on the project on stakeholder Dashboard Detailed view Page. (Fail)";
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User has NOT redirected to project page OR project Name/Version/Group/Portfolio does NOT matched");
												
											}
											
											driver.close();
		
									        driver.switchTo().window(mainWinID);
										
									        breakCount = 1;
											
											break;
										}
										
										
									}
									
									if (breakCount == 1) 
									{
										break;
									}
									
								} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
								
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
