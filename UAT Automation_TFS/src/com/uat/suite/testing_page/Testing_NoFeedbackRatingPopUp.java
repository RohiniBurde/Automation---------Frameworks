/*Author Name:-Swati Gangarde
 * Created Date:- 30th Nov 2014
 * Last Modified on Date:-13th Dec 2014
 * Test Case Description:- 
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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class Testing_NoFeedbackRatingPopUp extends TestSuiteBase
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
	boolean flag =true;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Beginning test case :" +this.getClass().getSimpleName());
		if(!TestUtil.isTestCaseRunnable(testingPageSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case" +this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(testingPageSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	// Test Case Implementation ...	
	@Test(dataProvider="getTestData")
	public void Test_Testing_NoFeedbackRatingPopUp
			(       String Role,String GroupName,String PortfolioName,String ProjectName, String Version,
					String EndMonth, String EndYear, String EndDate, String VersionLead ,
					String TestPassName, String TestManager,String Tester,String TesterRole1,
					String TesterRole2,	String TestCaseName, String TestStepDetails1,String TestStepExpectedResult1, 
					String TestStepDetails2,String TestStepExpectedResult2,String TestStepResult
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
						APP_LOGS.debug("Project : " +ProjectName+ " Creation Unsuccessful(Fail) by "+Role+". ");
						comments+="Project : " +ProjectName+ " Creation Unsuccessful(Fail) by "+Role+". ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
						throw new SkipException("Project : " +ProjectName+ " Creation Unsuccessfull");
					}
					
					APP_LOGS.debug("Project : " +ProjectName+ " : Created Successfully.  ");					
					
					//Creation of two Test Passes 
					testPass = TestPassName.split(",");
					if (!customCreateTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{	
						APP_LOGS.debug("Test Pass : " +TestPassName+ " CreationUnsuccessful(Fail). ");
						comments+="Test Pass : " +TestPassName+ " Creation Unsuccessful(Fail). " ;						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName+ " : TestPassCreationFailure");
						throw new SkipException(" Test Pass : " +TestPassName+ "  Creation Unsuccessful ");
					}
					APP_LOGS.debug("Test Pass : " +TestPassName+ " : Created Successfully. ");	
					
					/*
					 Clicking on Configuration Tab to select the created project, version and Test Pass and set Test Pass Level Option 
				    * "Select Feedback Rating Option:" as "No Rating Pop Up."
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
						APP_LOGS.debug("Fail : Unable to click on Configuration tab . ");
				    	comments+="Fail : Unable to click on Configuration tab . ";				    
				    	throw new SkipException("Fail : Unable to click on Configuration tab .");
				    }
					
				    testPass = TestPassName.split(",");				    
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
					APP_LOGS.debug("Tester : " +tester.get(0).username+ " : Created Successfully for Test Pass : " + testPass[0]);
					
					//Creation of Tester in Test Pass2				
					if (!createTester(GroupName, PortfolioName, ProjectName, Version, testPass[1], tester.get(0).username, TesterRole2,TesterRole2)) 
					{	
						APP_LOGS.debug("Tester Creation Unsuccessfull");
						comments+="Tester Creation Unsuccessful(Fail)" ;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");						
						throw new SkipException("Tester Creation Unsuccessfull");
					}
					APP_LOGS.debug("Tester : " +tester.get(0).username+ " : Created Successfully for Test Pass : " + testPass[1]);
					
			
					//Creation of two Test Case in Test Pass1
					if(!customCreateTestCase(GroupName, PortfolioName, ProjectName, Version, testPass[0], TestCaseName))
					{  
					     APP_LOGS.debug("Test Case is not created successfully. ");
                    	 comments+="Fail- Test Case not Created Successfully. ";	                    	
     					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");	     					 
     					 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
					}						
					APP_LOGS.debug("Test Case : " +TestCaseName+ " : Created Successfully in Test Pass : " + testPass[0]);
    		
    				//Creation of two Test Case in Test Pass2    				
					if(!customCreateTestCase(GroupName, PortfolioName, ProjectName, Version, testPass[1], TestCaseName))
					{
						 APP_LOGS.debug("Test Case is not created successfully");
                    	 comments+="Fail- Test Case not Created Successfully. ";	                    	
     					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");	     					  					 
            			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
					}
					APP_LOGS.debug("Test Case : " +TestCaseName+ " : Created Successfully in Test Pass : " + testPass[1]);	  
    				
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
		        	
    				//Creation of two test steps for Test Case2 of Test Pass2 	    				
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
    					testPass=TestPassName.split(",");
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[0],TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult,"Begin Testing",resourceFileConversion.getProperty("Testing_goToFeedbackButtonOption")))
    					{
    						fail = true;
    						assertTrue(false);
    						APP_LOGS.debug("Fail- Testing for " +testPass[0]+ " : for tester with role : " +TesterRole1+ " is unsuccessful.");
    						//throw new SkipException("Fail- "+ "Testing for " +testPass[0]+ " with role"+TesterRole1+ " is unsuccessful.");
    						
    					}
    					
    					//Redirecting to Dashboard Page 
        				getElement("UAT_dashboard_Id").click();
        				Thread.sleep(1000);
        				
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[1],TestStepDetails1,TestStepDetails2, TesterRole2, TestStepResult,"Begin Testing", resourceFileConversion.getProperty("Testing_returnToHomeButtonOption")))
    					{
    						fail = true;
    						assertTrue(false);
    						APP_LOGS.debug("Fail- Testing for " +testPass[1]+ " : for tester with role : " +TesterRole2+ " is unsuccessful.");
    						//throw new SkipException("Testing for " +testPass[1]+ " with role " +TesterRole2+ " is unsuccessful.");
    					}
    					
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, testPass[0],TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult,"Testing Complete", ""))
    					{
    						fail = true;
    						assertTrue(false);
    						APP_LOGS.debug("Fail- Testing for " +testPass[0]+ " with role " +TesterRole1+ " is unsuccessful.");
    						//throw new SkipException("Testing for " +testPass[0]+ " with role " +TesterRole1+ " is unsuccessful.");
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
						APP_LOGS.debug("Skip Exception or other exception occured hence skipping the test case execution");
						comments+="Skip Exception or other exception occured" ;
				}
				
		
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
			}
			else 
			{
					fail=true;
					assertTrue(false);
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
	public void reportTestResult() throws Exception{
		if(isTestPass)
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(testingPageSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName1,String testStepName2,
			String testerRole, String testStepResult,String action, String ButtonOption)
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
				APP_LOGS.debug("More than 1 page avaialble on My Activities . Calculating total pages : " + totalPages);			
				totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}
			
			//Iterating the loop to the Total Number of Pages found on My Activities
			for (int i = 0; i < totalPages; i++) 
			{
				//Calculating the number of rows present in a page
				myActivityTableRows = getElement("DashboardMyActivity_table_Id").findElements(By.tagName("tr")).size();
				APP_LOGS.debug("Number of rows present in a page : " + myActivityTableRows );
				
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
			                    			
			                    			APP_LOGS.debug("Clicking on Save button ");
			                    			getElement("TestingPage_saveButton_Id").click();
			                    			Thread.sleep(500);
										}
			                    		
										//Test Step Feedback Pop up verification 
			                			APP_LOGS.debug("Verification of Test Step Feedback is display or not" );
			                			if(getObject("TestingPage_testStepFeebackPopup").isDisplayed())
			                			{
			                    			APP_LOGS.debug("Pass - Test Step feedback pop up display ");
			                    			comments+="Pass - Test Step feedback pop up display ";					                    			
			                    			
			                    			//Verification of 'Go to Feedback' button and 'Return to Home' button are displaying in Test Step Feedback pop up				                    			
			                    			if(assertTrue(getObject("TestingPage_goToFeedbackButton").isDisplayed())&&(getObject("TestingPage_returnToHomeButton").isDisplayed()))
			                    			{
			                    				APP_LOGS.debug("Pass - Verification of 'Go to Feedback' and 'Return to Home' buttons are displaying in Test Step Feedback Pop Up .");
			                    				comments+="Pass - Verification of 'Go to Feedback' and 'Return to Home' buttons are displaying in Test Step Feedback Pop Up . ";
			                    			}
			                    			else
			                    			{
			                    				APP_LOGS.debug("Fail - 'Go to Feedback' and 'Return to Home' buttons are not displaying in Test Step Feedback Pop Up. ");						                    			
				                    			comments+="Fail- 'Go to Feedback' and 'Return to Home' buttons are not displaying in Test Step Feedback Pop Up. ";
				                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"GotoFeedbackORReturntoHomeButtonsAreNotDisplay");				            					
				                    			return false;                  				
			                    			}
			                    			
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
						                    				return true;
					                    				}
					                    				else
					                    				{
					                    					fail=true;
					                    					assertTrue(false);
							                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Feedback page but the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is not shown by default. ");				
							                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Feedback page but the project : " +project+ " , test pass : " +testPass+ ", tester : " + tester.get(0).username+ " and its feedback grid(on which testing was performed) is not shown by default. ";
							                    		}
					                    			}
					                    			else
					                    			{						                    				
						                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Feedback page but page title and test step feedback tab were not displayed . ");						                    			
						                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Feedback page but page title and test step feedback tab were not displayed . ";						                    		
						                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackPageTitleOrTestStepFeedbackTabVerificationFailure");
						                    			return false;
					                    			}
				                    			 }
				                    			 catch(Throwable t)
				                    			 {
				                    				  t.printStackTrace();					                    				
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
					                    				return true;
			                    					}
					                    			else
					                    			{						                    				
						                    			APP_LOGS.debug("Fail - Tester  : " +tester.get(0).username+ " redirects on Dashboard page , page title is not matched. ");						                    			
						                    			comments+="Fail- Tester  : " +tester.get(0).username+ " redirects on Dashboard page , page title is not displaying. ";
						                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DashboardPageTitleVerificationFailure");
						                    			return false;
					                    			}
			                    			  }
			                    			  catch(Throwable t)
			                    			  {
			                    				  t.printStackTrace();			                    			
					                    		  APP_LOGS.debug("Fail - Unable to verify 'Return to Home' button functionality. ");						                    			
					                    		  comments+="Fail - Unable to verify 'Return to Home' button functionality.  ";				            					
					                    		  return false;
			                    			  }
			                    		 }		                			
			                    	}
		                			else
		                    		{
		                				APP_LOGS.debug("Fail - Test Step Feedback popup not visible.");
		                    			comments+="Fail- Test Step Feedback popup not visible.";		                    			
		                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepFeedbackRatingPopupNotVisible");				            					
		                    			return false;
		                    			
		                    		}
		                			flag=true;
		                            i=totalPages;
		                            break;
							 }
		                    
		                    else if((daysLeft>0 && notCompletedCount==0) && (!(passCount==0)) && 
	                    			(!(failCount==0)) && (actionCell.equals(action)))
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
		                    			}
		                    			else if(resultArray[a].contains("Fail"))
		                    			{
		                    				APP_LOGS.debug("Clicking on Fail Radio button. ");
		                    				getElement("TestingPage_failRadioButton_Id").click();
		                    				failCount++;
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
	                    		
	                    		//Test Step Feedback Pop up verification 
                    			APP_LOGS.debug("Verification of Test Step Feedback is display or not" );
                    			if(isElementExists(OR.getProperty("TestingPage_testStepFeebackPopup")))	
                    			{
                    				APP_LOGS.debug("Test Step Feedback popup visible.");
	                    			comments+="Fail- Test Step Feedback popup visible.";	                    			
	                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepPopupVisible");	           			
	                    			return false;
                    			}
                    			else
                    			{
                    				APP_LOGS.debug("Pass-Test Step Feedback popup is not visible after testing complete.");
	                    			comments+="Pass- Test Step Feedback popup is not visible after testing complete .";
	                    			return true;
                    			}
                    
		                    }
		        			
                			/*flag=true;
                            i=totalPages;
                            break;*/
                		
                    }//j
        			flag=true;
        		    i=totalPages;	
        			if (totalPages>1 && totalPages!=(totalPages-1)&&(flag==false)) 
                    {
                         getObject("DashboardMyActivity_NextLink").click();
                         totalPages--;
                    }
        		  }//i
        	  }
        }
		catch(Throwable t)
		{
			APP_LOGS.debug("My Activity Grid not visible");
			comments+="My Activity Grid not visible.";			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"MyActivityGridNotVisible");		  
			return false;
		}
		return true;
		
	}
	
	private void configuationSettingFeedbackRating(String ProjectName, String Version , String TestPassName) throws IOException
	{
		try
		{
			//Selecting created project name from 'Select Project:' drop down 
			Select selectProject = new Select(getElement("Configuration_selectProjectDropDown_Id"));			
			selectProject.selectByVisibleText(ProjectName);
			APP_LOGS.debug( ProjectName + ": is selected from 'Select Project'");				
	
			//Selecting created project's version from 'Select Version:' drop down 
			Select selectVersion = new Select(getElement("Configuration_selectVersionDropDown_Id"));
			selectVersion.selectByVisibleText(Version);
			APP_LOGS.debug( Version + ": is selected from 'Select Version'");
			
			//Selecting created project's version's Test Pass from 'Select Test Pass:' drop down 
			Select selectTestPass = new Select(getElement("Configuration_selectTestPassDropDown_Id"));
			selectTestPass.selectByVisibleText(TestPassName);
			APP_LOGS.debug( TestPassName + ": is selected from 'Select Test Pass'");			
			
			/*Verification of on Configuration page in Test Pass Level Options: for Select Feedback Rating :
	    	 "No Rating popup" selected as option
			*/
			
			String feedbackRatingOption3Label =getObject("Configuration_feedbackRatingOption3Label").getText();
	
			getObject("Configuration_feedbackRatingOption3RadioBtn").click();
			boolean lblFeedbackRatingOption3isSelected = getObject("Configuration_feedbackRatingOption3RadioBtn").isSelected();
			if(assertTrue(lblFeedbackRatingOption3isSelected))
			{
				APP_LOGS.debug( feedbackRatingOption3Label + ": is option selected from 'Select Feedback Rating Option'");
				comments+= feedbackRatingOption3Label + " : is option selected from 'Select Feedback Rating Option'";	
			}
			else
			{	
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FeedbackRatingOption3IsNotSelected");
				comments+= "Fail - Feedback Rating Option3 is not selecetd... So Skipping all tests in Testing Suite"; 
				throw new SkipException("Feedback Rating Option3 is not selecetd... So Skipping all tests in Testing Suite");
			}
		
			//After selecting option viz. "No Rating popup" clicking on Save buton
			getObject("Configuratiion_GeneralSettingSaveBtn").click();
			
			//Verification of success message clicking on Save button
			APP_LOGS.debug("Verification of General Settings pop up Success messsage clicking on Save button ");
			if(getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText().contains("configured successfully!"))
			{
				APP_LOGS.debug(getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText() + " : Message verified successfully");
				
				//Clicking on OK button
				getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
			}
			else
			{
				fail=true;
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Configuration Settings Message is not matched or unable to click on Ok button");
				APP_LOGS.debug("Fail - Configuration Settings Message is not matched or unable to click on Ok button .");
				comments+="Fail - Configuration Settings Message is not matched or unable to click on Ok button .";
			}
	
	   }
	   catch(Throwable t)
	   {
	    	fail=true;
			assertTrue(false);	
	    	APP_LOGS.debug("Fail : Unable to perform the Configuration Settings operation . ");
	    	comments+="Fail : Unable to perform the Configuration Settings operation.  ";		    
	   }
	}
}