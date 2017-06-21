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

public class VerifyTesterAddEditDeleteRole extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	
	String comments;
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
	public void testVerifyTesterAddEditDeleteRole(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate,String tester_Role,
			String tester_EditedRole, String tester_description, String tester_Editeddescription, String tester_alertText, String alertText_EditMultipleRole,
			String successfulUpdatedMessage, String delete_StandardMessage, String role_deleteAlert) throws Exception
	{
		count++;
		comments = "";
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
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
					closeBrowser();
					throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project is not created successfully");//reports
				}
				APP_LOGS.debug("Closing the browser after saving project.");
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser after saving project.");
				openBrowser();
				
				APP_LOGS.debug("Login with version lead");
				if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				{
					//Thread.sleep(2000);
					APP_LOGS.debug("Clicking on test management tab.");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Creating Test Pass.");
					
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						APP_LOGS.debug("Test Pass Creation Fails");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
						closeBrowser();
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					
					APP_LOGS.debug("Closing the browser after saving Test pass.");
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					openBrowser();
					
					APP_LOGS.debug("Login with Test Manager");
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
					
				//click on testManagement tab
						//Thread.sleep(1500);
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
					
						APP_LOGS.debug("click on Tester Tab");
						getElement("TesterNavigation_Id").click();
						Thread.sleep(1000);
						if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
						{
							getObject("TesterCreateNew_TesterActiveX_Close").click();
						}
						dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
						dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
						dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
						dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
						dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
						
						APP_LOGS.debug("Click on Add Tester link");
						
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						
						//TEST CASE 1: Role should get saved successfully and should be displayed in 'Select Role' /'Role Configuration box' section.
						APP_LOGS.debug("TEST CASE 1 EXECUTING");
					
						int numberOfRolePresentBeforeAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						
						if(getElement("TesterAddTester_addRole_Id").isDisplayed())
							getElement("TesterAddTester_addRole_Id").click();
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Link to add role was not visible");
							comments+="Link to add role was not visible (FAIL)";
							throw new SkipException("Link to add role was not visible");
						}
						
						if(getElement("TesterAddTester_roleTextBox_Id").isDisplayed())
							getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Text Box to add role was not visible");
							comments+="Text Box to add role was not visible (FAIL)";
							throw new SkipException("Text Box to add role was not visible");
						}
						
						if(assertTrue(getElement("TesterAddTester_descriptionTextBox_Id").isDisplayed()))
							getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Text Box to add description was not visible");
							comments+="Text Box to add description was not visible (FAIL)";
						}
						
						if(getElement("TesterAddTester_addRoleButton_Id").isDisplayed())
							getElement("TesterAddTester_addRoleButton_Id").click();
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Add Role link was not visible");
							comments+="Add Role link was not visible (FAIL)";
							throw new SkipException("Add Role link was not visible");
						}
						
						int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						Thread.sleep(2000);
							 
						if (numberOfRolePresentAfterAddingRole > numberOfRolePresentBeforeAddingRole) 
						{
							APP_LOGS.debug("Role Has been added in Select Role Box");
							
							String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
							
							if (compareStrings(tester_Role, titleOfNewlyAddedRole)) 
							{
								APP_LOGS.debug("Added Role Is displayed in Select Role box.");
								comments = "Added Role Is displayed in Select Role box... (PASS)";
							}
							else 
							{
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Added Role Is displayed in Select Role box");
								fail = true;
								APP_LOGS.debug("Role Is not displayed in Select Role box. Test Case has been Failed.");
								comments = "Fail occurred: Role Is not displayed in Select Role box...";
							}
						}
						else 
						{
							APP_LOGS.debug("Role has not been added in Select Role Box. Test Case has been Failed.");
							comments = "Fail occurred: Role has not been added in Select Role Box...";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Added Role is not seen in 'Select Role' box");
							throw new SkipException("Role has not been added in Select Role Box");
						}
							
						//Test Case 2:  Role should get updated successfully also user should get a message that 'Tester role updated successfully!' and should be displayed in 'Select Role' /'Role Configuration box' section.
						APP_LOGS.debug("TEST CASE 2 EXECUTING");
						
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", 1).click();
						
						if(getElement("TesterAddTester_editRole_Id").isDisplayed())
							getElement("TesterAddTester_editRole_Id").click();
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Role link was not visible");
							comments+="Edit Role link was not visible (FAIL)";
							throw new SkipException("Edit Role link was not visible");
						}
						
						String alertText = waitForElementVisibility("TesterAddTester_popupTextAfterUpdateButtonClick",10).getText();
						if(assertTrue(getObject("TesterAddTester_popupTextAfterUpdateButtonClick").isDisplayed()))
						{
							comments+="User was given the alert when trying to edit Standard role. (PASS)";
							//alertText = getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText();
							
							if(compareStrings(tester_alertText, alertText))
							{
								comments+="User was not allowed to edit the Standard Role (Pass)";
								getObject("TesterAddTester_selectOkButton").click();
							}
							else
							{
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User should not be allowed to edit Standard Role");
								comments+="User was allowed to edit Standard Role (FAIL)";
								getElement("TesterAddTester_cancelRoleButton_Id").click();
							}
						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User was not given the alert when trying to edit Standard role");
							comments+="User was not given the alert when trying to edit Standard role (FAIL)";
						}	
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						getElement("TesterAddTester_editRole_Id").click();
						alertText = waitForElementVisibility("TesterAddTester_popupTextAfterUpdateButtonClick",10).getText();
						if(assertTrue(getObject("TesterAddTester_popupTextAfterUpdateButtonClick").isDisplayed()))
						{
							comments+="User was given the alert when trying to edit multiple role. (PASS)";
							//alertText = getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText();
							
							if(compareStrings(alertText_EditMultipleRole, alertText))
							{
								comments+="User was not allowed to edit multiple Role (Pass)";
								getObject("TesterAddTester_selectOkButton").click();
							}
							else
							{
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User should not be allowed to edit multiple Role");
								comments+="User was allowed to edit multiple Role (FAIL)";
								getElement("TesterAddTester_cancelRoleButton_Id").click();
							}
						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User was not given the alert when trying to edit Standard role");
							comments+="User was not given the alert when trying to edit multiple role (FAIL)";
						}
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", 1).click();
	
						getElement("TesterAddTester_editRole_Id").click();
						
						String currentRoleInRoleTextBox = getElement("TesterAddTester_roleTextBox_Id").getAttribute("value");
						String currentDescriptionInRoleTextBox = getElement("TesterAddTester_descriptionTextBox_Id").getText();
						
						if(compareStrings(tester_Role, currentRoleInRoleTextBox)&&compareStrings(tester_description, currentDescriptionInRoleTextBox))
						{
							comments+="The saved Role and description was successfully visible there. (Pass)";
						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The saved Role and description was not visible");
							comments+="The saved Role and description was not visible (FAIL)";
						}
						
						getElement("TesterAddTester_roleTextBox_Id").clear();
						
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
						
						getElement("TesterAddTester_descriptionTextBox_Id").clear();
						
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
						
						getElement("TesterAddTester_updateRoleButton_Id").click();
						
						String roleUpdatedPopupText = getTextFromAutoHidePopUp();
						
						
						if (compareStrings(successfulUpdatedMessage, roleUpdatedPopupText)) 
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
						
						//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						if (numberOfRolePresentAfterAddingRole > numberOfRolePresentBeforeAddingRole) 
						{
							APP_LOGS.debug("Updated Role Has been added in Select Role Box");
						//	String titleOfNewlyAddedRole = eventfiringdriver.findElement(By.xpath("//ul[@id='ulItemstesterRoles']/div/li["+numberOfRolePresentAfterAddingRole+"]")).getAttribute("title");
							
							String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
							
							if (compareStrings(tester_EditedRole, titleOfNewlyAddedRole)) 
							{
								APP_LOGS.debug("Updated Role Is displayed in Select Role box.");
								comments =comments+ "Updated Role Is displayed in Select Role box after Editing...";
							}
							else 
							{
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated Role Is not displayed in Select Role box");
								fail = true;
								APP_LOGS.debug("Updated Role Is not displayed in Select Role box. Test Case has been Failed.");
								comments = comments+ "Fail occurred: Updated Role Is not displayed in Select Role box after Editing...";
							}
						}
						else 
						{
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated Role has not been added in Select Role Box");
							fail = true;
							APP_LOGS.debug("Updated Role has not been added in Select Role Box. Test Case has been Failed.");
							comments = comments+ "Fail occurred: Updated Role has not been added in Select Role Box...";
						}
						
						//Test Case 3: A message appears 'Role deleted successfully' and Deleted role is removed from role configuration box.
						APP_LOGS.debug("TEST CASE 3 EXECUTING");
						
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", 1).click();
						
						if(getElement("TesterAddTester_deleteRole_Id").isDisplayed())
							getElement("TesterAddTester_deleteRole_Id").click();
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Delete Role link was not visible");
							comments+="Delete Role link was not visible (FAIL)";
							throw new SkipException("Delete Role link was not visible");
						}
						
						alertText = waitForElementVisibility("TesterAddTester_popupTextAfterUpdateButtonClick",10).getText();
						
						if(assertTrue(getObject("TesterAddTester_popupTextAfterUpdateButtonClick").isDisplayed()))
						{
							comments+="User was given the alert when trying to delete Standard role. (PASS)";
							
							//alertText = getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText();
							
							if(compareStrings(delete_StandardMessage, alertText))
							{
								comments+="User was not allowed to delete the Standard Role (Pass)";
								getObject("TesterAddTester_selectOkButton").click();
							}
							else
							{
								fail=true;
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User should not be allowed to delete Standard Role");
								comments+="User was allowed to delete Standard Role (FAIL)";
								getElement("TesterAddTester_cancelRoleButton_Id").click();
							}
						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User was not given the alert when trying to edit Standard role");
							comments+="User was not given the alert when trying to edit Standard role (FAIL)";
						}
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", 1).click();
						
						//click on check box from select role
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_deleteRole_Id").click();
						
						String popupTextAfterClickOnDelete = getTextFromAutoHidePopUp();
						
	
						if (compareStrings(role_deleteAlert, popupTextAfterClickOnDelete)) 
						{
							APP_LOGS.debug("Role Deleted Successfully is displayed in popup");
							comments = comments+ "Role Deleted Successfully is displayed... (PASS)";
						}
						else 
						{
							fail = true;
							APP_LOGS.debug("Role Deleted Successfully is displayed in popup. Test Case has been Failed.");
							comments = comments+  "Fail occurred: Role Deleted Successfully is not displayed...";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role Deleted Successfully is not displayed");
						}
						
						//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						int numberOfRolePresentAfterDeletingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						if(compareIntegers(numberOfRolePresentBeforeAddingRole, numberOfRolePresentAfterDeletingRole))
						{
							comments+="The deleted role is no more seen in Select Role area (PASS)";
						}
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The deleted role is still seen in Select Role area");
							fail = true;
							APP_LOGS.debug("The deleted role is still seen in Select Role area. Test Case has been Failed.");
							comments = comments+  "Fail occurred: The deleted role is still seen in Select Role check box area...";
						}
					
					APP_LOGS.debug("close browser");			
					closeBrowser();
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Login Not Successful for Test Manager");
					comments+="Login Not Successful for Test Manager (FAIL)";
				}
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Login Not Successful for version Lead");
				comments+="Login Not Successful for version Lead (FAIL)";
			}
		}
		catch(Throwable t)
		{
			fail = true;
			comments+="Some Exception Occured while testing role feature of tester (FAIL)";
			assertTrue(false);
			closeBrowser();
		}
	}
		
	else 
	{
		fail=true;
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
		public void reportTestResult() throws Exception
		{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}
		
		


}

