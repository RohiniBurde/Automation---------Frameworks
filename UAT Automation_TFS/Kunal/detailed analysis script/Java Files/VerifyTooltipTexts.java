package com.uat.suite.dashboard_detailedAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

public class VerifyTooltipTexts extends TestSuiteBase{
	
	String runmodes[] = null;
	boolean fail = false;
	boolean skip = false;
	boolean isTestPass = true;
	int count = -1;
	WebDriverWait wait;
	boolean isLoginSuccess = false;
	String comments;
	
	Actions builder;
	
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
			
		/*tester = new ArrayList<Credentials>();
		versionLead = new ArrayList<Credentials>();
		testManager = new ArrayList<Credentials>();*/
	}
	
	@Test(dataProvider = "getTestData")
	public void testVerifyTooltipTexts(String Role,	String GroupName, String Portfolio, String ProjectName, 
			String Version, String endMonth, String endYear, String endDate, String VersionLead, String testPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName, 
			String testerRole, String testCaseName,String testStepName, String testStepExpectedResult, 
			String assigendRole, String expectedExportButtonTooltipMessage)throws Exception
	{
			count++;
			
			comments = "";

			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip = true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			int Tester_count = Integer.parseInt(tester_testerName);
			tester = getUsers("Tester", Tester_count);
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);

			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			openBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			
			isLoginSuccess = login(Role);
			
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
					
					
					// creating test pass
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						 fail = true;
		        		    
	        		     APP_LOGS.debug(testPassName+" not created successfully. ");
	                   	 
	        		     comments  +=  "Fail Occurred:- "+testPassName+" Not created successfully. ";
						 
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
						
						//Creating Tester
						if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))
						{
							 fail = true;
							 
							 APP_LOGS.debug(tester.get(0).username+ "Tester not ceated successfully for test pass "+testPassName);
							
							 comments  +=  "Fail Occurred:- "+tester.get(0).username+ "Tester not ceated successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass... So Skipping all tests");
						}
					
						APP_LOGS.debug(" Tester with Role: "+testerRole+" Created Successfully.");
						
						//Creating Test Case 
						if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
						{ 
							 fail = true;
						
							 APP_LOGS.debug(testCaseName+ "Test Case not created successfully for test pass "+testCaseName);
							
							 comments  +=  "Fail Occurred:- "+testCaseName+ "Test Case not created successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
							 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
						}
						
						APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
						
						//Creatiing Test Step 
						
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, testStepExpectedResult,
								testCaseName, assigendRole))
						{
							 fail = true;
							
							 APP_LOGS.debug(testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName);
							
							 comments   +=  "Fail Occurred:- "+testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						
						APP_LOGS.debug(testStepName+" Test Step of test Case "+testCaseName+" Created Successfully.");
						
						APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
						
						//comments += "Data has been made Successfully from Test Management tab... ";
	
					
						APP_LOGS.debug("Closing Browser after creating Tester, Test Cases and Test Steps.");
						
						closeBrowser();

						APP_LOGS.debug("Opening Browser... ");
			 		   	
						openBrowser();
			 			
			 			if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			 			{
				 	        APP_LOGS.debug("Log in browser with Tester");
			 	        
				 	        if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, testPassName)) 
							{
			 	        		fail = true;									
								
								APP_LOGS.debug(testPassName+" is not been displayed in My activity Area. Test case has been failed.");									
								
								comments  +=  "Fail Occurred:- "+testPassName+" is not been displayed in My activity Area.... ";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), testPassName +"is not in My Activity Area");									
								
								throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
							
							}
				 	        else 
				 	        {
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
			 			
			 	        APP_LOGS.debug("Closing browser.");
			 	       
			 	        closeBrowser();
			 	       
			 	        APP_LOGS.debug("Opening browser."); 
			 	        
			 	        openBrowser(); 
		
			 	        if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
			 	        {
			 	 	    	   getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
			 	 	    	   
			 	 	    	   List<WebElement> listOfProjectsName = getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));
			 	 	    	   
			 	 	    	   for(int i = 0;i<listOfProjectsName.size();i++)
			 	 	    	   {
			 	 	    		   if(listOfProjectsName.get(i).getAttribute("title").equals(ProjectName))
			 	 	    		   {
			 	 	    			  listOfProjectsName.get(i).click();
			 	 	    			
			 	 	    			  APP_LOGS.debug("Project is selected");
			 	 	    			 
			 	 	    			  break;
			 	 	    		   }
			 	 	    	   }
			 				   Thread.sleep(2000);
			 				   
			 				   //verifying tooltip message on hovering on Export Test Case Statistics button 
			 				 
			 				/*   builder = new Actions((driver));
			 				     WebElement exportButton  = getElement("DetailedAnalysis_ExportTCStatisticsbutton_Id");
			 				     builder.moveToElement(exportButton).build().perform();
			 				     Thread.sleep(2000);
			 				     String actualExportbuttonMessage = getObject("DetailedAnalysis_ExtractTooltipMessage").getText();
			 			         String actualExportbuttonMessage = tagElement.getAttribute("title");
			 					   if(compareStrings(expectedExportButtonTooltipMessage, actualExportbuttonMessage))
			 					   {
			 						   APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is verified");						 
			 						   comments += "Export Test Case Statistics Button Tooltip Message is verified(Pass).";
			 					   }
			 					   else{
			 						  fail = true;
			 						  APP_LOGS.debug("Export Test Case Statistics Button Tooltip Message is not matching");
			 						  comments += "Export Test Case Statistics Button Tooltip Message not matching(Fail).";
			 						  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExportButtonMessageNotMatching");
			 					   }
			 				  */
			 				   
			 				   //verifying tooltip message on hovering on Test Manager Name 
			 				   Thread.sleep(2000);
			 				   
			 				   builder = new Actions((driver));
			 				  
			 				   WebElement TMName  = getElement("DetailedAnalysis_DescriptionTableTestManager_Id");
			 				  
			 				   builder.moveToElement(TMName).build().perform();
			 				   
			 				   Thread.sleep(2000);
			 				   
			 				   String expectedTMName = testManager.get(0).username.replace(".", " ");
			 			       
			 				   String actualTMName = TMName.getAttribute("title");
			 				   
			 			       //Verification of Tooltip Message for Test Manager
			 			       if(assertTrue(expectedTMName.equalsIgnoreCase(actualTMName)))
			 				   {
			 					   APP_LOGS.debug("Test Manager Name Tooltip Message is verified");						 
			 					  
			 					   comments += "Test Manager Name Tooltip Message is verified(Pass).";
			 				   }
			 				   else
			 				   {
			 					  fail = true;
			 					 
			 					  APP_LOGS.debug("Test Manager Name Tooltip Message is not matching");
			 					 
			 					  comments += "Test Manager Name Tooltip Message not matching(Fail).";
			 					  
			 					  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TMNameTooltipMessageNotMatching");
			 				   }
			 				   
			 				   //verifying tooltip message on hovering on Version Lead Name 
			 				   builder = new Actions((driver));
			 				  
			 				   WebElement VLName  = getElement("DetailedAnalysis_DescriptionTableVersionLaed_Id");
			 				   
			 				   builder.moveToElement(VLName).build().perform();
			 				  
			 				   Thread.sleep(2000);
			 				   
			 				   String expectedVLName = versionLead.get(0).username.replace(".", " ");
			 			       
			 				   String actualVLName = VLName.getAttribute("title");
			 				   
			 			       
			 			       //Verification of Tooltip Message for Version Lead
			 			       if(assertTrue(expectedVLName.equalsIgnoreCase(actualVLName)))
			 				   {
			 					  
			 			    	   APP_LOGS.debug("Version Lead Name Tooltip Message is verified");						 
			 					   
			 			    	   comments += "Version Lead Name Tooltip Message is verified(Pass).";
			 				   }
			 				   else
			 				   {
			 					  fail = true;
			 					 
			 					  APP_LOGS.debug("Version Lead Name Tooltip Message is not matching");
			 					 
			 					  comments += "Version Lead Name Tooltip Message not matching(Fail).";
			 					  
			 					  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VLNameMessageNotMatching");
			 				   }
			 					
			 				   //verifying tooltip message on hovering on Test Pass Name under Test Pass Details section 
			 				   
			 			       builder = new Actions((driver));
			 				  
			 				   WebElement TPName  = getElement("DetailedAnalysis_ExtractTPTooltipMessage_Id");
			 				  
			 				   builder.moveToElement(TPName).build().perform();
			 				   
			 				   Thread.sleep(2000);
			 			       
			 				   String actualTPName = TPName.getAttribute("title");
			 				   
			 			       //Verification of Tooltip Message for Test Pass Name
			 			       if(compareStrings(testPassName,actualTPName))
			 				   {
			 					   APP_LOGS.debug("Test Paas Name Tooltip Message is verified");						 
			 					   
			 					   comments += "Test Paas Name Tooltip Message is verified(Pass).";
			 				   }
			 				   else
			 				   {
			 					  fail = true;
			 					 
			 					  APP_LOGS.debug("Test Paas Name Tooltip Message is not matching");
			 					 
			 					  comments += "Test Paas Name Tooltip Message not matching(Fail).";
			 					 
			 					  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TPNameMessageNotMatching");
			 				   }
			 	        	
			 			       
			 			       
					 	    }
							else
							{
								fail = true;
								APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);								
								comments  +=  "Fail Occurred :-Login Unsuccessful for Test Manager "+testManager.get(0).username;
								assertTrue(false);	 
							}
							
			 			}
						else 
						{
							fail = true;
							APP_LOGS.debug("Login Unsuccessful for Tester1 "+tester.get(0).username);					
							comments  +=  "Fail :-Login Unsuccessful for Tester1 "+tester.get(0).username;
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
	
	
/*	private void clickOnFAILRadioButtonAndSave() throws IOException, InterruptedException
	{
		getElement("TestingPage_failRadioButton_Id").click();
		Thread.sleep(500);
			
		getElement("TestingPage_saveButton_Id").click();
	}*/
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(DetailedAnalysisXls, this.getClass().getSimpleName()) ;
	}
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass = false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
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
