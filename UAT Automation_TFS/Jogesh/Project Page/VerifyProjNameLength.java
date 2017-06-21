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

public class VerifyProjNameLength extends TestSuiteBase  {
	
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String comments;
	
	ArrayList<Credentials> versionLead;
	
	// Runmode of test case in a suite
		@BeforeTest
		public void checkTestSkip(){
			
			if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName())){
				APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
				throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
			}
			runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
			
			versionLead=new ArrayList<Credentials>();

		}
		
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void VerifyProjNameLength1(String Role,String GroupName,String PortfolioName,String ProjectName, String Version,String EndMonth, String EndYear, String EndDate, String VersionLead,String Expected_Messsage) throws Exception
		{
			count++;
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
						}
			
			int versionLead_count = Integer.parseInt(VersionLead);
			
			versionLead=new ArrayList<Credentials>();
			
			versionLead = getUsers("Version Lead", versionLead_count);
			
			APP_LOGS.debug(" Executing Test Case -> VerifyProjNameLength...");
			

			System.out.println(" Executing Test Case -> VerifyProjNameLength...");				
			
			APP_LOGS.debug("Opening Browser... ");
			
			System.out.println("Opening Browser... ");
			
			openBrowser();
			
			
			
			isLoginSuccess = login(Role);
			
			
			
			if(isLoginSuccess)
			{
				
			
				// clicking on Test Management Tab
			
				APP_LOGS.debug("Clicking On Test Management Tab ");
			
				System.out.println("Clicking On Test Management Tab ");
			
				getElement("UAT_testManagement_Id").click();
			
				Thread.sleep(300);
				
			
			
				//Clicking On Create New Link to Create Project
				APP_LOGS.debug("Clicking on Create New link ...");
			
				System.out.println("Clicking on Create New link ...");
			
				getObject("Projects_createNewProjectLink").click();
			
			
			
				//Group selection or Creation
			
				try
				{
				
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );

				
				}
				catch(Throwable e)
				{
					   fail=true;
					   e.printStackTrace();
					   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				}
			
			
			
			
				//Portfolio selection or Creation
			
				try
				{
				
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
				
				
				
				}
				catch(Throwable e)
				{
					   fail=true;
					   e.printStackTrace();
					   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
			
				}
			
			
			
			
			
				//Project Selection or Creation
			
				try
				{
				
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
				
				
				}
				catch(Throwable e)
				{
					   fail=true;
					   e.printStackTrace();
					   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				}
			
			
			
			
				//Project version Name
				try
				{
				
					getObject("ProjectCreateNew_versionTextField").sendKeys(Version);
				
					APP_LOGS.debug("Version : "+ Version +" is entered in Version Text Field...");
				
					System.out.println("Version : "+ Version +" is entered in Version Text Field...");
				}
				catch(Throwable e)
				{
					fail=true;
					   
					e.printStackTrace();
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				}
			
			
				try
				{
					//To select start date from Start Date Picker 
					
					toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);

				}
				catch(Throwable e)
				{
					   fail=true;
					   e.printStackTrace();
					   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				}
				
				//Version Lead selection
				try
				{
					toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(0).username);

				}
				catch(Throwable t){
					   fail=true;
					   t.printStackTrace();
					   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				}
			
				
				
				
		//		Clicking On Save Button to Save whole Project
				
				
				try 
				{
					APP_LOGS.debug("Clicking on Save Button to Save Project");
					
					System.out.println("Clicking on Save Button to Save Project");
					
					getObject("ProjectCreateNew_projectSaveBtn").click();
					
					APP_LOGS.debug(ProjectName+": Project is Saved Successfully..");
					System.out.println(ProjectName+": Project is Saved Successfully..");
					
				} 
				catch (Throwable t) 
				{
					fail=true;
					t.printStackTrace();
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
					
				}
				
				//Verifying PopUp Success Message for Saving Project Successfully	
				
	
				
				//Clicking OK button from message box and verifying the text also clicking on OK button
				  try{
	
				   String actual_SaveProjectandVersion_SuccessMessage = getElement("ProjectCreateNew_projectSuccessMessageText_Id").getText();
				   
				  
				   
				   compareStrings(actual_SaveProjectandVersion_SuccessMessage, Expected_Messsage);
				   
				   APP_LOGS.debug(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
				   
				   System.out.println(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
				   
				   getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();  
				   
				  
				  }
				  catch(Throwable e)
				  {
					  fail = true;
					  e.printStackTrace();
					  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
				  }
			
				  closeBrowser();
			
			}
		
		else
		{	
			 isLoginSuccess=false;
			 APP_LOGS.debug("Login Not Successful");
			 System.out.println("Login Not Successful");
		
			 
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
		public void reportTestResult(){
			if(isTestPass)
				TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "PASS");
			else
				TestUtil.reportDataSetResult(TM_projectSuiteXls, "Test Cases", TestUtil.getRowNum(TM_projectSuiteXls,this.getClass().getSimpleName()), "FAIL");
		
		}
		
		
		
		@DataProvider
		public Object[][] getTestData()
		{
			return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
		}
		
		
		
		
		//Verifying if the Portfolio name is present in Portfolio/Process Name drop down , if it is present then select it  if not present then create it
		
				public void SelectorCreationGroupAndPortfolio(WebElement DropDownList, WebElement AddButton, String InputtedTestData) throws IOException
				{
				  
						

					
					try
					{
						List<WebElement> elements = DropDownList.findElements(By.tagName("option"));
				  
						//System.out.println("The elements present inside  Dropdown List is : " +elements.size());
				  
						int flag = 0;
				  
						for(int i =0 ;i<elements.size();i++)
						{
								//System.out.println("Each element's present inside Dropdown is having text as : " + elements.get(i).getText());
								
					  
								if(elements.get(i).getText().equals(InputtedTestData))
								{
										flag++;
						  	
										elements.get(i).click();
										
										APP_LOGS.debug( InputtedTestData + " : is selected...");
										
										System.out.println(InputtedTestData + " : is selected...");
						  	
										break;
								}
						}
				  
				 
						if(flag==0)
					  
						{
								//Click on Plus icon to add Group or Portfolio
								System.out.println("Clicking on Add icon ");
							
								APP_LOGS.debug("Clicking on Add icon ");
								
								
								Thread.sleep(4000);
								
								AddButton.click();
					  
								
								
								System.out.println("Inputting Text :" +InputtedTestData +" in Text Field ");
								
								APP_LOGS.debug("Inputting Text :" +InputtedTestData +" in Text Field ");
					  
								getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(InputtedTestData);
								
								//Save the entered group or portfolio 
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
					
					catch(Throwable t)
					{
						 fail=true;
						 t.printStackTrace();
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
					}
					
				}
				
				
		
				public void toSelectStartDateandEndDate(WebElement startEndDateImage, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
				 {
					
						try
						{
								//Select start date   
								WebElement startDateImage = startEndDateImage;  
				   
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
							fail=true;
							
							t.printStackTrace();
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
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
					catch(Throwable t)
					{
						fail=true;
						t.printStackTrace();
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
					}
				}



	
		

}