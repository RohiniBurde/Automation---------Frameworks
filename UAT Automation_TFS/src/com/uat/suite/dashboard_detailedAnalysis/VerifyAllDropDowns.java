package com.uat.suite.dashboard_detailedAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class VerifyAllDropDowns extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String comments;
	
	Select selectProject;
	Select selectVersion;
	Select selectTestPass;
	Select testerDropDown;
	String totalNumOfProject;

	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	Utility utilRecorder = new Utility();
	ArrayList<String> optionInTestersDD = new ArrayList<String>();
	ArrayList<String> testersArray = new ArrayList<String>();
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());		

		if(!TestUtil.isTestCaseRunnable(DetailedAnalysisXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(DetailedAnalysisXls, this.getClass().getSimpleName());
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}

	@Test(dataProvider="getTestData")
	public void testVerifyAllDropDowns(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,String TestPassName2,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
			String tester_Role2,String TP1_TestCaseName1,String TP1_TestCaseName2,String TP2_TestCaseName1,String TP2_TestCaseName2,
			String TP1_TC1_TestStepName1,String TP1_TC2_TestStepName1,String TP2_TC1_TestStepName1,String TP2_TC2_TestStepName1,
			String TestStepExpectedResult,String AssignedRole1, String AssignedRole2 ) throws Exception
		{
		count++;
		
		comments = "";
		
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
		
		APP_LOGS.debug("Opening Browser... ");
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
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
	 				
					APP_LOGS.debug("Project has not created successfully. Test case has been failed.");	 				
	 				
					comments += "Fail Occurred: Project is not Created Successfully... ";	 				
	 				
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");	 				
					
	 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
					
				}
				else 
				{
					APP_LOGS.debug("Project Created Successfully.");					
					
					APP_LOGS.debug("Click on View all and get Total record Value.");					
					
					getObject("Projects_viewAllProjectLink").click();
					
					totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
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
		    		 	APP_LOGS.debug("Test Pass1 Created Successfully with test pass name 1.");
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
						APP_LOGS.debug("Test Pass Created Successfully with test pass name 2.");
				}
			
				Thread.sleep(700);
				//create Tester1 for TP1
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
					APP_LOGS.debug("Tester1 for TP1 Created Successfully.");
				}
			
				Thread.sleep(700);
				//create Tester2 for TP1
				if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(1).username, tester_Role1, tester_Role1))
				{
						
					 fail=true;
					 
					 APP_LOGS.debug(tester.get(1).username+ "Tester not ceated successfully for test pass "+TestPassName1);
					
					 comments+="Fail Occurred:- "+tester.get(1).username+ "Tester not ceated successfully for test pass "+TestPassName1+". ";
 					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
 					 
        			 throw new SkipException("Tester2 is not created successfully For Test Pass1 ... So Skipping all tests");
				}
				else 
				{
					 APP_LOGS.debug("Tester2 for TP1 Created Successfully.");
				}
			
				Thread.sleep(700);
				//create Tester3 for TP1
				if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(2).username, tester_Role1, tester_Role1))
				{
					 fail=true;
					 
					 APP_LOGS.debug(tester.get(2).username+ "Tester not ceated successfully for test pass "+TestPassName1);
					
					 comments+="Fail Occurred:- "+tester.get(2).username+ "Tester not ceated successfully for test pass "+TestPassName1+". ";
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
					 
					 throw new SkipException("Tester3 is not created successfully For Test Pass1 ... So Skipping all tests");
				}
				else 
				{	
					 APP_LOGS.debug("Tester Created Successfully.");
				}
				
				List<WebElement> listOfTesters=getObject("TesterViewAll_Table").findElements(By.tagName("tr"));
				for(int i=1;i<=listOfTesters.size();i++)
				{
					 String testerName=getObject("TesterViewAll_testerNameXpath1","TesterViewAll_testerNameXpath2",i).getText();
					 testersArray.add(testerName);
				}
			
				Thread.sleep(700);
				//create Tester4 for TP2
				if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, tester.get(3).username, tester_Role1, tester_Role1))
				{
					 fail=true;
					 
					 APP_LOGS.debug(tester.get(3).username+ "Tester not ceated successfully for test pass "+TestPassName2);
					
					 comments+="Fail Occurred:- "+tester.get(3).username+ "Tester not ceated successfully for test pass "+TestPassName2+". ";
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
					 
					 throw new SkipException("Tester4 is not created successfully For Test Pass2 ... So Skipping all tests");
				}
				else 
				{
					 APP_LOGS.debug("Tester4 for TP2 Created Successfully.");
				}
			
				Thread.sleep(700);
				//create Tester5 for TP2
				if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, tester.get(4).username, tester_Role1, tester_Role1))
				{
				     fail=true;
					 
					 APP_LOGS.debug(tester.get(4).username+ "Tester not ceated successfully for test pass "+TestPassName2);
					
					 comments+="Fail Occurred:- "+tester.get(4).username+ "Tester not ceated successfully for test pass "+TestPassName2+". ";
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
					 
       			     throw new SkipException("Tester2 is not created successfully For Test Pass1 ... So Skipping all tests");
				}
				else 
				{    
					 APP_LOGS.debug("Tester5 for TP2 Created Successfully.");
				}
				
				List<WebElement> listOfTesters1=getObject("TesterViewAll_Table").findElements(By.tagName("tr"));
				for(int i=1;i<=listOfTesters1.size();i++)
				{
					 String testerName=getObject("TesterViewAll_testerNameXpath1","TesterViewAll_testerNameXpath2",i).getText();
					 testersArray.add(testerName);
				}
				
				//create Test Case for TP1
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
					 APP_LOGS.debug("Test Case1 Created Successfully for test pass 1.");
				}
			
				
				//create Test Case for TP2
				if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP2_TestCaseName1))
				{
					 fail=true;
					
					 APP_LOGS.debug(TP2_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName2);
					
					 comments+="Fail Occurred:- "+TP2_TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName2+". ";
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
					 
					 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
				}
				else 
				{
						APP_LOGS.debug("Test Case1 Created Successfully for test pass 2.");
				}
			
				
				//create Test Step1 for TP1_TC1
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
					 APP_LOGS.debug("TP1_TC1_Test Step1 Created Successfully.");
				}
			    Thread.sleep(500);
			
				//create Test Step1 for TP2_TC1
				if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName2,TP2_TC1_TestStepName1,TestStepExpectedResult,
						TP2_TestCaseName1, AssignedRole1))
				{
						 
					 fail=true;
					
					 APP_LOGS.debug(TP2_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName1+ " for test pass "+TestPassName2);
					
					 comments+="Fail Occurred:- "+TP2_TC1_TestStepName1+ "Test Step not created successfully under Test Case "+TP2_TestCaseName1+ " for test pass "+TestPassName2+". ";
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
					 
					 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
				}
				else 
				{
					 APP_LOGS.debug("TP2_TC1_Test Step1 Created Successfully.");
				}
			
				
				getElement("UAT_dashboard_Id").click();
				
				APP_LOGS.debug("Click on Detailed analysis link.");
				
			//	getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();     
				
				getObject("Dashboard_detailedAnalysis").click();           //To be used for test2.0 env,Auto test env
				
				Thread.sleep(1000);
				
				selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
				
				selectVersion = new Select(getElement("DetailedAnalysis_versionDropDown_Id"));
			
				int totalNumOfProjectsInProjectDD = selectProject.getOptions().size();
				APP_LOGS.debug("TotalNumOfProjectsInProjectDD: "+ totalNumOfProjectsInProjectDD);
				
				int countOfVersion = 0;
				
				try 
				{
					
					for (int i = 0; i < totalNumOfProjectsInProjectDD; i++) 
					{
						//selecting each project and getting the number of versions for selected project
						selectProject.selectByIndex(i);
						
						Thread.sleep(426);
						
						int totalVersionDisplayedForProject = selectVersion.getOptions().size();
						
						countOfVersion += totalVersionDisplayedForProject;
					}
											
					if (compareIntegers(Integer.parseInt(totalNumOfProject), countOfVersion)) 
					{
						APP_LOGS.debug("Total projects Present in UAT are displayed in Project Drop Down.");
						
						comments += "Total projects Present in UAT are displayed in Project Drop Down....";
					}
					else 
					{
						 fail=true;	
	                	
						 APP_LOGS.debug("Total projects Present in UAT are not displayed in Project Drop Down. Test case has been failed.");		                	 
	                	
						 comments+="Fail Occurred:- Total projects Present in UAT are not displayed in Project Drop Down.... ";		                	 
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "projects Present in UAT are not displayed in Project Drop Down");
					}
				} 
				catch (Throwable t) 
				{
					fail=true;						
					
					APP_LOGS.debug("Exception Occurred: Total projects Present in UAT are not displayed in Project Drop Down. Test case has been failed.");						
					
					comments+="Exception Occurred:- Total projects Present in UAT are not displayed in Project Drop Down.... ";						
					
					assertTrue(false);
					
					t.printStackTrace();
				}
			
			
				selectProject.selectByVisibleText(ProjectName);					
				
				Thread.sleep(536);
				
				selectTestPass = new Select(getElement("DetailedAnalysis_testPassDropDown_Id"));
				
				int totalNumOfTestPassInSelectTestPassDD = selectTestPass.getOptions().size();	
				
				APP_LOGS.debug("totalNumOfTestPassInSelectTestPassDD "+totalNumOfTestPassInSelectTestPassDD);
				
				String OptionInTestPassDD = null;
				
				for (int i = 0; i < totalNumOfTestPassInSelectTestPassDD; i++) 
				{
					OptionInTestPassDD = selectTestPass.getOptions().get(i).getAttribute("title");
					
					if (i==0) 
					{
						if (compareStrings(TestPassName1, OptionInTestPassDD)) 
						{
							APP_LOGS.debug("Provided test pass name1 is displayed in Test pass dropdown.");									
							
							comments+="Provided test pass name1 is displayed in Test pass dropdown.... ";
						}
						else
						{
							 fail=true;								 
		                	 
							 APP_LOGS.debug("Fail Occurred:- Provided test pass name1 is Not displayed in Test pass dropdown. Test case has been failed.");			                	 
		                	
							 comments+="Fail Occurred:- Provided test pass name1 is Not displayed in Test pass dropdown.... ";			                	 
		 					 
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Provided test pass name1 is Not displayed in Test pass dropdown.");
						}
					}
					
					if (i==1) 
					{
						
						if (compareStrings(TestPassName2, OptionInTestPassDD)) 
						{
							APP_LOGS.debug("Pass- Provided test pass name2 is displayed in Test pass dropdown.");								
							
							comments+="Provided test pass name2 is displayed in Test pass dropdown.... ";
						}
						else
						{
							 fail=true;									 
		                	
							 APP_LOGS.debug("Provided test pass name2 is Not displayed in Test pass dropdown. Test case has been failed.");				                	 
		                	
							 comments+="Fail Occurred:- Provided test pass name2 is Not displayed in Test pass dropdown.... ";				                	 
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Provided test pass name2 is Not displayed in Test pass dropdown.");
						}
					}
				}
				
			    ///by preeti - this ccould cover in test pass verification itself, no need to add extra loop for TP 
				APP_LOGS.debug("Select test pass and get the testers associated.");
				
				for(int j =0 ;j<selectTestPass.getOptions().size();j++)
				{
					if(selectTestPass.getOptions().get(j).getAttribute("title").equals(TestPassName1))
					{
						selectTestPass.selectByIndex(j);								
						
						break;
					}
				}
			
				Thread.sleep(687);
				
				testerDropDown = new Select(getElement("DetailedAnalysis_testerDropDown_Id"));
				
				int totalNumOfTestersInSelectTestersDD = testerDropDown.getOptions().size();
				
				//String[] optionInTestersDD = null ;
				int iflag=0;
				
				for (int i = 1; i < totalNumOfTestersInSelectTestersDD; i++) 
				{
					optionInTestersDD.add(testerDropDown.getOptions().get(i).getAttribute("title"));
					
					int jflag=0;
					
					for (int j = 0; j < testersArray.size(); j++) 
					{
						if (optionInTestersDD.get(i-1).equalsIgnoreCase(testersArray.get(j)))
						{
							jflag++;
							
							break;
						}
					}
					
					if (jflag!=1) 
					{
						 jflag=0;

						 iflag = 1;

						 break;
					}
					
				}
				
				if (iflag != 1) 
				{
					 APP_LOGS.debug("Testers are displayed in Testers dropdown associated with selected Test Pass....");				                	 
               	 
					 comments+="Testers are displayed in Testers dropdown associated with selected Test Pass.... ";				                	 
					
				}
				else 
				{
					 fail=true;					
					 
					 assertTrue(false);
					 
					 APP_LOGS.debug("Testers are not displayed in Testers dropdown associated with selected Test Pass. Test case has been failed.");				                	 
               	 
					 comments+="Fail Occurred:- Testers are not displayed in Testers dropdown associated with selected Test Pass.... ";				                	 
					
					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expexted Testers not displayed in Testers Dropdown.");
				}  
			}
			catch (Throwable t) 
			{
				t.printStackTrace();
				
				fail=true;
				
				comments += "Exception Occurred :-Skip or Any other exception has Occurred.";
				
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
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
}
