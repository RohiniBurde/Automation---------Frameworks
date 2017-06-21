package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase 
{
	static boolean fail=false;
	String displayNamefromPioplePicker;
	
    // check if the suite ex has to be skipped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception
	{
		initialize();
		APP_LOGS.debug("Checking Runmode of SD_DetailedView Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "SD_DetailedView Suite"))
		{
			APP_LOGS.debug("Skipped SD_DetailedView Suite as the runmode was set to NO");
			
			throw new SkipException("Runmode of SD_DetailedView Suite set to no. So Skipping all tests in Suite A");
		}
	}
	
	
	public boolean ifElementIsClickableThenClick(String key)
	 {
	  
		   try
		   {
			   eventfiringdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			   eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
			   getObject(key).click();
			   return true;
		    
		   }
		   catch(Throwable t)
		   {
		    
			   return false;
		    
		   }
		   finally
		   {
			   eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(CONFIG.getProperty("default_implicitWait")), TimeUnit.SECONDS);
		           
		   }
	   
	  }
	
	public boolean ifElementIsNotDisableThenClick(String key) throws IOException
	 {
	  
		if (getElement(key).getAttribute("disabled").equals("")) 
		{
			getElement(key).click();
			
			return true;
		}   
		else 
		{
			return false;
		}
		/*try
		   {
			   eventfiringdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			   eventfiringdriver.findElement(By.id(OR.getProperty(key))).isDisplayed();
			   getElement(key).click();
			   return true;
		    
		   }
		   catch(Throwable t)
		   {
		    
			   return false;
		    
		   }
		   finally
		   {
			   eventfiringdriver.manage().timeouts().implicitlyWait(Long.parseLong(CONFIG.getProperty("default_implicitWait")), TimeUnit.SECONDS);
		           
		   }*/
	   
	  }
	
	public void selectDDContent(List<WebElement> listOfElements, String selectOption)
	{
		for(int i=1;i<listOfElements.size();i++)
		{
			String actualOption = listOfElements.get(i).getAttribute("title");
			
			if(actualOption.equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				
			}
		}	
	}
	

	public void selectDDContentByText(List<WebElement> listOfElements, String selectOption)
	{
		for(int i=1;i<listOfElements.size();i++)
		{	
			String actualOption=listOfElements.get(i).getText();
			
			if(actualOption.equalsIgnoreCase(selectOption))
			{
				listOfElements.get(i).click();
				
				
			}
		}
	}
	
	public int getFiscalYear(String date)
	{
		
		int month = Integer.parseInt(date.substring(0, 2));
		int fiscalYear;
		
		if(month>=7)		
			fiscalYear = Integer.parseInt(date.substring(8))+1;		
		else
			fiscalYear = Integer.parseInt(date.substring(8));
		
		return fiscalYear;
	}
	
	public int getQuarter(String date)
	{
		int month = Integer.parseInt(date.substring(0, 2));
		int quarter;
		
		if(month>=7 && month<= 9)
			
			quarter = 1;
		
		else if(month>=10 && month<= 12)
			
			quarter = 2;
		
		else if(month>=1 && month<= 3)
			
			quarter = 3;
		
		else
			quarter = 4;
		
		return quarter;
		
	}
	
	
	
	
	public String[] getUniqueGroupsFromArray(String[][] arrayOfTheDetailsToBeGetFrom)
	{
		
		Set<String> setArray = new HashSet<>();
		
		String[] returnStringArray ;
		
		for (int i = 0; i < arrayOfTheDetailsToBeGetFrom.length; i++) 
		{
			setArray.add((String) arrayOfTheDetailsToBeGetFrom[i][0]);	
		}
		
		returnStringArray = new String[setArray.size()];
		
		int i = 0;
		
		Iterator<String> it = setArray.iterator();
		
		while (it.hasNext()) 
		{
			returnStringArray[i] = (String) it.next();
			
			i++;
		}
		
		return returnStringArray;
		
	}
	
	public String[] getUniquePortfolioFromArray(String[][] arrayOfTheDetailsToBeGetFrom)
	{
		
		Set<String> setArray = new HashSet<>();
		
		String[] returnStringArray ;
		
		for (int i = 0; i < arrayOfTheDetailsToBeGetFrom.length; i++) 
		{
			setArray.add((String) arrayOfTheDetailsToBeGetFrom[i][1]);	
		}
		
		returnStringArray = new String[setArray.size()];
		
		int i = 0;
		
		Iterator<String> it = setArray.iterator();
		
		while (it.hasNext()) 
		{
			returnStringArray[i] = (String) it.next();
			
			i++;
		}
		
		return returnStringArray;
		
	}
	
	public String[][] getTestPassToBeDisplayed(String[][] projectArrayToSearchIn) throws IOException
	{
		ArrayList<String> arrayListToStoreEachElementsWithoutBlank = new ArrayList<String>();
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			
			String testPassInArray = (String)projectArrayToSearchIn[i][6];
			
			
			if (!(testPassInArray.equals("No Test Pass")||testPassInArray.equals(""))) 
			{ 
				
				String[] eachElementInArraylist = (String[]) ((String) projectArrayToSearchIn[i][6]).split(", ");
				
				for (int j = 0; j < eachElementInArraylist.length; j++) 
				{
					arrayListToStoreEachElementsWithoutBlank.add(eachElementInArraylist[j]);
				}
				
			}
		}
	
		int requiredTestPassCount = arrayListToStoreEachElementsWithoutBlank.size();
		
		String[][] requiredTestPasses = new String[requiredTestPassCount][1];
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < requiredTestPasses.length; j++) 
		{
			
			requiredTestPasses[j][0] = arrayListToStoreEachElementsWithoutBlank.get(j);
			
		}
		
		
		return requiredTestPasses;
		
		
	}
	
	public int getTheNumberOfGroupsAfterComparisonWithGivenArray(String[] arrayOfUniqueGroup) throws IOException, InterruptedException
	{
		int groupCount = 0;
		
		Select selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
		
		if (compareIntegers(arrayOfUniqueGroup.length, selectGroup.getOptions().size())) 
		{
		
			for (int i = 0; i < selectGroup.getOptions().size(); i++) 
			{
				String groupNamePresentInGroupDD = selectGroup.getOptions().get(i).getText();
				
				for (int j = 0; j < arrayOfUniqueGroup.length; j++) 
				{
					if (groupNamePresentInGroupDD.equals(arrayOfUniqueGroup[j])) 
					{
						groupCount++;
						
						break;
					}
					
				}
				
			}
			
		}
		
		return groupCount;
	}
	
	
	public int getTheNumberProjectsDisplayedInDetailedView() throws IOException, InterruptedException
	{
		int projectCount = 0;
		
		Select selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
		
		Select selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
		
		for (int i = 0; i < selectGroup.getOptions().size(); i++) 
		{
			selectGroup.selectByIndex(i);
			
			Thread.sleep(500);
			
			for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
			{
				selectPortfolio.selectByIndex(j);
				
				Thread.sleep(500);
				
				do
				{
					int numOfProjectPresentInGrid = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'prjDetails']/tr")).size();
				
					/*for (int k = 0; k < numOfProjectPresentInGrid; k++) 
					{
						projectCount++;
					}*/
					projectCount += numOfProjectPresentInGrid;
					
					
					
				} while (ifElementIsClickableThenClick("SDDetailedView_nextLink"));	
				
			}
			
		}
		return projectCount;
	}
	
	
	public int numberOfTestPassAfterComparison(String[][] arrayOfTestPassToBeDisplayedInDetailedView) throws IOException, InterruptedException
	{
		int testPassCount = 0;
		
		Select selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
		
		Select selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
		
		for (int i = 0; i < selectGroup.getOptions().size(); i++) 
		{
			
			selectGroup.selectByIndex(i);
			
			Thread.sleep(500);
			
			for (int j = 1; j < selectPortfolio.getOptions().size(); j++) 
			{
				selectPortfolio.selectByIndex(j);
				
				Thread.sleep(500);
				
				if (!getElement("SDDetailedView_noTestPassAvailableText_Id").isDisplayed()) 
				{
					do
					{
						int numberOfTestPassDisplayed = eventfiringdriver.findElements(By.xpath("//tbody[@id = 'TPTable']/tr")).size();
						
						for (int k = 1; k <= numberOfTestPassDisplayed; k++) 
						{
							
							String testPassNameInGrid = getObject("SDDetailedView_testPassNameInGrid1", "SDDetailedView_testPassNameInGrid2", k).getText();
							
							for (int l = 0; l < arrayOfTestPassToBeDisplayedInDetailedView.length; l++) 
							{
								
								if (testPassNameInGrid.equals(arrayOfTestPassToBeDisplayedInDetailedView[l][0])) 
								{
									
									testPassCount++;
									
								}
								
							}	
							
						}
						
						
					} while (ifElementIsClickableThenClick("SDDetailedView_testPassStatusNextLink"));	
					
				}
				
			}
			
		}
		
		return testPassCount;
	}
	
	
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead, String stakeholder  ) throws IOException, InterruptedException
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
		   
				getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
		   
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		   
				driver.switchTo().defaultContent();
				
				//Stakeholder selection
				toSelectStakeholderfromPeoplePicker(getObject("ProjectCreateNew_StakeholderPeoplePickerImg"), stakeholder);
				
				
				getObject("ProjectCreateNew_projectSaveBtn").click();
				
				Thread.sleep(2000);
				
				if (getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText().contains("successfully")) 
				{
					getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
					
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
					
					Thread.sleep(1000);
					
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
	
	public void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
	 {
		
			try
			{
					//Select start date   
					WebElement startDateImage = calendarImg;  
	   
					APP_LOGS.debug("Clicking on Date Calendar icon...");
	   
					startDateImage.click();  
					Thread.sleep(1000);
	   
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
	
	
	public void toSelectStakeholderfromPeoplePicker( WebElement StakeholderPeoplePickerImage,String InputedTestData)
	{
			try{
				//Clicking on people picker image icon
				APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");		
				StakeholderPeoplePickerImage.click();
			
				//Switching to the people picker frame 
				Thread.sleep(500);driver.switchTo().frame(1);
				APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
				
				//Wait till the find text field is visible
				wait = new WebDriverWait(driver,20);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				//Inputting test data in people picker text field
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(InputedTestData); 
				APP_LOGS.debug(InputedTestData + " : Inputted text in Version Stakeholder text field...");
				
				//Clicking on Search button 
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
				APP_LOGS.debug("Clicked on Search button from Version Stakeholder People Picker ...");
			
				//Selecting search result 
				getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
				APP_LOGS.debug("Version Stakeholder is selected from searched user...");
				
				//Clicking on add button from Version Stakeholder people picker frame
				getObject("ProjectCreateNew_StakeholderAddBtn").click();
				APP_LOGS.debug("Clicked on Add button from Version Stakeholder People Picker frame ...");
						
				
				//Clicking on OK button from People Picker
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
				APP_LOGS.debug("Clicked on OK button from Version Stakeholder People Picker frame ...");
			
				//Switching back to the default content 
				driver.switchTo().defaultContent();
				APP_LOGS.debug("Out of the Version Stakeholder People Picker frame...");
			}
			catch(Throwable t)
			{
				fail=true;
				t.printStackTrace();
			}
		}
	
}
