/* Test Case:-	1. Login in UAT with Tester user credentials.
				2. Verify the 'Test Management' tab available on Dashboard or any other page.  
				
   Expected Result :-	1. User should be logged in as a 'Tester' user.
						2. On all pages, 'Test Management' tab should be disabled for a Tester user. (Hence Tester should not be able to land on 'Projects' page)


*/

package com.uat.suite.tm_project;

import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;


@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class Tester_TMnStakeDashDisabled extends TestSuiteBase  {
	
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
	
	
	// Test Case Implementation 	
		
	@Test(dataProvider="getTestData")
	public void Tester_TMnStakeDashDisabled1(String role) throws Exception, InterruptedException
	{
         count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		APP_LOGS.debug(" Executing Test Tester_TMnStakeDashDisabled...");
		
		System.out.println("Executing Test Tester_TMnStakeDashDisabled...");				
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		System.out.println("Opening Browser... ");
		
		isLoginSuccess = login(role);
		
		
		if(isLoginSuccess)
		{
		
				APP_LOGS.debug("Dashoboard Page is displayed...");
		
				System.out.println("Dashoboard Page is displayed...");
		
				APP_LOGS.debug("Navigating Cursor On Test Management Tab Link...");
			
				System.out.println("Navigating Cursor On Test Management Tab Link...");
			
				//Checking whether Test Management Tab is Disabled Or Not
				
				try
				{
					WebElement TM_Tab_Disabled = getElement("UAT_testManagement_Id");
			
					String isDisabled=TM_Tab_Disabled.getAttribute("disabled");
			
			
					if (isDisabled==null || !isDisabled.equals("true"))
					{	
							fail=true;
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Testers_TMnStakeDashDisabledError");
							
							System.out.println("Test Management Tab link: Enabled");
							
							APP_LOGS.debug("Test Management Tab link: Enabled");
							
							System.out.println(" Test Case Failed ");
							
							APP_LOGS.debug(" Test Case Failed ");
					}
					else
					{	
							
							System.out.println("Test Management Tab link: Disabled");
							
							APP_LOGS.debug("Test Management Tab link: Disabled");
							
							APP_LOGS.debug("For User " + role + ": Test Management Tab is showing Disabled Successfully ");
							
							System.out.println("For User " + role + ": Test Management Tab is showing Disabled Successfully ");
					}
				}
			
			
				catch(Throwable t)
				{	
					fail=true;
					
					t.printStackTrace();
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Testers_TMnStakeDashDisabledError");
				}
			
			
				APP_LOGS.debug("Navigating Cursor On Stakeholder Dashboard  Tab Link...");
			
				System.out.println("Navigating Cursor On  Stakeholder Dashboard  Tab Link...");
				
				//Checking whether Stakeholder Dashboard Tab is Disabled Or Not
				try
				{
					WebElement StakeDash_Tab_Disabled = getElement("UAT_stakeholderDashboard_Id");
			
					String isDisabled=StakeDash_Tab_Disabled.getAttribute("disabled");
				
			
					if (isDisabled==null || !isDisabled.equals("true"))
					{	
							fail=true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Testers_TMnStakeDashDisabledError");
							System.out.println("Stakeholder Dashboard Tab Link: Enabled");
							
							
							APP_LOGS.debug("Stakeholder Dashboard Tab Link: Enabled");
							
							System.out.println(" Test Case Failed ");
							
							APP_LOGS.debug(" Test Case Failed ");
					}
					else
					{	
							
							System.out.println("Stakeholder Dashboard Tab Link: Disabled");
							
							APP_LOGS.debug("Stakeholder Dashboard Tab Link: Disabled");
							
							APP_LOGS.debug("For User " + role + ": Stakeholder Dashboard Tab is showing Disabled Successfully ");
							
							System.out.println("For User " + role + ": Stakeholder Dashboard Tab is showing Disabled Successfully ");
					}
				}
			
			
				catch(Throwable t)
				{	
					fail=true;
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Testers_TMnStakeDashDisabledError");
					
					t.printStackTrace();
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
