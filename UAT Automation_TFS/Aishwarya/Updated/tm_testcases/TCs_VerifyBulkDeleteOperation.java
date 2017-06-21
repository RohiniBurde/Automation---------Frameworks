package com.uat.suite.tm_testcases;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class TCs_VerifyBulkDeleteOperation extends TestSuiteBase {
	
	
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
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip() throws Exception{
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			testManager=new ArrayList<Credentials>();
			testers=new ArrayList<Credentials>();
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
	
		// Test Case Implementation ...
			
		@Test(dataProvider="getTestData")
		public void verifyBulkDeleteOperation (String GroupName,String PortfolioName,String ProjectName, String Version,
				String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,String TestManager,
				String Tester,String AddRole,String TestCaseName,String ExpectedBulkDeleteConfirmationMessage,
				String ExpectedBulkDeleteTestCasesSuccessMessage ) throws Exception
		{
			
			count++;
			comments="";
			String role = "Admin";
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
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
			
			
			
			APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
			
			
			APP_LOGS.debug("Opening Browser... ");
			
			
			openBrowser();
			
			
			isLoginSuccess = login(role);
			

			if(isLoginSuccess)
			{
				try
				{
					
				/*	//click on testManagement tab
					APP_LOGS.debug("Clicking On Test Management Tab ");					
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(1000);
					
					APP_LOGS.debug(" User "+ role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
					
					
					if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{	
						
						//fail=true;
						//assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
						comments+="Project Creation Unsuccessful(Fail) by "+role+". ";
						APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+". ");
						//closeBrowser();
						throw new SkipException("Project Creation Unsuccessfull");
					}
					*/
					APP_LOGS.debug("Closing Browser... ");										
					
					closeBrowser();				
					
					APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
										
					openBrowser();
					
					if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
					{												
					/*	//click on testManagement tab
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
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
							//closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessfull");
						}*/
						
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
							
							/*Thread.sleep(2000);
							
							if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
								comments+="Tester Creation Unsuccessful(Fail). " ;
								APP_LOGS.debug("Tester Creation Unsuccessfull");
								//closeBrowser();
								throw new SkipException("Tester Creation Unsuccessfull");
							}
							*/
							
							
							APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
					
							Thread.sleep(2000);
					
							// Calling Function To Create Test Cases
					
							createBulkTestCases(GroupName,PortfolioName,ProjectName,Version,TestPassName,TestCaseName);
							
							Thread.sleep(1000);
							
							
							//Clicking On View All Link Of Test Cases
							APP_LOGS.debug("Test Cases :Clicking On View All Link");
					
							
					
							getObject("TestCases_viewAllTestCasesLink").click();
				
							//Clicking On Bulk Delete Button to delete All Created Test Cases
				
							APP_LOGS.debug("Clicking On Bulk Delete Button ");
						
				
							Thread.sleep(1000);
							
							//String BulkDeleteButtonText=getElement("TestCases_ViewAllBulkDeleteButton_Id").getText();
				
				
							Thread.sleep(1000);
				
							getElement("TestCases_ViewAllBulkDeleteButton_Id").click();
					
							String actualBulkDeleteConfirmationMessage=getElement("TestCases_ViewAll_deleteTestCasesConfirmationText_Id").getText();
						
							if(compareStrings(ExpectedBulkDeleteConfirmationMessage, actualBulkDeleteConfirmationMessage))
							{						
								APP_LOGS.debug("TEST CASES : Bulk Delete Button Confirmation Message showing Properly stating : " + actualBulkDeleteConfirmationMessage);
						   						
							}							
							else
							{
								fail=true;	
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BulkDeleteConfirmationMessageIssue");
								APP_LOGS.debug("Bulk Delete Confirmation Meesage is not Proper (Fail)");			
								
								comments+="Bulk Delete Confirmation Meesage is not proper (Fail).   .";
							}
							
					
							Thread.sleep(2000);
							
						
							getObject("ProjectViewAll_delectProjectPopUpCancelButton").click();
							
							if (getElement("TestCases_NoTestCasesAvailable_Id").isDisplayed()) 
							{
								fail = true;
								assertTrue(false);
								comments+="Test Cases Deleted after Cancel Click (Fail).  ";
								APP_LOGS.debug("Test Cases Deleted after Cancel Click (Fail). ");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case deleted on Cancel");
							}
							else
							{
								comments+="Test Cases Present after Cancel Click (Pass).  ";
								APP_LOGS.debug("Test Cases Present after Cancel Click (Pass). ");
							}
							
							
							Thread.sleep(1000);
							
							getElement("TestCases_ViewAllBulkDeleteButton_Id").click();
							
							APP_LOGS.debug("Clicking On Delete Button to delete Test Cases");						
						
		
							getObject("ProjectViewAll_PopUpDeleteButton").click();
						
							//Verifying Test cases Deleted COnfirmation Message....
							String actualTestCasesDeletedSuccessMessage = wait.until(ExpectedConditions.visibilityOf(getElement("TestCases_ViewAll_TestCasesDeletedSuccessMessage_Id"))).getText();
						    
							if(compareStrings(actualTestCasesDeletedSuccessMessage, ExpectedBulkDeleteTestCasesSuccessMessage ))
						    
							{	
								comments+="Bulk Delete SuccessMessage is proper(Pass). ";
								APP_LOGS.debug("Test Cases are  Deleted Successfully with Success Message Stating :  "+ actualTestCasesDeletedSuccessMessage);
							}
							else
							{
								fail=true;	
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BulkDeleteSUccessMessageIssue");
								APP_LOGS.debug("Bulk Delete SuccessMessage is not Proper  (Fail)");
								comments+="Bulk Delete SuccessMessage is not Proper  (Fail)";
							}							
							
						
						
					//		getObject("TestCases_ViewAll_DeleteConfirmOkButton").click();
							
							Thread.sleep(1000);
				
							//Verifying " No Test Case(es) Available" Text is present or not when No test Cases available on Page
				
							//String noTestCaseAvailText =getElement("TestCases_NoTestCasesAvailable_Id").getText();
							
					
							if(getElement("TestCases_NoTestCasesAvailable_Id").isDisplayed())
							{					
								comments+="Test Cases Deleted after Delete Confirmation (Pass).  ";
								APP_LOGS.debug("Test Cases Deleted after Delete Confirmation (Pass). ");
					
							}
							else
							{
								fail=true;	
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoTestCaseAvailTextOnViewAllPageIssue");
								comments+="Test Cases Present after Delete Confirmation (Fail).  ";
								APP_LOGS.debug("Test Cases Present after Delete Confirmation (Fail). ");
								
							}
			
		
			
						}
						else 
						{
							fail= true;
							APP_LOGS.debug("Test Manager Login Not Successful");
							
						}
						
					}
					else 
					{
						fail= true;
						APP_LOGS.debug("Version Lead Login Not Successful");
						
					}
					
				}
				catch (Throwable e) 
				{
						e.printStackTrace();
						fail = true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
						comments+="Skip Exception or other exception occured. " ;
				}
				
		
				APP_LOGS.debug("Closing Browser... ");
		
	
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
				
				else if(!isLoginSuccess){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				}
				else if(fail){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
					TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}
				else{
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
					TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}
				skip=false;
				fail=false;
				
		
			}
			
			
			@AfterTest
			public void reportTestResult() throws Exception{
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
				utilRecorder.stopRecording();
			}
			
			
			
			
			
			@DataProvider
			public Object[][] getTestData(){
				return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
			}
			
			
			
			
			public void createBulkTestCases(String group, String portfolio, String project, String version, String testPassName, 
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

				try
				{
					
						dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
						
						dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
						
						dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
						
						dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
						
						dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
						
						getObject("TestCase_createNewProjectLink").click();
						
						Thread.sleep(1000);
						
						
						for(int i=1;i<=4;i++)
						{
							getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName+i); 
						
							getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
							
						//	getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
						
						}
							
						/*if (getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id").getText().contains("successfully")) 
						{
								return true;
						}
						else 
						{
								return false;
						}*/
						
						
				} 
				catch (Throwable e) 
				{	
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Exception in createTestCase function.");
					e.printStackTrace();
					
				}
				
			}

}
