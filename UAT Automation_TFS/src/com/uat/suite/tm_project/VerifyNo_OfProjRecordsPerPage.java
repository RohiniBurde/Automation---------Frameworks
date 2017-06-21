package com.uat.suite.tm_project;

import java.io.IOException;
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
public class VerifyNo_OfProjRecordsPerPage extends TestSuiteBase {
	
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	boolean isLoginSuccess=true;
	int count=-1;
	int projectLimitPerPage=10;
	
	Credentials user;
	Utility utilRecorder = new Utility();
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip() throws Exception
		{
			
			APP_LOGS.debug("Beginning test case '"+this.getClass().getSimpleName()+"'.");
			
			if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
			
			
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void verifyProjRecords(String group, String portfolio, String project, String version, String startMonth, 
				String startYear, String startDate, String endMonth, String endYear, String endDate, String role) throws Exception
		{
			int ProjectsPerPage;
			int totalPages;
			int availableProjects;
			int requiredProjects=11;
			boolean deleteProject=false;
						
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}			
			
			user= getUserWithAccess(role);
			
			if (user!=null) 
			{
				
				openBrowser();
				
				APP_LOGS.debug("Calling Login with role "+ role);
				
				if(login(user.username, user.password, role))
				{
					APP_LOGS.debug("Login Successfull");
					
					getElement("UAT_testManagement_Id").click();
					
					
					
					
					if (getElement("ProjectViewAll_NoProjectsLabel_Id").isDisplayed()) 
					{
						APP_LOGS.debug("No projects available");
						
						Thread.sleep(1000);						
						
						
						createProject(requiredProjects, group, portfolio, project, version, startMonth, startYear, startDate, endMonth, endYear, 
								endDate, user.username);
						
						//getObject("Projects_viewAllProjectLink").click();
						
						Thread.sleep(500);
						
						//getObject("ProjectViewAll_PreviousLink").click();						
						
						//deleteProject= true;
											
					}
					
					try 
					{
						if (getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/span")).size()==3) 
						{
							APP_LOGS.debug("Only 1 page available on View All page.");
							
							totalPages=1;
							
						}
						else 
						{
							APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
							
							totalPages = getElement("ProjectViewAll_Pagination_Id").findElements(By.xpath("div/a")).size();
						}
						
						
						
						if (totalPages>1)
						{
							APP_LOGS.debug("Calculating total projects available on page 1");
							
							ProjectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
							
							try 
							{
								
								//getElement("viewAllPaginationId").findElement(By.xpath("div/a[text()='Next']")).click();
								
								if (ProjectsPerPage!=projectLimitPerPage) 
								{
									APP_LOGS.debug("Project per page limit is not correct. Failing test case.");
									
									fail= true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NumOfProjectPerPage");
									
																	
								}
								else 
								{
									APP_LOGS.debug("Project per page limit is correct. Passing test case.");
								}
								
								
								if (deleteProject) 
								{
									deleteProject(requiredProjects);
								}
								
								closeBrowser();
								
							} 
							catch (Throwable e) 
							{
								e.printStackTrace();
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExceptionOccured");
								closeBrowser();
										
							}
							
						}
						else 
						{
							APP_LOGS.debug("Calculating required projects for paging.");
							
							availableProjects = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
							
							requiredProjects=requiredProjects-availableProjects;
							
							//getObject("Projects_createNewProjectLink").click();
							
							createProject(requiredProjects, group, portfolio, project, version, startMonth, startYear, startDate, endMonth, endYear, 
									endDate, user.username);
							
							//getObject("Projects_viewAllProjectLink").click();
							
							Thread.sleep(500);
							
							//getObject("ProjectViewAll_PreviousLink").click();
							
							//deleteProject= true;						
							
							
							APP_LOGS.debug("Calculating total projects available on page 1");
							
							ProjectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
							
							try 
							{
								
								
								
								if (ProjectsPerPage!=projectLimitPerPage) 
								{
									APP_LOGS.debug("Project per page limit is not correct. Failing test case.");
									
									fail= true;
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "NumOfProjectPerPage");
									
									
								}							
								else 
								{
									APP_LOGS.debug("Project per page limit is correct. Passing test case.");
								}
								
								if (deleteProject) 
								{
									deleteProject(requiredProjects);
								}
								
								closeBrowser();
								
							} 
							catch (Throwable e) 
							{
								e.printStackTrace();
								
								fail=true;
								assertTrue(false);
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ExceptionOccured");
								closeBrowser();
										
							}
													
						}
						
					} 
					catch (Throwable e) 
					{
						APP_LOGS.debug("Top Level Exception");
						fail = true;
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TopLevelException");
						e.printStackTrace();
						closeBrowser();
						
					}						
					
				}
				else 
				{
					isLoginSuccess=false;
					
				}
				
			}
			else 
			{
				skip=true;
				APP_LOGS.debug("The user for role "+role+" is null from getUserToLogin()....hence Skipping for test set data no. "+(count+1));
				throw new SkipException("The user for role "+role+" is null from getUserToLogin()....hence Skipping for test set data no. "+(count+1));
								
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
			}
			else
				TestUtil.reportDataSetResult(TM_projectSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			
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
		
		
		private void createProject(int numOfProjects, String group, String portfolio, String project, String version, String startMonth, 
				String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating "+numOfProjects+" Projects.");
			
			try {
				
				for (int i = 1; i <= numOfProjects; i++) {
					
					getObject("Projects_createNewProjectLink").click();
					
					dropDownSelectAdd(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), group );
					
					dropDownSelectAdd(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
					
					dropDownSelectAdd(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), (project+i) );
					
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
					
					getTextFromAutoHidePopUp();
					
					//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
					
				}
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("EXception in createProject function.");
				e.printStackTrace();
			}
			
		}
		
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
		
		
		private void deleteProject(int projectsToDelete) throws IOException, InterruptedException
		{
			
			APP_LOGS.debug("Deleting "+projectsToDelete+" Projects.");
			
			for (int i = 0; i < projectsToDelete; i++) 
			{
				getObject("ProjectViewAll_DeleteImg1", "ProjectViewAll_DeleteImg2", 1).click();
				
				Thread.sleep(1000);
				
				getObject("ProjectViewAll_PopUpDeleteButton").click();
				
				//Thread.sleep(1000);
				getTextFromAutoHidePopUp();
				//getObject("ProjectViewAll_DeleteConfirmOkButton").click();
				
				//Thread.sleep(1000);
				
			}
			
			
			/*getObject("ProjectViewAll_NextLink").click();
			
			
			
			
			int projectsToDeleteOnPage1 = projectsToDelete;
			
			getObject("ProjectViewAll_DeleteImg1", "ProjectViewAll_DeleteImg2", 1).click();
			
			
			
			Thread.sleep(1000);
			
			getObject("ProjectViewAll_PopUpDeleteButton").click();
			
			
			
			Thread.sleep(1000);
			
			getObject("ProjectViewAll_DeleteConfirmOkButton").click();
			
			
			
			Thread.sleep(1000);
			
			
			
			for (int i = projectLimitPerPage; i > (projectLimitPerPage-projectsToDeleteOnPage1); i--) 
			{
				
				getObject("ProjectViewAll_DeleteImg1", "ProjectViewAll_DeleteImg2", i).click();
				//getElement("viewAllTableId").findElement(By.xpath("tr["+i+"]/td[7]/a[2]/img")).click();
				
				Thread.sleep(1000);
				
				getObject("ProjectViewAll_PopUpDeleteButton").click();
				//eventfiringdriver.findElement(By.xpath("//span[text()='Delete']")).click();
				
				Thread.sleep(1000);
				
				getObject("ProjectViewAll_DeleteConfirmOkButton").click();
				//eventfiringdriver.findElement(By.xpath("//span[text()='Ok']")).click();
				
				Thread.sleep(1000);
				
								
			}*/
			
			
						
						
		}
	

}
