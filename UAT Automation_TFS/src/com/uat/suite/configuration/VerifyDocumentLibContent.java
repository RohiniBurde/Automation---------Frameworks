/* Author Name:-Aishwarya Deshpande
 * Created Date:- 6th Jan 2015
 * Last Modified on Date:- 7th Jan 2015
 * Module Description:- Verification of Contents of Document Library tab with positive set of Data
 */

package com.uat.suite.configuration;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class VerifyDocumentLibContent extends TestSuiteBase
{
	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	
	
	//Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug("Executing VerifyDocumentLibContent Test Case");

		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyDocumentLibContent(String Role, String groupName, String Portfolio, String projectName, String Version,
			String endMonth, String endYear, String endDate, String VersionLead, String testPassName, String testManager, 
			String testerName,String testerRole, String testCase, String testStep, String assignedRole, String expectedResult, 
			String attachmentName, String fileName1 , String fileName2, String fileName3, String fileName4, String fileName5, String fileName6,
			String attachmentDescription) throws Exception
	{
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		comments="";			

		APP_LOGS.debug("Opening Browser... ");
		openBrowser();

		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			//version lead
			 int versionlead_count = Integer.parseInt(VersionLead);
			 versionLead=new ArrayList<Credentials>();
			 versionLead = getUsers("Version Lead", versionlead_count);
			 
			 //TestManager 
			 int testManager_count = Integer.parseInt(testManager);
			 test_Manager=new ArrayList<Credentials>();
			 test_Manager = getUsers("Test Manager", testManager_count);
			 
			 //Tester 
			 int tester_count = Integer.parseInt(testerName);
			 tester=new ArrayList<Credentials>();
			 tester = getUsers("Tester", tester_count);

			 //click on testManagement tab
			 APP_LOGS.debug(" Clicking on Test Management Tab ");
			 getElement("UAT_testManagement_Id").click();
			 Thread.sleep(1000);
			 
			 if(!createProject(groupName, Portfolio, projectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(projectName+" Project not Created Successfully.");
				comments=projectName+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(projectName+" Project Created Successfully.");
			 			
			 if(!createTestPass(groupName, Portfolio, projectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName+" Test Pass not Created Successfully.");
				comments+=testPassName+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTPCreation");
				closeBrowser();

				throw new SkipException(testPassName+" Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
			
			 if(!createTester(groupName, Portfolio, projectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+testerRole+"not Created Successfully.");
				comments+="Tester with role "+testerRole+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTesterCreation");
				closeBrowser();

				throw new SkipException("Tester with role "+testerRole+" not created successfully");
			 }
			 APP_LOGS.debug(" Tester with role "+testerRole+" Created Successfully.");

			 if(!createTestCase(groupName, Portfolio, projectName, Version, testPassName, testCase))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testCase+" Test Case not Created Successfully.");
				comments+=testCase+" Test Case not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTC1Creation");
				closeBrowser();

				throw new SkipException(testCase+" Test Case not created successfully");
			 }
			 APP_LOGS.debug(testCase+" Test Case Created Successfully.");
	
			 if(!createTestStep(groupName, Portfolio, projectName, Version, testPassName, testStep, expectedResult, testCase, assignedRole))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testStep+" Test Step not Created Successfully.");
				comments+=testStep+" Test Step not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTS1Creation");
				closeBrowser();

				throw new SkipException(testStep+" Test Step not created successfully");
			 }
			 APP_LOGS.debug(testStep+" Test Step Created Successfully.");

			 APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
			 comments+="Data has been made Successfully from Test Management tab... ";
			
			 APP_LOGS.debug("Closing Browser... ");
			 closeBrowser();
			 
			 APP_LOGS.debug("Opening Browser... ");
			 openBrowser();
			 
			 if(login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager"))
			 {
	            APP_LOGS.debug("Logged in browser with Test Manager");
	            try
	            {
	            	//Clicking on Configuration Tab
					getElement("UAT_configuration_Id").click();
					APP_LOGS.debug("Configuration tab clicked ");
					Thread.sleep(2000);
					
					//Uploading documents in Document Library
	            	getElement("Configuration_DocumentLibraryTab_Id").click();   //Clicking on Document Library Tab
	            	APP_LOGS.debug("Document Library tab clicked");
					Thread.sleep(2000);
					
	            	//Selecting Project
	            	List<WebElement> listOfDocLibProjects=getElement("Configuration_DocumentLibraryProjectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfDocLibProjects, projectName);
	            	
	            	//Selecting version
	            	List<WebElement> listOfDocLibVersion=getElement("Configuration_DocumentLibraryVersionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfDocLibVersion, Version);
	            	
	            	//Verify if upload button is available
	            	if(assertTrue(isElementExistsById("Configuration_DocumentLibraryUploadFileButton_Id")))
	            	{
	            		comments+="Upload button is available(Pass).";
	        			APP_LOGS.debug("Upload button is available");
	        			
	        			//Uploading file
//		            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
		            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 5).click();     //Clicking on Upload File button
		            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
		            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
		            	
		            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
		            	
		            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
		            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
		            	
		            	//Verify if the Uploaded file details are entered in the grid
		            	List<WebElement> listOfDocumentsUploaded=getObject("Configuration_uplodedFilesGrid_Xpath").findElements(By.tagName("tr"));
		            	for(int i=1;i<=listOfDocumentsUploaded.size();i++)
		            	{
		            		String displayedFileName=getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFileName_Xpath2", i).getAttribute("title");
		            		if(displayedFileName.equalsIgnoreCase(attachmentName))
		            		{
		            			//String displayedFileName=getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFileName_Xpath2", i).getAttribute("title");
		            			String displayedFileView=getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", i).getText();
		            			String displayedFileDescription=getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesDescription_Xpath2", i).getAttribute("title");
		            		
		            			verifyDisplayedContent(attachmentName, displayedFileName, "File Name");
		            			verifyDisplayedContent(fileName1, displayedFileView, "File View");
		            			verifyDisplayedContent(attachmentDescription, displayedFileDescription, "File Description");
		            			break;
		            		}
		            	}
	        		}
	        		else
	        		{
	        			fail=true;
	        			comments+="Upload button is not available(Fail).";
	        			APP_LOGS.debug("Upload button is not available.");
	        			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"UploadButtonNotAvailable");
	        		}

	            	/*
	            	 * Pagination Created by :- Kunal Gujarkar
	            	 * Created Date:- 9th Feb 2015
					 * Last Modified on Date:- 9th Feb 2015
	            	 */
	            	
	            	//Uploading file
//	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 15).click();     //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName2);
	            	
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	            	
	            	//Uploading file
//	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 15).click();     //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName3);
	            	
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	            	
	            	//Uploading file
//	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 15).click();     //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName4);
	            	
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	            	
	            	//Uploading file
//	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 15).click();     //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName5);
	            	
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	            	
	            	
	            	if (! ( isNextLinkEnable("Configuration_DocumentLibraryNextLink") ) && 
	            		! (	isPreviousLinkEnable("Configuration_DocumentLibraryPreviousLink") ) ) 
	            	{
	            	
		            	APP_LOGS.debug("Next and Previous links are Not enable for 5 attachments ");
						
						comments += "\n- Next and Previous links are Not enable for 5 attachments ";
					
					}
					else 
					{
						assertTrue(false);
						
						fail=true;
						
						APP_LOGS.debug("Next and Previous links are enable for 5 attachments.(Fail)");
						
						comments += "\n- Next and Previous links are enable for 5 attachments. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next link is enable for 5 attachments");
						
					}
	            	
	            	//Uploading file
//	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	waitForElementVisibility("Configuration_DocumentLibraryUploadFileButton_Id", 15).click();     //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	            	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName6);
	            	
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	            	
	            	
	            	
	            	if (   isNextLinkEnable("Configuration_DocumentLibraryNextLink") &&
	            		 ! isPreviousLinkEnable("Configuration_DocumentLibraryPreviousLink") ) 
	            	{
	            	
		            	APP_LOGS.debug("Next link is Enable(Pagination is activated) for 6 attachments. ");
						
						comments += "\n- Next link is Enable(Pagination is activated) for 6 attachments. ";
					
						getObject("Configuration_DocumentLibraryNextLink").click();
					}
					else 
					{
						assertTrue(false);
						
						fail=true;
						
						APP_LOGS.debug("Next link is NOT Enable(Pagination is NOT activated) for 6 attachments.(Fail)");
						
						comments += "\n- Next link is NOT Enable(Pagination is NOT activated) for 6 attachments. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pagination is NOT activated for 6 attachments");
						
					}
	            	
	            	Thread.sleep(500);
	            	
	            	int numberOfAttachmentOnNextPage = eventfiringdriver.findElements(By.xpath("//div[@id='grdUploadedFiles']/table/tbody/tr")).size();
	            	
	            	if ( compareIntegers(1, numberOfAttachmentOnNextPage) && 
	            		 compareStrings( fileName6 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 1).getText() ) ) 
	            	{
	            		APP_LOGS.debug("Next page is displaying next attachments. ");
						
						comments += "\n- Next page is displaying next attachments. ";
					
					}
					else 
					{
						fail=true;
						
						APP_LOGS.debug("Next page is NOT displaying next attachments.(Fail)");
						
						comments += "\n- Next page is NOT displaying next attachments. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next page is NOT displaying next attachments");
						
					}
	            	
	            	if (  ! isNextLinkEnable("Configuration_DocumentLibraryNextLink") &&
		            		isPreviousLinkEnable("Configuration_DocumentLibraryPreviousLink") ) 
		            	{
		            	
			            	APP_LOGS.debug("Previous link is Enable after click on Next button. ");
							
							comments += "\n- Previous link is Enable after click on Next button. ";
						
							getObject("Configuration_DocumentLibraryPreviousLink").click();
						}
						else 
						{
							assertTrue(false);
							
							fail=true;
							
							APP_LOGS.debug("Previous link is NOT Enable after click on Next button.(Fail)");
							
							comments += "\n- Previous link is NOT Enable after click on Next button.(Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous link is NOT Enable after click on Next button.");
							
						}
	            	
	            	Thread.sleep(500);
	            	
	            	int numberOfAttachmentOnPreviousPage = eventfiringdriver.findElements(By.xpath("//div[@id='grdUploadedFiles']/table/tbody/tr")).size();
	            	
	            	if ( compareIntegers(5, numberOfAttachmentOnPreviousPage) && 
	            		 compareStrings( fileName1 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 1).getText() ) &&
	            		 compareStrings( fileName2 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 2).getText() ) &&
	            		 compareStrings( fileName3 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 3).getText() ) &&
	            		 compareStrings( fileName4 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 4).getText() ) &&
	            		 compareStrings( fileName5 , getObject("Configuration_uplodedFilesDetails_Xpath1", "Configuration_uplodedFilesView_Xpath2", 5).getText() ) ) 
	            	{
	            		APP_LOGS.debug("Previous page is displaying All 5 Attchments. ");
						
						comments += "\n- Previous page is displaying All 5 Attchments. ";
					
					}
					else 
					{
						fail=true;
						
						APP_LOGS.debug("Previous page is NOT displaying All 5 Attchments.(Fail)");
						
						comments += "\n- Previous page is NOT displaying All 5 Attchments. (Fail)";
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Previous page is NOT displaying All 5 Attchment");
						
					}
	            	
	            	
	            }
	            catch (Throwable t) 
				{
					
	            	t.printStackTrace();
	            	fail = true;
					APP_LOGS.debug("Exception occurred while verifying Document Library contents.");
					comments += "Exception occurred while verifying Document Library contents. ";
					
					throw new SkipException("Exception occurred while verifying Document Library contents");
				}
			 }
			 else
			 {
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Test Manager");
	           	comments="Failed Login for Role: Test Manager(Fail)";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TestManagerLoginFailed");
			 }
			 APP_LOGS.debug("Closing Browser... ");
 	         closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Not Successful");
			comments+="Login Not Successful";
		}
	}
	
	private void selectElementFromDDByTitle(List<WebElement> listOfElements,String selectedElement)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getAttribute("title").equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(selectedElement+" is selected");
				break;
			}
    	}
	}
	
	
	 
	private void selectElementFromDDByText(List<WebElement> listOfElements,String selectedElement)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getText().equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(selectedElement+" is selected");
				break;
			}
    	}
	}
	
	private void verifyDisplayedContent(String expectedContent, String displayedContent, String element)
	{
		if(compareStrings(expectedContent, displayedContent))
		{
			comments+=element+" of the uploaded file is found correct(Pass).";
			APP_LOGS.debug(element+" of the uploaded file is found correct.");
		}
		else
		{
			fail=true;
			comments+=element+" of the uploaded file not matching(Fail)";
			APP_LOGS.debug(element+" of the uploaded file not matching");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"OfDocLibNotMatching");
		}
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(ConfigurationSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(ConfigurationSuiteXls, "Test Cases", TestUtil.getRowNum(ConfigurationSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(ConfigurationSuiteXls, this.getClass().getSimpleName()) ;
	}
}
