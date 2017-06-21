/* Author Name:-Kunal Gujarkar
 * Created Date:- 2nd Feb 2015
 * Last Modified on Date:- 2nd Feb 2015
 */

package com.uat.suite.configuration;

import java.io.IOException;
import java.util.ArrayList;

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
		
public class VerfiyEditGeneralSettings extends TestSuiteBase
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
	public void checkTestSkip() throws Exception
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
	public void testVerfiyEditGeneralSettings(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
			String TP1_TestCaseName1,String TP1_TC1_TestStepName1,String TP1_TC1_TestStepName2,
			String TestStepExpectedResult, String AssignedRole1, String AssignedRole2) throws Exception
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
		 				
						comments += "\n- Project is Not created successfully.  (Fail) ";	 				
		 				
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
				
				
					Thread.sleep(700);
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
				
				
					//create Test Case for TP1
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
				    
				    if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TC1_TestStepName2, TestStepExpectedResult,
							TP1_TestCaseName1, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC1_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
						
						 comments += "\n- "+TP1_TC1_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". (Fail)";
						
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
						 
	       			     throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						 APP_LOGS.debug("TP1_TC1_Test Step1 Created Successfully.");
					}
				    Thread.sleep(500);
				
				    
					//Clicking on Configuration Tab
					
				    getElement("UAT_configuration_Id").click();
					
					Thread.sleep(2000);
					
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
					
					
					String defaultTestingTypeRadioSelected = testingTypeSelectedRadioButton();
					
					String defaultFeedbackRatingRadioSelected = feedbackRatingOptionSelectedRadioButton();
					
					if (defaultTestingTypeRadioSelected.equals("no Radio button is selected in Testing Type")) // Given text is from the called function 
					{
						
						fail=true;
						
						APP_LOGS.debug("No Radio button is selected in Testing Type.(Fail) ");
						
						comments += "\n- No Radio button is selected in Testing Type.(Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No Radio button is selected in Testing Type");
						
						throw new SkipException("No Radio button is selected in Testing Type... So Skipping all tests");
						
					}
					
					if (defaultFeedbackRatingRadioSelected.equals("no Radio button is selected in Feedback Rating Option")) // Given text is from the called function
					{
						
						fail=true;
						
						APP_LOGS.debug("no Radio button is selected in Feedback Rating Option.(Fail) ");
						
						comments += "\n- no Radio button is selected in Feedback Rating Option.(Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "no Radio button is selected in Feedback Rating Option");
						
						throw new SkipException("no Radio button is selected in Feedback Rating Option... So Skipping all tests");
						
					}
					
					
					//selecting radio buttons and click on save
					getElement("Configuration_sequentialTestingWithinTestPass_Id").click();
					
					getElement("Configuration_afterCompletionOfEachTestCase_Id").click();
					
					getObject("Configuratiion_GeneralSettingSaveBtn").click();
					
					String actualPopupText = getObject("Configuration_popupText").getText();
					
					if (compareStrings( resourceFileConversion.getProperty("Configuration_generalSettingsConfiguredSuccessfullyText"), actualPopupText)) 
					{
						
						APP_LOGS.debug("Before the testing begins, message 'General Settings have configured successfully!' is displayed. ");
						
						comments += "\n- Before the testing begins, message 'General Settings have configured successfully!' is displayed. ";
					
						getObject("Configuration_processDetailsPopupOkButton").click();
					}
					else 
					{
						
						fail=true;
						
						APP_LOGS.debug("Before the testing begins, message 'General Settings have configured successfully!' is NOT displayed. (Fail)");
						
						comments += "\n- Before the testing begins, message 'General Settings have configured successfully!' is NOT displayed. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "General Settings have configured successfully! is NOT displayed");
						
					}
					
					String testingTypeRadioButtonSelectedAfterTest = testingTypeSelectedRadioButton();
					
					String feedbackRatingOptionRadioButtonSelectedAfterTest = feedbackRatingOptionSelectedRadioButton();
	 	        	
					if ( (! defaultTestingTypeRadioSelected.equals(testingTypeRadioButtonSelectedAfterTest) ) &&
						 (! defaultFeedbackRatingRadioSelected.equals(feedbackRatingOptionRadioButtonSelectedAfterTest) )) 
					{
					
						if ( compareStrings("radio2", testingTypeRadioButtonSelectedAfterTest) &&
							 compareStrings("radio2", feedbackRatingOptionRadioButtonSelectedAfterTest)) 
						{
							APP_LOGS.debug("Selected Radio Button is displayed selected.");
						}
						else 
						{
							
							fail=true;
							
							APP_LOGS.debug("Selected Radio Button is NOT displayed selected before testing begins.(Fail) ");
							
							comments += "\n- Selected Radio Button is Not displayed selected before testing begins. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Selected Radio Button is NOT displayed selected before testing begins");
							
						}
						
						APP_LOGS.debug("Clicking on reset button and veryfying Radio selections.");
						
						getObject("Configuratiion_GeneralSettingResetBtn").click();
						
						getObject("Configuratiion_GeneralSettingSaveBtn").click();
						
						getObject("Configuration_processDetailsPopupOkButton").click();
						
						
						testingTypeRadioButtonSelectedAfterTest = testingTypeSelectedRadioButton();
						
						feedbackRatingOptionRadioButtonSelectedAfterTest = feedbackRatingOptionSelectedRadioButton();
		 	        	
						
						if ( compareStrings(testingTypeRadioButtonSelectedAfterTest, defaultTestingTypeRadioSelected) &&
			 	        	 compareStrings(feedbackRatingOptionRadioButtonSelectedAfterTest, defaultFeedbackRatingRadioSelected) ) 
			 	        	{
			 	        		
			 	        		APP_LOGS.debug("Radio buttons retain their selection after Click on 'Reset' Button  ");
								
								comments += "\n- Radio buttons retain their selection after Click on 'Reset' Button ";
							
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("Radio buttons didn't retain their selection after Click on 'Reset' Button.(Fail) ");
								
								comments += "\n- Radio buttons didn't retain their selection after Click on 'Reset' Button. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Radio buttons didn't retain their selection after Click on 'Reset' Button");
								
							}
			 	        	
						
					}
					
					
					closeBrowser();
					
					APP_LOGS.debug("Login with testers1 credentials");
					
					openBrowser();
					
					if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
					{
			
						APP_LOGS.debug("Clicking on Begin Testing in My Activity");
						
						if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
						{
							fail = true;									
							
							APP_LOGS.debug("Test Pass 1 is not been displayed in My activity Area. Test case has been failed.");									
							
							comments += "\n- Test Pass 1 is not been displayed in My activity Area. (Fail) ";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 +"is not in My Activity Area");									
							
							throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
						}
						else
						{
							//Keeping Testing incomplete and testing for the pop up message on Configuration.
							
							APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");								
							
							clickOnPASSRadioButtonAndSave();
							
						}
						
						
						APP_LOGS.debug("Closing browser.");
				 	       
			 	        closeBrowser();
			 	       
			 	        APP_LOGS.debug("Opening browser."); 
			 	        
			 	        openBrowser(); 
		
			 	        if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
			 	        {
			 	        	
			 	        	getElement("UAT_configuration_Id").click();
								
							Thread.sleep(2000);
							
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
							
							defaultTestingTypeRadioSelected = testingTypeSelectedRadioButton();
							
							defaultFeedbackRatingRadioSelected = feedbackRatingOptionSelectedRadioButton();
							
							if (defaultTestingTypeRadioSelected.equals("no Radio button is selected in Testing Type")) 
							{
								
								fail=true;
								assertTrue(false);
								
								APP_LOGS.debug("No Radio button is selected in Testing Type.(Fail) ");
								
								comments += "\n- No Radio button is selected in Testing Type.(Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No Radio button is selected in Testing Type");
								
								throw new SkipException("No Radio button is selected in Testing Type... So Skipping all tests");
								
							}
							
							if (defaultFeedbackRatingRadioSelected.equals("no Radio button is selected in Feedback Rating Option")) // Given text is from the called function
							{
								
								fail=true;
								assertTrue(false);
								
								APP_LOGS.debug("no Radio button is selected in Feedback Rating Option.(Fail) ");
								
								comments += "\n- no Radio button is selected in Feedback Rating Option.(Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "no Radio button is selected in Feedback Rating Option");
								
								throw new SkipException("no Radio button is selected in Feedback Rating Option... So Skipping all tests");
								
							}
							
							//selecting radio buttons and click on save
							getElement("Configuration_sequentialTestingWithinTestPass_Id").click();
							
							getElement("Configuration_afterCompletionOfEachTestCase_Id").click();
							
							getObject("Configuratiion_GeneralSettingSaveBtn").click();
							
							
							actualPopupText = getObject("Configuration_popupText").getText();
							
							if (compareStrings( resourceFileConversion.getProperty("Configuration_testingIsAlreadyBegunText"), actualPopupText)) 
							{
								
								APP_LOGS.debug("Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is displayed. ");
								
								comments += "\n- After the testing has begun, message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is displayed. ";
							
								getObject("Configuration_processDetailsPopupOkButton").click();
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("After the testing has begun, message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is NOT displayed.(Fail) ");
								
								comments += "\n- After the testing has begun, message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is NOT displayed. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Testing is already begun for selected Test Pass...' is NOT displayed");
								
							}
							
							testingTypeRadioButtonSelectedAfterTest = testingTypeSelectedRadioButton();
							
							feedbackRatingOptionRadioButtonSelectedAfterTest = feedbackRatingOptionSelectedRadioButton();
			 	        	
			 	        	if ( compareStrings(testingTypeRadioButtonSelectedAfterTest, defaultTestingTypeRadioSelected) &&
			 	        	     compareStrings(feedbackRatingOptionRadioButtonSelectedAfterTest, defaultFeedbackRatingRadioSelected) ) 
			 	        	{
			 	        		
			 	        		APP_LOGS.debug("Radio buttons retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup  ");
								
								comments += "\n- Radio buttons retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup. ";
							
							}
							else 
							{
								
								fail=true;
								
								APP_LOGS.debug("Radio buttons didn't retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup.(Fail) ");
								
								comments += "\n- Radio buttons didn't retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup. (Fail)";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Radio buttons didn't retain their selection");
								
							}
			 	        	
			 	        	
			 	        	
			 	        	closeBrowser();
							
							APP_LOGS.debug("Login with testers1 credentials");
							
							openBrowser();
							
							if (login(tester.get(0).username,tester.get(0).password, "Tester")) 
							{
					
								APP_LOGS.debug("Clicking on Begin Testing in My Activity");
								
								if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
								{
									fail = true;									
									
									APP_LOGS.debug("Test Pass 1 is not been displayed in My activity Area. Test case has been failed.");									
									
									comments += "\n- Test Pass 1 is not been displayed in My activity Area. (Fail) ";		
									
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 +"is not in My Activity Area");									
									
									throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
								}
								else
								{
									//Keeping Testing incomplete and testing for the pop up message on Configuration.
									
									APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");								
									
									clickOnPASSRadioButtonAndSave();
									
									Thread.sleep(1000);
									
									APP_LOGS.debug("Clicking on very Satisfied Radio Button");								
									getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
									
									Thread.sleep(500);
									
									
									APP_LOGS.debug("Clicking on Save button of Rating popup");								
									getObject("TestingPageRatingPopup_saveButton").click();
									
									Thread.sleep(500);
									
									
									APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");								
									getObject("TestingPage_returnToHomeButton").click();
								}
								
							
							   APP_LOGS.debug("Closing browser.");
						 	      
					 	       closeBrowser();
					 	      
					 	       APP_LOGS.debug("Opening browser."); 
					 	        
					 	       openBrowser(); 
				
					 	        
					 	       if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					 	        {
					 	        	
					 	        	getElement("UAT_configuration_Id").click();
										
									Thread.sleep(2000);
									
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
									
									defaultTestingTypeRadioSelected = testingTypeSelectedRadioButton();
									
									defaultFeedbackRatingRadioSelected = feedbackRatingOptionSelectedRadioButton();
									
									if (defaultTestingTypeRadioSelected.equals("no Radio button is selected in Testing Type")) 
									{
										
										fail=true;
										
										assertTrue(false);
										
										APP_LOGS.debug("No Radio button is selected in Testing Type.(Fail) ");
										
										comments += "\n- No Radio button is selected in Testing Type.(Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No Radio button is selected in Testing Type");
										
										throw new SkipException("No Radio button is selected in Testing Type... So Skipping all tests");
										
									}
									
									if (defaultFeedbackRatingRadioSelected.equals("no Radio button is selected in Feedback Rating Option")) // Given text is from the called function
									{
										
										fail=true;
										
										assertTrue(false);
										
										APP_LOGS.debug("no Radio button is selected in Feedback Rating Option.(Fail) ");
										
										comments += "\n- no Radio button is selected in Feedback Rating Option.(Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "no Radio button is selected in Feedback Rating Option");
										
										throw new SkipException("no Radio button is selected in Feedback Rating Option... So Skipping all tests");
										
									}
									
									//selecting radio buttons and click on save
									getElement("Configuration_sequentialTestingWithinTestPass_Id").click();
									
									getElement("Configuration_afterCompletionOfEachTestCase_Id").click();
									
									getObject("Configuratiion_GeneralSettingSaveBtn").click();
									
									
									actualPopupText = getObject("Configuration_popupText").getText();
									
									if (compareStrings( resourceFileConversion.getProperty("Configuration_testingIsAlreadyBegunText"), actualPopupText)) 
									{
										
										APP_LOGS.debug("Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is displayed. ");
										
										comments += "\n- After the testing has been completed (Testing completed is in link form), message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is displayed. ";
									
										getObject("Configuration_processDetailsPopupOkButton").click();
									}
									else 
									{
										
										fail=true;
										
										APP_LOGS.debug("After the testing has been completed (Testing completed is in link form), message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is NOT displayed.(Fail) ");
										
										comments += "\n- After the testing has been completed (Testing completed is in link form), message 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' is NOT displayed. (Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Testing is already begun for selected Test Pass...' is NOT displayed After testing completed");
										
									}
									
									testingTypeRadioButtonSelectedAfterTest = testingTypeSelectedRadioButton();
									
									feedbackRatingOptionRadioButtonSelectedAfterTest = feedbackRatingOptionSelectedRadioButton();
					 	        	
					 	        	if ( compareStrings(testingTypeRadioButtonSelectedAfterTest, defaultTestingTypeRadioSelected) &&
					 	        	     compareStrings(feedbackRatingOptionRadioButtonSelectedAfterTest, defaultFeedbackRatingRadioSelected) ) 
					 	        	{
					 	        		
					 	        		APP_LOGS.debug("Radio buttons retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup, After testing completed");
										
										comments += "\n- Radio buttons retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup, After testing completed ";
									
									}
									else 
									{
										
										fail=true;
										
										APP_LOGS.debug("Radio buttons didn't retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup, After testing completed(Fail) ");
										
										comments += "\n- Radio buttons didn't retain their selection after 'Testing is already begun for selected Test Pass so you won't be able to change 'Testing Type' and 'Feedback Rating Option'!' popup, After testing completed (Fail)";
										
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Radio buttons didn't retain their selection");
										
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
							fail = true;
							
							APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);								
							
							comments += "\n-Login Unsuccessful for Test Manager "+testManager.get(0).username+" (Fail)";
							
							assertTrue(false);	 
						}
						
					}
					else 
					{
						fail=true;
						
						APP_LOGS.debug("Login Unsuccessful for Tester1 "+tester.get(0).username);					
						
						comments += "\n-Login Unsuccessful for Tester1 "+tester.get(0).username +" (Fail)";
						
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
		
	
	//Function
	
	private String testingTypeSelectedRadioButton() throws IOException
	{
		if (getElement("Configuration_testingTypeOptionRadionButton_Id").isSelected()) 
		{
			return "radio1";
		}
		if (getElement("Configuration_sequentialTestingWithinTestPass_Id").isSelected()) 
		{
			return "radio2";
		}
		if (getElement("Configuration_testingOutOfSequence_Id").isSelected()) 
		{
			return "radio3";
		}
		return "no Radio button is selected in Testing Type";
	}
	
	private String feedbackRatingOptionSelectedRadioButton() throws IOException
	{
		if (getElement("Configuration_feedbackOptionRadionButton_Id").isSelected()) 
		{
			return "radio1";
		}
		if (getElement("Configuration_afterCompletionOfEachTestCase_Id").isSelected()) 
		{
			return "radio2";
		}
		if (getElement("Configuration_noRatingPopup_Id").isSelected()) 
		{
			return "radio3";
		}
		return "no Radio button is selected in Feedback Rating Option";
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
