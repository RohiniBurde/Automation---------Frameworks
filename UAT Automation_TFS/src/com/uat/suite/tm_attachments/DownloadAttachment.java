package com.uat.suite.tm_attachments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
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
public class DownloadAttachment extends TestSuiteBase
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
			String EndMonth, String EndYear, String EndDate, String VersionLead, String TestPassName, String TP_TestManager, 
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String TestCase, String TestStep, String TestStepExpectedResult, String AttachmentName, 
			String Description, String FileName, String AttachmentSavedMessage, String DownloadExpectedResult) throws Exception
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
			versionLead=new ArrayList<Credentials>();
			versionLead = getUsers("Version Lead", versionlead_count);
			 
			//TestManager 
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager=new ArrayList<Credentials>();
			testManager = getUsers("Test Manager", testManager_count);
			 
			//Tester 
			int tester_count = Integer.parseInt(TesterName);
			tester=new ArrayList<Credentials>();
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
					Thread.sleep(1000);
					
					if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
         			{
         				//fail=true;
         				APP_LOGS.debug("Project is not created successfully");
         				comments+= "Project not Created Successfully(Fail). ";
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
	         				// closeBrowser();
	         					 
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
								 //fail=true;
	                        	 APP_LOGS.debug("Test Case is not created successfully");
	                        	 comments+="Test Case not Created Successfully(Fail). ";
	         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							APP_LOGS.debug("Test Case Created Successfully.");
	        				
							if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestStep, TestStepExpectedResult, TestCase, TesterRole))
							{
								// fail=true;
	                        	APP_LOGS.debug("Test Step is not created successfully");
	                        	comments+="Test Step not Created Successfully(Fail). ";
	         					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
	         					// closeBrowser();
	         					 
	                			throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
							}
							APP_LOGS.debug("Test Step Created Successfully.");
				            
							Thread.sleep(3000);
							
							getElement("TM_attachmentTab_Id").click();
							
	                        //creating an attachment
	                        //createAttachment(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileName, TestStepExpectedResult, TesterRole, AttachmentSavedMessage);
							
							//commented by Sapan on 22nd Dec, 2014 as project and test case will already be selected as per UAT functionality
							
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
                        
							
						    APP_LOGS.debug("Clicking on Create New link");
							getObject("Attachments_createNew_link").click();
							Thread.sleep(500);
							    
						    /*APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
						    getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(AttachmentName);
						    
										
							APP_LOGS.debug("Inputing attachment description:" +Description);
							getObject("AttachmentsCreateNew_attachmentDescriptionTextField").sendKeys(Description);
							
										    
							APP_LOGS.debug("Uploading file:" +FileName);
							getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+FileName);*/
							   
							
							APP_LOGS.debug("Selecting test step:" +TestStep);
							getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
							
							if (!createAttachmentAndVerifyMessage(AttachmentName, Description, FileName, AttachmentSavedMessage)) 
							{
								fail=true;
								APP_LOGS.debug(AttachmentName+" is not Saved");
					    	    comments+="Fail- "+AttachmentName+" is not Saved";
					    	    throw new SkipException("Skipping as attachment creation unsuccessfull");
						    	//TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNotSavedError");
							}
							/*APP_LOGS.debug("Clicking on Save button");
						   
							getElement("AttachmentsCreateNew_saveButton_Id").click();
							Thread.sleep(3000);
						   
                          
						    //verify alert message
						    String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
							if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))   
							{
								APP_LOGS.debug(AttachmentName+" Save successfully");
								comments+="Pass- "+AttachmentName+" Save successfully";
								getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
								Thread.sleep(1000);
							}
						    
							else
							{
								fail=true;
								APP_LOGS.debug(AttachmentName+" is not Saved");
					    	    comments+="Fail- "+AttachmentName+" is not Saved";
						    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNotSavedError");
							}*/
						    
						    //clicking edit for saved attachment in view all table
							Thread.sleep(1000);
							getObject("AttachmentsViewAll_editImageXpath1", "AttachmentsViewAll_editImageXpath2", 1).click();
							
	                        //Extracting number of rows and columns from View All Project table		
	                		/*APP_LOGS.debug("Extracting Number of attachments present in Attachment grid");
	                		WebElement attachmentTable = getObject("AttachmentsViewAll_table");
	                		List<WebElement> attachmentRows = attachmentTable.findElements(By.tagName("tr"));
	                	    for (int i =  1; i <=attachmentRows.size(); i++) 
	                	    {	
	                	    	//Extracting the number of project present in Project column
	                	    	WebElement attachmentTableColsXpath = getObject("AttachmentsViewAll_tableXpath1", "AttachmentsViewAll_tableXpath2", i);
	                			
	                	    	String attachmentNameColTitle = attachmentTableColsXpath.getText();
	                			
	                			if(assertTrue(attachmentNameColTitle.equals(AttachmentName)))
	                			{
	                				APP_LOGS.debug("Clicking on Edit Icon...");
	                				getObject("AttachmentsViewAll_editImageXpath1", "AttachmentsViewAll_editImageXpath2", i).click();
	                				Thread.sleep(1000);
	                			}
	                			else
	                			{
	                				fail=true;
	                				APP_LOGS.debug(AttachmentName+" not found in table....so not able to click on it");
						    	    comments+="Fail- "+AttachmentName+" not found in table name....so not able to click on it";
							    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNameNotVisibleInTable");
	                			}
	                	    }*/
                        
                            //clicking on Open/View button
	                	
	                		if (getElement("AttachmentsCreateNew_attachmentDownloadBtn_Id").isDisplayed()) 
	                		{
	                			APP_LOGS.debug("Open/View Button Visible(Pass). ");
	                			comments+="Open/View Button Visible(Pass). ";
	                			
	                			String parentWindowHandle;
	                			
	                			parentWindowHandle = eventfiringdriver.getWindowHandle();
	                			 
	                			getElement("AttachmentsCreateNew_attachmentDownloadBtn_Id").click();
	                			Thread.sleep(2000);
	                			
	                			APP_LOGS.debug("Redirecting to the new window");
	                		    
	                		    APP_LOGS.debug("Verifying file name in new window");
	                			verifyUploadedFile(parentWindowHandle, FileName);   
							}
	                		else
	                		{
	                			fail = true;
	                			assertTrue(false);
	                			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNameNotVisibleInTable");
	                			APP_LOGS.debug("Open/View Button Not Visible(Fail). ");
	                			comments+="Open/View Button Not Visible(Fail). ";
	                		}
	                		    //eventfiringdriver.executeScript("$('#attDownload').click()");
        				}
					    else 
						{
							fail= true;								
							comments+="Login Unsuccessfull for test manager. ";
						}
					}	
					else
					{
						fail= true;
						APP_LOGS.debug("Login Unsuccessfull");
						comments+="Login Unsuccessfull for version lead. ";
					}
            	}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail = true;
					APP_LOGS.debug("Skip or other exception");
					comments+="Skip or other exception. ";
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
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
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
			Thread.sleep(2000);				
			
	      
		    //verify alert message
		    //String popUpMessageAfterSave = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
			String popUpMessageAfterSave=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
			if(compareStrings(expectedMessageOnSave, popUpMessageAfterSave))   
			{
				Thread.sleep(3000);
		//		getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
				return true;
				
			}	    
			else
			{	
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), expectedMessageOnSave);
		//		getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
				return false;    	   
			}					
	}
	
	//common code opening image in new browser window
	 private void verifyUploadedFile(String parentHandleName, String fileName)
	 {
		 try
		 {	
		     // call the method numberOfWindowsToBe as follows
		     //WebDriverWait wait = new WebDriverWait(eventfiringdriver, 15, 100);
		     wait.until(numberOfWindowsToBe(2));
		     Set<String> availableWindows = eventfiringdriver.getWindowHandles();//this set size is
		     
		     // returned as 1 and not 2
		      String newWindow = null;
		      for (String window : availableWindows) {
		           	  if (!parentHandleName.equals(window)) {
		              newWindow = window;
		          }
		      }

		      // switch to new window
		      eventfiringdriver.switchTo().window(newWindow);
		      eventfiringdriver.manage().window().maximize();
		      Thread.sleep(1000);
		      String getURL = eventfiringdriver.getCurrentUrl();
		      
		      if(getURL.contains(fileName))
		      {
		    	  APP_LOGS.debug(fileName+": is available in url : " + getURL);		    	  
		    	  comments+="Downloaded File is same as Uploaded(Pass). ";
		    	  APP_LOGS.debug("Downloaded File is same as Uploaded(Pass). ");		    	  
		    	  
		      }
		      else
		      {
		    	  fail = true;
		    	  comments+="Downloaded File is not same as Uploaded(Fail). ";
		    	  APP_LOGS.debug("Downloaded File is not same as Uploaded(Fail). ");
		    	  assertTrue(false);
		    	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Downloaded Uploaded File Not Same");
		      }
		      // and then close the new window
		      eventfiringdriver.close();
		      
		      // switch to parent
		      eventfiringdriver.switchTo().window(parentHandleName);
		 }
		 catch(Throwable t)
		 {
			 t.printStackTrace();
			 comments+="Exception in verifyUploadedFile(). ";
	    	  APP_LOGS.debug("Exception in verifyUploadedFile(). ");
	    	  assertTrue(false);
	    	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in verifyUploadedFile method");
		 }
	 }
	 
	//code for no of windows 
		 private ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
		     return new ExpectedCondition<Boolean>() {
		       @Override
		       public Boolean apply(WebDriver driver) {
		        eventfiringdriver.getWindowHandles();
		         return eventfiringdriver.getWindowHandles().size() == numberOfWindows;
		       }
		     };
		 }
}
