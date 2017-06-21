package com.uat.suite.tm_testcases;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
import com.uat.util.Xls_Reader;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class TCs_VerifyDownUploadTempPage extends TestSuiteBase {
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> testers;
	String comments;
	
	// Runmode of test case in a suite
	
		@BeforeTest
		public void checkTestSkip(){
			
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
		public void verifyTCDownUploadTempPage (String Role,String GroupName,String PortfolioName,String ProjectName, 
				String Version,String EndMonth, String EndYear, String EndDate, String VersionLead ,String TestPassName,
				String TestManager,String Tester,String AddRole,String TestCaseName, String uploadFileName ) throws Exception
		{
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
						}
			
			
			
			int versionLead_count = Integer.parseInt(VersionLead);
			
			versionLead = getUsers("Version Lead", versionLead_count);
			

			int testManager_count = Integer.parseInt(TestManager);
			
			testManager = getUsers("Test Manager", testManager_count);
			
			
			int testers_count = Integer.parseInt(Tester);			
			
					
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
					
					APP_LOGS.debug(" User "+ Role +" creating PROJECT with Version Lead "+versionLead.get(0).username );
					
					
					if (!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{	
						
						//fail=true;
						//assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationFailure");
						comments+="Project Creation Unsuccessful(Fail) by "+Role+". ";
						APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+Role+". ");
						//closeBrowser();
						throw new SkipException("Project Creation Unsuccessful");
					}
					
					APP_LOGS.debug("Closing Browser... ");
					
					
					closeBrowser();
					
				
					
					APP_LOGS.debug("Opening Browser...Logging In With Role Version Lead "+versionLead.get(0).username + " to create Test Pass ");
					
					
					openBrowser();
					
					if (login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead")) 
					{
						
						
						//click on testManagement tab
						APP_LOGS.debug("Clicking On Test Management Tab ");
						
						Thread.sleep(500);
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						if (!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
						{	
							//fail=true;
							//assertTrue(false);
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationFailure");
							comments+="Test Pass Creation Unsuccessful(Fail). " ;
							APP_LOGS.debug("Test Pass CreationUnsuccessful(Fail) ");
							//closeBrowser();
							throw new SkipException("Test Pass Creation Unsuccessful");
						}
						
						APP_LOGS.debug("Closing Browser... ");
						
												
						closeBrowser();
						
						
						

						APP_LOGS.debug("Opening Browser...Logging In With Role Test Manager "+testManager.get(0).username + " to create Testers ");
						
						openBrowser();
						
						if (login(testManager.get(0).username,testManager.get(0).password, "Test Manager")) 
						{
							
							//click on testManagement tab
							APP_LOGS.debug("Clicking On Test Management Tab ");
							
							Thread.sleep(500);
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
						
							if (!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, testers.get(0).username,AddRole, AddRole)) 
							{	
								//fail=true;
								//assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationFailure");
								comments+="Tester Creation Unsuccessful(Fail)" ;
								APP_LOGS.debug("Tester Creation Unsuccessful");
								//closeBrowser();
								throw new SkipException("Tester Creation Unsuccessful");
							}								
					
							Thread.sleep(2000);
							getElement("TestCaseNavigation_Id").click();
							
						
							
							
							Thread.sleep(1000);							
							
							
							getElement("TestCasesDownload_UploadTemplateTestCasesLink_Id").click();
							
							
							getObject("TestCases_DownLoadTemplateButton").click();
							
													
							try
							{
							    eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
							    comments+="DownloadTemplate - ActiveX is enabled. ";
							    
							    try
							    {
							    	Thread.sleep(4000);
							    	if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
							    	{
							    		getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
							    		comments+="Steps are present to enable it - (Fail). ";
								    	fail = true;
								    	assertTrue(false);
								    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXEnabledStepsPresent");
							    	}
							    	else
							    	{
							    		comments+="Steps are not present to enable it(Pass). ";
							    	}
							    	
							    }
							    catch(Throwable t)
							    {
							    	comments+="Steps are not present to enable it(Pass). ";
							    }
							    
							}
							catch(Exception e)
							{
								comments+="DownloadTemplate - ActiveX is disabled. ";
							    //Active x code
							    try
							    {
							    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id(OR.getProperty("TestCaseCreateNew_TestCaseActiveX_Id"))));
								    if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
								    {
								    	getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
								    	comments+="Steps are present to enable it(Pass). ";
								    }							     
								    else
								    {
								    	fail = true;
								    	assertTrue(false);
								    	comments+="Steps are not present to enable it(Fail). ";
									    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");
								    }
							    }
							    catch(Throwable t)
							    {
							    	fail = true;
							    	assertTrue(false);
							    	comments+="Steps are not present to enable it(Fail). ";
								    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");
							    }
							    
							    	
							   
							}
							
						    Thread.sleep(2000);						    
						    
						
							getElement("TestCases_UplaodTestCaseButton_Id").click();
							
							try
							{
							    eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
							    comments+="UploadTemplate - ActiveX is enabled. ";
							    
							    Xls_Reader fileToUpload = new Xls_Reader("src\\com\\uat\\xls\\TestData\\"+uploadFileName);
							    
							    int totalTestCasesToUpload = fileToUpload.getRowCount("Test Case Template")-3;
							    
							    Thread.sleep(1000);
								
								Alert myAlert=eventfiringdriver.switchTo().alert();
								
								myAlert.sendKeys(System.getProperty("user.dir")+"\\src\\com\\uat\\xls\\TestData\\"+uploadFileName);
								
								//myAlert.accept();
								Robot r = new Robot();
								

								r.keyPress(KeyEvent.VK_ENTER);
						        
						        Thread.sleep(3000);
						        
						        eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[1]/div[1]/button")).click();
						        
						        Thread.sleep(500);
						        
						        try
							    {
							    	Thread.sleep(4000);
							    	if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
							    	{
							    		getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
							    		comments+="Steps are present to enable it - (Fail). ";
								    	fail = true;
								    	assertTrue(false);
								    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXEnabledStepsPresent");
							    	}
							    	else
							    	{
							    		comments+="Steps are not present to enable it(Pass). ";
							    	}
							    	
							    }
							    catch(Throwable t)
							    {
							    	comments+="Steps are not present to enable it(Pass). ";
							    }
						        
						        getObject("TestCases_viewAllTestCasesLink").click();
						        Thread.sleep(500);
						        
						        if(assertTrue(getElement("TestCasesViewAll_table_Id").isDisplayed()))
						        {
						        	int visibleTestCases = getElement("TestCasesViewAll_tableBody_Id").findElements(By.xpath("tr")).size();
						        	
						        	if (compareIntegers(totalTestCasesToUpload, visibleTestCases)) 
						        	{
										comments+="Uploaded and Visible Test Cases Count Match(Pass). ";
									}
						        	else
						        	{
						        		comments+="Uploaded and Visible Test Cases Count doesn't Match(Fail). ";
						        		fail=true;
							        	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Uploaded and Visible Test Cases Count");
						        		
						        	}
						        	
						        }
						        else
						        {
						        	fail=true;
						        	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "View All Table Not Visible");
						        	comments+="View All Table Not Visible(Fail). ";						        	
						        	
						        }						        
							    
							    
							    
							}
							catch(Exception e)
							{
								comments+="UploadTemplate - ActiveX is disabled. ";
							    //Active x code
							    try
							    {
							    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id(OR.getProperty("TestCaseCreateNew_TestCaseActiveX_Id"))));
								    if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
								    {
								    	getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
								    	comments+="Steps are present to enable it(Pass). ";
								    }							     
								    else
								    {
								    	fail = true;
								    	assertTrue(false);
								    	comments+="Steps are not present to enable it(Fail). ";
									    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");
								    }
							    }
							    catch(Throwable t)
							    {
							    	fail = true;
							    	assertTrue(false);
							    	comments+="Steps are not present to enable it(Fail). ";
								    TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActiveXDisabledStepsNotPresent");
							    }					    
							    	
							   
							}	
						
					
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
				catch(Throwable e) 
				{
					e.printStackTrace();
					fail = true;
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
					comments+="Skip Exception or other exception occured" ;
				}
				
				closeBrowser();
				

				
			}			
			else 
			{				
				APP_LOGS.debug("Login Not Successful");
				
			}	
	
	
	}			
		
		



				@AfterMethod
				public void reportDataSetResult()
				{
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
					else{
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