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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;


@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class TCs_CopyToTPNotHavingTester extends TestSuiteBase {
	
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	public String comments;
	String testpassCheckboxMode;
	
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip()
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
	}

	// Test Case Implementation ...			
	@Test(dataProvider="getTestData")
	public void testTCs_CopyToTPNotHavingTester 
		(
			String Role,String GroupName,String PortfolioName,String ProjectName, String Version, 
			String EndMonth, String EndYear, String EndDate,String VersionLead,
			String TestPassName1,String TestPassName2,String TestManager,String TP_EndMonth,String TP_EndYear,String TP_EndDate,
			String Tester,String AddRole,String TestCaseName, String ExpectedNoTesterText
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
		openBrowser();
		
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
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure_CopyToTPNotHavingTester");
					comments="Project Creation Unsuccessful(Fail) by "+Role+". ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
					closeBrowser();
					throw new SkipException("Project Creation Unsuccessfull");
				}
				
				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();
				
				APP_LOGS.debug("Opening Browser...");
				openBrowser();
				
				APP_LOGS.debug("Logging In With Role Version Lead "+versionLead.get(0).username + "of PROJECT :" +ProjectName+ " to create Test Pass :" + TestPassName1);
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
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure_CopyToTPNotHavingTester");
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
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure_CopyToTPNotHavingTester");
						comments="Test Pass Creation Unsuccessful(Fail)" ;
						APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
						closeBrowser();
						throw new SkipException("Test Pass Creation Unsuccessfull");
					}
					
					APP_LOGS.debug("Closing Browser... ");
					closeBrowser();
					
					APP_LOGS.debug("Opening Browser...");
					openBrowser();
					
					APP_LOGS.debug("Logging In With Role Test Manager "+testManager.get(0).username + " to create Tester : " + testers.get(0).username + "with role :"+AddRole);
					if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
					{ 
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						//Tester1 with AddRole1 creation for Test Pass1							
						APP_LOGS.debug("Creation of Tester : " +testers.get(0).username + "with role : " +AddRole);
						if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, testers.get(0).username,AddRole, AddRole)) 
						{	
							fail=true;
							assertTrue(fail);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure_CopyToTPNotHavingTester");
							comments="Tester Creation Unsuccessful(Fail)" ;
							APP_LOGS.debug("Tester Creation Unsuccessfull");
							closeBrowser();
							throw new SkipException("Tester Creation Unsuccessfull");
						}
						
						APP_LOGS.debug("Test Manager "+testManager.get(0).username + " creating  Test Cases :" +TestCaseName);						
						if (!createTestCase(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestCaseName))
						{	
							fail=true;
							assertTrue(fail);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationFailure_CopyToTPNotHavingTester");
							comments="Tester Creation Unsuccessful(Fail)" ;
							APP_LOGS.debug("Test Case Creation Unsuccessfull");
							closeBrowser();
							throw new SkipException("Test Case Creation Unsuccessfull");
					
						}
						
						//Implementatuion of Test Case						
						//Click on View link 
						APP_LOGS.debug("Clicking On View All Link");
						getObject("TestCases_viewAllTestCasesLink").click();
						
						//Click on COPY link
						APP_LOGS.debug("Clicking On COPY Link");
						getElement("TestCases_CopyTestCasesLink_Id").click();
						
						//Click on COPY Icon of TC 						
						APP_LOGS.debug("Verifiication of actual Test Case is displaying in Copy Test Case table of : " +TestPassName1);
						List<WebElement> listofCopyTestCases = getObject("TestCaseCopyTC_copyTestCaseTable").findElements(By.tagName("tr"));
						String actualtestCaseName;
						
						for(int i=1;i<=listofCopyTestCases.size();i++)
						{
							actualtestCaseName = getObject("TestCaseCopyTC_copyTestCaseNameExtractXpath1", "TestCaseCopyTC_copyTestCaseNameExtractXpath2", i).getText();
							System.out.println("Name of test cases present in Test Case Table :" +actualtestCaseName);
							
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
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "CopyTestCaseError_CopyToTPNotHavingTester");
								APP_LOGS.debug("Test Case Name not matched hence not clicked on Copy icon :Fail");
								closeBrowser();
								throw new SkipException("Test Case Name not matched hence not clicked on Copy icon:Fail");
							}
						}
						
						//Verification of checkboxes
						APP_LOGS.debug("Verification of Fly Out Test Passes name and their checkboxes mode");
						List<WebElement> listOftestPassesInFlyOut = getObject("TestCaseCopyTC_flyOutTestPassTable").findElements(By.tagName("li"));
						System.out.println(listOftestPassesInFlyOut.size());
						for(int flyoutTPCount=1;flyoutTPCount<=listOftestPassesInFlyOut.size();flyoutTPCount++)
						{
							APP_LOGS.debug("Extracting Test Passes name from Copy test Case Fly Out");
							String actualFlyoutTPName = getObject("TestCaseCopyTC_testPassNameExtractXpath1", "TestCaseCopyTC_testPassNameExtractXpath2", flyoutTPCount).getText();
							System.out.println("Name of test pass present in Copy Test Pass Fly out :" +actualFlyoutTPName);
							
							APP_LOGS.debug("Extract name of Test Pass selcetd in Upper ribbon drop down");
							String selectedTPTextFromUpperRibbon =getObject("TestCases_testPassMemberSelected").getAttribute("title");
							System.out.println("From Upper Ribbon test pass selected title is : " + selectedTPTextFromUpperRibbon);
							
							//Verification of Upper ribbon selecetd test pass name with the name of Test Passses name present in copy fly out 
							APP_LOGS.debug("Verification of Upper ribbon selecetd test pass name with the name of Test Passses name present in copy fly out ");
							if(actualFlyoutTPName.equals(selectedTPTextFromUpperRibbon))
							{
								APP_LOGS.debug("Test Passes name matched, hence identifying the checkbox is enabled and checked");
								testpassCheckboxMode =eventfiringdriver.findElement(By.xpath("//div[@id='testPassSelectDiv']//ul[@id='ulItemstestPassSelectDiv']/div/li[@title='"+actualFlyoutTPName+"']/input")).getAttribute("disabled");
								System.out.println("The checkbox of Test Pass : " +actualFlyoutTPName + " is in :" +testpassCheckboxMode+ "mode");
							
								APP_LOGS.debug("Verifying the checkbox mode of Upper Ribbon selecetd Test Pass and Fly Out Test Pass");
								if(assertTrue(testpassCheckboxMode.equalsIgnoreCase("True")))
								{
									APP_LOGS.debug(TestCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" + testpassCheckboxMode +"checked and disabled");
								}
								
							}
							else
							{
								APP_LOGS.debug(TestCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" +testpassCheckboxMode+ "unchecked and disabled");
								APP_LOGS.debug("Redirecting to TESTERS page, for verification of Test pass :"+TestPassName2+" is not having tester");
								System.out.println("The checkbox of Test Pass : " +actualFlyoutTPName+ "is in :" +testpassCheckboxMode);	
								 
								APP_LOGS.debug("Verifying the checkbox mode of Test Pass whose name is not matched with the Upper Ribbon selected test pass name");
								if(compareStrings(testpassCheckboxMode, null))
								{
									APP_LOGS.debug(TestCaseName +" is not belonging to test pass :" + actualFlyoutTPName + "having checkbox mode as :" + testpassCheckboxMode +"checked and disabled");
								
								}
							}
						}
						
							//Verification of Test Pass2 is not having text as "No Tester Available"
							APP_LOGS.debug(" Verification of Test Pass2 is not having text as \"No Tester Available\"");
							try{							
								Thread.sleep(2000);	
								
								APP_LOGS.debug("Clicking on Copy link after test step creation in : " +TestCaseName+ " of " +TestPassName1);
								
								getElement("TesterNavigation_Id").click();
								
								dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
								
								dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
								
								dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
								
								dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
								
								dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName2 );
							
						      int testerTableAvailableCount = getObject("TestersViewAll_testerAvailable").findElements(By.tagName("h2")).size();
						      
							      
						      //verifying whether table is available or not
						      if (testerTableAvailableCount==1) {
						       
						       
						    	  if(!compareStrings(getObject("TestersViewAll_withoutTester").getText(), ExpectedNoTesterText))
							       {
							        fail=true;
							        APP_LOGS.debug("No Tester(s) Available! text is not found");
							        comments=comments+"No Tester(s) Available! (FAIL)"; 
							        
							       }
							       APP_LOGS.debug("No Tester(s) Available!");
							       comments=comments+"No Tester(s) Available! (PASS)"; 
						      }
						      else{
						          
						          //Tester is already available total count is displayed
						          if(!assertTrue(getObject("TestersViewAll_totalRecordCount").isDisplayed())){
						           fail=true;
						           APP_LOGS.debug("Total Record Count is not being displayed");
						           comments=comments+"Total Record Count is not being displayed (FAIL)";
						          }
						          APP_LOGS.debug("Total Records: "+getObject("TestersViewAll_totalRecordCount").getText());
						          comments=comments+"Total Record Count is being displayed (PASS)";
						          
						          }
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
			
				APP_LOGS.debug("Closing Browser... ");
				
				System.out.println("Closing Browser... ");
	
				closeBrowser();
				
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
	
	
	public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
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
					
					Thread.sleep(2000);
					
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
		
	}

}