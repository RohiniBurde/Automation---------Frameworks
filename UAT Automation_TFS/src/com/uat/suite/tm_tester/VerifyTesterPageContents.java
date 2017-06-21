package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyTesterPageContents extends TestSuiteBase{

	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	boolean totalCountVisibility=false;
	// Runmode of test case in a suite
	Utility utilRecorder = new Utility();
			
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
			public void testVerifyTesterPageContents(String Role, String GroupName, String Portfolio, String Project, String Version,String TestPassName, String TP_Tester,
					String TesterRoleCreation,String testerDescription, String ExpectedNoTesterText,String TesterRoleSelection,String EndMonth,String EndYear,String EndDate,String TestManager, String VersionLead,String ExpectedPageHeadingText) throws Exception
			{
				count++;
				
				if(!runmodes[count].equalsIgnoreCase("Y")){
					skip=true;
					APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

					throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
				}
				comments = "";
				
				int tester_count = Integer.parseInt(TP_Tester);
				tester = getUsers("Tester", tester_count);
				
				int testManager_count = Integer.parseInt(TestManager);
				testManager = getUsers("Test Manager", testManager_count);
				
				int versionLead_count = Integer.parseInt(VersionLead);
				versionLead = getUsers("Version Lead", versionLead_count);
				
				openBrowser();
				
				APP_LOGS.debug("Opening Browser... ");
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
					
					try
					{
				
						//Step 1:  clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						
						
	
						for(int i=1;i<=2;i++)
						{
							if(!createProject(GroupName, Portfolio, Project+i, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
							{
								fail=true;
								APP_LOGS.debug("Project Creation fails");
								comments=comments+"Project Creation Fails";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");
								
								closeBrowser();
								assertTrue(false);
								throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project is not created successfully");//reports
							}
						}
						
						closeBrowser();
						
						openBrowser();
						APP_LOGS.debug("Opening Browser... ");
						
						if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
						{
							//clicking on Test Management Tab
							//Thread.sleep(2500);
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(500);
							
	
							for(int i=1;i<=2;i++)
							{
								if(!createTestPass(GroupName, Portfolio, Project+i, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
								{
									fail=true;
									APP_LOGS.debug("Test Pass Creation Fails");
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
									closeBrowser();
									assertTrue(false);
									throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
								}
							}
							
							closeBrowser();
							
							openBrowser();
							APP_LOGS.debug("Opening Browser... ");
							
							if(login(testManager.get(0).username,testManager.get(0).password,"Test Manager"))
							{
							
							
								//Thread.sleep(1000);
								//clicking on Test Management Tab
								getElement("UAT_testManagement_Id").click();
								Thread.sleep(500);
								//Thread.sleep(2000);
								//clicking on Test Pass Tab
								getElement("TM_testersTab_Id").click();
								Thread.sleep(500);
								
								
								//Thread.sleep(1000);					
								if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
								{
									getObject("TesterCreateNew_TesterActiveX_Close").click();
								}
								
								if(!verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1", GroupName))
								{
									comments+="Could not find group name in group dropdown.. (FAIL)";
									throw new SkipException("Could not find group name in group dropdown");
								}
								
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
									APP_LOGS.debug("User " + Role+ " is not being landed on 'Testers' Page (FAIL)");
									comments="User " + Role+ " is not being landed on 'Testers' Page (FAIL)";
									throw new SkipException("User " + Role+ " is not being landed on 'Testers' Page");
								}
									
								APP_LOGS.debug("User " + Role+ " is being landed on 'Testers' Page successfully ");
								comments="User " + Role+ " is being landed on 'Testers' Page successfully (PASS)";
	
								if(assertTrue(getObject("Testers_viewAllLink").isDisplayed()))
								{
									APP_LOGS.debug("View All Link is displayed successfully");
									comments=comments+"View All Link is displaying successfully (PASS)";	
								}
								else
								{
									fail=true;
									APP_LOGS.debug("View All Link is not displayed");
									comments=comments+"View All Link is not displayed (FAIL)";
								}
								
								if(assertTrue(getObject("Testers_addTesterLink").isDisplayed()))
								{
									APP_LOGS.debug("Add Tester Link is displayed successfully");
									comments=comments+"Add Tester Link is displaying successfully (PASS)";	
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Add Tester Link is not displayed.");
									comments=comments+"Add Tester Link is not displayed (FAIL)";	
								}
								
				
								/*if(assertTrue(getObject("Testers_downloadUploadTemplateLink").isDisplayed()))
								{
									APP_LOGS.debug("Download and Upload Template Link is displayed successfully");
									comments=comments+"Download and Upload Template Link is displayed successfully(PASS)";
									
								}else
								{
									
									fail=true;
									APP_LOGS.debug("Download and Upload Template Link is displayed successfully");
									comments=comments+"Download and Upload Template Link is not displaying successfully (FAIL)";		
								}*/
								
								
								//******Whether table  is displayed or no testers text is displaying*******
								
								int testerTableAvailableCount = getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size();
									
									//verifying whether table is available or not
								if (compareIntegers(1, testerTableAvailableCount))
								{
									APP_LOGS.debug("No Testers Available message was highlighted");
									comments=comments+"No Testers Available message was highlighted(PASS)";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("No Testers Available message was not highlighted but it should have been");
									comments=comments+"No Testers Available message was not highlighted but it should have been (FAIL)";
								}
								if(!compareStrings(getObject("TestersViewAll_withoutTester").getText(), ExpectedNoTesterText))
								{
									fail=true;
									APP_LOGS.debug("No Tester(s) Available! text is not found");
									comments=comments+"No Tester(s) Available! is not found in absence of teser (FAIL)";	
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Absence of tester message not displayed");
								}
								else
								{
									APP_LOGS.debug("No Tester(s) Available!");
									comments=comments+"No Tester(s) Available! is not found in absence of teser(PASS)";	
								}
										
										// add tester for the test pass
								if(!createTester(GroupName, Portfolio, Project+"2", Version, TestPassName, tester.get(0).username, TesterRoleCreation, testerDescription, TesterRoleSelection))
								{
									totalCountVisibility=false;
									APP_LOGS.debug("Tester is not created successfully....");
									comments=comments+"Tester is not created successfully (FAIL)";
	
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
									throw new SkipException("FailedCreatingTester");
								}
								APP_LOGS.debug("Tester created successfully....");
								comments=comments+"Tester is created successfully (PASS)";
									
								//getObject("Testers_viewAllLink").click();
								
								if(!assertTrue(getObject("TestersViewAll_totalRecordCount").isDisplayed()))
								{
									fail=true;
									APP_LOGS.debug("Total Record Count is not being displayed");
									comments=comments+"Total Record Count is not being displayed in presence of tester (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Record Count not displayed in presence of tester");
								}
								else
								{
									APP_LOGS.debug("Total Records: "+getObject("TestersViewAll_totalRecordCount").getText());
									comments=comments+"Total Record Count is being displayed (PASS)";	
								}
								testerTableAvailableCount = getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size();
								
								//verifying whether table is available or not
								if (compareIntegers(1, testerTableAvailableCount))
								{
									fail=true;
									APP_LOGS.debug("No Testers Available message was highlighted even after testers were added");
									comments=comments+"No Testers Available message was highlighted even after testers were added(Fail)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No testers available message shown after adding testers");
								}
								else
								{	
									APP_LOGS.debug("No Testers Available message was not highlighted after adding testers");
									comments=comments+"No Testers Available message was not highlighted after adding testers(PASS)";
								}	
								int testerTableAvailableGrid = 	getObject("Tester_NumberOfAddedTesters").findElements(By.tagName("tr")).size();
								
								if(compareIntegers(1, testerTableAvailableGrid))
								{
									comments+="As 1 tester was added, corresponding entry is being shown in the grid (PASS)";
								}
								else
								{
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Table was not seen after tester creation");
									comments+="Table was not seen after tester creation (FAIL)";
									throw new SkipException("Table was not seen after tester creation");
								}
								int numOfColumnsInGrid = getObject("Tester_NumberOfAddedTesters").findElements(By.tagName("tr")).get(0).findElements(By.tagName("td")).size();
								
								if(compareIntegers(4, numOfColumnsInGrid))
								{
									comments+="Number of expected columns in tester grid is 4 and it was same. (PASS)";
								}
								else
								{
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected number of rows was not found in tester grid.");
									comments+="Expected number of rows was not found in tester grid (FAIL)";
									throw new SkipException("Expected number of columns was not found in tester grid");
								}
								
								getObject("TestersViewAll_testerRoleEditImgInGrid").click();
								Thread.sleep(500);
								
								if(!assertTrue(getElement("TesterEditTester_editTestertab_Id").isDisplayed()))
								{
									fail=true;
									comments+="Edit link should have been displayed after clicking edit icon (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit link should have been displayed after clicking edit icon");
								}
								else
								{
									comments+="Edit link should is being displayed after clicking edit icon (Pass)";
									if(!assertTrue(getElement("Tester_EditForm_Id").isDisplayed()))
									{
										fail=true;
										comments+="Edit page should have been displayed after clicking edit icon (FAIL)";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit page should have been displayed after clicking edit icon");
									
									}
									else
										comments+="Edit page should is being displayed after clicking edit icon (Pass)";
								}
								
								
								//Thread.sleep(1000);									
								
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Login Not Successful for test Manager");
								comments+="Login Not Successful for Test Manager (FAIL)";
							}
							
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Login Not Successful for version Lead");
							comments+="Login Not Successful for version Lead (FAIL)";
						}
							
					}	
					catch(Throwable t)
					{
						fail=true;
						assertTrue(false);
						comments+="Some exception occured while testing tester page (FAIL)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Some exception occured while testing tester page");
						
					}
					
					closeBrowser();
						
				}
				
				else 
				{
					fail=true;
					APP_LOGS.debug("Login Not Successful");
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
