package com.uat.suite.tm_attachments;

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
public class VerifyValidUserDeleteAttchmnt extends TestSuiteBase
{

	int count=-1;
	boolean skip=false;
	boolean pass=false;
	boolean fail=false;
	boolean isTestPassed=true;
	boolean isLoginSuccess=false;
	String comments;
	String runmodes[]=null;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	String attachmentNameColTitle;
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
	public void VerifyDeleteAttachment(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String testPassName, String TP_TestManager, 
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String testCaseName, String testStepName, String TestStepExpectedResult, String AttachmentName, 
			String Description, String FileName, String AttachmentSavedMessage, String DeleteAttachmentConfirmationMessage,
			String AttachmenttDeletedConfirmationMessage) throws Exception
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
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		 
		//Tester 
		int tester_count = Integer.parseInt(TesterName);
		tester = getUsers("Tester", tester_count);
			
		APP_LOGS.debug("Opening Browser... for role "+role);
			
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
					fail=true;
	 				
	 				APP_LOGS.debug(ProjectName+" not created successfully. ");
	 				
	 				comments += "\n "+ProjectName+" not created successfully.(Fail) ";
	 				
	 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
	 			}
				APP_LOGS.debug(ProjectName+" Project Created Successfully.");
				
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser... ");
				
				openBrowser();
				
				if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
				{
					APP_LOGS.debug("Logged in browser with Version Lead");

					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(3000);
					
					//create test pass
	             	 if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, testPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
		         	 {
	             		fail=true;
	           		    
	           		    APP_LOGS.debug(testPassName+" not created successfully. ");
	                   	
	           		    comments += "\n "+testPassName+" not created successfully.(Fail) ";
	   					 
	           		    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	   					 
	   					throw new SkipException("Test Pass is not created successfully... So Skipping all tests");
                	 }
                	APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
					
					closeBrowser();
					   
	                APP_LOGS.debug("Opening Browser... ");
					
	                openBrowser();
					
					APP_LOGS.debug("Logged in browser with Test Manager");
					
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						APP_LOGS.debug("Logged in browser with Version Lead");

						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(3000);
						
						if(!createTester(GroupName, PortfolioName, ProjectName, Version, testPassName, tester.get(0).username, TesterRole, TesterRole))
						{
							fail=true;
							 
							APP_LOGS.debug(tester.get(0).username+ "Tester not created successfully for test pass "+testPassName);
							
							comments += "\n "+tester.get(0).username+ "Tester not created successfully for test pass "+testPassName+". (Fail)";
		 					
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			throw new SkipException("Tester is not created successfully For Test Pass ... So Skipping all tests");
						}
						APP_LOGS.debug("Tester Created Successfully.");
	    				
						if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, testPassName, testCaseName))
						{
							fail=true;
							
							APP_LOGS.debug(testCaseName+ "Test Case not created successfully for test pass "+testPassName);
							
							comments += "\n "+testCaseName+ "Test Case not created successfully for test pass "+testPassName+". (Fail)";
		 					
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
		        			throw new SkipException("Test Case is not created successfully for test pass... So Skipping all tests");
						}
						APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
       				
						if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, testPassName, testStepName, TestStepExpectedResult, testCaseName, TesterRole))
						{
							 fail=true;
								
							 APP_LOGS.debug(testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName);
							
							 comments += "\n  "+testStepName+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName+". (Fail)";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests");
						}
						APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
		                	
						getElement("TM_attachmentTab_Id").click();
						
						//creating an attachment
                        //createAttachment(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileName, TestStepExpectedResult, TesterRole, AttachmentSavedMessage);
							
						dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
					       
				        dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), PortfolioName );
					       
					    dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
					      
					    dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
					      
					    dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
						
					    //selecting test case
					    APP_LOGS.debug("Selecting test case:" +testCaseName);
					      
				        getElement("Attachments_testCaseDropDown_Id").click();
				      
				        List<WebElement> testCaseElements = getElement("Attachments_testCaseDropDown_Id").findElements(By.tagName("option"));
					    
					    for(int i =0 ;i<testCaseElements.size();i++)
					    {
					       if(assertTrue(testCaseElements.get(i).getText().equals(testCaseName)))
						    {
					    	   testCaseElements.get(i).click();
						       
						       APP_LOGS.debug( testCaseName + " : is selected...");
						       
						       break;
				            }
					    }
                    
					    //creating an attachment
					    APP_LOGS.debug("Clicking on Create New link");
						
					    getObject("Attachments_createNew_link").click();
						Thread.sleep(500);
						    
					    APP_LOGS.debug("Input attachment name:" +AttachmentName);
					   
					    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
					    
						APP_LOGS.debug("Input attachment description:" +Description);
						
						getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
						
						APP_LOGS.debug("Uploading file:" +FileName);
						
						getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+FileName);
						   
						APP_LOGS.debug("Selecting test step:" +testStepName);
						
						getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
						
						APP_LOGS.debug("Clicking on Save button");
						
						getElement("AttachmentsCreateNew_saveButton_Id").click();
					 
						Thread.sleep(3000);
						
					    //verify alert message
			//		    String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
					    String actual_SaveAttachment_SuccessMessage=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
					    if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))   
						{
							APP_LOGS.debug(AttachmentName+" Saved successfully");
							
					//		getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
							
							Thread.sleep(1000);
						}
					    
						else
						{
							fail=true;
							
							APP_LOGS.debug("attachment save popup showing "+actual_SaveAttachment_SuccessMessage+" message. Test case failed");
				    	   
							comments += "\n Attachment did not save successfully (Fail)";
					    	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), AttachmentSavedMessage+" meassge is not displayed on popup");
							
							throw new SkipException(AttachmentSavedMessage+" meassge is not displayed on Attachment Save popup ... So Skipping all tests");
						}
					    
                	    WebElement attachmentTableColsXpath = getObject("AttachmentsViewAll_tableXpath1", "AttachmentsViewAll_tableXpath2", 1);
                			
                	    attachmentNameColTitle = attachmentTableColsXpath.getText();
                			
                		if(compareStrings(AttachmentName, attachmentNameColTitle))
                		{
                			APP_LOGS.debug("Added attachment(s) is displayed on View All Page.");
                				
                			comments += "\n Added attachment(s) is displayed on View All Page(Pass).";
                		}
                		else
                		{
                			fail = true;									
    							
    						APP_LOGS.debug("Added attachment(s) is NOT displayed on View All Page. Test case has been failed.");									
    							
    						comments += "\n Added attachment(s) is NOT displayed on View All Page(Fail).";		
    							
    						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Added attachment(s) is NOT displayed on View All Page");		
                		}
					    
                		getObject("AttachmentsViewAll_deleteImageXpath1", "AttachmentsViewAll_deleteImageXpath2", 1).click();
                	    
                		//verify delete alert message	
                		String DeleteButton_PopUp_Message = getElement("ProjectViewAll_deleteProjectConfirmationText_Id").getText();
                //		String DeleteButton_PopUp_Message = (String)eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
						if(compareStrings(DeleteAttachmentConfirmationMessage, DeleteButton_PopUp_Message))
						{
							APP_LOGS.debug(DeleteButton_PopUp_Message+ " Message displayed on Delete Popup");
							  
							comments += "\n "+DeleteButton_PopUp_Message+ " Message displayed on Delete Popup(Pass)";
						}
						else
						{
							fail = true;									
								
							APP_LOGS.debug(DeleteButton_PopUp_Message+ " Message displayed on Delete Popup. Test case has been failed.");									
								
							comments += "\n "+ DeleteButton_PopUp_Message+ " Message displayed on Delete Popup(Pass)";		
								
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected Popup text is not displayed After Click on Delete");									
						}
								  
						APP_LOGS.debug("Clicking on Cancel Button");
						 
						  // Xpath - //span[text()='Cancel'] is used for clicking on Cancel button.
						  
						getObject("AttachmentViewAll_delectAttachmentPopUpCancelButton").click();
								 
						attachmentTableColsXpath = getObject("AttachmentsViewAll_tableXpath1", "AttachmentsViewAll_tableXpath2", 1);
	          			
	              	    attachmentNameColTitle = attachmentTableColsXpath.getText();
	              			
	              	    if(compareStrings(AttachmentName, attachmentNameColTitle))
	              	    {
	              	    	APP_LOGS.debug("Attachment is not deleted from table");
	        				
	              	    	comments += "\n Attachment is not deleted from table (After click on Cancel Button)(Pass).";
	              	    }
	              	    else
	              	    {
	              	    	fail = true;									
								
	              	    	APP_LOGS.debug("\n Attachment is deleted from table (After click on Cancel Button).Test case Failed.");									
								
	              	    	comments += "\n Attachment is deleted from table (After click on Cancel Button)(Fail).";		
								
	              	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Attachment not displayed on view All after click on cancel button");
	              	    }
							  
					    Thread.sleep(1000);
					    
					    getObject("AttachmentsViewAll_deleteImageXpath1", "AttachmentsViewAll_deleteImageXpath2", 1).click();
						
					    Thread.sleep(1500);
										 
					    // Xpath - //span[text()='Delete'] is used for clicking on Delete button.

					    getObject("AttachmentViewAll_delectAttachmentPopUpDeleteButton").click();
									    
			//		    String actualDeleteConfirmationMessage = getElement("AttachmentViewAll_attachmentDeletedConfirmationText_Id").getText();
					    String actualDeleteConfirmationMessage = (String)eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
						if(compareStrings(AttachmenttDeletedConfirmationMessage, actualDeleteConfirmationMessage))
						{
							APP_LOGS.debug(" Delete confirmation message is correct stating: "+AttachmenttDeletedConfirmationMessage);
            				
							comments += "\n Delete confirmation message is correct stating: "+AttachmenttDeletedConfirmationMessage +" (Pass)";
						}
						else
						{
							fail = true;									
							
							APP_LOGS.debug(" Delete confirmation message is NOT correct stating: "+actualDeleteConfirmationMessage);
            				
							comments += "\n Delete confirmation message is NOT correct stating: "+actualDeleteConfirmationMessage +" (Fail)";
            					
	              	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Delete confirmation popup message is NOT correct");
						}
						
			//			getObject("AttachmentViewAll_attachmentDeletedSuccessfullyOkButton").click();
        				
						Thread.sleep(1000);
						
						if (compareStrings("No Attachment Available.", getElement("Attachments_noAttachmentAvailableDiv_Id").getText())) 
						{
							  APP_LOGS.debug("Attachment is deleted from table");
	            				
	              	    	  comments += "\n Attachment is deleted from table (After click on Delete Button)(Pass).";
	              	    }
	              	    else
	              	    {
	              	    	fail = true;									
							
	              	    	APP_LOGS.debug("\n Attachment is NOT deleted from table (After click on Delete Button).Test case Failed.");									
								
	              	    	comments += "\n Attachment is NOT deleted from table (After click on Delete Button)(Fail).";		
								
	              	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Attachment is displayed on view All after click on Delete button");
	              	    }
		            }
				    else 
					{
				    	fail=true;
						APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);				
						comments  +=  "Login Unsuccessful for Test Manager "+testManager.get(0).username;
						assertTrue(false);	
					}
				}						
				else
				{
					fail=true;
					APP_LOGS.debug("Login Unsuccessful for Version Lead "+versionLead.get(0).username);				
					comments  +=  "Login Unsuccessful for Version Lead "+versionLead.get(0).username;
					assertTrue(false);	
				}
			} 
			catch(Throwable t)
	        {
				t.printStackTrace();
				fail=true;
				comments  +=  "\n Skip or Any other exception has Occurred.(Fail)";
				assertTrue(false);	
				APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");
	        }
			
		    closeBrowser();	
		}			
		else
		{
			APP_LOGS.debug("Login Unsuccessful for user "+role);
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
		}
	    else
	    {
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		}
		utilRecorder.stopRecording();
     }
}

