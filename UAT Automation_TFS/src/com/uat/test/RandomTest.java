package com.uat.test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.uat.base.TestBase;

public class RandomTest extends TestBase {

	
	@Test
	public void test1() throws Exception
	{
		initialize();
		openBrowser();
		login("Admin");
		getElement("UAT_testManagement_Id").click();
		Thread.sleep(1000);
		getObject("Projects_createNewProjectLink").click();
		Thread.sleep(1000);
		getObject("ProjectCreateNew_StakeholderPeoplePickerImg").click();
		driver.switchTo().frame(1);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='ctl00_PlaceHolderDialogBodySection_ctl07_queryTextBox']")));
		
		getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys("uat.tester255");
		
		getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		
		Thread.sleep(1000);
		
		
		
		getObject("ProjectCreateNew_StakeholderAddBtn").click();
		
		getObject("ProjectCreateNew_versionLeadStakeholderTextField").clear();
		
		getObject("ProjectCreateNew_versionLeadStakeholderTextField").sendKeys("uat.tester256");
		
		getObject("ProjectCreateNew_versionLeadStakeholderSearchBtn").click();
		
		Thread.sleep(1000);
		
		
		
		getObject("ProjectCreateNew_StakeholderAddBtn").click();
		
		getObject("ProjectCreateNew_versionLeadStakeholderOkBtn").click();
		
		driver.switchTo().defaultContent();
		
		Thread.sleep(1000);
		
		eventfiringdriver.findElement(By.xpath("//div[@id='ctl00_ctl00_midPannel_ContentPlaceHolder1_Child_ContentPlaceHolder1_PeopleEditorUser4Port_upLevelDiv']")).sendKeys(Keys.DELETE);
		
		eventfiringdriver.findElement(By.xpath("//div[@id='ctl00_ctl00_midPannel_ContentPlaceHolder1_Child_ContentPlaceHolder1_PeopleEditorUser4Port_upLevelDiv']")).sendKeys(Keys.DELETE);
		//getObject("ProjectCreateNew_stakeholderDisplayField1", "ProjectCreateNew_stakeholderDisplayField2", 1).clear();
		eventfiringdriver.findElement(By.xpath("//div[@id='ctl00_ctl00_midPannel_ContentPlaceHolder1_Child_ContentPlaceHolder1_PeopleEditorUser4Port_upLevelDiv']")).sendKeys(Keys.DELETE);
		eventfiringdriver.findElement(By.xpath("//div[@id='ctl00_ctl00_midPannel_ContentPlaceHolder1_Child_ContentPlaceHolder1_PeopleEditorUser4Port_upLevelDiv']")).sendKeys(Keys.DELETE);
		
		
	}
}
