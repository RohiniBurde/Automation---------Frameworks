package com.uat.suite.tm_tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class VerifyAssignedAddEditDeleteRole extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
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
	public void testVerifyAssignedAddEditDeleteRole(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_EditedRole, String tester_description, String tester_Editeddescription, String tester_area, String tester_roleUpdateMessage, String tester_roleDeleteMessage, String tester_successRoleDelete) throws Exception
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
				Thread.sleep(1000);
				APP_LOGS.debug("Clicking On Test Management Tab ");			
						
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
						
						//click on testManagement tab
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);
						
						APP_LOGS.debug("click on Tester Tab");
						getElement("TesterNavigation_Id").click();
						Thread.sleep(500);
						
						if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
						{
							getObject("TesterCreateNew_TesterActiveX_Close").click();
						}
						
						APP_LOGS.debug("verifying headers");
						
						dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
						dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
						dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
						dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
						dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
						
						
						APP_LOGS.debug("Done verifying headers");
						
						//Test case 1: Tester is added successfully and should be display in VIEW ALL page
						APP_LOGS.debug("TEST CASE 1 EXECUTING");
						
						APP_LOGS.debug("Click on Add Tester link");
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						
						enterTesterName(tester.get(0).username);
						
						getElement("TesterAddTester_addRole_Id").click();
						
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
						
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
						
						getElement("TesterAddTester_addRoleButton_Id").click();
						
						int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						Thread.sleep(2000);
						
						//Click on newly added role
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						Select area = new Select(getElement("TesterAddTester_Area_ID"));
						area.selectByVisibleText(tester_area);
						
						getElement("TesterAddTester_saveTester_Id").click();
						
						//verify text from the popup and click on ok button
						
						String textFromThePopupAfterSaveButton = getTextFromAutoHidePopUp();
						if(compareStrings(displayNamefromPeoplePicker+" Tester(s) added successfully with the role(s) "+tester_Role+".", textFromThePopupAfterSaveButton))
						{
							
							comments = comments+ "Tester saved successfully message appeared...(PASS) ";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Popup text is Not as expected while saving a Tester. Test Case has been failed");
							
							comments = comments+ "Fail Occurred: Popup text is Not as expected while saving a Tester... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text");
						}
						
						
						//Click on view all and verify Tester is displayed
						//getObject("Testers_viewAllLink").click();
						
						if(searchForTheTester(tester.get(0).username))
						{
							comments+="Added tester was visible in view all grid (PASS)";
							if(verifyTesterFields(tester.get(0).username, tester_Role, tester_description, tester_area))
							{
								comments+="The tester was added with all fields saved successfully (PASS)";
							}
							else
							{
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester fields weren't saved successfully");
								comments+="The tester was added but all the fields weren't saved successfully (FAIL)";
							}
						}
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester fields weren't saved successfully");
							comments+="The tester saved successfully message was shown but was not visible in view all grid (FAIL)";
							throw new SkipException("The tester saved successfully message was shown but was not visible in view all grid");

						}
						
						//Test case 2:  Role should get updated successfully also user should get a message that 'Tester role updated successfully!' and should be displayed in 'Select Role' /'Role Configuration box' section.
						APP_LOGS.debug("TEST CASE 2 EXECUTING");
						
						APP_LOGS.debug("Click on Add Tester link");
						
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_editRole_Id").click();
						
						getElement("TesterAddTester_roleTextBox_Id").clear();
						
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
						
						getElement("TesterAddTester_descriptionTextBox_Id").clear();
						
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
						
						getElement("TesterAddTester_updateRoleButton_Id").click();
						
						String roleUpdatedPopupText = getTextFromAutoHidePopUp();
						
						
						if (compareStrings(tester_roleUpdateMessage, roleUpdatedPopupText)) 
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
						
						//getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						String titleOfNewlyAddedRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", numberOfRolePresentAfterAddingRole).getAttribute("title");
						String standardRole = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", 1).getAttribute("title");
						
						if (compareStrings(tester_EditedRole, titleOfNewlyAddedRole)&&compareStrings("Standard", standardRole)) 
						{
							APP_LOGS.debug("Updated Role Is displayed in Select Role box with Standard as default.");
							
							comments =comments+ "Updated Role Is displayed in Select Role box after Editing with Standard as default...";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Updated Role Is not displayed in Select Role box or Standard role is not seen. Test Case has been Failed.");
							
							comments = comments+ "Fail occurred: Updated Role Is not displayed in Select Role box after Editing  or Standard role is not seen...";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated Role Is not displayed in Select Role box or Standard role is not seen");
							
						}
						
						// click on view all and verify if the edited role is reflected in tester grid
						getObject("Testers_viewAllLink").click();
						
						Thread.sleep(500);
						
						if(!(tester.get(0).username.replace(".", " ").equalsIgnoreCase(getObject("TesterViewAll_testerNameXpath1","TesterViewTester_testerNameText",1).getText())&&tester_EditedRole.equals(getObject("TesterViewAll_testerNameXpath1", "TesterViewTester_RoleNameText", 1).getText())))
						{
							assertTrue(false);
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The edited role was not reflected in Testers View All Page");
							comments+="The edited role was not reflected in Testers View All Page(FAIL) ";
						}
						//Test Case 3: A message appears 'Role cannot be deleted because this role is already assigned to some tester in this project'
						APP_LOGS.debug("TEST CASE 3 EXECUTING");
						
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_deleteRole_Id").click();
						
						String popupTextAfterClickOnDelete = waitForElementVisibility("TesterAddTester_popupTextAfterUpdateButtonClick",10).getText();
						
						
						if(compareStrings(tester_roleDeleteMessage, popupTextAfterClickOnDelete))
						{
							APP_LOGS.debug("Role cannot be deleted because this role is already assigned to some tester in this project- text is displayed.");
							
							comments = comments+ "Role cannot be deleted- text is displayed...(PASS)";
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug("Role Deleted Successfully is displayed in popup Test Case has been Failed.");
							
							comments = comments+  "As role is being assigned to the tester, it should not have shown deletion message..(FAIL).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Assigned Role showed succesful deletion message");
						}
						getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						boolean flag=true;
						List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));

						for(int j = 0; j < roleSelectionNames.size();j++)
						{
	
							if(roleSelectionNames.get(j).getAttribute("title").equals(tester_EditedRole))
							{
								flag=false;
								break;
							}
						}
						if(flag==true)
						{
							assertTrue(false);
							comments+="The role deleted message was not shown but was deleted (FAIL)";
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The role deleted message was not shown but was deleted");
						}
						else
						{
							comments+="The role showed 'cannot delete message' and was not deleted (PASS)";
						}
						//click on view all link and delete 
						try 
						{
							APP_LOGS.debug("Deleting tester/tester role from view all page.");
							
							getObject("Testers_viewAllLink").click();
							Thread.sleep(500);
							
							getObject("TestersViewAll_testerRoleDeleteImgInGrid").click();
							 
							Thread.sleep(2000);
							
							getObject("TestersViewAll_popUpDeleteButton").click();
							
							getTextFromAutoHidePopUp();
						} 
						catch (Throwable t) 
						{
							fail = true;
							
							assertTrue(false);
							
							APP_LOGS.debug("Delete/Ok button not displayed or found");
							
							comments = comments+  "Fail occurred: Delete/Ok button not displayed or found on view all page...";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Delete/Ok button on popup");
						}
						
						if(getObject("TestersViewAll_testerAvailable").findElements(By.tagName("tr")).size()!=0||getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size()==0||getObject("TestersViewAll_totalRecordCount").isDisplayed())
						{
							comments+="As tester was deleted, either the table did not disappear/No Tester Available message did not appear/the count div was still shown";

							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester deletion was not successful");
							throw new SkipException("Tester deletion was not successful");
						}
						
						getElement("Tester_addTesterLink_Id").click();
						Thread.sleep(500);
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_deleteRole_Id").click();
						
						popupTextAfterClickOnDelete = getTextFromAutoHidePopUp();
						
						if(compareStrings(tester_successRoleDelete, popupTextAfterClickOnDelete))
						{
							APP_LOGS.debug("Role Deleted Successfully is displayed in popup");
							comments = comments+ "Role Deleted Successfully is displayed...";
						}
						else 
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role Deleted Successfully is not displayed");
							fail = true;
							APP_LOGS.debug("Role Deleted Successfully is not displayed in popup.. Test Case has been Failed.");
							comments = comments+  "Fail occurred: Role Deleted Successfully is not displayed...";
						}
						
						flag=true;
						
						roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));

						for(int j = 0; j < roleSelectionNames.size();j++)
						{
							if((roleSelectionNames.get(j)).getAttribute("title").equals(tester_EditedRole))
							{
								flag=false;
								break;
							}
						}
						
						if(flag==false)
						{
							assertTrue(false);
							comments+="The role was not deleted although it no more have assigned tester(FAIL)";
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The role was not deleted although it no more have assigned tester");
						}
						else
						{
							comments+="The role was deleted as it no more have assigned tester(PASS)";
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
				fail = true;
				
				APP_LOGS.debug("Something went wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
				assertTrue(false);
				comments = comments+ "Exception Occurred: Something went wrong while Executing the script(FAIL)";
			
			}
			closeBrowser();
				
		}
		
		
		else 
		{
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

