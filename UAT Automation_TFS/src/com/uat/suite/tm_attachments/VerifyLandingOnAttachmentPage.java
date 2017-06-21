package com.uat.suite.tm_attachments;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyLandingOnAttachmentPage extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing VerifyLandingOnAttachmentsPage Test Case");
			
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void Test_VerifyLandingOnAttachmentPage(String Role) throws Exception
	{
		count++;
		comments="";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		APP_LOGS.debug("Opening Browser... ");
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try
			{
				// clicking on Test Management Tab
				getElement("UAT_testManagement_Id").click();
				APP_LOGS.debug("Test Management tab clicked ");
				Thread.sleep(2000);
	
				getElement("TM_attachmentTab_Id").click();
				APP_LOGS.debug("Attachments tab clicked ");
				Thread.sleep(2000);
				
				String attachment_tab_highlighted=getElement("TM_attachmentTab_Id").getAttribute("class");
				String testManagementTabClass = getElement("UAT_testManagement_Id").getAttribute("class");
				
				if (compareStrings(OR.getProperty("UAT_testManagementTab_Class"), testManagementTabClass)) 
				{
					APP_LOGS.debug("Test Management Tab is highlighted for Role: "+Role);
					comments+="Test Management tab is Highlighted for Role: "+Role+"(Pass).";
				}
				else
				{
					fail = true;				
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TMTabHighlightedError");							
					APP_LOGS.debug("Test Management Tab is not highlighted for Role: "+Role);
					comments+="Test Management tab is not Highlighted for Role: "+Role+"(Fail).";
				}
				
				if (!compareStrings(OR.getProperty("TM_attachmentTab_class"),attachment_tab_highlighted)) 
				{
					fail = true;
					APP_LOGS.debug("Attachment tab is not Highlighted for Role: "+Role);
					comments+="Attachment tab is not Highlighted for Role: "+Role+"(Fail).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Attachment tab not highlighted");
				}
				else
				{
					APP_LOGS.debug("Attachment tab is Highlighted for Role: "+Role);
					comments+="Attachment tab is Highlightedfor Role: "+Role+"(Pass).";
				}						
				
				String actualAttachmentPageHeadingText = getObject("TM_attachmentTabHeadingDetails").getText();
	
				if(compareStrings(actualAttachmentPageHeadingText,resourceFileConversion.getProperty("TM_AttachmentTab_AttachmentDetailsText")))
				{
					APP_LOGS.debug("User " + Role+ " is landing on 'Attachment' Page successfully ");
					comments+="User " + Role+ " is landing on 'Attachment' Page successfully (PASS). ";
				}				
				else
				{				
					fail=true;
					APP_LOGS.debug("User " + Role+ " is not landing on 'Attachment' Page (FAIL)");
					comments+="User " + Role+ " is not landing on 'Attachment' Page (FAIL). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(),"FailedLanding");
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				fail = true;
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception");
				APP_LOGS.debug("Exception Occured");
				comments+="Exception Occured. ";
			}

			APP_LOGS.debug("Closing Browser... ");
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful for Role: "+Role);
		}	
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName()) ;
	}
}
		