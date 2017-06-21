package com.uat.suite.dashboard;

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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyMyActivity_Tester extends TestSuiteBase
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
	int testStepCount;
	int myActivityProjectRows;
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
	String[] testStepArray;
	String[] resultArray;
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
	public void verifyMyActivity_Tester(String role, String groupName, String portfolioName, String projectName, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String versionLeadName, String testPassName, 
			String testManagerName,	String testPassEndMonth, String testPassEndYear,String testPassEndDate, String testerName, 
			String testerRole, String testCase, String testStep, String testStepExpectedResult, String testStepResult, 
			String testerNoActivityAssignedMessage, String feedbackRatingPopupText, String rating, String feedback) throws Exception
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
		
		APP_LOGS.debug("Opening Browser... for user "+role);
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
			
			closeBrowser();
			
			//login with tester
	/*		APP_LOGS.debug("Opening Browser... for user Tester");
			openBrowser();
			if(login("Tester"))
			{
				if(assertTrue(getObject("DashboardMyActivity_noActivityAssignedText").getText().equals(testerNoActivityAssignedMessage)))
				{
					APP_LOGS.debug(testerNoActivityAssignedMessage+" message is correct for Tester");
    				comments+=testerNoActivityAssignedMessage+" message is correct for Tester(Pass). ";
				}
				else
				{
					 fail=true;
                	 APP_LOGS.debug(testerNoActivityAssignedMessage+" message is not visible for Tester");
                	 comments+=testerNoActivityAssignedMessage+" message is not visible for Tester(Fail). ";
 					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NoActivityAssignToTesterMsgIncorrect");
				}
				closeBrowser();
			}
			else
			{
				APP_LOGS.debug("Login Unsuccessfull for Tester. ");
				comments+="Login Unsuccessfull for Tester. ";
			}
    	*/			
			//login with version lead
			APP_LOGS.debug("Opening Browser... for user "+versionLead.get(0).username);
			openBrowser();
			
			if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
			{
				//click on testManagement tab
 				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
 				getElement("UAT_testManagement_Id").click();
 				Thread.sleep(2000);
 				
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
				
 				//create test steps
				if(createTestSteps(groupName, portfolioName, projectName, version, testPassName, testCase, testerRole, testStep, testStepExpectedResult))										
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
				}
				closeBrowser();
		
				//again login with tester user
				APP_LOGS.debug("Opening Browser... for user "+tester.get(0).username);
				openBrowser();
				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
				{
					//code for searching test pass and perform testing
					if(searchTestPassAndPerformTesting(projectName, version, testPassName, testerRole, testStepResult, 
						rating, feedback, testStep, "Begin Testing"))
					{
						APP_LOGS.debug("Testing for "+testPassName+" with role "+testerRole+" is successfull after begin testing.");
						comments+="Testing for "+testPassName+" with role "+testerRole+" is successfull after begin testing(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Testing for "+testPassName+" with role "+testerRole+" is unsuccessfull after begin testing.");
						comments+="Testing for "+testPassName+" with role "+testerRole+" is unsuccessfull after begin testing(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingUnsucessfullInBeginTesting");
						closeBrowser();
						
						throw new SkipException("Testing unsuccessfull for Tester ... So Skipping all tests in Dashboard Suite");
					}
					
					//verify count is updated after testing started and action link changed to continue testing
					if(verifyUpdatedCountAfterTesting(projectName, version, testPassName, testerRole, notCompletedCount, passCount, failCount, "Continue Testing"))
					{
						APP_LOGS.debug("After begin testing count for Not Completed, Pass and Fail is updated in My Activity grid and action is replaced with continue testing.");
						comments+="After begin testing count for Not Completed, Pass and Fail is updated in My Activity grid and action is replaced with continue testing(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("After begin testing count for Not Completed, Pass and Fail is not updated in My Activity grid and action is not replaced with continue testing.");
						comments+="After begin testing count for Not Completed, Pass and Fail is not updated in My Activity grid and action is not replaced with continue testing(Fail). ";
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CountNotUpdatedInMyActivityGridAfterBeginTesting");
						closeBrowser();
						
						throw new SkipException("Action is not changed to 'Continue Testing' ... So Skipping all tests in Dashboard Suite");
					}
					
					//code for continue testing
					if(searchTestPassAndPerformTesting(projectName, version, testPassName, testerRole, testStepResult, rating, feedback, testStep, "Continue Testing"))
					{
						APP_LOGS.debug("Testing for "+testPassName+" with role"+testerRole+" is successfull after continue testing.");
						comments+="Testing for "+testPassName+" with role"+testerRole+" is successfull after continue testing(Pass).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Testing for "+testPassName+" with role"+testerRole+" is unsuccessfull after continue testing.");
						comments+="Testing for "+testPassName+" with role"+testerRole+" is unsuccessfull after continue testing(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestingUnsucessfullInContinueTesting");
						closeBrowser();
						
						throw new SkipException("Testing unsuccessfull for Tester after continue testing... So Skipping all tests in Dashboard Suite");
					}
					
					//verify count is updated after testing complete and action link changed to Testing Complete
					if(verifyUpdatedCountAfterTesting(projectName, version, testPassName, testerRole, notCompletedCount, passCount, failCount, "Testing Complete"))
					{
						APP_LOGS.debug("After testing count for Not Completed, Pass and Fail is updated in My Activity grid and action is replaced with testing complete.");
						comments+="After testing count for Not Completed, Pass and Fail is updated in My Activity grid and action is replaced with testing complete(Pass). ";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("After continue testing count for Not Completed, Pass and Fail is not updated in My Activity grid and action is not replaced with testing complete. ");
						comments+="After continue testing count for Not Completed, Pass and Fail is not updated in My Activity grid and action is not replaced with testing complete(Fail). ";
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CountNotUpdatedInMyActivityGridAfterContinueTesting");
					}
					
					closeBrowser();
				}
				else
				{
    				APP_LOGS.debug("Login Unsuccessfull for Tester " +tester.get(0).username);
    				comments+="Login Unsuccessfull for Tester "+tester.get(0).username;
				}
			}
			else
			{
				APP_LOGS.debug("Login Unsuccessfull for Version Lead "+versionLead.get(0).username);
				comments+="Login Unsuccessfull for Version Lead "+versionLead.get(0).username;
			}
		}
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ role+"'.";
		}	
	}
	
	//create test steps	
	private boolean createTestSteps(String group, String portfolio, String project, String version, String testPassName, 
			String testCasesToBeSelected, String rolesToBeSelected, String testStep, String testStepExpectedResult) throws IOException, InterruptedException
	{
		//APP_LOGS.debug("Creating Test Step");
		Thread.sleep(2000);
		
		getElement("TestStepNavigation_Id").click();
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
		}
		Thread.sleep(2000);	
		
		try 
		{
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
				Thread.sleep(3000);
				
				getObject("TestStep_createNewProjectLink").click();
				
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
				
				String testStepCreatedResult=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
				
				if (testStepCreatedResult.contains("successfully")) 
				{
			//		getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
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
	
	//code for searching test pass and perform testing
	private boolean searchTestPassAndPerformTesting(String project, String version, String testPass, String testerRole, String testStepResult, 
			String Rating, String Feedback, String testStep, String action)
	{
		boolean feedbackPopup;
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
				myActivityProjectRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
				
				for (int j = 1; j <= myActivityProjectRows; j++) 
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
							
					if ((projectCell.equals(project)) && (versionCell.equals(version)) && (testPassCell.equals(testPass)) && (roleCell.equals(testerRole))) 
					{
						//verifying Days Left & Not Completed count
		                if((daysLeft>0) && (notCompletedCount>0) && (passCount==0) && (failCount==0) && actionCell.equals(action))
		                {
                    		APP_LOGS.debug("Action is Begin Testing.");			                    		
                    		getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
                    		Thread.sleep(3000);
                    		
                    		//spit test step names and pass/fail result by comma
                    		testStepArray = testStep.split(",");
                    		resultArray = testStepResult.split(",");
                    		
                    		for (int a = 0; a < 2; a++) 
                    		{
	                    			if((resultArray[a].contains("Pass")) && (getElement("TestingPage_passRadioButton_Id").getAttribute("value")).equals("2"))     //For Pass attr value=2
	                    			{
	                    				getElement("TestingPage_passRadioButton_Id").click();
	                    				passCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else if((resultArray[a].contains("Fail")) && (getElement("TestingPage_failRadioButton_Id").getAttribute("value")).equals("3"))     //For Fail attr value=3
	                    			{
	                    				getElement("TestingPage_failRadioButton_Id").click();
	                    				failCount++;
	                    				notCompletedCount--;
	                    			}
	                    			else
	                    			{
	                    				getElement("TestingPage_pendingRadioButton_Id").click();
	                    				notCompletedCount++;
	                    			}
	                    			
	                    			getElement("TestingPage_saveButton_Id").click();
	                    			Thread.sleep(500);
							}
                    		
                			getElement("UAT_dashboard_Id").click();
                			Thread.sleep(2000);
                			projectFound=true;
                			return true;
		                }
		                else if((daysLeft>0) && (notCompletedCount>0) && (!(passCount==0)) && (!(failCount==0)) && (actionCell.equals(action)))
		                {
                			APP_LOGS.debug("Action is Continue Testing.");
                    		getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).click();
                    		Thread.sleep(3000);
                    		
                    		testStepArray = testStep.split(",");
                    		resultArray = testStepResult.split(",");
                    		for (int a = 2; a < testStepArray.length; a++) 
                    		{
                    			if(resultArray[a].contains("Pass") && (getElement("TestingPage_passRadioButton_Id").getAttribute("value")).equals("2"))
                    			{
                    				getElement("TestingPage_passRadioButton_Id").click();
                    				passCount++;
                    				notCompletedCount--;
                    			}
                    			else if(resultArray[a].contains("Fail") && (getElement("TestingPage_failRadioButton_Id").getAttribute("value")).equals("3"))
                    			{
                    				getElement("TestingPage_failRadioButton_Id").click();
                    				failCount++;
                    				notCompletedCount--;
                    			}
                    			else
                    			{
                    				getElement("TestingPage_pendingRadioButton_Id").click();
                    				notCompletedCount++;
                    			}
                    			
                    			getElement("TestingPage_saveButton_Id").click();
                    			Thread.sleep(500);
                    		}
                   		
                    		feedbackPopup = isElementExistsById("TestingPage_feebackRatingPopup_Id");
                    		if(assertTrue(feedbackPopup))
                    		{
                    			APP_LOGS.debug("Feedback Rating popup visible");
                				comments+="Pass- Feedback Rating popup visible";
                				
                    			if(Rating.equals(getObject("TestingPage_rating_verySatisfiedRadioBtn").getAttribute("value")))
                    			{
                    				getObject("TestingPage_rating_verySatisfiedRadioBtn").click();
                    			}
                    			
                    			else if(Rating.equals(getObject("TestingPage_rating_veryDissatisfiedRadioBtn").getAttribute("value")))
                    			{
                    				getObject("TestingPage_rating_veryDissatisfiedRadioBtn").click();
                    			}
                    			
                    			else if(Rating.equals(getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").getAttribute("value")))
                    			{
                    				getObject("TestingPage_rating_somewhatSatisfiedRadioBtn").click();
                    			}
                    			
                    			else if(Rating.equals(getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").getAttribute("value")))
                    			{
                    				getObject("TestingPage_rating_somewhatDissatisfiedRadioBtn").click();
                    			}
                			
                    			//provide feedback
                    			String feedbackDetails = "$(document).contents().find('#rte3').contents().find('body').text('"+Feedback+"')";
                    			eventfiringdriver.executeScript(feedbackDetails);
                    			
                    			//save
                    			getObject("TestingPage_feedbackRating_SaveButton").click();
                    			Thread.sleep(500);
                    			
                    			//return to dashboard
                    			getObject("TestingPage_returnToHomeButton").click();
                    			Thread.sleep(2000);
                    		}
                    		else
                    		{
                    			fail=true;
                    			APP_LOGS.debug("Feedback Rating popup not shown");
                				comments+="Feedback Rating popup not shown(Fail). ";
                    			return false;
                    		}
                    		
                    		getElement("UAT_dashboard_Id").click();
                			Thread.sleep(2000);
                			projectFound=true;
                			return true;
		                }
		                else if((daysLeft>0) && (notCompletedCount>0) && (!(passCount==0)) && 
		                    (!(failCount==0)) && (actionCell.equals(action)))
			            {
		                	APP_LOGS.debug("Action is Testing Ccomplete with link.");
		                    comments+="Action is Testing Complete..hence all the test steps under "+testPassCell+" are executed. ";
		                    projectFound=true;
		                    return true;
			            }
		                else if((daysLeft==0) && (notCompletedCount>0) && (!(passCount==0)) && 
		                    (!(failCount==0)) && (actionCell.equals(action)))
			            {
		                	APP_LOGS.debug("Action is Testing Incomplete without link.");
		                    projectFound=true;
		                    return true;
			            }
		                else if((daysLeft==0) && (notCompletedCount==0) && (!(passCount==0)) && 
		                    (!(failCount==0)) && (actionCell.equals(action)))
			            {
		                	APP_LOGS.debug("Action is Testing Complete without link.");
		                    projectFound=true;
		                    return true;
			            }
		                else if((daysLeft>0 && notCompletedCount==0) && (!(passCount==0)) && 
		                    (!(failCount==0)) && (actionCell.equals(action)))
			            {
		                	APP_LOGS.debug("Action is Testing Complete with link.");
		                    projectFound=true;
		                    return true;
			            }
					}
				}
				//if total pages greater than 1 click next link
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
			APP_LOGS.debug("Exception occured in searchTestPassAndPerformTesting function");
			comments+="Exception occured in searchTestPassAndPerformTesting function. ";
			assertTrue(false);
			return false;
		}
	}
	
	//code for verifying updated count
	private boolean verifyUpdatedCountAfterTesting(String project, String version, String testPass, String testerRole, int notCompletedCount, 
			int passCount, int failCount, String actionCellStatus) throws IOException
	{
		int updatedNotCompletedCount=0;
		int updatedPassCount=0;
		int updatedFailCount=0;
		boolean updatedProjectFound=false;
		boolean nextLinkEnabled;
		int paginationCount=0;
		
		try
		{
			if(getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
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
				myActivityProjectRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();
				
				for (int j = 1; j <= myActivityProjectRows; j++) 
				{
					projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
					versionCell = getObject("DashboardMyActivity_versionColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
					testPassCell = getObject("DashboardMyActivity_testPassNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
					roleCell = getObject("DashboardMyActivity_roleNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");
					updatedNotCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_notCompletedCountColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());
					updatedPassCount = Integer.parseInt(getObject("DashboardMyActivity_passCountColumn1", "DashboardMyActivity_passCountColumn2", j).getText());
					updatedFailCount = Integer.parseInt(getObject("DashboardMyActivity_failCountColumn1", "DashboardMyActivity_failCountColumn2", j).getText());
					actionCell = getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).getText();
							
					if ((projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole)
						&& (updatedNotCompletedCount==notCompletedCount) && (updatedPassCount==passCount) && (updatedFailCount==failCount) && actionCell.equals(actionCellStatus))) 
					{
						updatedProjectFound=true;
						return true;
					}
				}
				if (totalPages>1 && updatedProjectFound==false) 
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
			APP_LOGS.debug(testPass+ " Not found in My Activity grid for verifying updated count");
			comments+=testPass+ " Not found in My Activity grid for verifying updated count. ";
			return false;
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
			APP_LOGS.debug("Exception occured in verifying updated count for test pass after testing My Activity grid");
			comments+="Exception occured in verifying updated count for test pass after testing My Activity grid. ";
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
