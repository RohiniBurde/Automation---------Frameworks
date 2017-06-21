package com.uat.suite.testing_page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
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

public class Testing_VerifyContentsOfPage extends TestSuiteBase {


	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	int counter=0;
	String oldViewAttachment1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> version_Lead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	String testCaseCreatedSuccessMessage;
	String comments;
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


	// Runmode of test case in a suite

	@BeforeTest
	public void checkTestSkip(){

		if(!TestUtil.isTestCaseRunnable( testingPageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(testingPageSuiteXls, this.getClass().getSimpleName());

		version_Lead=new ArrayList<Credentials>();
		test_Manager=new ArrayList<Credentials>();
		tester=new ArrayList<Credentials>();


	}



	// Test Case Implementation ...

	@Test(dataProvider="getTestData")
	public void testing_VerifyContentsOfPage  (String role,String groupName,String portfolioName,String projectName, 
			String version,String startMonth, String startYear, String startDate,String endMonth, String endYear, String endDate, String versionLead ,String testPassName1, 
			String tp1TestCase1,String tp1TC1TestStep1,String tp1TC1expectedResultsTestStep_1,String tp1TC1TestStep2,
			String tp1TC1expectedResultsTestStep_2,String tp1TestCase2,String tp1TC2TestStep1,String tp1TC2expectedResultsTestStep_1,
			String tp1TC2TestStep2,String tp1TC2expectedResultsTestStep_2,String testPassName2, String tp2TestCase1,
			String tp2TC1TestStep1,String tp2TC1expectedResultsTestStep_1,String tp2TC1TestStep2,String tp2TC1expectedResultsTestStep_2,
			String tp2TestCase2,String tp2TC2TestStep1,String tp2TC2expectedResultsTestStep_1,String tp2TC2TestStep2,
			String tp2TC2expectedResultsTestStep_2,String testPassName3, String tp3TestCase1,String tp3TC1TestStep1,
			String tp3TC1expectedResultsTestStep_1,String tp3TC1TestStep2,String tp3TC1expectedResultsTestStep_2,String tp3TestCase2,
			String tp3TC2TestStep1,String tp3TC2expectedResultsTestStep_1,String tp3TC2TestStep2,String tp3TC2expectedResultsTestStep_2,
			String estimatedTime ,String testManager,String testers,String addRole,String area,String environmentURL,String environmentAliasName
			,String testStepResult,String TestPassEndDate,String feedback,String totalNumOfTestStepsForTP2,String expectedstartDateYetToComeForTestPassPopUpMessage) throws Exception
	{
		count++;

		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}


		comments="";


		int versionLead_count = Integer.parseInt(versionLead);
		version_Lead = getUsers("Version Lead", versionLead_count);

		int testManager_count = Integer.parseInt(testManager);
		test_Manager = getUsers("Test Manager", testManager_count);

		int testers_count = Integer.parseInt(testers);			
		tester = getUsers("Tester", testers_count);


		APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
		APP_LOGS.debug("Opening Browser... ");

		openBrowser();

		isLoginSuccess = login(role);

		if(isLoginSuccess)
		{
			try
			{

				//click on testManagement tab
				APP_LOGS.debug("Clicking On Test Management Tab ");
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(1000);


				APP_LOGS.debug(" User "+ role +" creating PROJECT with Version Lead "+version_Lead.get(0).username );
				if (!createProject(groupName, portfolioName, projectName, version, endMonth, endYear, endDate, version_Lead.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
					comments="Project Creation Unsuccessful(Fail) by "+role+"  . ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+" . ");
					throw new SkipException("Project Creation Unsuccessfull");
				}

				APP_LOGS.debug("......................................................******............................................");

				//Creating First Test Pass  With Their Test Cases and Respective Test Steps

				APP_LOGS.debug("Creating Test Pass :" + testPassName1+ " with Test Cases : " + tp1TestCase1 + " And " + tp1TestCase2 + " with their respective Test Steps  ." );

				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass Creation Unsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}

				if (!createTester(groupName, portfolioName, projectName, version, testPassName1, tester.get(0).username,addRole,addRole,area)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");
				}


				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName1,tp1TestCase1,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}


				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName1,tp1TestCase2,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}	

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName1,tp1TC1TestStep1,tp1TC1expectedResultsTestStep_1,tp1TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName1,tp1TC1TestStep2,tp1TC1expectedResultsTestStep_2,tp1TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName1,tp1TC2TestStep1,tp1TC2expectedResultsTestStep_1,tp1TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName1,tp1TC2TestStep2,tp1TC2expectedResultsTestStep_2,tp1TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test StepCreation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				APP_LOGS.debug("Test Pass Name :" +testPassName1 + " with " + tp1TestCase1 +" and "+ tp1TestCase2 + "having 2 Test Steps each Created Successfully   ." );
				comments+="Test Pass Name :" +testPassName1 + " with " + tp1TestCase1 + " and "+ tp1TestCase2 + "having 2 Test Steps each Created Successfully (Pass)  ." ;

				APP_LOGS.debug("......................................................******............................................");



				//Creating Second Test Pass  With Their Test Cases and Respective Test Steps


				APP_LOGS.debug("Creating Test Pass :" + testPassName2+ " with Test Cases : " + tp2TestCase1 + " And " + tp2TestCase2 + " with their respective Test Steps  ." );

				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}


				if (!createTester(groupName, portfolioName, projectName, version, testPassName2, tester.get(0).username,addRole,addRole,area)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}


				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName2,tp2TestCase1,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}								

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName2,tp2TestCase2,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}	

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC1TestStep1,tp2TC1expectedResultsTestStep_1,tp2TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC1TestStep2,tp2TC1expectedResultsTestStep_2,tp2TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC2TestStep1,tp2TC2expectedResultsTestStep_1,tp2TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC2TestStep2,tp2TC2expectedResultsTestStep_2,tp2TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				APP_LOGS.debug("Test Pass Name :" +testPassName2 + " with " + tp2TestCase1 +" and "+ tp2TestCase2 + "having 2 Test Steps each Created Successfully   ." );
				comments+="Test Pass Name :" +testPassName2 + " with " + tp2TestCase1 + " and "+ tp2TestCase2 + "having 2 Test Steps each Created Successfully (Pass)  ." ;

				APP_LOGS.debug("......................................................******............................................");



				//Creating Third Test Pass  With Their Test Cases and Respective Test Steps


				APP_LOGS.debug("Creating Test Pass :" + testPassName3+ " with Test Cases : " + tp3TestCase1 + " And " + tp3TestCase2 + " with their respective Test Steps  ." );
				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName3,startMonth,startYear,startDate, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass Creation Unsuccessfull");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}



				if (!createTester(groupName, portfolioName, projectName, version, testPassName3, tester.get(0).username,addRole,addRole,area)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");


				}

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName3,tp3TestCase1,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}								

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName3,tp3TestCase2,estimatedTime))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}	

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC1TestStep1,tp3TC1expectedResultsTestStep_1,tp3TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC1TestStep2,tp3TC1expectedResultsTestStep_2,tp3TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC2TestStep1,tp3TC2expectedResultsTestStep_1,tp3TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC2TestStep2,tp3TC2expectedResultsTestStep_2,tp3TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("Test Step Creation Unsuccessfull");
				}

				APP_LOGS.debug("Test Pass Name :" +testPassName3 + " with " + tp3TestCase1 +" and "+ tp3TestCase2 + "having 2 Test Steps each Created Successfully   ." );
				comments+="Test Pass Name :" +testPassName3 + " with " + tp3TestCase1 + " and "+ tp3TestCase2 + "having 2 Test Steps each Created Successfully (Pass)  ." ;

				APP_LOGS.debug("......................................................******............................................");



				//  Creation Of New UAT Environment and Assigning to Tester

				try
				{
					APP_LOGS.debug("Creating New  UAT Environment For Testing and Assigning that Environment to The Tester ");	

					Thread.sleep(1000);

					getElement("UATConfigurationTab_Id").click();

					Thread.sleep(1000);

					getElement("ConfigUserSettingTab_Id").click();

					APP_LOGS.debug("Selecting  Project from User Setting Dropdown ");
					Thread.sleep(1000);

					Select projectSelection=new Select(getElement("ConfigUserSettingSelectProjectDropdown_Id"));

					projectSelection.selectByVisibleText(projectName);

					Thread.sleep(500);

					APP_LOGS.debug("Selecting Version from User Setting Dropdown");

					Select versionSelection=new Select(getElement("ConfigUserSettingSelectProjectVersionNameDropdown_Id"));

					versionSelection.selectByVisibleText(version);

					Thread.sleep(500);

					APP_LOGS.debug("Selecting Test Pass from User Setting DropDown");


					List<WebElement> selectTP = getElement("ConfigUserSettingSelecttestPassNameDropdown_Id").findElements(By.tagName("option"));
					for(int i=1 ;i<=selectTP.size();i++)
					{
						//String selectTestPassFromDropdown=eventfiringdriver.findElement(By.xpath("//select[@id='testPassNames']/option["+i+"]")).getAttribute("title").trim();
						String selectTestPassFromDropdown=getObject("TestingPageMyActivitySelectTestPassFromDropdownXpath1","TestingPageMyActivitySelectTestPassFromDropdownXpath2",i).getAttribute("title").trim();
						if(selectTestPassFromDropdown.equals(testPassName1))
						{
							getObject("TestingPageMyActivitySelectTestPassFromDropdownXpath1","TestingPageMyActivitySelectTestPassFromDropdownXpath2",i).click();
							Thread.sleep(300);
							counter++;

							break;
						}
					}

					if(counter==0)
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestpassNotPresentInDropdownOfTestingPageFailure");
						APP_LOGS.debug("Test pass is not present in Dropdown Of Testing Page (Fail)   .");
						comments=comments+"Test pass is not present in Dropdown Of Testing Page (Fail)  .";
					}
					else
					{

						APP_LOGS.debug( testPassName1 +" : test pass is selected...");

					}
					counter=0;
					getElement("ConfigUserSettingAddUATEnvironment_Id").click();
					getElement("ConfigUserSettingURL_Id").sendKeys(environmentURL);
					getElement("ConfigUserSettingAliasURL_Id").sendKeys(environmentAliasName);
					getObject("ConfigUserSettingAddUATEnvironmentAddButton").click();


					getObject("ConfigUserSettingTesterSelectionCheckBox").click();
					getObject("ConfigUserSettingEnvironmentSelectionCheckBox").click();
					getElement("ConfigUserSettingUATEnvironmentSaveButton_Id").click();
					getObject("ConfigUserSettingEnvironmentYesButton").click();
					getObject("ConfigUserSettingEnvironmentOKButton").click();

					//  Verifying the created Environment Present In User Setting Grid .... 

					String envNameinUserSettingGrid=getObject("ConfigUserSettingEnvironmentName").getText();
					if (compareStrings(environmentAliasName, envNameinUserSettingGrid)) {

						comments=comments+"Creation Of New UAT Environment and assigning Tester to the Environment : "+ environmentAliasName +" done successfully (Pass)  .";
						APP_LOGS.debug("Creation Of New UAT Environment and assigning Tester to the Environment : "+ environmentAliasName +" done successfully (Pass)  .");


					} 
					else 
					{	
						fail=true;
						comments=comments+"Creation Of New UAT Environment and assigning Tester to the Environment : "+ environmentAliasName +" Failed  (Fail)  .";
						APP_LOGS.debug("Creation Of New UAT Environment and assigning Tester to the Environment : "+ environmentAliasName +" Failed  (Fail)  .");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "EnvironmentNotPresentFailure");
					}

					APP_LOGS.debug("......................................................******............................................");

				}

				catch(Throwable t)
				{
					fail=true;
					assertTrue(false);
					t.printStackTrace();
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UATEnvironmentCreationFailure");
					comments=comments+"Creation Of New UAT Environment and assigning Tester failure  (Fail)";
					APP_LOGS.debug("Creation Of New UAT Environment and assigning Tester failure  (Fail)  .");

				}


				closeBrowser();


				// Log in with Tester for verification of Contents Of Testing Page

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " for Testing Page Verification");

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{

					if(!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,testStepResult,"Begin Testing"))
					{
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass1");
						comments+="Begin Testing Click Failure For "+testPassName1 +" ( Fail )";
						APP_LOGS.debug("Begin Testing Click Failure For "+testPassName1 +" (Fail ) ");
						throw new SkipException("Begin Testing Click Failure For "+testPassName1);	
					}

					try
					{
						// Verification of Project Name ,Test Pass Name,Test Step Name,Test Case Name, Test Pass Name, Due Date,Tester Role ,Estimated Time,

						APP_LOGS.debug("Verifying Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time on Testing Page ... ");

						Thread.sleep(1000);

						String actualProjectNamePresentOnTestingPage=getObject("TestingPageMyActivityProjectName").getAttribute("title");

						String actualTestPassDueDate=getElement("TestingPageMyActivityTestPassEndDate_Id").getText();

						String actualTesterRole=getElement("TestingPageMyActivityTesterRole_Id").getText();

						String actualTestPassNamePresentOnTestingPage=getObject("TestingPageMyActivityTesPassName").getAttribute("title");

						String actualTestManagerPresent=getElement("TestingPageMyActivityTestManagerUserName_Id").getText();

						String actualTestCaseNamePresent=getElement("TestingPageMyActivityTestCaseName_id").getText();

						String actualEstimatedTime=getElement("TestingPageMyActivityEstimatedTime_Id").getText();

						String actualTestStepName =getObject("TestingPageMyActivityTestStepName").getText();


						if(actualProjectNamePresentOnTestingPage.equals(projectName) && actualTesterRole.equals(addRole) && actualTestPassDueDate.equals(TestPassEndDate)
								&& actualTestPassNamePresentOnTestingPage.equals(testPassName1) && actualTestCaseNamePresent.equals(tp1TestCase1) && actualEstimatedTime.equals(estimatedTime) && actualTestStepName.equals(tp1TC1TestStep1)
								&& actualTestManagerPresent.equalsIgnoreCase(test_Manager.get(0).username.replace(".", " ")))
						{
							comments=comments+" Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time are showing Properly On Testing Page ... (Pass)   .";
							APP_LOGS.debug( "Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time are showing Properly On Testing Page ...  (Pass)    .");
						}		
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug(" Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time are not showing Properly On Testing Page ... (Fail)   .");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DisplayTextIssueOnTestingPage");
							comments=comments+" Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time are not showing Properly On Testing Page ... (Fail)   .";
						}		


						APP_LOGS.debug("......................................................******............................................");
					}
					catch(Throwable t)
					{
						fail=true;
						assertTrue(false);
						t.printStackTrace();
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjNameNTPnTSTMnTRoleVerificationFailure");
						comments=comments+"Verification Failure Of  Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time on Testing Page  (Fail)  .";
						APP_LOGS.debug("Verification Failure of Project Name ,Test Pass Name,Test Step Name,Test Case Name,TestManager UserName, Test Pass Due Date,Tester Role ,Estimated Time on Testing Page  (Fail)  .");
					}




					// Dropdown Verification For Test Pass Selection  and Environment Selection from Of Testing Page   

					try
					{
						APP_LOGS.debug("Verification Of Test Passes showing properly Or Not on Testing Page ???");


						getObject("TestingPageMyActivity_TestPassDropDown").click();
						List<WebElement> listOfTP = getObject("TestingPageMyActivity_TestPassDropdownList").findElements(By.tagName("a"));
						for(int i=1; i<=listOfTP.size();i++)
						{

							String actualTPName=getObject("TestingPageMyActivityTestPassSelectionfromDropdownXpath1","TestingPageMyActivityTestPassSelectionfromDropdownXpath2",i).getAttribute("title");

							if(actualTPName.equals(testPassName1))
							{
								APP_LOGS.debug("Test Pass :"+ testPassName1+" is showing Properly In TestPass Dropdown ");
								counter++;
							}
							else if (actualTPName.equals(testPassName2))
							{
								APP_LOGS.debug("Test Pass :"+ testPassName2+ " is showing Properly In TestPass Dropdown ");
								counter++;
							}
							else if (actualTPName.equals(testPassName3))
							{
								APP_LOGS.debug("Test Pass :"+ testPassName3+ " is showing Properly In TestPass Dropdown ");
								counter++;

								Thread.sleep(1500);
								getObject("TestingPageMyActivity_TestPassDropDown").click();

							}

							else
							{	
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingPage_TestPassesNotPresentInTestPassDropDown");
								APP_LOGS.debug("Test Passes for Project : "+ projectName +" are not present in Test Pass Dropdown(Fail) ");
								comments=comments+"Test Passes for Project : "+ projectName +" are not present in Test Pass Dropdown(Fail) ";
							}

						}
						if(counter==3)
						{
							comments=comments+" All Test Passes for Project : "+ projectName +" are showing properly in Test Pass Dropdown Of Testing Page (Pass)   .";

						}
						else
						{	
							fail=true;
							assertTrue(false);
							comments=comments+" All Test Passes for Project : "+ projectName +" are not showing correctly in Test Pass Dropdown Of Testing Page (Fail)   .";
							APP_LOGS.debug("All Test Passes for Project : "+ projectName +" are not showing correctly in Test Pass Dropdown Of Testing Page (Fail)  .");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingPage_TestPassesNotPresentInTestPassDropDown");
						}
						//Reinitializing counter to 0 as default
						counter=0;
						Thread.sleep(1000);




						//APP_LOGS.debug("Verification Of Environment  showing properly Or Not on Testing Page");

						APP_LOGS.debug("Verification Of Environment showing properly Or Not on Testing Page ???");
						getObject("TestingPageMyActivity_EnvironmentDropdown").click();


						String actualAliasEnvName=getObject("TestingPageMyActivity_EnvironmentALiasName").getText();
						String actualEnvUrl=getObject("TestingPageMyActivity_EnvironmentEnvUrl").getAttribute("title");

						if(compareStrings(environmentAliasName, actualAliasEnvName)&&compareStrings(environmentURL, actualEnvUrl))
						{
							APP_LOGS.debug("Assigned Environment aliasName : "+ actualAliasEnvName +" with proper Environment Url : "+ actualEnvUrl + " is displaying...(Pass)");
							comments=comments+"Assigned Environment aliasName : "+ actualAliasEnvName +" with proper Environment Url : "+ actualEnvUrl + " is displaying...";
							getObject("TestingPageMyActivity_EnvironmentDropdown").click();
						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingPage_EnvironmentNotPresentInEnvironmentDropDown");
							APP_LOGS.debug("Assigned Environment aliasName : "+ actualAliasEnvName +" with proper Environment Url : "+ actualEnvUrl + " is not displaying... (Fail)");
							comments=comments+"Assigned Environment aliasName : "+ actualAliasEnvName +" with proper Environment Url: "+ actualEnvUrl + " is not  displaying...(Fail)  .";

						}


						APP_LOGS.debug("......................................................******............................................");	

					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingPage_TestPassesandEnvironmentValidationTestingPageFailure");
						APP_LOGS.debug("Test Passes for Project : "+ projectName +" TestPassesAndEnvironmentValidationFailure (fail) ");
						comments=comments+"Test Passes for Project : "+ projectName +" TestPassesAndEnvironmentValidationFailure (Fail) ";
					}




					//Performing Testing to verify Status Begin Testing Or Continue Testing Or Testing Complete


					try
					{

						APP_LOGS.debug("Performing Testing to verify Status Begin Testing Or Continue Testing Or Testing Complete");

						getElement("UAT_dashboard_Id").click();

						APP_LOGS.debug(" Navigating on Dashboard Page to verify  Action Status Of Test Pass " +testPassName1+ " should be Begin Testing ");

						if(!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,testStepResult,"Begin Testing"))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass1");
							comments+="Begin Testing Click Failure For "+testPassName1 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName1 +" (Fail ) ");
							throw new SkipException("Begin Testing Click Failure For "+testPassName1);	
						}
						APP_LOGS.debug("Action Status : ' Begin Testing ' For Test Pass:  " + testPassName1+" showing properly (Pass)");

						comments=comments+"Action Status : ' Begin Testing ' For Test Pass:  " + testPassName1+" showing properly ( Pass )  .";

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP1-TC1-Test StepName_1 is created Successfully");

						getElement("TestingPage_passRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP1-TC1-Test StepName_2 is failed ");

						getElement("TestingPage_failRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						getElement("UAT_dashboard_Id").click();

						APP_LOGS.debug(" Navigating on Dashboard Page to verify  Action Status Of Test Pass " +testPassName1+ " should be Continue Testing ");

						if(!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,testStepResult,"Continue Testing"))
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ContinueTestingClickFailureForTestPass1");
							comments="Continue Testing Click Failure For "+testPassName1 +" ( Fail )";
							APP_LOGS.debug("Continue Testing Click Failure For "+testPassName1 +" (Fail ) ");
							throw new SkipException("Continue Testing Click Failure For "+testPassName1);	
						}
						APP_LOGS.debug("Action Status : ' Continue Testing ' For Test Pass:  " + testPassName1+" showing properly (Pass)");
						comments=comments+"Action Status : ' Continue Testing ' For Test Pass:  " + testPassName1+" showing properly  (Pass)  .";



						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP1-TC2-Test StepName_1 is created Successfully (Pass)  .");

						getElement("TestingPage_passRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP1-TC2-Test StepName_2 is created Successfully");

						getElement("TestingPage_passRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						getObject("TestingPage_rating_verySatisfiedRadioBtn").click();

						//provide feedback
						String feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+feedback+"')";
						eventfiringdriver.executeScript(feedbackDetails);

						//save
						getObject("TestingPage_feedbackRating_SaveButton").click();

						Thread.sleep(500);	

						//Clicking on 'Return To Home' button 
						getObject("TestingPage_returnToHomeButton").click();
						APP_LOGS.debug("Clicking on 'Return to Home' button ");

						if(!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,testStepResult,"Testing Complete"))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingCompleteClickFailureForTestPass1");
							comments=" Testing Complete Click Failure For "+testPassName1 +" ( Fail )";
							APP_LOGS.debug("Testing Complete Click Failure For "+testPassName1 +" (Fail ) ");
							throw new SkipException("Testing Complete Click Failure For "+testPassName1);	

						}


						APP_LOGS.debug("Action Status : 'Testing Complete ' For Test Pass: " + testPassName1+" showing properly (Pass)");
						comments=comments+"Action Status : 'Testing Complete ' For Test Pass: " + testPassName1+" showing properly (Pass)";

						APP_LOGS.debug("......................................................******............................................");

					}
					catch(Throwable t)
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Failure in Test Cases Execution On Testing Page");
						comments=comments+"Failure in Test Cases Execution On Testing Page   (Fail).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseExecutionOnTestingPageIssue");
					}


					closeBrowser();	
				}	
				else 
				{
					fail= true;
					APP_LOGS.debug("Tester Login Not Successful");

				}




				// Log in with Tester for verification of Contents Of Testing Page

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " for Testing Page Verification");

				openBrowser();



				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{

					//verification of Test Pass whose 'Start Date' is yet come, After clicking on User should get a validation message


					try
					{	
						APP_LOGS.debug("verification of Test Pass whose 'Start Date' is yet come, After clicking on User should get a validation message ");

						if(!searchTestPassAndPerformTesting(projectName, version,testPassName3,tp3TC1TestStep1,tp3TC1TestStep2,addRole,testStepResult,"Begin Testing"))
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass1");
							comments="Begin Testing Click Failure For "+testPassName3 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName3 +" (Fail ) ");
							throw new SkipException("Begin Testing Click Failure For "+testPassName3);	
						}

						if(getElement("TestingPageMyActivity_TestPassStartDateYetToComePopUp_Id").isDisplayed())
						{
							String testPassStartDateYetToComeMessage=getElement("TestingPageMyActivity_TestPassStartDateYetToComePopUp_Id").getText();

							if(compareStrings(expectedstartDateYetToComeForTestPassPopUpMessage, testPassStartDateYetToComeMessage))
							{

								APP_LOGS.debug(" Start Date For Test Pass Yet to Come functionality worked ...so PopUp Message is displayed Stating :"+testPassStartDateYetToComeMessage + " so redirecting to Dashboard Page (Pass) ");
								comments=comments+" Start Date For Test Pass Yet to Come functionality worked ...so PopUp Message is displayed Stating : "+testPassStartDateYetToComeMessage + " so redirecting to Dashboard Page  (Pass)  .";	
								getObject("TestingPageMyActivity_TestPassStartDateYetToComePopUp").click();
							}
							else
							{
								fail=true;
								APP_LOGS.debug("PopUp text : "+ testPassStartDateYetToComeMessage + " is not correct as expected (Fail)");
								comments=comments+"PopUp text : "+ testPassStartDateYetToComeMessage + " is not correct as expected (Fail)  .";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PopUpForTPStartDateYetNotComePopUpTextFailure");
							}


						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("PopUp Message is not  displayed For Test Pass whose Start Date has Yet to come (Fail)");
							comments=comments+"PopUp Message is not  displayed For Test Pass whose Start Date has Yet to come ( Fail)  .";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PopUpForTPStartDateYetNotCome");

						}

						APP_LOGS.debug("......................................................******............................................");
					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Validation Message Issue for For Test Pass whose Start Date has Yet to come (Fail)");
						comments=comments+"Validation Message Issue for For Test Pass whose Start Date has Yet to come (Fail)  .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PopUpForTPStartDateYetNotComeIssue");
					}

					closeBrowser();
				}	
				else 
				{

					APP_LOGS.debug("Tester Login Not Successful");

				}



				// Log in with Tester for verification of Contents Of Testing Page

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " for Testing Page Verification");

				openBrowser();



				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{
					//Verification Of Statistics of Test Steps as we perform Execution Of Test Steps and Changes should be reflected on Grid

					APP_LOGS.debug("Verification Of Statistics Of Test Step on testing Page As we perform execution of Test Steps and  Test Steps Execution should get reflected on Statistics Grid");

					if(!searchTestPassAndPerformTesting(projectName, version,testPassName2,tp2TC1TestStep1,tp2TC1TestStep2,addRole,testStepResult,"Begin Testing"))
					{
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass1");
						comments+="Begin Testing Click Failure For "+testPassName2 +" ( Fail )";
						APP_LOGS.debug("Begin Testing Click Failure For "+testPassName2 +" (Fail ) ");
						closeBrowser();
						throw new SkipException("Begin Testing Click Failure For "+testPassName2);	
					}


					try		

					{

						APP_LOGS.debug("Performing Testing to verify Test Steps Statistics");

						APP_LOGS.debug("Initial Count Of Test Steps Before Performing Test Steps Execution are as Follows ");

						int countForPassedTestSteps=0;
						int countForFailedTestSteps=0;
						int countForNotCompletedTestSteps=4;
						int countForPendingTestSteps=0;
						int ExpectedtotalNumOfTestStepsForTP2=Integer.parseInt(totalNumOfTestStepsForTP2);

						APP_LOGS.debug("countForPassedTestSteps : "+ countForPassedTestSteps + "  countForFailedTestSteps :" + countForFailedTestSteps  +"  countForNotCompletedTestSteps  :" +countForNotCompletedTestSteps + " countForPendingTestSteps  :"+ countForPendingTestSteps + "  totalCountOfTestSteps :  " + ExpectedtotalNumOfTestStepsForTP2);

						//APP_LOGS.debug("Initial Count For Passed Test Steps :"+countForPassedTestSteps + "    Initial Count For Failed Test Steps :"+countForFailedTestSteps +"  Initial Count For Not Completed Test Steps :" + countForNotCompletedTestSteps);
						APP_LOGS.debug("Verifying Passed and Not Completed Count in Test Step Statistics  .");


						APP_LOGS.debug("Executing Test Step 1  : "+ tp2TC1TestStep1 + " Of Test Pass : " + testPassName2 +"  and making it Passed");
						Thread.sleep(500);

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP2-TC1-Test StepName_1 is created Successfully");

						getElement("TestingPage_passRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						APP_LOGS.debug("Execution Of  Test Step 1  :"+ tp2TC1TestStep1 + " Of Test Pass : " + testPassName2 +"  is done successfully");

						countForPassedTestSteps++;
						countForNotCompletedTestSteps--;
						String countForTestStepsPassed=getObject("TestingPageMyActivity_TestStepsPassedCount").getText();
						int actualCountForTestStepsPassed = Integer.parseInt(countForTestStepsPassed);
						String countForTestStepsNotCompleted=getObject("TestingPageMyActivity_TestStepsNotCompletedCount").getText();
						int actualCountForTestStepsNotCompleted = Integer.parseInt(countForTestStepsNotCompleted);

						if(compareIntegers(countForPassedTestSteps, actualCountForTestStepsPassed)&&compareIntegers(countForNotCompletedTestSteps, actualCountForTestStepsNotCompleted))
						{
							APP_LOGS.debug("After Execution of Test Step1 : "+ tp2TC1TestStep1 + ".     Passed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsPassed +" as expected    .");
							APP_LOGS.debug("  Not Completed Count in Test Steps Statistics Grid showing : "+  actualCountForTestStepsNotCompleted +" as expected    .");
							comments=comments+"After Execution of Test Step1 : "+ tp2TC1TestStep1 + ".     Passed Count in Test Steps Statistics Grid showing i.e. : "+actualCountForTestStepsPassed +" and  Not Completed Count in Test Steps Statistics Grid showing  i.e. : "+  actualCountForTestStepsNotCompleted +" as expected  (Pass)   .";

						}
						else
						{
							fail=true;
							comments=comments+" Passed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsPassed +" Wrong  .  Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" Wrong ( Failed ) ";
							APP_LOGS.debug(" Passed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsPassed +" Wrong  .  Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" Wrong ( Failed ) ");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsPassed&NotCompletedStatiticsFAilure");
						}

						APP_LOGS.debug("Verifying Failed and Not Completed Count in Test Step Statistics  .");

						APP_LOGS.debug("Executing Test Step 2  : "+ tp2TC1TestStep2 + " Of Test Pass : " + testPassName2 +" and making it Failed");

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP2-TC1-Test StepName_2 is created Successfully");

						getElement("TestingPage_failRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						APP_LOGS.debug("Execution Of  Test Step 2  :"+ tp2TC1TestStep2 + " Of Test Pass : " + testPassName2 +"  is done successfully");

						countForFailedTestSteps++;
						countForNotCompletedTestSteps--;

						String countForTestStepsFailed=getObject("TestingPageMyActivity_TestStepsFailedCount").getText();
						int actualCountForTestStepsFailed = Integer.parseInt(countForTestStepsFailed);

						countForTestStepsNotCompleted=getObject("TestingPageMyActivity_TestStepsNotCompletedCount").getText();
						actualCountForTestStepsNotCompleted = Integer.parseInt(countForTestStepsNotCompleted);
						if(compareIntegers(countForFailedTestSteps, actualCountForTestStepsFailed)&&compareIntegers(countForNotCompletedTestSteps, actualCountForTestStepsNotCompleted))
						{
							APP_LOGS.debug("After Execution of Test Step2 :  "+ tp2TC1TestStep2 + ".   Failed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsFailed +" as expected    .");
							APP_LOGS.debug("Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" as expected    .");
							comments=comments+"After Execution of Test Step2 :  "+ tp2TC1TestStep2 + ". Failed Count in Test Steps Statistics Grid showing i.e. = : "+actualCountForTestStepsFailed +"  Not Completed Count in Test Steps Statistics Grid showing  i.e. = : "+actualCountForTestStepsNotCompleted +"  as expected   (Pass)";

						}
						else
						{
							fail=true;
							comments=comments+" Failed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsFailed +" Wrong  . and Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" Wrong ( Failed ) ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsFailed&NotCompletedStatiticsFAilure");
							APP_LOGS.debug(" Failed Count in Test Steps Statistics Grid showing i.e = "+actualCountForTestStepsFailed +" Wrong  . and  Not Completed Count in Test Steps Statistics Grid showing i.e. =: "+actualCountForTestStepsNotCompleted +"  wrong ( Failed ) ");
						}


						APP_LOGS.debug("Verifying Pending and Not Completed Count in Test Step Statistics  .");

						APP_LOGS.debug("Executing Test Step 1  : "+ tp2TC2TestStep1 + " Of Test Pass : " + testPassName2 +" and making it Pending");

						getElement("TestingPage_pendingRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						APP_LOGS.debug("Execution Of  Test Step 1  :"+ tp2TC2TestStep1 + " Of Test Pass : " + testPassName2 +"  done and made it as Pending");

						countForPendingTestSteps++;
						countForNotCompletedTestSteps--;

						String countForTestStepsPending=getObject("TestingPageMyActivity_TestStepsPendingCount").getText();
						int actualCountForPendingTestSteps = Integer.parseInt(countForTestStepsPending);

						countForTestStepsNotCompleted=getObject("TestingPageMyActivity_TestStepsNotCompletedCount").getText();
						actualCountForTestStepsNotCompleted = Integer.parseInt(countForTestStepsNotCompleted);

						if(compareIntegers(countForPendingTestSteps, actualCountForPendingTestSteps)&&compareIntegers(countForNotCompletedTestSteps, actualCountForTestStepsNotCompleted))
						{
							APP_LOGS.debug("After Execution of Test Step1 :  "+ tp2TC2TestStep1 + ".  Pending Count in Test Steps Statistics Grid showing : "+actualCountForPendingTestSteps +" as expected    .");
							APP_LOGS.debug("Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" as expected    .");
							comments=comments+"After Execution of Test Step1 :  "+ tp2TC2TestStep1 + ".  Pending Count in Test Steps Statistics Grid showing i.e. : "+actualCountForPendingTestSteps +" and Not Completed Count in Test Steps Statistics Grid showing i.e.: "+actualCountForTestStepsNotCompleted +" as expected  (Pass)   .";


						}
						else
						{
							fail=true;
							comments=comments+" Pending  Count in Test Steps Statistics Grid showing : "+actualCountForPendingTestSteps +" Wrong  .Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" Wrong ( Failed ) ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsPending&NotCompletedStatiticsFAilure");
							APP_LOGS.debug(" Pending  Count in Test Steps Statistics Grid showing : "+actualCountForPendingTestSteps +" Wrong  . Not Completed Count in Test Steps Statistics Grid showing : "+actualCountForTestStepsNotCompleted +" Wrong ( Failed ) .");
						}


						String countOfTotalOfTestSteps =getObject("TestingPageMyActivity_TestStepsTotalCount").getText();
						int actualCountForTotalTestSteps = Integer.parseInt(countOfTotalOfTestSteps);


						if(compareIntegers(ExpectedtotalNumOfTestStepsForTP2, actualCountForTotalTestSteps))
						{

							APP_LOGS.debug("Total Count is showing : " +countOfTotalOfTestSteps +"as Expected");
							comments=comments+"Total Count is showing : " +countOfTotalOfTestSteps +"as Expected (Pass)    .";

						}
						else
						{
							fail=true;
							comments=comments+" Total Count in Test Steps Statistics Grid showing i.e. : "+actualCountForTotalTestSteps +" Wrong  ( Failed ). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalStepsStatiticsFAilure");
							APP_LOGS.debug(" Total Count in Test Steps Statistics Grid showing i.e. : "+actualCountForTotalTestSteps +" Wrong  ( Failed ). ");
						}


						APP_LOGS.debug("......................................................******............................................");


					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Steps Statistics : Count Of PAss , Fail ,Pending ,Not Completed,Total is not showing as Expected  (Fail)");
						comments=comments+"Test Steps Statistics : Count Of PAss , Fail ,Pending ,Not Completed,Total is not showing as Expected  (Fail) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "testStepsStatiticsFailureOnTestingPage");
					}


					try{

						//After Clicking On Cancel Button ,The grid should come to its previous state and the data should not be saved into the Sharepoint List. 


						APP_LOGS.debug("Verification Of Testing Page After Clicking On Cancel Button ,The grid should come to its previous state and the data should not be saved into the Sharepoint List. ");

						APP_LOGS.debug("Executing Test Step 2  : "+ tp2TC2TestStep2 + " Of Test Pass : " + testPassName2 );

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP2-TC2-Test StepName_2 is creating ");

						getElement("TestingPage_failRadioButton_Id").click();

						APP_LOGS.debug("Clicking On Cancel Button so that status should come to Not Completed Radio Button");

						getElement("TestingPageCancelButton_Id").click();

						if(getElement("TestingPage_NotCompletedRadioButton").isSelected())
						{
							APP_LOGS.debug(" testing page Grid came to its Previous State and data is not saved As Expected (Pass)");
							comments=comments+"After clicking on Cancel Button..status should come to Not Completed Radio Button ...so testing page Grid came to its Previous State and data is not saved As Expected (Pass)";
						}
						else
						{
							fail=true;
							assertTrue(false);
							comments=comments+"Testing Page Grid is not Coming on previous state After clicking on Cancel Button  (Fail)  .";
							APP_LOGS.debug("Testing Page Grid is not Coming on previous state After clicking on Cancel Button  (Fail)  .");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CancelButtonClickingGridComeInPreviousStateIssue");
						}

						APP_LOGS.debug("......................................................******............................................");
					}
					catch(Throwable t)
					{

						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Testing Page Grid is not Coming on previous state After clicking on Cancel Button   (Fail)");
						comments=comments+"Test Steps Statistics : Count Of PAss , Fail ,Pending ,Not Completed,Total is not showing as Expected  (Fail) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CancelButtonClickingGridComeInPreviousStateIssue");
					}




					try
					{	


						//Verification Of Actual Result Text Area and Expected Result Text Area

						APP_LOGS.debug("Verification Of Actual Result Text Area and Expected Result Text Area");

						if(getElement("TestingPageMyActivity_ExpectedResultTextArea_Id").isDisplayed() && getElement("TestingPageMyActivity_ActualResultTextArea_Id").isDisplayed())
						{
							APP_LOGS.debug("Expected Result Text Area and Actual Result Text Area is showing properly on the middle of page ");
							comments=comments+"Expected Result Text Area and Actual Result Text Area is showing properly on the middle of page (Pass)";
						}
						else
						{	
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ActualTextAreaAndExpectedTextAreaIssue");
							APP_LOGS.debug("Expected Text Area  and Actual Text Area is not showing properly in the middle of page");
							comments=comments+"Expected Text Area  and Actual Text Area is not showing properly in the middle of page  (Fail)";

						}


						//"Verification Of Attachments on Testing Page  .


						APP_LOGS.debug("Verify Add Attachment Link is displaying  ?");

						if(getObject("TestingPageMyActivity_AddAttachmentLink").isDisplayed())
						{
							APP_LOGS.debug(" 'Add Attachment' Link is visible for Test Steps");
							comments=comments+"Add Attachment Link is  visible properly ..(Pass)  .";

						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"AddAttachmentLinkIssue");
							APP_LOGS.debug("Add Attachment Link is not visible ..(Fail) ");
							comments=comments+"Add Attachment Link is not visible ..(Fail)  .";

						}




						//Verifying Add attachment works properly and attachment should get added properly

						try
						{

							APP_LOGS.debug("Verifying Add attachment works properly and attachment should get added  ");

							getObject("TestingPageMyActivity_AddAttachmentLink").click();

							getElement("TestingPageMyActivity_AttachmentNameText_Id").sendKeys("Attachment1");

							Thread.sleep(1000);
							getElement("TestingPageMyActivity_AttachmentNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\TestData\\Attachment1.png");

							getElement("TestingPageMyActivity_AttachmentNameOkButton_Id").click();

							eventfiringdriver.executeScript("$('#ctl00_midPannel_g_5813db46_d81b_4e55_85e7_d6be9540207e_ctl00_toolBarTbltop_RightRptControls_ctl01_ctl00_diidIOSaveItem').click()");
							Thread.sleep(5000);


							oldViewAttachment1=getObject("TestingPageMyActivity_ViewAttachmentLink1").getAttribute("href");
							APP_LOGS.debug("Attachment is Added Successfully within Test Step :" +tp2TC2TestStep2 + " of Test case :"  +tp2TestCase2+ " Pass " );
							comments=comments+"Attachment is Added Successfully within Test Step :" +tp2TC2TestStep2 + " of Test case :"  +tp2TestCase2 +"(Pass)";

						}
						catch(Throwable t)
						{
							t.printStackTrace();
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Attachment is not Added  within Test Step :" +tp2TC2TestStep2 + " of Test case :"  +tp2TestCase2 +"Fail");
							comments=comments+"Attachment is not Added  within Test Step :" +tp2TC2TestStep2 + " of Test case :"  +tp2TestCase2 +"(Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"AttachmentAddingFailure");
						}


						// "Verifying  visibility Of' View Attachment ' Link "

						APP_LOGS.debug("Verifying  visibility Of' View Attachment ' Link ");


						if( eventfiringdriver.findElement(By.partialLinkText("[ View Attachment1 ]")).isDisplayed())
						{



							APP_LOGS.debug("View Attachment Link is visible on Testing Page   ");

							APP_LOGS.debug("Verify, after Clicking on View Attachment Link ,Attachment should open in new Window " );

							eventfiringdriver.findElement(By.partialLinkText("[ View Attachment1 ]")).click();

							String downloadHandle = eventfiringdriver.getWindowHandle();


							if(openInNewWindow(downloadHandle))
							{
								APP_LOGS.debug("View Attachment Link worked...Attachment is opening properly in New Window");
								comments=comments+"View Attachment Link worked...Attachment is opening properly in New Window (Pass)";
							}
							else
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ViewAttachmentLinkOpeningFailure");
								APP_LOGS.debug("View Attachment Link is not opening correctly ..(Fail) ");
								comments=comments+"View Attachment Link is not opening correctly ..(Fail) )  .";


							}


						}
						else
						{

							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ViewAttachmentLinkIssue");
							APP_LOGS.debug("View Attachment Link is not visible on Testing Page ..(Fail) ");
							comments=comments+"View Attachment Link is not visible on Testing Page ..(Fail)  .";

						}




						//Verification Of Testing Page that Maximum 3 Attachment can be added for Single TestStep

						APP_LOGS.debug(" Verification Of Testing Page that Maximum 3 Attachment can be added for Single TestStep,If Added 4th ,1st Attachment should be replaced with 4th Attachment  ");



						for(int i=2;i<=4;i++)
						{
							getObject("TestingPageMyActivity_AddAttachmentLink").click();

							getElement("TestingPageMyActivity_AttachmentNameText_Id").sendKeys("Attachment"+i);

							Thread.sleep(1000);
							getElement("TestingPageMyActivity_AttachmentNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\TestData\\Attachment"+i+".png");

							getElement("TestingPageMyActivity_AttachmentNameOkButton_Id").click();


							// getElement("TestingPageMyActivity_AttachmentNameSaveButton_Id").click();   
							//eventfiringdriver.findElement(By.xpath("//span[text()='Save']")).click();
							eventfiringdriver.executeScript("$('#ctl00_midPannel_g_5813db46_d81b_4e55_85e7_d6be9540207e_ctl00_toolBarTbltop_RightRptControls_ctl01_ctl00_diidIOSaveItem').click()");
							Thread.sleep(5000);

						}

						String newViewAttachment1=getObject("TestingPageMyActivity_ViewAttachmentLink2").getAttribute("href");

						String newViewAttachment2=getObject("TestingPageMyActivity_ViewAttachmentLink3").getAttribute("href");

						String newViewAttachment3=getObject("TestingPageMyActivity_ViewAttachmentLink4").getAttribute("href");

						if(!(oldViewAttachment1.equalsIgnoreCase(newViewAttachment1)||oldViewAttachment1.equalsIgnoreCase(newViewAttachment2)||oldViewAttachment1.equalsIgnoreCase(newViewAttachment3)))
						{
							APP_LOGS.debug("As we added 4th Attachment,1st Attachment is replaced with 4th Attachment Successfully as Expected  (Pass) .");
							comments=comments+"As we added 4th Attachment,1st Attachment is replaced with 4th Attachment Successfully as Expected  (Pass)   .";

						}

						else
						{	
							assertTrue(false);
							fail = true;
							APP_LOGS.debug("As we added 4th Attachment,1st Attachment is not replacing  with 4th Attachment Successfully as Expected (Fail)");
							comments+="As we added 4th Attachment,1st Attachment is not replacing  with 4th Attachment Successfully as Expected (Fail)" ;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentReplaceMentIssue");

						}





						APP_LOGS.debug("......................................................******............................................");




					}
					catch(Throwable t)
					{
						t.printStackTrace();
						assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Add&ViewAttachmentAndTextAreaIssue ");
						comments+="Add&ViewAttachmentAndTextAreaFailure  (fail)" ;
					}





					//verifying ' Click here to Preview ' Link work and PopUp will Open up		

					try
					{	

						APP_LOGS.debug(" verifying ' Click here to Preview ' Link work and PopUp will Open up ");

						String  actualresultText="TP2-TC1-TestStepName_2 created successfully";

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys(actualresultText);

						getElement("TestingPage_passRadioButton_Id").click();



						getObject("TestingPageMyActivity_ExpectedClickHereToPreviewLink").click();

						APP_LOGS.debug(" 'click Here To Review' Worked  and Expected Result Pop Up Open ");

						String expectedResultOnTestingpage=getElement("TestingPageMyActivity_ExpectedResultTextArea_Id").getText();

						if(compareStrings(tp2TC2expectedResultsTestStep_2,expectedResultOnTestingpage))
						{
							APP_LOGS.debug("Test Step Expected result " + expectedResultOnTestingpage +" is showing properly in Expected Result Pop Up  ");
							comments=comments+"'Click Here To Preview ' functinality is working As Expected ( Pass )   .";
						}
						else
						{	fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "clickHereToPreviewFailure");
						APP_LOGS.debug("Test Step Expected result " + expectedResultOnTestingpage +" is  not showing properly in Expected Result Pop Up ( Fail )  .");
						comments=comments+"Test Step Expected result " + expectedResultOnTestingpage +" is  not showing properly in Expected Result Pop Up ( Fail )  .";
						}

						getObject("ProjectViewAll_DeleteConfirmOkButton").click();


						getObject("TestingPageMyActivity_ActualClickHereToPreviewLink").click();

						APP_LOGS.debug(" 'click Here To Review' Worked  and ACtual Result Pop Up Open ");
						String actualResultOnTestingpage=getElement("TestingPageMyActivity_ActualResultTextArea_Id").getText();

						if(compareStrings(actualresultText,actualResultOnTestingpage))
						{
							APP_LOGS.debug("Test Step Expected result " + actualResultOnTestingpage +" is showing properly in Actual Result Pop Up  ");
							comments=comments+"'Click Here To Preview ' functinality is working As Expected ( Pass )   .";
						}
						else
						{	
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "clickHereToPreviewFailure");
							APP_LOGS.debug("Test Step Actual result " + actualResultOnTestingpage +" is  not showing properly in Actual Result Pop Up ( Fail )  .");
							comments=comments+"Test Step Actual result " + actualResultOnTestingpage +" is  not showing properly in Actual Result Pop Up ( Fail )  .";
						}

						getObject("TestingPage_feedbackRating_SaveButton").click();

						getElement("TestingPage_saveButton_Id").click();


					}
					catch(Throwable t)
					{
						t.printStackTrace();
						assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), " ClickHereToPreviewIssue ");
						comments+=" 'Click Here To Preview ' functionality failed  (fail)    ." ;
					}

					closeBrowser();

				}
				else 
				{
					APP_LOGS.debug("Tester Login Not Successful");

				}

			}
			catch (Throwable e) 
			{
				e.printStackTrace();
				assertTrue(false);
				fail = true;
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
				comments+="Skip Exception or other exception occured" ;
				closeBrowser();

			}

			

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
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
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
	public Object[][] getTestData(){
		return TestUtil.getData(testingPageSuiteXls, this.getClass().getSimpleName()) ;
	}

	private boolean createTestPass(String group, String portfolio, String project, String version, String testPassName,String startMonth,String startYear,String startDate,
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2000);

		getElement("TestPassNavigation_Id").click();
		Thread.sleep(2000);
		getObject("TestPasses_createNewProjectLink").click();


		try {

			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );

			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );

			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );

			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			//getElement("Version").sendKeys(version);
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);

			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();

			driver.switchTo().frame(1);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));

			getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 

			getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();

			getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();

			getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();

			driver.switchTo().defaultContent();

			selectStartOrEndDate(getObject("TestPassCreateNew__startDateImage"),startMonth,startYear, startDate);

			selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);

			getObject("TestPassCreateNew_testPassSaveBtn").click();

			Thread.sleep(2000);							


			if (getElement("TestPassCreateNew_testPassSuccessMessageText_Id").getText().contains("successfully")) 
			{
				getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();

				return true;
			}
			else 
			{
				return false;
			}





		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}

	}

	private boolean createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection,String area) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		Thread.sleep(3000);
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
			String[] testerRoleSelectionArray = testerRoleSelection.split(",");
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


			Select areaSelection=new Select(getElement("TesterAddTester_AreaSelection_Id"));

			areaSelection.selectByVisibleText(area);


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



	// Create Test Cases for that project


	private boolean createTestCase(String group, String portfolio, String project, String version, String testPassName, 
			String testCaseName,String estimatedTime) throws IOException, InterruptedException
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
			getElement("TestCasesCreateNew_EstimatedTime_id").sendKeys(estimatedTime);

			getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();

			Thread.sleep(2000);

			if (getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id").getText().contains("successfully")) 
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


	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName1,String testStepName2,String testerRole, String testStepResult, 
			String action)
	{

		try
		{
			//Calculating the number of pages in My Activities section
			APP_LOGS.debug("Calculation of number of pages available in My Activities. ");

			Thread.sleep(700);
			if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on My Activities .");
				totalPages=1;

			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on My Activities . Calculating total pages.");			
				totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}

			//Iterating the loop to the Total Number of Pages found on My Activities
			for (int i = 0; i < totalPages; i++) 
			{
				APP_LOGS.debug("Calculating number of rows present in a page. ");
				myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
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

						APP_LOGS.debug( tester.get(0).username + " : Tester is  having assigned Test Steps for Test Pass : " +testPass+ " :  of Project : " +project);

						//verifying Days Left & Not Completed count
						if((daysLeft>0) && (notCompletedCount>0 || notCompletedCount!=0) && (passCount==0) && 
								(failCount==0) && actionCell.equals(action))
						{	
							APP_LOGS.debug("Action is Begin Testing.");

							getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
							Thread.sleep(3000);

							return true;
						}

						else if((daysLeft>0) && (notCompletedCount>0 || notCompletedCount!=0) && (!(passCount==0)) && 
								(!(failCount==0)) && actionCell.equals(action))
						{
							APP_LOGS.debug("Action is Continue Testing.");

							getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
							Thread.sleep(3000);
							return true;

							/*	flag=true;
					                            i=totalPages;
					                            break;*/
						}
						else if((daysLeft>0 && notCompletedCount==0) && (!(passCount==0)) && 
								(!(failCount==0)) && (actionCell.equals(action)))
						{
							APP_LOGS.debug("Action is Testing Complete with link.");

							return true;
						}

					}
				}
				if (totalPages>1) 
				{
					if(getObject("DashboardMyActivity_NextLink").isDisplayed())
					{
						getObject("DashboardMyActivity_NextLink").click();
					}
					else
					{
						APP_LOGS.debug("Next link is disabled");
					}
				}
			}


			APP_LOGS.debug("Project Not Found");
			return false;

		}

		catch(Throwable t)
		{
			fail=true;
			APP_LOGS.debug("My Activity Grid not visible (Fail)");
			comments+="My Activity Grid not visible. (Fail)  .";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"MyActivityGridNotVisible");		  
			return false;
		}

	}




	//common code opening image in new browser window
	public static boolean openInNewWindow(String parentHandleName)
	{
		try
		{ 
			// call the method numberOfWindowsToBe as follows
			//WebDriverWait wait = new WebDriverWait(eventfiringdriver, 15, 100);
			wait.until(numberOfWindowsToBe(2));
			Set<String> availableWindows = eventfiringdriver.getWindowHandles();//this set size is

			// returned as 1 and not 2
			String newWindow = null;
			for (String window : availableWindows) {
				if (!parentHandleName.equals(window)) {
					newWindow = window;
				}
			}

			// switch to new window
			eventfiringdriver.switchTo().window(newWindow);
			eventfiringdriver.manage().window().maximize();
			Thread.sleep(1000);

			// and then close the new window
			eventfiringdriver.close();

			// switch to parent
			eventfiringdriver.switchTo().window(parentHandleName);

			return true;

		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}
	}


	//code for no of windows 
	public static ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				eventfiringdriver.getWindowHandles();
				return eventfiringdriver.getWindowHandles().size() == numberOfWindows;
			}
		};

	}






}






