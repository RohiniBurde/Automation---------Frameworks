package com.uat.suite.tm_project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class DeletingProject extends TestSuiteBase 
{
	String runmodes[]=null;
	static boolean isLoginSuccess=true;
	String selectedGroupsArray[];
	List<WebElement> GroupsInUpperRibbon;
	ArrayList<String> ProjectNames = new ArrayList<String>();
	int flagCount = 0;
	
/*	@BeforeTest
	public void checkTestSkip() throws Exception
	{
		if(!TestUtil.isTestCaseRunnable(TM_projectSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_projectSuiteXls, this.getClass().getSimpleName());
	}*/
	
	@Test
	public void DeleteAllProjects() throws Exception
	{
		openBrowser();
		
		APP_LOGS.debug("Calling Login with role Admin");
		
		if(login("Admin"))
		{
			APP_LOGS.debug("Login Successful");
			final String JQUERY_LOAD_PORTFOLIO_SCRIPT = "src\\js_collection\\GroupSelectionForDataCleanUp.js";
			APP_LOGS.debug("Starting with navigation to test pass page.");
			try
			{
				getElement("UAT_testManagement_Id").click();
				Thread.sleep(1000);
				getElement("TM_testCasesTab_Id").click();
				if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
				{
					getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
				}
				Thread.sleep(1000);
				String jQueryLoader = readFile(JQUERY_LOAD_PORTFOLIO_SCRIPT);
				GroupsInUpperRibbon = getElement("TestCaseUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
				int numOfAvailableGroups = GroupsInUpperRibbon.size();
				eventfiringdriver.executeScript(jQueryLoader);
				wait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				String selectedGroupNames = alert.getText();
				System.out.println(selectedGroupNames);
				// All the projects are taken into array separated by ','.
				selectedGroupsArray = selectedGroupNames.split(",");
				int numOfGroups = selectedGroupsArray.length;
				alert.accept();
				assertTrue(deleteTestCase(selectedGroupsArray, numOfGroups, numOfAvailableGroups));
				System.out.println("testcase deletion complete");
				assertTrue(deleteTestPass(selectedGroupsArray, numOfGroups, numOfAvailableGroups));
				System.out.println("testPass deletion complete");
				
				getElement("TM_projectsTab_Id").click();
				Thread.sleep(1500);
				int numOfAvailableProject = Integer.parseInt(getElement("Deletion_Project_TotalRecords").getText());
				for(int i=0;i<ProjectNames.size();i++)
				{
					if(numOfAvailableProject>10)
					{
						getObject("Deletion_Project_FirstPagination_SelectFirstasDefault").click();
					}
					assertTrue(deleteProject());
				}
				
				
				System.out.println("Project deletion complete");
				assertTrue(deleteGroups(selectedGroupsArray, numOfGroups, numOfAvailableGroups));
				System.out.println("Group Portfolio deletion complete");
			}catch(Throwable e)
			{
				APP_LOGS.debug("Exception in selecting group name from alerts.");
				e.printStackTrace();
			}
		}else 
		{
			isLoginSuccess=false;
		}
	}
	
	// Delete Group and Portfolio	

	public boolean deleteGroups(String[] selectedGroupsArray, int numOfGroups, int numOfAvailableGroups) throws IOException, InterruptedException
	{
		getObject("Deletion_Project_CreateNew").click();
		Thread.sleep(1000);
		List<WebElement> GroupNamesinDropDown = getElement("Deletion_GroupNamesinDropDown_Id").findElements(By.tagName("option"));
		List<WebElement> PortfolioNamesinDropDown;
		try
		{
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				getElement("Deletion_GroupNamesinDropDown_Id").click();
				for(int j=0;j<numOfAvailableGroups;j++)
				{
					if(selectedGroupsArray[groupCount].equals(GroupNamesinDropDown.get(j).getAttribute("title")))
					{
						GroupNamesinDropDown = getElement("Deletion_GroupNamesinDropDown_Id").findElements(By.tagName("option"));
						GroupNamesinDropDown.get(j).click();
						PortfolioNamesinDropDown = getElement("Deletion_PortfolioNamesinDropDown_Id").findElements(By.tagName("option"));
						for(int k =1; k<=PortfolioNamesinDropDown.size();k++)
						{
							getElement("Deletion_PortfolioNamesinDropDown_Id").click();
							getObject("Deletion_PortfolioNamesinDropDown_Xpath1", "Deletion_PortfolioNamesinDropDown_Xpath1", k).click();
							getObject("Deletion_PortfolioName_DeleteIcon").click();
							Thread.sleep(1500);
							getObject("Deletion_GroupPortfolioDeleteConfirmationMessage").click();
							getObject("Deletion_GroupPortfolioProjectDeletePerformedOkMessage").click();
						}
						getObject("Deletion_GroupName_DeleteIcon").click();
						Thread.sleep(800);
						getObject("Deletion_GroupPortfolioDeleteConfirmationMessage").click();
						getObject("Deletion_GroupPortfolioProjectDeletePerformedOkMessage").click();
						break;
					}
					
				}
			}
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in delete GroupPortfolio function.");
			e.printStackTrace();
			return false;
		}
	}
	
	//  Delete Project
		
	public boolean deleteProject() throws IOException, InterruptedException
	{
		String pName;
		int ProjectFound;
		try
		{
			
			for(int i = 1; i<= 10;i++)
			{
				ProjectFound =0;
				pName = getObject("Deletion_AvailableProjectNames_Xpath1", "Deletion_AvailableProjectNames_Xpath2", i).getAttribute("title");
				for(int j = 0;j<ProjectNames.size();j++)
				{
					if(pName.equals(ProjectNames.get(j)))
					{
						flagCount++;
						ProjectFound = 1;
						getObject("Deletion_AvailableProjectNames_Xpath1", "Deletion_AvailableProjectNames_Xpath2", i).click();
						getObject("Deletion_Project_DeleteIcon").click();
						Thread.sleep(800);
						getObject("Deletion_ProjectDeleteConfirmationMessage").click();
						getObject("Deletion_GroupPortfolioProjectDeletePerformedOkMessage").click();
						break;
					}
				}
				if((i%10==0)&&(ProjectFound == 0))
				{
					i=1;
					eventfiringdriver.findElement(By.linkText("Next")).click();
				}
				if(flagCount==ProjectNames.size())
				{
					break;
				}
			}
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in delete Project function.");
			e.printStackTrace();
			return false;
		}
	}
		
	//Delete test Pass

	public boolean deleteTestPass(String[] selectedGroupsArray, int numOfGroups, int numOfAvailableGroups) throws IOException, InterruptedException
	{
		try
		{
			getElement("TM_testPassesTab_Id").click();
			Thread.sleep(2500);
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				getElement("TestPassUpperRibbon_groupDropDown_Id").click();
				GroupsInUpperRibbon = getElement("TestPassUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
				for(int j=0;j<numOfAvailableGroups;j++)
				{
					if(selectedGroupsArray[groupCount].equals(GroupsInUpperRibbon.get(j).getAttribute("title")))
					{
						GroupsInUpperRibbon = getElement("TestPassUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
						GroupsInUpperRibbon.get(j).click();
						break;
					}
				}
				List<WebElement> PortfoliosInUpperRibbon = getElement("TestPassUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
				int numOfPortfolios = PortfoliosInUpperRibbon.size();
				for(int PortfolioCount = 0; PortfolioCount<numOfPortfolios; PortfolioCount++)
				{
					getElement("TestPassUpperRibbon_PortfolioDropDown_Id").click();
					Thread.sleep(500);
					PortfoliosInUpperRibbon = getElement("TestPassUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
					PortfoliosInUpperRibbon.get(PortfolioCount).click();
					List<WebElement> ProjectsInUpperRibbon = getElement("TestPassUpperRibbon_ProjectList_Id").findElements(By.tagName("li"));
					int numOfProjects = ProjectsInUpperRibbon.size();
					for(int ProjectCount = 0; ProjectCount<numOfProjects; ProjectCount++)
					{
						getElement("TestPassUpperRibbon_projectDropDown_Id").click();
						Thread.sleep(800);
						ProjectNames.add(ProjectsInUpperRibbon.get(ProjectCount).getText());
						ProjectsInUpperRibbon.get(ProjectCount).click();
						List<WebElement> VersionsInUpperRibbon = getElement("TestPassUpperRibbon_VersionList_Id").findElements(By.tagName("li"));
						int numOfVersions = VersionsInUpperRibbon.size();
						for(int versionCount = 0; versionCount< numOfVersions;versionCount++)
						{
							getElement("TestPassUpperRibbon_versionDropDown_Id").click();
							Thread.sleep(1000);
							VersionsInUpperRibbon.get(versionCount).click();
							if(!getElement("Deletion_NoTestPassAvailable_Id").isDisplayed())
							{
								while(!getElement("Deletion_NoTestPassAvailable_Id").isDisplayed())
								{
									getObject("Deletion_TestPassDeletion_XPath1", "Deletion_TestPassDeletion_XPath2", 1).click();
									Thread.sleep(1000);
									getObject("Deletion_TestPassDeleteConfirmationMessage").click();
									Thread.sleep(1000);
									getObject("Deletion_TestPassDeletePerformedOkMessage").click();
								}
							}
							
						}
					}
				}
			}
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in testpass deletion function.");
			e.printStackTrace();
			return false;
		}
	}
	
	//Deleting Test Case
	
	public boolean deleteTestCase(String[] selectedGroupsArray, int numOfGroups, int numOfAvailableGroups) throws IOException, InterruptedException
	{
		return true;
		/*try
		{
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				getElement("TestCaseUpperRibbon_groupDropDown_Id").click();
				for(int j=0;j<numOfAvailableGroups;j++)
				{
					GroupsInUpperRibbon = getElement("TestCaseUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
					if(selectedGroupsArray[groupCount].equals(GroupsInUpperRibbon.get(j).getAttribute("title")))
					{
						GroupsInUpperRibbon.get(j).click();
						break;
					}
				}
				List<WebElement> PortfoliosInUpperRibbon = getElement("TestCaseUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
				int numOfPortfolios = PortfoliosInUpperRibbon.size();
				for(int PortfolioCount = 0; PortfolioCount<numOfPortfolios; PortfolioCount++)
				{
					getElement("TestCaseUpperRibbon_PortfolioDropDown_Id").click();
					Thread.sleep(500);
					PortfoliosInUpperRibbon = getElement("TestCaseUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
					PortfoliosInUpperRibbon.get(PortfolioCount).click();
					List<WebElement> ProjectsInUpperRibbon = getElement("TestCaseUpperRibbon_ProjectList_Id").findElements(By.tagName("li"));
					int numOfProjects = ProjectsInUpperRibbon.size();
					for(int ProjectCount = 0; ProjectCount<numOfProjects; ProjectCount++)
					{
						getElement("TestCaseUpperRibbon_projectDropDown_Id").click();
						Thread.sleep(500);
						ProjectsInUpperRibbon.get(ProjectCount).click();
						List<WebElement> VersionsInUpperRibbon = getElement("TestCaseUpperRibbon_VersionList_Id").findElements(By.tagName("li"));
						int numOfVersions = VersionsInUpperRibbon.size();
						for(int versionCount = 0; versionCount< numOfVersions;versionCount++)
						{
							getElement("TestCaseUpperRibbon_versionDropDown_Id").click();
							Thread.sleep(1000);
							VersionsInUpperRibbon.get(versionCount).click();
							List<WebElement> numOfTestPassesInUpperRibbon = getElement("TestCaseUpperRibbon_TestPassList_Id").findElements(By.tagName("li"));
							int numOfTestPasses = numOfTestPassesInUpperRibbon.size();
							for(int testPassCount =0;testPassCount<numOfTestPasses;testPassCount++)
							{
								getElement("TestCaseUpperRibbon_testPassDropDown_Id").click();
								Thread.sleep(1000);
								numOfTestPassesInUpperRibbon.get(testPassCount).click();
								if(!getElement("Deletion_NoTestCasePresentDiv_Id").isDisplayed())
								{
									getElement("Deletion_TestCasesBulkDeleteBtn_Id").click();
									Thread.sleep(1000);
									getObject("Deletion_TestCasesBulkDeleteConfirmationMessage").click();
									Thread.sleep(1000);
									getObject("Deletion_TestCasesBulkDeletePerformedOkMessage").click();
								}
							}					
						}
					}
				}
				
			}
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("Exception in testcase deletion function.");
			e.printStackTrace();
			return false;
		}*/
	}
	
	private String readFile(String file) throws IOException 
	{
        Charset cs = Charset.forName("UTF-8");
        FileInputStream stream = new FileInputStream(file);
        try 
        {
            Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) 
            {
                builder.append(buffer, 0, read);
            }
            return builder.toString();
        }
        finally 
        {
            stream.close();
        }        
    }
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName()) ;
	}
	
}
