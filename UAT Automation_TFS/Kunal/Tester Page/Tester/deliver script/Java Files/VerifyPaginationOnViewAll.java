package com.uat.suite.tm_tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyPaginationOnViewAll extends TestSuiteBase{

	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	static boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	boolean totalCountVisibility=false;
	int totalPages;
	int totalTesterToAdd=11;
	int totalNoOftesters;
	
	// Runmode of test case in a suite
	
			@BeforeTest
			public void checkTestSkip(){
				APP_LOGS.debug("Begining of the Verify Pagination On View All");
				System.out.println("Begining of the Verify Pagination On View All");
				
				if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName())){
					APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
				}
				runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
				
				tester=new ArrayList<Credentials>();
				versionLead=new ArrayList<Credentials>();
				testManager=new ArrayList<Credentials>();				
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
				
				APP_LOGS.debug(" Executing Test Case -> VerifyTesterPageContents...");
				System.out.println(" Executing Test Case -> VerifyTesterPageContents...");				
				
				openBrowser();
				APP_LOGS.debug("Opening Browser... ");
				System.out.println("Opening Browser... ");
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
				//Step 1:  clicking on Test Management Tab
					try {
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
					
						//Step 2:  clicking on Project Tab
						getElement("TM_projectsTab_Id").click();
						APP_LOGS.debug("Clicking On Project Tab ");
						System.out.println("Clicking On Project Tab ");
						comments="Clicking On Project Tab...";
						
					} catch (Throwable t) {
						fail=true;
						APP_LOGS.debug("Clicking On Project Tab is failed");
						comments="Clicking On Project Tab is failed";
					}
					
					if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Project Creation fails");
						comments=comments+"Project Creation Fails";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProjects");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					System.out.println("Successfully created the Project for second Project");
					
					Thread.sleep(1000);
					closeBrowser();
					
					openBrowser();
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					
					//login with version lead
					login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead");
					
					try {
						Thread.sleep(1000);
						
						//clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("Clicking On Test Pass Tab is failed");
						System.out.println("Clicking On Test Pass Tab is failed");
						
					}
					
					// version lead creates the test pass
					if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Test Pass Creation Fails");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					System.out.println("Successfully created the Test Pass for first Project");
					

					System.out.println("Successfully created the Test Pass for second Project");
					Thread.sleep(1000);
					closeBrowser();
					
					openBrowser();
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					
					//Test manager logs in here
					login(testManager.get(0).username,testManager.get(0).password,"Test Manager");
					
					try {
						Thread.sleep(1000);
						//clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
						Thread.sleep(2000);
						//clicking on Test Pass Tab
						getElement("TM_testersTab_Id").click();
						APP_LOGS.debug("Clicking On Testers Tab ");
						System.out.println("Clicking On Testers Tab ");
						
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("Clicking On Test Pass Tab is failed");
						System.out.println("Clicking On Test Pass Tab is failed");
						
					}
					
					Thread.sleep(1000);					
					
					String testersTabHighlighted = getObject("Testers_testerTab").getAttribute("class");
					
					if(!compareStrings(testersTabHighlighted,OR.getProperty("Testers_testerTab_Class")))
					{
						fail=true;
						APP_LOGS.debug("Tester Tab is not Highlighted");
						comments=comments+"Tester Tab is not Highlighted";
						
					}
					
					APP_LOGS.debug("Testers Tab is Highlighted ");
					
					if(!compareStrings(getObject("Testers_testersDetailsHeading").getText(),ExpectedPageHeadingText))
					{
						fail=true;
						APP_LOGS.debug("User " + Role+ " is not landing on 'Testers' Page (FAIL)");
						comments="User " + Role+ " is not landing on 'Testers' Page (FAIL)";
					}
						APP_LOGS.debug("User " + Role+ " is landing on 'Testers' Page successfully ");
						comments="User " + Role+ " is landing on 'Testers' Page successfully (PASS)";
					
						//select the drop down from here
						Thread.sleep(1000);
						
						dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
						dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
						dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
						dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
						dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
							
						try {
														
								if(!compareStrings(getObject("TestersViewAll_withoutTester").getText(), ExpectedNoTesterText))
								{
									fail=true;
									APP_LOGS.debug("No Tester(s) Available! text is not found");
									comments=comments+"No Tester(s) Available! (FAIL)";	
								}
								APP_LOGS.debug("No Tester(s) Available!");
								comments=comments+"No Tester(s) Available! (PASS)";	
								
								getElement("TesterNavigation_Id").click();
								Thread.sleep(1000);
								
								getElement("Tester_createNewProjectLink_Id").click();
								Thread.sleep(1000);
								
								for(int i=0;i<totalTesterToAdd;i++){
									
									// add tester for the test pass
									if(!createTesterPagination(GroupName, Portfolio, Project, Version, TestPassName, tester.get(i).username, TesterRoleCreation, TesterRoleSelection))
									{
										fail=true;
										totalCountVisibility=false;
										APP_LOGS.debug("Tester is not created successfully....");
										comments=comments+"Tester is not created successfully (FAIL)";

										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
									}
									APP_LOGS.debug(i+" : Tester created successfully");
									comments=comments +i+" : Tester created successfully (PASS)";
								}
								
								getObject("Testers_viewAllLink").click();
								
								
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
								
								
								if(totalPages>1){
									// click on the next page
									Thread.sleep(1000);
									//previous is disable
									if(!assertTrue(getObject("TestersViewAll_disablePreviousLink").isDisplayed())){
										APP_LOGS.debug("Previous link is not visible (FAIL)");
										comments=comments+"Previous link is not visible (FAIL)";
									}
									
									APP_LOGS.debug("Previous link is visible (PASS)");
									comments=comments+"Previous link is visible (PASS)";
									
									getObject("TestersViewAll_enableNextLink").click();
									System.out.println("Next Link is clicked");
									
								
									Thread.sleep(1000);
									totalNoOftesters = getObject("TestersViewAll_testerAvailableInGrid").findElements(By.tagName("tr")).size();
									totalNoOftesters=totalNoOftesters+10;
									
									//next is disabled
									if(!assertTrue(getObject("TestersViewAll_disableNextLink").isDisplayed())){
										APP_LOGS.debug("Next link is not visible (FAIL)");
										comments=comments+"NExt link is not visible (FAIL)";
									}
									
									APP_LOGS.debug("Next link is visible (PASS)");
									comments=comments+"NExt link is visible (PASS)";
									
									getObject("TestersViewAll_enablePreviousLink").click();
									System.out.println("Previous link is clicked");
									Thread.sleep(1000);
									
									APP_LOGS.debug("Successfully shown all the Second page the tester in the Test Pass (PASS)");
									comments=comments+"Successfully shown all the Second in the Test Pass (PASS)";
								
								}else{
									Thread.sleep(1000);
									totalNoOftesters = getObject("TestersViewAll_testerAvailableInGrid").findElements(By.tagName("tr")).size();
									System.out.println("Tester count is : "+totalNoOftesters);
									
									//Previous is diabled
									if(!assertTrue(getObject("TestersViewAll_disablePreviousLink").isDisplayed())){
										APP_LOGS.debug("Previous link is not visible (FAIL)");
										comments=comments+"Previous link is not visible (FAIL)";
									}
									
									APP_LOGS.debug("Previous link is disable (PASS)");
									comments=comments+"Previous link is disable (PASS)";
									
									//next is disabled
									if(!assertTrue(getObject("TestersViewAll_disableNextLink").isDisplayed())){
										APP_LOGS.debug("Next link is not disable (FAIL)");
										comments=comments+"NExt link is not disable (FAIL)";
									}
									
									APP_LOGS.debug("Next link is visible (PASS)");
									comments=comments+"NExt link is visible (PASS)";
									
									APP_LOGS.debug("Successfully shown all the First page tester in the Test Pass (PASS)");
									comments=comments+"Successfully shown all the First page the tester in the Test Pass (PASS)";
								
								}
							}catch (Throwable t) {
							
								fail=true;
								APP_LOGS.debug("For Tester testerGrid element is not found ");
								comments=comments+"For Tester testerGrid element is not found (FAIL)";
								//take screen shot
								TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
								TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
							}
						
						APP_LOGS.debug("Closing Browser... ");
						System.out.println("Closing Browser... ");
						
						Thread.sleep(1000);
						closeBrowser();
				}
							
				else 
				{
					isLoginSuccess=false;
					APP_LOGS.debug("Login Not Successful");
					System.out.println("Login Not Successful");
					closeBrowser();
				}	
			}
			
			
			public boolean createTesterPagination(String group, String portfolio, String project, String version, String testPassName, 
					String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
			{
				APP_LOGS.debug("Creating Tester");
				Thread.sleep(1000);
				
				if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
				{
					getObject("TesterCreateNew_TesterActiveX_Close").click();
				}
								
				try {
						Thread.sleep(1000);
					
						getObject("TesterCreateNew_PeoplePickerImg").click();
						   
						driver.switchTo().frame(1);
						
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
						
						getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
						   
						getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
				   
						getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
				   
						getObject("TesterCreateNew_PeoplePickerOkBtn").click();
				   
						driver.switchTo().defaultContent();
						Thread.sleep(2000);
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
						
						Thread.sleep(2000);
						
						if (getElement("TesterCreateNew_testerSuccessMessageText_Id").getText().contains("successfully")) 
						{
							getObject("Tester_testeraddedsuccessfullyOkButton").click();
							
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
			public void reportTestResult(){
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
			}
			
			@DataProvider
			public Object[][] getTestData(){
				return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
			}
}
					
					