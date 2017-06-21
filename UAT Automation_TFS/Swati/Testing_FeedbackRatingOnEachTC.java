/*Author Name:-Swati Gangarde
 * Created Date:- 31st Nov 2014
 * Last Modified on Date:-12th Dec 2014
 * Test Case Description:- Feedback Rating After completion of each (Pass) Test Case. - 
 A).Feedback Rating after each ‘Pass’ Test Case  - Verify 'Continue Testing' pop up appears if it is not the last test case of activity.
 B). Verify Go to Feedback
 C). Verify Return Home
 D).Verify in case of Testing Complete, Test Step feedback pop up should not appear.
*/

package com.uat.suite.testing_page;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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
public class Testing_FeedbackRatingOnEachTC extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	String comments;
	String[] testPass;
	String[] testCase;
	String[] testSteps;
	String[] testSteps1;//Test Case1 Test Steps Details 
	String[] testSteps2;//Test Case2 Test Steps Details 
	String[] totalTestSteps;//For 
	int myActivityTableRows;
	int totalPages;
	String projectCell;
	String versionCell;
	String testPassCell;
	String roleCell;
	int daysLeft;
	int notCompletedCount=6;
	int passCount;
	int failCount;
	String actionCell;
	String[] resultArray;
	boolean flag = false;
	int i=0;
	
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Beginning test case :" +this.getClass().getSimpleName());
		if(!TestUtil.isTestCaseRunnable(testingPageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case" +this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(testingPageSuiteXls, this.getClass().getSimpleName());
	}
	
   //Test Case Implementation ...	
	@Test(dataProvider="getTestData")
	public void Test_Testing_FeedbackRatingOnEachTC 
			(       String Role,String GroupName,String PortfolioName,String ProjectName, String Version,
					String EndMonth, String EndYear, String EndDate, String VersionLead ,
					String TestPassName, String TestManager,String Tester,String TesterRole1,
					String TesterRole2,	String TestCaseName, String TestStepDetails1,String TestStepExpectedResult1, 
					String TestStepDetails2,String TestStepExpectedResult2,String TestStepResult1,String TestStepResult2,String FeedbackRatingPopupText,
					String Rating, String Feedback , String feedbackNote
			) throws Exception
	{
			count++;
				
				if(!runmodes[count].equalsIgnoreCase("Y"))
				{
					skip=true;
					APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
		
					throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
				}
				
				 comments="";
				
				  //version lead
				 int versionlead_count = Integer.parseInt(VersionLead);
				 versionLead=new ArrayList<Credentials>();
				 versionLead = getUsers("Version Lead", versionlead_count);
				 
				 //TestManager 
				 int testManager_count = Integer.parseInt(TestManager);
				 testManager=new ArrayList<Credentials>();
				 testManager = getUsers("Test Manager", testManager_count);
				 
				 //Tester 
				 int tester_count = Integer.parseInt(Tester);
				 tester=new ArrayList<Credentials>();
				 tester = getUsers("Tester", tester_count);			
				
				 APP_LOGS.debug("Executing Test Case :-"+ this.getClass().getSimpleName());
				 
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
						
						if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{	
							APP_LOGS.debug("Fail - Project : " +ProjectName+ " : Creation Unsuccessful by : " +Role+ ". ");							
							comments+="Fail - Project : " +ProjectName+ " : Creation Unsuccessful by : " +Role+ ". ";						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
							throw new SkipException("Project : " +ProjectName+ " : Creation Unsuccessfull");
						}						
						APP_LOGS.debug("Project : " +ProjectName+ " : Created Successfully.  ");				
						
						//Creation Test Pass
						testPass = TestPassName.split(",");
						if (!customCreateTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
						{	
							APP_LOGS.debug("Fail - Test Pass : " +TestPassName+ " : Creation Unsuccessful by : " +Role+ " . ");	
							comments+="Fail - Test Pass :  " +TestPassName+ " : Creation Unsuccessful by : " +Role+ " . " ;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName+"TestPassCreationFailure");
							throw new SkipException(" Test Pass : " +TestPassName+ " : Creation Unsuccessful ");
						}
						APP_LOGS.debug("Test Pass : " +TestPassName+ " : Created Successfully. ");	
						
						/*Clicking on Configuration Tab to select the created project, version and Test Pass and set Test Pass Level Option 
					    * "Select Feedback Rating Option:" as "After completion of each (Pass) Test Case Activity."
					    */
					    Thread.sleep(1000);
					  
						try
					    {
						    //Click on Configuration tab				
							APP_LOGS.debug("Clicking On Configuration Tab ");				
							getElement("UAT_configuration_Id").click();	
							
							Thread.sleep(1000);
					    }
					    catch(Throwable t)
					    {
							t.printStackTrace();					  
					    	APP_LOGS.debug("Fail : Unable to click on Configuration tab . ");
					    	comments+="Fail : Unable to click on Configuration tab .  ";
					    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnabletoClickOnConfigurationTab");
					    	throw new SkipException("Fail : Unable to click on Configuration tab .");
					    }
						
						//Calling configuationSettingFeedbackRating function to save the Feedback rating option
						configuationSettingFeedbackRating( ProjectName, Version ,  testPass[0]);
						 
						//Calling configuationSettingFeedbackRating function to save the Feedback rating option
						configuationSettingFeedbackRating( ProjectName, Version ,  testPass[1]);
				    
						
						//Return to the Test Management Page and creation tester and rest of input data 					   
		 				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
		 				getElement("UAT_testManagement_Id").click();
		 				Thread.sleep(2000);
		 				
						 
						//Creation of Tester in Test Pass1						
						if (!createTester(GroupName, PortfolioName, ProjectName, Version, testPass[0], tester.get(0).username, TesterRole1,TesterRole1)) 
						{	
							APP_LOGS.debug("Tester Creation Unsuccessfull");	
							comments+="Tester Creation Unsuccessful(Fail)" ;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");						
							throw new SkipException("Tester Creation Unsuccessfull");
						}
						APP_LOGS.debug("Tester : " +tester.get(0).username+ " created successfully in Test Pass : " + testPass[0]);
					
						
						//Creation of Tester in Test Pass2						
						if (!createTester(GroupName, PortfolioName, ProjectName, Version, testPass[1], tester.get(0).username, TesterRole2,TesterRole2)) 
						{	
							APP_LOGS.debug("Tester Creation Unsuccessfull");	
							comments+="Tester Creation Unsuccessful(Fail)" ;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");						
							throw new SkipException("Tester Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Tester : " +tester.get(0).username+ " is created successfully in Test Pass : " + testPass[1]);						
						
												
						//Creation of two Test Case in Test Pass1 						
						testPass=TestPassName.split(",");
						if(!customCreateTestCase(GroupName, PortfolioName, ProjectName, Version, testPass[0], TestCaseName))
						{  
						     APP_LOGS.debug("Test Case is not created successfully. ");
	                    	 comments+="Fail- Test Case not Created Successfully. ";	                    	
	     					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");	     					 
	     					 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
						}						
						APP_LOGS.debug("Test Case : " +TestCaseName+ " Created Successfully in Test Pass : " + testPass[0]);
	    		
	    				//Creation of two Test Case in Test Pass2    				
						if(!customCreateTestCase(GroupName, PortfolioName, ProjectName, Version, testPass[1], TestCaseName))
						{
							 APP_LOGS.debug("Test Case is not created successfully");
	                    	 comments+="Fail- Test Case not Created Successfully. ";	                    	
	     					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");	     					  					 
	            			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
						}
						APP_LOGS.debug("Test Case Created Successfully in Test Pass : " + testPass[1]);	    				
	    				
						//Creation of two test steps for Test Case1 of Test Pass1 Test Steps : " 
						testPass=TestPassName.split(",");
						testCase = TestCaseName.split(",");
	    				testSteps = TestStepDetails1.split(",");
	    				
	    				//Creation of two test steps for Test Case1 of Test Pass1    				
	    				if(!customCreateTestStep(GroupName, PortfolioName, ProjectName, Version, testPass[0],TestStepDetails1,TestStepExpectedResult1 , TestCaseName, TesterRole1))
	    				{
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");         					   					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Testing Suite");
	    				}
			        	
	    				//Creation of two test steps for Test Case2 of Test Pass1 	    				
	    				if(!customCreateTestStep(GroupName, PortfolioName, ProjectName, Version, testPass[1],TestStepDetails2,TestStepExpectedResult2 , TestCaseName, TesterRole2))
	    				{
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");         				        					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Testing Suite");
	    				}
						 
						APP_LOGS.debug("Closing Browser... ");
						closeBrowser();
						
						//Opening browser for Tester
						APP_LOGS.debug("Opening Browser...");
	    				openBrowser();
	    				
	    				//login with tester user
	    				APP_LOGS.debug("Login with Tester : " + tester.get(0).username + " to perform testing. ");
	    				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
	    				{   
	    					/*
	    					 Calling searchTestPassAndPerformTesting() first time to perform testing andverifying the Test Pass Feedbak pop
	    					 * appears after completion of each test case 
	    					  */
	    					testPass=TestPassName.split(",");
	    					if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[0],TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult1, Rating, Feedback, FeedbackRatingPopupText, "Begin Testing", feedbackNote,"Go to Feedback"))
	    					{
	    						APP_LOGS.debug("Testing for " +testPass[0]+ " with role " +TesterRole1+ " is unsuccessful.");
								comments+="Fail- "+ "Testing for " +testPass[0]+ " with role"+TesterRole1+ " is unsuccessful.";
	    					}
	    					
	    					//Redirecting to Dashboard Page 
	        				getElement("UAT_dashboard_Id").click();
	        				Thread.sleep(1000);
	        				
	        				if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[1],TestStepDetails1,TestStepDetails2, TesterRole2, TestStepResult2, Rating, Feedback, FeedbackRatingPopupText, "Begin Testing", feedbackNote, "Return to Home"))
	    					{
	    						APP_LOGS.debug("Testing for " +testPass[1]+ " with role " +TesterRole2+ " is unsuccessful.");
								comments+="Fail- "+ "Testing for " +testPass[1]+ " with role"+TesterRole2+ " is unsuccessful.";
	    					}
	        				
	        				if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[0],TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult1, Rating, Feedback, FeedbackRatingPopupText, "Testing Complete", feedbackNote,""))
	    					//if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[1],TestStepDetails,TestStepDetails2, TesterRole1, TestStepResult1, Rating, Feedback, FeedbackRatingPopupText, "Testing Complete", feedbackNote,""))
	    					{
	    						APP_LOGS.debug("Testing for " +testPass[0]+ " with role " +TesterRole2+ " is unsuccessful.");
								comments+="Fail- "+ "Testing for " +testPass[0]+ " with role"+TesterRole2+ " is unsuccessful.";
	    					}
	    				}
	    				else
	    				{
	    					APP_LOGS.debug("Fail : Login Unsuccessful for Tester : " + tester.get(0).username );
	    					comments+="Fail : Login Unsuccessful for Tester : " + tester.get(0).username;
	    					throw new SkipException("Tester is unable to login ... So Skipping all tests in Testing Suite");
	    					
	    				}
	    				
					}	
					catch (Throwable e) 
					{
						e.printStackTrace();
						fail = true;
						assertTrue(false);
						APP_LOGS.debug("Skip Exception or other exception occured" ) ;
						comments+="Skip Exception or other exception occured" ;
					}
					
					APP_LOGS.debug("Closing Browser... ");
					closeBrowser();
				}
				else 
				{
						fail=true;						
						APP_LOGS.debug("Login Unsuccessful for the user with role '"+ Role+"'.");
						comments+="Login Unsuccessful for the user with role '"+ Role+"'.";
				}	
		 }
		
		@AfterMethod
		public void reportDataSetResult(){
		if(skip)
		{
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else{
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		
	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(testingPageSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	private void configuationSettingFeedbackRating(String ProjectName, String Version , String TestPassName) throws IOException
    {
		//Catch block does not have fail veriable updated nor does it report errors
		try
		{
			//Selecting created project name from 'Select Project:' drop down 
			Select selectProject = new Select(getElement("Configuration_selectProjectDropDown_Id"));			
			selectProject.selectByVisibleText(ProjectName);
			APP_LOGS.debug( ProjectName + ": is selected from Select Project drop down");				
	
			//Selecting created project's version from 'Select Version:' drop down 
			Select selectVersion = new Select(getElement("Configuration_selectVersionDropDown_Id"));
			selectVersion.selectByVisibleText(Version);
			APP_LOGS.debug( Version + ": is selected from Select Version drop down ");	
			
			//Selecting created project's version's Test Pass from 'Select Test Pass:' drop down 
			Select selectTestPass = new Select(getElement("Configuration_selectTestPassDropDown_Id"));
			selectTestPass.selectByVisibleText(TestPassName);
			APP_LOGS.debug( TestPassName + ": is selected from Select Test Pass drop down");				
			
			/* Verification of on Configuration page in Test Pass Level Options: for Select Feedback Rating :
	    	 "After completion of each (Pass) Test Case." is clicked and is selected 
			*/
			
			String feedbackRatingOption2Label =getObject("Configuration_feedbackRatingOption2Label").getText();
	
			getObject("Configuration_feedbackRatingOption2RadioBtn").click();
			boolean lblFeedbackRatingOption1isSelected = getObject("Configuration_feedbackRatingOption2RadioBtn").isSelected();
			if(assertTrue(lblFeedbackRatingOption1isSelected))
			{
				APP_LOGS.debug(feedbackRatingOption2Label + ": option is selected from Select Feedback Rating Option :");
				comments+= feedbackRatingOption2Label + ": option is selected from Select Feedback Rating Option:";	
			}
			else
			{
				fail=true;				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FeedbackRatingOption1IsNotSelectedOption");
				APP_LOGS.debug("Fail - " + feedbackRatingOption2Label + " : option is not selected from Feedback Rating Option: ");
				comments+="Fail - " + feedbackRatingOption2Label + " : option is not selected from Feedback Rating Option: ";
				throw new SkipException("Fail - " + feedbackRatingOption2Label + " : option is not selected from Feedback Rating Option: hence skipping further test step execution");
			}
		
			//After selecting an option viz. "After completion of each Pass Test Case activity" clicking on Save buton
			APP_LOGS.debug("Clicking on Save button ");
			getObject("Configuratiion_GeneralSettingSaveBtn").click();
			
			//Verification of success message clicking on Save button
			APP_LOGS.debug("Verification of General Settings pop up Success messsage clicking on Save button ");
			if(getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText().contains("configured successfully!"))
			{
				APP_LOGS.debug("Pass - " + getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText() + " : Message verified successfully");
				
				//Clicking on OK button
				getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
			}
			else
			{
				fail=true;
				assertTrue(false);				
				comments+="Fail - Configuration Settings Message is not matched or unable to click on Ok button .";
				APP_LOGS.debug("Fail - Configuration Settings Message is not matched or unable to click on Ok button .");
			}
		
		}
	    catch(Throwable t)
	    {		    
	    	APP_LOGS.debug("Fail : Unable to perform the Configuration Settings operation . ");
	    	comments+="Fail : Unable to perform the Configuration Settings operation.  ";		    
	    }
    }

	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName1,String testStepName2,String testerRole, String testStepResult, 
				String Rating, String Feedback, String expectedFeedbackRatingPopUpTitle,String action , String feedbackNote, String ButtonOption)
	{
			
			try
			{
				//Calculating the number of pages in My Activities section
				APP_LOGS.debug("Calculation of number of pages available in My Activities. ");
				if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
				{
					APP_LOGS.debug("Only 1 page available on My Activities .");
					totalPages=1;
				}
				else 
				{
					APP_LOGS.debug("More than 1 page avaialble on My Activities . Calculating total pages : " + totalPages );			
					totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
				}
				
				//Iterating the loop to the Total Number of Pages found on My Activities
				for (int i = 0; i < totalPages; i++) 
				{
					myActivityTableRows = getElement("DashboardMyActivity_table_Id").findElements(By.tagName("tr")).size();
					APP_LOGS.debug("Number of rows present in a page : " + myActivityTableRows );
					APP_LOGS.debug("Calculating number of rows present in a page : " + myActivityTableRows);
					
					//Iterating through the number of rows 
					for (int j = 1; j <= myActivityTableRows; j++) 
					{
						//Extracting the rowwsie column data
						projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");//Project name
						versionCell = getObject("DashboardMyActivity_versionColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");//Version 
						testPassCell = getObject("DashboardMyActivity_testPassNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");//Test Pass
						roleCell = getObject("DashboardMyActivity_roleNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");//Role
						daysLeft = Integer.parseInt(getObject("DashboardMyActivity_daysLeftColumn1", "DashboardMyActivity_daysLeftColumn2", j).getText());//Test Pass End Date Calculation Days Left
						notCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_notCompletedCountColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());//Not Completed Test Steps count
						passCount = Integer.parseInt(getObject("DashboardMyActivity_passCountColumn1", "DashboardMyActivity_passCountColumn2", j).getText());//Pass Test Steps count
						failCount = Integer.parseInt(getObject("DashboardMyActivity_failCountColumn1", "DashboardMyActivity_failCountColumn2", j).getText());//Fail Test Steps Count
						actionCell = getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).getText();//Action text Extract
							
						APP_LOGS.debug("Verification of Project Name , Version ,Test Pass and  Role of Tester in My Activities . ");
						if (projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole)) 
						{
							comments+="Verification of Project Name , Version , Test Pass and  Role of Tester in My Activities is successful. ";
							APP_LOGS.debug( tester.get(0).username + " : Tester is  having assigned Test Steps for Test Pass : " +testPass+ " :  of Project : " +project);
							
							//verifying Days Left & Not Completed count
		                    if((daysLeft>0) && (notCompletedCount>0 || notCompletedCount!=0) && (passCount==0) && 
		                    		(failCount==0) && actionCell.equals(action))
							{	
                    			APP_LOGS.debug("Action is Begin Testing.");
	                    		
	                    		getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
	                    		Thread.sleep(3000);
	                    		
	                    		resultArray = testStepResult.split(",");			                    		
	                    		testSteps1 = testStepName1.split(",");
	                    		int testStepResultToBe=0;
	                    		for (int a = testStepResultToBe; a < testSteps1.length; a++) 
	                    		{
	                    			if(resultArray[a].contains("Pass"))
	                    			{
	                    				APP_LOGS.debug("Clicking on Pass Radio button. ");
	                    				getElement("TestingPage_passRadioButton_Id").click();
	                    				passCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else if(resultArray[a].contains("Fail"))
	                    			{
	                    				APP_LOGS.debug("Clicking on Fail Radio button. ");
	                    				getElement("TestingPage_failRadioButton_Id").click();
	                    				failCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else
	                    			{
	                    				APP_LOGS.debug("Clicking on Pending Radio button. ");
	                    				getElement("TestingPage_pendingRadioButton_Id").click();
	                    				notCompletedCount++;
	                    			}
	                    			if(a==testSteps1.length-1)
	                    			{
		                    			if(isElementExists(OR.getProperty("TestingPage_feedbackNote")))
		                    			{
		                    				String actualFeedbackNote = eventfiringdriver.findElement(By.xpath("//div[@id='feedbackNote']")).getText();						                    			
		                    				if(compareStrings(feedbackNote, actualFeedbackNote))
		                    				{
		                    					APP_LOGS.debug("Pass - Feedback Note is appearing on last test step execution : " + actualFeedbackNote );
		                    					comments+="Pass - Feedback Note is appearing on last test step execution : " + actualFeedbackNote;
		                    				}
		                    				else
		                    				{
		                    					fail=true;
		                						APP_LOGS.debug("Fail - Feedback Note is not appearing on last test step execution : " + actualFeedbackNote);
		                						comments+="Fail - Feedback Note is not appearing on last test step execution : " + actualFeedbackNote;
		                    				}
		                    			}
		                    		}
	                    			APP_LOGS.debug("Clicking on Save button ");
	                    			getElement("TestingPage_saveButton_Id").click();
	                    			Thread.sleep(500);
								 }
	                    		 testStepResultToBe+=2;	
	                    		 if((passCount!=0)&&(failCount==0))
	                    		 {
	                    			 //Ekta Else does not report error
	                    			 
                    			    //Verify After completion of each Pass Test Case activity pop up displays or not
                    			   if(isElementExists(OR.getProperty("TestingPage_feebackRatingPopup")))	
		                    	   {
	                    			 APP_LOGS.debug("Pass - Verification of \"After completion of each Pass Test Case activity.\" feedback pop up display . " );
	                    			 comments+="Pass - Verification of \"After completion of each Pass Test Case activity.\" feedback pop up display . ";
	                    			
	                    			//Test Case Feedback Pop up message verification
	                    			APP_LOGS.debug("Verification of title of Test Case Feedback rating pop up. ");
	                    			String actualFeedbackRatingPopUpTitle = getObject("TestingPage_feebackRatingPopup_Text").getText();
	                    		
	                    			if(compareStrings(expectedFeedbackRatingPopUpTitle, actualFeedbackRatingPopUpTitle))
	                    			{
	                    				APP_LOGS.debug("Pass - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is successful.");
	                    				comments+="Pass- Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is successful. ";
	                    			}
	                    			else
	                    			{
	                    				fail=true;		                    				
	                    				APP_LOGS.debug("Fail - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is not match.");
	                    				comments+="Fail - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " on Test Case Feedback Pop Up is not match. ";
	                    				TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackRatingPopupTitleNotMatch");
	                    			}
	                    			if(Rating.equals(getObject("TestingPage_rating_verySatisfiedRadioBtn").getAttribute("value")))
	                    			{
	                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_verySatisfiedRadioBtn").getAttribute("value"));
	                    				getObject("TestingPage_rating_verySatisfiedRadioBtn").click();
	                    			}
	                    			else if(Rating.equals( getObject("TestingPage_rating_veryDissatisfiedRadioBtn").getAttribute("value")))
	                    			{
	                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_veryDissatisfiedRadioBtn").getAttribute("value"));
	                    				getObject("TestingPage_rating_veryDissatisfiedRadioBtn").click();
	                    			}
	                    			else if(Rating.equals( getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").getAttribute("value")))
	                    			{
	                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").getAttribute("value"));
	                    				getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").click();
	                    			}
	                    			else if(Rating.equals(getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value")))
	                    			{
	                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value"));
	                    			    getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").click();
	                    			}
	                    			
	                    			//provide feedback
	                    			String feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+Feedback+"')";
	                    			eventfiringdriver.executeScript(feedbackDetails);
	                    			
	                    			//save
	                    			getObject("TestingPage_feedbackRating_SaveButton").click();
	                    			Thread.sleep(500);			                    			
	                    			
	                    		   /*Verification after providing feedback for each pass test step of test case 
	                    			 * Test Step Feedback Pop up appears with Continue Testing button 				                    			
	                    			*/
	                    			APP_LOGS.debug("Verification of Test Step Feedback is displaying with 'Continue Testing' button . " );
	                    			if(isElementExists(OR.getProperty("TestingPage_testStepFeebackPopup")))
	                    			{
		                    			//Verification of 'Continue Testing' button is displaying in Test Step Feedback pop up					                    							                    			
		                    			if(assertTrue(getObject("TestingPage_continueTestingButton").isDisplayed()))
		                    			{
		                    				APP_LOGS.debug("Pass - 'Continue Testing' button is displaying in Test Step Feedback Pop Up . ");
		                    				comments+="Pass - 'Continue Testing' button is displaying in Test Step Feedback Pop Up . ";
		                    				
		                    				//Clicking on Continue Testing button
		                    				getObject("TestingPage_continueTestingButton").click();						                    				 
		                    			}
		                    			else
		                    			{
		                    				fail=true;						                    			
			                    			APP_LOGS.debug("Fail -'Continue Testing' button is not displaying in Test Step Feedback Pop Up. ");						                    			
			                    			comments+="Fail- 'Continue Testing' button is not displaying in Test Step Feedback Pop Up. ";
			                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ContinueTestingButtonNotInTestStepFeedbackPopup");				            					
			                    			return false;                  				
		                    			}
		                    			//After clicking on 'Continue Testing' carrying testing in continuation
		                    			testSteps2 = testStepName2.split(",");	                	
			                    		for (int a = testStepResultToBe; a < (testSteps2.length+2); a++) 
			                    		{
			                    			if(resultArray[a].contains("Pass"))
			                    			{
			                    				APP_LOGS.debug("Clicking on Pass Radio button. ");
			                    				getElement("TestingPage_passRadioButton_Id").click();
			                    				passCount++;
			                    				notCompletedCount--;
			                    			}
			                    			else if(resultArray[a].contains("Fail"))
			                    			{
			                    				APP_LOGS.debug("Clicking on Fail Radio button. ");
			                    				getElement("TestingPage_failRadioButton_Id").click();
			                    				failCount++;
			                    				notCompletedCount--;
			                    			}
			                    			else
			                    			{
			                    				APP_LOGS.debug("Clicking on Pending Radio button. ");
			                    				getElement("TestingPage_pendingRadioButton_Id").click();
			                    				notCompletedCount++;
			                    			}
			                    			
			                    			APP_LOGS.debug("Clicking on Save button ");
			                    			getElement("TestingPage_saveButton_Id").click();
			                    			Thread.sleep(500);
									   }
			                    	   if((passCount!=0)&&(failCount==0))
		                    		   {		
										   //Verify Test Case feedback popup displays or not						                    		
			                    		   if(isElementExists(OR.getProperty("TestingPage_feebackRatingPopup")))
			                    		   {
				                    			APP_LOGS.debug("Pass - Verification of \"After completion of each Pass Test Case activity.\" feedback pop up display is successful. " );
				                    			comments+="Pass - Verification of \"After completion of each Pass Test Case activity.\" feedback pop up display is successful. ";
				                    			
				                    			//Test Case Feedback Pop up message verification
				                    			APP_LOGS.debug("Verification of title of Test Case Feedback rating pop up. ");
				                    			actualFeedbackRatingPopUpTitle = getObject("TestingPage_feebackRatingPopup_Text").getText();
				                    			
				                    			//Comparision of Test Case feedback pop up title with the expected 
				                    			if(compareStrings(expectedFeedbackRatingPopUpTitle, actualFeedbackRatingPopUpTitle))
				                    			{
				                    				APP_LOGS.debug("Pass - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is successful.");
				                    				comments+="Pass- Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is successful. ";
				                    			}
				                    			else
				                    			{
				                    				fail=true;		                    				
				                    				APP_LOGS.debug("Fail - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " : on Test Case Feedback Pop Up is not match.");
				                    				comments+="Fail - Verification of title : " +expectedFeedbackRatingPopUpTitle+ " on Test Case Feedback Pop Up is not match. ";
				                    				TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackRatingPopupTitleNotMatch");
				                    			}
				                    			if(Rating.equals(getObject("TestingPage_rating_verySatisfiedRadioBtn").getAttribute("value")))
				                    			{
				                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_verySatisfiedRadioBtn").getAttribute("value"));						                    				
				                    				getObject("TestingPage_rating_verySatisfiedRadioBtn").click();
				                    			}
				                    			else if(Rating.equals(getObject("TestingPage_rating_veryDissatisfiedRadioBtn").getAttribute("value")))
				                    			{
				                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_veryDissatisfiedRadioBtn").getAttribute("value"));
				                    				getObject("TestingPage_rating_veryDissatisfiedRadioBtn").click();
				                    			}
				                    			else if(Rating.equals(getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").getAttribute("value")))
				                    			{
				                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").getAttribute("value"));
				                    				getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").click();
				                    			}
				                    			else if(Rating.equals( getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value")))
				                    			{
				                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value"));
				                    				getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").click();
				                    			}
				                    			
				                    			//provide feedback
				                    			feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+Feedback+"')";
				                    			eventfiringdriver.executeScript(feedbackDetails);
				                    			
				                    			//save
				                    			getObject("TestingPage_feedbackRating_SaveButton").click();
				                    			Thread.sleep(500);		                    			
												
				                    			/*Performing clicking operation on Go to Feedback button in if block for Test Pass1
				                    			 * else click on Return to Home button for Test Pass2*/
				                    			if(ButtonOption.equalsIgnoreCase("Go to Feedback"))
				                    			{
					                    			 try
					                    			 {
						                    			//Clicking on 'Go To Feedback' button 
						                    			getObject("TestingPage_goToFeedbackButton").click();
						                    			APP_LOGS.debug("Clicking on 'Go to Feedback' button ");
						                    			
						                    			/*  Verification user redirects to Test Step Feedback page 
						                    			 * 	String feedbackPageTitle ->For the verification of Feedback page title
						                    			 *  String testStepFeedbackTabTitle ->For the verification of Test Step Feedback tab title
						                    			 *  String testStepFeedbackTabisActive ->For the verification of Test Step Feedback tab is highlighted
						                    			*/
						                    			//Feedback page title
						                    			String feedbackPageTitle = getObject("Feedback_pageHeading").getText();
						                    			
						                    			//Test Step Feedback tab title
						                    			String testStepFeedbackTabTitle = getObject("Feedback_testStepFeedbackTabTitle").getText();					                    			
						                    			
						                    			//Class attribute of Test Step Feedback tab 
						                    			String testStepFeedbackTabisActive = getObject("Feedback_testStepFeedbackTab").getAttribute("class");
						                    		
						                    			if(assertTrue(feedbackPageTitle.equals(resourceFileConversion.getProperty("Feedback_pageHeadingText"))	                    					
						                    					&& (testStepFeedbackTabTitle.equals(resourceFileConversion.getProperty("Feedback_testStepFeedbackTabText")))
						                    					&& (testStepFeedbackTabisActive.equals(resourceFileConversion.getProperty("Feedback_testStepFeedbackTabClass")))
						                    					))
					
						                    			{	
						                    				/*Now verify after redirecting to Feedback page Test Step Feedback where the project, 
						                    				test pass, tester and its feedback grid(on which testing was performed)  should be shown by default.*/
						                    				//Project 
						                    				Select projectDefaultText = new Select(getElement("Feedback_projectDropDown_ID"));
						                    				
						                    				//Version
						                    				Select versionDefaultText = new Select(getElement("Feedback_versionDropDown_ID"));
						                    				
						                    				//Test Pass		
						                    				Select testPassDefaultText = new Select(getElement("Feedback_testPassDropDown_ID"));
						                    				
						                    				//Tester
						                    				Select testerDefaultText = new Select(getElement("Feedback_testerDropDown_ID"));
						                    				
						                    				//Role
						                    				Select roleDefaultText = new Select(getElement("Feedback_roleDropDown_ID"));
						                    				
						                    				//Verification of if default selection option displaying in drop down is matching with the sheet data
						                    				if(assertTrue(projectDefaultText.getFirstSelectedOption().getAttribute("title").equals(project)
						                    					    && versionDefaultText.getFirstSelectedOption().getText().equals(version)
						                    					    && testPassDefaultText.getFirstSelectedOption().getAttribute("title").equals(testPass)
						                    					    && testerDefaultText.getFirstSelectedOption().getAttribute("title").equalsIgnoreCase(tester.get(0).username.replace(".", " "))
						                    					    && roleDefaultText.getFirstSelectedOption().getAttribute("title").equals(testerRole)))
						                    				{
							                    				APP_LOGS.debug("Pass- Tester  : " +tester.get(0).username+ " redirects on Feedback page and the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is  shown by default. ");
							                    				comments+="Pass - Tester  : " +tester.get(0).username+ " redirects on Feedback page and the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is shown by default. ";
						                    				}
						                    				else
						                    				{
						                    					fail=true;						                    				
								                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Feedback page but the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is not shown by default. ");				
								                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Feedback page but the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is not shown by default. ";
								                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackPageDefaultSelectionFailure");				            					
								                    			return false; 
						                    				}
						                    			}
						                    			else
						                    			{
						                    				fail=true;
							                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Feedback page but page title and test step feedback tab were not displayed . ");						                    			
							                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Feedback page but page title and test step feedback tab were not displayed . ";						                    		
							                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackPageTitleOrTestStepFeedbackTabVerificationFailure");
							                    			return false;
						                    			}
					                    			  }
					                    			  catch(Throwable t)
					                    			  {
					                    				  t.printStackTrace();
					                    				  fail=true;
							                    		  APP_LOGS.debug("Fail - Unable to verify 'Go to Feedback' button functionality. ");						                    			
							                    		  comments+="Fail - Unable to verify 'Go to Feedback' button functionality.  ";						                    					            					
							                    		  return false;
					                    			  }
				                    			}
				                    			else if(ButtonOption.equalsIgnoreCase("Return to Home"))
				                    			{
				                    				try
				                    				{	
					                    				//Clicking on 'Return To Home' button 
					                    				getObject("TestingPage_returnToHomeButton").click();
					                    				APP_LOGS.debug("Clicking on 'Return to Home' button ");
					                    				
					                    				 /* Verification user redirects to Dashboard page 
						                    			  * String dashboardPageTitle ->For the verification of Dashboard page title
						                    			  */
					                    				
						                    			//Dashboard page title
						                    			String dashboardPageTitle = getElement("Dashboard_pageHeaderText_Id").getText();
						                    			if(assertTrue(dashboardPageTitle.equals(resourceFileConversion.getProperty("Dashboard_pageHeadingText"))))
				                    					{
						                    				APP_LOGS.debug("Pass - Tester  : " +tester.get(0).username+ " successfully redirects on : " +dashboardPageTitle+ " page , page title ");
						                    				comments+="Pass - Tester  : " +tester.get(0).username+ " successfully redirects on : " +dashboardPageTitle+ " page , page title . ";
				                    					}
						                    			else
						                    			{
						                    				fail=true;
							                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Dashboard page , page title is not displaying. ");						                    			
							                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Dashboard page , page title is not displaying. ";
							                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DashboardPageTitleVerificationFailure");
							                    			return false;
						                    			}
				                    				}
				                    			  catch(Throwable t)
				                    			  {
				                    				  t.printStackTrace();
				                    				  fail=true;					                    			
						                    		  APP_LOGS.debug("Fail - Unable to verify 'Return to Home' button functionality. ");						                    			
						                    		  comments+="Fail - Unable to verify 'Return to Home' button functionality.  ";				            					
						                    		  return false;
				                    			  }
				                    		 }
		                    			}
			                    		else
			                    		{
			                    			fail=true;
			                    			APP_LOGS.debug("Fail - Test Pass Feedback rating popup not visible so skipping the further test step execution.");
			                    			comments+="Fail- Test Pass Feedback rating popup not visible so skipping the further test step execution .";
			                    			assertTrue(false);
			                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackRatingPopupNotVisible");
			            					return false;
			                    		}
		                    		  }
		                    		  else if((passCount!=0)&&(failCount!=0)||(passCount==0)&&(failCount!=0))
		                    		  {
			                    			//verify test case feedback popup displays or not
				                    		APP_LOGS.debug("Verification of \"After completion of each Pass Test Case activity\" . feedback pop up display" );				                    	
				                    		if(getObject("TestingPage_feebackRatingPopup").isDisplayed())
				                    		{
			                    				fail=true;
				                    			APP_LOGS.debug("Fail-Test Pass Feedback popup is visible as test step is failed from Test Case.");
				                    			comments+="Fail-Test Pass Feedback popup is visible as test step is failed from Test Case.";
				                    			assertTrue(false);
				                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPassFeedbackRatingPopupVisible");				            					
				                    			return false;                 			
			                    			}
			                    			else
			                    			{
			                    				APP_LOGS.debug("Pass-Test Pass Feedback popup is not visible as test step is failed from Test Case.");
				                    			comments+="Pass-Test Pass Feedback popup is not visible as test step is failed from Test Case.";
				                    			
				                    			//Redirecting to Dashboard Page 
				    	        				getElement("UAT_dashboard_Id").click();
				    	        				Thread.sleep(1000);
				                    			return true;
			                    			}
		                    		   }
		                    	  }
	                    		  else
		                    	  {
	                    				fail=true;
		                    			APP_LOGS.debug("Fail - Test Step Feedback popup not visible so skipping the further test step execution .");
		                    			comments+="Fail- Test Step Feedback popup not visible so skipping the further test step execution .";
		                    			assertTrue(false);
		                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepFeedbackPopupNotVisible");				            					
		                    			return false;
		                    		}
		                    	}
	                    		else
	                    		{
	                    			fail=true;
	                    			APP_LOGS.debug("Fail - Test Pass Feedback rating popup not visible so skipping the further test step execution.");
	                    			comments+="Fail- Test Pass Feedback rating popup not visible so skipping the further test step execution .";
	                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackRatingPopupNotVisible");
	            					return false;
	                    		}
                    		}
                    		else if((passCount!=0)&&(failCount!=0)||(passCount==0)&&(failCount!=0))
                    		{
                    			//verify test case feedback popup displays or not
	                    		APP_LOGS.debug("Verification of \"After completion of each Pass Test Case activity\" . feedback pop up display" );
	                    		if(isElementExists(OR.getProperty("TestingPage_feebackRatingPopup")))	
                    			{
                    				fail=true;
	                    			APP_LOGS.debug("Fail-Test Pass Feedback popup is visible as test step is failed from Test Case.");
	                    			comments+="Fail-Test Pass Feedback popup is visible as test step is failed from Test Case.";
	                    			assertTrue(false);
	                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPassFeedbackRatingPopupVisible");				            					
	                    			break;                  			
                    			}
                    			else
                    			{
                    				APP_LOGS.debug("Pass-Test Pass Feedback popup is not visible as test step is failed from Test Case.");
	                    			comments+="Pass-Test Pass Feedback popup is not visible as test step is failed from Test Case.";
	                    			break;
                    			}
                    		}
	                    	flag=true;
		                    i=totalPages;
		                    break;
						}
	                    else if((daysLeft>0 && notCompletedCount==0) && (!(passCount==0)) && 
                    			((failCount==0)) && (actionCell.equals(action)))
	                    {
                    		APP_LOGS.debug("Action is Testing Complete with link.");
                    		comments+="Action is Testing Complete..hence all the test steps under "+testPassCell+" are executed. ";
                    		getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
                    		resultArray = testStepResult.split(",");
                    		testSteps1 = testStepName1.split(",");
                    		testSteps2 = testStepName2.split(",");
                    		totalTestSteps = new String[testSteps1.length+testSteps2.length];
                    		
                    		for (int a = 0; a < totalTestSteps.length; a++) 
                    		{
	                    			if(resultArray[a].contains("Pass"))
	                    			{
	                    				APP_LOGS.debug("Clicking on Pass Radio button. ");
	                    				getElement("TestingPage_passRadioButton_Id").click();
	                    				passCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else if(resultArray[a].contains("Fail"))
	                    			{
	                    				APP_LOGS.debug("Clicking on Fail Radio button. ");
	                    				getElement("TestingPage_failRadioButton_Id").click();
	                    				failCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else
	                    			{
	                    				APP_LOGS.debug("Clicking on Pending Radio button. ");
	                    				getElement("TestingPage_pendingRadioButton_Id").click();
	                    				notCompletedCount++;
	                    			}
	                    			if(a==testSteps1.length-1)
	                    			{		                    		
		                    			if(isElementExists(OR.getProperty("TestingPage_feedbackNote")))
		                    			{
		                    				String actualFeedbackNote =getObject("TestingPage_feedbackNote").getText();						                    			
		                    				if(compareStrings(feedbackNote, actualFeedbackNote))
		                    				{
		                    					APP_LOGS.debug("Feedback Note is appearing on last test step execution : " + actualFeedbackNote );		                    					
		                    				}
		                    				else
		                    				{
		                    					fail=true;
		                						APP_LOGS.debug("Feedback Note is not appearing on last test step execution : " + actualFeedbackNote);
		                						comments+="Feedback Note is not appearing on last test step execution : " + actualFeedbackNote;		                					
		                    				}
		                    			}
		                    		}
	                    			APP_LOGS.debug("Clicking on Save button ");
	                    			getElement("TestingPage_saveButton_Id").click();
	                    			Thread.sleep(500);
							}
                    		
                    		//verify test case feedback popup displays or not
                    		APP_LOGS.debug("Verification of \"After completion of each Pass Test Case activity\" . feedback pop up display" );
                    		if(isElementExists(OR.getProperty("TestingPage_feebackRatingPopup")))	
                			{
                				fail=true;
                				assertTrue(false);
                    			APP_LOGS.debug("Test Case Feedback popup visible.");
                    			comments+="Fail- Test Case Feedback popup visible.";                    			
                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepPopupVisible");				            					
                    			break;                  			
                			}
                			else
                			{
                				APP_LOGS.debug("Pass-Test Step Feedback popup is not visible after testing complete.");
                    			comments+="Pass- Test Step Feedback popup is not visible after testing complete .";
                    			break;
                			}
                    		
	                    }
	                    flag=true;
                       // i=totalPages;
                        break;
					}
			 }//j
			 //if (totalPages>1 && totalPages!=(totalPages-1)&&(flag==false))
			 if (totalPages>1&&flag==false)
             {
				 if(getObject("DashboardMyActivity_NextLink").isDisplayed())
				 {
					 getObject("DashboardMyActivity_NextLink").click();
				 }
				 else
				 {
					 
					 APP_LOGS.debug("Next Link is disabled");
				 }
               //  totalPages--;
             }
			 else
				 break;
		  }//i
		 return true;
		}
		catch(Throwable t)
		{
			fail=true;
			APP_LOGS.debug("My Activity Grid not visible");
			comments+="My Activity Grid not visible.";
			assertTrue(false);
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"MyActivityGridNotVisible");		  
			return false;
		}
		
	
	 }
 }
