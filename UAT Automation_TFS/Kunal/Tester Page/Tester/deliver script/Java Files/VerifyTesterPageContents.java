package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyTesterPageContents extends TestSuiteBase{

	
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
	int projectsPerPage;
	int projectLimitPerPage=10;
	String ProjectII;
	String ProjectI; 
	
	// Runmode of test case in a suite
	
			@BeforeTest
			public void checkTestSkip(){
				APP_LOGS.debug("Begining of the Verify Tester Page Content");
				System.out.println("Begining of the Verify Tester Page Content");
				
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
			public void testVerifyTesterPageContents(String Role, String GroupName, String Portfolio, String Project, String Version,String TestPassName, String TP_Tester,
					String TesterRoleCreation, String ExpectedNoTesterText,String TesterRoleSelection,String EndMonth,String EndYear,String EndDate,String TestManager, String VersionLead,String ExpectedPageHeadingText) throws Exception
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
						
					} catch (Throwable t) {
						fail=true;
						APP_LOGS.debug("Clicking On Project Tab is failed");
						comments=comments+"Clicking On Project Tab is failed";
					}
					
					ProjectI=Project+"1";					
					if(!createProject(GroupName, Portfolio, ProjectI, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Project Creation fails");
						comments=comments+"Project Creation Fails";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					System.out.println("Successfully created the Project for second Project");
					
					ProjectII =Project+"2";					
					if(!createProject(GroupName, Portfolio, ProjectII, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Project Creation fails");
						comments=comments+"Project Creation Fails";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					
					System.out.println("Successfully created the Project for second Project");
					
					Thread.sleep(1000);
					closeBrowser();
					
					openBrowser();
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					
					login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead");
					
					try {
						Thread.sleep(1000);
						//clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
						/*//clicking on Test Pass Tab
						getElement("TM_testPassesTab_Id").click();
						APP_LOGS.debug("Clicking On Test Pass Tab ");
						System.out.println("Clicking On Test Pass Tab ");*/
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("Clicking On Test Pass Tab is failed");
						System.out.println("Clicking On Test Pass Tab is failed");
						
					}
					
					
					if(!createTestPass(GroupName, Portfolio, ProjectI, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Test Pass Creation Fails");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					System.out.println("Successfully created the Test Pass for first Project");
					
					
					if(!createTestPass(GroupName, Portfolio, ProjectII, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Test Pass Creation Fails");
						comments=comments+"Test Pass Creation Fails";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					
					System.out.println("Successfully created the Test Pass for second Project");
					Thread.sleep(1000);
					closeBrowser();
					
					openBrowser();
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					
					login(testManager.get(0).username,testManager.get(0).password,"Test Manager");
					
					try {
						Thread.sleep(1000);
						//clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						
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
					int groupResult = verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1", GroupName);
					System.out.println("if 1 means group exist else not exist: "+groupResult);
					
					/* if(groupResult==0){
						 createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username);
						 createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username);
						 getObject("Testers_testerTab").click();
						 verifyHeaders("Testers_groupDropDown_Id", "Attachments_groupNameDDElemnts", "Attachment_groupNameDDXpath1", GroupName);
					 }*/
					
					String testersTabHighlighted = getObject("Testers_testerTab").getAttribute("class");
					
					if(!compareStrings(testersTabHighlighted,OR.getProperty("Testers_testerTab_Class")))
					{
						fail=true;
						APP_LOGS.debug("Tester Tab is not Highlighted");
						comments=comments+"Tester Tab is not Highlighted";
						
					}
					
					APP_LOGS.debug("Testers Tab is Highlighted ");
					System.out.println("Testers Tab is Highlighted ");
					
					if(!compareStrings(getObject("Testers_testersDetailsHeading").getText(),ExpectedPageHeadingText)){
						fail=true;
						APP_LOGS.debug("User " + Role+ " is not landing on 'Testers' Page (FAIL)");
						comments="User " + Role+ " is not landing on 'Testers' Page (FAIL)";
					}
						APP_LOGS.debug("User " + Role+ " is landing on 'Testers' Page successfully ");
						comments="User " + Role+ " is landing on 'Testers' Page successfully (PASS)";
					
					
					try {
						
						getObject("Testers_viewAllLink").isDisplayed();
						APP_LOGS.debug("View All Link is displayed successfully");
						comments=comments+"View All Link is displaying successfully (PASS)";	
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("View All Link is not displaying successfully");
						comments=comments+"View All Link is not displaying successfully (FAIL)";	
					}
					
					try {
						
						getObject("Testers_addTesterLink").isDisplayed();
						APP_LOGS.debug("Add Tester Link is displayed successfully");
						comments=comments+"Create New Link is not displaying successfully (PASS)";	
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("Add Tester Link is not displaying successfully");
						comments=comments+"Add Tester Link is not displaying successfully (FAIL)";	
					}
									
					try {
						
						getObject("Testers_downloadUploadTemplateLink").isDisplayed();
						APP_LOGS.debug("Download and Upload Template Link is displayed successfully");
						comments=comments+"Download and Upload Template Link is displayed successfully(PASS)";
						
					} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("Download and Upload Template Link is displayed successfully");
						comments=comments+"Download and Upload Template Link is not displaying successfully (FAIL)";		
						
					}
					
					
					//******Whether table  is displayed or no testers text is displaying*******
					
					
					try {
						int testerTableAvailableCount = getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size();
						
						//verifying whether table is available or not
						if (testerTableAvailableCount==1) {
							
							
							if(!compareStrings(getObject("TestersViewAll_withoutTester").getText(), ExpectedNoTesterText))
							{
								fail=true;
								APP_LOGS.debug("No Tester(s) Available! text is not found");
								comments=comments+"No Tester(s) Available! (FAIL)";	
								
							}
							APP_LOGS.debug("No Tester(s) Available!");
							comments=comments+"No Tester(s) Available! (PASS)";	
							
							// add tester for the test pass
							if(!createTester(GroupName, Portfolio, ProjectII, Version, TestPassName, tester.get(0).username, TesterRoleCreation, TesterRoleSelection))
							{
								fail=true;
								totalCountVisibility=false;
								APP_LOGS.debug("Tester is not created successfully....");
								comments=comments+"Tester is not created successfully (FAIL)";

								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
								
							}
							APP_LOGS.debug("Tester created successfully....");
							comments=comments+"Tester is created successfully (PASS)";
								
							getObject("Testers_viewAllLink").click();
							
							if(!assertTrue(getObject("TestersViewAll_totalRecordCount").isDisplayed())){
								
								fail=true;
								APP_LOGS.debug("Total Record Count is not being displayed");
								comments=comments+"Total Record Count is not being displayed (FAIL)";
								
							}
							APP_LOGS.debug("Total Records: "+getObject("TestersViewAll_totalRecordCount").getText());
							comments=comments+"Total Record Count is being displayed (PASS)";	
							}
						else{
							
							//Tester is already available total count is displayed
							if(!assertTrue(getObject("TestersViewAll_totalRecordCount").isDisplayed())){
								fail=true;
								APP_LOGS.debug("Total Record Count is not being displayed");
								comments=comments+"Total Record Count is not being displayed (FAIL)";
							}
							APP_LOGS.debug("Total Records: "+getObject("TestersViewAll_totalRecordCount").getText());
							comments=comments+"Total Record Count is being displayed (PASS)";
							
							}
						} catch (Throwable t) {
						
						fail=true;
						APP_LOGS.debug("For Tester testerGrid element is not found ");
						comments=comments+"For Tester testerGrid element is not found (FAIL)";
						
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
