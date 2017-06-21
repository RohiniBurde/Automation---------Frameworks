
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

public class VerifyValidUserDeleteProjects extends TestSuiteBase  {
	
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments="";
	
	ArrayList<Credentials> versionLead;
	Credentials user;
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
			utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

		}
		
		// Test Case Implementation ...
		
		@Test(dataProvider="getTestData")
		public void verifyValidUserDeleteProjects(String Role,String GroupName,String PortfolioName,String ProjectName,
				String Version,String EndMonth, String EndYear, String EndDate, String VersionLead,
				String expectedProjectSavedMessage,String DeleteProjectConfirmationMessage,String projectDeletedConfirmationMessage  ) throws Exception
		{
			count++;
			comments="";
			
			if(!runmodes[count].equalsIgnoreCase("Y")){
				skip=true;
				APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));

				throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
			}
			
			user=getUserWithAccess(Role);
			
			int versionLead_count = Integer.parseInt(VersionLead);
			versionLead=new ArrayList<Credentials>();
			versionLead = getUsers("Version Lead", versionLead_count);
			
			
			
			APP_LOGS.debug(" Executing Test Case -> VerifyValidUserDeleteProjects...");
					
			APP_LOGS.debug("Opening Browser... ");
			
			openBrowser();
			
			isLoginSuccess = login(user.username, user.password, Role);
			
			if(isLoginSuccess)
			{
			
				// clicking on Test Management Tab
				try
				{
				
						APP_LOGS.debug("Clicking On Test Management Tab ");						
						
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(500);
						
						//Clicking On Create New Link to Create Project
						
						APP_LOGS.debug("Clicking on Create New link ...");
						
						getObject("Projects_createNewProjectLink").click();
						Thread.sleep(500);		
						//Group selection or Creation
						
											
						SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_groupDropDown_Id"),getObject("ProjectCreateNew_groupAddButton"), GroupName );
			
											
						//Portfolio selection or Creation
						
							
						SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_PortfolioDropDown_Id"),getObject("ProjectCreateNew_portfolioAddButton"), PortfolioName);
							
												
						//Project Selection or Creation
						
							
						SelectorCreationGroupAndPortfolio(getElement("ProjectCreateNew_projectDropDown_Id"),getObject("ProjectCreateNew_projectAddButton"), ProjectName );
							
													
						//Project version Name
						
					
						APP_LOGS.debug("Entering Version Name...");
							
						getObject("ProjectCreateNew_versionTextField").sendKeys(Version);
							
						APP_LOGS.debug("Version : "+ Version +" is entered in Version Text Field...");
							
							
						
						
						
							//To select start date from Start Date Picker 
							
						toSelectStartDateandEndDate(getObject("ProjectCreateNew_endDateImage"),EndMonth,EndYear, EndDate);
			
						
						
						
						
						
						//Version Lead selection
						if (Role.equals("Version Lead")) 
						{
							toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),user.username);
						}
						else if (Role.equals("Stakeholder"))
						{
							toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(0).username);
							selectStakeholders(user.username);
						}
						else
						{
							toSelectVersionLeadfromPeoplePicker(getObject("ProjectCreateNew_versionLeadPeoplePickerImg"),versionLead.get(0).username);
						}
						
		
						
						
						
						
					
						
				//		Clicking On Save Button to Save whole Project
						
						
						
						APP_LOGS.debug("Clicking Save Button to Save Project");
							
						getObject("ProjectCreateNew_projectSaveBtn").click();
							
						APP_LOGS.debug(ProjectName+": Project is Saved Successfully..");
							
							
						
						
						//Verifying PopUp Success Message for Saving Project Successfully	
						
						
						
						String actual_SaveProjectandVersion_SuccessMessage = getTextFromAutoHidePopUp();
						   
						//compareStrings(actual_SaveProjectandVersion_SuccessMessage, expectedProjectSavedMessage);
						  
						APP_LOGS.debug(ProjectName+": Project is Saved Successfully stating : " + actual_SaveProjectandVersion_SuccessMessage);
						  	   
						//getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click(); 
		
						 
						
						  
						  
						//Clicking On View All Link
						  
						
						//APP_LOGS.debug("Clicking on View All Button");
								
						//getObject("Projects_viewAllProjectLink").click();
								
								
						
						  
						
						  	
							
						//finding Project Name and Version Name of Created Project 
						  
						String createdProjectName = getObject("ProjectViewAll_selectedRowProjectName").getText();
							  
						String createdVersionName = getObject("ProjectViewAll_selectedRowVersionName").getText();
							  
							  
							  
							  	 
							  
					  	if(createdProjectName.equals(ProjectName) && createdVersionName.equals(Version))
					  	{	
					  
					  			APP_LOGS.debug("Clicking On Delete Button to delete Project");
					  
					  			getObject("ProjectViewAll_selectedRowDeleteProjectButton").click();
					  	}
							  
								  
							  
							  
							  
							  
								  
							  
						 // getting Delete Button PoUp Text  to compare with Expected Text
							  
						  String DeleteButton_PopUp_Message =getElement("ProjectViewAll_deleteProjectConfirmationText_Id").getText();
								  
						 APP_LOGS.debug("Verifying Project Delete Button PopUp Message ");
						 
						if (compareStrings(DeleteProjectConfirmationMessage,DeleteButton_PopUp_Message)) 
						{
							comments+="User received delete confirmation message(Pass). ";
						}
						else
						{
							fail = true;
							comments+="User didn't receive delete confirmation message(Fail). ";
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Delete Confirmation Message Issue");
						}
						 
								  
						APP_LOGS.debug("Project Delete Button PopUp Message showing properly...stating : " +  DeleteButton_PopUp_Message);
								  
						Thread.sleep(3000);
			
						APP_LOGS.debug("Clicking on Cancel Button");
								 
						getObject("ProjectViewAll_delectProjectPopUpCancelButton").click();
						
						createdProjectName = getObject("ProjectViewAll_selectedRowProjectName").getText();
						  
						createdVersionName = getObject("ProjectViewAll_selectedRowVersionName").getText();
								 
					  
					  	if (createdProjectName.isEmpty() && createdVersionName.isEmpty() )
					  	{
					  		assertTrue(false);
					  		fail=true;
					  		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "After Cancel Click Project not available");
					  		comments+="After Cancel Click Project not available(Fail). ";
					  		throw new SkipException("Project not selected/visible");
					  	}
					  	else
					  	{	
					  		
					  		APP_LOGS.debug(createdProjectName + " : Project is not deleted from the Application and user is able to see the same Project in the 'View All' grid.");
					  		comments+="After Cancel Click Project available(Pass). ";
					  
					  	}					
								 
								 	 	
								  
					  	if(createdProjectName.equals(ProjectName) && createdVersionName.equals(Version))
					  	{	
					  
					  			APP_LOGS.debug("Clicking On Delete Button to delete Project");
					  			
					  			Thread.sleep(2000);
					  			
					  			getObject("ProjectViewAll_selectedRowDeleteProjectButton").click();
					  			 
		
					  		    APP_LOGS.debug("Clicking on Delete  Button");
									 
								Thread.sleep(3000);
								 
							    getObject("ProjectViewAll_PopUpDeleteButton").click();
							    
							    String actualDeleteConfirmationMessage = getTextFromAutoHidePopUp();
							    
							    APP_LOGS.debug("Verifying Project Deleted Success PopUp Message ");
							    
							    if (compareStrings(projectDeletedConfirmationMessage,actualDeleteConfirmationMessage  )) 
							    {
							    	APP_LOGS.debug("Project is Deleted Successfully with Success Message Stating :  "+ actualDeleteConfirmationMessage);
							    	comments+="Delete Successful message correct and project deleted successfuly(PAss). ";
							    	
							    	//Thread.sleep(1000);
									
									//getObject("ProjectViewAll_DeleteConfirmOkButton").click();
									
									//Thread.sleep(1000);
									
							    
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
							    else
							    {
							    	
							  		fail=true;
							  		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Delete Successfull Message issue");
							  		comments+="Delete successful message not correct(Fail). ";
							   }
							    
						}
					  	else
					  	{
					  		assertTrue(false);
					  		fail=true;
					  		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project not selected");
					  		comments+="Project not selected(Fail). ";
					  		
					  	}
					  	
					  	
			  	
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					assertTrue(false);
			  		TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Skip or other Exception");
			  		comments+="Skip Exception or other exception Occured. ";
					
				}						  
				  
				closeBrowser();  
						  
		}		
							
		
	}
					  
				
			  

				  
		

			@AfterMethod
			public void reportDataSetResult()
			{
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
				
				
				Thread.sleep(2000);
				
				AddButton.click();
	  
				APP_LOGS.debug("Inputting Text :" +InputtedTestData +" in Text Field ");
	  
				getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(InputtedTestData);
	  
				//Save the entered group or portfolio 
				
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
		 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyValidUserDeleteProjectsError");
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
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyValidUserDeleteProjectsError");
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
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyValidUserDeleteProjectsError");
			}
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



}
	