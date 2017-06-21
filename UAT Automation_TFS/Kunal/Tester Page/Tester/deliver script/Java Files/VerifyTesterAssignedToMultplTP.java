package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyTesterAssignedToMultplTP extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	
	String comments;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		stakeholder=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		//testManager=getUsers("Test Manager", 1);
	}

	@Test(dataProvider="getTestData")
	public void testVerifyTesterAssignedToMultplTP(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,String TestPassName2,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_description) throws Exception
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
		
		APP_LOGS.debug(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");
		
		System.out.println(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");				
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try 
			{
				getElement("UAT_testManagement_Id").click();
				
				APP_LOGS.debug("Clicking On Test Management Tab ");
			
				Thread.sleep(3000);
			
				APP_LOGS.debug("Creating a project");
				
				createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username/*, stakeholder.get(0).username*/);
				
				APP_LOGS.debug("Closing the browser after saving project.");
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser after saving project.");
				openBrowser();
				
				APP_LOGS.debug("Login with version lead");
				login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead");
				
				APP_LOGS.debug("Clicking on test management tab.");
				getElement("UAT_testManagement_Id").click();
				
				APP_LOGS.debug("Creating Test Pass.");
				createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username);
			
				APP_LOGS.debug("Opening the browser after saving Test pass.");
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser after saving Test pass.");
				openBrowser();
				
				APP_LOGS.debug("Login with Test Manager");
				login(testManager.get(0).username,testManager.get(0).password, "Test Manager");
				
				//click on testManagement tab
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(2000);
				
				APP_LOGS.debug("click on Tester Tab");
				getElement("TesterNavigation_Id").click();
				Thread.sleep(1000);
				
				APP_LOGS.debug("verifying headers");
				
				dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
				dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
				dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
				dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
				
				
				APP_LOGS.debug("Done verifying headers");
				
				//Test case 1: Tester is added successfully and should be display in VIEW ALL page
				APP_LOGS.debug("TEST CASE 1 EXICUTING");
				
				APP_LOGS.debug("Click on Add Tester link");
				getElement("Tester_addTesterLink_Id").click();
				
				enterTesterName(tester.get(0).username);
				
				int numberOfRolePresentBeforeAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
				
				System.out.println("numberOfRolePresentBeforeAddingRole" + numberOfRolePresentBeforeAddingRole);
				
				getElement("TesterAddTester_addRole_Id").click();
				
				getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
				
				getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
				
				getElement("TesterAddTester_addRoleButton_Id").click();
				
				int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
				
				getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
				
				getElement("TesterAddTester_saveTester_Id").click();
				
				String textFromThePopupAfterSaveButton = getObject("TesterDetailsAddTester_testerPopupTextAfterClickOnSave").getText();
				
				System.out.println("textFromThePopupAfterSaveButton : "+textFromThePopupAfterSaveButton);
				
				System.out.println("textFromThePopupAfterSaveButton : "+displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.");
				
				if (textFromThePopupAfterSaveButton.equals(displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.")) 
				{
					APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
					
					APP_LOGS.debug("Click on OK Button");
				
					Thread.sleep(1000);

					getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
					
					comments = "Tester saved successfully for Test Pass1... ";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Tester saved successfully for Test Pass1. Test Case has been failed");
					
					comments = "Fail Occurred: Tester saved successfully for Test Pass1... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
				}
				
				getObject("Testers_viewAllLink").click();
				
				Thread.sleep(1000);
				APP_LOGS.debug("Create test pass for same project");
				
				createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username);

				APP_LOGS.debug("click on Tester Tab");
				getElement("TesterNavigation_Id").click();
				Thread.sleep(1000);
				
				APP_LOGS.debug("verifying headers");
				
				verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1",GroupName);
				verifyHeaders("Testers_portfolioDropDown_Id", "Testers_portfolioNameDDElemnts", "Testers_portfolioNameDDXpath1", PortfolioName);
				verifyHeaders("Testers_projectDropDown_Id", "Testers_projectNameDDElemnts", "Testers_projectNameDDXpath1", ProjectName);
				verifyHeaders("Testers_versionDropDown_Id", "Testers_versionNameDDElemnts", "Testers_versionNameDDXpath1", Version);
				verifyHeaders("Testers_testPassDropDown_Id", "Testers_testPassNameDDElemnts", "Testers_testPassNameDDXpath1", TestPassName2);
				
				APP_LOGS.debug("Done verifying headers");
			
				getElement("Tester_addTesterLink_Id").click();
				
				enterTesterName(tester.get(0).username);
				
				numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
				
				getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
				
				getElement("TesterAddTester_saveTester_Id").click();
				
				textFromThePopupAfterSaveButton = getObject("TesterDetailsAddTester_testerPopupTextAfterClickOnSave").getText();
				
				System.out.println("textFromThePopupAfterSaveButton : "+textFromThePopupAfterSaveButton);
				
				System.out.println("textFromThePopupAfterSaveButton : "+displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.");
				
				if (textFromThePopupAfterSaveButton.equals(displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.")) 
				{
					APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
					
					APP_LOGS.debug("Click on OK Button");
				
					Thread.sleep(1000);

					getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
					
					comments = comments+ "Tester saved successfully for Test Pass2... ";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
					
					comments = comments+ "Fail Occurred: Tester saved successfully for Test Pass2... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
				}
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("Something gone wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
				
				comments = comments+ "Exception Occurred: Something gone wrong while Executing the script";
			}
			
			getObject("Testers_viewAllLink").click();
			closeBrowser();
		}
		
		else 
		{
			isLoginSuccess=false;
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
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
}
