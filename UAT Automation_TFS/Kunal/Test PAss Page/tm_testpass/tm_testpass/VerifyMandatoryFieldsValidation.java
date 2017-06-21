package com.uat.suite.tm_testpass;



import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
	
	ArrayList<Credentials> testManager;
	
	String runmodes[]=null;

	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_testPassSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testPassSuiteXls, this.getClass().getSimpleName());
		
		testManager=new ArrayList<Credentials>();
		//testManager=getUsers("Test Manager", 1);
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyMandatoryFields(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
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
			try 
			{
				int testManager_count = Integer.parseInt(TP_TestManager);
				testManager = getUsers("Test Manager", testManager_count);
				APP_LOGS.debug("Valid Test manager name is porvided.");
			} 
			catch (Throwable t) 
			{
				APP_LOGS.debug("Test manager name porvided is blank.");
			}
		
			
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
					Thread.sleep(3000);
					
					getElement("TM_testPassesTab_Id").click();
					Thread.sleep(1000);
					
					Thread.sleep(2000);
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
					
					selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
							"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
					
					
					/*APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
					
					selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
							"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
				
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
					
					selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
							"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
					
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
					
					selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
							"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);*/
					
					
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
					getObject("TestPasses_createNewProjectLink").click();
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
					getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
				
					//Providing Description
					if (TestPassName.equals("")||TP_TestManager.equals("")||TP_EndMonth.equals("")) 
					{
						APP_LOGS.debug("Found Blanks in Mandatory fields. So, Ignoring Test Pass Description");
					}
					else 
					{
						APP_LOGS.debug("Found Valid Data in Mandatory fields. So, Writing in the Test Pass Description");
						
						getElement("TestPassCreateNew_descriptionTextField_ID").sendKeys(TP_Description);
					}
					
					//Providing Test Manager name	
					if (TP_TestManager.equals("")) 
					{
						APP_LOGS.debug("Providing Blank Name for Test Manager : "+testManager.get(0).username);
						
						try 
						{
							APP_LOGS.debug("Clicking on Test Manager Image Icon...");
							getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
							  
							driver.switchTo().frame(1);
							APP_LOGS.debug("Switched to the Version Lead frame...");
						  
							wait = new WebDriverWait(driver,200);
							wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
						  
							APP_LOGS.debug(testManager.get(0).username + " : Input text in Test Manager text field...");
							getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(testManager.get(0).username); 
						   
							APP_LOGS.debug("Click on Search button from Version Lead frame ...");
							getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
						   
							String noResultFoundInPeoplPicker = getObject("TestPassCreateNew_noResultFoundMessageInSearchBoxOfPeoplePicker").getText();
							APP_LOGS.debug("Text Found is: "+noResultFoundInPeoplPicker);
							
							getObject("TestPassCreateNew_cancelButtonOfPeoplePicker").click();
							
							driver.switchTo().defaultContent();
							
							comments = "Blank Test manager name provided... ";
						
						}
						catch(Throwable t)
						{
							fail = true;
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Test manager name provided is having some error.");
							
							comments = "Exception occurred: Test manager name provided is having some error.... ";
							
						}
					}
					else 
					{
						APP_LOGS.debug("Provided valid Name for Test Manager : "+testManager.get(0).username);
						
						comments = "Valid Test manager name has provided... ";
						
						if(!enterTestManagerName(testManager.get(0).username))
						{
							fail =  true;
							
							comments = "Fail occurred: Test manager name provided is having some error.... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect Test Manager Name");
						}
					}
					
					if (TP_EndMonth.equals("")) 
					{
						APP_LOGS.debug("Provided Blank End Date. So directly saving the Test Pass");
						
						comments = comments+ "Blank End Date has provided... ";
					}
					else 
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
						
						if (textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							APP_LOGS.debug("Click on OK Button");
							
							Thread.sleep(1000);
							
							getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
							
							comments = comments+" Test Pass Added Successfully is displayed on popup.... ";
						}
						else if (textFromThePopupAfterSaveButton.equals("Fields marked with asterisk(*) are mandatory!.")) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							String alertTextOfMarkWithAsteriskAreMandatory = getObject("TestPassCreateNew_alertTextOfMarkWithAsteriskAreMandatory").getText();
							
							compareStrings(alertTextOfMarkWithAsteriskAreMandatory, Assert_MandatoryFieldsText);
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
						
							getObject("TestPassCreateNew_OkButtonForPopupOf55Charectors").click();
							
							comments = comments+"Fields marked with asterisk(*) are mandatory!. is displayed on popup...  ";
							
						}
						else 
						{
							fail = true;
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Getting Unexpexted popup text");
							
							comments = comments+"Failed Occurred: Getting Unexpexted popup text aftre click on Save button...  ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Popup text is unexpected");
						}
					} 
					catch (Exception e) 
					{
						fail = true;
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Xpath might not found while getting the popup text" );
						
						comments = comments+"Exception Occurred: Couldn't find the Popup Text...  ";
						
					}

					deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName);
				
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


