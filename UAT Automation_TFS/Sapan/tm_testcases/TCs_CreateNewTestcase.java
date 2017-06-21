package com.uat.suite.tm_testcases;

import java.io.IOException;
import java.util.ArrayList;

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

public class TCs_CreateNewTestcase extends TestSuiteBase {
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	String testCaseCreatedSuccessMessage;
	String comments;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			testManager=new ArrayList<Credentials>();
			testers=new ArrayList<Credentials>();
		}
	
		// Test Case Implementation ...
			
			@Test(dataProvider="getTestData")
			public void Test_TCs_CreateNewTestcase (String Role,String GroupName,String PortfolioName,String ProjectName, 
					String Version,String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,
					String TestManager,String Tester,String AddRole,String TestCaseName, String testCaseDescription, 
					String estimatedTestTime, String ExpectedTestCaseCreatedSuccessMessage ) throws Exception
			{
				count++;
				comments="";
				
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
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
					
					try
					{
					
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(1000);
						APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
						if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{	
							
							//fail=true;
							//assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
							comments+="Project Creation Unsuccessful(Fail) by "+Role+"  . ";
							APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+" . ");
							//closeBrowser();
							throw new SkipException("Project Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						closeBrowser();
						
					
						
						APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
						
						
						openBrowser();
						
						if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
						{
							
							
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							
							Thread.sleep(800);
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
							
							if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{	
								
								
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
								comments+="Test Pass Creation Unsuccessful(Fail)   ." ;
								APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
								//closeBrowser();
								throw new SkipException("Test Pass Creation Unsuccessfull");
							}
							
							APP_LOGS.debug("Closing Browser... ");
							
													
							closeBrowser();
							
							
							

							APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
							
							
							openBrowser();
							
							if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
							{
								
								//click on testManagement tab
								APP_LOGS.debug("Clicking On Test Management Tab ");
								
								Thread.sleep(800);
								getElement("UAT_testManagement_Id").click();
								
								Thread.sleep(2000);
								
								if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
								{	
									//fail=true;
									//assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
									comments+="Tester Creation Unsuccessful(Fail)   ." ;
									APP_LOGS.debug("Tester Creation Unsuccessfull");
									//closeBrowser();
									throw new SkipException("Tester Creation Unsuccessfull");
									
									
								}
								
								
								
								APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
								
								if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName,TestCaseName, 
										testCaseDescription, estimatedTestTime, ExpectedTestCaseCreatedSuccessMessage))
								{	
									//fail=true;
									//assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
									APP_LOGS.debug("Test Case Creation Unsuccessfull");
									comments+="Test Case Creation Unsuccessful(Fail)   ." ;
									//closeBrowser();
									throw new SkipException("Test Case Creation Unsuccessfull");
								}								
								
								comments+="Test Case Creation successful(Pass)   ." ;
								
								
								//Clicking On View All Link Of Test Cases
								
								Thread.sleep(1000);
								
								APP_LOGS.debug("Test Cases :Clicking On View All Link");
								
								
								
									
								getObject("TestCases_viewAllTestCasesLink").click();
								
								boolean totalRecordsDivVisible= getElement("TestCases_viewAll_totalRecordsDiv_Id").isDisplayed();
								
								if(assertTrue(totalRecordsDivVisible))
								{
									comments+="Total Records Visible(Pass) ." ;
									
									if (compareStrings("1", getElement("TestCases_viewAll_totalRecordsCountText_Id").getText())) 
									{
										comments+="Total Records Count is correct(Pass) ." ;
									}
									else
									{
										fail = true;
										comments+="Total Records Count is not correct(Fail) ." ;
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total Records Count not correct");
									}
								}
								else
								{
									comments+="Total Records Not Visible(Fail) ." ;
									fail= true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Total Records Div Not visible");
								}
								
								String ActualTestCaseNamePresentInTestCaseGrid=getObject("TestCases_ViewAll_TestCaseNameText").getText();	
								String actualEstimatedTestTime=getObject("TestCases_ViewAll_ETT").getText();
								
								
								if (TestCaseName.equals(ActualTestCaseNamePresentInTestCaseGrid) && estimatedTestTime.equals(actualEstimatedTestTime))
								{
									
									APP_LOGS.debug("Test Case Name : "+TestCaseName +"  is created Successfully and showing Properly in Test CaseS Grid");
									
									comments=comments+"Test Case Name : "+TestCaseName +"  is created Successfully and showing Properly in Test CaseS Grid(Pass)   .";
								}
								else 
								{
									assertTrue(false);
									fail=true;	
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseNameNotPresentInTCGrid");
									
									APP_LOGS.debug("TestCase Creation Failure :Test Case Name "+TestCaseName + " is not Present in TeseCase Table");
									
									comments=comments+"TestCase not availablet in Test Case Table(Fail)  .";
									
								}							
						
								
								APP_LOGS.debug("Closing Browser... ");
								
							
								//closeBrowser();
								
							}
							else 
							{
								fail= true;
								comments+="Test Manager Login Not Successful";
								APP_LOGS.debug("Test Manager Login Not Successful");
													
							}
						}
						else
						{
							fail= true;
							comments+="Version Lead Login Not Successful";
							APP_LOGS.debug("Version Lead Login Not Successful");
							
						}
						
					}							
					catch (Throwable e) 
					{
						e.printStackTrace();
						assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
						comments+="Skip Exception or other exception occured" ;
					}
					
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
					else
					{
						TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
						TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
					}
					skip=false;
					fail=false;
					
			
				}
				

				@AfterTest
				public void reportTestResult(){
					if(isTestPass)
						TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
					else
						TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
				
				}
				
				
				
				@DataProvider
				public Object[][] getTestData(){
					return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
				}

				
				
				
				
				private boolean createTestCase(String group, String portfolio, String project, String version, String testPassName, 
						String testCaseName, String description, String estimatedTestTime, String expectedMessage) throws IOException, InterruptedException
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
							
							getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName);
							
							getElement("TestCases_CreateNew_TestCaseDescriptionTextField_Id").clear();
							
							getElement("TestCases_CreateNew_TestCaseDescriptionTextField_Id").sendKeys(description);
							
							getElement("TestCases_CreateNew_TestCaseEstTimeInMinTextField_Id").sendKeys(estimatedTestTime);
							
							getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
							
							Thread.sleep(2000);
							
							if (getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id").getText().equals(expectedMessage)) 
							{
								getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
								
								return true;
							}
							else 
							{
								return false;
							}
							
							
						
					} 
					catch (Throwable e) 
					{
						APP_LOGS.debug("Exception in createTestCase function.");
						e.printStackTrace();
						return false;
					}
					
				}
				
					
}			
				
	




				