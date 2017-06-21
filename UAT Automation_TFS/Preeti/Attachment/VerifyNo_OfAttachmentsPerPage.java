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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyNo_OfAttachmentsPerPage extends TestSuiteBase
{

	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPassed=true;
	static boolean isLoginSuccess=false;
	String comments=null;
	String runmodes[]=null;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	static int i;
	int testStepCount = 11;
	static int attachmentLimitPerPage=10;
	boolean testStepResult = false;
	int availableAttachments;
    int AttachmentsPerPage;
    int requiredAttachments = 11;
    
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName()))
		{

			 APP_LOGS.debug(" Executing Test Case -> VerifyNoOfAttachmentPerPage ");
				
			 System.out.println(" Executing Test Case -> VerifyNoOfAttachmentPerPage ");				
				
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyNoOfAttachmentsPerPage(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String TestPassName, String TP_TestManager, 
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String TestCase, String TestStep, String TestStepExpectedResult, String AttachmentName, 
			String Description, String FileName, String AttachmentSavedMessage) throws Exception
		 {
		    
		   
		    // test the runmode of current dataset
			count++;
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
				
			System.out.println("Opening Browser... ");
				
			openBrowser();
			
			System.out.println("************** Old Version Lead Login******************");
			isLoginSuccess = login(role);
			
			if (isLoginSuccess) 
			{				
							
							//click on testManagement tab
							APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(3000);
							
							if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
	             			{
	             				fail=true;
	             				APP_LOGS.debug("Project is not created successfully");
	             				comments= "Fail- Project not Created Successfully. ";
	             				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
	            				closeBrowser();
	            				
	             				throw new SkipException("Project is not created successfully ... So Skipping all tests in Attachment Suite");
	             			}
              
							APP_LOGS.debug(" Project Created Successfully.");
							comments= "Pass- Project Created Successfully. ";
							
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
		                     		     fail=true;
			                        	 APP_LOGS.debug("Test Pass is not created successfully");
			                        	 comments+="Test Pass not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Pass is not created successfully ... So Skipping all tests in Attachment Suite");
		                     	 }
		                        
		                     	APP_LOGS.debug("Test Pass Created Successfully.");
		        				comments+="Pass- Test Pass Created Successfully. ";
           
		        				closeBrowser();
	    					
		        				APP_LOGS.debug("Opening Browser... ");
			 				
		        				System.out.println("Opening Browser... ");
			 				
			 				
		        				openBrowser();
		        				if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
		        				{
					 				//click on testManagement tab
					 				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					 				getElement("UAT_testManagement_Id").click();
					 				Thread.sleep(2000);
					 				
									if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, TesterRole, TesterRole))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Tester is not created successfully");
			                        	 comments+="Tester not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Tester is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Tester Created Successfully.");
			        				comments+="Pass- Tester Created Successfully. ";
			        				
									if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Test Case is not created successfully");
			                        	 comments+="Test Case not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Test Case Created Successfully.");
			        				comments+="Pass- Test Case Created Successfully. ";
			        				
									if(!createTestSteps(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestStep, TestStepExpectedResult, TestCase, TesterRole))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Test Step is not created successfully");
			                        	 comments+="Test Step not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Test Step Created Successfully.");
			        				comments+="Pass- Test Step Created Successfully. ";
					 				
					 				
									try
									{
										getElement("TM_attachmentTab_Id").click();
										Thread.sleep(2000);
							            
										dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
									       
								        dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), PortfolioName );
									       
									    dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
									      
									    dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
									      
									    dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), TestPassName );
									       
										if (!getElement("Attachments_noAttachmentAvailableDiv_Id").isDisplayed()) 
										{
											fail=true;
											APP_LOGS.debug("Fail - No Attachment Available. text is not found");
											comments=comments+"Fail- No Attachment Available. text is not found";
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoAttachmentAvailableTextNotFound");
											
										}
										
										APP_LOGS.debug("PASS- No Attachment Available.");
										comments=comments+"PASS- No Attachment Available. text is found";
										Thread.sleep(1000);
										
										//create required attchments
										createAttachment(requiredAttachments, GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileName, 
												TestStepExpectedResult, TesterRole, AttachmentSavedMessage);
										
										/*if (getElement("AttachmentViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
										{
											APP_LOGS.debug("Only 1 page available on View All page.");
											
											totalPages=1;
											
										}
										else 
										{
											APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
											
											totalPages = getElement("AttachmentViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
										}
										
										
										if (totalPages>1)
										{*/
											/*Thread.sleep(1000);
											//previous is disable
											previousLinkDisabled = getObject("AttachmentsViewAll_PreviousLinkDisabled").isEnabled();
											
											if(!assertTrue(previousLinkDisabled))
											{
												APP_LOGS.debug("FAIL- Previous link is enable");
												comments=comments+"FAIL - Previous link is enable";
												TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PreviousLinkEnable");
											}
											else
											{
												APP_LOGS.debug(" (PASS)- Previous link is disable");
												comments=comments+" (PASS)- Previous link is disable";
											}*/
											APP_LOGS.debug("Calculating total attachments available on page 1");
											AttachmentsPerPage = getObject("AttachmentsViewAll_table").findElements(By.tagName("tr")).size();
											
												if (AttachmentsPerPage!=attachmentLimitPerPage) 
												{
													fail= true;
													APP_LOGS.debug("Attachment per page limit is not correct. Failing test case.");
													comments=comments+"FAIL - Attachment per page limit is not correct. Failing test case.";		
												}
												
												else if (AttachmentsPerPage == attachmentLimitPerPage) 
												{
													
													APP_LOGS.debug("Attachment per page limit is correct. Passing test case.");
													comments=comments+"PASS - Attachment per page limit is correct. Passing test case.";
													
													try 
													{
														if(assertTrue(getObject("AttachmentsViewAll_PreviousLinkDisabled").isDisplayed()))
														{
															APP_LOGS.debug("Previous Link is disabled.");
															comments=comments+"PASS - Previous Link is disabled.";
														}
													} 
													catch (Throwable t) 
													{
														fail=true;
														APP_LOGS.debug("Previous Link is enabled.");
														comments=comments+"FAIL - Previous Link is enabled.";
													}
													
													try 
													{
														if(assertTrue(getObject("AttachmentsViewAll_NextLinkEnabled").isEnabled()))
														{
															APP_LOGS.debug("Next Link is enabled.");
															comments=comments+"PASS - Next Link is enabled.";
															
															getObject("AttachmentsViewAll_NextLinkEnabled").click();													
															Thread.sleep(2000);
														
															APP_LOGS.debug("Available attachments in next page" +availableAttachments);
															AttachmentsPerPage = getObject("AttachmentsViewAll_table").findElements(By.tagName("tr")).size();
														}
													} 
													catch (Throwable t) 
													{
														fail=true;
														APP_LOGS.debug("Next Link is disabled.");
														comments=comments+"FAIL - Next Link is disabled.";
													}
													
													
													try 
													{
														if(assertTrue(getObject("AttachmentsViewAll_NextLinkDisabled").isDisplayed()))
														{
															APP_LOGS.debug("Next Link is disabled.");
															comments=comments+"PASS - Next Link is disabled.";
														}
													} 
													catch (Throwable t) 
													{
														fail=true;
														APP_LOGS.debug("Next Link is enabled.");
														comments=comments+"FAIL - Previous Link is enabled.";
													}
													
													
													try 
													{
														if(assertTrue(getObject("AttachmentsViewAll_PreviousLinkEnabled").isEnabled()))
														{
															APP_LOGS.debug("Previous Link is enabled.");
															comments=comments+"PASS - Previous Link is enabled.";
															
															getObject("AttachmentsViewAll_PreviousLinkEnabled").click();													
															Thread.sleep(2000);
														
														}
													} 
													catch (Throwable t) 
													{
														fail=true;
														APP_LOGS.debug("Previous Link is disabled.");
														comments=comments+"FAIL - Previous Link is disabled.";
													}
													
													closeBrowser();
												
												}
										
								        }
										catch(Throwable t)
										{
											t.printStackTrace();
											fail = true;
											APP_LOGS.debug("Exception in Attachments Tab");
											comments+="Exception in Attachments Tab. ";
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentsTabException");
											assertTrue(false);
											closeBrowser();
										}
											
										}
									else 
									{
										fail = true;
										APP_LOGS.debug("Login Unsuccessfull");
										comments+="Login Unsuccessfull";
									}
									} 
		        				else 
		        				{
		        					fail = true;
		        					APP_LOGS.debug("Login Unsuccessfull");
		        					comments+="Login Unsuccessfull";
		        				}
							}		
									
			else 
			{
				fail = true;
				APP_LOGS.debug("Login Unsuccessfull");
				comments+="Login Unsuccessfull";
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
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPassed=false;
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
	public void reportTestResult() throws InterruptedException
	{
		if(isTestPassed){
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		    TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
		    else{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		    TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
     }
	
		
	//create new attachment
	private void createAttachment(int numOfAttachments, String group, String portfolio, String project, String version, String testPassName, String TestCase, String AttachmentName, 
			String Description, String TestStep, String fileName1, String TestStepExpectedResult, String TesterRole, String AttachmentSavedMessage) throws IOException, InterruptedException
	{
			
			       dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			       
			       dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			       
			       dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			      
			       dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			      
			       dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
			
				   //selecting test case
			       APP_LOGS.debug("Selecting test case:" +TestCase);
			       getElement("Attachments_testCaseDropDown_Id").click();
			       List<WebElement> testCaseElements = getElement("Attachments_testCaseDropDown_Id").findElements(By.tagName("option"));
				    
				   for(int i =0 ;i<testCaseElements.size();i++)
				   {
					       if(testCaseElements.get(i).getText().equals(TestCase))
						    {
						       testCaseElements.get(i).click();
						       APP_LOGS.debug( TestCase + " : is selected...");
						       System.out.println(TestCase + " : is selected...");
						       break;
				            }			    
				   }
				   
			        for (int j = 1; j<= numOfAttachments; j++) 
			        {
						APP_LOGS.debug("Clicking on Create New link");
						getObject("Attachments_createNew_link").click();
						    
					    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
					    getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(AttachmentName);
					    
									
						APP_LOGS.debug("Inputing attachment description:" +Description);
						getElement("AttachmentsCreateNew_attachmentDescriptionTextField_Id").sendKeys(Description);
						
									    
						APP_LOGS.debug("Uploading file:" +fileName1);
						getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
						   
						
						APP_LOGS.debug("Selecting test step");
						getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", j).click();
						
						
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
						}
					    
					    
			       }

				
	}
					
	
	private boolean createTestSteps(String group, String portfolio, String project, String version, String testPassName, 
			String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Step");
		Thread.sleep(2000);
		
		getElement("TestStepNavigation_Id").click();
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
		}
		Thread.sleep(2000);	
		
		try {
			
				dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
				
				getObject("TestStep_createNewProjectLink").click();
				Thread.sleep(1000);
			
				List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
				int numOfTestCases = TestCaseSelectionNames.size();
				for(i = 0; i<numOfTestCases;i++)
				{
						if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
						{
							getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
							break;
						}
				}
				
				for(int k=1; k<=testStepCount; k++)
				{
					
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testStepName+k)+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				    
				    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults); 
								
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
					
					Thread.sleep(2000);
				
					if (getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
						
						testStepResult=true;
					}
					else 
					{
						testStepResult=false;
					}
				}
				
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			fail=true;
			
		}
		return testStepResult;
		
		
	}
	
	
	
	
}


