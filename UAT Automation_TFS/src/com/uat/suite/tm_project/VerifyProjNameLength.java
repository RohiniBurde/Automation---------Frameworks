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

public class VerifyProjNameLength extends TestSuiteBase  {
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	Utility utilRecorder = new Utility();
	ArrayList<Credentials> versionLead;
	
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
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void verifyProjNameLength(String Role,String GroupName,String PortfolioName,String ProjectName, String Version,String EndMonth, String EndYear, String EndDate, String VersionLead,String Expected_Messsage) throws Exception
		{
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			int versionLead_count = Integer.parseInt(VersionLead);
			
			versionLead=new ArrayList<Credentials>();
			
			versionLead = getUsers("Version Lead", versionLead_count);
			
			APP_LOGS.debug(" Executing Test Case -> VerifyProjNameLength...");
			

			
			APP_LOGS.debug("Opening Browser... ");
			
			
			openBrowser();			
			
			isLoginSuccess = login(Role);
			
			
			
			if(isLoginSuccess)
			{
				try
				{
			
					// clicking on Test Management Tab			
					APP_LOGS.debug("Clicking On Test Management Tab ");
				
				
					getElement("UAT_testManagement_Id").click();
				
					Thread.sleep(1000);					
				
					//Clicking On Create New Link to Create Project
					APP_LOGS.debug("Clicking on Create New link ...");
				
				
					getObject("Projects_createNewProjectLink").click();				
			
				
					//Group selection or Creation
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
			
					//Portfolio selection or Creation
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
							
					//Project Selection or Creation				
					SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
										
					//Project version Name				
					getObject("ProjectCreateNew_versionTextField").sendKeys(Version);
				
					APP_LOGS.debug("Version : "+ Version +" is entered in Version Text Field...");
				
				
					//To select start date from Start Date Picker 					
					toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);				
				
					//Version Lead selection				
					toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(0).username);
				
					//Clicking On Save Button to Save whole Project				
					APP_LOGS.debug("Clicking on Save Button to Save Project");
					
					
					getObject("ProjectCreateNew_projectSaveBtn").click();
					
					APP_LOGS.debug(ProjectName+": Project is Saved Successfully..");				
					
					String actual_SaveProjectandVersion_SuccessMessage;
					
					if (ProjectName.length()<56)						
						actual_SaveProjectandVersion_SuccessMessage = getTextFromAutoHidePopUp();
					else
						actual_SaveProjectandVersion_SuccessMessage = getElement("ProjectCreateNew_alertDiv_Id").getText();
				
					//Verifying PopUp Success Message for Saving Project Successfully				
					//Clicking OK button from message box and verifying the text also clicking on OK button	
					
				   
					if (compareStrings(Expected_Messsage, actual_SaveProjectandVersion_SuccessMessage)) 
					{
						APP_LOGS.debug(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
						   
					   
						//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
						
						closeBrowser();
						
						openBrowser();
						
						if (login("Admin"))
						{
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(500);
							
							if (ProjectName.length()<56) 
							{
								if (assertTrue(searchProjectInViewAllPage(ProjectName, Version))) 
								{
									comments+="Project Creation Successful and project found in View All table (Pass). ";
								}
								else
								{
									fail= true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project not found in View All Table");
									comments+="Project Creation Successful but project not found in View All table (Fail). ";
								}
							}
							else
							{
								if (assertTrue(!searchProjectInViewAllPage(ProjectName, Version))) 
								{
									comments+="Project Creation UnSuccessful and project not found in View All table (Pass). ";
								}
								else
								{
									fail= true;
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project found in View All Table");
									comments+="Project Creation UnSuccessful but project found in View All table (Fail). ";
								}
							}
						}
						else
						{
							fail= true;
							comments+="Login Failed for Admin(Fail). ";
						}
						
						
					}
					else 
					{
						comments += "Expected and actual messages didn't match";
						 fail = true;
						 APP_LOGS.debug("Project Name Length Test Case Failed");						
						 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Fail");

					}
				   
					 
				   
				  
				 }
				 catch(Throwable e)
				 {
					 comments+="Exception Occured. ";
					  fail = true;
					  assertTrue(false);
					  e.printStackTrace();
					  TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthException");
				 }
			
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
		
		
		
		
		//Verifying if the Portfolio name is present in Portfolio/Process Name drop down , if it is present then select it  if not present then create it
		
				public void SelectorCreationGroupAndPortfolio(WebElement DropDownList, WebElement AddButton, String InputtedTestData) throws IOException
				{
				  
						

					
					try
					{
						List<WebElement> elements = DropDownList.findElements(By.tagName("option"));
				  
				  
						int flag = 0;
				  
						for(int i =0 ;i<elements.size();i++)
						{
								
					  
								if(elements.get(i).getText().equals(InputtedTestData))
								{
										flag++;
						  	
										elements.get(i).click();
										
										APP_LOGS.debug( InputtedTestData + " : is selected...");
										
						  	
										break;
								}
						}
				  
				 
						if(flag==0)
					  
						{
								//Click on Plus icon to add Group or Portfolio
							
								APP_LOGS.debug("Clicking on Add icon ");
								
								
								Thread.sleep(4000);
								
								AddButton.click();
					  
								
								
								
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
					catch(Throwable t)
					{
						fail=true;
						t.printStackTrace();
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyProjectNameLengthError");
					}
				}



	
		

}