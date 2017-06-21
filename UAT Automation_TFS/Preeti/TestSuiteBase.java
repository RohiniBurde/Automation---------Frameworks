package com.uat.suite.tm_attachments;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase{

	// check if the suite ex has to be skiped
		@BeforeSuite
		public void checkSuiteSkip() throws Exception{
			initialize();
			APP_LOGS.debug("Checking Runmode of TM_Attachments Suite");
			if(!TestUtil.isSuiteRunnable(suiteXls, "TM_Attachments Suite")){
				APP_LOGS.debug("Skipped Attachment Suite  as the runmode was set to NO");
				throw new SkipException("Runmode of Attachments Suite set to NO ... So Skipping all tests in Attachment Suite");
			}
			
		}
		
		//common function for creating project
		public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
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
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
					   
					getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			   
					getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			   
					getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
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
					//getElement("Version").sendKeys(version);
					getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
					
					getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
					   
					getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
			   
					getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
			   
					getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					selectStartOrEndDate(getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
					
					getObject("TestPassCreateNew_testPassSaveBtn").click();
					
					Thread.sleep(2000);							
					
					
					if (getElement("TestPassCreateNew_testPassSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
						
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
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
					   
					getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
			   
					getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
			   
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
					getElement("TesterCreateNew_testerSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					if (getElement("TesterCreateNew_testerSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("Tester_testeraddedsuccessfullyOkButton").click();
						
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
					
					getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					if (getElement("TestCaseCreateNew_testCaseSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
						
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
					System.out.println(testStepName);
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
					
					if (getElement("TestStepCreateNew_testStepSuccessMessageText_Id").getText().contains("successfully")) 
					{
						getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
						
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

		public void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException {
			
			dropDownList.click();
			
			List<WebElement> elements = SelectionList.findElements(By.tagName("li"));
			  
			//System.out.println("The elements present inside  Dropdown List is : " +elements.size());
	  
					  
			for(int i =0 ;i<elements.size();i++)
			{
					//System.out.println("Each element's present inside Dropdown is having text as : " + elements.get(i).getText());
					if(elements.get(i).getText().equals(selectionText))
					{
							elements.get(i).click();
							
							APP_LOGS.debug( selectionText + " : is selected...");
							
							System.out.println(selectionText + " : is selected...");
			  	
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
		  
				//System.out.println("The elements present inside  Dropdown List is : " +elements.size());
		  
						  
				for(int i =0 ;i<elements.size();i++)
				{
						//System.out.println("Each element's present inside Dropdown is having text as : " + elements.get(i).getText());
						
			  
						if(elements.get(i).getText().equals(text))
						{
								flag++;
				  	
								elements.get(i).click();
								
								APP_LOGS.debug( text + " : is selected...");
								
								System.out.println(text + " : is selected...");
				  	
								break;
						}
				}
		  
		 
				if(flag==0)
			  
				{
						//Click on Plus icon to add Group or Portfolio
						System.out.println("Clicking on Add icon ");
					
						APP_LOGS.debug("Clicking on Add icon ");
						
						addButton.click();
			  
						
						
						System.out.println("Inputting Text :" +text +" in Text Field ");
						
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
		   
						System.out.println("Clicking on Date Calendar icon...");
		   
						startDateImage.click();  
		   
						Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
		   
						year.selectByValue(StartEndYear);
		   
						APP_LOGS.debug(StartEndYear +" : Year is selected...");
		   
						System.out.println(StartEndYear +" : Year is selected...");
		   
						Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
		   
						month.selectByVisibleText(StartEndMonth);
		   
						APP_LOGS.debug(StartEndMonth +" : Month is selected...");
		   
						System.out.println(StartEndMonth +" : Month is selected...");
						
						WebElement datepicker= getObject("ProjectCreateNew_dateTable");
		   
						//List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
		   
						List<WebElement> cols = datepicker.findElements(By.tagName("td"));
						
						for(WebElement cell :cols)
			   
						{
								if(cell.getText().equals(StartEndDate))
								{
										cell.findElement(By.linkText(""+StartEndDate+"")).click();
		     
										APP_LOGS.debug(StartEndDate +" : Date is selected...");
		     
										System.out.println(StartEndDate +" : Date is selected...");
		     
										break;
								}
						}
				}
			
				catch(Throwable t)
				{
					
					
					t.printStackTrace();
				}
		 }
		
		
		//common code opening image in new browser window
		 public static void openInNewWindow(String parentHandleName, String fileName)
		 {
			 try
			 {	
			     // call the method numberOfWindowsToBe as follows
			     WebDriverWait wait = new WebDriverWait(eventfiringdriver, 15, 100);
			     wait.until(numberOfWindowsToBe(2));
			     Set<String> availableWindows = eventfiringdriver.getWindowHandles();//this set size is
			     
			     // returned as 1 and not 2
			      String newWindow = null;
			      for (String window : availableWindows) {
			           	  if (!parentHandleName.equals(window)) {
			              newWindow = window;
			          }
			      }

			      // switch to new window
			      eventfiringdriver.switchTo().window(newWindow);
			      eventfiringdriver.manage().window().maximize();
			      Thread.sleep(1000);
			      String getURL = eventfiringdriver.getCurrentUrl();
			      
			      if(getURL.contains(fileName))
			      {
			    	  APP_LOGS.debug(fileName+": is available in url : " + getURL);
			    	  System.out.println(fileName+": is available in url : " + getURL);
			      }
			      // and then close the new window
			      eventfiringdriver.close();
			      
			      // switch to parent
			      eventfiringdriver.switchTo().window(parentHandleName);
			     
			 }
			 catch(Throwable t)
			 {
				 t.printStackTrace();
			 }
		 }
		 
		 
		//code for no of windows 
			 public static ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
			     return new ExpectedCondition<Boolean>() {
			       @Override
			       public Boolean apply(WebDriver driver) {
			        eventfiringdriver.getWindowHandles();
			         return eventfiringdriver.getWindowHandles().size() == numberOfWindows;
			       }
			     };
			  
			 }
		
	
}
