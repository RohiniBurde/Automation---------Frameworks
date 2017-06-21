/*
Author Name:-SwatiGangarde
 * Created Date:-5th Nov 2014
 * Last Modified on Date:-06th Nov 2014
 * Module Description:- Execute test cases of Edit Project functionality*/
 

package com.uat.suite.tm_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class EditProjectFields extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=true;
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
	public void testEditProjectFields(
			String role, String GroupName,String PortfolioName,String ProjectName, String Version, String Description, 
			String StartMonth, String StartYear, String StartDate,String EndMonth, String EndYear, String EndDate, 
			String VersionLead, String Stakeholder, String ProjectURL,String AliasProjectURL, String ApplicationURL, 
			String AliasApplicationURL, String ExpectedMessageText,String EditGroupName,String EditPortfolioName,String EditProjectName, String EditVersion, String EditDescription, 
			String EditStartMonth, String EditStartYear, String EditStartDate,String EditEndMonth, String EditEndYear, String EditEndDate, 
			String EditProjectURL,String EditAliasProjectURL, String EditApplicationURL, 
			String EditAliasApplicationURL, String EditExpectedMessageText ) throws Exception
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
		
		//login("Stakeholder");
		APP_LOGS.debug("Calling Login with role "+ role);
		
		
		isLoginSuccess = login(role);

		if(isLoginSuccess)
		{
		
			APP_LOGS.debug(" Executing Test VerifyFieldsForCreNewProj");	
			
			getElement("UAT_testManagement_Id").click();
			APP_LOGS.debug("Clicking on Test Management tab ...");
			
			Thread.sleep(300);
			getObject("Projects_createNewProjectLink").click();
			APP_LOGS.debug("Clicking on Create New link ...");
			
			//To edit the project I need to create a new project so creation of new project is executing first
			//Group selection or Creation
			SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
	
			//Portfolio selection or Creation
			SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
			
			//Project Selection or Creation
			SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
			
			//Enter version data 	
			getObject("ProjectCreateNew_versionTextField").sendKeys(Version);	
			
			//To enter the description 
			getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(Description);
			
			//To select start date from Start Date Picker	
			toSelectStartDateandEndDate(getObject("ProjectCreateNew_startDateImage"),StartMonth,StartYear, StartDate );
			
			//To select end date from End Date Picker 
			toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);
			
			//Version Lead selection
			toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(1).username);
			
			//Stakeholder selection
			toSelectStakeholderfromPeoplePicker(getObject("ProjectCreateNew_StakeholderPeoplePickerImg"),stakeholder.get(0).username);
			
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
				APP_LOGS.debug("Clicking on OK Button to Save Project");	
				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button[1]")).click();
			}catch(Throwable e)
			{
				fail = true;
			}
			
			//Clicking on View All link
			APP_LOGS.debug("Clicking on VIEW ALL link");	
			getObject("Projects_viewAllProjectLink").click();
			
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
		    
	/*			Verify the name of the project present in Project column and the nam of the version present in the respecive project's version 
				 * column is match with the name of the project and version which was created in test case viz. "VerifyFieldsForCreNewProj" 
				 */
				if((projectNameCols.equals(ProjectName))&&(projectVersionCols.equals(Version)))
				{
					APP_LOGS.debug("Clicking on Edit Icon...");
					eventfiringdriver.findElement(By.xpath("//tbody[@id='showproject']/tr["+i+"]/td[7]/a[1]/img")).click();				
				}
		    }
	
		    //Group selection or Creation
		    try
			{	
		    	SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), EditGroupName );
			}
		    catch(Throwable e)
		    {
			   fail=true;
			   e.printStackTrace();
			}
		    
		   //Portfolio selection or Creation
		    try
		    {	
		    	SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), EditPortfolioName);
			}
		    catch(Throwable e)
		    {
				   fail=true;
				   e.printStackTrace();
			}
		    
		    //Update Project name 
	//	     1.By clearing it as in edit mode of project, project becomes text field
	//		 * 2.Clicking Update button to verify the validation message
	//		 * 3.Enter update project input 
			try
			{
				APP_LOGS.debug("Clearing Project Name...");
				getElement("ProjectCreateNew_projectDropDown_Id").clear();
				
				APP_LOGS.debug("Verifying validation message , when Project field data is cleared ...");
				validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectUpdateBtn"),"Project Name is a mandatory field!",getElement("ProjectEdit_validationMessage_Id"),OR.getProperty("ProjectEdit_validationMessage_Id"));
				
				APP_LOGS.debug("Entering Project Name...");
				getElement("ProjectCreateNew_projectDropDown_Id").sendKeys(EditProjectName);
			}
			catch(Throwable e)
			{
					fail=true;
					e.printStackTrace();
			}
		
			//Update Version field value/data 
		/*	 1.By clearing it
			 * 2.Clicking Update button to verify the validation message
			 * 3.Enter update version input */
			try
			{
				APP_LOGS.debug("Clearing Version...");
				getObject("ProjectCreateNew_versionTextField").clear();
				
				APP_LOGS.debug("Verifying validation message , when Project field data is cleared ...");
				validationMessageExceptVersionLead(getObject("ProjectCreateNew_projectUpdateBtn"),"Version is a mandatory field!", getElement("ProjectEdit_validationMessage_Id"),OR.getProperty("ProjectEdit_validationMessage_Id") );
				
				APP_LOGS.debug("Entering Version Name...");
				getObject("ProjectCreateNew_versionTextField").sendKeys(EditVersion);		
			}
			catch(Throwable e)
			{
				   fail=true;
				   e.printStackTrace();
			}
			
			//Update Description field value/data 
			//1.Enter description of project as it is not mandatory hence not clear the data present in it already 
			try
			{
				APP_LOGS.debug("Clearing Project Name...");
				getElement("ProjectCreateNew_descriptionTextField_Id").clear();
				
				APP_LOGS.debug("Description is getting enter...");	
				getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(EditDescription);			
			}
			catch(Throwable e)
			{
				   fail=true;
				   e.printStackTrace();
			}
			
			//Update Version Lead 
			try
			{
				Boolean versionLeadPeoplePickerImageisdisplayed=getObject("ProjectCreateNew_versionLeadPeoplePickerImg").isDisplayed();
				if(versionLeadPeoplePickerImageisdisplayed==true)
				{
					APP_LOGS.debug("Fail : Version Lead is having access to update himself...");
					comments+="Fail : Version Lead is having access to update himself...";
					compareStrings("Version lead should not have access updating himself", "Version lead is having access updating himself");
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), OR.getProperty("ProjectCreateNew_versionLeadPeoplePickerImg"));
				}
				else
				{
					APP_LOGS.debug("Version Lead is not having access to update himself...");
				
				}
				
			}
			catch(Throwable e)
			{
			   fail=true;
			   APP_LOGS.debug("Fail : Version Lead is having access to update himself...");
			   comments+="Fail : Version Lead is having access to update himself...";
			   e.printStackTrace();
			}
			
			//Selection of start date from Start Date Picker using function
			try
			{	
				toSelectStartDateandEndDate(getObject("ProjectCreateNew_startDateImage"),EditStartMonth,EditStartYear, EditStartDate );
				
			}catch(Throwable e)
			{
				   fail=true;
				   e.printStackTrace();
			}
			
			//Selection of end date from End Date Picker using function
			try
			{
				toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EditEndMonth,EditEndYear, EditEndDate);
			}
			catch(Throwable e)
			{
			   fail=true;
			   e.printStackTrace();
			}
			
			//Stakeholder selection
			try
			{
				toSelectStakeholderfromPeoplePicker(getObject("ProjectCreateNew_StakeholderPeoplePickerImg"),stakeholder.get(1).username);
			}
			catch(Throwable e)
			{
				fail=true;
				e.printStackTrace();
			}
			
			//Project URL
			try
			{
				//Clicking on Project URL image icon 
				APP_LOGS.debug("Project URL Image is gettng click...");	
				getObject("ProjectCreateNew_projectURLImageIcon").click();		
				APP_LOGS.debug("Project URL pop up is getting open...");	
				
				//To update Project URL data first clear data which is previously present in the field then enter updated data
				getElement("ProjectCreateNew_projectURLTextField_Id").clear();
				APP_LOGS.debug("Project URL is getting enter...");	
			    getElement("ProjectCreateNew_projectURLTextField_Id").sendKeys(EditProjectURL);		
			    
			    //To update Project URL alias data first clear dtaa which is previously present in the field then enter updated data
				getElement("ProjectCreateNew_projectAliasURLTextField_Id").clear();
				APP_LOGS.debug("Project URL Alias is getting enter...");
				getElement("ProjectCreateNew_projectAliasURLTextField_Id").sendKeys(EditAliasProjectURL);
				
				//After entering data in Project URL and Alias text field clicking on OK button
				APP_LOGS.debug("Clicking on OK button ...");
				getObject("ProjectCreateNew_projectAliasURLOKBtn").click();
			}
			catch(Throwable t)
			{
					fail=true;
					t.printStackTrace();
			}
			
			//Appplication URL
			try
			{			
				//Clicking on Project URL image icon 
				APP_LOGS.debug("Application URL Image is gettng click...");	
				getObject("ProjectCreateNew_applicationURLImageIcon").click();
				APP_LOGS.debug("Application URL pop up is getting open...");	
				
				//To update Application URL data first clear dtaa which is previously present in the field then enter updated data
				getElement("ProjectCreateNew_applicationURLTextField_Id").clear();
				APP_LOGS.debug("Application URL is getting enter...");	
				getElement("ProjectCreateNew_applicationURLTextField_Id").sendKeys(EditApplicationURL);
				
				//To update Application URL alias data first clear dtaa which is previously present in the field then enter updated data
				getElement("ProjectCreateNew_applicationAliasURLTextField_Id").clear();
				APP_LOGS.debug("Application URL Alias is getting enter...");
				getElement("ProjectCreateNew_applicationAliasURLTextField_Id").sendKeys(EditAliasApplicationURL);
				
				//After entering data in Project URL and Alias text field clicking on OK button
				APP_LOGS.debug("Clicking on OK button ...");
				getObject("ProjectCreateNew_applicationAliasURLOKBtn").click();
			
			}
			catch(Throwable t)
			{
				fail = true;
				t.printStackTrace();
			}
			
			//Clicking on Update button after entering test data in all mandatory and non mandatory text fields
			try
			{
				APP_LOGS.debug("Clicking on Update Button to Update Project...");		    
				getObject("ProjectCreateNew_projectUpdateBtn").click();
			}
			catch(Throwable e)
			{
				fail = true;
			}
			
			//Clicking OK button from successful project updation message box and verifiying the text then clicking on OK button
			try
			{
				//extracting text "Project and Verion is updated Successfully"
				APP_LOGS.debug("Extracting text Project and Verion is updated Successfully from Application...");
				String actual_SaveProjectandVersion_SuccessMessage = getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
				
				//Asserting text Project and Verion is updated Successfully from Application with the expected message text			
				compareStrings(actual_SaveProjectandVersion_SuccessMessage, EditExpectedMessageText);
				
				APP_LOGS.debug(ProjectName+": Project is Updated Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
				System.out.println(ProjectName+": Project is Updated Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);			
				
				APP_LOGS.debug("Clicking on OK Button to Update Project");
				eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button[1]")).click();
			}
			catch(Throwable e)
			{
				fail = true;
			}
			
				//Closing browser
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
		public void SelectorCreationGroupAndPortfolio(WebElement DropDownList, WebElement AddButton, String InputtedTestData) throws IOException
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
				else {
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
				String actualvValidatonText=actualValidationExtractID.getText();
				APP_LOGS.debug("Without entering any data in any fields validation message prompt as : " +actualvValidatonText);
				//Assert.assertEquals(expectedValidationText,actualvValidatonText);
				compareStrings(actualvValidatonText, expectedValidationText);
				
				//Click on OK button form validation pop up message
				eventfiringdriver.findElement(By.xpath("//div[@id='"+ID+"']/following-sibling::div[9]/div[1]/button[1]")).click();
				APP_LOGS.debug("Clicked on Ok button from validation message pop up");
			}
			catch(Throwable t)
			{
				fail=true;
				t.printStackTrace();
			}
		}
		
		public void validationMessageForVersionLead(WebElement SaveBtn,  String expectedValidationText) throws InterruptedException
		{
			try
			{
				//Checking for blank fields message for entering data in manadatory fields		
				SaveBtn.click();
				APP_LOGS.debug("Click on Save button to check the validation message");
				
				//Verified the text dispalying on pop up is same as that of the expected string 
				String actualvValidatonText=eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']")).getText();
				APP_LOGS.debug("Without entering any data in any fields validation message prompt as : " +actualvValidatonText);
				Assert.assertEquals(expectedValidationText,actualvValidatonText);
			
				//Click on OK button form validation pop up message		
				APP_LOGS.debug("Clicking on OK Button to Save Project");	
				getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();	
			}
			catch(Throwable t)
			{
				fail=true;
				t.printStackTrace();
			}
		}
	}
