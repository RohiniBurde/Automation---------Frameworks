package com.uat.suite.tm_project;

import java.util.ArrayList;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

public class GetUsersFunctionality extends TestSuiteBase {
	
	ArrayList<Credentials> stakeholder;
	ArrayList<Credentials> testManager;
	static int numOfTestManagers=2;
	static int flag=0;
	
	
	@Test(dataProvider="getData")
	public void testingGetUsers(String role, String totalStakeholders) throws Exception
	{
		
		
		int stakeholders = Integer.parseInt(totalStakeholders);
		
		//int TestManagers = Integer.parseInt(totalTestManagers);
		
		System.out.println(role);
		
		System.out.println(stakeholders);
		
		//System.out.println(TestManagers);
		
		
		testManager = new ArrayList<Credentials>();
		
		testManager = getUsers("Test Manager", numOfTestManagers);
		
		
		
		
		
		stakeholder = new ArrayList<Credentials>();
		
		stakeholder = getUsers("Stakeholder", stakeholders);
		
		System.out.println("\n \n \n \nStakeholder");
		
		
		/*if (stakeholder!=null) 
		{
			
			System.out.println(stakeholder.size());
			System.out.println("*********************************************");
			
			for (int i = 0; i < stakeholder.size(); i++) {
				
				System.out.println(stakeholder.get(i).username+"          "+stakeholder.get(i).password);
				
				
			}
		}
		else {
			System.out.println("stakeholder is "+stakeholder);
		}*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*System.out.println("\n \n \n \nTest Manager");
		
		if (testManager!=null) 
		{
			
			System.out.println(testManager.size());
			System.out.println("*********************************************");
			
			for (int i = 0; i < testManager.size(); i++) 
			{
				
				System.out.println(testManager.get(i).username+"          "+testManager.get(i).password);
								
			}
		}
		else 
		{
			System.out.println("testManager is "+testManager);
		}*/
		if (flag==0) 
		{
			
			openBrowser();
			
			System.out.println(login(testManager.get(0).username, testManager.get(0).password, "Test Manager"));
			
			closeBrowser();
			
			flag++;
		}
		else 
		{
			
			openBrowser();
			
			System.out.println(login(testManager.get(1).username, testManager.get(1).password, "Test Manager"));
			
			closeBrowser();
		}
			
			
			
			
		
		
		
		
	}
	
	
	
	
	@DataProvider
	public Object[][] getData(){
		return TestUtil.getData(TM_projectSuiteXls, this.getClass().getSimpleName());
	}

}
