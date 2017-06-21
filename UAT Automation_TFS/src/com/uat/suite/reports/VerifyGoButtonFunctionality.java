/* Author Name:-Aishwarya Deshpande
 * Created Date:-4th Dec 2014
 * Last Modified on Date:- 13th Feb 2015
 * Module Description:- Verification of Go button functionality on Reports Page with the positive data  
 */

package com.uat.suite.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

public class VerifyGoButtonFunctionality extends TestSuiteBase{
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	int FailedTS_count;
	int PassedTS_count;
	int TestStepCreated_count;
	int NotCompletedTS_count;
	String PassedTS;
	String FailedTS;
	String NotCompletedTS;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	ArrayList<String> testersArray = new ArrayList<String>();

	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing VerifyGoButtonFunctionality Test Case");

		if(!TestUtil.isTestCaseRunnable(ReportsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ReportsSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyGoButtonFunctionality(String Role, String GroupName, String Portfolio, String ProjectName1, 
			String ProjectDescription, String ProjectName2, String ProjectName3, String Version, String endMonth,
			String endYear, String endDate, String VersionLead, String testPassName1, String TP_description,
			String testManager, String testPassName2, String testPassName3, String testerName, String Role1, String Role2,
			String testCaseName1, String testCaseName2, String testStep1, String testStep2, String testStep3, 
			String testStep4, String assignedRole, String expectedResult, String expectedNoProjectMessage,
			String expectedNoTPAvailableMessage, String expectedNoTesterAvailableMessage,
			String expectedNoTSMessage) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		comments="";		
		FailedTS_count=0;
		PassedTS_count=0;
		TestStepCreated_count=0;
		NotCompletedTS_count=0;
		PassedTS="";
		FailedTS="";
		NotCompletedTS="";
		
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
			 
			 //Tester 
			 int tester_count = Integer.parseInt(testerName);
			 tester=new ArrayList<Credentials>();
			 tester = getUsers("Tester", tester_count);

			 //click on testManagement tab
			 APP_LOGS.debug(" Clicking on Test Management Tab ");
			 getElement("UAT_testManagement_Id").click();
			 Thread.sleep(100);
			
			 if(!createProject(GroupName, Portfolio, ProjectName1, Version, ProjectDescription, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName1+" Project not Created Successfully(Fail).");
				comments=ProjectName1+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject1Creation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName1+" Project Created Successfully.");
			
			 if(!createProject(GroupName, Portfolio, ProjectName2, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName2+" Project not Created Successfully(Fail).");
				comments=ProjectName2+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject2Creation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName2+" Project Created Successfully.");
			
			 if(!createProject(GroupName, Portfolio, ProjectName3, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName3+" Project not Created Successfully(Fail).");
				comments=ProjectName3+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject3Creation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName3+" Project Created Successfully.");
			
		     //Extracting the Project ID with a Test Pass with a failed test step
			 String actualProjectID=searchProjectAndExtractID(ProjectName1, Version);
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName1, Version, testPassName1,TP_description, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully(Fail).");
				comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTP1Creation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName1+" Test Pass Created Successfully.");
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName1, Version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully(Fail).");
				comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTP2Creation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName3, Version, testPassName3, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName3+" Test Pass not Created Successfully(Fail).");
				comments+=testPassName3+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTP3Creation");
				closeBrowser();

				throw new SkipException("Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName3+" Test Pass Created Successfully.");
		
			 //Extracting the Test Pass Id which has a failed test step
			 Thread.sleep(3000);
			 String expectedTestPassID=searchTPAndExtractID(GroupName, Portfolio, ProjectName1, Version, testPassName1);  
		
			 if(!createTester(GroupName, Portfolio, ProjectName1, Version, testPassName1, tester.get(0).username, Role1, Role1))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+Role1+"not Created Successfully(Fail).");
				comments+="Tester with role "+Role1+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
				closeBrowser();

