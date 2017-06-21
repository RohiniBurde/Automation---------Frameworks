package com.uat.suite.tm_project;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class VerifyProjFieldsValidation extends TestSuiteBase {
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	boolean isLoginSuccess=true;
	int count=-1;
	int projectLimitPerPage=10;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	DateFormat dateFormat;
	Calendar calendar;
	String actualDate;
	String expectedDate;
	String currentDay;
	String currentMonth;
	String currentYear;
	String comments;
	String selectedVersionLead;
	String displayedVersionLead;
	String[] selectedStakeholder;
	String[] displayedStakeholder;
	Select statusDD;
	String actualStatus;
	Utility utilRecorder = new Utility();
	
	
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip() throws Exception{
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			
			versionLead=getUsers("Version Lead", 1);
			
			stakeholder=new ArrayList<Credentials>();
			
			dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			calendar = Calendar.getInstance();
			
			
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

			

		}
		
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void verifyProjFieldsValidation(String group, String portfolio, String project, String version, String startMonth, 
				String startYear, String startDate, String endMonth, String endYear, String endDate, String role, String totalRequiredStakeholders,
				String status, String projectUrl, String projectAlias, String appUrl, String appAlias) throws Exception
		{
			
			String displayedURL;
			
			String displayedAlias;
			
			int stakeholderFailCount = 0;
			
			int numOfStakeholders = Integer.parseInt(totalRequiredStakeholders);
			
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			stakeholder=getUsers("Stakeholder",numOfStakeholders );
			
			
			
			if (stakeholder==null) {
				
				skip=true;
				APP_LOGS.debug("Required number of stakeholders not present in Users xls or it exceeds maximum number of stakeholders(20). Skipping for test set data no. "+(count+1));
				throw new SkipException("Required number of stakeholders not present in Users xls or it exceeds maximum number of stakeholders(20). Skipping for test set data no. "+(count+1));
				
			}
			
			selectedStakeholder= new String[numOfStakeholders];
			displayedStakeholder= new String[numOfStakeholders];
			
			openBrowser();
			
			APP_LOGS.debug("Calling Login with role "+ role);
			
			if(login(role))
			{
				try 
				{				
				
				
					APP_LOGS.debug("Login Successfull");
					
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(500);
					
					getObject("Projects_createNewProjectLink").click();
					
					dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
					
					dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
					
					dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), (project) );
					
					getObject("ProjectCreateNew_versionTextField").sendKeys(version);
					
					expectedDate = dateFormat.format(calendar.getTime());
					
					actualDate=getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
					
					APP_LOGS.debug("Verifying Start Date as today's date");
					
					if (compareStrings(expectedDate, actualDate)) 
					{
						APP_LOGS.debug("In Start date field, the default value selected is 'Todays' date");
						comments ="In Start date field, the default value selected is 'Todays' date(Pass). ";
					}
					else 
					{
						
						APP_LOGS.debug("In Start date field, the default value selected is not 'Todays' date");
						comments="In Start date field, the default value selected is not 'Todays' date(Fail). ";
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StartDateFieldDefault");
						
					}
										
					APP_LOGS.debug("Verifying End Date as blank");
					
					expectedDate = "";
					
					actualDate=getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");
					
					
					if (compareStrings(expectedDate, actualDate)) 
					{
						APP_LOGS.debug("In End date field, the default value is blank");
						
						comments=comments+"In End date field, the default value is blank(Pass). ";
					}
					else 
					{
						
						APP_LOGS.debug("In End date field, the default value is not blank");
						comments=comments+"In End date field, the default value is not blank(Fail). ";
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "EndDateFieldDefault");
						
					}
					
					
					
					APP_LOGS.debug("Verifying Start Date Calendar has today's date");
					
					if (!assertTrue(checkCalenderCurrentDate(getObject("ProjectCreateNew_startDateImage")))) 
					{
						//assertTrue(false);
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StartDateCalenderDefault");
						APP_LOGS.debug("In start date calendar, the default values are not of current date");
						comments=comments+"In start date calendar, the default values are not of current date(Fail). ";
						
					}
					else 
					{
						APP_LOGS.debug("In start date calendar, the default values are of current date");
						comments=comments+"In start date calendar, the default values are of current date(Pass). ";
					}
					
					
					
					APP_LOGS.debug("Verifying Start Date to be as selected date");
					
					selectStartOrEndDate(getObject("ProjectCreateNew_startDateImage"), startMonth, startYear, startDate);
					
					actualDate=getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
					
					expectedDate=getMonthNumber(startMonth)+"/"+startDate+"/"+startYear;
					
					if (compareStrings(expectedDate, actualDate)) {
						
						APP_LOGS.debug("In Start date field, the selected value is as expected");
						comments = comments+"In Start date field, the selected value is as expected(Pass). ";
						
					}
					else 
					{
						APP_LOGS.debug("In Start date field, the selected value is not as expected");
						comments = comments+"In Start date field, the selected value is not as expected(Fail). ";
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StartDateSelectedDate");
						
					}
					
					
					
					APP_LOGS.debug("Verifying End Date calendar has today's date");
					
					if (!assertTrue(checkCalenderCurrentDate(getObject("ProjectCreateNew_endDateImage")))) 
					{
						//assertTrue(false);
						fail=true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "EndDateCalenderDefault");
						APP_LOGS.debug("In end date calendar, the default values are not of current date");
						comments=comments+"In end date calendar, the default values are not of current date(Fail). ";
						
					}
					else 
					{
						APP_LOGS.debug("In end date calendar, the default values are of current date");
						comments=comments+"In end date calendar, the default values are of current date(Pass). ";
					}
					
					APP_LOGS.debug("Verifying End Date to be as selected date");
					
					selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"), endMonth, endYear, endDate);
					
					actualDate=getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");
					
					expectedDate=getMonthNumber(endMonth)+"/"+endDate+"/"+endYear;
					
					if (compareStrings(expectedDate, actualDate)) 
					{
						
						APP_LOGS.debug("In End date field, the selected value is as expected");
						comments = comments+"In End date field, the selected value is as expected(Pass). ";
						
					}
					else 
					{
						
						APP_LOGS.debug("In End date field, the selected value is not as expected");
						comments = comments+"In End date field, the selected value is not as expected(Fail). ";
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "EndDateSelectedDate");

						
					}
					
					
					APP_LOGS.debug("Verifying Version Lead People picker functionality");
					
					getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
					   
					Thread.sleep(500);driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead.get(0).username); 
					   
					getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
					
					selectedVersionLead = getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").getText();
			   
					getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			   
					getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					displayedVersionLead=getObject("ProjectCreateNew_versionLeadDisplayField").getText();
					
					if (compareStrings(selectedVersionLead, displayedVersionLead)) 
					{
						
						APP_LOGS.debug("Version Lead is correctly displayed");						
						comments=comments+"Version Lead is correctly displayed(Pass). ";
						
					}
					else 
					{
						
						fail= true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VersionLeadIssue");
						APP_LOGS.debug("Version Lead is not correctly displayed");						
						comments=comments+"Version Lead is not correctly displayed(Fail). ";

					}
					
					
					
					APP_LOGS.debug("Verifying Stakeholder People picker functionality");
					
					getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
					   
					Thread.sleep(500);driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					for (int i = 0; i < stakeholder.size(); i++) {
						
						getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(stakeholder.get(i).username);
						
						getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
						
						Thread.sleep(500);
						
						selectedStakeholder[i]=getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").getText();
						
						Thread.sleep(500);
						
						getObject("ProjectCreateNew_StakeholderAddBtn").click();
						
						getObject("ProjectCreateNew_versionLeadStakeholderTextField").clear();
															
					}
					
					getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
					
					driver.switchTo().defaultContent();
					
					Thread.sleep(1000);
					
					for (int i = 1; i <= stakeholder.size(); i++)
					{
						
						displayedStakeholder[i-1]=getObject("ProjectCreateNew_stakeholderDisplayField1", "ProjectCreateNew_stakeholderDisplayField2", i).getText();
						
						if (!selectedStakeholder[i-1].equals(displayedStakeholder[i-1]))
						{
							stakeholderFailCount++;							
							
						}
						
					}
					
					if (stakeholderFailCount!=0) 
					{
						
						fail = true;
						assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StakeholderIssue");
						APP_LOGS.debug("Selected Stakeholder and Displayed Stakeholder doesn't match for "+stakeholderFailCount+" stakeholders");
						comments= comments+"Selected Stakeholder and Displayed Stakeholder doesn't match for "+stakeholderFailCount+" stakeholders(Fail). ";
						
					}
					else 
					{
						APP_LOGS.debug("Selected Stakeholder and Displayed Stakeholder matches.");
						comments= comments+"Selected Stakeholder and Displayed Stakeholder matches (Pass). ";
					}
					
					/*if (assertTrue(displayedStakeholder.equals(selectedStakeholder))) 
					{
						APP_LOGS.debug("Selected Stakeholder and Displayed Stakeholder matches");
						comments= comments+"Selected Stakeholder and Displayed Stakeholder matches(Pass). ";						
					}
					else 
					{
						//assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StakeholderIssue");
						APP_LOGS.debug("Selected Stakeholder and Displayed Stakeholder doesn't match");
						comments= comments+"Selected Stakeholder and Displayed Stakeholder doesn't match(Fail). ";						
					}*/
					
					
					
					APP_LOGS.debug("Verifying Status");
					
					statusDD = new Select(getElement("ProjectCreateNew_statusDropDown_Id"));
					
					actualStatus= statusDD.getFirstSelectedOption().getText();
					
					if (compareStrings(status, actualStatus))
					{
						APP_LOGS.debug("Expected and Selected statuses match");
						comments= comments+"Expected and Selected statuses match(Pass). ";
					}
					else
					{
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "StatusIssue");
						APP_LOGS.debug("Expected and Selected statuses don't match");
						comments= comments+"Expected and Selected statuses don't match(Fail). ";
					}
					
					
					
					APP_LOGS.debug("Verifying Project URL fields");
					
					getObject("ProjectCreateNew_projectURLEditButton").click();
					
					getElement("ProjectCreateNew_projectURLPopUpTextField_Id").sendKeys(projectUrl);
					
					getElement("ProjectCreateNew_projectAliasPopUpTextField_Id").sendKeys(projectAlias);
					
					getObject("ProjectCreateNew_projectURLPopUpOkButton").click();
					
					displayedURL = getObject("ProjectCreateNew_projectURLDisplayField").getText();
					
					displayedAlias= getObject("ProjectCreateNew_projectAliasDisplayField").getText();
															
					if (!assertTrue((displayedURL.equals(projectUrl) && displayedAlias.equals(projectAlias)))) 
					{
						//assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectURLAliasIssue");
						APP_LOGS.debug("Displayed and entered project url/alias don't match");
						comments= comments+"Displayed and entered project url/alias don't match(Fail). ";
					}
					else 
					{
						APP_LOGS.debug("Displayed and entered project url/alias match");
						comments= comments+"Displayed and entered project url/alias match(Pass). ";
					}
					 
					APP_LOGS.debug("Verifying Application URL fields");   
					
					getObject("ProjectCreateNew_appURLEditButton").click();
					
					Thread.sleep(500);
					
					getElement("ProjectCreateNew_appURLPopUpTextField_Id").sendKeys(appUrl);
					
					getElement("ProjectCreateNew_appAliasPopUpTextField_Id").sendKeys(appAlias);
					
					getObject("ProjectCreateNew_appURLPopUpOkButton").click();
					
					displayedURL = getObject("ProjectCreateNew_appURLDisplayField").getText();
					
					displayedAlias= getObject("ProjectCreateNew_appAliasDisplayField").getText();
					
					if (!assertTrue((displayedURL.equals(appUrl) && displayedAlias.equals(appAlias)))) 
					{
						//assertTrue(false);
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ApplicationUrlAliasIssue");
						APP_LOGS.debug("Displayed and entered application url/alias don't match");
						comments= comments+"Displayed and entered application url/alias don't match(Fail). ";
					}
					else 
					{
						APP_LOGS.debug("Displayed and entered application url/alias match");
						comments= comments+"Displayed and entered application url/alias match(Pass). ";
					}
					
					closeBrowser();
					
				}
				catch(Throwable t)
				{
					fail = true;
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TopLevelException");
					assertTrue(false);
					t.printStackTrace();
					APP_LOGS.debug("Top level Exception occured in test case '"+this.getClass().getSimpleName()+"'.");
					closeBrowser();
					comments= comments+"Exception occured. ";
				}
				
								
			}
			else 
			{
				isLoginSuccess=false;
				
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
		public void reportTestResult() throws Exception{
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
		
			
			utilRecorder.stopRecording();
		}
		
		
		@DataProvider
		public Object[][] getTestData()
		{
			return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
		}
		
		
		
		private boolean checkCalenderCurrentDate(WebElement calendarImg) throws IOException
		 {
			
			String selectedYear;
			String selectedMonth;
			String currentDayClass;
			String highlightedDateClassContent="highlight";
			//String selectedDateClassContent="active";
			String[] currentDate = new String[3];
			
			
				try
				{
						//Select start date   
							   
						calendarImg.click(); 
						
						Thread.sleep(1000);
		   
						Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
		   
						selectedYear = year.getFirstSelectedOption().getText();
		   
						Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
		   
						selectedMonth=getMonthNumber(month.getFirstSelectedOption().getText());
						
						currentDate=dateFormat.format(calendar.getTime()).split("/");
						
						currentDayClass=eventfiringdriver.findElement(By.linkText(currentDate[1])).getAttribute("class");
						
						if (selectedYear.equals(currentDate[2]) && currentDayClass.contains(highlightedDateClassContent) && selectedMonth.equals(currentDate[0])) 
						{
							
							calendarImg.click();
							return true;								
								
						}
						else 
						{
							calendarImg.click();
							return false;
						}
						 
						
				}
			
				catch(Throwable t)
				{
					calendarImg.click();
					t.printStackTrace();
					return false;
				}
		  
		 }
		
		/*private void createProject(int numOfProjects, String group, String portfolio, String project, String version, String startMonth, 
				String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating "+numOfProjects+" Projects.");
			
			try {
				
				for (int i = 1; i <= numOfProjects; i++) {
					
					dropDownSelect(getElement("Group_DrpDwnList"),getObject("Create_Group"), group );
					
					dropDownSelect(getElement("Portfolio_DrpDwnList"),getObject("Create_Portfolio"), portfolio );
					
					dropDownSelect(getElement("Project_DrpDwnList"),getObject("Create_Project"), (project+i) );
					
					getElement("Version").sendKeys(version);
					
					selectStartOrEndDate(getObject("endDateImage"),endMonth,endYear, endDate);
					
					getObject("Version_Lead_PeoplePicker_Img").click();
					   
					Thread.sleep(500);driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					getObject("Version_Lead_Stakeholder_TextField").sendKeys(versionLead); 
					   
					getObject("Version_Lead_Stakeholder_SearchBtn").click();
			   
					getObject("Version_Lead_Stakeholder_SelectSearchResult").click();
			   
					getObject("Version_Lead_Stakeholder_OkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					getObject("SaveBtn").click();
					
					Thread.sleep(2000);
					
					getObject("projectSavePopUpBtn").click();
					
				}
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("EXception in createProject function.");
				e.printStackTrace();
			}
			
		}*/
		
		private void dropDownSelectAdd(WebElement dropDownList, WebElement addButton, String text) throws IOException
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
				 fail=true;
				 t.printStackTrace();
			}
			
		}
		
		
		
		private void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
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
					fail=true;
					
					t.printStackTrace();
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
		
		
		/*private void deleteProject(int projectsToDelete) throws IOException, InterruptedException
		{
			
			APP_LOGS.debug("Deleting "+projectsToDelete+" Projects.");
			
			getObject("viewAllNextLink").click();
			
			//getElement("viewAllPaginationId").findElement(By.xpath("div/a[text()='Next']")).click();
			
			
			int projectsToDeleteOnPage1 = projectsToDelete-1;
			
			getObject("viewAllDeleteImg1", "viewAllDeleteImg2", 1).click();
			
			//getElement("viewAllTableId").findElement(By.xpath("tr/td[7]/a[2]/img")).click();
			
			Thread.sleep(1000);
			
			getObject("viewAllPopUpDeleteButton");
			
			//eventfiringdriver.findElement(By.xpath("//span[text()='Delete']")).click();
			
			Thread.sleep(1000);
			
			getObject("viewAllDeleteConfirmOkButton");
			
			//eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
			
			Thread.sleep(1000);
			
			for (int i = projectLimitPerPage; i > (projectLimitPerPage-projectsToDeleteOnPage1); i--) 
			{
				
				getObject("viewAllDeleteImg1", "viewAllDeleteImg2", i).click();
				//getElement("viewAllTableId").findElement(By.xpath("tr["+i+"]/td[7]/a[2]/img")).click();
				
				Thread.sleep(1000);
				
				getObject("viewAllPopUpDeleteButton");
				//eventfiringdriver.findElement(By.xpath("//span[text()='Delete']")).click();
				
				Thread.sleep(1000);
				
				getObject("viewAllDeleteConfirmOkButton");
				//eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
				
				Thread.sleep(1000);
				
								
			}
			
			
			
			
			
		}*/
	

}
	


