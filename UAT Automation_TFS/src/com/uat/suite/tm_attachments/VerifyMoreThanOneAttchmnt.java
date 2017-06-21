package com.uat.suite.tm_attachments;

import java.io.IOException;
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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;
@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyMoreThanOneAttchmnt extends TestSuiteBase 
{
	int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPassed=true;
	boolean isLoginSuccess=false;
	String comments=null;
	String runmodes[]=null;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	Utility utilRecorder = new Utility();
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
		
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyMoreThanOneAttchmntAdd(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String TestPassName, String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, String TestCase, String TestStep,String TestStepExpectedResult, String AttachmentName, 
			String Description, String FileNamePath1, String ExpectedAttachmentSaveMessage, String FileNamePath2, String ExpectedAttchmentNotSave) throws Exception
	{
		// test the runmode of current dataset
		count++;
		comments="";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			throw new SkipException("Runmode for test set data set to no "+count);
		}

		//version lead
		int versionlead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionlead_count);
		 
		//TestManager 
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		 
		//Tester 
		int tester_count = Integer.parseInt(TesterName);
		tester = getUsers("Tester", tester_count);
		 
		APP_LOGS.debug("Opening Browser... ");
			
		openBrowser();
		
		isLoginSuccess = login(role);
		
		if (isLoginSuccess) 
		{
			try
			{
				//click on testManagement tab
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
     			{
     				//fail=true;
     				APP_LOGS.debug("Project is not created successfully");
     				comments= "Project not Created Successfully(Fail). ";
     				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
    				//closeBrowser();
    				
     				throw new SkipException("Project is not created successfully ... So Skipping all tests in Attachment Suite");
     			}
				APP_LOGS.debug(" Project Created Successfully.");
				
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser... ");
				openBrowser();
				
				if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				{
					//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
					
					//create test pass
                 	if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
                 	{
                 		// fail=true;
                        APP_LOGS.debug("Test Pass is not created successfully");
                        comments+="Test Pass not Created Successfully(Fail). ";
         				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
         				//closeBrowser();
         					 
         				throw new SkipException("Test Pass is not created successfully ... So Skipping all tests in Attachment Suite");
                 	}
                 	APP_LOGS.debug("Test Pass Created Successfully.");
		
                 	closeBrowser();
				
    				APP_LOGS.debug("Opening Browser... ");
    				openBrowser();

    				if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
    				{
		 				//click on testManagement tab
		 				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
		 				getElement("UAT_testManagement_Id").click();
		 				Thread.sleep(2000);
		 				
		 				if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, TesterRole, TesterRole))
						{
							// fail=true;
                        	APP_LOGS.debug("Tester is not created successfully");
                        	comments+="Tester not Created Successfully(Fail). ";
         					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
         					// closeBrowser();
         					 
                			throw new SkipException("Tester is not created successfully ... So Skipping all tests in Attachment Suite");
						}
						APP_LOGS.debug("Tester Created Successfully.");
        				
						if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase))
						{
							// fail=true;
                        	APP_LOGS.debug("Test Case is not created successfully");
                        	comments+="Test Case not Created Successfully(Fail). ";
         					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
         					// closeBrowser();
         					 
                			throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
						}
						APP_LOGS.debug("Test Case Created Successfully.");
        				
						if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestStep, TestStepExpectedResult, TestCase, TesterRole))
						{
							//fail=true;
                        	APP_LOGS.debug("Test Step is not created successfully");
                        	comments+="Test Step not Created Successfully(Fail). ";
         					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
         					//closeBrowser();
         					 
                			throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
						}
						APP_LOGS.debug("Test Step Created Successfully.");
		               	
						getElement("TM_attachmentTab_Id").click();
						
                        //selecting dropdown values
						dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
					       
				        dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), PortfolioName );
					       
					    dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
					      
					    dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
					      
					    dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), TestPassName );
						
						
					    //selecting test case
				        APP_LOGS.debug("Selecting test case:" +TestCase);
				        getElement("Attachments_testCaseDropDown_Id").click();
				        List<WebElement> testCaseElements = getElement("Attachments_testCaseDropDown_Id").findElements(By.tagName("option"));
					    
					    for(int i =0 ;i<testCaseElements.size();i++)
					    {
					    	if(assertTrue(testCaseElements.get(i).getText().equals(TestCase)))
							{
					    		testCaseElements.get(i).click();
							    APP_LOGS.debug(" Correct " +TestCase+ " selected. ");
							    break;
					        }
					    }
			
					    //creating an attachment
                        createAttachment(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileNamePath1, TestStepExpectedResult, TesterRole);
					   
                        //verify alert message on first time saving
            	//	    String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
                        String actual_SaveAttachment_SuccessMessage =(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
            		   
                        if(!compareStrings(ExpectedAttachmentSaveMessage, actual_SaveAttachment_SuccessMessage))   
            			{
            				fail=true;
            				APP_LOGS.debug(AttachmentName+" is not Saved");
            	    	    comments+="Attachment Save Unsuccessful(Fail). ";
            		    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNotSavedError");
            		    	
            		    	throw new SkipException("Attachment Save unsuccessful");          				  
            			}            		    
            			APP_LOGS.debug("File Attached successfully");
            			
         //   			Thread.sleep(1000);
          //  			getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();
            			
                        //creating an attachment for the same test step
                        createAttachment(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileNamePath2, TestStepExpectedResult, TesterRole);
			
                        //verify alert message on second time saving
            		    String actual_NoSaveAttachment_Message = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
            			if(compareStrings(ExpectedAttchmentNotSave, actual_NoSaveAttachment_Message))   
            			{
            				APP_LOGS.debug(" You cannot attach more than one attachment message shown successfully");
            				comments+="You cannot attach more than one attachment message shown successfully (Pass)";
            				getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
            				Thread.sleep(1000);
            			}
            			else
            			{
            				fail=true;
            				APP_LOGS.debug("You cannot attach more than one attachment message is not shown");
            	    	    comments+="You cannot attach more than one attachment message not shown (Fail). ";
            		    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "More than one attachment issue");
            			}	   
    				}
    				else 
    				{
    					fail = true;
    					APP_LOGS.debug("Login Unsuccessfull for test manager");
    					comments+="Login Unsuccessfull test manager. ";
    				}
				}
				else
				{
					fail = true;
					APP_LOGS.debug("Login Unsuccessfull for version lead");
					comments+="Login Unsuccessfull for version lead. ";
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				fail = true;
				APP_LOGS.debug("Skip or other Exception occured");
				comments+="Skip or other Exception occured. ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SkipOrOtherException");
				assertTrue(false);
			}
			closeBrowser();
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessfull");
		}
	}
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess)
		{
			isTestPassed=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
			
		}
		else if(fail)
		{
			isTestPassed=false;
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
		if(isTestPassed)
		{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		    //TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
	    else
	    {
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		   // TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
	
	    }
		utilRecorder.stopRecording();
	}


	//create new attachment
	public void createAttachment(String group, String portfolio, String project, String version, String testPassName, String TestCase, String AttachmentName, 
		String Description, String TestStep, String fileName1, String TestStepExpectedResult, String TesterRole) throws IOException, InterruptedException
	{
		//creating an attachment
	    APP_LOGS.debug("Clicking on Create New link");
		getObject("Attachments_createNew_link").click();
		Thread.sleep(500);
		    
	    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
	    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
					
		APP_LOGS.debug("Inputing attachment description:" +Description);
		getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
					    
		APP_LOGS.debug("Uploading file:" +fileName1);
		getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
		   
		//selecting test step
		APP_LOGS.debug("Selecting test step:" +TestStep);
		getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
		
		APP_LOGS.debug("Clicking on Save button");
	    try
	    {
		    getElement("AttachmentsCreateNew_saveButton_Id").click();
		    APP_LOGS.debug("Save button clicked");
		    Thread.sleep(3000);
	    }
	    catch(Throwable t)
	    {
	    	fail=true;
	    	APP_LOGS.debug("Save button not clickable");
	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SaveAttachmentOperationError");
	    }
	}
}
