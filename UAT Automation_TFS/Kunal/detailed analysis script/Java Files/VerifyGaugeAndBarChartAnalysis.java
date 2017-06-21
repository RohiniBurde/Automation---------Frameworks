package com.uat.suite.dashboard_detailedAnalysis;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyGaugeAndBarChartAnalysis extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;
	
	Select selectProject;
	Select selectVersion;
	Select selectTestPass;
	Select testerDropDown;
	
	String totalNumOfProject;
	float passPercentageForTP1;
	int expectedFailPercentageForTP1, expectedPassPercentageForTP1, expectedNCPercentageForTP1;
		
	int tp1_testStepCreatedCount = 0;
	int tp2_testStepCreatedCount = 0;
	int testerCountForTestPass1 = 2;
	int testerCountForTestPass2 = 1;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	//ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(DetailedAnalysisXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(DetailedAnalysisXls, this.getClass().getSimpleName());
		
		/*tester=new ArrayList<Credentials>();
		
		versionLead=new ArrayList<Credentials>();
		
		testManager=new ArrayList<Credentials>();*/
		
		//testManager=getUsers("Test Manager", 1);
	}

	@Test(dataProvider="getTestData")
	public void testVerifyGaugeAndBarChartAnalysis(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,String TestPassName2,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
			String tester_Role2,String TP1_TestCaseName1,String TP1_TestCaseName2,String TP2_TestCaseName1,String TP2_TestCaseName2,
			String TP1_TC1_TestStepName1,String TP1_TC1_TestStepName2,String TP1_TC1_TestStepName3,String TP1_TC1_TestStepName4,
			String TP1_TC1_TestStepName5,String TP2_TC1_TestStepName1,String TP2_TC1_TestStepName2,String TP2_TC1_TestStepName3,
			String TestStepExpectedResult,String AssignedRole1, String AssignedRole2 ) throws Exception
		{
		count++;
		
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int Tester_count = Integer.parseInt(tester_testerName);
		tester = getUsers("Tester", Tester_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());				
		
		
		
		APP_LOGS.debug("Opening Browser...for user "+Role);
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try 
			{
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				getElement("UAT_testManagement_Id").click();		
				
				Thread.sleep(3000);
			
				//Create Project
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
	 			{
	 				fail=true;	 				
	 				APP_LOGS.debug("Project has not created successfully. Test case has been failed.");	 				
	 				comments += "Fail Occurred: Project is not Created Successfully... ";	 				
	 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");	 				
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
	 			}	
				else 
				{
					APP_LOGS.debug(" Project Created Successfully.");
				}
				
				
				//create test pass1
	        	 if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
	        	 {
	    		     fail=true;	    		     
	               	 APP_LOGS.debug("Test Pass1 is not created successfully. Test case has been failed.");	               	 
	               	 comments += "Fail Occurred: Test Pass1 not Created Successfully...";	               	 
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass1 Creation Unsuccessful");					 
					 
						 
	       			 throw new SkipException("Test Pass1 is not created successfully... So Skipping all tests");
	        	 }
	        	 else 
				 {
	        		 APP_LOGS.debug("Test Pass1 Created Successfully with name "+TestPassName1);
				 }
	        	
	
	        	//create test pass2
				if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
				{
	       		     fail=true;	       		     
	               	 APP_LOGS.debug("Test Pass2 is not created successfully. Test case has been failed.");	               	 
	               	 comments += "Fail Occurred: Test Pass2 not Created Successfully...";	               	 
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass2 Creation Unsuccessful");					 
					 
					 
	      			 throw new SkipException("Test Pass2 is not created successfully... So Skipping all tests");
	       	 	}
				else 
				{
					APP_LOGS.debug("Test Pass2 Created Successfully with name "+TestPassName2);
				}
				
				APP_LOGS.debug("Closing the browser after creation of Test Passes.");				
				closeBrowser();
				
				
				APP_LOGS.debug("Opening the browser for user "+testManager.get(0).username);				
				openBrowser();
				
				if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(2000);
							
							//create TP1_Tester1 with R1
							if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(0).username, tester_Role1, tester_Role1))
							{
								 fail=true;
				            	 APP_LOGS.debug("TP1_Tester1 with R1 is not created successfully for test pass 1. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred: TP1_Tester1 with R1 not Created Successfully for test pass 1... ";	
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("Tester is not created successfully For Test Pass1 ... So Skipping all tests");
							}
							else 
							{
								APP_LOGS.debug("Tester: TP1_Tester1 with R1 Created Successfully.");
								
							}
							
							Thread.sleep(700);
							//create TP1_Tester1 with R2
							if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(1).username, tester_Role2, tester_Role2))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP1_Tester1 with R2 is not created successfully for test pass 2. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred: TP1_Tester1 with R2 not Created Successfully for test pass 2... ";	
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_Tester1 with R2 is not created successfully For Test Pass2... So Skipping all tests");
							}
							else 
							{
								APP_LOGS.debug("Tester: TP1_Tester1 with R2 Created Successfully.");
								
							}
							
							Thread.sleep(700);
							//create TP2_Tester1 with R1
							if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, tester.get(0).username, tester_Role1, tester_Role1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP2_Tester1 with R1 is not created successfully for test pass 2. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred: TP2_Tester1 with R1 not Created Successfully for test pass 2... ";	
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP2_Tester1 with R1 is not created successfully For Test Pass2... So Skipping all tests");
							}
							else 
							{
								APP_LOGS.debug("Tester: TP2_Tester1 with R1 Created Successfully.");							
							}
							
							
							//create TP1_Test case 1
							if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TestCaseName1))
							{
								 fail=true;							 							 
				            	 APP_LOGS.debug("TP1_Test case 1 is not created successfully for test pass 1. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_Test case 1 not Created Successfully for test pass 1... ";	
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_Test case 1 is not created successfully for test pass 1... So Skipping all ");
							}
							else 
							{
								APP_LOGS.debug("TP1_Test case 1 Created Successfully for test pass 1.");
							}
							
							
							//create TP2_Test case 1
							if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP2_TestCaseName1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP2_Test case 1 is not created successfully for test pass 2. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP2_Test case 1 not Created Successfully for test pass 2... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP2_Test case 1 is not created successfully for test pass 2... So Skipping all tests");
							}
							else 
							{
								APP_LOGS.debug("TP2_Test case 1 Created Successfully for test pass 2.");
							}
							
							
							//create TP1_TC1_Test step1
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName1,TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP1_TC1_Test step1 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_TC1_Test Step1 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp1_testStepCreatedCount = tp1_testStepCreatedCount+1;
								
								APP_LOGS.debug("TP1_TC1_Test Step1 Created Successfully.");
								
							}
							
							//create TP1_TC1_Test step2
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName2,TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP1_TC1_Test Step2 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_TC1_Test Step2 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
								 
				    			 throw new SkipException("TP1_TC1_Test Step2 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp1_testStepCreatedCount = tp1_testStepCreatedCount+1;							
								APP_LOGS.debug("TP1_TC1_Test Step1 Created Successfully.");
								
							}
							
							
							//create TP1_TC1_Test step3
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName3,TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP1_TC1_Test Step3 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_TC1_Test Step3 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_TC1_Test Step3 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp1_testStepCreatedCount = tp1_testStepCreatedCount+1;							
								APP_LOGS.debug("TP1_TC1_Test Step3 Created Successfully.");
								
							}
							
							
							//create TP1_TC1_Test step4
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName4,TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole2))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP1_TC1_Test Step4 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_TC1_Test Step4 not Created Successfully... ";	
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_TC1_Test Step4 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp1_testStepCreatedCount = tp1_testStepCreatedCount+1;							
								APP_LOGS.debug("TP1_TC1_Test Step4 Created Successfully.");
								
							}
							
							
							//create TP1_TC1_Test step5
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName5,TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole2))
							{
								 fail=true;
				            	 APP_LOGS.debug("TP1_TC1_Test Step5 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP1_TC1_Test Step5 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP1_TC1_Test Step5 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp1_testStepCreatedCount = tp1_testStepCreatedCount+1;							
								APP_LOGS.debug("TP1_TC1_Test Step5 Created Successfully.");
								
							}
							
							
							//create TP2_TC1_Test step1
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC1_TestStepName1,TestStepExpectedResult,
									TP2_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP2_TC1_Test Step1 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP2_TC1_Test Step1 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP2_TC1_Test Step1 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp2_testStepCreatedCount = tp2_testStepCreatedCount+1;							
								APP_LOGS.debug("TP2_TC1_Test Step1 Created Successfully.");
								
							}
							
							
							//create TP2_TC1_Test step2
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC1_TestStepName2,TestStepExpectedResult,
									TP2_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP2_TC1_Test step2 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP2_TC1_Test step2 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP2_TC1_Test step2 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp2_testStepCreatedCount = tp2_testStepCreatedCount+1;							
								APP_LOGS.debug("TP2_TC1_Test step2 Created Successfully.");
								
							}
							
							
							//create TP2_TC1_Test step3
							if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC1_TestStepName3,TestStepExpectedResult,
									TP2_TestCaseName1, AssignedRole1))
							{
								 fail=true;							 
				            	 APP_LOGS.debug("TP2_TC1_Test step3 is not created successfully. Test case has been failed.");			            	 
				            	 comments += "Fail Occurred:- TP2_TC1_Test step3 not Created Successfully... ";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");							 
								 
								 
				    			 throw new SkipException("TP2_TC1_Test step3 is not created successfully ... So Skipping all tests");
							}
							else 
							{
								tp2_testStepCreatedCount = tp2_testStepCreatedCount+1;							
								APP_LOGS.debug("TP2_TC1_Test step3 Created Successfully.");
								
							}
							
							System.out.println("tp1_testStepCreatedCount: "+tp1_testStepCreatedCount);						
							System.out.println("tp2_testStepCreatedCount: "+tp2_testStepCreatedCount);
							
							APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
							
							
							
							
							
							
							
							
							
							closeBrowser();
							
							APP_LOGS.debug("Login with testers1 credentials");
							
							openBrowser();
							
							if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
								{
										
									APP_LOGS.debug("Clicking on Begin Testing in My Activity");
									
									if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
									{
										fail = true;									
										APP_LOGS.debug("Test Pass 1 is not been displayed in My activity Area. Test case has been failed.");									
										comments += "Fail Occurred:- Test Pass 1 is not been displayed in My activity Area.... ";		
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 +"is not in My Activity Area");									
										
										
										throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
									}
									else
									{
										APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");								
										clickOnPASSRadioButtonAndSave();
										
									
										APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");								
										clickOnPASSRadioButtonAndSave();
										
									
										APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");								
										clickOnFAILRadioButtonAndSave();
									
										Thread.sleep(1000);
										
										APP_LOGS.debug("Clicking on very Satisfied Radio Button");								
										getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
										
										Thread.sleep(500);
										
										
										APP_LOGS.debug("Clicking on Save button of Rating popup");								
										getObject("TestingPageRatingPopup_saveButton").click();
										
										Thread.sleep(500);
										
										
										APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");								
										getObject("TestingPage_returnToHomeButton").click();
												
										Thread.sleep(1000);
									}
									
									APP_LOGS.debug("Clicking on Begin Testing in My Activity");
									
									if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName2)) 
									{
										fail = true;									
										APP_LOGS.debug("Test Pass 2 is not been displayed in My activity Area. Test case has been failed.");									
										comments += "Fail Occurred:- Test Pass 2 is not been displayed in My activity Area.... ";	
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName2 +"is not in My Activity Area");
										
										
										throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
									}
									else
									{
										APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");								
										clickOnPASSRadioButtonAndSave();				
										
										Thread.sleep(1000);
										
										APP_LOGS.debug("Clicking on Dashboard tab button as Testing is Complete(Keeping two test step as NC)");	
										
										getElement("UAT_dashboard_Id").click();								
										Thread.sleep(1000);
									}
										
									closeBrowser();
									
									APP_LOGS.debug("Login with testers2 credentials");
									
									openBrowser();
									
									if (login(tester.get(1).username,tester.get(1).password, "Tester")) 
									{
										APP_LOGS.debug("Clicking on Begin Testing in My Activity");
										
										if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
										{
											fail = true;										
											APP_LOGS.debug("Begin Testing has not been clicked using provided details. Test case has been failed.");										
											comments += "Fail Occurred:- Begin Testing has not been clicked... ";	
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Begin Testing has not been clicked");										
											
											
											throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
										}
										else
										{
											APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");									
											clickOnPASSRadioButtonAndSave();									
											Thread.sleep(1000);
											
											APP_LOGS.debug("Clicking on Dashboard tab button as Testing is Complete(Keeping two test step as NC)");									
											getElement("UAT_dashboard_Id").click();
											Thread.sleep(1000);
										}
											
											
										APP_LOGS.debug("Closing the browser after saving TM Data and Done Testing for user "+tester.get(1).username);
										closeBrowser();
										
										
										APP_LOGS.debug("Opening the browser for user "+testManager.get(0).username);
										openBrowser();
										
										if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
										{
											
							 				if (getElement("DashboardTestingStatus_detailedAnalysisButton_Id").isDisplayed()) 
							 				{
							 					getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();	
											}
							 				else
							 				{
							 					fail = true;										
												APP_LOGS.debug("Detailed Ananlysis button is not displayed. Test case has been failed.");										
												comments += "Fail Occurred:- Detailed Ananlysis button is not displayed.... ";	
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Detailed Ananlysis button is not displayed.");										
												
												
												throw new SkipException("Detailed Ananlysis button is not displayed.... So Skipping all tests");
											}
											
											 
						 					Thread.sleep(520);
						 					
											//On Detailed Analysis page
											selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
											
											selectProject.selectByVisibleText(ProjectName);
											
											selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
						
											String testPassDropDownSelectedValue = selectTestPass.getFirstSelectedOption().getText();
											
											System.out.println("testPassDropDownSelectedValue: "+testPassDropDownSelectedValue);
											
											System.out.println(getElement("DetailedAnalysis_testPassNameDisplayedAboveGauge_Id").getAttribute("title"));
											
											
											////Ask about 2nd condition
											String testPassNameSubString;
											int indexOfTestPassName = getElement("DetailedAnalysis_testPassNameDisplayedAboveGauge_Id").getText().indexOf("...");
											
											if(indexOfTestPassName<0)
												testPassNameSubString = getElement("DetailedAnalysis_testPassNameDisplayedAboveGauge_Id").getText().substring(19);
											else
												testPassNameSubString = getElement("DetailedAnalysis_testPassNameDisplayedAboveGauge_Id").getText().substring(19, indexOfTestPassName);
											
											if (assertTrue(getElement("DetailedAnalysis_testPassNameDisplayedAboveGauge_Id").getAttribute("title").equals(TestPassName1) &&
													TestPassName1.contains(testPassNameSubString)))
											{
												APP_LOGS.debug("Test Pass name displayed above Gauge is same as Test Pass name Selected in Test pass Dropdown");
											}
											else
											{
												fail = true;											
												
												APP_LOGS.debug("Test Pass name displayed above Gauge is NOT same as Test Pass name Selected in Test pass Dropdown. Test Case has been failed.");
												
												comments += "Exception occurred: Test Pass name displayed above Gauge is NOT same as Test Pass name Selected in Test pass Dropdown...";											
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass name or Hover Text displayed above Gauge is NOT as expected");
											}
	
										
											APP_LOGS.debug("=====Selecting the PROJECT and Checking whether the Pass/Fail/NC percentage displayed on Gauge is as expected.=====");
						 					
											APP_LOGS.debug("Select Project.");					 					
											
											selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));										
											
											selectProject.selectByVisibleText(ProjectName);										
							 				
											APP_LOGS.debug("Selecting the Test Pass 1 from Test Pass Drop Down");
											
							 				if (!selectTestPassFromTP_DD(TestPassName1)) 
							 				{
												fail = true;	
												
												comments += "Fail Occurred :- "+TestPassName1+" has not found in Test Pass Dropdown...";
												
												APP_LOGS.debug(TestPassName1+" has not found in Test Pass Dropdown. Test case failed");
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1+" has not found in Test Pass Dropdown");
											
												throw new SkipException(TestPassName1+" has not found in Test Pass Dropdown.... So Skipping all tests");
							 				}
											
											//FOR PASS
							 				if (!passPercentageDisplayedOnGauge(tp1_testStepCreatedCount)) 
							 				{
							 					fail = true;		
							 					
							 					assertTrue(false);
												
							 					APP_LOGS.debug("Pass percentage displayed on Gauge is NOT as expected for Tp1. Test case failed");											
												
							 					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass Gauge displaying different count than Expected");
												
							 					comments += "Fail Occurred :- Pass percentage displayed on Gauge is NOT as expected for Tp1...";
							 					
											}
							 				else
							 				{
							 					comments += "Pass percentage displayed on Gauge is as expected for Tp(After Selecting Project)...";
											}
											
							 				//FOR FAIL
							 				if (!failPercentageDisplayedOnGauge(tp1_testStepCreatedCount)) 
							 				{
							 					fail = true;		
							 					
							 					assertTrue(false);
							 					
							 					APP_LOGS.debug("Fail percentage displayed on Gauge is NOT as expected for Tp1. Test case failed");											
							 					
							 					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail Gauge displaying different count than Expected");
							 					
							 					comments += "Fail Occurred :- Fail percentage displayed on Gauge is NOT as expected for Tp1...";
							 				}
							 				else
							 				{
							 					comments += "Fail percentage displayed on Gauge is as expected for Tp(After Selecting Project)...";
											}
							 				
											//FOR NC
							 				if (!ncPercentageDisplayedOnGauge(tp1_testStepCreatedCount)) 
							 				{
												fail = true;											
												
												assertTrue(false);
												
												APP_LOGS.debug("NC percentage displayed on Gauge is NOT as expected for Tp1. Test case failed");											
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC Gauge displaying different count than Expected");
												
												comments += "Fail Occurred :- Not Completed percentage displayed on Gauge is NOT as expected for Tp1...";
							 				}
							 				else
							 				{
							 					comments += "Not Completed percentage displayed on Gauge is as expected for Tp(After Selecting Project)...";
											}

							 				
							 				APP_LOGS.debug("=====Selecting the TEST PASS and Checking whether the Pass/Fail/NC percentage displayed on Gauge is as expected.=====");
							 			
							 				APP_LOGS.debug("Selecting the Test Pass 2 from Test Pass Drop Down");
											
							 				
							 				if (!selectTestPassFromTP_DD(TestPassName2)) 
							 				{
												fail = true;	
												
												comments += "Fail Occurred :- "+TestPassName2+" has not found in Test Pass Dropdown.";
												
												APP_LOGS.debug(TestPassName1+" has not found in Test Pass Dropdown. Test case failed");
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1+" has not found in Test Pass Dropdown");
											
												throw new SkipException(TestPassName1+" has not found in Test Pass Dropdown.... So Skipping all tests");
							 				}
							 				
											//FOR PASS
											if (!passPercentageDisplayedOnGauge(tp2_testStepCreatedCount))
							 				{
												fail = true;		
							 					
							 					assertTrue(false);
												
							 					APP_LOGS.debug("Pass percentage displayed on Gauge is NOT as expected for Tp2. Test case failed");											
												
							 					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass Gauge displaying different count than Expected");
												
							 					comments += "Fail Occurred :- Pass percentage displayed on Gauge is NOT as expected for Tp2...";
							 					
											}
											else
							 				{
							 					comments += "Pass percentage displayed on Gauge is as expected for Tp(After Selecting Test Pass)...";
											}
											
											
											//FOR FAIL
											//by preeti need assertTrue condition
											if (!failPercentageDisplayedOnGauge(tp2_testStepCreatedCount)) 
							 				{
												fail = true;		
							 					
							 					assertTrue(false);
							 					
							 					APP_LOGS.debug("Fail percentage displayed on Gauge is NOT as expected for Tp2. Test case failed");											
							 					
							 					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail Gauge displaying different count than Expected");
							 					
							 					comments += "Fail Occurred :- Fail percentage displayed on Gauge is NOT as expected for Tp2...";
							 				}
							 				else
							 				{
							 					comments += "Fail percentage displayed on Gauge is as expected for Tp2(After Selecting Test Pass)...";
											}
							 				
						
											//by preeti need assertTrue condition
											//FOR NC
											if (!ncPercentageDisplayedOnGauge(tp2_testStepCreatedCount)) 
							 				{
												fail = true;
												
												assertTrue(false);
												
												APP_LOGS.debug("NC percentage displayed on Gauge is NOT as expected for Tp2. Test case failed");											
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC Gauge displaying different count than Expected");
												
												comments += "Fail Occurred :- Not Completed percentage displayed on Gauge is NOT as expected for Tp2...";
							 				}
							 				else
							 				{
							 					comments += "Not Completed percentage displayed on Gauge is as expected for Tp(After Selecting Test Pass)...";
											}
											
										
											//-------------------------------------------------------------
											APP_LOGS.debug("=====Selecting the TESTER and Checking whether the Pass/Fail/NC percentage displayed on Gauge is as expected.=====");
											
											APP_LOGS.debug("Selecting the Test Pass 1 from Test Pass Drop Down");
											
											if (!selectTestPassFromTP_DD(TestPassName1)) 
							 				{
												fail = true;	
												
												comments += "Fail Occurred :- "+TestPassName1+" has not found in Test Pass Dropdown.";
												
												APP_LOGS.debug(TestPassName1+" has not found in Test Pass Dropdown. Test case failed");
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1+" has not found in Test Pass Dropdown");
											
												throw new SkipException(TestPassName1+" has not found in Test Pass Dropdown.... So Skipping all tests");
							 				}
											
											APP_LOGS.debug("Selecting the Tester1 from Tester Drop Down");
											
											if (!selectTesterFromTesterDD(tester.get(0).username)) 
											{
												 fail=true;
												 
								            	 APP_LOGS.debug("Testers are not displayed in Testers dropdown associated with selected Test Pass. Test case has been failed.");
								            	 
								            	 comments += "Fail Occurred:- Testers are not displayed in Testers dropdown associated with selected Test Pass.... ";
												 
								            	 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expexted Testers not displayed in Testers Dropdown.");
												
								            	 throw new SkipException(tester.get(0).username+" has not found in Testers Dropdown.... So Skipping all tests");
	
											}
											
											try 
											{
												String testStepPassedText = getElement("DetailedAnalysis_testStepPassedText_Id").getText().substring(18);
												
												System.out.println("testStepPassedText: "+testStepPassedText);
												
												String testStepForThisTesterFromDescriptionTable = getElement("DetailedAnalysis_testStepForThisTester_Id").getText();
												
												passPercentageForTP1 = ((float)Integer.parseInt(testStepPassedText) /(float) Integer.parseInt(testStepForThisTesterFromDescriptionTable)) * 100;
												
												System.out.println("passPercentageForTP1: "+passPercentageForTP1);
												
												expectedPassPercentageForTP1 = Math.round(passPercentageForTP1);
												
												System.out.println("expectedPassPercentageForTP1: "+expectedPassPercentageForTP1);
												
												String passCount = (String) eventfiringdriver.executeScript("return $('#visualizationPass').find('table tbody tr td div div svg g g').text()");
											
												if (compareIntegers(expectedPassPercentageForTP1, Integer.parseInt(passCount))) 
												{
													APP_LOGS.debug("Pass percentage displayed on Gauge is as expected for Tp1(After Selecting Tester)");
													///by preeti - comments not updated for Pass
												}
												else 
												{
													///by preeti - fail is not updated
													fail = true;
													
													APP_LOGS.debug("Pass percentage displayed on Gauge is NOT as expected for Tp1 (After Selecting Tester). Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass Gauge displaying different count than Expected");
													
													comments += "Fail Occurred :- Pass percentage displayed on Gauge is NOT as expected for Tp2 (after selecting Tester)...";
													///by preeti - comments not updated for Fail 
												}
												
												//FOR FAIL
												
												String testStepFailedText = getElement("DetailedAnalysis_testStepFailedText_Id").getText().substring(18);
												
												System.out.println("testStepFailedText: "+testStepFailedText);
												
												float failPercentageForTP1 = ((float)Integer.parseInt(testStepFailedText) /(float) Integer.parseInt(testStepForThisTesterFromDescriptionTable)) * 100;
												
												System.out.println("failPercentageForTP1: "+failPercentageForTP1);
												
												expectedFailPercentageForTP1 = Math.round(failPercentageForTP1);
												
												System.out.println("expectedFailPercentageForTP1: "+expectedFailPercentageForTP1);
												
												String failCount = (String) eventfiringdriver.executeScript("return $('#visualizationFail').find('table tbody tr td div div svg g g').text()");
											
												if (compareIntegers(expectedFailPercentageForTP1, Integer.parseInt(failCount))) 
												{
													APP_LOGS.debug("Fail percentage displayed on Gauge is as expected for Tp1 (After Selecting Tester)");
													///by preeti - comments not updated for Pass
												}
												else 
												{
													fail = true;
													
													APP_LOGS.debug("Fail percentage displayed on Gauge is NOT as expected for Tp1(After Selecting Tester). Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail Gauge displaying different count than Expected");
													///by preeti - comments not updated for Fail 
													comments += "Fail Occurred :- Fail percentage displayed on Gauge is NOT as expected for Tp2 (after selecting Tester)...";
												}
								
												//FOR NC
												
												String testStepNCText = getElement("DetailedAnalysis_testStepNCText_Id").getText().substring(14);
												
												System.out.println("testStepNCText: "+testStepNCText);
												
												float ncPercentageForTP1 = ((float)Integer.parseInt(testStepNCText) /(float) Integer.parseInt(testStepForThisTesterFromDescriptionTable)) * 100;
												
												System.out.println("ncPercentageForTP1: "+ncPercentageForTP1);
												
												expectedNCPercentageForTP1 = Math.round(ncPercentageForTP1);
												
												System.out.println("expectedNCPercentageForTP1: "+expectedNCPercentageForTP1);
												
												String NCCount = (String) eventfiringdriver.executeScript("return $('#visualizationNC').find('table tbody tr td div div svg g g').text()");
											
												if (compareIntegers(expectedNCPercentageForTP1, Integer.parseInt(NCCount))) 
												{
													APP_LOGS.debug("NC percentage displayed on Gauge is as expected for Tp1 (After Selecting Tester)");
													///by preeti - comments not updated for Pass
												}
												else 
												{
													fail = true;
													
													APP_LOGS.debug("NC percentage displayed on Gauge is NOT as expected for Tp1 (After Selecting Tester). Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC Gauge displaying different count than Expected");
													///by preeti - comments not updated for Fail 
													comments += "Fail Occurred :- Not completed percentage displayed on Gauge is NOT as expected for Tp2 (after selecting Tester)...";
													
												}
											} 
											catch (Throwable t) 
											{
												fail = true;
												
												assertTrue(false);
												
												t.printStackTrace();
												
												APP_LOGS.debug("Exception occurred while varifying gauge percentage after selecting tester. Test case failed.");
												
												comments += "Fail occurred:- Pass/Fail/NC percentage displayed on Gauge is NOT as expected for Selected tester...";
											}
											
											
											
											//-----------------------------------------------------------------------------
											
											APP_LOGS.debug("====Verifying, Total testers displayed in DD, description box and Bar charts should be same====");
										
											APP_LOGS.debug("Selecting the test pass 1");
											
											if (!selectTestPassFromTP_DD(TestPassName1)) 
							 				{
												fail = true;	
												
												comments += "Fail Occurred :- "+TestPassName1+" has not found in Test Pass Dropdown.";
												
												APP_LOGS.debug(TestPassName1+" has not found in Test Pass Dropdown. Test case failed");
												
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1+" has not found in Test Pass Dropdown");
											
												throw new SkipException(TestPassName1+" has not found in Test Pass Dropdown.... So Skipping all tests");
							 				}
											
											try 
											{
												testerDropDown = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
												
												APP_LOGS.debug("Selection All Testers with value 0");
												
												testerDropDown.selectByValue("0");
												
												int totalNumOfTestersInSelectTestersDD = testerDropDown.getOptions().size();
												
												
												if (compareIntegers(testerCountForTestPass1, (totalNumOfTestersInSelectTestersDD-1))) 
												{
													APP_LOGS.debug("Options displayed in Testers DD are as expected.");
													///by preeti - comments not updated for Pass	
												}
												else 
												{
													fail = true;
													
													APP_LOGS.debug("Options displayed in Testers DD are NOT as expected.. Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Options displayed in Testers DD are NOT as expected");
													///by preeti - comments not updated for Fail 
												}
												
												APP_LOGS.debug("Verifying whether Total Testers are displaying correct Count of Testers associated to test pass.");
												
												String totalTestersInDescriptionBox = getElement("DetailedAnalysis_totalTestersInDescriptionBox_Id").getText();
												
												if (compareIntegers(testerCountForTestPass1, Integer.parseInt(totalTestersInDescriptionBox))) 
												{
													APP_LOGS.debug("Count Displayed in Total testers is Correct.");
													///by preeti - comments not updated for Pass
												}
												else 
												{
													fail = true;
													
													APP_LOGS.debug("Count Displayed in Total testers is Not Correct... Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Count Displayed in Total testers is Not Correct.");
													///by preeti - comments not updated for Fail 
												}
												
												String barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
												
												barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("chxl=1:")+8, barChartSrcText.indexOf("&chxt"));
												
												System.out.println("barChartSrcText "+barChartSrcText);
												
												String[] testernames = barChartSrcText.split("\\|");
												
												int numOfBarChart = testernames.length;
												
												System.out.println("numOfBarChart "+numOfBarChart);
												
												///by preeti - could use compareIntegers
												
												if (compareIntegers(testerCountForTestPass1, numOfBarChart)) 
												{
													APP_LOGS.debug("Bar charts are displayed as expected.");
													
													comments += "Number of Bar charts are displayed as expected...";
												}
												else 
												{
													fail = true;
													
													APP_LOGS.debug("Number of Bar charts are not displayed as expected.... Test case failed");
													
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Bar charts are not displayed as expected.");
													
													comments += "Fail Occurred: Number of Bar charts are not displayed as expected...";
												}
												
											//--------------------------------------------------------------------------------
												APP_LOGS.debug("====Verifying, bar chart percenatage values are as Expected====");
											
												barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
												
												barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("&chd=t:")+7, barChartSrcText.indexOf("&chma="));
												
												System.out.println("barChartSrcText "+barChartSrcText);
												
												String[] passFailNCPercentage = barChartSrcText.split("\\|");
												
												int passFailNCPercentageCounts = passFailNCPercentage.length;
												
												System.out.println("passFailNCPercentageCounts "+passFailNCPercentageCounts);
												
												int failFlag = 1;
												
												for (int i = 0; i < passFailNCPercentageCounts; i++) 
												{
													if (i==0) 
													{
														System.out.println(passFailNCPercentage[i]);
														
														String[] passPercentage = passFailNCPercentage[i].split(",");
														
														System.out.println("passPercentage[0] "+ passPercentage[0]);
														
														if (!compareIntegers(expectedPassPercentageForTP1, Integer.parseInt(passPercentage[0]))) 
														{
															APP_LOGS.debug("Pass percentage is not displayed on Bar Chart as expected. Test Case Has been Failed");
															
															fail = true;
															
															TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass percentage is not displayed on Bar Chart as expected.");
														
															failFlag = 0;
														}
														///by preeti - comments not updated for Pass
													}
													if (i==1) 
													{
														System.out.println(passFailNCPercentage[i]);
														
														String[] failPercentage = passFailNCPercentage[i].split(",");
														
														if (!compareIntegers(expectedFailPercentageForTP1, Integer.parseInt(failPercentage[0]))) 
														{
															APP_LOGS.debug("Fail percentage is not displayed on Bar Chart as expected. Test Case Has been Failed");
														
															fail = true;
															
															TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail percentage is not displayed on Bar Chart as expected.");
														
															failFlag = 0;
														}
														
														///by preeti - comments not updated for Pass
													}
													if (i==2) 
													{
														System.out.println(passFailNCPercentage[i]);
														
														String[] ncPercentage = passFailNCPercentage[i].split(",");
														
														if (!compareIntegers(expectedNCPercentageForTP1, (Integer.parseInt(ncPercentage[0])+1))) 
														{
															APP_LOGS.debug("NC percentage is not displayed on Bar Chart as expected. Test Case Has been Failed");
															
															fail = true;
															
															TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC percentage is not displayed on Bar Chart as expected.");
														
															failFlag = 0;
														}
														///by preeti - comments not updated for Pass
													}
													
												}
												
												if (failFlag != 0) 
												{
													comments += "Percenatge count on Bar Chart is displayed as Expected ...";
												}
												else 
												{
													comments += "Fail Occurred:- Percenatge count on Bar Chart is NOT displayed as Expected ...";
												}
											} 
											catch (Throwable t) 
											{
												fail = true;										
												
												APP_LOGS.debug("Percenatge count on Bar Chart is not displayed as Expected . Test case failed");										
												
												assertTrue(false);										
												
												comments += "Exception occurred: Percenatge count on Bar Chart is not displayed as Expected ...";										
												
												t.printStackTrace();
												
											}
												
										
											
									}
									else 
									{
										fail=true;
										APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);								
										comments += "Fail Occurred :-Login Unsuccessful for Test Manager "+testManager.get(0).username;
										assertTrue(false);	 
									}
										
							}
							else 
							{
								fail=true;
								APP_LOGS.debug("Login Unsuccessful for Tester2 "+tester.get(1).username);						
								comments += "Fail Occurred :-Login Unsuccessful for Tester2 "+tester.get(1).username;
								assertTrue(false);	 
							}
									
									
						}
						else 
						{
							fail=true;
							APP_LOGS.debug("Login Unsuccessful for Tester1 "+tester.get(0).username);					
							comments += "Fail Occurred :-Login Unsuccessful for Tester1 "+tester.get(0).username;
							assertTrue(false);	 
						}
				
				}
				else 
				{
					fail=true;
					APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);				
					comments += "Login Unsuccessful for Test Manager "+testManager.get(0).username;
					assertTrue(false);	 
					
				}
							
			} 
			catch (Throwable t) 
			{
				t.printStackTrace();
				fail=true;
				comments += "Exception Occurred :-Skip or Any other exception has Occurred.";
				assertTrue(false);	
				APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");	
			}
			
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);				
			comments += "Fail Occurred :- Login Unsuccessful for user "+Role;
		}			
	}	
	
	/*//Function
	private void clickOnPASSRadioButtonAndSave() throws IOException, InterruptedException
	{
		getElement("TestingPage_passRadioButton_Id").click();
		Thread.sleep(500);
		
		getElement("TestingPage_saveButton_Id").click();
		Thread.sleep(500);
	}
	
	
	private void clickOnFAILRadioButtonAndSave() throws IOException, InterruptedException
	{
		getElement("TestingPage_failRadioButton_Id").click();
		Thread.sleep(500);
		
		getElement("TestingPage_saveButton_Id").click();
	}*/
	
	
	private boolean selectTestPassFromTP_DD(String TestPassName) throws IOException, InterruptedException
	{
		selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
		int numOfTestPassOption = selectTestPass.getOptions().size();
		
		for (int i = 0; i < numOfTestPassOption; i++) 
		{
			String testPassOptionText = selectTestPass.getOptions().get(i).getAttribute("title");
			
			if (testPassOptionText.equals(TestPassName)) 
			{
				selectTestPass.selectByIndex(i);
				return true;
			}
			
		}
		return false;
	}
	
	
	private boolean selectTesterFromTesterDD(String testerName) throws IOException, InterruptedException
	{
		
		testerDropDown = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
		int totalNumOfTestersInSelectTestersDD = testerDropDown.getOptions().size();
		
		System.out.println("totalNumOfTestPassInProjectDD "+totalNumOfTestersInSelectTestersDD);
		
		String OptionInTestersDD = null;
		
		for (int i = 1; i < totalNumOfTestersInSelectTestersDD; i++) 
		{
			OptionInTestersDD = testerDropDown.getOptions().get(i).getAttribute("title");
			System.out.println("OptionInTestersDD "+OptionInTestersDD);
			
			
				if (OptionInTestersDD.equalsIgnoreCase(testerName.replace(".", " "))) 
				{
					testerDropDown.selectByIndex(i);
					
					return true;
				}
			
		}
		return false;
	}
	
	
	
	private Boolean passPercentageDisplayedOnGauge(int tp_testStepCreatedCount) throws IOException, InterruptedException
	{
		String testStepPassedText = getElement("DetailedAnalysis_testStepPassedText_Id").getText().substring(18);
		
		System.out.println("testStepPassedText: "+testStepPassedText);
		
		float passPercentageForTP =((float)Integer.parseInt(testStepPassedText) /(float)tp_testStepCreatedCount) * 100;
		
		System.out.println("expectedPassPercentageForTP: "+passPercentageForTP);
		
		int expectedPassPercentageForTP = Math.round(passPercentageForTP);
		
		System.out.println("expectedPassPercentageForTP: "+expectedPassPercentageForTP);
		
		String passCount = (String) eventfiringdriver.executeScript("return $('#visualizationPass').find('table tbody tr td div div svg g g').text()");
	
		if (compareIntegers(expectedPassPercentageForTP, Integer.parseInt(passCount))) 
		{
			APP_LOGS.debug("Pass percentage displayed on Gauge is as expected for Tp");
			///by preeti - comments not updated for Pass
			return true;
		}
		else 
		{
			///by preeti - comments not updated for Fail and fail=true not updated
			//APP_LOGS.debug("Pass percentage displayed on Gauge is NOT as expected for Tp. Test case failed");
			//TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass Gauge displaying different count than Expected");
			return false;
		}
		
	}
	
	
	
	private Boolean failPercentageDisplayedOnGauge(int tp_testStepCreatedCount) throws IOException, InterruptedException
	{
		
		String testStepFailedText = getElement("DetailedAnalysis_testStepFailedText_Id").getText().substring(18);
		
		System.out.println("testStepFailedText: "+testStepFailedText);
		
		float failPercentageForTP = ((float)Integer.parseInt(testStepFailedText) /(float) tp_testStepCreatedCount) * 100;
		
		System.out.println("failPercentageForTP: "+failPercentageForTP);
		
		int expectedFailPercentageForTP = Math.round(failPercentageForTP);
		
		System.out.println("expectedFailPercentageForTP: "+expectedFailPercentageForTP);
		
		String failCount = (String) eventfiringdriver.executeScript("return $('#visualizationFail').find('table tbody tr td div div svg g g').text()");
	
		if (compareIntegers(expectedFailPercentageForTP, Integer.parseInt(failCount))) 
		{
			APP_LOGS.debug("Fail percentage displayed on Gauge is as expected for Tp");
			///by preeti - comments not updated for Pass
			return true;
		}
		else 
		{
			//APP_LOGS.debug("Fail percentage displayed on Gauge is NOT as expected for Tp. Test case failed");
			//TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail Gauge displaying different count than Expected");
			return false;
			///by preeti - comments not updated for Fail 
		}
		
	}
	
	
	
	private Boolean ncPercentageDisplayedOnGauge(int tp_testStepCreatedCount) throws IOException, InterruptedException
	{
		
		String testStepNCText = getElement("DetailedAnalysis_testStepNCText_Id").getText().substring(14);
		
		System.out.println("testStepNCText: "+testStepNCText);
		
		float ncPercentageForTP = ((float)Integer.parseInt(testStepNCText) /(float)tp_testStepCreatedCount) * 100;
		
		System.out.println("ncPercentageForTP1: "+ncPercentageForTP);
		
		expectedNCPercentageForTP1 = Math.round(ncPercentageForTP);
		
		System.out.println("expectedNCPercentageForTP1: "+expectedNCPercentageForTP1);
		
		String NCCount = (String) eventfiringdriver.executeScript("return $('#visualizationNC').find('table tbody tr td div div svg g g').text()");
	
		if (compareIntegers(expectedNCPercentageForTP1, Integer.parseInt(NCCount))) 
		{
			APP_LOGS.debug("NC percentage displayed on Gauge is as expected for Tp");
			///by preeti - comments not updated for Pass
			return true;
		}
		else 
		{
			//APP_LOGS.debug("NC percentage displayed on Gauge is NOT as expected for Tp. Test case failed");
			//TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC Gauge displaying different count than Expected");
			return false;
			///by preeti - comments not updated for Fail 
		}
		
	}	
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(DetailedAnalysisXls, this.getClass().getSimpleName()) ;
	}
	
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	

}
