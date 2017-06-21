package com.uat.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.uat.base.TestBase;


public class TestUtil extends TestBase{

	// finds if the test suite is runnable 
		public static boolean isSuiteRunnable(Xls_Reader xls , String suiteName){
			boolean isExecutable=false;
			for(int i=2; i <= xls.getRowCount("Test Suite") ;i++ ){
				//String suite = xls.getCellData("Test Suite", "TSID", i);
				//String runmode = xls.getCellData("Test Suite", "Runmode", i);
			
				if(xls.getCellData("Test Suite", "TSID", i).equalsIgnoreCase(suiteName)){
					if(xls.getCellData("Test Suite", "Runmode", i).equalsIgnoreCase("Y")){
						isExecutable=true;
					}else{
						isExecutable=false;
					}
				}

			}
			xls=null; // release memory
			return isExecutable;
			
		}
		
		
		// returns true if runmode of the test is equal to Y
		public static boolean isTestCaseRunnable(Xls_Reader xls, String testCaseName){
			boolean isExecutable=false;
			for(int i=2; i<= xls.getRowCount("Test Cases") ; i++){
				//String tcid=xls.getCellData("Test Cases", "TCID", i);
				//String runmode=xls.getCellData("Test Cases", "Runmode", i);
				//System.out.println(tcid +" -- "+ runmode);
				
				
				if(xls.getCellData("Test Cases", "TCID", i).equalsIgnoreCase(testCaseName)){
					if(xls.getCellData("Test Cases", "Runmode", i).equalsIgnoreCase("Y")){
						isExecutable= true;
					}else{
						isExecutable= false;
					}
				}
			}
			
			return isExecutable;
			
		}
		
		
		// return the test data from a test in a 2 dim array
		public static Object[][] getData(Xls_Reader xls , String testCaseName){
			// if the sheet is not present
			if(! xls.isSheetExist(testCaseName)){
				xls=null;
				return new Object[1][0];
			}
			
			
			int rows=xls.getRowCount(testCaseName);
			int cols=xls.getColumnCount(testCaseName);
			//System.out.println("Rows are -- "+ rows);
			//System.out.println("Cols are -- "+ cols);
			
		    Object[][] data =new Object[rows-1][cols-3];
			for(int rowNum=2;rowNum<=rows;rowNum++){
				for(int colNum=0;colNum<cols-3;colNum++){
					//System.out.print(xls.getCellData(testCaseName, colNum, rowNum) + " -- ");
					data[rowNum-2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
				}
				//System.out.println();
			}
			return data;
			
		}
		
		
		
		// checks RUnmode for dataSet
		public static String[] getDataSetRunmodes(Xls_Reader xlsFile,String sheetName){
			String[] runmodes=null;
			if(!xlsFile.isSheetExist(sheetName)){
				xlsFile=null;
				sheetName=null;
				runmodes = new String[1];
				runmodes[0]="Y";
				xlsFile=null;
				sheetName=null;
				return runmodes;
			}
			runmodes = new String[xlsFile.getRowCount(sheetName)-1];
			for(int i=2;i<=runmodes.length+1;i++){
				runmodes[i-2]=xlsFile.getCellData(sheetName, "Runmode", i);
			}
			xlsFile=null;
			sheetName=null;
			return runmodes;
			
		}

		// update results for a particular data set	
		public static void reportDataSetResult(Xls_Reader xls, String testCaseName, int rowNum,String result){	
			xls.setCellData(testCaseName, "Result", rowNum, result);
		}
		
		public static void printComments(Xls_Reader xls, String testCaseName, int rowNum,String comments) {
			   xls.setCellData(testCaseName, "Comments", rowNum, comments);
			  }
		
		// return the row num for a test
		public static int getRowNum(Xls_Reader xls, String id){
			for(int i=2; i<= xls.getRowCount("Test Cases") ; i++){
				String tcid=xls.getCellData("Test Cases", "TCID", i);
				
				if(tcid.equals(id)){
					xls=null;
					return i;
				}
				
				
			}
			
			return -1;
		}

		public static void takeScreenShot(String testCaseName, String fileName) 
		{
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			try 
		    {
				FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\screenshots\\"+screenshotDateTime+"\\"+testCaseName+"\\"+fileName+".jpg"));
		    } 
			catch (Exception e) 
			{		   
				e.printStackTrace();
			}
		}
		
		public static void createProject(String group, String portfolio, String project, String version, String startMonth, 
				String startYear, String startDate, String endMonth, String endYear, String endDate, String versionLead ) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Projects.");
			
			//wait.until(ExpectedConditions.presenceOfElementLocated())

			tb.getObject("Projects_createNewProjectLink").click();
			try {
				
					dropDownSelectAdd(tb.getElement("ProjectCreateNew_groupDropDown_Id"),tb.getObject("ProjectCreateNew_groupAddButton"), group );
					
					dropDownSelectAdd(tb.getElement("ProjectCreateNew_PortfolioDropDown_Id"),tb.getObject("ProjectCreateNew_portfolioAddButton"), portfolio );
					
					dropDownSelectAdd(tb.getElement("ProjectCreateNew_projectDropDown_Id"),tb.getObject("ProjectCreateNew_projectAddButton"), project );
					
					tb.getObject("ProjectCreateNew_versionTextField").sendKeys(version);
					//getElement("Version").sendKeys(version);
					
					selectStartOrEndDate(tb.getObject("ProjectCreateNew_endDateImage"),endMonth,endYear, endDate);
					
					tb.getObject("ProjectCreateNew_versionLeadPeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					tb.getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(versionLead); 
					   
					tb.getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
			   
					tb.getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
			   
					tb.getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					tb.getObject("ProjectCreateNew_projectSaveBtn").click();
					
					Thread.sleep(2000);
					
					tb.getObject("ProjectCreateNew_projectSuccessPopUpOkBtn").click();
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("EXception in createProject function.");
				e.printStackTrace();
			}
			
		}
		// Create Test Pass for that project:
		
		
		public static void createTestPass(String group, String portfolio, String project, String version, String testPassName, 
				String endMonth, String endYear, String endDate, String testManager ) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Test Pass");
			Thread.sleep(2000);
			tb.getElement("TestPassNavigation_Id").click();
			Thread.sleep(2000);
			tb.getObject("TestPasses_createNewProjectLink").click();
			
			
			try {
				
					dropDownSelect(tb.getElement("TestPassUpperRibbon_groupDropDown_Id"), tb.getElement("TestPassUpperRibbon_GroupList_Id"), group );
					
					dropDownSelect(tb.getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), tb.getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
					
					dropDownSelect(tb.getElement("TestPassUpperRibbon_projectDropDown_Id"), tb.getElement("TestPassUpperRibbon_ProjectList_Id"), project );
					
					dropDownSelect(tb.getElement("TestPassUpperRibbon_versionDropDown_Id"), tb.getElement("TestPassUpperRibbon_VersionList_Id"), version );
					//getElement("Version").sendKeys(version);
					tb.getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(testPassName);
					
					tb.getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					tb.getObject("TestPassCreateNew_testManagerPeoplePickerTextField").sendKeys(testManager); 
					   
					tb.getObject("TestPassCreateNew_testManagerPeoplePickerSearchBtn").click();
			   
					tb.getObject("TestPassCreateNew_testManagerPeoplePickerSelectSearchResult").click();
			   
					tb.getObject("TestPassCreateNew_testManagerPeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					
					selectStartOrEndDate(tb.getObject("TestPassCreateNew__endDateImage"),endMonth,endYear, endDate);
					
					tb.getObject("TestPassCreateNew_testPassSaveBtn").click();
					
					Thread.sleep(2000);
					
					tb.getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("EXception in createProject function.");
				e.printStackTrace();
			}
			
		}
		
		
		
		public static void createTester(String group, String portfolio, String project, String version, String testPassName, 
				String tester, String testerRoleCreation, String testerRoleSelection) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Tester");
			Thread.sleep(2000);
			tb.getElement("TesterNavigation_Id").click();
			
			if(tb.getElement("TesterCreateNew_TesterActiveX_Id").isDisplayed())
			{
				tb.getObject("TesterCreateNew_TesterActiveX_Close").click();
			}
			Thread.sleep(2000);
			
			try {
				
					dropDownSelect(tb.getElement("TesterUpperRibbon_groupDropDown_Id"), tb.getElement("TesterUpperRibbon_GroupList_Id"), group );
					
					dropDownSelect(tb.getElement("TesterUpperRibbon_PortfolioDropDown_Id"), tb.getElement("TesterUpperRibbon_PortfolioList_Id"), portfolio );
					
					dropDownSelect(tb.getElement("TesterUpperRibbon_projectDropDown_Id"), tb.getElement("TesterUpperRibbon_ProjectList_Id"), project );
					
					dropDownSelect(tb.getElement("TesterUpperRibbon_versionDropDown_Id"), tb.getElement("TesterUpperRibbon_VersionList_Id"), version );
					
					dropDownSelect(tb.getElement("TesterUpperRibbon_testPassDropDown_Id"), tb.getElement("TesterUpperRibbon_TestPassList_Id"), testPassName );
					tb.getElement("Tester_createNewProjectLink_Id").click();
					Thread.sleep(1000);
					tb.getObject("TesterCreateNew_PeoplePickerImg").click();
					   
					driver.switchTo().frame(1);
					
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
					
					tb.getObject("TesterCreateNew_PeoplePickerTextField").sendKeys(tester); 
					   
					tb.getObject("TesterCreateNew_PeoplePickerSearchBtn").click();
			   
					tb.getObject("TesterCreateNew_PeoplePickerSelectSearchResult").click();
			   
					tb.getObject("TesterCreateNew_PeoplePickerOkBtn").click();
			   
					driver.switchTo().defaultContent();
					Thread.sleep(2000);
					String[] testerRoleArray = testerRoleCreation.split(",");
					
					for(int i=0;i<testerRoleArray.length;i++)
					{
						if((!testerRoleArray[i].equals("Standard"))&&(!testerRoleArray[i].equals("All")))
						{
							tb.getElement("TesterCreateNew_addTesterRoleLink_Id").click();
							tb.getElement("TesterCreateNew_roleName_Id").sendKeys(testerRoleArray[i]);
							tb.getElement("TesterCreateNew_addTesterRole_Id").click();
						}
					}
					String[] testerRoleSelectionArray = testerRoleSelection.split(",");
					List<WebElement> roleSelectionNames = tb.getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					System.out.println(numOfRoles +"In UI ***************");
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
							{
								tb.getObject("TesterCreateNew_SelectionRoleList_Xpath1", "TesterCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
						}
					}
					tb.getElement("TesterCreateNew_testerSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					tb.getObject("Tester_testeraddedsuccessfullyOkButton").click();
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("Exception in createTester function.");
				e.printStackTrace();
			}
			
		}


		public static void createTestCase(String group, String portfolio, String project, String version, String testPassName, 
				String testCaseName) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Test Case");
			Thread.sleep(2000);
			tb.getElement("TestCaseNavigation_Id").click();
			if(tb.getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
			{
				tb.getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
			}
			Thread.sleep(2000);

			try {
				
					dropDownSelect(tb.getElement("TestCaseUpperRibbon_groupDropDown_Id"), tb.getElement("TestCaseUpperRibbon_GroupList_Id"), group );
					
					dropDownSelect(tb.getElement("TestCaseUpperRibbon_PortfolioDropDown_Id"), tb.getElement("TestCaseUpperRibbon_PortfolioList_Id"), portfolio );
					
					dropDownSelect(tb.getElement("TestCaseUpperRibbon_projectDropDown_Id"), tb.getElement("TestCaseUpperRibbon_ProjectList_Id"), project );
					
					dropDownSelect(tb.getElement("TestCaseUpperRibbon_versionDropDown_Id"), tb.getElement("TestCaseUpperRibbon_VersionList_Id"), version );
					
					dropDownSelect(tb.getElement("TestCaseUpperRibbon_testPassDropDown_Id"), tb.getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
					tb.getObject("TestCase_createNewProjectLink").click();
					Thread.sleep(1000);
					tb.getElement("TestCaseCreateNew_TestCaseNameTextField_Id").sendKeys(testCaseName); 
					
					tb.getElement("TestCaseCreateNew_testCaseSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					tb.getObject("TestCase_testCaseaddedsuccessfullyOkButton").click();
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("Exception in createTestCase function.");
				e.printStackTrace();
			}
			
		}

		public static void createTestStep(String group, String portfolio, String project, String version, String testPassName, 
				String testStepName, String testStepExpectedResults, String testCasesToBeSelected, String rolesToBeSelected) throws IOException, InterruptedException
		{
			APP_LOGS.debug("Creating Test Step");
			Thread.sleep(2000);
			tb.getElement("TestStepNavigation_Id").click();
			if(tb.getElement("TestStepCreateNew_TestStepActiveX_Id").isDisplayed())
			{
				tb.getObject("TestStepCreateNew_TestStepActiveX_Close").click();
			}
			Thread.sleep(2000);
			
			try {
				
					dropDownSelect(tb.getElement("TestStepUpperRibbon_groupDropDown_Id"), tb.getElement("TestStepUpperRibbon_GroupList_Id"), group );
					
					dropDownSelect(tb.getElement("TestStepUpperRibbon_PortfolioDropDown_Id"), tb.getElement("TestStepUpperRibbon_PortfolioList_Id"), portfolio );
					
					dropDownSelect(tb.getElement("TestStepUpperRibbon_projectDropDown_Id"), tb.getElement("TestStepUpperRibbon_ProjectList_Id"), project );
					
					dropDownSelect(tb.getElement("TestStepUpperRibbon_versionDropDown_Id"), tb.getElement("TestStepUpperRibbon_VersionList_Id"), version );
					
					dropDownSelect(tb.getElement("TestStepUpperRibbon_testPassDropDown_Id"), tb.getElement("TestStepUpperRibbon_TestPassList_Id"), testPassName );
					
					tb.getObject("TestStep_createNewProjectLink").click();
					
					Thread.sleep(1000);
					
					String testStepDetails = "$(document).contents().find('#rte1').contents().find('body').text('"+testStepName+"')";
				    eventfiringdriver.executeScript(testStepDetails);
				     
				    tb.getElement("TestStepCreateNew_testStepExpectedResults_ID").sendKeys(testStepExpectedResults); 
					
					List<WebElement> TestCaseSelectionNames = tb.getObject("TesterCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfTestCases = TestCaseSelectionNames.size();
					for(int i = 0; i<numOfTestCases;i++)
					{
							if((TestCaseSelectionNames.get(i)).getAttribute("title").equals(testCasesToBeSelected))
							{
								tb.getObject("TestStepCreateNew_SelectionTestCaseList_Xpath1", "TestStepCreateNew_SelectionTestCaseList_Xpath2", (i+1)).click();
								break;
							}
					}
					
					String[] testerRoleSelectionArray = rolesToBeSelected.split(",");
					List<WebElement> roleSelectionNames = tb.getObject("TestStepCreateNew_SelectionRoleList").findElements(By.tagName("li"));
					int numOfRoles = roleSelectionNames.size();
					for(int i = 0; i<numOfRoles;i++)
					{
						for(int j = 0; j < testerRoleSelectionArray.length;j++)
						{
							if((roleSelectionNames.get(i)).getAttribute("title").equals(testerRoleSelectionArray[j]))
							{
								tb.getObject("TestStepCreateNew_SelectionRoleList_Xpath1", "TestStepCreateNew_SelectionRoleList_Xpath2", (i+1)).click();
							}
						}
					}
					
					tb.getElement("TestStepCreateNew_testStepSaveBtn_Id").click();
					
					Thread.sleep(2000);
					
					tb.getObject("TestStep_testStepaddedsuccessfullyOkButton").click();
				
			} 
			catch (Throwable e) 
			{
				APP_LOGS.debug("Exception in createTestStep function.");
				e.printStackTrace();
			}
			
		}

		public static void dropDownSelect(WebElement dropDownList,WebElement SelectionList, String selectionText) throws IOException {
			
			dropDownList.click();
			
			List<WebElement> elements = SelectionList.findElements(By.tagName("li"));
			  
			//System.out.println("The elements present inside  Dropdown List is : " +elements.size());
	  
					  
			for(int i =0 ;i<elements.size();i++)
			{
					//System.out.println("Each element's present inside Dropdown is having text as : " + elements.get(i).getText());
					if(elements.get(i).getText().equals(selectionText))
					{
							elements.get(i).click();
							
							APP_LOGS.debug( selectionText + " : is selected...");
							
							System.out.println(selectionText + " : is selected...");
			  	
							break;
					}
			}
			/*if(Thread.currentThread().getStackTrace()[2].getMethodName().equals("createTestCase"))
			{	
				if(tb.getObject("TestCaseCreateNew_NoTestPassAvailable_DefaultProject").isDisplayed())
				{
					System.out.println("****************");
					tb.getObject("TestCaseCreateNew_NoTestPassAvailable_DefaultProject").click();
				}
			}
			else if(tb.getObject("TesterCreateNew_NoTestPassAvailable_DefaultProject").isDisplayed())
			{
				tb.getObject("TesterCreateNew_NoTestPassAvailable_DefaultProject").click();
			}*/
		}


		public static void dropDownSelectAdd(WebElement dropDownList, WebElement addButton, String text) throws IOException
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
			  
						tb.getElement("ProjectCreateNew_groupPortfolioProjectTextField_Id").sendKeys(text);
						
						
			  
						//Save the entered group or portfolio 
						if (tb.getElement("ProjectCreateNew_projectAddPopUpLabel_Id").getText().contains("Project")) {
							tb.getObject("ProjectCreateNew_projectAddBtn").click();
						}
						else {
							tb.getObject("ProjectCreateNew_groupPortfolioSaveBtn").click(); 
						}
						
						
						
						
			  
		    
				}
			}
			
			catch(Throwable t)
			{
				
				 t.printStackTrace();
			}
			
		}

		public static void selectStartOrEndDate(WebElement calendarImg, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
		 {
			
				try
				{
						//Select start date   
						WebElement startDateImage = calendarImg;  
		   
						APP_LOGS.debug("Clicking on Date Calendar icon...");
		   
						System.out.println("Clicking on Date Calendar icon...");
		   
						startDateImage.click();  
		   
						Select year = new Select(tb.getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
		   
						year.selectByValue(StartEndYear);
		   
						APP_LOGS.debug(StartEndYear +" : Year is selected...");
		   
						System.out.println(StartEndYear +" : Year is selected...");
		   
						Select month = new Select(tb.getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
		   
						month.selectByVisibleText(StartEndMonth);
		   
						APP_LOGS.debug(StartEndMonth +" : Month is selected...");
		   
						System.out.println(StartEndMonth +" : Month is selected...");
						
						WebElement datepicker= tb.getObject("ProjectCreateNew_dateTable");
		   
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
					
					
					t.printStackTrace();
				}
		  
		 }

}
