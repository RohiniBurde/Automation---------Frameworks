package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)

public class VerifyDVSelectDateDropdown extends TestSuiteBase 
{

	String runmodes[]=null;
	boolean fail=false;
	boolean skip=false;
	boolean isTestPass=true;
	int count=-1;
	WebDriverWait wait;
	boolean isLoginSuccess=false;
	String comments;	
	
	Select selectDateDD;
	Select selectGroup ;
	Select selectPortfolio;
	Select projectStatusDD;
	Select fiscalYearDD;
	
	int rows;
	int cols;
	static String[ ][ ] projectDetails;
	
	// Runmode of test case in a suite
	@BeforeTest
	public void checkTestSkip()
	{
		
		APP_LOGS.debug(" Executing Test Case -> "+this.getClass().getSimpleName());
		
		System.out.println(" Executing Test Case -> "+this.getClass().getSimpleName());		
		
		if(!TestUtil.isTestCaseRunnable(SD_detailedViewXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(SD_detailedViewXls, this.getClass().getSimpleName());
	}

	
	@Test(dataProvider="getTestData")
	public void testVerifyDVSelectDateDropdown(String Role,String fromMonth,String fromYear,String fromDate,String toMonth,String toYear,
									          String toDate ) throws Exception
	{
		
		count++;		
	
		comments = "";
		
		if(!runmodes[count].equalsIgnoreCase("Y"))
		{
			skip=true;
			APP_LOGS.debug("Runmode for test set data no. "+(count+1)+" set to no.........So its skipping Test Set Data No."+(count+1));;

			throw new SkipException("Runmode for test set data no. "+(count+1)+" set to no");
		}
		
		APP_LOGS.debug("Opening Browser... for role "+Role);
		
		openBrowser();
		
		isLoginSuccess = login(Role);
		
		if(isLoginSuccess)
		{
			try
			{
				APP_LOGS.debug("Clicking On Test Management Tab ");
				
				getElement("UAT_testManagement_Id").click();
				
				Thread.sleep(3000);
				
				String totalNumOfProject = getElement("Deletion_Project_TotalRecords").getText();
				
				int projectsPerPage;
			
				int totalNumOfProjectInt = Integer.parseInt(totalNumOfProject);
				
				rows = totalNumOfProjectInt;// total records
				cols = 7; // hardcoaded	
				
				projectDetails = new String[rows][cols];
				
				ArrayList<String> testPassArrayList = new ArrayList<String>();
				
				int projectDetailsRowIndex = 0;	
				
				do
				{
					
					projectsPerPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
					
					for (int j = 1; j <= projectsPerPage; j++) 
					{						
						
						getObject("ProjectViewAll_editProjectIcon1", "ProjectViewAll_editProjectIcon2", j).click();
						
						String groupName = getElement("ProjectCreateNew_groupDropDown_Id").getAttribute("value");
						
						String portfolioName = getElement("ProjectCreateNew_PortfolioDropDown_Id").getAttribute("value");
						
						String projectName = getElement("ProjectCreateNew_projectDropDown_Id").getAttribute("value");
						
						String versionName =getObject("ProjectCreateNew_versionTextField").getAttribute("value");
						
						String startDate = getElement("ProjectCreateNew_startDateField_Id").getAttribute("value");
						
						String endDate = getElement("ProjectCreateNew_endDateField_Id").getAttribute("value");						
						
						projectDetails[projectDetailsRowIndex][0]= groupName;//End Date names 
						
						projectDetails[projectDetailsRowIndex][1]= portfolioName;//End Date names 
						
						projectDetails[projectDetailsRowIndex][2]= projectName;//project names ;
						
						projectDetails[projectDetailsRowIndex][3]= versionName;//Version ;
						
						projectDetails[projectDetailsRowIndex][4]= startDate;//Start Date names ;
						
						projectDetails[projectDetailsRowIndex][5]= endDate;//End Date names ;					
						
						Thread.sleep(500);
						
					
						String testPassNameToBeInArray = null; 
						
							
						getElement("TM_testStepsTab_Id").click();	
						
						Thread.sleep(1500);
						
						if (!getObject("TestSteps_testPassTitleSelected").getAttribute("title").equals("No Test Pass")) 
						{
							int numberOfTestPassInTestPassDropdown = eventfiringdriver.findElements(By.xpath("//ul[@id = 'tpul']/li")).size();
							
							for (int k = 1; k <= numberOfTestPassInTestPassDropdown; k++) 
							{
								Thread.sleep(500);
							
						        getElement("Attachments_testPassDropDown_Id").click();
						      
						        Thread.sleep(500);
								
						        getObject("Attachment_testpassNameDDXpath1", "Attachments_xPath2_end", k).click();
								
								if (!(getElement("TestSteps_noTestStepAvailable_Text_Id").getText().contains(" No"))) 
								{
									
									if (getElement("TestStepsViewAll_table_Id").isDisplayed()) 
									{
										
										testPassNameToBeInArray = getObject("TestSteps_testPassTitleSelected").getAttribute("title");
									
										testPassArrayList.add(testPassNameToBeInArray);
										
									}
									
								}
								
							}
							
						}
						
						
						String storeArraylist = "";

						for (int i = 0; i < testPassArrayList.size(); i++) 
						{
							String tempVariableForArraylist = testPassArrayList.get(i);
							
							storeArraylist += tempVariableForArraylist  + ", ";
							
						}

						projectDetails[projectDetailsRowIndex][6]= storeArraylist;
						
						/*
						System.out.println("Group "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][0]);
						System.out.println("Portfolio "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][1]);
						System.out.println("Project "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][2]);
						System.out.println("Version "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][3]);
						System.out.println("StartDate "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][4]);
						System.out.println("EndDate "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][5]);
						System.out.println("Test pass "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][6]);
							
						String[] eachElementInArraylist = storeArraylist.split(", ");
						
						for (int i = 0; i < eachElementInArraylist.length; i++) 
						{
							System.out.println("eachElementInArraylist "+i+"-"+eachElementInArraylist[i]);
						}*/
						
						
						testPassArrayList.clear();
						
						Thread.sleep(1000);
						
						getElement("TM_projectsTab_Id").click();
						
						Thread.sleep(2500);						
						
						projectDetailsRowIndex++;		
						
					}			
					
				} while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
				/*System.out.println("Test pass "+0 +"- "+ projectDetails[0][6]);
				
				String[] testPassInAProject = (projectDetails[0][6].toString()).split(", ");
				
				for (int k = 0; k < testPassInAProject.length; k++) 
				{
					System.out.println("testPassInAProject "+testPassInAProject[k]);
				}
				*/
				
				getElement("UAT_stakeholderDashboard_Id").click();
				
				Thread.sleep(3000);
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));

				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
				
				fiscalYearDD = new Select(getElement("StakeholderDashboard_fiscalYearDropDown_Id"));
				
				DateFormat dateFormat;
				String systemDate;
				
				Date date = new Date();
				
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				
				systemDate = dateFormat.format(date);
				
				String sysdateInDateFormat = dateFormat.format(dateFormat.parse(systemDate));
				
				int currentFiscalyear = getFiscalYear(sysdateInDateFormat);
				
				//Select TODAY-1
				
				selectDateDD.selectByIndex(3);
				
				Thread.sleep(1000);
				
				if (compareStrings("Today - 1", selectDateDD.getFirstSelectedOption().getText()) &&
						compareStrings("All", projectStatusDD.getFirstSelectedOption().getText()) &&
						compareStrings("FY "+currentFiscalyear, fiscalYearDD.getFirstSelectedOption().getText())) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						String[][] projectsDetailsInTodayMinus1 = projectsWhenTodayMinus1IsSelected(projectDetails);
				
						
						//Verifying Group Drop down elements
						
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectsDetailsInTodayMinus1);
	
						
						int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
						
						
						if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Groups are displayed in Group Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)");
							
							comments += "\n- All Groups are displayed in Group Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Groups are NOT displayed in Group Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) (FAIL) ";
							
							APP_LOGS.debug("All Groups are NOT displayed in Group Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) Test case failed");
							
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectsDetailsInTodayMinus1);
						
