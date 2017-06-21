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

public class VerifyTesterAddEditDeleteRole extends TestSuiteBase
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
	public void testVerifyTesterAddEditDeleteRole(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_EditedRole, String tester_description, String tester_Editeddescription) throws Exception
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
		
		APP_LOGS.debug(" Executing Test Case -> VerifyTesterAddEditDeleteRole...");
		
		System.out.println(" Executing Test Case -> VerifyTesterAddEditDeleteRole...");				
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
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
			
			getObject("Testers_testerTab").click();
			Thread.sleep(1000);
			APP_LOGS.debug("Click on Add Tester link");
			
			getElement("Tester_addTesterLink_Id").click();
			
			enterTesterName(tester.get(0).username);
			
			//TEST CASE 1: Role should get saved successfully and should be displayed in 'Select Role' /'Role Configuration box' section.
			APP_LOGS.debug("TEST CASE 1 EXICUTING");
		
			int numberOfRolePresentBeforeAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
			
			System.out.println("numberOfRolePresentBeforeAddingRole" + numberOfRolePresentBeforeAddingRole);
			
			getElement("TesterAddTester_addRole_Id").click();
			
			getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
			
			getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
			
			getElement("TesterAddTester_addRoleButton_Id").click();
			
			int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
			
			Thread.sleep(2000);
			System.out.println("numberOfRolePresentAfterAddingRole" + numberOfRolePresentAfterAddingRole);
				 
			if (numberOfRolePresentAfterAddingRole > numberOfRolePresentBeforeAddingRole) 
			{
				APP_LOGS.debug("Role Has been added in Select Role Box");
				
				String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
				
				if (titleOfNewlyAddedRole.equals(tester_Role)) 
				{
					APP_LOGS.debug("Role Is displayed in Select Role box.");
					comments = "Role Is displayed in Select Role box...";
				}
				else 
				{
					fail = true;
					APP_LOGS.debug("Role Is not displayed in Select Role box. Test Case has been Failed.");
					comments = "Fail occurred: Role Is not displayed in Select Role box...";
				}
			}
			else 
			{
				fail = true;
				APP_LOGS.debug("Role has not been added in Select Role Box. Test Case has been Failed.");
				comments = "Fail occurred: Role has not been added in Select Role Box...";
			}
				
			//Test Case 2:  Role should get updated successfully also user should get a message that 'Tester role updated successfully!' and should be displayed in 'Select Role' /'Role Configuration box' section.
			APP_LOGS.debug("TEST CASE 2 EXICUTING");
			
			//click on check box in select role box
			getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
		
			getElement("TesterAddTester_editRole_Id").click();
			
			getElement("TesterAddTester_roleTextBox_Id").clear();
			getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
			
			getElement("TesterAddTester_descriptionTextBox_Id").clear();
			getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
			
			getElement("TesterAddTester_updateRoleButton_Id").click();
			
			String roleUpdatedPopupText = getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText();
			
			System.out.println("roleUpdatedPopupText "+ roleUpdatedPopupText);
			
			if (roleUpdatedPopupText.equals("Tester role updated successfully!")) 
			{
				APP_LOGS.debug("Tester role updated successfully! is displayed in popup");
				comments = comments+ "Tester role updated successfully! is displayed...";
			}
			else 
			{
				fail = true;
				APP_LOGS.debug("Tester role updated successfully! is displayed in popup Test Case has been Failed.");
				comments = comments+  "Fail occurred: Tester role updated successfully! is not displayed...";
			}
			
			getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
			
			numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
			
			if (numberOfRolePresentAfterAddingRole > numberOfRolePresentBeforeAddingRole) 
			{
				APP_LOGS.debug("Updated Role Has been added in Select Role Box");
			//	String titleOfNewlyAddedRole = eventfiringdriver.findElement(By.xpath("//ul[@id='ulItemstesterRoles']/div/li["+numberOfRolePresentAfterAddingRole+"]")).getAttribute("title");
				
				String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
				
				if (titleOfNewlyAddedRole.equals(tester_EditedRole)) 
				{
					APP_LOGS.debug("Updated Role Is displayed in Select Role box.");
					comments =comments+ "Updated Role Is displayed in Select Role box after Editing...";
				}
				else 
				{
					fail = true;
					APP_LOGS.debug("Updated Role Is not displayed in Select Role box. Test Case has been Failed.");
					comments = comments+ "Fail occurred: Updated Role Is not displayed in Select Role box after Editing...";
				}
			}
			else 
			{
				fail = true;
				APP_LOGS.debug("Updated Role has not been added in Select Role Box. Test Case has been Failed.");
				comments = comments+ "Fail occurred: Updated Role has not been added in Select Role Box...";
			}
			
			//Test Case 3: A message appears 'Role deleted successfully' and Deleted role is removed from role configuration box.
			APP_LOGS.debug("TEST CASE 3 EXICUTING");
			
			//click on check box from select role
			getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
			
			getElement("TesterAddTester_deleteRole_Id").click();
			
			String popupTextAfterClickOnDelete = getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText();
			
			System.out.println("popupTextAfterClickOnDelete "+ popupTextAfterClickOnDelete);
			
			if (popupTextAfterClickOnDelete.equals("Role Deleted Successfully")) 
			{
				APP_LOGS.debug("Role Deleted Successfully is displayed in popup");
				comments = comments+ "Role Deleted Successfully is displayed...";
			}
			else 
			{
				fail = true;
				APP_LOGS.debug("Role Deleted Successfully is displayed in popup Test Case has been Failed.");
				comments = comments+  "Fail occurred: Role Deleted Successfully is not displayed...";
			}
			
			getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
			
			APP_LOGS.debug("close browser");			
			closeBrowser();
		}
		
		else 
		{
			isLoginSuccess=false;
			APP_LOGS.debug("Login Not Successful");
		}		
	}
		
		//functions
		
		
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
		}
		
			
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
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

