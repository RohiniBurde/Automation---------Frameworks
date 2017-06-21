package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
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
public class VerifyTesterAssignedToMultplTP extends TestSuiteBase
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
	public void testVerifyTesterAssignedToMultplTP(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,String TestPassName2,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_description, String testerRole, String testerDescription) throws Exception
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
					
					APP_LOGS.debug("Clicking on test management tab.");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(500);

					APP_LOGS.debug("Creating Test Pass.");
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						APP_LOGS.debug("Failed to create the Test Pass");
						comments=comments+"Failed to create the Test Pass 1(FAIL)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass1");
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					openBrowser();
					
					APP_LOGS.debug("Login with Test Manager");
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						//click on testManagement tab
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						
						APP_LOGS.debug("TEST CASE 1 EXECUTING");
						
						if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, tester_Role, tester_description, tester_Role))
						{
							APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
							
							comments = comments+ "Fail Occurred: Popup text is Not as expected while saving a Tester... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in create tester");
							throw new SkipException("Error in creating tester");
							
						}
							
					
						
						if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
						{
							APP_LOGS.debug("Failed to create the Test Pass");
							comments=comments+"Failed to create the Test Pass 2(FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass2");
							throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
						}
						
						
						APP_LOGS.debug("click on Tester Tab");
						getElement("TesterNavigation_Id").click();
						Thread.sleep(500);
						if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
						{
							getObject("TesterCreateNew_TesterActiveX_Close").click();
						}
						
						verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1",GroupName);
						verifyHeaders("Testers_portfolioDropDown_Id", "Testers_portfolioNameDDElemnts", "Testers_portfolioNameDDXpath1", PortfolioName);
						verifyHeaders("Testers_projectDropDown_Id", "Testers_projectNameDDElemnts", "Testers_projectNameDDXpath1", ProjectName);
						verifyHeaders("Testers_versionDropDown_Id", "Testers_versionNameDDElemnts", "Testers_versionNameDDXpath1", Version);
						verifyHeaders("Testers_testPassDropDown_Id", "Testers_testPassNameDDElemnts", "Testers_testPassNameDDXpath1", TestPassName2);
						
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						enterTesterName(tester.get(0).username);
						
						int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
					
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_saveTester_Id").click();
						//Thread.sleep(500);
						String textFromThePopupAfterSaveButton = getTextFromAutoHidePopUp();
						
						if (assertTrue(textFromThePopupAfterSaveButton.equals(displayNamefromPeoplePicker+" Tester(s) added successfully with the role(s) "+tester_Role+"."))) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							comments+="Was able to assign tester with same role to multiple test passes (Pass)/";
							//Thread.sleep(1000);
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
							
							comments+="Was not able to assign tester with same role to multiple test passes (FAIL) /";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text while assigning tester with same role to multiple test passes");
						}

						//getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
						//getObject("Testers_viewAllLink").click();
						if(!searchForTheTester(displayNamefromPeoplePicker))
						{
							comments+="The tester saving text was found but was not saved (FAIL)";
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester was not saved with same role in other test pass");
						}
						else
						{
							if(!verifyTesterFields(displayNamefromPeoplePicker, tester_Role, tester_description, "Select Area"))
							{
								comments+="The tester fields were not saved properly in other test pass (FAIL)";
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester fields were not saved with same role in other test pass");
							}
						}
						getElement("TesterAddTester_cancelRoleButton_Id").click();
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						Thread.sleep(500);
						
						getElement("TesterCreateNew_addTesterRoleLink_Id").click();
						//getElement("TesterCreateNew_roleName_Id").clear();
						getElement("TesterCreateNew_roleName_Id").sendKeys(testerRole);
						//getElement("TesterAddTester_descriptionTextBox_Id").clear();
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(testerDescription);
						getElement("TesterCreateNew_addTesterRole_Id").click();
						
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterEditTester_updateButton_Id").click();
						
						textFromThePopupAfterSaveButton = getTextFromAutoHidePopUp();
						
						if (assertTrue(textFromThePopupAfterSaveButton.equals("Tester updated successfully!"))) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							comments+="Was able to assign tester with different role to multiple test passes (Pass)";
							//Thread.sleep(1000);
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
							
							comments+="Was not able to assign tester with different role to multiple test passes (FAIL) ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text while assigning tester with different role to multiple test passes");
						}

						//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						//getObject("Testers_viewAllLink").click();
						if(!searchForTheTester(displayNamefromPeoplePicker))
						{
							comments+="The tester saving text was found but was not saved (FAIL)";
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester was not saved with same role in other test pass");
						}
						else
						{
							if(!verifyTesterFields(displayNamefromPeoplePicker, testerRole, testerDescription, "Select Area"))
							{
								comments+="The tester fields were not saved properly in other test pass (FAIL)";
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester fields were not saved with same role in other test pass");
							}
						}
					}
					else
					{
						fail = true;
						comments+="Login Not Successful for Test Manager(FAIL)";
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
				APP_LOGS.debug("Something went wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
				
				comments = comments+ "Exception Occurred: Something went wrong while Executing the script";
			}
			
			closeBrowser();
		}
		
		else 
		{
			APP_LOGS.debug("Login Not Successful");
		}		
	}
	
	
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
		else if(fail){
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
