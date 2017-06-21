package com.uat.suite.tm_attachments;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyLandingOnAttachmentPage extends TestSuiteBase {
	
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	
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
	public void Test_VerifyLandingOnAttachmentPage(String Role) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		
		APP_LOGS.debug(" Executing VerifyLandingOnAttachmentsPage Test Case");
		System.out.println(" Executing VerifyLandingOnAttachmentsPage Test Case");				
		
		APP_LOGS.debug("Opening Browser... ");
		System.out.println("Opening Browser... ");
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{	
			// clicking on Test Management Tab
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug(" Test Management tab clicked ");
			System.out.println(" Test Management tab clicked ");
			Thread.sleep(2000);

			getElement("TM_attachmentTab_Id").click();
			APP_LOGS.debug(" Attachments tab clicked ");
			System.out.println(" Attachments tab clicked ");
			Thread.sleep(2000);
			
			String attachment_tab_highlighted=getElement("TM_attachmentTab_Id").getAttribute("class");
			Thread.sleep(2000);
			
			compareStrings(attachment_tab_highlighted,OR.getProperty("TM_attachmentTab_class"));
			
			APP_LOGS.debug("Attachment tab is Highlighted ");
			
			System.out.println("Attachment tab is Highlighted ");
			
			comments="Attachment tab is Highlighted for User "+Role;
			
			String actualAttachmentPageHeadingText = getObject("TM_attachmentTabHeadingDetails").getText();
			
			try {
				compareStrings(actualAttachmentPageHeadingText,"Attachment Details");
				
				APP_LOGS.debug("User " + Role+ " is landing on 'Attachment' Page successfully ");
				
				comments+="User " + Role+ " is landing on 'Attachment' Page successfully (PASS)";
			
			} catch (Exception e) {
				
				fail=true;
				
				APP_LOGS.debug("User " + Role+ " is not landing on 'Attachment' Page (FAIL)");
				
				comments+="User " + Role+ " is not landing on 'Attachment' Page (FAIL)";
				
			}				

			APP_LOGS.debug("Closing Browser... ");
			
			System.out.println("Closing Browser... ");
		
			closeBrowser();
			
		
		}
		
		else 
		{
			isLoginSuccess=false;
			APP_LOGS.debug("Login Not Successful");
			System.out.println("Login Not Successful");
			
		}	
	}

	
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
		{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
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
		