				throw new SkipException("Tester not created successfully");
			 }
			 APP_LOGS.debug(" Tester with role "+Role1+" Created Successfully.");

		/*	 Thread.sleep(2000);
			 getElement("TesterNavigation_Id").click();
				
			 dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
				
			 dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
				
			 dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName1 );
				
			 dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
				
			 dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName1 );
*/
			
			 List<WebElement> listOfTesters=getObject("TesterViewAll_Table").findElements(By.tagName("tr"));
			 for(int i=1;i<=listOfTesters.size();i++)
			 {
				 String testerNames=getObject("TesterViewAll_testerNameXpath1","TesterViewAll_testerNameXpath2",i).getText();
				 testersArray.add(testerNames);
			 }

			 if(!createTester(GroupName, Portfolio, ProjectName3, Version, testPassName3, tester.get(1).username, Role2, Role2))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+Role2+"not Created Successfully(Fail).");
				comments+="Tester with role "+Role2+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester2Creation");
				closeBrowser();

				throw new SkipException("Tester not created successfully");
			 }
			 APP_LOGS.debug(" Tester with role "+Role2+" Created Successfully.");
			 
			 List<WebElement> listOfTesters1=getObject("TesterViewAll_Table").findElements(By.tagName("tr"));
			 for(int i=1;i<=listOfTesters1.size();i++)
			 {
				 String testerNames=getObject("TesterViewAll_testerNameXpath1","TesterViewAll_testerNameXpath2",i).getText();
				 testersArray.add(testerNames);
			 }
			 
			 if(!createTestCase(GroupName, Portfolio, ProjectName1, Version, testPassName1, testCaseName1))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testCaseName1+" Test Case not Created Successfully(Fail).");
				comments+=testCaseName1+" Test Case not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTC1Creation");
				closeBrowser();

				throw new SkipException("Test Case not created successfully");
			 }
			 APP_LOGS.debug(testCaseName1+" Test Case Created Successfully.");
			
			 if(!createTestCase(GroupName, Portfolio, ProjectName3, Version, testPassName3, testCaseName2))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testCaseName2+" Test Case not Created Successfully(Fail).");
				comments+=testCaseName2+" Test Case not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTC2Creation");
				closeBrowser();

				throw new SkipException("Test Case not created successfully");
			 }
			 APP_LOGS.debug(testCaseName2+" Test Case Created Successfully.");
		
			 if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStep1, expectedResult, testCaseName1, assignedRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStep1+" Test Step not Created Successfully(Fail).");
				comments+=testStep1+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTS1Creation");
				closeBrowser();

				throw new SkipException("Test Step not created successfully");
			 }
			 TestStepCreated_count++;
			 APP_LOGS.debug(testStep1+" Test Step Created Successfully.");
			
			 if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStep2, expectedResult, testCaseName1, assignedRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStep2+" Test Step not Created Successfully(Fail).");
				comments+=testStep2+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTS2Creation");
				closeBrowser();

				throw new SkipException("Test Step not created successfully");
			 }
			 TestStepCreated_count++;
			 APP_LOGS.debug(testStep2+" Test Step Created Successfully.");
			
			 if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStep3, expectedResult, testCaseName1, assignedRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStep3+" Test Step not Created Successfully(Fail).");
				comments+=testStep3+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTS3Creation");
				closeBrowser();

				throw new SkipException("Test Step not created successfully");
			 }
			 TestStepCreated_count++;
			 APP_LOGS.debug(testStep3+" Test Step Created Successfully.");
			
			 if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStep4, expectedResult, testCaseName1, assignedRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStep4+" Test Step not Created Successfully(Fail).");
				comments+=testStep4+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTS4Creation");
				closeBrowser();

				throw new SkipException("Test Step not created successfully");
			 }
			 TestStepCreated_count++;
			 APP_LOGS.debug(testStep4+" Test Step Created Successfully.");
			 
			 NotCompletedTS_count=TestStepCreated_count;
			 
			 APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
			 comments+="Data has been made Successfully from Test Management tab... ";
			 
			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
			
 	         APP_LOGS.debug("Opening Browser... ");
			 openBrowser();
			
			 if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			 {
	            APP_LOGS.debug("Logged in browser with Tester ");
	            
	            try
	            {
	 	        	if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName1, Version, testPassName1)) 
					{
						fail = true;
						APP_LOGS.debug("Begin Testing has not been clicked using provided details. Test case has been failed(Fail).");
						comments+="Fail Occurred:- Begin Testing has not been clicked... ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Begin Testing has not been clicked");
						
						throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
					}
				
					APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");
					clickOnPASSRadioButtonAndSave();
					PassedTS_count++;
					NotCompletedTS_count--;
					
					APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");
					clickOnPASSRadioButtonAndSave();
					PassedTS_count++;
					NotCompletedTS_count--;
					
					APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");
					clickOnFAILRadioButtonAndSave();
					FailedTS_count++;
					NotCompletedTS_count--;
					
					APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");
					clickOnFAILRadioButtonAndSave();
				    FailedTS_count++;
				    NotCompletedTS_count--;
				     
					PassedTS=String.valueOf(PassedTS_count);
					FailedTS=String.valueOf(FailedTS_count);
					NotCompletedTS=String.valueOf(NotCompletedTS_count);
					Thread.sleep(1000);
					
					APP_LOGS.debug("Clicking on very Satisfied Radio Button");
					getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on Save button of Rating popup");
					getObject("TestingPageRatingPopup_saveButton").click();
					Thread.sleep(500);
					
					APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
					getObject("TestingPage_returnToHomeButton").click();
							
					Thread.sleep(1000);
	 	        }
	 	        catch (Throwable t) 
				{
	 	        	t.printStackTrace();
					fail = true;
					assertTrue(false);
					APP_LOGS.debug("Exception occurred: Testing the Test Management data from dashboard page. Test Case has been failed.");
					comments += "Exception occurred: While Testing the Test Management data from dashboard page.";
				}
	 		 }
			 else
			 {
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Tester");
	           	comments="Failed Login for Role: Tester(Fail)";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
			 }
			 APP_LOGS.debug("Closing Browser... ");
 	         closeBrowser();
	
 			 APP_LOGS.debug("Opening Browser... ");
			 openBrowser();
			 try
			 {	
				if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				{
					APP_LOGS.debug("Logged in browser with Version Lead");
	            
					// Clicking on Reports Tab
					getElement("UAT_reports_Id").click();
					APP_LOGS.debug("Reports tab clicked ");
					Thread.sleep(2000);
				
 					getObject("Report_GoButton").click();
					
 					String actualNoProjectMessage=getElement("ProjectViewAll_projectDeletedConfirmationText_Id").getText();
					if(compareStrings(expectedNoProjectMessage, actualNoProjectMessage))
					{
						APP_LOGS.debug("No Project selected message verified(Pass)");
						comments+="No Project selected message verified(Pass)";
						getObject("Tester_testeraddedsuccessfullyOkButton").click();
					}
					else{
						fail=true;
						APP_LOGS.debug("No Project selected message doesnot match(Fail)");
						comments+="No Project selected message doesnot match(Fail)";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoProjectMessageNotMatching");
					}
					Thread.sleep(500);
					
					//Verification for no Test Pass available
					List<WebElement> listOfProjectsName=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName, ProjectName2, "Project");
					
					Select listOfVersions =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions.selectByVisibleText(Version);
					
					getObject("Report_GoButton").click();
					
					String actualNoTPAvailableMessage=getObject("Reports_ExtractNoTPAvailableText").getText();
					verifyMessage(expectedNoTPAvailableMessage, actualNoTPAvailableMessage);
					Thread.sleep(500);
					
					//Verification for No Tester available message
					getElement("UAT_reports_Id").click();		
					Thread.sleep(1000);
					List<WebElement> listOfProjectsName1=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName1, ProjectName1, "Project");
					Select listOfVersions1 =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions1.selectByVisibleText(Version);
					
					List<WebElement> listOfTestPass=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTestPass, testPassName2, "Test Pass");

					getObject("Report_GoButton").click();

					List<WebElement> listOfTester=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					String actualNoTesterMessage=listOfTester.get(0).getText();
					verifyMessage(expectedNoTesterAvailableMessage, actualNoTesterMessage);
					Thread.sleep(500);
					
					//Verification for No Test Step available message
					List<WebElement> listOfProjectsName2=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName2, ProjectName3, "Project");
					Select listOfVersions2 =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions2.selectByVisibleText(Version);

					List<WebElement> listOfTestPass1=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTestPass1, testPassName3, "Test Pass");
				//	String tester1Name=testersArray.get(0); 
					List<WebElement> listOfTester1=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTester1, testersArray.get(1), "Tester");
					
					getObject("Report_GoButton").click();

					String actualNoTSMessage=getObject("Triage_testStepDetailSection").getText();
					verifyMessage(expectedNoTSMessage, actualNoTSMessage);
					Thread.sleep(500);
					
					//Verification for Test Steps after selecting a Test Pass
					APP_LOGS.debug("Verification for Test Steps after selecting a Test Pass");
					List<WebElement> listOfProjectsName3=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName3, ProjectName1, "Project");
					Select listOfVersions3 =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions3.selectByVisibleText(Version);

					List<WebElement> listOfTestPass2=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTestPass2, testPassName1, "Test Pass");
					
					getObject("Report_GoButton").click();
					
					verifyProjectandTPDetails(ProjectName1,actualProjectID,Version,ProjectDescription,endMonth,endDate,endYear,expectedTestPassID,testPassName1,TP_description,PassedTS,FailedTS,NotCompletedTS);
					
					String actualTSArray[]=verifyTSContents();
					String expectedTSArray[]={testStep1,testStep2, testStep3, testStep4 };
					DDContentVerifiaction(expectedTSArray, actualTSArray);
					Thread.sleep(500);
					
					//Verification for Test Steps after selecting a Tester
					APP_LOGS.debug("Verification for Test Steps after selecting a Tester");
					selectDDContent(listOfProjectsName3, ProjectName1, "Project");

					listOfVersions3.selectByVisibleText(Version);

					selectDDContentByText(listOfTestPass2, testPassName1, "Test Pass");
					
					List<WebElement> listOfTester2=getElement("Triage_testerDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTester2, testersArray.get(0), "Tester");
					
					getObject("Report_GoButton").click();
					
					String actualTSArray1[]=verifyTSContents();
					DDContentVerifiaction(expectedTSArray, actualTSArray1);
					Thread.sleep(500);
					
					//Verification for Test Steps after selecting a Status
					APP_LOGS.debug("Verification for Test Steps after selecting a Status");
					selectDDContent(listOfProjectsName3, ProjectName1, "Project");

					listOfVersions3.selectByVisibleText(Version);

					selectDDContentByText(listOfTestPass2, testPassName1, "Test Pass");
					
					selectDDContentByText(listOfTester2, testersArray.get(0), "Tester");
					
					Select listOfStatus =  new Select(getElement("Reports_StatusDropDown_Id"));
					listOfStatus.selectByVisibleText("Fail");
					APP_LOGS.debug("Status is selected");
					getObject("Report_GoButton").click();
					
					String expectedTSStatusArray[]={testStep3,testStep4};
					
					String actualTSArray2[]=verifyTSContents();
					DDContentVerifiaction(expectedTSStatusArray, actualTSArray2);
					
 			   }
			   else
			   {
					fail=true;
					APP_LOGS.debug("Failed Login for Role: Version Lead");
		           	comments="Failed Login for Role: Version Lead(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"VLLoginFailed");
				}
			    APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser... ");
				openBrowser();
				
				if(login(tester.get(0).username, tester.get(0).password, "Tester"))
				{
					APP_LOGS.debug("Logged in browser with Version Lead");
	            
					// Clicking on Reports Tab
					getElement("UAT_reports_Id").click();
					APP_LOGS.debug("Reports tab clicked ");
					Thread.sleep(2000);
					
					APP_LOGS.debug("Verification for Version after Logged in as a tester");
					List<WebElement> listOfProjectsName3=getElement("Triage_projectDropDown_Id").findElements(By.tagName("option"));
					selectDDContent(listOfProjectsName3, ProjectName1, "Project");
					Select listOfVersions3 =  new Select(getElement("Triage_versionDropDown_Id"));
					listOfVersions3.selectByVisibleText(Version);

					List<WebElement> listOfTestPass2=getElement("Triage_testPassDropDown_Id").findElements(By.tagName("option"));
					selectDDContentByText(listOfTestPass2, testPassName1, "Test Pass");
					
					getObject("Report_GoButton").click();
					String expectedVersion="Version - "+Version;
					String displayedVersion=getElement("Reports_ExtractVersion").getText();
					verifyDisplayedcontent(expectedVersion, displayedVersion,"Version");

				}
				else
				{
					fail=true;
					APP_LOGS.debug("Failed Login for Role:  Tester(Fail)");
		           	comments="Failed Login for Role: Tester(Fail)";
		           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
				}
 			 }  
			 catch(Throwable t)
	         {
	         	 fail=true;
	         	 assertTrue(false);
	           	 t.printStackTrace();
	           	 comments="Failed Login for Role" +Role;
	           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
	         }
			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");	
		}	
	}
	
	private void selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		for(int i=1;i<listOfElements.size();i++){
			String actualOption=listOfElements.get(i).getAttribute("title");
			if(actualOption.equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}	
	}
	
	private void selectDDContentByText(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		for(int i=1;i<listOfElements.size();i++)
		{	
			String actualOption=listOfElements.get(i).getText();
			if(actualOption.equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}
	}
	
	private void verifyMessage(String expectedMessage, String actaulMessage)
	{
		if(compareStrings(expectedMessage, actaulMessage))
		{
			APP_LOGS.debug(expectedMessage+" message verified(Pass)");
			comments+=expectedMessage+" message verified(Pass)";
		}
		else{
			fail=true;
			APP_LOGS.debug(expectedMessage+" message doesnot match(Fail)");
			comments+=expectedMessage+" message doesnot match(Fail)";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), expectedMessage+"MessageNotMatching");
		}
	}	
	
	private boolean createProject(String group, String portfolio, String project, String version, String description,String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");

		getObject("Projects_createNewProjectLink").click();
		
		try 
		{
			dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
			
			dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
			
			dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
			
			getObject("ProjectCreateNew_versionTextField").sendKeys(version);
			
			getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(description);
			
			selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
			
			getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
			
			Thread.sleep(500);driver.switchTo().frame(1);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
			
			getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			waitForElementVisibility("ProjectCreateNew_versionLeadStakeholderSelectSearchResult",10).click();

	//		getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			
			getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			
			driver.switchTo().defaultContent();
			
			//getObject("ProjectCreateNew_projectSaveBtn").click();
			
			click(getObject("ProjectCreateNew_projectSaveBtn"));
			
			if (getTextFromAutoHidePopUp().contains("successfully")) 
			{
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, String description, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2000);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(2000);
		getObject("TestPasses_createNewProjectLink").click();
		
		try 
		{
			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
			
			getElement("TestPassCreateNew_descriptionTextField_ID").sendKeys(description);
			
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			
			Thread.sleep(500);driver.switchTo().frame(1);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
			
			getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
			
			waitForElementVisibility("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult",10).click();
			
	//		getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
	   
			getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
			
			driver.switchTo().defaultContent();
			
			selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
			
		//	getObject("TestPassCreateNew_testPassSaveBtn").click();
			
			click(getObject("TestPassCreateNew_testPassSaveBtn"));
			
			if (getTextFromAutoHidePopUp().contains("successfully")) 
			{
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
	}
	
	private void verifyDisplayedcontent(String expectedName, String displayedName, String ContentType)
	{
		if(assertTrue(expectedName.equalsIgnoreCase(displayedName)))
		{
			APP_LOGS.debug(ContentType+" is displayed(Pass)");
			comments+=ContentType+" is displayed(Pass).";
		}
		else{
			fail=true;
			APP_LOGS.debug(ContentType+" is not displayed(Fail)");
			comments+=ContentType+" is not displayed(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), ContentType+"NotMatching");
		}
	}
	
	private String[] verifyTSContents()
	{
		List<WebElement> listOfTestSteps =getObject("Traige_testStepGrid").findElements(By.tagName("tr"));
		String actualArray[] = new String[listOfTestSteps.size()];
		for(int i=0;i<listOfTestSteps.size();i++)
		{	
			actualArray[i]=getObject("Triage_failedTestStepNameXpath1", "Triage_testPassGrid_TPDescription2", i+1).getAttribute("title");
		}
		return actualArray;
	}
	
	private void DDContentVerifiaction(String expectedContentArray[], String actualContentArray[])
	{
		if(compareIntegers(expectedContentArray.length,actualContentArray.length))
		{
			for(int i=0;i<expectedContentArray.length;i++)
			{
				if(assertTrue(expectedContentArray[i].equalsIgnoreCase(actualContentArray[i])))
				{
					comments+=expectedContentArray[i]+" present in the dropdown(Pass).";
					APP_LOGS.debug(expectedContentArray[i]+" present in the dropdown(Pass)");
				}
				else
				{
					fail=true;
					comments+=expectedContentArray[i]+" not present in the dropdown(Fail)";
					APP_LOGS.debug(expectedContentArray[i]+" not present in the dropdown(Fail)");
				}
			}
		}
		else
		{
			fail=true;
			comments+="Total Number of elements in the dropdown do not match(Fail)";
			APP_LOGS.debug("Total Number of elements in the dropdown do not match(Fail)");
		}
	}

	private void verifyProjectandTPDetails(String ProjectName, String ProjectId,String Version,String description, String endMonth,String endDate, 
			String endYear,String testPassId,String testPassName, String testPassDescription, String PassedTS, 
			String FailedTS, String NCTS) throws IOException
	{
		//Project Details
		String expectedProjectName="Project - "+ProjectName;
		String expectedVersion="Version - "+Version;
		String expectedProjectID="(#ID: "+ProjectId+")";
		
		int indexOfID = getElement("Triage_ExtractProjectName").getText().indexOf("(#ID");
		
		String displayedProjectName=getElement("Triage_ExtractProjectName").getText().substring(0, indexOfID);
		verifyDisplayedcontent(expectedProjectName, displayedProjectName,"Project Name");

		String displayedProjectID=getObject("Triage_ExtractProjectID").getText();
		verifyDisplayedcontent(expectedProjectID, displayedProjectID,"Project ID");
		
		String displayedVersion=getElement("Reports_ExtractVersion").getText();
		verifyDisplayedcontent(expectedVersion, displayedVersion,"Version");

		String displayedDescroption=getElement("Triage_ExtractProjectDescription").getText();
		verifyDisplayedcontent(description, displayedDescroption,"Project Description");
		
		
		//Test Pass Details
		List<WebElement> listOfTestPasses=getObject("Triage_testPassGrid").findElements(By.tagName("tr"));
		for(int i=1;i<=listOfTestPasses.size();i++)
		{
			String displayedTPID=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPID2", i).getText();
			String displayedTPName=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPName2", i).getText();
			String displayedTPDescription=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPDescription2", i).getText();
			String displayedTestManager=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TestManger2", i).getText();
			String displayedTPEndDate=getObject("Triage_testPassGridDetails1", "Triage_testPassGrid_TPEndDate2", i).getText();
			String dispalyedPassedTS=getObject("Triage_testPassGridDetails1", "Reports_testPassGrid_PassedTS", i).getText();
			String dispalyedFailedTS=getObject("Triage_testPassGridDetails1", "Reports_testPassGrid_FailedTS", i).getText();
			String dispalyedNCTS=getObject("Triage_testPassGridDetails1", "Reports_testPassGrid_NCTS", i).getText();
			
			String expectedManager=test_Manager.get(0).username.replace(".", " ");
			String month=getMonthNumber(endMonth);
			String expectedEndDate=month+"-"+endDate+"-"+endYear;
			
			verifyDisplayedcontent(testPassId, displayedTPID,"Test Pass ID");
			verifyDisplayedcontent(testPassName, displayedTPName, "Test Pass Name");
			verifyDisplayedcontent(testPassDescription, displayedTPDescription, "Test Pass Description");
			verifyDisplayedcontent(expectedManager, displayedTestManager,"Test Manager");
			verifyDisplayedcontent(expectedEndDate, displayedTPEndDate, "Due Date");
			verifyDisplayedcontent(PassedTS, dispalyedPassedTS,"Passed Test Steps in TP");
			verifyDisplayedcontent(FailedTS, dispalyedFailedTS,"Failed Test Steps in TP");
			verifyDisplayedcontent(NCTS, dispalyedNCTS,"Not Completed Test Steps in TP");
		}	
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ReportsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ReportsSuiteXls, "Test Cases", TestUtil.getRowNum(ReportsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ReportsSuiteXls, this.getClass().getSimpleName()) ;
	}
}