						int portfolioCount = 0;
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectsDetailsInTodayMinus1.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectsDetailsInTodayMinus1[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						
						if (compareIntegers(1, portfolioCount)) 
						{
							APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)");
							
							comments += "\n- All Portfolios are displayed in Portfolio Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) (FAIL) ";
							
							APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) Test case failed");
							
						}
						
						
						//Verifying Projects 
						
						int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
						
						
						if (compareIntegers(projectsDetailsInTodayMinus1.length , totalNumberOfProjectsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Projects are displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)");
							
							comments += "\n- All Projects are displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Projects are NOT displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) (FAIL) ";
							
							APP_LOGS.debug("All Projects are NOT displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) Test case failed");
							
						}
						
						
						//Verifying Test Passes
						
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectsDetailsInTodayMinus1);
						
						
						int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
						{
							APP_LOGS.debug("All Test Passes are displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)");
							
							comments += "\n- All Test Passes are displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Test Passes are NOT displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) (FAIL) ";
							
							APP_LOGS.debug("All Test Passes are NOT displayed whose end dates are one less than today's date and greater than or Equal to today's date.(TODAY-1) Test case failed");
							
						}
						
					}
					else 
					{
						skip = true;
						
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'TODAY-1' is Selected in Select Date Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'TODAY-1' is Selected in Select Date Dropdown. So, verification cannot be done and skipping the test.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Today - 1' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. (FAIL) ";
					
					APP_LOGS.debug("'Today - 1' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Today-1'/'All'/current Fiscal Year is not display selected");	
				}
				
				//Select Today
				
				selectDateDD.selectByIndex(2);
				
				Thread.sleep(1000);
				
				if (compareStrings("Today", selectDateDD.getFirstSelectedOption().getText()) &&
						compareStrings("All", projectStatusDD.getFirstSelectedOption().getText()) &&
						compareStrings("FY "+currentFiscalyear, fiscalYearDD.getFirstSelectedOption().getText())) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						String[][] projectsDetailsInToday = projectsWhenTodayIsSelected(projectDetails);
				
						
						//Verifying Group Drop down elements
						
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectsDetailsInToday);
	
						
						int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
						
						if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Groups are displayed in Group Drp down whose end dates are equal to and greater than today's date.(TODAY)");
							
							comments += "\n- All Groups are displayed in Group Drp down whose end dates are equal to and greater than today's date.(TODAY)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Groups are NOT displayed in Group Drp down whose end dates are equal to and greater than today's date.(TODAY) (FAIL) ";
							
							APP_LOGS.debug("All Groups are NOT displayed in Group Drp down whose end dates are equal to and greater than today's date.(TODAY) Test case failed");
							
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectsDetailsInToday);
						
						int portfolioCount = 0;
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectsDetailsInToday.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectsDetailsInToday[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						if (compareIntegers(1, portfolioCount)) 
						{
							APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down whose end dates are equal to and greater than today's date.(TODAY)");
							
							comments += "\n- All Portfolios are displayed in Portfolio Drp down whose end dates are equal to and greater than today's date.(TODAY)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down whose end dates are equal to and greater than today's date.(TODAY) (FAIL) ";
							
							APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down whose end dates are equal to and greater than today's date.(TODAY) Test case failed");
							
						}
						
						
						//Verifying Projects 
						
						int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
						
						
						if (compareIntegers(projectsDetailsInToday.length , totalNumberOfProjectsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Projects are displayed  whose end dates are equal to and greater than today's date.(TODAY)");
							
							comments += "\n- All Projects are displayed whose end dates are equal to and greater than today's date.(TODAY)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Projects are NOT displayed whose end dates are equal to and greater than today's date.(TODAY) (FAIL) ";
							
							APP_LOGS.debug("All Projects are NOT displayed whose end dates are equal to and greater than today's date.(TODAY) Test case failed");
							
						}
						
						
						//Verifying Test Passes
						
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectsDetailsInToday);
						
						
						int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
						{
							APP_LOGS.debug("All Test Passes are displayed whose end dates are equal to and greater than today's date.(TODAY)");
							
							comments += "\n- All Test Passes are displayed whose end dates are equal to and greater than today's date.(TODAY)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Test Passes are NOT displayed whose end dates are equal to and greater than today's date.(TODAY) (FAIL) ";
							
							APP_LOGS.debug("All Test Passes are NOT displayed whose end dates are equal to and greater than today's date.(TODAY) Test case failed");
							
						}
						
					}
					else 
					{
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'TODAY' is Selected in Select Date Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'TODAY' is Selected in Select Date Dropdown. So, verification cannot be done.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Today' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. (FAIL) ";
					
					APP_LOGS.debug("'Today' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Today-1'/'All'/current Fiscal Year is not display selected");
				}
				
				
				//Select Last 7 Days
				
				selectDateDD.selectByIndex(4);
				
				Thread.sleep(1000);
				
				if (compareStrings("Last 7 Days", selectDateDD.getFirstSelectedOption().getText()) &&
						compareStrings("All", projectStatusDD.getFirstSelectedOption().getText()) &&
						compareStrings("FY "+currentFiscalyear, fiscalYearDD.getFirstSelectedOption().getText())) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						String[][] projectsDetailsForLast7DaysSelected = projectsWhenLast7DaysIsSelected(projectDetails);
				
						
						//Verifying Group Drop down elements
						
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectsDetailsForLast7DaysSelected);
	
						
						int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
						
						
						if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Groups are displayed in Group Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)");
							
							comments += "\n- All Groups are displayed in Group Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Groups are NOT displayed in Group Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) (FAIL) ";
							
							APP_LOGS.debug("All Groups are NOT displayed in Group Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) Test case failed");
							
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectsDetailsForLast7DaysSelected);
						
						int portfolioCount = 0;
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectsDetailsForLast7DaysSelected.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectsDetailsForLast7DaysSelected[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						if (compareIntegers(1, portfolioCount)) 
						{
							APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)");
							
							comments += "\n- All Portfolios are displayed in Portfolio Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) (FAIL) ";
							
							APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) Test case failed");
							
						}
						
						
						//Verifying Projects 
						
						int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
						
						
						if (compareIntegers(projectsDetailsForLast7DaysSelected.length , totalNumberOfProjectsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Projects are displayed  whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)");
							
							comments += "\n- All Projects are displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Projects are NOT displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) (FAIL) ";
							
							APP_LOGS.debug("All Projects are NOT displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) Test case failed");
							
						}
						
						
						//Verifying Test Passes
						
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectsDetailsForLast7DaysSelected);
						
						
						int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
						{
							APP_LOGS.debug("All Test Passes are displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)");
							
							comments += "\n- All Test Passes are displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Test Passes are NOT displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) (FAIL) ";
							
							APP_LOGS.debug("All Test Passes are NOT displayed whose end dates are either lying in between last 7 days from today's date or equal to and greater than today's date.(LAST 7 DAYS) Test case failed");
							
						}
						
					}
					else 
					{
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'Last 7 Days' is Selected in Select Date Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'Last 7 Days' is Selected in Select Date Dropdown. So, verification cannot be done.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Last 7 Days' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. (FAIL) ";
					
					APP_LOGS.debug("'Last 7 Days' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Last 7 Days'/'All'/current Fiscal Year is not display selected");
				}
				
				
				//Select Dates- From/To
				Thread.sleep(1000);
				
				selectDateDD.selectByIndex(1);
				
				Thread.sleep(1000);
				
				selectFromDateAndToDate(getObject("SDDetailedView_fromCalenderIcon"), fromMonth, fromYear, fromDate);
				
				selectFromDateAndToDate(getObject("SDDetailedView_toCalenderIcon"), toMonth, toYear, toDate);
				
				String selectedFromDate = getElement("SDDetailedView_fromInputBox_Id").getAttribute("value");
				
				String selectedToDate = getElement("SDDetailedView_toInputBox_Id").getAttribute("value");
				
				
				if ( compareStrings("Dates - From/To", selectDateDD.getFirstSelectedOption().getText()) &&
						compareStrings("All", projectStatusDD.getFirstSelectedOption().getText()) &&
						compareStrings("FY "+currentFiscalyear, fiscalYearDD.getFirstSelectedOption().getText()) ) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) ) 	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						String[][] projectsDetailsForDatesFromToSelected = projectsWhenDatesFromToIsSelected(projectDetails, selectedFromDate, selectedToDate);
				
						//Verifying Group Drop down elements
						
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectsDetailsForDatesFromToSelected);
	
						
						int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
						
						
						if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Groups are displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)");
							
							comments += "\n- All Groups are displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Groups are NOT displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) (FAIL) ";
							
							APP_LOGS.debug("All Groups are NOT displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) Test case failed");
							
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectsDetailsForDatesFromToSelected);
						
						int portfolioCount = 0;
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectsDetailsForDatesFromToSelected.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectsDetailsForDatesFromToSelected[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						if (compareIntegers(1, portfolioCount)) 
						{
							APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)");
							
							comments += "\n- All Portfolios are displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) (FAIL) ";
							
							APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) Test case failed");
							
						}
						
						
						//Verifying Projects 
						
						int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
						
						
						if (compareIntegers(projectsDetailsForDatesFromToSelected.length , totalNumberOfProjectsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Projects are displayed  whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)");
							
							comments += "\n- All Projects are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Projects are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) (FAIL) ";
							
							APP_LOGS.debug("All Projects are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) Test case failed");
							
						}
						
						
						//Verifying Test Passes
						
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectsDetailsForDatesFromToSelected);
						
						
						int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
						{
							APP_LOGS.debug("All Test Passes are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)");
							
							comments += "\n- All Test Passes are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Test Passes are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) (FAIL) ";
							
							APP_LOGS.debug("All Test Passes are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between From & To Date OR Start date (<) less than From date and end date (>) greater than To date.(DATES - FROM/TO) Test case failed");
							
						}
						
					}
					else 
					{
						APP_LOGS.debug("'No Groups' is displayed in Group Dropdown when 'Dates - From/To' is Selected in Select Date Dropdown.");
						
						comments += "\n- 'No Groups' is displayed in Group Dropdown when 'Dates - From/To' is Selected in Select Date Dropdown. So, verification cannot be done.";
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Dates - From/To' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. (FAIL) ";
					
					APP_LOGS.debug("'Dates - From/To' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Dates - From/To'/'All'/current Fiscal Year is not display selected");
				}
				
				
				//Select Select Filter
				
				selectDateDD.selectByIndex(0);
				
				Thread.sleep(1000);
				
				if (compareStrings("Select Filter", selectDateDD.getFirstSelectedOption().getText()) &&
						compareStrings("All", projectStatusDD.getFirstSelectedOption().getText()) &&
						compareStrings("FY "+currentFiscalyear, fiscalYearDD.getFirstSelectedOption().getText())) 
				{
				
					String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
					
					
					
					if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )	//noTestStepAvailableTagSize == 1  => means no Test step Available is displayed (Pie chart is not displayed) 
					{
						
						//Verifying Group Drop down elements
						
						String[] uniqueGroupArray = getUniqueGroupsFromArray(projectDetails);
	
						
						int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
						
						
						if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Groups are displayed in Group Drp down .(SELECT FILTER)");
							
							comments += "\n- All Groups are displayed in Group Drp down.(SELECT FILTER)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Groups are NOT displayed in Group Drp down.(SELECT FILTER) (FAIL) ";
							
							APP_LOGS.debug("All Groups are NOT displayed in Group Drp down.(SELECT FILTER) Test case failed");
							
						}
						
						
						//Verifying Portfolio Drop down elements
						
						//For verifying the portfolio .. need to apply proper verification logic 
						//Following logic is not enough for the verification
						
						String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectDetails);
						
						int portfolioCount = 0;
						
						for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
						{
							String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
							
							for (int j = 0; j < projectDetails.length; j++) 
							{
								if (portfolioNamePresentInPortfolioDD.equals(projectDetails[j][1]))
								{
									portfolioCount = 1;
								}
							}
						}
							
						
						if (compareIntegers(1, portfolioCount)) 
						{
							APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down.(SELECT FILTER)");
							
							comments += "\n- All Portfolios are displayed in Portfolio Drp down.(SELECT FILTER)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down.(SELECT FILTER) (FAIL) ";
							
							APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down.(SELECT FILTER) Test case failed");
							
						}
						
						
						//Verifying Projects 
						
						int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
						
						
						if (compareIntegers(projectDetails.length , totalNumberOfProjectsOnDetailedView)) 
						{
							
							APP_LOGS.debug("All Projects are displayed .(SELECT FILTER)");
							
							comments += "\n- All Projects are displayed.(SELECT FILTER)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Projects are NOT displayed.(SELECT FILTER) (FAIL) ";
							
							APP_LOGS.debug("All Projects are NOT displayed.(SELECT FILTER) Test case failed");
							
						}
						
						
						//Verifying Test Passes
						
						
						String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectDetails);
						
						
						int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
						
						
						if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
						{
							APP_LOGS.debug("All Test Passes are displayed.(SELECT FILTER)");
							
							comments += "\n- All Test Passes are displayed.(SELECT FILTER)";
						}
						else 
						{
							fail=true;
							
							comments += "\n- All Test Passes are NOT displayed.(SELECT FILTER) (FAIL) ";
							
							APP_LOGS.debug("All Test Passes are NOT displayed.(SELECT FILTER) Test case failed");
							
						}
						
					}
					
				}
				else 
				{
					fail=true;
					
					comments += "\n- 'Select Filter' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. (FAIL) ";
					
					APP_LOGS.debug("'Select Filter' in Select Date OR 'All' in Project Status OR Current Fiscal Year In Fiscal Year is not display selected. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'Select Filter'/'All'/current Fiscal Year is not display selected");
				}
				
				
			}
			catch(SkipException t)
			 {
				skip = true; 
			   
			   	t.printStackTrace();
				
				comments += "\n- Skip exception has Occurred.";
				
			 }
			catch(Throwable t)
			{
				t.printStackTrace();
				
				fail=true;
				
				assertTrue(false);	
				
				comments += "\n- Fail :-Any other exception has Occurred.";
				
				APP_LOGS.debug("Any other exception has Occurred. Test Case has been Failed");
			}
			
			closeBrowser();
		}
		else 
		{
			APP_LOGS.debug("Login Unsuccessful for user "+Role);
			
			comments+="Login Unsuccessful for Tester "+Role;
		}
		
	}	
	
	//Functions
	
	private String[][] projectsWhenTodayIsSelected (String[][] projectArrayToSearchIn) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		String systemDate;
		Date projectStartDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{			
			projectStartDate = dateFormat.parse(projectArrayToSearchIn[i][4]);
			
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			if (projectEndDate.compareTo(sysdateInDateFormat) >= 0 && !(projectStartDate.compareTo(sysdateInDateFormat) > 0)) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
		
	}
	
	private String[][] projectsWhenTodayMinus1IsSelected (String[][] projectArrayToSearchIn) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		String systemDate;
		Date projectStartDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		int n = 1;
			
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			projectStartDate = dateFormat.parse(projectArrayToSearchIn[i][4]);
			
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			Date dayMinus1Date = new Date(sysdateInDateFormat.getTime() - n * 24 * 3600 * 1000) ; //Subtract n days
			
			if (projectEndDate.compareTo(dayMinus1Date) >= 0 && !(projectStartDate.compareTo(sysdateInDateFormat) > 0)) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
		
	}
	
	private String[][] projectsWhenLast7DaysIsSelected (String[][] projectArrayToSearchIn) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		String systemDate;
		Date projectStartDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date date = new Date();
		
		systemDate = dateFormat.format(date);
		
		Date sysdateInDateFormat = dateFormat.parse(systemDate);
		
		int n = 7;
			
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			projectStartDate = dateFormat.parse(projectArrayToSearchIn[i][4]);
			
			projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			Date dayMinus7Date = new Date(sysdateInDateFormat.getTime() - n * 24 * 3600 * 1000) ; //Subtract n days
			
			if (projectEndDate.compareTo(dayMinus7Date) >= 0 && !(projectStartDate.compareTo(sysdateInDateFormat) > 0)) 
			{ 
				arrayToStoreIndexOfReqProjects.add(i);
			}
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
	}
	
	private String[][] projectsWhenDatesFromToIsSelected (String[][] projectArrayToSearchIn, String selectedFromDate, String selectedToDate) throws ParseException, IOException
	{
		DateFormat dateFormat;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		Date fromDate = dateFormat.parse(selectedFromDate);
		
		Date toDate = dateFormat.parse(selectedToDate);
		
		for (int i = 0; i < projectArrayToSearchIn.length; i++) 
		{	
			Date projectStartDate = dateFormat.parse(projectArrayToSearchIn[i][4]);
			
			Date projectEndDate = dateFormat.parse(projectArrayToSearchIn[i][5]);
			
			if (  ( projectEndDate.after(fromDate) && projectEndDate.before(toDate) ) ||
				  ( projectStartDate.after(fromDate) && projectStartDate.before(toDate) ) ||
				  ( ( projectEndDate.after(fromDate) && projectEndDate.before(toDate) ) && ( projectStartDate.after(fromDate) && projectStartDate.before(toDate) ) ) ||
				  ( projectStartDate.before(fromDate) && projectEndDate.after(toDate) )    ) 
			
			{
			
				arrayToStoreIndexOfReqProjects.add(i);
			
			}
			
		}
	
		int projectsCountForReqDatesRange = arrayToStoreIndexOfReqProjects.size();
		
		String[][] projectDetailsForReqDateRange = new String[projectsCountForReqDatesRange][7];
		
		int rowToGetDetailsFromArraylist;
		
		//loop to insert values based on the indexes got from the above loop
		for (int j = 0; j < projectDetailsForReqDateRange.length; j++) 
		{
			rowToGetDetailsFromArraylist = arrayToStoreIndexOfReqProjects.get(j);
			
			projectDetailsForReqDateRange[j][0] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][0];
			projectDetailsForReqDateRange[j][1] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][1];
			projectDetailsForReqDateRange[j][2] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][2];
			projectDetailsForReqDateRange[j][3] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][3];
			projectDetailsForReqDateRange[j][4] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][4];
			projectDetailsForReqDateRange[j][5] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][5];
			projectDetailsForReqDateRange[j][6] =  projectArrayToSearchIn[rowToGetDetailsFromArraylist][6];
		}
		
		
		return projectDetailsForReqDateRange;
		
	}
	
	//Function to select the start date and end date from date picker 
	public void selectFromDateAndToDate(WebElement fromToDateImage, String fromToMonth, String fromToYear,String fromToDate) throws IOException
	{
		try
		{
			//Select start date 		
			WebElement startDateImage = fromToDateImage;		
			startDateImage.click();		
			APP_LOGS.debug("Clicked on Date Calendar icon...");
			
			Select year = new Select(getElementByClassAttr("ProjectCreateNew_yearDropDown_Class"));
			year.selectByValue(fromToYear);
			APP_LOGS.debug(fromToYear +" : Year is selected...");
			
			Select month = new Select(getElementByClassAttr("ProjectCreateNew_monthDropDown_Class"));
			month.selectByVisibleText(fromToMonth);
			APP_LOGS.debug(fromToMonth +" : Month is selected...");
			
			WebElement datepicker= getObject("ProjectCreateNew_dateTable");
			//List<WebElement> rows = datepicker.findElements(By.tagName("tr"));
			List<WebElement> cols = datepicker.findElements(By.tagName("td"));
			for(WebElement cell :cols)
			{
				if(cell.getText().equals(fromToDate))
				{
					cell.findElement(By.linkText(""+fromToDate+"")).click();
					APP_LOGS.debug(fromToDate +" : Date is selected...");
					break;
				}
			}
		}
		catch(Throwable t)
		{
			fail=true;
			t.printStackTrace();
		}
		
	}

	
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(SD_detailedViewXls, this.getClass().getSimpleName()) ;
	}
	
		
	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
		{
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "SKIP");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);	
		}
		else if(!isLoginSuccess)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "Login Unsuccessful");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPass=false;
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		{
			TestUtil.reportDataSetResult(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, "PASS");
			TestUtil.printComments(SD_detailedViewXls, this.getClass().getSimpleName(), count+2, comments);
		}
		//skip=false;
		fail=false;
	}


	@AfterTest
	public void reportTestResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "SKIP");
		else if(isTestPass)
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(SD_detailedViewXls, "Test Cases", TestUtil.getRowNum(SD_detailedViewXls,this.getClass().getSimpleName()), "FAIL");
		skip=false;
	}
			
}
