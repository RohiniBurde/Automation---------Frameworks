package com.uat.suite.dashboard_detailedAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class VerifyDescriptionTableArea extends TestSuiteBase{
	
	String runmodes[] = null;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	static int count = -1;
	static WebDriverWait wait;
	static boolean isLoginSuccess = false;
	String comments;
	
	Select selectProject;
	Select selectVersion;
	Select selectTestPass;
	Select testerDropDown;
	String totalNumOftestSteps;
	
	int tc1_testStepCreatedCount  =  0;;
	int tc2_testStepCreatedCount  =  0;;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{

		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());				
		
		if(!TestUtil.isTestCaseRunnable(DetailedAnalysisXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		
		runmodes = TestUtil.getDataSetRunmodes(DetailedAnalysisXls, this.getClass().getSimpleName());
/*		
		tester = new ArrayList<Credentials>();
		versionLead = new ArrayList<Credentials>();
		testManager = new ArrayList<Credentials>();
*/	
	}
	
	@Test(dataProvider = "getTestData")
	public void testVerifyDescriptionTableArea(String Role, String GroupName, String Portfolio, String ProjectName, 
			String Version, String endMonth, String	endYear, String	endDate, String VersionLead, String	testPassName,
			String  TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,
			String tester_Role1, String tester_Role2, String testCaseName1, String testCaseName2,
			String TC1_TestStepName1, String TC1_TestStepName2, String TC2_TestStepName1, String TC2_TestStepName2,
			String TC2_TestStepName3, String TestStepExpectedResult, String	AssignedRole1, String AssignedRole2) 
			throws Exception
		{
			count++;
			
			comments  =  "";
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip = true;
				
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			int Tester_count  =  Integer.parseInt(tester_testerName);
			tester  =  getUsers("Tester", Tester_count);
			
			int versionLead_count  =  Integer.parseInt(VersionLead);
			versionLead  =  getUsers("Version Lead", versionLead_count);

			int testManager_count  =  Integer.parseInt(TP_TestManager);
			testManager  =  getUsers("Test Manager", testManager_count);
			
			openBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			
			isLoginSuccess  =  login(Role);
			
			if(isLoginSuccess)
			{
				try
				{
					getElement("UAT_testManagement_Id").click();
					
					APP_LOGS.debug("Clicking On Test Management Tab ");
					
					Thread.sleep(3000);
					
					
					//Creating Project
					if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
					{
						fail = true;
		 				
		 				APP_LOGS.debug(ProjectName+" not created successfully. ");
		 				
		 				comments =  comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
		 				
		 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
						
		 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
					}
					
					APP_LOGS.debug(ProjectName+" Project Created Successfully.");			
					
					Thread.sleep(1000);
					// creating test pass
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, TP_EndMonth, TP_EndYear, TP_EndDate,
							testManager.get(0).username))
					{
						 fail = true;
		        		    
	        		     APP_LOGS.debug(testPassName+" not created successfully. ");
	                   	 
	        		     comments += "Fail Occurred:- "+testPassName+" Not created successfully. ";
						 
	        		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	    					 
	           			 throw new SkipException("Test Pass is not created successfully... So Skipping all tests");
					}
					
					APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
					
					APP_LOGS.debug("Closing the browser after creation of Test Passes.");
					
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser for role "+testManager.get(0).username);
					
					openBrowser();
					
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
					
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						
						//Creating Tester1 with Role1
						if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, tester_Role1, tester_Role1))
						{
							 fail = true;
							 
							 APP_LOGS.debug(tester.get(0).username+ "Tester not ceated successfully for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+tester.get(0).username+ "Tester not ceated successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass... So Skipping all tests");
						}
					
						APP_LOGS.debug(" Tester with Role: "+tester_Role1+" Created Successfully.");
						
						
						//Creating Tester2 with Role2
						if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(1).username, tester_Role2, tester_Role2))
						{
							 fail = true;
							 
							 APP_LOGS.debug(tester.get(1).username+ "Tester not ceated successfully for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+tester.get(1).username+ "Tester not ceated successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass ... So Skipping all tests");
						}
					
						APP_LOGS.debug(" Tester with Role: "+tester_Role2+" Created Successfully.");
						
						
						//Creating testCaseName1 
						if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName1))
						{ 
							 fail = true;
						
							 APP_LOGS.debug(testCaseName1+ "Test Case not created successfully for test pass "+testCaseName1);
							
							 comments += "Fail Occurred:- "+testCaseName1+ "Test Case not created successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
							 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
						}
						
						APP_LOGS.debug(testCaseName1+" Test Case Created Successfully.");
						
						
						//Creating testCaseName2
						if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName2))
						{
							 fail = true;
							
							 APP_LOGS.debug(testCaseName2+ "Test Case not created successfully for test pass "+testCaseName2);
							
							 comments += "Fail Occurred:- "+testCaseName2+ "Test Case not created successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Test Case2 is not created successfully for test pass 1... So Skipping all tests");
						}
						
						APP_LOGS.debug(testCaseName2+" Test Case Created Successfully.");
						
						
						//Creating TC1_TestStepName1
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, TC1_TestStepName1, TestStepExpectedResult,
								testCaseName1, AssignedRole1))
						{
							 fail = true;
							
							 APP_LOGS.debug(TC1_TestStepName1+ "Test Step not created successfully under Test Case "+testCaseName1+ " for test pass "+testPassName);
							
							 comments  += "Fail Occurred:- "+TC1_TestStepName1+ "Test Step not created successfully under Test Case "+testCaseName1+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						
						APP_LOGS.debug(TC1_TestStepName1+" Test Step of test Case "+testCaseName1+" Created Successfully.");
						
						tc1_testStepCreatedCount++;
						
						
						//Creating TC1_TestStepName2
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, TC1_TestStepName2, TestStepExpectedResult,
								testCaseName1, AssignedRole1))
						{
							 fail = true;
								
							 APP_LOGS.debug(TC1_TestStepName2+ "Test Step not created successfully under Test Case "+testCaseName1+ " for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+TC1_TestStepName2+ "Test Step not created successfully under Test Case "+testCaseName1+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						APP_LOGS.debug(TC1_TestStepName2+" Test Step of test Case "+testCaseName1+" Created Successfully.");
					
						tc1_testStepCreatedCount++;
						
						
						//Creating TC2_TestStepName1
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, TC2_TestStepName1, TestStepExpectedResult,
								testCaseName2, AssignedRole2))
						{
							fail = true;
							
							 APP_LOGS.debug(TC2_TestStepName1+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+TC2_TestStepName1+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						
						APP_LOGS.debug(TC2_TestStepName1+" Test Step of test Case "+testCaseName2+" Created Successfully.");
						
						tc2_testStepCreatedCount++;
						
						
						//Creating TC2_TestStepName2
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, TC2_TestStepName2, TestStepExpectedResult,
								testCaseName2, AssignedRole2))
						{
							fail = true;
							
							 APP_LOGS.debug(TC2_TestStepName2+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+TC2_TestStepName2+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						APP_LOGS.debug(TC2_TestStepName2+" Test Step of test Case "+testCaseName2+" Created Successfully.");
						
						tc2_testStepCreatedCount++;
						
						
						//Creating TC2_TestStepName3
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, TC2_TestStepName3, TestStepExpectedResult,
								testCaseName2, AssignedRole2))
						{
							 fail = true;
							
							 APP_LOGS.debug(TC2_TestStepName3+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName);
							
							 comments += "Fail Occurred:- "+TC2_TestStepName3+ "Test Step not created successfully under Test Case "+testCaseName2+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
					
						APP_LOGS.debug(TC2_TestStepName3+" Test Step of test Case "+testCaseName2+" Created Successfully.");
						
						tc2_testStepCreatedCount++;
						
						totalNumOftestSteps  =  String.valueOf(tc1_testStepCreatedCount+tc2_testStepCreatedCount);
					
						String TSCountOfTester1  =  String.valueOf(tc1_testStepCreatedCount);
						
						APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
						
						//comments += "Data has been made Successfully from Test Management tab... ";
						
						closeBrowser();
						
						APP_LOGS.debug("Closing Browser after creating Tester, Test Cases and Test Steps.");
						
						APP_LOGS.debug("Opening Browser... ");
			 		   
						openBrowser();
			 				
						if(login(tester.get(0).username,tester.get(0).password, "Tester"))
						{
				 			APP_LOGS.debug("Logged in browser with Tester1");
			 	        	
				 			if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, testPassName)) 
							{
			 	        		fail = true;									
								
								APP_LOGS.debug(testPassName+" is not been displayed in My activity Area. Test case has been failed.");									
								
								comments += "Fail Occurred:- "+testPassName+" is not been displayed in My activity Area.... ";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), testPassName +"is not in My Activity Area");									
								
								throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
							
							}
			 	        	else 
			 	        	{
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
							    
				 	        APP_LOGS.debug("Closing browser after testing is completed by Tester 1");
				 	        
				 	        closeBrowser();
				 	        
				 	        
				 	        APP_LOGS.debug("Opening Browser... ");

				 	        openBrowser();
				 			
				 	       if (login(tester.get(1).username,tester.get(1).password, "Tester")) 
							{
				 	    	  
				 	    	    APP_LOGS.debug("Logged in browser with Tester2");
					 	        
				 	        	if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, testPassName)) 
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
					 	       
					 	        APP_LOGS.debug("Closing the browser after saving TM Data and Done Testing.");
								
					 	        closeBrowser();
								
								APP_LOGS.debug("Opening the browser after saving TM Data and Done Testing.");
								
								openBrowser();
								
								if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
								{
									 getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
									 
								     // Verification of Description Table area for All Testers Option
									
									 selectProject  =  new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
									
									 selectProject.selectByVisibleText(ProjectName);
									
									 String month = getMonthNumber(TP_EndMonth);
									 
									 String expectedDueDate = month+"-"+TP_EndDate+"-"+TP_EndYear;	
									 
									 String TestManager = testManager.get(0).username.replace(".", " ");
									 
									 String Version_Lead = versionLead.get(0).username.replace(".", " ");
									
									 
									 verifyDescriptionTableDetails(totalNumOftestSteps, tester_testerName, expectedDueDate, TestManager,
											 						Version_Lead,"All Testers");
								  
									 
								     // Verification of Description Table area when Tester1 is selected
									
									 List<WebElement> listOfTester = getElement("DetailedAnalysis_testerDropDown_Id").findElements(By.tagName("option"));
									 
									 for(int i = 1;i<listOfTester.size();i++)
								     {	
										String testerList = listOfTester.get(i).getText();
										
										if(testerList.equalsIgnoreCase(tester.get(0).username.replace(".", " ")))
										{
											listOfTester.get(i).click();
											
											APP_LOGS.debug("Tester is selected.");
											
											//comments += "Tester is selected.";
											break;
										}
									 }
									 
									 verifyDescriptionTableDetails(totalNumOftestSteps, tester_testerName, expectedDueDate, TestManager,
											                          Version_Lead,tester.get(0).username);
									 
									 String actualTSTester1 = getElement("DetailedAnalysis_DescriptionTableTSCountSelectedTester_Id").getText();
									 
									 if(compareStrings(TSCountOfTester1, actualTSTester1))
									 {
										 APP_LOGS.debug("Total number of Test Step Assigned for Tester: " +tester.get(0).username.replace(".", " ")+" is verified.");
										
										 comments += "Total number of Test Step Assigned for Tester: " +tester.get(0).username.replace(".", " ")+" is verified(Pass).";
									 }
									 else
									 {
										fail  =  true;
										
										APP_LOGS.debug("Total Number of Test Step for Tester: " +tester.get(0).username.replace(".", " ")+" not matching");
										
										comments += "Total Number of Test Step for Tester: " +tester.get(0).username.replace(".", " ")+" not matching(Fail).";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TSforTesterNotMatching");
									 }
									 
									 ///////no action is covered or mentioned for verifying 'Total Test Cases for the selected Tester' in Description table
									 
									 
									 //Verification of IE settings for exporting the Test Case Statistics 
									
									 //Verifying active-x control  
							            Boolean result  =  true;
							            try
							            {
							                  eventfiringdriver.executeScript("var xlApp  =  new ActiveXObject('Excel.Application')");
							                  
							                  Thread.sleep(2000);
							            }
							            catch(Throwable t)
							            {
							                  result  =  false;
							            }
							            
							            getElement("DetailedAnalysis_ExportTCStatisticsbutton_Id").click();
							            //need thread.sleep
							            if(result == false)
							            { 
							             // ActiveX code
							            // wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("activeX-form")));
							               
							            	if(assertTrue(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed()))
							                {
							                	getObject("TestStepCreateNew_TestStepActiveX_Close").click();
							                	
							                	APP_LOGS.debug("ActiveX is disabled and hence cannot export Test Case Statistics");
							                	
							                	comments += "ActiveX is disabled and hence cannot export Test Case Statistics";
							                	
							                }
							                else
							                {
							                	APP_LOGS.debug("An alert informing the user of disabled activex should be present.");
							                	
							                	comments += "An alert informing the user of disabled activex should be present.";
							                	
							                	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabled");
							                	
							                	comments += "Fail- Active-x is disabled..but the popup not displayed/handled";
							                	
							                }
							            }
							            else
							            {
							            	APP_LOGS.debug("Test Case Statistics exported successfully");
							            	
							            	comments += "Test Case Statistics exported successfully";
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
							comments += "Fail :-Login Unsuccessful for Tester1 "+tester.get(0).username;
							assertTrue(false);	 
						}	
						
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);				
						comments  +=  "Login Unsuccessful for Test Manager "+testManager.get(0).username;
						assertTrue(false);	
					}
					
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail = true;
					comments  +=  "Fail :-Skip or Any other exception has Occurred.";
					assertTrue(false);	
					APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");
				}
				
				closeBrowser();
			}
			else 
			{
				APP_LOGS.debug("Login Unsuccessful for user "+Role);
				comments += "Login Unsuccessful for Tester "+Role;
			}
		}
		
	
	//Function
	
	private void verifyDescriptionTableDetails(String totalNumOftestSteps, String tester_testerName,String expectedDueDate, String TestManager, String VersionLead, String Tester ) throws IOException
	{
		//need try catch
		String totalTestSteps = getElement("DetailedAnalysis_DescriptionTableTotalTS_Id").getText();
		 
		 if(compareStrings(totalNumOftestSteps, totalTestSteps))
		 {
			 APP_LOGS.debug("Total number of test step for a Test Pass is verified for "+Tester);
			
			 comments += "Total number of test step for a Test Pass is verified for "+Tester+"(Pass).";
		 }
		 else
		 {
			fail  =  true;
			
			APP_LOGS.debug("Total number of test steps do not match for "+Tester);
			
			comments += "Total number of test steps do not match for "+Tester+"(Fail).";
			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalTSNotMatching");
		 }

		 //Verification of total number of Testers
		
		 String totalTesters = getElement("DetailedAnalysis_DescriptionTableTotalTester_Id").getText();
		
		 if(compareStrings(tester_testerName, totalTesters))
		 {
			 APP_LOGS.debug("Total number of tester for a Test Pass is verified for "+Tester);
			
			 comments += "Total number of tester for a Test Pass is verified for "+Tester+"(Pass).";
		 }
		 else
		 {
			fail  =  true;
			
			APP_LOGS.debug("Total number of tester do not match for "+Tester);
			
			comments += "Total number of tester do not match for "+Tester+"(Fail).";
			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalTesterNotMatching");
		 }
		 
		 //Verification of the Due Date, Test Manager and Project Manager.
		
		 String actualDueDate = getElement("DetailedAnalysis_DescriptionTableDueDater_Id").getText();
		
		 String actualTestManager = getElement("DetailedAnalysis_DescriptionTableTestManager_Id").getText();
		 
		 String actualVersionLead = getElement("DetailedAnalysis_DescriptionTableVersionLaed_Id").getText();
		
		 if(compareStrings(expectedDueDate, actualDueDate)){
			 APP_LOGS.debug("Due Date is verified for "+Tester);
			
			 comments += "Due Date is verified for "+Tester+"(Pass).";
		 }
		 else
		 {
			fail  =  true;
			
			APP_LOGS.debug("Due Date does not match for "+Tester);
			
			comments += "Due Date does not match for "+Tester+"(Fail).";
			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DueDateNotMatching");
		 }
		 
		 if(assertTrue(actualTestManager.equalsIgnoreCase(TestManager)))
		 {
			 APP_LOGS.debug("Test Manager is verified for "+Tester);
			 
			 comments += "Test Manager is verified for "+Tester+"(Pass).";
		 }
		 else
		 {
			fail  =  true;
			
			APP_LOGS.debug("Test Manager does not match for "+Tester);
			
			comments += "Test Manager does not match for "+Tester+"(Fail).";
			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestManagerNotMatching");
		 }
		 
		 if(assertTrue(actualVersionLead.equalsIgnoreCase(VersionLead)))
		 {
			 APP_LOGS.debug("Version Lead is verified for "+Tester);
			
			 comments += "Version Lead is verified for "+Tester+"(Pass).";
		 }
		 else
		 {
			fail  =  true;
			
			APP_LOGS.debug("Version Lead does not match for "+Tester);
			
			comments += "Version Lead does not match for "+Tester+"(Fail).";
			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalVersionLeadNotMatching");
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
			isTestPass = false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass = false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip = false;
		fail = false;
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
