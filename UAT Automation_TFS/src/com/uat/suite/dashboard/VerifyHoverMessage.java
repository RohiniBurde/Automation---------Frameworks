package com.uat.suite.dashboard;

import java.util.ArrayList;

import org.openqa.selenium.interactions.Actions;
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
public class VerifyHoverMessage extends TestSuiteBase
{
	
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
	Actions builder;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(dashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(dashboardSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
		
	}
	
	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyHoverMessage(String role, String groupName,String portfolioName,String project, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String versionLeadName, String testPass, 
			String testManagerName, String testPassEndMonth, String testPassEndYear, String testPassEndDate, String testerName, 
			String testerRole, String testCase, String testStep, String testStepExpectedResult) throws Exception
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
		 versionLead= new ArrayList<Credentials>();
		 versionLead = getUsers("Version Lead", versionlead_count);
		 
		//TestManager 
		 int testManager_count = Integer.parseInt(testManagerName);
		 testManager=new ArrayList<Credentials>();
		 testManager = getUsers("Test Manager", testManager_count);
		 
		 //Tester 
		 int tester_count = Integer.parseInt(testerName);
		 tester=new ArrayList<Credentials>();
		 tester = getUsers("Tester", tester_count);
		 
		 APP_LOGS.debug("Opening Browser... for user "+role);
		 openBrowser();
		
		isLoginSuccess = login(role);
		
		if(isLoginSuccess)
		{	
					/*//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
					
					//create project
					if(createProject(groupName, portfolioName, project, version, projectEndMonth, projectEndYear, projectEndDate, versionLead.get(0).username))
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
	            	 if(createTestPass(groupName, portfolioName, project, version, testPass, testPassEndMonth, testPassEndYear, testPassEndDate, testManager.get(0).username))
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
					if(createTester(groupName, portfolioName, project, version, testPass, tester.get(0).username, testerRole, testerRole))
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
	 				if(createTestCase(groupName, portfolioName, project, version, testPass, testCase))
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
				    
	 				//create test step
					if(createTestStep(groupName, portfolioName, project, version, testPass, testStep, testStepExpectedResult, testCase, testerRole))
					{
						APP_LOGS.debug("Test Step Created Successfully.");
					}
					else
					{
	    				fail=true;
                   	 	APP_LOGS.debug("Test Step is not created successfully");
                   	 	comments+="Test Step not Created Successfully(Fail). ";
                   	 	assertTrue(false);
    					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
    					closeBrowser();
    					 
           			 	throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Dashboard Suite");
					}*/
					
    				closeBrowser();
			        		
    				
    				//again login with tester user
    				APP_LOGS.debug("Opening Browser... for user "+tester.get(0).username);
    				
    				openBrowser();
    				
    				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
    				{
    					try
    					{
    							builder = new Actions(eventfiringdriver);
    					
		    					//verify hover text on Days Left
    							boolean daysLeftColVisible = isElementExistsByXpath("DashboardMyActivity_daysLeftHoverColumn");
		    			        if(assertTrue(daysLeftColVisible))
		    			        {
		    			        	
			    			        builder.moveToElement(getObject("DashboardMyActivity_daysLeftHoverColumn")).build().perform();
			    			        Thread.sleep(1000);
			    			      
			    					if(compareStrings(resourceFileConversion.getProperty("DaysLeftMessage"), getObject("DashboardMyActivity_daysLeftHoverColumn").getAttribute("title")))
			    					{
			    						APP_LOGS.debug("Hover message on Days Left column is matched and verified");
			        					comments+="Hover message on Days Left column is matched and verified(Pass). ";
			    					}
			    					else
			    					{
			    						fail=true;
			        					APP_LOGS.debug("Hover message on Days Left column is not matched");
			        					comments+="Hover message on Days Left column is not matched(Fail). ";
			        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DaysLeftMsgNotMatched");
			    					}
		    			        }
		    			        else
		    			        {
		    			        	fail=true;
		    			        	APP_LOGS.debug("Days Left column not visible");
		        					comments+="Days Left column not visible(Fail). ";
		        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DaysLeftColumnNotVisible");
		    			        }
		    					
		    			        
		    					//verify hover text on Not Completed 
		    			        boolean notCompletedColVisible = isElementExistsByXpath("DashboardMyActivity_notCompletedHoverColumn");
		    			        if(assertTrue(notCompletedColVisible))
		    			        {
				    			        builder.moveToElement(getObject("DashboardMyActivity_notCompletedHoverColumn")).build().perform();
				    			        Thread.sleep(1000);
				    			       
				    					if(compareStrings(resourceFileConversion.getProperty("NotCompletedMessage"), getObject("DashboardMyActivity_notCompletedHoverColumn").getAttribute("title")))
				    					{
				    						APP_LOGS.debug("Hover message on Not Completed column is matched and verified");
				        					comments+="Hover message on Not Completed column is matched and verified(Pass). ";
				    					}
				    					else
				    					{
				    						fail=true;
				        					APP_LOGS.debug("Hover message on Not Completed column is not matched");
				        					comments+="Fail- Hover message on Not Completed column is not matched. ";
				        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NotCompletedMsgNotMatched");
				    					}
		    			        }
		    			        else
		    			        {
		    			        	    fail=true;
			    			        	APP_LOGS.debug("Not Completed column not visible");
			        					comments+="Not Completed column not visible(Fail). ";
			        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NotCompletedColumnNotVisible");
		    			        }
		    					
		    			        
		    					//verify hover text on Offline Testing
		    			        boolean offlineTestingColVisible = isElementExistsById("DashboardMyActivity_offlineTestingHoverColumn_Id");
		    			        if(assertTrue(offlineTestingColVisible))
		    			        {
				    					
				    			        builder.moveToElement(getElement("DashboardMyActivity_offlineTestingHoverColumn_Id")).build().perform();
				    			        Thread.sleep(1000);
				    			        
				    			        String offlineTestingText = (String)eventfiringdriver.executeScript("return $('#"+OR.getProperty("DashboardMyActivity_offlineTestingOnHover_Id")+"').text()");
				    			       
				    					if(compareStrings(resourceFileConversion.getProperty("OfflineTestingMessage"), offlineTestingText))
				    					{
				    						APP_LOGS.debug("Hover message on Offline Testing column is matched and verified");
				        					comments+="Hover message on Offline Testing column is matched and verified(Pass). ";
				    					}
				    					else
				    					{
				    						fail=true;
				    						APP_LOGS.debug("Hover message on Offline Testing column is not matched");
				        					comments+="Hover message on Offline Testing column is not matched(Fail). ";
				        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "OfflineTestingMsgNotMatched");
				    					}
		    			        }
		    			        else
		    			        {
			    			        	fail=true;
			    			        	APP_LOGS.debug("Offline Testing column not visible");
			        					comments+="Offline Testing column not visible(Fail). ";
			        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "OfflineTestingColumnNotVisible");
		    			        }
		    					
		    			        
		    					//verify hover text on Detailed Analysis button
		    			        boolean detailedAnalysisBtnVisible = isElementExistsById("DashboardMyActivity_detailedAnalysisBtn_Id");
		    			        if(assertTrue(detailedAnalysisBtnVisible))
		    			        {
				    					
				    			        builder.moveToElement(getElement("DashboardMyActivity_detailedAnalysisBtn_Id")).build().perform();
				    			        Thread.sleep(1000);
				    			        
				    					String detailedAnalysisHoveredText = (String) eventfiringdriver.executeScript("return DashBoard.gDetailAnalysisTitle");
				    					
				    					if(compareStrings(resourceFileConversion.getProperty("DetailedAnalysisMessage"), detailedAnalysisHoveredText))
				    					{
				    						APP_LOGS.debug("Hover message on Detailed Analysis button is matched and verified");
				        					comments+="Hover message on Detailed Analysis button is matched and verified(Pass). ";
				    					}
				    					else
				    					{
				    						fail=true;
				    						APP_LOGS.debug("Hover message on Detailed Analysis button is not matched");
				        					comments+="Hover message on Detailed Analysis button is not matched(Fail). ";
				        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisgMsgNotMatched");
				    					}
		    			        }
		    			        else
		    			        {
		    			        	    fail=true;
			    			        	APP_LOGS.debug("Detailed Analysis button not visible");
			        					comments+="Detailed Analysis button not visible(Fail). ";
			        					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DetailedAnalysisBtnNotVisible");
		    			        }
		    					
    					}
    					catch(Throwable t)
    					{
    						fail=true;
    						t.printStackTrace();
    						assertTrue(false);
    				    	APP_LOGS.debug("Exception occured in verifying Hover Message TestCase");
    						comments+="Exception occured in verifying Hover Message TestCase. ";
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
				APP_LOGS.debug("Login Unsuccessfull for user "+role);
				comments+="Login Unsuccessfull for user "+role;
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
		else
		{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		
	}
	
	
	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}


	
}

