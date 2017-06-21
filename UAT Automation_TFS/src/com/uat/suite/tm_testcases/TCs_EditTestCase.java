package com.uat.suite.tm_testcases;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
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

public class TCs_EditTestCase extends TestSuiteBase {
	
	
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
		public void Test_TCs_EditTestCase (String Role,String GroupName,String PortfolioName,String ProjectName, String Version,
				String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,String TestManager,
				String Tester,String AddRole,String TestCaseName, String description, String estimatedTestTime, String ExpectedTestCaseCreatedSuccessMessage, 
				String EditTestCaseName,String editDescription, String editEstimatedTesttime,String ExpectedTestCaseUpdatedTestCasesSuccessMessage ) throws Exception
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
				try{
				
					/*//click on testManagement tab
					APP_LOGS.debug("Clicking On Test Management Tab ");
					
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(1000);
					
					APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
					
					
					if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{	
						
						//fail=true;
						//assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
						comments+="Project Creation Unsuccessful(Fail) by "+Role+". ";
						APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
						//closeBrowser();
						throw new SkipException("Project Creation Unsuccessful");
					}
					
					APP_LOGS.debug("Closing Browser... ");
					*/
					
					closeBrowser();
					
				
					
					APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
					
					
					openBrowser();
					
					if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
					{
						
						
					/*	//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						
						Thread.sleep(800);
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
						{	
							//fail=true;
							//assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
							comments+="Test Pass Creation Unsuccessful(Fail)" ;
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)   .");
							//closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessful");
						}
					
						APP_LOGS.debug("Closing Browser... ");
						
					*/							
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
						
					/*		if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
								comments+="Tester Creation Unsuccessful(Fail)  ." ;
								APP_LOGS.debug("Tester Creation Unsuccessful");
								//closeBrowser();
								throw new SkipException("Tester Creation Unsuccessful");
							}
							
							
							
							APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
					*/		
							if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName,TestCaseName, 
									description, estimatedTestTime, ExpectedTestCaseCreatedSuccessMessage))
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
								APP_LOGS.debug("Test Case Creation Unsuccessfull");
								comments+="Test Case Creation Unsuccessful(Fail)  ." ;
								//closeBrowser();
								throw new SkipException("Test Case Creation Unsuccessfull");
							}
							
							
							
							
							
							//Clicking On View All Link Of Test Cases
							
							Thread.sleep(1000);
							
							APP_LOGS.debug("Test Cases :Clicking On View All Link");
							
							
							
								
							getObject("TestCases_viewAllTestCasesLink").click();																									
							
							String ActualTestCaseNamePresentInTestCaseGrid=getObject("TestCases_ViewAll_TestCaseNameText").getText();	
							
							
							if (compareStrings(TestCaseName,ActualTestCaseNamePresentInTestCaseGrid))
							{
								APP_LOGS.debug("Test Case Name : "+TestCaseName +"  is created Successfully and showing Properly in Test CaseS Grid");
								
								
								APP_LOGS.debug("CLicking On Edit Test case Button");
								
								
								Thread.sleep(2000);
								
								getObject("TestCases_ViewAll_TestCaseEditButton").click();
								
								Thread.sleep(1000);
								
								if (!editTestCase(EditTestCaseName, 
										editDescription, editEstimatedTesttime, ExpectedTestCaseUpdatedTestCasesSuccessMessage))
								{	
									//fail=true;
									//assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseEditFailure");
									APP_LOGS.debug("Test Case Edit Unsuccessfull");
									comments+="Test Case Edit Unsuccessfull(Fail)  ." ;
									//closeBrowser();
									throw new SkipException("Test Case Edit Unsuccessfull");
								}
								
								comments+="Test Case Edit successfull(Pass). ";
								
								ActualTestCaseNamePresentInTestCaseGrid=getObject("TestCases_ViewAll_TestCaseNameText").getText();
								
								if(compareStrings(EditTestCaseName,ActualTestCaseNamePresentInTestCaseGrid))
								{		
									Thread.sleep(3000);

									getObject("TestCases_ViewAll_TestCaseEditButton").click();
//									eventfiringdriver.findElement(By.xpath("//tbody[@id='showTestCases']/tr/td[4]/span/a[2]")).click();
									
									Thread.sleep(1000);
									
									if (!verifyUpdatedTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName,EditTestCaseName, 
											editDescription, editEstimatedTesttime))
									{	
										fail=true;										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UpdatedTestCaseVerificationFailure");
										APP_LOGS.debug("Updated Test Case Verification Unsuccessfull");
										comments+="Updated Test Case Verification Unsuccessful(Fail)  ." ;
										//closeBrowser();
										
									}
									else
									{
										APP_LOGS.debug("Updated Test Case Verification successfull");
										comments+="Updated Test Case Verification successful(Pass)  ." ;
									}								
									
								
								}
								else 
								{
									fail=true;								
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "EditedTestCaseNotPresent");
									APP_LOGS.debug("Edited Test case name is not present in Test Case Grid");
									comments+="Edited Test case name is not present in Test Case Grid (Fail)   .";	
								}		
							
							}
							else
							{
								fail=true;								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActualCreatedTestCaseNotPresent");
								APP_LOGS.debug("Created Test case name is not present in Test Case Grid");
								comments+="Created Test case name is not present in Test Case Grid (Fail)   .";	

								
							}		
							
							
						}
						else 
						{
							fail= true;
							APP_LOGS.debug("Test Manager Login Not Successful");
							comments+="Test Manager Login Not Successful";
						}	

					}
					else 
					{
						fail= true;
						APP_LOGS.debug("Version Lead Login Not Successful");
						comments+="Version Lead Login Not Successful";
					
					
					}
						
			}	
			catch (Throwable e) 
			{
				e.printStackTrace();
				fail = true;
				assertTrue(false);
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
			
			
			
			public boolean createTestCase_BulkDelete(String group, String portfolio, String project, String version, String testPassName, 
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
						
						getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
						
						getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
						
						String testCaseBulkDeleteResult=wait.until(ExpectedConditions.visibilityOf(getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id"))).getText();
						
					//	Thread.sleep(2000);
						
						if (testCaseBulkDeleteResult.contains("successfully")) 
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
					assertTrue(false);
					return false;
				}
				
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
						
						String testCaseCreatedResult=wait.until(ExpectedConditions.visibilityOf(getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id"))).getText();
				//		Thread.sleep(2000);
						
						if (testCaseCreatedResult.equals(expectedMessage)) 
						{
				//			getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
							
							return true;
						}
						else 
						{
							return false;
						}
						
						
					
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in createTestCase().");
					e.printStackTrace();
					return false;
				}
				
			}
			
			
			private boolean editTestCase(String testCaseName, String description, String estimatedTestTime, String expectedMessage) throws IOException, InterruptedException
			{
				APP_LOGS.debug("Editing Test Case");
				
				try {						
						
						getElement("TestCaseCreateNew_TestCaseNameTextField_Id").clear();
						
						Thread.sleep(1000);
						
						getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName);
						
						getElement("TestCases_CreateNew_TestCaseDescriptionTextField_Id").clear();
						
						getElement("TestCases_CreateNew_TestCaseDescriptionTextField_Id").sendKeys(description);
						
						getElement("TestCases_CreateNew_TestCaseEstTimeInMinTextField_Id").clear();
						
						getElement("TestCases_CreateNew_TestCaseEstTimeInMinTextField_Id").sendKeys(estimatedTestTime);
						
						getElement("TestCases_ViewAll_UpdateButton_Id").click();
						
						String testCaseUpdatedResult=wait.until(ExpectedConditions.visibilityOf(getElement("TestCases_ViewAll_TestCaseUpdatedSuccessMessage_Id"))).getText();
					//	Thread.sleep(2000);
						
						if (testCaseUpdatedResult.equals(expectedMessage)) 
						{
					//		getObject("TestCases_ViewAll_TestCasesUpdatedSuccessOKButton").click();
							
							return true;
						}
						else  
						{
							return false;
						}
						
						
					
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in editTestCase().");
					e.printStackTrace();
					return false;
				}
				
			}
			
			
			private boolean verifyUpdatedTestCase(String group, String portfolio, String project, String version, String testPassName, 
					String testCaseName, String description, String estimatedTestTime) throws IOException, InterruptedException
			{
				String selectedGroup, selectedPortfolio, selectedProject, selectedVersion, selectedTestPass;
				String visibleTestCaseName, visibleEstimatedTestTime, visibleDescription;
				
				try {
					
						selectedGroup= getElement("TestCases_Tiles_Group_Id").getText();
						
						selectedPortfolio= getElement("TestCases_Tiles_Portfolio_Id").getText();
						
						selectedProject= getElement("TestCases_Tiles_Project_Id").getText();
						
						selectedVersion= getElement("TestCases_Tiles_Version_Id").getText();
						
						selectedTestPass= getElement("TestCases_Tiles_TestPass_Id").getText();
						
						visibleTestCaseName= getElement("TestCaseCreateNew_TestCaseNameTextField_Id").getAttribute("value");
						
						visibleDescription=getElement("TestCases_CreateNew_TestCaseDescriptionTextField_Id").getText();
								
						visibleEstimatedTestTime=getElement("TestCases_CreateNew_TestCaseEstTimeInMinTextField_Id").getAttribute("value");						
												
						if (assertTrue(group.equals(selectedGroup) && portfolio.equals(selectedPortfolio) && project.equals(selectedProject)
								&& version.equals(selectedVersion) && testPassName.equals(selectedTestPass) && testCaseName.equals(visibleTestCaseName)
								&& description.equals(visibleDescription) && estimatedTestTime.equals(visibleEstimatedTestTime))) 
						{			
							
							return true;
						}
						else 
						{
							return false;
						}
						
						
					
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in verifyUpdatedTestCase().");
					e.printStackTrace();
					return false;
				}
				
			}

}
