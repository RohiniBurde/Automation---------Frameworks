/*
 * Verify the 'Attachment' is opening when logged in as Stakeholder
   /Test Manager/Admin/Project Lead
 */
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
public class VerifyLandingOnAttachmentPage2 extends TestSuiteBase{

	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	
	@BeforeTest
	public void checkTestSkip(){
		
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void VerifyLandingOnAttachmentPage1(String Role) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Skipping Test Set Data No."+(count+1)+" as Runmode for test set data is set to No");;
			throw new SkipException("Runmode for test set data no. "+(count+1)+" is set to no");
		}
		
		APP_LOGS.debug(" Executing VerifyLandingOnAttachmentPage Test Case ");
		System.out.println(" Executing VerifyLandingOnAttachmentPage Test Case ");				
		
		openBrowser();
		APP_LOGS.debug("Opening Browser");
	
		isLoginSuccess = login(Role);
		if(isLoginSuccess)
		{
			// Clicking on TestManagement tab
			APP_LOGS.debug("User " + Role+ " is logged in Successfully..");
			System.out.println("User " + Role+ " is logged in Successfully..");
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug(" Test Management tab clicked ");
			System.out.println(" Test Management tab clicked ");
			Thread.sleep(2000);
			
			// Clicking on Attachments tab
			getElement("TM_attachmentTab_Id").click();
			APP_LOGS.debug(" Attachments tab clicked ");
			
			System.out.println(" Attachments tab clicked ");
			Thread.sleep(2000);
			
			// Verifying 
			String attachment_tab_highlighted=getObject("TM_attachmentTab_Id").getAttribute("class");
			
			
				boolean isTrue=compareStrings(attachment_tab_highlighted, OR.getProperty("TM_attachmentTab_Class"));
				if(isTrue==true)
				{
					APP_LOGS.debug("Attachment TAB is Highlighted ");
					System.out.println("Attachment TAB is Highlighted ");
				
					APP_LOGS.debug("User " + Role+ " is landing on' Attachment' Page successfully ");
					System.out.println("User " + Role+ " is landing on' Attachment' Page successfully");
				}
				else
				{
					APP_LOGS.debug("Attachment TAB is not Highlighted ");
					System.out.println("Attachment TAB is not Highlighted ");
				}
				
			
			
			// Closing browser
			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();		
		}
		
		else
		{
			isLoginSuccess=false;
			System.out.println("Login was not Successful");
			APP_LOGS.debug("Login was not Successful");
				
		}
		
	}
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip){
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
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		
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
