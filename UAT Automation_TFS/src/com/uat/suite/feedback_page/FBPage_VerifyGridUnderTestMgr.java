package com.uat.suite.feedback_page;

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
import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class FBPage_VerifyGridUnderTestMgr extends TestSuiteBase {

	static int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPassed=true;
	int counter=0;
	static boolean isLoginSuccess=false;


	String comments;
	String runmodes[]=null;

	ArrayList<Credentials> version_Lead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;

	ArrayList<String> testStepExecutionResultOnTestingPage;
	ArrayList<String> testStepExecutionResultInTestStepFeedbackGrid;
	ArrayList<String> testStepNameInGrid;
	ArrayList<String>associateTestCaseNameInGrid;

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

	String testStepExpectedResultGivenByTester="Creation should be successful Of Test Step : Feedback Page Grid Verification -Test Step_";
	String testStepActualResultExpectedByTester="Successful Creation Of Test Step : Feedback Page Grid Verification -Test Step_";

	String projectNameOnReceivedFeedbackPopUp;
	String testPassNameOnReceivedFeedbackpopUp;
	String testcaseNameOnReceivedFeedbackPopUp;
	String testStepNameOnReceivedFeedbackPopUp;
	String resultStatusOnReceivedFeedbackpopUp;
	String actualResultOnReceivedFeedbackPopUp;
	String expectedResultOnReceivedFeedbackPopUp;

	int noOfDropdownOnFeedbackPage =5;
	int totalTestStepsToBeCreated;


	// Runmode of test case in a suite

	@BeforeTest
	public void checkTestSkip(){


		if(!TestUtil.isTestCaseRunnable( feedbackPageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(feedbackPageSuiteXls, this.getClass().getSimpleName());

		version_Lead=new ArrayList<Credentials>();
		test_Manager=new ArrayList<Credentials>();
		tester=new ArrayList<Credentials>();

		testStepExecutionResultOnTestingPage=new ArrayList<String>();
		testStepNameInGrid=new ArrayList<String>();
		associateTestCaseNameInGrid=new ArrayList<String>();
		testStepExecutionResultInTestStepFeedbackGrid=new ArrayList<String>();


	}



	@Test(dataProvider="getTestData")
	public void feedbackPage_GridVerificationUnderTestManager (String role,String groupName,String portfolioName,String projectName, 
			String version,String endMonth, String endYear, String endDate, String versionLead ,String testPassName, 
			String testCaseName, String testStepName, String numberOfTestStepsToBeCreated,String expectedTestStepResults,String testManager,String testers,String addRole,String providedFeedbackForTestStepByTester) throws Exception
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


		totalTestStepsToBeCreated = Integer.parseInt(numberOfTestStepsToBeCreated);	

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




				if (!createProject(groupName, portfolioName, projectName, version, endMonth, endYear, endDate, version_Lead.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
					comments="Project Creation Unsuccessful(Fail) by "+role+"  . ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+" . ");
					throw new SkipException("Project Creation Unsuccessfull");
				}
				APP_LOGS.debug(" User "+ role +" created PROJECT : "+projectName+" with Version Lead :"+version_Lead.get(0).username );



				//APP_LOGS.debug("Creating Test Pass :" + testPassName+ " with Test Cases : " + tp4TestCase1 + " with their respective Test Steps  ." );

				if (!createTestPass(groupName, portfolioName, projectName, version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}

				if (!createTester(groupName, portfolioName, projectName, version, testPassName, tester.get(0).username,addRole,addRole)) 
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}



				if (!createTestCase(groupName,portfolioName,projectName,version,testPassName,testCaseName))
				{	
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
					APP_LOGS.debug("Test Case Creation Unsuccessfull");
					comments="Test Case Creation Unsuccessful(Fail)   ." ;
					throw new SkipException("Test Case Creation Unsuccessfull");
				}


				testStepsCreationToVerifyFeedbackPageGrid(groupName,portfolioName,projectName,version,testPassName,testStepName,expectedTestStepResults,testCaseName, addRole);



				closeBrowser();




				// Log in with Tester to perform Testing on Project :Feedback Page Grid Verification

				APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to perform Testing on Project :" + projectName  );

				openBrowser();

				if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
				{




					APP_LOGS.debug("Performing Testing On Test Pass : "+ testPassName +" to verify Provide Feedback Link ");

					if (!searchTestPassAndPerformTesting(projectName, version,testPassName,testStepName,addRole,"Begin Testing"))
					{	          
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "BeginTestingClickFailure");
						comments="Begin Testing Click Failure For "+testPassName +" ( Fail )";
						APP_LOGS.debug("Begin Testing Click Failure For "+testPassName +" (Fail ) ");
						throw new SkipException("Begin Testing Click Failure For "+testPassName);

					}

					for(int i=1;i<=totalTestStepsToBeCreated;i++)
					{	
						if(i==4 || i == 5)
						{
							getElement("TestingPageMyActivity_ActualResult_Id").sendKeys(testStepActualResultExpectedByTester+i);
							//TestingPage_failRadioButton_Id
							getElement("TestingPage_failRadioButton_Id").click();
							getElement("TestingPage_saveButton_Id").click();
							testStepExecutionResultOnTestingPage.add("Fail");
							continue;

						}

						getElement("TestingPageMyActivity_ActualResult_Id").sendKeys(testStepActualResultExpectedByTester+i);

						getElement("TestingPage_passRadioButton_Id").click();

						getElement("TestingPage_saveButton_Id").click();

						testStepExecutionResultOnTestingPage.add("Pass");
					}

					String provideFeedbackForTestPass="Acceptance Testing Of Project- \"Feedback Page Grid Verification\" is done Very SATISFACTORILY ";

					String feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+provideFeedbackForTestPass+"')";
					eventfiringdriver.executeScript(feedbackDetails);
					getObject("TestingPage_rating_verySatisfiedRadioBtn").click();
					//getObject("TestingPage_feedback_IframeNameTextArea").sendKeys("Acceptance Testing Of Project- ' Feedback Page Grid Verification ' is done Very SATISFACTORILY ");
					getObject("TestingPage_feedbackRating_SaveButton").click();
					Thread.sleep(1000);
					getObject("TestingPage_returnToHomeButton").click();

					Thread.sleep(2000);
					getElement("UAT_Feedback_Id").click();
					Thread.sleep(500);

					getObject("FeedbackPage_TestStepFeedbackSelection").click();

					Select projectSelectionOnTestStepFeedbackTab = new Select(getElement("FeedbackPage_TestStepFeedbackProjectDropdown_Id"));
					List<WebElement> selectProject = getElement("FeedbackPage_TestStepFeedbackProjectDropdown_Id").findElements(By.tagName("option"));



					for(int i=1;i<=selectProject.size();i++)
					{	
						if(projectName.equals(selectProject.get(i).getAttribute("title")))
						{	
							projectSelectionOnTestStepFeedbackTab.selectByIndex(i);

							break;
						}

					}

					Select versionSelectionOnTestStepFeedbackTab= new Select(getElement("FeedbackPage_TestStepFeedbackVersionDropdown_Id"));
					versionSelectionOnTestStepFeedbackTab.selectByVisibleText(version);

					Select testPassSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackTestPassDropdown_Id"));
					List<WebElement> selectTestPass = getElement("FeedbackPage_TestStepFeedbackTestPassDropdown_Id").findElements(By.tagName("option"));

					for(int i=1;i<=selectTestPass.size();i++)
					{
						if(testPassName.equals(selectTestPass.get(i).getAttribute("title")))
						{
							testPassSelectionOnTestStepFeedbackTab.selectByIndex(i);
							break;

						}

					}



					Select testerSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackTesterDropdown_Id"));
					//testerSelectionOnTestStepFeedbackTab.selectByVisibleText(tester.get(0).username.replace(".", " "));

					List<WebElement> selectTester = getElement("FeedbackPage_TestStepFeedbackTesterDropdown_Id").findElements(By.tagName("option"));

					for(int i=0;i<selectTester.size();i++)
					{
						if(tester.get(0).username.replace(".", " ").equalsIgnoreCase(selectTester.get(i).getAttribute("title")))
						{
							testerSelectionOnTestStepFeedbackTab.selectByIndex(i);
							break;

						}

					}



					Select roleSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackRoleDropdown_Id"));
					List<WebElement> selectRole = getElement("FeedbackPage_TestStepFeedbackRoleDropdown_Id").findElements(By.tagName("option"));

					for(int i=1;i<=selectRole.size();i++)
					{
						if(addRole.equals(selectRole.get(i).getAttribute("title")))
						{
							roleSelectionOnTestStepFeedbackTab.selectByIndex(i);

							break;
						}
					}


					getElement("FeedbackPage_TestStepFeedbackGoButton_Id").click();


					//The 'Feedback Details' Grid should open which will have the following columns --> Test Id, Associated Test Case name, Test Step name, Result & Action column. 


					Thread.sleep(1500);

					if(getObject("Feedbackpage_TestStepFeedbackGridTestIdColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridTestId_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridTestStepColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridTestStepName_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridAssocaiteTestCaseColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridAssociateTestCaseName_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridResultColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridResultColumn_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridActionColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridActionColumn_Text")))

					{
						APP_LOGS.debug("Test Id, Associated Test Case name, Test Step name, Result & Action column.are showing properly in Test Step Feedback Grid  (Pass) .");
						comments+="\nTest Id, Associated Test Case name, Test Step name, Result & Action column.are showing properly in Test Step Feedback Grid  (Pass) .";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Id, Associated Test Case name, Test Step name, Result & Action column.are not showing properly in Test Step Feedback Grid  (Fail) .");
						comments+="\nTest Id, Associated Test Case name, Test Step name, Result & Action column.are not showing properly in Test Step Feedback Grid  (Fail) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Column Name in TestStepFeedbackGrid are Not showing ");
					}



					//Verification Of Test Step Name,Associate Test Case Name ,Status Of Test Step are assigned to respective user are showing properly or not

					List<WebElement>  rowCountOnTestStepFeedbackPage=getObject("FeedbackPage_TestStepFeedbackGridSize").findElements(By.tagName("tbody"));

					int totalRowCountOnFeedbackPageGrid=0;

					for(int i=1;i<=totalTestStepsToBeCreated;i++)
					{
						String testStepNamePresentInGrid=getObject("FeedbackPage_TestStepFeedbackGridTestStepNameXpath1","FeedbackPage_TestStepFeedbackGridTestStepNameXpath2",i).getAttribute("title");
						testStepNameInGrid.add(testStepNamePresentInGrid);
						String associateTestCase=getObject("FeedbackPage_TestStepFeedbackGridAssociateTestCaseNameXpath1","FeedbackPage_TestStepFeedbackGridAssociateTestCaseNameXpath2",i).getAttribute("title");
						associateTestCaseNameInGrid.add(associateTestCase);
						String testStepPassFailResult=getObject("FeedbackPage_TestStepFeedbackGridResultStatusXpath1","FeedbackPage_TestStepFeedbackGridResultStatusXpath2",i).getText();
						testStepExecutionResultInTestStepFeedbackGrid.add(testStepPassFailResult);




						if(testStepNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepName+(totalRowCountOnFeedbackPageGrid+1)) && associateTestCaseNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testCaseName) && testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepExecutionResultOnTestingPage.get(totalRowCountOnFeedbackPageGrid)))
						{
							counter++;
							getObject("FeedbackPage_ProvideFeedbackPerTestStepXpath1","FeedbackPage_ProvideFeedbackPerTestStepXpath2",i).click();
							Thread.sleep(1000);
							providedFeedbackForTestStepByTester= "Very Satisfied with Test Step Result ";
							feedbackDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+providedFeedbackForTestStepByTester+"')";

							eventfiringdriver.executeScript(feedbackDetails);
							getObject("TestingPage_feedbackRating_SaveButton").click();
							getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();
						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Test Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .");
							comments+="\nTest Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsWithAssociateTestCaseAndResultNotShowingProperlyOnGrid");
						}

						totalRowCountOnFeedbackPageGrid++;

						if(rowCountOnTestStepFeedbackPage.size()==10 && i==10)
						{	

							APP_LOGS.debug("Tester is able to see  only 10 Test Steps in Grid  on one paging Successfully ( Pass )");
							comments+="\nTester is able to see  only 10 Test Steps in Grid  on one paging Successfully ( Pass )";

							boolean isNextLinkDisabled= (boolean) eventfiringdriver.executeScript("return $('#next').attr('disabled')");
							boolean isPreviousLinkDisabled= (boolean) eventfiringdriver.executeScript("return $('#previous').attr('disabled')");


							if(!(isPreviousLinkDisabled &&  isNextLinkDisabled  ))
							{	

								getElement("FeedbackPage_TestStepFeedbackGridNextLink_Id").click();
								APP_LOGS.debug(" Next Link is Enabled As Expected  (Pass)");
								APP_LOGS.debug("there are more than  10 Test Steps are Available on Feedback Page Grid...So Clicking On Next Link to  Verify Further Test Steps  ");
								i=0;
								continue;
							}
							else
							{
								APP_LOGS.debug("there are less than 10 or Exact 10 Test Cases are Available on Feedback Page Grid  (Pass )");
							}
						}



						if(totalRowCountOnFeedbackPageGrid==totalTestStepsToBeCreated)
						{
							break;// to exit from For Loop 
						}
					}


					if(counter ==totalRowCountOnFeedbackPageGrid)
					{
						APP_LOGS.debug("The Grid is showing  All test Steps with associate Test Cases and Result Of Selected Test Pass For The Selected Tester Successfully ( Pass )");

						comments+="\nThe Grid is showing  All test Steps with associate Test Cases and Result Of Selected Test Pass For The Selected Tester Successfully ( Pass )";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .");
						comments+="\nTest Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsWithAssociateTestCaseAndResultNotShowingProperlyOnGrid");
					}

					counter=0;//Reinitializing the Counter to 0

					closeBrowser();
				}
				else
				{

					APP_LOGS.debug("Tester Login Not Successful");

				}


				//Clearing indexing values from Array List (Empty Array List)

				testStepNameInGrid.clear();
				associateTestCaseNameInGrid.clear();
				testStepExecutionResultInTestStepFeedbackGrid.clear();





				APP_LOGS.debug("__________________________________________________________________________________________________________________________________________________");


				APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+test_Manager.get(0).username + " to verify Grid Of Feedback Tab ");

				openBrowser();



				if (login(test_Manager.get(0).username ,test_Manager.get(0).password, "Test Manager")) 
				{

					APP_LOGS.debug("Verifying Feedback Page i.e.Test Pass Feedback and Test Step Feedback when login As Test Manager");

					getElement("UAT_Feedback_Id").click();
					Thread.sleep(500);
					getObject("FeedbackPage_TestStepFeedbackSelection").click();
					Select projectSelectionOnTestStepFeedbackTab = new Select(getElement("FeedbackPage_TestStepFeedbackProjectDropdown_Id"));
					List<WebElement> selectProject = getElement("FeedbackPage_TestStepFeedbackProjectDropdown_Id").findElements(By.tagName("option"));
					for(int i=1;i<=selectProject.size();i++)
					{	
						if(projectName.equals(selectProject.get(i).getAttribute("title")))
						{	

							projectSelectionOnTestStepFeedbackTab.selectByIndex(i);
							counter++;

							break;
						}

					}
					Select versionSelectionOnTestStepFeedbackTab= new Select(getElement("FeedbackPage_TestStepFeedbackVersionDropdown_Id"));
					versionSelectionOnTestStepFeedbackTab.selectByVisibleText(version);
					counter++;
					Select testPassSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackTestPassDropdown_Id"));
					List<WebElement> selectTestPass = getElement("FeedbackPage_TestStepFeedbackTestPassDropdown_Id").findElements(By.tagName("option"));

					for(int i=1;i<=selectTestPass.size();i++)
					{
						if(testPassName.equals(selectTestPass.get(i).getAttribute("title")))
						{	

							testPassSelectionOnTestStepFeedbackTab.selectByIndex(i);
							counter++;
							break;

						}

					}

					Select testerSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackTesterDropdown_Id"));
					//testerSelectionOnTestStepFeedbackTab.selectByVisibleText(tester.get(0).username.replace(".", " "));

					List<WebElement> selectTester = getElement("FeedbackPage_TestStepFeedbackTesterDropdown_Id").findElements(By.tagName("option"));

					for(int i=0;i<selectTester.size();i++)
					{
						if(tester.get(0).username.replace(".", " ").equalsIgnoreCase(selectTester.get(i).getAttribute("title")))
						{	

							testerSelectionOnTestStepFeedbackTab.selectByIndex(i);
							counter++;
							break;

						}

					}



					Select roleSelectionOnTestStepFeedbackTab=new Select(getElement("FeedbackPage_TestStepFeedbackRoleDropdown_Id"));
					List<WebElement> selectRole = getElement("FeedbackPage_TestStepFeedbackRoleDropdown_Id").findElements(By.tagName("option"));

					for(int i=1;i<=selectRole.size();i++)
					{
						if(addRole.equals(selectRole.get(i).getAttribute("title")))
						{	

							roleSelectionOnTestStepFeedbackTab.selectByIndex(i);
							counter++;

							break;
						}
					}


					if(counter==noOfDropdownOnFeedbackPage)
					{
						APP_LOGS.debug("\nExpected Project Name : "+ projectName +" ,  Version Name  : "+ version + " , TestPass name  : "+testPassName +" ,Tester Name : " + tester.get(0).username.replace(".", " ") + "  and Role : "+addRole +" is Selected From Dropdwon Of Feedback Page ( Pass )");
						comments+="\nExpected Project Name : "+ projectName +" , Version Name  : "+ version +" ,TestPass name  : "+testPassName +" ,Tester Name : " + tester.get(0).username.replace(".", " ") + " and Role : "+addRole +" is Selected From Dropdwon Of Feedback Page ( Pass )";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Expected Project Name : "+ projectName +"  ,Version Name  : "+ version +" ,TestPass name  : "+testPassName +" ,Tester Name : " + tester.get(0).username.replace(".", " ") + " and Role : "+addRole +" is not  Selected From Dropdwon Of Feedback Page ( Fail )");
						comments+="\nExpected Project Name : "+ projectName +"  ,Version Name  : "+ version +" ,TestPass name  : "+testPassName +" ,Tester Name : " + tester.get(0).username.replace(".", " ") + " and Role : "+addRole +" is not  Selected From Dropdwon Of Feedback Page ( Fail )";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExpectedDropdownValuesAreNotSelectedOrNotPresentInDropDownListOfFeedbackPage");
					}

					counter=0;//Reinitializing the Counter to 0

					getElement("FeedbackPage_TestStepFeedbackGoButton_Id").click();



					//The 'Feedback Details' Grid should open which should have the following columns --> Test Id, Associated Test Case name, Test Step name, Result & Action column. 


					Thread.sleep(1500);

					if(getObject("Feedbackpage_TestStepFeedbackGridTestIdColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridTestId_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridTestStepColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridTestStepName_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridAssocaiteTestCaseColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridAssociateTestCaseName_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridResultColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridResultColumn_Text"))
							&&getObject("Feedbackpage_TestStepFeedbackGridActionColumn").getText().equals(resourceFileConversion.getProperty("Feedbackpage_TestStepFeedbackGridActionColumn_Text")))

					{
						APP_LOGS.debug("Test Id, Associated Test Case name, Test Step name, Result & Action column.are showing properly in Test Step Feedback Grid For Test Manager (Pass) .");
						comments+="\nTest Id, Associated Test Case name, Test Step name, Result & Action column.are showing properly in Test Step Feedback Grid  For Test Manager (Pass) .";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Id, Associated Test Case name, Test Step name, Result & Action column.are not showing properly in Test Step Feedback Grid For Test Manager  (Fail) .");
						comments+="\nTest Id, Associated Test Case name, Test Step name, Result & Action column.are not showing properly in Test Step Feedback Grid For Test Manager  (Fail) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Column Names in TestStepFeedbackGrid are Not showing ");
					}



					//Verification Of Test Step Name,Associate Test Case Name ,Status Of Test Step are assigned to respective user are showing properly or not
					APP_LOGS.debug("Verifying Test Step Name,Associate Test Case Name ,Status Of Test Step are assigned to Test Manager : "+test_Manager.get(0).username + "  are showing properly Or Not");
					List<WebElement>  rowCountOnTestStepFeedbackPage=getObject("FeedbackPage_TestStepFeedbackGridSize").findElements(By.tagName("tbody"));

					int totalRowCountOnFeedbackPageGrid=0;


					for(int i=1;i<=totalTestStepsToBeCreated;i++)
					{
						String testStepNamePresentInGrid=getObject("FeedbackPage_TestStepFeedbackGridTestStepNameXpath1","FeedbackPage_TestStepFeedbackGridTestStepNameXpath2",i).getText();
						testStepNameInGrid.add(testStepNamePresentInGrid);
						String associateTestCase=getObject("FeedbackPage_TestStepFeedbackGridAssociateTestCaseNameXpath1","FeedbackPage_TestStepFeedbackGridAssociateTestCaseNameXpath2",i).getAttribute("title");
						associateTestCaseNameInGrid.add(associateTestCase);
						String testStepPassFailResult=getObject("FeedbackPage_TestStepFeedbackGridResultStatusXpath1","FeedbackPage_TestStepFeedbackGridResultStatusXpath2",i).getText();
						testStepExecutionResultInTestStepFeedbackGrid.add(testStepPassFailResult);




						if(testStepNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepName+(totalRowCountOnFeedbackPageGrid+1)) && associateTestCaseNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testCaseName)&& testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepExecutionResultOnTestingPage.get(totalRowCountOnFeedbackPageGrid)))
						{
							counter++;
							getObject("FeedbackPage_ProvideFeedbackPerTestStepXpath1","FeedbackPage_ProvideFeedbackPerTestStepXpath2",i).click();

							projectNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpProjectName").getText();
							testPassNameOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestPassName").getText();
							testcaseNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestCaseName").getText();
							testStepNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestStepName").getText();
							resultStatusOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpResultStatus").getText();
							actualResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpActualResult").getText();
							expectedResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpExpectedResult").getText();


							if(	projectNameOnReceivedFeedbackPopUp.equals(projectName) 
									&& testcaseNameOnReceivedFeedbackPopUp.equals(testCaseName)
									&& testStepNameOnReceivedFeedbackPopUp.equals(testStepName+(totalRowCountOnFeedbackPageGrid+1))
									&& resultStatusOnReceivedFeedbackpopUp.equals(testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid))
									&& actualResultOnReceivedFeedbackPopUp.equals(testStepActualResultExpectedByTester+(totalRowCountOnFeedbackPageGrid+1))
									&& expectedResultOnReceivedFeedbackPopUp.equals(testStepExpectedResultGivenByTester+(totalRowCountOnFeedbackPageGrid+1))
									&& getObject("FeedbackPage_ReceivedFeedbackPopUpPreviousLink").getText().equals("Previous")
									&& getObject("FeedbackPage_ReceivedFeedbackPopUpNextLink").getText().equals("Next")
									&& testPassNameOnReceivedFeedbackpopUp.equals(testPassName))
								//havent verified Test Step Id As it's Dyanamic
							{

								APP_LOGS.debug("Received Feedback Popup is showing required Fields i.e. Project Name, Test Pass Name , Test Case Name,, Test Step Name, Result, Expected Result,Previous Link, Next Link, and Actual Result of Test Step Successfully  ( Pass )");
								//	comments+="\nReceived Feedback Popup is showing required Fields i.e. Project Name, Test Pass Name , Test Case Name,, Test Step Name, Previous Link,Next Link, Result, Expected Result, and Actual Result of Test Step Successfully  ( Pass )";



							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Received Feedback Popup is Not showing required Fields i.e. Project Name, Test Pass Name , Test Case Name, Previous Link,Next Link, Test Step Name, Result, Expected Result, and Actual Result of Test Step Properly ( Fail ) ");
								comments+="\nReceived Feedback Popup is showing required Fields i.e. Project Name, Test Pass Name , Test Case Name, Previous Link, Next Link, Test Step Name, Result, Expected Result, and Actual Result of Test Step Properly  ( Fail )" ;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SummaryFieldsAboutProjectOnReceivedFeedbackPopUpNotShowingProperly"+i);
							}



							Thread.sleep(1000);
							getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();
						}
						else
						{
							fail=true;
							assertTrue(false);
							APP_LOGS.debug("Test Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid For Test Manager ( Fail ) .");
							comments+="\nTest Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid For Test Manager ( Fail ) .";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsWithAssociateTestCaseAndResultNotShowingProperlyOnGrid");
						}

						totalRowCountOnFeedbackPageGrid++;

						if(rowCountOnTestStepFeedbackPage.size()==10 && i==10)
						{	

							APP_LOGS.debug("Test Manager is able to see  only 10 Test Steps in Grid  on one paging Successfully ( Pass )");
							comments+="\nTest Manager is able to see  only 10 Test Steps in Grid  on one paging Successfully ( Pass )";

							boolean isNextLinkDisabled= (boolean) eventfiringdriver.executeScript("return $('#next').attr('disabled')");
							boolean isPreviousLinkDisabled= (boolean) eventfiringdriver.executeScript("return $('#previous').attr('disabled')");


							if(!(isPreviousLinkDisabled &&  isNextLinkDisabled  ))
							{	

								getElement("FeedbackPage_TestStepFeedbackGridNextLink_Id").click();
								APP_LOGS.debug(" Next Link is enabled As Expected  (Pass)");
								APP_LOGS.debug("There are more than  10 Test Steps are Available on Feedback Page Grid...So Clicking On Next Link to  Verify Further Test Steps  ");
								i=0;
								continue;
							}
							else
							{
								APP_LOGS.debug("There are less than 10 or Exact 10 Test Cases are Available on Feedback Page Grid  (Pass )");
							}
						}



						if(totalRowCountOnFeedbackPageGrid==totalTestStepsToBeCreated)
						{
							break;// to exit from For Loop 
						}
					}


					if(counter ==totalRowCountOnFeedbackPageGrid)
					{
						APP_LOGS.debug("Test Manager is able to see All test Steps with associate Test Cases and Result Of Selected Test Pass For The Selected Tester Successfully ( Pass )");

						comments+="\nTest Manager is able to see All test Steps with associate Test Cases and Result Of Selected Test Pass For The Selected Tester Successfully ( Pass )";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Test Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .");
						comments+="\nTest Step with associate Test Cases and Result Of Selected Test Pass For The Selected Tester is not showing properly in Grid ( Fail ) .";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsWithAssociateTestCaseAndResultNotShowingProperlyOnGrid");
					}







					getObject("FeedbackPage_TestStepDeedbackGridPreviousLink").click();

					totalRowCountOnFeedbackPageGrid=0;	
					if(testStepNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepName+(totalRowCountOnFeedbackPageGrid+1)) && associateTestCaseNameInGrid.get(totalRowCountOnFeedbackPageGrid).equals(testCaseName)&&testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid).equals(testStepExecutionResultOnTestingPage.get(totalRowCountOnFeedbackPageGrid)))
					{
						getObject("FeedbackPage_ProvideFeedbackOn1stTestStepOfGrid").click();
						int i;

						for(i=1;i<=totalTestStepsToBeCreated;i++)
						{

							//getObject("FeedbackPage_ProvideFeedbackPerTestStepXpath1","FeedbackPage_ProvideFeedbackPerTestStepXpath2",i).click();


							projectNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpProjectName").getText();
							testPassNameOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestPassName").getText();
							testcaseNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestCaseName").getText();
							testStepNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestStepName").getText();
							resultStatusOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpResultStatus").getText();
							actualResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpActualResult").getText();
							expectedResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpExpectedResult").getText();


							if(	projectNameOnReceivedFeedbackPopUp.equals(projectName) 
									&& testcaseNameOnReceivedFeedbackPopUp.equals(testCaseName)
									&& testStepNameOnReceivedFeedbackPopUp.equals(testStepName+(totalRowCountOnFeedbackPageGrid+1))
									&& resultStatusOnReceivedFeedbackpopUp.equals(testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid))
									&& actualResultOnReceivedFeedbackPopUp.equals(testStepActualResultExpectedByTester+(totalRowCountOnFeedbackPageGrid+1))
									&& expectedResultOnReceivedFeedbackPopUp.equals(testStepExpectedResultGivenByTester+(totalRowCountOnFeedbackPageGrid+1))
									&&testPassNameOnReceivedFeedbackpopUp.equals(testPassName)
									&& getObject("FeedbackPage_ReceivedFeedbackPopUpPreviousLink").getText().equals("Previous")
									&& getObject("FeedbackPage_ReceivedFeedbackPopUpNextLink").getText().equals("Next"))

								//									//havent verified Test Step Id As it's Dyanamic
							{

								

								totalRowCountOnFeedbackPageGrid++;
								String isNextLinkEnabled= getObject("FeedbackPage_ReceivedFeedbackPopUpNextLink").getAttribute("style");

								if(isNextLinkEnabled.contains("cursor: pointer"))

								{	
									APP_LOGS.debug("As it is not a last Test Step of Test Pass for this Tester,Clicking On Next Link and then Rich Text Box is showing the information about next Test Step ( Pass )");
									getObject("FeedbackPage_ReceivedFeedbackPopUpNextLink").click();

								}
								else
								{
									if(!(isNextLinkEnabled.contains("cursor: pointer")))
									{
										APP_LOGS.debug("After continue clicking on Next Link of Received Feedback PopUp.. As it reached to last Test Step of Test Pass therefore Received Feedback PopUp is not showing the information about next Test Step as Expected  ( Next Link Disabled  ) ( Pass )");
										comments+="\n After continue clicking on Next Link of Received Feedback PopUp.. As it reached to last Test Step of Test Pass therefore Received Feedback PopUp is not showing the information about next Test Step as Expected  ( Next Link Disabled  ) ( Pass )";
										break;
									}

								}
								
								

								//APP_LOGS.debug("Received Pop Up Text Information is showing properly with Project Name, Test Pass Name , Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result ( Pass )");
								//	comments+="\nReceived Feedback Popup is showing required Fields i.e. Project Name, Test Pass Name , Test Case Name,, Test Step Name, Previous Link,Next Link, Result, Expected Result, and Actual Result of Test Step Successfully  ( Pass )";



							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Received Pop Up Text Information is not  showing properly with Project Name, Test Pass Name , Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result (Fail )");
								comments+="\nReceived Pop Up Text Information is not  showing properly with Project Name, Test Pass Name , Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result (Fail )";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ReceivedPopUpTextInformationNotShowingProperly"+i);
							}


						}





						//getObject("FeedbackPage_ProvideFeedbackPerTestStepXpath1","FeedbackPage_ProvideFeedbackPerTestStepXpath2",i).click();



						//In this loop we are verifying previous link on received feedback pop Up in reverse direction

						for(int j =totalRowCountOnFeedbackPageGrid;j<=totalTestStepsToBeCreated;j--)
						{
							projectNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpProjectName").getText();
							testPassNameOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestPassName").getText();
							testcaseNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestCaseName").getText();
							testStepNameOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpTestStepName").getText();
							resultStatusOnReceivedFeedbackpopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpResultStatus").getText();
							actualResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpActualResult").getText();
							expectedResultOnReceivedFeedbackPopUp=getObject("FeedbackPage_ReceivedFeedbackPopUpExpectedResult").getText();


							if(	projectNameOnReceivedFeedbackPopUp.equals(projectName) 
									&& testcaseNameOnReceivedFeedbackPopUp.equals(testCaseName)
									&& testStepNameOnReceivedFeedbackPopUp.equals(testStepName+(totalRowCountOnFeedbackPageGrid))
									&& resultStatusOnReceivedFeedbackpopUp.equals(testStepExecutionResultInTestStepFeedbackGrid.get(totalRowCountOnFeedbackPageGrid-1)) //It is array list ,so indexing start with 0  therefore I kept (totalRowCountOnFeedbackPageGrid-1) i.e. ( 15-1 )
									&& actualResultOnReceivedFeedbackPopUp.equals(testStepActualResultExpectedByTester+(totalRowCountOnFeedbackPageGrid))
									&& expectedResultOnReceivedFeedbackPopUp.equals(testStepExpectedResultGivenByTester+(totalRowCountOnFeedbackPageGrid))
									&& testPassNameOnReceivedFeedbackpopUp.equals(testPassName)
									)

								//havent verified Test Step Id As it's Dyanamic
							{



								APP_LOGS.debug("Received Pop Up Text Information is showing properly with Project Name, Test Pass Name ,Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result ( Pass )");
								//	comments+="\nReceived Feedback Popup is showing required Fields i.e. Project Name, Test Pass Name , Test Case Name,, Test Step Name, Previous Link,Next Link, Result, Expected Result, and Actual Result of Test Step Successfully  ( Pass )";



							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Received Pop Up Text Information is not showing properly with Project Name, Test Pass Name , Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result (Fail )");
								comments+="\nReceived Pop Up Text Information is not showing properly with Project Name, Test Pass Name , Test Case Name,, Test Step Name, Status Result, Expected Result, and Actual Result (Fail )";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ReceivedPopUpTextInformationNotShowingProperly");
							}

							totalRowCountOnFeedbackPageGrid--;
							String isPreviousLinkEnabled= getObject("FeedbackPage_ReceivedFeedbackPopUpPreviousLink").getAttribute("style");
							if(isPreviousLinkEnabled.contains("cursor: pointer"))

							{	
								APP_LOGS.debug("As it is not a 1st Test Step of Test Pass for this Tester,Clicking On previous Link and then Rich Text Box is showing the information about previousTest Step ( Pass )");
								getObject("FeedbackPage_ReceivedFeedbackPopUpPreviousLink").click();

							}
							else
							{
								if(!(isPreviousLinkEnabled.contains("cursor: pointer")))
								{	
									APP_LOGS.debug("After continue clicking on previous Link of Received Feedback PopUp  As it reached to First Test Step of Test Pass therefore Received Feedback PopUp is not showing the information about Previous Test Step as Expected  ( Previous Link Disabled  ) ( Pass )");  
									comments+="/n After continue clicking on previous Link of Received Feedback PopUp  As it reached to First Test Step of Test Pass therefore Received Feedback PopUp is not showing the information about Previous Test Step as Expected  ( Previous Link Disabled  ) ( Pass )";  
									break;
								}

							}




						}
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Created Test Step ,Test case and Status Result is not showing on Feedback Page When test manager is logged in to verify Received Feedback ( Fail ) ");
						comments+="\nCreated Test Step ,Test case and Status Result is not showing on Feedback Page When test manager is logged in to verify Received Feedback ( Fail ) ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "createdTSTCandStatusResultNotShowingOnGrid");

					}



					closeBrowser();
				}

				else 
				{

					APP_LOGS.debug("Test Manager Login Not Successful");

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
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPassed=false;
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPassed=false;
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;

	}


	@AfterTest
	public void reportTestResult(){
		if(isTestPassed)
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, "Test Cases", TestUtil.getRowNum(feedbackPageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, "Test Cases", TestUtil.getRowNum(feedbackPageSuiteXls,this.getClass().getSimpleName()), "FAIL");

	}



	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(feedbackPageSuiteXls, this.getClass().getSimpleName()) ;
	}






	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testStepName,String testerRole, String action)
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






	public void testStepsCreationToVerifyFeedbackPageGrid(String group, String portfolio, String project, String version, String testPassName, 
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

			for(int t=1;t<=totalTestStepsToBeCreated;t++)
			{

				Thread.sleep(1000);
				getObject("TestStep_createNewProjectLink").click();
				String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+t+"')";
				eventfiringdriver.executeScript(testStepDetails);

				getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResultGivenByTester+t); 


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
				//getObject("ConfigUserSettingEnvironmentOKButton").click();



			}
			APP_LOGS.debug(" Test Pass : " +"'"+testPassName+ "'" + "  with Test Case : "+"'" + testCasesToBeSelected+"'" + "  along with Test Steps is Created Successfully  (Pass) .");
			comments+="Test Pass : " +"'"+testPassName+ "'" +"  with Test Case : " + "'" + testCasesToBeSelected+"'" + "  along with Test Steps is Created Successfully  (Pass) .";

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
