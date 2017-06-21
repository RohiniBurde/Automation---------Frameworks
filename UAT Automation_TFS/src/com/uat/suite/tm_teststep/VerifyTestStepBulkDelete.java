package com.uat.suite.tm_teststep;

import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyTestStepBulkDelete extends TestSuiteBase{
		
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip() throws Exception
		{
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		@Test(dataProvider="getTestData")
		public void Test_VerifyTestStepBulkDelete(String Role, String GroupName, String Portfolio, String ProjectName, String Version, 
				String endMonth,String endYear, String endDate, String VersionLead,String testPassName, String testManager, String testerName,
				String testerRole, String testCaseName,String testStepName1, String expectedResult1,String testStepName2, 
				String expectedResult2, String assignedRole, String expectedAllTestStepsDeleteConfirmationMessage,
				String expectedAllTestStepsDeletedSuccessMessage) throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

			}
			
			APP_LOGS.debug("Opening Browser... ");
			openBrowser();

			isLoginSuccess = login(Role);
			
			if(isLoginSuccess)
			{	
				//version lead
				 int versionlead_count = Integer.parseInt(VersionLead);
				 versionLead=new ArrayList<Credentials>();
				 versionLead = getUsers("Version Lead", versionlead_count);
				 
				 //TestManager 
				 int testManager_count = Integer.parseInt(testManager);
				 test_Manager=new ArrayList<Credentials>();
				 test_Manager = getUsers("Test Manager", testManager_count);
				 
				 //Tester 
				 int tester_count = Integer.parseInt(testerName);
				 tester=new ArrayList<Credentials>();
				 tester = getUsers("Tester", tester_count);
				 
				 // clicking on Test Management Tab
				 getElement("UAT_testManagement_Id").click();
				 APP_LOGS.debug(" Test Management tab clicked ");
				 Thread.sleep(2000);
					
				 if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				 {
					fail=true;
					APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
					comments=ProjectName+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
					throw new SkipException("Project not created successfully");
				 }
				
				 APP_LOGS.debug(ProjectName+" Project Created Successfully.");
					
				 closeBrowser();
				
				 APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");
				
				 APP_LOGS.debug("Opening Browser... ");
				 openBrowser();
						
				 if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))	 
				 {
				    APP_LOGS.debug("Logged in browser with Version Lead");
				            
					//click on testManagement tab
					APP_LOGS.debug(" Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
							
					// creating test pass,test case,testers and test step
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(testPassName+" Test Pass not Created Successfully.");
						comments+=testPassName+" Test Pass not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestPassCreation");
						throw new SkipException("Test Pass not created successfully");
					}
					
					APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
					comments+=testPassName+" Test Pass Created Successfully(Pass). ";

					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))							
					{	
						fail=true;
						APP_LOGS.debug("Tester not Created Successfully.");
						comments+="Tester not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTesterCreation");
						closeBrowser();

						throw new SkipException("Tester not created successfully");
					}
					APP_LOGS.debug(" Tester Created Successfully.");
					comments+=" Tester Created Successfully(Pass). ";

					if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
					{
						fail=true;
						APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
						comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCaseCreation");
						closeBrowser();

						throw new SkipException("Test Case not created successfully");
					}
					APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
					comments+=testCaseName+" Test Case Created Successfully(Pass). ";
			
					closeBrowser();
			 	   	APP_LOGS.debug(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");

			 	   	try
				 	   	{
				 	   	APP_LOGS.debug("Opening Browser... ");
				 	   	openBrowser();
		 				
				 	   	login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager");
				 	   	APP_LOGS.debug("Logged in browser with Test Manager");
		 	            
		 				//click on testManagement tab
		 				APP_LOGS.debug(" Clicking on Test Management Tab");
		 				getElement("UAT_testManagement_Id").click();
		 				Thread.sleep(3000);
		 				
		 				
		 				if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName1, expectedResult1, testCaseName, assignedRole))
		 				{
		
							fail=true;
							APP_LOGS.debug(testCaseName+" Test Step 1 not Created Successfully.");
							comments+=testCaseName+" Test Step 1 not Created Successfully(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStep1Creation");
							closeBrowser();
		
							throw new SkipException("Test Step 1 not created successfully");
						}
						APP_LOGS.debug(testCaseName+" Test Step 1 Created Successfully.");
						comments+=testCaseName+" Test Step 1 Created Successfully(Pass). ";
		 				
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName2, expectedResult2, testCaseName, assignedRole))
		 				{
		
							fail=true;
							APP_LOGS.debug(testCaseName+" Test Step 2 not Created Successfully.");
							comments+=testCaseName+" Test Step 2 not Created Successfully(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStep2Creation");
							closeBrowser();
		
							throw new SkipException("Test Step 2 not created successfully");
						}
						APP_LOGS.debug(testCaseName+" Test Step 2 Created Successfully.");
						comments+=testCaseName+" Test Step 2 Created Successfully(Pass). ";
						
						getObject("TestSteps_viewAllLink").click();            //Click on view all
						
						getElement("TestCases_ViewAllBulkDeleteButton_Id").click();            //Click on bulk delete
						
						String actualAllTestStepsDeleteConfirmationMessage =getElement("TestCases_ViewAll_deleteTestCasesConfirmationText_Id").getText();
						
						if(compareStrings(actualAllTestStepsDeleteConfirmationMessage, expectedAllTestStepsDeleteConfirmationMessage) ){
							comments+= " Test Step Bulk Delete Button Confirmation Message showing Properly (Pass).";
							APP_LOGS.debug(" Test step Bulk Delete Button Confirmation Message showing Properly stating : " +actualAllTestStepsDeleteConfirmationMessage );
						}
						else
						{
							fail=true;
							comments+= " Test step Bulk Delete Button Confirmation Message not showing Properly(Fail).";
							APP_LOGS.debug(" Test step Bulk Delete Button Confirmation Message not showing Properly stating : " +actualAllTestStepsDeleteConfirmationMessage);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepBulkDeleteMessage");
						}
						
						//Click on delete button
						getObject("ProjectViewAll_PopUpDeleteButton").click();
						
						String actualAllTestStepsDeletedSuccessMessage =getElement("TestCases_ViewAll_TestCasesDeletedSuccessMessage_Id").getText();
						
						if(compareStrings(actualAllTestStepsDeletedSuccessMessage, expectedAllTestStepsDeletedSuccessMessage) ){
							comments+= " Test Steps are deleted successfully (Pass).";
							APP_LOGS.debug(" Test Steps are deleted successfully stating : " +actualAllTestStepsDeletedSuccessMessage );
						}
						else
						{
							fail=true;
							comments+= " Test Steps are not deleted successfully (Fail).";
							APP_LOGS.debug(" Test Steps are not deleted successfullystating : " +actualAllTestStepsDeletedSuccessMessage);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepDeleteMessage");
						}
						
						getObject("TestStep_testStepaddedsuccessfullyOkButton").click(); //Ok button
						
	
						Thread.sleep(1000);
			
						//Verifying " No Test Step(es) Available" Text is present or not when No test Steps available on Page
			
						String noTestStepAvailableText =getElement("TestSteps_NoTestStepsAvailable_Id").getText();
									
						if(compareStrings(noTestStepAvailableText, resourceFileConversion.getProperty("TM_TestStepsTab_NoTestStepsAvailMessage")))
						{
							comments+=" All Test Steps are deleted Successfully with Bulk Delete Button(Pass)";
							APP_LOGS.debug(" All Test Steps are deleted Successfully with Bulk Delete Button");
						}
						else
						{
							fail=true;	
							comments+=" All Test Steps are not deleted Successfully with Bulk Delete Button(Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulBulkDeleteTestStep");
						}
			
					}
			 	   	catch(Throwable t)
			 	   	{
			 	   		fail=true;
			 	   		t.printStackTrace();
			 	   		comments="Failed Login for Role" +Role;
			 	   		TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
			 	   	}
			 	   	APP_LOGS.debug("Closing Browser... ");
			 	   	closeBrowser();
			 }
			 else
			 {
				fail=true;
				APP_LOGS.debug("Login Unsuccessful for version Lead");
				comments+="Login Unsuccessful for version Lead";
			 }
		
			}
			else 
			{
				APP_LOGS.debug("Login Not Successful");
			}	
			
		}
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
			{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;	

		}
		
		@AfterTest
		public void reportTestResult() throws Exception{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
		}
	
}
