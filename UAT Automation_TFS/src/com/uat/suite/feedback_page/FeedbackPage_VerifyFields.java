package com.uat.suite.feedback_page;

import java.util.ArrayList;
import java.util.List;



import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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

public class FeedbackPage_VerifyFields extends TestSuiteBase {


	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int counter=0;
	static int count=-1;
	int totalProject ;
	int projectCount;
	int projectVIewAllRows;
	int totalPages;
	boolean flag=false;
	int testManagementProjectTableRows;
	int totalProjectCountOfportfolio;
	int feedbackPageProjectListSize;
	static boolean isLoginSuccess=false;
	ArrayList<Credentials> version_Lead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	ArrayList<String> totalProjectCountOfProjectTab;
	ArrayList<String> totalProjectCountOnFeedbackTab;
	ArrayList<String> totalVersionCountOnProjectTab;
	String comments;

	int summationOfAllProjectOfDashboardPage=0;
	int projectCountOnProjectPage=0;


	// Runmode of test case in a suite

	@BeforeTest
	public void checkTestSkip(){

		if(!TestUtil.isTestCaseRunnable( feedbackPageSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(feedbackPageSuiteXls, this.getClass().getSimpleName());

		version_Lead=new ArrayList<Credentials>();
		test_Manager=new ArrayList<Credentials>();
		tester=new ArrayList<Credentials>();
		totalProjectCountOfProjectTab=new ArrayList<String>();
		totalProjectCountOnFeedbackTab=new ArrayList<String>();


		totalVersionCountOnProjectTab=new ArrayList<String>();

	}


	//String testManager,String tester,String area,String addRole,String testCaseName,String estimatedTime
	// Test Case Implementation ...

	@Test(dataProvider="getTestData")
	public void verifyFeedbackPageFields  (String role,String groupName,String portfolioName,String projectName1,String totalProjectCount, 
			String proj1version,String endMonth, String endYear, String endDate, String versionLead ,String proj1TestPassName1, 
			String proj1TestPassName2,String testManager,String testers,String proj1TP1addRole1,String proj1TP1addRole2,String proj1TP1addRole3)throws Exception
	{
		count++;

		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}


		comments="";


		int versionLead_count = Integer.parseInt(versionLead);

		version_Lead = getUsers("Version Lead", versionLead_count);


		int testManager_count = Integer.parseInt(testManager);

		test_Manager = getUsers("Test Manager", testManager_count);


		int testers_count = Integer.parseInt(testers);			


		tester = getUsers("Tester", testers_count);



		APP_LOGS.debug(" Executing Test Case :-"+ this.getClass().getSimpleName());

		APP_LOGS.debug("Opening Browser... ");

		openBrowser();

		isLoginSuccess = login(role);

		if(isLoginSuccess)
		{
			try
			{

				//click on testManagement tab
				APP_LOGS.debug("Clicking On Test Management Tab ");

				getElement("UAT_testManagement_Id").click();
				Thread.sleep(1000);


				APP_LOGS.debug(" User "+ role +" creating PROJECT with Version Lead "+version_Lead.get(0).username );
				if (!createProject(groupName, portfolioName, projectName1, proj1version, endMonth, endYear, endDate, version_Lead.get(0).username))
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
					comments="Project Creation Unsuccessful(Fail) by "+role+"  . ";
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+" . ");
					throw new SkipException("Project Creation Unsuccessfull");
				}



				if (!createTestPass(groupName, portfolioName, projectName1, proj1version, proj1TestPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}
				if (!createTestPass(groupName, portfolioName, projectName1, proj1version, proj1TestPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
					comments="Test Pass Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail)");
					throw new SkipException("Test Pass Creation Unsuccessfull");
				}

				if (!createTester(groupName, portfolioName, projectName1, proj1version, proj1TestPassName1, tester.get(0).username,proj1TP1addRole1,proj1TP1addRole1)) 
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}

				if (!createTester(groupName, portfolioName, projectName1, proj1version, proj1TestPassName1, tester.get(1).username,proj1TP1addRole2,proj1TP1addRole2)) 
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}
				
				if (!createTester(groupName, portfolioName, projectName1, proj1version, proj1TestPassName1, tester.get(2).username,proj1TP1addRole3,proj1TP1addRole3)) 
				{	

					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
					comments="Tester Creation Unsuccessful(Fail)   ." ;
					APP_LOGS.debug("Tester Creation Unsuccessfull");
					throw new SkipException("Tester Creation Unsuccessfull");

				}


				APP_LOGS.debug("Closing Browser... ");
				closeBrowser();



				APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+test_Manager.get(0).username + " to verify contents Of Feedback Page ");

