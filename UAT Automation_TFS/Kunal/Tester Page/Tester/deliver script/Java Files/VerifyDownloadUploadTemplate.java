package com.uat.suite.tm_tester;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyDownloadUploadTemplate extends TestSuiteBase  
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String TemplateNotificationLine1;
	String TemplateNotificationLine2;
	String TemplateNotificationLine3;
	String  addNewDataPopupTextAfterUploadClicked;
	int numberOfTestersPresentInGridBefore;
	String invalidRowspopText;
	Alert alert;
	Robot r;
	
	String comments;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_testerSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_testerSuiteXls, this.getClass().getSimpleName());
		
		tester=new ArrayList<Credentials>();
		versionLead=new ArrayList<Credentials>();
		stakeholder=new ArrayList<Credentials>();
		testManager=new ArrayList<Credentials>();
		//testManager=getUsers("Test Manager", 1);
	}
	
	@Test(dataProvider="getTestData")
	public void testVerifyDownloadUploadTemplate(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String Stakeholder,String TestPassName,
			String TP_TestManager, String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role,
			String tester_description, String missingInformationExcelFile, String alreadyExistExcelFile,
			String InvalidProjectExcelFile,String ValidUploadExcelFile) throws Exception
	{
		count++;
		comments = "";
		
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;
	
			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		int Tester_count = Integer.parseInt(tester_testerName);
		tester = getUsers("Tester", Tester_count);
		
		int versionLead_count = Integer.parseInt(VersionLead);
		versionLead = getUsers("Version Lead", versionLead_count);
		
		int stakeholder_count = Integer.parseInt(Stakeholder);
		stakeholder = getUsers("Stakeholder", stakeholder_count);
		
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		APP_LOGS.debug(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");
		
		System.out.println(" Executing Test Case -> VerifyAssignedAddEditDeleteRole...");				
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			getElement("UAT_testManagement_Id").click();
			
			APP_LOGS.debug("Clicking On Test Management Tab ");
		
			Thread.sleep(3000);
		
			APP_LOGS.debug("Creating a project");
			
			createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username/*, stakeholder.get(0).username*/);
			
			APP_LOGS.debug("Closing the browser after saving project.");
			closeBrowser();
			
			APP_LOGS.debug("Opening the browser after saving project.");
			openBrowser();
			
			APP_LOGS.debug("Login with version lead");
			login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead");
			
			APP_LOGS.debug("Clicking on test management tab.");
			getElement("UAT_testManagement_Id").click();
			
			APP_LOGS.debug("Creating Test Pass.");
			createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username);
		
			APP_LOGS.debug("Opening the browser after saving Test pass.");
			closeBrowser();
			
			APP_LOGS.debug("Opening the browser after saving Test pass.");
			openBrowser();
			
			APP_LOGS.debug("Login with Test Manager");
			login(testManager.get(0).username,testManager.get(0).password, "Test Manager");
			
			//click on testManagement tab
			APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
			
			getElement("UAT_testManagement_Id").click();
			Thread.sleep(2000);
			
			APP_LOGS.debug("click on Tester Tab");
			getElement("TesterNavigation_Id").click();
			Thread.sleep(1000);
			
			APP_LOGS.debug("verifying headers");
			
			dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
			dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
			dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
			dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
			dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName );
			
			
			APP_LOGS.debug("Done verifying headers");
			
			//Test case 1: Click on 'Download Template' to download a template
			APP_LOGS.debug("TEST CASE 1 EXICUTING");
			
			APP_LOGS.debug("Click on Download Upload Template Tab.");
		//	wait.until(ExpectedConditions.elementToBeClickable(By.id("downloadT")));
		
			getObject("Testers_downloadUploadTemplateLink").click();
			
			APP_LOGS.debug("click on Download Template Button");
			getElement("TestersDownloadUpload_downloadTemplate_Id").click();
			
			APP_LOGS.debug("Clicking on YES button while Downloading the Template");
			getObject("TestersDownloadUpload_yesButtonOnPopupWhileDownload").click();
			
			System.out.println(getObject("TestersDownloadUpload_templateNotificationDiv").getText());
			
			TemplateNotificationLine1 = getObject("TestersDownloadUpload_templateNotificationPopopText1", "TestersDownloadUpload_templateNotificationPopopText2", 1).getText();
			
			TemplateNotificationLine2 = getObject("TestersDownloadUpload_templateNotificationPopopText1", "TestersDownloadUpload_templateNotificationPopopText2", 2).getText();
		
			String  templateDownloadSuccessMessage = TemplateNotificationLine1+" "+TemplateNotificationLine2;
			
			System.out.println("templateDownloadSuccessMessage "+ templateDownloadSuccessMessage) ;
			
			if (templateDownloadSuccessMessage.equals("Sorry! Tester Information is not available. Blank Template is Downloaded successfully!")) 
			{
				APP_LOGS.debug("Blank Template is Downloaded successfully! has been displayed in Template Notification.");
				
				comments ="Blank Template is Downloaded successfully!...";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Blank Template is Downloaded successfully! has not been displayed in Template Notification.");
				
				comments ="Blank Template is Downloaded successfully! is not displayed...";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Blank Template is Downloaded successfully! is not displayed");
			}
			Thread.sleep(1000);
			getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
			
			//---------------------------------------
			Thread.sleep(1000);
			
			APP_LOGS.debug("Creating a tester for Downloading Template.");
			
			try 
			{
				APP_LOGS.debug("Click on Add Tester link");
				
				getElement("Tester_addTesterLink_Id").click();
				
				enterTesterName(tester.get(0).username);
				
				int numberOfRolePresentBeforeAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
				
				System.out.println("numberOfRolePresentBeforeAddingRole" + numberOfRolePresentBeforeAddingRole);
				
				getElement("TesterAddTester_addRole_Id").click();
				
				getElement("TesterAddTester_roleTextBox_Id").sendKeys(tester_Role);
				
				getElement("TesterAddTester_addRoleButton_Id").click();
				
				int numberOfRolePresentAfterAddingRole = eventfiringdriver.findElements(By.xpath(OR.getProperty("TesterAddTester_numberOfRolePresentInSelectRoleBox"))).size();
				
				getObject("TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox1", "TesterAddTester_checkBoxOfNewlyAddedRoleInRoleBox2", numberOfRolePresentAfterAddingRole).click();
				
				getElement("TesterAddTester_saveTester_Id").click();
				
				Thread.sleep(1000);

				getObject("TesterDetailsAddTester_testeraddedsuccessfullyOkButton").click();
				
				numberOfTestersPresentInGridBefore = eventfiringdriver.findElements(By.xpath(OR.getProperty("TestersViewAll_totalTestersInGrid"))).size();
				
				
					
			} catch (Exception e) {
				fail = true;
				APP_LOGS.debug("Error occured while creating a tester.");
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "create tester");
			}
			//------------------------
			
			APP_LOGS.debug("Downloading Template after creating a tester.");
			
			APP_LOGS.debug("Click on Download Upload Template Tab.");
			//	wait.until(ExpectedConditions.elementToBeClickable(By.id("downloadT")));
			
			getObject("Testers_downloadUploadTemplateLink").click();
			
			APP_LOGS.debug("click on Download Template Button");
			getElement("TestersDownloadUpload_downloadTemplate_Id").click();
			
			APP_LOGS.debug("Clicking on YES button while Downloading the Template");
			Thread.sleep(1000);
			getObject("TestersDownloadUpload_yesButtonOnPopupWhileDownload").click();
			
			Thread.sleep(10000);
	
			templateDownloadSuccessMessage = getObject("TestersDownloadUpload_templateNotificationDiv").getText();
			
			System.out.println(getObject("TestersDownloadUpload_templateNotificationDiv").getText());
			System.out.println("templateDownloadSuccessMessage "+ templateDownloadSuccessMessage) ;
			
			if (templateDownloadSuccessMessage.equals("Template Downloaded successfully!")) 
			{
				APP_LOGS.debug("Template Downloaded successfully! has been displayed in Template Notification.");
				
				comments =comments+"Template Downloaded successfully!...";
			}
			else 
			{
				fail = true;
				
				APP_LOGS.debug("Template Downloaded successfully! has not been displayed in Template Notification.");
				
				comments =comments+"Template Downloaded successfully! is not displayed...";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Template Downloaded successfully! is not displayed");
			}
			//Thread.sleep(2000);
			getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
				
			//-------------------------------------------------
			//Test Case1: It should display Invalid Rows with text as example ex: Row 5: Version Name information is missing from the cell.
			APP_LOGS.debug("Exicuting UPLOAD MISSING ROWS test case");
			try 
			{
				APP_LOGS.debug("Click on Upload button");
				getElement("TestersDownloadUpload_uploadTemplate_Id").click();
				
				//APP_LOGS.debug("Click On Ok Button After Upload button clicked");
				//getObject("TestersDownloadUpload_okButtonWhileUpload").click();
				
				TemplateNotificationLine1 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 1).getText();
				TemplateNotificationLine2 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 2).getText();
				TemplateNotificationLine3 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 3).getText();
				
				addNewDataPopupTextAfterUploadClicked   = TemplateNotificationLine1+" "+TemplateNotificationLine2+" "+TemplateNotificationLine3;
				
				System.out.println("addNewDataPopupTextAfterUploadClicked "+ addNewDataPopupTextAfterUploadClicked) ;
				
				if (addNewDataPopupTextAfterUploadClicked.equals("Please use the Downloaded Template only! Click Ok to continue. Click Cancel to stop this process.")) 
				{
					APP_LOGS.debug("Please use the Downloaded Template only! has been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only!...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Please use the Downloaded Template only! has not been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only! is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Please use the Downloaded Template only! is not displayed");
				}
				
				APP_LOGS.debug("Clicking on Ok button to upload a file");
				getObject("TestersDownloadUpload_okButtonOfuploadTemplateNotification").click();
				Thread.sleep(500);
				APP_LOGS.debug("switching to alert for uploading a file");
				
				alert = driver.switchTo().alert();
				System.out.println(missingInformationExcelFile);
				
				Thread.sleep(1000);
				alert.sendKeys(System.getProperty("user.dir")+"\\Download_UploadTemplate\\"+missingInformationExcelFile);
			    
			    Thread.sleep(1000);
			    Robot r = new Robot();
		        r.keyPress(KeyEvent.VK_ENTER);
		        
		      //  getObject("TestersDownloadUpload_invalidRowsTextAfterUpload").getText();
		        Thread.sleep(1000);
		        invalidRowspopText = getObject("TestersDownloadUpload_invalidRowsDetailsPopopTextAfterUpload").getText();
		        
		        if (invalidRowspopText.contains("information is missing from the cell.")) 
		        {
					APP_LOGS.debug("Invalid Rows is displayed and 'Tester Name information is missing from the cell' is displayed");
					
					comments =comments+"'Tester Name information is missing from the cell' is displayed...";
				}
		        else 
		        {
		        	fail = true;
		        	
		        	APP_LOGS.debug("Invalid Rows is not displayed and 'Tester Name information is missing from the cell' is not displayed. Test case has been failed.");
					
					comments =comments+"'Tester Name information is missing from the cell' is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Check Invalid rows popup text");	
				}
			    
		        getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("something gone wrong while uploading 'Missing Information Tester upload.xlsx'.");
			}
			
	        
			//--------------------------------------------------------
	        //Test Case2: It should display -Invalid Rows with text as example: Row 5: Shrutika Gupta already exists for test pass(es) of this project.
			APP_LOGS.debug("Exicuting UPLOAD ALREADY EXIST TESTERS test case");
			
			try 
			{
				Thread.sleep(1000);
				
				APP_LOGS.debug("Click on Download/upload template tab");
				
				getObject("Testers_downloadUploadTemplateLink").click();
				
				APP_LOGS.debug("Click on Upload button");
				getElement("TestersDownloadUpload_uploadTemplate_Id").click();
				
				//APP_LOGS.debug("Click On Ok Button After Upload button clicked");
				//getObject("TestersDownloadUpload_okButtonWhileUpload").click();
				
				TemplateNotificationLine1 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 1).getText();
				TemplateNotificationLine2 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 2).getText();
				TemplateNotificationLine3 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 3).getText();
				
				addNewDataPopupTextAfterUploadClicked = TemplateNotificationLine1+" "+TemplateNotificationLine2+" "+TemplateNotificationLine3;
				
				System.out.println("addNewDataPopupTextAfterUploadClicked "+ addNewDataPopupTextAfterUploadClicked) ;
				
				if (addNewDataPopupTextAfterUploadClicked.equals("Please use the Downloaded Template only! Click Ok to continue. Click Cancel to stop this process.")) 
				{
					APP_LOGS.debug("Please use the Downloaded Template only! has been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only!...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Please use the Downloaded Template only! has not been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only! is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Please use the Downloaded Template only! is not displayed");
				}
				
				APP_LOGS.debug("Clicking on Ok button to upload a file");
				getObject("TestersDownloadUpload_okButtonOfuploadTemplateNotification").click();
				Thread.sleep(500);
				APP_LOGS.debug("switching to alert for uploading a file");
				
				alert = driver.switchTo().alert();
				
				Thread.sleep(1000);
				alert.sendKeys(System.getProperty("user.dir")+"\\Download_UploadTemplate\\"+alreadyExistExcelFile);
			    
			    Thread.sleep(1000);
			    r = new Robot();
		        r.keyPress(KeyEvent.VK_ENTER);
		        
		      //  getObject("TestersDownloadUpload_invalidRowsTextAfterUpload").getText();
		        Thread.sleep(2000);
		        
		        invalidRowspopText = getObject("TestersDownloadUpload_invalidRowsDetailsPopopTextAfterUpload").getText();
		        
		        System.out.println("invalidRowspopText: "+invalidRowspopText);
		        
		        if (invalidRowspopText.contains("already exists for test pass(es) of this project.")) 
		        {
					APP_LOGS.debug("Invalid Rows is displayed and 'Tester241 already exists for test pass(es) of this project.' is displayed");
					
					comments =comments+"'Tester241 already exists for test pass(es) of this project.' is displayed...";
				}
		        else 
		        {
		        	fail = true;
		        	
		        	APP_LOGS.debug("Invalid Rows is not displayed and 'Tester Name information is missing from the cell' is not displayed. Test case has been failed.");
					
					comments =comments+"'Tester241 already exists for test pass(es) of this project.' is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Check Invalid rows popup text");	
				}
			    
		        getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("something gone wrong while uploading 'Already Exist tester Upload.xlsx'.");
			}
			
			//--------------------------------------------------------
	        //Test Case3: It should display -Invalid Rows with text as example:Row 5: Either this project is not associated with you or no longer available!
			APP_LOGS.debug("Exicuting UPLOAD ALREADY EXIST TESTERS test case");
			
			try 
			{
				Thread.sleep(1000);
				APP_LOGS.debug("Click on Download/upload template tab");
				
				getObject("Testers_downloadUploadTemplateLink").click();
				
				APP_LOGS.debug("Click on Upload button");
				getElement("TestersDownloadUpload_uploadTemplate_Id").click();
				
				//APP_LOGS.debug("Click On Ok Button After Upload button clicked");
				//getObject("TestersDownloadUpload_okButtonWhileUpload").click();
				
				TemplateNotificationLine1 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 1).getText();
				TemplateNotificationLine2 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 2).getText();
				TemplateNotificationLine3 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 3).getText();
				
				addNewDataPopupTextAfterUploadClicked = TemplateNotificationLine1+" "+TemplateNotificationLine2+" "+TemplateNotificationLine3;
				
				System.out.println("addNewDataPopupTextAfterUploadClicked "+ addNewDataPopupTextAfterUploadClicked) ;
				
				if (addNewDataPopupTextAfterUploadClicked.equals("Please use the Downloaded Template only! Click Ok to continue. Click Cancel to stop this process.")) 
				{
					APP_LOGS.debug("Please use the Downloaded Template only! has been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only!...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Please use the Downloaded Template only! has not been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only! is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Please use the Downloaded Template only! is not displayed");
				}
				
				APP_LOGS.debug("Clicking on Ok button to upload a file");
				getObject("TestersDownloadUpload_okButtonOfuploadTemplateNotification").click();
				Thread.sleep(500);
				APP_LOGS.debug("switching to alert for uploading a file");
				
				alert = driver.switchTo().alert();
				
				Thread.sleep(1500);
				alert.sendKeys(System.getProperty("user.dir")+"\\Download_UploadTemplate\\"+InvalidProjectExcelFile);
			    
			    Thread.sleep(1500);
			    r = new Robot();
		        r.keyPress(KeyEvent.VK_ENTER);
		        
		      //  getObject("TestersDownloadUpload_invalidRowsTextAfterUpload").getText();
		        Thread.sleep(3000);
		        APP_LOGS.debug("File has been selected.");
		        
		        invalidRowspopText = getObject("TestersDownloadUpload_invalidRowsDetailsPopopTextAfterUpload").getText();
		        
		        System.out.println("invalidRowspopText: "+invalidRowspopText);
		        
		        if (invalidRowspopText.contains("this project is not associated with you or no longer available")) 
		        {
					APP_LOGS.debug("this project is not associated with you' is displayed");
					
					comments =comments+"'this project is not associated with you' is displayed...";
				}
		        else 
		        {
		        	fail = true;
		        	
		        	APP_LOGS.debug("Invalid Rows is not displayed and 'this project is not associated with you' is not displayed. Test case has been failed.");
					
					comments =comments+"'this project is not associated with you' is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Check Invalid rows popup text");	
				}
			    
		        getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("something gone wrong while uploading 'InvalidProject Tester Upload.xlsx'.");
			}
			//--------------------------------------------------------
	        //Test Case4:  it should display message example: Row 5 : kunal.gujarkar@rgensolutions.com added successfully !
			APP_LOGS.debug("Exicuting UPLOAD VALID TESTERS test case");
			
			try 
			{
				Thread.sleep(1000);
				APP_LOGS.debug("Click on Download/upload template tab");
				
				getObject("Testers_downloadUploadTemplateLink").click();
				
				APP_LOGS.debug("Click on Upload button");
				getElement("TestersDownloadUpload_uploadTemplate_Id").click();
				
				//APP_LOGS.debug("Click On Ok Button After Upload button clicked");
				//getObject("TestersDownloadUpload_okButtonWhileUpload").click();
				
				TemplateNotificationLine1 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 1).getText();
				TemplateNotificationLine2 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 2).getText();
				TemplateNotificationLine3 = getObject("TestersDownloadUpload_uploadTemplateNotificationPopopText1", "TestersDownloadUpload_uploadTemplateNotificationPopopText2", 3).getText();
				
				addNewDataPopupTextAfterUploadClicked = TemplateNotificationLine1+" "+TemplateNotificationLine2+" "+TemplateNotificationLine3;
				
				System.out.println("addNewDataPopupTextAfterUploadClicked "+ addNewDataPopupTextAfterUploadClicked) ;
				
				if (addNewDataPopupTextAfterUploadClicked.equals("Please use the Downloaded Template only! Click Ok to continue. Click Cancel to stop this process.")) 
				{
					APP_LOGS.debug("Please use the Downloaded Template only! has been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only!...";
				}
				else 
				{
					fail = true;
					
					APP_LOGS.debug("Please use the Downloaded Template only! has not been displayed in Template Notification.");
					
					comments =comments+"Please use the Downloaded Template only! is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Please use the Downloaded Template only! is not displayed");
				}
				
				APP_LOGS.debug("Clicking on Ok button to upload a file");
				getObject("TestersDownloadUpload_okButtonOfuploadTemplateNotification").click();
				Thread.sleep(500);
				APP_LOGS.debug("switching to alert for uploading a file");
				
				alert = driver.switchTo().alert();
				
				Thread.sleep(1500);
				alert.sendKeys(System.getProperty("user.dir")+"\\Download_UploadTemplate\\"+ValidUploadExcelFile);
			    
			    Thread.sleep(1500);
			    r = new Robot();
		        r.keyPress(KeyEvent.VK_ENTER);
		        
		        Thread.sleep(5000);
		        APP_LOGS.debug("File has been selected.");
		        
		        String invalidRows0 = getObject("TestersDownloadUpload_invalidRowsTextAfterUpload").getText();
		        System.out.println("invalidRows0 : "+invalidRows0);
		        
		        String addedSuccessfullyText = getObject("TestersDownloadUpload_testerAddedSuccessfullyInValidRowsSection").getText();
		        System.out.println("addedSuccessfully : "+addedSuccessfullyText);
		        
		        if (invalidRows0.equals("Invalid Rows: 0") && addedSuccessfullyText.contains("added successfully")) 
		        {
					APP_LOGS.debug("Invalid Rows: 0 and (Tester Name) added successfully' is displayed in template notification.");
					
					comments =comments+"'Invalid Rows: 0' and '(Tester Name) added successfully' is displayed...";
				}
		        else 
		        {
		        	fail = true;
		        	
		        	APP_LOGS.debug("'Invalid Rows: 0' and '(Tester Name) added successfully' is not displayed. Test case has been failed.");
					
					comments =comments+"'(Tester Name) added successfully' is not displayed...";
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Check Valid rows popup text");	
				}
			    
		        getObject("TestersDownloadUpload_okButtonOfTemplateNotification").click();
		        
		        
			} 
			catch (Throwable t) 
			{
				fail = true;
				
				APP_LOGS.debug("something gone wrong while uploading 'Valid Tester Upload.xlsx'.");
			}
			
			Thread.sleep(2000);
			int numberOfTestersPresentInGridAfter = eventfiringdriver.findElements(By.xpath(OR.getProperty("TestersViewAll_totalTestersInGrid"))).size();
			
			if (numberOfTestersPresentInGridAfter>numberOfTestersPresentInGridBefore) 
			{
				APP_LOGS.debug("Tester has been added in Tester grid using upload functionality.");
				
				comments = comments+"Tester has been added in Tester grid using upload functionality....";
			}
			else 
			{
				fail = true;
	        	
	        	APP_LOGS.debug("Tester has not been added in Tester grid. Test case has been failed.");
				
				comments =comments+"Tester has not been added in Tester grid...";
				
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "added testers are not in grid");	
			}
			
		closeBrowser();
			
		}
		else 
		{
			isLoginSuccess=false;
			APP_LOGS.debug("Login Not Successful");
		}		
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_testerSuiteXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_testerSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_testerSuiteXls, "Test Cases", TestUtil.getRowNum(TM_testerSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
		
}
