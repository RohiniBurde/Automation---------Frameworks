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

public class VerifyTPNameLength extends TestSuiteBase 
{

		static int count=-1;
		static boolean skip=false;
		static boolean pass=false;
		static boolean fail=false;
		static boolean isTestPass=true;
		static boolean isLoginSuccess=false;
		boolean alertTextForMoreThan55Charector = false;
		String runmodes[]=null;
		ArrayList<Credentials> testManager;
		String comments;
		
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
			//testManager=getUsers("Test Manager", 1)
		}
		
		@Test(dataProvider = "getTestData")
		public void VerifyToAcceptCombiOfAlphabets(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
				String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
				String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL, String ExpectedMessageText,
				String TestPassName,String TP_Description,String TP_Status,String TP_TestManager, String TP_EndMonth,
				String TP_EndYear,String TP_EndDate, String Assert_55CharectorsText) throws Exception
			{
			// test the runmode of current dataset
				count++;
				
				if(!runmodes[count].equalsIgnoreCase("Y"))
				{
					skip=true;
					
					throw new SkipException("Runmode for test set data set to no "+count);
				}
				
				int testManager_count = Integer.parseInt(TP_TestManager);
				testManager = getUsers("Test Manager", testManager_count);
				
				APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
				
				openBrowser();
				
				isLoginSuccess = login(role);
				
				if (isLoginSuccess) 
				{
					//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(2000);
					
					//Click on Test Pass
					getElement("TM_testPassesTab_Id").click();
					
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
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
					getObject("TestPasses_createNewProjectLink").click();
					
					APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
					getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
					
					//Providing Test Manager name	
					APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
					if(enterTestManagerName(testManager.get(0).username))
					{
						comments = "Test Manager Name added successfully... ";
					}
					else{
						fail =  true;
						
						comments = "Fail Occurred: while adding Test Manager Name... ";
					
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "adding Test Manager Name");
					}
					
					APP_LOGS.debug("Selecting the End date");
					if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
					{
						fail = true;
						
						comments = comments+"Fail Occurred: while adding End Date... ";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "while adding End Date");
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
						else if (textFromThePopupAfterSaveButton.equals("Test Pass name should contain only 55 characters.")) 
						{
							APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
							
							String alertTextForMoreThan55Charectors = getObject("TestPassCreateNew_alertTextFor55Charectors").getText();
							
							try 
							{
								compareStrings(alertTextForMoreThan55Charectors, Assert_55CharectorsText);
							
								//Assert.assertEquals(alertTextForMoreThan55Charectors, "Test Pass name should contain only 55 characters.");	
								
								alertTextForMoreThan55Charector = true;
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
							
							APP_LOGS.debug(this.getClass().getSimpleName()+" Different Popup text from expected is getting");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Incorrect Popup text");
						}
					} 
					catch (Exception e) 
					{
						fail = true;
						
						APP_LOGS.debug(this.getClass().getSimpleName()+" Xpath might not found while getting the popup text" );
						
						closeBrowser();
					}
					
					Thread.sleep(1000);
					//Goto View all Page and delete test pass created.
					
					deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(TestPassName);
					
				/*	APP_LOGS.debug("Click on view all page");
					
					getObject("TestPasses_viewAllProjectLink").click();
					
					int numberOfRowsPresentOnViewAllPAge = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'showTestPass']/tr")).size();
					
					System.out.println("numberOfRowsPresentOnViewAllPAge "+ numberOfRowsPresentOnViewAllPAge);
					
					for (int i = 1; i <=numberOfRowsPresentOnViewAllPAge ; i++) 
					{
						
						String testPassNameFromViewAllPage = getObject("TestPassViewAll_testPassNameFromViewAllPage1", "TestPassViewAll_testPassNameFromViewAllPage2", i).getText();
						
						if (testPassNameFromViewAllPage.equals(TestPassName)) 
						{
							APP_LOGS.debug("Deleting Test Pass");
							
							getObject("TestPassViewAll_deleteTestPassNameFromViewAllPage1", "TestPassViewAll_deleteTestPassNameFromViewAllPage2", i).click();
							
							getObject("TestPassViewAll_popUpDeleteButton").click();
							
							getObject("TestPassViewAll_deleteConfirmOkButton").click();
						}
						else 
						{
							APP_LOGS.debug("Test Pass name Not Found.");
						}
					}*/
					
				closeBrowser();	
					
				}else 
				{
					APP_LOGS.debug("Login NOT SUCCESSFUL");
					
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
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
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
		Thread.sleep(2000);
		closeBrowser();
		}

}
