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
public class EditAttachmentFields extends TestSuiteBase
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
	public void VerifyEditAttachment(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String testPassName,String TP_TestManager, String TP_EndMonth,
			String TP_EndYear, String TP_EndDate, String TesterName, String TesterRole, String testCaseName,String testStepName1, 
			String testStepName2, String TestStepExpectedResult, String AttachmentName, String Description, String FileNamePath1,
			String AttachmentSavedMessage, String editAttachmentName, String EditedDescription, String AttachmentEditedMessage) throws Exception
	{
			count++;		
			
			comments = "";

			// test the runmode of current dataset
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
		 				
		 				comments= comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
		 				
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
		                   	
		           		    comments+="Fail Occurred:- "+testPassName+" not created successfully. ";
		   					 
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
								
								 comments+="Fail Occurred:- "+tester.get(0).username+ "Tester not created successfully for test pass "+testPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Tester is not created successfully For Test Pass ... So Skipping all tests");
							}
							APP_LOGS.debug(" Tester Created Successfully.");
	        				
							if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, testPassName, testCaseName))
							{
								 fail=true;
									
								 APP_LOGS.debug(testCaseName+ "Test Case not created successfully for test pass "+testPassName);
								
								 comments+="Fail Occurred:- "+testCaseName+ "Test Case not created successfully for test pass "+testPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Test Case is not created successfully for test pass... So Skipping all tests");
							}
							APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
	        				
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, testPassName, testStepName1, TestStepExpectedResult,
												testCaseName, TesterRole))
							{
								 fail=true;
								
								 APP_LOGS.debug(testStepName1+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName);
								
								 comments+="Fail Occurred:- "+testStepName1+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests");
							}
							APP_LOGS.debug(testStepName1+" Test Step Created Successfully.");
							
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, testPassName, testStepName2, TestStepExpectedResult,
									testCaseName, TesterRole))
							{
								 fail=true;
								
								 APP_LOGS.debug(testStepName2+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName);
								
								 comments+="Fail Occurred:- "+testStepName2+ "Test Step not created successfully under Test Case "+testCaseName+ " for test pass "+testPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests");
							}
							APP_LOGS.debug(testStepName2+" Test Step Created Successfully.");
							
							//Click on attachment tab 
							
							getElement("TM_attachmentTab_Id").click();
							
	                        //selecting dropdown values
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
										    
							APP_LOGS.debug("Uploading file:" +FileNamePath1);
							
							getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+FileNamePath1);
							   
							APP_LOGS.debug("Selecting test step:" +testStepName1);
							
							getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
							
							APP_LOGS.debug("Clicking on Save button");
							
							getElement("AttachmentsCreateNew_saveButton_Id").click();
						 
					//		String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
							
							String actual_SaveAttachment_SuccessMessage =(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
							Thread.sleep(3000);
							
						    if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))   
							{
								APP_LOGS.debug(AttachmentName+" Saved successfully");
								
					//			getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
					//			Thread.sleep(1000);
							}
							else
							{
								fail=true;
								
								APP_LOGS.debug("attachment save popup showing "+actual_SaveAttachment_SuccessMessage+" message. Test case failed");
					    	   
								comments+="\n Attachment did not save successfully (Fail)";
						    	
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), AttachmentSavedMessage+" meassge is not displayed on popup");
								
								throw new SkipException(AttachmentSavedMessage+" meassge is not displayed on Attachment Save popup ... So Skipping all tests");
							}
						    
							int flag = 0;
							
							WebElement attachmentTable = getObject("AttachmentsViewAll_table");
	                		
							List<WebElement> attachmentRows = attachmentTable.findElements(By.tagName("tr"));
	                		
	                	    for (int i =  1; i <= attachmentRows.size(); i++) 
	                	    {	
	                	    	//Extracting the number of project present in Project column
	                	    	WebElement attachmentTableColsXpath = getObject("AttachmentsViewAll_tableXpath1", "AttachmentsViewAll_tableXpath2", i);
	                			
	                	    	String attachmentNameColTitle = attachmentTableColsXpath.getText();
	                			
	                			if(compareStrings(AttachmentName, attachmentNameColTitle))
	                			{
	                				flag  =  1;
	                			}
	                			else
	                			{
	                				flag  =  0;
	                			}
	                	    }
	                	    
	                	    if (flag == 1) 
	                	    {
	                	    	APP_LOGS.debug("Added attachment(s) is displayed on View All Page.");
                				
                				comments+="Added attachment(s) is displayed on View All Page(Pass).";
							}
	                	    else 
	                	    {
	                	    	fail = true;									
								
								APP_LOGS.debug("Added attachment(s) is NOT displayed on View All Page. Test case has been failed.");									
								
								comments+="\n Added attachment(s) is NOT displayed on View All Page(Fail).";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Added attachment(s) is NOT displayed on View All Page");									
							}
	                	    
	                	    APP_LOGS.debug("Clicking on edit icon of attachment.");
	                	    
	                	    getObject("AttachmentsViewAll_editImageXpath1", "AttachmentsViewAll_editImageXpath2", 1).click();
            				Thread.sleep(1000);
                        
            				
            				String editAttachmentClassName = getObject("AttachmentsEdit_editAttachmentTab").getAttribute("class");
            				
            				String editAttachmentText = getObject("AttachmentsEdit_editAttachmentTab").getText();
            				
            				if (assertTrue(getObject("AttachmentsEdit_editAttachmentTab").isDisplayed()) && 
            						compareStrings("activeTab", editAttachmentClassName) && compareStrings("EDIT ATTACHMENT", editAttachmentText)) 
            				{
            					APP_LOGS.debug("Edit Attachment Tab is displayed after click on Edit Icon");
            					
            					comments+="\n Edit Attachment Tab is displayed after click on Edit Icon(Pass).";		
							}
            				else 
            				{
            					fail = true;									
								
								APP_LOGS.debug("Edit Attachment Tab is NOT displayed after click on Edit Icon. Test case has been failed.");									
								
								comments+="\n Edit Attachment Tab is NOT displayed after click on Edit Icon(Fail).";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Attachment Tab is NOT displayed after click on Edit");									
		 						
							    throw new SkipException("Edit Attachment Tab is NOT displayed after click on Edit Icon... So Skipping all tests");
							}
            				
	                	    //update attachment name and description fields
	                	    
	                	    APP_LOGS.debug("Update attachment name with :" +editAttachmentName);
	                	   
	                	    getObject("AttachmentsCreateNew_attachmentNameTextField").clear();
	                	   
	                	    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(editAttachmentName);
	                		
	                        APP_LOGS.debug("Update adescription with :" +EditedDescription);
	                		
	                        getObject("AttachmentsCreateNew_attachmentDescriptionTextField").clear();
	                        
	                        getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(EditedDescription);
	                		
	                		APP_LOGS.debug("Selecting test step:" +testStepName2);
							
							getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 2).click();
							
							if (!getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").isDisplayed()) 
							{
								APP_LOGS.debug("No option is available to update 'attached file' field while Editing the Attachment.");
								
								comments+="\n No option is available to update 'attached file' field while Editing the Attachment(Pass).";		
							}
							else 
							{
								fail = true;		
								
								assertTrue(false);
								
								APP_LOGS.debug("Browse File is Displayed to update 'attached file' field While Editing the Attachment. Test case has been failed.");									
								
								comments+="\n An Option is available to update 'attached file' field While Editing the Attachment(Fail).";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Browse File is Displayed while Editing attachment");									
							}
							
	                		if (getElement("AttachmentsCreateNew_previewBox_Id").isDisplayed() && 
	                				getElement("AttachmentsCreateNew_attachmentDownloadBtn").isDisplayed())
	                		{
	                			APP_LOGS.debug("Open/View Button is displayed for Attachment Preview field while Editing the Attachment.");
								
								comments+="\n Open/View Button is displayed for Attachment Preview field while Editing the Attachment(Pass).";
							}
	                		else 
							{
								fail = true;		
								
								assertTrue(false);
								
								APP_LOGS.debug("Open/View Button is NOT displayed for Attachment Preview field while Editing the Attachment. Test case has been failed.");									
								
								comments+=" Open/View Button is NOT displayed for Attachment Preview field while Editing the Attachment(Fail).";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Download Button is NOT displayed While editing");									
							}
	                		
	                		APP_LOGS.debug("Clicking on Update button");
	                		    
	                		getElement("AttachmentsCreateNew_attachmentUpdateBtn").click();
	                		
	                		Thread.sleep(1000);
	                		 
	                //	    String updateActualMessage = getElement("AttachmentsEdit_attachmentSuccessMessageText_Id").getText();
	                		String updateActualMessage=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
                			if(compareStrings(AttachmentEditedMessage, updateActualMessage))
                			{
	                			APP_LOGS.debug(editAttachmentName+": Attachment is updated Successfully stating : " + updateActualMessage);
	                			
	                			comments+= "\n"+ editAttachmentName+ ": Attachment is updated Successfully stating : " + updateActualMessage +" (Pass).";
	             			    
	           //     			getObject("AttachmentsCreateNew_attachmentSuccessPopUpOkBtn").click();  
	             			    
	                			Thread.sleep(1000);
	                		}
                			else
	                		{
	                			fail=true;
	                			
	                			APP_LOGS.debug(editAttachmentName+": Attachment is NOT updated Successfully stating : " + updateActualMessage);
					    	  
	                			comments+= "\n"+ editAttachmentName+ ": Attachment is NOT updated Successfully stating : " + updateActualMessage +" (Fail).";
							   
							    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Attachment is updated Successfully popup text not displayed");
	                		
							    throw new SkipException("Update Popup Text is not displayed and OK button has not clicked... So Skipping all tests");
	                		}
	                		
                			String attachmentNameInTable = getObject("AttachmentsViewAll_attachmentNameInTable").getText();
			 				
                			String testStepTextInTable = getObject("AttachmentsViewAll_testStepInTable1", "AttachmentsViewAll_testStepInTable2", 1).getText();
                			
			 				if (compareStrings(editAttachmentName, attachmentNameInTable) &&
			 						assertTrue(testStepTextInTable.contains(testStepName1) && testStepTextInTable.contains(testStepName2))) 
			 				{
			 					getObject("AttachmentsViewAll_editImageXpath1", "AttachmentsViewAll_editImageXpath2", 1).click();
		            				
			 				    Thread.sleep(1000);
			 					
			 				    int flag1 = 1;
			 				    
			 					String descriptionText = getObject("AttachmentsEdit_descriptionText").getText();
			 					 
			 					if (compareStrings(EditedDescription, descriptionText)) 
			 					{
			 						APP_LOGS.debug("Edited description is displayed in discription text area after update.");	
			 					}
			 					else 
			 					{
			 						fail = true;									
									
									APP_LOGS.debug("Edited description is NOT displayed in disription text area after update. Test case has been failed.");									
									
									comments+="\n Edited description is NOT displayed in disription text area after update(Fail).";		
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edited description is NOT displayed after update");
									
									flag1 = 0;
			 					}
			 					
		 						if (getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).isSelected() &&
		 								getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 2).isSelected()) 
		 						{
		 							APP_LOGS.debug("Displyed test step in view all page is displayed selected on Edit attachment page.");
								}
		 						else
		 						{
		 							fail = true;									
									
									APP_LOGS.debug("Displyed test step in view all page is NOT displayed selected on Edit attachment page. Test case has been failed.");									
									
									comments+="\n Displyed test step in view all page is NOT displayed selected on Edit attachment page.(Fail).";		
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Both checkboxes are not checked in Select Test Step box");	
									
									flag1 = 0;
								}
			 					
		 						if (flag1 != 0) 
		 						{
		 							APP_LOGS.debug("Updated attachment details are Saved properly and displyed on View all page and Edit attachment page.");
				 					
				 					comments+= "\n Updated attachment details are Saved properly and displyed on View all page and Edit attachment page.";
								}
							}
			 				else 
			 				{
			 					fail = true;									
								
								APP_LOGS.debug("Updated attachment is NOT displayed on View All Page. Test case has been failed.");									
								
								comments+="\n Added attachment is NOT displayed on View All Page(Fail).";		
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), editAttachmentName+" is NOT displayed on View All Page");									
							}
				        }
					    else 
						{
					    	fail=true;
							APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);				
							comments += "Login Unsuccessful for Test Manager "+testManager.get(0).username;
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
		else if(!isLoginSuccess){
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
