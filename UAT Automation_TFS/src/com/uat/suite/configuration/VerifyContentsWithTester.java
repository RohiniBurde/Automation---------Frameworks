/* Author Name:-Aishwarya Deshpande
 * Created Date:- 2nd Jan 2015
 * Last Modified on Date:- 6th Jan 2015
 * Module Description:- Verification of Contents of Configuration tab for a Tester
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

public class VerifyContentsWithTester extends TestSuiteBase
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
		APP_LOGS.debug("Executing VerifyContentsWithTester Test Case");

		if(!TestUtil.isTestCaseRunnable(ConfigurationSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(ConfigurationSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void Test_VerifyContentsWithTester(String Role, String GroupName, String Portfolio, String ProjectName1, String Version,
			String endMonth, String	endYear, String	endDate, String VersionLead, String ProjectName2, String testPassName1, 
			String	testManager, String	testPassName2, String testerName, String testerRole1, String testerRole2, String testCase, 
			String testStep, String assignedRole, String expectedResult, String environmentURL, String environmentAlias, 
			String attachmentName, String fileName, String attachmentDescription, String AsIStext, String AsIsDesc, String ToBeText, String ToBeDesc) throws Exception
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
			 
/*			 if(!createProject(GroupName, Portfolio, ProjectName1, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName1+" Project not Created Successfully.");
				comments=ProjectName1+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject1Creation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName1+" Project Created Successfully.");
			
			 if(!createProject(GroupName, Portfolio, ProjectName2, Version, endMonth, endYear, endDate, versionLead.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(ProjectName2+" Project not Created Successfully.");
				comments=ProjectName2+" Project not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProject2Creation");
				closeBrowser();

				throw new SkipException("Project not created successfully");
			 }
			 APP_LOGS.debug(ProjectName2+" Project Created Successfully.");
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName1, Version, testPassName1, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName1+" Test Pass not Created Successfully.");
				comments+=testPassName1+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTP1Creation");
				closeBrowser();

				throw new SkipException(testPassName1+" Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName1+" Test Pass Created Successfully.");
			
			 if(!createTestPass(GroupName, Portfolio, ProjectName2, Version, testPassName2, endMonth, endYear, endDate, test_Manager.get(0).username))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug(testPassName2+" Test Pass not Created Successfully.");
				comments+=testPassName2+" Test Pass not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTP2Creation");
				closeBrowser();

				throw new SkipException(testPassName2+" Test Pass not created successfully");
			 }
			 APP_LOGS.debug(testPassName2+" Test Pass Created Successfully.");
		
			 if(!createTester(GroupName, Portfolio, ProjectName1, Version, testPassName1, tester.get(0).username, testerRole1, testerRole1))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+testerRole1+"not Created Successfully.");
				comments+="Tester with role "+testerRole1+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester1Creation");
				closeBrowser();

				throw new SkipException("Tester with role "+testerRole1+" not created successfully");
			 }
			 APP_LOGS.debug(" Tester with role "+testerRole1+" Created Successfully.");

			 if(!createTester(GroupName, Portfolio, ProjectName2, Version, testPassName2, tester.get(0).username, testerRole2, testerRole2))
			 {
				fail=true;
				assertTrue(false);
				APP_LOGS.debug("Tester with role "+testerRole1+"not Created Successfully.");
				comments+="Tester with role "+testerRole1+" not Created Successfully(Fail). ";
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTester2Creation");
				closeBrowser();

				throw new SkipException("Tester with role "+testerRole2+" not created successfully");
			 }
			 APP_LOGS.debug("Tester with role "+testerRole2+" Created Successfully.");
			
			 if(!createTestCase(GroupName, Portfolio, ProjectName1, Version, testPassName1, testCase))
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
	
			 if(!createTestStep(GroupName, Portfolio, ProjectName1, Version, testPassName1, testStep, expectedResult, testCase, assignedRole))
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
					
					//Clicking on User Settings Tab
					getElement("Congiguration_userSettingsTab_Id").click();
					APP_LOGS.debug("User Settings tab clicked ");
					Thread.sleep(2000);
					
	            	//Setting User Settings 
	            	//Selecting Project
	            	List<WebElement> listOfProjects=getElement("Configuration_userSettingsProjectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfProjects, ProjectName1,"Project");
	            	
	            	//Selecting version
	            	List<WebElement> listOfVersion=getElement("Configuration_userSettingsVersionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfVersion, Version,"Version");
	            	
	            	//Selecting Test Pass
	            	List<WebElement> listOfTestPass=getElement("Configuration_userSettingsTestPassDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfTestPass, testPassName1,"Test Pass");
	            	
	            	//Selecting User
	            	List<WebElement> listOfUsers=getElement("Configuration_userSettingsUsersSelectionList_Id").findElements(By.tagName("li"));
	            	for(int i=1;i<=listOfUsers.size();i++)
	            	{	
	            		String userName=getObject("Configuration_userSettingsUsersSelection_Xpath1", "Configuration_userSettingsUsersList_Xpath2", i).getText();
	        			if(userName.equalsIgnoreCase(tester.get(0).username.replace(".", " ")))
	        			{
	        				getObject("Configuration_userSettingsUsersSelection_Xpath1", "Configuration_userSettingsUsersSelection_Xpath2", i).click();
	        				APP_LOGS.debug("User: "+userName+" is selected");
	        				break;
	        			}
	            	}
	            	
	            	//Adding environment
	            	getElement("Configuration_userSettingsAddEnvironment_Id").click();
	            	getElement("Configuration_userSettingsURL_Id").sendKeys(environmentURL);    //Adding URL
	            	getElement("Configuration_userSettingsAlias_Id").sendKeys(environmentAlias);   //adding Alias
	            	getObject("Configuration_userSettingsURLPopupAddButton").click();      //Clicking on Add button
	            	
	            	//Selecting UAT Environment
	            	List<WebElement> listOfEnvironment=getElement("Configuration_userSettingsEnvironmentList_Id").findElements(By.tagName("li"));
	            	for(int i=1;i<=listOfEnvironment.size();i++)
	            	{	
	            		String environmentURLTitle=getObject("Configuration_userSettingsEnvironmentSelection_Xpath1", "Configuration_userSettingsEnvironmentName_Xpath2", i).getAttribute("title");
	        			if(environmentURLTitle.equalsIgnoreCase(environmentURL))
	        			{
	        				getObject("Configuration_userSettingsEnvironmentSelection_Xpath1", "Configuration_userSettingsEnvironmentSelection_Xpath2", i).click();
	        				APP_LOGS.debug("UAT Environmet: "+environmentURLTitle+" is selected");
	        				break;
	        			}
	            	}
	            	
	            	getElement("Configuration_userSettingsSaveButton_Id").click();   //Clicking on save button
	            	getObject("Configuration_userSettingsAdminRequestPopup").click();     //Clicking No on Send Request to Admin popup
	          	
	            	//Uploading documents in Document Library
	            	getElement("Configuration_DocumentLibraryTab_Id").click();   //Clicking on Document Library Tab
	            	APP_LOGS.debug("Document Library tab clicked");
					Thread.sleep(2000);
					
	            	//Selecting Project
	            	List<WebElement> listOfDocLibProjects=getElement("Configuration_DocumentLibraryProjectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfDocLibProjects, ProjectName1,"Project");
	            	
	            	//Selecting version
	            	List<WebElement> listOfDocLibVersion=getElement("Configuration_DocumentLibraryVersionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfDocLibVersion, Version,"Version");
	            	
	            	getElement("Configuration_DocumentLibraryUploadFileButton_Id").click();  //Clicking on Upload File button
	            	getElement("Configuration_DocumentLibraryAttachmentName_Id").sendKeys(attachmentName);   //Adding Attachment Name
	              	getElement("Configuration_DocumentLibraryFileDescription_Id").sendKeys(attachmentDescription);      //Adding Attachment Description
	            	getElement("Configuration_DocumentLibraryBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName);
	            	getElement("Configuration_DocumentLibraryOkButton_Id").click();   //Clicking on Ok button
	            	getObject("Configuration_DocumentLibrarySaveButton_Id").click();  ///Clicking on Save button
	           	
	            	//Addidng Process Details
	            	Thread.sleep(2000);
	            	waitForElementVisibility("Configuration_processDetails_Id", 10).click();   //Clicking on Process Details tab
//	            	getElement("Configuration_processDetails_Id").click();   //Clicking on Process Details tab
	            	APP_LOGS.debug("Process Details tab clicked");
					Thread.sleep(2000);
					
					//Selecting Project
	            	List<WebElement> listOfProcessDetailsProjects=getElement("Configuration_processDetailsProjectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfProcessDetailsProjects, ProjectName1,"Project");
	            		            	
	            	//Selecting version
	            	List<WebElement> listOfProcessDetailsVersion=getElement("Configuration_processDetailsVersionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfProcessDetailsVersion, Version,"Version");
	            	
	            	getElement("Configuration_processDetailsAsIsText_Id").sendKeys(AsIStext);         //Adding AS IS Text
	            	getElement("Configuration_processDetailsAsIsDescription_Id").sendKeys(AsIsDesc);         //Adding AS IS Description
	            	getElement("Configuration_processDetailsToBeText_Id").sendKeys(ToBeText);         //Adding TO BE Text
	            	getElement("Configuration_processDetailsToBeDescription_Id").sendKeys(ToBeDesc);         //Adding TO BE Description
	            	
	            	getElement("Configuration_processDetailsSaveButton_Id").click();      //Clicking on save button
	            	getObject("Configuration_processDetailsPopupOkButton").click();       //Clicking on Ok button
	       
	            }
	            catch (Throwable t) 
				{
					t.printStackTrace();
					fail = true;
					APP_LOGS.debug("Exception occurred while Setting configuration page settings.");
					comments += "Exception occurred while Setting configuration page settings. ";
					
					throw new SkipException("Exception occurred while Setting configuration page settings");
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
 	         
 	         APP_LOGS.debug("Opening Browser... ");
			 openBrowser();
			 
			 if(login(tester.get(0).username,tester.get(0).password, "Tester"))
			 {
	            APP_LOGS.debug("Logged in browser with Tester");
	            try
	            {
	            	//Clicking on Configuration Tab
					getElement("UAT_configuration_Id").click();
					APP_LOGS.debug("Configuration tab clicked ");
					Thread.sleep(2000);
					
					//Selecting Project
	            	List<WebElement> listOfConfigurationTabProjects=getElement("Configuration_projectDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByTitle(listOfConfigurationTabProjects, ProjectName1, "Project");
	            	
	            	//Verifying Version Dropdown availability
	            	String versionDDClassAttribute=getElement("Configuration_versionDD_Id").getAttribute("class");
	            	String selectVersionLabel=getObject("Configuration_selectVersionLabelOnTesters_Xpath").getText();
	            	boolean versionDDAvailable=verifyDDAvailbility(OR.getProperty("Configuration_generalSettingsDD_class"), versionDDClassAttribute, 
	            			"Version Dropdown",resourceFileConversion.getProperty("Configuration_selectVersionLabel"), 
	            			selectVersionLabel, "Select Version Label");
	            			
	            	if(versionDDAvailable==true)
	            	{
	            		//Selecting version
	            		List<WebElement> listOfConfigurationTabVersion=getElement("Configuration_versionDD_Id").findElements(By.tagName("option"));
		            	selectElementFromDDByText(listOfConfigurationTabVersion, Version, "Version");
	            	}
	            	else
	            	{
	            		comments+="Version cannot be selected as Version Dropdown not available";
	            		APP_LOGS.debug("Version cannot be selected as Version Dropdown not available");
	            	}
            	
	            	//Verifying User Name
	            	if(assertTrue(getElement("Configuration_userName_Id").getText().equalsIgnoreCase(tester.get(0).username.replace(".", " "))))
	            	{
	            		APP_LOGS.debug("User Name displayed is the Tester Name.");
	            		comments+="User Name displayed is the Tester Name(Pass).";
	            	}
	            	else
	            	{
	            		fail=true;
	            		APP_LOGS.debug("User Name displayed is not the Tester Name.");
	            		comments+="User Name displayed is not the Tester Name(Fail).";
	            		TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectUserName");
	            	}
	            	
	            	//Verifying Test Pass Dropdown Content
	            	List<WebElement> listOfConfigurationTabTestPass=getElement("Configuration_testPassDD_Id").findElements(By.tagName("option"));
	            	String actualTestPassArray[]=new String[listOfConfigurationTabTestPass.size()];
	            	for(int i=0;i<=listOfConfigurationTabTestPass.size();i++)
	            	{
	            		actualTestPassArray[i]=listOfConfigurationTabTestPass.get(i).getAttribute("title");
						if(actualTestPassArray[i].equalsIgnoreCase(testPassName1))
						{
							listOfConfigurationTabTestPass.get(i).click();
							APP_LOGS.debug("Test Pass is selected");
							break;
						}	            	
	            	}
	            	
	            	String expectedTestPassArray[]={testPassName1};
	            	verifyDropdownContents(expectedTestPassArray, actualTestPassArray);
	            	
	            	//Verifying UAT Environment details
	            	String actualEnvironmentURL=getObject("Configuration_UATEnvironment").getAttribute("title");
	            	String actualEnvironmentAlias=getObject("Configuration_UATEnvironment").getText();
	            	if(assertTrue(actualEnvironmentURL.equalsIgnoreCase(environmentURL) && actualEnvironmentAlias.equalsIgnoreCase(environmentAlias)))
	            	{
	            		APP_LOGS.debug("UAT Environment is displayed correctly for the Tester.");
	            		comments+="UAT Environment is displayed correctly for the Tester(Pass).";
	            	}
	            	else
	            	{
	            		fail=true;
	            		APP_LOGS.debug("UAT Environment is not displayed correctly for the Tester.");
	            		comments+="UAT Environment is not displayed correctly for the Tester(Fail).";
	            		TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectEnvironmentDetails");
	            	}
	            	
	            	//Verifying Document Library Details
	            	List<WebElement> listOfDocuments=getElement("Configuration_documentLibraryFileGrid").findElements(By.tagName("tr"));
	            	for(int i=1;i<=listOfDocuments.size();i++)
	            	{
	            		String uploadedFileName=getObject("Configuration_documentLibraryFileName_Xpath1", "Configuration_documentLibraryFileName_Xpath2", i).getText();
	            		String actualFileDescription=getObject("Configuration_documentLibraryFileName_Xpath1", "Configuration_documentLibraryFileName_Xpath2", i).getAttribute("title");
	            		if(uploadedFileName.equalsIgnoreCase(attachmentName))
	            		{
	            			if(compareStrings(attachmentDescription,actualFileDescription ))
	            			{
	            				comments+="Document Library details are found correct(Pass)";
	            				APP_LOGS.debug("Document Library details are found correct(Pass)");
	            			}
	            			else
	            			{
	            				fail=true;
	            				comments+="Document Library details are not found correct(Fail)";
	            				APP_LOGS.debug("Document Library details are not found correct(Fail)");
	            				TestUtil.takeScreenShot(this.getClass().getSimpleName(),"DocumentLibraryDetailsNotMatching");
	            			}
	            		}
	            	}
	            	
	            	//Verifying Process Details
	            	List<WebElement> listOfProcess=getObject("Configuration_processDetailsGrid").findElements(By.tagName("tr"));
	            	for(int i=1;i<=listOfProcess.size();i++)
	            	{
	            		String displayedProjectName=getObject("Configuration_processDetailsGrid_Xpath1", "Configuration_PDProjectName_Xpath2", i).getAttribute("title");
	            		String asIs=getObject("Configuration_processDetailsGrid_Xpath1", "Configuration_PDAsIs_Xpath2", i).getAttribute("title");
	            		String asIsDesc=getObject("Configuration_processDetailsGrid_Xpath1", "Configuration_PDAsIsDescription_Xpath2", i).getAttribute("title");
	            		String toBe=getObject("Configuration_processDetailsGrid_Xpath1", "Configuration_PDToBe_Xpath2", i).getAttribute("title");
	            		String toBeDesc=getObject("Configuration_processDetailsGrid_Xpath1", "Configuration_PDToBeDescription_Xpath2", i).getAttribute("title");
	            		
	            		verifyDisplayedProcess(ProjectName1, displayedProjectName, "ProjectName");
	            		verifyDisplayedProcess(AsIStext, asIs, "AS IS");
	            		verifyDisplayedProcess(AsIsDesc, asIsDesc, "AS IS Description");
	            		verifyDisplayedProcess(ToBeText, toBe, "TO BE");
	            		verifyDisplayedProcess(ToBeDesc, toBeDesc, "TO BE Description");
	            	}
	            	
	            	//Verifying for data not avaliable messages
	            	//Selecting Project
	            	selectElementFromDDByTitle(listOfConfigurationTabProjects, ProjectName2, "Project");
	            	
	            	//Selecting version
	            	List<WebElement> listOfVersion1=getElement("Configuration_versionDD_Id").findElements(By.tagName("option"));
	            	selectElementFromDDByText(listOfVersion1, Version, "Version");
	            	
	            	//Verifying No UAT Environment available Message
	            	String actualNoEnvironment=getObject("Configuration_noUATEnvironment").getText();
	            	if(compareStrings(actualNoEnvironment, resourceFileConversion.getProperty("Configuration_NoEnvironmentAvaliableMessage")))
	            	{
	            		APP_LOGS.debug("No UAT Environment available message is displayed correctly.");
	            		comments+="No UAT Environment available message is displayed correctly(Pass).";
	            	}
	            	else
	            	{
	            		fail=true;
	            		APP_LOGS.debug("No UAT Environment available message is not displayed correctly.");
	            		comments+="No UAT Environment available message is not displayed correctly(Fail).";
	            		TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectNoEnvironmentMessage");
	            	}
	            	
	            	//Verfiying No Document Library Available Message
	            	String actualNoDocument=getObject("Configuration_noDocumentUploaded").getText();
	            	if(compareStrings(actualNoDocument, resourceFileConversion.getProperty("Configuration_NoDocumentLibraryAvailableMessage")))
	            	{
	            		APP_LOGS.debug("No Files uploaded Message is displayed correctly.");
	            		comments+="No Files uploaded Message is displayed correctly(Pass).";
	            	}
	            	else
	            	{
	            		fail=true;
	            		APP_LOGS.debug("No Files uploaded Message is not displayed correctly.");
	            		comments+="No Files uploaded Message is not displayed correctly(Fail).";
	            		TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectUploadedFilesMessage");
	            	}
	            	
	            	//Verifying No Process Details Available Message
	            	String actualNoProcessAvailable=getObject("Configuration_noProcessAvailable").getText();
	            	if(compareStrings(actualNoProcessAvailable, resourceFileConversion.getProperty("Configuration_NoProcessAvailableMessage")))
	            	{
	            		APP_LOGS.debug("No Process Available Message is displayed correctly.");
	            		comments+="No Process Available Message is displayed correctly(Pass).";
	            	}
	            	else
	            	{
	            		fail=true;
	            		APP_LOGS.debug("No Process Available Message is not displayed correctly.");
	            		comments+="No Process Available Message is not displayed correctly(Fail).";
	            		TestUtil.takeScreenShot(this.getClass().getSimpleName(),"IncorrectNoProcessAvailableMessage");
	            	}
	            }
	            catch (Throwable t) 
				{
					
	            	t.printStackTrace();
	            	fail = true;
					APP_LOGS.debug("Exception occurred while verifying the Contents of Configuration Page for a Tester.");
					comments += "Exception occurred while verifying the Contents of Configuration Page for a Tester. ";
					
					throw new SkipException("Exception occurred while verifying the Contents of Configuration Page for a Tester");
				}
			 }
			 else
			 {
				fail=true;
				APP_LOGS.debug("Failed Login for Role: Tester");
	           	comments="Failed Login for Role: Tester(Fail)";
	           	TestUtil.takeScreenShot(this.getClass().getSimpleName(),"TesterLoginFailed");
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
	
	private void selectElementFromDDByTitle(List<WebElement> listOfElements,String selectedElement, String element)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getAttribute("title").equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(element+" is selected");
				break;
			}
    	}
	}
	
	private void selectElementFromDDByText(List<WebElement> listOfElements,String selectedElement, String element)
	{
		for(int i=0;i<listOfElements.size();i++)
    	{
			if(listOfElements.get(i).getText().equalsIgnoreCase(selectedElement))
			{
				listOfElements.get(i).click();
				APP_LOGS.debug(element+" is selected");
				break;
			}
    	}
	}
	
	private void verifyDropdownContents(String[] expectedArray, String[] actualArray)
	{
		if(compareIntegers(expectedArray.length,actualArray.length))
		{
			for(int i=0;i<expectedArray.length;i++)
			{
				if(assertTrue(expectedArray[i].equalsIgnoreCase(actualArray[i])))
				{
					comments+=expectedArray[i]+" Test Pass present in the dropdown(Pass).";
					APP_LOGS.debug(expectedArray[i]+" Test Pass present in the dropdown ");
				}
				else
				{
					fail=true;
					comments+=expectedArray[i]+" Test Pass not present in the dropdown(Fail)";
					APP_LOGS.debug(expectedArray[i]+" Test Pass not present in the dropdown ");
				}
			}
		}
		else
		{
			fail=true;
			comments+="Total Number of elements in Test Pass Dropdown do not match";
			APP_LOGS.debug("Total Number of elements in Test Pass Dropdown do not match");
		}
	}
	
	private void verifyDisplayedProcess(String expectedContent, String displayedContent, String element)
	{
		if(compareStrings(expectedContent, displayedContent))
		{
			comments+=element+" of Process Details is found correct(Pass)";
			APP_LOGS.debug(element+" of Process Details is found correct");
		}
		else
		{
			fail=true;
			comments+=element+" of Process Details not matching(Fail)";
			APP_LOGS.debug(element+" of Process Details not matching");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),"ProcessDetailsNotMatching");
		}
	}
	
	private boolean verifyDDAvailbility(String expected, String actual, String element, String expectedLabel, String displayedLabel, 
			String labelOfElement)
	{
		if(compareStrings(expected, actual))
		{
			APP_LOGS.debug(element+" is displayed");
			comments+=element+" is displayed(Pass).";
			//Verify Dropdown Label
			verifyContent(expectedLabel,displayedLabel , labelOfElement);
			return true;
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" is not displayed");
			comments+=element+" is not displayed(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"Notdisplayed");
			return false;
		}
	}
	
	private void verifyContent(String expected, String actual, String element)
	{
		if(compareStrings(expected, actual))
		{
			APP_LOGS.debug(element+" is displayed");
			comments+=element+" is displayed(Pass).";
		}
		else
		{
			fail=true;
			APP_LOGS.debug(element+" is not displayed");
			comments+=element+" is not displayed(Fail).";
			TestUtil.takeScreenShot(this.getClass().getSimpleName(),element+"Notdisplayed");
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
