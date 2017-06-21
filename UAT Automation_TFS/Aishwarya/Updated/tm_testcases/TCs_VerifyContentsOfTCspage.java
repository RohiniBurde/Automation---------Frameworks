package com.uat.suite.tm_testcases;

import java.util.ArrayList;

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

public class TCs_VerifyContentsOfTCspage extends TestSuiteBase 
{
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	String comments;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip() throws Exception
		{
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			testManager=new ArrayList<Credentials>();
			testers=new ArrayList<Credentials>();
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
	
		// Test Case Implementation ...
			
		@Test(dataProvider="getTestData")
		public void Test_TCs_VerifyContentsOfTCspage (String Role,String GroupName,String PortfolioName,String ProjectName, String Version,String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,String TestManager,String Tester,String AddRole,String expectedProjectsTabText,String expectedTestPassesText,String expectedTestersTabText,String expectedTestCasesTabText,String expectedTestStepsTabText,String expectedAttachmentsTabText) throws Exception
		{
				count++;
				comments="";
				
				if(!runmodes[count].equalsIgnoreCase("Y"))
				{
					skip=true;
					APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
		
					throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
				}
				
				int testManager_count = Integer.parseInt(TestManager);
				
				//testManager=new ArrayList<Credentials>();
				testManager = getUsers("Test Manager", testManager_count);
				
				
				int versionLead_count = Integer.parseInt(VersionLead);
				
				//versionLead=new ArrayList<Credentials>();
				versionLead = getUsers("Version Lead", versionLead_count);
				
				int testers_count = Integer.parseInt(Tester);			
				testers = getUsers("Tester", testers_count);
				
				APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
				
				APP_LOGS.debug("Opening Browser... ");
				
				openBrowser();
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
					try
					{
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(1000);
						
						APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
						
						if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{	
							//fail=true;
							//assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
							comments="Project Creation Unsuccessful(Fail) by "+Role+"  . ";
							APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
							//closeBrowser();
							throw new SkipException("Project Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						closeBrowser();
					
						APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
						openBrowser();
						
						if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
						{
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							Thread.sleep(1000);
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
							
							if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{		
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
								comments="Test Pass Creation Unsuccessful(Fail)   ." ;
								APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
								//closeBrowser();
								throw new SkipException("Test Pass Creation Unsuccessfull");
							}
							
							APP_LOGS.debug("Closing Browser... ");
							closeBrowser();					

							APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
							openBrowser();
						
							if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
							{
								//click on testManagement tab
								APP_LOGS.debug("Clicking On Test Management Tab ");
								Thread.sleep(1000);
								getElement("UAT_testManagement_Id").click();
							
								Thread.sleep(2000);
							
								if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
								{	
									//fail=true;
								//	assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
									comments="Tester Creation Unsuccessful(Fail)" ;
									APP_LOGS.debug("Tester Creation Unsuccessfull");
									//closeBrowser();
									throw new SkipException("Tester Creation Unsuccessfull");
								}
							
								APP_LOGS.debug("Clicking on Test Cases Tab");
								getElement("TestCaseNavigation_Id").click();
							
								APP_LOGS.debug("Verifying if Navigation Panel is Displaying Properly");
							
								if(getElement("TM_LeftPanel_Id").isDisplayed())
								{
									APP_LOGS.debug("Navigation Panel is Displayed Properly");
									comments+="Navigation Panel is Displayed Properly(Pass). ";
								}
								else
								{	
									fail=true;
									APP_LOGS.debug("Navigation Panel is not displaying properly: Failed  ");
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NavigationPanelDisplayIssue");
									comments=comments+"Navigation Panel is not displaying properly(Fail)   .";
								}
							
								Thread.sleep(1000);

								// Verifying Text Of PROJECTS Tab
								String projectsTab_Text=getElement("TM_projectsTab_Id").getText();
								if(compareStrings(expectedProjectsTabText, projectsTab_Text))
								{
									APP_LOGS.debug(expectedProjectsTabText +": Project Tab Text Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectTabTextIssue");
									comments=comments+"Project Tab Text - NOT Present(Fail)     .";
									APP_LOGS.debug("Project Tab Text - NOT Present : Failed. ");
								}
								
								// Verifying Text Of TEST PASSES Tab
								
								String testPassesTab_Text=getElement("TM_testPassesTab_Id").getText();
								if(compareStrings(expectedTestPassesText, testPassesTab_Text))
								{
									APP_LOGS.debug(expectedTestPassesText +" :TEST PASSES Tab Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassesTabTextIssue");
									comments=comments+" TEST PASSES Tab Text - NOT Present(Fail)     .";
									APP_LOGS.debug("TEST PASSES Tab Text - NOT Present  : Failed. ");
								}
								
								// Verifying Text Of TESTERS Tab
								
								String testersTab_Text=getElement("TM_testersTab_Id").getText();
								if(compareStrings(expectedTestersTabText,testersTab_Text))
								{
									APP_LOGS.debug(expectedTestersTabText+" :TESTERS Text Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestersTabTextIssue");
									comments=comments+" TESTERS Tab Text - NOT Present(Fail)     .";
									APP_LOGS.debug("TESTERS Tab Text - NOT Present  : Failed. ");
								}

								// Verifying Text Of TEST CASES Tab
								
								String testcasesTab_Text=getElement("TM_testCasesTab_Id").getText();
								if(compareStrings(expectedTestCasesTabText,  testcasesTab_Text))
								{
									APP_LOGS.debug(expectedTestersTabText+" :TEST CASES Text Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCasesTabTextIssue");
									comments=comments+"Test Cases Tab Text - NOT Present(Fail)    .";
									APP_LOGS.debug("TEST Cases Tab Text - NOT Present  : Failed. ");
								}
								
								// Verifying Text Of TEST STEPS Tab
								
								String testStepsTab_Text=getElement("TM_testStepsTab_Id").getText();
								if(compareStrings(expectedTestStepsTabText, testStepsTab_Text))
								{
									APP_LOGS.debug(expectedTestStepsTabText+ " :TEST STEPS Tab Text Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsTabTextIssue");
									comments=comments+"Test Steps Tab Text - NOT Present(Fail)    .";
									APP_LOGS.debug("Test Step Tab Text - NOT Present  : Failed. ");
								}
								
								// Verifying Text Of ATTACHMENTS Tab
								
								String attachmentsTab_Text=getElement("TM_attachmentsTab_Id").getText();
								if(compareStrings(expectedAttachmentsTabText,attachmentsTab_Text))
								{
									APP_LOGS.debug(expectedAttachmentsTabText + " :ATTACHMENTS  Tab Text Showing properly...");
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentsTaBTextIssue");
									comments=comments+"Attachments Tab Text - NOT Present(Fail)    .";
									APP_LOGS.debug("Attachments Tab Text NOT Present  : Failed. ");
								}
								
								// Verifying VIEW ALL Link is available or not
								
								String viewAllLinkText=getObject("TestCases_viewAllTestCasesLink").getText();
								//driver.findElement(By.linkText(resourceFileConversion.getProperty("TM_TestCasesTab_ViewAllLink_Text"))).isDisplayed();
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_ViewAllLink_Text"),viewAllLinkText))
								{
									comments+="TEST CASES Page VIEW ALL Link : is showing properly(Pass). ";
									APP_LOGS.debug("TEST CASES Page VIEW ALL Link : is showing properly..");
								}
								else
								{
									fail=true;
									APP_LOGS.debug("TEST CASES Page VIEW ALL Link : Broken Or NOT Present");
									comments=comments+"TEST CASES Page VIEW ALL Link : Broken Or NOT Present(Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCases_ViewAllTextBrokenOrNotPresent ");
								}
								
								// Verifying CREATE NEW Link is available or not
								
								String CreateNewLinkText=getObject("TestCases_createNewTestCasesLink").getText();
								
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_CreateNewLink_Text"),CreateNewLinkText))
								{
									comments+="TEST CASES Page CREATE NEW Link : is showing properly(Pass). ";
									APP_LOGS.debug("TEST CASES Page CREATE NEW Link : is showing properly..");
								}
								else
								{
									APP_LOGS.debug("TEST CASES Page CREATE NEW Link : Broken Or NOT Present");
									comments=comments+"TEST CASES Page CREATE NEW Link : Broken Or NOT Presentt(Fail)  .";		
									fail=true;
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCases_CreateNewTextBrokenOrNotPresent ");
								}
								
								// Verifying DOWNLOAD UPLOAD TEMPLATE Link is available or not
								
						/*		String downloadUploadTemplateLinkText=getElement("TestCasesDownload_UploadTemplateTestCasesLink_Id").getText();
									
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_Download/UploadTemplateLink_Text"),downloadUploadTemplateLinkText))
								{
									comments+="TEST CASES Page Download/Upload Template Link : is showing properly(Pass). ";
									APP_LOGS.debug("TEST CASES Page Download/Upload Template Link : is showing properly..");
								}
								else
								{
									
									APP_LOGS.debug("TEST CASES Page Download/Upload Template Link : Broken Or NOT Present");
									comments=comments+" TEST CASES Page Download/Upload Template Link : Broken Or NOT Present(fail)  .";
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCases_DownloadUploadTemplateTextBrokenOrNotPresent ");
								}
*/								
								
								// Verifying COPY Link is available or not
								
								String COPY_LinkText=getElement("TestCases_CopyTestCasesLink_Id").getText();
									
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_CopyLink_Text"),COPY_LinkText))
								{
									comments+="TEST CASES Page COPY Link : is showing properly(Pass). ";
									APP_LOGS.debug("TEST CASES Page COPY Link : is showing properly..");
								}
								else
								{
									comments=comments+"TEST CASES Page COPY Link Link : Broken Or NOT Present(fail).";
									APP_LOGS.debug("TEST CASES Page COPY Link Link : Broken Or NOT Present(fail)");
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCases_COPYTextBrokenOrNotPresent ");
								}
								
								//Verifying " No Test Case(es) Available" Text is present or not when No test Cases available on Page
								
								String noTestCaseAvailText =getElement("TestCases_NoTestCasesAvailable_Id").getText();
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_NoTestCasesAvailMessage"),noTestCaseAvailText))
								{
									comments+="No Test Case(es) Available Message : is showing properly(Pass). ";
									APP_LOGS.debug("No Test Case(es) Available Message : is showing properly..");
								}
								else
								{
									APP_LOGS.debug("No Test Case(es) Available Message :: Broken Or NOT Present");
									comments=comments+" No Test Case(es) Available Message :: Broken Or NOT Present(fail)  .";
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No_Test_Case(es)_Available_MessageBrokenOrNotPresent ");
								}

								//Verifying Header " Test Case Details " Text is present or not 
								
								String headTestCaseDetailsText =getObject("TestCases_TestStepDetailsMessage").getText();
								
								if(compareStrings(resourceFileConversion.getProperty("TM_TestCasesTab_TestCaseDetailsText"),headTestCaseDetailsText))
								{
									comments+="Test Case Page Header is showing properly(Pass). ";
									APP_LOGS.debug("Test Case Page Header is showing properly..");
								}
								else
								{
									comments=comments+"Test Case Page Header Broken Or NOT Present(fail).";
									APP_LOGS.debug("Test Case Page Header Broken Or NOT Present");
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCasePageHeaderBrokenOrNotPresent");
								}
								
								//Verifying the navigation panel dropdowns
								
								if(getElement("TestCases_Tiles_Group_Id").isDisplayed())
								{	
									String groupTitleName=getElement("TestCases_Tiles_Group_Id").getText();
									comments+="Group Name Tile Group Name: "+ groupTitleName+ " is Present(Pass). ";
									APP_LOGS.debug("Group Name Tile Group Name: "+ groupTitleName+ " is Present");
								}
								else
								{
									APP_LOGS.debug("Group Name tile is not present(failed)");
									comments=comments+"Group Name tile is not present(fail).";
									fail=true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"GroupNameTile_BrokenOrNotPresent");
								}
								
								if(getElement("TestCases_Tiles_Portfolio_Id").isDisplayed())
								{	
									String portfolioTitleName=getElement("TestCases_Tiles_Portfolio_Id").getText();
									comments+="Portfolio Name Tile with Portfolio Name: "+ portfolioTitleName+ " is Present(Pass). ";
									APP_LOGS.debug("Portfolio Name Tile with Portfolio Name: "+ portfolioTitleName+ " is Present");
									
								}
								else
								{
									APP_LOGS.debug("Portfolio Name tile is not present(failed)");
									comments=comments+"Portfolio Name tile is not present(fail).";
									fail=true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"PortfolioTile_BrokenOrNotPresent");
								}
								
								if(getElement("TestCases_Tiles_Project_Id").isDisplayed())
								{	
									String projectTitleName=getElement("TestCases_Tiles_Project_Id").getText();
									comments+="Project Name Tile with Project Name: "+ projectTitleName+ " is Present(Pass). ";
									APP_LOGS.debug("Project Name Tile with Project Name: "+ projectTitleName+ " is Present");
								}
								else
								{
									APP_LOGS.debug("Project Name tile is not present(failed)");
									comments=comments+" Project Name tile is not present(fail).";
									fail=true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectNameTile_BrokenOrNotPresent");
								}
								
								if(getElement("TestCases_Tiles_Version_Id").isDisplayed())
								{	
									String versionTitleName=getElement("TestCases_Tiles_Version_Id").getText();
									comments+="Version Name Tile with Version Name: "+ versionTitleName+ " is Present(Pass). ";
									APP_LOGS.debug("Version Name Tile with Version Name: "+ versionTitleName+ " is Present");
								}
								else
								{
									APP_LOGS.debug("Version Name: tile is not present(failed)");
									comments=comments+" Version Name: tile is not present(fail).";
									fail=true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"VersionNameTile_BrokenOrNotPresent");
								}
								
								if(getElement("TestCases_Tiles_TestPass_Id").isDisplayed())
								{	
									String testPassTitleName=getElement("TestCases_Tiles_TestPass_Id").getText();
									comments+="TestPass Name Tile with Tess Pass Title : "+ testPassTitleName+ " is Present(Pass). ";
									APP_LOGS.debug("TestPass Name Tile with Tess Pass Title : "+ testPassTitleName+ " is Present");
								}
								else
								{
									APP_LOGS.debug("Test Pass Name tile is not present(failed)");
									comments=comments+" Test Pass Name tile is not present(fail).";
									fail=true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPassNameTile_BrokenOrNotPresent");
								}
								
								APP_LOGS.debug("Contents of the Test Cases page Verified Successfully");
								//comments=comments+"Contents of the Test Cases page Verified Successfully (Pass)    .";
							
								APP_LOGS.debug("Closing Browser... ");
								closeBrowser();
							}
							else 
							{
								fail= true;
								comments+="Test Manager Login Not Successful";
								APP_LOGS.debug("Test Manager Login Not Successful");
							}	
						}	
						else 
						{
							fail= true;
							comments+="Version Lead Login Not Successful";
							APP_LOGS.debug("Version Lead Login Not Successful");
						}
					}	
					catch (Throwable e) 
					{
						e.printStackTrace();
						assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
						comments+="Skip Exception or other exception occured" ;
					}
			//	closeBrowser();
			}
			else 
			{
				APP_LOGS.debug("Login Not Successful");
			}	
		}	

		@AfterMethod
		public void reportDataSetResult()
		{
			if(skip)
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
					
			else if(!isLoginSuccess)
			{
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			}
			else if(fail)
			{
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else
			{
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;
		}
				
		@AfterTest
		public void reportTestResult() throws Exception
		{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}

		@DataProvider
		public Object[][] getTestData()
		{
			return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
		}
				
}
