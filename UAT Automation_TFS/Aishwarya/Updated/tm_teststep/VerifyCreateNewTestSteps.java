package com.uat.suite.tm_teststep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyCreateNewTestSteps extends TestSuiteBase{
	

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
		public void checkTestSkip() throws Exception{
			APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());						
			
			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		@Test(dataProvider="getTestData")
		public void Test_VerifyCreateNewTestSteps(String Role, String GroupName, String Portfolio, String ProjectName, String Version, String endMonth,
				String endYear, String endDate, String VersionLead,String testPassName, String testManager, String testerName, String testerRole, 
				String testCaseName, String testStepName, String expectedBlankFieldsMessage, String assignedRole, String expectedResult, 
				String expectedTestStepsAddedSuccessMessage) throws Exception
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

				try
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
					comments=ProjectName+" Project Created Successfully(Pass). ";
				
					closeBrowser();
					
					APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");
					
					APP_LOGS.debug("Opening Browser... ");
					
					openBrowser();
					
					if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
					{   
						Thread.sleep(2000);
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
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTesterCreation");
							throw new SkipException("Tester not created successfully");
						}
						APP_LOGS.debug(" Tester Created Successfully.");
						comments+=" Tester Created Successfully(Pass). ";

						if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
						{
							fail=true;
							APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
							comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulTestCaseCreation");
							throw new SkipException("Test Case not created successfully");
						}
						APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
						comments+=testCaseName+" Test Case Created Successfully(Pass). ";
						
						closeBrowser();
		 		    	APP_LOGS.debug(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");
						
						APP_LOGS.debug("Opening Browser... ");
		 				
						openBrowser();
		 				
		 				if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
		 				{
			 				APP_LOGS.debug("Logged in browser with Test Manager");
		 	            
			 				//click on testManagement tab
			 				APP_LOGS.debug(" Clicking on Test Management Tab");
			 				getElement("UAT_testManagement_Id").click();
			 				Thread.sleep(3000);
			 				
			 				//clicking on TestStep tab
							getElement("TM_testStepsTab_Id").click();
							APP_LOGS.debug("Test Step tab clicked ");
							Thread.sleep(2000);
							
							
							//Validation
							dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), GroupName );
							
							dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), Portfolio );
							
							dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), ProjectName );
							
							dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), Version );
							
							dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
							
							getObject("TestStep_createNewProjectLink").click();
							
							//Clicking on save button without entering any data
							boolean blankTestStepMessageResult= blankFeildValidation(expectedBlankFieldsMessage);
							
							if(blankTestStepMessageResult==true)
							{
								comments+= " Clicked on save button without entering any data and correct message was displayed(Pass).";
							}
							else
							{
								comments+= " Clicked on save button without entering any data and correct message was not displayed(Fail).";
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectBlankTestStepMessage");
							}
						    
							//Clicking on save button after entering test step details
						    String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+"')";
						    eventfiringdriver.executeScript(testStepDetails);
						    
						    boolean blankTestCaseMessageResult= blankFeildValidation(expectedBlankFieldsMessage);
						    
							if(blankTestCaseMessageResult==true){
								comments+= " Clicked on save button after entering test step details only and correct message was displayed(Pass).";
							}
							else
							{
								comments+= " Clicked on save button after entering test step details only and correct message was not displayed(Fail).";
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectBlankTestCaseMessage");
							}
			 
							//Clicking on save button after entering test step details and selecting test case
						    List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
							int numOfTestCases = TestCaseSelectionNames.size();
							for(int i = 0; i<numOfTestCases;i++)
							{
									if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCaseName))
									{
										getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
										break;
									}
							}
						    
							boolean blankAssignedRoleMessageResult= blankFeildValidation(expectedBlankFieldsMessage);
							
							if(blankAssignedRoleMessageResult==true)
							{
								comments+= " Clicked on save button after entering test step details and selecting test case and correct message was displayed(Pass).";
							}
							else
							{
								comments+= " Clicked on save button after entering test step details and selecting test case and correct message was not displayed(Fail).";
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectBlankAssignedRoleMessage");
							}
							
						    APP_LOGS.debug("Validation Completed for all mandatory fields in Create New Test Step ");    
						   
						    if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, expectedResult, testCaseName, assignedRole))
						    {
						    	fail=true;
						    	APP_LOGS.debug(testStepName+": Test Step is not saved Successfully ");
						    	comments+="TestStep not saved successfully(Fail)";
						    	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepCreation");
	
						    }
						    
							comments+=testStepName+" Test Step Created Successfully(Pass). ";
							
							getObject("TestSteps_viewAllLink").click();
							
							if(searchForTheTestStep(testStepName))
							{
								comments+="Saved Test Step found in TS Grid (PASS) ";
								if (assertTrue(getObject("TestSteps_EditTestStepsLink").isDisplayed())) 
								{
									APP_LOGS.debug("Edit link displayed.");
									comments+="Edit link displayed on clicking edit icon(Pass). ";
								}
								else 
								{
									fail = true;
									APP_LOGS.debug("Edit link not displayed.");
									comments+="Edit link not displayed on clicking edit icon(Fail). ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit link not displayed on clicking edit icon");
								}
							}
							else
							{
								fail = true;
								comments+="Saved Test Step not found in TS Grid(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Saved Test Step not found in TS Grid");
								assertTrue(false);
							}
							if(verifyTestStepFields(testStepName, expectedResult, testCaseName, assignedRole))  
							{
								comments+="All the test step fields are verified and they have been saved correctly (PASS) ";
							}
							else
							{
								fail = true;
								comments+="The test step details saved did not match while verification(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Saved Test Step had incorrect entries");
								assertTrue(false);
							}
							
						}
						else
						{
							fail=true;
							APP_LOGS.debug("Login Unsuccessful for Test Manager");
							comments+="Login Unsuccessful for Test Manager";
						}
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Login Unsuccessful for version Lead");
						comments+="Login Unsuccessful for version Lead";
					}
				}
				catch(Throwable t)
	            {
		         	 fail=true;
		           	 t.printStackTrace();
		           	 comments="Something went wrong while executing Create Test Step Class (FAIL) ";
		           	 assertTrue(false);
		           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),"Something went wrong while executing Create Test Step Class");
	            }
				APP_LOGS.debug("Closing Browser... ");
			    closeBrowser();	
			}
			
			else 
			{
				APP_LOGS.debug("Login Not Successful");
			}	
		}
		
		private boolean blankFeildValidation(String expectedfieldBlankMessage) throws IOException, InterruptedException{
			
			getElement("TestStepCreateNew_testStepSaveBtn_Id").click();   //save button
			
			String actualFieldBlankMessage=getElement("TestStepCreateNew_successMessagePopupText_Id").getText();
			boolean result=compareStrings(actualFieldBlankMessage, expectedfieldBlankMessage);
			
			getObject("TestStep_testStepaddedsuccessfullyOkButton").click();      //Ok button

			return result;
		}
		@AfterMethod
		public void reportDataSetResult()
		{
			if(skip)
			{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(!isLoginSuccess)
			{
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(fail)
			{
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else
			{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;	

		}
		
		@AfterTest
		public void reportTestResult() throws Exception
		{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}
		
		@DataProvider
		public Object[][] getTestData()
		{
			return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
		}
		
}
