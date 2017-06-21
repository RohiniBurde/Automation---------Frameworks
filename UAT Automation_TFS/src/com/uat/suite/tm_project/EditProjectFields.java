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
public class EditProjectFields extends TestSuiteBase {
	
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=true;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
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
	public void editProjectFields(
			String role, String group,String portfolio,String project, String version, String description, 
			String startMonth, String startYear, String startDate,String endMonth, String endYear, String endDate, 
			String versionLeadCount, String stakeholderCount, String projectUrl,String projectAlias, String applicationUrl, 
			String applicationAlias, String expectedMessageText,String editGroup,String editPortfolio,String editProject, String editVersion, String editDescription, 
			String editStartMonth, String editStartYear, String editStartDate,String editEndMonth, String editEndYear, String editEndDate, 
			String editProjectUrl,String editProjectAlias, String editApplicationUrl, 
			String editApplicationAlias, String updateExpectedMessageText ) throws Exception
	{
		count++;
		comments="";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
			
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		
		
		
		int versionlead_count = Integer.parseInt(versionLeadCount);
		int stakeholder_count = Integer.parseInt(stakeholderCount);		
		
		
		versionLead = getUsers("Version Lead", versionlead_count);
		stakeholder = getUsers("Stakeholder", stakeholder_count);
		
		openBrowser();		
		APP_LOGS.debug("Browser is getting opened... ");
		
		//login("Stakeholder");
		APP_LOGS.debug("Calling Login with role "+ role);
		
		
		

		if(login(role))
		{
			
			try 
			{
				
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(500);
				
				getObject("Projects_createNewProjectLink").click();
				
				if (!createProject(group, portfolio, project, version, description, startMonth, startYear, startDate, endMonth, endYear,
						endDate, versionLead.get(0).username, stakeholder.get(0).username, projectUrl, projectAlias, applicationUrl,
						applicationAlias, expectedMessageText)) 
				{
					APP_LOGS.debug("Project Creation Unsuccessful(Fail) by "+role+". ");
					comments="Project Creation Unsuccessful(Fail) by "+role+". ";
					//assertTrue(false);
					//fail =true;
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Create Unsuccessful");
					throw new SkipException("Project Creation Unsuccessfull");
				}
				
				closeBrowser();
				
				openBrowser();
				
				if (login(stakeholder.get(0).username, stakeholder.get(0).password, "Stakeholder")) 
				{
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(500);
					
					if (!searchProjectAndEdit(project, version))
					{
						APP_LOGS.debug("Project Not found in View All Table for Stakeholder for editing");
						comments="Project Not found in View All Table for Stakeholder for editing(Fail). ";
						//assertTrue(false);
						//fail =true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project not found for stakeholder");
						throw new SkipException("Project Not found in View All Table for Stakeholder for editing");
					}
					
					
					
					if (!editProject("Stakeholder", editGroup, editPortfolio, editProject, editVersion, editDescription, editStartMonth,
							editStartYear, editStartDate, editEndMonth, editEndYear, editEndDate, versionLead.get(1).username, 
							stakeholder.get(1).username, editProjectUrl, editProjectAlias, editApplicationUrl, editApplicationAlias, updateExpectedMessageText)) 
					{
						APP_LOGS.debug("Project Edit Unsuccessful for Stakeholder");
						comments="Project Edit Unsuccessful for Stakeholder(Fail). ";
						//assertTrue(false);
						//fail =true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Project Unsuccessful for Stakeholder");
						throw new SkipException("Project Edit Unsuccessful for Stakeholder");
					}
					else 
					{
						comments="Project Edit successful for Stakeholder(Pass). ";
					}
					
					if (!searchProjectAndEdit(editProject, editVersion)) 
					{
						APP_LOGS.debug("Project Not found in View All Table for Stakeholder for verifying updates");
						comments+="Project Not found in View All Table for Stakeholder for verifying updates(Fail). ";
						//assertTrue(false);
						//fail =true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Verfication Search Project Unsuccessful for Stakeholder");
						throw new SkipException("Project Not found in View All Table for Stakeholder for verifying updates");
					}
					
					if (!verifyProjectUpdatedFields(editGroup, editPortfolio, editProject, editVersion, editDescription, editStartMonth,
							editStartYear, editStartDate, editEndMonth, editEndYear, editEndDate,versionLead.get(1).username, 2, 
							editProjectUrl, editProjectAlias, editApplicationUrl, editApplicationAlias)) 
					{
						APP_LOGS.debug("Updated Project Verification Failed for Stakeholder");
						comments+="Updated Project Verification Failed for Stakeholder(Fail). ";
						//assertTrue(false);
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Verfication failure for Stakeholder");
						//fail =true;
						
						throw new SkipException("Updated Project Verification Failed for Stakeholder");
					}
					else 
					{
						comments+="Updated Project Verification Success for Stakeholder(Pass). ";
					}
					
					
					closeBrowser();
					
					openBrowser();
					
					if (login(versionLead.get(1).username, versionLead.get(1).password, "Version Lead")) 
					{
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(800);
						
						if (!searchProjectAndEdit(editProject, editVersion)) 
						{
							APP_LOGS.debug("Project Not found in View All Table for Version Lead for editing");
							comments+="Project Not found in View All Table for Version Lead for editing(Fail). ";
							//assertTrue(false);
							//fail =true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project not found for Version Lead");
							throw new SkipException("Project Not found in View All Table for Version Lead for editing");
						}
						
						if (!editProject("Version Lead", group, portfolio, project, version, description, startMonth, startYear, 
								startDate, endMonth, endYear, endDate, versionLead.get(1).username, stakeholder.get(2).username, 
								projectUrl, projectAlias, applicationUrl, applicationAlias, updateExpectedMessageText)) 
						{
							APP_LOGS.debug("Project Edit Unsuccessful for Version LEad");
							comments+="Project Edit Unsuccessful for Version LEad(Fail). ";
							//assertTrue(false);
						//	fail =true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Edit Project Unsuccessful for Version Lead");
							throw new SkipException("Project Edit Unsuccessful for Version LEad");
						}
						else
						{
							comments+="Project Edit successful for Version LEad(Pass). ";
						}
						
						if (!searchProjectAndEdit(project, version)) 
						{
							APP_LOGS.debug("Project Not found in View All Table for Version Lead for verifyng updates");
							comments+="Project Not found in View All Table for Version Lead for verifyng updates(Fail). ";
							//assertTrue(false);
							//fail =true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Verfication Search Project Unsuccessful for Version Lead");
							throw new SkipException("Project Not found in View All Table for Version Lead for verifyng updates");
						}
						
						if (!verifyProjectUpdatedFields(group, portfolio, project, version, description, startMonth, startYear, startDate,
								endMonth, endYear, endDate,versionLead.get(1).username, 3, projectUrl, projectAlias, applicationUrl, applicationAlias)) 
						{
							APP_LOGS.debug("Updated Project Verification Failed for Version LEad");
							comments+="Updated Project Verification Failed for Version LEad(Fail). ";
							//assertTrue(false);
							//fail =true;
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Verfication failure for Version Lead");
							throw new SkipException("Updated Project Verification Failed for Version LEad");
						}
						else
						{
							comments+="Updated Project Verification Success for Version LEad(Pass). ";
						}
						
						
						
					}
					else
					{
						fail= true;
						comments+="Login Unsuccessfull for Version Lead";
						
					}
					
					
				}
				else
				{
					fail= true;
					comments+="Login Unsuccessfull for Stakeholder";
				}
				
				
				
				
			} 
			catch (Throwable e) 
			{
				e.printStackTrace();
				fail = true;
				assertTrue(false);
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or some other exception");
				comments+="Skip Exception or other exception occured" ;
			}
			
			closeBrowser();
		
		}
		else 
		{
			isLoginSuccess= false;
			
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
	public Object[][] getTestData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}
	
	
	
	private boolean createProject(String group, String portfolio, String project, String version,String description, String startMonth, 
			String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead, String stakeholders, String projectUrl, String projectAlias, String appUrl, String appAlias, String expectedSuccessMessage ) throws IOException, InterruptedException
	{	
		try
		{
			
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(version);
				
				getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(description);
				//getElement("Version").sendKeys(version);
				selectStartOrEndDate(getObject("ProjectCreateNew_startDateImage"),startMonth,startYear, startDate);
				
				selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
				
				selectVersionLead(versionLead);
				
				selectStakeholders(stakeholders);
				
				getObject("ProjectCreateNew_projectURLEditButton").click();
				
				getElement("ProjectCreateNew_projectURLPopUpTextField_Id").sendKeys(projectUrl);
				
				getElement("ProjectCreateNew_projectAliasPopUpTextField_Id").sendKeys(projectAlias);
				
				getObject("ProjectCreateNew_projectURLPopUpOkButton").click();
				
				
				getObject("ProjectCreateNew_appURLEditButton").click();
				
				Thread.sleep(500);
				
				getElement("ProjectCreateNew_appURLPopUpTextField_Id").sendKeys(appUrl);
				
				getElement("ProjectCreateNew_appAliasPopUpTextField_Id").sendKeys(appAlias);
				
				getObject("ProjectCreateNew_appURLPopUpOkButton").click();
				
				getObject("ProjectCreateNew_projectSaveBtn").click();
								
				//Thread.sleep(2000);
				
				if (getTextFromAutoHidePopUp().equals(expectedSuccessMessage)) 
				{
					//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();						
					return true;
				}
				else 
				{
					//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();					
					assertTrue(false);
					return false;
				}
				
								
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			//assertTrue(false);
			return false;
		}
			
			
		
		
	}
	
	private void dropDownSelectAdd(WebElement dropDownList, WebElement addButton, String text) throws IOException
	{
	  
		int flag = 0;
			
		
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
	
	
	
	private void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
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
	
	
	
	private void selectVersionLead(String versionLead) throws InterruptedException
	{
		
			//Clicking on people picker image icon
			APP_LOGS.debug("Clicking on Version Lead People Picker Image Icon...");
			getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
			Thread.sleep(500);
	
			//Switching to the people picker frame 
			Thread.sleep(500);Thread.sleep(500);driver.switchTo().frame(1);
			APP_LOGS.debug("Switched to the Version Lead People Picker frame...");
			
			//Wait till the find text field is visible
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
	
			//Inputting test data in people picker text field
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
			
		
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
	
	
	private void selectStakeholders(String stakeholders) throws InterruptedException
	{
		String[] stakeholder = stakeholders.split(",");
			
				//Clicking on people picker image icon
				APP_LOGS.debug("Clicking on Version Stakeholder People Picker Image Icon...");		
				getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
			
				//Switching to the people picker frame 
				Thread.sleep(500);driver.switchTo().frame(1);
				
				APP_LOGS.debug("Switched to the Version Stakeholder People Picker frame...");
				
				//Wait till the find text field is visible
				
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
				
				//Inputting test data in people picker text field
				
				for (int i = 0; i < stakeholder.length; i++) 
				{
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(stakeholder[i]);
					
					//Clicking on Search button 
					getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
					APP_LOGS.debug("Clicked on Search button from Version Stakeholder People Picker ...");
				
					//Selecting search result 
					getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
					APP_LOGS.debug("Version Stakeholder is selected from searched user...");
					
					//Clicking on add button from Version Stakeholder people picker frame
					getObject("ProjectCreateNew_StakeholderAddBtn").click();
					APP_LOGS.debug("Clicked on Add button from Version Stakeholder People Picker frame ...");
					
					getObject("ProjectCreateNew_versionLeadStakeholderTextField").clear();
					
					
				}
				 						
				
				//Clicking on OK button from People Picker
				getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
				APP_LOGS.debug("Clicked on OK button from Version Stakeholder People Picker frame ...");
			
				//Switching back to the default content 
				driver.switchTo().defaultContent();
				APP_LOGS.debug("Out of the Version Stakeholder People Picker frame...");
			
			
	}
	
	private boolean searchProjectAndEdit(String project, String version) throws IOException, InterruptedException
	{
		//int totalPages;
		int projectsOnPage;
		String gridProject;
		String gridVersion;
		//int projectFoundFlag=0;
		APP_LOGS.debug("Searching Project to Edit");
		
		try
		{
			
			do 
			{
				
				projectsOnPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
				
				for (int j = 1; j <= projectsOnPage; j++) 
				{
					gridProject=getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", j).getAttribute("title");
					gridVersion= getObject("ProjectViewAll_projectVersionColumn1", "ProjectViewAll_projectVersionColumn2", j).getText();
					
					if (gridProject.equals(project) && gridVersion.equals(version)) 
					{
						getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
						Thread.sleep(1000);
						APP_LOGS.debug("Project Found in View All page.");
						return true;
					}
					
					
				}
				
			}while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
		
			/*if (getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
			{
				APP_LOGS.debug("Only 1 page available on View All page.");
				
				totalPages=1;
				
			}
			else 
			{
				APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
				
				totalPages = getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
			}		
			
			for (int i = 0; i < totalPages; i++) 
			{
				projectsOnPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
				
				for (int j = 1; j <= projectsOnPage; j++) 
				{
					gridProject=getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", j).getAttribute("title");
					gridVersion= getObject("ProjectViewAll_projectVersionColumn1", "ProjectViewAll_projectVersionColumn2", j).getText();
					
					if (gridProject.equals(project) && gridVersion.equals(version)) 
					{
						getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
						Thread.sleep(1000);
						APP_LOGS.debug("Project Found in View All page.");
						return true;
					}
					
					
				}			
				
				if (totalPages>1 && i!=(totalPages-1)) 
				{
					getObject("ProjectViewAll_NextLink").click();
				}
				
			}*/
			
			//assertTrue(false);
			APP_LOGS.debug("Project Not found in View All page");
			
			return false;
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			//assertTrue(false);
			APP_LOGS.debug("Exception in searchProjectAndEdit(). ");
			return false;
		}
		
	}
	
	
	private boolean editProject(String roleEditingProject, String group, String portfolio, String project, String version,String description, String startMonth, 
			String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead, 
			String stakeholders, String projectUrl, String projectAlias, String appUrl, String appAlias, String expectedSuccessMessage ) throws IOException, InterruptedException
	{	
		try
		{
			
				dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
				
				dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
				
				//dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), project );
				getElement("ProjectCreateNew_projectDropDown_Id").clear();
				getElement("ProjectCreateNew_projectDropDown_Id").sendKeys(project);
				
				getObject("ProjectCreateNew_versionTextField").clear();
				
				getObject("ProjectCreateNew_versionTextField").sendKeys(version);
				
				getElement("ProjectCreateNew_descriptionTextField_Id").clear();
				getElement("ProjectCreateNew_descriptionTextField_Id").sendKeys(description);
				//getElement("Version").sendKeys(version);
				selectStartOrEndDate(getObject("ProjectCreateNew_startDateImage"),startMonth,startYear, startDate);
				
				selectStartOrEndDate(getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
				
				if (roleEditingProject.equalsIgnoreCase("Version Lead") || roleEditingProject.equalsIgnoreCase("Test Manager")) 
				{
					if (getObject("ProjectCreateNew_versionLeadPeoplePickerImg").isDisplayed()) 
					{
						comments+=roleEditingProject+" is able to see version lead edit image(Fail). ";
						return false;
					}
				}
				else 
				{
					selectVersionLead(versionLead);
				}
				
				
				selectStakeholders(stakeholders);
				
				getObject("ProjectCreateNew_projectURLEditButton").click();
				
				getElement("ProjectCreateNew_projectURLPopUpTextField_Id").clear();
				getElement("ProjectCreateNew_projectURLPopUpTextField_Id").sendKeys(projectUrl);
				
				getElement("ProjectCreateNew_projectAliasPopUpTextField_Id").clear();
				getElement("ProjectCreateNew_projectAliasPopUpTextField_Id").sendKeys(projectAlias);
				
				getObject("ProjectCreateNew_projectURLPopUpOkButton").click();
				
				
				getObject("ProjectCreateNew_appURLEditButton").click();
				
				Thread.sleep(500);
				
				getElement("ProjectCreateNew_appURLPopUpTextField_Id").clear();
				getElement("ProjectCreateNew_appURLPopUpTextField_Id").sendKeys(appUrl);
				
				getElement("ProjectCreateNew_appAliasPopUpTextField_Id").clear();
				getElement("ProjectCreateNew_appAliasPopUpTextField_Id").sendKeys(appAlias);
				
				getObject("ProjectCreateNew_appURLPopUpOkButton").click();
				
				getObject("ProjectCreateNew_projectUpdateBtn").click();
				
				//Thread.sleep(2000);
				
				if (getTextFromAutoHidePopUp().equals(expectedSuccessMessage)) 
				{
					//getObject("ProjectCreateNew_projectUpdatePopUpOkBtn").click();
					
					return true;
				}
				else 
				{
					//getObject("ProjectCreateNew_projectUpdatePopUpOkBtn").click();
					return false;
				}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			return false;
		}		
		
	}
	
	private boolean verifyProjectUpdatedFields(String group, String portfolio, String project, String version,String description, String startMonth, 
			String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead, 
			int stakeholdersCount, String projectUrl, String projectAlias, String appUrl, String appAlias) throws IOException, InterruptedException
	{	
		Select groupDropDown;
		String displayedGroup;
		Select portfolioDropDown;
		String displayedPortfolio;
		//Select projectDropDown;
		String displayedProject;
		String displayedVersion;
		String displayedDescription;
		String selectedStartDate;
		String selectedEndDate;
		String displayedVersionLead;
		int displayedStakeholdersCount;
		String displayedProjectUrl;		
		String displayedProjectAlias;
		String displayedApplicationUrl;		
		String displayedApplicationAlias;
		String expectedStartDate = getMonthNumber(startMonth)+"/"+dateHandler(startDate)+"/"+startYear;
		String expectedEndDate= getMonthNumber(endMonth)+"/"+dateHandler(endDate)+"/"+endYear;
		versionLead= versionLead.replace(".", " ");
		
		try
		{	
			
			groupDropDown = new Select(getElement("ProjectCreateNew_groupDropDown_Id"));
			displayedGroup= groupDropDown.getFirstSelectedOption().getText();
			
			portfolioDropDown= new Select(getElement("ProjectCreateNew_PortfolioDropDown_Id"));
			displayedPortfolio= portfolioDropDown.getFirstSelectedOption().getText();
			
			//projectDropDown= new Select(getElement("ProjectCreateNew_projectDropDown_Id"));
			displayedProject= getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
			
			displayedVersion=getObject("ProjectCreateNew_versionTextField").getAttribute("value");
			
			displayedDescription=getElement("ProjectCreateNew_descriptionTextField_Id").getText();
			
			selectedStartDate= getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
			
			selectedEndDate=getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");
			
			
			displayedVersionLead=getObject("ProjectCreateNew_versionLeadDisplayField").getText();			
			
			
			displayedStakeholdersCount= eventfiringdriver.findElement(By.xpath("//div[@id='ctl00_ctl00_midPannel_ContentPlaceHolder1_Child_ContentPlaceHolder1_PeopleEditorUser4Port_upLevelDiv']")).findElements(By.xpath("span")).size();
			
			getObject("ProjectCreateNew_projectURLEditButton").click();
			
			displayedProjectUrl=getElement("ProjectCreateNew_projectURLPopUpTextField_Id").getAttribute("value");
			
			displayedProjectAlias=getElement("ProjectCreateNew_projectAliasPopUpTextField_Id").getAttribute("value");
			
			getObject("ProjectCreateNew_projectURLPopUpOkButton").click();
			
			
			getObject("ProjectCreateNew_appURLEditButton").click();
			
			Thread.sleep(500);
			
			displayedApplicationUrl=getElement("ProjectCreateNew_appURLPopUpTextField_Id").getAttribute("value");
			
			displayedApplicationAlias=getElement("ProjectCreateNew_appAliasPopUpTextField_Id").getAttribute("value");
			
			getObject("ProjectCreateNew_appURLPopUpOkButton").click();
			
			if (assertTrue(displayedGroup.equals(group) && displayedPortfolio.equals(portfolio) && displayedProject.equals(project) &&
					displayedVersion.equals(version) && displayedDescription.equals(description) && selectedStartDate.equals(expectedStartDate) && 
					selectedEndDate.equals(expectedEndDate) && displayedVersionLead.equalsIgnoreCase(versionLead) && displayedStakeholdersCount==stakeholdersCount && 
					displayedProjectUrl.equals(projectUrl) && displayedProjectAlias.equals(projectAlias) && displayedApplicationUrl.equals(appUrl) &&
					displayedApplicationAlias.equals(appAlias))) 
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
	
	private boolean ifElementIsClickableThenClick(String key)
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

}
