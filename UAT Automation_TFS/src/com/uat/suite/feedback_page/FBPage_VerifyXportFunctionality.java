package com.uat.suite.feedback_page;

import java.util.ArrayList;
import org.openqa.selenium.By;
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


public class FBPage_VerifyXportFunctionality extends TestSuiteBase {


	static int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPassed=true;
	int counter=0;
	static boolean isLoginSuccess=false;
	ArrayList<Credentials> test_Manager;
	String comments;
	String runmodes[]=null;





	// Runmode of test case in a suite

	@BeforeTest
	public void checkTestSkip(){


		if(!TestUtil.isTestCaseRunnable( feedbackPageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(feedbackPageSuiteXls, this.getClass().getSimpleName());

		test_Manager=new ArrayList<Credentials>();

	}



	@Test(dataProvider="getTestData")
	public void feedbackPage_GridVerificationUnderTestManager (String role ,String testManager) throws Exception
	{
		count++;

		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}


		comments="";

		int testManager_count = Integer.parseInt(testManager);
		test_Manager = getUsers("Test Manager", testManager_count);

		APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
		APP_LOGS.debug("Opening Browser... ");

		openBrowser();
		isLoginSuccess = login(role);

		if(isLoginSuccess)
		{	
			closeBrowser();

			Thread.sleep(1500);
			//APP_LOGS.debug("Opening Browser...Logging In With Tester "+tester.get(0).username + " to perform Testing on Project :" + projectName  );

			openBrowser();

			if (login(test_Manager.get(0).username ,test_Manager.get(0).password, "Test Manager")) 
			{
				try
				{


					APP_LOGS.debug("Verifying Test Pass Feedback Export Functionality On Feedback Page when logged in As Test Manager");

					getElement("UAT_Feedback_Id").click();
					Thread.sleep(500);
					getObject("Feedback_testPassFeedbackTabTitle").click();

					//Verifying activex control
					Boolean result = true;
					try
					{
						eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
						Thread.sleep(2000);
					}
					catch(Throwable t)
					{
						result = false;
					}

					Select projectSelectionOnTestPassFeedbackTab = new Select(getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id"));
					projectSelectionOnTestPassFeedbackTab.selectByIndex(1);

					Select versionSelectionOnTestPassFeedbackTab= new Select(getElement("FeedbackPage_TestPassFeedbackVersionDropdown_Id"));
					versionSelectionOnTestPassFeedbackTab.selectByIndex(0);

					Select testPassSelectionOnTestPassFeedbackTab=new Select(getElement("FeedbackPage_TestPassFeedbackTestPassDropdown_Id"));
					testPassSelectionOnTestPassFeedbackTab.selectByIndex(1);

					Select testerSelectionOnTestPassFeedbackTab=new Select(getElement("FeedbackPage_TestPassFeedbackTesterDropdown_Id"));
					testerSelectionOnTestPassFeedbackTab.selectByIndex(1);

					Select roleSelectionOnTestPassFeedbackTab=new Select(getElement("FeedbackPage_TestPassFeedbackRoleDropdown_Id"));
					roleSelectionOnTestPassFeedbackTab.selectByIndex(1);

					getElement("FeedbackPage_TestPassFeedbackExportButton_Id").click();

					if(result.equals(false))
					{
						//Active x code
						wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
						if(assertTrue(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed()))
						{
							getObject("TestStepCreateNew_TestStepActiveX_Close").click();
							APP_LOGS.debug("ActiveX is disabled and hence cannot export file");
							comments+="ActiveX is disabled and hence cannot export file";
							throw new SkipException("ActiveX is disabled and hence cannot export details");
						}
						else
						{	
							fail=true;
							APP_LOGS.debug("An alert informing the user of disabled activex should be present.");
							comments+="An alert informing the user of disabled activex should be present.";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabled");
							comments+="Fail- Activex is disabled..but the popup not displayed";
						}
					}
					else
					{
						APP_LOGS.debug("As ActiveX controls are active so Test Pass Feedback Details exported successfully  (  PASS )");
						comments+="As ActiveX controls are active so Test Pass Feedback Details exported successfully ( PASS )";
					}

					getObject("ProjectCreateNew_projectMandatoryFieldValidationOkBtn").click();
					closeBrowser();


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

				APP_LOGS.debug("Test Manager Login Not Successful");

			}		

		}
		else 
		{
			fail=true;
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

}
