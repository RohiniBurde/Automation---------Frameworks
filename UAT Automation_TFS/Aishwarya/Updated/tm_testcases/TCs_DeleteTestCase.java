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

public class TCs_DeleteTestCase extends TestSuiteBase {
	
	
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
	//static String testCaseCreatedSuccessMessage;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());

		if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		testers=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	//Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void deleteTestCase (String Role,String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,String TestManager,
			String Tester,String AddRole,String TestCaseName,String ExpectedtestCaseDeleteConfirmationMessage,
			String ExpectedTestCaseDeletedSuccessMessage, String testStep1, String testStep2 ) throws Exception
	{
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);
			
			int testManager_count = Integer.parseInt(TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			int testers_count = Integer.parseInt(Tester);			
			testers = getUsers("Tester", testers_count);
			
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
						comments+="Project Creation Unsuccessful(Fail) by "+Role+". ";
						APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
						//closeBrowser();
						throw new SkipException("Project Creation Unsuccessful");
					}

					APP_LOGS.debug("Closing Browser... ");
					closeBrowser();
					
					APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
					openBrowser();
					
					if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
					{
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						Thread.sleep(500);
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
						{	
							//fail=true;
							//assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
							comments+="Test Pass Creation Unsuccessful(Fail). " ;
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail) ");
							//closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessful");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						closeBrowser();

						APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
						openBrowser();
						
						if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
						{
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							Thread.sleep(500);
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
							
							if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
								comments+="Tester Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Tester Creation Unsuccessful");
								//closeBrowser();
								throw new SkipException("Tester Creation Unsuccessful");
							}
							
							APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
							
							if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName,TestCaseName))
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
								APP_LOGS.debug("Test Case Creation Unsuccessfull");
								comments+="Test Case Creation Unsuccessful(Fail). " ;
								//closeBrowser();
								throw new SkipException("Test Case Creation Unsuccessfull");
							}								
							
							if (!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName, testStep1, "", TestCaseName, AddRole))
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStep1CreationFailure");
								APP_LOGS.debug("Test Step 1 Creation Unsuccessfull");
								comments+="Test Step 1 Creation Unsuccessful(Fail). " ;
								//closeBrowser();
								throw new SkipException("Test Step 1 Creation Unsuccessfull");
							}
							
							if (!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName, testStep2, "", TestCaseName, AddRole))
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStep2CreationFailure");
								APP_LOGS.debug("Test Step 2 Creation Unsuccessfull");
								comments+="Test Step 2 Creation Unsuccessful(Fail). " ;
								//closeBrowser();
								throw new SkipException("Test Step 2 Creation Unsuccessfull");
							}
							
							//Clicking On View All Link Of Test Cases
							
							getElement("TestCaseNavigation_Id").click();

/*								APP_LOGS.debug("Test Cases :Clicking On View All Link");
								Thread.sleep(500);								
								
								//getObject("TestCases_viewAllTestCasesLink").click();																									
*/								
							String ActualTestCaseNamePresentInTestCaseGrid=getObject("TestCases_ViewAll_TestCaseNameText").getText();	
							if (compareStrings(TestCaseName,ActualTestCaseNamePresentInTestCaseGrid))
							{									
								APP_LOGS.debug("CLicking On Delete Button ");
								Thread.sleep(2000);
								
								getObject("TestCases_ViewAll_TestCaseDeleteButton").click();
								
								String TestCaseDeleteConfirmationMessage=getElement("TestCases_ViewAll_deleteTestCasesConfirmationText_Id").getText();
								
								if(compareStrings(ExpectedtestCaseDeleteConfirmationMessage, TestCaseDeleteConfirmationMessage))
								{
									APP_LOGS.debug("TEST CASES :  Delete Button Confirmation Message showing Properly stating : " + TestCaseDeleteConfirmationMessage);
									comments+="Test Case Delete Confirmation Message is correct (Pass). ";	
								}
								else
								{	
									fail=true;												
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseDeleteConfirmMessage");
									APP_LOGS.debug("Test Case Delete Confirmation Message is Not correct ");
									comments+="Test Case Delete Confirmation Message is Not correct (Fail). ";												
									
								}

								Thread.sleep(1000);
								
								APP_LOGS.debug("Clicking On Delete Button to delete Test Case");
			
								getObject("ProjectViewAll_PopUpDeleteButton").click();
								
								//Verifying Test cases Deleted COnfirmation Message....
								
								String actualTestCasesDeletedSuccessMessage = getElement("TestCases_ViewAll_TestCasesDeletedSuccessMessage_Id").getText();
							    
								if(compareStrings(actualTestCasesDeletedSuccessMessage, ExpectedTestCaseDeletedSuccessMessage ))
								{	
										APP_LOGS.debug("Test Case Deleted Success Message Stating :  "+ actualTestCasesDeletedSuccessMessage);
										comments+="Test Case Delete Success Message is correct (Pass). ";	
								}
								else
								{
									fail=true;	
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseDeleteSuccessMessage");
									APP_LOGS.debug("Test Case Delete Success Message is Not showing ");
									comments+="Test Case Delete Success Message is Not correct (Fail). ";	
								}
							
						//		getObject("TestCases_ViewAll_DeleteConfirmOkButton").click();
							
								Thread.sleep(1000);
								
								APP_LOGS.debug("Verifying Deleted Test Case is showing on test Case Table or Not ?");
								
								if(getElement("TestCases_NoTestCasesAvailable_Id").isDisplayed())
								{
									APP_LOGS.debug("(PASS)Test Case name : "+ ActualTestCaseNamePresentInTestCaseGrid +" Deleted Successfully");
									comments+="Test Case Name "+ ActualTestCaseNamePresentInTestCaseGrid+" Deleted Successfully(PASS). ";
								}
								else
								{
									fail=true;
									assertTrue(false);
									APP_LOGS.debug("Test Case is not Deleted (Fail) ");
									comments+="Test Case is not Deleted (Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case not Deleted");
								}
								
								getElement("TestStepNavigation_Id").click();
								
								Thread.sleep(1500);
								
								if(getElement("TestSteps_noTestStepAvailable_Text_Id").isDisplayed())
								{
									APP_LOGS.debug("Associated Test Steps Deleted Successfully");
									comments+="Associated Test Steps Deleted Successfully(PASS). ";
								}
								else
								{
									fail=true;
									assertTrue(false);
									APP_LOGS.debug("Associated Test Steps not Deleted(Fail) ");
									comments+="Associated Test Steps not Deleted(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Associated Test Step not Deleted");
								}									
								//closeBrowser();
							}
							else
							{
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CreatedTestCaseNotPresentToDelete");
								APP_LOGS.debug("Created Test Case is Not Present in TestCse Table to Delete");
								comments+="Created Test Case is Not Present in TestCse Table to Delete (Fail). ";	
							}
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
				catch(Throwable e) 
				{
					e.printStackTrace();
					fail = true;
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
					comments+="Skip Exception or other exception occured" ;
				}
				closeBrowser();
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
