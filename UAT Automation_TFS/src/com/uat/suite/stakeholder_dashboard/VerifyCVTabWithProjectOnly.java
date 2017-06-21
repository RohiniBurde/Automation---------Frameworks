/**
 * @author preeti.walde
 * Created: 4th Feb 2015
 * Last Modified: 4th Feb 2015
 * Description: Code to verify only project created is displayed in project section in Consolidated View tab.
 */
package com.uat.suite.stakeholder_dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class VerifyCVTabWithProjectOnly extends TestSuiteBase
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	boolean isLoginSuccess=false;
	String comments;
	String[] displayProjectArray;	
	int fiscalYear;
	DateFormat dateFormat;
	String systemDate;		
	
	// Runmode of test case in a suite	
	@BeforeTest
	public void checkTestSkip()
	{
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		if(!TestUtil.isTestCaseRunnable(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(stakeholderDashboardSuiteXls, this.getClass().getSimpleName());
	}
	
	
	// Test Case Implementation ...		
	@Test(dataProvider="getTestData")
	public void verifyCVTabWithProjectOnly(String Role) throws Exception
	{		
		int month=0;
		int noTestStepAvailableTagSize;
		int projectsPerPage;	 
		
	    ArrayList<String> projectArrayList;
	    ArrayList<String> versionArrayList;
	    
	    String projectNameToBeInArray = null; 
	    String versionNameToBeInArray = null;
	    
		count++;
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");

		}
		
		
		APP_LOGS.debug("Opening Browser...for user "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{	
				try
				{		
										
						if(Role.equalsIgnoreCase("Admin") || Role.equalsIgnoreCase("Version Lead") || Role.equalsIgnoreCase("Test Manager")
		        	 			|| Role.equalsIgnoreCase("Stakeholder+Tester") || Role.equalsIgnoreCase("Admin+Stakeholder"))
		        	 	{
		        	 		//click on Stakeholder dashboard page
							getElement("UAT_stakeholderDashboard_Id").click();
							Thread.sleep(1000);
		        	 	}
						
						getElement("UAT_testManagement_Id").click();
						Thread.sleep(3000);
						
						projectArrayList = new ArrayList<String>();
						versionArrayList = new ArrayList<String>();
						
					    do
					    {
						     projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
						     
						     for (int j = 1; j <= projectsPerPage; j++) 
						     {    
						    	 //select the project
							      getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", j).click();
							     
							      //go to test pass page
							      getElement("TM_testPassesTab_Id").click(); 							      
							      Thread.sleep(1500);
							        
							      //verify if project contains test passes or not, if not ad it to array list
							      if (getElement("TestPassViewAll_NoTestPassAvailable_Id").getText().contains("No")) 
							      {	
							    	  projectNameToBeInArray = getObject("TestSteps_projectTitleSelected").getAttribute("title");
							    	  versionNameToBeInArray = getObject("TestSteps_versionTitleSelected").getAttribute("title");
						        	  
							    	  projectArrayList.add(projectNameToBeInArray);	
							    	  versionArrayList.add(versionNameToBeInArray);
						        	  
							    	  break;										        					         
							      }								        
								       					      									     
							      getElement("TM_projectsTab_Id").click();									      
							      Thread.sleep(2500);  
						     }   
					     
					    } while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
					    
						
						//click on Stakeholder dashboard page
						getElement("UAT_stakeholderDashboard_Id").click();
						Thread.sleep(1000);						
						
						//verify default selection in project status dropdown and other options available like All, Active, Completed
						Select projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
						
						if (assertTrue(getElement("StakeholderDashboard_projectStatusDropDown_Id").isDisplayed())) 
						{
							 String selectedProjectStatus = projectStatusDD.getFirstSelectedOption().getText();
							 
							 if (compareStrings("Active", selectedProjectStatus)) 
							 {
								 APP_LOGS.debug("'Active' is selected as default in 'Project Status' dropdown");						 
								 comments += "\n 'Active' is selected as default in 'Project Status' dropdown(Pass).";
								  	
							 }
							 else 
							 {
								fail=true;							
								comments += "\n 'Active' is NOT selected as default in 'Project Status' dropdown.(Fail) ";						
								APP_LOGS.debug("'Active' is NOT selected as default in 'Project Status' dropdown. Test case failed");						
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Active' option is not displayed selected by default in Project Status dropdown");
							 }
						}
						else 
						{
								fail=true;					
								comments += "\n 'Project Status' dropdown is not displayed on Consolidated View tab.(Fail) ";					
								APP_LOGS.debug("'Project Status' dropdown is not displayed on Consolidated View tab. Test case failed");					
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Project Status' dropdown not visible");
						}
					
						//verify default selection in Select Date dropdown and its available options
						Select selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));	
						
						if (assertTrue(getElement("StakeholderDashboard_selectDateDropDown_Id").isDisplayed())) 
						{
							String defaultSelectedDate = selectDateDD.getFirstSelectedOption().getText();
							
							if (compareStrings("Today", defaultSelectedDate)) 
							 {
								 APP_LOGS.debug("'Today' is selected as default in 'Select Date' dropdown.");							 
								 comments += "\n 'Today' is selected as default in 'Select Date' dropdown.(Pass)";							 
							 }
							 else 
							 {
								fail=true;								
								comments += "\n 'Today' is NOT selected as default in 'Select Date' dropdown.(Fail) ";							
								APP_LOGS.debug("'Today' is NOT selected as default in 'Select Date' dropdown. Test case failed");							
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Today' option is not displayed selected by default in Select Date dropdown");
							 }
						}
						else 
						{
								fail=true;					
								comments += "\n 'Select Date' dropdown is not displayed on Consolidated View tab.(Fail) ";					
								APP_LOGS.debug("'Select Date' dropdown is not displayed on Consolidated View tab. Test case failed");					
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Select Date' dropdown not visible");
						}
					
						//verify default selection in Fiscal Year dropdown and its available options like FY14, FY15
						Select selectFiscalYearDD = new Select(getElement("StakeholderDashboard_fiscalYearDropDown_Id"));
						
						if (assertTrue(getElement("StakeholderDashboard_fiscalYearDropDown_Id").isDisplayed())) 
						{
								String defaultSelectedFiscalYear = selectFiscalYearDD.getFirstSelectedOption().getText();
													
								dateFormat = new SimpleDateFormat("MM/dd/yyyy");					
								Date date = new Date();					
								systemDate = dateFormat.format(date);
								
								month = Integer.parseInt(systemDate.substring(0, 2));
							
								if(month >= 7)		
								{				
									fiscalYear = Integer.parseInt(systemDate.substring(8))+1;
									
									if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
									{
										APP_LOGS.debug("Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default. ");								
										comments += "\n Current fiscal year "+fiscalYear+" is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
									}
									else 
									{
										fail=true;								
										comments += "\n Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
										APP_LOGS.debug("Current fiscal year "+fiscalYear+" is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year "+fiscalYear+" is not displayed selected in 'Fiscal Year' dropdown");
									}
								}
								else
								{
									fiscalYear = Integer.parseInt(systemDate.substring(8));
									
									if (compareIntegers(fiscalYear, Integer.parseInt(defaultSelectedFiscalYear.substring(3)))) 
									{
										APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default. ");								
										comments += "\n Current fiscal year '"+fiscalYear+"' is displayed in 'Fiscal Year' dropdown as default.(Pass) ";
									}
									else 
									{
										fail=true;								
										comments += "\n Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default.(Fail) ";								
										APP_LOGS.debug("Current fiscal year '"+fiscalYear+"' is NOT displayed in 'Fiscal Year' dropdown as default. Test case failed");								
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "current fiscal year "+fiscalYear+" is not displayed selected in 'Fiscal Year' dropdown");
									}
								}
						}
						else 
						{
								fail=true;						
								comments += "\n 'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page.(Fail) ";						
								APP_LOGS.debug("'Fiscal Year' dropdown is not displayed on Stakeholder Dashboard Page. Test case failed");						
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Fiscal Year' dropdown not displayed");
						}
					
						//verify quarter checkboxes visible or not and current quarter is selected by default or not
						if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isDisplayed() 
								&& getElement("StakeholderDashboard_Q2Checkbox_Id").isDisplayed() 
								&& getElement("StakeholderDashboard_Q3Checkbox_Id").isDisplayed() 
								&& getElement("StakeholderDashboard_Q4Checkbox_Id").isDisplayed())) 
							{
							
									if(month>=7 && month<= 9)
									{
										if (assertTrue(getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected())) 
										{
											APP_LOGS.debug("Current Quarter 'Q1' is selected as default. ");								
											comments += "\n Current Quarter 'Q1' is selected as default.(Pass)";
										}
										else 
										{
											fail=true;								
											comments += "\n Current Quarter 'Q1' is Not selected as default.(Fail) ";								
											APP_LOGS.debug("Current Quarter 'Q1' is Not selected as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q1' is Not selected as default.");
										}
									}
									else if(month>=10 && month<= 12)
									{
										if (assertTrue(getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected())) 
										{
											APP_LOGS.debug("Current Quarter 'Q2' is selected as default. ");								
											comments += "\n Current Quarter 'Q2' is selected as default.(Pass)";
										}
										else 
										{
											fail=true;								
											comments += "\n Current Quarter 'Q2' is Not selected as default.(Fail) ";								
											APP_LOGS.debug("Current Quarter 'Q2' is Not selected as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q2' is Not selected as default.");
										}
									}
									else if(month>=1 && month<= 3)
									{
										if (assertTrue(getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected())) 
										{
											APP_LOGS.debug("Current Quarter 'Q3' is selected as default. ");								
											comments += "\n Current Quarter 'Q3' is selected as default.";
										}
										else 
										{
											fail=true;								
											comments += "\n Current Quarter 'Q3' is Not selected as default.(Fail) ";								
											APP_LOGS.debug("Current Quarter 'Q3' is Not selected as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter 'Q3' is Not selected as default.");
										}
									}
									else
									{
										if (assertTrue(getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected())) 
										{
											APP_LOGS.debug("Current Quarter 'Q4' is selected as default. ");								
											comments += "\n Current Quarter 'Q4' is selected as default.(Pass) ";
										}
										else 
										{
											fail=true;								
											comments += "\n Current Quarter 'Q4' is Not selected as default.(Fail) ";								
											APP_LOGS.debug("Current Quarter 'Q4' is Not selected as default. Test case failed");								
											TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Current Quarter is Not selected as default.");
										}
									}						
						}
						else 
						{
								fail=true;						
								comments += "\n Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page.(Fail) ";						
								APP_LOGS.debug("Any/All Quarter checkboxes are not displayed on Stakeholder Dashboard Page. Test case failed");						
								TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkboxes not available");
						}						
						
						int numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
						
						//if no project available for selected default criteria
						if(numOfProjectPresentInGrid==0)
						{
								noTestStepAvailableTagSize = getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").findElements(By.xpath("div/b")).size();
								
								if(noTestStepAvailableTagSize==1)
								{
									if(compareStrings(resourceFileConversion.getProperty("StakeholderDashboardCV_noTestStepAvailableMsg"), getElement("StakeholderDashboardConsolidatedView_statusPieChart_Id").getText()))
									{
										APP_LOGS.debug("Verified 'No Test Steps Available!' message");
										comments+="Verified 'No Test Steps Available!' message.(Pass) ";
									}
									else
									{
										fail=true;
										APP_LOGS.debug("'No Test Step Available' message not shown");
										comments+="'No Test Step Available' message not shown.(Fail) ";
										TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'No Test Steps Available' message not shown");
									}
								}
								
								//verify if pie chart displays
								long pieChart = (long)eventfiringdriver.executeScript("return $('#pieChartDet').find('div svg').size();");
								int pieChartCount = (int)pieChart;
								
								if(pieChartCount!=1)
								{									
									APP_LOGS.debug("Pie Chart not visible as no project available");
									comments+="Pie Chart not visible as no project available.(Pass) ";
								}
								else
								{
									fail=true;
									APP_LOGS.debug("Pie Chart not visible though no project available.");
									comments+="Pie Chart not visible though no project available.(Fail) ";
									assertTrue(false);
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Pie Chart Visible though no project available");
								}
								
						}
						else
						{
							displayProjectArray = new String[6];
							
							try
							{							
									do
									{	
										numOfProjectPresentInGrid = getElement("StakeholderDashboardConsolidatedView_projectGridTbody_Id").findElements(By.xpath("tr")).size();
										
										for (int x = 1; x <= numOfProjectPresentInGrid; x++) 
										{   						
												int grpTDSize = driver.findElements(By.xpath("//table[@id='tblProj']/tbody/tr["+x+"]/td")).size();
												int i=1;
																						
												if(grpTDSize==6)
												{
													displayProjectArray[0]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
												}
												displayProjectArray[1]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
												displayProjectArray[2]= getObject("StakeholderDashboardConsolidatedView_projectNameWithLinkXpath1", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath2", "StakeholderDashboardConsolidatedView_projectNameWithLinkXpath3", x, i++).getAttribute("title");
												displayProjectArray[3]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getAttribute("title");
												displayProjectArray[4]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
												displayProjectArray[5]= getObject("StakeholderDashboardConsolidatedView_projectGridXPath1", "StakeholderDashboardConsolidatedView_projectGridXPath2", "StakeholderDashboardConsolidatedView_projectGridXPath3", x, i++).getText();
												
												//verify projects displayed in CV tab
												if(compareProjects(projectArrayList, versionArrayList, displayProjectArray))	
												{
													APP_LOGS.debug("Project '"+projectArrayList.get(0)+"' having no test pass not showing in the grid");
												}
												else
												{
													fail=true;
													comments += "\n Project '"+projectArrayList.get(0)+"' having no test pass showing in the grid.(Fail) ";						
													APP_LOGS.debug("Project '"+projectArrayList.get(0)+"' having no test pass showing in the grid. Test case failed");
													assertTrue(false);
												}
										}
										
									} while (ifElementIsClickableThenClick("StakeholderDashboardConsolidatedView_NextLink"));
									
							}
							catch(Throwable t)
							{
								t.printStackTrace();
								fail=true;
							}
						
						}
						
						//release memory
						displayProjectArray=null;
						projectArrayList.clear();
						versionArrayList.clear();
						
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					fail=true;
					APP_LOGS.debug("Exception occured");
					assertTrue(false);
				}
				
				closeBrowser();
		}
		else 
		{
				APP_LOGS.debug("Login Unsuccessfull for the user with role '"+ Role+"'.");
				comments+="Login Unsuccessfull for the user with role '"+ Role+"'.";
		}	
	}
	
	
	
	
	//comparing projects 
	private boolean compareProjects(ArrayList<String> projectArrayList, ArrayList<String> versionArrayList, String[] displayProject)
	{		
		for (int i = 0; i < projectArrayList.size(); i++) 
		{
			if (displayProject[2].equals(projectArrayList.get(0))
				&& displayProject[3].equals(versionArrayList.get(0))) 
			{
				return false;
			} 			
		}		
		return true;		
	}
	
	
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(!isLoginSuccess){
			isTestPass=false;
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessfull");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else
		{
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(stakeholderDashboardSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		skip=false;
		fail=false;
		
	}
	
	
	@AfterTest
	public void reportTestResult()
	{
		if(isTestPass)
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, "Test Cases", TestUtil.getRowNum(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(stakeholderDashboardSuiteXls, "Test Cases", TestUtil.getRowNum(stakeholderDashboardSuiteXls,this.getClass().getSimpleName()), "FAIL");
	
	}
	
	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(stakeholderDashboardSuiteXls, this.getClass().getSimpleName()) ;
	}
}