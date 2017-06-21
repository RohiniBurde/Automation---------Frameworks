package com.uat.suite.tm_testcases;


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

public class CopiedTestStepDeletePossible extends TestSuiteBase {
	
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	static String testCaseCreatedSuccessMessage;
	String comments;
	String testpassCheckboxMode;
	int testStepCount;
	String actualTestStepName;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			testManager=new ArrayList<Credentials>();
			testers=new ArrayList<Credentials>();
		}
	
		// Test Case Implementation ...
			
			@Test(dataProvider="getTestData")
			public void Test_CopiedTestStepDeletePossible (String Role,String GroupName,String PortfolioName,String ProjectName, 
					String Version,String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName1,String TestPassName2,
					String TestManager,String Tester,String AddRole1,String AddRole2,String TestCaseName,String TestStepExpectedDetails,String ExpectedTestSTepResults, String expectedDeleteSuccessMessage  ) throws Exception
			{
				count++;
				
				if(!runmodes[count].equalsIgnoreCase("Y")){
					skip=true;
					APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
		
					throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
							}
				
				
				comments="";
				int versionLead_count = Integer.parseInt(VersionLead);
				
				versionLead = getUsers("Version Lead", versionLead_count);
				

				int testManager_count = Integer.parseInt(TestManager);
				
				testManager = getUsers("Test Manager", testManager_count);
				
				
				int testers_count = Integer.parseInt(Tester);			
				
						
				testers = getUsers("Tester", testers_count);
				
				
				
				APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());
				
				System.out.println(" Executing Test Case :-"+ this.getClass().getSimpleName());				
				
				APP_LOGS.debug("Opening Browser... ");
				
				System.out.println("Opening Browser... ");
				
				openBrowser();
				
				
				isLoginSuccess = login(Role);
				

				if(isLoginSuccess)
				{
						
					
					
					try
					{
					
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						
						System.out.println("Clicking On Test Management Tab ");
						
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(1000);
						
						APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
						
						System.out.println(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username);
						
						if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{	
							
							fail=true;
							assertTrue(fail);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
							comments="Project Creation Unsuccessful(Fail) by "+Role+". ";
							APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
							closeBrowser();
							throw new SkipException("Project Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						System.out.println("Closing Browser... ");						
						closeBrowser();
						
					
						
						APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
						System.out.println("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
						
						openBrowser();
						
						if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
						{
							
							
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							System.out.println("Clicking On Test Management Tab ");
							
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(2000);
							
							if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
								comments="Test Pass Creation Unsuccessful(Fail)";
								APP_LOGS.debug("Test Pass Creation Creation Unsuccessful(Fail)");
								closeBrowser();
								throw new SkipException("Test Pass Creation Unsuccessfull");
							}
							
							
							if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName2, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{	
								fail=true;
								assertTrue(fail);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
								comments="Test Pass Creation Unsuccessful(Fail)";
								APP_LOGS.debug("Test Pass Creation Creation Unsuccessful(Fail)");
								closeBrowser();
								throw new SkipException("Test Pass Creation Unsuccessfull");
							}
							
							APP_LOGS.debug("Closing Browser... ");
							
							System.out.println("Closing Browser... ");
													
							closeBrowser();
							
							
							

							APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
							
							System.out.println("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
							
							openBrowser();
							
							if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
							{
								
								
								
								try
								{
									
									
									//click on testManagement tab
									APP_LOGS.debug("Clicking On Test Management Tab ");
									
									System.out.println("Clicking On Test Management Tab ");
									
									getElement("UAT_testManagement_Id").click();
									
									Thread.sleep(2000);
									
									if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, testers.get(0).username,AddRole1, AddRole1)) 
									{	
										fail=true;
										assertTrue(fail);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestersCreationFailure");
										comments="Testers Creation Unsuccessful(Fail)";
										APP_LOGS.debug("Testers Creation Unsuccessful(Fail)");
										closeBrowser();
										throw new SkipException("Testers Creation Unsuccessfull");
									}
									
									
									if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName2, testers.get(1).username,AddRole2, AddRole2)) 
									{	
										fail=true;
										assertTrue(fail);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestersCreationFailure");
										comments="Testers Creation Unsuccessful(Fail)";
										APP_LOGS.debug("Testers Creation Unsuccessful(Fail)");
										closeBrowser();
										throw new SkipException("Testers Creation Unsuccessfull");
									}  
								
								
									System.out.println("Test Manager "+testManager.get(0).username + " creating Test Cases ");
								
									APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases ");
								
									if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestCaseName))
									{	
										fail=true;
										assertTrue(fail);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
										comments="Test Case Creation Unsuccessful(Fail)";
										APP_LOGS.debug("Test Case Creation Unsuccessful(Fail)");
										closeBrowser();
										throw new SkipException("Test Case Creation Unsuccessfull");
									}								
								
									APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Step :" +TestCaseName);      
							       if (!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestStepExpectedDetails,ExpectedTestSTepResults,TestCaseName, AddRole1))
							       { 
							    	   	fail=true;
										assertTrue(fail);
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure");
										comments="Test Case Creation Unsuccessful(Fail)";
										APP_LOGS.debug("Test Case Creation Unsuccessful(Fail)");
										closeBrowser();
										throw new SkipException("Test Case Creation Unsuccessfull");
							       }
							     
							       
								      try
								      { 
								       
								     //Implementation of Test Case 
								       APP_LOGS.debug("Creating Test Case");
								       Thread.sleep(2000);       
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
								    	  comments="DropDown Selection Failure Occurring While Creating Test case(Fail)";
								    	  APP_LOGS.debug("DropDown Selection Failure Occurring While Creating Test case(Fail)");
											
										
								      }
							      
								      //Click on COPY link
								      
								      APP_LOGS.debug("Clicking On COPY Link Of Test Cases Page");
								      
								      getElement("TestCases_CopyTestCasesLink_Id").click();
							       
								      //Click on COPY Icon of Test Case
								      
								      APP_LOGS.debug("Verifying The Actual Created Test Case is present within the Test Case Table ");
								      
								     
								      List<WebElement> listofCopyTestCases = getObject("TestCaseCopyTC_copyTestCaseTable").findElements(By.tagName("tr"));
								      String actualtestCaseName;
							       
								      for(int i=1;i<=listofCopyTestCases.size();i++)
								      {
								    	  actualtestCaseName = getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyTestCaseNameExtractXpath2", i).getText();
								    	  System.out.println("Name of test cases present in Test Case Table :" +actualtestCaseName);
								        
								    	  if(assertTrue(actualtestCaseName.equals(TestCaseName)))
								    	  {
										         APP_LOGS.debug("Test Case name matched, hence clicking on Copy Icon");        
										         getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyIconXpath2", i).click();        
								    	  }
								    	  else
								    	  {
									         fail=true;
									         assertTrue(fail);
									         TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseNameNotpresentToMatch ");
									         APP_LOGS.debug("ACtual Created Test Case Name is not present to match in Test Case Table (Fail)");
									         comments+="ACtual Created Test Case Name is not present to match in Test Case Table so we can not click on COPY Icon (Fail)";
									         
									         closeBrowser();
									         throw new SkipException("ACtual Created Test Case Name is not present to match in Test Case Table so we can not click on COPY Icon");
								    	  }
								     }
							       
							 
							       
								      	APP_LOGS.debug("Verifying CheckBoxes to select ");							
								      	List<WebElement> listOftestPassesInFlyOut = getObject("TestCaseCopyTC_flyOutTestPassTable").findElements(By.tagName("li"));
								      	System.out.println(listOftestPassesInFlyOut.size());
								      	
								      	
								      	for(int flyoutTPCount=1;flyoutTPCount<=listOftestPassesInFlyOut.size();flyoutTPCount++)
								      	{
								      		String actualFlyoutTPName = getObject("TestCaseCopyTC_testPassNameExtractXpath1", "TestCaseCopyTC_testPassNameExtractXpath2", flyoutTPCount).getText();
								      		System.out.println("Name of test pass present in Copy Test Pass Fly out :" +actualFlyoutTPName);
									
								      		String selectedTPTextFromUpperRibbon =getObject("TestCases_testPassMemberSelected").getAttribute("title");
								      		System.out.println("From Upper Ribbon test pass selected title is : " + selectedTPTextFromUpperRibbon);
								      		if(actualFlyoutTPName.equals(selectedTPTextFromUpperRibbon))
								      		{
								      			APP_LOGS.debug("Test Passes name matched, hence identifying the checkbox is enabled and checked");
										
								      			String testpassCheckboxMode =eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).getAttribute("disabled");
								      			System.out.println("The checkbox of Test Pass : " +actualFlyoutTPName + " is in :" +testpassCheckboxMode+ "mode");
									
								      			if(assertTrue(testpassCheckboxMode.equalsIgnoreCase("True")))
								      			{
								      				APP_LOGS.debug(TestCaseName +" is  belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" + testpassCheckboxMode +"checked and disabled");
											
								      			}
								      		}
								      		else
								      		{
								      			System.out.println("In Else block : "+actualFlyoutTPName);
								      			APP_LOGS.debug(TestCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" +testpassCheckboxMode+ "unchecked and enaabled");
								      			Thread.sleep(500);
							         
								      			APP_LOGS.debug("Selecting checkbox of Test Pass : " + actualFlyoutTPName + "whose name is not matched with teh upper ribbon selcetd Test Pass");
								      			eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).click();
							         
								      			APP_LOGS.debug("After seelcting checkbox of other Test Pass :" +actualFlyoutTPName+ "clicking on flyout OK button");
								      			getElement("TestCaseCopyTC_flyOutOkBtn_Id").click();
							         
								      			APP_LOGS.debug("Test Case succesfully copied to TP : "+actualFlyoutTPName);
								      			getObject("TestCaseCopyTC_copyTCSuccessPopUpOkBtn").click();         
								      			break;
								      		}
								      }
								      	
							       
							       //Verification of Copy Test Step should get Delete
								      	APP_LOGS.debug("Verification of Copy Test Step is successful");
								       try
								       { 
							                    
							                 //Implementation of Test Case 
							                 
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
							    	  assertTrue(fail);
							    	  t.printStackTrace();
							    	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DropDownSelectionFailure");
							    	  comments="DropDown Selection Failure Occurring While Creating Test case(Fail)";
							    	  APP_LOGS.debug("DropDown Selection Failure Occurring While Creating Test case(Fail)");
				             
				                  }
							       
							       
							       //("Verification Of Test steps Name With the Actual created Test Step to perform the deletion Operation ");
							       APP_LOGS.debug("Verification Of Test steps Name With the Actual created Test Step to perform the deletion Operation ");
							       List<WebElement> listofCopiedTestStep =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
							       System.out.println("The number of Test Steps rows present in Test Pass :" +TestPassName2+ "is :" +listofCopiedTestStep.size());
							       
							       for(testStepCount=1;testStepCount<=listofCopiedTestStep.size();testStepCount++)
							       {
							    	   actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
							    	   System.out.println("The name of test step present in Test Pass :" +TestPassName2+ " is :" +actualTestStepName);
							       
							    	   if(compareStrings(actualTestStepName, TestStepExpectedDetails))
							    	   {
							    		   System.out.println("Inside IF to delete the test step of Test Pass: " +TestPassName2);
							    		   APP_LOGS.debug("The name of test steps are matched , hence clicking on Delete icon");
							    		   getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepDeleteIconXpath2", testStepCount).click();
							    		
							    		   APP_LOGS.debug("Clicking on Delete button from Confirmation message");
							    			eventfiringdriver.findElement(By.xpath("//div[@id='divConfirm']/following-sibling::div[1]/div/button[1]")).click();
											
											 String actualDeleteSuccessMessage=eventfiringdriver.findElement(By.id("divAlert")).getText();
										 	 APP_LOGS.debug("The delete success message is having text : " +actualDeleteSuccessMessage);
										 	
										 	if(compareStrings(actualDeleteSuccessMessage, expectedDeleteSuccessMessage))
											{
												System.out.println("The text from Delete Success message is matched with the expected : " +actualDeleteSuccessMessage);
												APP_LOGS.debug("The text from Delete Success message is matched with the expected : " +actualDeleteSuccessMessage);
												
												//Clicking on OK button
												getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
											}
							    		   
							    		   APP_LOGS.debug("Test Step in Test Pass :" +TestPassName2+ "is deleted succesfully");
							    	   }
							    	   else
							    	   {
							    		   APP_LOGS.debug("Name of test step is not matcehd with the actual name of test step hence not click on Delete icon");
							    		   comments="Name of test step is not matcehd with the actual name of test step hence not click on Delete icon";
							    	   }
							       }
							        
							       
							       //"Verification Of Test steps Name With the Actual created Test Step is still present even copied Test Steps Deleted ");
						    	   APP_LOGS.debug("Verification Of Test steps Name With the Actual created Test Step is still present even copied Test Steps Deleted ");
						    	   
						    	   //Verification of original Test Pass is having still the same name of test step
						    	   dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
						    	   
						    	   dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
				               
						    	   dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
				             	 
						    	   dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
				               
						    	   dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName1);
				               
					              listofCopiedTestStep =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
					              System.out.println("The number of Test Steps rows present in Test Pass :" +TestPassName2+ "is :" +listofCopiedTestStep.size());
								  for(testStepCount=1;testStepCount<=listofCopiedTestStep.size();testStepCount++)
								  {
										//String actualTestStepName=eventfiringdriver.findElement(By.xpath("//tbody[@id='showTestSteps']/tr["+testStepCount+"]/td[2]")).getText();
										actualTestStepName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestStepNameXpath2", testStepCount).getText();
										System.out.println("The name of test step present in Test Pass :" +TestPassName2+ " is :" +actualTestStepName);
									
										String actualTestCaseName=getObject("TestStepViewAll_TestStepNameXpath1", "TestStepViewAll_TestCaseNameXpath2", testStepCount).getText();
										System.out.println("The name of test step present in Test Pass :" +TestPassName2+ " is :" +actualTestCaseName);
								  
										if(compareStrings(actualTestStepName, TestStepExpectedDetails)&&compareStrings(actualTestCaseName, TestCaseName))
										{
											System.out.println("The name of Test Step is as it is in Test Pass: " +TestPassName1);
											APP_LOGS.debug("The name of Test Step is as it is in Test Pass: " +TestPassName1);
										}
								  }
										  
									  Thread.sleep(2000);
							       
												
								}
								catch(Throwable t)
								{
									
								}
						
						closeBrowser();
					    }
							else 
							{
								fail= true;
								APP_LOGS.debug("Test Manager Login Not Successful");
								System.out.println("Test Manager Login Not Successful");						
							}
						}
						
						
						else 
						{
							fail= true;
							APP_LOGS.debug("Version Lead Login Not Successful");
							System.out.println("Version Lead Login Not Successful");
							
						}
						
					}	
					catch (Throwable e) 
					{
						e.printStackTrace();
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
						comments+="Skip Exception or other exception occured" ;
					}
				}
				else 
				{
					isLoginSuccess=false;
					APP_LOGS.debug("Login Not Successful");
					System.out.println("Login Not Successful");
					
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
			public void reportTestResult(){
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testCasesSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testCasesSuiteXls,this.getClass().getSimpleName()), "FAIL");
			
			}
			
			@DataProvider
			public Object[][] getTestData(){
				return TestUtil.getData(TM_testCasesSuiteXls, this.getClass().getSimpleName()) ;
			}
}

