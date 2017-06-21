package com.uat.suite.tm_tester;

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
import com.uat.util.Xls_Reader;

public class TestSuiteBase extends TestBase{
	
	static boolean fail=false;
	String displayNamefromPeoplePicker;
	//String testerDisplayName;
	
    // check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of TM_TestPass suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "TM_Testers Suite"))
		{
			APP_LOGS.debug("Skipped TM_Testers Suite as the runmode was set to NO");
			throw new SkipException("Runmode of TM_Testers Suite set to no. So Skipping all tests in Suite A");
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
	
	public void deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(String TestPassName) throws InterruptedException
	{
	
		APP_LOGS.debug("Deleting Created Test Pass");
		
		getObject("TestPasses_viewAllProjectLink").click();
		
		String testPassNameFromViewAllPage = getObject("TestPassViewAll_testPassNameFromViewAllPage").getText();
	
		if (testPassNameFromViewAllPage.equals(TestPassName)) 
		{
			APP_LOGS.debug("Deleting Test Pass");
			
			//getObject("TestPassViewAll_deleteTestPassNameFromViewAllPage1", "TestPassViewAll_deleteTestPassNameFromViewAllPage2", i).click();
			getObject("TestPassViewAll_deleteTestPassNameImageFromViewAllPage").click();
			getObject("TestPassViewAll_popUpDeleteButton").click();
			Thread.sleep(1500);
			getObject("TestPasses_okButtonForPopup").click();
		}
		else 
		{
			APP_LOGS.debug("Test Pass name Not Found.");
		}
	}
	
	public void enterValidDataInMandatoryFieldsOfTestPass(String TestPassName, String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate ) throws InterruptedException, IOException
	{
			
		APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
		
		getObject("TestPasses_createNewProjectLink").click();
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
		getObject("TestPasses_createNewProjectLink").click();
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
		getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
		enterTestManagerName(TP_TestManager);
		
		APP_LOGS.debug("Selecting the End date");
		selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate);   

		APP_LOGS.debug("Click on Save");
		getObject("ProjectCreateNew_projectSaveBtn").click();
		
		
		String textFromThePopupAfterSaveButton = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
		APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
		
		if (textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")) {
			
			APP_LOGS.debug("Click on OK Button");
			
			getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
			
		}
		else {
			APP_LOGS.debug("Popup text is other than : 'Test Pass added successfully!'");
			fail = true;
		}
		
			
	}
	
