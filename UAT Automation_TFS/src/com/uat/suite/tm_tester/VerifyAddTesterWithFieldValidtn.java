package com.uat.suite.tm_tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyAddTesterWithFieldValidtn extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	Utility utilRecorder = new Utility();
	
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip() throws Exception{
		APP_LOGS.debug("Executing Test Case "+this.getClass().getSimpleName());	
		if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
	}
	
			
	// Test Case Implementation ...
	@Test(dataProvider="getTestData")
	public void testVerifyAddTesterWithFieldValidation(String Role, String GroupName, String Portfolio, String Project,
			String Version,String TestPassName,String TP_Tester,String TesterRoleCreation, String TesterRoleSelection,
			String EndMonth,String EndYear,String EndDate,String TestManager,String VersionLead,String tester_description,
			String tester_Role,String ExpectedAlertforRole,String ExpectedPageHeadingText,String AddTesterAlertText1,
			String AddTesterAlertText2,String SelectedArea, String ExpectedSelectRoleText, String ExpectedSelectTesterText) throws Exception
			{
				count++;
				comments = "";
				
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
				APP_LOGS.debug("Opening Browser... ");
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
				//Step 1:  clicking on Test Management Tab
					try
					{	
						//Thread.sleep(2000);
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(500);
						//Step 2:  clicking on Project Tab
						getElement("TM_projectsTab_Id").click();
						Thread.sleep(500);
						
						if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
						{
							APP_LOGS.debug("Project Creation fails");
							comments=comments+"Project Creation Fails";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingProject");							
							throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Project is not created successfully");//reports
						}
					
						//Thread.sleep(1000);
						closeBrowser();
						
						openBrowser();
						APP_LOGS.debug("Opening Browser... ");
						
						//version lead login here
						if(login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead"))
						{
							//Thread.sleep(1000);
							//clicking on Test Management Tab
							getElement("UAT_testManagement_Id").click();
							APP_LOGS.debug("Clicking On Test Management Tab ");
							Thread.sleep(500);
							//clicking on Test Pass Tab
							getElement("TM_testersTab_Id").click();
							Thread.sleep(500);
							APP_LOGS.debug("Clicking On Testers Tab ");
					
							if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
							{
								getObject("TesterCreateNew_TesterActiveX_Close").click();
							}
							
							Thread.sleep(500);				
					
							dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
							dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
							dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
							dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
							dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
					
							if(!compareStrings(getObject("Testers_testersDetailsHeading").getText(),ExpectedPageHeadingText))
							{
								APP_LOGS.debug("User " + Role+ " is not landing on 'Testers' Page successfully ");
								comments="User " + Role+ " did not land on 'Testers' Page successfully (FAIL)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "User " + Role+ " did not land on 'Testers' Page successfully");
								throw new SkipException("User " + Role+ " did not land on 'Testers' Page successfully");
							}
					
							//Thread.sleep(1000);
							getObject("Testers_addTesterLink").click();
							
							if(assertTrue(getElement("TesterAddTester_AlertText_ID").isDisplayed()))
							{
								comments+="As tester is not created, expected alert appeared (PASS)";
								String actualAlertAddTestPass = waitForElementVisibility("TesterAddTester_AlertText_ID",10).getText();//getTextFromAlert(getElement("TesterAddTester_AlertText_ID"));
								
								if(!compareStrings(AddTesterAlertText1+Project+AddTesterAlertText2, actualAlertAddTestPass))
								{
									comments=comments+ "Expected validation message is not prompted for Add Test Pass(FAIL)";
									fail=true;
									
								}
								//Thread.sleep(1000);
								getObject("TesterAddTester_selectCancelButton").click();
							}
							else
							{
								comments+="As tester is not created, expected alert did not appear (FAIL)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "As tester is not created, expected alert did not appear");
								fail=true;
							}
							
							
							//create the test pass with the version lead
							Thread.sleep(1000);
							if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
							{
								APP_LOGS.debug("Failed to create the Test Pass");
								comments=comments+"Failed to create the Test Pass (FAIL)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
								throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
							}
							
							//Thread.sleep(1000);
							closeBrowser();
							
							openBrowser();
							APP_LOGS.debug("Opening Browser... ");
							
							if(login(testManager.get(0).username,testManager.get(0).password,"Test Manager"))
							{
								//Thread.sleep(1000);
								//clicking on Test Management Tab
								getElement("UAT_testManagement_Id").click();
								APP_LOGS.debug("Clicking On Test Management Tab ");
								
								Thread.sleep(500);
								getElement("TM_testersTab_Id").click();
								Thread.sleep(500);
								APP_LOGS.debug("Clicking On Testers Tab ");
								
								if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
								{
									getObject("TesterCreateNew_TesterActiveX_Close").click();
								}
								
								Thread.sleep(1000);
								dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
								Thread.sleep(500);
								dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
							
								Thread.sleep(500);
								
								getObject("Testers_addTesterLink").click();
								Thread.sleep(500);
								//verifying the prompt text oF POP UPS
								if(selectRole(TesterRoleCreation, TesterRoleSelection))
								{
									getObject("TesterDetailsAddTester_SaveBtn").click();
									APP_LOGS.debug("Tester please select Role first");
									if(!(compareStrings(ExpectedSelectTesterText.toLowerCase(), waitForElementVisibility("ProjectEdit_validationMessage_Id",10).getText().toLowerCase())))
									{
										fail=true;
										APP_LOGS.debug("Tester not created successfully Role Block Testers Message");
										comments=comments+"Expected Select Role Text did not match (FAIL)";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExpectedSelectRoleText did not match");
									}
									
									getObject("TesterAddTester_selectOkButton").click();
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unable to create//select role");
									assertTrue(false);
								}
								
								List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
								int numOfRoles = roleSelectionNames.size();
								for(int i = 0; i<numOfRoles;i++)
								{
									if((roleSelectionNames.get(i)).getAttribute("title").equals(TesterRoleSelection))
									{
										getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
										break;
									}
									
								}
								
								if(selectTester(tester.get(0).username))
								{
									Thread.sleep(1000);
									getObject("TesterDetailsAddTester_SaveBtn").click();
									APP_LOGS.debug("Clicking on save button");
									if(!(compareStrings(ExpectedSelectRoleText.toLowerCase(), waitForElementVisibility("ProjectEdit_validationMessage_Id",10).getText().toLowerCase())))
									{
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Expected role (to be checked) validation message did not appear");
										fail=true;
										APP_LOGS.debug("Tester not created successfully Tester Block Role Message");
										comments=comments+"Expected role (to be checked) validation message did not appear (FAIL)";
									}
									Thread.sleep(1000);
									getObject("TesterAddTester_selectOkButton").click();
								}
								else
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unable to create tester");
									assertTrue(false);
								}
								
								getElement("TesterAddTester_addRole_Id").click();
								
								//sets the description
								if(!selectDescription(tester_description))
								{
									fail=true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unable to type description");
									assertTrue(false);
								}
								getElement("TesterAddTester_addRoleButton_Id").click();
								
								if(!compareStrings(ExpectedAlertforRole, waitForElementVisibility("ProjectEdit_validationMessage_Id",10).getText()))
								{
									fail=true;
									comments=comments+"Expected role (to be typed with description) validation message did not appear (FAIL)";
								}
								Thread.sleep(1000);
								getObject("TesterAddTester_selectOkButton").click();
								
								//set the role
								if(!selectRole(tester_Role))
								{
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Role of the tester was not added successfully");
									throw new SkipException("Could not type role in role box");
								}
								
								getElement("TesterAddTester_addRoleButton_Id").click();
								
								
								if(!selectArea(SelectedArea))
								{
									fail=true;
									assertTrue(false);
									comments+="Could not select area from dropdown. (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Could not select area from dropdown.");
								}
							
								roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
								numOfRoles = roleSelectionNames.size();
								for(int i = 0; i<numOfRoles;i++)
								{
									if((roleSelectionNames.get(i)).getAttribute("title").equals(TesterRoleSelection))
									{
										getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
										break;
									}
									
								}
								
								getObject("TesterDetailsAddTester_SaveBtn").click();
								
								if (!(getTextFromAutoHidePopUp().contains("successfully")))
								{
									fail=true;
									assertTrue(false); 
									APP_LOGS.debug("Tester creation successful message did not appear");
									comments=comments+"Tester creation successful message did not appear (FAIL)";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester creation successful message did not appear");
								}	
								else
								{
									//getObject("Tester_testeraddedsuccessfullyOkButton").click();
									comments+="Succesful tester creation message appeared (PASS)";
									if(searchForTheTester(tester.get(0).username))
									{
										APP_LOGS.debug("Tester created successfully");
										comments+="Tester created successfully (PASS)";
									}
									else
									{
										fail=true;
										assertTrue(false); 
										APP_LOGS.debug("Tester not created successfully");
										comments=comments+"Tester not created successfully (FAIL)";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Tester not created successfully");
									}
								}
								
												
								
							}
							else
							{
								fail = true;
								comments+="Login Not Successful for test manager(FAIL)";							}
							}	
						else
						{
							fail = true;
							comments+="Login Not Successful for version lead(FAIL)";
						}
					}
					catch (Throwable t) 
					{
						fail = true;
						assertTrue(false);
						APP_LOGS.debug("Something went wrong while Executing "+this.getClass().getSimpleName()+".java. Test case has been failed.");
						
						comments = comments+ "Exception Occurred: Something went wrong while Executing the script(FAIL)";
					
					}
				
					closeBrowser();	
				}
				else 
				{
					APP_LOGS.debug("Login Not Successful");
					comments+="Login Not Successful (FAIL)";
				}
			}
	
					
			public boolean selectTester(String testerName)
			{
						
				try 
				{
					//Thread.sleep(1000);
					
					getObject("TesterCreateNew_PeoplePickerImg").click();
					   
					Thread.sleep(500);driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(testerName);
					   
					getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
			   
					getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
			   
					getObject("TesterCreateNew_PeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
				} catch (Throwable e) {
					APP_LOGS.debug("Exception in createTester function.");
					comments=comments+"Exception in createTester function. (FAIL)";
					return false;
				}
				return true;
			}
			
			
			public boolean selectRole(String testerRoleCreation,String testerRoleSelection) throws IOException, InterruptedException{
				try {
					
					Thread.sleep(1000);
					String[] testerRoleArray = testerRoleCreation.split(",");
					for(int i=0;i<testerRoleArray.length;i++)
					{
						if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
						{
							getElement("TesterCreateNew_addTesterRoleLink_Id").click();
							getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
							getElement("TesterCreateNew_addTesterRole_Id").click();
							//getTextFromAutoHidePopUp();
						}
					}
				
					String[] testerRoleSelectionArray = testerRoleSelection.split(",");
					
					List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
							{
								getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
						}
					}
					
				} catch (Throwable e) {
					APP_LOGS.debug("Exception in createTester function.");
					comments=comments+"Exception in selecting/creating role for tester. (FAIL)";
					return false;
				}
				return true;
			}
			
			
			public boolean selectRole(String tester_Role){
				
				try {
					
					getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
					
				} catch (Throwable e) {
					
					APP_LOGS.debug("Role of the tester not added successfully");
					comments=comments+"Role of the tester not added successfully (FAILs)";
					return false;
				}
				return true;
			}
			
			public boolean selectDescription(String tester_description){
				try {
					
					getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
					
				} catch (Exception e) {
					APP_LOGS.debug("Role of the tester not added successfully with description");
					comments=comments+"Could not add role with description(FAIL)";
					return false;
				}
				return true;
			}
			
			
			public boolean selectArea(String SelectedArea){
				
				try {
					Thread.sleep(1000);
					getElement("TesterAddTester_Area_ID").click();
					
					List<WebElement> areaDDElements = getElement("TesterAddTester_Area_ID").findElements(By.tagName("option"));
					  
					   for(int i =0 ;i<areaDDElements.size();i++)
					   {
					         if(areaDDElements.get(i).getText().equals(SelectedArea))
					       {
					          areaDDElements.get(i).click();
					          APP_LOGS.debug( SelectedArea + " : is selected...");
					          break;
					       }       
					   }
				} catch (Exception e) {
					comments=comments+"Exception in adding area function.";
					return false;
				}
				return true;
			}
			
			@AfterMethod
			public void reportDataSetResult(){
				if(skip)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
				
				else if(!isLoginSuccess){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
				}
				else if(fail){
					isTestPass=false;
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
					TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}
				else
				{
					TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
					TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
				}	
				skip=false;
				fail=false;
			}
		
			@AfterTest
			public void reportTestResult() throws Exception{
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
				utilRecorder.stopRecording();
				}
			
				
}				
				
				