/* Author Name:-Kunal Gujarkar
 * Created Date:- 5th Feb 2015
 * Last Modified on Date:- 6th Feb 2015
 */
package com.uat.suite.configuration;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class VerifyFeedbckAfterEachTPActivty extends TestSuiteBase
{

	String runmodes[] = null;
	static boolean fail = false;
	static boolean skip = false;
	static boolean isTestPass = true;
	static int count = -1;
	static WebDriverWait wait;
	static boolean isLoginSuccess = false;
	String comments;
	
	Select selectProject;	
	Select selectVersion ;
	Select selectTestPass;
		
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void testVerifyFeedbckAfterEachTPActivty() throws Exception
	{

		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
		
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	@Test(dataProvider = "getTestData")
	public void testVerifyDescriptionTableArea( String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
			String TP1_TestCaseName1,String TP1_TestCaseName2,String TP1_TC1_TestStepName1, String TP1_TC2_TestStepName1, String TP1_TC2_TestStepName2,String TestStepExpectedResult, String AssignedRole1) throws Exception
		{
			count++;
			
			comments  =  "";
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip = true;
				
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			int Tester_count = Integer.parseInt(tester_testerName);
			tester = getUsers("Tester", Tester_count);
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);
			
			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			openBrowser();
			
			APP_LOGS.debug("Opening Browser... ");
			
			isLoginSuccess  =  login(Role);
			
			if(isLoginSuccess)
			{
				try
				{
					
					getElement("UAT_testManagement_Id").click();			
					
					APP_LOGS.debug("Clicking On Test Management Tab ");		
					
					Thread.sleep(3000);
						
					if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{
						fail=true;	 				
		 				
						APP_LOGS.debug("Project has Not created successfully. Test case has been failed.");	 				
		 				
						comments += "\n- Project is Not created successfully. (Fail) ";	 				
		 				
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");	 				
						
		 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
						
					}
					else 
					{
						APP_LOGS.debug("Project Created Successfully.");					
						
					}

					
					//create test pass1
			    	 if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
			    	 {
			    		 fail=true;
		        		    
	        		     APP_LOGS.debug(TestPassName1+" Not created successfully.  (Fail)");
	                   	 
	        		     comments += "\n- "+TestPassName1+" Not created successfully.  (Fail)";
						 
	        		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	    					 
	           			 throw new SkipException("Test Pass1 is Not created successfully.  (Fail) So Skipping all tests");
			    	 }
			    	 else 
					 {
			    		 	APP_LOGS.debug("Test Pass1 Created Successfully with test pass name 1.");
					 }
			    	 
			    	 APP_LOGS.debug("Closing the browser after creation of Test Passes.");
						
				 	 closeBrowser();
					
					 APP_LOGS.debug("Opening the browser for role "+testManager.get(0).username);
					
					 openBrowser();
			    	 
			    	 if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
			 	     {

			    		 getElement("UAT_testManagement_Id").click();			
							
	 					 APP_LOGS.debug("Clicking On Test Management Tab ");		
							
						 Thread.sleep(3000);
			    		 
			    		//create Tester1 for TP1
			    		 
						if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(0).username, tester_Role1, tester_Role1))
						{
							 fail=true;
							 
							 APP_LOGS.debug(tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1);
							
							 comments += "\n- "+tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1+". (Fail)";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass1 ... So Skipping all tests");
						}
						else 
						{
							APP_LOGS.debug("Tester1 for TP1 Created Successfully.");
						}
					
						
						//create Test Case1 for TP1
						
						if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TestCaseName1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TP1_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
							 
							 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
						}
						else 
						{
							 APP_LOGS.debug("Test Case1 Created Successfully for test pass 1.");
						}
					
						
						//create Test Case2 for TP1
						
						if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TestCaseName2))
						{
							 fail=true;
							
							 APP_LOGS.debug(TP1_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
							 
							 throw new SkipException("Test Case2 is not created successfully for test pass 1... So Skipping all ");
						}
						else 
						{
							 APP_LOGS.debug("Test Case2 Created Successfully for test pass 1.");
						}
						
						
						//create Test Step1 for TP1_TC1
						
						if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TC1_TestStepName1, TestStepExpectedResult,
								TP1_TestCaseName1, AssignedRole1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
							 
		       			     throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
						else 
						{
							 APP_LOGS.debug("TP1_TC1_Test Step1 Created Successfully.");
						}
					    Thread.sleep(500);
					    
					    
					    //create Test Step1 for TP1_TC2
					    
					    if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TC2_TestStepName1, TestStepExpectedResult,
								TP1_TestCaseName2, AssignedRole1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TP1_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
							 
		       			     throw new SkipException("TP1_TC2_Test Step1 is not created successfully ... So Skipping all tests");
						}
						else 
						{
							 APP_LOGS.debug("TP1_TC2_Test Step1 Created Successfully.");
						}
					    Thread.sleep(500);
					    
					    
					    //create Test Step2 for TP1_TC2
					    
					    if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TC2_TestStepName2, TestStepExpectedResult,
								TP1_TestCaseName2, AssignedRole1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TP1_TC2_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TC2_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
							 
		       			     throw new SkipException("TP1_TC2_Test Step2 is not created successfully ... So Skipping all tests");
						}
						else 
						{
							 APP_LOGS.debug("TP1_TC2_Test Step2 Created Successfully.");
						}
					    Thread.sleep(500);
					    
					    
					    //Clicking on Configuration Tab
						
					    getElement("UAT_configuration_Id").click();
		    		 
					    Thread.sleep(3000);
						
						selectProject = new Select(getElement("Configuration_selectProjectDropDown_Id"));			
					
						for (int i = 0; i < selectProject.getOptions().size(); i++) 
						{
							if (selectProject.getOptions().get(i).getAttribute("title").equals(ProjectName)) 
							{
								selectProject.selectByIndex(i);
							}
						}
					
						selectVersion = new Select(getElement("Configuration_selectVersionDropDown_Id"));
						
						selectVersion.selectByVisibleText(Version);
						
						
						selectTestPass = new Select(getElement("Configuration_selectTestPassDropDown_Id"));
						
						for (int i = 0; i < selectTestPass.getOptions().size(); i++) 
						{
							if (selectTestPass.getOptions().get(i).getAttribute("title").equals(TestPassName1)) 
							{
								selectTestPass.selectByIndex(i);
							}
						}
					
						if ( getElement("Configuration_feedbackOptionRadionButton_Id").isSelected() ) 
						{
							
							closeBrowser();
							
							APP_LOGS.debug("Login with testers1 credentials");
							
							openBrowser();
							
							
							if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
							{
						
								APP_LOGS.debug("Clicking on Begin Testing in My Activity");
								
								if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
								{
									fail = true;									
									assertTrue(false);	
									APP_LOGS.debug("Test Pass 1 is not been displayed in My activity Area. Test case has been failed.");									
									
									comments += "\n- Test Pass 1 is not been displayed in My activity Area. (Fail) ";		
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 +"is not in My Activity Area");									
									
									throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
								}
						
								clickOnPASSRadioButtonAndSave();
								
								if ( ! waitForElementVisibilityPrivate("TestingPage_feedbackPopupDiv") ) 
								{
									
									APP_LOGS.debug("Feedback popup is not displayed when Test case has been switched while testing.");
									
									comments += "\n- Feedback popup is not displayed when Test case has been switched while testing.";
								
								}
								else 
								{
									assertTrue(false);	
									
									fail=true;
									
									APP_LOGS.debug("Feedback popup is displayed when Test case has been switched while testing.(Fail) ");
									
									comments += "\n- Feedback popup is displayed when Test case has been switched while testing. (Fail)";
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Feedback popup is displayed");
									
								}
						
								clickOnPASSRadioButtonAndSave();
								
								clickOnFAILRadioButtonAndSave();
						
								if ( waitForElementVisibilityPrivate("TestingPage_feedbackPopupDiv") ) 
								{
									
									APP_LOGS.debug("Feedback popup is displayed when Testing of a Test Pass has been completed.");
									
									comments += "\n- Feedback popup is displayed when Testing of a Test Pass has been completed.";
								
								}
								else 
								{
									assertTrue(false);	
									
									fail=true;
									
									APP_LOGS.debug("Feedback popup is NOT displayed when Testing of a Test Pass has been completed.(Fail) ");
									
									comments += "\n- Feedback popup is NOT displayed when Testing of a Test Pass has been completed. (Fail)";
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Feedback popup is NOT displayed");
									
								}
						
								getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
								Thread.sleep(500);
								
								APP_LOGS.debug("Clicking on Save button of Rating popup");
								
								getObject("TestingPageRatingPopup_saveButton").click();
								Thread.sleep(500);
								
								APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
								
								getObject("TestingPage_returnToHomeButton").click();
								Thread.sleep(500);
						
							}
							else 
							{
								fail=true;
								
								APP_LOGS.debug("Login Unsuccessful for Tester1 "+tester.get(0).username);					
								
								comments += "\n-Login Unsuccessful for Tester1 "+tester.get(0).username +" (Fail)";
								
								assertTrue(false);	 
							}
							
							
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("'Sequential Testing within a Test Case' Radio button is not selected by Default.(Fail) ");
							
							comments += "\n- 'Sequential Testing within a Test Case' Radio button is not selected by Default. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Sequential Testing within a Test Case' Radio is not selected");
							
						}
						
			 	    }
			    	 else
			    	 {
						fail = true;
						
						APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);								
						
						comments += "\n-Login Unsuccessful for Test Manager "+testManager.get(0).username+" (Fail)";
						
						assertTrue(false);	 
			    	 }	
					
					
					
					
				}
				catch (Throwable t) 
				{
					t.printStackTrace();
					
					fail=true;
					
					comments += "\n-Skip or Any other exception has Occurred. (Fail)";
					
					assertTrue(false);	
					
					APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");	
				}	
				
				closeBrowser();
			}
			else 
			{
				APP_LOGS.debug("Login Unsuccessful for user "+Role);				
				
				comments += "Login Unsuccessful for user "+Role;
			}	
		}
		
	
	//Functions
	
	 private boolean waitForElementVisibilityPrivate(String key) throws InterruptedException
	  {
		  By locator;
		  boolean isXpath = true;
		  WebDriverWait wait = new WebDriverWait(eventfiringdriver, 2);
		  try
		  {
			  
			  setImplicitWait(1);
			  
			  if (!OR.getProperty(key).contains("/"))
				  isXpath = false;				 
			  
			  if (isXpath) 
				  locator = By.xpath(OR.getProperty(key));
			  else
				  locator = By.id(OR.getProperty(key));
			 
			  wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			  
			  return true;
			  
		  }
		  catch(Throwable t)
		  {
			  return false;
		  }
		  finally
		  {
			  resetImplicitWait();
		  }			 
		  
	  }
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ConfigurationSuiteXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass = false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass = false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip = false;
		fail = false;
	}


	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
}