				openBrowser();

				if (login(test_Manager.get(0).username ,test_Manager.get(0).password, "Test Manager")) 
				{

					try{

						getElement("UAT_dashboard_Id").click();


						APP_LOGS.debug("Calculating the number Of shown Projects of all portfolio on Dashboard Page assigned to the Test Manager : "+test_Manager.get(0).username);

						List<WebElement> selectPortfolio = getElement("DashboardPage_portfolioDropdown_id").findElements(By.tagName("option"));

						System.out.println("Portfolio Size : " + selectPortfolio.size());
						
						// Getting Count Of All Projects Assigned to the Test Manager
						Select portfolioSelection = new Select(getElement("DashboardPage_portfolioDropdown_id"));
						
						for(int i=0 ;i<selectPortfolio.size();i++)
						{	
							portfolioSelection.selectByIndex(i);
							List<WebElement> selectProject = getElement("DashboardPage_projectDropdown_id").findElements(By.tagName("option"));
							if(selectProject.size()>1)
							{
								totalProjectCountOfportfolio = selectProject.size();
								totalProject = totalProjectCountOfportfolio-1;	
							}
							else if(selectProject.size()==1)
							{
								totalProjectCountOfportfolio = selectProject.size();
								totalProject = totalProjectCountOfportfolio;								
							}
							
							
							summationOfAllProjectOfDashboardPage=summationOfAllProjectOfDashboardPage+totalProject;
							System.out.println("summationOfAllProjectOfDashboardPage :"+summationOfAllProjectOfDashboardPage);

						}

						APP_LOGS.debug("Total Number Of Projects Assigned to Test Manager : "+test_Manager.get(0).username + " are  "+ summationOfAllProjectOfDashboardPage);






						Thread.sleep(500);
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(500);

					try
						{
							//Calculating the number of pages in My Activities section				
							if (getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
							{			
								totalPages=1;

							}
							else 
							{		
								totalPages = getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
							}
							
							//Iterating the loop to the Total Number of Pages found on My Activities
							
							
							do 
							{								
								
								projectVIewAllRows = getElement("ProjectViewAll_Table_Id").findElements(By.tagName("tr")).size();
								System.out.println("projectVIewAllRows : "+projectVIewAllRows);
								projectCountOnProjectPage=projectCountOnProjectPage+projectVIewAllRows;
								//Iterating through the number of rows 
								for (int j = 1; j <= projectVIewAllRows; j++) 
								{	
									//Storing Project Name and Version in Array List for comparison 
									String projectCell=getObject("ProjectViewAll_ProjectNameXpath1","ProjectViewAll_ProjectNameXpath2",j).getAttribute("title");
									String versionCell=getObject("ProjectViewAll_VersionNameXpath1","ProjectViewAll_VersionNameXpath2",j).getAttribute("title");
									totalProjectCountOfProjectTab.add(projectCell);
									totalVersionCountOnProjectTab.add(versionCell);
					
								}
								
								
								
							} while (ifElementIsClickableThenClick("project_NextLink"));
							
							
							
							for (int i = 0; i < totalPages; i++) 
							{
								projectVIewAllRows = getElement("ProjectViewAll_Table_Id").findElements(By.tagName("tr")).size();
								System.out.println("projectVIewAllRows : "+projectVIewAllRows);
								projectCountOnProjectPage=projectCountOnProjectPage+projectVIewAllRows;
								//Iterating through the number of rows 
								for (int j = 1; j <= projectVIewAllRows; j++) 
								{	
									//Storing Project Name and Version in Array List for comparison 
									String projectCell=getObject("ProjectViewAll_ProjectNameXpath1","ProjectViewAll_ProjectNameXpath2",j).getAttribute("title");
									String versionCell=getObject("ProjectViewAll_VersionNameXpath1","ProjectViewAll_VersionNameXpath2",j).getAttribute("title");
									totalProjectCountOfProjectTab.add(projectCell);
									totalVersionCountOnProjectTab.add(versionCell);
					
								}

								if(totalPages>1)
								{
									if(isElementExistsByXpath("project_NextLink"))
									{	
										getObject("project_NextLink").click();
									}
									else
									{
										APP_LOGS.debug("Next Link is disabled");

									}
								}


							}
						}
						catch(Throwable t)
						{	
							t.printStackTrace();
							assertTrue(false);
							fail=true;
							APP_LOGS.debug("Projects Not Visible ");
							comments+="\nProjects Not Visible (Fail)";
						}


						getElement("UAT_Feedback_Id").click();

						String feedbackTabHighlighted  =getElement("UAT_Feedback_Id").getAttribute("class");
						
						if(compareStrings(OR.getProperty("UAT_FeedbackTab_Class"), feedbackTabHighlighted))
						{
							APP_LOGS.debug("Feedback Tab Highlighted Successfully ( Pass)");
							comments=comments+"\nFeedback Tab  Highlighted Successfully (Pass)";

						}
						else
						{
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FeedbackTabHighlightedFailure");
							APP_LOGS.debug("Feedback Tab Not Highlighted  ( Fail)");
							comments=comments+"Feedback Tab Not Highlighted  ( Fail)";
						}

						if(getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id").isDisplayed() && getElement("FeedbackPage_TestPassFeedbackVersionDropdown_Id").isDisplayed() 
								&& getElement("FeedbackPage_TestPassFeedbackTestPassDropdown_Id").isDisplayed()&&
								getElement("FeedbackPage_TestPassFeedbackTesterDropdown_Id").isDisplayed() && getElement("FeedbackPage_TestPassFeedbackRoleDropdown_Id").isDisplayed()&&
								getElement("FeedbackPage_TestPassFeedbackGoButton_Id").isDisplayed() && getElement("FeedbackPage_TestPassFeedbackExportButton_Id").isDisplayed()
								&&getObject("FeedbackPage_TestStepFeedbackSelection").isDisplayed())

						{
							APP_LOGS.debug("'Select Project', 'Select Version', 'Select Test Pass', 'Select Tester', 'Select Role' Dropdown ,'Go' Button and 'Export' Button showing properly on Feedback Page (Pass)");
							comments=comments+"\n 'Select Project', 'Select Version', 'Select Test Pass', 'Select Tester', 'Select Role' Dropdown ,'Go' Button and 'Export' Button showing properly on Feedback Page (Pass)";
						}
						else
						{
							assertTrue(false);
							fail=true;
							APP_LOGS.debug("'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role' Dropdown ,'Go' Button and 'Export' Button not Present as landed on Feedback Page  ( Fail) ");
							comments=comments+" \n'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role'  Dropdown,'Go' Button and 'Export' Button not Present as landed on Feedback Page  ( Fail)  . ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DropdownNotPresentFailure");

						}





						APP_LOGS.debug("Without Selecting anything from any dropdown Verifying that as Default Value 'Select Project','Select Test Pass','Select Tester','Select Role' selected in Dropdown ");
						Thread.sleep(800);

						Select projectSelection=new Select(getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id"));
						List<WebElement> defaultSelectedProjectValue  = projectSelection.getOptions();
						String actualDefaultSelectedProjectValue =defaultSelectedProjectValue.get(0).getText();

						Select testPassSelection=new Select(getElement("FeedbackPage_TestPassFeedbackTestPassDropdown_Id"));
						List<WebElement> defaultSelectedTestPassValue  = testPassSelection.getOptions();
						String actualDefaultSelectedTestPassValue=defaultSelectedTestPassValue.get(0).getText();


						Select versionSelection=new Select(getElement("FeedbackPage_TestPassFeedbackVersionDropdown_Id"));
						List<WebElement> defaultSelectedVersionValue  = versionSelection.getOptions();
						String actualDefaultSelectedVersionValue =defaultSelectedVersionValue.get(0).getText();


						Select testerSelection=new Select(getElement("FeedbackPage_TestPassFeedbackTesterDropdown_Id"));
						List<WebElement> defaultSelectedTesterValue  = testerSelection.getOptions();
						String actualDefaultSelectedTesterValue =defaultSelectedTesterValue.get(0).getText();

						Select roleSelection=new Select(getElement("FeedbackPage_TestPassFeedbackRoleDropdown_Id"));
						List<WebElement> defaultSelectedRoleValue  = roleSelection.getOptions();
						String actualDefaultSelectedRoleValue =defaultSelectedRoleValue.get(0).getText();


						if(actualDefaultSelectedProjectValue.equals(resourceFileConversion.getProperty("DefaultProjectValue")) && actualDefaultSelectedVersionValue.equals(resourceFileConversion.getProperty("DefaultVersionValue"))
								&& actualDefaultSelectedTestPassValue.equals(resourceFileConversion.getProperty("DefaultTestPassValue")) && actualDefaultSelectedTesterValue.equals(resourceFileConversion.getProperty("DefaultTesterValue"))
								&& actualDefaultSelectedRoleValue.equals(resourceFileConversion.getProperty("DefaultRoleValue")) )
						{
							APP_LOGS.debug("'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role' as default value showing selected as landed on Feedback Page ( Pass) ");
							comments=comments+" \n 'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role' as default value showing selected as landed on Feedback Page ( Pass) ";
						}
						else
						{	
							assertTrue(false);
							fail=true;
							APP_LOGS.debug("'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role' as default value not showing selected  as landed on Feedback Page  ( Fail) ");
							comments=comments+" \n'Select Project' ,'Select Version','Select Test Pass','Select Tester','Select Role' as default value not showing selected  as landed on Feedback Page  ( Fail)  . ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "DefaultValueSelectionOnFeedbackPageFailure");
						}


						List<WebElement> selectProject = getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id").findElements(By.tagName("option"));
						int actualTotalNumberOfProjectCountOnFeedbackPage = ((selectProject.size())-1);
						APP_LOGS.debug("Substracting 1 option tag : 'Select Project' to get actual total Number Of Project Count ....So total Number Of Project Count is : "+ actualTotalNumberOfProjectCountOnFeedbackPage);


						if(compareIntegers(summationOfAllProjectOfDashboardPage,actualTotalNumberOfProjectCountOnFeedbackPage))
						{
							APP_LOGS.debug("Total Number Of Projects Assigned To Test Manager  : "+test_Manager.get(0).username +" are showing Properly in Project Dropdown Of Feedback page ( Pass )");
							comments=comments+" \nTotal Number Of Projects Assigned To Test Manager  : "+test_Manager.get(0).username +" are showing Properly in Project Dropdown Of Feedback page ( Pass )";
						}

						else
						{	
							fail=true;
							APP_LOGS.debug("Total Number Of Projects Assigned To Test Manager  :"+test_Manager.get(0).username +"  are NOT showing correctly in Project Dropdown Of Feedback page ( Fail )");
							comments=comments+" \nTotal Number Of Projects Assigned To Test Manager  :"+test_Manager.get(0).username +"  are NOT showing correctlyin Project Dropdown Of Feedback page ( Fail )";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TotalNumOfProjectNotBepresentOnDeedbackPageFailure");
						}




						//Feedback Page project Drop down Value
						
						
						
						Select projectSelectionOnFeedbackPage = new Select(getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id"));

						List<WebElement> projectList = projectSelectionOnFeedbackPage.getOptions();

						System.out.println(feedbackPageProjectListSize);
						
						String projectName;

						for(int i =2; i<=feedbackPageProjectListSize ; i++)
						{
							projectName=getObject("FeedbackPage_ProjectDropdownNoOfProjectXpath1","FeedbackPage_ProjectDropdownNoOfProjectXpath2",i).getAttribute("title").trim();
							totalProjectCountOnFeedbackTab.add(projectName);
							System.out.println(projectName);

						}

						
						//	Verifying All the Projects from Projects Tab are showing correctly  on Feedback Page or not ... and also verifying multiple versions of same project
						
						for(int k=0;k<summationOfAllProjectOfDashboardPage;k++)
						{
							for(int n=0;n<feedbackPageProjectListSize;n++)
							{
								if(totalProjectCountOfProjectTab.get(k).equals(totalProjectCountOnFeedbackTab.get(n)))
								{	
									counter++;
									Select projectSelectionOnFeedbackPage1 = new Select(getElement("FeedbackPage_TestPassFeedbackProjectDropdown_Id"));
									projectSelectionOnFeedbackPage1.selectByIndex(n);
													
												Select versionSelectionFromFeedbackpage = new Select(getElement("FeedbackPage_TestPassFeedbackVersionDropdown_Id"));
				
													List<WebElement> versionSize = versionSelectionFromFeedbackpage.getOptions();
				
													int	feedbackPageVersionListSize = versionSize.size();
				
													System.out.println(feedbackPageVersionListSize);
				
													if(feedbackPageVersionListSize>1)
													{
														for(int i=1;i<=feedbackPageVersionListSize;i++)
														{
															String versionName=getObject("FeedbackPage_VersionDropdownXpath1","FeedbackPage_VersionDropdownXpath2",i).getText();
															System.out.println(versionName);
				
															for(int j=0;j<projectCountOnProjectPage;j++)
															{
																if(totalVersionCountOnProjectTab.get(j).equals(versionName))
																{
																	APP_LOGS.debug("The Project : "+totalProjectCountOnFeedbackTab.get(n) +"having Multiple Version i.e. " + versionName);
																}
				
															}
														}
													}
								}
								
							}
						}


						if(counter<projectCountOnProjectPage)
						{
							fail=true;
							assertTrue(false);
							comments=comments+" \n Some Projects are not found in dropdown of Feedback Page (Fail)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SomeProjectsNotFoundInDropDownOfFeedbackPage");
							APP_LOGS.debug("Some Projects are not found in dropdown of feedback Page  (Fail )");
							
						}
						else
						{
							APP_LOGS.debug("All projects are present in Dropdown of Feedback Page  ( Pass ) ");
							comments+=" All projects are showing correctly in Dropdown of Feedback Page  ( Pass ) ";
						}


						
						
						//Verifying respected Version,TestPass ,Tester,Role are showing properly for selected Project 
					
						for(int n=0;n<feedbackPageProjectListSize;n++)
						{
							if(totalProjectCountOnFeedbackTab.get(n).equals(projectName1))
							{		
								
									Thread.sleep(5000);
									projectSelectionOnFeedbackPage.selectByIndex(n);
						
							}
						}
						
						
						

					}
					catch(Throwable t)
					{
						t.printStackTrace();
					}









							
							//getElement("ConfigGeneralSettingTab_Id").click();

							APP_LOGS.debug("Selecting  Portfolio From Dashboard Page Portfolio Dropdown ");
							Thread.sleep(1000);

							Select portfolioSelection=new Select(getElement("DashboardPage_portfolioDropdown_id"));

				/*			portfolioSelection.selectByVisibleText(portfolioName);

							Thread.sleep(500);

							APP_LOGS.debug("Selecting Version from General Setting Version Dropdown");

							Select versionSelection=new Select(getElement("ConfigGeneralSettingVersionSelectDropdown_Id"));

							versionSelection.selectByVisibleText(version);

							Thread.sleep(500);

							APP_LOGS.debug("Getting Project Count For Selected Portfolio");
							List<WebElement> portfolioCount = getElement("DashboardPage_portfolioDropdown_id").findElements(By.tagName("option"));
							System.out.println("The size of Select Test Pass drop down" + portfolioCount.size());

							List<WebElement> selectProject = getElement("DashboardPage_projectDropdown_id").findElements(By.tagName("option"));
							//int projectCount
							System.out.println("The size of Select Test Pass drop down" + selectProject.size());
							for(int i=1 ;i<=selectProject.size();i++)
							{
								String selectTestPassFromDropdown=eventfiringdriver.findElement(By.xpath("//select[@id='tpForGS']/option["+i+"]")).getAttribute("title").trim();
							//	System.out.println(selectTestPassFromDropdown);
								if(selectTestPassFromDropdown.equals(testPassName1))
								{
									eventfiringdriver.findElement(By.xpath("//select[@id='tpForGS']/option["+i+"]")).click();
									Thread.sleep(300);
									getElement("ConfigGeneralSettingSeqTestingWithinTCRadioButton_Id").click();
									Thread.sleep(500);
									getObject("Configuratiion_GeneralSettingSaveBtn").click();
									Thread.sleep(500);
									getObject("ProjectViewAll_DeleteConfirmOkButton").click();
									APP_LOGS.debug("Test Pass Level Options : ' Sequential Testing Within Test Case is selected for Test Pass ' : "+ selectTestPassFromDropdown + "   (Pass)  .");
									comments=comments+"Test Pass Level Options : ' Sequential Testing Within Test Case is selected for Test Pass ' : "+ selectTestPassFromDropdown +"  (Pass)  .";  
									break;
								}
							}

					 */




				}
				else 
				{
			
					APP_LOGS.debug("Test Manager Login Not Successful");

				}
















				closeBrowser();

			}

			catch(Throwable t)
			{	


				t.printStackTrace();
				fail=true;
				assertTrue(false);
			}

		}
		else 
		{
			fail=true;
			APP_LOGS.debug("Login Not Successful");

		}
















	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");

		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(feedbackPageSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;


	}

	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, "Test Cases", TestUtil.getRowNum(feedbackPageSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(feedbackPageSuiteXls, "Test Cases", TestUtil.getRowNum(feedbackPageSuiteXls,this.getClass().getSimpleName()), "FAIL");

	}



	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(feedbackPageSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
	
	private boolean ifElementIsClickableThenClick(String key)
	{
		
	  try
	  {
		  eventfiringdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		   eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
		   getObject(key).click();
		   return true;
	   
	  }
	  catch(Throwable t)
	  {
		  
		  return false;
	   
	  }
	  finally
	  {
			 eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(CONFIG.getProperty("default_implicitWait")), TimeUnit.SECONDS);
			 							 
	  }
	  
	 }


}		
