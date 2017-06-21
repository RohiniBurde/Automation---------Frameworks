package com.uat.suite.tm_testpass;

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

public class TestSuiteBase extends TestBase{
	//String comments;
	//static WebDriverWait w;
    // check if the suite ex has to be skipped
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
	
	public boolean enterTestManagerName(String testManagerName)
	{
		try 
		{
			APP_LOGS.debug("Clicking on Test Manager Image Icon...");
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			  
			Thread.sleep(500);driver.switchTo().frame(1);
			APP_LOGS.debug("Switched to the Version Lead frame...");
		  
			//wait = new WebDriverWait(driver,200);
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
			return true;
		} 
		catch (Throwable t) 
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+" Test manager name provided is having some error.");
			return false;
		}
		
	}

	public boolean enterValidDataInMandatoryFieldsOfTestPass(String TestPassName, String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate ) throws InterruptedException, IOException
	{
		try
		{
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
			
			//getObject("TestPasses_createNewProjectLink").click();
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
			getObject("TestPasses_createNewProjectLink").click();
			Thread.sleep(500);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
			
			APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
			if(!enterTestManagerName(TP_TestManager))
			{
				return false;
			}
			
			APP_LOGS.debug("Selecting the End date");
			if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
			{
				return false;
			}
	
			APP_LOGS.debug("Click on Save");
			getObject("ProjectCreateNew_projectSaveBtn").click();
			
			
			String textFromThePopupAfterSaveButton ;
			
			textFromThePopupAfterSaveButton = getTextFromAutoHidePopUp();
			
			
			APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
			
			if (!textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")) {
				
				APP_LOGS.debug("Popup text is other than : 'Test Pass added successfully!'");
				return false;
			}
			
			return true;
		
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}
			
	}
	
	public boolean selectStartDateandEndDate(WebElement startEndDateImage, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
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
	     
	   //   List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
	     
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
	     return false;
	    }
	    
		return true;
	 }
	
	public void selectDetailsFromDD(WebElement allDropDown,WebElement DDElement,String xpathKey1, String xpathKey2, String elementName )
	{
		try 
		{
			allDropDown.click();
			//Thread.sleep(500);
			List<WebElement> DDMember = DDElement.findElements(By.tagName("li"));
			for(int i =1; i<=DDMember.size();i++)
			{
				//String listMemebers = getElement(xpathKey1, xpathKey2, i).getText();
				String listMemebers = getObject(xpathKey1, xpathKey2, i).getAttribute("title");
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
	
	//common function for creating project
	public boolean createProject(String group, String portfolio, String project, String version, String endMonth, String endYear, String endDate, String versionLead, String stakeHolder ) throws IOException, InterruptedException
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
				
				toSelectStakeholderfromPeoplePicker(getObject("ProjectCreateNew_StakeholderPeoplePickerImg"),stakeHolder);
				
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
	
	//Function to perform the selection of  version lead from the people  picker 
	public void toSelectStakeholderfromPeoplePicker( WebElement StakeholderPeoplePickerImage,String InputedTestData) throws InterruptedException
	{
	   
	    //Clicking on people picker image icon
	    APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");  
	    StakeholderPeoplePickerImage.click();
	   
	    //Switching to the people picker frame 
	    Thread.sleep(500);driver.switchTo().frame(1);
	    APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
	    
	    //Wait till the find text field is visible
	    //wait = new WebDriverWait(driver,20);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
	    
	    //Inputting test data in people picker text field
	    getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(InputedTestData); 
	    
	    //Clicking on Search button 
	    getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
	   
	    //Selecting search result 
	    getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
	    
	    //Clicking on add button from Version Stakeholder people picker frame
	    getObject("ProjectCreateNew_StakeholderAddBtn").click();
	     
	    //Clicking on OK button from People Picker
	    getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
	   
	    //Switching back to the default content 
	    driver.switchTo().defaultContent();
	   
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
							
				//			System.out.println(text + " : is selected...");
			  	
							break;
					}
			}
	  
	 
			if(flag==0)
		  
			{
				//Click on Plus icon to add Group or Portfolio
		//		System.out.println("Clicking on Add icon ");
			
				APP_LOGS.debug("Clicking on Add icon ");
				
				addButton.click();
	  
				
				
		//		System.out.println("Inputting Text :" +text +" in Text Field ");
				
				APP_LOGS.debug("Inputting Text :" +text +" in Text Field ");
	  
				getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(text);

				//Save the entered group or portfolio 
				if (getElement("ProjectCreateNew_projectAddPopUpLabel_Id").getText().contains("Project")) {
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

	public void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
			 {
				
					try
					{
							//Select start date   
							WebElement startDateImage = calendarImg;  
			   
							APP_LOGS.debug("Clicking on Date Calendar icon...");
			   
			//				System.out.println("Clicking on Date Calendar icon...");
			   
							startDateImage.click();  
			   
							Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
			   
							year.selectByValue(StartEndYear);
			   
							APP_LOGS.debug(StartEndYear +" : Year is selected...");
			   
			//				System.out.println(StartEndYear +" : Year is selected...");
			   
							Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
			   
							month.selectByVisibleText(StartEndMonth);
			   
							APP_LOGS.debug(StartEndMonth +" : Month is selected...");
			   
			//				System.out.println(StartEndMonth +" : Month is selected...");
							
							WebElement datepicker= getObject("ProjectCreateNew_dateTable");
			   
							//List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
			   
							List<WebElement> cols = datepicker.findElements(By.tagName("td"));
							
							for(WebElement cell :cols)
				   
							{
									if(cell.getText().equals(StartEndDate))
									{
											cell.findElement(By.linkText(""+StartEndDate+"")).click();
			     
											APP_LOGS.debug(StartEndDate +" : Date is selected...");
			     
				//							System.out.println(StartEndDate +" : Date is selected...");
			     
											break;
									}
							}
					}
				
					catch(Throwable t)
					{
						
						
						t.printStackTrace();
					}
			 }
	
	public void activeXHandling() throws IOException{
	try
	   {
	   eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
	   }catch(Exception e)
	   {
	    //Active x code
//	    System.out.println("Active x disabled");
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
	    if(getElement("activeX_Id").isDisplayed())
	    	getObject("TesterCreateNew_TesterActiveX_Close").click();
//	    else
//	     System.out.println("An alert informing the user of disabled activex should be present."); 
	   }
	}

	protected void dropDownSelect(WebElement dropDownList, String text)
	{
		List<WebElement> elements = dropDownList.findElements(By.tagName("option"));		  
		for(int i =0 ;i<elements.size();i++)
		{
 			if(elements.get(i).getAttribute("title").equals(text))
				{
						elements.get(i).click();
						
						APP_LOGS.debug( text + " : is selected...");
						
	//					System.out.println(text + " : is selected...");
		  	
						break;
				}
		}
	}
	
	protected boolean searchForTheTestPass(String testPassName) throws IOException, InterruptedException
	{
		int totalPages;
		int testPassesOnPage;
		String gridTestPass;
		//int projectFoundFlag=0;
		APP_LOGS.debug("Searching Test Pass to Edit");
		
		try
		{
		
			if (getElement("TestPassViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				
				totalPages=1;
				
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
				
				totalPages = getElement("TestPassViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
			}		
			
			for (int i = 0; i < totalPages; i++) 
			{
				testPassesOnPage = getElement("TestPassViewAll_Table_Id").findElements(By.xpath("tr")).size();
				
				for (int j = 1; j <= testPassesOnPage; j++) 
				{
					gridTestPass=getObject("TestPassViewAll_testPassNameXpath1", "TestPassViewAll_testPassNameXpath2", j).getAttribute("title");
					
					if (gridTestPass.equals(testPassName)) 
					{
						getObject("TestPassViewAll_editProjectIcon1", "TestPassViewAll_editProjectIcon2", j).click();
						Thread.sleep(1000);
						APP_LOGS.debug("Test Pass Found in View All page.");
						return true;
					}
					
					
				}			
				
				if (totalPages>1 && i!=(totalPages-1)) 
				{
					getObject("TestPassViewAll_NextLink").click();
				}
			}
			
			//assertTrue(false);
			APP_LOGS.debug("Test Pass Not found in View All page");
			
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			//assertTrue(false);
			APP_LOGS.debug("Exception in searchTest Pass AndEdit(). ");
			return false;
		}
		
	}
	
	protected boolean verifyTestPassFields(String testPassName, String description, String status, String managerName, String endMonth, String endYear, String endDate) throws IOException, InterruptedException
	{	
		
		String displayedTestPass;
		String displayedDescription;
		String displayedStatus;
		String displayedTestManager;
		String selectedEndDate;
		String expectedEndDate= getMonthNumber(endMonth)+"/"+dateHandler(endDate)+"/"+endYear;
		managerName= managerName.replace(".", " ");
		
		try
		{	
			displayedTestPass= getElement("TestPassCreateNew_testPassNameTextField_Id").getAttribute("value");
			
			displayedDescription=getElement("TestPass_Description_Id").getText();
			
			selectedEndDate=getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");
			
			displayedTestManager=getObject("TestPassCreateNew_testManagerDisplayField").getText();			
			
			displayedStatus = getElement("TestPassCreateNew_statusDropDown_Id").getAttribute("value");
			
			if (assertTrue(displayedTestPass.equals(testPassName) && displayedDescription.equals(description) && 
				displayedStatus.equals(status) && selectedEndDate.equals(expectedEndDate) && displayedTestManager.equalsIgnoreCase(managerName))) 
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
		
	protected boolean editTestPass(String testPassName, String description, String status, String managerName, String endMonth, String endYear, String endDate, String expectedSuccessMessage ) throws IOException, InterruptedException
	{	
		try
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
			
			getElement("TestPassCreateNew_testPassNameTextField_Id").clear();
			
			getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
	
			getElement("TestPass_Description_Id").clear();
			
			getElement("TestPass_Description_Id").sendKeys(description);
			
			Select statusTP = new Select(getElement("TestPassCreateNew_statusDropDown_Id"));
			
			statusTP.selectByVisibleText(status);
			
			selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  endMonth,  endYear, endDate);
			
			enterTestManagerName(managerName);

			APP_LOGS.debug("Click on update Button");
			
			getObject("ProjectCreateNew_projectUpdateBtn").click();
			
			String updateOpupTextAfterUpdateButtonClicked = getTextFromAutoHidePopUp();
			
			APP_LOGS.debug(this.getClass().getSimpleName()+" Text found on the popup is : "+updateOpupTextAfterUpdateButtonClicked );
			
		//	System.out.println("Popup text : " + updateOpupTextAfterUpdateButtonClicked);
				
			if (compareStrings(expectedSuccessMessage, updateOpupTextAfterUpdateButtonClicked)) 
			{
				APP_LOGS.debug("Got Popup with Text : "+updateOpupTextAfterUpdateButtonClicked);
				
				APP_LOGS.debug(this.getClass().getSimpleName()+" Click on OK Button of alert");
			
				//getObject("TestPasses_okButtonForPopup").click();
				
				return true;
				
			}
			else 
			{
				APP_LOGS.debug(this.getClass().getSimpleName()+" Getting Unexpexted popup text");
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Updated sucessfully popup is not displayed");
			
				return false;
	
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}		
		
	}

	private String getMonthNumber(String month)
	{
		switch (month) {
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
	
	private String dateHandler(String date)
	{
		int idate = Integer.parseInt(date);
		
		if (idate<10) 
		{
			date="0"+date;
		}
		
		return date;
	}
	
	protected void selectProjectFromDropdownHeads(String GroupName,String PortfolioName,String ProjectName,String Version) throws IOException, InterruptedException
	{
		//Thread.sleep(1000);
		APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Group "+ GroupName);
		
		dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
		
		dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), PortfolioName );
		
		dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
		
		dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
		
		/*selectDetailsFromDD(getElement("TestPasses_groupDropDown_Id"),getObject("TestPasses_groupDropDownMembers"),
				"TestPasses_groupMemberSelect1","TestPasses_MemberSelect2", GroupName);
		
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Portfolio "+ PortfolioName);
		
		selectDetailsFromDD(getElement("TestPasses_portfolioDropDown_Id"),getObject("TestPasses_portfolioDropDownMembers"),
				"TestPasses_portfolioMemberSelect1","TestPasses_MemberSelect2", PortfolioName);
	
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Project Name "+ ProjectName);
		
		
		selectDetailsFromDD(getElement("TestPasses_projectDropDown_Id"),getObject("TestPasses_projectDropDownMembers"),
				"TestPasses_projectMemberSelect1","TestPasses_MemberSelect2", ProjectName);
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"selecting the Version "+ Version);
		
		selectDetailsFromDD(getElement("TestPasses_versionDropDown_Id"),getObject("TestPasses_versionDropDownMembers"),
				"TestPasses_versionMemberSelect1","TestPasses_MemberSelect2", Version);*/
	}

	protected void createTester(String group, String portfolio, String project, String version, String testPassName, 
			String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Tester");
		Thread.sleep(500);
		//getElement("TesterNavigation_Id").click();
		
		if(getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
		{
			getObject("TesterCreateNew_TesterActiveX_Close").click();
		}
		Thread.sleep(500);
		
		try {
			
				dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
				getElement("Tester_createNewProjectLink_Id").click();
				Thread.sleep(1000);
				
				getObject("TesterCreateNew_PeoplePickerImg").click();
				   
				Thread.sleep(500);
				
				driver.switchTo().frame(1);
				
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
					}
				}
				String[] testerRoleSelectionArray = testerRoleSelection.split(",");
				List<WebElement> roleSelectionNames = getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
				int numOfRoles = roleSelectionNames.size();
			//	System.out.println(numOfRoles +"In UI ***************");
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
				
				getTextFromAutoHidePopUp();
				
				//getObject("Tester_testeraddedsuccessfullyOkButton").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTester function.");
			e.printStackTrace();
		}
		
	}

	public void createTestCase(String group, String portfolio, String project, String version, String testPassName, 
			String testCaseName) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Case");
		Thread.sleep(500);
	//	getElement("TestCaseNavigation_Id").click();
		if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
		{
			getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
		}
		Thread.sleep(500);

		try {
			
				dropDownSelect(getElement("TestCaseUpperRibbon_groupDropDown_Id"), getElement("TestCaseUpperRibbon_GroupList_Id"), group );
				
				dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_projectDropDown_Id"), getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_versionDropDown_Id"), getElement("TestCaseUpperRibbon_VersionList_Id"), version );
				
				dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				
				getObject("TestCase_createNewProjectLink").click();
				
				Thread.sleep(500);
				
				getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
				
				getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
				
				getTextFromAutoHidePopUp();
				
				//getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestCase function.");
			e.printStackTrace();
		}
		
	}

	public void createTestStep(String group, String portfolio, String project, String version, String testPassName, 
			String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
	{
		APP_LOGS.debug("Creating Test Step");
		Thread.sleep(500);
	//	getElement("TestStepNavigation_Id").click();
		if(getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
		{
			getObject("TestStepCreateNew_TestStepActiveX_Close").click();
		}
		Thread.sleep(500);
		
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
				
				getTextFromAutoHidePopUp();
				
				//getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
			
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in createTestStep function.");
			e.printStackTrace();
		}
		
	}
	
	private void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException, InterruptedException {
		
		Thread.sleep(500);
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
						
				//		System.out.println(selectionText + " : is selected...");
		  	
						break;
				}
		}
		
	}
}