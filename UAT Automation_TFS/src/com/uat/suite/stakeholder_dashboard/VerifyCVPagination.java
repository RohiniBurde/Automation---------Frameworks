/**
 * @author preeti.walde
 * Created: 30th Jan 2015
 * Last Modified: 2nd Feb 2015
 * Description: Code to test Pagination options in Consolidated View tab.
 */

package com.uat.suite.stakeholder_dashboard;

import java.io.IOException;

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
public class VerifyCVPagination extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	int totalPages;
	int anchorSize;	
	int projectLimitPerPage=5;	
	int testStepLimitPerPage=10;

	
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
	public void verifyCVPagination(String Role) throws Exception
	{
		
		String previousLink;
		String nextLink;
		
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
						
						int numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						if(numOfProjectPresentInGrid==0)
						{
							skip=true;
							APP_LOGS.debug("Project not available... can not verify pagination operation.");
							comments+="Project not available... can not verify pagination operation(Skip). ";
							closeBrowser();
							
							throw new SkipException("Project not available... So Skipping next previous link verification in stakeholder dashboard page for project section");
							
						}
						else
						{
								//project section pagination
								if(getElement("StakeholderDashboardConsolidatedView_ProjectSectionPagination_Id").findElements(By.xpath("div/span")).size()==3) 
								{
										APP_LOGS.debug("Only 1 page available on Project Section of Consolidated View.");
										
										if(assertTrue((!isLinkEnabled("StakeholderDashboardConsolidatedView_NextLink"))
												&& (!isLinkEnabled("StakeholderDashboardConsolidatedView_PreviousLink"))))
										{
												APP_LOGS.debug("Next and Previous links are disabled as only one page is available on project section.");
												comments+="Next and Previous links are disabled(Pass). ";
												
												int projectsPerPage = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
												
												if(projectLimitPerPage == projectsPerPage)
												{
														APP_LOGS.debug("Project per page "+projectsPerPage+" is equal to projectLimit "+projectLimitPerPage+" on first page of project section");
												}
												else
												{													
														APP_LOGS.debug("Project per page "+projectsPerPage+" is not equal to projectLimit "+projectLimitPerPage+" on first page of project section.");
												}
										}
										else
										{
											fail=true;
											APP_LOGS.debug("Next and Previous links are enabled though only 1 page is available on project section.");
											comments+="Next and Previous links are enabled though only 1 page is available(Fail). ";
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NextPreviousLinksEnabledThoughOnlyOnePageAvailable");
										}										
										
								}
								else 
								{
										APP_LOGS.debug("More than 1 page avaialble on Project Section of Consolidated View on project section.");
										
										totalPages = getElement("StakeholderDashboardConsolidatedView_ProjectSectionPagination_Id").findElements(By.xpath("div/a")).size();
								}
								
								if (totalPages>1)
								{
										int projectsPerPage = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
										
										if(assertTrue(isLinkEnabled("StakeholderDashboardConsolidatedView_NextLink") && (!isLinkEnabled("StakeholderDashboardConsolidatedView_PreviousLink")))) 
										{
												APP_LOGS.debug("Previous link is disabled and next link is enabled on 2nd page of project section.");
												
												if(projectLimitPerPage == projectsPerPage)
												{
														APP_LOGS.debug("Project per page "+projectsPerPage+" is equal to projectLimit "+projectLimitPerPage+" on first page of project section");
												}
												else
												{													
														APP_LOGS.debug("Project per page "+projectsPerPage+" is not equal to projectLimit "+projectLimitPerPage+" on first page of project section.");
												}
										}
										else
										{
											fail=true;
											APP_LOGS.debug("Previous link is enabled and next link is disabled on 2nd page of project section.");
											assertTrue(false);
										}
												
										
										if(totalPages>2)
										{
												getObject("StakeholderDashboardConsolidatedView_NextLink").click();
												
												if(assertTrue(isLinkEnabled("StakeholderDashboardConsolidatedView_NextLink") 
														&& isLinkEnabled("StakeholderDashboardConsolidatedView_PreviousLink")))
												{
													APP_LOGS.debug("Previous link and next link both are enabled on 2nd page of project section.");
												}
												else
												{
													fail=true;
													APP_LOGS.debug("Previous link and next link are disabled on 2nd page of project section.");
												}
										}
										
										//iterate to all pages till last page
										goToLastPageOfProject();
										
										//verify if next link is disabled and previous link is enabled on last page
										if(assertTrue((!isLinkEnabled("StakeholderDashboardConsolidatedView_NextLink")) && isLinkEnabled("StakeholderDashboardConsolidatedView_PreviousLink")))
										{
												APP_LOGS.debug("Previous link is enabled and next link is disabled on last page of project section.");
										}
										else
										{
												fail=true;
												APP_LOGS.debug("Previous link is disabled and next link is enabled on last page of project section.");
										}
								}
							}
						
						
						
							//******************test step section pagination*********************************
						
							getElement("StakeholderDashboardConsolidatedView_statusLinkAll_Id").click();
							Thread.sleep(500);
							
							int noTestStepMsgSize = getElement("StakeholderDashboardConsolidatedView_noTestStepLabelDiv_Id").findElements(By.tagName("a")).size();
							
							if(noTestStepMsgSize==1)
							{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepLabel"), getObject("StakeholderDashboardConsolidatedView_noTestStepLabel").getText()))
									{
										APP_LOGS.debug("No Test Steps available! message is verified for 'All' link.");
										comments+="No Test Steps available! message is verified for 'All' link.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Steps available!' message not shown for 'All' link.");
										comments+="'No Test Steps available!' message not shown for 'All' link.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown for 'All' link");
									}
									
									skip=true;
									APP_LOGS.debug("Test Steps not available... can not verify pagination operation.");
									comments+="Test Steps not available... can not verify pagination operation(Skip). ";
									
									throw new SkipException("Test Steps not available... So Skipping next previous link verification in stakeholder dashboard page for test steps");
								
							}
							else
							{
									String testStepPaginationLabel = getObject("StakeholderDashboardConsolidatedView_testStepGridPaginationLabel_Text").getText();
									 
									String testStepCountArray = testStepPaginationLabel.split("total ")[1];
									int endDelimiter = testStepCountArray.indexOf(" ");
									int testStepCount = Integer.parseInt(testStepCountArray.substring(0,endDelimiter));
									
									int q = testStepCount/10;
									int r = testStepCount%10;
									int totalTestStepPages;
									
									if(r==0)
									{
										totalTestStepPages = q + 0;
									}
									else
									{
										totalTestStepPages = q + 1;
									}
									
									if(totalTestStepPages==1)
									{
											int testStepPerPage = getElement("StakeholderDashboardConsolidatedView_testStepTable_Id").findElements(By.xpath("tbody/tr")).size();
											
											previousLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepPreviousLink");
											nextLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepNextLink");
											
											//verify next previous links are disabled if only one page is available																																													
											if(previousLink.equals("true") && nextLink.equals("true"))
											{
													APP_LOGS.debug("Previous and Next both links are disabled as only 1 page available on test step section.");
													
													if(testStepPerPage == testStepLimitPerPage)
													{
														APP_LOGS.debug("Test Step per page "+testStepPerPage+" is equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
													}
													else
													{											
														APP_LOGS.debug("Test Step count "+testStepPerPage+" is not equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
														
													}													
											}
											else
											{
												fail=true;
												APP_LOGS.debug("Previous and Next both links are enabled though only 1 page available on test step section.");
												assertTrue(false);
											}
									}									
									else
									{								
											int testStepPerPage = getElement("StakeholderDashboardConsolidatedView_testStepTable_Id").findElements(By.xpath("tbody/tr")).size();
																						
											previousLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepPreviousLink");
											nextLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepNextLink");
											
											//verify if previous link disabled and next link enabled on first page																																														
											if(previousLink.equals("true") && nextLink==null)
											{
													APP_LOGS.debug("Previous link is disabled and next link is enabled on first page of test step section.");
													
													if(testStepPerPage == testStepLimitPerPage)
													{
														APP_LOGS.debug("Test Step per page "+testStepPerPage+" is equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
													}
													else
													{											
														APP_LOGS.debug("Test Step count "+testStepPerPage+" is not equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
														
													}		
													
											}
											else
											{
												fail=true;
												APP_LOGS.debug("Previous link is enabled and next link is disabled on 2nd page of test step section.");
												assertTrue(false);
											}
											
											if(totalTestStepPages>2)
											{
													//click on next link
													getObject("StakeholderDashboardConsolidatedView_testStepNextLink").click();
													
													previousLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepPreviousLink");
													nextLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepNextLink");
													
													if(previousLink==null && nextLink==null)
													{
														APP_LOGS.debug("Previous link and next link both are enabled on 2nd page of test step section.");
													}
													else
													{
														fail=true;
														APP_LOGS.debug("Previous link and next link are disabled on 2nd page of test step section.");
														assertTrue(false);
													}
											}										
									
											//iterate to all pages till last page of test step
											goToLastPageOfTestStep("StakeholderDashboardConsolidatedView_testStepNextLink");
																							
											previousLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepPreviousLink");
											nextLink = isLinkDisabled("StakeholderDashboardConsolidatedView_testStepNextLink");
											
											//verify if next link is disabled and previous link is enabled on last page
											if((previousLink==null) && nextLink.equals("true"))
											{
													APP_LOGS.debug("Previous link is enabled and next link is disabled on last page of test step section.");
											}
											else
											{
													fail=true;
													APP_LOGS.debug("Previous link is disabled and next link is enabled on last page of test step section.");
													assertTrue(false);
											}	
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
	
	
	
	
	//verify if link is enabled
	private boolean isLinkEnabled(String key)
	{
		
			try
			{
				setImplicitWait(1);
				eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();			 
			    return true;
			}
			catch(Throwable t)
			{
				return false;
			}
			finally
		   {
			   resetImplicitWait();
		           
		   }
			
			
	}
	
	
	
	//click next till last page of project section	
	private void goToLastPageOfProject() throws IOException
	{				
			do
			{
				int projectsPerPage = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
									
				if(projectLimitPerPage == projectsPerPage)
				{
						APP_LOGS.debug("Project per page "+projectsPerPage+" is equal to projectLimit "+projectLimitPerPage+" on first page of project section");
				}
				else
				{													
						APP_LOGS.debug("Project per page "+projectsPerPage+" is not equal to projectLimit "+projectLimitPerPage+" on first page of project section.");
				}
				
				
			}while(ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
		
	}
	
	
	//click next till last page of test step section
	private void goToLastPageOfTestStep(String nextLink) throws IOException
	{
		boolean isNextLinkEnabled = true;
				
		while(isNextLinkEnabled)		
		{
				int testStepPerPage = getElement("StakeholderDashboardConsolidatedView_testStepTable_Id").findElements(By.xpath("tbody/tr")).size();
				
				try
				{
					if(testStepPerPage == testStepLimitPerPage)
					{
						APP_LOGS.debug("Test Step per page "+testStepPerPage+" is equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
					}
					else
					{											
						APP_LOGS.debug("Test Step count "+testStepPerPage+" is not equal to testStepLimit "+testStepLimitPerPage+" of test step section.");
						
					}		
					
					if(isLinkDisabled(nextLink)==null)
					{
						setImplicitWait(0);	
						
						//click on Next link
						getObject("StakeholderDashboardConsolidatedView_testStepNextLink").click();
					    
					}
					else
					{
						isNextLinkEnabled = false;
					}					
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					isNextLinkEnabled = false;
				}
				finally
				{
					resetImplicitWait();
				}				
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
		else if(!isLoginSuccess)
		{
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
