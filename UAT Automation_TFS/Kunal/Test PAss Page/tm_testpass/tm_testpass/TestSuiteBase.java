package com.uat.suite.tm_testpass;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

import com.uat.base.TestBase;
import com.uat.util.TestUtil;

public class TestSuiteBase extends TestBase{
	//String comments;
	//static WebDriverWait w;
    // check if the suite ex has to be skipped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		APP_LOGS.debug("Checking Runmode of TM_TestPass suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "TM_TestPass Suite"))
		{
			APP_LOGS.debug("Skipped TM_TestPass Suite as the runmode was set to NO");
			throw new SkipException("Runmode of TM_TestPass Suite set to no. So Skipping all tests in Suite A");
		}
	}
	
	public boolean enterTestManagerName(String testManagerName)
	{
		try 
		{
			APP_LOGS.debug("Clicking on Test Manager Image Icon...");
			getObject("TestPassCreateNew_testManagerPeoplePickerImg").click();
			  
			driver.switchTo().frame(1);
			APP_LOGS.debug("Switched to the Version Lead frame...");
		  
			wait = new WebDriverWait(driver,200);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
		  
			APP_LOGS.debug(testManagerName + " : Input text in Test Manager text field...");
			getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys(testManagerName); 
		   
			APP_LOGS.debug("Click on Search button from Version Lead frame ...");
			getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		   
			APP_LOGS.debug("TEst Manager is selected from search result...");
			getObject("ProjectCreateNew_versionLeadStakeholderSelectSearchResult").click();
		  
			APP_LOGS.debug("Click on OK button from Test Manager frame ...");
			getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		  
			driver.switchTo().defaultContent();
			return true;
		} 
		catch (Throwable t) 
		{
			APP_LOGS.debug(this.getClass().getSimpleName()+" Test manager name provided is having some error.");
			return false;
		}
		
	}
	
	public void deleteTestPassFromFirstRowIfProvidedTestPassNamePresent(String TestPassName) throws InterruptedException, IOException
	{
		try 
		{
			APP_LOGS.debug("Deleting Created Test Pass");
			
			getObject("TestPasses_viewAllProjectLink").click();
			
			if(!getElement("TestPassViewAll_NoTestPassAvailable_Id").isDisplayed())
			{
				
				String testPassNameFromViewAllPage = getObject("TestPassViewAll_testPassNameFromViewAllPage").getText();
				
				if (testPassNameFromViewAllPage.equals(TestPassName)) 
				{
					APP_LOGS.debug("Deleting Test Pass");
					
					//getObject("TestPassViewAll_deleteTestPassNameFromViewAllPage1", "TestPassViewAll_deleteTestPassNameFromViewAllPage2", i).click();
					getObject("TestPassViewAll_deleteTestPassNameImageFromViewAllPage").click();
					getObject("TestPassViewAll_popUpDeleteButton").click();
					Thread.sleep(1500);
					getObject("TestPasses_okButtonForPopup").click();
				}
				else 
				{
					APP_LOGS.debug("Test Pass name Not Found.");
				}
			}
			else 
			{
				APP_LOGS.debug("No Test Pass Are present on Test Pass Details Page");
			}
			
		} catch (Throwable t) 
		{
			APP_LOGS.debug("Some Error occured while Deleting the test pass from first row. The test case has been failed");
		}
		
	}
	
	public boolean enterValidDataInMandatoryFieldsOfTestPass(String TestPassName, String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate ) throws InterruptedException, IOException
	{
			
		APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
		
		getObject("TestPasses_createNewProjectLink").click();
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Click on Create New link");
		getObject("TestPasses_createNewProjectLink").click();
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Providing the Test Pass Name");
		getElement("TestPassCreateNew_testPassNameTextField_Id").sendKeys(TestPassName);
		
		APP_LOGS.debug(this.getClass().getSimpleName()+"Providing Test Manager name");
		if(!enterTestManagerName(TP_TestManager))
		{
			return false;
		}
		
		APP_LOGS.debug("Selecting the End date");
		if(!selectStartDateandEndDate(getObject("TestPassCreateNew_endDateImage"),  TP_EndMonth,  TP_EndYear, TP_EndDate))
		{
			return false;
		}

		APP_LOGS.debug("Click on Save");
		getObject("ProjectCreateNew_projectSaveBtn").click();
		
		
		String textFromThePopupAfterSaveButton = getObject("TestPassCreateNew_popupTextGettingAfterSaveUpdateButtonClicked").getText();
		APP_LOGS.debug("Got Popup with Text : "+textFromThePopupAfterSaveButton);
		
		if (textFromThePopupAfterSaveButton.equals("Test Pass added successfully!")) {
			
			APP_LOGS.debug("Click on OK Button");
			
			getObject("TestPassCreateNew_testPassaddedsuccessfullyOkButton").click();
			
		}
		else {
			APP_LOGS.debug("Popup text is other than : 'Test Pass added successfully!'");
			return false;
		}
		
		return true;
			
	}
	
	public boolean selectStartDateandEndDate(WebElement startEndDateImage, String StartEndMonth, String StartEndYear,String StartEndDate) throws IOException
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
	     
	      System.out.println(StartEndYear +" : Year is selected...");
	     
	      Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
	     
	      month.selectByVisibleText(StartEndMonth);
	     
	      APP_LOGS.debug(StartEndMonth +" : Month is selected...");
	     
	      System.out.println(StartEndMonth +" : Month is selected...");
	      
	      WebElement datepicker= getObject("ProjectCreateNew_dateTable");
	     
	      List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
	     
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
	     return false;
	    }
	    
		return true;
	 }
	
	public void selectDetailsFromDD(WebElement allDropDown,WebElement DDElement,String xpathKey1, String xpathKey2, String elementName )
	{
		try 
		{
			allDropDown.click();
			Thread.sleep(1000);
			List<WebElement> DDMember = DDElement.findElements(By.tagName("li"));
			System.out.println("The number of elements present in group drop down list is : " + DDMember.size());
			for(int i =1; i<=DDMember.size();i++)
			{
				//String listMemebers = getElement(xpathKey1, xpathKey2, i).getText();
				String listMemebers = getObject(xpathKey1, xpathKey2, i).getText();
				System.out.println(listMemebers);
				if(listMemebers.equals(elementName))
				{
					getObject(xpathKey1, xpathKey2, i).click();
				}
			}
		} 
		catch (Throwable t) {
			APP_LOGS.debug("Test Case Has Been FAILED In 'selectDetailsFromDD' function.");
			//fail = true;
		}
	}
	
	
	
	public void activeXHandling() throws IOException{
	try
	   {
	   eventfiringdriver.executeScript("var xlApp = new ActiveXObject('Excel.Application')");
	   }catch(Exception e)
	   {
	    //Active x code
	    System.out.println("Active x disabled");
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeX-form")));
	    if(getElement("activeX_Id").isDisplayed())
	     System.out.println("Element found");
	    else
	     System.out.println("An alert informing the user of disabled activex should be present.");
	    Assert.assertTrue(false);
	   }
	}
}