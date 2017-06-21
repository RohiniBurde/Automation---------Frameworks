package com.uat.suite.tm_testpass;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.suite.tm_testpass.TestSuiteBase;
import com.uat.util.TestUtil;

public class VerifyMandatoryFieldsValidation extends TestSuiteBase {

		static int count=-1;
		static boolean skip=false;
		static boolean pass=false;
		static boolean fail=false;
		static boolean isTestPass=true;
		static boolean isLoginSuccess=false;
		
		String runmodes[]=null;

		@BeforeTest
		public void checkTestSkip()
		{
			
			if(!TestUtil.isTestCaseRunnable(TM_TestPass_Suite_xls,this.getClass().getSimpleName()))
			{
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_TestPass_Suite_xls, this.getClass().getSimpleName());
		}
		
		@Test(dataProvider = "getTestData")
		public void VerifyMandatoryFields(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
				String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
				String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
				String TestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
				String TP_EndYear,String TP_EndDate) throws Exception
			{
			// test the runmode of current dataset
				count++;
				if(!runmodes[count].equalsIgnoreCase("Y"))
				{
					skip=true;
					throw new SkipException("Runmode for test set data set to no "+count);
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
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
						getObject("TestPasses_createNewProjectLink").click();
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
						getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
					
						//Providing Description
						if (TestPassName.equals("")||TP_TestManager.equals("")||TP_EndMonth.equals("")) 
						{
							APP_LOGS.debug("Found Blanks in Mandatory fields. So, Ignoring Test Pass Description");
						}
						else {
							APP_LOGS.debug("Found Valid Data in Mandatory fields. So, Writing in the Test Pass Description");
							
							getElement("TestPassCreateNew_descriptionTextField_ID").sendKeys(TP_Description);
						}
						
						
						//Providing Test Manager name	
						if (TP_TestManager.equals("")) 
						{
							APP_LOGS.debug("Provided Blank Name for Test Manager : "+TP_TestManager);
							
							try 
							{
								APP_LOGS.debug("Clicking on Test Manager Image Icon...");
								getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
								  
								driver.switchTo().frame(1);
								APP_LOGS.debug("Switched to the Version Lead frame...");
							  
								w = new WebDriverWait(driver,200);
								w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
							  
								APP_LOGS.debug(TP_TestManager + " : Input text in Test Manager text field...");
								getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(TP_TestManager); 
							   
								APP_LOGS.debug("Click on Search button from Version Lead frame ...");
								getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
							   
								String noResultFoundInPeoplPicker = getObject("TestPassCreateNew_noResultFoundMessageInSearchBoxOfPeoplePicker").getText();
								APP_LOGS.debug("Text Found is: "+noResultFoundInPeoplPicker);
								
								getObject("TestPassCreateNew_cancelButtonOfPeoplePicker").click();
								
								driver.switchTo().defaultContent();
							
							}
							catch(Throwable t)
							{
								fail = true;
								
								APP_LOGS.debug(this.getClass().getSimpleName()+" Test manager name provided is having some error.");
							}
						}
						else 
						{
							APP_LOGS.debug("Provided valid Name for Test Manager : "+TP_TestManager);
							enterTestManagerName(TP_TestManager);
						}
						
						if (TP_EndMonth.equals("")) 
						{
							APP_LOGS.debug("Provided Blank End Date. So directly saving the Test Pass");
						}
						else 
						{
							APP_LOGS.debug("Selecting the End date");
							selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate);  
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
							}
							else if (textFromThePopupAfterSaveButton.equals("Fields marked with asterisk(*) are mandatory!.")) 
							{
								APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
								
								String alertTextOfMarkWithAsteriskAreMandatory = getObject("TestPassCreateNew_alertTextOfMarkWithAsteriskAreMandatory").getText();
								
								try 
								{
									Assert.assertEquals(alertTextOfMarkWithAsteriskAreMandatory, "Fields marked with asterisk(*) are mandatory!.");	
									
								} 
								catch (Throwable t) 
								{
									fail = true;
									
									APP_LOGS.debug(this.getClass().getSimpleName()+" Assertion has been failed.");
								}
								
								APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
							
								getObject("TestPassCreateNew_OkButtonForPopupOf55Charectors").click();
								
							}
							else 
							{
								fail = true;
								
								APP_LOGS.debug(this.getClass().getSimpleName()+" Getting Unexpexted popup text");
							}
						} 
						catch (Exception e) 
						{
							fail = true;
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Xpath might not found while getting the popup text" );
							
						}

						deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName);
					
					}
					catch (Throwable t) 
					{
						fail = true;
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
			return TestUtil.getData(TM_TestPass_Suite_xls, this.getClass().getSimpleName());
		}

		@AfterMethod
		public void reportDataSetResult()
		{
			if(skip)
				TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
			}
			else if(fail)
			{
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			}
			else
			TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			skip=false;
			fail=false;
		}
		
		@AfterTest
		public void reportTestResult() throws InterruptedException
		{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, "Test Cases", TestUtil.getRowNum(TM_TestPass_Suite_xls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_TestPass_Suite_xls, "Test Cases", TestUtil.getRowNum(TM_TestPass_Suite_xls,this.getClass().getSimpleName()), "FAIL");
		}

}
