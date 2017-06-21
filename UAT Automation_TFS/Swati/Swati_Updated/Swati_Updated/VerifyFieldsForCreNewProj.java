/*Author Name:-SwatiGangarde
 * Created Date:-31st of Oct 2014
 * Last Modified on Date:-05th Nov 2014
 * Module Description:- Creation of project with the positive data and handeling the validation message if any i=of the mandatory field is blank
 */


package com.uat.suite.tm_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;
@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyFieldsForCreNewProj extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=true;
	static int count=-1;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());

	}
	
	@Test(dataProvider="getTestData")
	public void testVerifyFieldsForCreNewProj(
			String role, String GroupName,String PortfolioName,String ProjectName, String Version, String Description, 
			String StartMonth, String StartYear, String StartDate,String EndMonth, String EndYear, String EndDate, 
			String VersionLead, String Stakeholder, String ProjectURL,String AliasProjectURL, String ApplicationURL, 
			String AliasApplicationURL, String ExpectedBlankValidatiionMessage ,String ExpectedMessageText ) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
			
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int versionlead_count = Integer.parseInt(VersionLead);
		int stakeholder_count = Integer.parseInt(Stakeholder);
		
		versionLead=new ArrayList<Credentials>();
		
		stakeholder=new ArrayList<Credentials>();
		
		versionLead = getUsers("Version Lead", versionlead_count);
		stakeholder = getUsers("Stakeholder", stakeholder_count);
		
		openBrowser();	
		
		APP_LOGS.debug("Browser is getting opened... ");
		
		APP_LOGS.debug("Calling Login with role "+ role);
	
		
		
		isLoginSuccess = login(role);
		
		if(isLoginSuccess)
		{
			APP_LOGS.debug("Login Successfull");
			
			APP_LOGS.debug("Executing Test VerifyFieldsForCreNewProj");	
		
			getElement("UAT_testManagement_Id").click();
					
			Thread.sleep(300);
			
			getObject("Projects_createNewProjectLink").click();
			
			//Without entering or selecting group name clicking in Save button validation message appears for that using the function viz.'validationMessageExceptVersionLead'
			APP_LOGS.debug("Verifying Group is not selected ...");
			validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage,getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));
			
			//After validation message pop up selecting or creating group using this function viz. 'SelectOrCreationGroupAndPortfolioAndProject'
			SelectOrCreationGroupAndPortfolioAndProject(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
		
			//Without entering or selecting portfolio name clicking in Save button validation message appears for that using the function viz.'validationMessageExceptVersionLead'
			APP_LOGS.debug("Verifying Portfolio is not selected ...");
			validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage, getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));
			
			//After validation message pop up selecting or creating portfolio using this function viz. 'SelectOrCreationGroupAndPortfolioAndProject'
			SelectOrCreationGroupAndPortfolioAndProject(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
			
			//Without entering or selecting project name clicking in Save button validation message appears for that using the function viz.'validationMessageExceptVersionLead'
			APP_LOGS.debug("Verifying Project is not selected ...");
			validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage,getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));
			
			//After validation message pop up selecting or creating project using this function viz. 'SelectOrCreationGroupAndPortfolioAndProject'
			SelectOrCreationGroupAndPortfolioAndProject(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
			
			//Enter version data 		
			APP_LOGS.debug("Verifying version field is blank ...");
			validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage,getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));	
				
			APP_LOGS.debug("Entering Version Name...");
			getObject("ProjectCreateNew_versionTextField").sendKeys(Version);		
			
			//To enter the description 
			try
			{	
				APP_LOGS.debug("Description is getting entered...");
				getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(Description);
			}
			catch(Throwable e)
			{
				   fail=true;
				   e.getMessage();
			}
			
			//Version Lead selection
			try
			{
				APP_LOGS.debug("Verifying version lead field is blank ...");
				validationMessageForVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),"Version Lead cannot be empty!");
				toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(1).username);

			}catch(Throwable e){
				   fail=true;
			}
			
			//Start Date
			try
			{	
				APP_LOGS.debug("Verifying Start Date mandatory field is blank ...");
				validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage, getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));
				
				//To select start date from Start Date Picker	
				toSelectStartDateandEndDate(getObject("ProjectCreateNew_startDateImage"),StartMonth,StartYear, StartDate );
				
			}catch(Throwable e)
			{
				   fail=true;
				   System.out.println(e.getMessage());
			}
			
			//End Date selection 
			try
			{
	
				APP_LOGS.debug("Verifying End Date mandatory field is blank ...");
				validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectSaveBtn"),ExpectedBlankValidatiionMessage, getElement("ProjectCreateNew_validationMessage_Id"),OR.getProperty("ProjectCreateNew_validationMessage_Id"));
				
				//To select end date from End Date Picker 
				toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);
			}
			catch(Throwable e)
			{
				   fail=true;
				   System.out.println(e.getMessage());
			}
			
			//Stakeholder selection
			try{
				//Stakeholder selection
				toSelectStakeholderfromPeoplePicker(getObject("ProjectCreateNew_StakeholderPeoplePickerImg"),stakeholder.get(0).username);
			}
			catch(Throwable e)
			{
				fail=true;
				System.out.println(e.getMessage());
			}
			
			//Project URL
			try{
				//Clicking on Project URL image icon 
				getObject("ProjectCreateNew_projectURLImageIcon").click();			
				getElement("ProjectCreateNew_projectURLTextField_Id").sendKeys(ProjectURL);		
				getElement("ProjectCreateNew_projectAliasURLTextField_Id").sendKeys(AliasProjectURL);		
				getObject("ProjectCreateNew_projectAliasURLOKBtn").click();
					
			}catch(Throwable e )
			{
				fail = true;
				e.printStackTrace();
			}
		
			//Application URL
			try{
				//Clicking on Application URL image icon 
				getObject("ProjectCreateNew_applicationURLImageIcon").click();			
				getElement("ProjectCreateNew_applicationURLTextField_Id").sendKeys(ApplicationURL);			
				getElement("ProjectCreateNew_applicationAliasURLTextField_Id").sendKeys(AliasApplicationURL);			
				getObject("ProjectCreateNew_applicationAliasURLOKBtn").click();
				
			}catch(Throwable e )
			{
				fail = true;
				e.printStackTrace();
			}
		
			//Clicked on Save button after entering test data in all mandatory and non mandatory text fields
			try{
				APP_LOGS.debug("Clicking on Save Button to Save Project");    
				getObject("ProjectCreateNew_projectSaveBtn").click();
			}catch(Throwable e)
			{
				fail = true;
				e.printStackTrace();
			}
		
			//Clicking OK button from message box and verifiying the text also clicking on OK button
			try{
				
				String actual_SaveProjectandVersion_SuccessMessage = getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
				compareStrings(actual_SaveProjectandVersion_SuccessMessage, ExpectedMessageText);
				
				APP_LOGS.debug(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
				comments = comments+(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
				
				try{
					APP_LOGS.debug("Clicking on OK Button to Save Project");			    
					eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button[1]")).click();
				}catch(Throwable e)
				{
					fail = true;
					e.printStackTrace();
				}
			
			}catch(Throwable e)
			{
				fail = true;
				e.printStackTrace();
			}
			//Clicked on
			APP_LOGS.debug("Browser is getting closed...");
			closeBrowser();
		
			/////////////////Verifying VL is able to view the creatd project 
			openBrowser();	
			
			APP_LOGS.debug("Browser is getting opened... ");
						
			login(versionLead.get(1).username, versionLead.get(1).password, "Version Lead");
			APP_LOGS.debug("Login Successfull");
			
			APP_LOGS.debug("Executing Test VerifyFieldsForCreNewProj");	
		
			getElement("UAT_testManagement_Id").click();
					
			Thread.sleep(300);
			
			//Extracting number of rows and columns from View All Project table		
			APP_LOGS.debug("Extracting Number of projects present in Project Table");
			WebElement projectTable = getElement("ProjectViewAll_Table_Id");
			List<WebElement> projectRows = projectTable.findElements(By.tagName("tr"));
			
			for (int i = 1; i <=projectRows.size(); i++) 
		    {	
		    	//Extracting the number of project present in Project column
		    	String  projectNameCols = eventfiringdriver.findElement(By.xpath("//tbody[@id='showproject']/tr["+i+"]/td[2]/span")).getAttribute("title");
				System.out.println(projectNameCols);
				
				//Extracting the number of project's version is present in Version column
				String  projectVersionCols = eventfiringdriver.findElement(By.xpath("//tbody[@id='showproject']/tr["+i+"]/td[3]/span")).getText();
				System.out.println(projectVersionCols);
				
				if((projectNameCols.equals(ProjectName))&&(projectVersionCols.equals(Version)))
				{
					APP_LOGS.debug("Hence Verified Version Lead is able to access the project : " +ProjectName);
					break;			
				}
		    }
		
			//Clicked on
			APP_LOGS.debug("Browser is getting closed...");
			closeBrowser();
		
		
		}
		else 
		{
			isLoginSuccess=false;
			closeBrowser();
		}			
		
	}
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else{
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
			
		
		skip=false;
		fail=false;		

	}
	
	
	@AfterTest
	public void reportTestResult(){
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	//Verifying if the Portfolio name is present in Portfolio/Process Name drop down , if it is present then select it  if not present then create it
	public void SelectOrCreationGroupAndPortfolioAndProject(WebElement DropDownList, WebElement AddButton, String InputtedTestData) throws IOException
	{
		
		List<WebElement> elements = DropDownList.findElements(By.tagName("option"));
		System.out.println("The elements present inside  Dropdown List is : " +elements.size());
		int flag = 0;
		
		for(int i =0 ;i<elements.size();i++)
		{
			System.out.println("Each element's present inside Dropdown is having text as : " + elements.get(i).getText());
			if(elements.get(i).getText().equals(InputtedTestData))
			{
				flag++;
				elements.get(i).click();
				break;
			}
		}
		if(flag==0)
		{
			//Click on Plus icon to add Group or Portfolio
			AddButton.click();
			System.out.println("Click on Add icon ");
				
			getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").clear();
			getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(InputtedTestData);
			
			//To validate the blank input test data if the test data is not blank then go here in IF
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
	
	
	//Function to select the strat date and end dat efrom date picker 
	public void toSelectStartDateandEndDate(WebElement startEndDateImage, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
	{
		try
		{
			//Select start date 		
			WebElement startDateImage = startEndDateImage;		
			startDateImage.click();		
			APP_LOGS.debug("Clicked on Date Calendar icon...");
			
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
	

	//Function to perform the selection of  version lead from the people  picker 
	public void toSelectVersionLeadfromPeoplePicker( WebElement VersionLeadPeoplePickerImage,String InputedTestData)
	{
		try{
			//Clicking on people picker image icon
			APP_LOGS.debug("Clicking on Version Lead People Picker Image Icon...");
			VersionLeadPeoplePickerImage.click();
	
			//Switching to the people picker frame 
			driver.switchTo().frame(1);
			APP_LOGS.debug("Switched to the Version Lead People Picker frame...");
			
			//Wait till the find text field is visible
			wait = new WebDriverWait(driver,20);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
	
			//Inputting test data in people picker text field
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(InputedTestData); 
			APP_LOGS.debug(InputedTestData + " : Inputted text in Version Lead text field...");
		
			//Clicking on Search button 
			getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			APP_LOGS.debug("Clicked on Search button from People Picker ...");
		
			//Selecting search result 
			getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			APP_LOGS.debug("Version Lead is selected from searched user...");
		
			//Clicking on OK button from People Picker
			getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			APP_LOGS.debug("Clicked on OK button from Version Lead People Picker frame ...");
		
			//Switching back to the default content 
			driver.switchTo().defaultContent();
			APP_LOGS.debug("Out of the Version Lead People Picker frame...");
	
		}
		catch(Throwable t){
			fail=true;
			t.printStackTrace();
		}
	}
	
	//Function to perform the selection of  version lead from the people  picker 
	public void toSelectStakeholderfromPeoplePicker( WebElement StakeholderPeoplePickerImage,String InputedTestData)
	{
			try{
				//Clicking on people picker image icon
				APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");		
				StakeholderPeoplePickerImage.click();
			
				//Switching to the people picker frame 
				driver.switchTo().frame(1);
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
	
	public void validationMessageExceptVersionLead(WebElement SaveUpdateBtn,  String expectedValidationText, WebElement actualValidationExtractID, String ID)
	{
		try{
			//Checking for blank fields message for entering data in manadatory fields		
			SaveUpdateBtn.click();
			APP_LOGS.debug("Click on Save or Update button to check the validation message");
		
			//Verified the text dispalying on pop up is same as that of the expected string 
			String actualValidatonText=actualValidationExtractID.getText();
			APP_LOGS.debug("Without entering any data in any fields validation message prompt as : " +actualValidatonText);
			
			//Comparing text of validation messages  			
			compareStrings(actualValidatonText, expectedValidationText);
			
			//Click on OK button form validation pop up message
			eventfiringdriver.findElement(By.xpath("//div[@id='"+ID+"']/following-sibling::div[9]/div[1]/button[1]")).click();
			APP_LOGS.debug("Clicked on Ok button from validation message pop up");
		}
		catch(Throwable t)
		{
			fail=true;
			System.out.println(t.getMessage());
		}
	}
	
	public void validationMessageForVersionLead(WebElement SaveBtn,  String expectedVLValidationText) throws InterruptedException
	{
		try
		{
			//Checking for blank fields message for entering data in manadatory fields		
			SaveBtn.click();
			APP_LOGS.debug("Click on Save button to check the validation message");
			
			//Verified the text dispalying on pop up is same as that of the expected string 
			String actualVLValidatonText=eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']")).getText();
			APP_LOGS.debug("Without entering any data in any fields validation message prompt as : " +actualVLValidatonText);
		
			//Compairing the texts of validation message coming on keeping blank the Version Lead
			compareStrings(actualVLValidatonText, expectedVLValidationText);
			
			//Click on OK button form validation pop up message		
			APP_LOGS.debug("Clicking on OK Button to Save Project");	
			getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();	
		}
		catch(Throwable t)
		{
			fail=true;
			System.out.println(t.getMessage());
		}
	}
}
