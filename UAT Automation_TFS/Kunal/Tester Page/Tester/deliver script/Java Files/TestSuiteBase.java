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

public class TestSuiteBase extends TestBase{
	
	static boolean fail=false;
	String displayNamefromPioplePicker;
	
    // check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of TM_TestPass suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "TM_TestPass Suite"))
		{
			APP_LOGS.debug("Skipped TM_TestPass Suite as the runmode was set to NO");
			throw new SkipException("Runmode of TM_TestPass Suite set to no. So Skipping all tests in Suite A");
		}
	}
	
	public void enterTestManagerName(String testManagerName)
	{
		try 
		{
			APP_LOGS.debug("Clicking on Test Manager Image Icon...");
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			  
			driver.switchTo().frame(1);
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
		  
		driver.switchTo().frame(1);
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
		System.out.println("display name from people picker: "+displayNamefromPioplePicker);
		
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
	   System.out.println("The number of elements present in group drop down list is : " + DDMember.size());
	   for(int i =1; i<=DDMember.size();i++)
	   {
	    //String listMemebers = getElement(xpathKey1, xpathKey2, i).getText();
	    String listMemebers = getObject(xpathKey1, xpathKey2, i).getText();
	    System.out.println(listMemebers);
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
	
	public void callCreateNewProjectFunction(String GroupName, String PortfolioName, String ProjectName, String Version, String EndMonth, String EndYear, String EndDate, String versionLead, String stakeholder) throws IOException, InterruptedException
	{
		Thread.sleep(1000);
			//redirect to project page for group creation
			getElement("TM_projectsTab_Id").click();
			Thread.sleep(1000);
			//create project function calling
			createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead/*, stakeholder*/);
			
			//getElement("TM_testersTab_Id").click();
	}
	
	public int verifyHeaders(String dropdownID, String dropdownAllElements, String xPath1, String inputValue) throws IOException, InterruptedException
	{
		int flag=0;
		//click on group dropdown
		getElement(dropdownID).click();
		Thread.sleep(500);
		List<WebElement> listOfGrpElements = getObject(dropdownAllElements).findElements(By.tagName("li"));
		System.out.println(listOfGrpElements.size());
		
		for(int i =1; i<=listOfGrpElements.size();i++)
		{
			WebElement grpNameXpath = getObject(xPath1, "Attachments_xPath2_end", i);
			String grpElementTitle = grpNameXpath.getAttribute("title");
			
			if(grpElementTitle.equalsIgnoreCase(inputValue))
			{
				flag++;
				grpNameXpath.click();
				break;
			}
		}
		return flag;
	
	}
	
	/*public void createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead, String StakeholderName ) throws IOException, InterruptedException
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
				
				Thread.sleep(2000);
				
				
				//selecting stakeholder
			    //Clicking on people picker image icon
			    APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");  
			    getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
			   
			    //Switching to the people picker frame 
			    driver.switchTo().frame(1);
			    APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
			    
			    //Wait till the find text field is visible
			    wait = new WebDriverWait(driver,20);
			    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
			    
			    //Inputting test data in people picker text field
			    getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(StakeholderName); 
			    APP_LOGS.debug(StakeholderName + " : Inputted text in Version Stakeholder text field...");
			    
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
				     
				getObject("ProjectCreateNew_projectSaveBtn").click();
				
				Thread.sleep(2000);
				
				getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
		}
		
	}*/
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
	public void selectTesterName(String testerName) throws InterruptedException
	{
		APP_LOGS.debug("Clicking on Tester Image Icon...");
		getObject("TesterDetailsAddTester_testerPeoplePickerImg").click();
		  
		driver.switchTo().frame(1);
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
		System.out.println("display name from people picker: "+displayNamefromPioplePicker);
		
		APP_LOGS.debug("Click on OK button from frame ...");
		getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
	  
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
	}
	
	/*public void createTestPass(String group, String portfolio, String project, String version, String testPassName, 
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
				
				getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in createProject function.");
			e.printStackTrace();
		}
		
	}
	*/
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
	
	/*public void createTester(String group, String portfolio, String project, String version, String testPassName, 
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
				String[] testerRoleSelectionArray = testerRoleSelection.split(",");
				List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
				int numOfRoles = roleSelectionNames.size();
				System.out.println(numOfRoles +"In UI ***************");
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
				
				getObject("Tester_testeraddedsuccessfullyOkButton").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTester function.");
			e.printStackTrace();
		}
		
	}*/

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
	
	
}