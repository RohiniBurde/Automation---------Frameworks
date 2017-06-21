package com.uat.suite.tm_testStep;

import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyImportTestStepGuidelines extends TestSuiteBase {
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			APP_LOGS.debug(" Executing VerifyImportTestStepGuidelines Test Case");
			System.out.println(" Executing VerifyImportTestStepGuidelines Test Case");				
	
			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
		}
		
		@Test(dataProvider="getTestData")
		public void Test_VerifyImportTestStepGuidelines(String Role, String GroupName, String Portfolio, String ProjectName, String Version, 
				String endMonth,String endYear, String endDate, String VersionLead,String testPassName, String testManager, String testerName,
				String testerRole, String testCaseName, String expectedGuideLineFileDownloadResult) throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

			}
					
			APP_LOGS.debug("Opening Browser... ");
			System.out.println("Opening Browser... ");
			openBrowser();

			isLoginSuccess = login(Role);
			
			if(isLoginSuccess)
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
					System.out.println(" Test Management tab clicked ");
					Thread.sleep(2000);
					
					if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
						comments=ProjectName+" Project not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullProjectCreation");
						closeBrowser();

						throw new SkipException("Project not created successfully");
					}
					APP_LOGS.debug(ProjectName+" Project Created Successfully.");
					comments=ProjectName+" Project Created Successfully(Pass). ";
					
					closeBrowser();
					APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");
					System.out.println(" Closed Browser after verifying if project was existing and creating project if not existing ");
						
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					openBrowser();
						
					login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead");
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
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPassCreation");
						closeBrowser();

						throw new SkipException("Test Pass not created successfully");
					}
					APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
					comments+=testPassName+" Test Pass Created Successfully(Pass). ";

					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))							
					{	
						fail=true;
						APP_LOGS.debug("Tester not Created Successfully.");
						comments+="Tester not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTesterCreation");
						closeBrowser();

						throw new SkipException("Tester not created successfully");
					}
					APP_LOGS.debug(" Tester Created Successfully.");
					comments+=" Tester Created Successfully(Pass). ";

					if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
					{
						fail=true;
						APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
						comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCaseCreation");
						closeBrowser();

						throw new SkipException("Test Case not created successfully");
					}
					APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
					comments+=testCaseName+" Test Case Created Successfully(Pass). ";
			
					closeBrowser();
			 	   	APP_LOGS.debug(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");
					System.out.println(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");

					try{
						
						APP_LOGS.debug("Opening Browser... ");
		 				System.out.println("Opening Browser... ");
		 				openBrowser();
		 				
		 				login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager");
		 				System.out.println("Logged in browser with Test Manager");
		 	            APP_LOGS.debug("Logged in browser with Test Manager");
		 	            
		 				//click on testManagement tab
		 				APP_LOGS.debug(" Clicking on Test Management Tab");
		 				getElement("UAT_testManagement_Id").click();
		 				Thread.sleep(3000);
		 				
		 				//clicking on TestStep tab
						getElement("TM_testStepsTab_Id").click();
						APP_LOGS.debug(" TestStep tab clicked ");
						System.out.println(" TestStep tab clicked ");
						Thread.sleep(2000);
						
						dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), GroupName );
						
						dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), Portfolio );
						
						dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), ProjectName );
						
						dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), Version );
						
						dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
						
						
						getObject("TestStep_ImportExportTestSteps_Link").click();     //Click on ImportExportTestSteps Link
						
						Thread.sleep(3000);
						if(assertTrue(getObject("TestStep_ImportTestStepGuideline_link").isDisplayed()))
						{
							getObject("TestStep_ImportTestStepGuideline_link").click();
							String actualGuideLineFileDownloadResult=getObject("TestStep_ImportTestStepGuideline_link").getAttribute("href");
							
							boolean successResult=compareStrings(actualGuideLineFileDownloadResult,resourceFileConversion.getProperty("TestStep_expectedGuideLineFileDownloadResult"));

							if(successResult==true)
							{
								comments+= " Test Step guidelines file is downloaded successfully(Pass).";
								APP_LOGS.debug(" Test Step guidelines is downloaded successfully ");
								System.out.println(" Test Step guidelines is downloaded successfully ");
							}
							else
							{
								fail=true;
								comments+= " Test Step guidelines is not downloaded successfully(Fail).";
								APP_LOGS.debug(" Test Step guidelines is not downloaded successfully ");
								System.out.println(" Test Step guidelines is not downloaded successfully ");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulImportTestStepGuidelines");
							}
						}
						
						else{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "GuidelinesfileNotPresent");
						}
					}
					
					catch(Throwable t)
		            {
		         	 fail=true;
		           	 t.printStackTrace();
		           	 comments="Failed Login for Role" +Role;
		           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
		            }
					APP_LOGS.debug("Closing Browser... ");
				    closeBrowser();	
								
			}
			
			else 
			{
				isLoginSuccess=false;
				APP_LOGS.debug("Login Not Successful");
				System.out.println("Login Not Successful");
			}	
		}
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
			{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else{
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;	

		}
		
		@AfterTest
		public void reportTestResult(){
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
		
		}
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
		}
		
}