	public void enterTesterName(String testerName) throws InterruptedException
	{
		APP_LOGS.debug("Clicking on Tester Image Icon...");
		getObject("TesterDetailsAddTester_testerPeoplePickerImg").click();
		  
		Thread.sleep(500);driver.switchTo().frame(1);
		APP_LOGS.debug("Switched to the Version Lead frame...");
	  
		//wait = new WebDriverWait(driver,200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
	  
		APP_LOGS.debug(testerName + " : Input text in Tester text field...");
		getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(testerName); 
	   
		APP_LOGS.debug("Click on Search button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
	   
		APP_LOGS.debug("Tester is selected from search result...");
		getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
	  
		displayNamefromPeoplePicker = getObject("TesterDetailsAddTester_testerDisplayNameFromPeoplePicker").getText();
		
		APP_LOGS.debug("Click on OK button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
	  
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
	}
		
	public void selectStartDateandEndDate(WebElement startEndDateImage, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
	   {
	   
	    try
	    {
	      //Select start date   
	      WebElement startDateImage = startEndDateImage;  
	     
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
	     fail=true;
	     
	     t.printStackTrace();
	    }
	    
	   }
	
	public void selectDetailsFromDD(WebElement allDropDown,WebElement DDElement,String xpathKey1, String xpathKey2, String elementName )
	 {
	  try 
	  {
	   allDropDown.click();
	   Thread.sleep(1000);
	   List<WebElement> DDMember = DDElement.findElements(By.tagName("li"));
	   for(int i =1; i<=DDMember.size();i++)
	   {
	    //String listMemebers = getElement(xpathKey1, xpathKey2, i).getText();
	    String listMemebers = getObject(xpathKey1, xpathKey2, i).getText();
	    if(listMemebers.equals(elementName))
	    {
	     getObject(xpathKey1, xpathKey2, i).click();
	    }
	   }
	  } 
	  catch (Throwable t) {
	   APP_LOGS.debug("Test Case Has Been FAILED In 'selectDetailsFromDD' function.");
	   //fail = true;
	  }
	 }

	public boolean verifyHeaders(String dropdownID, String dropdownAllElements, String xPath1, String inputValue) throws IOException, InterruptedException
	{
		Thread.sleep(500);
		//click on group dropdown
		getElement(dropdownID).click();
		//Thread.sleep(500);
		List<WebElement> listOfGrpElements = getObject(dropdownAllElements).findElements(By.tagName("li"));
		
		for(int i =1; i<=listOfGrpElements.size();i++)
		{
			WebElement grpNameXpath = getObject(xPath1, "Attachments_xPath2_end", i);
			String grpElementTitle = grpNameXpath.getAttribute("title");
			
			if(grpElementTitle.equalsIgnoreCase(inputValue))
			{
				
				grpNameXpath.click();
				return true;
			}
		}
		return false;
	}
		
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead/*,String StakeholderName*/ ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Projects.");
		
		//wait.until(ExpectedConditions.presenceOfElementLocated())

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
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
			return false;
		}
		
	}	
	
	public void selectTesterName(String testerName) throws InterruptedException
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
	  
		displayNamefromPeoplePicker = getObject("TesterDetailsAddTester_testerDisplayNameFromPeoplePicker").getText();
		
		APP_LOGS.debug("Click on OK button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
	  
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
	}
	
	public boolean createTestPass(String group, String portfolio, String project, String version, String testPassName, 
			String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Pass");
		//Thread.sleep(2500);
		getElement("TestPassNavigation_Id").click();
		Thread.sleep(500);
		wait.until(ExpectedConditions.elementToBeClickable(getObject("TestPasses_createNewProjectLink"))).click();
		Thread.sleep(500);
		
		
		try {
			
				dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
				Thread.sleep(500);
				dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
				Thread.sleep(500);
				dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
				Thread.sleep(500);
				dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
				//getElement("Version").sendKeys(version);
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
			APP_LOGS.debug("EXception in create test pass function.");
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
			String tester, String testerRoleCreation, String testerDescription, String testerRoleSelection) throws IOException, InterruptedException
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
				
				displayNamefromPeoplePicker = getObject("TesterCreateNew_PeoplePickerSelectSearchResult").getText();
		   
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
						getElement("TesterAddTester_descriptionTextBox_Id").sendKeys(testerDescription);
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
	
	public void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException {
		
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
	
	public String[] retrieveNameOfTester(String filepath)
	{
		Xls_Reader testerNameSpecific = new Xls_Reader(filepath);
		int numOfRows = testerNameSpecific.getRowCount("Tester Template");
		String[] testerName = new String[numOfRows-4];
		int j=0;
		for(int i=5;i<=numOfRows;i++)
		{
			testerName[j] = testerNameSpecific.getCellData("Tester Template", 4, i);
			j++;
		}
		return testerName;
	}
	
	protected boolean searchForTheTester(String testerName) throws IOException, InterruptedException
	{
		int totalPages;
		int testersOnPage;
		String gridTesters;
		//int projectFoundFlag=0;
		APP_LOGS.debug("Searching Tester to Edit");
		
		try
		{
			
			if (getElement("TestersViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				
				totalPages=1;
				
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
				
				totalPages = getElement("TestersViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
			}		
			
			for (int i = 0; i < totalPages; i++) 
			{
				testersOnPage = getObject("TestersViewAll_testerAvailableInGrid").findElements(By.xpath("tr")).size();
				
				for (int j = 1; j <= testersOnPage; j++) 
				{
					gridTesters=getObject("TesterViewAll_testerNameXpath1", "TesterViewAll_testerNameXpath2", j).getText();
					
					if (gridTesters.equalsIgnoreCase(testerName.replace(".", " "))) 
					{
						getObject("TesterViewAll_editProjectIcon1", "TesterViewAll_editProjectIcon2", j).click();
						Thread.sleep(1000);
						APP_LOGS.debug("Tester Found in View All page.");
						return true;
					}
					
					
				}			
				
				if (totalPages>1 && i!=(totalPages-1)) 
				{
					getObject("TestersViewAll_enableNextLink").click();
				}
			}
			
			//assertTrue(false);
			APP_LOGS.debug("Tester Not found in View All page");
			
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			//assertTrue(false);
			APP_LOGS.debug("Exception in searchTesterAndEdit(). ");
			return false;
		}
		
	}
	
	protected boolean verifyTesterFields(String testerName, String roleName, String roleDescription, String tester_area) throws IOException, InterruptedException
	{	
		String displayedTesterName;
		String displayedTesterRole;
		String displayedTesterDescription;
		String displayedArea;
		testerName= testerName.replace(".", " ");
		
		try
		{	
			displayedTesterName= getObject("TesterCreateNew_testerDisplayField").getText();
			
			getElement("TesterAddTester_editRole_Id").click();
			
			displayedTesterRole = getElement("TesterAddTester_roleTextBox_Id").getAttribute("value");
			
			displayedTesterDescription=getElement("TesterAddTester_descriptionTextBox_Id").getText();
			
			displayedArea = new Select(getElement("TesterAddTester_Area_ID")).getFirstSelectedOption().getText();
			
			if (assertTrue(displayedTesterName.equalsIgnoreCase(testerName) && displayedTesterRole.equals(roleName) && 
					displayedTesterDescription.equals(roleDescription) && displayedArea.equals(tester_area))) 
			{
				return true;
				
			}
			
			return false;					
		}
		catch(Throwable t)
		{
			
			t.printStackTrace();
			return false;
		}	
	}
}