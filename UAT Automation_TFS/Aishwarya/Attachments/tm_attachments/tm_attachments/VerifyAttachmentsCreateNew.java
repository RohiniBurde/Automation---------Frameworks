package com.uat.suite.tm_attachments;

import java.io.IOException;
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

public class VerifyAttachmentsCreateNew extends TestSuiteBase {
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static boolean isLoginSuccess=false;
	String comments;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> test_Manager;
	ArrayList<Credentials> tester;
	int testCaseFlag = 0;
	int testStepFlag = 0;
	
	@BeforeTest
	public void checkTestSkip(){
		
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName())){
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider="getTestData")
	public void VerifyAttachmentsCreateNew1(String Role,String GroupName,String Portfolio,String ProjectName,String Version,String endMonth,String endYear,
			String endDate,String VersionLead,String ExpectedMessage,String testPassName,String testManager, String TP_endMonth,String TP_endYear,
			String TP_endDate,String testerName,String testerRole,String testCaseName,String testStepName,String TestStepExpectedResult, String assignedRole,String attachmentName,
			String expectedAttachmentNameBlankMessage,String Description,String expectedTestStepBlankMessage,String fileName,String expectedFileNameBlankMessage,String expectedResult) throws Exception{
		
		count++;
		
		if(!runmodes[count].equalsIgnoreCase("Y")){
			skip=true;
			APP_LOGS.debug("Skipping Test Set Data No."+(count+1)+" as Runmode for test set data is set to No");;
			throw new SkipException("Runmode for test set data no. "+(count+1)+" is set to no");
		}
		
		APP_LOGS.debug(" Executing VerifyAttachmentsCreateNew Test Case ");
		System.out.println(" Executing VerifyAttachmentsCreateNew Test Case ");				
		
		openBrowser();
		APP_LOGS.debug("Opening Browser");
	
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try{
				
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
		
				if(!createProject(GroupName, Portfolio, ProjectName, Version, endMonth, endYear, endDate, versionLead.get(0).username))
				{
					fail=true;
					APP_LOGS.debug(ProjectName+" Project not Created Successfully.");
					comments=ProjectName+" Project not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulProjectCreation");
					closeBrowser();

					throw new SkipException("Project not created successfully");
				}
				APP_LOGS.debug(ProjectName+" Project Created Successfully.");
				comments=ProjectName+" Project Created Successfully(Pass). ";

				closeBrowser();
			    APP_LOGS.debug(" Closed Browser after verifying if project was existing and creating project if not existing ");
				System.out.println(" Closed Browser after verifying if project was existing and creating project if not existing ");

                APP_LOGS.debug("Opening Browser... ");
				System.out.println("Opening Browser... ");
				openBrowser();
			
				login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead");
	            APP_LOGS.debug("Logged in browser with Version Lead");
	            
				//click on testManagement tab
				APP_LOGS.debug(" Clicking on Test Management Tab");
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(3000);
				
				// creating test pass,test case,testers and test step
				if(!createTestPass(GroupName, Portfolio, ProjectName, Version, testPassName, endMonth, endYear, endDate, test_Manager.get(0).username))
				{
					fail=true;
					APP_LOGS.debug(testPassName+" Test Pass not Created Successfully.");
					comments+=testPassName+" Test Pass not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestPassCreation");
					closeBrowser();

					throw new SkipException("Test Pass not created successfully");
				}
				APP_LOGS.debug(testPassName+" Test Pass Created Successfully.");
				comments+=testPassName+" Test Pass Created Successfully(Pass). ";
				
				if(!createTester(GroupName, Portfolio, ProjectName, Version, testPassName, tester.get(0).username, testerRole, testerRole))
				{
					fail=true;
					APP_LOGS.debug("Tester not Created Successfully.");
					comments+="Tester not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTesterCreation");
					closeBrowser();

					throw new SkipException("Tester not created successfully");
				}
				APP_LOGS.debug(" Tester Created Successfully.");
				comments+=" Tester Created Successfully(Pass). ";

				if(!createTestCase(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName))
				{
					fail=true;
					APP_LOGS.debug(testCaseName+" Test Case not Created Successfully.");
					comments+=testCaseName+" Test Case not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestCaseCreation");
					closeBrowser();

					throw new SkipException("Test Case not created successfully");
				}
				APP_LOGS.debug(testCaseName+" Test Case Created Successfully.");
				comments+=testCaseName+" Test Case Created Successfully(Pass). ";

				if(!createTestStep(GroupName, Portfolio, ProjectName, Version, testPassName, testStepName, TestStepExpectedResult, testCaseName, assignedRole))
				{
					fail=true;
					APP_LOGS.debug(testStepName+" Test Step not Created Successfully.");
					comments+=testStepName+" Test Step not Created Successfully(Fail). ";
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfullTestStepCreation");
					closeBrowser();

					throw new SkipException("Test Step not created successfully");
				}
				APP_LOGS.debug(testStepName+" Test Step Created Successfully.");
				comments+=testStepName+" Test Step Created Successfully(Pass). ";
					
				closeBrowser();
	 		    APP_LOGS.debug(" Closed Browser after verifying if Test Pass was existing and creating if not existing ");
				System.out.println(" Closed Browser after verifying if Test Pass was existing and creating if not existing ");
 			
                APP_LOGS.debug("Opening Browser... ");
 				System.out.println("Opening Browser... ");
 				openBrowser();
 				
 				login(test_Manager.get(0).username,test_Manager.get(0).password, "Test Manager");
 				System.out.println("Logged in browser with Test Manager");
 	            APP_LOGS.debug("Logged in browser with Test Manager");
 	            
 				//click on testManagement tab
 				APP_LOGS.debug(" Clicking on Test Management Tab");
 				getElement("UAT_testManagement_Id").click();
 				Thread.sleep(3000);
 			
 				//click on attachment tab
 				APP_LOGS.debug(" Clicking on Attachment Tab");
 				getElement("TM_attachmentTab_Id").click();
 				Thread.sleep(3000);
 				
 				
 				 // Verifying Create New Attachment Validation
 				if(!getObject("Attachments_groupMemberSelected").getAttribute("title").equalsIgnoreCase(GroupName)){
 				       dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
 				}
 				
 				if(!getObject("Attachments_portfolioMemberSelected").getAttribute("title").equalsIgnoreCase(Portfolio)){
 				       dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), Portfolio );
 				}
 				
 				if(!getObject("Attachments_projectMemberSelected").getAttribute("title").equalsIgnoreCase(ProjectName)){
 				       dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
 				}
 				
 				if(!getObject("Attachments_versionMemberSelected").getAttribute("title").equalsIgnoreCase(Version)){
 				      dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
 				}
 				
 				if(!getObject("Attachments_testPassMemberSelected").getAttribute("title").equalsIgnoreCase(testPassName)){
 				      dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
 				}
 							
 				APP_LOGS.debug("Selecting test case:" +testCaseName);
 				testCaseDropdown(getElement("Attachments_testCaseDropDown_Id"), testCaseName);
 							
 				APP_LOGS.debug("Clicking on Create New link");
 				getObject("Attachments_createNew_link").click();
 				
 				//VALIDATION
 					boolean blankNameMessageResult = blankFieldvalidation(expectedAttachmentNameBlankMessage);
 					if(blankNameMessageResult==true){
 						comments+=" Without entering the mandatory fields clicked on save button and correct message was displayed(Pass).";
 					}
 					if(blankNameMessageResult==false){
 						comments+=" Without entering the mandatory fields clicked on save button and correct message was not displayed(Fail).";
 					}
				    APP_LOGS.debug("Clicking on OK Button for clicking save button without entering any data");    
				    System.out.println("Clicking on OK Button for clicking save button without entering any data");
				//   getObject("AttachmentsCreateNew_attachmentSuccessPopUpOkBtn").click();
				    
				//Checking validation after entering attachment name
				   
				    APP_LOGS.debug("Inputing attachment name:" +attachmentName);
					getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(attachmentName);
					
					boolean blankTestStepMessageResult = blankFieldvalidation(expectedTestStepBlankMessage);
 					if(blankTestStepMessageResult==true){
 						comments+= " Clicked on save button after entering attachmnet nameand correct message was displayed(Pass).";
 					}
 					if(blankTestStepMessageResult==false){
 						comments+=" Clicked on save button after entering attachmnet name and correct message was not displayed(Fail).";
 					}
					APP_LOGS.debug("Clicking on OK Button after clicking save button after entering attachment name");  
				    System.out.println("Clicking on OK Button after clicking save button after entering attachment name");
				    
				    
				  //Checking validation  after entering attachment name and test step
					   
					    APP_LOGS.debug("Inputing attachment name:" +attachmentName);
						getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(attachmentName);
						selectTestStep(testStepName);
						
						boolean blankFileFieldMessageResult = blankFieldvalidation(expectedFileNameBlankMessage);
	 					if(blankFileFieldMessageResult==true){
	 						comments+= " Clicked on save button after entering attachmnet name and test step and correct message was displayed(Pass).";
	 					}
	 					if(blankFileFieldMessageResult==false){
	 						comments+=" Clicked on save button after entering attachmnet name and test step and correct message was not displayed(Fail).";
	 					}  
	 					APP_LOGS.debug("Clicking on OK Button after clicking save button after entering attachment name and selecting test step ");  
					    System.out.println("Clicking on OK Button after clicking save button after entering attachment name and selecting test step ");
					    
					    
 				 //creating an attachment 
					     System.out.println("Creating new attachment");
					     APP_LOGS.debug("Creating new attachment");
					     createAttachment(GroupName, Portfolio, ProjectName, Version, testPassName, testCaseName, attachmentName, Description, testStepName, fileName, TestStepExpectedResult, testerRole, expectedResult);
 				
			}
			catch(Throwable t)
            {
         	 fail=true;
           	 t.printStackTrace();
           	 comments="Failed login for Role " +Role;
           	 TestUtil.takeScreenShot(this.getClass().getSimpleName(),this.getClass().getSimpleName());
            }
			APP_LOGS.debug("Closing Browser... ");
		    closeBrowser();	
		}
		else
		{
			isLoginSuccess=false;
			System.out.println("Login was not Successful");
			APP_LOGS.debug("Login was not Successful");
		}
	}
	
	private boolean blankFieldvalidation(String expectedfieldBlankMessage) throws IOException, InterruptedException{
		
		//Clicking on save button after entering attachment name,test step
		getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
		
	    String actualFieldBlankMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
	    boolean result = compareStrings(actualFieldBlankMessage, expectedfieldBlankMessage);
	    
	    //Click on OK button form validation pop up message
	    Thread.sleep(1000);
	    eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
	    
	    return result;
	}
	@AfterMethod
	public void reportDataSetResult(){
		if(skip){
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail){
			isTestPass=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		
		skip=false;
		fail=false;
	
	}
	
	@AfterTest
	public void reportTestResult(){
		
		if(isTestPass)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
	}
	
	@DataProvider
	public Object[][] getTestData(){
		
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName()) ;
	}

	
	//test case selection dropdown
		public int testCaseDropdown (WebElement testCaseDD, String testCaseName)
		{
			testCaseDD.click();
		
			List<WebElement> testCaseElements = testCaseDD.findElements(By.tagName("option"));
		
			for(int i =0 ;i<testCaseElements.size();i++)
			{
		       if(testCaseElements.get(i).getText().equals(testCaseName))
			    {
			       testCaseElements.get(i).click();
			       testCaseFlag++;
			       APP_LOGS.debug( testCaseName + " : is selected...");
			       System.out.println(testCaseName + " : is selected...");
			       break;
		        }			    
			}
			return testCaseFlag;
		}
		
		
		 public int selectTestStep(String TestStep)
		 {
		  List<WebElement> testStepCount = getObject("AttachmentsCreateNew_testStepDetailsBox").findElements(By.tagName("li"));
		  
		     for(int i=1; i<=testStepCount.size(); i++)
		     {
		      WebElement testStepNameXpath = getObject("AttachmentsCreateNew_testStepNameXpath1", "AttachmentsCreateNew_testStepNameXpath2", i);
		      String testStepTitle = testStepNameXpath.getAttribute("title");
		      if(testStepTitle.equalsIgnoreCase(TestStep))
		     
		      {
		       testStepFlag++;
		       getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", i).click();
		       break;
		      }
		     }
		     return testStepFlag;
		 }
		

		//create new attachment
		public void createAttachment(String group, String portfolio, String project, String version, String testPassName, String TestCase, String AttachmentName, 
				String Description, String TestStep, String fileName1, String TestStepExpectedResult, String TesterRole, String AttachmentSavedMessage) throws IOException, InterruptedException
		{
				if(!getObject("Attachments_groupMemberSelected").getAttribute("title").equalsIgnoreCase(group)){
				       dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), group );
				}
				
				if(!getObject("Attachments_portfolioMemberSelected").getAttribute("title").equalsIgnoreCase(portfolio)){
				       dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), portfolio );
				}
				
				if(!getObject("Attachments_projectMemberSelected").getAttribute("title").equalsIgnoreCase(project)){
				       dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), project );
				}
				
				if(!getObject("Attachments_versionMemberSelected").getAttribute("title").equalsIgnoreCase(version)){
				      dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), version );
				}
				
				if(!getObject("Attachments_testPassMemberSelected").getAttribute("title").equalsIgnoreCase(testPassName)){
				      dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), testPassName );
				}
							
				APP_LOGS.debug("Selecting test case:" +TestCase);
				testCaseDropdown(getElement("Attachments_testCaseDropDown_Id"), TestCase);
							
				APP_LOGS.debug("Clicking on Create New link");
				getObject("Attachments_createNew_link").click();
					
				APP_LOGS.debug("Selecting test step:" +TestStep);	
				selectTestStep(TestStep);
			    
			    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
				getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(AttachmentName);
				
				APP_LOGS.debug("Inputing attachment description:" +Description);
				getElement("AttachmentsCreateNew_attachmentDescriptionTextField_Id").sendKeys(Description);
					    
				APP_LOGS.debug("Uploading file:" +fileName1);
			    getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+fileName1);
		    
			    APP_LOGS.debug("Clicking on Save button");

			    getElement("AttachmentsCreateNew_attachmentSaveBtn").click();
			    Thread.sleep(3000);
			    
			    String actual_SaveAttachment_SuccessMessage = eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']")).getText();

				boolean successMessage=compareStrings(actual_SaveAttachment_SuccessMessage, AttachmentSavedMessage);
			    if(successMessage)
			    {
					APP_LOGS.debug(AttachmentName+": Attachment is Saved Successfully stating : " + actual_SaveAttachment_SuccessMessage);
					System.out.println(AttachmentName+": Attachment is Saved Successfully stating : " + actual_SaveAttachment_SuccessMessage);
					Thread.sleep(1000);
					eventfiringdriver.findElement(By.xpath("//div[@id='divAlert']/following-sibling::div[9]/div[1]/button")).click();
					APP_LOGS.debug("Clicked on Ok button");
			    }
			    else
			    {
			    	fail=true;
			    	comments+="Attachment not saved successfully";
			    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "UnsuccessfulAttachmentCreation");			    }
		    
		}
		
}





