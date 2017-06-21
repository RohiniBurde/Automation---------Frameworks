package com.uat.suite.testing_page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class Testing_VerifySequencingFeature extends TestSuiteBase
{

	static int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPassed=true;
	int counter=0;
	static boolean isLoginSuccess=false;
	String tp2selectedTestCaseName2;
	int countForPassedTestSteps=0;
	int countForFailedTestSteps=0;
	int countForNotCompletedTestSteps=4;
	int countForPendingTestSteps=0;
	String selectTestCaseFromDropdown;
	String comments;
	String runmodes[]=null;
	ArrayList<Credentials> version_Lead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	String[] testPass;
	String[] testCase;
	String[] testSteps;
	int myActivityTableRows;
	int totalPages;
	String projectCell;
	String versionCell;
	String testPassCell;
	String roleCell;
	int daysLeft;
	int notCompletedCount;
	int passCount;
	int failCount;
	String actionCell;
	String[] resultArray;
	boolean flag=false;
	Utility utilRecorder = new Utility();

	// Runmode of test case in a suite

	@BeforeTest
	public void checkTestSkip() throws Exception{

		//Aishwarya---Executing TestCase statement required
		if(!TestUtil.isTestCaseRunnable( testingPageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(testingPageSuiteXls, this.getClass().getSimpleName());

		version_Lead=new ArrayList<Credentials>();
		test_Manager=new ArrayList<Credentials>();
		tester=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());


	}


	//String testManager,String tester,String area,String addRole,String testCaseName,String estimatedTime
	// Test Case Implementation ...

	@Test(dataProvider="getTestData")
	public void VerifySequencingFeature  (String role,String groupName,String portfolioName,String projectName, 
			String version,String endMonth, String endYear, String endDate, String versionLead ,String testPassName1, 
			String tp1TestCase1,String tp1TC1TestStep1,String tp1TC1expectedResultsTestStep_1,String tp1TC1TestStep2,
			String tp1TC1expectedResultsTestStep_2,String tp1TestCase2,String tp1TC2TestStep1,String tp1TC2expectedResultsTestStep_1,
			String tp1TC2TestStep2,String tp1TC2expectedResultsTestStep_2,String testPassName2, String tp2TestCase1,
			String tp2TC1TestStep1,String tp2TC1expectedResultsTestStep_1,String tp2TC1TestStep2,String tp2TC1expectedResultsTestStep_2,
			String tp2TestCase2,String tp2TC2TestStep1,String tp2TC2expectedResultsTestStep_1,String tp2TC2TestStep2,
			String tp2TC2expectedResultsTestStep_2,String testPassName3, String tp3TestCase1,String tp3TC1TestStep1,
			String tp3TC1expectedResultsTestStep_1,String tp3TC1TestStep2,String tp3TC1expectedResultsTestStep_2,String tp3TestCase2,
			String tp3TC2TestStep1,String tp3TC2expectedResultsTestStep_1,String tp3TC2TestStep2,String tp3TC2expectedResultsTestStep_2,String testPassName4,
			String tp4TestCase1,String tp4TC1TestStep1,String tp4TC1expectedResultsTestStep_1,String tp4TC1TestStep2,String tp4TC1expectedResultsTestStep_2,
			String testManager,String testers,String addRole,String environmentURL,String environmentAliasName,
			String feedback,String properSequencingPopUpMessage) throws Exception
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
				Thread.sleep(2000);



				if (!createProject(groupName, portfolioName, projectName, version, endMonth, endYear, endDate, version_Lead.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
					comments="Project Creation Unsuccessful(Fail) by "+role+"  . ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+" . ");
					throw new SkipException("Project Creation Unsuccessfull");
				}
				APP_LOGS.debug(" User "+ role +" created PROJECT : "+projectName+" with Version Lead :"+version_Lead.get(0).username );


				//Creating First Test Pass  With Their Test Cases and Respective Test Steps

				APP_LOGS.debug("......................................................******............................................");

				APP_LOGS.debug("Creating Test Pass :" + testPassName1+ " with Test Cases : " + tp1TestCase1 + " And " + tp1TestCase2 + " with their respective Test Steps  ." );

				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass Creation Unsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}

				if (!createTester(groupName, portfolioName, projectName, version, testPassName1, tester.get(0).username,addRole,addRole)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");	
				}

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName1,tp1TestCase1))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}			

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName1,tp1TestCase2))
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
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
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

				if (!createTester(groupName, portfolioName, projectName, version, testPassName2, tester.get(0).username,addRole,addRole)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}


				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName2,tp2TestCase1))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}								

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName2,tp2TestCase2))
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
					throw new SkipException("TestStep Creation Unsuccessfull");
				}

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC1TestStep2,tp2TC1expectedResultsTestStep_2,tp2TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC2TestStep1,tp2TC2expectedResultsTestStep_1,tp2TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName2,tp2TC2TestStep2,tp2TC2expectedResultsTestStep_2,tp2TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
				}

				APP_LOGS.debug("Test Pass Name :" +testPassName2 + " with " + tp2TestCase1 +" and "+ tp2TestCase2 + "having 2 Test Steps each Created Successfully   ." );
				comments+="Test Pass Name :" +testPassName2 + " with " + tp2TestCase1 + " and "+ tp2TestCase2 + "having 2 Test Steps each Created Successfully (Pass)  ." ;
				APP_LOGS.debug("......................................................******............................................");


				//Creating Third Test Pass  With Their Test Cases and Respective Test Steps


				APP_LOGS.debug("Creating Test Pass :" + testPassName3+ " with Test Cases : " + tp3TestCase1 + " And " + tp3TestCase2 + " with their respective Test Steps  ." );
				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName3,endMonth, endYear, endDate, test_Manager.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}



				if (!createTester(groupName, portfolioName, projectName, version, testPassName3, tester.get(0).username,addRole,addRole)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");


				}

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName3,tp3TestCase1))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}								

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName3,tp3TestCase2))
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
					throw new SkipException("TestStep Creation Unsuccessfull");
				}

				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC1TestStep2,tp3TC1expectedResultsTestStep_2,tp3TestCase1, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC2TestStep1,tp3TC2expectedResultsTestStep_1,tp3TestCase2, addRole))
				{ 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					closeBrowser();
					throw new SkipException("TestStep Creation Unsuccessfull");
				}
				if (!createTestStep(groupName,portfolioName,projectName,version,testPassName3,tp3TC2TestStep2,tp3TC2expectedResultsTestStep_2,tp3TestCase2, addRole))
				{ 	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure");
					comments="Test Step Creation Unsuccessful(Fail)";
					APP_LOGS.debug("Test Step Creation Unsuccessful(Fail)");
					throw new SkipException("TestStep Creation Unsuccessfull");
				}

				APP_LOGS.debug("Test Pass Name :" +testPassName3 + " with " + tp3TestCase1 +" and "+ tp3TestCase2 + "having 2 Test Steps each Created Successfully   ." );
				comments+="Test Pass Name :" +testPassName3 + " with " + tp3TestCase1 + " and "+ tp3TestCase2 + "having 2 Test Steps each Created Successfully (Pass)  ." ;
				APP_LOGS.debug("......................................................******............................................");



				//Creating 4th Test Pass  With Their Test Cases and Respective Test Steps
				APP_LOGS.debug("Creating Test Pass :" + testPassName4+ " with Test Cases : " + tp4TestCase1 + " with their respective Test Steps  ." );

				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName4, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}

				if (!createTester(groupName, portfolioName, projectName, version, testPassName4, tester.get(0).username,addRole,addRole)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}

				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName4,tp4TestCase1))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}
				createTestStepsToVerifyTestStepsNextPrevLink(groupName,portfolioName,projectName,version,testPassName4,tp4TC1TestStep1,tp4TC1expectedResultsTestStep_1,tp4TestCase1, addRole);


				APP_LOGS.debug("......................................................******............................................");



				//Verifying Functionality

				//Assigning 3 different Sequential Testing Functionality to the 3 individual Test Passes

				try
				{



					APP_LOGS.debug("Assigning Test Pass level Options i.e.  Sequential Testing within a Test Case Or Test Pass Or out of sequence to the Test Passes  ");
					Thread.sleep(1000);

					getElement("UATConfigurationTab_Id").click();

					//getElement("ConfigGeneralSettingTab_Id").click();

					APP_LOGS.debug("Selecting  Project from General Setting Project Dropdown ");
					Thread.sleep(1000);

					Select projectSelection=new Select(getElement("ConfigGeneralSettingProjectSelectDropdown_Id"));

					projectSelection.selectByVisibleText(projectName);

					Thread.sleep(500);

					APP_LOGS.debug("Selecting Version from General Setting Version Dropdown");

					Select versionSelection=new Select(getElement("ConfigGeneralSettingVersionSelectDropdown_Id"));

					versionSelection.selectByVisibleText(version);

					Thread.sleep(500);

					APP_LOGS.debug("Selecting Test Pass from General Setting Test Pass Dropdown");


					List<WebElement> selectTP = getElement("ConfigGeneralSettingTestPassSelectDropdown_Id").findElements(By.tagName("option"));

					for(int i=1 ;i<=selectTP.size();i++)
					{   
						String selectTestPassFromDropdown=getObject("ConfigGeneralSettingSelectTestPassFromDropdownXPath1","ConfigGeneralSettingSelectTestPassFromDropdownXPath2",i).getAttribute("title").trim();

						if(selectTestPassFromDropdown.equals(testPassName1))
						{
							getObject("ConfigGeneralSettingSelectTestPassFromDropdownXPath1","ConfigGeneralSettingSelectTestPassFromDropdownXPath2",i).click();
							Thread.sleep(300);
							getElement("ConfigGeneralSettingSeqTestingWithinTCRadioButton_Id").click();
							Thread.sleep(500);
							getObject("Configuratiion_GeneralSettingSaveBtn").click();
							Thread.sleep(500);
							getObject("ProjectViewAll_DeleteConfirmOkButton").click();
							APP_LOGS.debug("Test Pass Level Options : ' Sequential Testing Within Test Case is selected for Test Pass ' : "+ selectTestPassFromDropdown + "   (Pass)  .");
							comments=comments+"Test Pass Level Options : ' Sequential Testing Within Test Case is selected for Test Pass ' : "+ selectTestPassFromDropdown +"  (Pass)  .";  
							//break;
						}

						else if(selectTestPassFromDropdown.equals(testPassName2))
						{
							getObject("ConfigGeneralSettingSelectTestPassFromDropdownXPath1","ConfigGeneralSettingSelectTestPassFromDropdownXPath2",i).click();
							Thread.sleep(300);
							getElement("ConfigGeneralSettingSeqTestingWithinTPRadioButton_Id").click();
							Thread.sleep(500);
							getObject("Configuratiion_GeneralSettingSaveBtn").click();
							Thread.sleep(500);
							getObject("ProjectViewAll_DeleteConfirmOkButton").click();
							APP_LOGS.debug("Test Pass Level Options : ' Sequential Testing Within Test Pass is selected for Test Pass ' : "+ selectTestPassFromDropdown + "  (Pass)  .");
							comments=comments+"Test Pass Level Options : ' Sequential Testing Within Test Pass is selected for Test Pass ' : "+ selectTestPassFromDropdown +"   (Pass)  .";  
							//break;
						}

						else if(selectTestPassFromDropdown.equals(testPassName3))
						{
							getObject("ConfigGeneralSettingSelectTestPassFromDropdownXPath1","ConfigGeneralSettingSelectTestPassFromDropdownXPath2",i).click();
							Thread.sleep(300);
							getElement("ConfigGeneralSettingSeqTestingWithinOutOfTestingRadioButton_Id").click();
							Thread.sleep(500);
							getObject("Configuratiion_GeneralSettingSaveBtn").click();
							Thread.sleep(500);
							getObject("ProjectViewAll_DeleteConfirmOkButton").click();
							APP_LOGS.debug("Test Pass Level Options : ' Sequential Testing Out Of Sequence is selected for Test Pass ' : "+ selectTestPassFromDropdown + "     (Pass)  .");
							comments=comments+"Test Pass Level Options : ' Sequential Testing Out Of Sequence is selected for Test Pass ' : "+ selectTestPassFromDropdown +"   (Pass)  .";  
							//break;
						}

						else if(selectTestPassFromDropdown.equals(testPassName4))
						{
							//break;				
						}

						else
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SelectedTestPassShouldNotBePresentWithinProjectFailure:");
							comments=comments+"Selected Test Pass :"+selectTestPassFromDropdown+" Should Not present within Project ( Fail)) ";
							APP_LOGS.debug("Selected Test Pass :"+selectTestPassFromDropdown+" Should Not present within Project ( Fail))");
							closeBrowser();
							throw new SkipException("Selected Test Pass : "+selectTestPassFromDropdown+" Should Not present within Project ( Fail)");



						}

					}

				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassLevelOptionsSeqTestingFailure:");
					comments=comments+"SequentialLevelTestingSelectionFailure...Skipping Everything ( Fail) ";
					APP_LOGS.debug("SequentialLevelTestingSelectionFailure...Skipping Everything ( Fail) ");

				}


				//End Of Assignment OF Sequential Testing Functionality to the Test Passes 

				closeBrowser();




				APP_LOGS.debug(".........................................****************************..............................................");			

				// Log in with Tester to perform Testing According to  Sequential Testing Selection Of TCs ,TPs and Out OF sequence

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to Testing According to  Sequential Testing withinTest Cases" );

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{





					try
					{		



						//StartOf Test Pass Level Options :  Sequential Testing within a Test Case Functionality 

						APP_LOGS.debug("verifying the Sequential Testing according Test Pass Level Options i.e. Sequential Testing within a Test Case ");

						if (!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,"Begin Testing"))
						{	          
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailure");
							comments="Begin Testing Click Failure For "+testPassName1 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName1 +" (Fail ) ");
							throw new SkipException("Begin Testing Click Failure For "+testPassName1);

						}


						APP_LOGS.debug("Executing Test Cases by Selecting Test Pass Level Options : Sequential Testing within a Test Case");

						String actualTestPassName1PresentOnTestingPage=getObject("TestingPageMyActivityTesPassName").getAttribute("title");

						// Comparing Required Test Pass Name is selected Or not on Testing Page )

						if(actualTestPassName1PresentOnTestingPage.equals(testPassName1))
						{	



							APP_LOGS.debug("As we Selected Test Pass Level Options :Sequential Testing within a Test Case ...So User should be able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1);

							APP_LOGS.debug("Test case 2 : "+ tp1TestCase2 +" is selecting from dropdown for execution  before Test Case 1 : " + tp1TestCase1);

							List<WebElement> selectTC = getElement("TestingPageMyActivityTestCasesDropdown_Id").findElements(By.tagName("option"));

							for(int i=1 ;i<=selectTC.size();i++)
							{	


								selectTestCaseFromDropdown=getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).getAttribute("title").trim();

								//Verifying Required Test case is selected From Drop down
								if(selectTestCaseFromDropdown.equals(tp1TestCase2))
								{	
									getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).click();
									Thread.sleep(300);

									tp2selectedTestCaseName2=getElement("TestingPageMyActivityTestCaseName_Id").getText();
									counter++;
									break;

								}

							}

							if(counter==0)
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestCaseNotPresentInDropdownOfTestingPageFailure");
								APP_LOGS.debug("Test Case is not present in Dropdown Of Testing Page (Fail)   .");
								comments=comments+"Test Case is not present in Dropdown Of Testing Page (Fail)  .";
							}
							else
							{

								APP_LOGS.debug( tp1TestCase2 +" : test Case is selected...");

							}
							//after using again reinitializing zero 0
							counter=0;

							//Comparing Required Test Cases Name is selected and showing on Testing Page
							if(compareStrings(tp1TestCase2, tp2selectedTestCaseName2))
							{

								APP_LOGS.debug("Executing Test Case :"+ tp1TestCase2 +"Before Test Case :"+ tp1TestCase1);
								APP_LOGS.debug("Selecting test Step 2 : "+tp1TC2TestStep2 +"Before Test Step 1:"+tp1TC2TestStep2);

								getObject("TestingPageMyActivityTestStep2Selection").click();

								String actualTestStepNamePresentOnTestingPage=getObject("TestingPageMyActivityTestStepNameTextOnTestingPage").getText();

								//Verifying Test Step2 is present on Testing Page

								APP_LOGS.debug("Verifying That as we selected Test Pass Level Option : Sequential Testing within a Test Case ..User should not be execute Test Step2 Before Test Step 1");
								APP_LOGS.debug("Uset should get Message stating :You must complete Test Steps in proper Sequence");

								if(compareStrings(tp1TC2TestStep2, actualTestStepNamePresentOnTestingPage))
								{

									getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP3-TC2-Test StepName_2 is created Successfully");

									getElement("TestingPage_passRadioButton_Id").click();

									getElement("TestingPage_saveButton_Id").click();

									//Verifying Pop Up Message :  should display as we select 

									if(getObject("TestingPageMyActivityInProperSequencingPopUp").isDisplayed())
									{      
										String properTestCaseSequenceMessage=getElement("TestingPageMyActivityInProperSequencingPopUpMessage_Id").getText();

										if(compareStrings(properSequencingPopUpMessage, properTestCaseSequenceMessage))
										{
											APP_LOGS.debug("proper Sequence Expected Pop Up Message is displayed Stating : "+properTestCaseSequenceMessage);

										}
										else
										{
											fail=true;
											TestUtil.takeScreenShot(this.getClass().getSimpleName(),"properSequencePopUpMessageFailure");
											APP_LOGS.debug("proper Sequence Expected Pop Up Message is not displaying as expected (Fail)");
											comments=comments+"proper Sequence Expected Pop Up Message is not displaying as expected (Fail)  .";
										}
										getObject("ConfigUserSettingEnvironmentOKButton").click();
										APP_LOGS.debug("user is not allowed to execute Test Step 2 Before Test Step 1 as Expected ( Pass )");
										APP_LOGS.debug("User is not Following proper Sequence Of test Steps Pop Up  Message is showing Stating : "+ properTestCaseSequenceMessage  +" As Expected   (Pass)" );
										comments=comments+"User is not Following proper Sequence Of test Steps Pop Up  Message is showing Stating : "+ properTestCaseSequenceMessage  +" As Expected   (Pass)" ;

									}
									else
									{	
										fail=true;
										assertTrue(false);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ExecutionOfTestStep2BeforeTestStep1Failure");
										APP_LOGS.debug("Test Steps is saved without showing Pop Up Message (Fail)   .");
										comments=comments+"Test Steps is saved without showing Pop Up Message (Fail)  .";
									}

								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestStep2NotPresentOnTestingPageFailure");
									APP_LOGS.debug("Test Steps 2 :"+tp1TC2TestStep2+" is not present on testing Page (Fail)   .");
									comments=comments+"Test Steps 2 :"+tp1TC2TestStep2+" is not present on testing Page (Fail)   .";
								}



							}
							else
							{
								fail=true;		
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestCaseNotPresentInDropdownOfTestingPageFailure");
								APP_LOGS.debug("Test Case is not present in Dropdown Of Testing Page (Fail)   .");
								comments=comments+"Test Case is not present in Dropdown Of Testing Page (Fail)  .";

							}	
						}
						else
						{
							fail=true;
							assertTrue(false);         
							APP_LOGS.debug("Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)");
							comments=comments+"Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestPassNotPresentOnTestingPage");
						}







						getElement("UAT_dashboard_Id").click();

						if (!searchTestPassAndPerformTesting(projectName, version,testPassName1,tp1TC1TestStep1,tp1TC1TestStep2,addRole,"Begin Testing"))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailure");
							comments="Begin Testing Click Failure For "+testPassName1 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName1 +" (Fail ) ");
							closeBrowser();
							throw new SkipException("Begin Testing Click Failure For "+testPassName1);

						}


						if(actualTestPassName1PresentOnTestingPage.equals(testPassName1))
						{	



							APP_LOGS.debug("As we Selected Test Pass Level Options :Sequential Testing within a Test Case ...So User should be able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1);

							APP_LOGS.debug("Test case 2 : "+ tp1TestCase2 +" is selecting from dropdown for execution  before Test Case 1 : " + tp1TestCase1);

							List<WebElement> selectTC = getElement("TestingPageMyActivityTestCasesDropdown_Id").findElements(By.tagName("option"));

							for(int i=1 ;i<=selectTC.size();i++)
							{	


								selectTestCaseFromDropdown=getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).getAttribute("title").trim();

								//Verifying Required Test case is selected From Drop down
								if(selectTestCaseFromDropdown.equals(tp1TestCase2))
								{	
									getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).click();
									Thread.sleep(300);

									tp2selectedTestCaseName2=getElement("TestingPageMyActivityTestCaseName_Id").getText();
									counter++;
									break;

								}

							}

							if(counter==0)
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestCaseNotPresentInDropdownOfTestingPageFailure");
								APP_LOGS.debug("Test Case is not present in Dropdown Of Testing Page (Fail)   .");
								comments=comments+"Test Case is not present in Dropdown Of Testing Page (Fail)  .";
							}
							else
							{

								APP_LOGS.debug( tp1TestCase2 +" : test Case is selected...");

							}
							//after using again reinitializing zero 0
							counter=0;


							//Comparing Required Test Cases Name is selected and showing on Testing Page
							if(compareStrings(tp1TestCase2, tp2selectedTestCaseName2))
							{

								APP_LOGS.debug("Executing Test Case :"+ tp1TestCase2 );

								for(int j=1;j<=2;j++)
								{
									getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP1-TC2-Test StepName_"+j+" is created Successfully");

									getElement("TestingPage_passRadioButton_Id").click();

									getElement("TestingPage_saveButton_Id").click();

									countForPassedTestSteps++;

									countForNotCompletedTestSteps--;

								}	
								String countForTestStepsPassed=getObject("TestingPageMyActivity_TestStepsPassedCount").getText();
								int actualCountForTestStepsPassed = Integer.parseInt(countForTestStepsPassed);
								String countForTestStepsNotCompleted=getObject("TestingPageMyActivity_TestStepsNotCompletedCount").getText();
								int actualCountForTestStepsNotCompleted = Integer.parseInt(countForTestStepsNotCompleted);


								if(compareIntegers(countForPassedTestSteps, actualCountForTestStepsPassed)&&compareIntegers(countForNotCompletedTestSteps, actualCountForTestStepsNotCompleted))
								{
									APP_LOGS.debug("As we Selected Test Pass Level Options :Sequential Testing within a Test Case....So User is  able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +"  ... Sequential Testing within a Test Case Functionality Worked properly ...( Pass  ).");
									comments=comments+"As we Selected Test Pass Level Options :Sequential Testing within a Test Case....So User is  able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +" ...  Sequential Testing within a Test Case Functionality Worked properly ...( Pass)  .";
								}
								else
								{	fail=true;
								APP_LOGS.debug("As we Selected Test Pass Level Options :Sequential Testing within a Test Case....So User is not able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +" ... Sequential Testing within a Test Case Functionality is not Working properly ...( Fail)  .");
								comments=comments+"As we Selected Test Pass Level Options :Sequential Testing within a Test Case....So User is not able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +".... Sequential Testing within a Test Case Functionality is not Working properly ...( Fail)  ..";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SequentialTestingwithinaTestCaseFailure");
								}

							}
							else
							{
								fail=true;
								APP_LOGS.debug("Selected Test case " + tp1TestCase2 +" is not present in  On Testing Page (Fail)");
								comments=comments+"Selected Test case " + tp1TestCase2 +" is not present in  On Testing Page (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestCaseNotPresentOnTestingPage");
							}


						}

						else
						{
							fail=true;
							assertTrue(false);         
							APP_LOGS.debug("Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)");
							comments=comments+"Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestPassNotPresentOnTestingPage");
						}
						//reinitializing value to default
						countForPassedTestSteps=0;
						countForFailedTestSteps=0;
						countForNotCompletedTestSteps=4;
						countForPendingTestSteps=0;	

					}				

					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug(" Some other Exception in Sequential Testing within a TestCase Functionality   (Fail)  .");
						comments=comments+" Some other Exception in Sequential Testing within a TestCase Functionality   (Fail)  .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SomeExceptionInSequentialTestingwithinaTestCaseFailure");
					}

					//End Of Test Pass Level Options :  Sequential Testing within a Test Case Functionality 

					closeBrowser();

				}
				else
				{
					APP_LOGS.debug("Tester Login Not Successful");
				}




				APP_LOGS.debug(".........................................****************************..............................................");


				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to verify Sequential Testing within a Test Pass Functionality ");

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{


					try
					{		


						//Start Of Test Pass Level Options :  Sequential Testing within a Test Pass Functionality 

						getElement("UAT_dashboard_Id").click();

						APP_LOGS.debug("verifying the Sequential Testing according Test Pass Level Options i.e. Sequential Testing within a Test Pass ");

						if (!searchTestPassAndPerformTesting(projectName, version,testPassName2,tp2TC1TestStep1,tp2TC1TestStep2,addRole,"Begin Testing"))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass2");
							comments="Begin Testing Click Failure For "+testPassName2 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName2 +" (Fail ) ");
							closeBrowser();
							throw new SkipException("Begin Testing Click Failure For "+testPassName2);

						}


						APP_LOGS.debug("Executing Test Cases by Selecting Test Pass Level Options : Sequential Testing within a Test Pass");

						String actualTestPassName2PresentOnTestingPage=getObject("TestingPageMyActivityTesPassName").getAttribute("title");


						// Comparing Required Test Pass Name is selected Or not on Testing Page )

						if(actualTestPassName2PresentOnTestingPage.equals(testPassName2))
						{

							APP_LOGS.debug("As we Selected Test Pass Level Options :Sequential Testing within a Test Pass ...So User should not be able to execute Test case 2 : "+ tp2TestCase2 +" before Test Case 1 : "+tp2TestCase1);
							APP_LOGS.debug("Verifying that User can not execute the test case 2 :"+ tp2TestCase2 +" before Test Case 1 : "+tp2TestCase1 +" As expected ");

							APP_LOGS.debug("Test case 2 : "+ tp2TestCase2 +" is selecting from dropdown for execution  before Test Case 1 : " + tp2TestCase1 );


							List<WebElement> selectTC = getElement("TestingPageMyActivityTestCasesDropdown_Id").findElements(By.tagName("option"));





							for(int i=1 ;i<=selectTC.size();i++)
							{	
								selectTestCaseFromDropdown=getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).getAttribute("title").trim();


								//Verifying Required Test case is selected From Drop down
								if(selectTestCaseFromDropdown.equals(tp2TestCase2))
								{	
									getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).click();
									Thread.sleep(300);
									counter++;
									tp2selectedTestCaseName2=getElement("TestingPageMyActivityTestCaseName_Id").getText();
									break;

								}

							}

							if(counter==0)
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestCaseNotPresentInDropdownOfTestingPageFailure");
								APP_LOGS.debug("Test Case is not present in Dropdown Of Testing Page (Fail)   .");
								comments=comments+"Test Case is not present in Dropdown Of Testing Page (Fail)  .";
							}
							else
							{

								APP_LOGS.debug( tp2TestCase2 +" : test Case is selected...");

							}
							//after using again reinitializing zero 0
							counter=0;


							//Comparing Required Test Cases Name is selected and showing on Testing Page
							if(compareStrings(tp2TestCase2, tp2selectedTestCaseName2))
							{

								APP_LOGS.debug("Executing Test Case :"+ tp2TestCase2 +"Before Test Case :"+ tp2TestCase1 +"  Pop Up Message should come Stating : You must complete Test Steps in proper Sequence" );



								getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP2-TC2-Test StepName_2 is created Successfully");

								getElement("TestingPage_passRadioButton_Id").click();

								getElement("TestingPage_saveButton_Id").click();

								//Verifying Pop Up Message :  should display as we select 

								if(getObject("TestingPageMyActivityInProperSequencingPopUp").isDisplayed())
								{
									String properTestCaseSequenceMessage=getElement("TestingPageMyActivityInProperSequencingPopUpMessage_Id").getText();
									if(compareStrings(properSequencingPopUpMessage, properTestCaseSequenceMessage))
									{
										APP_LOGS.debug("proper Sequence Expected Pop Up Message is displayed Stating : "+properTestCaseSequenceMessage);

									}
									else
									{
										fail=true;
										TestUtil.takeScreenShot(this.getClass().getSimpleName(),"properSequencePopUpMessageFailure");
										APP_LOGS.debug("proper Sequence Expected Pop Up Message is not displaying as expected (Fail)");
										comments=comments+"proper Sequence Expected Pop Up Message is not displaying as expected (Fail)  .";
									}
									getObject("ConfigUserSettingEnvironmentOKButton").click();

									APP_LOGS.debug("Sequential Testing within a Test Pass Functionality worked properly...User is not Following proper Sequence Of test Steps Pop Up  Message is showing Stating : "+ properTestCaseSequenceMessage  +" As Expected   (Pass)" );
									comments=comments+"Sequential Testing within a Test Pass Functionality worked properly...User is not Following proper Sequence Of test Steps Pop Up  Message is showing Stating : "+ properTestCaseSequenceMessage  +" As Expected   (Pass)" ;

								}
								else
								{	fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SequentialTestingwithinaTestPassFunctionalityFailure");
								APP_LOGS.debug("Test Steps is saved without showing Pop Up Message (Fail)   .");
								comments=comments+"Test Steps is saved without showing Pop Up Message (Fail)  .";
								}


							}
							else
							{
								fail=true;
								APP_LOGS.debug("Selected Test case " + tp2TestCase2 +" is not present in  On Testing Page (Fail)");
								comments=comments+"Selected Test case " + tp2TestCase2 +" is not present in  On Testing Page (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestCaseNotPresentOnTestingPage");
							}

						}	
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)");
							comments=comments+"Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestPassNotPresentOnTestingPage");
						}

						//reinitializing value to default	
						countForPassedTestSteps=0;
						countForFailedTestSteps=0;
						countForNotCompletedTestSteps=4;
						countForPendingTestSteps=0;				

					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug(" Some other Exception in Sequential Testing within a Test Pass Functionality   (Fail)  .");
						comments=comments+" Some other Exception in Sequential Testing within a Test Pass Functionality   (Fail)  .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SomeExceptionInSequentialTestingwithinaTestPassFailure");
					}

					closeBrowser();

					//End Of Test Pass Level Options :  Sequential Testing within a Test Pass Functionality 

					APP_LOGS.debug(".........................................****************************..............................................");


				}		
				else
				{
					APP_LOGS.debug("Tester Login Not Successful");
				}



				//Start  Of Test Pass Level Options : Testing Out Of Sequence Functionality 


				APP_LOGS.debug(".........................................****************************..............................................");


				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to verify Testing Out Of Sequence Functionality  ");

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{



					try
					{

						getElement("UAT_dashboard_Id").click();

						APP_LOGS.debug("verifying the Sequential Testing according Test Pass Level Options i.e. Testing Out Pf Sequence ");




						if (!searchTestPassAndPerformTesting(projectName, version,testPassName3,tp3TC1TestStep1,tp3TC1TestStep2,addRole,"Begin Testing"))
						{	

							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass3");
							comments="Begin Testing Click Failure For "+testPassName3 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName3 +" (Fail ) ");
							throw new SkipException("Begin Testing Click Failure For "+testPassName3);	


						}


						APP_LOGS.debug("Executing Test Cases by Selecting Test Pass Level Options :  Testing Out Of Sequence");

						String actualTestPassName3PresentOnTestingPage=getObject("TestingPageMyActivityTesPassName").getAttribute("title");

						// Comparing Required Test Pass Name is selected Or not on Testing Page )

						if(actualTestPassName3PresentOnTestingPage.equals(testPassName3))
						{

							APP_LOGS.debug("As we Selected Test Pass Level Options : Testing Out Of Sequence ...So User should able to execute Test case 2 : "+ tp3TestCase2 +" before Test Case 1 : "+tp3TestCase1 +"  as well as  Test Step 2 : "+ tp3TC2TestStep2 +" before Test Step 1 : " +tp3TC2TestStep1 +" Randomly");
							APP_LOGS.debug("Verifying that User can execute the test case 2 :"+ tp3TestCase2 + "and Test Step 2 : " + tp3TC2TestStep2 +"  before Test Case 1 : "+tp3TestCase1 +  "and Test Step 1 : " + tp3TC2TestStep1 +" randomly As expected ");

							APP_LOGS.debug("Test case 2 : "+ tp3TestCase2 +" is selecting from dropdown and Test Step 2 :"+tp3TC2TestStep2 +" is selecting.. ");


							List<WebElement> selectTC = getElement("TestingPageMyActivityTestCasesDropdown_Id").findElements(By.tagName("option"));

							for(int i=1 ;i<=selectTC.size();i++)
							{	
								selectTestCaseFromDropdown=getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).getAttribute("title").trim();

								//Verifying Required Test case is selected From Drop down
								if(selectTestCaseFromDropdown.equals(tp3TestCase2))
								{	
									getObject("TestingPageMyActivitySelectTestCaseFromDropdownXpath1","TestingPageMyActivitySelectTestCaseFromDropdownXpath2",i).click();
									Thread.sleep(300);
									tp2selectedTestCaseName2=getElement("TestingPageMyActivityTestCaseName_Id").getText();
									counter++;
									break;

								}

							}


							if(counter==0)
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestCaseNotPresentInDropdownOfTestingPageFailure");
								APP_LOGS.debug("Test Case is not present in Dropdown Of Testing Page (Fail)   .");
								comments=comments+"Test Case is not present in Dropdown Of Testing Page (Fail)  .";
							}
							else
							{

								APP_LOGS.debug( tp3TestCase2 +" : test Case is selected...");

							}
							//after using again reinitializing zero 0
							counter=0;

							//Comparing Required Test Cases Name is selected and showing on Testing Page
							if(compareStrings(tp3TestCase2, tp2selectedTestCaseName2))
							{

								APP_LOGS.debug("Executing Test Case :"+ tp3TestCase2 +"Before Test Case :"+ tp3TestCase1);
								APP_LOGS.debug("Selecting test Step 2 : "+tp3TC2TestStep2 +"Before Test Step 1:"+tp3TC2TestStep2);
								getObject("TestingPageMyActivityTestStep2Selection").click();

								String actualTestStepNamePresentOnTestingPage=getObject("TestingPageMyActivityTestStepNameTextOnTestingPage").getText();

								//Verifying Test Step2 is present on Testing Page

								if(compareStrings(tp3TC2TestStep2, actualTestStepNamePresentOnTestingPage))
								{

									getElement("TestingPageMyActivity_ActualResult_Id").sendKeys("TP3-TC2-Test StepName_2 is created Successfully");

									getElement("TestingPage_passRadioButton_Id").click();

									getElement("TestingPage_saveButton_Id").click();
									countForPassedTestSteps++;
									countForNotCompletedTestSteps--;
									String countForTestStepsPassed=getObject("TestingPageMyActivity_TestStepsPassedCount").getText();
									int actualCountForTestStepsPassed = Integer.parseInt(countForTestStepsPassed);	
									String countForTestStepsNotCompleted=getObject("TestingPageMyActivity_TestStepsNotCompletedCount").getText();
									int actualCountForTestStepsNotCompleted = Integer.parseInt(countForTestStepsNotCompleted);


									if(compareIntegers(countForPassedTestSteps, actualCountForTestStepsPassed)&&compareIntegers(countForNotCompletedTestSteps, actualCountForTestStepsNotCompleted))
									{
										APP_LOGS.debug("As we Selected Test Pass Level Options : Testing Out Of Sequence....So User is  able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +"  ...  as well as Test Step 2 before Test Step1 randomly as Expected ...( Pass  ).");
										comments=comments+"As we Selected Test Pass Level Options : Testing Out Of Sequence....So User is  able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +"  ...  as well as Test Step 2 before Test Step1 randomly as Expected ...( Pass  ).  .";
									}
									else
									{		fail=true;
									APP_LOGS.debug(" ( Failure) As we Selected Test Pass Level Options :Testing Out Of Sequence....So User is not able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +" ... as well as Test Step 2 before Test Step1 randomly as Expected ...( Fail)  .");
									comments=comments+"  ( Failure) As we Selected Test Pass Level Options :Testing Out Of Sequence....So User is not able to execute Test case 2 : "+ tp1TestCase2 +" before Test Case 1 : "+tp1TestCase1 +".... as well as Test Step 2 before Test Step1 randomly as Expected ...( Fail  ).  ..";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingOutOfSequence");
									}


								}
								else
								{
									fail=true;
									APP_LOGS.debug("Selected Test Step : " + tp3TC2TestStep2 +" is not present in  On Testing Page (Fail)");
									comments=comments+"Selected Test Step " + tp3TC2TestStep2 +" is not present in  On Testing Page (Fail)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestStepNotPresentOnTestingPage");
								}



							}
							else
							{
								fail=true;
								APP_LOGS.debug("Selected Test case " + tp3TestCase2 +" is not present in  On Testing Page (Fail)");
								comments=comments+"Selected Test case " + tp3TestCase2 +" is not present in  On Testing Page (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestCaseNotPresentOnTestingPage");
							}

						}	
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)");
							comments=comments+"Selected Test Pass is not showing on Testing Page (Below Dashboard tab)   (Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(),"SelectedTestPassNotPresentOnTestingPage");
						}


						countForPassedTestSteps=0;
						countForFailedTestSteps=0;
						countForNotCompletedTestSteps=4;
						countForPendingTestSteps=0;				

					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug(" Some other Exception in Testing Out Of Sequence Functionality   (Fail)  .");
						comments=comments+" Some other Exception in  Testing Out Of Sequence Functionality   (Fail)  .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SomeExceptionInTestingwOutOfSequenceFailure");
					}

					closeBrowser();

				}
				else
				{
					APP_LOGS.debug("Tester Login Not Successful");
				}



				//End Of Test Pass Level Options : Testing Out Of Sequence Functionality 



				closeBrowser();
				APP_LOGS.debug(".........................................****************************..............................................");

				//Start  Of  : To Verify TestSteps Next Prev Link 

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to verify Testing Out Of Sequence Functionality  ");

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{


					try
					{

						getElement("UAT_dashboard_Id").click();

						APP_LOGS.debug("verifying the Test Steps Next Previous Link Functionality after adding Test Steps more than 10 ");

						if(!searchTestPassAndPerformTesting(projectName, version,testPassName4,tp4TC1TestStep1,tp4TC1TestStep2,addRole,"Begin Testing"))
						{	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailureForTestPass4");
							comments="Begin Testing Click Failure For "+testPassName4 +" ( Fail )";
							APP_LOGS.debug("Begin Testing Click Failure For "+testPassName4 +" (Fail ) ");
							closeBrowser();
							throw new SkipException("Begin Testing Click Failure For "+testPassName4);	


						}

						APP_LOGS.debug("As we landed on Testing Page ...Prev Link Of Test Steps under Test Cases Selection should be disabled.. ");

						if(isElementExistsById("TestingPageMyActivityTestStepsTableprevLink_Id"))
						{
							if(getElement("TestingPageMyActivityTestStepsTableprevLink_Id").getAttribute("disabled").contains("true"))
							{
								APP_LOGS.debug("Previous Link Of Test Steps under Test Cases Selection is disabled as expected ( Pass )   .");
								comments=comments+"Previous Link Of Test Steps under Test Cases Selection is disabled as expected ( Pass )   .";
							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("' Previous 10 ' Link Of Test Steps under Test Cases Selection is not disabled as expected ( Fail )   .");
								comments=comments+"' Previous Link ' Of Test Steps under Test Cases Selection is not disabled as expected ( Fail )   .";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "prevLinkDisabledFailure");
							}

						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Previous Page link is not present( Fail)");
							comments=comments+"Previous Page link is not present( Fail)";
						}

						int limitForTestStepsPerPage=10;
						long numOfTestStepsPresentForSelectedTestCaseLongValue=(long) eventfiringdriver.executeScript("return $('.tblResult').find('tbody tr').size();");	
						int numOfTestStepsPresentForSelectedTestCase = (int)numOfTestStepsPresentForSelectedTestCaseLongValue;


						if(numOfTestStepsPresentForSelectedTestCase>limitForTestStepsPerPage)
						{//Aishwarya---verification of Next Link isdisplayed or not is required

							if(getElement("TestingPageMyActivityTestStepsTableNextLink_Id").isEnabled())
							{	Thread.sleep(1000);
							getElement("TestingPageMyActivityTestStepsTableNextLink_Id").click();
							Thread.sleep(1000);
							APP_LOGS.debug("Test Steps Next Previous Link Functionality  Worked...'Next 10 ' Link Of Test Steps under Test Cases Selection is enabled when there is more that 10 steps available and clicked on ' Next 10 ' link successfully  ( Pass )   .");
							comments=comments+"Test Steps Next Previous Link Functionality Worked ...'Next 10 ' Link Of Test Steps under Test Cases Selection is enabled when there is more that 10 steps available and clicked on ' Next 10 ' link successfully  ( Pass )   .";
							}
							else
							{	fail=true;
							assertTrue(false);
							APP_LOGS.debug("Test Steps Next Previous Link Functionality failure... 'Next 10 ' Link Of Test Steps under Test Cases Selection is disabled even there is more that 10 steps available ( Fail)  .");
							comments=comments+"Test Steps Next Previous Link Functionality failure... 'Next 10 ' Link Of Test Steps under Test Cases Selection is disabled even there is more that 10 steps available ( Fail)  .";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next10LinkDisabledFailure");

							}

						}


					}
					catch(Throwable t)
					{
						t.printStackTrace();
						fail=true;
						assertTrue(false);
						APP_LOGS.debug(" Some other Exception in To Verify TestSteps Next Prev Link   (Fail)  .");
						comments=comments+"  Some other Exception in To Verify TestSteps Next Prev Link   (Fail)  .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTestStepsNextPrevLinkFailure");

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

			}


			closeBrowser();
		}
		else 
		{

			APP_LOGS.debug("Login Not Successful");

		}




	}



	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
		{
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPassed=false;
			TestUtil.reportDataSetResult(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(testingPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPassed=false;
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
	public void reportTestResult() throws Exception{
		if(isTestPassed)
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(testingPageSuiteXls, "Test Cases", TestUtil.getRowNum(testingPageSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}



	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(testingPageSuiteXls, this.getClass().getSimpleName()) ;
	}




	/*private boolean isElementExists(String key)
	{
		try{
			eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
			return true;
		}catch(Throwable t)
		{
			return false;
		}
	}*/


	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName1,String testStepName2,String testerRole, String action)
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

					APP_LOGS.debug("Verification of Project Name , Version ,Test Pass and  Role of Tester in My Activities . ");
					if ((projectCell.equals(project)) && (versionCell.equals(version)) && (testPassCell.equals(testPass)) && (roleCell.equals(testerRole)))
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
			APP_LOGS.debug("project Not FOund in My Activity");
			return false;	

		}

		catch(Throwable t)
		{
			fail=true;
			APP_LOGS.debug("My ACtivity Grid not visible");
			comments+="My ACtivity Grid not visible.";

			//TestUtil.takeScreenShot(this.getClass().getSimpleName(),"MyActivityGridNotVisible");		  
			return false;
		}


	}




	public void createTestStepsToVerifyTestStepsNextPrevLink(String group, String portfolio, String project, String version, String testPassName, 
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

			//getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults+t); 

			List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
			int numOfTestCases = TestCaseSelectionNames.size();
			for(int i = 0; i<numOfTestCases;i++)
			{
				if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
				{
					getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
					break;
				}
			}

			for(int t=1;t<=14;t++)
			{
				Thread.sleep(1000);
				String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+t+"')";
				eventfiringdriver.executeScript(testStepDetails);

				getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults+t); 


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
				getObject("ConfigUserSettingEnvironmentOKButton").click();



			}
			APP_LOGS.debug("Test Pass Name :" +testStepName + " with " + testCasesToBeSelected +"having  Test Steps each Created Successfully   .");
			comments+="Test Pass Name :" +testStepName + " with " + testCasesToBeSelected + "having  Test Steps each Created Successfully (Pass) .";

		}	


		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			fail=true;
			assertTrue(false);

		}


	}


}


