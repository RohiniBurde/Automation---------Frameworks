/* Author Name:-Aishwarya Deshpande
 * Created Date:- 8th Jan 2015
 * Last Modified on Date:- 12th Jan 2015
 * Module Description:- Verification of Contents of General Settings tab with positive set of Data
 */

package com.uat.suite.configuration;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyGeneralSettingsContent extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	boolean flag=false;
	
	
	//Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing VerifyGeneralSettingsContent Test Case");

		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyProcessDetailsContent(String Role, String groupName, String Portfolio, String projectName, String Version,
			String endMonth, String endYear, String endDate, String VersionLead, String testPassName, String testManager) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		comments="";			

		APP_LOGS.debug("Opening Browser... ");
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			//version lead
			 int versionlead_count = Integer.parseInt(VersionLead);
			 versionLead=new ArrayList<Credentials>();
			 versionLead = getUsers("Version Lead", versionlead_count);
			 
			 //TestManager 
			 int testManager_count = Integer.parseInt(testManager);
			 test_Manager=new ArrayList<Credentials>();
			 test_Manager = getUsers("Test Manager", testManager_count);

			 //click on testManagement tab
			 APP_LOGS.debug(" Clicking on Test Management Tab ");
			 getElement("UAT_testManagement_Id").click();
			 Thread.sleep(1000);
			 
			 if(!createProject(groupName, Portfolio, projectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(projectName+" Project not Created Successfully.");
				comments=projectName+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(projectName+" Project Created Successfully.");
			 			
			 if(!createTestPass(groupName, Portfolio, projectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName+" Test Pass not Created Successfully.");
				comments+=testPassName+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTPCreation");
				closeBrowser();

				throw new SkipException(testPassName+" Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
			 
			 APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
			 comments+="Data has been made Successfully from Test Management tab... ";
			
			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
			 
			 APP_LOGS.debug("Opening Browser... ");
			 openBrowser();
			 
			 if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			 {
	            APP_LOGS.debug("Logged in browser with Test Manager");
	            try
	            {
	            	//Clicking on Configuration Tab
					getElement("UAT_configuration_Id").click();
					APP_LOGS.debug("Configuration tab clicked ");
					Thread.sleep(2000);
					
					String configurationTabName=getElement("Configuration_generalSettingsTab_Id").getText();
					if(resourceFileConversion.getProperty("Configuration_NonTesterDefaultTabText").equalsIgnoreCase(configurationTabName))
					{
						APP_LOGS.debug("General Settings tab is Highlighted by default.");
					}
					else
					{
						APP_LOGS.debug("General Settings tab is not Highlighted by default and hence clicking on General settings tab.");
						getElement("Configuration_generalSettingsTab_Id").click();
					}
					
					//Verifying Project Dropdown availability
					String projectDDClassAttribute=getElement("Configuration_generalSettingsProjectDD_Id").getAttribute("class");
					String selectProjectLabel=getObject("Configuration_selectProjectLabel_Xpath").getText();
					
					boolean projectDDAvailable=verifyDDAvailbility(OR.getProperty("Configuration_generalSettingsDD_class"), 
							projectDDClassAttribute, "Project Dropdown",resourceFileConversion.getProperty("Configuration_selectProjectLabel"), 
							selectProjectLabel, "Select Project Label");

	            	if(projectDDAvailable==true)
	            	{
	            		//Selecting Project
	            		List<WebElement> listOfGSProjects=getElement("Configuration_generalSettingsProjectDD_Id").findElements(By.tagName("option"));
	            		selectElementFromDDByTitle(listOfGSProjects, projectName,"Project");
	            	}
	            	else
	            	{
	            		comments+="Project cannot be selected as Project Dropdown not available";
	            		APP_LOGS.debug("Project cannot be selected as Project Dropdown not available");
	            	}
	            	
	            	
	            	//Verifying Version Dropdown availability
	            	String versionDDClassAttribute=getElement("Configuration_generalSettingsVersionDD_Id").getAttribute("class");
	            	String selectVersionLabel=getObject("Configuration_selectVersionLabel_Xpath").getText();
	            	boolean versionDDAvailable=verifyDDAvailbility(OR.getProperty("Configuration_generalSettingsDD_class"), versionDDClassAttribute, 
	            			"Version Dropdown",resourceFileConversion.getProperty("Configuration_selectVersionLabel"), 
	            			selectVersionLabel, "Select Version Label");
	            			
	            	if(versionDDAvailable==true)
	            	{
	            		//Selecting version
	            		List<WebElement> listOfGSVersion=getElement("Configuration_generalSettingsVersionDD_Id").findElements(By.tagName("option"));
		            	selectElementFromDDByText(listOfGSVersion, Version, "Version");
	            	}
	            	else
	            	{
	            		comments+="Version cannot be selected as Version Dropdown not available";
	            		APP_LOGS.debug("Version cannot be selected as Version Dropdown not available");
	            	}
	            	
	            	
	            	//Verifying Test Pass Dropdown availability
	            	String testPassDDClassAttribute=getElement("Configuration_generalSettingsTestPassDD_Id").getAttribute("class");
	            	String selectTestPassLabel=getObject("Configuration_selectTestPassLabel_Xpath").getText();
	            	boolean testPassDDAvailable=verifyDDAvailbility(OR.getProperty("Configuration_generalSettingsDD_class"), 
	            			testPassDDClassAttribute, "TestPass Dropdown", resourceFileConversion.getProperty("Configuration_selectTestPassLabel"), 
	            			selectTestPassLabel, "Select Test Pass Label");
	            	
	            	if(testPassDDAvailable==true)
	            	{
	            		//Selecting Test Pass
		            	List<WebElement> listOfGSTestPass=getElement("Configuration_generalSettingsTestPassDD_Id").findElements(By.tagName("option"));
		            	selectElementFromDDByTitle(listOfGSTestPass, testPassName, "Test Pass");
	            	}
	            	else
	            	{
	            		comments+="Test Pass cannot be selected as Test Pass Dropdown not available";
	            		APP_LOGS.debug("Test Pass cannot be selected as Test Pass Dropdown not available");
	            	}
	            	
					//Verifying contents of General setting tab
	            	//Verifying the Test Pass level Options label
					String actualTPLevelOptionLabel=getElement("Configuration_TestPassLevelOption").getText();
					if(compareStrings(resourceFileConversion.getProperty("Configuration_testPAssLevelOptionLabel"), actualTPLevelOptionLabel))
					{
						APP_LOGS.debug("Test Pass level Options section is available on General Settings page");
						comments+="Test Pass level Options section is available on General Settings page(Pass)";
						
						//Verifying Test Pass level Options contents
						//Verifying Select Testing Type Label
						String actualSelectTestingTypeLabel=getObject("Configuration_selectTestingTypeLable_Xpath").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_expectedselectTestingTypeLabel"), actualSelectTestingTypeLabel, "'Select Testing Type'");
						
						//Verifying Select Testing Type option: Sequential Testing within a Test Case
						String sequentialTestingWithinTCLabel=getElement("Configuration_sequentialTestingWithinTCLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_sequentialTestingWithinTC"), sequentialTestingWithinTCLabel, "'Sequential Testing within a Test Case'");
						
						//Verifying if First option is selcted by default
						String testingTypeSelected=getElement("Configuration_testingTypeOptionRadionButton_Id").getAttribute("CHECKED");
						verifyTPLevelSelectedOption(resourceFileConversion.getProperty("Configuration_testPassLevelOptionSelected"),testingTypeSelected, "First Testing Type option");
						
						
						//Verifying Select Testing Type option: Sequential Testing within a Test Pass
						String sequentialTestingWithinTPLabel=getElement("Configuration_sequentialTestingWithinTPLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_sequentialTestingWithinTP"), sequentialTestingWithinTPLabel, "'Sequential Testing within a Test Pass'");

						//Verifying Select Testing Type option: Testing out of sequence
						String testingOutOfSequenceLabel=getObject("Configuration_testingOutOfSequenceLabel_Xpath").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_testingOutOfSequence"), testingOutOfSequenceLabel, "'Testing out of sequence'");

						
						//Verifying Select Feedback Rating Option Label
						String actualSelectFeedbackRatingLabel=getObject("Configuration_selectFeedbackRatingOption_Xpath").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_selectFeedbackRatingLabel"), actualSelectFeedbackRatingLabel, "'Select Feedback Rating Option'");
						
						//Verifying Select Feedback Rating Option: After completion of each Test Pass activity.
						String afterCompletionOfTPLabel=getElement("Configuration_afterCompletionOfTPLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_afterCompletionOfTPLabel"), afterCompletionOfTPLabel, "'After completion of each Test Pass activity.'");
						
						//Verifying if First option is selcted by default
						String feedbackRatingOptionSelected=getElement("Configuration_feedbackOptionRadionButton_Id").getAttribute("CHECKED");
						verifyTPLevelSelectedOption(resourceFileConversion.getProperty("Configuration_testPassLevelOptionSelected"),feedbackRatingOptionSelected , "First Feedback Rating Option");
						
						//Verifying Select Feedback Rating Option: After completion of each (Pass) Test Case.
						String afterCompletionOfPassTCLabel=getElement("Configuration_afterCompletionOfPassTCLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_afterCompletionOfPassTCLabel"), afterCompletionOfPassTCLabel, "'After completion of each (Pass) Test Case.'");

						//Verifying Select Feedback Rating Option: No Rating popup
						String noRatingPopupLabel=getElement("Configuration_noRatingPopupLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_noRatingPopupLabel"), noRatingPopupLabel, "'No Rating popup'");

						
						//Verifying Test Step Name Constraint: Label
						String actualTSNameConstraintLabel=getElement("Configuration_TSNameConstraint_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_TSNameConstraintLabel"), actualTSNameConstraintLabel, "'Test Step Name Constraint'");
						
						//Verifying Test Step Name Constraint Option: Allow same Test Step name within Test Case
						String sameTSNameAllowedLabel=getElement("Configuration_sameTSNameAllowedLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_sameTSNameAllowedLabel"), sameTSNameAllowedLabel, "'Same TestStep Name Allowed'");
						
						//Verifying if First option is selcted by default
						String testStepNameConstraintSelected=getElement("Configuration_TSNameConstraintRadionButton_Id").getAttribute("CHECKED");
						verifyTPLevelSelectedOption(resourceFileConversion.getProperty("Configuration_testPassLevelOptionSelected"),testStepNameConstraintSelected , "First Test Step Name Constraint");
						
						
						//Verifying Test Step Name Constraint Option: Don't allow same Test Step name within Test Case
						String sameTSNameNotAllowedLabel=getElement("Configuration_sameTSNameNotAllowedLabel_Id").getText();
						verifyContent(resourceFileConversion.getProperty("Configuration_sameTSNameNotAllowedLabel"), sameTSNameNotAllowedLabel, "'Same TestStep Name Not Allowed'");
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Test Pass level Options section is not available on General Settings page");
						comments+="Test Pass level Options section is not available on General Settings page";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TPLevelOptionsSectionNotAvailable");
					}
					
	    /*        	//Verify Auto approval for Testing? Checkbox Label
	            	String autoApprovalForTestingText=getObject("Configuration_autoApprovalTestingLabel_Xpath").getText();
	            	verifyContent(resourceFileConversion.getProperty("Configuration_autoApprovalTestingLabel"), autoApprovalForTestingText, "Auto approval for Testing");
	           	
	            	//Verify Project Level Options: Role Configuration:
	            	String projectLevelOptionsLabel=getElement("Configuration_projectLevelOptionsLabel_Id").getText();
	            	String roleConfigurationLabel=getElement("Configuration_roleConfigurationLabel_Id").getText();
	            	verifyContent(resourceFileConversion.getProperty("Configuration_projectLevelOptionsLabel"), projectLevelOptionsLabel, "Project Level Options");
	            	verifyContent(resourceFileConversion.getProperty("Configuration_roleConfigurationLabel"), roleConfigurationLabel, "Role Configuration");
	      */ 
	            }
	            catch (Throwable t) 
				{
	            	t.printStackTrace();
	            	fail = true;
					APP_LOGS.debug("Exception occurred while verifying General Settings contents.");
					comments += "Exception occurred while verifying General Settings contents. ";
					
					throw new SkipException("Exception occurred while verifying General Settings contents");
				}
			 }
			 else
			 {
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Test Manager");
	           	comments="Failed Login for Role: Test Manager(Fail)";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestManagerLoginFailed");
			 }
			 APP_LOGS.debug("Closing Browser... ");
 	         closeBrowser();

		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");
			comments+="Login Not Successful";
		}
	}
	private void selectElementFromDDByTitle(List<WebElement> listOfElements,String selectedElement,String element)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getAttribute("title").equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(element+" is selected");
				break;
			}
    	}
	}
	
	private void selectElementFromDDByText(List<WebElement> listOfElements,String selectedElement,String element)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getText().equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(element+" is selected");
				break;
			}
    	}
	}
	
	private void verifyContent(String expected, String actual, String element)
	{
		if(compareStrings(expected, actual))
		{
			APP_LOGS.debug(element+" is displayed");
			comments+=element+" is displayed(Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" is not displayed");
			comments+=element+" is not displayed(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"Notdisplayed");
		}
	}
	
	private boolean verifyDDAvailbility(String expected, String actual, String element, String expectedLabel, String displayedLabel, 
			String labelOfElement)
	{
		if(compareStrings(expected, actual))
		{
			APP_LOGS.debug(element+" is displayed");
			comments+=element+" is displayed(Pass).";
			//Verify Dropdown Label
			verifyContent(expectedLabel,displayedLabel , labelOfElement);
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" is not displayed");
			comments+=element+" is not displayed(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"Notdisplayed");
			return false;
		}
	}
	
	private void verifyTPLevelSelectedOption(String expected, String actual, String element)
	{
		if(compareStrings(expected, actual))
		{
			APP_LOGS.debug(element+" is selected by default");
			comments+=element+" is selected by default(Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" is not selected by default");
			comments+=element+" is not selected by default(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"NotSelected");
		}
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ConfigurationSuiteXls, this.getClass().getSimpleName()) ;
	}
}
