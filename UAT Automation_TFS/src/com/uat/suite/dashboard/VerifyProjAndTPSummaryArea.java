package com.uat.suite.dashboard;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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
public class VerifyProjAndTPSummaryArea extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	boolean totalCountVisibility=false;
	int totalPages;
	int projectTestPassSummaryRows;
	boolean groupAndPortfolioMatch=false;
	String actual;
	String groupTestPassSummary;
	//String[] testSteps;
	int countofPortfolio;
 	String projectCell,versionCell,testPassCell,roleCell,actionCell;
	int daysLeft,notCompletedCount,passCount,failCount;
	
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
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();	
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());
	}
			
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void VerifyProjAndTPSummaryArea1(String Role, String GroupName, String Portfolio, String Project, String Version,
			String VersionLead,String EndMonth,String EndYear,String EndDate,String TestPassName, String TestManager,String TP_Tester,
			String TesterRoleCreation,String TesterRoleSelection, String projectAndTestPassColumnHead) throws Exception
	{
		count++;
		comments="";
		
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int tester_count = Integer.parseInt(TP_Tester);
		tester = getUsers("Tester", tester_count);
		
		int testManager_count = Integer.parseInt(TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		openBrowser();
		APP_LOGS.debug("Opening Browser for user "+Role);
		
		isLoginSuccess=login(Role);
		
		if(isLoginSuccess)
		{
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug("Clicking On Test Management Tab.");
			Thread.sleep(3000);		
			
			//Project creation
			if(createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
			{
				APP_LOGS.debug("Project successfully created (PASS).");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Project Creation fails (FAIL).");
				comments+="Project Creation Fails (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project not created successfully");//reports
			}
			
			// Test Pass creation 
			if(createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
			{
				APP_LOGS.debug("First Test Pass successfully created (PASS).");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Test Pass Creation fails (FAIL).");
				comments+="Test Pass Creation Fails (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
			}
			
			// Tester creation 
			if(createTester(GroupName, Portfolio, Project, Version, TestPassName, tester.get(0).username, TesterRoleCreation, TesterRoleSelection))
			{
				APP_LOGS.debug("Tester created successfully for TP I (PASS).");
			}
			else
			{
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester Creation fails for Test pass 1  (FAIL).");
				comments+="Tester Creation Fails for Test pass 1  (FAIL).";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
				closeBrowser();
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Tester is not created for Test pass 1 successfully");//reports
			}
			
			DateFormat dateFormatVerification = new SimpleDateFormat("MM/dd/yyyy");
			Date dateVerification = new Date();
	
			String dateStartVerify=dateFormatVerification.format(dateVerification);
			
			DateFormat dateFormatTPSum = new SimpleDateFormat("MM-dd-yyyy");
			Date dateProjTPSum = new Date();
		
			String dateStartTPSum=dateFormatTPSum.format(dateProjTPSum);
			
			closeBrowser();
					
			APP_LOGS.debug("Opening browser for user "+versionLead.get(0).username);
			openBrowser();
			
			if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
			{
				if(getObject("DashboardProjectTestPassSummaryDiv").isDisplayed())
				{
					if(compareStrings(resourceFileConversion.getProperty("Dashboard_expProjectAndTestPassSummaryText"), getObject("DashboardProjectAndTestPassSummary_Text").getText()))
					{
						APP_LOGS.debug("Project and Test Pass Summary text is matched (PASS).");
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project and Test Pass Summary text is not matched (FAIL).");
						comments+="Project and Test Pass Summary text is not matched (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectTestPassSummaryTextNotMatched");
					}
					
					//verify project test pass summary grid headings
					if(verifyProjectAndTestPassGridColumnHeading(projectAndTestPassColumnHead))
					{
						APP_LOGS.debug("In Project and Test Pass Summary all column headings are matched (PASS).");
						comments+="In Project and Test Pass Summary all column headings are matched (PASS).";
					}
					else
					{
						fail=true;
						APP_LOGS.debug("Project and Test Pass Summary column headings not matched (FAIL).");
						comments+="Project and Test Pass Summary column headings not matched (FAIL). ";
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectAndTestPassSummaryColumnHeadingNotMatch");
					}
					
					//verifying project in Project Test Pass Summary table
					if(projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassName, dateStartTPSum, EndDate, EndYear, EndMonth))
					{
						APP_LOGS.debug("Project in Project and Test Pass Summary is found and verified (PASS).");
						comments+="Project in Project and Test Pass Summary is found and verified (PASS). ";
						
						//when project found successful click on the Project Name Link						
					    try 
					    {
					    	// Storing parent window reference into a String Variable
					    	String Parent_Window = driver.getWindowHandle();
						    Thread.sleep(1000);
							
						    if(groupAndPortfolioMatch==true)
						    {
						    	getObject("DashboardProjectTestPassSummary_project1Click","DashboardProjectTestPassSummary_project2Click",countofPortfolio).click();
						    	Thread.sleep(5000);
						    }
							    
							// Switching from parent window to child window
							for (String Child_Window : driver.getWindowHandles())
						    { 
						    	driver.switchTo().window(Child_Window);
						    	driver.manage().window().maximize();
						    	Thread.sleep(2000);
						    }
							
							if(compareStrings(resourceFileConversion.getProperty("ProjectEdit_pgHead"),getElementByClassAttr("ProjectEdit_pageHeading_Class").getText()))
							{
								APP_LOGS.debug("Project page landing on edit mode successful (PASS).");
								comments+="Project page landing on edit mode successful (PASS). ";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Project page landing on edit mode unsuccessful(FAIL).");
								comments+="Project page landing on edit mode unsuccessful (FAIL).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectPageLandingUnsuccessful");
							}
							
							//Project Edit page should be opened
							if(verifyProjectFields(GroupName, Portfolio, Project, Version, dateStartVerify, EndMonth, EndYear, EndDate, versionLead.get(0).username))
							{
								APP_LOGS.debug("Project Edit page is containing all the items as expected (PASS).");
								comments+="Project Edit page is containing all the items as expected (PASS).";
							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Project Edit page is not containing all the items as expected (FAIL).");
								comments+="Project Edit page is not containing all the items as expected (FAIL).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectContentNotAsExp");
							}
							
							driver.close();
							driver.switchTo().window(Parent_Window);
							Thread.sleep(3000);
							
							//when project found successful click on the Test Pass Name Link
							String Parent_WindowD = driver.getWindowHandle();
							
							if(groupAndPortfolioMatch==true)
							{
								getObject("DashboardProjectTestPassSummary_testPass1Click","DashboardProjectTestPassSummary_testPass2Click",countofPortfolio).click();
								Thread.sleep(6000);
							}
																	
							//Switching from parent window to child window
							for (String Child_Window : driver.getWindowHandles())
						    { 
						    	driver.switchTo().window(Child_Window);
						    	driver.manage().window().maximize();
						    	Thread.sleep(2000);
						    }
							
							//Test Pass Edit page open
							if(compareStrings(resourceFileConversion.getProperty("TestPass_pgHead"),getElementByClassAttr("ProjectEdit_pageHeading_Class").getText()))
							{
								APP_LOGS.debug("Test Pass page landing on edit mode successful (PASS).");
								comments+="Test Pass page landing on edit mode successful (PASS). ";
							}
							else
							{
								fail=true;
								APP_LOGS.debug("Test Pass page landing on edit mode unsuccessful (FAIL).");
								comments+="Test Pass page landing on edit mode unsuccessful (FAIL). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPassLandingUnsuccessful");
							}
							
							//Verification of Test Pass page should be done
							if(verifyTestPassFields(TestPassName, dateStartVerify, testManager.get(0).username, EndMonth, EndYear, EndDate))
							{
								APP_LOGS.debug("Test Pass page is containing all the items as expected (PASS).");
								comments+="Test Pass page is containing all the items as expected (PASS). ";
							}
							else
							{
								fail=true;
								assertTrue(false);
								APP_LOGS.debug("Test Pass Edit page is not containing all the items as expected (FAIL).");
								comments+="Test Pass Edit page is not containing all the items as expected (FAIL).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestPassContentNotAsExp");
							}
							
							driver.close();
							
							driver.switchTo().window(Parent_WindowD);
							Thread.sleep(500);
						}
					    catch (Throwable e) 
						{
							e.printStackTrace();
							APP_LOGS.debug("Project and Test Pass is not clickable due UI unnstable (FAIL).");
							comments+="Project and Test Pass is not clickable due UI unnstable (FAIL).";
							fail=true;
							assertTrue(false);
						}
					}
					else
					{
						fail=true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjectNotFound");
						APP_LOGS.debug("Project in Project and Test Pass Summary is not found (FAIL).");
						comments+="Project in Project and Test Pass Summary is not found (FAIL).";
					}
				}
				else
				{
					fail=true;
					assertTrue(false);
					APP_LOGS.debug("Project and Test Pass Summary section is blank(FAIL).");
					comments+="Project and Test Pass Summary section is blank(FAIL).";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectAndTestPassSummaryNotFound");
				}
				closeBrowser();	
				
				openBrowser();	
				APP_LOGS.debug("Opening browser for user "+tester.get(0).username);
				if(login(tester.get(0).username,tester.get(0).password, "Tester"))
				{
					if(projectTestPassSummary(GroupName, Portfolio, Project, Version, TestPassName, dateStartTPSum, EndDate, EndYear, EndMonth))
					{
						APP_LOGS.debug("Project and Test Pass Summary is varification of Tester (PASS).");
						comments+="Project and Test Pass Summary is varification of Tester (PASS).";
					}
					else
					{
						fail=true;
						assertTrue(false);
						APP_LOGS.debug("Project and Test Pass Summary is varification of Tester (FAIL).");
						comments+="Project and Test Pass Summary is varification of Tester (FAIL).";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProjTPStatusverificationFails");
					}
					closeBrowser();
				}
				else
				{
					fail=true;
					APP_LOGS.debug("Login for "+tester.get(0).username+" is Unsuccessfull.");
					comments+="Login for "+tester.get(0).username+" is Unsuccessfull.";
				}
			}
			else
			{
				APP_LOGS.debug("Login for "+versionLead.get(0).username+" is Unsuccessfull.");
				comments+="Login for "+versionLead.get(0).username+" is Unsuccessfull. ";
			}
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessfull for user "+Role);
			comments+="Login Unsuccessfull for user "+Role;
		}
	}
	
	//verify project test pass summary column headings
	public boolean verifyProjectAndTestPassGridColumnHeading(String projectAndTestPassColumnHead) 
	{
		String columnHeading[] = projectAndTestPassColumnHead.split(",");
		for(int i=1;i<=9;i++)
		{
			if(compareStrings(columnHeading[i-1].trim(), getObject("Dashboard_projectTestPassSummaryColumnHead1", "Dashboard_projectTestPassSummaryColumnHead2", i).getText().trim()))
			{
				APP_LOGS.debug("Project and Test Pass Summary column heading "+columnHeading[i-1]+" matched.");
			}
			else
			{
				return false;
			}
		}
		return true;
	}
			
			
	//verify project in project test pass summary table		
	public boolean projectTestPassSummary(String GroupName,String Portfolio,String Project,String Version,
			String TestPassName,String dateStart,String EndDate,String EndYear,String EndMonth)
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean testPassNameFound = false;
		try 
		{
			Thread.sleep(200);
			if (getElement("Dashboard_projectTestPassSummary_ID").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on Project Test Pass Summary area.");
				totalPages=1;
				paginationCount=1;
			}
			else 
			{
				totalPages = getElement("Dashboard_projectTestPassSummary_ID").findElements(By.xpath("div/a")).size();
				APP_LOGS.debug("More than 1 page avaialble on Project Test Pass Summary area. Calculating total pages.");
			}
			
			nextLinkEnabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
				projectTestPassSummaryRows = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).size();
				
				for (int j = 1; j <= projectTestPassSummaryRows; j++) 
				{
					List<String> projectDetails=new ArrayList<String>();
					
					groupTestPassSummary = getElement("Dashboard_projectTestPassSummary_group_Id").findElements(By.tagName("tr")).get(j-1).getAttribute("group");
					
					if(groupTestPassSummary.equals(GroupName))
					{

						int countFlagForProject = 0;
						int groupTdSize = getObject("DashboardProjectTestPassSummary_table").findElements(By.xpath("tr["+j+"]/td")).size();
						
						if(getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(0).getAttribute("title").equals(Portfolio)
							|| getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(1).getAttribute("title").equals(Portfolio))
						{
							for(int td=0;td<groupTdSize;td++)
							{
								Thread.sleep(200);
								if(td>=groupTdSize-3)
								actual = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getText();
								else
								actual = getObject("DashboardProjectTestPassSummary_table").findElements(By.tagName("tr")).get(j-1).findElements(By.tagName("td")).get(td).getAttribute("title");
								if((Project.equals(actual) && td==2) || (Project.equals(actual) && td==1))
								{
									countofPortfolio = j;
								}
								projectDetails.add(actual);
								countFlagForProject++;
							}
						}
						
						if(countFlagForProject>0)
						{
							Thread.sleep(2000);
							if(projectDetails.size()==9)
							{
								String testManagerReplace = (testManager.get(0).username).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(GroupName) 
									&& projectDetails.get(1).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(2).equalsIgnoreCase(Project)  
									&& projectDetails.get(3).equalsIgnoreCase(Version) 
									&& projectDetails.get(4).equalsIgnoreCase(TestPassName) 
									&& projectDetails.get(5).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(6).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(7).equalsIgnoreCase(dateStart)
									&& projectDetails.get(8).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
									{
										groupAndPortfolioMatch=true;
										testPassNameFound=true;
										APP_LOGS.debug("Project in Project and Test Pass Summary is found at 1st position(PASS).");
										comments+="Project in Project and Test Pass Summary is found at 1st position(PASS).";
										return true;
									}	
							}
							else
							{
								String testManagerReplace = (testManager.get(0).username).replace(".", " ");
								
								if(projectDetails.get(0).equalsIgnoreCase(Portfolio) 
									&& projectDetails.get(1).equalsIgnoreCase(Project)  
									&& projectDetails.get(2).equalsIgnoreCase(Version) 
									&& projectDetails.get(3).equalsIgnoreCase(TestPassName) 
									&& projectDetails.get(4).equalsIgnoreCase(testManagerReplace)
									&& projectDetails.get(5).equalsIgnoreCase(resourceFileConversion.getProperty("Dashboard_projectStatus"))
									&& projectDetails.get(6).equalsIgnoreCase(dateStart)
									&& projectDetails.get(7).equalsIgnoreCase(getMonthNumber(EndMonth)+"-"+EndDate+"-"+EndYear))
									{
										groupAndPortfolioMatch=true;
										testPassNameFound=true;
										APP_LOGS.debug("Project in Project and Test Pass Summary is found at 2nd position(PASS).");
										comments+="Project in Project and Test Pass Summary is found at 2nd position(PASS).";
										return true;
									}	
							}
						}
					}
				}          	
				if (totalPages>1 && testPassNameFound==false)
				{
					if(isElementExistsByXpath("DashboardProjectTestPassSummary_nextLink"))
					{
						nextLinkEnabled = isElementExistsByXpath("DashboardProjectTestPassSummary_nextLink");
						getObject("DashboardProjectTestPassSummary_nextLink").click();
						Thread.sleep(500);
					}
					else
					{
						APP_LOGS.debug("Next link is disabled");
					}
				}
				else
				{
					break;
				}
				
			}
		} 
		catch (Throwable t) 
		{
			fail=true;
			t.printStackTrace();
			APP_LOGS.debug("Exception occured in 'Project and Test Pass Summary' ");
			comments+="Exception occured in 'Project and Test Pass Summary'. ";
			return false;
		}
		return false;
	}
	
	//get month number		
	private String getMonthNumber(String month)
	{
		switch (month) 
		{
			case "Jan":
				return "01";
			case "Feb":
				return "02";
			case "Mar":
				return "03";
			case "Apr":
				return "04";
			case "May":
				return "05";
			case "Jun":
				return "06";
			case "Jul":
				return "07";
			case "Aug":
				return "08";
			case "Sep":
				return "09";
			case "Oct":
				return "10";
			case "Nov":
				return "11";
			case "Dec":
				return "12";
	
			default:
				return null;
		}
	}	
		
	//verifying project fields
	private boolean verifyProjectFields(String group, String portfolio, String project,String version, 
			String startDate, String endMonth, String endYear, String endDate, String versionLead) throws IOException, InterruptedException
	{	
		String expectedEndDate= getMonthNumber(endMonth)+"/"+endDate+"/"+endYear;
		versionLead= versionLead.replace(".", " ");
		try
		{	
			Select groupDropDown = new Select(getElement("ProjectCreateNew_groupDropDown_Id"));
			Select portfolioDropDown= new Select(getElement("ProjectCreateNew_PortfolioDropDown_Id"));
			if (assertTrue(groupDropDown.getFirstSelectedOption().getText().equals(group) 
					&& portfolioDropDown.getFirstSelectedOption().getText().equals(portfolio) 
					&& getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value").equals(project) 
					&& getObject("ProjectCreateNew_versionTextField").getAttribute("value").equals(version) 
					&& getElement("ProjectCreateNew_endDateField_Id").getAttribute("value").equals(expectedEndDate) 
					&& getElement("ProjectCreateNew_startDateField_Id").getAttribute("value").equals(startDate)
					&& getObject("ProjectCreateNew_versionLeadDisplayField").getText().equalsIgnoreCase(versionLead)))
			{
				return true;
			}
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}	
	}
	
	//verifying test pass fields
	 protected boolean verifyTestPassFields(String testPassName, String startDate, String managerName, String endMonth, String endYear, String endDate) throws IOException, InterruptedException
	 { 
		 String expectedEndDate= getMonthNumber(endMonth)+"/"+dateHandler(endDate)+"/"+endYear;
		 managerName= managerName.replace(".", " ");
		 try
		 { 
			 Select statusDown = new Select(getElement("TestPassCreateNew_statusDropDown_Id"));
				
			 if(assertTrue(getElement("TestPassCreateNew_testPassNameTextField_Id").getAttribute("value").equals(testPassName) 
					&& statusDown.getFirstSelectedOption().getText().equals("Active") 
					&& getElement("ProjectCreateNew_startDateField_Id").getAttribute("value").equals(startDate) 
					&& getElement("ProjectCreateNew_endDateField_Id").getAttribute("value").equals(expectedEndDate) 
					&& getElement("TestPassEdit_testManagerPeoplePicker_Id").getText().equalsIgnoreCase(managerName))) 
			 {
				 return true;
			 }
			 return false;
		 }
		 catch(Throwable t)
		 {
			t.printStackTrace();
			return false;
		 } 
	 }
	
	 private String dateHandler(String date)
	 {
		 int idate = Integer.parseInt(date);
		 if (idate<10) 
		 {
			 date="0"+date;
		 }
		 return date;
	 }
			
			 
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
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
