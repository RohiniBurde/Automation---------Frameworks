package com.uat.suite.tm_tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class VerifyRoleBoxUIChangByTesterGrp extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	List<WebElement> rolePresentInRoleBox;
	
	int numberOfTestersPresentInGridBefore;
	int flag;
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
		
		versionLead=new ArrayList<Credentials>();
		stakeholder=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}

	@Test(dataProvider="getTestData")
	public void testVerifyRoleBoxUIChangByTesterGrp(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_EditedRole,String tester_description, String tester_Editeddescription, String roleDeletePopUpText) throws Exception
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
			
				Thread.sleep(3000);
				
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
					Thread.sleep(2000);
					APP_LOGS.debug("Clicking on test management tab.");
					getElement("UAT_testManagement_Id").click();
					
					APP_LOGS.debug("Creating Test Pass.");
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
					{
						comments+="Exception in creating Test Pass (FAIL)";
						APP_LOGS.debug("Test Pass Creation Fails");
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
						throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser after saving Test pass.");
					openBrowser();
					
					APP_LOGS.debug("Login with Test Manager");
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						Thread.sleep(1000);
						//click on testManagement tab
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(2000);
						
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
						
						//Test case 1: Select test uat group and click ok.
						APP_LOGS.debug("TEST CASE 1 EXECUTING");
						
						APP_LOGS.debug("Click on Add Tester link");
						getElement("Tester_addTesterLink_Id").click();
						
						APP_LOGS.debug("Total Number of Roles present in Role box before adding Group.");
						
						rolePresentInRoleBox = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
						int numberOfrolePresentInRoleBox = rolePresentInRoleBox.size();
						
						System.out.println("numberOfTestersPresentInGridBefore: "+numberOfrolePresentInRoleBox );
						
						Thread.sleep(1000);
					
						//Hard coding the tester group name (getting from the data provider) because the only group has to be added for testing
						 
						enterTesterName(tester_testerName);
					
						Thread.sleep(1000);
						APP_LOGS.debug("Click on Ok button of IE Alert");
						
						Alert alert = driver.switchTo().alert();
						Thread.sleep(1500);
						alert.accept();
						
						//This function checks if activex is enabled on browser or not. If disabled, it skips the rest of the execution
						activeXHandling();
						
						//Handling active X
						if(skip==true)
						{
							throw new SkipException("Have to Skip as Active-X is disabled and it have to be enabled in order to test this feature.");
						}
						
						
						if (getElement("TesterAddTester_testerRoleInBulkDiv_Id").isDisplayed()) 
						{
							APP_LOGS.debug("Tester/Roles are displayed in Assign Role Box.");
						
							comments = comments + " Tester/Roles are displayed in Assign Role Box (Pass) ";
							
						}
						else 
						{
							APP_LOGS.debug("Tester/Roles are not displayed in Assign Role Box (FAIL).");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester/Roles are not displayed in Assign Role Box");
							
							comments = comments + " Tester/Roles are not displayed in Assign Role Box (FAIL) ";
						
							throw new SkipException("Tester/Roles are not displayed in Assign Role Box");
						}
						
						
						APP_LOGS.debug("Checking the Tester checkbox are selected or not. if Not Then checking the checkbox");
						
						int totalNumOftestersinRoleBox = 0;
						
						if(!getElement("Tester_ADDL_SelectAll_Id").isSelected())
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The populated testers were not checked by default");
							comments+="The populated testers were not checked by default";
							getElement("Tester_ADDL_SelectAll_Id").click();
						}
						
						List<WebElement> testersPresentInAssignRoleBox = getObject("TesterAddTester_totalTestersPresentInAssignRoleBox").findElements(By.tagName("tr"));
						totalNumOftestersinRoleBox = testersPresentInAssignRoleBox.size();
						
						String testerNamesArray[] = new String[totalNumOftestersinRoleBox];
						
						//Holding the name of each tester in Assign Role Box section
						for(int i = 0;i<totalNumOftestersinRoleBox;i++)
						{
							testerNamesArray[i] = getObject("TesterAddTester_totalTestersPresentInAssignRoleBox").findElements(By.tagName("tr")).get(i).findElements(By.tagName("td")).get(0).getText();
						}
						
						APP_LOGS.debug("Getting the number of 'td' present in table before adding a roles.");
						
						List<WebElement> tdPresentBeforeInAssignRoleBox = getObject("TesterAddTester_totalTdPresentInAssignRoleBox").findElements(By.tagName("td"));
						int totalNumOfTdPresentbefore = tdPresentBeforeInAssignRoleBox.size();
						
						APP_LOGS.debug("Adding a role");
						
						getElement("TesterAddTester_addRole_Id").click();
						
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
						
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
						
						getElement("TesterAddTester_addRoleButton_Id").click();
						
						List<WebElement> tdPresentAfterInAssignRoleBox = getObject("TesterAddTester_totalTdPresentInAssignRoleBox").findElements(By.tagName("td"));
						
						int totalNumOfTdPresentAfter = tdPresentAfterInAssignRoleBox.size();
						
						if (totalNumOfTdPresentAfter > totalNumOfTdPresentbefore) 
						{
							APP_LOGS.debug("Role has been added in Role Box beside Standard Role.");
							//get the title of role and check with the given role
							String newlyAddedRoleName = getObject("TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox1", "TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox2", totalNumOfTdPresentAfter).getAttribute("title");
							
							if (compareStrings(tester_Role, newlyAddedRoleName)) 
							{
								APP_LOGS.debug("Role with the name "+tester_Role+" has been added in role box.");
								
								comments = comments+ "Role with the same name has been displayed in Role Box (PASS) ";
							}
							else 
							{
								fail = true;
								comments = comments+ "Role with the same name has not been displayed in Role Box (FAIL) ";
								APP_LOGS.debug("Role with the name "+tester_Role+" has not been added in role box. Test case hass been failed.");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role with the name "+tester_Role+" has not been added in role box");
								throw new SkipException("Role with the same name has not been displayed in Role Box");
							}
						}
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The new role was not added successfully");
							comments+="The new role was not added successfully (FAIL)";
							throw new SkipException("The new role was not added successfully");
						}
						
						APP_LOGS.debug("Click on newly added role and save testers");
						
						getObject("TesterAddTester_newlyAddedRoleCheckBoxInRoleBox1", "TesterAddTester_newlyAddedRoleCheckBoxInRoleBox2", totalNumOfTdPresentAfter).click();
						
						boolean roleCheckBox = false;
						for (int i = 1; i <= totalNumOftestersinRoleBox; i++) 
						{
							if (!(getObject("TesterAddTester_eachCheckBoxOfNewlyAddedRole1", "TesterAddTester_eachCheckBoxOfNewlyAddedRole2", i).isSelected())) 
							{
								if(!roleCheckBox)
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "All the checkboxes were not seen selected under role column");
								roleCheckBox=true;
								getObject("TesterAddTester_eachCheckBoxOfNewlyAddedRole1", "TesterAddTester_eachCheckBoxOfNewlyAddedRole2", i).click();
								APP_LOGS.debug("All Checkboxes are not checked.");
							}	
						}
						
						if(roleCheckBox==true)
						{
							fail=true;
							assertTrue(false);
							comments+="All Checkboxes are not checked under Role column after clicking on Select All (FAIL)";
						}
						
						APP_LOGS.debug("Clicking on save");
						
						getElement("TesterAddTester_saveTester_Id").click();
						
						if(!getElement("TesterCreateNew_testerSuccessMessageText_Id").isDisplayed())
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester successful creation messgae did not appear");
							comments+="The tester successful creation message did not appear (FAIL) ";
						}
						//Expected is each tester should be successfully saved
						String grpCreatedSuccessMeassage = getObject("TesterAddTester_testerGroupCreatedSuccessMessage").getText();
						
						String userNameTextInPopup = getObject("TesterAddTester_usernameAndRoleTextOnPopup1", "TesterAddTester_usernameAndRoleTextOnPopup2", 1).getText();
						
						String RoleTextInPopup = getObject("TesterAddTester_usernameAndRoleTextOnPopup1", "TesterAddTester_usernameAndRoleTextOnPopup2", 3).getText();
						
						if(compareStrings(totalNumOftestersinRoleBox+" Testers created successfully", grpCreatedSuccessMeassage)&&
								compareStrings("User Name", userNameTextInPopup)&&compareStrings("Role(s)", RoleTextInPopup))
						{
							APP_LOGS.debug("A tester creation status pop up has appear and tester Group created and success message is displayed");
							comments = comments+ "The tester creation popup was successfully populated with username and roles column (PASS) ";
						}
						else 
						{
							fail = true;
							APP_LOGS.debug("A tester creation status pop up did not appear.");	
							comments = comments+ "The tester creation popup was successfully populated but not with username and roles column (FAIL) ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The successful tester creation popup did not have expected contents");
						}
						//Gets the text of successful tester creation and their names and checks if all the names are showin popup or not
						int numberOfTestersShownInTesterCreationPopUp = getObject("Tester_ADDL_NumberOfTestersInTesterGroupAdditionPopup").findElements(By.tagName("tr")).size()-1;
						if(!compareIntegers(totalNumOftestersinRoleBox, numberOfTestersShownInTesterCreationPopUp))
						{
							fail=true;
							comments+="The total number of testers that were in Role Box was " +totalNumOftestersinRoleBox +"but the pop up shows only "+numberOfTestersShownInTesterCreationPopUp +"(FAIL) " ;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect number of testers shown in tester group addition popup");
						}
						else
						{
							for(int i =0;i<totalNumOftestersinRoleBox;i++)
							{
								if(!(compareStrings(testerNamesArray[i], getObject("Tester_ADDL_NumberOfTestersInTesterGroupAdditionPopup").findElements(By.tagName("tr")).get(i+1).findElements(By.tagName("td")).get(0).getText())&&(compareStrings(tester_Role,getObject("Tester_ADDL_NumberOfTestersInTesterGroupAdditionPopup").findElements(By.tagName("tr")).get(i+1).findElements(By.tagName("td")).get(2).getText()))))
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Either the tester name or their role did not match with saved ones");
									comments+="Either the tester name or their role did not match with saved ones (FAIL)";
									break;
								}
							
							}
						}
						
						APP_LOGS.debug("Click on OK button after successfully added tester group.");
						
						getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
						
						getObject("Testers_viewAllLink").click();
						Thread.sleep(1000);
						//Checks the tester grid if all the saved testers are shown in view all page
						
						int numberOfTestersPresentInGridAfter = eventfiringdriver.findElements(By.xpath(OR.getProperty("TestersViewAll_totalTestersInGrid"))).size();
						System.out.println("numberOfTestersPresentInGridAfter : "+numberOfTestersPresentInGridAfter);
						
						if (compareIntegers(totalNumOftestersinRoleBox, numberOfTestersPresentInGridAfter)) 
						{
							APP_LOGS.debug("Tester has been added in Tester grid using Tester Group functionality.");
							
							comments = comments+"Tester has been added in Tester grid using Tester Group functionality. (PASS) ";
							
							for(int i =0;i<totalNumOftestersinRoleBox;i++)
							{
								if(!(compareStrings(testerNamesArray[i], getObject("TesterViewAll_testerNameXpath1","TesterViewTester_testerNameText",i+1).getText())&&compareStrings(tester_Role, getObject("TesterViewAll_testerNameXpath1", "TesterViewTester_RoleNameText", i+1).getText())))
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Either the tester name or their role did not match with saved ones in Testers View All Page");
									comments+="Either the tester name or their role did not match with saved ones in Testers View All Page(FAIL) ";
									break;
								}
							}
						}
						else 
						{
							fail = true;
				        	
				        	APP_LOGS.debug("Tester has not been added in Tester grid using Tester Group functionality. Test case has been failed.");
							
							comments =comments+"Tester has not been added in Tester grid (FAIL) ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester has not been added in Tester grid");	
						}
						
						//Checking if the tester already added through tester group can be added or not
						
						getObject("TesterDetails_addTesterTab").click();
						
						enterTesterName(testerNamesArray[0]);
						
						int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_saveTester_Id").click();
						
						if(getElement("TesterCreateNew_testerSuccessMessageText_Id").isDisplayed())
						{
							fail=true;
							assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The tester successful creation message shown for already added tester");
							getObject("Tester_testeraddedsuccessfullyOkButton").click();
							getObject("Testers_viewAllLink").click();
							comments+=testerNamesArray[0]+" tester was already added through tester group but when trying to add it manually, it got added again (FAIL) ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester added 2 times");
						}
						else
						{
							getObject("Tester_testeraddedsuccessfullyOkButton").click();
							comments+="Tester once added either manually or through group is not allowed to be added again (PASS)";
						}
						
						Thread.sleep(1000);
						
						//Checking the error message should appear if the same group is added again
						getObject("TesterDetails_addTesterTab").click();
						enterTesterName(tester_testerName);
						
						driver.switchTo().alert().accept();
						Thread.sleep(4000);
						wait.until(ExpectedConditions.elementToBeClickable(getObject("TesterAddTester_newlyAddedRoleCheckBoxInRoleBox1", "TesterAddTester_newlyAddedRoleCheckBoxInRoleBox2", totalNumOfTdPresentAfter)));
						getObject("TesterAddTester_newlyAddedRoleCheckBoxInRoleBox1", "TesterAddTester_newlyAddedRoleCheckBoxInRoleBox2", totalNumOfTdPresentAfter).click();
						
						getElement("TesterAddTester_saveTester_Id").click();
						
						if(getElement("TesterCreateNew_testerSuccessMessageText_Id").isDisplayed())
						{
							comments+="Testers already exists message was shown (Pass) ";
							if(compareIntegers(totalNumOftestersinRoleBox, getObject("Tester_GroupAdditionPopUpTextSecondTime_Tester").findElements(By.tagName("span")).size()))
							{
								if(!compareStrings(totalNumOftestersinRoleBox+" Testers already exist for selected Test Pass", getObject("Tester_GroupAdditionPopUpTextSecondTime_Text").getText()))
								{
									fail=true;
									assertTrue(false);
									comments+="Incorrect text was shown in popup message of already available testers (FAIL) ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect text was shown in popup message of already available testers");
								
								}	
							}
							else
							{
								fail=true;
								assertTrue(false);
								comments+="Incorrect count of testers available in group was shown (FAIL) ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect cound of testers available in group was shown");
							}
						}
						else
						{
							fail=true;
							assertTrue(false);
							comments+="The group and their testers were already added. While trying to add second time, it did not show already exists popup (FAIL) ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "While adding testers through group, same testers are allowed multiple times");
						}
						
						getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
						
						numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						//Editing the saved role and checks if result is reflected in view all or not
						getElement("TesterAddTester_editRole_Id").click();
						
						getElement("TesterAddTester_roleTextBox_Id").clear();
						getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
						
						getElement("TesterAddTester_descriptionTextBox_Id").clear();
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
						
						getElement("TesterAddTester_updateRoleButton_Id").click();
							
						getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
						
						if (totalNumOfTdPresentAfter > totalNumOfTdPresentbefore) 
						{
							APP_LOGS.debug("Role has been added in Role Box beside Standard Role.");
							//get the title of role and check with the given role
							String newlyAddedRoleName = getObject("TesterAddTester_textOfRoleInSelectRoleBox1", "TesterAddTester_textOfRoleInSelectRoleBox2", 2).getAttribute("title");
							
							if (compareStrings(tester_EditedRole, newlyAddedRoleName)) 
							{
								APP_LOGS.debug("Role with the name "+tester_EditedRole+" has been updated in role box.");
								
								comments = comments+ "Role with the same name has been updated in Role Box (PASS) ";
							}
							else 
							{
								fail = true;
								comments = comments+ "Role with the same name has not been updated in Role Box (FAIL) ";
								APP_LOGS.debug("Role with the name "+tester_EditedRole+" has not been updated in role box. Test case has been failed.");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role with the name "+tester_EditedRole+" has not been updated in role box");
								throw new SkipException("Role with the same name has not been updated in Role Box");
							}
						}
						else
						{
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The new role was not updated successfully");
							comments+="The new role was not updated successfully (FAIL)";
							throw new SkipException("The new role was not updated successfully");
						}
						
						getObject("Testers_viewAllLink").click();
						Thread.sleep(1000);
						
						if(!compareStrings(tester_EditedRole, getObject("TesterViewAll_RoleNameXpath").getText()))
						{
							fail = true;
							comments = comments+ "The edited role was not reflected in view all grid (FAIL) ";
							APP_LOGS.debug("The edited role was not reflected in view all grid. Test case has been failed.");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The edited role was not reflected in view all grid");	
						}
						// Trying to delete the saved role
						getObject("TesterDetails_addTesterTab").click();
						
						getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
						
						getElement("TesterAddTester_deleteRole_Id").click();
						
						if(getObject("TesterAddTester_popupTextAfterUpdateButtonClick").isDisplayed())
						{
							comments+="User was not allowed to delete role as its being already assigned (PASS)";
							if(compareStrings(roleDeletePopUpText, getObject("TesterAddTester_popupTextAfterUpdateButtonClick").getText()))
							{
								comments+="The expected text was shown in popup when trying to delete already assigned role (PASS) ";
							}
							else
							{
								fail = true;
								comments = comments+ "The expected text was not shown in popup when trying to delete already assigned role (FAIL) ";
								APP_LOGS.debug("The expected text was not shown in popup when trying to delete already assigned role .");
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The expected text was not shown in popup when trying to delete already assigned role");	
							}
						}
						else
						{
							fail = true;
							assertTrue(false);
							comments+="User was allowed to delete role although its being already assigned (FAIL)";
							APP_LOGS.debug("User was allowed to delete role although its being already assigned. Test case has been failed.");
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User was allowed to delete role although its being already assigned");	
						}
						
						closeBrowser();
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
				if(skip==false)
				{
					fail = true;
					assertTrue(false);
					APP_LOGS.debug("Something went wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
					comments = comments+ "Exception Occurred: Something went wrong while Executing the script";
				}
				
				closeBrowser();
			}
			
			
		}		
	}
	
	//functions
	
	
	
	private void activeXHandling() {
		try
        {
            eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
            comments+="Distribution List - ActiveX is enabled. ";
            
            Thread.sleep(4000);
            if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
            {
            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXEnabledStepsPresent");
                getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
                comments+="Steps are present to enable active X while it already enabled- (Fail). ";
                fail = true;
                assertTrue(false);
            }
            else
            {
            	comments+="Steps are not present to enable active X as its already enabled- (Pass). ";
            }
          
        }
        catch(Exception e)
        {
            //Active x code
            System.out.println("Active x disabled");
            skip=true;
           
            try
            {
              wait.until(ExpectedConditions.presenceOfElementLocated(By.id(OR.getProperty("TestCaseCreateNew_TestCaseActiveX_Id"))));
              if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
              {
                getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
                comments+="Steps are present to enable active X as it was disabled(Pass). But will have to skip the rest process as active-x have to be enabled in order to test this feature.";

              }                                          
              else
              {
            	  fail=true;
            	  assertTrue(false);
                comments+="Steps are not present to enable active X but it should have been(Fail). And will have to skip the rest process as active-x have to be enabled in order to test this feature.";
                TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");

              }
            }
            catch(Throwable t)
            {
              comments+="Steps are not present to enable it(Fail). And will have to skip the rest process as active-x have to be enabled in order to test this feature. ";
              TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");

            }
        }
	}

	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
		{
			if(fail)
				isTestPass=false;
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
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
