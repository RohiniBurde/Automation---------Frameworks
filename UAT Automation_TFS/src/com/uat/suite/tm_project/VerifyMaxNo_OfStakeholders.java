package com.uat.suite.tm_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

//@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyMaxNo_OfStakeholders extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=true;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	String[] selectedStakeholder;
	String comments;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
		
		if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());


	}
	
	@Test(dataProvider="getTestData")
	public void verifyNumberOfStakeholders(
			String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, 
			String VersionLead, String Stakeholder, String ExpectedMessageText ) throws Exception
	{
		count++;
		comments="";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
			
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int versionlead_count = Integer.parseInt(VersionLead);
		int stakeholder_count = Integer.parseInt(Stakeholder);		
		
		
		versionLead = getUsers("Version Lead", versionlead_count);
		stakeholder = getUsers("Stakeholder", stakeholder_count);
		
		if (stakeholder==null) 
		{
			skip=true;
			comments+="Skipping because expected number of stakeholders not available to use in the test case. ";
			APP_LOGS.debug("Stakeholder count not proper for data set num. "+(count+1)+" ........So its skipping Test Set Data No."+(count+1));;
			
			throw new SkipException("Stakeholder count not proper for data set num. "+(count+1)+" ........So its skipping Test Set Data No."+(count+1));
		}
		
		//When Stakeholder count send from sheet is not equal to the number of stakeholder present in Users sheet then skip it
		/*if (stakeholder.size()!=stakeholder_count || stakeholder.size()>20) 
		{
			skip=true;
			APP_LOGS.debug("Required number of stakeholders not present in Users xls or it exceeds maximum number of stakeholders(20). Skipping for test set data no. "+(count+1));
			throw new SkipException("Required number of stakeholders not present in Users xls or it exceeds maximum number of stakeholders(20). Skipping for test set data no. "+(count+1));
		}*/
		
		openBrowser();		
		APP_LOGS.debug("Browser is getting opened... ");
		
		isLoginSuccess = login(role);
		APP_LOGS.debug("Calling Login with role "+ role);		

		if(isLoginSuccess)
		{
			APP_LOGS.debug("Executing Test VerifyFieldsForCreNewProj");	
	
			try
			{
				getElement("UAT_testManagement_Id").click();
				APP_LOGS.debug("Clicking on Test Management tab ...");
						
				Thread.sleep(300);
				getObject("Projects_createNewProjectLink").click();
				APP_LOGS.debug("Clicking on Create New link ...");
				
				//String[] arr = ((String)Stakeholder).split(",");
				//To edit the project I need to create a new project so creation of new project is executing first
				
				//Group selection or Creation
				SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
		
				//Portfolio selection or Creation
				SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
			
				//Project Selection or Creation
				SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
				
				//Enter version data 	
				getObject("ProjectCreateNew_versionTextField").sendKeys(Version);	
				
				//To select end date from End Date Picker 
				toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);
				
				//Version Lead selection
				toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(0).username);
				
				//Stakeholder selection
				
				//Clicking on people picker image icon
				APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");		
				getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
					
				//Switching to the people picker frame 
				Thread.sleep(500);driver.switchTo().frame(1);
				APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
			
					
			   for(int i=0 ; i< stakeholder.size() ; i++)
			   {
	               
				   //Wait till the find text field is visible
					//wait = new WebDriverWait(driver,20);
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty("ProjectCreateNew_versionLeadStakeholderTextField"))));
					
					//Inputting test data in people picker text field
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").clear();
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(stakeholder.get(i).username); 
					APP_LOGS.debug(stakeholder.get(i).username + " : Inputted text in Version Stakeholder text field...");
				
					//Clicking on Search button 
					getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
					APP_LOGS.debug("Clicked on Search button from Version Stakeholder People Picker ...");
				
					//Selecting search result 
					getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
					APP_LOGS.debug("Version Stakeholder is selected from searched user...");
					
					//Clicking on add button from Version Stakeholder people picker frame
					getObject("ProjectCreateNew_StakeholderAddBtn").click();
					APP_LOGS.debug("Clicked on Add button from Version Stakeholder People Picker frame ...");
		 
		       }
			
				//Clicking on OK button from People Picker
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
				APP_LOGS.debug("Clicked on OK button from Version Stakeholder People Picker frame ...");
			
				//Switching back to the default content 
				driver.switchTo().defaultContent();
				APP_LOGS.debug("Out of the Version Stakeholder People Picker frame...");
				  
			
				//Clicked on Save button after entering test data in all mandatory and non mandatory text fields
				
				APP_LOGS.debug("Clicking on Save Button to Save Project");    
				getObject("ProjectCreateNew_projectSaveBtn").click();
				
				
				
				//Clicking OK button from message box and verifiying the text also clicking on OK button
				String actual_SaveProjectandVersion_SuccessMessage;
				
				if (stakeholder.size()<21)
					actual_SaveProjectandVersion_SuccessMessage=getTextFromAutoHidePopUp();
				else
					actual_SaveProjectandVersion_SuccessMessage=getElement("ProjectCreateNew_alertDiv_Id").getText();
				
					
				if (!compareStrings(ExpectedMessageText, actual_SaveProjectandVersion_SuccessMessage)) 
				{
					fail= true;
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Stakeholders message issue");
				} 			
						
					
				
				
				APP_LOGS.debug("Clicking on OK Button");
				
				if(stakeholder.size()>20)
					eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button[1]")).click();
				
				closeBrowser();
				
				openBrowser();
				
				if (login("Admin"))
				{
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(500);
					
					if (stakeholder.size()<21) 
					{
						if (assertTrue(searchProjectInViewAllPage(ProjectName, Version))) 
						{
							comments+="Project found in View All table (Pass). ";
						}
						else
						{
							fail= true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project not found in View All Table");
							comments+="Project not found in View All table (Fail). ";
						}
					}
					else
					{
						if (assertTrue(!searchProjectInViewAllPage(ProjectName, Version))) 
						{
							comments+="Project not found in View All table (Pass). ";
						}
						else
						{
							fail= true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project found in View All Table");
							comments+="Project found in View All table (Fail). ";
						}
					}
				}
				else
				{
					fail= true;
					comments+="Login Failed for Admin(Fail). ";
				}				
				
				
				
			}
			catch(Throwable t)
			{
				APP_LOGS.debug("Exception Occured");	
				t.printStackTrace();
				fail=true;
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception");
			}
						
		
			//Closing browser
			APP_LOGS.debug("Browser is getting closed...");
			closeBrowser();
		
		}
				
	
	}
	
	@AfterMethod
	public void reportDataSetResult(){
		if(skip)
		{
			TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
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
	public void reportTestResult() throws Exception{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
		
		utilRecorder.stopRecording();
	}
	
	@DataProvider
	public Object[][] getTestData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}

	//Verifying if the Portfolio name is present in Portfolio/Process Name drop down , if it is present then select it  if not present then create it
		public void SelectorCreationGroupAndPortfolio(WebElement DropDownList, WebElement AddButton, String InputtedTestData) throws IOException, InterruptedException
		{
			
			List<WebElement> elements = DropDownList.findElements(By.tagName("option"));
			int flag = 0;
			
			for(int i =0 ;i<elements.size();i++)
			{
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
				Thread.sleep(2000);
				AddButton.click();
					
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
				Thread.sleep(500);driver.switchTo().frame(1);
				APP_LOGS.debug("Switched to the Version Lead People Picker frame...");
				
				//Wait till the find text field is visible
				//wait = new WebDriverWait(driver,20);
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
}