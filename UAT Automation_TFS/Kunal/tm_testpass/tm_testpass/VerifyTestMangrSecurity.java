package com.uat.suite.tm_testpass;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.suite.tm_testpass.TestSuiteBase;
import com.uat.util.TestUtil;

public class VerifyTestMangrSecurity extends TestSuiteBase {

		static int count=-1;
		static boolean skip=false;
		static boolean pass=false;
		static boolean fail=false;
		static boolean isTestPass=true;
		static boolean isLoginSuccess=false;
		ArrayList<Credentials> testManager;
		String comments;
		String runmodes[]=null;

		@BeforeTest
		public void checkTestSkip()
		{
			
			if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
			{
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
			
			testManager=new ArrayList<Credentials>();
			testManager=getUsers("Test Manager", 2);
			
		}
		
		@Test(dataProvider = "getTestData")
		public void VerifyToAcceptCombiOfAlphabets(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
				String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
				String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
				String TestPassName,String TP_Description,String TP_Status,/*String TP_TestManager,*/ String TP_EndMonth, String TP_EndYear,
				String TP_EndDate, String TestPassName2, /*String TP_TestManager2,*/ String TP_EndMonth2, String TP_EndYear2, String TP_EndDate2) throws Exception
			{
			// test the runmode of current dataset
						count++;
						if(!runmodes[count].equalsIgnoreCase("Y"))
						{
							skip=true;
							throw new SkipException("Runmode for test set data set to no "+count);
						}
						
						//int testManager_count = Integer.parseInt(TP_TestManager);
						//testManager = getUsers("Test Manager", testManager_count);
						
					APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
					openBrowser();
					
					isLoginSuccess = login(role);
					
					if (isLoginSuccess) 
					{

						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(3000);
						
						getElement("TM_testPassesTab_Id").click();
						
						selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, Version);
						
						/*
						Thread.sleep(2000);
						APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
						
						selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
								"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
						
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
						
						selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
								"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
					
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
						
						selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
								"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
						
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
						
						selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
								"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);
						
					*/	
						APP_LOGS.debug("Enter Valid data For USER1");
					
						if(enterValidDataInMandatoryFieldsOfTestPass(TestPassName, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
						{
							comments = "Valid data in mandatory fields of test pass has been entered and saved successfully for user 1... ";
						}
						else
						{
							fail = true;
							
							comments = "Fail occurred: Entering Valid data in mandatory fields of test pass for user 1... ";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Entering Valid data in mandatory fields");
						}
						
						APP_LOGS.debug("Enter Valid data For USER2");
					
						if(enterValidDataInMandatoryFieldsOfTestPass(TestPassName2, testManager.get(1).username, TP_EndMonth2, TP_EndYear2, TP_EndDate2))
						{
							comments = "Valid data in mandatory fields of test pass has been entered and saved successfully for user 2... ";
						}
						else
						{
							fail = true;
							
							comments = "Fail occurred: Entering Valid data in mandatory fields of test pass for user 2... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Entering Valid data in mandatory fields");
						}
						APP_LOGS.debug("Close and open browser for login by USER1");
						closeBrowser();
						openBrowser();
						
						APP_LOGS.debug("Login with Test Manager 1");
						login(testManager.get(0).username, "newuser@123", "Test Manager");
						
						//login With User1 And Check User1 can not see Test pass Name provided for User 2 
						//(User 1 can see only those Test passes in which he is test Manager)
						loginWithUser1n2AndCheckSecurity(TestPassName2);
						
						APP_LOGS.debug("Close and open browser for login by USER2");
						closeBrowser();
						openBrowser();

						APP_LOGS.debug("Login with Test Manager 1");
						login(testManager.get(1).username, "newuser@123", "Test Manager");
						
						//login With User1 And Check User2 can not see Test pass Name provided for User 1
						//(User 2 can see only those Test passes in which he is test Manager)
						loginWithUser1n2AndCheckSecurity(TestPassName);
						
						closeBrowser();
						openBrowser();
						
						login(role);
						
						APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(3000);
						
						getElement("TM_testPassesTab_Id").click();
						
						selectProjectFromDropdownHeads(GroupName, PortfolioName, ProjectName, Version);
						
						deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName2);
						Thread.sleep(2000);
						
						deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName);
						
						APP_LOGS.debug("Closing the browser");
						closeBrowser();
					}else 
					{
						isLoginSuccess=false;
					}	
		}
		
		//private Functions
		private void selectProjectFromDropdownHeads(String GroupName,String PortfolioName,String ProjectName,String Version) throws IOException, InterruptedException
		{
			Thread.sleep(2000);
			APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
			
			selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
					"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
			
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
			
			selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
					"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
		
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
			
			selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
					"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
			
			selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
					"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);
		}
		private void loginWithUser1n2AndCheckSecurity(String testPassNameThatNotToBeSeenByProvidedUser) throws IOException, InterruptedException
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
			getElement("UAT_testManagement_Id").click();
			
			Thread.sleep(3000);
			
			getElement("TM_testPassesTab_Id").click();
			
			int numberOfRowsPresentOnViewAllPAge = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'showTestPass']/tr")).size();
			
			for (int i = 1; i <=numberOfRowsPresentOnViewAllPAge ; i++) 
			{
				
				String testPassNameFromViewAllPage = getObject("TestPassViewAll_testPassNameFromViewAllPage1", "TestPassViewAll_testPassNameFromViewAllPage2", i).getText();
				
				if (!testPassNameFromViewAllPage.equals(testPassNameThatNotToBeSeenByProvidedUser)) 
				{
					APP_LOGS.debug("USER1 could not see the Test pass for USER2. Test Case Has been Passed");
				}
				else 
				{
					APP_LOGS.debug("USER1 could see the Test pass for USER2. Test Case Has been Failed");
					
					fail = true;
				}
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
		public void reportTestResult() throws InterruptedException
		{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
		}

}
