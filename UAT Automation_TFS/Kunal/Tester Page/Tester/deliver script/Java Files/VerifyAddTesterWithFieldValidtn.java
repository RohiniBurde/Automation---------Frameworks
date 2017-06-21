package com.uat.suite.tm_tester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.regexp.recompile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyAddTesterWithFieldValidtn extends TestSuiteBase
{

	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	static boolean testerAvailable=false;
	String comments;
	TestSuiteBase testSuiteBase;
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	boolean totalCountVisibility=false;
	int totalPages;
	int projectsPerPage;
	int projectLimitPerPage=10;
	
	boolean areaSelect=false;
	boolean testerSelect=false;
	boolean roleSelect=false;
	
	
	
	// Runmode of test case in a suite
	
	@BeforeTest
	public void checkTestSkip(){
		APP_LOGS.debug("Begining of the Verify Add Tester with Field Validation");
		System.out.println("Begining of the Verify Add Tester with Field Validation");
		if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();				
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
				
				
				APP_LOGS.debug(" Executing Test Case -> VerifyTesterPageContents...");
				System.out.println(" Executing Test Case -> VerifyTesterPageContents...");				
				
				openBrowser();
				APP_LOGS.debug("Opening Browser... ");
				System.out.println("Opening Browser... ");
				
				isLoginSuccess = login(Role);
				
				if(isLoginSuccess)
				{
				//Step 1:  clicking on Test Management Tab
					try
					{	
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						comments=comments+"Clicking On Test Management Tab (PASS)";
						
							
						//Step 2:  clicking on Project Tab
						getElement("TM_projectsTab_Id").click();
						APP_LOGS.debug("Clicking On Project Tab ");
						comments=comments+"Clicking On Project Tab (PASS)";
						
					} 
					catch (Throwable t) 
					{
						fail=true;
						APP_LOGS.debug("Clicking On Project Tab is failed");
						comments=comments+"Clicking On Project Tab is failed";
					}
					
					if(!createProject(GroupName, Portfolio, Project, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
					{
						fail=true;
						APP_LOGS.debug("Project Creation fails");
						comments=comments+"Project Creation Fails";
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestStep");
						
						closeBrowser();
						
						throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as Test Pass is not created successfully");//reports
					}
					
					Thread.sleep(1000);
					closeBrowser();
					
					openBrowser();
					APP_LOGS.debug("Opening Browser... ");
					System.out.println("Opening Browser... ");
					
					//version lead login here
					login(versionLead.get(0).username,versionLead.get(0).password,"Version Lead");
					
					try 
					{
						Thread.sleep(1000);
						//clicking on Test Management Tab
						getElement("UAT_testManagement_Id").click();
						APP_LOGS.debug("Clicking On Test Management Tab ");
						System.out.println("Clicking On Test Management Tab ");
						Thread.sleep(2000);
						//clicking on Test Pass Tab
						getElement("TM_testersTab_Id").click();
						APP_LOGS.debug("Clicking On Testers Tab ");
						System.out.println("Clicking On Testers Tab ");
						
						
					} 
					catch (Throwable t) 
					{
						
						fail=true;
						APP_LOGS.debug("Clicking On Tester Tab is failed");
						System.out.println("Clicking On Tester Tab is failed");
						
					}
					
					Thread.sleep(1000);					
					/*int groupResult = verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1", GroupName);
					System.out.println("if 1 means group exist else not exist: "+groupResult);*/
					
					dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
					dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
					dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
					dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
					dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
					
					String testersTabHighlighted = getObject("Testers_testerTab").getAttribute("class");
					
					if(!compareStrings(testersTabHighlighted,OR.getProperty("Testers_testerTab_Class"))){
					
						fail=true;
						APP_LOGS.debug("Testers Tab is Highlighted ");
						System.out.println("Testers Tab is Highlighted ");
						
					}
					
					APP_LOGS.debug("Testers Tab is Highlighted ");
					System.out.println("Testers Tab is Highlighted ");
					
					if(!compareStrings(getObject("Testers_testersDetailsHeading").getText(),ExpectedPageHeadingText)){
						
						fail=true;
						APP_LOGS.debug("User " + Role+ " is not landing on 'Testers' Page successfully ");
						comments="User " + Role+ " is not landing on 'Testers' Page successfully (FAIL)";
					
					}
					
						Thread.sleep(1000);
						getObject("Testers_addTesterLink").click();
						
						System.out.println("Alert Text:    "+getElement("TesterAddTester_AlertText_ID").getText());
						System.out.println(AddTesterAlertText1+Project+AddTesterAlertText2);
						
						String actualAlertAddTestPass=getElement("TesterAddTester_AlertText_ID").getText();
						if (actualAlertAddTestPass.equals(AddTesterAlertText1+Project+AddTesterAlertText2)){
							if(!createTester(GroupName, Portfolio, Project, Version, TestPassName, tester.get(0).username, TesterRoleCreation, TesterRoleSelection))
							{
								fail=true;
								APP_LOGS.debug("Failed to create the Testers");
								comments=comments+"Failed to create the Testers (FAIL)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTester");
							}
						}
						
						APP_LOGS.debug("Expected validation message is prompt for Add Test Pass");
						comments=comments+ "Expected validation message is prompt for Add Test Pass(PASS)";
						
						Thread.sleep(1000);
						getObject("TesterAddTester_selectCancelButton").click();
						//create the test pass with the version lead
						Thread.sleep(1000);
						if(!createTestPass(GroupName, Portfolio, Project, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username)){
							fail=true;
							APP_LOGS.debug("Failed to create the Test Pass");
							comments=comments+"Failed to create the Test Pass (FAIL)";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "FailedCreatingTestPass");
						}
						
						System.out.println("Successfully created the Test Pass for Project");
						Thread.sleep(1000);
						closeBrowser();
						
						openBrowser();
						APP_LOGS.debug("Opening Browser... ");
						System.out.println("Opening Browser... ");
						
						login(testManager.get(0).username,testManager.get(0).password,"Test Manager");
						
						try 
						{
							Thread.sleep(1000);
							//clicking on Test Management Tab
							getElement("UAT_testManagement_Id").click();
							APP_LOGS.debug("Clicking On Test Management Tab ");
							System.out.println("Clicking On Test Management Tab ");
							
							Thread.sleep(1000);
							//clicking on Test Pass Tab
							getElement("TM_testersTab_Id").click();
							APP_LOGS.debug("Clicking On Testers Tab ");
							System.out.println("Clicking On Testers Tab ");
							
							
							dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
							dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), Portfolio );
							dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), Project );
							dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
							dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
							
						/*	verifyHeaders("Testers_groupDropDown_Id", "Testers_groupNameDDElemnts", "Testers_groupNameDDXpath1",GroupName);
							verifyHeaders("Testers_portfolioDropDown_Id", "Testers_portfolioNameDDElemnts", "Testers_portfolioNameDDXpath1", Portfolio);
							verifyHeaders("Testers_projectDropDown_Id", "Testers_projectNameDDElemnts", "Testers_projectNameDDXpath1", Project);
							verifyHeaders("Testers_versionDropDown_Id", "Testers_versionNameDDElemnts", "Testers_versionNameDDXpath1", Version);
							verifyHeaders("Testers_testPassDropDown_Id", "Testers_testPassNameDDElemnts", "Testers_testPassNameDDXpath1", TestPassName);*/
							
							
						} catch (Throwable t) {
							
							fail=true;
							APP_LOGS.debug("Clicking On Testers Tab is failed");
							System.out.println("Clicking On Testers Tab is failed");
							
						}
						
						
						/*try {
							Thread.sleep(1000);
							
							//clicking on Test Pass Tab
							getElement("TM_testersTab_Id").click();
							APP_LOGS.debug("Clicking On Testers Tab ");
							System.out.println("Clicking On Testers Tab ");
							
							
						} catch (Throwable t) {
							
							fail=true;
							APP_LOGS.debug("Clicking On Tester Tab is failed");
							System.out.println("Clicking On Tester Tab is failed");
							
						}*/
						
						Thread.sleep(500);
						
						getObject("Testers_addTesterLink").click();
						Thread.sleep(500);
						//verifying the prompt text oF POP UPS
						System.out.println("role selected "+selectRole(TesterRoleCreation, TesterRoleSelection));
						if(selectRole(TesterRoleCreation, TesterRoleSelection))
						{
							getObject("TesterDetailsAddTester_SaveBtn").click();
							APP_LOGS.debug("Tester please select Role first");
							System.out.println(getElement("ProjectEdit_validationMessage_Id").getText()+"expected is : "+ExpectedSelectTesterText);
							
							if(!(getElement("ProjectEdit_validationMessage_Id").getText().equalsIgnoreCase(ExpectedSelectTesterText)))
							{
								fail=true;
								APP_LOGS.debug("Tester not created successfully Role Block Testers Message");
								comments=comments+"Tester not created successfully Role Block (FAIL)";
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExpectedSelectRoleText did not matched");
							}
							
							getObject("TesterAddTester_selectOkButton").click();
						}
						
						
						
						System.out.println("Before entering select tester");
						if(selectTester(tester.get(0).username))
						{
							Thread.sleep(1000);
							getObject("TesterDetailsAddTester_SaveBtn").click();
							APP_LOGS.debug("Clicking on save button");
							System.out.println(getElement("ProjectEdit_validationMessage_Id").getText()+"expected is : "+ExpectedSelectRoleText);
							if(!(getElement("ProjectEdit_validationMessage_Id").getText().equalsIgnoreCase(ExpectedSelectRoleText)))
							{
								fail=true;
								APP_LOGS.debug("Tester not created successfully Tester Block Role Message");
								comments=comments+"Tester not created successfully Tester Block (FAIL)";
							}
							Thread.sleep(1000);
							getObject("TesterAddTester_selectOkButton").click();
						}
						
						
						getElement("TesterAddTester_addRole_Id").click();
						
						
						
						//sets the description
						selectDescription(tester_description);
						getElement("TesterAddTester_addRoleButton_Id").click();
						
						if(!compareStrings(getElement("ProjectEdit_validationMessage_Id").getText(), ExpectedAlertforRole))
						{
							fail=true;
							APP_LOGS.debug("Tester not created successfully");
							comments=comments+"Tester not created successfully (FAIL)";
						}
						Thread.sleep(1000);
						getObject("TesterAddTester_selectOkButton").click();
						
						//set the role
						selectRole(tester_Role);
						getElement("TesterAddTester_addRoleButton_Id").click();
						
						
						System.out.println("Area Selection is: "+selectArea(SelectedArea));
						
						selectRole(TesterRoleCreation, TesterRoleSelection);
						getObject("TesterDetailsAddTester_SaveBtn").click();
						APP_LOGS.debug("Tester please select Role first");
						comments=comments+"Tester please select Role first";
							
	//							getObject("TesterDetailsAddTester_SaveBtn").click();
	
						if (!(getElement("TesterCreateNew_testerSuccessMessageText_Id").getText().contains("successfully"))){
							fail=true;
							APP_LOGS.debug("Tester not created successfully");
							comments=comments+"Tester not created successfully";
						}	
						
						getObject("Tester_testeraddedsuccessfullyOkButton").click();
						APP_LOGS.debug("Tester created successfully");
						comments=comments+"Tester created successfully";
					}
				
				APP_LOGS.debug("Tester created successfully with all pop up verifications");
				comments=comments+"Tester created successfully with all pop up verifications (PASS)";
						closeBrowser();					
	}
	
					
					public boolean selectTester(String testerName){
						
						try {
							Thread.sleep(1000);
							getObject("TesterCreateNew_PeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(testerName);
					   
					getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
			   
					getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
			   
					getObject("TesterCreateNew_PeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
				} catch (Throwable e) {
					APP_LOGS.debug("Exception in createTester function.");
					comments=comments+"Exception in createTester function.";
					e.printStackTrace();
					return false;
				}
				return true;
			}
			
			
			public boolean selectRole(String testerRoleCreation,String testerRoleSelection) throws IOException, InterruptedException{
				try {
					
					Thread.sleep(1000);
					String[] testerRoleArray = testerRoleCreation.split(",");
					System.out.println("Role Creation are: "+testerRoleArray);
					for(int i=0;i<testerRoleArray.length;i++)
					{
						if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
						{
							getElement("TesterCreateNew_addTesterRoleLink_Id").click();
							getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
							getElement("TesterCreateNew_addTesterRole_Id").click();
						}
					}
					System.out.println(testerRoleSelection);
					System.out.println(testerRoleSelection.split(",").length);
					
					String[] testerRoleSelectionArray = testerRoleSelection.split(",");
					System.out.println(testerRoleSelectionArray.length);
					
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
					comments=comments+"Exception in createTester function.";
					e.printStackTrace();
					return false;
				}
				return true;
			}
			
			
			public boolean selectRole(String tester_Role){
				
				try {
					
					getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
					
				} catch (Throwable e) {
					
					APP_LOGS.debug("Role of the tester not added successfully");
					comments=comments+"Role of the tester not added successfully";
					return false;
				}
				return true;
			}
			
			public boolean selectDescription(String tester_description){
				try {
					
					getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(tester_description);
					
				} catch (Exception e) {
					APP_LOGS.debug("Role of the tester not added successfully with description");
					comments=comments+"Role of the tester not added successfully with description";
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
					          areaSelect=true;
					          APP_LOGS.debug( SelectedArea + " : is selected...");
					          System.out.println(SelectedArea + " : is selected...");
					          break;
					       }       
					   }
				} catch (Exception e) {
					APP_LOGS.debug("Exception in createTester function.");
					comments=comments+"Exception in createTester function.";
					e.printStackTrace();
					return false;
				}
				return areaSelect;
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
			public void reportTestResult(){
				if(isTestPass)
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
				else
					TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
			}
			
				
}				
				
				