package com.uat.suite.tm_teststep;

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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyDeleteTestStep extends TestSuiteBase{

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
	boolean flag=false;
	int deleteResult=0;
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
		public void Test_VerifyDeleteTestStep(String Role, String GroupName, String Portfolio, String ProjectName, String Version, 
				String endMonth,String endYear, String endDate, String VersionLead,String testPassName, String testManager, String testerName,
				String testerRole, String testCaseName,String testStepName,  String assignedRole, String expectedResult,
				String expectedAllTestStepsDeleteConfirmationMessage,String expectedAllTestStepsDeletedSuccessMessage) throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

			}
			
			comments="";
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
					 
				/* // clicking on Test Management Tab
				 getElement("UAT_testManagement_Id").click();
				 APP_LOGS.debug(" Test Management tab clicked ");
				 Thread.sleep(2000);
				
				 if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				 {
					fail=true;
					APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
					comments=ProjectName+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
					closeBrowser();
					throw new SkipException("Project not created successfully");
				 }
				 APP_LOGS.debug(ProjectName+" Project Created Successfully.");
				*/
				 closeBrowser();
				 APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");
				 
				 APP_LOGS.debug("Opening Browser... ");
				 openBrowser();
						
				 if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				 {
					/* APP_LOGS.debug("Logged in browser with Version Lead");
				            
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
						closeBrowser();
						throw new SkipException("Test Pass not created successfully");
					}
					APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");

					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))							
					{	
						fail=true;
						APP_LOGS.debug("Tester not Created Successfully.");
						comments+="Tester not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTesterCreation");
						closeBrowser();

						throw new SkipException("Tester not created successfully");
					}
					APP_LOGS.debug(" Tester Created Successfully.");

					if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
					{
						fail=true;
						APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
						comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestCaseCreation");
						closeBrowser();

						throw new SkipException("Test Case not created successfully");
					}
					APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");*/
			
					closeBrowser();
			 	   	APP_LOGS.debug(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");
					
					try
					{
						
						APP_LOGS.debug("Opening Browser... ");
		 				openBrowser();
		 				
		 				if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
		 				{
		 					
		 					Thread.sleep(1500);
			 	            APP_LOGS.debug("Logged in browser with Test Manager");
			 	            
			 				//click on testManagement tab
			 				APP_LOGS.debug(" Clicking on Test Management Tab");
			 				getElement("UAT_testManagement_Id").click();
			 				Thread.sleep(1500);
			 				
			 				if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, expectedResult, testCaseName, assignedRole))
			 				{
			
								fail=true;
								APP_LOGS.debug(testCaseName+" Test Step not Created Successfully.");
								comments+=testCaseName+" Test Step not Created Successfully(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestStepCreation");
								closeBrowser();
			
								throw new SkipException("Test Step  not created successfully");
							}
			 				
							APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
		 				
							/*Thread.sleep(1500);
			 				//Clicking on View All Link
			 				getObject("TestSteps_viewAllLink").click();*/
			 				
							List<WebElement> listtestStepName =getElement("TestStepViewAll_Table_Id").findElements(By.tagName("tr"));
							for(int i =1;i<=listtestStepName.size();i++)
							{
								String actualTestStepName = getObject("TestStepViewAll_TestStepNameXpath1","TestStepViewAll_TestStepNameXpath2",i).getText();
								if(actualTestStepName.equals(testStepName))
								{	
									if(CONFIG.getProperty("environment").equals("Local"))
										getObject("TestStepdelete_img").click();  //Delete icon
									else
									{
										String selectAllCheckboxScript = "$(\"#chkbxSelectAll\").attr(\"checked\",true);$(\".chkBoxTS\").attr(\"checked\",true);";
									    
									   // String selectNoneCheckboxScript = "$(\"#chkbxSelectAll\").attr(\"checked\",false);$(\".chkBoxTS\").attr(\"checked\",false);";
									    
									    eventfiringdriver.executeScript(selectAllCheckboxScript);
									    
									//	getElement("TestStepViewAll_DeleteAllCheckBox_Id").click();
										getElement("TestSteps_ViewAllBulkDeleteButton_Id").click();
									}
									flag=true;
									break;
								}
							}
							if(flag==true)
							{
								String actualAllTestStepsDeleteConfirmationMessage =getElement("TestCases_ViewAll_deleteTestCasesConfirmationText_Id").getText();
								
								if(compareStrings(expectedAllTestStepsDeleteConfirmationMessage, actualAllTestStepsDeleteConfirmationMessage) )
								{
									comments+= " Test Step Delete icon Confirmation Message showing Properly (Pass).";
									APP_LOGS.debug(" Test step Delete icon Confirmation Message showing Properly stating : " +actualAllTestStepsDeleteConfirmationMessage );
									
									//Click on delete button
									getObject("ProjectViewAll_PopUpDeleteButton").click();
									
							//		String actualAllTestStepsDeletedSuccessMessage =wait.until(ExpectedConditions.visibilityOf(getElement("TestCases_ViewAll_TestCasesDeletedSuccessMessage_Id"))).getText();
									String actualAllTestStepsDeletedSuccessMessage =getTextFromAutoHidePopUp();
									
									if(compareStrings(expectedAllTestStepsDeletedSuccessMessage,actualAllTestStepsDeletedSuccessMessage) )
									{
										comments+= " Test Steps deleted successfully (Pass).";
										APP_LOGS.debug(" Test Steps are deleted successfully stating : " +actualAllTestStepsDeletedSuccessMessage );
										deleteResult=1;
									}
									else
									{
										fail=true;
										comments+= " Test Steps deleted successfully was not correct(Fail).";
										APP_LOGS.debug(" Test Steps are not deleted successfullystating : " +actualAllTestStepsDeletedSuccessMessage);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepDeleteMessage");
									}
								
								}
								else
								{
									fail=true;
									comments+= " Test step Delete icon Confirmation Message not showing Properly(Fail).";
									APP_LOGS.debug(" Test step Delete icon Confirmation Message not showing Properly stating : " +actualAllTestStepsDeleteConfirmationMessage);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepDeleteMessage");
								}
								
					//			getObject("TestStep_testStepaddedsuccessfullyOkButton").click(); //Ok button
								
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Saved test step not seen in the grid ");
								comments+="Saved test step not seen in the grid (Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepNotMatching");
							}
							
							//Verifying " No Test Step(es) Available" Text is present or not when No test Steps available on Page
							
							if(deleteResult==1)
							{
								if(getElement("TestSteps_NoTestStepsAvailable_Id").isDisplayed())
								{
									String noTestStepAvailableText = getElement("TestSteps_NoTestStepsAvailable_Id").getText();
									if(compareStrings(noTestStepAvailableText, resourceFileConversion.getProperty("TM_TestStepsTab_NoTestStepsAvailMessage")))
									{
										comments+=" Test Step is deleted Successfully with Delete button(Pass)";
										APP_LOGS.debug(" Test Step is deleted Successfully with Delete button");
									}
									else
									{
										fail=true;	
										comments+=" Test Step is not deleted Successfully with Delete button(Fail)";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTeststepDelete");
									}
								}
								else
								{
									fail=true;	
									comments+="Even ater test step deletion, no test steps message was not displayed (Fail)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestStepDelete");
								}
							}
							else
							{
								fail=true;	
								comments+=" Test Step is not deleted Successfully with Delete icon(Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestStepDelete");
							}
		 				}
		 				else
		 				{
		 					fail=true;
		 					APP_LOGS.debug("Login Unsuccessful for Test Manager");
		 					comments+="Login Unsuccessful for Test Manager (FAIL)";
		 				}

					}	
					catch(Throwable t)
		            {
			         	 fail=true;
			           	 t.printStackTrace();
			           	 comments="Something went wrong in deleting test step feature (FAIL)";
			           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),"Something went wrong in deleting test step feature");
		            }
					APP_LOGS.debug("Closing Browser... ");
				    closeBrowser();
				 }
				 else
				 {
					fail=true;
					APP_LOGS.debug("Login Unsuccessful for version Lead");
					comments+="Login Unsuccessful for version Lead (FAIL)";
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
