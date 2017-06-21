/*Test Case:- Projects : Verify the 'Test Management' tabis highlighted when user lands on'Projects' page of 'Test Management'.

Expected Result :- 1. User should be logged in as per the valid user credentials entered.
2. User should land on 'Projects' Page successfully. 
3. 'Test Management' tab should be highlighted.
4. 'Test Management' tab should be highlighted.*/

package com.uat.suite.tm_project;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;


@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyTMTabHighlighted extends TestSuiteBase  {
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
		}
	// Test Case Implementation ...
		
	@Test(dataProvider="getTestData")
	public void VerifyTMTabHighlighted1(String Role) throws Exception
	{
		count++;
		System.out.println(" Executing Test Case -> VerifyTMTabHighlighted... "+Role);
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));
			System.out.println("Runmode for role "+Role+" set to no");

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
					}
		
		APP_LOGS.debug(" Executing Test Case -> VerifyTMTabHighlighted... for role "+Role);
		

		//System.out.println(" Executing Test Case -> VerifyTMTabHighlighted... "+Role);				
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		System.out.println("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		
		{	
				
				// clicking on Test Management Tab
				
				
				getElement("UAT_testManagement_Id").click();
				
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				System.out.println("Clicking On Test Management Tab ");
				
				
				String TM_Tab_Highlighted  =eventfiringdriver.findElement(By.xpath("//a[@id='testMgnt']")).getAttribute("class");
				
				APP_LOGS.debug("Navigating Cursor On Test Management Tab...");
				
				System.out.println("Navigating Cursor On Test Management Tab...");
				
				
				try
				{
					compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
					
					APP_LOGS.debug("Test Management Tab is highlighted...");
					
					System.out.println("Test Management Tab is Highlighted...");
					
				}
				catch(Throwable e){
					
					fail=true;
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
				}
				
				for(int i=1;i<=6;i++)
				{
				
					switch (i) {
				
						case 1:	
								APP_LOGS.debug("Clicking On PROJECTS Tab ...");
								
								System.out.println("Clicking On PROJECTS Tab ...");
								
								getElement("TM_projectsTab_Id").click();
								
								Thread.sleep(2000);
								
								try
								{
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
								
										APP_LOGS.debug("Test Management Tab is highlighted...");
										
										System.out.println("Test Management Tab is Highlighted...");
								
								}
								
								catch(Throwable e)
								{
										
										fail=true;
										e.printStackTrace();
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
										
										
								}
						
								break;
						
						case 2:	
								APP_LOGS.debug("Clicking On TEST PASSES Tab ...");
								
								System.out.println("Clicking On TEST PASSES Tab ...");
								
								getElement("TM_testPassesTab_Id").click();
								
								Thread.sleep(3000);
								
								
								try
								{
										
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
							
										APP_LOGS.debug("Test Management Tab is highlighted...");
										
										System.out.println("Test Management Tab is Highlighted...");
							
								}
								
								catch(Throwable e)
								{
										
										fail=true;
										e.printStackTrace();
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
								}
					
								break;
						
						
						case 3:	
								APP_LOGS.debug("Clicking On TESTERS Tab ...");
								
								System.out.println("Clicking On TESTERS Tab ...");
								
								getElement("TM_testersTab_Id").click();
								//eventfiringdriver.findElement(By.id("navTesters")).click();
								
								Thread.sleep(2000);
								
								
								
								try{
									
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
							
										APP_LOGS.debug("Test Management Tab is highlighted...");
										
										System.out.println("Test Management Tab is Highlighted...");
							
								}
								
								catch(Throwable e)
								{
										fail=true;
										e.printStackTrace();
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
								}
					
								break;	
					
					case 4:	
						
						
								APP_LOGS.debug("Clicking On TEST CASES Tab ...");
								
								System.out.println("Clicking On TEST CASES Tab ...");
								
								getElement("TM_testCasesTab_Id").click();
								//eventfiringdriver.findElement(By.id("navTestCases")).click();
								
								Thread.sleep(2000);
								
								try
								{
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
							
										APP_LOGS.debug("Test Management Tab is highlighted...");
							
										System.out.println("Test Management Tab is Highlighted...");
							
								}
								catch(Throwable e)
								{
										fail=true;
										e.printStackTrace();
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
								}
					
								break;	
					
					case 5:	
								APP_LOGS.debug("Clicking On TEST STEPS Tab ...");
								
								System.out.println("Clicking On TEST STEPS Tab ...");
								
								getElement("TM_testStepsTab_Id").click();
								
								Thread.sleep(2000);
								
								
								try
								{
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
							
										APP_LOGS.debug("Test Management Tab is highlighted...");
										
										System.out.println("Test Management Tab is Highlighted...");
							
								}
								catch(Throwable e)
								{
									fail=true;
									e.printStackTrace();
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
								}
					
								break;
					
					case 6:	
						
								APP_LOGS.debug("Clicking On ATTACHMENTS Tab ...");
								
								System.out.println("Clicking On ATTACHMENTS Tab ...");
								
								getElement("TM_attachmentsTab_Id").click();
								
								Thread.sleep(2000);
								
						
								try
								{
										compareStrings(TM_Tab_Highlighted,OR.getProperty("UAT_testManagementTab_Class"));
							
										APP_LOGS.debug("Test Management Tab is highlighted...");
										
										System.out.println("Test Management Tab is Highlighted...");
							
								}
								
								catch(Throwable e)
								{
									fail=true;
									e.printStackTrace();
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyTMTabHighlightedError");
								}
					
								break;	
		
					
					}
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
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		

		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
		}
		else
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
		
		skip=false;
		fail=false;
		

	}
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}
}
	

	
	
	


