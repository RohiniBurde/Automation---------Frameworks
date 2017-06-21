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

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyNo_OfProjRecordsPerPage extends TestSuiteBase {
	
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static boolean isLoginSuccess=true;
	static int count=-1;
	static int projectLimitPerPage=10;
	
	ArrayList<Credentials> versionLead;
	
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_Project_Suite_xls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_Project_Suite_xls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();
			
			versionLead=getUsers("Version Lead", 1);

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
			
			openBrowser();
			
			APP_LOGS.debug("Calling Login with role "+ role);
			
			if(login(role))
			{
				APP_LOGS.debug("Login Successfull");
				
				getElement("Test_Management_ID").click();
				
				//wait.until(ExpectedConditions.presenceOfElementLocated())
				
				
				if (getElement("viewAllNoProjectsLabelId").isDisplayed()) 
				{
					APP_LOGS.debug("No projects available");
					
					Thread.sleep(1000);
					
					getObject("Create_New_Project").click();
					
					createProject(requiredProjects, group, portfolio, project, version, startMonth, startYear, startDate, endMonth, endYear, 
							endDate, versionLead.get(0).username);
					
					getObject("viewAllLink").click();
					
					getObject("viewAllPreviousLink").click();
					
					//getElement("viewAllPaginationId").findElement(By.xpath("div/a[text()='Prev']")).click();
					deleteProject= true;
										
				}
				
				try 
				{
					if (getElement("viewAllPaginationId").findElements(By.xpath("div/span")).size()==3) 
					{
						APP_LOGS.debug("Only 1 page available on View All page.");
						
						totalPages=1;
						
					}
					else 
					{
						APP_LOGS.debug("More than 1 page avaialble on View All page. Calculating total pages.");
						
						totalPages = getElement("viewAllPaginationId").findElements(By.xpath("div/a")).size();
					}
					
					
					
					if (totalPages>1)
					{
						APP_LOGS.debug("Calculating total projects available on page 1");
						
						ProjectsPerPage = getElement("viewAllTableId").findElements(By.xpath("tr")).size();
						
						try 
						{
							
							//getElement("viewAllPaginationId").findElement(By.xpath("div/a[text()='Next']")).click();
							
							if (ProjectsPerPage!=projectLimitPerPage) 
							{
								APP_LOGS.debug("Project per page limit is not correct. Failing test case.");
								
								fail= true;
								
							}
							else if(deleteProject)
							{
								APP_LOGS.debug("Project per page limit is correct. Passing test case.");
								deleteProject(requiredProjects);
							}
							
						} 
						catch (Throwable e) 
						{
							e.printStackTrace();
							fail=true;
									
						}
						
					}
					else 
					{
						APP_LOGS.debug("Calculating required projects for paging.");
						
						availableProjects = getElement("viewAllTableId").findElements(By.xpath("tr")).size();
						
						requiredProjects=requiredProjects-availableProjects;
						
						getObject("Create_New_Project").click();
						
						createProject(requiredProjects, group, portfolio, project, version, startMonth, startYear, startDate, endMonth, endYear, 
								endDate, versionLead.get(0).username);
						
						getObject("viewAllLink").click();
						
						getObject("viewAllPreviousLink").click();
						
						deleteProject= true;						
						
						
						APP_LOGS.debug("Calculating total projects available on page 1");
						
						ProjectsPerPage = getElement("viewAllTableId").findElements(By.xpath("tr")).size();
						
						try 
						{
							
							//getElement("viewAllPaginationId").findElement(By.xpath("div/a[text()='Next']")).click();
							
							if (ProjectsPerPage!=projectLimitPerPage) 
							{
								APP_LOGS.debug("Project per page limit is not correct. Failing test case.");
								
								fail= true;
								
							}
							else if(deleteProject)
							{
								APP_LOGS.debug("Project per page limit is correct. Passing test case.");
								
								deleteProject(requiredProjects);
							}
							
						} 
						catch (Throwable e) 
						{
							e.printStackTrace();
							
							fail=true;
									
						}
												
					}
					
				} 
				catch (Throwable e) 
				{
					e.printStackTrace();
					// TODO: handle exception
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
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
			else if(!isLoginSuccess){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			}
			else if(fail){
				isTestPass=false;
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			}
			else
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			
			skip=false;
			fail=false;
			

		}
		
		
		@AfterTest
		public void reportTestResult(){
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, "Test Cases", TestUtil.getRowNum(TM_Project_Suite_xls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_Project_Suite_xls, "Test Cases", TestUtil.getRowNum(TM_Project_Suite_xls,this.getClass().getSimpleName()), "FAIL");
		
		}
		
		
		@DataProvider
		public Object[][] getTestData()
		{
			return TestUtil.getData(TM_Project_Suite_xls, this.getClass().getSimpleName()) ;
		}
		
		
		private void createProject(int numOfProjects, String group, String portfolio, String project, String version, String startMonth, 
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
					   
					driver.switchTo().frame(1);
					
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
			
		}
		
		private void dropDownSelect(WebElement dropDownList, WebElement addButton, String text) throws IOException
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
			  
						getElement("Group_Portfolio_TextField").sendKeys(text);
						
						
			  
						//Save the entered group or portfolio 
						if (getElement("popUpLabel").getText().contains("Project")) {
							getObject("Project_AddBtn").click();
						}
						else {
							getObject("Group_Portfolio_SaveBtn").click(); 
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
		   
						System.out.println("Clicking on Date Calendar icon...");
		   
						startDateImage.click();  
		   
		  
		   
						Select year = new Select(getElementByClassAttr("year_DrpDwnList"));
		   
						year.selectByValue(StartEndYear);
		   
						APP_LOGS.debug(StartEndYear +" : Year is selected...");
		   
						System.out.println(StartEndYear +" : Year is selected...");
		   
						Select month = new Select(getElementByClassAttr("month_DrpDwnList"));
		   
						month.selectByVisibleText(StartEndMonth);
		   
						APP_LOGS.debug(StartEndMonth +" : Month is selected...");
		   
						System.out.println(StartEndMonth +" : Month is selected...");
		    
						WebElement datepicker= getObject("datePicker");
		   
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
		
		
		private void deleteProject(int projectsToDelete) throws IOException, InterruptedException
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
			
			
			
			
			
		}
	

}
