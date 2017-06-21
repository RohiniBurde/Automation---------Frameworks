package com.uat.suite.dashboard;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class VerifyUploadTemplate extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	String comments;
	boolean isLoginSuccess=true;
	Alert alert;
	Robot r;
	int myActivityTableRows;
	int totalPages;
	String projectCell;
	String versionCell;
	String testPassCell;
	String roleCell;
	int daysLeft;
	int notCompletedCount;
	int passCount;
	int failCount;
	String actionCell;
	String offlineTestingCell;
	String[] testStepArray;
	String[] splitFileName;
	boolean isAlertBoxDisplayed;
	boolean activeXResult = true;
	String fileExtension;

	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(dashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(dashboardSuiteXls, this.getClass().getSimpleName());
	}

	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyUploadTemplateFunctionality(String role, String groupName, String portfolioName, String projectName, String version,
			String projectEndMonth, String projectEndYear, String projectEndDate, String versionLeadName, String testPassName, String testManagerName, 
			String testPassEndMonth, String testPassEndYear, String testPassEndDate, String testerName, String testerRole, String testCase, 
			String testStep, String testStepExpectedResult, String testStepResult, String uploadWordDoc, String expectedMessageOtherThanXLXFile, 
			String uploadInvalidTemplate, String expectedMessageInvalidTemplate, String uploadValidTemplate1, String uploadValidTemplate2, 
			String expectedMessageValidTemplate) throws Exception
			{
		count++;
		comments="";

		ArrayList<Credentials> versionLead = getUsers("Version Lead", 1);
		ArrayList<Credentials> testManager = getUsers("Test Manager", 1);
		ArrayList<Credentials> tester = getUsers("Tester", 1);


		if(!(runmodes[count].equalsIgnoreCase("Y") && versionLead!=null && testManager!=null && tester!=null))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}


		APP_LOGS.debug("Opening Browser...for user "+role);
		openBrowser();

		isLoginSuccess = login(role);

		if(isLoginSuccess)
		{	

			//click on testManagement tab
			APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
			getElement("UAT_testManagement_Id").click();
			Thread.sleep(3000);

			//create project
			if(createProject(groupName, portfolioName, projectName, version, projectEndMonth, projectEndYear, projectEndDate, versionLead.get(0).username))
			{
				APP_LOGS.debug(" Project Created Successfully.");
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Project is not created successfully");
				comments+= "Project is not Created Successfully(Fail). ";
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Project is not created successfully ... So Skipping all tests in Dashboard Suite");
			}

			//create test pass
			if(createTestPass(groupName, portfolioName, projectName, version, testPassName, testPassEndMonth, testPassEndYear, testPassEndDate, testManager.get(0).username))
			{
				APP_LOGS.debug("Test Pass Created Successfully."); 
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Test Pass is not created successfully");
				comments+="Test Pass not Created Successfully(Fail). ";
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Test Pass is not created successfully ... So Skipping all tests in Dashboard Suite");
			}

			//create Tester
			if(createTester(groupName, portfolioName, projectName, version, testPassName, tester.get(0).username, testerRole, testerRole))
			{
				APP_LOGS.debug("Tester Created Successfully.");
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Tester is not created successfully");
				comments+="Tester not Created Successfully(Fail). ";
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Tester is not created successfully ... So Skipping all tests in Dashboard Suite");
			}

			//create test case
			if(createTestCase(groupName, portfolioName, projectName, version, testPassName, testCase))
			{
				APP_LOGS.debug("Test Case Created Successfully.");
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Test Case is not created successfully");
				comments+="Test Case not Created Successfully(Fail). ";
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Dashboard Suite");
			}

			//create test steps				
			if(createTestSteps(groupName, portfolioName, projectName, version, testPassName, testCase, testerRole, testStep, testStepExpectedResult))
			{
				APP_LOGS.debug("Test Step Created Successfully.");
			}
			else
			{
				fail=true;
				APP_LOGS.debug("Test Step is not created successfully");
				comments+="Test Step not Created Successfully(Fail). ";
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
				closeBrowser();

				throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Dashboard Suite");
			}

			closeBrowser();


			//login with tester user
			APP_LOGS.debug("Opening Browser...for user "+tester.get(0).username);

			openBrowser();
			if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			{

				//uploading other than excel file
				if(uploadTemplateFunction(projectName, version, testPassName, testerRole, uploadWordDoc))
				{
					if((activeXResult==false) && (fileExtension.equals("xlsx")))
					{
						APP_LOGS.debug("ActiveX is disabled..hence cannot perform upload template operation.");
						comments+="ActiveX is disabled..hence cannot perform upload template operation(Pass). ";
					}
					else
					{
						isAlertBoxDisplayed = isElementExistsById("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id");
						if(assertTrue(isAlertBoxDisplayed))
						{

							APP_LOGS.debug("verify alert on importing other than xls file");
							if(compareStrings(expectedMessageOtherThanXLXFile, getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()))
							{
								APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct");
								comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct(Pass). ";					            	
								getObject("DashboardMyActivity_offlineTestingImportTestStepAlertOkButton").click();
								Thread.sleep(1000);

							}
							else
							{
								fail=true;					            	
								APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect");
								comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "OtherThanExcelFileMsgIncorrect");
							}
						}
						else
						{
							fail=true;
							APP_LOGS.debug("No alert box displays. ");
							comments+="No alert box displays(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AlertBoxNotVisible");
						}
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug(uploadWordDoc+" file uploading operation unsuccessful.");
					comments+=uploadWordDoc+" file uploading operation unsuccessful(Fail). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), uploadWordDoc+"FileUploadOperationUnsuccessfull");
				}

				//uploading invalid xlsx template
				if(uploadTemplateFunction(projectName, version, testPassName, testerRole, uploadInvalidTemplate))
				{
					if((activeXResult==false) && (fileExtension.equals("xlsx")))
					{
						APP_LOGS.debug("ActiveX is disabled..hence cannot perform upload template operation.");
						comments+="ActiveX is disabled..hence cannot perform upload template operation(Pass). ";
						closeBrowser();

						throw new SkipException("ActiveX is disabled..hence cannot upload template");

					}
					else
					{
						isAlertBoxDisplayed = isElementExistsById("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id");
						if(assertTrue(isAlertBoxDisplayed))
						{

							APP_LOGS.debug("verify alert on importing other invalid xls file");
							if(compareStrings(expectedMessageInvalidTemplate, getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText()))
							{
								APP_LOGS.debug(getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText()+ " message is correct");
								comments+=getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText()+ " message is correct(Pass).";
								getObject("DashboardMyActivity_offlineTestingImportTestStepAlertOkButton").click();
								Thread.sleep(1000);

							}
							else
							{
								fail=true;
								APP_LOGS.debug(getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText()+ " message is incorrect");
								comments+=getElement("TestStepsImportExport_importOtherThanXLXFile_Text_Id").getText()+ " message is incorrect(Fail). ";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "InvalidTemplateMsgIncorrect");
							}
						}
						else
						{
							fail=true;
							APP_LOGS.debug("No alert box displays. ");
							comments+="No alert box displays(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AlertBoxNotVisible");
						}
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug(uploadInvalidTemplate+" file uploading operation unsuccessful.");
					comments+=uploadInvalidTemplate+" file uploading operation unsuccessful(Fail). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), uploadInvalidTemplate+"FileUploadOperationUnsuccessfull");

				}


				//uploading valid template
				//Begin Testing
				if(uploadTemplateFunction(projectName, version, testPassName, testerRole, uploadValidTemplate1))
				{
					if((activeXResult==false) && (fileExtension.equals("xlsx")))
					{
						APP_LOGS.debug("ActiveX is disabled..hence cannot perform upload template operation.");
						comments+="ActiveX is disabled..hence cannot perform upload template operation(Pass).";
						closeBrowser();

						throw new SkipException("ActiveX is disabled..hence cannot upload template");
					}
					else
					{
						isAlertBoxDisplayed = isElementExistsById("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id");
						if(assertTrue(isAlertBoxDisplayed))
						{
							if(compareStrings(expectedMessageValidTemplate, getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()))
							{
								APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct");
								comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct(Pass).";
								getObject("DashboardMyActivity_offlineTestingImportTestStepAlertOkButton").click();
								Thread.sleep(2000);
							}
							else
							{
								fail=true;
								APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect");
								comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect(Fail).";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ValidTemplateMsgIncorrect");
								closeBrowser();

								throw new SkipException("Testing done successfully message incorrect ... So Skipping all tests in Dashboard Suite");
							}
						}
						else
						{
							fail=true;
							APP_LOGS.debug("No alert box displays. ");
							comments+="No alert box displays(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AlertBoxNotVisible");
							closeBrowser();

							throw new SkipException("'Testing done successfully' message not shown after uploading template ... So Skipping all tests in Dashboard Suite");
						}
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug(uploadValidTemplate1+" file uploading operation unsuccessful.");
					comments+=uploadValidTemplate1+" file uploading operation unsuccessful(Fail). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), uploadValidTemplate1+"FileUploadOperationUnsuccessfull");
				}


				//verify updated count after uploading template
				if(verifyUpdatedCount(projectName, version, testPassName, testerRole, notCompletedCount, passCount, failCount, uploadValidTemplate1, "Continue Testing"))
				{
					APP_LOGS.debug("Count for Not Completed, Pass and Fail is updated in My Activity for Test Pass ' " +testPassCell+" .");
					comments+="Count for Not Completed, Pass and Fail is updated in My Activity for Test Pass ' " +testPassCell+" (Pass).";
				}

				else
				{
					fail=true;
					APP_LOGS.debug("Count for Not Completed, Pass and Fail is not updated in My Activity for Test Pass ' " +testPassCell+" . and action column is still Begin Testing. ");
					comments+="Count for Not Completed, Pass and Fail is not updated in My Activity for Test Pass ' " +testPassCell+" .and action column is still Begin Testing(Fail- ). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsCountNotUpdated");
					closeBrowser();

					throw new SkipException("Action is not changed to continue testing ... So Skipping all tests in Dashboard Suite");
				}


				// Continue testing
				if(uploadTemplateFunction(projectName, version, testPassName, testerRole, uploadValidTemplate2))
				{
					isAlertBoxDisplayed = isElementExistsById("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id");
					if(assertTrue(isAlertBoxDisplayed))
					{
						if(compareStrings(expectedMessageValidTemplate, getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()))
						{
							APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct");
							comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is correct(Pass).";
							getObject("DashboardMyActivity_offlineTestingImportTestStepAlertOkButton").click();
							Thread.sleep(2000);

						}
						else
						{
							fail=true;
							APP_LOGS.debug(getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect");
							comments+=getElement("DashboardMyActivity_offlineTestingImportTestStepGuidelineAlert_Id").getText()+ " message is incorrect(Fail).";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ValidTemplateMsgIncorrect");
							closeBrowser();

							throw new SkipException("'Testing done successfully' message not shown after uploading template ... So Skipping all tests in Dashboard Suite");
						}
					}
					else
					{
						fail=true;
						APP_LOGS.debug("No alert box displays. ");
						comments+="No alert box displays(Fail). ";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AlertBoxNotVisible");
						closeBrowser();

						throw new SkipException("Testing not done after uploading template ... So Skipping all tests in Dashboard Suite");
					}
				}
				else
				{
					fail=true;
					APP_LOGS.debug(uploadValidTemplate2+" file uploading operation unsuccessful.");
					comments+=uploadValidTemplate2+" file uploading operation unsuccessful(Fail). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), uploadValidTemplate2+"FileUploadOperationUnsuccessfull");
				}

				//verify updated count
				if(verifyUpdatedCount(projectName, version, testPassName, testerRole, notCompletedCount, passCount, failCount, uploadValidTemplate2, "Testing Complete"))
				{
					APP_LOGS.debug("Count for Not Completed, Pass and Fail is updated in My Activity for Test Pass ' " +testPassCell+" .");
					comments+="Count for Not Completed, Pass and Fail is updated in My Activity for Test Pass ' " +testPassCell+" (Pass). ";
				}

				else
				{
					fail=true;
					APP_LOGS.debug("Count for Not Completed, Pass and Fail is not updated in My Activity for Test Pass ' " +testPassCell+" . and action column is stil continue testing. ");
					comments+="Count for Not Completed, Pass and Fail is not updated in My Activity for Test Pass ' " +testPassCell+" .and action column is stil continue testing(Fail). ";
					assertTrue(false);
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepsCountNotUpdated");
				}

				closeBrowser();
			}
			else
			{
				APP_LOGS.debug("Login Unsuccessfull for Tester "+tester.get(0).username);
				comments+="Login Unsuccessfull for Tester "+tester.get(0).username;
			}
		}
		else
		{
			APP_LOGS.debug("Login Unsuccessfull for user "+role);
			comments+="Login Unsuccessfull for user "+role;
		}

			}



	//create test steps	
	private boolean createTestSteps(String group, String portfolio, String project, String version, String testPassName, 
			String testCasesToBeSelected, String rolesToBeSelected, String testStep, String testStepExpectedResult) throws IOException, InterruptedException
	{
		//APP_LOGS.debug("Creating Test Step");
		Thread.sleep(2000);

		getElement("TestStepNavigation_Id").click();
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
		}
		Thread.sleep(2000);	


		try {

			dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), group );

			dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );

			dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), project );

			dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), version );

			dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );

			getObject("TestStep_createNewProjectLink").click();
			Thread.sleep(1000);

			List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
			int numOfTestCases = TestCaseSelectionNames.size();
			for(int i = 0; i<numOfTestCases;i++)
			{
				if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
				{
					getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
					break;
				}
			}

			testStepArray = testStep.split(",");
			for(int k =0;k<testStepArray.length;k++)
			{

				String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testStepArray[k])+"')";
				eventfiringdriver.executeScript(testStepDetails);

				String[] testStep_ExpectedResult = testStepExpectedResult.split(",");
				getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStep_ExpectedResult[k]); 

				String[] testerRoleSelectionArray = rolesToBeSelected.split(",");
				List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
				int numOfRoles = roleSelectionNames.size();
				for(int i = 0; i<numOfRoles;i++)
				{
					for(int j = 0; j < testerRoleSelectionArray.length;j++)
					{
						if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
						{
							getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
						}
					}
				}

				getElement("TestStepCreateNew_testStepSaveBtn_Id").click();

				Thread.sleep(2000);

				if (getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText().contains("successfully")) 
				{
					getObject("TestStep_testStepaddedsuccessfullyOkButton").click();

				}
				else 
				{
					return false;
				}

			}
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			fail=true;
			return false;
		}

	}

	
    //code for uploading file, verifying existance of activeX
	private boolean uploadTemplateFunction(String project, String version, String testPass, String testerRole, String fileName)
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean projectFound=false;
		
		try
		{

			//function call
			if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on My Activity Grid.");
				totalPages=1;
				paginationCount=1;
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on My Activity Grid. Calculating total pages.");
				totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}

			nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
				myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();

				for (int j = 1; j <= myActivityTableRows; j++) 
				{
					projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
					versionCell = getObject("DashboardMyActivity_versionColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
					testPassCell = getObject("DashboardMyActivity_testPassNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
					roleCell = getObject("DashboardMyActivity_roleNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");
					daysLeft = Integer.parseInt(getObject("DashboardMyActivity_daysLeftColumn1", "DashboardMyActivity_daysLeftColumn2", j).getText());
					notCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_notCompletedCountColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());
					passCount = Integer.parseInt(getObject("DashboardMyActivity_passCountColumn1", "DashboardMyActivity_passCountColumn2", j).getText());
					failCount = Integer.parseInt(getObject("DashboardMyActivity_failCountColumn1", "DashboardMyActivity_failCountColumn2", j).getText());
					actionCell = getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).getText();
					offlineTestingCell = getObject("DashboardMyActivity_offlineTestingUploadTemplateIconColumn1", "DashboardMyActivity_offlineTestingUploadTemplateIconColumn2", j).getAttribute("title");

					if (projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole)) 
					{

						//verifying Days Left & Not Completed count
						if((daysLeft>0) && (notCompletedCount>0) && (passCount==0) && (failCount==0) && 
								(actionCell.equals("Begin Testing")) &&(offlineTestingCell.equals("Upload Testing Template")))
						{

							try
							{
								eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
								Thread.sleep(2000);
							}
							catch(Throwable t)
							{
								activeXResult = false;
							}

							getObject("DashboardMyActivity_offlineTestingUploadTemplateIconColumn1", "DashboardMyActivity_offlineTestingUploadTemplateIconColumn2", j).click();
							Thread.sleep(5000);

							try
							{

								APP_LOGS.debug("switching to alert for uploading a file");			            					
								alert = eventfiringdriver.switchTo().alert();
								Thread.sleep(1500);

								alert.sendKeys(System.getProperty("user.dir")+"\\Upload_Templates\\"+fileName);

								Thread.sleep(2000);
								r = new Robot();

								r.keyPress(KeyEvent.VK_ENTER);
								Thread.sleep(3000);

								//spliting file name
								splitFileName = fileName.split("\\.");
								fileExtension = splitFileName[1];

								if(fileExtension.equals("xlsx"))
								{
										//verifying if actives is disabled or not
										if(activeXResult==false)
										{
												//Active x code
												wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
												if(assertTrue(isElementExistsById("TestStepsImportExport_activeXMessage_Id")))
												{ 
													skip=true;
													APP_LOGS.debug("Activex is disabled..and the popup displayed");
													comments+="Activex is disabled..and the popup displayed(Pass). ";
													getObject("TestStepCreateNew_TestStepActiveX_Close").click();
													return true;
												}
												else
												{
													fail=true;
													APP_LOGS.debug("Activex is disabled..but the popup not displayed");
													comments+="Activex is disabled..but the popup not displayed(Fail)";
													TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ActivexDisabledPopupNotVisible");
													return false;
												}
										}
								}

								Thread.sleep(4000);
								projectFound=true;
								return true;
							}
							catch(Throwable t)
							{
								fail=true;
								APP_LOGS.debug("Error in uploading file "+fileName);
								comments+="Fail - "+ "Error in uploading file "+fileName;
								t.printStackTrace();
								return false;
							}
                            
						}
						else if((daysLeft>0) && (notCompletedCount>0) && (passCount!=0) && (failCount!=0) && 
								(actionCell.equals("Continue Testing")) &&(offlineTestingCell.equals("Upload Testing Template")))
						{

							getObject("DashboardMyActivity_offlineTestingUploadTemplateIconColumn1", "DashboardMyActivity_offlineTestingUploadTemplateIconColumn2", j).click();
							Thread.sleep(5000);

							try
							{
								APP_LOGS.debug("switching to alert for uploading a file");			            					
								alert = eventfiringdriver.switchTo().alert();
								Thread.sleep(1500);

								alert.sendKeys(System.getProperty("user.dir")+"\\Upload_Templates\\"+fileName);

								Thread.sleep(2000);
								r = new Robot();

								r.keyPress(KeyEvent.VK_ENTER);
								Thread.sleep(4000);
								projectFound=true;
								return true;
							}
							catch(Throwable t)
							{
								fail=true;
								APP_LOGS.debug("Error in uploading file "+fileName);
								comments+="Fail - "+ "Error in uploading file "+fileName;
								t.printStackTrace();
								return false;
							}
                            
						}
						else if((daysLeft>0) && (notCompletedCount>0) && (passCount!=0) && (failCount!=0) && 
								(actionCell.equals("Testing Complete")) &&(offlineTestingCell.equals("Upload Testing Template")))
						{
							APP_LOGS.debug(testPass+" execution is complete");
							projectFound=true;
							return true;
						}

					}
				}
				if (totalPages>1 && projectFound==false) 
				{
				    	if(isElementExistsByXpath("DashboardMyActivity_NextLink"))
				    	{
				    		nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
				    		getObject("DashboardMyActivity_NextLink").click();
							Thread.sleep(500);
				    	}
				    	else
				    	{
				    		APP_LOGS.debug("Next link disabled.");
				    	}
				}
			    else
			    {
			    	break;
			    }

			}
			APP_LOGS.debug(testPass+" Not found in My Activity grid");
			comments+=testPass+" Not found in My Activity grid(Fail).";
			return false;
		}
		catch(Throwable t)
		{
			fail=true;
			APP_LOGS.debug("Exception in Uploading Template operation");
			comments+="Exception in Uploading Template operation. ";
			t.printStackTrace();
			return false;
		}

	}


	//veriofy updated count in my activity grid	
	private boolean verifyUpdatedCount(String project, String version, String testPass, String testerRole,
			int notCompletedCount, int passCount, int failCount, String fileName, String actionCellStatus) throws IOException
	{
		boolean nextLinkEnabled;
		int paginationCount=0;
		boolean projectFound=false;
		
		try
		{
			//reading offline testing template and getting count of rows
			Xls_Reader offlineTestingResult = new Xls_Reader(System.getProperty("user.dir")+"\\Upload_Templates\\"+fileName);
			int rowCount = offlineTestingResult.getRowCount("Testing Template");

			int countPass = 0;
			int countFail = 0;
			int countNotCompleted = 0;
			for(int i = 13; i<= rowCount-2 ; i++)
			{
				if(offlineTestingResult.getCellData("Testing Template", 4, i).equals("Pass"))
					countPass++;
				else if (offlineTestingResult.getCellData("Testing Template", 4, i).equals("Fail"))
					countFail++;
				else countNotCompleted++;

			}
			int updatedNotCompletedCount;
			int updatedPassCount;
			int updatedFailCount;

			if (getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				totalPages=1;
				paginationCount=1;
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
				totalPages = getElement("DashboardMyActivity_Pagination_Id").findElements(By.xpath("div/a")).size();
			}

			nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
			while(nextLinkEnabled==true || paginationCount==1)
			{
				myActivityTableRows = getObject("DashboardMyActivity_table").findElements(By.tagName("tr")).size();

				for (int j = 1; j <= myActivityTableRows; j++) 
				{
					projectCell = getObject("DashboardMyActivity_projectNameColumn1", "DashboardMyActivity_projectNameColumn2", j).getAttribute("title");
					versionCell = getObject("DashboardMyActivity_versionColumn1", "DashboardMyActivity_versionColumn2", j).getAttribute("title");
					testPassCell = getObject("DashboardMyActivity_testPassNameColumn1", "DashboardMyActivity_testPassNameColumn2", j).getAttribute("title");
					roleCell = getObject("DashboardMyActivity_roleNameColumn1", "DashboardMyActivity_roleNameColumn2", j).getAttribute("title");
					updatedNotCompletedCount = Integer.parseInt(getObject("DashboardMyActivity_notCompletedCountColumn1", "DashboardMyActivity_notCompletedCountColumn2", j).getText());
					updatedPassCount = Integer.parseInt(getObject("DashboardMyActivity_passCountColumn1", "DashboardMyActivity_passCountColumn2", j).getText());
					updatedFailCount = Integer.parseInt(getObject("DashboardMyActivity_failCountColumn1", "DashboardMyActivity_failCountColumn2", j).getText());
					actionCell = getObject("DashboardMyActivity_actionColumn1", "DashboardMyActivity_actionColumn2", j).getText();

					if ((projectCell.equals(project) && versionCell.equals(version) && testPassCell.equals(testPass) && roleCell.equals(testerRole)
							&& (updatedNotCompletedCount==countNotCompleted) && (updatedPassCount==countPass) && (updatedFailCount==countFail) && actionCell.equals(actionCellStatus))) 
					{

						APP_LOGS.debug(testPass+" found in My Activity grid for verifying updated count");
						comments+=testPass+" found in My Activity grid for verifying updated count.(Pass)";
						projectFound=true;
						return true;
					}
				}
				if (totalPages>1 && projectFound==false) 
				{
				    	if(isElementExistsByXpath("DashboardMyActivity_NextLink"))
				    	{
				    		nextLinkEnabled = isElementExistsByXpath("DashboardMyActivity_NextLink");
				    		getObject("DashboardMyActivity_NextLink").click();
							Thread.sleep(500);
				    	}
				    	else
				    	{
				    		APP_LOGS.debug("Next link disabled.");
				    	}
				}
			    else
			    {
			    	break;
			    }
			  
			}
			APP_LOGS.debug(testPass+" Not found in My Activity grid for verifying updated count");
			comments+=testPass+" Not found in My Activity grid for verifying updated count. ";	
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			fail=true;
			return false;
		}

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
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(dashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)		
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else		
			TestUtil.reportDataSetResult(dashboardSuiteXls, "Test Cases", TestUtil.getRowNum(dashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");


	}

	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(dashboardSuiteXls, this.getClass().getSimpleName()) ;
	}


}
