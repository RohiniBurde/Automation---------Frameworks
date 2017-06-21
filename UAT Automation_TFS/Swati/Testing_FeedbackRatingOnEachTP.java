package com.uat.suite.testing_page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class Testing_FeedbackRatingOnEachTP extends TestSuiteBase
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
	String[] testCaseArray;
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
	boolean flag= false;
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
	
	// Test Case Implementation ...	
	@Test(dataProvider="getTestData")
	public void Test_Testing_FeedbackRatingOnEachTP 
			(       String Role,String GroupName,String PortfolioName,String ProjectName, String Version,
					String EndMonth, String EndYear, String EndDate, String VersionLead ,
					String TestPassName1,String TestPassName2, String TestManager,String Tester,String TesterRole1,
					String TesterRole2,	String TestCaseName, String TestStepDetails1,String TestStepExpectedResult1, 
					String TestStepDetails2,String TestStepExpectedResult2,String TestStepResult,String FeedbackRatingPopupText,
					String Rating, String Feedback
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
					
					//Creation of Project
					if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{	
						APP_LOGS.debug("Project : " +ProjectName+ " Creation Unsuccessful(Fail) by "+Role+". ");
						comments+="Project : " +ProjectName+ " Creation Unsuccessful(Fail) by "+Role+". ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");						
						throw new SkipException("Project : " +ProjectName+ " Creation Unsuccessfull");
					}
					APP_LOGS.debug("Project : " +ProjectName+ " : Created Successfully.  ");
					
					
					//Creation Test Pass1 					
					if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{	
						APP_LOGS.debug("Test Pass : " +TestPassName1+ " CreationUnsuccessful(Fail). ");
						comments+="Test Pass : " +TestPassName1+ " Creation Unsuccessful(Fail). " ;						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1+ " : TestPassCreationFailure");
						throw new SkipException(" Test Pass : " +TestPassName1+ "  Creation Unsuccessful ");
					}
					APP_LOGS.debug("Test Pass : " +TestPassName1+ " Created Successfully. ");		
					
				    
				    //Creation Test Pass2 					
					if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, EndMonth, EndYear, EndDate, testManager.get(0).username))
					{	
						APP_LOGS.debug("Test Pass : " +TestPassName2+ " CreationUnsuccessful(Fail). ");
						comments+="Test Pass : " +TestPassName2+ " Creation Unsuccessful(Fail). " ;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName2+ " : TestPassCreationFailure");
						throw new SkipException(" Test Pass : " +TestPassName2+ "  Creation Unsuccessful ");
					}
					APP_LOGS.debug("Test Pass : " +TestPassName2+ " Created Successfully. ");				  
				    
					/*
					  Clicking on Configuration Tab to select the created project, version and Test Pass and set Test Pass Level Option 
					 * "Select Feedback Rating Option:" as "After completion of each Test Pass activity."
					*/
					
				    Thread.sleep(1000);
				    try
				    {
					    //Click on Configuration tab				
						APP_LOGS.debug("Clicking On Configuration Tab ");				
						getElement("UAT_configuration_Id").click();							
						Thread.sleep(1000);
						
						//Calling configuationSettingFeedbackRating function to save the Feedback rating option
						configuationSettingFeedbackRating( ProjectName, Version ,  TestPassName1);
						 
						//Calling configuationSettingFeedbackRating function to save the Feedback rating option
						configuationSettingFeedbackRating( ProjectName, Version ,  TestPassName2);
				    }
				    catch(Throwable t)
				    {
				    	t.printStackTrace();
				    	fail=true;
				    	assertTrue(false);
				    	APP_LOGS.debug("Fail : Unable to perform the Configuration settings operation . ");
				    	comments+="Fail : Unable to perform the Configuration settings operation  ";
				    }
				    
				    //After performing Configuration Settings Operation Return to the Test Management Page and creation tester and rest of input data 				 
	 				APP_LOGS.debug(this.getClass().getSimpleName()+ " clicking on Test Management tab after configuring Generall Settings for Test Pass : " +TestPassName1+ "" +TestPassName2);
	 				getElement("UAT_testManagement_Id").click();
	 				Thread.sleep(2000);
	 				
					//Creation of Tester in Test Pass1					
					if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(0).username, TesterRole1,TesterRole1)) 
					{
						APP_LOGS.debug("Tester Creation Unsuccessfull");	
						comments+="Tester Creation Unsuccessful(Fail)" ;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 + " : TesterCreationFailure");				
						throw new SkipException("Tester Creation Unsuccessfull");
					}
					APP_LOGS.debug("Tester : " +tester.get(0).username+ " Created Successfully for Test Pass : " + TestPassName1);
					
					//Creation of Tester in Test Pass2					
					if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, tester.get(0).username, TesterRole2,TesterRole2)) 
					{	
						APP_LOGS.debug("Tester Creation Unsuccessfull");		
						comments+="Tester Creation Unsuccessful(Fail)" ;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName2 + " : TesterCreationFailure");			
						throw new SkipException("Tester Creation Unsuccessfull");
					}
					APP_LOGS.debug("Tester : " +tester.get(0).username+ " Created Successfully for Test Pass : " + TestPassName2);					
					
					//Creation of two Test Case in Test Pass1
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TestCaseName))
					{
						 APP_LOGS.debug("Test Case is not created successfully. ");
                    	 comments+="Fail- Test Case not Created Successfully. ";
     					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 + " : TestCaseCreationUnsuccessfull");
     					 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
					}
					
					APP_LOGS.debug(TestCaseName+ " : Test Case Created Successfully in Test Pass : " + TestPassName1);    				
    				
    				//Creation of two Test Case in Test Pass2    				
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TestCaseName))
					{
						 APP_LOGS.debug("Test Case is not created successfully");
                    	 comments+="Fail- Test Case not Created Successfully. ";                    	 
                    	 TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName2 + " : TestCaseCreationUnsuccessfull");     					  					 
            			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Testing Suite");
					}
					APP_LOGS.debug(TestCaseName+ " : Test Case Created Successfully in Test Pass : " + TestPassName2);
    				
    				
					//Creation of two test steps for Test Case1 of Test Pass1 Test Steps : " 
					testCaseArray = TestCaseName.split(",");
    				testSteps = TestStepDetails1.split(",");
    				
    				//Creation of two test steps for Test Case1 of Test Pass1    				
    				if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1,TestStepDetails1,TestStepExpectedResult1 , TestCaseName, TesterRole1))
    				{
                        	 APP_LOGS.debug("Test Step is not created successfully");
                        	 comments+="Test Step not Created Successfully(Fail). ";
         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");         					   					 
                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Testing Suite");
    				}
		        	
    				//Creation of two test steps for Test Case2 of Test Pass1 
    				APP_LOGS.debug("User "+ Role + " is creating : " +testSteps.length+ " Test Steps in Test Case  : " + testCaseArray[1] + " : of Test Pass : "  +TestPassName1 );
    				if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName2,TestStepDetails2,TestStepExpectedResult2 , TestCaseName, TesterRole2))
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
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, TestPassName1,TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult, Rating, Feedback, FeedbackRatingPopupText, "Begin Testing","Go to Feedback"))
    					{
    						APP_LOGS.debug("Testing for " +TestPassName1+ " with role " +TesterRole1+ " is unsuccessful.");
							comments+="Fail- "+ " Testing for " +TestPassName1+ " with role"+TesterRole1+ " is unsuccessful.";
    					}
    					
        				//Redirecting to Dashboard Page 
        				getElement("UAT_dashboard_Id").click();
        				Thread.sleep(1000);
        				
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, TestPassName2,TestStepDetails1,TestStepDetails2, TesterRole2, TestStepResult, Rating, Feedback, FeedbackRatingPopupText, "Begin Testing","Return to Home"))
    					{
    						APP_LOGS.debug("Testing for " +TestPassName1+ " with role " +TesterRole2+ " is unsuccessful.");
							comments+="Fail- "+ "Testing for " +TestPassName1+ " with role"+TesterRole2+ " is unsuccessful.";						
    					}
    					
    					if(!searchTestPassAndPerformTesting(ProjectName, Version, TestPassName1,TestStepDetails1,TestStepDetails2, TesterRole1, TestStepResult, Rating, Feedback, FeedbackRatingPopupText, "Testing Complete",""))
    					{
    						APP_LOGS.debug("Testing for " +TestPassName1+ " with role " +TesterRole2+ " is unsuccessful.");
							comments+="Fail- "+ "Testing for " +TestPassName1+ " with role"+TesterRole2+ " is unsuccessful.";
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
	
	@Override
	public boolean createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		Thread.sleep(2000);
		getElement("TesterNavigation_Id").click();
		
		if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
		{
			getObject("TesterCreateNew_TesterActiveX_Close").click();
		}
		Thread.sleep(2000);
		
		try {
		
			dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
			
			dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
			
			getElement("Tester_createNewProjectLink_Id").click();
			
			Thread.sleep(1000);
			
			getObject("TesterCreateNew_PeoplePickerImg").click();
			   
			driver.switchTo().frame(1);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
			   
			getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
	   
			getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
	   
			getObject("TesterCreateNew_PeoplePickerOkBtn").click();
	   
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			String[] testerRoleArray = testerRoleCreation.split(",");
			
			for(int i=0;i<testerRoleArray.length;i++)
			{
				if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
				{
					getElement("TesterCreateNew_addTesterRoleLink_Id").click();
					getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
					getElement("TesterCreateNew_addTesterRole_Id").click();
					if(getElement("TesterCreateNew_roleAlreayExistsAlert_Id").isDisplayed())
					{
						getObject("TesterCreateNew_roleAlreayExistsAlertOkBtn").click();
						getElement("TesterCreateNew_addroleCancelBtn_Id").click();
					}
				}
			}
			System.out.println(testerRoleSelection);
			System.out.println(testerRoleSelection.split(",").length);
			String[] testerRoleSelectionArray = testerRoleSelection.split(",");
			System.out.println(testerRoleSelectionArray.length);
			List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
			int numOfRoles = roleSelectionNames.size();
			for(int i = 0; i<numOfRoles;i++)
			{
				for(int j = 0; j < testerRoleSelectionArray.length;j++)
				{
					if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
					{
						getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
					}
				}
			}
			getElement("TesterCreateNew_testerSaveBtn_Id").click();
			
			Thread.sleep(2000);
			
			if (getElement("TesterCreateNew_testerSuccessMessageText_Id").getText().contains("successfully")) 
			{
				getObject("Tester_testeraddedsuccessfullyOkButton").click();
				
				return true;
			}
			else 
			{
				return false;
			}	
	} 
	catch (Throwable e) 
	{
		APP_LOGS.debug("Exception in createTester function.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public boolean createTestCase(String group, String portfolio, String project, String version, String testPassName, 
			String testCaseName) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Case");
		Thread.sleep(2000);
		getElement("TestCaseNavigation_Id").click();
		if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
		{
			getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
		}
		Thread.sleep(2000);

		try {
			
				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(1000);				
				
				testCaseArray = testCaseName.split(",");
				
				for(int testCaseCount =0 ;testCaseCount<testCaseArray.length;testCaseCount++)
				{	
						getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseArray[testCaseCount]); 
						
						getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
						
						Thread.sleep(2000);
						
						if (getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id").getText().contains("successfully")) 
						{
							getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
						}
						else 
						{
							return false;
						}
						
				}	
				 return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestCase function.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public boolean createTestStep(String group, String portfolio, String project, String version, String testPassName, 
			String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Step");
		Thread.sleep(2000);
		getElement("TestStepNavigation_Id").click();
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
		}
		Thread.sleep(2000);
		
		try {
			
			dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), version );
			
			dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
			
			getObject("TestStep_createNewProjectLink").click();
			
			Thread.sleep(1000);
			
			testCaseArray = testCasesToBeSelected.split(",");
			
			
			for(int m=0;m<testCaseArray.length;m++)
		    {
				 
				List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
				int numOfTestCases = TestCaseSelectionNames.size();
				for(i = 0; i<numOfTestCases;i++)
				{
						if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCaseArray[m]))
						{
							getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
							break;
						}
				}
				
				testSteps = testStepName.split(",");
				
				for(int k =0;k<testSteps.length;k++)
				{
					
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testSteps[k])+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				    
				    String[] testStep_ExpectedResult = testStepExpectedResults.split(",");
				    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStep_ExpectedResult[k]); 
				    				
					String[] testerRoleSelectionArray = rolesToBeSelected.split(",");
					List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
							{
								getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
						}
					}
		    
					getElement("TestStepCreateNew_testStepSaveBtn_Id").click();
					
					Thread.sleep(2000);
				
					if (getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
						APP_LOGS.debug("Test Step : " +(testSteps[k])+ " : is created successfully for Test Case : " + testCasesToBeSelected + " : of Test Pass : " +testPassName);
						comments+="Pass- Test Step : " +(testSteps[k])+ " : is created successfully for Test Case : " + testCasesToBeSelected + " : of Test Pass : " +testPassName ;
					}
					else 
					{
						return false;
					}
			  }
		      getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
		   
		    }
		  
		   return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName1,String testStepName2,String testerRole, String testStepResult, 
			String Rating, String Feedback, String expectedFeedbackRatingTPTitle,String action, String ButtonOption)
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
						
					
					if (projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole)) 
					{
						comments+="Pass - Verification of Project Name , Version , Test Pass and  Role of Tester in My Activities is successful. ";
						APP_LOGS.debug(tester.get(0).username + " : is  having assigned Test Steps for Test Pass : " +testPass+ " :  of Project : " +project+ "with role as : " +roleCell);
						
						//Verifying Days Left , Not Completed  , Pass , Fail test steps counts with the action cell
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
		                    		
		                    		//Verify test pass feedback popup displays or not
		                    		if(getObject("TestingPage_feebackRatingPopup").isDisplayed())
		                    		{
		                    			APP_LOGS.debug("Pass - Verification of \"After completion of each Test Pass activity.\" feedback pop up display . " );
		                    			comments+="Pass - Verification of \"After completion of each Test Pass activity.\" feedback pop up display . ";
		                    			
		                    			//Test Pass Feedback Pop up message verification
		                    			APP_LOGS.debug("Verification of title of Test Pass Feedback rating pop up. ");
		                    			String actualFeedbackRatingTPTitle = getObject("TestingPage_feebackRatingPopup_Text").getText();
		                    			
		                    			//Comparision of Test Pass feedback pop up title with the expected 
		                    			if(compareStrings(expectedFeedbackRatingTPTitle, actualFeedbackRatingTPTitle))
		                    			{
		                    				APP_LOGS.debug("Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is successful.");
		                    				comments+="Pass- Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is successful. ";
		                    			}
		                    			else
		                    			{
		                    				fail=true;
		                    				assertTrue(false);
		                    				APP_LOGS.debug("Fail - Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is not match.");
		                    				comments+="Fail - Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is not match. ";
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
		                    			else if(Rating.equals(getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value")))
		                    			{
		                    				APP_LOGS.debug("Rating is "+getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value"));
		                    				getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").click();
		                    			}
		                    			
		                    			//Provide feedback
		                    			String feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+Feedback+"')";
		                    			eventfiringdriver.executeScript(feedbackDetails);
		                    			
		                    			//save
		                    			getObject("TestingPage_feedbackRating_SaveButton").click();
		                    			Thread.sleep(500);			                    			
		                    			
		                    			//Test Step Feedback Pop up verification 
		                    			if(getObject("TestingPage_testStepFeebackPopup").isDisplayed())
		                    			{
			                    			APP_LOGS.debug("Pass - Verification of Test Step Feedback is display");
			                    			comments+="Pass - Verification of Test Step Feedback is display ";					                    			
			                    			
			                    			//Verification of 'Go to Feedback' button and 'Return to Home' button are displaying in Test Step Feedback pop up				                    			
			                    			if(assertTrue(getObject("TestingPage_goToFeedbackButton").isDisplayed())&&(getObject("TestingPage_returnToHomeButton").isDisplayed()))
			                    			{
			                    				APP_LOGS.debug("Pass - Verification of 'Go to Feedback' and 'Return to Home' buttons are displaying in Test Step Feedback Pop Up .");
			                    				comments+="Pass - Verification of 'Go to Feedback' and 'Return to Home' buttons are displaying in Test Step Feedback Pop Up . ";
			                    			}
			                    			else
			                    			{
			                    				fail=true;
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
		                    			assertTrue(false);
		                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FeedbackRatingPopupNotVisible");
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
	                    		getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
	                    		Thread.sleep(3000);
	                    		getElement("TestingPage_passRadioButton_Id").click();
	                    		getElement("TestingPage_saveButton_Id").click();
                    			Thread.sleep(500);
	                    		
	                    		
	                    		//verify test pass feedback popup displays or not	                    		
	                    		if(getObject("TestingPage_feebackRatingPopup").isDisplayed())
	                    		{
	                    			APP_LOGS.debug("Pass - Verification of \"After completion of each Test Pass activity.\" feedback pop up display is successful. " );
	                    			comments+="Pass - Verification of \"After completion of each Test Pass activity.\" feedback pop up display is successful. ";
	                    			
	                    			//Test Pass Feedback Pop up message verification
	                    			APP_LOGS.debug("Verification of title of Test Pass Feedback rating pop up. ");
	                    			String actualFeedbackRatingTPTitle = getObject("TestingPage_feebackRatingPopup_Text").getText();
	                    			
	                    			//Comparision of Test Pass feedback pop up title with the expected 
	                    			if(compareStrings(expectedFeedbackRatingTPTitle, actualFeedbackRatingTPTitle))
	                    			{
	                    				APP_LOGS.debug("Pass- Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is successful.");
	                    				comments+="Pass- Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is successful. ";
	                    			}
	                    			else
	                    			{
	                    				fail=true;
	                    				assertTrue(false);
	                    				APP_LOGS.debug("Fail - Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is not match.");
	                    				comments+="Fail - Verification of title : " +expectedFeedbackRatingTPTitle+ " on Test Pass Feedback Pop Up is not match. ";
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
	                    			
	                    			//Test Step Feedback Pop up verification 
	                    			APP_LOGS.debug("Verification of in case of Testing Complete, Test Step feedback pop up is display or  not. " );
	                    			if(isElementExists(OR.getProperty("TestingPage_testStepFeebackPopup")))	
	                    			{
	                    				fail=true;
		                    			APP_LOGS.debug("Fail-Test Step Feedback popup visible in case of Testing Complete .");
		                    			comments+="Fail- Test Step Feedback popup visible in case of Testing Complete.";
		                    			assertTrue(false);
		                    			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStepPopupVisible");	
		                    		                    			                  			
	                    			}
	                    			else
	                    			{
	                    				APP_LOGS.debug("Pass-Test Step Feedback popup is not visible after testing complete.");
		                    			comments+="Pass- Test Step Feedback popup is not visible after testing complete .";
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
	                    		flag=true;
	                            i=totalPages;
	                            break;
		                    }
		                 
	        		   }
	        	    }//j
					
	   			 	if (totalPages>1 && totalPages!=(totalPages-1)&&(flag==false)) 
	                {
	                    getObject("DashboardMyActivity_NextLink").click();
	                    totalPages--;
	                }
	   			 	
	        	}
		}
		catch(Throwable t)
		{
			fail=true;
			assertTrue(false);
			APP_LOGS.debug("My Activity Grid not visible");
			comments+="My Activity Grid not visible.";			
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"MyActivityGridNotVisible");		  
			return false;
		}
		return true;
		
}
	
	private void configuationSettingFeedbackRating(String ProjectName, String Version , String TestPassName) throws IOException
	{
			//Selecting created project name from 'Select Project:' drop down 
			Select selectProject = new Select(getElement("Configuration_selectProjectDropDown_Id"));			
			selectProject.selectByVisibleText(ProjectName);
			APP_LOGS.debug( ProjectName + ": is selected from \"'Select Project'\" : ");					

			//Selecting created project's version from 'Select Version:' drop down 
			Select selectVersion = new Select(getElement("Configuration_selectVersionDropDown_Id"));
			selectVersion.selectByVisibleText(Version);
			APP_LOGS.debug( Version + ": is selected from \"'Select Version'\":");		
			
			//Selecting created project's version's Test Pass from 'Select Test Pass:' drop down 
			Select selectTestPass = new Select(getElement("Configuration_selectTestPassDropDown_Id"));
			selectTestPass.selectByVisibleText(TestPassName);
			APP_LOGS.debug( TestPassName + ": is selected from \"'Select Test Pass'\":");			
		
			/* Verification of on Configuration page in Test Pass Level Options: for Select Feedback Rating :
	    	 "After completion of each Test Pass activity" selected by default 
			*/
			
			APP_LOGS.debug("Verification of Feedback rating Option default selection . ");
			String feedbackRatingOption1Label =getObject("Configuration_feedbackRatingOption1Label").getText();
	
			boolean lblFeedbackRatingOption1isSelected = getObject("Configuration_feedbackRatingOption1RadioBtn").isSelected();
			if(assertTrue(lblFeedbackRatingOption1isSelected))
			{
				APP_LOGS.debug( "Pass- " + feedbackRatingOption1Label + ": is selected as default option from \"'Select Feedback Rating Option'\":");
				comments+= feedbackRatingOption1Label + ": is selected as default option from \"'Select Feedback Rating Option'\":";	
			}
			else
			{
				fail=true;
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FeedbackRatingOption1IsNotDefaultSelectedOption");
				comments+= "Fail -" + feedbackRatingOption1Label + "is not selected as default option in Select Feedback Rating Option . ";
			}
		
			//After verification of option viz. "After completion of each Test Pass activity" is selected by default clicking on Save buton			
			getObject("Configuratiion_GeneralSettingSaveBtn").click();
			
			//Verification of success message clicking on Svae button
			APP_LOGS.debug("Verification of General Settings pop up Success messsage clicking on Save button ");
			if(getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText().contains("configured successfully!"))
			{
				APP_LOGS.debug(getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText() + " : Message verified successfully");
				
				//Clicking on OK button
				getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
			}
			else
			{
				comments+="Fail : Unable to click on Ok button of General Setting Success Message Pop Up. ";
				APP_LOGS.debug("Fail : Unable to click on Ok button of General Setting Success Message Pop Up. ");
			}
		
	}
}
