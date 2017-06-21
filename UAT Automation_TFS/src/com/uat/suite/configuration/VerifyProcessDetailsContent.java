/* Author Name:-Aishwarya Deshpande
 * Created Date:- 6th Jan 2015
 * Last Modified on Date:- 7th Jan 2015
 * Module Description:- Verification of Contents of Process Details tab with positive set of Data
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
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class VerifyProcessDetailsContent extends TestSuiteBase
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
		APP_LOGS.debug("Executing VerifyProcessDetailsContent Test Case");

		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyProcessDetailsContent(String Role, String groupName, String Portfolio, String projectName, String Version,
			String endMonth, String endYear, String endDate, String VersionLead, String testPassName, String testManager, 
			String testerName,String testerRole, String testCase, String testStep, String assignedRole, String expectedResult, 
			String asIStext1,String asIStext2,String asIStext3,String asIStext4,String asIStext5,String asIStext6,
			String AsIsDesc, String toBeText1, String toBeText2,String toBeText3,String toBeText4,String toBeText5,String toBeText6,
			String ToBeDesc, String procAddedSuccessfullyMessage) throws Exception
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
			 
	/*		 //click on testManagement tab
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
		*/	 
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
					
					//Process Details tab
	            	getElement("Configuration_processDetails_Id").click();   //Clicking on Process Details tab
	            	APP_LOGS.debug("Process Details tab clicked");
					Thread.sleep(2000);
					
					//Selecting Project
	            	List<WebElement> listOfProcessDetailsProjects=getElement("Configuration_processDetailsProjectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfProcessDetailsProjects, projectName);
	            		            	
	            	//Selecting version
	            	List<WebElement> listOfProcessDetailsVersion=getElement("Configuration_processDetailsVersionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfProcessDetailsVersion, Version);
	            	
	            	//Adding Process Details
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext1);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsAsIsDescription_Id").sendKeys(AsIsDesc);         //Adding AS IS Description
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText1);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsToBeDescription_Id").sendKeys(ToBeDesc);         //Adding TO BE Description
	            	
	            	//On Clicking cancel no entry should be made
	            	getElement("Configuration_processDetailsCancelButton_Id").click();
	            	String displayedAsIs=getElement("Configuration_processDetailsAsIsText_Id").getText();         //Extracting AS IS Text
	            	String displayedAsIsDesc=getElement("Configuration_processDetailsAsIsDescription_Id").getText();         //Extracting AS IS Description
	            	String displayedToBe=getElement("Configuration_processDetailsToBeText_Id").getText();         //Extracting TO BE Text
	            	String displayedToBeDesc=getElement("Configuration_processDetailsToBeDescription_Id").getText();         //Extracting TO BE Description
	            	
	            	verifyContentAfterClickingCancel("",displayedAsIs, "AS IS");
	            	verifyContentAfterClickingCancel("",displayedAsIsDesc, "AS IS Description");
	            	verifyContentAfterClickingCancel("",displayedToBe,"TO BE");
	            	verifyContentAfterClickingCancel("",displayedToBeDesc,"TO BE Description");
	            	
	            	
	            	//Adding Process Details
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext1);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsAsIsDescription_Id").sendKeys(AsIsDesc);         //Adding AS IS Description
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText1);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsToBeDescription_Id").sendKeys(ToBeDesc);         //Adding TO BE Description
	            	
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	
	            	//Verify Message
	            	String displayedSuccessMessage=getElement("ProjectCreateNew_alertDiv_Id").getText();
	            	verifyDisplayedContent(procAddedSuccessfullyMessage, displayedSuccessMessage, "'Process added successfully!' Message");
	            	
	            	getObject("Configuration_processDetailsPopupOkButton").click();       //Clicking on Ok button
	            	
	            	//Verying if Process Details are added in the grid
	            	List<WebElement> listOfProcesses=getObject("Configuration_processDetailsProcessGrid").findElements(By.tagName("tr"));
	            	for(int i=1;i<=listOfProcesses.size();i++)
	            	{	
	            		String actualAsIs=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDAsIs_Xpath2",i).getAttribute("title");         //Extracting AS IS Text
	            		String actualToBe=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDToBe_Xpath2",i).getAttribute("title");         //Extracting TO BE Text
	            		
	            		if(actualAsIs.equalsIgnoreCase(asIStext1) && actualToBe.equalsIgnoreCase(toBeText1))
	            		{
		            		String actualProjectName=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDProjectName_Xpath2",i).getText();
		            	//	String actualAsIs=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDAsIs_Xpath2",i).getAttribute("title");         //Extracting AS IS Text
		            		String actualAsIsDesc=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDAsIsDescription_Xpath2",i).getAttribute("title");         //Extracting AS IS Description
		            	//	String actualToBe=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDToBe_Xpath2",i).getAttribute("title");         //Extracting TO BE Text
		            		String actualToBeDesc=getObject("Configuration_ProcessDetailsGrid_Xpath1","Configuration_PDToBeDescription_Xpath2",i).getAttribute("title");         //Extracting TO BE Description
		            	
		            		verifyDisplayedContent(projectName,actualProjectName,"Project Name");
		            		verifyDisplayedContent(asIStext1,actualAsIs,"AS IS Text");
		            		verifyDisplayedContent(AsIsDesc,actualAsIsDesc,"AS IS Description");
		            		verifyDisplayedContent(toBeText1,actualToBe,"TO BE Text");
		            		verifyDisplayedContent(ToBeDesc,actualToBeDesc,"TO BE Description");
	            		}
	            	}
	            	
	            	
	            	/*
	            	 * Pagination Created by :- Kunal Gujarkar
	            	 * Created Date:- 9th Feb 2015
					 * Last Modified on Date:- 9th Feb 2015
	            	 */
	            	
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext2);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText2);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	getObject("Configuration_processDetailsPopupOkButton").click();
	            	
	            	
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext3);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText3);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	getObject("Configuration_processDetailsPopupOkButton").click();
	            	
	            	
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext4);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText4);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	getObject("Configuration_processDetailsPopupOkButton").click();
	            	
	            	
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext5);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText5);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	getObject("Configuration_processDetailsPopupOkButton").click();
	            	
	            	
	            	if (! ( isNextLinkEnable("Configuration_processDetailsNextLink") ) && 
		            		! (	isPreviousLinkEnable("Configuration_processDetailsPreviousLink") ) ) 
		            	{
		            	
			            	APP_LOGS.debug("Next and Previous links are Not enable for 5 Processess ");
							
							comments += "\n- Next and Previous links are Not enable for 5 Processess ";
						
						}
						else 
						{
							assertTrue(false);
							
							fail=true;
							
							APP_LOGS.debug("Next and Previous links are enable for 5 Processess.(Fail)");
							
							comments += "\n- Next and Previous links are enable for 5 Processess. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next link is enable for 5 Processess");
							
						}
		            	
		            	
		            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(asIStext6);         //Adding AS IS Text
		            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(toBeText6);         //Adding TO BE Text
		            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
		            	getObject("Configuration_processDetailsPopupOkButton").click();
		            	
		            	
		            	if (   isNextLinkEnable("Configuration_processDetailsNextLink") &&
		            		 ! isPreviousLinkEnable("Configuration_processDetailsPreviousLink") ) 
		            	{
		            	
			            	APP_LOGS.debug("Next link is Enable(Pagination is activated) for 6 Processess. ");
							
							comments += "\n- Next link is Enable(Pagination is activated) for 6 Processess. ";
						
							getObject("Configuration_processDetailsNextLink").click();
						}
						else 
						{
							assertTrue(false);
							
							fail=true;
							
							APP_LOGS.debug("Next link is NOT Enable(Pagination is NOT activated) for 6 Processess.(Fail)");
							
							comments += "\n- Next link is NOT Enable(Pagination is NOT activated) for 6 Processess. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pagination is NOT activated for 6 Processess");
							
						}
		            	
		            	Thread.sleep(500);
		            	
		            	int numberOfAttachmentOnNextPage = eventfiringdriver.findElements(By.xpath("//div[@id='processGrid']/table/tbody/tr")).size();
		            	
		            	if ( compareIntegers(1, numberOfAttachmentOnNextPage) && 
		            		 compareStrings( asIStext6 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 1).getAttribute("title") ) ) 
		            	{
		            		APP_LOGS.debug("Next page is displaying next Processess. ");
							
							comments += "\n- Next page is displaying next Processess. ";
						
						}
						else 
						{
							fail=true;
							
							APP_LOGS.debug("Next page is NOT displaying next Processess.(Fail)");
							
							comments += "\n- Next page is NOT displaying next Processess. (Fail)";
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Next page is NOT displaying next Processess");
							
						}
		            	
		            	if (  ! isNextLinkEnable("Configuration_processDetailsNextLink") &&
			            		isPreviousLinkEnable("Configuration_processDetailsPreviousLink") ) 
			            	{
			            	
				            	APP_LOGS.debug("Previous link is Enable after click on Next button. ");
								
								comments += "\n- Previous link is Enable after click on Next button. ";
							
								getObject("Configuration_processDetailsPreviousLink").click();
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
		            	
		            	int numberOfAttachmentOnPreviousPage = eventfiringdriver.findElements(By.xpath("//div[@id='processGrid']/table/tbody/tr")).size();
		            	
		            	if ( compareIntegers(5, numberOfAttachmentOnPreviousPage) && 
		            		 compareStrings( asIStext1 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 1).getAttribute("title") ) &&
		            		 compareStrings( asIStext2 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 2).getAttribute("title") ) &&
		            		 compareStrings( asIStext3 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 3).getAttribute("title") ) &&
		            		 compareStrings( asIStext4 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 4).getAttribute("title") ) &&
		            		 compareStrings( asIStext5 , getObject("Configuration_ProcessDetailsGrid_Xpath1", "Configuration_AsIsText_Xpath2", 5).getAttribute("title") ) ) 
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
					APP_LOGS.debug("Exception occurred while verifying Process Details contents.");
					comments += "Exception occurred while verifying Process Details contents. ";
					
					throw new SkipException("Exception occurred while verifying Process Details contents");
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
			comments+=element+" is found correct(Pass).";
			APP_LOGS.debug(element+" is found correct.");
		}
		else
		{
			fail=true;
			comments+=element+" not matching(Fail)";
			APP_LOGS.debug(element+" not matching");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"NotMatching");
		}
	}
	
	private void verifyContentAfterClickingCancel(String expectedContent, String displayedContent, String element)
	{
		if(compareStrings(expectedContent, displayedContent))
		{
			comments+=element+" field entry was cleared after clicking Cancel(Pass).";
			APP_LOGS.debug(element+" field entry was cleared after clicking Cancel.");
		}
		else
		{
			fail=true;
			comments+=element+" field entry was not cleared after clicking Cancel(Fail)";
			APP_LOGS.debug(element+" field entry was not cleared after clicking Cancel");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"OfProcessesNotMatching");
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
