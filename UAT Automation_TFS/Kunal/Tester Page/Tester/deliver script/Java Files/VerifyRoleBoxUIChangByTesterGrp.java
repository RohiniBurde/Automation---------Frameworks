package com.uat.suite.tm_tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyRoleBoxUIChangByTesterGrp extends TestSuiteBase 
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	List<WebElement> rolePresentInRoleBox;
	int totalNumOftestersinRoleBox;
	int numberOfTestersPresentInGridBefore;
	int flag;
	
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
	public void testVerifyRoleBoxUIChangByTesterGrp(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
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
			
			//Test case 1: Select test uat group and click ok.
			APP_LOGS.debug("TEST CASE 1 EXICUTING");
			
			String noTesterAvailableText = getObject("TestersViewAll_withoutTester").getText();
			System.out.println("noTesterAvailable: "+ noTesterAvailableText);
		
			if (assertTrue(noTesterAvailableText.equals("No Tester(s) Available!"))) 
			{
				APP_LOGS.debug("No projects available");
				flag = 0;
			}
			else 
			{
				numberOfTestersPresentInGridBefore = eventfiringdriver.findElements(By.xpath(OR.getProperty("TestersViewAll_totalTestersInGrid"))).size();
				flag = 1;
			}
			
			APP_LOGS.debug("Click on Add Tester link");
			getElement("Tester_addTesterLink_Id").click();
			
			APP_LOGS.debug("Total Number of Roles present in Role box before adding Group.");
			
			rolePresentInRoleBox = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
			int numberOfrolePresentInRoleBox = rolePresentInRoleBox.size();
			
			System.out.println("numberOfTestersPresentInGridBefore: "+numberOfrolePresentInRoleBox );
			
			Thread.sleep(1000);
			enterTesterName(tester.get(11).username);
			
			Thread.sleep(1000);
			APP_LOGS.debug("Click on Ok button of IE Alert");
			
			Alert alert = driver.switchTo().alert();
			Thread.sleep(1500);
			alert.accept();
			
			comments = " Security warning pop has displayed...";
			
			if (getElement("TesterAddTester_testerRoleInBulkDiv_Id").isDisplayed()) 
			{
				APP_LOGS.debug("Tester/Roles are displayed in Assign Role Box.");
			
				comments = comments + " Tester/Roles are displayed in Assign Role Box... ";
				
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Tester/Roles are not displayed in Assign Role Box.");
				
				comments = comments + " Tester/Roles are not displayed in Assign Role Box... ";
			}
			
			
			APP_LOGS.debug("Checking the Tester checkbox are selected or not. if Not Then checking the checkbox");
			
			try 
			{
				List<WebElement> testersPresentInAssignRoleBox = getObject("TesterAddTester_totalTestersPresentInAssignRoleBox").findElements(By.tagName("tr"));
				totalNumOftestersinRoleBox = testersPresentInAssignRoleBox.size();
				
				System.out.println("totalNumOftestersinRoleBox :"+ totalNumOftestersinRoleBox);
				
				for (int i = 1; i <= totalNumOftestersinRoleBox; i++) 
				{
					if (getObject("TesterAddTester_testerCheckBoxInAssignRoleBox1", "TesterAddTester_testerCheckBoxInAssignRoleBox2", i).isSelected()) 
					{
						APP_LOGS.debug("Tester"+i+" CheckBox is selected in Role Box");
					}
					else 
					{
						APP_LOGS.debug("Tester"+i+" CheckBox is not selected in Role Box");

						APP_LOGS.debug("Checking unchecked checkbox.");
						
						getObject("TesterAddTester_testerCheckBoxInAssignRoleBox1", "TesterAddTester_testerCheckBoxInAssignRoleBox2", i).click();
						
					}
					
				}	
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("Error Occured while Checking the Tester checkbox are selected or not");	
			
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester checkboxes in Role box is selected or not");
			}
			//----------------------------
			
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
				APP_LOGS.debug("Role has been added in Role Box besides Standard Role.");
				//get the title of role and check with the given role
				String newlyAddedRoleName = getObject("TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox1", "TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox2", totalNumOfTdPresentAfter).getAttribute("title");
				
				if (newlyAddedRoleName.equals(tester_Role)) 
				{
					APP_LOGS.debug("Role with the name "+tester_Role+" has been added in role box.");
					
					comments = comments+ "Role with the same name has been displayed in Role Box...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Role with the name "+tester_Role+" has not been added in role box. Test case hass been failed.");	
				
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester checkboxes in Role box is selected or not");
				}
			}
			
			try 
			{
				APP_LOGS.debug("Edit the role and verify its visibility.");
				
				getObject("TesterAddTester_newlyAddedRoleCheckBoxInRoleBox1", "TesterAddTester_newlyAddedRoleCheckBoxInRoleBox2", totalNumOfTdPresentAfter).click();
				
				getElement("TesterAddTester_editRole_Id").click();
				
				getElement("TesterAddTester_roleTextBox_Id").clear();
				getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_EditedRole);
				
				getElement("TesterAddTester_descriptionTextBox_Id").clear();
				getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_Editeddescription);
				
				getElement("TesterAddTester_updateRoleButton_Id").click();
					
				getObject("TesterAddTester_okButtonAfterUpdateDeleteButtonClick").click();
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("Something gone wrong while updating edited role. test case has been failed");
				
			}
			
			if (totalNumOfTdPresentAfter > totalNumOfTdPresentbefore) 
			{
				APP_LOGS.debug("Role has been added in Role Box besides Standard Role.");
				//get the title of role and check with the given role
				String newlyAddedRoleName = getObject("TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox1", "TesterAddTester_newlyAddedRoleNameDisplayedInRoleBox2", totalNumOfTdPresentAfter).getAttribute("title");
				
				if (assertTrue(newlyAddedRoleName.equals(tester_EditedRole))) 
				{
					APP_LOGS.debug("Role with the name "+tester_EditedRole+" has been added in role box.");
					
					comments = comments+ "Edited Role with the same name has been displayed in Role Box...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Role with the name "+tester_EditedRole+" has not been added in role box. Test case hass been failed.");	
				
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester checkboxes in Edited Role box is selected or not");
				}
			}
			//-------------------------------------------
			try 
			{
				APP_LOGS.debug("Click on newly added role and save testers");
				
			//	List<WebElement> tdPresentInRoleBox = getObject("TesterAddTester_totalTdPresentInAssignRoleBox").findElements(By.tagName("td"));
				//totalNumOfTdPresentAfter = tdPresentAfterInAssignRoleBox.size();
				
				getObject("TesterAddTester_newlyAddedRoleCheckBoxInRoleBox1", "TesterAddTester_newlyAddedRoleCheckBoxInRoleBox2", totalNumOfTdPresentAfter).click();
				for (int i = 1; i < totalNumOfTdPresentAfter; i++) 
				{
					if (assertTrue(getObject("TesterAddTester_eachCheckBoxOfNewlyAddedRole1", "TesterAddTester_eachCheckBoxOfNewlyAddedRole2", i).isSelected())) 
					{
						APP_LOGS.debug("All Checkboxes are checked.");
					}
					else 
					{
						fail= true;
						
						APP_LOGS.debug("All Checkboxes are not checked.");
						
					}
						
				}
				
				APP_LOGS.debug("Clicking on save");
				getElement("TesterAddTester_saveTester_Id").click();
				
				String grpCreatedSuccessMeassage = getObject("TesterAddTester_testerGroupCreatedSuccessMessage").getText();
				String userNameTextInPopup = getObject("TesterAddTester_usernameAndRoleTextOnPopup1", "TesterAddTester_usernameAndRoleTextOnPopup2", 1).getText();
				String RoleTextInPopup = getObject("TesterAddTester_usernameAndRoleTextOnPopup1", "TesterAddTester_usernameAndRoleTextOnPopup2", 3).getText();
				if (assertTrue(grpCreatedSuccessMeassage.contains("Testers created successfully")&&userNameTextInPopup.contains("User Name")&&RoleTextInPopup.contains("Role(s)"))) 
				{
					APP_LOGS.debug("A tester creation status pop up has appear and tester Group created and success message is displayed");
					
					comments = comments+ "A tester creation status pop up has appear and tester Group created and success message is displayed...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("A tester creation status pop up has appear.");	
				
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester checkboxes in Edited Role box is selected or not");
				}	
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("Somthing gone wrong while saving the tester group and verifying popup contents");
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "error while saving the tester group and verifying popup contents");
			}
			
			APP_LOGS.debug("Click on OK button after successfully added tester group.");
			
			getObject("TestPassCreateTestCase_testCaseAddedsuccessfullyOkButton").click();
			
			getObject("Testers_viewAllLink").click();
			Thread.sleep(1000);
			
			int numberOfTestersPresentInGridAfter = eventfiringdriver.findElements(By.xpath(OR.getProperty("TestersViewAll_totalTestersInGrid"))).size();
			System.out.println("numberOfTestersPresentInGridAfter : "+numberOfTestersPresentInGridAfter);
			
			
			if (flag == 0) 
			{
				System.out.println("flag: "+flag);
				if (numberOfTestersPresentInGridAfter == totalNumOftestersinRoleBox ) 
				{
					APP_LOGS.debug("Tester has been added in Tester grid using Tester Group functionality.");
					
					comments = comments+"Tester has been added in Tester grid using Tester Group functionality.";
				}
				else 
				{
					fail = true;
		        	
		        	APP_LOGS.debug("Tester has not been added in Tester grid using Tester Group functionality. Test case has been failed.");
					
					comments =comments+"Tester has not been added in Tester grid...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "added testers are not in grid");	
				}
			}
			else 
			{	
				System.out.println("flag: "+flag);
				if (numberOfTestersPresentInGridAfter == (totalNumOftestersinRoleBox + numberOfTestersPresentInGridBefore) ) 
				{
					APP_LOGS.debug("Tester has been added in Tester grid using Tester Group functionality.");
					
					comments = comments+"Tester has been added in Tester grid using Tester Group functionality.";
				}
				else 
				{
					fail = true;
		        	
		        	APP_LOGS.debug("Tester has not been added in Tester grid using Tester Group functionality. Test case has been failed.");
					
					comments =comments+"Tester has not been added in Tester grid...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "added testers are not in grid");	
				}
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
