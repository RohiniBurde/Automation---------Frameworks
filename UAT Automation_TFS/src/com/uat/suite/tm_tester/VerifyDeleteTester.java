package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyDeleteTester extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());	
		if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}

	@Test(dataProvider="getTestData")
	public void testVerifyDeleteTester(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_description, String tester_DeletePopupText) throws Exception
	{
		count++;
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int Tester_count = Integer.parseInt(tester_testerName);
		tester = getUsers("Tester", Tester_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		int stakeholder_count = Integer.parseInt(Stakeholder);
		stakeholder = getUsers("Stakeholder", stakeholder_count);
		
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
					
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try 
			{
				getElement("UAT_testManagement_Id").click();
				
				APP_LOGS.debug("Clicking On Test Management Tab ");
			
				Thread.sleep(500);
			
				APP_LOGS.debug("Creating a project");
				
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
				{
					APP_LOGS.debug("Project Creation fails");
					comments=comments+"Project Creation Fails";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");							
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project is not created successfully");//reports
				}
				
				APP_LOGS.debug("Closing the browser after saving project.");
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser after saving project.");
				openBrowser();
				
				APP_LOGS.debug("Login with version lead");
				if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				{
					//Thread.sleep(1000);
					
					APP_LOGS.debug("Clicking on test management tab.");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(500);

					APP_LOGS.debug("Creating Test Pass.");
					
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						APP_LOGS.debug("Failed to create the Test Pass");
						comments=comments+"Failed to create the Test Pass (FAIL)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					openBrowser();
					
					APP_LOGS.debug("Login with Test Manager");
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						//Thread.sleep(1000);
					//click on testManagement tab
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						
						//Test case 1: User should get message that  'Tester association deleted successfully'.
						APP_LOGS.debug("TEST CASE 1 EXECUTING");
						
						try 
						{
							if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, tester_Role, tester_description, tester_Role))
							{
								APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
								
								comments = comments+ "Fail Occurred: Popup text is Not as expected while saving a Tester... ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in create tester");
								throw new SkipException("Error in creating tester");
							}
							
							//getObject("Testers_viewAllLink").click();
							
							String testerRoleNameInGrid = getObject("TestersViewAll_testerRoleNameInGrid").getText();
							
							if (!assertTrue(testerRoleNameInGrid.equals(tester_Role))) 
							{
								fail = true;
								
								APP_LOGS.debug("Role Has not been displayed in Grid...");
								
								comments ="Fail Occurred: Role Has not been displayed in Grid... ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role Has not been displayed in Grid");	
							}
							
							getObject("TestersViewAll_testerRoleDeleteImgInGrid").click();
						
							//Thread.sleep(1000);
							String deleteConfirmationPopuptext = waitForElementVisibility("TestersViewAll_deleteConfirmationText_Id",10).getText();
							if(compareStrings(tester_DeletePopupText, deleteConfirmationPopuptext))
							{
								APP_LOGS.debug("Are you sure want to delete this tester association? has been displayed while deleting the Tester");
								
								comments =comments+  "Expected popup text has been displayed while deleting the Tester... (PASS)";
							}
							else 
							{
								fail = true;
								
								APP_LOGS.debug("Are you sure want to delete this tester association? has not been displayed while deleting the Tester");
								
								comments = comments+ "Expected popup text has not been displayed while deleting the Tester... (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected popup text has not been displayed while deleting the Tester");	
							}
						
						
							getObject("TestersViewAll_popUpDeleteButton").click();	
							getTextFromAutoHidePopUp();
							
							//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
							
							if(getObject("TestersViewAll_testerAvailable").findElements(By.tagName("tr")).size()!=0||getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size()==0||getObject("TestersViewAll_totalRecordCount").isDisplayed())
							{
								comments+="As tester was deleted, either the table did not disappear/No Tester Available message did not appear/the count div was still shown";

								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester deletion was not successful");
								throw new SkipException("Tester deletion was not successful");
							}
						
						} 
						catch (Throwable t) 
						{
							fail = true;
							assertTrue(false);
							APP_LOGS.debug("Something gone wrong while deleting the tester from grid.");
							
							comments = comments+ "Exception Occurred: Something gone wrong while deleting the tester from grid.... ";
						
						}
					}
				}
				else
				{
					fail = true;
					comments+="Login Not Successful for version lead(FAIL)";
				}
			} 
			catch (Throwable t) 
			{
				t.printStackTrace();
				fail = true;
				assertTrue(false);
				APP_LOGS.debug("Something gone wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
				comments = comments+ "Exception Occurred: Something went wrong while Executing the script";
			}
		
			
			APP_LOGS.debug("Closing the browser");		
			closeBrowser();
			
		}
		
		else 
		{
			APP_LOGS.debug("Login Not Successful");
		}		
	}
	
	//functions
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	

}
