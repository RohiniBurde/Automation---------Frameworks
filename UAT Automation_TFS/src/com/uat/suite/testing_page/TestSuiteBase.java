package com.uat.suite.testing_page;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase
{
	int i=0;
	
	// check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception
	{
		initialize();
		APP_LOGS.debug("Checking Runmode of Testing Page Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Testing Page Suite")){
			APP_LOGS.debug("Skipped Testing Page Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Testing Page Suite set to NO ... So Skipping all tests in Testing Page Suite");
		}
		
	}
	
	//common function for creating project
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
	{
			APP_LOGS.debug("Creating Projects.");
			
			//wait.until(ExpectedConditions.presenceOfElementLocated())
		//	Thread.sleep(1000);

			getObject("Projects_createNewProjectLink").click();
			
			Thread.sleep(500);
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
			   
					getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			   
					getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					getObject("ProjectCreateNew_projectSaveBtn").click();
					
					//Thread.sleep(2000);
					
					if (getTextFromAutoHidePopUp().contains("successfully")) 
					{
						//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
						
						return true;
					}
					else 
					{
						return false;
					}
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in createProject function.");
					e.printStackTrace();
					return false;
				}
				
	 }
	
	// Create Test Pass for that project:
	 public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
									String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
				APP_LOGS.debug("Creating Test Pass");
				//Thread.sleep(2000);
				getElement("TestPassNavigation_Id").click();
				Thread.sleep(500);
				getObject("TestPasses_createNewProjectLink").click();
				Thread.sleep(500);
				
				
				try {
					
						dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
						dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
						dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
						dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
						
						getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
						
						getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
						   
						Thread.sleep(500);driver.switchTo().frame(1);
						
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
						
						getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
						   
						getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
				   
						getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
				   
						getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
				   
						driver.switchTo().defaultContent();
						
						selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
						
						getObject("TestPassCreateNew_testPassSaveBtn").click();
						
						//Thread.sleep(2000);							
						
						
						if (getTextFromAutoHidePopUp().contains("successfully")) 
						{
							//getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
							
							return true;
						}
						else 
						{
							return false;
						}
				
					
				} 
				catch (Throwable e) 
				{
					APP_LOGS.debug("Exception in create Test Pass function.");
					e.printStackTrace();
					return false;
				}
				
			}
			
	public boolean createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		//Thread.sleep(2000);
		getElement("TesterNavigation_Id").click();
		Thread.sleep(500);
		
		if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
		{
			getObject("TesterCreateNew_TesterActiveX_Close").click();		
			Thread.sleep(2000);
		}
		
		try {
			
				dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
				dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
				dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
				dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
				
				getElement("Tester_createNewProjectLink_Id").click();
				
				Thread.sleep(500);
				
				getObject("TesterCreateNew_PeoplePickerImg").click();
				   
				Thread.sleep(500);driver.switchTo().frame(1);
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
				   
				getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
		   
				getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
		   
				getObject("TesterCreateNew_PeoplePickerOkBtn").click();
		   
				driver.switchTo().defaultContent();
				Thread.sleep(1000);
				
				String[] testerRoleArray = testerRoleCreation.split(",");
				
				for(int i=0;i<testerRoleArray.length;i++)
				{
					if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
					{
						getElement("TesterCreateNew_addTesterRoleLink_Id").click();
						getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
						getElement("TesterCreateNew_addTesterRole_Id").click();
						if(getElement("TesterCreateNew_roleAlreayExistsAlert_Id").isDisplayed())
						{
							getObject("TesterCreateNew_roleAlreayExistsAlertOkBtn").click();
							getElement("TesterCreateNew_addroleCancelBtn_Id").click();
						}
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
				
				//Thread.sleep(2000);
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
					//getObject("Tester_testeraddedsuccessfullyOkButton").click();
					
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
		//Thread.sleep(2000);
		getElement("TestCaseNavigation_Id").click();
		Thread.sleep(500);
		
		if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
		{
			getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();		
			Thread.sleep(2000);
		}
		
		try {
			
				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
				dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
				dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
				dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
				dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(500);
				
				getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
				
				getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
				
				//Thread.sleep(2000);
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
					//getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
					
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
		//Thread.sleep(2000);
		getElement("TestStepNavigation_Id").click();
		Thread.sleep(500);
		
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();		
			Thread.sleep(2000);
		}	
		
		try {
			
				dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), group );
				dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );
				dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), project );
				dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), version );
				dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
				
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
				
				Thread.sleep(2000);
				String testStepCreatedResult= (String) eventfiringdriver.executeScript("return $('#autoHideAlert').text();");
				if (testStepCreatedResult.contains("successfully")) 
				{
					//getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
					
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


	public void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException, InterruptedException {
		
		Thread.sleep(100);
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
	
	public boolean isElementExists(String key)
	{
		
		try
		{
			setImplicitWait(0);
			if(eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed())
				return true;
			else
				return false;
		}
		catch(Throwable t)
		{
			return false;
		}
		finally
		{
			 resetImplicitWait();
		}
		
	}
	
	public boolean customCreateTestPass(String group, String portfolio, String project, String version, String testPassName, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		//Thread.sleep(2000);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(500);

		try 
		{
			dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
			
			

			String[] testPass = testPassName.split(",");

			for(int testPassCount =0 ;testPassCount<testPass.length;testPassCount++)
			{
				getObject("TestPasses_createNewProjectLink").click();
				Thread.sleep(500);
				
				getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPass[testPassCount]);
				
				getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
				
				Thread.sleep(500);driver.switchTo().frame(1);
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
				
				getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
				
				getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
				
				getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
				
				driver.switchTo().defaultContent();
				
				selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
				
				getObject("TestPassCreateNew_testPassSaveBtn").click();
				
				//Thread.sleep(2000);							


				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
					//getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();								
				
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
			APP_LOGS.debug("Exception in create Test Pass function.");
			e.printStackTrace();
			return false;
		}

	}
			
	public boolean customCreateTestCase(String group, String portfolio, String project, String version, String testPassName, 
			String testCaseName) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Case");
		
		getElement("TestCaseNavigation_Id").click();		
		Thread.sleep(500);
		
		if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
		{
			getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
			Thread.sleep(2000);
		}
		

		try {
			
				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(500);	
				
				String[] testCase = testCaseName.split(",");
				
				for(int testCaseCount =0 ;testCaseCount<testCase.length;testCaseCount++)
				{	
						
					
					getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCase[testCaseCount]); 
					
					getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
					
					//Thread.sleep(2000);
					
					if (getTextFromAutoHidePopUp().contains("successfully")) 
					{
						//getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
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
			APP_LOGS.debug("Exception in createTestCase function.");
			e.printStackTrace();
			return false;
		}
		
	}		
	
	public boolean customCreateTestStep(String group, String portfolio, String project, String version, String testPassName, 
			String testStepName, String testStepExpectedResults, String testCaseToBeSelected, String roleToBeSelected) throws IOException, InterruptedException
	{
		
		APP_LOGS.debug("Creating Test Step");
		//Thread.sleep(2000);
		getElement("TestStepNavigation_Id").click();
		Thread.sleep(500);
		
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
			Thread.sleep(2000);
		}
		
		
		try {
			
			dropDownSelect(getElement("TestStepUpperRibbon_groupDropDown_Id"), getElement("TestStepUpperRibbon_GroupList_Id"), group );
			
			dropDownSelect(getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );
			
			dropDownSelect(getElement("TestStepUpperRibbon_projectDropDown_Id"), getElement("TestStepUpperRibbon_ProjectList_Id"), project );
			
			dropDownSelect(getElement("TestStepUpperRibbon_versionDropDown_Id"), getElement("TestStepUpperRibbon_VersionList_Id"), version );
			
			dropDownSelect(getElement("TestStepUpperRibbon_testPassDropDown_Id"), getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
			
			
			
			//String[] testCase = testCaseToBeSelected.split(",");	
			String[] testSteps = testStepName.split(",");	
			String[] testStep_ExpectedResult = testStepExpectedResults.split(",");
			
			
			for (int testStepCounter = 0; testStepCounter < testSteps.length; testStepCounter++) 
			{
				getObject("TestStep_createNewProjectLink").click();
				
				Thread.sleep(500);
				
				List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
				int numOfTestCases = TestCaseSelectionNames.size();
				
				for (int testCaseCounter = 0; testCaseCounter < numOfTestCases; testCaseCounter++) 
				{							
					if(TestCaseSelectionNames.get(testCaseCounter).getAttribute("title").equals(testCaseToBeSelected))
					{
						getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (testCaseCounter+1)).click();
						break;
					}
					
				}
				
				List<WebElement> roleSelectionNames = getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
				int numOfRoles = roleSelectionNames.size();
				
				for (int roleCounter = 0; roleCounter < numOfRoles; roleCounter++)
				{
					if(roleSelectionNames.get(roleCounter).getAttribute("title").equals(roleToBeSelected))
					{
						getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (roleCounter+1)).click();
					}
				}
				
				String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testSteps[testStepCounter])+"')";
			    eventfiringdriver.executeScript(testStepDetails);
			    
			    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStep_ExpectedResult[testStepCounter]);
			    
			    getElement("TestStepCreateNew_testStepSaveBtn_Id").click();				
			
				if (!getTextFromAutoHidePopUp().contains("successfully")) 
					return false;				
				
			}		
			
			
			
			/*for(int m=0;m<testCase.length;m++)
		    {				 
				List<WebElement> TestCaseSelectionNames = getObject("TestStepCreateNew_SelectionTestCaseList").findElements(By.tagName("li"));
				int numOfTestCases = TestCaseSelectionNames.size();
				for(i = 0; i<numOfTestCases;i++)
				{
						if(TestCaseSelectionNames.get(i).getAttribute("title").equals(testCase[m]))
						{
							getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
							break;
						}
				}
				
				
				
				for(int k =0;k<testSteps.length;k++)
				{
					
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+(testSteps[k])+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				    
				    String[] testStep_ExpectedResult = testStepExpectedResults.split(",");
				    getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStep_ExpectedResult[k]); 
				    				
					String[] testerRoleSelectionArray = roleToBeSelected.split(",");
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
					
					//Thread.sleep(2000);
				
					if (getTextFromAutoHidePopUp().contains("successfully")) 
					{
						getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
						APP_LOGS.debug("Test Step : " +(testSteps[k])+ " : is created successfully for Test Case : " + testCaseToBeSelected + " : of Test Pass : " +testPassName);
					}
					else 
					{
						return false;
					}
			  }
		      getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
		   
		    }*/
		  
		   return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean isElementExistsByXpath(String key)
	 {
		try
		{
			setImplicitWait(0);			
			if(eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed())
				return true;
			else
				return false;
		}
		catch(Throwable t)
		{
			return false;
		}
		finally
		{
			 resetImplicitWait();
		}
	 }
	 

	 public boolean isElementExistsById(String key)
	 {
		 
			try
			{
				setImplicitWait(0);	
				
				if(eventfiringdriver.findElement(By.id(OR.getProperty(key))).isDisplayed())
					return true;
				else
					return false;
			}
			catch(Throwable t)
			{
				return false;
			}
			finally
			{
				resetImplicitWait();
			}
	 }
	
	
}

