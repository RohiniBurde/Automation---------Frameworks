package com.uat.suite.tm_testpass;


import java.io.IOException;
import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyEditFunctionForUsers extends TestSuiteBase 
{

	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeHolders;
	String runmodes[]=null;
	Utility utilRecorder = new Utility();
	
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
		
		testManager=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		stakeHolders=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyEditDoneByValidUsers(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL,
			String TestPassName,String EditedTestPassName,String TP_Description, String Updated_TP_Description, String TP_Status, String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate, String TP_EditedEndMonth, String TP_EditedEndYear, String TP_EditedEndDate, String Assert_UpdatedText) throws Exception
		{
		// test the runmode of current dataset
			count++;
			comments+="";
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				throw new SkipException("Runmode for test set data set to no "+count);
			}
			
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);
			
			int stakeHolders_count = Integer.parseInt(Stakeholder);
			stakeHolders = getUsers("Stakeholder", stakeHolders_count);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
			openBrowser();
			
			isLoginSuccess = login(role);
			
			boolean isLoginSuccessNewRole;
			
			if (isLoginSuccess) 
			{
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(500);
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, 
						versionLead.get(0).username, stakeHolders.get(0).username))
				{
					fail=true;
					APP_LOGS.debug(ProjectName +" Project not Created Successfully.");
					comments=ProjectName +" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessful");
					closeBrowser();
					assertTrue(false);
					throw new SkipException("Project Successfully not created");
					
				}
				
				APP_LOGS.debug(ProjectName+" Project Created Successfully.");
				comments= ProjectName+" Project Created Successfully(Pass). ";
				
				closeBrowser();
				
				openBrowser();
				
				if(role.equalsIgnoreCase("StakeHolder"))
					isLoginSuccessNewRole = login(stakeHolders.get(0).username, stakeHolders.get(0).password, "Stakeholder");
				else
					isLoginSuccessNewRole = login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead");
				
				if (isLoginSuccessNewRole) 
				{
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(500);
				
				
					getElement("TM_testPassesTab_Id").click();
				
					Thread.sleep(500);
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
					
					selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, VersionLead);
					
					if(enterValidDataInMandatoryFieldsOfTestPass(TestPassName, TP_Description, TP_Status, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
					{
						comments = "Valid data in mandatory fields of test pass has been entered and saved successfully... ";
					}
					
					else 
					{
						comments = "Fail occurred: Entering Valid data in mandatory fields of test pass... ";
						fail = true; 
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in creating test pass");
						closeBrowser();
						assertTrue(false);
						throw new SkipException("Exception in creating test pass");
						
					}
					
					APP_LOGS.debug("Click on View All");
					//getObject("TestPasses_viewAllProjectLink").click();
					if(role.equalsIgnoreCase("Test Manager"))
					{
						enterValidDataInMandatoryFieldsOfTestPass(TestPassName+1, TP_Description, TP_Status, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate);

						closeBrowser();
						
						openBrowser();
						
						if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
						{
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(500);
							getElement("TM_testPassesTab_Id").click();
							
							Thread.sleep(500);
							APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
							
							selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, VersionLead);	
						}
						else
						{
							fail=true;
							comments+="Test Pass was created but assigned test manager did not get the required access (FAIL)";
							throw new SkipException("Test Pass was created but assigned test manager did not get the required access");		
						}
						
					}
					if(!searchForTheTestPass(TestPassName))
					{
						comments = "Fail occurred: The saved test pass not visible to "+role+" of the project";
						fail = true; 
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in visibility of test pass to "+role);
						closeBrowser();
						assertTrue(false);
						throw new SkipException("Exception in creating test pass");
					}
					else
						comments+="The saved test pass was visible to "+role+" of the project (PASS)";
					
					
					if(!getElement("TestPass_EditFormPage_Id").isDisplayed())
					{
						
						comments = comments+ "Edit Test Pass Page is  not displayed... (FAIL) ";
					
						APP_LOGS.debug("Edit Test Pass Page is not displayed");
						
						fail = true;
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Test Pass Page is not displayed after clicking edit");
				
						closeBrowser();
						
						assertTrue(false);
						
						throw new SkipException("Edit Test Pass Page is not displayed");
					
					}
					else
						comments+="Edit Test Pass Page displayed on clicking edit icon (PASS)";
					
					if(!verifyTestPassFields(TestPassName, TP_Description, TP_Status, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
					{
						comments = comments+ "Saved contents by "+role+" not visible in test pass details... (FAIL) ";
						
						APP_LOGS.debug("Saved contents by "+role+" not visible in test pass details");
						
						fail = true;
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Saved contents by "+role+" not visible in test pass details");
				
						closeBrowser();
						
						assertTrue(false);
						
						throw new SkipException("Saved contents by "+role+" not visible in test pass details");
					}
					else
						comments+="Saved contents by "+role+" visible in test pass details (PASS)";
					
					if(!editTestPass(EditedTestPassName, Updated_TP_Description, Status, testManager.get(1).username, TP_EditedEndMonth, TP_EditedEndYear, TP_EditedEndDate, Assert_UpdatedText))
					{
						comments = comments+ role+" was unable to update the contents... (FAIL) ";
						
						APP_LOGS.debug(role+" was unable to update the contents");
						
						fail = true;
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),role+" was unable to update the contents");
				
						closeBrowser();
						
						assertTrue(false);
						
						throw new SkipException(role+" was unable to update the contents");
					}
					else
						comments+=role+" was able to update the contents (PASS)";
					
					//getObject("TestPasses_viewAllProjectLink").click();
				
					if(!role.equalsIgnoreCase("Test Manager")&&!searchForTheTestPass(EditedTestPassName))
					{
						comments = "Fail occurred: The edited test pass not visible to "+role+" of the project";
						fail = true; 
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in visibility of edited test pass to "+role);
						closeBrowser();
						assertTrue(false);
						throw new SkipException("Exception in updating test pass");
						
					}
					else if(role.equalsIgnoreCase("Test Manager")&&!searchForTheTestPass(EditedTestPassName))
					{
						comments+="As test manager updated himself, he is no more able to view the test pass. Thats correct (PASS)";
						closeBrowser();
						openBrowser();
						if(login(testManager.get(1).username,testManager.get(1).password, "Test Manager"))
						{
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(500);
							
							getElement("TM_testPassesTab_Id").click();
							
							Thread.sleep(500);
							APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
							
							selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, VersionLead);
						}
						else
						{
							fail=true;
							comments+="Test Pass was updated by test manager but assigned test manager did not get the required access (FAIL)";
							throw new SkipException("Test Pass was updated by test manager but assigned test manager did not get the required access");		
						}
						
						if(!searchForTheTestPass(EditedTestPassName))
						{
							comments = "Fail occurred: The saved test pass not visible to updated "+role+" of the project";
							fail = true; 
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in visibility of test pass to updated "+role);
							closeBrowser();
							assertTrue(false);
							throw new SkipException("Exception in visibility of test pass to updated test manager");
						}
						else
							comments+="The updated test pass was visible to updated "+role+" of the project (PASS)";
						
					
					}
					else
						comments+="The edited test pass was visible to "+role+" of the project (PASS)";
					
					if(!verifyTestPassFields(EditedTestPassName, Updated_TP_Description, Status, testManager.get(1).username, TP_EditedEndMonth, TP_EditedEndYear, TP_EditedEndDate))
					{
						comments = comments+ "Edited contents by "+role+" not visible in test pass details... (FAIL) ";
						
						APP_LOGS.debug("Edited contents by "+role+" not visible in test pass details");
						
						fail = true;
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edited contents by "+role+" not visible in test pass details");
				
						closeBrowser();
						
						assertTrue(false);
						
						throw new SkipException("Edited contents by "+role+" not visible in test pass details");
					}
					else
						comments+="Edited contents by "+role+" was visible in test pass details (PASS)";
					
					
					APP_LOGS.debug("Closing the browser");
					closeBrowser();	
				}
				else 
				{
					fail=true;
					comments+="Could not login with "+role+" credential";
				}
			}
			else 
			{
				fail=true;
			}

	}
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	public boolean enterValidDataInMandatoryFieldsOfTestPass(String TestPassName, String TP_Description, String TP_Status, String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate ) throws InterruptedException, IOException
	{
			
		try
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
			
			getObject("TestPasses_createNewProjectLink").click();
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
			getObject("TestPasses_createNewProjectLink").click();
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
			
			getElement("TestPass_Description_Id").sendKeys(TP_Description);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
			if(!enterTestManagerName(TP_TestManager))
			{
				return false;
			}
			
			APP_LOGS.debug("Selecting the End date");
			if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
			{
				return false;
			}
	
			APP_LOGS.debug("Click on Save");
			getObject("ProjectCreateNew_projectSaveBtn").click();
			
			
			String textFromThePopupAfterSaveButton ;
			
			textFromThePopupAfterSaveButton = getTextFromAutoHidePopUp();
			
			
			APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
			
			if (!textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")) {
				
				APP_LOGS.debug("Popup text is other than : 'Test Pass added successfully!'");
				return false;
			}
			
			return true;
			
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}
			
	}
}
