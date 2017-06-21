package com.uat.suite.tm_attachments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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
public class VerifyAllNoneSelectedOptns extends TestSuiteBase
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
	int testStepCount = 5;
	int i;
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
	public void VerifyAllNoneSelectedOptions(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String TestPassName, String TP_TestManager, 
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String testCaseAllNoneFunctionality, String testCaseSelectedFunctionality, String testStepForAllNone, 
			String testStepForSelected, String TestStepExpectedResult, String attachmentNameForAllNone, 
			String attachmentNameForSelected, String Description, String attachmentFileForAllNone, String attachmentFileForSelected,
			String AttachmentSavedMessage, String ExpectedAttachmentNotSelectedMessage) throws Exception
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
			
			if(isLoginSuccess) 
			{
				try
                {
	          		//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
					
					if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
	     			{
	     				APP_LOGS.debug("Project is not created successfully");
	     				comments= "Project not Created Successfully(Fail). ";
	     				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
	    				
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
	                 		APP_LOGS.debug("Test Pass is not created successfully");
	                 		comments+="Test Pass not Created Successfully(Fail). ";
	                 		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
	         					 
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
	                        	 APP_LOGS.debug("Tester is not created successfully");
	                        	 comments+="Tester not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
	         					 
	                			 throw new SkipException("Tester is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							APP_LOGS.debug("Tester Created Successfully.");
	        			
							APP_LOGS.debug("Creating Test Case");
							
							/*Thread.sleep(2000);
							
							getElement("TestCaseNavigation_Id").click();
							
							if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
							{
								getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
							}
							
							getObject("TestCase_createNewProjectLink").click();
							
							//Thread.sleep(1000);
							
							if(!createTestCase(testCaseAllNoneFunctionality))
							{
	                        	 APP_LOGS.debug("Test Case is not created successfully");
	                        	 comments+="Test Case not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
	         					 
	                			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							
							if(!createTestCase(testCaseSelectedFunctionality))
							{
	                        	 APP_LOGS.debug("Test Case is not created successfully");
	                        	 comments+="Test Case not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
	         					 
	                			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							APP_LOGS.debug("Test Case Created Successfully.");
	        			*/
							if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, testCaseAllNoneFunctionality))
							{
								 fail=true;
									
								 APP_LOGS.debug(testCaseAllNoneFunctionality+ "Test Case not created successfully for test pass "+TestPassName);
								
								 comments+="Fail Occurred:- "+testCaseAllNoneFunctionality+ "Test Case not created successfully for test pass "+TestPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Test Case is not created successfully for test pass... So Skipping all tests");
							}
							APP_LOGS.debug(testCaseAllNoneFunctionality+" Test Case Created Successfully.");
							
							if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, testCaseSelectedFunctionality))
							{
								 fail=true;
									
								 APP_LOGS.debug(testCaseSelectedFunctionality+ "Test Case not created successfully for test pass "+TestPassName);
								
								 comments+="Fail Occurred:- "+testCaseSelectedFunctionality+ "Test Case not created successfully for test pass "+TestPassName+". ";
			 					
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
			 					 
			        			 throw new SkipException("Test Case is not created successfully for test pass... So Skipping all tests");
							}
							APP_LOGS.debug(testCaseSelectedFunctionality+" Test Case Created Successfully.");
							
							APP_LOGS.debug("Creating Test Step");
																											
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, testStepForAllNone, 
									TestStepExpectedResult, testCaseAllNoneFunctionality, TesterRole))
							{
								// fail=true;
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, testStepForSelected, 
									TestStepExpectedResult, testCaseAllNoneFunctionality, TesterRole))
							{
								// fail=true;
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
							}							
							
							
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, testStepForAllNone, 
									TestStepExpectedResult, testCaseSelectedFunctionality, TesterRole))
							{
								// fail=true;
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
							}
						  		
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, testStepForSelected, 
									TestStepExpectedResult, testCaseSelectedFunctionality, TesterRole))
								
							{
								// fail=true;
	                        	 APP_LOGS.debug("Test Step is not created successfully");
	                        	 comments+="Test Step not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							APP_LOGS.debug("Test Step Created Successfully.");
			              
							getElement("TM_attachmentTab_Id").click();
							
	                        //selecting dropdown values
							/*dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
						       
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
								       comments+="Pass- Correct " +TestCase+ " selected. ";
								       break;
						            }
							       else
							       {
							    	   fail=true;
							    	   APP_LOGS.debug(TestCase+ " not available in dropdown ");
							    	   comments+="Fail- "+TestCase+ " not available in dropdown ";
							    	   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseNotAvailable");
							       }
						   }*/
							
							getObject("Attachments_createNew_link").click();
							Thread.sleep(500);
							
							Select testCaseDropDown = new Select(getElement("Attachments_testCaseDropDown_Id")) ;
							
							testCaseDropDown.selectByVisibleText(testCaseAllNoneFunctionality);
							
						    //create an attachment to verify selected option
							getElement("AttachmentsCreateNew_testStepNameAllLink_ID").click();
							
							Thread.sleep(500);
							
							getElement("AttachmentsCreateNew_testStepNameNoneLink_ID").click();
							
	                	    if (createAttachmentAndVerifyMessageForNoSelectedTestStep(attachmentNameForAllNone, Description, attachmentFileForAllNone, 
	                	    		ExpectedAttachmentNotSelectedMessage)) 
	                	    {
	                	    	APP_LOGS.debug("On Selecting None, user gets expected message on Save(Pass). ");
								comments+="On Selecting None, user gets expected message on Save(Pass). ";
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	APP_LOGS.debug("On Selecting None, user doesn't get expected message on Save(Fail). ");
	            				comments+="On Selecting None, user doesn't get expected message on Save(Fail). ";	            				
	                	    }
	                	    
	                	    getObject("Attachments_viewAll_link").click();
	                	    Thread.sleep(500);
	                	    
	                	    if (getElement("Attachments_noAttachmentAvailableDiv_Id").isDisplayed()) 
	                	    {
	                	    	APP_LOGS.debug("No attachments saved after selecting none(Pass). ");
								comments+="No attachments saved after selecting none(Pass). ";
							}
	                	    else
	                	    {
	                	    	assertTrue(false);
	                	    	fail = true;
	                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Attachments Saved after None");
	                	    	APP_LOGS.debug("Attachments saved after selecting none(Fail). ");
								comments+="Attachments saved after selecting none(Fail). ";
	                	    }
	                
	                	    getObject("Attachments_createNew_link").click();
							Thread.sleep(500);							
	                	    	                	    
	                	    getElement("AttachmentsCreateNew_testStepNameAllLink_ID").click();
	                	    
	                	    if (createAttachmentAndVerifyMessage(attachmentNameForAllNone, Description, attachmentFileForAllNone, 
	                	    		AttachmentSavedMessage)) 
	                	    {
								APP_LOGS.debug("On Selecting All, user gets expected message on Save(Pass). ");
								comments+="On Selecting All, user gets expected message on Save(Pass). ";
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	APP_LOGS.debug("On Selecting All, user doesn't get expected message on Save(Fail). ");
	            				comments+="On Selecting All, user doesn't get expected message on Save(Fail). ";	            				
	                	    }
	                	    
	                	    Thread.sleep(500);
	                	    
	                	    if (assertTrue(getElement("AttachmentsViewAll_table_Id").isDisplayed())) 
	                	    {
	                	    	String viewAllTableTestStepColumnText = getObject("AttachmentsViewAll_testStepText1", 
	                	    			"AttachmentsViewAll_testStepText2", 1).getAttribute("title");
	                	    	
								if (assertTrue(viewAllTableTestStepColumnText.contains(testStepForAllNone) && 
										viewAllTableTestStepColumnText.contains(testStepForSelected))) 
								{
									APP_LOGS.debug("Attachment added successfully for all test steps(Pass). ");
		            				comments+="Attachment added successfully for all test steps(Pass). ";
								}
								else
								{
									fail = true;
		                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Name don't match");
									APP_LOGS.debug("Attachment did not add successfully for all test steps(Fail). ");
		            				comments+="Attachment did not add successfully for all test steps(Fail). ";
								}
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "View all Table Not displayed For All Option");
	                	    	APP_LOGS.debug("On Saving attachment with All selected, View all Table Not displayed(Fail). ");
	            				comments+="On Saving attachment with All selected, View all Table Not displayed(Fail). ";	
	                	    }
	                	    
	                	    Thread.sleep(500);
	                	    getObject("Attachments_createNew_link").click();
							Thread.sleep(500);							
	                	    
							testCaseDropDown = new Select(getElement("Attachments_testCaseDropDown_Id")) ;
	                	    testCaseDropDown.selectByVisibleText(testCaseSelectedFunctionality);
	                	    Thread.sleep(500);
	                	    
	                	    getObject("AttachmentsCreateNew_testStepNameCheckBox1", "AttachmentsCreateNew_testStepNameCheckBox2", 2).click();
	                	    
	                	    getElement("AttachmentsCreateNew_testStepNameSelectedLink_ID").click();
	                	    
	                	    boolean isFirstTestStepDisplayed = getObject("AttachmentsCreateNew_testStepNameXpath1", "AttachmentsCreateNew_testStepNameXpath2", 1).isDisplayed();
	                	    boolean isSecondTestStepDisplayed = getObject("AttachmentsCreateNew_testStepNameXpath1", "AttachmentsCreateNew_testStepNameXpath2", 2).isDisplayed();
	                	    
	                	    if(isSecondTestStepDisplayed && (!isFirstTestStepDisplayed) ) 
	                	    {
	                	    	APP_LOGS.debug("On clicking Selected option, only selected test step is shown(Pass). ");
	            				comments+="On clicking Selected option, only selected test step is shown(Pass). ";
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step not proper for Selected Option");
	                	    	assertTrue(false);
	                	    	APP_LOGS.debug("On clicking Selected option, the shown test step is not proper(Fail). ");
	            				comments+="On clicking Selected option, the shown test step is not proper(Fail). ";
	                	    }
	                	    
	                	    if(createAttachmentAndVerifyMessage(attachmentNameForSelected, Description, attachmentFileForSelected, AttachmentSavedMessage)) 
	                	    {								
	                	    	APP_LOGS.debug("On Selecting Selected, user gets expected message on Save(Pass). ");
								comments+="On Selecting Selected, user gets expected message on Save(Pass). ";
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	APP_LOGS.debug("On Selecting Selected, user doesn't get expected message on Save(Fail). ");
	            				comments+="On Selecting Selected, user doesn't get expected message on Save(Fail). ";	            				
	            				 
	                	    }
	                	    
	                	    Thread.sleep(500);
	                	    
	                	    if(assertTrue(getElement("AttachmentsViewAll_table_Id").isDisplayed())) 
	                	    {
	                	    	String viewAllTableTestStepColumnText = getObject("AttachmentsViewAll_testStepText1", 
	                	    			"AttachmentsViewAll_testStepText2", 1).getAttribute("title");
	                	    	
								if (assertTrue(viewAllTableTestStepColumnText.equals(testStepForSelected))) 
								{
									APP_LOGS.debug("Attachment added successfully for selected test step(Pass). ");
		            				comments+="Attachment added successfully for selected test step(Pass). ";
								}
								else
								{
									fail = true;
		                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Name don't match");
									APP_LOGS.debug("Attachment did not add successfully for selected test step(Fail). ");
		            				comments+="Attachment did not add successfully for selected test step(Fail). ";
								}
							}
	                	    else
	                	    {
	                	    	fail = true;
	                	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "View all Table Not displayed For Selected Option");
	                	    	APP_LOGS.debug("On Saving attachment with Selected Option, View all Table Not displayed(Fail). ");
	            				comments+="On Saving attachment with Selected Option, View all Table Not displayed(Fail). ";	
	                	    }	   
	                }
				    else 
					{
						fail = true;
						APP_LOGS.debug("Login Unsuccessfull");
						comments+="Login Unsuccessfull for test manager";
					}
				}						
				else
				{
					fail = true;
					APP_LOGS.debug("Login Unsuccessfull");
					comments+="Login Unsuccessfull for version lead";					
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
		   // TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		    //TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
		utilRecorder.stopRecording();
     }
	
	
	//create new attachment
	public void createAttachment1(String group, String portfolio, String project, String version, String testPassName, String TestCase, String AttachmentName, 
			String Description, String TestStep, String fileName1, String TesterRole, String AttachmentSavedMessage, int testStepCountToBeSelected, String ExpectedAttachmentNotSelectedMessage) throws IOException, InterruptedException
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
		if(testStepCountToBeSelected==1)	
		{
			APP_LOGS.debug("Selecting test step:" +TestStep);
			getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
			
			//clicking on selected link
			APP_LOGS.debug("Selecting test step:" +TestStep);
			getElement("AttachmentsCreateNew_testStepNameSelectedLink_ID").click();
		}
		else if(testStepCountToBeSelected>1)
		{
			APP_LOGS.debug("Selecting None option");
			getElement("AttachmentsCreateNew_testStepNameNoneLink_ID").click();
			Thread.sleep(2000);
			try
			{
				getElement("AttachmentsCreateNew_saveButton_Id").click();
				Thread.sleep(2000);
		//		String actual_attachmentNotSelected_Message = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
				String actual_attachmentNotSelected_Message=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
				compareStrings(ExpectedAttachmentNotSelectedMessage, actual_attachmentNotSelected_Message);
		//		getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
				Thread.sleep(2000);
			}
			catch(Throwable t)
			{
				fail=true;
			}
			
			getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
			getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
			getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
			
			//clicking on All link
			APP_LOGS.debug("Selecting All option");
			getElement("AttachmentsCreateNew_testStepNameAllLink_ID").click();
		}
		
		APP_LOGS.debug("Clicking on Save button");
	    try
	    {
		    getElement("AttachmentsCreateNew_saveButton_Id").click();
		    APP_LOGS.debug("Save button clicked");
    	    comments+="Pass- Save button clicked";
		    Thread.sleep(3000);
	    }
	    catch(Throwable t)
	    {
	    	fail=true;
	    	APP_LOGS.debug("Save button not clickable");
    	    comments+="Fail- Save button not clickable";
	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SaveAttachmentOperationError");
	    }
      
	    //verify alert message
	   // String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
	    String actual_SaveAttachment_SuccessMessage = (String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
	    if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))   
		{
			APP_LOGS.debug(AttachmentName+" Save successfully");
			comments+="Pass- "+AttachmentName+" Save successfully";
		//	getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
			Thread.sleep(1000);
		}
	    
		else
		{
			fail=true;
			APP_LOGS.debug(AttachmentName+" is not Saved");
    	    comments+="Fail- "+AttachmentName+" is not Saved";
	    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNotSavedError");
		}
	}
	
	private boolean createAttachmentAndVerifyMessage(String AttachmentName,String Description, String fileToupload,String expectedMessageOnSave) throws IOException, InterruptedException
	{
			
		//creating an attachment
	    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
	    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
					
		APP_LOGS.debug("Inputing attachment description:" +Description);
		getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
					    
		APP_LOGS.debug("Uploading file:" +fileToupload);
		getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileToupload);
		   
		//selecting test step
		
		getElement("AttachmentsCreateNew_saveButton_Id").click();
		Thread.sleep(3000);				
      
	    //verify alert message
	 //   String popUpMessageAfterSave = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
		String popUpMessageAfterSave=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
		if(compareStrings(expectedMessageOnSave, popUpMessageAfterSave))   
		{
		//	getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
			return true;
		}	    
		else
		{	
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), expectedMessageOnSave);
			getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
			return false;    	   
		}					
	}
	
	private boolean createAttachmentAndVerifyMessageForNoSelectedTestStep(String AttachmentName,String Description, String fileToupload,String expectedMessageOnSave) throws IOException, InterruptedException
	{
			
		//creating an attachment
	    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
	    getObject("AttachmentsCreateNew_attachmentNameTextField").sendKeys(AttachmentName);
					
		APP_LOGS.debug("Inputing attachment description:" +Description);
		getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
					    
		APP_LOGS.debug("Uploading file:" +fileToupload);
		getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileToupload);
		   
		//selecting test step
		
		getElement("AttachmentsCreateNew_saveButton_Id").click();
		Thread.sleep(3000);				
      
	    //verify alert message
	    String popUpMessageAfterSave = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
	//	String popUpMessageAfterSave=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
		if(compareStrings(expectedMessageOnSave, popUpMessageAfterSave))   
		{
			getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
			return true;
		}	    
		else
		{	
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), expectedMessageOnSave);
			getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
			return false;    	   
		}					
	}
	
	private boolean createTestCase(String testCaseName) throws IOException, InterruptedException
	{
		Thread.sleep(1000);

		try 
		{				
			getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
				
			getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
				
		//	Thread.sleep(2000);
			String createTestCaseResult=getTextFromAutoHidePopUp();
			
			if (createTestCaseResult.contains("successfully")) 
			{
		//		getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
					
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestCase function.");
			e.printStackTrace();
			return false;
		}
	}
					
	//create test steps
	private boolean createTestStep(String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
	{
		try 
		{
			Thread.sleep(1000);
				
			String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+"')";
			eventfiringdriver.executeScript(testStepDetails);
			    
			getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults); 
				
			List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
			int numOfTestCases = TestCaseSelectionNames.size();
			for(int i = 0; i<numOfTestCases;i++)
			{
				if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
				{
					getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
					break;
				}
			}
				
			String[] testerRoleSelectionArray = rolesToBeSelected.split(",");
			List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
			int numOfRoles = roleSelectionNames.size();
			for(int i = 0; i<numOfRoles;i++)
			{
				for(int j = 0; j < testerRoleSelectionArray.length;j++)
				{
					if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
					{
						getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
					}
				}
			}
				
			getElement("TestStepCreateNew_testStepSaveBtn_Id").click();
				
		//	Thread.sleep(2000);
			String testStepCreatedResult=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
			
			if (testStepCreatedResult.contains("successfully")) 
			{
		//		getObject("TestStep_testStepaddedsuccessfullyOkButton").click();

				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			return false;
		}
	}
}


