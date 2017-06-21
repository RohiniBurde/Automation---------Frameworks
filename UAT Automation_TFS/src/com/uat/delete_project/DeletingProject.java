package com.uat.delete_project;

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
import org.testng.annotations.Test;

import com.uat.util.TestUtil;

public class DeletingProject extends TestSuiteBase 
{
	String runmodes[]=null;
	boolean isLoginSuccess=true;
	String selectedGroupsArray[];
	List<WebElement> GroupsInUpperRibbon;
	ArrayList<String> ProjectNames = new ArrayList<String>();
	int flagCount = 0;
	
	
	@Test
	public void DeleteAllProjects() throws Exception
	{
		int paginationLimit = 10;
		
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
				Thread.sleep(100);
				getElement("TM_testCasesTab_Id").click();
				if(getElement("TestCaseCreateNew_TestCaseActiveX_Id").isDisplayed())
				{
					getObject("TestCaseCreateNew_TestCaseActiveX_Close").click();
				}
				Thread.sleep(100);
				String jQueryLoader = readFile(JQUERY_LOAD_PORTFOLIO_SCRIPT);
				GroupsInUpperRibbon = getElement("TestCaseUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
				int numOfAvailableGroups = GroupsInUpperRibbon.size();
				eventfiringdriver.executeScript(jQueryLoader);
				wait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				String selectedGroupNames = alert.getText();
				
				// All the projects are taken into array separated by ','.
				selectedGroupsArray = selectedGroupNames.split(",");
				int numOfGroups = selectedGroupsArray.length;
				alert.accept();
				
				assertTrue(deleteTestCase(selectedGroupsArray, numOfGroups, numOfAvailableGroups));
				
				assertTrue(deleteTestPass(selectedGroupsArray, numOfGroups, numOfAvailableGroups));					
				
				getElement("TM_projectsTab_Id").click();
				Thread.sleep(100);
				int numOfAvailableProject = Integer.parseInt(getElement("Deletion_Project_TotalRecords").getText());
				
				if(numOfAvailableProject>10)
					goToFirstPage();
				
				assertTrue(deleteProjects(ProjectNames, paginationLimit));
				
				assertTrue(deleteGroups(selectedGroupsArray, numOfGroups));
				
			}
			catch(Throwable e)
			{
				APP_LOGS.debug("Exception in selecting group name from alerts.");
				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in DeleteAllProjects func");
				e.printStackTrace();
			}
			
			closeBrowser();
		}
		else 
		{
			isLoginSuccess=false;
		}
		
	}
	
	// Delete Group and Portfolio	

	public boolean deleteGroups(String[] selectedGroupsArray, int numOfGroups) throws IOException, InterruptedException
	{
		getObject("Deletion_Project_CreateNew").click();
		Thread.sleep(100);
		List<WebElement> GroupNamesinDropDown = getElement("Deletion_GroupNamesinDropDown_Id").findElements(By.tagName("option"));
		List<WebElement> PortfolioNamesinDropDown;
		
		try
		{
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				//getElement("Deletion_GroupNamesinDropDown_Id").click();
				for(int j=1;j<=GroupNamesinDropDown.size();j++)
				{
					GroupNamesinDropDown = getElement("Deletion_GroupNamesinDropDown_Id").findElements(By.tagName("option"));
					if(selectedGroupsArray[groupCount].equals(GroupNamesinDropDown.get(j).getText()))
					{
						GroupNamesinDropDown.get(j).click();
						Thread.sleep(100);
						GroupNamesinDropDown = getElement("Deletion_GroupNamesinDropDown_Id").findElements(By.tagName("option"));
						PortfolioNamesinDropDown = getElement("Deletion_PortfolioNamesinDropDown_Id").findElements(By.tagName("option"));
						for(int k =1; k<PortfolioNamesinDropDown.size();k++)
						{
							PortfolioNamesinDropDown = getElement("Deletion_PortfolioNamesinDropDown_Id").findElements(By.tagName("option"));
							Thread.sleep(100);
							PortfolioNamesinDropDown.get(1).click();
							
							getObject("Deletion_PortfolioName_DeleteIcon").click();
							
							waitForElementVisibility("GroupPortfolioDeleteText_Id", 5);
							//getTextFromAlert(getElement("GroupPortfolioDeleteText_Id"));
							getObject("Deletion_GroupPortfolioDeleteConfirmationMessage").click();
							
							getTextFromAutoHidePopUp();
						}
						
						getObject("Deletion_GroupName_DeleteIcon").click();
						waitForElementVisibility("GroupPortfolioDeleteText_Id",5);
						//getTextFromAlert(getElement("GroupPortfolioDeleteText_Id"));
						
						getObject("Deletion_GroupPortfolioDeleteConfirmationMessage").click();
					
						getTextFromAutoHidePopUp();
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
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteGroups func");
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
						
						waitForElementVisibility("ProjectViewAll_deleteProjectConfirmationText_Id",5);

						//getTextFromAlert(getElement("ProjectViewAll_deleteProjectConfirmationText_Id"));
						getObject("Deletion_ProjectDeleteConfirmationMessage").click();
						getTextFromAutoHidePopUp();
						
						break;
					}
				}
				if((i%10==0)&&(ProjectFound == 0))
				{
					i=0;
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
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteGroups func");
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
	public boolean deleteProjects1() throws IOException, InterruptedException
	{
		
		int projectsOnPage;
		String gridProject;
		boolean searchForNextProjectItem= false;		
		
		try
		{
			
			for (int i = 0; i < ProjectNames.size(); i++) 
			{
				
				do 
				{	
					
					projectsOnPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();
					
					for (int j = 1; j <= projectsOnPage; j++) 
					{
						gridProject=getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", j).getAttribute("title");
						
						if (gridProject.equals(ProjectNames.get(i))) 
						{
							getObject("Deletion_AvailableProjectNames_Xpath1", "Deletion_AvailableProjectNames_Xpath2", j).click();
							getObject("Deletion_Project_DeleteIcon").click();
							waitForElementVisibility("ProjectViewAll_deleteProjectConfirmationText_Id",5);
							//getTextFromAlert(getElement("ProjectViewAll_deleteProjectConfirmationText_Id"));
							getObject("Deletion_ProjectDeleteConfirmationMessage").click();
							getTextFromAutoHidePopUp();						
							searchForNextProjectItem = true;
							break;
						}						
						
					}
					
					if (searchForNextProjectItem) 
					{
						searchForNextProjectItem = false;
						break;
					}
					
				}while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
			}
			
			
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in delete Project function.");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteProjects func");
			e.printStackTrace();
			return false;
		}
	}
	
	
	private boolean deleteProjects(ArrayList<String> projectsToDelete, int paginationLimit)
	{
		
		int projectsOnPage;
		String gridProject;
		boolean searchForNextProjectItem= false;
		int rowToContinueFrom =1;		
		int totalRowsSearched = 0;
		
		int totalProjectsToDelete = projectsToDelete.size();
		
		
		try
		{
			//this For block is parent loop that will run everything based on the number of projects to delete 
			for (int projectsDeleteCount = 0; projectsDeleteCount < totalProjectsToDelete; projectsDeleteCount++) 
			{	
				//goes to the page, where last project was deleted, based on rows searched for projects to delete
				goToPage(getPageNum(paginationLimit , totalRowsSearched));
				
				//this do-while is used to keep searching in next page if no project deleted in previous page
				do 
				{								
					projectsOnPage = getElement("ProjectViewAll_Table_Id").findElements(By.xpath("tr")).size();					 
					 
					//this for block is used to search on the current page, it is initialized based on rows already searched in it
					for (int gridRow = rowToContinueFrom; gridRow <= projectsOnPage; gridRow++) 
					{
						gridProject=getObject("ProjectViewAll_projectNameColumn1", "ProjectViewAll_projectNameColumn2", gridRow).getAttribute("title");
						totalRowsSearched++;
						
						//this block searches for the grid project in the whole arraylist of projects, sequence doesn't matter
						for (int projectsArrayIndex = 0; projectsArrayIndex < projectsToDelete.size(); projectsArrayIndex++) 
						{
							
							if (gridProject.equals(projectsToDelete.get(projectsArrayIndex))) 
							{
								getObject("Deletion_AvailableProjectNames_Xpath1", "Deletion_AvailableProjectNames_Xpath2", gridRow).click();
								getObject("Deletion_Project_DeleteIcon").click();
								waitForElementVisibility("ProjectViewAll_deleteProjectConfirmationText_Id", 5);
								getObject("Deletion_ProjectDeleteConfirmationMessage").click();
								getTextFromAutoHidePopUp();
								
								//this line is for efficiency purposes to reduce arraylist if project found subsequently resulting the above for loop to run less times than previous
								projectsToDelete.remove(projectsArrayIndex);
								
								//reinitializing to start from the same row next time when on that page
								rowToContinueFrom = gridRow;//getRowNum(totalRowsSearched);
								
								searchForNextProjectItem = true;
								break;
							}
							
						}
						
						if (searchForNextProjectItem) 						
							break;												
						
					}
					
					if (searchForNextProjectItem)
					{
						searchForNextProjectItem = false;
						break;
					}
											
					
					//initializing to continue from row 1 for next page in case it was changed
					rowToContinueFrom = 1;
					
				}while (ifElementIsClickableThenClick("ProjectViewAll_NextLink"));
				
			}			
			
			return true;
		} 
		catch (Throwable e) 
		{
			APP_LOGS.debug("EXception in delete Project function.");
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteProjects func");
			e.printStackTrace();
			return false;
		}
	}
	
	
	private int getRowNum(int paginationLimit, int rowsCompleted)
	{
		//int paginationLimit = 10;		
		
		if(rowsCompleted%paginationLimit==0)
			return paginationLimit;
		else
			return rowsCompleted%paginationLimit;			
		
			
	}
	
	
	private int getPageNum(int paginationLimit, int rowsCompleted)
	{
		
		//int paginationLimit = 10;			
		
		if(rowsCompleted>paginationLimit)
		{
			if(rowsCompleted%paginationLimit==0)
				return rowsCompleted/paginationLimit;
			else
				return ((rowsCompleted/paginationLimit)+1);			
		}
		else
			return 1;
		
	}
	
	
	
		
	//Delete test Pass

	public boolean deleteTestPass(String[] selectedGroupsArray, int numOfGroups, int numOfAvailableGroups) throws IOException, InterruptedException
	{
		try
		{
			getElement("TM_testPassesTab_Id").click();
			Thread.sleep(100);
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				getElement("TestPassUpperRibbon_groupDropDown_Id").click();
				Thread.sleep(100);
				GroupsInUpperRibbon = getElement("TestPassUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
				for(int j=0;j<numOfAvailableGroups;j++)
				{
					if(selectedGroupsArray[groupCount].equals(GroupsInUpperRibbon.get(j).getAttribute("title")))
					{						
						GroupsInUpperRibbon.get(j).click();
						GroupsInUpperRibbon = getElement("TestPassUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
						Thread.sleep(100);
						break;
					}
				}
				
				List<WebElement> PortfoliosInUpperRibbon = getElement("TestPassUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
				int numOfPortfolios = PortfoliosInUpperRibbon.size();
				for(int PortfolioCount = 0; PortfolioCount<numOfPortfolios; PortfolioCount++)
				{
					getElement("TestPassUpperRibbon_PortfolioDropDown_Id").click();
					Thread.sleep(100);
					PortfoliosInUpperRibbon = getElement("TestPassUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
					PortfoliosInUpperRibbon.get(PortfolioCount).click();
					Thread.sleep(100);
					List<WebElement> ProjectsInUpperRibbon = getElement("TestPassUpperRibbon_ProjectList_Id").findElements(By.tagName("li"));
					int numOfProjects = ProjectsInUpperRibbon.size();
					for(int ProjectCount = 0; ProjectCount<numOfProjects; ProjectCount++)
					{
						getElement("TestPassUpperRibbon_projectDropDown_Id").click();
						Thread.sleep(100);
						ProjectNames.add(ProjectsInUpperRibbon.get(ProjectCount).getAttribute("title"));
						ProjectsInUpperRibbon.get(ProjectCount).click();
						Thread.sleep(100);
						List<WebElement> VersionsInUpperRibbon = getElement("TestPassUpperRibbon_VersionList_Id").findElements(By.tagName("li"));
						int numOfVersions = VersionsInUpperRibbon.size();
						for(int versionCount = 0; versionCount< numOfVersions;versionCount++)
						{
							getElement("TestPassUpperRibbon_versionDropDown_Id").click();
							Thread.sleep(100);
							VersionsInUpperRibbon.get(versionCount).click();
							Thread.sleep(100);
							if(!getElement("Deletion_NoTestPassAvailable_Id").isDisplayed())
							{
								while(!getElement("Deletion_NoTestPassAvailable_Id").isDisplayed())
								{
									getObject("Deletion_TestPassDeletion_XPath1", "Deletion_TestPassDeletion_XPath2", 1).click();
									waitForElementVisibility("ProjectViewAll_deleteProjectConfirmationText_Id", 5);
									//getTextFromAlert(getElement("ProjectViewAll_deleteProjectConfirmationText_Id"));
									getObject("Deletion_TestPassDeleteConfirmationMessage").click();
									getTextFromAutoHidePopUp();
									
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
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteTestPass func");
			e.printStackTrace();
			return false;
		}
	}
	
	//Deleting Test Case
	
	public boolean deleteTestCase(String[] selectedGroupsArray, int numOfGroups, int numOfAvailableGroups) throws IOException, InterruptedException
	{
		
		try
		{
			for(int groupCount =0;groupCount<numOfGroups;groupCount++)
			{
				getElement("TestCaseUpperRibbon_groupDropDown_Id").click();
				Thread.sleep(100);
				for(int j=0;j<numOfAvailableGroups;j++)
				{
					GroupsInUpperRibbon = getElement("TestCaseUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
					if(selectedGroupsArray[groupCount].equals(GroupsInUpperRibbon.get(j).getAttribute("title")))
					{
						
						GroupsInUpperRibbon.get(j).click();	
						GroupsInUpperRibbon = getElement("TestCaseUpperRibbon_GroupList_Id").findElements(By.tagName("li"));
						Thread.sleep(100);
						break;
					}
				}
				List<WebElement> PortfoliosInUpperRibbon = getElement("TestCaseUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
				int numOfPortfolios = PortfoliosInUpperRibbon.size();
				for(int PortfolioCount = 0; PortfolioCount<numOfPortfolios; PortfolioCount++)
				{
					getElement("TestCaseUpperRibbon_PortfolioDropDown_Id").click();
					Thread.sleep(100);
					PortfoliosInUpperRibbon = getElement("TestCaseUpperRibbon_PortfolioList_Id").findElements(By.tagName("li"));
					PortfoliosInUpperRibbon.get(PortfolioCount).click();
					Thread.sleep(100);
					List<WebElement> ProjectsInUpperRibbon = getElement("TestCaseUpperRibbon_ProjectList_Id").findElements(By.tagName("li"));
					int numOfProjects = ProjectsInUpperRibbon.size();
					for(int ProjectCount = 0; ProjectCount<numOfProjects; ProjectCount++)
					{
						getElement("TestCaseUpperRibbon_projectDropDown_Id").click();
						Thread.sleep(100);
						ProjectsInUpperRibbon.get(ProjectCount).click();
						Thread.sleep(100);
						List<WebElement> VersionsInUpperRibbon = getElement("TestCaseUpperRibbon_VersionList_Id").findElements(By.tagName("li"));
						int numOfVersions = VersionsInUpperRibbon.size();
						for(int versionCount = 0; versionCount< numOfVersions;versionCount++)
						{
							getElement("TestCaseUpperRibbon_versionDropDown_Id").click();
							Thread.sleep(100);
							VersionsInUpperRibbon.get(versionCount).click();
							Thread.sleep(100);
							List<WebElement> numOfTestPassesInUpperRibbon = getElement("TestCaseUpperRibbon_TestPassList_Id").findElements(By.tagName("li"));
							int numOfTestPasses = numOfTestPassesInUpperRibbon.size();
							for(int testPassCount =0;testPassCount<numOfTestPasses;testPassCount++)
							{
								getElement("TestCaseUpperRibbon_testPassDropDown_Id").click();
								Thread.sleep(100);
								numOfTestPassesInUpperRibbon.get(testPassCount).click();
								Thread.sleep(100);
								
								if(!getElement("Deletion_NoTestCasePresentDiv_Id").isDisplayed())
								{
									getElement("Deletion_TestCasesBulkDeleteBtn_Id").click();
									waitForElementVisibility("TestCases_ViewAll_deleteTestCasesConfirmationText_Id", 5);

									//getTextFromAlert(getElement("TestCases_ViewAll_deleteTestCasesConfirmationText_Id"));
									getObject("Deletion_TestCasesBulkDeleteConfirmationMessage").click();
									getTextFromAutoHidePopUp();
									
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
			TestUtil.takeScreenShot(this.getClass().getSimpleName(), "Exception in deleteTestCase func");

			e.printStackTrace();
			return false;
		}
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
	
	private void goToFirstPage()
	{
		boolean isPreviousLinkClickable = true;
		setImplicitWait(0);
		
		while(isPreviousLinkClickable)
		{
			try
		   {				   
			     eventfiringdriver.findElement(By.xpath(OR.getProperty("PreviousLink"))).click();		    
		   }
		   catch(Throwable t)
		   {	    
			   isPreviousLinkClickable = false;		    
		   }
			  
		}
		
		resetImplicitWait();
		
	}
	
	//assumes user is on page 1
	private void goToPage(int pageNum)
	{
		if (pageNum>1) 
		{
			setImplicitWait(1);
			
			for (int i = 1; i < pageNum; i++) 
			{
				try
				{				   
				    eventfiringdriver.findElement(By.xpath(OR.getProperty("NextLink"))).click();		    
				}
				catch(Throwable t)
				{	    				   
				   
				}
			}
			
			resetImplicitWait();
		}		
		
	}
	
	
	private boolean ifElementIsClickableThenClick(String key)
	 {
	  
		   try
		   {
			   setImplicitWait(0);
			     eventfiringdriver.findElement(By.xpath(OR.getProperty(key))).isDisplayed();
			     getObject(key).click();
			     return true;
		    
		   }
		   catch(Throwable t)
		   {
		    
			   return false;
		    
		   }
		   finally
		   {
			  resetImplicitWait();
		            
		   }
	   
	  }
	
	
	
}
