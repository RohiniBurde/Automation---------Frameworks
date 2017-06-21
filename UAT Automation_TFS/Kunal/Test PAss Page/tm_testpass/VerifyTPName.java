package com.uat.suite.tm_testpass;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyTPName extends TestSuiteBase 
{   

	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=false;
	static boolean SelectorCreationGroupAndPortfolio2 = false;
	static boolean SelectOrCreationProject = false;
	static int count=-1;
	static Select select;
	static WebDriverWait w;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> versionLead;
	String comments="";
	
	// Runmode of test case in a suite
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
		//testManager=getUsers("Test Manager", 1);
	}
	
	@Test(dataProvider = "getTestData")
	public void verifyCombinationInTestPassName(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String Description,String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder, String Status,
			String ProjectURL,String AliasProjectURL, String ApplicationURL,String AliasApplicationURL,
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
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead = getUsers("Version Lead", versionLead_count);

			int testManager_count = Integer.parseInt(TP_TestManager);
			testManager = getUsers("Test Manager", testManager_count);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" - opening the browser");
			openBrowser();
			
			isLoginSuccess = login(role);
			
			if (isLoginSuccess) 
			{
				try
				{
				//click on testManagement tab
					APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					
					Thread.sleep(2000);
					
					getElement("UAT_testManagement_Id").click();
					
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
						//Click on Test Pass
						getElement("TM_testPassesTab_Id").click();

						APP_LOGS.debug("Verify Group, portfolio, project, Version dropdowns are present when Portfolio is enabled");
						
						boolean groupDropDownIsDisplayed = getElement("TestPasses_groupDropDown_Id").isDisplayed();
						
						boolean portfolioDropDownIsDisplayed = getElement("TestPasses_portfolioDropDown_Id").isDisplayed();
						
						boolean projectDropDownIsDisplayed = getElement("TestPasses_projectDropDown_Id").isDisplayed();
						
						boolean versionDropDownIsDisplayed = getElement("TestPasses_versionDropDown_Id").isDisplayed();
						
					
						if (assertTrue(groupDropDownIsDisplayed&&portfolioDropDownIsDisplayed&&projectDropDownIsDisplayed&&versionDropDownIsDisplayed)) 
						{
							APP_LOGS.debug("Group, portfolio, project, Version dropdowns are displayed when Portfolio is enabled");
							comments += "Group, portfolio, project, Version dropdowns are displayed when Portfolio is enabled... (Pass)";
						}
						else 
						{
							APP_LOGS.debug("Group, portfolio, project, Version dropdowns are not displayed when Portfolio is enabled. Test Case has been failed");
							
							fail = true;
							
							comments += "Fail Occurred: Group, portfolio, project, Version dropdowns are not displayed when Portfolio is enabled.... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "The expected header dropdowns are not displayed");
						
							closeBrowser();
							
							throw new SkipException("As portfolio is enabled, Group, portfolio, project, Version dropdowns should have been displayed but are not.");
						}
						
				
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
						
						Thread.sleep(2000);
						APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
						getObject("TestPasses_createNewProjectLink").click();
						
						APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
						getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
						String[] statusArray = {"Active","On Hold","Complete"};
						int flagStatus=0;
						//Verify options present in Status Dropdown
						select = new Select(getElement("TestPassCreateNew_statusDropDown_Id"));
						
						List<WebElement> numberOfOptionsPresentInStatusDropDown = select.getOptions();
						int numberOfTestPassStatusOptions = numberOfOptionsPresentInStatusDropDown.size();
						if(!(numberOfTestPassStatusOptions==3))
						{
							assertTrue(false);
							comments+="The number of available options in status dropdown should have been 3 but was "+numberOfTestPassStatusOptions+"(FAIL)";
						}
						for (WebElement temp : numberOfOptionsPresentInStatusDropDown) 
						{ 
							flagStatus++;
							APP_LOGS.debug("Option in Status Dropdown is " + temp.getText());
		
							String optionNameInStatusDropDown = temp.getText();
							
							if(!compareStrings(statusArray[flagStatus-1], optionNameInStatusDropDown))
							{
								APP_LOGS.debug("Option "+optionNameInStatusDropDown+" is present in Status Dropdown which is unexpected. Test Case has been Failed");	
								fail = true;
								
								comments += "Fail Occurred: Options Present in Status Dropdown are unexpected... ";
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "unexpected Options Present in Status Dropdown");
								
							}
						}
					
						APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
						if(!enterTestManagerName(testManager.get(0).username))
						{
							fail =  true;
							
							comments = comments+"Fail Occurred: while entering test manager name ... ";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "entering test manager name");
							
							closeBrowser();
							throw new SkipException("Fail Occurred: while entering test manager name ...");
						}
						
						APP_LOGS.debug("Selecting the End date");
						if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
						{
							fail = true;
						
							comments = comments+"Fail Occurred: while entering End Date... ";
						
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "entering End date name");
							closeBrowser();
							throw new SkipException("Fail Occurred: while entering End Date...");
						}   
	
						APP_LOGS.debug("Click on Save");
						getObject("ProjectCreateNew_projectSaveBtn").click();
						
						String textFromThePopupAfterSaveButton = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
						APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
						
						if(!compareStrings("Test Pass added successfully!", textFromThePopupAfterSaveButton))
						{
							APP_LOGS.debug("Popup text is other than : 'Test Pass added successfully!' Test Case Has been Failed");
							
							fail = true;
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Unexpected popup text is displayed");
							
						}
						getObject("projectSuccessPopUpOkBtn").click();
						Thread.sleep(2000);
						
						getObject("TestPasses_viewAllProjectLink").click();
						
						selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
								"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
						
						selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
								"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
						
						selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
								"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
						
						selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
								"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);

						
						if(!(compareStrings(TestPassName, getObject("TestPass_ViewAllTestPassNameCell").getText()))&&(compareStrings(TP_TestManager.replace(".", " "), getObject("TestPass_ViewAllTestPassManagerCell").getText().toLowerCase())))
						{
							APP_LOGS.debug("Test Pass showed successful creation message but is not shown in test pass grid with correct data.");
							
							fail = true;
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Saved test Pass not shown in grid");
							
							comments+="Saved test Pass not shown in grid (FAIL)";
						
						}
						else
						{
							APP_LOGS.debug("Test Pass saved contents reflected in test pass grid with correct data.");
							
							comments+="Saved test Pass shown with expected data in grid (PASS)";
						}
						closeBrowser();
					}
					else 
					{
						APP_LOGS.debug("Login Not Succesful");
						isLoginSuccess=false;
						
					}
				}catch(Throwable t)
				{
					fail = true;
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Some exception occured.");
					
					comments+="Some exception occured (FAIL). ";
				}
			}
			else 
			{
				APP_LOGS.debug("Login Not Succesful");
				isLoginSuccess=false;
				
			}
		}

	//ALL FUNCTIONS

	public void selectDetailsFromDD(WebElement allDropDown,WebElement DDElement,String xpathKey1, String xpathKey2, String elementName )
	{
		try 
		{
			allDropDown.click();
			Thread.sleep(1000);
			List<WebElement> DDMember = DDElement.findElements(By.tagName("li"));
			System.out.println("The number of elements present in group drop down list is : " + DDMember.size());
			for(int i =1; i<=DDMember.size();i++)
			{
				//String listMemebers = getElement(xpathKey1, xpathKey2, i).getText();
				String listMemebers = getObject(xpathKey1, xpathKey2, i).getText();
				System.out.println(listMemebers);
				if(listMemebers.equals(elementName))
				{
					//getElement(xpathKey1, xpathKey2, i).click();
					getObject(xpathKey1, xpathKey2, i).click();
				}
			}
		} 
		catch (Throwable t) {
			APP_LOGS.debug("Test Case Has Been FAILED In 'selectDetailsFromDD' function.");
			fail = true;
			assertTrue(false);
		}
	}
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, this.getClass().getSimpleName(), count+2, "LOGIN NOT SUCCESSFUL");
		}
			
		else if(fail){
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
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testPassSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testPassSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_testPassSuiteXls, this.getClass().getSimpleName()) ;
	}		
	
	
	
		
		
}
