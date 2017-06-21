package com.uat.suite.sd_detailedView;

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
public class VerifyDVPagination extends TestSuiteBase  
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;	
	
	Select selectGroup ;
	Select selectPortfolio;
	Select projectStatusDD;
	
	int totalPages;
	
	
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
        	 		//click on Stakeholder dashboard page
					getElement("UAT_stakeholderDashboard_Id").click();
					Thread.sleep(1000);
        	 	}
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
				
				projectStatusDD.selectByIndex(0); // Select ALL from dropdown
				
				Thread.sleep(500);
				
				 boolean paginationFound = false;
				
				//Verify Project Section Pagination
				
				if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )
				{
				
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						if (eventfiringdriver.findElements(By.xpath("//div[@id='Pagination']/div/span")).size() == 2) 
						{
							paginationFound = true;
							
							totalPages = eventfiringdriver.findElements(By.xpath("//div[@id='Pagination']/div/a")).size();
							
							if(totalPages == 2)
							{
								if(assertTrue(isLinkEnabled("SDDetailedView_nextLink") && (!isLinkEnabled("SDDetailedView_PreviousLink")))) 
								{
									APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of project section.");
									
									comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of project section.";
										
								}
								else 
								{
									fail=true;
									
									APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of project section.");
									
									comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of project section. (Fail)";
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of project section.");
									
								}
								
							}
							
							if(totalPages > 2)
							{
								
								if(assertTrue(isLinkEnabled("SDDetailedView_nextLink") && (!isLinkEnabled("SDDetailedView_PreviousLink")))) 
								{
									APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of project section.");
									
									comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of project section.";
										
								}
								else 
								{
									fail=true;
									
									APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of project section.");
									
									comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of project section. (Fail)";
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of project section.");
									
								}
								
								getObject("SDDetailedView_nextLink").click();
								
								if(assertTrue( isLinkEnabled("SDDetailedView_PreviousLink") ) )
								{
									APP_LOGS.debug("Previous link is enabled on 2nd page of project section.");
									
									comments += "\n- Previous link is enabled on 2nd page of project section.";
								}
								else
								{
									fail=true;
									
									APP_LOGS.debug("Previous link is NOT enabled on 2nd page of project section.");
									
									comments += "\n- Previous link is NOT enabled on 2nd page of project section.(Fail)";
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled on 2nd page of project section.");
									
								}
							}
							
							//iterate to all pages till last page
							
							while(ifElementIsClickableThenClick("SDDetailedView_nextLink"));
							
							//verify if next link is disabled and previous link is enabled on last page
							if(assertTrue((!isLinkEnabled("SDDetailedView_nextLink")) && isLinkEnabled("SDDetailedView_PreviousLink")))
							{
									APP_LOGS.debug("Previous link is enabled and next link is disabled on last page of project section.");
									
									comments += "\n- Previous link is enabled and next link is disabled on last page of project section.";
							}
							else
							{
									fail=true;
									
									APP_LOGS.debug("Previous link is NOT enabled OR next link is NOT disabled on last page of project section.");
									
									comments += "\n- Previous link is NOT enabled OR next link is NOT disabled on last page of project section.";
							
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled OR next link is NOT disabled of project section");
							}
							
							break;
						}
					
						
					}
					
					if ( ! paginationFound ) 
					{
						
						APP_LOGS.debug("No Pagination has found to be verified in project section.");
						
						comments += "\n- No Pagination has found to be verified in project section.";
					}
					
					
					//Verify Test Pass Section Pagination
					
					comments += "\n";
					
					paginationFound = false;
					
					selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
					
					selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						int breakCount = 0;
						
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						for (int j = 0; j < selectPortfolio.getOptions().size(); j++) 
						{
							selectPortfolio.selectByIndex(j);
							
							if ( ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) && 
								 eventfiringdriver.findElements(By.xpath("//div[@id='PaginationTP']/div/span")).size() == 2) 
							{
								paginationFound = true;
								
								totalPages = eventfiringdriver.findElements(By.xpath("//div[@id='PaginationTP']/div/a")).size();
								
								if(totalPages == 2)
								{
									if(assertTrue(isLinkEnabled("SDDetailedView_testPassStatusNextLink") && (!isLinkEnabled("SDDetailedView_testPassStatusPreviousLink")))) 
									{
										APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of Test Pass Status Section.");
										
										comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of Test Pass Status Section.";
											
									}
									else 
									{
										fail=true;
										
										APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of Test Pass Status Section.");
										
										comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of Test Pass Status Section. (Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of Test Pass Status Section.");
										
									}
									
								}
								
								if(totalPages > 2)
								{
									
									if(assertTrue(isLinkEnabled("SDDetailedView_testPassStatusNextLink") && (!isLinkEnabled("SDDetailedView_testPassStatusPreviousLink")))) 
									{
										APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of Test Pass Status Section.");
										
										comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of Test Pass Status Section.";
											
									}
									else 
									{
										fail=true;
										
										APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of Test Pass Status Section.");
										
										comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of Test Pass Status Section. (Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of Test Pass Status Section.");
										
									}
									
									getObject("SDDetailedView_testPassStatusNextLink").click();
									
									if(assertTrue( isLinkEnabled("SDDetailedView_testPassStatusPreviousLink") ) )
									{
										APP_LOGS.debug("Previous link is enabled on 2nd page of Test Pass Status Section.");
										
										comments += "\n- Previous link is enabled on 2nd page of Test Pass Status Section.";
									}
									else
									{
										fail=true;
										
										APP_LOGS.debug("Previous link is NOT enabled on 2nd page of Test Pass Status Section.");
										
										comments += "\n- Previous link is NOT enabled on 2nd page of Test Pass Status Section.(Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled on 2nd page of Test Pass Status Section.");
										
									}
								}
								
								//iterate to all pages till last page
								
								while(ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));
								
								//verify if next link is disabled and previous link is enabled on last page
								if(assertTrue((!isLinkEnabled("SDDetailedView_testPassStatusNextLink")) && isLinkEnabled("SDDetailedView_testPassStatusPreviousLink")))
								{
										APP_LOGS.debug("Previous link is enabled and next link is disabled on last page of Test Pass Status Section.");
										
										comments += "\n- Previous link is enabled and next link is disabled on last page of Test Pass Status Section.";
								}
								else
								{
										fail=true;
										
										APP_LOGS.debug("Previous link is NOT enabled OR next link is NOT disabled on last page of Test Pass Status Section.");
										
										comments += "\n- Previous link is NOT enabled OR next link is NOT disabled on last page of Test Pass Status Section.";
								
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled OR next link is NOT disabled of Test Pass Status Section");
								}
								
								breakCount = 1;
								
								break;	
								
							}
							
						}
						if (breakCount == 1) 
						{
							break;	
						}
						
					}
						
					if ( ! paginationFound ) 
					{
						
						APP_LOGS.debug("No Pagination has found to be verified in Test Pass Status Section.");
						
						comments += "\n- No Pagination has found to be verified in Test Pass Status Section.";
					}
					
					//Verify Tester Section Pagination
					
					comments += "\n";
					
					paginationFound = false;
					
					selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
					
					selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
					
					for (int i = 0; i < selectGroup.getOptions().size(); i++) 
					{
						int breakCount = 0;
						
						selectGroup.selectByIndex(i);
						
						Thread.sleep(500);
						
						for (int j = 0; j < selectPortfolio.getOptions().size(); j++) 
						{
							selectPortfolio.selectByIndex(j);
							
							if ( ( ! getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed() ) )
							{
								int numberOfTestPassDisplayed2 = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
								
								for (int k = 1; k <= numberOfTestPassDisplayed2; k++) 
								{
								
									getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).click();
									
									if ( eventfiringdriver.findElements(By.xpath("//div[@id='PaginationTesterChart']/div/span")).size() == 2) 
									{
										paginationFound = true;
										
										totalPages = eventfiringdriver.findElements(By.xpath("//div[@id='PaginationTP']/div/a")).size();
										
										if(totalPages == 2)
										{
											if(assertTrue(isLinkEnabled("SDDetailedView_testerListNextLink") && (!isLinkEnabled("SDDetailedView_testerListPreviousLink")))) 
											{
												APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of Tester List Section.");
												
												comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of Tester List Section.";
													
											}
											else 
											{
												fail=true;
												
												APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of Tester List Section.");
												
												comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of Tester List Section. (Fail)";
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of Tester List Section.");
												
											}
											
										}
										
										if(totalPages > 2)
										{
											
											if(assertTrue(isLinkEnabled("SDDetailedView_testerListNextLink") && (!isLinkEnabled("SDDetailedView_testerListPreviousLink")))) 
											{
												APP_LOGS.debug("Next link is enabled and Previous link is disabled  on 1st page of Tester List Section.");
												
												comments += "\n- Next link is enabled and Previous link is disabled  on 1st page of Tester List Section.";
													
											}
											else 
											{
												fail=true;
												
												APP_LOGS.debug("Previous link is NOT disabled OR next link is NOT enabled on 1st page of Tester List Section.");
												
												comments += "\n- Previous link is NOT disabled OR next link is NOT enabled on 1st page of Tester List Section. (Fail)";
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT disabled OR next link is NOT enabled of Tester List Section.");
												
											}
											
											getObject("SDDetailedView_testerListNextLink").click();
											
											if(assertTrue( isLinkEnabled("SDDetailedView_testerListPreviousLink") ) )
											{
												APP_LOGS.debug("Previous link is enabled on 2nd page of Tester List Section.");
												
												comments += "\n- Previous link is enabled on 2nd page of Tester List Section.";
											}
											else
											{
												fail=true;
												
												APP_LOGS.debug("Previous link is NOT enabled on 2nd page of Tester List Section.");
												
												comments += "\n- Previous link is NOT enabled on 2nd page of Tester List Section.(Fail)";
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled on 2nd page of Tester List Section.");
												
											}
										}
										
										//iterate to all pages till last page
										
										while(ifElementIsClickableThenClick("SDDetailedView_testerListNextLink"));
										
										//verify if next link is disabled and previous link is enabled on last page
										if(assertTrue((!isLinkEnabled("SDDetailedView_testerListNextLink")) && isLinkEnabled("SDDetailedView_testerListPreviousLink")))
										{
												APP_LOGS.debug("Previous link is enabled and next link is disabled on last page of Tester List Section.");
												
												comments += "\n- Previous link is enabled and next link is disabled on last page of Tester List Section.";
										}
										else
										{
												fail=true;
												
												APP_LOGS.debug("Previous link is NOT enabled OR next link is NOT disabled on last page of Tester List Section.");
												
												comments += "\n- Previous link is NOT enabled OR next link is NOT disabled on last page of Tester List Section.";
										
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT enabled OR next link is NOT disabled of Tester List Section");
										}
										
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
						if (breakCount == 1) 
						{
							break;	
						}
					}
					
					if ( ! paginationFound ) 
					{
						
						APP_LOGS.debug("No Pagination has found to be verified in Tester List Section..");
						
						comments += "\n- No Pagination has found to be verified in Tester List Section.";
					}
					
					
				}
				else 
				{
					skip = true;
					
					APP_LOGS.debug("Verification of Pagination cannot be done as 'No Group' is displayed in Group Dropdown.");
					
					comments += "\n- Verification of Pagination cannot be done as 'No Group' is displayed in Group Dropdown and skipping the test.";	
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
		
	/*	
		
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
		*/
		//if link contains disable attribute
		 public String isLinkDisabled(String key)
		 {
		  String disabledAttr = null;
		   
		  try
		  {
		   setImplicitWait(1);
		      disabledAttr = eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).getAttribute("disabled");    
		     
		  }
		  catch(Throwable t)
		  {
		   t.printStackTrace();
		   
		  }
		  finally
		    {
		     resetImplicitWait();
		            
		    }
		  
		  return disabledAttr;
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
