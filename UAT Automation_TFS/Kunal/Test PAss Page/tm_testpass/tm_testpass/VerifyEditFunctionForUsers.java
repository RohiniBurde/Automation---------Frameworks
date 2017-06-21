package com.uat.suite.tm_testpass;


import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
		//testManager=getUsers("Test Manager", 1);
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyEditDoneByValidUsers(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
			String TestPassName,String EditedTestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate,String TP_EditedEndDate, String Assert_UpdatedText, String deleteTestPass) throws Exception
		{
		// test the runmode of current dataset
			count++;
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				throw new SkipException("Runmode for test set data set to no "+count);
			}
			
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
			openBrowser();
			
			isLoginSuccess = login(role);
			
			if (isLoginSuccess) 
			{
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(3000);
				
				getElement("TM_testPassesTab_Id").click();
				
				Thread.sleep(2000);
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
				
				selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
						"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
				
				
				/*APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
				
				selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
						"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
			
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
				
				selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
						"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
				
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
				
				selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
						"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);*/
				
				
				if(enterValidDataInMandatoryFieldsOfTestPass(TestPassName, testManager.get(0).username, TP_EndMonth, TP_EndYear, TP_EndDate))
				{
					comments = "Valid data in mandatory fields of test pass has been entered and saved successfully... ";
				}
				else 
				{
					comments = "Fail occurred: Entering Valid data in mandatory fields of test pass... ";
					fail = true; 
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Entering Mandatory Data in test Pass");
				}
				APP_LOGS.debug("Click on View All");
				getObject("TestPasses_viewAllProjectLink").click();
				
				APP_LOGS.debug("Clicking on Edit Image");
				getObject("TestPassViewAll_editTestPassImageFromViewAllPage").click();
				
				try 
				{
					getObject("TestPasses_editTestPassLink").isDisplayed();
					
					comments = comments+ "Edit Test Pass Page is displayed... ";
				}
				catch (Throwable t) 
				{
					APP_LOGS.debug("Edit Test Pass Page is not displayed");
					
					fail = true;
					
					comments = comments+ "Fail occurred: Edit Test Pass Page is not displayed... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Test Pass Page is not displayed");
				}
				
				APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
				
				getElement("TestPassCreateNew_testPassNameTextField_Id").clear();
				getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(EditedTestPassName);
			
				APP_LOGS.debug("Selecting the End date");
				if(selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EditedEndDate))
				{
					comments = comments+"End date has bedd selected successfully... ";
				}
				else 
				{
					fail = true;
					
					comments = comments+ "Fail occurred: while selecting End Date... ";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "selecting End date");
				}
				
				APP_LOGS.debug("Click on update Button");
				getObject("ProjectCreateNew_projectUpdateBtn").click();
				
				try 
				{
					String updateOpupTextAfterUpdateButtonClicked = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
					
					APP_LOGS.debug(this.getClass().getSimpleName()+" Text found on the popup is : "+updateOpupTextAfterUpdateButtonClicked );
					
					System.out.println("Popup text : " + updateOpupTextAfterUpdateButtonClicked);
					
					 if (updateOpupTextAfterUpdateButtonClicked.equals("Test Pass updated successfully!")) 
					{
						APP_LOGS.debug("Got Popup with Text : "+updateOpupTextAfterUpdateButtonClicked);
						
						String alertTextOfTestPassAddedSuccessfuly = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
						
						compareStrings(alertTextOfTestPassAddedSuccessfuly, Assert_UpdatedText);
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
					
						getObject("TestPasses_okButtonForPopup").click();
						
						comments = comments+ "Popup Text 'Test Pass updated successfully!' has found... ";
						
					}
					else 
					{
						fail = true;
						
						comments = comments+ "Fail occurred: Popup Text 'Test Pass updated successfully!' not found... ";
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Getting Unexpexted popup text");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated sucessfully popup is not displayed");
					}
				} 
				catch (Throwable t) 
				{
					fail = true;
					
					comments = comments+ "Exception occurred: Popup Text 'Test Pass updated successfully!' not found... ";
					
					APP_LOGS.debug(this.getClass().getSimpleName()+" Xpath might not found while getting the popup text" );
					
				}
				
				if(!role.equals("Stakeholder"))
				{	
					APP_LOGS.debug("Role Is not a Stakeholder so deleting the Edited Test Pass");
					deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(EditedTestPassName);
				}
					
				/*if(role.equals("Test Manager"))
				{
					APP_LOGS.debug("Closing the browser as User is Test Manager");
					closeBrowser();
					//ASK login(role);
				}*/
				
				APP_LOGS.debug("Closing the browser");
				closeBrowser();	
			}
			else 
			{
				isLoginSuccess=false;
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
