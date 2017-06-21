package com.uat.suite.tm_attachments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

public class VerifyAttachmentsCreateNew extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	int testCaseFlag = 0;
	int testStepFlag = 0;
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
	
	@Test(dataProvider="getTestData")
	public void testVerifyAttachmentsCreateNew(String Role,String GroupName,String Portfolio,String ProjectName,String Version,
			String endMonth,String endYear,	String endDate,String VersionLead,String ExpectedMessage,String testPassName,
			String testManager, String TP_endMonth,String TP_endYear,String TP_endDate,String testerName,String testerRole,
			String testCaseName,String testStepName,String TestStepExpectedResult, String assignedRole,String attachmentName,
			String expectedAttachmentNameBlankMessage,String Description,String expectedTestStepBlankMessage,String fileName,
			String expectedFileNameBlankMessage,String expectedResult,String expectedAllFieldsBlankMessage) throws Exception
	{
		count++;		
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Skipping Test Set Data No."+(count+1)+" as Runmode for test set data is set to No");;
			throw new SkipException("Runmode for test set data no. "+(count+1)+" is set to no");
		}
		
		//version lead
		int versionlead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionlead_count);
		 
		//TestManager 
		int testManager_count = Integer.parseInt(testManager);
		test_Manager = getUsers("Test Manager", testManager_count);
		 
		//Tester 
		int tester_count = Integer.parseInt(testerName);
		tester = getUsers("Tester", tester_count);
		 
		APP_LOGS.debug("Opening Browser... for role "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try
			{
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				{
					fail=true;
	 				
	 				APP_LOGS.debug(ProjectName+" not created successfully. ");
	 				
	 				comments= comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
	 				
	 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
				}
				
				APP_LOGS.debug(ProjectName+" Project Created Successfully.");

				closeBrowser();
			   
                APP_LOGS.debug("Opening Browser... ");
				
                openBrowser();
			
				if (login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead")) 
				{
					APP_LOGS.debug("Logged in browser with Version Lead");
		            
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(3000);
					
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
					{
					     fail=true;
		           		    
	           		     APP_LOGS.debug(testPassName+" not created successfully. ");
	                   	
	           		     comments+="Fail Occurred:- "+testPassName+" not created successfully. ";
	   					 
	           		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	   					 
	   					 throw new SkipException("Test Pass is not created successfully... So Skipping all tests");
					}
					APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
					
					closeBrowser();
					   
	                APP_LOGS.debug("Opening Browser... ");
	                openBrowser();
					
					APP_LOGS.debug("Logged in browser with Test Manager");
					
					if (login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager")) 
					{
						APP_LOGS.debug("Logged in browser with Version Lead");
			            
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(3000);
						
						if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))
						{
							 fail=true;
							 
							 APP_LOGS.debug(tester.get(0).username+ "Tester not created successfully for test pass "+testPassName);
							
							 comments+="Fail Occurred:- "+tester.get(0).username+ "Tester not created successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass ... So Skipping all tests");
						}
						APP_LOGS.debug(" Tester Created Successfully.");
		
						if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
						{
							 fail=true;
								
							 APP_LOGS.debug(testCaseName+ "Test Case not created successfully for test pass "+testPassName);
							
							 comments+="Fail Occurred:- "+testCaseName+ "Test Case not created successfully for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Test Case is not created successfully for test pass... So Skipping all tests");
						}
						APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
					
						if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, TestStepExpectedResult, testCaseName, assignedRole))
						{
							 fail=true;
								
							 APP_LOGS.debug(testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName);
							
							 comments+="Fail Occurred:- "+testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests");
						}
						APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
		 				
						Thread.sleep(3000);
						
						getElement("TM_attachmentTab_Id").click();
						
						Thread.sleep(1000);
						
		 				// Verifying Create New Attachment Validation
						dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
						
						dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), Portfolio );
						
						dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
						
						dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
						
						dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
						
		 				APP_LOGS.debug("Selecting test case:" +testCaseName);
		 				
		 				testCaseDropdown(getElement("Attachments_testCaseDropDown_Id"), testCaseName);
		 							
		 				APP_LOGS.debug("Clicking on Create New link");
		 				
		 				getObject("Attachments_createNew_link").click();

		 				
		 				//VALIDATION: click on save button keeping all fields blank and verify pop up text
	 					if(blankFieldvalidation(expectedAllFieldsBlankMessage))
	 					{
	 						APP_LOGS.debug("'Please browse the attachment first!' message is displayed keeping all fields blank(Pass).");

	 						comments+=" 'Please browse the attachment first!' message is displayed keeping all fields blank(Pass).";
	 					}
	 					else
	 					{
	 						fail = true;									
							
							APP_LOGS.debug("'Please browse the attachment first!' message is NOT displayed keeping all fields blank. Test case Failed.");									
							
							comments+=" 'Please browse the attachment first!' message is NOT displayed keeping all fields blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Please browse the attachment first!' message is NOT displayed");									
	 						
	 					}
	 					
	 					
	 					//VALIDATION: click on save button keeping only 'File Name' blank (enter data in all other mandatory fields) and verify pop up text
		 				
		 				APP_LOGS.debug("Input attachment name:" +attachmentName);
			 			
		 				getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(attachmentName);
	 					
		 				APP_LOGS.debug("Selecting test step:" +testStepName);	
		 				
	 					selectTestStep(testStepName);

		 			    APP_LOGS.debug("Clicking on Save button");

		 			    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		 			    Thread.sleep(1500);
						   
		 			    String actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		 				
		 				if(compareStrings(expectedFileNameBlankMessage, actualFieldBlankMessage))
	 					{
		 					APP_LOGS.debug("'Please browse the attachment first!' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).");
	 						
		 					comments+="'Please browse the attachment first!' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).";
	 					}
	 					else
	 					{
	 						fail = true;									
							
							APP_LOGS.debug("'Please browse the attachment first!' message is NOT displayed keeping only 'Select Test Step' mandatory field blank. Test case Failed.");									
							
							comments+="'Please browse the attachment first!' message is NOT displayed keeping only 'Select Test Step' mandatory field blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Please browse the attachment first!' popup message is NOT displayed1");									
	 						
	 					}
		 				Thread.sleep(1000);
		 				
		 				//Click on OK button form validation pop up message
		 				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
		 				
	 					
	 					//VALIDATION: click on save button keeping only Attachment name blank (enter data in all other mandatory fields) and verify pop up text
		 				APP_LOGS.debug("Clearing Attachment Name");
			 			
		 				getObject("AttachmentsCreateNew_attachmentNameTextField").clear();
		 				
		 				APP_LOGS.debug("Uploading file:" +fileName);
		 			   
		 				getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName);
		 		    
		 			    APP_LOGS.debug("Clicking on Save button");

		 			    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		 			    Thread.sleep(1500);
						   
		 				actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		 				
		 				if(compareStrings(expectedAttachmentNameBlankMessage, actualFieldBlankMessage))
	 					{
		 					APP_LOGS.debug("'Attachment Name is a mandatory field!' message is displayed keeping only 'Attachment Name' mandatory field blank(Pass).");
	 						
		 					comments+="'Attachment Name is a mandatory field!' message is displayed keeping only 'Attachment Name' mandatory field blank(Pass).";
	 					}
	 					else
	 					{
	 						fail = true;									
							
							APP_LOGS.debug("'Attachment Name is a mandatory field!' message is NOT displayed keeping only 'Attachment Name' mandatory field blank. Test case Failed.");									
							
							comments+="'Attachment Name is a mandatory field!' message is NOT displayed keeping only 'Attachment Name' mandatory field blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Attachment Name is a mandatory field!' popup message is NOT displayed1");									
	 						
	 					}
		 				Thread.sleep(1000);
		 				
		 				//Click on OK button form validation pop up message
		 				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
		 				
		 				
		 				//VALIDATION: click on save button keeping only 'Select Test Step' blank (enter data in all other mandatory fields) and verify pop up text
		 				APP_LOGS.debug("Input attachment name:" +attachmentName);
			 			
		 				getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(attachmentName);
	 					
		 				APP_LOGS.debug("Uploading file:" +fileName);
		 			   
		 				getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName);
		 				
		 				APP_LOGS.debug("Unselecting the Test Step");
		 				
		 				selectTestStep(testStepName);          //Unselect Test Step
		 				
		 			    APP_LOGS.debug("Clicking on Save button");

		 			    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		 			    Thread.sleep(1500);
						   
		 				actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		 				
		 				if(compareStrings(expectedTestStepBlankMessage, actualFieldBlankMessage))
	 					{
		 					APP_LOGS.debug("'Please select at least one test step' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).");
	 						
		 					comments+="'Please select at least one test step' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).";
	 					}
	 					else
	 					{
	 						fail = true;									
							
							APP_LOGS.debug("'Please select at least one test step' message is NOT displayed keeping only 'Select Test Step' mandatory field blank. Test case Failed.");									
							
							comments+="'Please select at least one test step' message is NOT displayed keeping only 'Select Test Step' mandatory field blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Please select at least one test step'popup message is NOT displayed1");									
	 						
	 					}
		 				Thread.sleep(1000);
		 				
		 				//Click on OK button form validation pop up message
		 				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
		 				
		 						
		 				//VALIDATION: Fill in all mandatory fields except 'Description' and click Save button. and verify a message stating that 'Attachment attached successfully'.
		 				if (createAttachment(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName, attachmentName, " ",
		 						testStepName, fileName, TestStepExpectedResult, testerRole, expectedResult))
     		 			{
		 					
		 					APP_LOGS.debug("'File attached successfully' message is displayed keeping 'Description' field blank(Pass).");
	 						
		 					comments+="'File attached successfully' message is displayed keeping 'Description' field blank(Pass).";
	 					
			 			}
		 				else 
		 				{
		 					fail = true;									
							
							APP_LOGS.debug("'File attached successfully' message is NOT displayed keeping 'Description' field blank(Fail).");									
							
							comments+="'File attached successfully' message is NOT displayed keeping 'Description' field blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Attachment attached successfully'' popup message is NOT displayed");									
	 						
						}
		 				
		 				String attachmentNameInTable = getObject("AttachmentsViewAll_attachmentNameInTable").getText();
		 				
		 				if (compareStrings(attachmentName, attachmentNameInTable)) 
		 				{
		 					APP_LOGS.debug("Added attachment "+attachmentName+" is displayed on View All Page.");
						}
		 				else 
		 				{
		 					fail = true;									
							
							APP_LOGS.debug("Added attachment (without Description field) is NOT displayed on View All Page. Test case has been failed.");									
							
							comments+="Added attachment (without Description field) is NOT displayed on View All Page.(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), attachmentName+" is NOT displayed on View All Page");									
	 						
						}
		 				
		 				getObject("AttachmentsViewAll_deleteImageXpath1", "AttachmentsViewAll_deleteImageXpath2", 1).click();
        				
        				
        				getObject("AttachmentViewAll_delectAttachmentPopUpDeleteButton").click();
        				Thread.sleep(500);
        				
		 				//VALIDATION: Fill in all mandatory and Non-Mandatory fields and click Save button. and verify a message stating that 'Attachment attached successfully'.
		 				if (createAttachment(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName, attachmentName, Description,
		 						testStepName, fileName, TestStepExpectedResult, testerRole, expectedResult))
     		 			{
		 					
		 					APP_LOGS.debug("'File attached successfully' message is displayed after filling all fieldsPass).");
	 						
		 					comments+="'File attached successfully' message is displayed after filling all fields(Pass).";
			 			}
		 				else 
		 				{
		 					fail = true;									
							
							APP_LOGS.debug("'File attached successfully' message is NOT displayed after filling all fields(Fail).");									
							
							comments+="'File attached successfully' message is NOT displayed after filling all fields(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'File attached successfully' popup message is NOT displayed");									
						}
		 				
		 				attachmentNameInTable = getObject("AttachmentsViewAll_attachmentNameInTable").getText();
		 				
		 				if (compareStrings(attachmentName, attachmentNameInTable)) 
		 				{
		 					APP_LOGS.debug("Added attachment is displayed on View All Page.");
						}
		 				else 
		 				{
		 					fail = true;									
							
							APP_LOGS.debug("Added attachment is NOT displayed on View All Page. Test case has been failed.");									
							
							comments+="Added attachment is NOT displayed on View All Page.(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), attachmentName+" is NOT displayed on View All Page");									
						}
		 				
		 				/*APP_LOGS.debug("Input attachment name:" +attachmentName);
			 			
		 				getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(attachmentName);
	 					
		 				APP_LOGS.debug("Selecting test step:" +testStepName);	
		 				
	 					selectTestStep(testStepName);
		 			    
		 				APP_LOGS.debug("Uploading file:" +fileName);
		 			   
		 				getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName);
		 		    
		 			    APP_LOGS.debug("Clicking on Save button");

		 			    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		 			    Thread.sleep(3000);
						   
		 				actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		 				
		 				if(compareStrings(expectedFileNameBlankMessage, actualFieldBlankMessage))
	 					{
		 					APP_LOGS.debug("'Please browse the attachment first!' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).");
	 						
		 					comments+="\n 'Please browse the attachment first!' message is displayed keeping only 'Select Test Step' mandatory field blank(Pass).";
	 					}
	 					else
	 					{
	 						fail = true;									
							
							APP_LOGS.debug("'Please browse the attachment first!' message is NOT displayed keeping only 'Select Test Step' mandatory field blank. Test case Failed.");									
							
							comments+="\n 'Please browse the attachment first!' message is NOT displayed keeping only 'Select Test Step' mandatory field blank(Fail).";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Please browse the attachment first!' popup message is NOT displayed1");									
	 						
	 					}
		 				Thread.sleep(1000);
		 				
		 				//Click on OK button form validation pop up message
		 				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
		 				*/
		 			   
		 			 
		 			/* 
						    
						//Checking validation after entering attachment name
						   
						    APP_LOGS.debug("Input attachment name:" +attachmentName);
							getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(attachmentName);
							
							boolean blankTestStepMessageResult = blankFieldvalidation(expectedTestStepBlankMessage);
		 					if(blankTestStepMessageResult==true){
		 						comments+= " Clicked on save button after entering attachmnet nameand correct message was displayed(Pass).";
		 					}
		 					if(blankTestStepMessageResult==false){
		 						comments+="\n Clicked on save button after entering attachmnet name and correct message was not displayed(Fail).";
		 					}
							APP_LOGS.debug("Clicking on OK Button after clicking save button after entering attachment name");  
						    
						    
						  //Checking validation  after entering attachment name and test step
							   
							    APP_LOGS.debug("Input attachment name:" +attachmentName);
								
							    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(attachmentName);
								
								selectTestStep(testStepName);
								
								boolean blankFileFieldMessageResult = blankFieldvalidation(expectedFileNameBlankMessage);
			 					if(blankFileFieldMessageResult==true)
			 					{
			 						comments+= " Clicked on save button after entering attachmnet name and test step and correct message was displayed(Pass).";
			 					}
			 					if(blankFileFieldMessageResult==false)
			 					{
			 						comments+="\n Clicked on save button after entering attachmnet name and test step and correct message was not displayed(Fail).";
			 					}  
			 					APP_LOGS.debug("Clicking on OK Button after clicking save button after entering attachment name and selecting test step ");  
							    
							    
		 				 //creating an attachment 
							     APP_LOGS.debug("Creating new attachment");
							     createAttachment(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName, attachmentName, Description, testStepName, fileName, TestStepExpectedResult, testerRole, expectedResult);
		 				*/
					}
	 	            else 
	 	            {
	 	            	fail=true;
						APP_LOGS.debug("Login Unsuccessful for Test Manager "+test_Manager.get(0).username);				
						comments += "Login Unsuccessful for Test Manager "+test_Manager.get(0).username;
						assertTrue(false);	
					}
					
	 	            
					
				}
	            else 
	            {
	            	fail=true;
					APP_LOGS.debug("Login Unsuccessful for Version Lead "+versionLead.get(0).username);				
					comments += "Login Unsuccessful for Version Lead "+versionLead.get(0).username;
					assertTrue(false);	
				}
		}
			catch(Throwable t)
            {
				t.printStackTrace();
				fail=true;
				comments += "Fail :-Skip or Any other exception has Occurred.";
				assertTrue(false);	
				APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");
            }
			
		   closeBrowser();	
		    
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);
		}
	}
	
	
	//Private functions
	
	private boolean blankFieldvalidation(String expectedfieldBlankMessage) throws IOException, InterruptedException
	{
		
		//Clicking on save button after entering attachment name,test step
		getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		
		String actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		boolean result = compareStrings(expectedfieldBlankMessage, actualFieldBlankMessage);
		
		//Click on OK button form validation pop up message
		Thread.sleep(1000);
		eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
		    
	    return result;
	}

	//test case selection dropdown
	private void testCaseDropdown (WebElement testCaseDD, String testCaseName) throws InterruptedException
	{
		Thread.sleep(500);
		
		testCaseDD.click();
	
		List<WebElement> testCaseElements = testCaseDD.findElements(By.tagName("option"));
	
		for(int i =0 ;i<testCaseElements.size();i++)
		{
	       if(testCaseElements.get(i).getText().equals(testCaseName))
		    {
		       testCaseElements.get(i).click();
		       
		       APP_LOGS.debug( testCaseName + " : is selected...");
		       
		       break;
	        }			    
		}
	}
	
	
	private int selectTestStep(String TestStep)
	 {
		List<WebElement> testStepCount = getObject("AttachmentsCreateNew_testStepDetailsBox").findElements(By.tagName("li"));
	  
	     for(int i=1; i<=testStepCount.size(); i++)
	     {
	      WebElement testStepNameXpath = getObject("AttachmentsCreateNew_testStepNameXpath1", "AttachmentsCreateNew_testStepNameXpath2", i);
	      String testStepTitle = testStepNameXpath.getAttribute("title");
	      if(testStepTitle.equalsIgnoreCase(TestStep))
	     
	      {
	       testStepFlag++;
	       getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", i).click();
	       break;
	      }
	     }
	     return testStepFlag;
	 }
	

	//create new attachment
	private boolean createAttachment(String group, String portfolio, String project, String version, String testPassName, String TestCase, String AttachmentName, 
			String Description, String TestStep, String fileName1, String TestStepExpectedResult, String TesterRole, String AttachmentSavedMessage) throws IOException, InterruptedException
	{
			if(!getObject("Attachments_groupMemberSelected").getAttribute("title").equalsIgnoreCase(group)){
			       dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			}
			
			if(!getObject("Attachments_portfolioMemberSelected").getAttribute("title").equalsIgnoreCase(portfolio)){
			       dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			}
			
			if(!getObject("Attachments_projectMemberSelected").getAttribute("title").equalsIgnoreCase(project)){
			       dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			}
			
			if(!getObject("Attachments_versionMemberSelected").getAttribute("title").equalsIgnoreCase(version)){
			      dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			}
			
			if(!getObject("Attachments_testPassMemberSelected").getAttribute("title").equalsIgnoreCase(testPassName)){
			      dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
			}
						
			APP_LOGS.debug("Selecting test case:" +TestCase);
			testCaseDropdown(getElement("Attachments_testCaseDropDown_Id"), TestCase);
						
			APP_LOGS.debug("Clicking on Create New link");
			getObject("Attachments_createNew_link").click();
				
			APP_LOGS.debug("Selecting test step:" +TestStep);	
			selectTestStep(TestStep);
		    
		    APP_LOGS.debug("Input attachment name:" +AttachmentName);
			getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
			
			APP_LOGS.debug("Input attachment description:" +Description);
			getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
				    
			APP_LOGS.debug("Uploading file:" +fileName1);
		    getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
	    
		    APP_LOGS.debug("Clicking on Save button");

		    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		    
		    Thread.sleep(3000);
		    
		    String actual_SaveAttachment_SuccessMessage =(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");		

		    if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))
		    {
				APP_LOGS.debug(AttachmentName+": Attachment is Saved Successfully stating : " + actual_SaveAttachment_SuccessMessage);
				return true;
		    }
		    else
		    {
		    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulAttachmentCreation");			    
		    	return false;
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
	public void reportTestResult() throws Exception{
		
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData(){
		
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName()) ;
	}

	
	
		
}





