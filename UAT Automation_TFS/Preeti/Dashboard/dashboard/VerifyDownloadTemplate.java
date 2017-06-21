package com.uat.suite.dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class VerifyDownloadTemplate extends TestSuiteBase{
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	int myActivityTableRows;
	int totalPages;
	String projectCell;
	String versionCell;
	String testPassCell;
	String roleCell;
	int daysLeft;
	int notCompletedCount;
	int passCount;
	int failCount;
	String actionCell;
	String offlineTestingCell;
	String[] testStepArray;
	Boolean activeXResult = true;
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(dashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug(" Executing Test Case -> " + this.getClass().getSimpleName());
			
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(dashboardSuiteXls, this.getClass().getSimpleName());
	}
	
		
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void verifyDownloadTemplateFunctionality(String role, String groupName,String portfolioName,String projectName, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String versionLeadName, String testPassName, String testManagerName, 
			String testPassEndMonth, String testPassEndYear,String testPassEndDate, String testerName, String testerRole, String testCase, 
			String testStep, String testStepExpectedResult, String testStepResult, String importTestStepGuidelineMessage) throws Exception
	{
		count++;
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		 
		 //version lead
		 int versionlead_count = Integer.parseInt(versionLeadName);
		 versionLead=new ArrayList<Credentials>();
		 versionLead = getUsers("Version Lead", versionlead_count);
		 
		//TestManager 
		 int testManager_count = Integer.parseInt(testManagerName);
		 testManager=new ArrayList<Credentials>();
		 testManager = getUsers("Test Manager", testManager_count);
		 
		 //Tester 
		 int tester_count = Integer.parseInt(testerName);
		 tester=new ArrayList<Credentials>();
		 tester = getUsers("Tester", tester_count);
		 
		 APP_LOGS.debug("Opening Browser for role " + role);
		 openBrowser();

		isLoginSuccess = login(role);
		
		if(isLoginSuccess)
		{	
			
					//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
					
					//create project
					if(createProject(groupName, portfolioName, projectName, version, projectEndMonth, projectEndYear, projectEndDate, versionLead.get(0).username))
		 			{
						APP_LOGS.debug(" Project Created Successfully.");
		 			}
					else
					{
						fail=true;
		 				APP_LOGS.debug("Project is not created successfully");
		 				comments+= "Project is not Created Successfully(Fail). ";
		 				assertTrue(false);
		 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
						closeBrowser();
						
		 				throw new SkipException("Project is not created successfully ... So Skipping all tests in Dashboard Suite");
					}	
					
					
					//create test pass
	            	 if(createTestPass(groupName, portfolioName, projectName, version, testPassName, testPassEndMonth, testPassEndYear, testPassEndDate, testManager.get(0).username))
	            	 {
	            		 APP_LOGS.debug("Test Pass Created Successfully.");	    
	            		 
	            	 }
	            	 else
	            	 {
	            		 fail=true;
	                   	 APP_LOGS.debug("Test Pass is not created successfully");
	                   	 comments+="Test Pass not Created Successfully(Fail). ";
	                   	 assertTrue(false);
    					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
    					 closeBrowser();
    					 
    					 throw new SkipException("Test Pass is not created successfully ... So Skipping all tests in Dashboard Suite");
	            	 }
	            	 
	            	 
					//create Tester
					if(createTester(groupName, portfolioName, projectName, version, testPassName, tester.get(0).username, testerRole, testerRole))
					{
						 APP_LOGS.debug("Tester Created Successfully.");
					}
					else
					{
						 fail=true;
	                   	 APP_LOGS.debug("Tester is not created successfully");
	                   	 comments+="Tester not Created Successfully(Fail). ";
	                   	 assertTrue(false);
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
						 closeBrowser();
						 
						 throw new SkipException("Tester is not created successfully ... So Skipping all tests in Dashboard Suite");
					}
					
					
    				//create test case
    				if(createTestCase(groupName, portfolioName, projectName, version, testPassName, testCase))
					{
    					APP_LOGS.debug("Test Case Created Successfully."); 
					}
    				else
    				{
							
	    				fail=true;
                    	APP_LOGS.debug("Test Case is not created successfully");
                    	comments+="Test Case not Created Successfully(Fail). ";
                    	assertTrue(false);
     					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
     					closeBrowser();
     					 
            			throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Dashboard Suite");
    				}
    				
    				
				    //create 2 test steps				
					if(createTestSteps(groupName, portfolioName, projectName, version, testPassName, testCase, testerRole, testStep, testStepExpectedResult))
						
					{
						APP_LOGS.debug("Test Step Created Successfully."); 
					}
					else
					{
						fail=true;
                   	 	APP_LOGS.debug("Test Step is not created successfully");
                   	 	comments+="Test Step not Created Successfully(Fail). ";
    					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
    					closeBrowser();
    					 
           			 	throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Dashboard Suite");	
					}
					
    				closeBrowser();
			        		
    				
    				//again login with tester user
    				APP_LOGS.debug("Opening browser for role " + tester.get(0).username);    				
    				openBrowser();
    				
    				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
    				{
		    				 //verify import guideline message and offline templte downloading operation
	    					 if(verifyGuidelineMsgAndDownloadTemplateFunction(projectName, version, testPassName, testerRole, testStepResult, importTestStepGuidelineMessage))
	    					 {
	    						 	//verify if activex object is created
		    						if(activeXResult==false)
		    						{
		    							APP_LOGS.debug("Template is not downloaded as activeX is disabled.");
			    						comments+="Template is not downloaded as activeX is disabled(Pass). ";
			    						closeBrowser();
			    						
			    						throw new SkipException("Activex is disabled ... So Skipping template downloading operation");
		    						}
		    						else
		    						{
			    						APP_LOGS.debug("Offline Testing Template downloaded successfully.");
			    						comments+="Offline Testing Template downloaded successfully(Pass). ";
		    						}
		    							
	    					 }
	    					 else
	    					 {
		    						fail=true;
		    						APP_LOGS.debug("Offline Testing Template downloading operation unsuccessfull for test pass "+testPassName);
		                            comments+="Offline Testing Template downloading operation unsuccessfull for test pass "+testPassName+"(Fail). ";
		                            assertTrue(false);
		                            TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DownloadTemplateOperationUnsuccessfull");
	    					  }
	    					  closeBrowser();
    				}
    				else
    				{
    					APP_LOGS.debug("Login Unsuccessfull for Tester "+tester.get(0).username);
    					comments+="Login Unsuccessfull for Tester "+tester.get(0).username;
    				}
			}
			else 
			{
					APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ role+"'.");
					comments+="Login Unsuccessfull for the user with role '"+ role+"'.";
			}	
 
	}

	
	//create multiple test steps	
	private boolean createTestSteps(String group, String portfolio, String project, String version, String testPassName, 
			        String testCasesToBeSelected, String rolesToBeSelected, String testStep, 
			        String testStepExpectedResult) throws IOException, InterruptedException
	{
			//APP_LOGS.debug("Creating Test Step");
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
					for(int i = 0; i<numOfTestCases;i++)
					{
							if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
							{
								getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
								break;
							}
					}
					
					testStepArray = testStep.split(",");
					for(int k =0;k<testStepArray.length;k++)
					{
						
						String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testStepArray[k])+"')";
					    eventfiringdriver.executeScript(testStepDetails);
					    
					    String[] testStep_ExpectedResult = testStepExpectedResult.split(",");
					    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStep_ExpectedResult[k]); 
					    				
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
							
						}
						else 
						{
							return false;
						}
					   
					}
					return true;
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("Exception in createTestStep function.");
				e.printStackTrace();
				fail=true;
				return false;
			}
		
	}
		
	
	//verify guideline message and template download operation(with and without activex enable/disable)
	private boolean verifyGuidelineMsgAndDownloadTemplateFunction(String project, String version, String testPass, String testerRole, 
			String testStepResult, String importTestStepGuidelineMessage)
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean projectFound=false;
		try
		{
			
			if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on My Activity Grid.");
				totalPages=1;
				paginationCount=1;
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on My Activity Grid. Calculating total pages.");
				totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}
			
			nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
				myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
				
				for (int j = 1; j <= myActivityTableRows; j++) 
				{
					projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
					versionCell = getObject("DashboardMyActivity_versionColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
					testPassCell = getObject("DashboardMyActivity_testPassNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
					roleCell = getObject("DashboardMyActivity_roleNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");
					daysLeft = Integer.parseInt(getObject("DashboardMyActivity_daysLeftColumn1", "DashboardMyActivity_daysLeftColumn2", j).getText());
					notCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_notCompletedCountColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());
					passCount = Integer.parseInt(getObject("DashboardMyActivity_passCountColumn1", "DashboardMyActivity_passCountColumn2", j).getText());
					failCount = Integer.parseInt(getObject("DashboardMyActivity_failCountColumn1", "DashboardMyActivity_failCountColumn2", j).getText());
					actionCell = getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).getText();
					offlineTestingCell = getObject("DashboardMyActivity_offlineTestingDownloadTemplateIconColumn1", "DashboardMyActivity_offlineTestingDownloadTemplateIconColumn2", j).getAttribute("title");
							
					if((projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole))) 
					{
							
							//verifying Days Left & Not Completed count
		                    if((daysLeft>0) && (notCompletedCount>0) && (passCount==0) && (failCount==0) && 
		                    	(actionCell.equals("Begin Testing")) &&(offlineTestingCell.equals("Download Testing Template")))
							{
			                    
			                    	APP_LOGS.debug("Click on Dwonload/Upload Template Guideline icon.");
			                    	
			                    	getObject("DashboardMyActivity_offlineTestingGuidelineIcon").click();
			                    	Thread.sleep(2000);
			                    	
			                    	//compare import guideline msg and report failuar if not matched
			                    	if(compareStrings(importTestStepGuidelineMessage, getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()))
			                    	{
			                    		APP_LOGS.debug("Import Test Step Guideline Message is matched.");
			                    		comments+="Import Test Step Guideline Message is matched(Pass). ";
			                    	}
			                    	
			                    	else
			                    	{
			                    		fail=true;
			                    		APP_LOGS.debug("Import Test Step Guideline Message is not matched.");
			                    		comments+="Import Test Step Guideline Message is not matched(Fail). ";
			                    		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ImportGuidelineMessageNotMatched");
			                    	}
			                    	
			                    	//verifying if save button displayed or not
			                    	if(isElementExistsByXpath("DashboardMyActivity_offlineTestingImportTestStepGuidelineOkButton"))
			                    	{
				                    	getObject("DashboardMyActivity_offlineTestingImportTestStepGuidelineOkButton").click();
			                    		Thread.sleep(500);
			                    	}
			                    	else
			                    	{
			                    		fail=true;
			                    		APP_LOGS.debug("Save button not available..hence cannot close the dialog box.");
			                    		comments+="Save button not available..hence cannot close the dialog box. ";
			                    		assertTrue(false);
			                    		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SaveBtnNotAvailableInGuidelineBox");
			                    		return false;
			                    	}
			                    	
			                    	
			                    	//verifying if activex object is creating
			                		try
			                		{
			                			eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
			                            Thread.sleep(2000);
			                		}
			                		catch(Throwable t)
			                		{
			                			activeXResult = false;
			                		}
			                		
			                		APP_LOGS.debug("Click on Download Template icon");
			                		
			                		getObject("DashboardMyActivity_offlineTestingDownloadTemplateIconColumn1", "DashboardMyActivity_offlineTestingDownloadTemplateIconColumn2", j).click();		                		
			                		Thread.sleep(5000);
			                		
			                		if(activeXResult.equals(false))
			                		{
				                			//Active x code
				                			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
				                            if(assertTrue(isElementExistsById("TestStepsImportExport_activeXMessage_Id")))
				                            {
				                            	skip=true;
				                            	APP_LOGS.debug("Activex is disabled..and the activeX popup displayed. ");
					                            comments+="Activex is disabled..and the activeX popup displayed(Pass). ";
					                            getObject("TestStepCreateNew_TestStepActiveX_Close").click();
					                            return true;
				                            }
				                            else
				                            {
				                            	 fail=true;
					                             APP_LOGS.debug("Activex is disabled..but the popup not displayed. ");
					                             comments+="Activex is disabled..but the popup not displayed(Fail). ";
					                             TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabledPopupNotShown");
					                             return false;
				                            }
			                		}
			                		else
			                		{
			                				projectFound=true;
			                				return true;
			                		}
							}
					}
				}
					
				if (totalPages>1 && projectFound==false) 
				{
				    	if(isElementExistsByXpath("DashboardMyActivity_NextLink"))
				    	{
				    		nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
				    		getObject("DashboardMyActivity_NextLink").click();
							Thread.sleep(500);
				    	}
				    	else
				    	{
				    		APP_LOGS.debug("Next link disabled.");
				    	}
				}
			    else
			    {
			    		break;
			    }
			}
			APP_LOGS.debug(testPass+ " Not found in My Activity grid");
			comments+=testPass+ " Not found in My Activity grid (Fail). ";
			return false;
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
			APP_LOGS.debug("Exception occured in Download Template Operation");
			comments+="Exception occured in Download Template Operation. ";
			return false;
		}
		
	}
	
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		
	}
	
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
}
