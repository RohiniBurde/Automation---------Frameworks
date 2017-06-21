package com.uat.suite.tm_tester;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyEditTesterDetail extends TestSuiteBase 
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
	public void testVerifyEditTesterDetail(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_EditedRole, String tester_description, String tester_Editeddescription, String tester_area, String testerUpdateMessage, String testerRoleUpdateMessage) throws Exception
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
					//Thread.sleep(2000);
					APP_LOGS.debug("Clicking on test management tab.");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Creating Test Pass.");
					
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						comments+="Exception in creating Test Pass (FAIL)";
						APP_LOGS.debug("Test Pass Creation Fails");
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
						APP_LOGS.debug("click on Tester Tab");
						
						if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, tester_Role,tester_description, tester_Role))
						 
						{
							fail = true;
							
							APP_LOGS.debug("Exception in create tester");
							
							comments = comments+ "Fail Occurred: Exception in create tester";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in create tester");
							throw new SkipException("Exception in create tester");
						}
						
												
						
						//Click on view all and verify Tester is displayed
						//getObject("Testers_viewAllLink").click();
						
						if(!searchForTheTester(displayNamefromPeoplePicker))
						{
							comments+="The saved tester was not seen in the grid (FAIL)";
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The saved tester was not seen in the grid (FAIL)");
							throw new SkipException("The saved tester was not seen in the grid");
						}
						
						else 
						{
							
							comments = comments+ "The saved tester was found in the grid (PASS)";
						}
						
						if (assertTrue(getElement("TesterEditTester_editTestertab_Id").isDisplayed())) 
						{
							APP_LOGS.debug("Edit Tester tab is displayed");
						
							comments = comments+ "Edit Tester tab is displayed... (PASS)";
						}
						else 
						{
							APP_LOGS.debug("Edit Tester tab is Not displayed");
							
							comments = comments+ "Fail Occurred: Edit Tester tab is Not displayed... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Tester tab is Not displayed");	
						}
						
						int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();

						
						APP_LOGS.debug("Editing/Changing the tester name.");
					
						//Thread.sleep(1000);
						
						enterTesterName(tester.get(1).username);
						
						//click on Role checkbox
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						Select area = new Select(getElement("TesterAddTester_Area_ID"));
						area.selectByVisibleText(tester_area);
						
						APP_LOGS.debug("Clicking on Update Button");
						
						getElement("TesterEditTester_updateButton_Id").click();
						//Thread.sleep(1000);
						
						String textFromThePopupAfterUpdateButton = getTextFromAutoHidePopUp();
						
						if (compareStrings(testerUpdateMessage, textFromThePopupAfterUpdateButton))
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterUpdateButton);
							
							comments = comments+ "Tester Updated message text appeared successfully... (PASS)";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while Updating a Tester. Test Case has been failed");
							
							comments = comments+ "Fail Occurred: Popup text is Not as expected while updating a Tester... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
						}
						
						
						if(searchForTheTester(displayNamefromPeoplePicker))
						{
							comments+="The updated tester was successfully visible (PASS)";
						}
						else
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The updated tester name was not seen in the grid");
							comments+="The updated tester name was not seen in the grid (FAIL)";
						}
						
						//Test case 2: Role should get updated successfully and user should get message that 'Tester role updated successfully!'.
						//Updated role should be dispalyed in 'Select Role'/'Role Configuration box' section. 
						APP_LOGS.debug("TEST CASE 2 EXECUTING");
						
						/*if(!assertTrue(!(getObject("TesterEdit_UpperRibbonGroupDropDownImage").isDisplayed()&&getObject("TesterEdit_UpperRibbonPortfolioDropDownImage").isDisplayed()
								&&getObject("TesterEdit_UpperRibbonProjectDropDownImage").isDisplayed()&&getObject("TesterEdit_UpperRibbonVersionDropDownImage").isDisplayed()&&getObject("TesterEdit_UpperRibbonTestPassDropDownImage").isDisplayed())))
						{
							comments+="The dropdown images should not have been visible in edit Tester form (FAIL)";
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Upper Ribbons Dropdown Visible in edit tester page");
						}*/
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
									
						getElement("TesterAddTester_roleTextBox_Id").clear();
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
						
						getElement("TesterAddTester_descriptionTextBox_Id").clear();
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
						
						getElement("TesterAddTester_updateRoleButton_Id").click();
						//Thread.sleep(1000);
						String roleUpdatedPopupText = getTextFromAutoHidePopUp();
						
						if(compareStrings(testerRoleUpdateMessage, roleUpdatedPopupText))
						{
							APP_LOGS.debug("Tester role updated successfully! is displayed in popup");
							comments = comments+ "Tester role updated successfully! is displayed... (PASS)";
						}
						else 
						{
							fail = true;
								
							APP_LOGS.debug("Tester role updated successfully! is not displayed in popup Test Case has been Failed.");
							
							comments = comments+  "Fail occurred: Tester role updated successfully! is not displayed...";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role update popup has not been displayed");
						}
						
						//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						
						if (numberOfRolePresentAfterAddingRole > 1) 
						{
							APP_LOGS.debug("Updated Role Has been added in Select Role Box");
							
							String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
							
							if (compareStrings(tester_EditedRole, titleOfNewlyAddedRole)) 
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
						//Thread.sleep(1000);
						
						getObject("TesterAddTester_checkBoxOfStandardRoleInRoleBox").click();
						Thread.sleep(500);
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						APP_LOGS.debug("Clicking on Update Button");
						
						getElement("TesterEditTester_updateButton_Id").click();
						
						textFromThePopupAfterUpdateButton = getTextFromAutoHidePopUp();
						
						if(compareStrings(testerUpdateMessage, textFromThePopupAfterUpdateButton))
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterUpdateButton);
							
							
							
							comments = comments+ "Tester Updated successfully...(PASS) ";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while Updating a Tester. Test Case has been failed");
							
							comments = comments+ "Fail Occurred: Popup text is Not as expected while updating a Tester with Standard and Some role";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
						}
					
					//test case 4: User should be able assign both Standard and some Role to the same Tester.
					
						String testerRoleNameInGrid = getObject("TestersViewAll_testerRoleNameInGrid").getText();
						if(compareStrings("Standard,"+tester_EditedRole, testerRoleNameInGrid))
						{
							APP_LOGS.debug("Edited Role and Standard Role Has been displayed in Grid.");
							
							comments = comments+ "Tester could be assigned to Standard and other role at a time (PASS)";
						}
						else 
						{
							fail = true;
								
							APP_LOGS.debug("Edited Role and Standard Role Has not been displayed in Grid...");
							
							comments = comments+ "Fail Occurred: Edited Role and Standard Role Has not been displayed in Grid... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edited and Standard Role not in Grid");	
						}
						
						APP_LOGS.debug("Test Case 4 Executing: Tester saving with 2 roles other than Standard");
						
						getObject("TestersViewAll_testerRoleEditImgInGrid").click();
						Thread.sleep(500);
						//getObject("TesterAddTester_checkBoxOfStandardRoleInRoleBox").click();
						//getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						getElement("TesterCreateNew_addTesterRoleLink_Id").click();
						getElement("TesterCreateNew_roleName_Id").sendKeys(tester_Role);
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
						getElement("TesterCreateNew_addTesterRole_Id").click();
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole-1).click();
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();

						getElement("TesterEditTester_updateButton_Id").click();
						
						textFromThePopupAfterUpdateButton = getTextFromAutoHidePopUp();
						
						if(compareStrings(testerUpdateMessage, textFromThePopupAfterUpdateButton))
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterUpdateButton);
							
							
							
							comments = comments+ "Tester Updated successfully with 2 roles... (PASS)";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while Updating a Tester. Test Case has been failed");
							
							comments = comments+ "Fail Occurred: Popup text is Not as expected while updating a Tester with 2 roles at a time";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
						}
						
						testerRoleNameInGrid = getObject("TestersViewAll_testerRoleNameInGrid").getText();
						if(compareStrings(tester_Role+","+tester_EditedRole, testerRoleNameInGrid))
						{
							APP_LOGS.debug("Edited Role and new Role Has been displayed in Grid.");
							
							comments = comments+ "Tester could be assigned to 2 roles at a time other than Standard (PASS)";
						}
						else 
						{
							fail = true;
								
							APP_LOGS.debug("Edited Role and new Role Has not been displayed in Grid...");
							
							comments = comments+ "Fail Occurred: Tester could not be assigned to 2 roles at a time other than Standard";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edited and new Role not in Grid");	
						}
						
					}
					else
					{
						fail = true;
						comments+="Login Not Successful for test manager(FAIL)";
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
				APP_LOGS.debug("Something went wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
				assertTrue(false);
				comments = comments+ "Exception Occurred: Something went wrong while Executing the script(FAIL)";
			
			}
			
			closeBrowser();
		}
		else 
		{
			//fail=true;
			APP_LOGS.debug("Login Not Successful");
			comments+="Login Not Successful (FAIL)";
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
