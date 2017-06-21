package com.uat.suite.dashboard_detailedAnalysis;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase{
	
	static boolean fail=false;
	String displayNamefromPioplePicker;
	
    // check if the suite ex has to be skipped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of DetailedAnalysis Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "DetailedAnalysis Suite"))
		{
			APP_LOGS.debug("Skipped dashboard_detailedAnalysis Suite as the runmode was set to NO");
			throw new SkipException("Runmode of dashboard_detailedAnalysis Suite set to no. So Skipping all tests in Suite A");
		}
	}
	
	public void enterTestManagerName(String testManagerName)
	{
		try 
		{
			APP_LOGS.debug("Clicking on Test Manager Image Icon...");
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			  
			Thread.sleep(500);driver.switchTo().frame(1);
			APP_LOGS.debug("Switched to the Version Lead frame...");
		  
			wait = new WebDriverWait(driver,200);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
		  
			APP_LOGS.debug(testManagerName + " : Input text in Test Manager text field...");
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(testManagerName); 
		   
			APP_LOGS.debug("Click on Search button from Version Lead frame ...");
			getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		   
			APP_LOGS.debug("TEst Manager is selected from search result...");
			getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
		  
			APP_LOGS.debug("Click on OK button from Test Manager frame ...");
			getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		  
			driver.switchTo().defaultContent();
		} 
		catch (Throwable t) 
		{
			fail = true;
			APP_LOGS.debug(this.getClass().getSimpleName()+" Test manager name provided is having some error.");
		}
		
	}
	
	
	public void enterTesterName(String testerName) throws InterruptedException
	{
		APP_LOGS.debug("Clicking on Tester Image Icon...");
		getObject("TesterDetailsAddTester_testerPeoplePickerImg").click();
		  
		Thread.sleep(500);driver.switchTo().frame(1);
		APP_LOGS.debug("Switched to the Version Lead frame...");
	  
		wait = new WebDriverWait(driver,200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
	  
		APP_LOGS.debug(testerName + " : Input text in Tester text field...");
		getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(testerName); 
	   
		APP_LOGS.debug("Click on Search button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
	   
		APP_LOGS.debug("Tester is selected from search result...");
		getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
	  
		displayNamefromPioplePicker = getObject("TesterDetailsAddTester_testerDisplayNameFromPeoplePicker").getText();
		
		APP_LOGS.debug("Click on OK button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
	  
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
	}
	
	
	
	//createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead, stakeholder);
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead/*,String StakeholderName*/ ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");
		
		//wait.until(ExpectedConditions.presenceOfElementLocated())

		getObject("Projects_createNewProjectLink").click();
		try {
			
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(version);
				//getElement("Version").sendKeys(version);
				
				selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
				
				getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
				   
				Thread.sleep(500);driver.switchTo().frame(1);
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
				   
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		   
				waitForElementVisibility("ProjectCreateNew_versionLeadStakeholderSelectSearchResult",10).click();
			//	getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
		   
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		   
				driver.switchTo().defaultContent();
				
				getObject("ProjectCreateNew_projectSaveBtn").click();
				
				String projectCreatedResult=getTextFromAutoHidePopUp();
				
				if (projectCreatedResult.contains("successfully")) 
				{
			//		getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
					
					return true;
				}
				else 
				{
					return false;
				}
				
				
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
		
	}	
	
	public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2500);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(getObject("TestPasses_createNewProjectLink"))).click();
		Thread.sleep(500);
		
		
		try {
			
			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
		
			Thread.sleep(500);
			//getElement("Version").sendKeys(version);
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
			
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			   
			Thread.sleep(500);driver.switchTo().frame(1);
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			
			getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
			   
			getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
	   
			waitForElementVisibility("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult",10).click();
			
		//  getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
	   
			getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
	   
			driver.switchTo().defaultContent();
			
			selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
			
			getObject("TestPassCreateNew_testPassSaveBtn").click();
			
			String testPassCreatedResult=getTextFromAutoHidePopUp();					
			
			if (testPassCreatedResult.contains("successfully")) 
			{
		//		getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
				
				return true;
			}
			else 
			{
				return false;
			}
				
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createTestpass function.");
			e.printStackTrace();
			return false;
		}
		
	}
	public void dropDownSelectAdd(WebElement dropDownList, WebElement addButton, String text) throws IOException
	{
	  
		int flag = 0;
			
		try
		{
			List<WebElement> elements = dropDownList.findElements(By.tagName("option"));
	  
	  
					  
			for(int i =0 ;i<elements.size();i++)
			{
					
		  
					if(elements.get(i).getText().equals(text))
					{
							flag++;
			  	
							elements.get(i).click();
							
							APP_LOGS.debug( text + " : is selected...");
							
			  	
							break;
					}
			}
	  
	 
			if(flag==0)
		  
			{
				//Click on Plus icon to add Group or Portfolio
			
				APP_LOGS.debug("Clicking on Add icon ");
				
				addButton.click();
	  
				
				
				
				APP_LOGS.debug("Inputting Text :" +text +" in Text Field ");
	  
				getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(text);
				
				
	  
				//Save the entered group or portfolio 
				if (getElement("ProjectCreateNew_projectAddPopUpLabel_Id").getText().contains("Project")) {
					getObject("ProjectCreateNew_projectAddBtn").click();
				}
				else {
					getObject("ProjectCreateNew_groupPortfolioSaveBtn").click(); 
				}
					
			}
		}
		
		catch(Throwable t)
		{
			
			 t.printStackTrace();
		}
		
	}
	
	public void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
	 {
		
			try
			{
					//Select start date   
					WebElement startDateImage = calendarImg;  
	   
					APP_LOGS.debug("Clicking on Date Calendar icon...");
	   
	   
					startDateImage.click();  
	   
					Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
	   
					year.selectByValue(StartEndYear);
	   
					APP_LOGS.debug(StartEndYear +" : Year is selected...");
	   
	   
					Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
	   
					month.selectByVisibleText(StartEndMonth);
	   
					APP_LOGS.debug(StartEndMonth +" : Month is selected...");
	   
					
					WebElement datepicker= getObject("ProjectCreateNew_dateTable");
	   
					//List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
	   
					List<WebElement> cols = datepicker.findElements(By.tagName("td"));
					
					for(WebElement cell :cols)
		   
					{
							if(cell.getText().equals(StartEndDate))
							{
									cell.findElement(By.linkText(""+StartEndDate+"")).click();
	     
									APP_LOGS.debug(StartEndDate +" : Date is selected...");
	     
	     
									break;
							}
					}
			}
		
			catch(Throwable t)
			{
				
				
				t.printStackTrace();
			}
	 }
	

	public boolean createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		
		Thread.sleep(2000);
		
		getElement("TesterNavigation_Id").click();
		
		if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
		{
			getObject("TesterCreateNew_TesterActiveX_Close").click();
		}
		Thread.sleep(3000);
		
		try 
		{
			
				dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
				Thread.sleep(500);
				getElement("Tester_createNewProjectLink_Id").click();
				Thread.sleep(500);
				getObject("TesterCreateNew_PeoplePickerImg").click();
				   
				Thread.sleep(500);driver.switchTo().frame(1);
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
				   
				getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
		  
				waitForElementVisibility("TesterCreateNew_PeoplePickerSelectSearchResult",10).click();
				
		//		getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
		   
				getObject("TesterCreateNew_PeoplePickerOkBtn").click();
		   
				driver.switchTo().defaultContent();
				Thread.sleep(2000);
				String[] testerRoleArray = testerRoleCreation.split(",");
				
				for(int i=0;i<testerRoleArray.length;i++)
				{
					if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
					{
						getElement("TesterCreateNew_addTesterRoleLink_Id").click();
						getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
						getElement("TesterCreateNew_addTesterRole_Id").click();
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
				getElement("TesterCreateNew_testerSaveBtn_Id").click();
				
				String testerCreatedResult=getTextFromAutoHidePopUp();
				
				if (testerCreatedResult.contains("successfully")) 
				{
	//				getObject("Tester_testeraddedsuccessfullyOkButton").click();
					
					return true;
				}
				else 
				{
					return false;
				}
				
				
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTester function.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean createTestCase(String group, String portfolio, String project, String version, String testPassName, 
			String testCaseName) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Case");
		Thread.sleep(2000);
		getElement("TestCaseNavigation_Id").click();
		if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
		{
			getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
		}
		Thread.sleep(2000);

		try {
			
				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				Thread.sleep(500);
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(500);
				
				getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
				
				getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
				
				String testCaseCreatedResult=getTextFromAutoHidePopUp();
				
				if (testCaseCreatedResult.contains("successfully")) 
				{
			//		getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
					
					return true;
				}
				else 
				{
					return false;
				}
				
				
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestCase function.");
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean createTestStep(String group, String portfolio, String project, String version, String testPassName, 
			String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Step");
	//	Thread.sleep(2000);
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
				Thread.sleep(500);
				getObject("TestStep_createNewProjectLink").click();
				
				Thread.sleep(500);
				String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+"')";
			    eventfiringdriver.executeScript(testStepDetails);
			    
			 getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults); 
				
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
				
				String testStepCreatedResult=(String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
				
				if (testStepCreatedResult.contains("successfully")) 
				{
	//				getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
					
					return true;
				}
				else 
				{
					return false;
				}
				
				
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	public void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException, InterruptedException 
	{
		Thread.sleep(500);
		
		dropDownList.click();
		
		List<WebElement> elements = SelectionList.findElements(By.tagName("li"));
		  
  
				  
		for(int i =0 ;i<elements.size();i++)
		{
				if(elements.get(i).getText().equals(selectionText))
				{
						elements.get(i).click();
						
						APP_LOGS.debug( selectionText + " : is selected...");
						
		  	
						break;
				}
		}
		
	}
	
	public boolean searchTestPassAndClickOnBeginOrContinueTesting(String project, String version,String testPass) throws IOException, InterruptedException
	{
		int totalPages;
		int projectsInTable;
		String gridProject;
		String gridVersion;
		String gridTestPass;
		//int projectFoundFlag=0;
		APP_LOGS.debug("Searching Test pass for testing");
		
		try
		{
		
			if (getElement("DashboardMyActivities_pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on My Activity page.");
				
				totalPages=1;
				
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on My Activity page. Calculating total pages.");
				
				totalPages = getElement("DashboardMyActivities_pagination_Id").findElements(By.xpath("div/a")).size();
			}		
			
			for (int i = 0; i < totalPages; i++) 
			{
				projectsInTable = getElement("DashboardMyActivities_table_Id").findElements(By.xpath("tr")).size();
				
				for (int j = 1; j <= projectsInTable; j++) 
				{
					gridProject =getObject("DashboardMyActivities_projectNameColumn1", "DashboardMyActivities_projectNameColumn2", j).getAttribute("title");
					
					gridVersion =getObject("DashboardMyActivities_versionColumn1", "DashboardMyActivities_versionColumn2", j).getAttribute("title");
					
					gridTestPass=getObject("DashboardMyActivities_testPassNameColumn1", "DashboardMyActivities_testPassNameColumn2", j).getAttribute("title");
					
					if (gridProject.equals(project) && gridVersion.equals(version) && gridTestPass.equals(testPass)) 
					{
						APP_LOGS.debug("Clicking on Begin Testing in My Activity");
						
						getObject("DashboardMyActivities_actionColumnLinksXpath1", "DashboardMyActivities_actionColumnLinksXpath2", j).click();
						
						Thread.sleep(1000);
						
						APP_LOGS.debug("Project Found in My Activity Block.");
						
						return true;
					}
					
					
				}			
				
				if (totalPages>1 && i!=(totalPages-1)) 
				{
					getObject("DashboardMyActivities_NextLink").click();
				}
				
			}
			
			//assertTrue(false);
			APP_LOGS.debug("Project, version and test pass Not found in View All page. Test case has been failed.");
			
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			assertTrue(false);
			APP_LOGS.debug("Exception in searchProjectAndEdit(). ");
			return false;
		}
	}
	
	public void clickOnPASSRadioButtonAndSave() throws IOException, InterruptedException
	{
		getElement("TestingPage_passRadioButton_Id").click();
		Thread.sleep(500);
			
		getElement("TestingPage_saveButton_Id").click();
		Thread.sleep(500);
	}
	
	
	public void clickOnFAILRadioButtonAndSave() throws IOException, InterruptedException
	{
		getElement("TestingPage_failRadioButton_Id").click();
		Thread.sleep(500);
			
		getElement("TestingPage_saveButton_Id").click();
	}
	
	public boolean isElementExistsByXpath(String key)
	 {
	  try{
	   eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
	   return true;
	  }catch(Throwable t)
	  {
	   return false;
	  }
	 }
	 

	 public boolean isElementExistsById(String key)
	 {
	  try{
	   eventfiringdriver.findElement(By.id(OR.getProperty(key))).isDisplayed();
	   return true;
	  }catch(Throwable t)
	  {
	   return false;
	  }
	 }
	 
	 
	public String getMonthNumber(String month)
	 {
		    switch (month) 
		    {
			    case "Jan":
			     return "01";
			    case "Feb":
			     return "02";
			    case "Mar":
			     return "03";
			    case "Apr":
			     return "04";
			    case "May":
			     return "05";
			    case "Jun":
			     return "06";
			    case "Jul":
			     return "07";
			    case "Aug":
			     return "08";
			    case "Sep":
			     return "09";
			    case "Oct":
			     return "10";
			    case "Nov":
			     return "11";
			    case "Dec":
			     return "12";

			    default:
			     return null;
		    }
	 }
		
}
