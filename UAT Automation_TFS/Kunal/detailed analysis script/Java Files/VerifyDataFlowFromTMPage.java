package com.uat.suite.dashboard_detailedAnalysis;

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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyDataFlowFromTMPage extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String[] splitPassNCFailValues;
	String comments;
	String passPercentageOnDashboard;
	String notCompletedPercentageOnDashboard;
	String failPercentageOnDashboard;
	Select selectProject;
	Select selectVersion;
	Select selectTestPass;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	//ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(DetailedAnalysisXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(DetailedAnalysisXls, this.getClass().getSimpleName());
		
		/*tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		stakeholder=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();*/
		//testManager=getUsers("Test Manager", 1);
	}

	@Test(dataProvider="getTestData")
	public void testVerifyDataFlowFromTMPage(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
				String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,String TestPassName2,
				String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
				String tester_Role2,String TP1_TestCaseName1,String TP1_TestCaseName2,String TP2_TestCaseName1,String TP2_TestCaseName2,
				String TP1_TC1_TestStepName1,String TP1_TC2_TestStepName1,String TP2_TC1_TestStepName1,String TP2_TC2_TestStepName1,
				String TestStepExpectedResult,String AssignedRole1, String AssignedRole2 ) throws Exception
	{
		
		count++;		
	
		comments = "";
		
		// If we need to change the Test Pass Name (for selecting tp1 Or Tp2 from drop downs)
		
		String selectedTestPassName = TestPassName1;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int Tester_count = Integer.parseInt(tester_testerName);
		tester = getUsers("Tester", Tester_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());		
		
		
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
			
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
	 			{
	 				fail=true;
	 				
	 				APP_LOGS.debug(ProjectName+" not created successfully. ");
	 				
	 				comments= comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
	 				
	 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
	 			}
				else 
				{
					APP_LOGS.debug(ProjectName+" created successfully. ");
				}
					
				//create test pass1
            	 if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
            	 {
        		     fail=true;
        		    
        		     APP_LOGS.debug(TestPassName1+" not created successfully. ");
                   	 
        		     comments+="Fail Occurred:- "+TestPassName1+" Not created successfully. ";
					 
        		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
    					 
           			 throw new SkipException("Test Pass1 is not created successfully... So Skipping all tests");
            	 }
            	 else 
 				 {
            		 APP_LOGS.debug(TestPassName1+" created successfully. ");
	 			 }
            	
            	 //create test pass2
				if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
				{
           		     fail=true;
           		    
           		     APP_LOGS.debug(TestPassName2+" not created successfully. ");
                   	
           		     comments+="Fail Occurred:- "+TestPassName2+" not created successfully. ";
   					 
           		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
   					 
   					 throw new SkipException("Test Pass2 is not created successfully... So Skipping all tests");
           	 	}
				else 
				{
					APP_LOGS.debug(TestPassName2+" created successfully. ");
				}
				
				APP_LOGS.debug("Closing the browser after creation of Test Passes.");
				
				closeBrowser();
				
				APP_LOGS.debug("Opening the browser for role "+testManager.get(0).username);
				
				openBrowser();
				
				if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
				{
				
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(3000);
					
					//create TP1_Tester1 with R1
					if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(0).username, tester_Role1, tester_Role1))
					{
						 fail=true;
						 
						 APP_LOGS.debug(tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Tester is not created successfully For Test Pass1 ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(tester.get(0).username+ "Tester created successfully for test pass "+TestPassName1);
					}
					
					Thread.sleep(700);
					//create TP2_Tester1 with R2
					if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, tester.get(0).username, tester_Role2, tester_Role2))
					{
						 fail=true;
						
						 APP_LOGS.debug(tester.get(0).username+ "Tester not created successfully for test pass "+TestPassName2);
						
						 comments+="Fail Occurred:- "+tester.get(0).username+ "Tester not created successfully for test pass "+TestPassName2+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Tester is not created successfully For Test Pass2... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(tester.get(0).username+ "Tester created successfully for test pass "+TestPassName2);
					}
					
					//create TP1_Test case 1
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TestCaseName1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
					}
					else 
					{
						APP_LOGS.debug(TP1_TestCaseName1+ "Test Case created successfully for test pass "+TestPassName1);
					}
					
					//create TP1_Test case 2
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP1_TestCaseName2))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Test Case2 is not created successfully for test pass 1... So Skipping all tests");
					}
					else 
					{
						 APP_LOGS.debug(TP1_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName1);
					}
					
					//create TP2_Test case 1
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP2_TestCaseName1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP2_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName2);
						
						 comments+="Fail Occurred:- "+TP2_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName2+". ";
	 					 
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Test Case 1 is not created successfully for test pass 2... So Skipping all tests");
					}
					else 
					{
						 APP_LOGS.debug(TP2_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName2);
					}
					
					//create TP2_Test case 2
					if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP2_TestCaseName2))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP2_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName2);
						
						 comments+="Fail Occurred:- "+TP2_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName2+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
	 					 
	        			 throw new SkipException("Test Case 2 is not created successfully for test pass 2... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP2_TestCaseName2+ "Test Case not created successfully for test pass "+TestPassName2);
					}
					
					//create TP1_TC1_Test step1
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName1,TestStepExpectedResult,
							TP1_TestCaseName1, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP1_TC1_TestStepName1+ "Test Step created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ");
					}
					
					//create TP1_TC2_Test step1
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC2_TestStepName1,TestStepExpectedResult,
							TP1_TestCaseName2, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP1_TC2_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						 APP_LOGS.debug(TP1_TC2_TestStepName1+ "Test Step created successfully under Test Case "+TP1_TestCaseName2+ " for test pass "+TestPassName1);
					}
				
					//create TP2_TC1_Test step1
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC1_TestStepName1,TestStepExpectedResult,
							TP2_TestCaseName1, AssignedRole2))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP2_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName1+ " for test pass "+TestPassName2);
						
						 comments+="Fail Occurred:- "+TP2_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName1+ " for test pass "+TestPassName2+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP2_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP2_TC1_TestStepName1+ "Test Step created successfully under Test Case "+TP2_TestCaseName1+ " for test pass "+TestPassName2);
					}
					
					//create TP2_TC2_Test step1
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC2_TestStepName1,TestStepExpectedResult,
							TP2_TestCaseName2, AssignedRole2))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP2_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName2+ " for test pass "+TestPassName2);
						
						 comments+="Fail Occurred:- "+TP2_TC2_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName2+ " for test pass "+TestPassName2+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP2_TC2_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						 APP_LOGS.debug(TP2_TC2_TestStepName1+ "Test Step created successfully under Test Case "+TP2_TestCaseName2+ " for test pass "+TestPassName2);
					}
			
					APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
					
					//comments+="Data has been made Successfully from Test Management tab... ";
							
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser for role "+tester.get(0).username);
					
					openBrowser();
					
					if(login(tester.get(0).username,tester.get(0).password, "Tester"))
					{
					    
						//verify test pass in My Activity grid
						if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName1)) 
						{		
							fail = true;									
							
							APP_LOGS.debug(TestPassName1+" is not been displayed in My activity Area. Test case has been failed.");									
							
							comments += "Fail Occurred:- "+TestPassName1+" is not been displayed in My activity Area.... ";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName1 +"is not in My Activity Area");									
							
							throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");
							
						}
						else
						{
							APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");
							
							getElement("TestingPage_passRadioButton_Id").click();
							Thread.sleep(500);
							
							getElement("TestingPage_saveButton_Id").click();
							Thread.sleep(500);
							
							APP_LOGS.debug("Clicking on FAIL Radio Button in Testing Page");
							
							getElement("TestingPage_failRadioButton_Id").click();
							Thread.sleep(500);
							
							getElement("TestingPage_saveButton_Id").click();
							Thread.sleep(500);
							
							APP_LOGS.debug("Clicking on very Satisfied Radio Button");
							
							getObject("TestingPageRatingPopup_verySatisfiedRadioButton").click();
							Thread.sleep(500);
							
							APP_LOGS.debug("Clicking on Save button of Rating popup");
							
							getObject("TestingPageRatingPopup_saveButton").click();
							Thread.sleep(500);
							
							APP_LOGS.debug("Clicking on return To Home Button of Test Step Feedback popup.");
							
							getObject("TestingPage_returnToHomeButton").click();
							Thread.sleep(500);
						}	
								
						APP_LOGS.debug("Clicking on Begin Testing in My Activity for Test Pass 2");
								
						if (!searchTestPassAndClickOnBeginOrContinueTesting(ProjectName, Version, TestPassName2)) 
						{
							fail = true;									
							
							APP_LOGS.debug(TestPassName2+" is not been displayed in My activity Area. Test case has been failed.");									
							
							comments += "Fail Occurred:- "+TestPassName2+" is not been displayed in My activity Area.... ";		
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), TestPassName2 +"is not in My Activity Area");									
							
							throw new SkipException("Testing the Test Management data from dashboard page has not done successfully... So Skipping all tests");							
						}
						
						else
						{
							APP_LOGS.debug("Clicking on PASS Radio Button in Testing Page");
							
							getElement("TestingPage_passRadioButton_Id").click();
							
							getElement("TestingPage_saveButton_Id").click();
							
							Thread.sleep(500);
							
							APP_LOGS.debug("Clicking on Dashboard tab button as Testing is Complete(Keeping one test step as NC)");
							
							getElement("UAT_dashboard_Id").click();
							Thread.sleep(1000);
						}
					
						//verify testing status dropdown selection values
						
						if (!selectDDvaluesAndGetPieChartValues(ProjectName, Version, selectedTestPassName)) 
						{
							fail=true;
							
							APP_LOGS.debug("Pie Chart for test pass "+selectedTestPassName+" not shown");
							
							comments+="Fail Occurred:- Pie Chart for test pass "+selectedTestPassName+" not shown";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "PieChartIsIncorrect");
							
							throw new SkipException("Exception occurred: Dropdown selection is not correct.");
							
						}
						else 
						{
							APP_LOGS.debug("Pie chart shown correct values as per the provided details.");
							
							comments+="Pass- Pie chart shown correct values as per the provided details. ";
						}
	
	
						//verify if button displays
						if(assertTrue(isElementExistsById("DashboardTestingStatus_detailedAnalysisButton_Id")))
						{
							
							getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
							
							Thread.sleep(520);
							
							//On Detailed Analysis page
							selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));									
							
							String projectDropDownSelectedValue = selectProject.getFirstSelectedOption().getText();
							
							if (compareStrings(ProjectName, projectDropDownSelectedValue)) 
							{
								comments += "Default selected project on detailed Analysis page is same as project selected on Dashboard page...";
								
								APP_LOGS.debug("Default selected project is "+ProjectName);
							}
							else 
							{
								fail=true;
								
								comments += "Fail Occurred:- Default selected project on detailed Analysis page is NOT same as project selected on Dashboard page... ";
								
								APP_LOGS.debug("Default selected project on detailed Analysis page is NOT same as project selected on Dashboard page.");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Dropdown showing incorrect value");
							}
							 
							selectVersion = new Select(getElement("DetailedAnalysis_versionDropDown_Id"));									
							
							String versionDropDownSelectedValue = selectVersion.getFirstSelectedOption().getText();
							
							if (compareStrings(Version, versionDropDownSelectedValue)) 
							{
								comments += "Default selected Version on detailed Analysis page is same as version selected on Dashboard page...";
								
								APP_LOGS.debug("Default selected version is "+Version);
							}
							else 
							{
								fail=true;
								
								comments += "Fail Occurred:- Default selected Version on detailed Analysis page is Not same as version selected on Dashboard page... ";
								
								APP_LOGS.debug("Default selected Version on detailed Analysis page is Not same as version selected on Dashboard page.");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Version Dropdown showing incorrect value");
							}
							
							selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
							
							String testPassDropDownSelectedValue = selectTestPass.getFirstSelectedOption().getText();
							
							if (compareStrings(selectedTestPassName, testPassDropDownSelectedValue)) 
							{
								comments += "Default selected Test pass on detailed Analysis page is same as Test pass selected on Dashboard page...";
								
								APP_LOGS.debug("Default selected Test pass on detailed Analysis page is same as Test pass selected on Dashboard page...");
							}
							else 
							{
								fail=true;
								
								comments += "Fail Occurred:- Default selected Test Pass on detailed Analysis page is Not same as Test pass selected on Dashboard page... ";
								
								APP_LOGS.debug("Default selected Test pass on detailed Analysis page is Not same as Test pass selected on Dashboard page... ");										
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Dropdown showing incorrect value");
							}
						
							
							String passCount = (String) eventfiringdriver.executeScript("return $('#visualizationPass').find('table tbody tr td div div svg g g').text()");											
							
							if (assertTrue(passCount.equals(passPercentageOnDashboard))) 
							{
								APP_LOGS.debug("Pass percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in Pass Gauge.");
								
								comments += "Pass percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in Pass Gauge... ";							
							}
							else 
							{
								fail=true;
								
								APP_LOGS.debug("Pass percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Pass Gauge. Test case failed");
								
								comments += "Fail Occurred:- Pass percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Pass Gauge. Test case failed... ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pass Gauge displaying different count than on Testing Status");
							}
							
							
							String failCount = (String) eventfiringdriver.executeScript("return $('#visualizationFail').find('table tbody tr td div div svg g g').text()");
							
							if (assertTrue(failCount.equals(failPercentageOnDashboard)))
							{
								APP_LOGS.debug("Fail percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in Pass Gauge. Test case failed");
								
								comments += "Fail percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in Pass Gauge... ";	
							}
							else 
							{
								fail=true;
								
								APP_LOGS.debug("Fail percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Fail Gauge. Test case failed");
								
								comments += "Fail Occurred:- Fail percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Fail Gauge. Test case failed... ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail Gauge displaying different count than on Testing Status");
							}
							
							String NCCount = (String) eventfiringdriver.executeScript("return $('#visualizationNC').find('table tbody tr td div div svg g g').text()");									
							
							if (assertTrue(NCCount.equals(notCompletedPercentageOnDashboard)))
							{
								APP_LOGS.debug("NC percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in NC Gauge.");
								
								comments += "NC percentage displayed on Testing Status(displayed in Pie Chart) is Similar to displayed in NC Gauge... ";
							}
							else 
							{
								fail=true;
								
								APP_LOGS.debug("NC percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Pass Gauge. Test case failed");
								
								comments += "Fail Occurred:- NC percentage displayed on Testing Status(displayed in Pie Chart) is NOT Similar to displayed in Pass Gauge. Test case failed. ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NC Gauge displaying different count than on Testing Status");
							}
						
						
						}
						else
						{
								fail=true;
								APP_LOGS.debug("Detailed Analysis button not available");
								comments+="Fail Occurred:- Detailed Analysis button not available";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisButtonNotAvailable");
								
								throw new SkipException("Exception occurred: Detailed Analysis button not available..hence cannot redirect.");
						}
								
					}
					else 
					{
						fail=true;
						APP_LOGS.debug("Login Unsuccessful for Tester1 "+tester.get(0).username);					
						comments += "Fail :-Login Unsuccessful for Tester1 "+tester.get(0).username;
						assertTrue(false);	 
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
			comments+="Login Unsuccessful for Tester "+Role;
		}	
	}
		
	
	
	//functions
	private boolean selectDDvaluesAndGetPieChartValues(String ProjectName,String Version,String TestPassName) throws InterruptedException, IOException
	{
		//select project
		selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
		
		selectProject.selectByVisibleText(ProjectName);
		Thread.sleep(500);
		
		//select Version
		selectVersion = new Select(getElement("DetailedAnalysis_versionDropDown_Id"));
		
		selectVersion.selectByVisibleText(Version);
		Thread.sleep(500);
		
		//select Test pass
		selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
		
		selectTestPass.selectByVisibleText(TestPassName);
		Thread.sleep(500);
		
		//verify if pie chart displayed or not
		
		if(assertTrue(isElementExistsById("Dashboard_pieChart_ID")))
		{
				String getPieChartValues1= 	getObject("DashboardTestingStatus_pieChartImage").getAttribute("src");
				
				APP_LOGS.debug("getPieChartValues "+ getPieChartValues1);
			
				String getPieChartValues = getObject("DashboardTestingStatus_pieChartImage").getAttribute("src").substring(60, 67);
				
				APP_LOGS.debug("getPieChartValues "+ getPieChartValues);
				
				for (int i = 0; i < 3; i++) 
				{
					splitPassNCFailValues =getPieChartValues.split(",");
					while (i==0) 
					{
						passPercentageOnDashboard = splitPassNCFailValues[i];
						break;
					}
					while (i==1) 
					{
						notCompletedPercentageOnDashboard = splitPassNCFailValues[i];
						break;
					}
					while (i==2) 
					{
						failPercentageOnDashboard = splitPassNCFailValues[i];
						break;
					}
					
				}
				
				APP_LOGS.debug("passPercentageOnDashboard "+passPercentageOnDashboard);
				APP_LOGS.debug("notCompletedPercentageOnDashboard "+notCompletedPercentageOnDashboard);
				APP_LOGS.debug("failPercentageOnDashboard "+failPercentageOnDashboard);
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(DetailedAnalysisXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "FAIL");
	
	}
			
}
