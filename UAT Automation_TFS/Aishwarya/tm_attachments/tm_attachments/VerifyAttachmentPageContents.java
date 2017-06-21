package com.uat.suite.tm_attachments;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class VerifyAttachmentPageContents extends TestSuiteBase{
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	String comments;
	static boolean isLoginSuccess=false;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
		}
	
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void VerifyAttachmentPageContents_test(String Role,String GroupName,String Portfolio,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String ExpectedMessage, String TestPassName,
			String TestManagerName, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String TestCase,String TestStepName,String AssignedRole, String ExpectedResult) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		
		APP_LOGS.debug(" Executing VerifyAttachmentPageContents Test Case");
		
		System.out.println(" Executing VerifyAttachmentPageContents Test Case");				
		
		APP_LOGS.debug("Opening Browser");
		
		System.out.println("Opening Browser");
		
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{	
			System.out.println("**************************");
			// clicking on Test Management Tab
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug(" Test Management tab clicked ");
			System.out.println(" Test Management tab clicked ");
			Thread.sleep(2000);
			
			getElement("TM_attachmentTab_Id").click();
			APP_LOGS.debug(" Attachments tab clicked ");
			System.out.println(" Attachments tab clicked ");
			Thread.sleep(2000);
			//Verify if View All link is displayed or not
			try{
				getObject("Attachments_viewAll_link").isDisplayed();
				
				APP_LOGS.debug("View All Link is displayed successfully");
				System.out.println("View All Link is displayed successfully");
				comments=comments+"View All Link is displaying successfully (PASS)";
			}
			catch(Throwable t){
				fail=true;
				
				APP_LOGS.debug("View All Link is not displaying successfully");
				System.out.println("View All Link is not displaying successfully");
				comments=comments+"View All Link is not displaying successfully (FAIL)";	
			}
			
			//Verify if Create new link is displayed or not
			try {
				
				getObject("Attachments_createNew_link").isDisplayed();
				
				APP_LOGS.debug("Add attachment Link is displayed successfully");
				System.out.println("Add attachment Link is displayed successfully");
				comments=comments+"Create New Link is not displaying successfully (PASS)";	
				
			} catch (Throwable t) {
				
				fail=true;
				
				APP_LOGS.debug("Add attachment Link is not displaying successfully");
				System.out.println("Add attachment Link is not displaying successfully");
				comments=comments+"Add attachment Link is not displaying successfully (FAIL)";	
			}
			

			// if project exist then delete it and then create the project
			//else create project directly
			
			boolean groupExist=compareDropDownSelectionText(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
			
			boolean portfolioExist=compareDropDownSelectionText(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
			
			boolean projectExist=compareDropDownSelectionText(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
			
			boolean versionExist=compareDropDownSelectionText(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
			
			boolean testPassExist=compareDropDownSelectionText(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
			
			System.out.println(groupExist+" "+portfolioExist+" "+projectExist+" "+versionExist+" "+testPassExist);
			APP_LOGS.debug("Closing Browser... ");
			System.out.println("Closing Browser... ");
			closeBrowser();
			
			
		}
		
		else{
			isLoginSuccess=false;
			APP_LOGS.debug("Login Not Successful");
			System.out.println("Login Not Successful");
		}
	}
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
		}
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=false;
		

	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
}
