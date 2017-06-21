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

public class VerifyEditTesterDetail extends TestSuiteBase 
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
	public void testVerifyEditTesterDetail(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
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
		
		APP_LOGS.debug(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");
		
		System.out.println(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");				
		
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
			
			//Test case 1: (i)User should be able to edit the tester detail  (ii)Message 'Tester updated successfully' should display
			
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
			
			//Click on newly added role
			getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
			
			getElement("TesterAddTester_saveTester_Id").click();
			
			//verify text from the popup and click on ok button
			
			String textFromThePopupAfterSaveButton = getObject("TesterDetailsAddTester_testerPopupTextAfterClickOnSave").getText();
			System.out.println("textFromThePopupAfterSaveButton : "+textFromThePopupAfterSaveButton);
			
			System.out.println("textFromThePopupAfterSaveButton : "+displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.");
			
			if (textFromThePopupAfterSaveButton.equals(displayNamefromPioplePicker+" Tester(s) Saved successfully with the role(s) "+tester_Role+".\nPlease click \"VIEW ALL\" to see the details.")) 
			{
				APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
				
				APP_LOGS.debug("Click on OK Button");
			
				Thread.sleep(1000);

				getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
				
				comments = comments+ "Tester saved successfully... ";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
				
				comments = comments+ "Fail Occurred: Popup text is Not as expected while saving a Tester... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
			}
			
			
			//Click on view all and verify Tester is displayed
			getObject("Testers_viewAllLink").click();
			
			String testerRoleNameInGrid = getObject("TestersViewAll_testerRoleNameInGrid").getText();
			
			if (testerRoleNameInGrid.equals(tester_Role)) 
			{
				APP_LOGS.debug("Role Has been displayed in Grid.");
				
				comments = comments+ "Role Has been displayed in Grid... ";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Role Has not been displayed in Grid...");
				
				comments = comments+ "Fail Occurred: Role Has not been displayed in Grid... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role Has not been displayed in Grid");	
			}
			
			APP_LOGS.debug("Clicking on Tester Edit image.");
			
			getObject("TestersViewAll_testerRoleEditImgInGrid").click();
			
			if (getElement("TesterEditTester_editTestertab_Id").isDisplayed()) 
			{
				APP_LOGS.debug("Edit Tester tab is displayed");
			
				comments = comments+ "Edit Tester tab is displayed... ";
			}
			else 
			{
				APP_LOGS.debug("Edit Tester tab is Not displayed");
				
				comments = comments+ "Fail Occurred: Edit Tester tab is Not displayed... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Tester tab is Not displayed");	
			}
			
			APP_LOGS.debug("Editing/Changing the tester name.");
		
			Thread.sleep(1000);
			
			enterTesterName(tester.get(1).username);
			
			//click on Role checkbox
			getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
			
			APP_LOGS.debug("Clicking on Update Button");
			getElement("TesterEditTester_updateButton_Id").click();
			
			String textFromThePopupAfterUpdateButton = getObject("TesterDetailsAddTester_testerPopupTextAfterClickOnUpdate").getText();
			
			if (textFromThePopupAfterUpdateButton.equals("Tester updated successfully!")) 
			{
				APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterUpdateButton);
				
				APP_LOGS.debug("Click on OK Button");
			
				Thread.sleep(1000);

				getObject("TesterAddTester_okButtonAfterUpdateButtonClick").click();
				
				comments = comments+ "Tester Updated successfully... ";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Popup text is Not as expected while Updating a Tester. Test Case has been failed");
				
				comments = comments+ "Fail Occurred: Popup text is Not as expected while updating a Tester... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
			}
			
			//Test case 2: Role should get updated successfully and user should get message that 'Tester role updated successfully!'.
			//Updated role should be dispalyed in 'Select Role'/'Role Configuration box' section. 
			APP_LOGS.debug("TEST CASE 2 EXICUTING");
			
			//Click on view all and verify Tester is displayed
			getObject("Testers_viewAllLink").click();
			
			APP_LOGS.debug("Clicking on Tester Edit image.");
			
			getObject("TestersViewAll_testerRoleEditImgInGrid").click();
			
			numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
			
			if (getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).isSelected()) 
			{
				APP_LOGS.debug("Checkbox is already Selected.");
			}
			else 
			{
				getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
			}
			getElement("TesterAddTester_editRole_Id").click();
			
			APP_LOGS.debug("verifying whether old role is displayed in Role text filed.");
			
			String old_role = (String) eventfiringdriver.executeScript("return $('#txtNewRole').val()");
			
			System.out.println("old_role: "+ old_role);
			
			if (old_role.equals(tester_Role)) 
			{
				APP_LOGS.debug("Old role has been displayed in role text filed");
			}
			else 
			{
				fail = true;
				APP_LOGS.debug("Old role has not been displayed in role text filed. Test case has been failed");
			}			
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
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role update popup has not been displayed");
			}
			
			getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
			
			
			if (numberOfRolePresentAfterAddingRole > numberOfRolePresentBeforeAddingRole) 
			{
				APP_LOGS.debug("Updated Role Has been added in Select Role Box");
				
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
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated role not displayed in role box");
			}
			
			APP_LOGS.debug("checking 2 checkboxes for (requirement for Test case 3)");
			Thread.sleep(2000);
			
			getObject("TesterAddTester_checkBoxOfStandardRoleInRoleBox").click();
			Thread.sleep(2000);
			
			getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
			
			APP_LOGS.debug("Clicking on Update Button");
			
			getElement("TesterEditTester_updateButton_Id").click();
			
			if (textFromThePopupAfterUpdateButton.equals("Tester updated successfully!")) 
			{
				APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterUpdateButton);
				
				APP_LOGS.debug("Click on OK Button");
			
				Thread.sleep(1000);

				getObject("TesterAddTester_okButtonAfterUpdateButtonClick").click();
				
				comments = comments+ "Tester Updated successfully... ";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Popup text is Not as expected while Updating a Tester. Test Case has been failed");
				
				comments = comments+ "Fail Occurred: Popup text is Not as expected while updating a Tester... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
			}
			
			//test case 4: User should be able assign both Standard and some Role to the same Tester.
			
			testerRoleNameInGrid = getObject("TestersViewAll_testerRoleNameInGrid").getText();
			
			if (testerRoleNameInGrid.equals(tester_EditedRole+",Standard")) 
			{
				APP_LOGS.debug("Edited Role and Standard Role Has been displayed in Grid.");
				
				comments = comments+ "Edited Role and Standard Role been displayed in Grid... ";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Edited Role and Standard Role Has not been displayed in Grid...");
				
				comments = comments+ "Fail Occurred: Edited Role and Standard Role Has not been displayed in Grid... ";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edited and Standard Role not in Grid");	
			}
			
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
