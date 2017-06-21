package com.uat.suite.tm_testpass;



import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyMandatoryFieldsValidation extends TestSuiteBase {

	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	
	String runmodes[]=null;

	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
		
		testManager=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyMandatoryFields(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL,
			String TestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
			String TP_EndYear,String TP_EndDate, String Assert_MandatoryFieldsText) throws Exception
	{
		// test the runmode of current dataset
		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			throw new SkipException("Runmode for test set data set to no "+count);
		}
		int testManager_count = 0;
		if(!TP_TestManager.isEmpty())
		testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		APP_LOGS.debug("Valid Test manager name is provided.");
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
		openBrowser();
		
		isLoginSuccess = login(role);
			
		if (isLoginSuccess) 
		{
			try 
			{
				//click on testManagement tab
				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(2000);
				
				
				if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, 
						versionLead.get(0).username))
				{
					fail=true;
					APP_LOGS.debug(ProjectName +" Project not Created Successfully.");
					comments=ProjectName +" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
					closeBrowser();
					assertTrue(false);
					throw new SkipException("Project Successfully not created");
					
				}
				
				APP_LOGS.debug(ProjectName+" Project Created Successfully.");
				comments= ProjectName+" Project Created Successfully(Pass). ";
				
				closeBrowser();
				
				openBrowser();
						
				if (login(versionLead.get(0).username, versionLead.get(0).password, "Version Lead")) 
				{
					getElement("UAT_testManagement_Id").click();
					Thread.sleep(2000);
					
					getElement("TM_testPassesTab_Id").click();
					Thread.sleep(1000);
					
					Thread.sleep(2000);
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
					
					selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
							"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
					
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
						
					selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
							"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
				
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
					
					selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
							"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
					
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
					
					selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
							"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);
					
					
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
					getObject("TestPasses_createNewProjectLink").click();
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
					getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
						
					getElement("TestPassCreateNew_descriptionTextField_ID").sendKeys(TP_Description);
					
					if(!TP_TestManager.isEmpty())
					{
						if(!enterTestManagerName(testManager.get(0).username))
						{
							fail = true;
							
							comments = comments+"Fail occurred: while providing Test Manager Name... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Cannot Fill Test Manager Name");

						}
					}
					
					if (!((TP_EndMonth.isEmpty())&&(TP_EndDate.isEmpty())&&(TP_EndDate.isEmpty()))) 
					{
						APP_LOGS.debug("Selecting the End date");
						if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
						{
							fail = true;
							
							comments = comments+"Fail occurred: while provinding End Date... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect End Date Provided");
						}  
					}
			
					APP_LOGS.debug("Click on Save");
					getObject("ProjectCreateNew_projectSaveBtn").click();
					
					try 
					{
						String textFromThePopupAfterSaveButton = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Text found on the popup is : "+textFromThePopupAfterSaveButton );
						
						System.out.println("Popup text : " + textFromThePopupAfterSaveButton);
						
						if (TestPassName.isEmpty()) 
						{
							//textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							if(!compareStrings(Assert_MandatoryFieldsText, textFromThePopupAfterSaveButton))
							{
								fail=true;
								comments+="When test pass name is not given, expected text was "+ Assert_MandatoryFieldsText+" but was not found as so (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected pop up text found");
								closeBrowser();
								throw new SkipException("Unexpected pop up text found");
							}
							
							APP_LOGS.debug("Click on OK Button");
							comments+="When test pass name is not given, expected text "+ Assert_MandatoryFieldsText+" was found (PASS)";
							Thread.sleep(1000);
							getObject("TestPassCreateNew_OkButtonForPopupOf55Charectors").click();
						
							
						}
						else if (TP_TestManager.isEmpty()) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							if(!compareStrings(Assert_MandatoryFieldsText, textFromThePopupAfterSaveButton))
							{
								fail=true;
								comments+="When test manager name is not given, expected text was "+ Assert_MandatoryFieldsText+" but was not found as so (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected pop up text found");
								closeBrowser();
								throw new SkipException("Unexpected pop up text found");
							}
							
							comments+="When test manager name is not given, expected text "+ Assert_MandatoryFieldsText+" was found (PASS)";
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
						
							getObject("TestPassCreateNew_OkButtonForPopupOf55Charectors").click();
											
						}
						else if (TP_EndDate.isEmpty()||TP_EndMonth.isEmpty()||TP_EndYear.isEmpty()) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							if(!compareStrings(Assert_MandatoryFieldsText, textFromThePopupAfterSaveButton))
							{
								fail=true;
								comments+="When end date is not given, expected text was "+ Assert_MandatoryFieldsText+" but was not found as so (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected pop up text found");
								closeBrowser();
								throw new SkipException("Unexpected pop up text found");
							}
							comments+="When end date is not given, expected text "+ Assert_MandatoryFieldsText+" was found (PASS)";
							APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
						
							getObject("TestPassCreateNew_OkButtonForPopupOf55Charectors").click();
											
						}
						else 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							if(!compareStrings(Assert_MandatoryFieldsText, textFromThePopupAfterSaveButton))
							{
								fail=true;
								comments+="With all the mandatory fields filled in, expected text was "+ Assert_MandatoryFieldsText+" but was not found as so (Fail)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected pop up text found");
								closeBrowser();
								throw new SkipException("Unexpected pop up text found");
							}
							
							comments+="With all the mandatory fields filled in, expected text "+ Assert_MandatoryFieldsText+" was found (PASS)";
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
						
							getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
						}
					} 
					catch (Exception e) 
					{
						fail = true;
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Xpath might not found while getting the popup text" );
						
						comments = comments+"Exception Occurred: Couldn't find the Popup Text...  ";
						
					}
				
				}
				else
				{
					isLoginSuccess=false;
				}	
			}
			catch (Throwable t) 
			{
				fail = true;
				
				comments = comments+"Exception Occurred: Test Case has been failed  ";
			}
			
			APP_LOGS.debug("Closing the browser");
			closeBrowser();	
				
			}else 
			{
				isLoginSuccess=false;
			}	
	}
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_testPassSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws InterruptedException
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}

}


