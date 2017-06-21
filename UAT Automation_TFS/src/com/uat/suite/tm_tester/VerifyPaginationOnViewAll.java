package com.uat.suite.tm_tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;
@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyPaginationOnViewAll extends TestSuiteBase{

	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	int totalPages;
	int totalTesterToAdd=9;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
			@BeforeTest
			public void checkTestSkip() throws Exception
			{
				APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());	
				
				if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName())){
					APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
				}
				runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
				
				
				utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
			
			}
			
			// Test Case Implementation ...
			
			@Test(dataProvider="getTestData")
			public void testVerifyPaginationOnViewAll
				( String Role, String GroupName, String Portfolio, String Project, String Version,String TestPassName, 
						String TP_Tester,String TesterRoleCreation ,String TesterRoleSelection,String EndMonth,
						String EndYear,String EndDate,String TestManager, String VersionLead,String ExpectedPageHeadingText,
						String ExpectedNoTesterText) throws Exception
			{
				count++;
				comments = "";
				
				if(!runmodes[count].equalsIgnoreCase("Y")){
					skip=true;
					APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

					throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
				}
							
				int tester_count = Integer.parseInt(TP_Tester);
				tester = getUsers("Tester", tester_count);
				
				int testManager_count = Integer.parseInt(TestManager);
				testManager = getUsers("Test Manager", testManager_count);
				
				int versionLead_count = Integer.parseInt(VersionLead);
				versionLead = getUsers("Version Lead", versionLead_count);			
				
				openBrowser();
				APP_LOGS.debug("Opening Browser... ");
				System.out.println("Opening Browser... ");
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
				//Step 1:  clicking on Test Management Tab
					try 
					{
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
						//Step 2:  clicking on Project Tab
						//getElement("TM_projectsTab_Id").click();
						APP_LOGS.debug("Clicking On Project Tab ");
						System.out.println("Clicking On Project Tab ");
						comments="Clicking On Project Tab...";
					
						if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{
							APP_LOGS.debug("Project Creation fails");
							comments=comments+"Project Creation Fails";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProjects");
							throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
						}
						System.out.println("Successfully created the Project for second Project");
						
						//Thread.sleep(1000);
						closeBrowser();
						
						openBrowser();
						APP_LOGS.debug("Opening Browser... ");
						
						//login with version lead
						if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
						{
							//Thread.sleep(1000);
							
							//clicking on Test Management Tab
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(500);
							APP_LOGS.debug("Clicking On Test Management Tab ");
							System.out.println("Clicking On Test Management Tab ");
							
							// version lead creates the test pass
							if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{
								comments+="Test Pass Creation Fails";
								APP_LOGS.debug("Test Pass Creation Fails");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
								throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
							}
							
							//Thread.sleep(1000);
							closeBrowser();
							
							openBrowser();
							APP_LOGS.debug("Opening Browser... ");
							
							//Test manager logs in here
							if(login(testManager.get(0).username,testManager.get(0).password,"Test Manager"))
							{
								
								//Thread.sleep(1000);
								//clicking on Test Management Tab
								getElement("UAT_testManagement_Id").click();
								APP_LOGS.debug("Clicking On Test Management Tab ");
								
								Thread.sleep(500);
								//clicking on Test Pass Tab
								getElement("TM_testersTab_Id").click();
								APP_LOGS.debug("Clicking On Testers Tab ");
								
						
								Thread.sleep(500);								
								String testersTabHighlighted = getObject("Testers_testerTab").getAttribute("class");
								
								if(!compareStrings(OR.getProperty("Testers_testerTab_Class"), testersTabHighlighted))
								{
									fail=true;
									APP_LOGS.debug("Tester Tab is not Highlighted");
									comments=comments+"Tester Tab is not Highlighted (FAIL) ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Tab is not Highlighted");
								}
						
								//select the drop down from here
								
								dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
								Thread.sleep(500);
								
								if(!compareStrings(getObject("TestersViewAll_withoutTester").getText(), ExpectedNoTesterText))
								{
									fail=true;
									APP_LOGS.debug("No Tester(s) Available! text is not found");
									comments=comments+"No Tester(s) Available! text is not found(FAIL) ";	
								}
								else
								{
									APP_LOGS.debug("No Tester(s) Available!");
									comments=comments+"No Tester(s) Available! (PASS)";	
								}								
															
								
								
								for(int i=0;i<totalTesterToAdd;i++)
								{
									// add tester for the test pass
									if(!createTesterPagination(tester.get(i).username, TesterRoleCreation, TesterRoleSelection))
									{
										APP_LOGS.debug("Tester is not created successfully....");
										comments=comments+"Tester is not created successfully (FAIL)";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTesters");
										throw new SkipException("Unable to create required number of testers");
									}
								}
								
								APP_LOGS.debug("Tester created successfully");
								//getObject("Testers_viewAllLink").click();
								
								paging();								
								
								
								if(!createTesterPagination(tester.get(9).username, TesterRoleCreation, TesterRoleSelection))
								{
									APP_LOGS.debug("Tester is not created successfully....");
									comments=comments+"10th Tester is not created successfully (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreating10thTester");
									throw new SkipException("Unable to create 10th tester");
								}
								totalTesterToAdd++;
								
								APP_LOGS.debug("Tester created successfully");
								
								//getObject("Testers_viewAllLink").click();
								
								paging();
								
								//getElement("Tester_createNewProjectLink_Id").click();
								if(!createTesterPagination(tester.get(10).username, TesterRoleCreation, TesterRoleSelection))
								{
									APP_LOGS.debug("Tester is not created successfully....");
									comments=comments+"11th Tester is not created successfully (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreating11thTester");
									throw new SkipException("Unable to create 11th tester");
								}
								totalTesterToAdd++;
								APP_LOGS.debug("Tester created successfully");
								//getObject("Testers_viewAllLink").click();
								paging();
								
								
							}
							else
							{
								fail = true;
								comments+="Login Not Successful for Test Manager(FAIL)";
							}	
						}
						else
						{
							fail = true;
							comments+="Login Not Successful for version lead(FAIL)";
						}
					}
					catch (Throwable t) 
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Something went wrong while testing the pagination feature for tester page");
						comments=comments+"Something went wrong while testing the pagination feature for tester page (FAIL)";
					}
				
					APP_LOGS.debug("Closing Browser... ");
					System.out.println("Closing Browser... ");
					
					//Thread.sleep(1000);
					closeBrowser();
				}
					
				else 
				{
					APP_LOGS.debug("Login Not Successful");
					System.out.println("Login Not Successful");
				}	
			}
			
			
			
			
			
			private void paging() throws InterruptedException, IOException
			{
				if (getElement("TestersViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
				{
					APP_LOGS.debug("Only 1 page available on View All page.");
					totalPages=1;
				}
				else 
				{
					APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
					totalPages = getElement("TestersViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
				}
				
				if(totalTesterToAdd>10)
				{
					if(compareIntegers(2, totalPages))
					{
						comments+="As total added testers were greater than 10, 2 pagings were available (PASS) ";
					}
					else
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "For testers greater than 10, total pagings should be 2");
						comments+="As total added testers were greater than 10, 2 pagings were paging should have been available (FAIL) ";
					}
					
					//Thread.sleep(1000);
					//previous is disable
					if(!assertTrue(isElementExists("TestersViewAll_disablePreviousLink")))
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is not disabled when user is in 1 of 2 pagings available");
						APP_LOGS.debug("Previous link is not disabled when user is in 1 of 2 pagings available (FAIL)");
						comments=comments+"Previous link is not disabled when user is in 1 of 2 pagings available (FAIL)";
					}
					else
					{
						APP_LOGS.debug("Previous link is disabled when user is in 1 of 2 pagings available (PASS)");
						comments=comments+"Previous link is disabled when user is in 1 of 2 pagings available (PASS)";
					}
					
					if(!assertTrue(isElementExists("TestersViewAll_enableNextLink")))
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next link is not disabled when user is in 1 of 2 pagings available ");
						APP_LOGS.debug("Next link is not disabled when user is in 1 of 2 pagings available (FAIL)");
						comments=comments+"Next link is not disabled when user is in 1 of 2 pagings available (FAIL)";
					}
					else
					{
						APP_LOGS.debug("Next link is disabled when user is in 1 of 2 pagings available (PASS)");
						comments=comments+"Next link is disabled when user is in 1 of 2 pagings available (PASS)";
						getObject("TestersViewAll_enableNextLink").click();
					}
					
					//next is disabled
					if(!assertTrue(isElementExists("TestersViewAll_disableNextLink")))
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next link is not disabled when user is in 1 of 2 pagings available ");
						APP_LOGS.debug("Next link is not disabled when user is in 1 of 2 pagings available (FAIL)");
						comments=comments+"Next link is not disabled when user is in 1 of 2 pagings available (FAIL)";
					}
					else
					{
						APP_LOGS.debug("Next link is disabled when user is in 2 of 2 pagings available (PASS)");
						comments=comments+"Next link is disabled when user is in 2 of 2 pagings available (PASS)";
					}
					
					
					if(!assertTrue(isElementExists("TestersViewAll_enablePreviousLink")))
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is not enabled when user is in 2 of 2 pagings available ");
						APP_LOGS.debug("Previous link is not enabled when user is in 2 of 2 pagings available (FAIL)");
						comments=comments+"Previous link is not enabled when user is in 2 of 2 pagings available (FAIL)";
					}
					else
					{
						getObject("TestersViewAll_enablePreviousLink").click();
						APP_LOGS.debug("Previous link is enabled when user is in 2 of 2 pagings available (PASS)");
						comments=comments+"Previous link is enabled when user is in 2 of 2 pagings available (PASS)";
					}
				}
				else
				{
					if(compareIntegers(1, totalPages))
					{
						comments+="As total added testers were less than equal to 10, only 1 paging is available (PASS) ";
					}
					else
					{
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "For less than or equal to 10 testers, total pagings should be 1");
						comments+="As total added testers were less than equal to 10, only 1 paging should have been available (FAIL) ";
					}
					
					//Previous is disabled
					if(!assertTrue(isElementExists("TestersViewAll_disablePreviousLink")))
					{
						fail=true;
						APP_LOGS.debug("Previous link is not disabled when tester count is less than equal to 10 (FAIL)");
						comments=comments+"Previous link is not disabled when tester count is less than equal to 10 (FAIL)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is not disabled when tester count is less than equal to 10");
					}
					else
					{
						APP_LOGS.debug("Previous link is disabled when tester count is less than equal to 10 (PASS)");
						comments=comments+"Previous link is disabled when tester count is less than equal to 10 (PASS)";
					}
					
					//next is disabled
					if(!assertTrue(isElementExists("TestersViewAll_disableNextLink")))
					{
						fail=true;
						APP_LOGS.debug("Next link is not disabled when tester count is less than equal to 10 (FAIL)");
						comments=comments+"Next link is not disabled when tester count is less than equal to 10 (FAIL)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next link is not disabled when tester count is less than equal to 10");
					}
					else
					{
						APP_LOGS.debug("Next link is disabled when tester count is less than equal to 10 (PASS)");
						comments=comments+"NExt link is disabled when tester count is less than equal to 10 (PASS)";
					}
				}
			}
			
			
			
			
			
			private boolean isElementExists(String key)
			{
				setImplicitWait(0);
				try
				{
					if(eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed())
						return true;
					else
						return false;
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
			
			
			
			
			
			public boolean createTesterPagination(String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
			{
				APP_LOGS.debug("Creating Tester");
				//Thread.sleep(1000);
				
				if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
				{
					getObject("TesterCreateNew_TesterActiveX_Close").click();
				}
								
				try {
						//Thread.sleep(1000);
						getElement("Tester_createNewProjectLink_Id").click();
						Thread.sleep(500);
					
						getObject("TesterCreateNew_PeoplePickerImg").click();
						   
						Thread.sleep(500);driver.switchTo().frame(1);
						
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
						
						getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
						   
						getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
						
						displayNamefromPeoplePicker = getObject("TesterCreateNew_PeoplePickerSelectSearchResult").getText();
				   
						getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
				   
						getObject("TesterCreateNew_PeoplePickerOkBtn").click();
				   
						driver.switchTo().defaultContent();
						Thread.sleep(1000);
						String[] testerRoleArray = testerRoleCreation.split(",");
						
						for(int i=0;i<testerRoleArray.length;i++)
						{
							if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
							{
								getElement("TesterCreateNew_addTesterRoleLink_Id").click();
								getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
								getElement("TesterCreateNew_addTesterRole_Id").click();
							}
						}
						String[] testerRoleSelectionArray = testerRoleSelection.split(",");
//						System.out.println(testerRoleSelectionArray.length);
						List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
						int numOfRoles = roleSelectionNames.size();
						for(int i = 0; i<numOfRoles;i++)
						{
							for(int j = 0; j < testerRoleSelectionArray.length;j++)
							{
								if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
								{
									getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
								}
							}
						}
						getElement("TesterCreateNew_testerSaveBtn_Id").click();
						
						//Thread.sleep(2000);
						
						if (getTextFromAutoHidePopUp().contains("successfully")) 
						{
							//getObject("Tester_testeraddedsuccessfullyOkButton").click();
							
							return true;
						}
						else 
						{
							return false;
						}
						
						
					
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in createTester function.");
					e.printStackTrace();
					return false;
				}
				
			}
			
				
			@AfterMethod
			public void reportDataSetResult(){
				if(skip)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				
				else if(!isLoginSuccess){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				}
				else if(fail){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
					TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}
				else
				{
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
					TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}					
				skip=false;
				fail=false;
			}
		
			@AfterTest
			public void reportTestResult() throws Exception{
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
				utilRecorder.stopRecording();
			}
			
			@DataProvider
			public Object[][] getTestData(){
				return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
			}
}
					
					