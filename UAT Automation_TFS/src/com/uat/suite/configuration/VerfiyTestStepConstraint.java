/* Author Name:-Kunal Gujarkar
 * Created Date:- 3rd Feb 2015
 * Last Modified on Date:- 3rd Feb 2015
 */

package com.uat.suite.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class VerfiyTestStepConstraint extends TestSuiteBase
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
	public void testVerifyDescriptionTableArea(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
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
			
		    	 
                 APP_LOGS.debug("Closing the browser after creation of Test Passes.");
					
			 	 closeBrowser();
				
				 APP_LOGS.debug("Opening the browser for role "+testManager.get(0).username);
				
				 openBrowser();
		    	 
		    	 if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
		 	     {
		    		 
		    		getElement("UAT_testManagement_Id").click();			
					
					APP_LOGS.debug("Clicking On Test Management Tab ");		
					
					Thread.sleep(2000);
			
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
					
					
					if (getElement("Configuration_TSNameConstraintRadionButton_Id").isSelected()) 
					{
						
						getElement("UAT_testManagement_Id").click();			
						
						APP_LOGS.debug("Clicking On Test Management Tab ");		
						
						Thread.sleep(3000);
						
						if( createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TC1_TestStepName1, TestStepExpectedResult,
									TP1_TestCaseName1, AssignedRole1))
						{
						
							 int actualNumberOfTestStep = numberOfSameTestStep(TP1_TC1_TestStepName1);
							 
							 if (compareIntegers(2, actualNumberOfTestStep)) 
							 {
								 
								 APP_LOGS.debug("Test steps with same name is allowed to be created while 'Allow Same Test Step name within Test Case' Radio button is Selected. ");
									
								 comments += "\n- Test steps with same name is allowed to be created while 'Allow Same Test Step name within Test Case' Radio button is Selected. ";
								
							 }
							 else 
							 {
								 fail=true;
									
								 APP_LOGS.debug("Test steps with same name is NOT allowed to be created while 'Allow Same Test Step name within Test Case' Radio button is Selected.");
								
								 comments += "\n- Test steps with same name is NOT allowed to be created while 'Allow Same Test Step name within Test Case' Radio button is Selected. (Fail)";
								
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test steps with same name is NOT allowed to be created");
								 
							 }
						 
						}
						else 
						{
							fail=true;
							
							 APP_LOGS.debug(TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
							
							 comments += "\n- "+TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". (Fail)";
							
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
							 
		       			     throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
						}
					 
					    Thread.sleep(500);
						
					}
					else 
					{
						
						fail=true;
						
						APP_LOGS.debug("'Allow same Test Step name within Test Case' Radio button is not selected by Default.(Fail) ");
						
						comments += "\n- 'Allow same Test Step name within Test Case' Radio button is not selected by Default. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Allow same Test Step name within Test Case' Radio is not selected");
						
					}
					
					
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
					
					getElement("Configuration_DonotAllowSameTestStepNameWithinTestCase_Id").click();
					
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
					
					getElement("UAT_testManagement_Id").click();			
					
					APP_LOGS.debug("Clicking On Test Management Tab ");		
					
					Thread.sleep(3000);
					
					
					APP_LOGS.debug("Creating Test Step");
					Thread.sleep(2000);
					
					getElement("TestStepNavigation_Id").click();
					
					if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
					{
						getObject("TestStepCreateNew_TestStepActiveX_Close").click();
					}
					Thread.sleep(2000);
					
						
					dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), GroupName );
					
					dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), PortfolioName );
					
					dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), ProjectName );
					
					dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), Version );
					
					dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), TestPassName1 );
					
					getObject("TestStep_createNewProjectLink").click();
					
					Thread.sleep(1000);
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+TP1_TC1_TestStepName1+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				    
				    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(TestStepExpectedResult); 
					
					List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
					int numOfTestCases = TestCaseSelectionNames.size();
					for(int i = 0; i<numOfTestCases;i++)
					{
						if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(TP1_TestCaseName1))
						{
							getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
							
							break;
						}
					}
					
					String[] testerRoleSelectionArray = AssignedRole1.split(",");
					List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							
							if( ( roleSelectionNames.get(i) ).getAttribute("title").equals( testerRoleSelectionArray[j] ) )
							{
								
								getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
							
						}
						
					}
					
					getElement("TestStepCreateNew_testStepSaveBtn_Id").click();
					
					//String testStepCreatedResult=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
					
					String testStepCreatedResult= getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText();
					
					getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
					
					
					getObject("TestStep_viewAll").click();
					
					
					int actualNumberOfTestStep = numberOfSameTestStep(TP1_TC1_TestStepName1);
					
					 if ( compareIntegers(2, actualNumberOfTestStep) && 
						  compareStrings("Test Step with \""+TP1_TC1_TestStepName1+"\" name already exists!", testStepCreatedResult)) 
					 {
						
						APP_LOGS.debug("Test steps with same name is NOT allowed to be created while 'Don't allow same Test Step name within Test Case' Radio button is Selected. ");
						
						comments += "\n- Test steps with same name is NOT allowed to be created while 'Don't allow same Test Step name within Test Case' Radio button is Selected. ";
						
						comments += "\n- Validation message 'Test Step with name already exists! is displayed while 'Don't allow same Test Step name within Test Case' Radio button is Selected. ";
						
						
					 }
					 else 
					 {
						 fail=true;
							
						 APP_LOGS.debug("Validation message 'Test Step with name already exists! is NOT displayed while 'Don't allow same Test Step name within Test Case' Radio button is Selected. (Fail)");
						
						 comments += "\n- Validation message 'Test Step with name already exists! is NOT displayed while 'Don't allow same Test Step name within Test Case' Radio button is Selected. (Fail)";
						
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Message 'Test Step with name already exists! is NOT displayed");
						 
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
	
	
	//Function
	
	private int numberOfSameTestStep (String testStepName) throws IOException, InterruptedException
	{
		int testStepOnPage;
		
		String gridTestStep;
		
		APP_LOGS.debug("Searching Test Pass to Edit");
		
		int sameTestStepCount = 0;
		
		try
		{
			
			testStepOnPage = getElement("TestStepViewAll_Table_Id").findElements(By.xpath("tr")).size();
			
			for (int j = 1; j <= testStepOnPage; j++) 
			{
				gridTestStep=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", j).getText();
				
				if (gridTestStep.equals(testStepName)) 
				{
					sameTestStepCount++;
					
				}
				
			}			
				
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			APP_LOGS.debug("Exception in search Test Step AndEdit(). ");
		}
		return sameTestStepCount;
		
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
