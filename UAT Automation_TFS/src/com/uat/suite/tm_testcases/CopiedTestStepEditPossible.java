package com.uat.suite.tm_testcases;

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

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;


@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class CopiedTestStepEditPossible extends TestSuiteBase 
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=true;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	public String comments;
	String testpassCheckboxMode;
	int i;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
		   
		if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
		
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		testers=new ArrayList<Credentials>();
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	// Test Case Implementation ...			
		@Test(dataProvider="getTestData")
		public void testTCs_CopiedTestStepEditPossible
			(
				String Role,String GroupName,String PortfolioName,String ProjectName, String Version, 
				String EndMonth, String EndYear, String EndDate,String VersionLead,
				String TestPassName1,String TestPassName2,String TestManager,String TP_EndMonth,String TP_EndYear,String TP_EndDate,
				String Tester,String AddRole1,String AddRole2,String TestCaseName, String TestStepName , String TestStepExpectedResults, 
				String TestStepNameUpdated, String TestStepUpdatedExpectedResults
		    ) throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			comments ="";
			
			int versionLead_count = Integer.parseInt(VersionLead);
			int testManager_count = Integer.parseInt(TestManager);
			int testers_count = Integer.parseInt(Tester);		

			versionLead = getUsers("Version Lead", versionLead_count);
			testManager = getUsers("Test Manager", testManager_count);				
			testers = getUsers("Tester", testers_count);
			
			APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
			
			APP_LOGS.debug("Opening Browser... ");
			Thread.sleep(1000);
			
			openBrowser();
			Thread.sleep(1000);
			isLoginSuccess = login(Role);
			
			if(isLoginSuccess)
			{
				try
				{	
					//click on testManagement tab				
					APP_LOGS.debug("Clicking On Test Management Tab ");				
					getElement("UAT_testManagement_Id").click();				
					Thread.sleep(1000);
					
					APP_LOGS.debug(" User : "+ Role +" creating PROJECT :" +ProjectName+ " with Version Lead "+versionLead.get(0).username );
					if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{						
						fail=true;
						assertTrue(fail);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure_CopiedTestStepEditPossible");
						comments="Project Creation Unsuccessful(Fail) by "+Role+". ";
						APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
						closeBrowser();
						throw new SkipException("Project Creation Unsuccessfull");
					}
					
					APP_LOGS.debug("Closing Browser... ");
					closeBrowser();
					
					APP_LOGS.debug("Opening Browser...");
					openBrowser();
					
					APP_LOGS.debug("Logging In With Role Version Lead " +versionLead.get(0).username + " of PROJECT :" +ProjectName+ " to create Test Pass :" + TestPassName1 + ""+TestPassName2);
					if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
					{
						//click on testManagement tab				
						APP_LOGS.debug("Clicking On Test Management Tab ");
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);		
						
						APP_LOGS.debug("Creation of Test Pass : "+TestPassName1);	
						if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP_EndMonth,TP_EndYear,TP_EndDate, testManager.get(0).username))
						{	
							fail=true;
							assertTrue(fail);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure_CopiedTestStepEditPossible");
							comments="Test Pass Creation Unsuccessful(Fail)" ;
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
							closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Creation of Test Pass : "+TestPassName2);
						if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, TP_EndMonth,TP_EndYear,TP_EndDate, testManager.get(0).username))
						{	
							fail=true;
							assertTrue(fail);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure_CopiedTestStepEditPossible");
							comments="Test Pass Creation Unsuccessful(Fail)" ;
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
							closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						closeBrowser();
						
						APP_LOGS.debug("Opening Browser...");
						openBrowser();
						
						APP_LOGS.debug("Logging In With Role Test Manager "+testManager.get(0).username + " to create Tester1 : " + testers.get(0).username + "with role :"+AddRole1 +" to create Tester2 : " + testers.get(1).username + "with role :"+AddRole2 );
						if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
						{ 
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
							
							//Tester1 with AddRole1 creation for Test Pass1							
							APP_LOGS.debug("Creation of Tester : " +testers.get(0).username + "with role : " +AddRole1);
							if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, testers.get(0).username,AddRole1, AddRole1)) 
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure_CopiedTestStepEditPossible");
								comments="Tester Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Tester Creation Unsuccessfull");
								closeBrowser();
								throw new SkipException("Tester Creation Unsuccessfull");
							}
							
							//Tester2 with AddRol2 creation for Test Pass2
							APP_LOGS.debug("Creation of Tester : " +testers.get(1).username + "with role : " +AddRole2);
							if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, testers.get(1).username,AddRole2, AddRole2)) 
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure_CopiedTestStepEditPossible");
								comments="Tester Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Tester Creation Unsuccessfull");
								closeBrowser();
								throw new SkipException("Tester Creation Unsuccessfull");
							}
							
							//Test Case creation for Test Pass1
							APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases :" +TestCaseName);						
							if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestCaseName))
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure_CopiedTestStepEditPossible");
								comments="Tester Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Test Case Creation Unsuccessfull");
								closeBrowser();
								throw new SkipException("Test Case Creation Unsuccessfull");
							}
							
							//Test Step creation for Test Case of Test Pass1
							APP_LOGS.debug(" Test Manager "+testManager.get(0).username + " creating  Test Step :" +TestStepName+ " for Test Case : " +TestCaseName+ "of Test Pass :" +TestPassName1 );						
							if (!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestStepName,TestStepExpectedResults,TestCaseName, AddRole1))
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationFailure_CopiedTestStepEditPossible");
								comments="Test Step Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Test Step Creation Unsuccessfull");
								closeBrowser();
								throw new SkipException("Test Step Creation Unsuccessfull");
							}
							
							try
							{
								//Implementation of Test Case 							
								Thread.sleep(2000);	
								
								APP_LOGS.debug("Clicking on Copy link after test step creation in : " +TestCaseName+ " of " +TestPassName1);
								
								getElement("TestCaseNavigation_Id").click();
								
								dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
								
								dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
								
								dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
								
								dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
								
								dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName1 );
							
							}
							catch(Throwable t)
							{
								  fail=true;
						    	  assertTrue(fail);
						    	  t.printStackTrace();
						    	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DropDownSelectionFailure");
						    	  comments="DropDown Selection Failure Occurring before clicking on copy icon(Fail)";
						    	  APP_LOGS.debug("DropDown Selection Failure Occurring before clicking on copy icon(Fail)");
							}
							
							
							
							//Click on COPY link
							APP_LOGS.debug("Clicking On COPY Link");
							getElement("TestCases_CopyTestCasesLink_Id").click();
							
							//Click on COPY Icon of TC
							APP_LOGS.debug("Verifiication of actual Test Case is displaying in Copy Test Case table of : " +TestPassName1);
							List<WebElement> listofCopyTestCases = getObject("TestCaseCopyTC_copyTestCaseTable").findElements(By.tagName("tr"));
							for(i=1;i<=listofCopyTestCases.size();i++)
							{
								String actualtestCaseName = getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyTestCaseNameExtractXpath2", i).getText();
								
								APP_LOGS.debug("Comparing test case name with the actual Test Case name then clicking on Copy Icon");
								if(assertTrue(actualtestCaseName.equals(TestCaseName)))
								{
									APP_LOGS.debug("Test Case name matched, hence clicking on Copy Icon");								
									getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyIconXpath2", i).click();								
								}
								else
								{
							    	fail=true;
									assertTrue(fail);
									comments+="Test Case Name not matched hence not clicked on Copy icon :Fail";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CopyTestCaseError_TCs_CopyToTPNotHavingTester ");
									APP_LOGS.debug("Test Case Name not matched hence not clicked on Copy icon :Fail");
									closeBrowser();
									throw new SkipException("Test Case Name not matched hence not clicked on Copy icon:Fail");
								}
							}
							
							//Verification of checkboxes	
							APP_LOGS.debug("Verification of Fly Out Test Passes name and their checkboxes mode");
							List<WebElement> listOftestPassesInFlyOut = getObject("TestCaseCopyTC_flyOutTestPassTable").findElements(By.tagName("li"));
							for(int flyoutTPCount=1;flyoutTPCount<=listOftestPassesInFlyOut.size();flyoutTPCount++)
							{
								APP_LOGS.debug("Extracting Test Passes name from Copy test Case Fly Out");
								String actualFlyoutTPName = getObject("TestCaseCopyTC_testPassNameExtractXpath1", "TestCaseCopyTC_testPassNameExtractXpath2", flyoutTPCount).getText();
								
								APP_LOGS.debug("Extract name of Test Pass selcetd in Upper ribbon drop down");
								String selectedTPTextFromUpperRibbon =getObject("TestCases_testPassMemberSelected").getAttribute("title");
								
								//Verification of Upper ribbon selecetd test pass name with the name of Test Passses name present in copy fly out 
								APP_LOGS.debug("Verification of Upper ribbon selecetd test pass name with the name of Test Passses name present in copy fly out ");
								if(actualFlyoutTPName.equals(selectedTPTextFromUpperRibbon))
								{
									APP_LOGS.debug("Test Pass name from copy fly out is  matched with the upper ribbon selected test pass name, hence verifying the checkbox is enabled and checked");
									testpassCheckboxMode =eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).getAttribute("disabled");
								
									APP_LOGS.debug("Verifying the checkbox mode of Upper Ribbon selecetd Test Pass and Fly Out Test Pass");
									if(assertTrue(testpassCheckboxMode.equalsIgnoreCase("True")))
									{
										APP_LOGS.debug(TestCaseName +" is  belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" + testpassCheckboxMode +"checked and disabled");
									}
								}
								else
								{
									///If Upper ribbon selecetd test pass name is not matched with  Test Passses name present in copy fly out then clicking on its checkbox
									APP_LOGS.debug(TestCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" +testpassCheckboxMode+ "unchecked and enaabled");
									Thread.sleep(500);
									
									APP_LOGS.debug("Selecting checkbox of Test Pass : " + actualFlyoutTPName + "whose name is not matched with teh upper ribbon selcetd Test Pass");
									eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).click();
									
									APP_LOGS.debug("After selecting checkbox of other Test Pass :" +actualFlyoutTPName+ "clicking on flyout OK button");
									getElement("TestCaseCopyTC_flyOutOkBtn_Id").click();
									
									APP_LOGS.debug("Test Case succesfully copied to TP : "+actualFlyoutTPName);
									getObject("TestCaseCopyTC_copyTCSuccessPopUpOkBtn").click();									
									break;
								}
							}
							
							
							//Verification of Copy Test Step should get edit in Test Pass 2 in which the test case is copied 
							APP_LOGS.debug("Verification of Copy Test Step should get edit in Test Pass 2 in which the test case is copied along with test step");
							try
							{ 
					           
				               Thread.sleep(2000);    
				               
				               getElement("TestStepNavigation_Id").click();
				         
				               dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
				               
				               dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
				               
				               dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
				               
				               dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
				               
				               dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName2);
					         }
				             catch(Throwable t)
				             {
				            	 fail=true;
				            	 t.printStackTrace();
								 comments+="Test Case Name not matched hence not clicked on Copy icon :Fail";
								 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CopyTestCaseError_TCs_CopyToTPNotHavingTester ");
								 APP_LOGS.debug("Test Case Name not matched hence not clicked on Copy icon :Fail");
								 closeBrowser();
								 throw new SkipException("Test Case Name not matched hence not clicked on Copy icon:Fail");
									
				         
				            }
							
							//Verification of copied test case test step in Other Test Pass
							APP_LOGS.debug("Verification of copied test case test step is also copied in Other Test Pass");							
							List<WebElement> listofCopiedTestStep =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
							for(int testStepCount=1;testStepCount<=listofCopiedTestStep.size();testStepCount++)
							{
								//Verification of copied test name with the actual test step name in other test pass
								APP_LOGS.debug("Verfication of Copied test step name is matched with the actual test step name");		
								String actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
							
								//Verification of copied test case name  with the actual test case name in other test pass
								String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
							
								APP_LOGS.debug("Comparing the name of copied test step with the actual test step and copied test case name with the actual test case name");
								if(compareStrings(actualTestStepName, TestStepName)&&compareStrings(actualTestCaseName, TestCaseName))
								{
									APP_LOGS.debug("Name of copied test step and copied test case is matched");									
									
									APP_LOGS.debug("The name of test steps are matched , hence clicking on Edit icon");									
									getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepEditIconXpath2", testStepCount).click();
									
									//Peform Edition operation of copied test step
									
									APP_LOGS.debug("Perform edition operation on copied test case test step");
									String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+TestStepNameUpdated+"')";
								   	eventfiringdriver.executeScript(testStepDetails);
								   	
								   	//Clearing Test Step expected result data and entering the updated input value
								    getElement("TestStepCreateNew_testStepExpectedResults_ID").clear(); 								    
								    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(TestStepUpdatedExpectedResults); 
									
								    //Selecting new role
								    String[] testerRoleSelectionArray = AddRole2.split(",");
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
									
									//After making updation in copied test case test step clicking on Update button
									APP_LOGS.debug("After making updation in copied test case test step clicking on Update button");
									getElement("TestStepCreateNew_testStepUpdateBtn_Id").click();
									
									//Verification of success message coming on clicking on Update button
									APP_LOGS.debug("Verification of text from Test Step update successfully message");
									if (getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText().contains("successfully")) 
									{
										getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
										APP_LOGS.debug("Test Step in Test Pass :" +TestPassName2+ "is updated succesfully");
										
									}
									break;
								}
								else
								{
									APP_LOGS.debug("Name of test step is not matched with the actual name of test step hence not click on Edit icon");
									comments+="Name of test step is not matched with the actual name of test step hence not click on Edit icon";
								}
							}
							
							
							//Verification of Copy Test Case Test Step is updated really in Other Test Pass i.e. Test Pass2 
							APP_LOGS.debug("Verification of copied test case test step is updated in other Test Pass");							
						    listofCopiedTestStep =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
							for(int testStepCount=1;testStepCount<=listofCopiedTestStep.size();testStepCount++)
							{
								//Verification of copied test name with the actual test step name in other test pass
								APP_LOGS.debug("Verfication of Copied test step name is matched with the actual test step name");		
								String actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
							
								//Verification of copied test case name  with the actual test case name in other test pass
								String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
						
								APP_LOGS.debug("Comparing the name of copied test step with the actual test step and copied test case name with the actual test case name");
								if(compareStrings(actualTestStepName, TestStepNameUpdated)&&compareStrings(actualTestCaseName, TestCaseName))
								{
									APP_LOGS.debug("Name of copied test step and copied test case is matched");									
								}
							}
							
							try
							{
								//Verification of original Test Pass is having still the same name of test step
								APP_LOGS.debug("Verification of original Test Pass is having still the same name of test step");
								dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
					               
				                dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
				               
				                dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
				               
				                dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
				               
				                dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName1);
				               
				                //Extracting the name of test step from Actual Test pass
				                APP_LOGS.debug("Retrieving the name of test step from actual test pass to verify is still present in Test Step table ");
					            listofCopiedTestStep =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
								for(int testStepCount=1;testStepCount<=listofCopiedTestStep.size();testStepCount++)
								{
									//Verification of actual test step name with the actual test step name in actual test pass
									APP_LOGS.debug("Verfication of  test step name in actual test pass");		
									String actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
								
									//Verification of actual test case name with the actual test case name in actual test pass
									APP_LOGS.debug("Verification of actual test case name with the actual test case name in actual test pass");		
									String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
							  
									//Comparing the name of test case and test step in actual test pass is still same as at the time of creation
									if(compareStrings(actualTestStepName, TestStepName)&&compareStrings(actualTestCaseName, TestCaseName))
									{
										APP_LOGS.debug("The name of Test Step is as it is in Test Pass: " +TestPassName1);
									}
								 }
								
							}
							catch(Throwable t)
							{
					               fail=true;
					               t.printStackTrace();
					               comments+="Unable to find the Actual test step and test case name in Original Test Pass";
					               TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unable Test Pass selction");
							}
							
							closeBrowser();
						}
						else 
						{
							fail= true;
							APP_LOGS.debug("Test Manager Login Not Successful");
						}			
					}
					else 
					{
						fail= true;
						APP_LOGS.debug("Version Lead Login Not Successful");
					}
				}
				catch (Throwable e) 
				{
						e.printStackTrace();
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
						comments+="Skip Exception or other exception occured" ;
				}
				
				APP_LOGS.debug("Closing Browser... ");
		
				closeBrowser();
			}
			else 
			{
				isLoginSuccess=false;
				APP_LOGS.debug("Login Not Successful");
			}	

		}
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else
			{
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			skip=false;
			fail=false;		

		}
		

		@AfterTest
		public void reportTestResult() throws Exception{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
			utilRecorder.stopRecording();
		}
		
		@DataProvider
		public Object[][] getTestData(){
			return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
		}
		
		
		/*public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
				String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Test Pass");
			Thread.sleep(2000);
			getElement("TestPassNavigation_Id").click();
			Thread.sleep(2000);
			getObject("TestPasses_createNewProjectLink").click();
			boolean result = true;
			
			try {
				
					dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
					
					dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
					
					dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
					
					dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
					
				
					getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
					
					getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
					   
					getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
			   
					getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
			   
					getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
					
					getObject("TestPassCreateNew_testPassSaveBtn").click();
				
				
					Thread.sleep(2000);							
					
					
					if (getElement("TestPassCreateNew_testPassSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
						
						result = true;
						
					}
					else 
					{
						result = false;
					}
					
					return result;
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("Exception in create Test Pass function.");
				e.printStackTrace();
				return false;
			}
			
		}*/

		
	public boolean createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		Thread.sleep(2000);
		getElement("TesterNavigation_Id").click();
		
		if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
		{
			getObject("TesterCreateNew_TesterActiveX_Close").click();
		}
		Thread.sleep(2000);
		
		try {
		
			dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
			
			dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
			
			getElement("Tester_createNewProjectLink_Id").click();
			
			Thread.sleep(1000);
			
			getObject("TesterCreateNew_PeoplePickerImg").click();
			   
			driver.switchTo().frame(1);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
			   
			getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
	   
			getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
	   
			getObject("TesterCreateNew_PeoplePickerOkBtn").click();
	   
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			String[] testerRoleArray = testerRoleCreation.split(",");
			
			for(int i=0;i<testerRoleArray.length;i++)
			{
				if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
				{
					getElement("TesterCreateNew_addTesterRoleLink_Id").click();
					getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
					getElement("TesterCreateNew_addTesterRole_Id").click();
					if(getElement("TesterCreateNew_roleAlreayExistsAlert_Id").isDisplayed())
					{
						getObject("TesterCreateNew_roleAlreayExistsAlertOkBtn").click();
						getElement("TesterCreateNew_addroleCancelBtn_Id").click();
					}
				}
			}
			String[] testerRoleSelectionArray = testerRoleSelection.split(",");
			List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
			int numOfRoles = roleSelectionNames.size();
			for(int i = 0; i<numOfRoles;i++)
			{
				for(int j = 0; j < testerRoleSelectionArray.length;j++)
				{
					if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
					{
						getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
					}
				}
			}
			getElement("TesterCreateNew_testerSaveBtn_Id").click();
			
			Thread.sleep(2000);
			
			if (getElement("TesterCreateNew_testerSuccessMessageText_Id").getText().contains("successfully")) 
			{
				getObject("Tester_testeraddedsuccessfullyOkButton").click();
				
				return true;
			}
			else 
			{
				return false;
			}	
	} 
	catch (Throwable e) 
	{
		APP_LOGS.debug("Exception in createTester function.");
		e.printStackTrace();
		return false;
	}
	
}

	}
