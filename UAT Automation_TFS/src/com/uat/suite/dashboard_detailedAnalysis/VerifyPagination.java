package com.uat.suite.dashboard_detailedAnalysis;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import recorder.Utility;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyPagination extends TestSuiteBase
{
	String runmodes[]=null;
	static boolean fail=false;
	static boolean skip=false;
	static boolean isTestPass=true;
	static int count=-1;
	static WebDriverWait wait;
	static boolean isLoginSuccess=false;
	String comments;
	int totalPages;
	
	ArrayList<Credentials> tester;
	ArrayList<Credentials> versionLead;
	//ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	
	Select selectProject;
	Select selectVersion;
	Select selectTestPass;
	Select testerDropDown;
	String totalNumOfProject;
	String barChartSrcText;
	String[] testernames;
	int numOfBarChart;
	
	String noTesterAvailabelText;
	Utility utilRecorder = new Utility();
	
	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(DetailedAnalysisXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(DetailedAnalysisXls, this.getClass().getSimpleName());
		
		utilRecorder.startRecording(System.getProperty("user.dir")+"\\Videos\\"+extractPackageName(this.getClass().getPackage().toString())+"\\"+this.getClass().getSimpleName());

	}

	@Test(dataProvider="getTestData")
	public void testVerifyAllDropDowns(String Role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead,String TestPassName1,String TP_TestManager,
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String tester_testerName,String tester_Role1, 
			String TestCaseName1,String TestStepName1,String TestStepExpectedResult,String AssignedRole1,String expectedNoTesterText,
			String expectedNoTestCaseText) throws Exception
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
		
		int testManager_count = Integer.parseInt(TP_TestManager);
		testManager = getUsers("Test Manager", testManager_count);
		
		openBrowser();
		
		APP_LOGS.debug("Opening Browser... ");
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			if(tester==null)
			{
				skip=true;
				
				APP_LOGS.debug("Required number of testers are not available...Skipping the Test case");
				
				comments+="Required number of testers are not available...Skipping the Test case";
				
				closeBrowser();
				
				throw new SkipException("Required number of testers are not available...Skipping the Test case");
			}
			else
			{
				try 
				{ 
					getElement("UAT_testManagement_Id").click();
					
					APP_LOGS.debug("Clicking On Test Management Tab ");
				
					Thread.sleep(3000);
				
					//Create Project
					if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
		 			{
		 				fail=true;
		 				
		 				APP_LOGS.debug(ProjectName+" not created successfully. ");
		 				
		 				comments= comments+"Fail Occurred:- " +ProjectName+" not created successfully. ";
		 				
		 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Project Creation Unsuccessful");
						
		 				throw new SkipException("Project is not created successfully ... So Skipping all tests");
		 			}
					else 
					{
						APP_LOGS.debug(ProjectName+" created successfully. ");
					}
					
					try 
					{
						APP_LOGS.debug("===Goto Detailed analysis page (After Creating Project) and verify 'There are noTester' message is displayed===");
						
						getElement("UAT_dashboard_Id").click();
						
						Thread.sleep(520);
						
//		 				getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
						
						getObject("Dashboard_detailedAnalysis").click();
		 				
		 				APP_LOGS.debug("Select Project.");
							
					/*	selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
						
						selectProject.selectByVisibleText(ProjectName);
						Thread.sleep(520);
					*/	
		 				List<WebElement> listOfProjectsName1=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

		 				selectDDContent(listOfProjectsName1, ProjectName, "Project");
						
						noTesterAvailabelText = getObject("DetailedAnalysis_noTestCaseAvailableText").getText();
						
						if (compareStrings(expectedNoTesterText, noTesterAvailabelText)) 
						{
							APP_LOGS.debug("On Detailed analysis page 'There are noTester' message is displayed(After Creating Project)");
							
							comments += "'There are noTester' message is displayed on Detailed analysis page (After Creating Project)...";
						}
						else 
						{
							fail=true;
			              	
							APP_LOGS.debug("'There are noTester' message is not displayed(After Creating Project). Test case has been failed.");
			              	
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'There are noTester' message is not displayed");
							
							comments += "Fail Occurred:- 'There are noTester' message is NOT displayed on Detailed analysis page (After Creating Project)...";
						}
					} 
					catch (Throwable t) 
					{
						fail=true;
						
						t.printStackTrace();
						
						APP_LOGS.debug("'There are noTester' message is Not displayed on Detailed analysis page (After Creating Project)");
						
						comments += "Exception Occurred: 'There are noTester' message is Not displayed on Detailed analysis page (After Creating Project)...";
					}
					
					//---------------------------------------------------------------------------------------
					
					getElement("UAT_testManagement_Id").click();
					
					Thread.sleep(2000);
					
					//create test pass
					if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TP_EndMonth, TP_EndYear, TP_EndDate, testManager.get(0).username))
	            	{
	        		     fail=true;
	        		    
	        		     APP_LOGS.debug(TestPassName1+" not created successfully. ");
	                   	 
	        		     comments+="Fail Occurred:- "+TestPassName1+" Not created successfully. ";
						 
	        		     TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Pass Creation Unsuccessful");
	    					 
	           			 throw new SkipException("Test Pass1 is not created successfully... So Skipping all tests");
	            	}
	            	else 
	 				{
	            		 APP_LOGS.debug(TestPassName1+" created successfully. ");
		 			}
					
	          		APP_LOGS.debug("===Goto Detailed analysis page (After Creating Test Pass) and verify 'There are noTester' message is displayed===");
		           	
		           	APP_LOGS.debug("Closing the browser after saving Test Passes.");
					
					closeBrowser();
					
					APP_LOGS.debug("Opening the browser after saving Test Passes.");
					
					openBrowser();
					
					if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
					{
						try 
			           	{
							//getElement("UAT_dashboard_Id").click();
							
			 			//	getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
			 			  
			 			    getObject("Dashboard_detailedAnalysis").click();
			 				
			 				APP_LOGS.debug("Select Project.");
								
					/*		selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
							
							selectProject.selectByVisibleText(ProjectName);
							
							Thread.sleep(520);
					*/
			 				List<WebElement> listOfProjectsName1=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

			 				selectDDContent(listOfProjectsName1, ProjectName, "Project");
			 				
							noTesterAvailabelText = getObject("DetailedAnalysis_noTestCaseAvailableText").getText();
							
							if (compareStrings(expectedNoTesterText, noTesterAvailabelText)) 
							{
								APP_LOGS.debug("On Detailed analysis page 'There are noTester' message is displayed");
								
								comments += "'There are noTester' message is displayed on Detailed analysis page (After Creating Test Pass)...";
							}
							else 
							{
								fail=true;
				              	
								APP_LOGS.debug("'There are noTester' message is not displayed. Test case has been failed.");
				              	
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'There are noTester' message is not displayed");
								
								comments += "Fail Occurred:- 'There are noTester' message is NOT displayed on Detailed analysis page (After Creating Test Pass)...";
							}
			           	}	
						catch (Throwable t) 
						{
							fail=true;
							
							t.printStackTrace();
							
							APP_LOGS.debug("'There are noTester' message is Not displayed on Detailed analysis page (After Creating Test Pass). Test case has been failed.");
							
							comments += "Exception Occurred: 'There are noTester' message is Not displayed on Detailed analysis page (After Creating Test Pass)...";
						}
		           
		           //---------------------------------------------------------
		           	 
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(4000);
						
			            //create Tester
					    if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName1, tester.get(0).username, tester_Role1, tester_Role1))
					    {
							 fail=true;
							 
							 APP_LOGS.debug(tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1);
							
							 comments+="Fail Occurred:- "+tester.get(0).username+ "Tester not ceated successfully for test pass "+TestPassName1+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), " Tester Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Tester is not created successfully For Test Pass1 ... So Skipping all tests");
						}
						else 
						{
							APP_LOGS.debug(tester.get(0).username+ "Tester created successfully for test pass "+TestPassName1);
						}
						
						try 
						{
							APP_LOGS.debug("===Goto Detailed analysis page and verify 'No Test Case is Available for the Tester(s)' message is displayed===");
							
							getElement("UAT_dashboard_Id").click();
							Thread.sleep(520);
							
			 		//		getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
							
							getObject("Dashboard_detailedAnalysis").click();
			 				
			 				APP_LOGS.debug("Select Project.");
								
			/*				selectProject = new Select(getElement("DetailedAnalysis_projectDropDown_Id"));
							
							selectProject.selectByVisibleText(ProjectName);
							
							Thread.sleep(520);
			*/
			 				List<WebElement> listOfProjectsName1=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

			 				selectDDContent(listOfProjectsName1, ProjectName, "Project");
			 				
							noTesterAvailabelText = getObject("DetailedAnalysis_noTestCaseAvailableText").getText();
							
							if (compareStrings(expectedNoTestCaseText, noTesterAvailabelText)) 
							{
								APP_LOGS.debug("On Detailed analysis page 'No Test Case is Available for the Tester(s)' message is displayed");
								
								comments += "'No Test Case is Available for the Tester(s)' message is displayed on Detailed analysis page(Before Creating Test Case)...";
							}
							else 
							{
								fail=true;
				              	
								APP_LOGS.debug("'No Test Case is Available for the Tester(s)' message is not displayed. Test case has been failed.");
				              	
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "No Test Case is Available for the Tester(s) message is not displayed");
								
								comments += "Fail Occurred:- 'No Test Case is Available for the Tester(s)' message is NOT displayed on Detailed analysis page (Before Creating Test Case)...";
			                  	 
							}
						}
						catch (Throwable t) 
						{
							fail=true;
							
							t.printStackTrace();
							
							APP_LOGS.debug("'No Test Case is Available for the Tester(s)' message is Not displayed on Detailed analysis page(Before Creating Test Case). Test case has been failed.");
							
							comments += "Exception Occurred: 'No Test Case is Available for the Tester(s)' message is Not displayed on Detailed analysis page(Before Creating Test Case)...";
						}
						
						//---------------------------------------------------------
						
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(2000);
						
						//create createTestCase 1
						if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName1, TestCaseName1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1);
							
							 comments+="Fail Occurred:- "+TestCaseName1+ "Test Case not created successfully for test pass "+TestPassName1+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Case Creation Unsuccessful");
		 					 
		        			 throw new SkipException("Test Case1 is not created successfully for test pass 1... So Skipping all ");
						}
						else 
						{
							APP_LOGS.debug(TestCaseName1+ "Test Case created successfully for test pass "+TestPassName1);
						}
						
						
						//create Test step1
						
						if(!createTestStep(GroupName,PortfolioName,ProjectName,Version,TestPassName1,TestStepName1,TestStepExpectedResult,
								TestCaseName1, AssignedRole1))
						{
							 fail=true;
							
							 APP_LOGS.debug(TestStepName1+ "Test Step not created successfully under Test Case "+TestCaseName1+ " for test pass "+TestPassName1);
							
							 comments+="Fail Occurred:- "+TestStepName1+ "Test Step not created successfully under Test Case "+TestCaseName1+ " for test pass "+TestPassName1+". ";
		 					
							 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Test Step Creation Unsuccessful");
		 					 
		        			 throw new SkipException(TestStepName1+" is not created successfully ... So Skipping all tests");
						}
						else 
						{
							APP_LOGS.debug(TestStepName1+ "Test Step created successfully under Test Case "+TestCaseName1+ " for test pass "+TestPassName1+". ");
						}
						
						APP_LOGS.debug("Data has been made Successfully from Test Management tab.");
						
						//comments+="Data has been Created Successfully from Test Management tab... ";
				//------------------------------------------------------------------------------------------------------------		
			
						APP_LOGS.debug("======Verifying bar charts are displayed after creating Test case and test step======");
						
						try 
						{
							getElement("UAT_dashboard_Id").click();
							Thread.sleep(520);
							
			 			//	getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
							
							getObject("Dashboard_detailedAnalysis").click();
			 				
			 				APP_LOGS.debug("Select Project.");
								
			 				List<WebElement> listOfProjectsName=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

			 				selectDDContent(listOfProjectsName, ProjectName, "Project");
			 				
							Thread.sleep(520);
							
							barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
							
							barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("chxl=1:")+8, barChartSrcText.indexOf("&chxt"));
							
							testernames = barChartSrcText.split("\\|");
							
							numOfBarChart = testernames.length;
							
							if (compareIntegers(1, numOfBarChart)) 
							{
								APP_LOGS.debug("One Bar chart is displayed as expected.");
								
								comments += "One Bar chart is displayed as expected....";
								
							}
							else 
							{
								fail = true;
								
								APP_LOGS.debug("One Bar chart is Not displayed as expected.... Test case failed");
								
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "One Bar chart is Not displayed on DA page");
								
								comments += "Fail Occurred: One Bar chart is Not displayed as expected...";
							}
							
						} 
						catch (Throwable t) 
						{
							fail=true;
							
							t.printStackTrace();
							
							APP_LOGS.debug("'One Bar chart is Not displayed as expected. Test case has been failed.");
							
							comments += "Exception Occurred: One Bar chart is Not displayed as expected...";
						}
					
						//--------------------------------------------------------------------------------
						
						APP_LOGS.debug("=====Add More 14 testers for verifying Next Button Disable=====");
						
						try 
						{
							getElement("UAT_testManagement_Id").click();
							
							Thread.sleep(2000);
							
							APP_LOGS.debug("Click on Tester Tab");
							
							getElement("TesterNavigation_Id").click();
							Thread.sleep(1000);
							
							APP_LOGS.debug("verifying headers");
							
							dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
							
							dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
							
							dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
							
							dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
							
							dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName1 );
							
							APP_LOGS.debug("Done verifying headers");
							
							for (int i = 1; i <= 14; i++) 
							{
								Thread.sleep(3000);
								
								APP_LOGS.debug("Click on Add Tester link");
								
								getElement("Tester_addTesterLink_Id").click();
								
								Thread.sleep(500);
								
								enterTesterName(tester.get(i).username);
								
								getElement("TesterDetailsAddTester_standardRoleCheckBox_Id").click();
								
								getObject("TesterDetailsAddTester_SaveBtn").click();
								
							}
						}
						catch (Throwable t) 
						{
							fail=true;
			 				
			 				APP_LOGS.debug("Exception Occurred while adding More 14 testers. Test Case failed. ");
			 				
			 				comments= comments+"Exception Occurred:-  while adding More 14 testers... ";
			 				
			 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "More 14 tester Creation Unsuccessful");
							
			 				throw new SkipException("14 Tetster are not created successfully ... So Skipping all tests");
						}
						
						//----------------------------------------------------------
						APP_LOGS.debug("====Verifying added Bar charts are displayed after adding 14 testers and verify Next button is disabled======");
						
						Thread.sleep(3000);
						
						getElement("UAT_dashboard_Id").click();
						
						Thread.sleep(520);
						
				//		getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
						
						getObject("Dashboard_detailedAnalysis").click();
		 				
		 				APP_LOGS.debug("Select Project.");
							
		 				List<WebElement> listOfProjectsName=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

		 				selectDDContent(listOfProjectsName, ProjectName, "Project");
		 				
						barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
						Thread.sleep(200);
						
						barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("chxl=1:")+8, barChartSrcText.indexOf("&chxt"));
						Thread.sleep(200);
						
						testernames = barChartSrcText.split("\\|");
						
						numOfBarChart = testernames.length;
						
						APP_LOGS.debug("Total Tester Bar charts are "+numOfBarChart+", present on First Page.");
						Thread.sleep(200);
						
						if (getElement("DetailedAnalysis_pagination_Id").findElements(By.xpath("div/span")).size()==3) 
						{
							APP_LOGS.debug("Only 1 page(for Bar Chart) available on Detailed Analysis page.");
							
							APP_LOGS.debug("Next button has not been activated");
						}
						else 
						{
							fail=true;
			 				
			 				APP_LOGS.debug("More than 1 page available on Detailed Analysis Page(for 15 testers). Test Case failed. ");
			 				
			 				comments= comments+"Fail occurred: More than 1 page available on Detailed Analysis Page(for 15 testers).";
			 				
			 				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pagination activated for 15 Testers");
							
			 				throw new SkipException("More than 1 page available on Detailed Analysis Page(for 15 testers).... So Skipping all tests");
						}	
						
						//-------------------------------------------------------
						
						APP_LOGS.debug("=====Add One more tester for verifying Next Button Enable=====");
						
						getElement("UAT_testManagement_Id").click();
						
						Thread.sleep(3000);
						
						APP_LOGS.debug("click on Tester Tab");
						
						getElement("TesterNavigation_Id").click();
						
						Thread.sleep(1000);
						
						APP_LOGS.debug("verifying headers");
						
						dropDownSelect(getElement("TesterUpperRibbon_groupDropDown_Id"), getElement("TesterUpperRibbon_GroupList_Id"), GroupName );
						
						dropDownSelect(getElement("TesterUpperRibbon_PortfolioDropDown_Id"), getElement("TesterUpperRibbon_PortfolioList_Id"), PortfolioName );
						
						dropDownSelect(getElement("TesterUpperRibbon_projectDropDown_Id"), getElement("TesterUpperRibbon_ProjectList_Id"), ProjectName );
						
						dropDownSelect(getElement("TesterUpperRibbon_versionDropDown_Id"), getElement("TesterUpperRibbon_VersionList_Id"), Version );
						
						dropDownSelect(getElement("TesterUpperRibbon_testPassDropDown_Id"), getElement("TesterUpperRibbon_TestPassList_Id"), TestPassName1 );
						
						APP_LOGS.debug("Done verifying headers");
						
						APP_LOGS.debug("Click on Add Tester link");
						
						getElement("Tester_addTesterLink_Id").click();
						
						enterTesterName(tester.get(15).username);
						
						getElement("TesterDetailsAddTester_standardRoleCheckBox_Id").click();
						
						getObject("TesterDetailsAddTester_SaveBtn").click();
				
						Thread.sleep(3000);
							
						APP_LOGS.debug("====Verifying whether pagination has been activated or not====");
						
						getElement("UAT_dashboard_Id").click();
						Thread.sleep(520);
						
		 	//			getElement("DashboardTestingStatus_detailedAnalysisButton_Id").click();
						
						getObject("Dashboard_detailedAnalysis").click();
		 				
		 				List<WebElement> listOfProjectsName1=getElement("DetailedAnalysis_projectDropDown_Id").findElements(By.tagName("option"));

		 				selectDDContent(listOfProjectsName1, ProjectName, "Project");
		 				
						barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
						Thread.sleep(200);
						
						barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("chxl=1:")+8, barChartSrcText.indexOf("&chxt"));
						
						Thread.sleep(200);
						
						testernames = barChartSrcText.split("\\|");
						
						numOfBarChart = testernames.length;
						
						Thread.sleep(200);
						
						if (getElement("DetailedAnalysis_pagination_Id").findElements(By.xpath("div/span")).size()==3) 
						{
							fail = true;
							
							assertTrue(false);
							
							APP_LOGS.debug("Only 1 page(for Bar Chart) available on Detailed Analysis page(For 16 testers). Test Case has been Failed.");
							
							APP_LOGS.debug("Next button has not been activated(For 16 testers). Test Case has been Failed.");
							
							comments += "Fail Occurred: Pagination has not been activated on 16th Bar chart....";
						}
						else 
						{
							APP_LOGS.debug("More than 1 page available on Detailed Analysis Page. Pagination has been activated");
							
							getObject("DetailedAnalysis_nextButton").click();
							
							barChartSrcText = getObject("DetailedAnalysis_testerDetailsBarChart").getAttribute("src");
							Thread.sleep(200);
							
							barChartSrcText = barChartSrcText.substring(barChartSrcText.indexOf("chxl=1:")+8, barChartSrcText.indexOf("&chxt"));
							
							Thread.sleep(200);
							
							testernames = barChartSrcText.split("\\|");
							
							numOfBarChart = testernames.length;
							
							if (numOfBarChart>0) 
							{
								APP_LOGS.debug("Bar chart is been displayed on Tester Details on Next page.");
								
								comments += "Pagination has been activated for 16 testers and One Bar chart is displayed on next page....";
							}
							else
							{
								fail = true;
								
								assertTrue(false);
								
								APP_LOGS.debug("Bar chart is not been displayed on Tester Deatils on Next page. Test Case has been Failed.");
								
								comments += "Fail Occurred: Pagination has not been activated on 16th Bar chart....";
							}
						}
					}
					else 
					{
						fail=true;
						
						APP_LOGS.debug("Login Unsuccessful for Test Manager "+testManager.get(0).username);				
						
						comments += "Login Unsuccessful for Test Manager "+testManager.get(0).username;
						
						assertTrue(false);	
					}	
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
					
					fail=true;
				
					comments += "Exception Occurred :-Skip or Any other exception has Occurred.";
				
					assertTrue(false);	
					
					APP_LOGS.debug("Skip or Any other exception has Occurred. Test Case has been Failed");	
				}
			}
			
			
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);				
			comments += "Fail Occurred :- Login Unsuccessful for user "+Role;
		}
	}	
	
	private void selectDDContent(List<WebElement> listOfElements, String selectOption, String DDName)
	{
		for(int i=0;i<listOfElements.size();i++){
			String actualOption=listOfElements.get(i).getAttribute("title");
			if(actualOption.equalsIgnoreCase(selectOption)){
				listOfElements.get(i).click();
				APP_LOGS.debug(DDName+" is selected");
			}
		}	
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(DetailedAnalysisXls, this.getClass().getSimpleName()) ;
	}
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(DetailedAnalysisXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
	}

	@AfterTest
	public void reportTestResult() throws Exception
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(DetailedAnalysisXls, "Test Cases", TestUtil.getRowNum(DetailedAnalysisXls,this.getClass().getSimpleName()), "FAIL");
		utilRecorder.stopRecording();
	}
	
}
