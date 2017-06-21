package com.uat.suite.stakeholder_dashboard;

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
	StackTraceElement[] stacktrace;
	StackTraceElement stackElement;
	String className;
	
	boolean flag= false;
	
	// check if Stakeholder Dashboard Suite has to be skipped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of Stakeholder Dashboard Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Stakeholder Dashboard Suite"))
		{
			APP_LOGS.debug("Skipped Stakeholder Dashboard Suite  as the runmode was set to NO");
			throw new SkipException("Runmode of Stakeholder Dashboard Suite set to NO ... So Skipping all tests in Stakeholder Dashboard Suite");
		}
		
	}
	
	//common function for creating project
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");
		
		getObject("Projects_createNewProjectLink").click();
		try {
			
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(version);
				
				selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear,endDate);
				
				getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
				   
				Thread.sleep(500);
				
				driver.switchTo().frame(1);
				
				//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				waitForElementVisibility("ProjectCreateNew_versionLeadStakeholderTextField", 70);
				
				getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
				   
				getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		   
				waitForElementVisibility("ProjectCreateNew_versionLeadStakeholderSelectSearchResult", 10).click();
			//	getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
		   
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		   
				driver.switchTo().defaultContent();
				
				click(getObject("ProjectCreateNew_projectSaveBtn"));
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
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
					
					
	// Create Test Pass for that project:
	public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		Thread.sleep(2000);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(2000);
		getObject("TestPasses_createNewProjectLink").click();
		
		
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
		
				waitForElementVisibility("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult", 10).click();
	//			getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
		   
				getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
		   
				driver.switchTo().defaultContent();
				
				selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
				
				click(getObject("TestPassCreateNew_testPassSaveBtn"));
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
//					getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
					
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
		Thread.sleep(2000);
		
		try {
			
				dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
				
				getElement("Tester_createNewProjectLink_Id").click();
				Thread.sleep(1000);
				
				getObject("TesterCreateNew_PeoplePickerImg").click();
				   
				Thread.sleep(500);driver.switchTo().frame(1);
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
				   
				getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
		   
				waitForElementVisibility("TesterCreateNew_PeoplePickerSelectSearchResult", 10).click();
				
	//			getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
		   
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
						
						 //code if role which is going to be created is already exist
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
				
				click(getElement("TesterCreateNew_testerSaveBtn_Id"));
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
//					getObject("Tester_testeraddedsuccessfullyOkButton").click();
					
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
				
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(1000);
				
				getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
				
				click(getElement("TestCaseCreateNew_testCaseSaveBtn_Id"));
				
				if (getTextFromAutoHidePopUp().contains("successfully")) 
				{
//					getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
					
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
	
	
	public void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException 
	{				
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
					APP_LOGS.debug("Clicking on Add icon ");
					addButton.click();
		  
					APP_LOGS.debug("Inputting Text :" +text +" in Text Field ");
		  
					getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(text);
					
					//Save the entered group or portfolio 
					if (getElement("ProjectCreateNew_projectAddPopUpLabel_Id").getText().contains("Project")) 
					{
						getObject("ProjectCreateNew_projectAddBtn").click();
					}
					else 
					{
						getObject("ProjectCreateNew_groupPortfolioSaveBtn").click(); 
					}
			}
		}
		catch(Throwable t)
		{					
			 t.printStackTrace();
		}				
	}
		
			
	public boolean compareDropDownSelectionText(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException 
	{
		  
		  dropDownList.click();
		  
		  List<WebElement> elements = SelectionList.findElements(By.tagName("li"));
		  
		  for(int i =0 ;i<elements.size();i++)
		  {
		   
			    if(elements.get(i).getText().equals(selectionText))
			    {
				      flag=true;
				      elements.get(i).click();
				      APP_LOGS.debug(elements.get(i)+" is exist ");
				      break;
			    }
		  }
		  return flag;
	}
	
	
	public boolean ifElementIsClickableThenClick(String key)
	{
		try
		   {
			   setImplicitWait(1);
			   
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
			   resetImplicitWait();
		           
		   }
	}
	
	public boolean ifElementClickable(String key)
	{
		try
		   {				
				String disabledAttr = isLinkDisabled(key);
				
				if(disabledAttr==null)
				{
					 setImplicitWait(1);	
				   
					 getObject(key).click();
					 return true;				    
				}
				else
				{
					return false;
				}
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
	
	
	//if link contains disable attribute
	public String isLinkDisabled(String key)
	{
		String disabledAttr = null;
			
		try
		{
			setImplicitWait(1);
		    disabledAttr = eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).getAttribute("disabled");			 
		   
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			
		}
		finally
	   {
		   resetImplicitWait();
	           
	   }
		
		return disabledAttr;
	}
	
	
	
	public int currentlySelectedQuarter() throws IOException
	{
		if (getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected()) 
		{
			return 1;
		}
		else if (getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected()) 
		{
			return 2;
		}
		else if (getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected()) 
		{
			return 3;
		}
		else  

			return 4;
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
	
	
	public WebElement getObject(String xpathKey1,String xpathKey2,String xpathKey3, int index1, int index2)
	  {
		  
		  String collectiveXpath = OR.getProperty(xpathKey1)+index1+OR.getProperty(xpathKey2)+index2+OR.getProperty(xpathKey3);
		   
		  try
		  {
			  WebElement x= eventfiringdriver.findElement(By.xpath(collectiveXpath));
	           		         
			  return x;		         			  
		    
		  }
		  catch(Throwable t)
		  {
			  stacktrace = Thread.currentThread().getStackTrace();
			  stackElement = stacktrace[2];
			  className = stackElement.getClassName();
			  String[] extractTestCaseName = className.split("\\.");
			  className = extractTestCaseName[extractTestCaseName.length-1];
			  
			  TestUtil.takeScreenShot(className, xpathKey1+"+"+index1+"+"+xpathKey2+"+"+index2+"+"+xpathKey3);
			  // report error
			  //ErrorUtil.addVerificationFailure(t);  
		    
			  APP_LOGS.debug("Cannot find object with key -- " +(xpathKey1+"+"+index1+"+"+xpathKey2+"+"+index2+"+"+xpathKey3));
		    
			  return null;
		  }
		   
	  }
	
}