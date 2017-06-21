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
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class CopyTCAlongWdTSToOthrTPDiffRole extends TestSuiteBase{
	
	

	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	String  testpassCheckboxMode;
	boolean result = false;
	// Runmode of test case in a suite
	Utility utilRecorder = new Utility();
		@BeforeTest
		public void checkTestSkip() throws Exception{
			APP_LOGS.debug(" Executing CopyTCAlongWdTSToOthrTPDiffRole Test Case");
			
			if(!TestUtil.isTestCaseRunnable(TM_testCasesSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_testCasesSuiteXls, this.getClass().getSimpleName());
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		@Test(dataProvider="getTestData")
		public void Test_CopyTCAlongWdTSToOthrTPDiffRole(String Role, String GroupName, String Portfolio, String ProjectName, String Version, 
				String endMonth,String endYear, String endDate, String VersionLead,String testPassName1, String testManager,
				String TP_endMonth, String TP_endYear, String TP_endDate,String testPassName2, String testers, String testerRole1,
				String testerRole2, String testerRole3, String testCaseName, String testStepName, String testStepExpectedResult, String ExpectedSuccessfulCopyMessage)
				throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

			}
			
			APP_LOGS.debug("Opening Browser... ");
			openBrowser();

			isLoginSuccess = login(Role);
			
			if(isLoginSuccess){
				
				//version lead
				 int versionlead_count = Integer.parseInt(VersionLead);
				 versionLead=new ArrayList<Credentials>();
				 versionLead = getUsers("Version Lead", versionlead_count);
				 
				//TestManager 
				 int testManager_count = Integer.parseInt(testManager);
				 test_Manager=new ArrayList<Credentials>();
				 test_Manager = getUsers("Test Manager", testManager_count);
				 
				 //Tester 
				 int tester_count = Integer.parseInt(testers);
				 tester=new ArrayList<Credentials>();
				 tester = getUsers("Tester", tester_count);
				 
				// clicking on Test Management Tab
				getElement("UAT_testManagement_Id").click();
				APP_LOGS.debug(" Test Management tab clicked ");
				Thread.sleep(2000);
				
					if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
						comments=ProjectName+" Project not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullProjectCreation");
						closeBrowser();

						throw new SkipException("Project not created successfully");
					}
					APP_LOGS.debug(ProjectName+" Project Created Successfully.");
					comments=ProjectName+" Project Created Successfully(Pass). ";
				
					closeBrowser();
					APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");

					APP_LOGS.debug("Opening Browser... ");
					openBrowser();
					
					login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead");
		            APP_LOGS.debug("Logged in browser with Version Lead");
		            
					//click on testManagement tab
					APP_LOGS.debug(" Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(3000);
					
					// creating test pass,test case,testers and test step
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully.");
						comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass1Creation");
						closeBrowser();

						throw new SkipException("Test Pass 1 not created successfully");
					}
					APP_LOGS.debug(testPassName1+" Test Pass  Created Successfully.");
					comments+=testPassName1+" Test Pass  Created Successfully(Pass). ";
					
					if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
					{
						fail=true;
						APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully.");
						comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPass2Creation");
						closeBrowser();

						throw new SkipException("Test Pass 2 not created successfully");
					}
					APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");
					comments+=testPassName2+" Test Pass Created Successfully(Pass). ";
					
					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName1, tester.get(0).username, testerRole1, testerRole1))
					{
						fail=true;
						APP_LOGS.debug("Tester 1 with role:"+ testerRole1+" not Created Successfully.");
						comments+="Tester 1 with role:"+ testerRole1+" not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
						closeBrowser();

						throw new SkipException("Tester 1 not created successfully");
					}
					APP_LOGS.debug(" Tester 1 with role:"+ testerRole1+" Created Successfully.");
					comments+=" Tester 1 with role:"+ testerRole1+" Created Successfully(Pass). ";
					
					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName1, tester.get(1).username, testerRole2, testerRole2))
					{
						fail=true;
						APP_LOGS.debug("Tester 2 with role:"+ testerRole2+" not Created Successfully.");
						comments+="Tester 2 with role:"+ testerRole2+" not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester2Creation");
						closeBrowser();

						throw new SkipException("Tester 2 not created successfully");
					}
					APP_LOGS.debug(" Tester 2 with role:"+ testerRole2+" Created Successfully.");
					comments+=" Tester 2 with role:"+ testerRole2+" Created Successfully(Pass). ";
					
					if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName2, tester.get(2).username, testerRole3, testerRole3))
					{
						fail=true;
						APP_LOGS.debug("Tester 3 with role:"+ testerRole3+" not Created Successfully.");
						comments+="Tester 3 with role:"+ testerRole3+" not Created Successfully(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester3Creation");
						closeBrowser();

						throw new SkipException("Tester 3 not created successfully");
					}
					APP_LOGS.debug(" Tester 3 with role:"+ testerRole3+"Created Successfully.");
					comments+=" Tester 3 with role:"+ testerRole3+" Created Successfully(Pass). ";
					
					closeBrowser();
	 		    	APP_LOGS.debug(" Closed Browser after verifying if Test Pass,Tester,TestCase was existing and creating if not existing ");

					APP_LOGS.debug("Opening Browser... ");
	 				openBrowser();
	 				
		 				try{
			 				login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager");
			 		
			 	            APP_LOGS.debug("Logged in browser with Test Manager");
			 	            
							//click on testManagement tab
			 				APP_LOGS.debug(" Clicking on Test Management Tab");
			 				getElement("UAT_testManagement_Id").click();
			 				Thread.sleep(3000);
			
			 				if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName1, testCaseName))
							{
								fail=true;
								APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
								comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCaseCreation");
								closeBrowser();
		
								throw new SkipException("Test Case not created successfully");
							}
							APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
							comments+=testCaseName+" Test Case Created Successfully(Pass). ";
							
							if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName1, testStepName, testStepExpectedResult, testCaseName, testerRole2))
							   {
							    	fail=true;
							    	APP_LOGS.debug(testStepName+": Test Step is not saved Successfully ");
							    	comments+="TestStep not saved successfully(Fail)";
							    	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UnsuccessfulTestStepCreation");
		
							   }
							   	APP_LOGS.debug(testStepName+": Test Step is Saved Successfully ");
								comments+=testStepName+" Test Step Created Successfully(Pass). ";
								
								APP_LOGS.debug(" Clicking on Test Case Tab");
				 				getElement("TM_testCasesTab_Id").click();
				 				Thread.sleep(3000);
				 				
				 				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), GroupName );
								dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), Portfolio );
								dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), ProjectName );
								dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), Version );
								dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName1 );

								//Click on COPY link
								getElement("TestCases_CopyTestCasesLink_Id").click();
								
								//Click on COPY Icon of TC 						
								List<WebElement> listofCopyTestCases = getObject("TestCaseCopyTC_copyTestCaseTable").findElements(By.tagName("tr"));
								String actualtestCaseName;
								
								for(int i=1;i<=listofCopyTestCases.size();i++)
								{
									actualtestCaseName = getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyTestCaseNameExtractXpath2", i).getText();
									
									if(assertTrue(actualtestCaseName.equals(testCaseName)))
									{
										APP_LOGS.debug("Test Case name matched, hence clicking on Copy Icon");	
										comments+="Test Case name matched, hence clicking on Copy Icon (Pass).";
										getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyIconXpath2", i).click();								
									}
									else
									{
										fail=true;
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CopyTestCaseError_TCs_CopyToTPNotHavingTester ");
										APP_LOGS.debug("Test Case Name not matched hence not clicked on Copy icon");
										comments+="Test Case Name not matched hence not clicked on Copy icon( Fail).";
										closeBrowser();
										throw new SkipException("Test Case Name not matched hence not clicked on Copy icon");
									}
								}
								
								
								//Clicking on Test pass2 to copy the test case of Test Pass1
								//Verification of checkboxes							
								List<WebElement> listOftestPassesInFlyOut = getObject("TestCaseCopyTC_flyOutTestPassTable").findElements(By.tagName("li"));
								
								for(int flyoutTPCount=1;flyoutTPCount<=listOftestPassesInFlyOut.size();flyoutTPCount++)
								{
									String actualFlyoutTPName = getObject("TestCaseCopyTC_testPassNameExtractXpath1", "TestCaseCopyTC_testPassNameExtractXpath2", flyoutTPCount).getText();
									
									String selectedTPTextFromUpperRibbon =getObject("TestCases_testPassMemberSelected").getAttribute("title");
									
									if(actualFlyoutTPName.equals(selectedTPTextFromUpperRibbon))
								    {
								         APP_LOGS.debug("Test Passes name matched, hence identifying the checkbox is enabled and checked");
								         
								         testpassCheckboxMode =eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).getAttribute("disabled");
								        
								         if(assertTrue(testpassCheckboxMode.equalsIgnoreCase("True")))
								         {
								        	 APP_LOGS.debug(testCaseName +" is  belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" + testpassCheckboxMode +"checked and disabled");
								          
								         }
								    }
									else
								    {
								         APP_LOGS.debug(testCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" +testpassCheckboxMode+ "unchecked and enaabled");
								     
								         APP_LOGS.debug("Selecting checkbox of Test Pass : " + actualFlyoutTPName + "whose name is not matched with teh upper ribbon selcetd Test Pass");
								         eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).click();
								          
								          APP_LOGS.debug("After seelcting checkbox of other Test Pass :" +actualFlyoutTPName+ "clicking on flyout OK button");
								          getElement("TestCaseCopyTC_flyOutOkBtn_Id").click();
								          
								          //After successful copying of Test case retrieving the message of the pop up
								          int numOfCharsinLegend = getObject("TestCaseCopyTC_copyTCSuccessPopUpText1").getText().length();
								          String actualTPCopyText = getObject("TestCaseCopyTC_copyTCSuccessPopUpText2").getText().substring(numOfCharsinLegend); 
								         
								          String expectedTPCopyText="In the ("+testPassName2+") there is no tester available with the role ("+testerRole2+"). Hence ("+tester.get(0).username.replace(".", " ")+") with the role ("+testerRole2+") is also copied from ("+testPassName1+") to ("+testPassName2+").";
								         
								          if(actualTPCopyText.equalsIgnoreCase(expectedTPCopyText))  
								          {
								        	  APP_LOGS.debug("Test Case succesfully copied to TP : "+actualFlyoutTPName);
								        	  comments+="Test Case succesfully copied to TP : "+actualFlyoutTPName+" (Pass).";
								        	  getObject("TestCaseCopyTC_copyTCSuccessPopUpOkBtn").click();
								        	  break;
								          }          
								          
								          else{
								        	  fail=true;
								        	  APP_LOGS.debug("Test Case copied message does not match");
								        	  comments+="Test Case copied message does not match (Fail).";
								        	  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "IncorrectTestCaseCopiedMessage");
								          }
								      }
								        
								}
				 		
								//Verification of test case from TP1 is copied in TP2
								//Click on View link 
								getObject("TestCases_viewAllTestCasesLink").click();
								dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), GroupName );
								dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), Portfolio );
								dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), ProjectName );
								dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), Version );
								dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName2 );

								
								List<WebElement> listtestCaseName = getObject("TestCaseViewAll_Table").findElements(By.tagName("tr"));
								for(int i =1;i<=listtestCaseName.size();i++)
								{
								
							//		String actualTestCaseName = eventfiringdriver.findElement(By.xpath("//tbody[@id='showTestCases']/tr["+i+"]/td[2]/span")).getText();
									String actualTestCaseName= getObject("TestCase_viewAllTestCaseNameExtractXpath1", "TestCase_viewAllTestCaseNameExtractXpath2", i).getText();
									if(compareStrings(actualTestCaseName, testCaseName))
									{
										APP_LOGS.debug("Test Cases name matched hence verified test case from  :" +testPassName1 +  " is copied into : " +testPassName2);
										comments+="Test Cases name matched hence verified test case from  :" +testPassName1 +  " is copied into : " +testPassName2+" (Pass).";
									}
									else{
										fail=true;
										APP_LOGS.debug("Test Cases name not matched and test case from  :" +testPassName1 +  " is not copied into : " +testPassName2);
										comments+="Test Cases name not matched and test case from  :" +testPassName1 +  " is not copied into : " +testPassName2+" (Fail).";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseNotCopied");
									}
								}
								//Verification of Test Step of TC1 from TP1 is also copied to TP2 with the same role
								
								//clicking on TestStep tab
								getElement("TM_testStepsTab_Id").click();
								APP_LOGS.debug(" TestStep tab clicked ");
								Thread.sleep(2000);
								
								dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), GroupName );
								dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), Portfolio );
								dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), ProjectName );
								dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), Version );
								dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName2 );
								
								List<WebElement> listtestStepName =getObject("TestStepViewAll_Table").findElements(By.tagName("tr"));
								for(int i =1;i<=listtestStepName.size();i++)
								{
									String actualTestStepName = getObject("TestStepViewAll_TestStepNameXpath1","TestStepViewAll_TestStepNameXpath2",i).getText();
									if(compareStrings(actualTestStepName, testStepName))
									{
										APP_LOGS.debug("Test Steps name matched hence verified test step from  :" +testPassName1 +  " is copied into : " +testPassName2);
										comments+="Test Steps name matched hence verified test step from  :" +testPassName1 +  " is copied into : " +testPassName2+" (Pass).";
									}
									else{
										fail=true;
										APP_LOGS.debug("Test Steps name not matched and test step from  :" +testPassName1 +  " is not copied into : " +testPassName2);
										comments+="Test Steps name not matched and test step from  :" +testPassName1 +  " is not copied into : " +testPassName2+" (Fail).";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepNotCopied");
									}
								}
								
								//Verification of Tester from TP1 with Role1 is also copied in TP2 as it is not having tester with that role
								//clicking on Tester tab
								getElement("TM_testersTab_Id").click();
								APP_LOGS.debug(" Tester tab clicked ");
								Thread.sleep(2000);
								
								dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), GroupName );
								dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), Portfolio );
								dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), ProjectName );
								dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), Version );
								dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName2 );
								
								List<WebElement> listtesters =getObject("TesterViewAll_Table").findElements(By.tagName("tr"));
								for(int i =1;i<=listtesters.size();i++)
								{
									String actualTesterName =getObject("Tester_TesterNameExtract1", "TestStepViewAll_TestStepNameXpath2",i).getText();	
									String actualTesterRole= getObject("Tester_TesterNameExtract1", "TestStepViewAll_TestCaseNameXpath2", i).getText();											
									if((actualTesterName.equalsIgnoreCase(tester.get(0).username.replace(".", " ")))&&(actualTesterRole.equalsIgnoreCase(testerRole2)))
									{
									  result = true;
									  break;
									}
								}
								
								if(assertTrue(result))
								{
									APP_LOGS.debug("Tester name and role matched hence verified tester from  :" +testPassName1 +  " is copied into : " +testPassName2);
									comments+="Tester name and role matched hence verified tester from  :" +testPassName1 +  " is copied into : " +testPassName2+" (Pass).";
									
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Test Steps name and role not matched and test step from  :" +testPassName1 +  " is not copied into : " +testPassName2);
									comments+="Test Steps name and role not matched and test step from  :" +testPassName1 +  " is not copied into : " +testPassName2+" (Fail).";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepNotCopied");
								
								}
								
		 			}
			 		catch(Throwable t)
		            {
		         	 fail=true;
		           	 t.printStackTrace();
		           	 comments="Failed Login for Role" +Role;
		           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
		            }
					APP_LOGS.debug("Closing Browser... ");
				    closeBrowser();	
			 			
			}
			else 
			{
				APP_LOGS.debug("Login Not Successful");
			}	
		}
		
		@AfterMethod
		public void reportDataSetResult(){
			if(skip)
			{
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
				TestUtil.printComments(TM_testCasesSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			}
			else{
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
}
