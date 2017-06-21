package com.uat.suite.sd_detailedView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
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
public class VerifyDVFiscalYearDropdown extends TestSuiteBase  
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
	public void testVerifyDVFiscalYearDropdown(String Role, String fiscalYear, String quarter ) throws Exception
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
						
						
						/*System.out.println("Group "+projectDetailsRowIndex +"- "+ projectDetails[projectDetailsRowIndex][0]);
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
				
				
				
				getElement("UAT_stakeholderDashboard_Id").click();
				
				Thread.sleep(3000);
				
				getObject("StakeholderDashboard_detailedViewPageTab").click();
				
				Thread.sleep(1500);
				
				
				selectDateDD = new Select(getElement("StakeholderDashboard_selectDateDropDown_Id"));

				selectGroup = new Select(getElement("SDDetailedView_groupDropdown_Id"));
				
				selectPortfolio = new Select(getElement("SDDetailedView_portfolioDropdown_Id"));
				
				projectStatusDD = new Select(getElement("StakeholderDashboard_projectStatusDropDown_Id"));
				
				fiscalYearDD = new Select(getElement("StakeholderDashboard_fiscalYearDropDown_Id"));
				
				
				//Get the current fiscal Year and do (current fiscal year - 1) so that all check boxes should be de-selected
				
				DateFormat dateFormat;
				String systemDate;
				
				Date date = new Date();
				
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				
				systemDate = dateFormat.format(date);
				
				String sysdateInDateFormat = dateFormat.format(dateFormat.parse(systemDate));
				
				int currentFiscalyear = getFiscalYear(sysdateInDateFormat);
				
				fiscalYearDD.selectByVisibleText( "FY " + ( currentFiscalyear - 1) );
				
				if (  getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected() ||
					  getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected() || 
					  getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected() ||
					  getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected()  ) 
				{
					
					fail=true;
					
					assertTrue(false);
					
					comments += "\n- All Quarter Checkboxes are NOT de-selected when Fiscal Year has been changed from current fiscal year.(Fail) ";
					
					APP_LOGS.debug("All Quarter Checkboxes are NOT de-selected when Fiscal Year has been changed from current fiscal year. Test case failed");
					
					TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter checkbox is selected while fiscal year changed");
					
				}
				else 
				{
					
					comments += "\n- All Quarter Checkboxes are de-selected when Fiscal Year has been changed from current fiscal year. ";
					
					APP_LOGS.debug("All Quarter Checkboxes are de-selected when Fiscal Year has been changed from current fiscal year ");	
					
					getElement("StakeholderDashboard_Q2Checkbox_Id").click();
					
					if (assertTrue( getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected() ) ) 
					{
						
						if ( compareStrings( "All", projectStatusDD.getFirstSelectedOption().getText() ) &&
							 compareStrings( "Select Filter", selectDateDD.getFirstSelectedOption().getText() ) )
						{
							
							comments += "\n- 'All' and 'Select Filter' has been display selected in Project Status and Select Date Drop Downs.";
							
							APP_LOGS.debug("'All' and 'Select Filter' has been display selected in Project Status and Select Date Drop Downs. ");
							
						}
						else 
						{
							
							fail=true;
							
							comments += "\n- 'All' and 'Select Filter' has NOT been display selected in Project Status and Select Date Drop Downs.(Fail) ";
							
							APP_LOGS.debug("'All' and 'Select Filter' has NOT been display selected in Project Status and Select Date Drop Downs. Test case failed");
							
							TestUtil.takeScreenShot(this.getClass().getSimpleName(), "'All' and 'Select Filter' has Not been display selected");
						
						}
						
					}
					else 
					{
						fail=true;
						
						comments += "\n- Quarter 2 has not been Selected after selecting it.(Fail) ";
						
						APP_LOGS.debug("Quarter 2 has not been Selected after selecting it Test case failed");
						
						TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Quarter 2 has not been Selected after selecting it");
					
					}
				}
				
				
				//Select Fiscal Year given by Data Provider
				
				fiscalYearDD.selectByVisibleText(fiscalYear);
				
				selectQuarter(quarter);
				
				
				String defaultGroupSelected = selectGroup.getFirstSelectedOption().getText();
				
				if (!(defaultGroupSelected.equalsIgnoreCase("No Group") ) )  
				{
					
					String[][] projectsDetailsForSelectedFYandQuarter = projectsInSelectedFYandQuarter(projectDetails, fiscalYear, quarter);
			
					
					/*for (int i = 0; i < projectsDetailsForSelectedFYandQuarter.length; i++) 
					{
						System.out.println("New Group "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][0]);
						System.out.println("New Portfolio "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][1]);
						System.out.println("New Projects "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][2]);
						System.out.println("New Version "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][3]);
						System.out.println("New Start Date "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][4]);
						System.out.println("New End Date "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][5]);
						System.out.println("New Test Case "+i+ " "+projectsDetailsForSelectedFYandQuarter[i][6]);
						
					}*/
					
					//Verifying Group Drop down elements
					
					
					String[] uniqueGroupArray = getUniqueGroupsFromArray(projectsDetailsForSelectedFYandQuarter);

					
					int numberOfGroupsOnDetailedView = getTheNumberOfGroupsAfterComparisonWithGivenArray(uniqueGroupArray);
					
					
					if (compareIntegers(uniqueGroupArray.length, numberOfGroupsOnDetailedView)) 
					{
						
						APP_LOGS.debug("All Groups are displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.");
						
						comments += "\n- All Groups are displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- All Groups are NOT displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.(Fail) ";
						
						APP_LOGS.debug("All Groups are NOT displayed in Group Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date. Test case failed");
						
					}
					
					
					//Verifying Portfolio Drop down elements
					
					//For verifying the portfolio .. need to apply proper verification logic 
					//Following logic is not enough for the verification
					
					String[] uniquePortfolioArray = getUniquePortfolioFromArray(projectsDetailsForSelectedFYandQuarter);
					
					int portfolioCount = 0;
					
					for (int i = 1; i < selectPortfolio.getOptions().size(); i++) 
					{
						String portfolioNamePresentInPortfolioDD = selectPortfolio.getOptions().get(i).getText();
						
						for (int j = 0; j < projectsDetailsForSelectedFYandQuarter.length; j++) 
						{
							if (portfolioNamePresentInPortfolioDD.equals(projectsDetailsForSelectedFYandQuarter[j][1]))
							{
								portfolioCount = 1;
							}
						}
					}
						
					if (compareIntegers(1, portfolioCount)) 
					{
						APP_LOGS.debug("All Portfolios are displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.");
						
						comments += "\n- All Portfolios are displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- All Portfolios are NOT displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.(Fail) ";
						
						APP_LOGS.debug("All Portfolios are NOT displayed in Portfolio Drp down whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date. Test case failed");
						
					}
					
					
					//Verifying Projects 
					
					int totalNumberOfProjectsOnDetailedView = getTheNumberProjectsDisplayedInDetailedView();
					
					if (compareIntegers(projectsDetailsForSelectedFYandQuarter.length , totalNumberOfProjectsOnDetailedView)) 
					{
						
						APP_LOGS.debug("All Projects are displayed  whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.");
						
						comments += "\n- All Projects are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- All Projects are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.(Fail) ";
						
						APP_LOGS.debug("All Projects are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date. Test case failed");
						
					}
					
					
					//Verifying Test Passes
					
					
					String[][] testPassToBeDisplayedInDetailedView = getTestPassToBeDisplayed(projectsDetailsForSelectedFYandQuarter);
					
					
					int numberOfTestPassDisplayed = numberOfTestPassAfterComparison(testPassToBeDisplayedInDetailedView);
					
					
					if (compareIntegers(testPassToBeDisplayedInDetailedView.length, numberOfTestPassDisplayed)) 
					{
						APP_LOGS.debug("All Test Passes are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.");
						
						comments += "\n- All Test Passes are displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.";
					}
					else 
					{
						fail=true;
						
						comments += "\n- All Test Passes are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date.(Fail) ";
						
						APP_LOGS.debug("All Test Passes are NOT displayed whose either start date or end date falls in between the given date range OR both Start & End Date are in between provided range OR Start date (<) less than provided Quarter start date and end date (>) greater than provided Quarter end date. Test case failed");
						
					}
					
				}
				else 
				{
					skip = true;
					
					APP_LOGS.debug("'No Groups' is displayed in Group Dropdown For Selected Fiscal Year.");
					
					comments += "\n- 'No Groups' is displayed in Group Dropdown For Selected Fiscal Year. So, verification cannot be done and skipping the test.";
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
	
	private  void selectQuarter(String quarter) throws IOException
	{
		
		String[] quarterToBeSelected = quarter.split(",");
		
		for (int i = 0; i < quarterToBeSelected.length; i++) 
		{
			
			String eachQuarter = quarterToBeSelected[i];
			
			if (eachQuarter.equals("1")) 
			{
				if ( ! getElement("StakeholderDashboard_Q1Checkbox_Id").isSelected() ) 
				{
					getElement("StakeholderDashboard_Q1Checkbox_Id").click();
				}
			}	
			else if (eachQuarter.equals("2")) 
			{
				if ( ! getElement("StakeholderDashboard_Q2Checkbox_Id").isSelected() ) 
				{
					getElement("StakeholderDashboard_Q2Checkbox_Id").click();
				}
				
			}
			else if (eachQuarter.equals("3")) 
			{
				if ( ! getElement("StakeholderDashboard_Q3Checkbox_Id").isSelected() ) 
				{
					getElement("StakeholderDashboard_Q3Checkbox_Id").click();
				}
				
			}
			else if (eachQuarter.equals("4")) 
			{
				if ( ! getElement("StakeholderDashboard_Q4Checkbox_Id").isSelected() ) 
				{
					getElement("StakeholderDashboard_Q4Checkbox_Id").click();
				}
				
			}
			
		}
		
	}
	
	
	
	private String[][] projectsInSelectedFYandQuarter (String[][] projectArrayToSearchIn, String fiscalYear, String quarter) throws ParseException, IOException
	{
		Date projectEndDate;
		DateFormat dateFormat;
		Date projectStartDate;
		
		ArrayList<Integer> arrayToStoreIndexOfReqProjects = new ArrayList<Integer>();
		
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		boolean includeInArray = false;
		
		String[] eachQuarter = quarter.split(",");
		

		for (int i = 0; i < eachQuarter.length; i++) 
		{
			
			Date startDateforEachQuarter = getStartDateForQuarter(Integer.parseInt( eachQuarter[i] ), fiscalYear);
			
			Date endDateforEachQuarter = getEndDateForQuarter(Integer.parseInt( eachQuarter[i] ), fiscalYear);
			
			
			for (int j = 0; j < projectArrayToSearchIn.length; j++) 
			{	
				projectStartDate = dateFormat.parse(projectArrayToSearchIn[j][4]);
				
				projectEndDate = dateFormat.parse(projectArrayToSearchIn[j][5]);
				

				if (  ( projectEndDate.after(startDateforEachQuarter) && projectEndDate.before(endDateforEachQuarter) ) ||
					  ( projectStartDate.after(startDateforEachQuarter) && projectStartDate.before(endDateforEachQuarter) ) ||
					  ( ( projectEndDate.after(startDateforEachQuarter) && projectEndDate.before(endDateforEachQuarter) ) && ( projectStartDate.after(startDateforEachQuarter) && projectStartDate.before(endDateforEachQuarter) ) ) ||
					  ( projectStartDate.before(startDateforEachQuarter) && projectEndDate.after(endDateforEachQuarter) )    ) 
					
				{
				
					includeInArray = true;
				
				}
				
				if (includeInArray) 
				{
					arrayToStoreIndexOfReqProjects.add(j);
					
					includeInArray = false;
				}
				
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
	
	private  int getsmallestQuarter(String quarter) throws IOException
	{
		
		String[] eachQuarter = quarter.split(",");
		
		int[] intQuarter = new int[eachQuarter.length];
		
		for (int i = 0; i < eachQuarter.length; i++) 
		{
		
			intQuarter[i] = Integer.parseInt(eachQuarter[i]);
			
		}
		
        List<Integer> b = Arrays.asList(ArrayUtils.toObject(intQuarter));
		
        int smallestQuarter = Collections.min(b);
       
        return smallestQuarter;
       
	}
	
	private  int getLargestQuarter(String quarter) throws IOException
	{
		
		String[] eachQuarter = quarter.split(",");
		
		int[] intQuarter = new int[eachQuarter.length];
		
		for (int i = 0; i < eachQuarter.length; i++) 
		{
		
			intQuarter[i] = Integer.parseInt(eachQuarter[i]);
			
		}
		
        List<Integer> b = Arrays.asList(ArrayUtils.toObject(intQuarter));
		
        int largestQuarter = Collections.max(b);
       
		return largestQuarter;
       
	}
	
	private Date getStartDateForQuarter(int quarterNumber, String fiscalYear) throws ParseException, IOException
	{
		Date returnStartDate;
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String temp;
		
		int year = getYearAccordingToFYandQuarter(fiscalYear, quarterNumber);
		
		if(quarterNumber == 1)
		{
			temp = "07/01/20"+year;	
		
			returnStartDate = dateFormat.parse(temp);
		}
		else if(quarterNumber == 2)
		{	
			temp = "10/01/20"+year;	
		
			returnStartDate = dateFormat.parse(temp);
		}
		else if(quarterNumber == 3)
		{	
			temp = "01/01/20"+year;
		
			returnStartDate = dateFormat.parse(temp);
		}
		else
		{
			temp = "04/01/20"+year;	
		
			returnStartDate = dateFormat.parse(temp);
		}
		return returnStartDate;
				
		
	}
	
	private Date getEndDateForQuarter(int quarterNumber, String fiscalYear) throws ParseException, IOException
	{
		Date returnEndDate;
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String temp;
		
		int year = getYearAccordingToFYandQuarter(fiscalYear, quarterNumber);
		
		if(quarterNumber == 1)
		{
			temp = "09/30/20"+year;	
		
			returnEndDate = dateFormat.parse(temp);
		}
		else if(quarterNumber == 2)
		{	
			temp = "12/31/20"+year;	
		
			returnEndDate = dateFormat.parse(temp);
		}
		else if(quarterNumber == 3)
		{	
			temp = "03/31/20"+year;
		
			returnEndDate = dateFormat.parse(temp);
		}
		else
		{
			temp = "06/30/20"+year;	
		
			returnEndDate = dateFormat.parse(temp);
		}
		return returnEndDate;
				
		
	}
	
	private String startDateForGivenFYandQuarter (String fiscalYear, int quarter) throws ParseException, IOException
	{
		String startDate = null;
		int year = getYearAccordingToFYandQuarter(fiscalYear, quarter);
		
		if (quarter == 1) 
		{
			startDate = "07/01/20"+year;
		}
		if (quarter == 2) 
		{
			startDate = "10/01/20"+year;
		}
		if (quarter == 3) 
		{
			startDate = "01/01/20"+year;
		}
		if (quarter == 4) 
		{
			startDate = "04/01/20"+year;
		}
		
		return startDate;
	}
	
	private String endDateForGivenFYandQuarter (String fiscalYear, int quarter) throws ParseException, IOException
	{
		String endDate = null;
		int year = getYearAccordingToFYandQuarter(fiscalYear, quarter);
		
		if (quarter == 1) 
		{
			endDate = "09/30/20"+year;
		}
		if (quarter == 2) 
		{
			endDate = "12/31/20"+year;
		}
		if (quarter == 3) 
		{
			endDate = "03/31/20"+year;
		}
		if (quarter == 4) 
		{
			endDate = "06/30/20"+year;
		}
		
		return endDate;
	}
	
	private int getYearAccordingToFYandQuarter (String fiscalYear, int quarter) throws ParseException, IOException
	{
		int returnFiscalYear = 0;
		
		if (quarter == 1 || quarter == 2) 
		{
			returnFiscalYear = Integer.parseInt(fiscalYear.substring(3))-1;
		}
		if (quarter == 3 || quarter == 4) 
		{
			returnFiscalYear = Integer.parseInt(fiscalYear.substring(3));
		}
		
		return returnFiscalYear;
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

