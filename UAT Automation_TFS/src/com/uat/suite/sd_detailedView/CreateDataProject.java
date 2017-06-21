package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

public class CreateDataProject extends TestSuiteBase
{

	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholderAndVersionLead;
	ArrayList<Credentials> testManager;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());		
		
		if(!TestUtil.isTestCaseRunnable(SD_detailedViewXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(SD_detailedViewXls, this.getClass().getSimpleName());
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}

	@Test(dataProvider="getTestData")
	public void testCreateDataProject(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
				String VersionLead,String Stakeholder, String TestPassName1,String TP_TestManager, String tester_testerName,String tester_Role1, 
				String TP1_TestCaseName1, String TP1_TC1_TestStepName1,String TP1_TC1_TestStepName2,String TP1_TC1_TestStepName3,
				String TP1_TC1_TestStepName4, String TestStepExpectedResult,String AssignedRole1 ) throws Exception
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
		
		int stakeholder_count = Integer.parseInt(Stakeholder);		
		stakeholderAndVersionLead = getUsers("Stakeholder+Version Lead", stakeholder_count);
		
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
			
				
				getObject("Projects_createNewProjectLink").click();
					
				
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(Version);
				//getElement("Version").sendKeys(version);
				
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date date = new Date();
				String requiredDate = dateFormat.format(addYearsInSysdate(date, 2));
				
				String[] dateMonthYear = requiredDate.split("/");
				String endMonth = dateMonthYear[0];
				String endDate = dateMonthYear[1];
				String endYear = dateMonthYear[2];
				
				selectStartOrEndDate2(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
				
				getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
				   
				Thread.sleep(500);driver.switchTo().frame(1);
				
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead.get(0).username);
				   
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		   
				waitForElementVisibility("ProjectCreateNew_versionLeadStakeholderSelectSearchResult",10).click();
			
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		   
				driver.switchTo().defaultContent();
				
				APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");		
				getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
					
				//Switching to the people picker frame 
				Thread.sleep(500);driver.switchTo().frame(1);
				APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
					
				//Input test data in people picker text field
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").clear();
				
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(stakeholderAndVersionLead.get(0).username); 
			
				//Clicking on Search button 
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();

				//Selecting search result 
				getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
				
				//Clicking on add button from Version Stakeholder people picker frame
				getObject("ProjectCreateNew_StakeholderAddBtn").click();
			
				//Clicking on OK button from People Picker
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			
				//Switching back to the default content 
				driver.switchTo().defaultContent();
				
				getObject("ProjectCreateNew_projectSaveBtn").click();
				
				APP_LOGS.debug(ProjectName+" created successfully. ");
			
				
				//Added 1 month in sysdate for a Active Test Pass
				
				requiredDate = dateFormat.format(addMonthInSysdate(date, 2));
				
				dateMonthYear = null;
				dateMonthYear = requiredDate.split("/");
				endMonth = dateMonthYear[0];
				endDate = dateMonthYear[1];
				endYear = dateMonthYear[2];
				
				//create test pass for Active
				if(!createTestPass1(GroupName, PortfolioName, ProjectName, Version, TestPassName1, endMonth, endYear, endDate, testManager.get(0).username))
           	 
				{
	       		     fail=true;
	       		    
	       		     APP_LOGS.debug(TestPassName1+" not created successfully. ");
	                  	 
	       		     comments+="Fail Occurred:- "+TestPassName1+" Not created successfully. ";
						 
	       		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	   					 
	          		 throw new SkipException("Test Pass1 is not created successfully... So Skipping all tests");
	           	 }
	       		 
	           	 APP_LOGS.debug(TestPassName1+" created successfully. ");
				
	           	 
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
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName2,TestStepExpectedResult,
							TP1_TestCaseName1, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC1_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TC1_TestStepName2+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP1_TC1_TestStepName2+ "Test Step created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ");
					}
					
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName3,TestStepExpectedResult,
							TP1_TestCaseName1, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC1_TestStepName3+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TC1_TestStepName3+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP1_TC1_TestStepName3+ "Test Step created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ");
					}
					
					if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TP1_TC1_TestStepName4,TestStepExpectedResult,
							TP1_TestCaseName1, AssignedRole1))
					{
						 fail=true;
						
						 APP_LOGS.debug(TP1_TC1_TestStepName4+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1);
						
						 comments+="Fail Occurred:- "+TP1_TC1_TestStepName4+ "Test Step not created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ";
	 					
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
	 					 
	        			 throw new SkipException("TP1_TC1_Test Step1 is not created successfully ... So Skipping all tests");
					}
					else 
					{
						APP_LOGS.debug(TP1_TC1_TestStepName4+ "Test Step created successfully under Test Case "+TP1_TestCaseName1+ " for test pass "+TestPassName1+". ");
					}
					
					
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
							
							APP_LOGS.debug("Clicking on Dashboard tab button as Testing is Complete(Keeping two test step as NC)");	
							
							getElement("UAT_dashboard_Id").click();								
							Thread.sleep(1000);
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
			
			//closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);
			comments+="Login Unsuccessful for Tester "+Role;
		}	
	}
		
	
	
	//functions
	
	private Date addMonthInSysdate(Date date, int numberOfMonthsToBeAdded) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(date.getTime());
	    cal.add(Calendar.MONTH, numberOfMonthsToBeAdded);
	    return new Date(cal.getTimeInMillis());
	}
	
	private Date addYearsInSysdate(Date date, int numberOfYearsToBeAdded) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(date.getTime());
	    cal.add(Calendar.YEAR, numberOfYearsToBeAdded);
	    return new Date(cal.getTimeInMillis());
	}
	
	private String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
	
	private void selectStartOrEndDate2(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
	 {
		
			try
			{
					//Select start date   
					WebElement startDateImage = calendarImg;  
	   
					APP_LOGS.debug("Clicking on Date Calendar icon...");
	   
					startDateImage.click();  
					Thread.sleep(1000);
	   
					Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
	   
					year.selectByValue(StartEndYear);
	   
					APP_LOGS.debug(StartEndYear +" : Year is selected...");
	   
					Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
	   
					month.selectByValue(Integer.valueOf(Integer.parseInt(StartEndMonth)-1).toString());
	   
					APP_LOGS.debug(StartEndMonth +" : Month is selected...");
	   
					
					WebElement datepicker= getObject("ProjectCreateNew_dateTable");
	   
					//List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
	   
					List<WebElement> cols = datepicker.findElements(By.tagName("td"));
					
					for(WebElement cell :cols)
		   
					{
							if(cell.getText().equals(StartEndDate))
							{
									cell.findElement(By.linkText(""+StartEndDate+"")).click();
	     
									APP_LOGS.debug(StartEndDate +" : Date is selected...");
	     
	     
									break;
							}
					}
			}
		
			catch(Throwable t)
			{
				
				
				t.printStackTrace();
			}
	 }
	
	
	
	private boolean createTestPass1(String group, String portfolio, String project, String version, String testPassName, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2500);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(500);
		getObject("TestPasses_createNewProjectLink").click();
		Thread.sleep(500);
		
		try {
			
			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
		
			Thread.sleep(1000);
			//getElement("Version").sendKeys(version);
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
			
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			   
			Thread.sleep(500);driver.switchTo().frame(1);
			
			//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
			   
			getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
	   
			waitForElementVisibility("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult",10).click();
			
		//  getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
	   
			getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
	   
			driver.switchTo().defaultContent();
			
			selectStartOrEndDate2(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
			
			getObject("TestPassCreateNew_testPassSaveBtn").click();
			
			String testPassCreatedResult=getTextFromAutoHidePopUp();					
			
			if (testPassCreatedResult.contains("successfully")) 
			{
		//		getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
				
				return true;
			}
			else 
			{
				return false;
			}
				
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createTestpass function.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(SD_detailedViewXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);	
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		//skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "SKIP");
		else if(isTestPass)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "FAIL");
		skip=false;
	}
			
}
