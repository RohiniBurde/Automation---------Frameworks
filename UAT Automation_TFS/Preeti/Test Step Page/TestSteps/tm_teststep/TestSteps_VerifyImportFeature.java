package com.uat.suite.tm_teststep;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class TestSteps_VerifyImportFeature extends TestSuiteBase{
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	String comments;
	static boolean isLoginSuccess=true;
	Alert alert;
	Robot r;
    int totalTestStepCount = 5;
    int validTestSteps = 5;
    int invalidTestSteps = 0;
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			APP_LOGS.debug(" Executing Test Case -> TestSteps_VerifyImportFeature ");
			
			System.out.println(" Executing Test Case -> TestSteps_VerifyImportFeature ");		
			 
			if(!TestUtil.isTestCaseRunnable(TM_testStepSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testStepSuiteXls, this.getClass().getSimpleName());
		}
	
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void VerifyTestStepsImportFunctionality(String role,String group,String portfolio,String project, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String testPassName,
			String testPassEndMonth, String testPassEndYear,String testPassEndDate, String testerRole,
			String testCaseName, String upload_wordDoc, String ExpectedMessage_OtherThanXLXFile, String upload_invalidTemplate, String ExpectedMessage_InvalidTemplate,
			String upload_missingMandatoryFieldsTemplate, String ExpectedMessage_MissingFieldsTemplate1, 
			String ExpectedMessage_MissingFieldsTemplate2, String ExpectedMessage_MissingFieldsTemplate3, String upload_validTemplate) throws Exception
	{
		count++;
		
		ArrayList<Credentials> versionLead = getUsers("Version Lead", 1);
		ArrayList<Credentials> testManager = getUsers("Test Manager", 1);
		ArrayList<Credentials> tester = getUsers("Tester", 1);
		
		boolean noTestStepIsDisplayed;
		boolean totalRecordsIsDisplayed;
		
		int totalRecordsCount;
		int totalTestSteps;
		
		
		if(!(runmodes[count].equalsIgnoreCase("Y") && versionLead!=null && testManager!=null && tester!=null))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		
		
		openBrowser();
		
		if (login(role)) 
		{
			
			
			getElement("UAT_testManagement_Id").click();
			
			Thread.sleep(1000);
			
			if(!createProject(group, portfolio, project, version, projectEndMonth, projectEndYear, projectEndDate, 
					versionLead.get(0).username))
			{
				fail=true;
				APP_LOGS.debug(project+" Project not Created Successfully.");
				comments=project+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Project Successfully not created");
				
			}
			
			APP_LOGS.debug(project+" Project Created Successfully.");
			comments= project+" Project Created Successfully(Pass). ";
			
			closeBrowser();
			
			openBrowser();
			
			if (login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead")) 
			{
				
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(1000);
				
				if(!createTestPass(group, portfolio, project, version, testPassName, testPassEndMonth, testPassEndYear, 
						testPassEndDate, testManager.get(0).username))
				{
					
					fail=true;
					APP_LOGS.debug("Test Pass not Created Successfully.");
					comments+="Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
					closeBrowser();

					throw new SkipException("Test Pass Successfully not created");
					
				}
				
				APP_LOGS.debug("Test Pass Created Successfully.");
				comments+="Test Pass Created Successfully(Pass). ";
				
				closeBrowser();
				
				openBrowser();
				if (login(testManager.get(0).username, testManager.get(0).password, "Test Manager")) 
				{
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(1000);
					
					if (!createTester(group, portfolio, project, version, testPassName, tester.get(0).username, testerRole, testerRole)) 
					{
						
						fail=true;
						APP_LOGS.debug("Tester not Created Successfully.");
						comments+="Tester not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
						closeBrowser();
	
						throw new SkipException("Tester Successfully not created");
						
					}
					
					APP_LOGS.debug("Tester Created Successfully.");
					comments+="Tester Created Successfully(Pass). ";
					
					
					if (!createTestCase(group, portfolio, project, version, testPassName, testCaseName)) 
					{
						
						fail=true;
						APP_LOGS.debug("Test Case not Created Successfully.");
						comments+="Test Case not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
						closeBrowser();
	
						throw new SkipException("Test Case Successfully not created");
						
					}
					
					APP_LOGS.debug("Test Case Created Successfully.");
					comments+="Test Case Created Successfully(Pass). ";
					
								
				try
				{
					
					getElement("TestStepNavigation_Id").click();
					
					Thread.sleep(3000);
					
					//selecting dropdown values
					dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
				       
			        dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
				       
				    dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
				      
				    dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
				      
				    dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
					
				    totalRecordsIsDisplayed= getElement("TestSteps_totalRecordsDiv_Id").isDisplayed();
					noTestStepIsDisplayed = getElement("TestSteps_noTestStepAvailable_Text_Id").isDisplayed();
					
					if (assertTrue(noTestStepIsDisplayed)) 
					{
						APP_LOGS.debug("'No Test Step Available' displayed.");
						comments+="'No Test Step Available' displayed(Pass). ";
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("'Test Step Available' not displayed.");
						comments+="'Test Step Available' not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test StepDisplay");
						
					}
					
					if (!assertTrue(totalRecordsIsDisplayed)) 
					{
						APP_LOGS.debug("'Total Records Count' not displayed.");
						comments+="'Total Records Count' not displayed(Pass). ";
					}
					else 
					{
						fail = true;
						APP_LOGS.debug("'Total Records Count' displayed.");
						comments+="'Total Records Count' displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsDisplay");
						
					}
					
				    APP_LOGS.debug("clicking on ImportExportTest Steps button");
				    
					getElement("TestStepsImportExport_importExportTestStepBtn_Id").click();
					
					
					//uploading other than excel file
					uploadTemplate(upload_wordDoc);
			
		            String actualOtherThanXLXFileMsg = getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText();
		            
		            APP_LOGS.debug("verify alert on importing other than xls file");
		            if(compareStrings(ExpectedMessage_OtherThanXLXFile, actualOtherThanXLXFileMsg))
		            {
		            	APP_LOGS.debug(actualOtherThanXLXFileMsg+ " is properly showing");
		            	comments+="Pass - "+ actualOtherThanXLXFileMsg+ " is properly showing";
		            	
		            	getElement("TestStepsImportExport_alertOkButton").click();
		            	//eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
		            	Thread.sleep(1000);
		            	
		            }
		            else
		            {
		            	APP_LOGS.debug(actualOtherThanXLXFileMsg+ " is not the same as "+ExpectedMessage_OtherThanXLXFile);
		            	comments+="Fail - "+ actualOtherThanXLXFileMsg+ " is not the same as "+ExpectedMessage_OtherThanXLXFile;
		            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "OtherThanExcelFileMsgIncorrect");
		            }
				  
		            
		            
				    //uploading invalid template
		            uploadTemplate(upload_invalidTemplate);
		            
		            String actualInvalidTemplateMsg = getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText();
		            
		            APP_LOGS.debug("verify alert on importing other invalid xls file");
		            if(compareStrings(ExpectedMessage_InvalidTemplate, actualInvalidTemplateMsg))
		            {
		            	APP_LOGS.debug(actualInvalidTemplateMsg+ " is properly showing");
		            	comments+="Pass - "+ actualInvalidTemplateMsg+ " is properly showing";
		            	
		            	getElement("TestStepsImportExport_alertOkButton").click();
		            	//eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
		            	Thread.sleep(1000);
		            	
		            }
		            else
		            {
		            	APP_LOGS.debug(actualInvalidTemplateMsg+ " is not the same as "+ExpectedMessage_InvalidTemplate);
		            	comments+="Fail - "+ actualInvalidTemplateMsg+ " is not the same as "+ExpectedMessage_InvalidTemplate;
		            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "InvalidTemplateMsgIncorrect");
		            }
		            
		            
		            //uploading missing mandatoryfields template
		            uploadTemplate(upload_missingMandatoryFieldsTemplate);
		            
		            int numOfCharsinLegend = getObject("TestStepsImportExport_invalidImportDetails_legendMsg").getText().length();
		            String actualDetailsText =getObject("TestStepsImportExport_invalidImportDetails_fieldSetMsg").getText().substring(numOfCharsinLegend); 
		            String[] detailsMsgArray = actualDetailsText.split("\\n");
		      
	            	if((detailsMsgArray[0].equals(ExpectedMessage_MissingFieldsTemplate1)) && (detailsMsgArray[1].equals(ExpectedMessage_MissingFieldsTemplate2))
		            		&& (detailsMsgArray[2].equals(ExpectedMessage_MissingFieldsTemplate3)))
		            {
		            	APP_LOGS.debug(actualDetailsText+ " is properly showing");
		            	comments+="Pass - "+ actualDetailsText+ " is properly showing";
		            	
		            	eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[1]/div[1]/button/span[text()='Ok']")).click();
		            	Thread.sleep(1000);
		            	
		            }
	            	else
		            {
		            	APP_LOGS.debug(actualDetailsText+ " is not correct");
		            	comments+="Fail - "+ actualDetailsText+ " is not correct";
		            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "MissingMandatoryFieldsMsgIncorrect");
		            }
					
		            
		            
	            	//uploading valid template
		            uploadTemplate(upload_validTemplate);
		            Thread.sleep(40000);	
		            
		            int statusLegendCharacter = getObject("TestStepsImportExport_validImportStatus_legendMsg").getText().length();
		            String validTestStepStatusText =getObject("TestStepsImportExport_validImportStatus_fieldSetMsg").getText().substring(statusLegendCharacter); 
		            List<WebElement> statusElements = getObject("TestStepsImportExport_validImportStatus_fieldSetMsg").findElements(By.tagName("label"));
		            
		            for (int i = 1; i <= statusElements.size(); i++) 
		            {
						String countStep = eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/fieldset[1]/label["+i+"]/font")).getText();
						if(i==1)
						{
							if(!compareStrings(String.valueOf(totalTestStepCount), countStep))
							{
								APP_LOGS.debug(validTestStepStatusText+ " is properly showing");
				            	comments+="Pass - "+ validTestStepStatusText+ " is properly showing";
							}
							else
							{
								APP_LOGS.debug(validTestStepStatusText+ " is not correct");
				            	comments+="Fail - "+ validTestStepStatusText+ " is not correct";
				            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ValidTemplateMsgIncorrect");
							}
						}else if(i==2)
						{
							if(compareStrings(String.valueOf(validTestSteps), countStep))
							{
								APP_LOGS.debug(validTestStepStatusText+ " is properly showing");
				            	comments+="Pass - "+ validTestStepStatusText+ " is properly showing";
							}
							else
							{
								APP_LOGS.debug(validTestStepStatusText+ " is not correct");
				            	comments+="Fail - "+ validTestStepStatusText+ " is not correct";
				            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ValidTemplateMsgIncorrect");
							}
						}else
						{
							if(compareStrings(String.valueOf(invalidTestSteps), countStep))
							{
								APP_LOGS.debug(validTestStepStatusText+ " is properly showing");
				            	comments+="Pass - "+ validTestStepStatusText+ " is properly showing";
							}
							else
							{
								APP_LOGS.debug(validTestStepStatusText+ " is not correct");
				            	comments+="Fail - "+ validTestStepStatusText+ " is not correct";
				            	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ValidTemplateMsgIncorrect");
							}
						}
					}
		            
		           //eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[1]/div[1]/button/span[text()='Ok']")).click();
	            	getObject("TestStepsImportExport_alertOkButton").click();
	            	Thread.sleep(1000);


		            //click on View All link
					getObject("TestSteps_viewAllLink").click();
					
					totalTestSteps = getElement("TestStepsViewAll_table_Id").findElements(By.xpath("table/tbody/tr")).size();
					
					if (totalTestSteps!=0) 
					{
						APP_LOGS.debug("Test Step Table displayed.");
						comments+="Test Step Table displayed(Pass). ";
						
						totalRecordsIsDisplayed= getElement("TestSteps_totalRecordsDiv_Id").isDisplayed();
						
						
						if (assertTrue(totalRecordsIsDisplayed)) 
						{
							
							APP_LOGS.debug("'Total Records Count' displayed after test step created .");
							comments+="'Total Records Count' displayed after test step created(Pass). ";
							
							totalRecordsCount=Integer.parseInt(getElement("TestSteps_totalRecordsCountText_Id").getText()); 
							
							if (totalTestSteps==totalRecordsCount) 
							{
								APP_LOGS.debug("'Total Records Count' is correct .");
								comments+="'Total Records Count' is correct(Pass). ";
								
							}
							else
							{
								fail = true;
								APP_LOGS.debug("'Total Records Count' is not correct .");
								comments+="'Total Records Count' is not correct(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsCount");
								assertTrue(false);
							
							}
						}
						else 
						{
							fail = true;
							APP_LOGS.debug("'Total Records Count' not displayed after test step created .");
							comments+="'Total Records Count' not displayed after test step created(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalRecordsNotDisplayed");
							
							
						}
					}
					else
					{
						fail = true;
						APP_LOGS.debug("Test Step Table not displayed.");
						comments+="Test Step Table not displayed(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepTableDisplay");
						assertTrue(false);
					}
					
		           
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail = true;
					APP_LOGS.debug("Exception in Test Step Tab");
					comments+="Exception in Test Step Tab. ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepTabException");
					assertTrue(false);
					closeBrowser();
					
				}								
				
			}
			else
			{
				isLoginSuccess = false;
				APP_LOGS.debug("Login Unsuccessfull");
				comments+="Login Unsuccessfull";
				
			}
			
			
			
			
		}
		else
		{
			isLoginSuccess = false;
			APP_LOGS.debug("Login Unsuccessfull");
			comments+="Login Unsuccessfull";
		}
		
		}
		else
		{
			isLoginSuccess = false;
			APP_LOGS.debug("Login Unsuccessfull");
			comments+="Login Unsuccessfull";
			
		}
	
	}
	
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testStepSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		

	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "PASS");
			TestUtil.printComments(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), "FAIL");
			TestUtil.printComments(TM_testStepSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testStepSuiteXls,this.getClass().getSimpleName()), comments);
		}
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testStepSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	//uploading file
	private void uploadTemplate(String fileName) throws IOException, InterruptedException, AWTException
	{
		Boolean result = true;
		try
		{
			eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
            Thread.sleep(2000);
		}
		catch(Throwable t)
		{
			result = false;
		}
		
		getElement("TestStepsImportExport_importTestStepsBtn_Id").click();
		if(result.equals(false))
		{
			//Active x code
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
            if(getElement("TestStepsImportExport_activeXMessage_Id").isDisplayed())
            {
            	getObject("TestStepCreateNew_TestStepActiveX_Close").click();
                closeBrowser();
                throw new SkipException("ActiveX is disabled..hence cannot import template");
            }
            else
            {
             System.out.println("An alert informing the user of disabled activex should be present.");
             TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabled");
             assertTrue(false);
             comments+="Fail- Activex is disabled..but the popup not displayed";
             closeBrowser();
            }
		}
		
		Thread.sleep(4000);
		try
		{
				
				
			APP_LOGS.debug("switching to alert for uploading a file");
				
			alert = eventfiringdriver.switchTo().alert();
			Thread.sleep(1500);
			
			alert.sendKeys(System.getProperty("user.dir")+"\\Upload_Templates\\"+fileName);
			       
	        Thread.sleep(2000);
	        r = new Robot();

	        r.keyPress(KeyEvent.VK_ENTER);
	        Thread.sleep(2000);
		}
		catch(Throwable t)
		{
			fail=true;
			APP_LOGS.debug("Error in uploading file "+fileName);
	    	comments+="Fail - "+ "Error in uploading file "+fileName;
	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ErrorInUploadingFile");
			
		}
	}

}